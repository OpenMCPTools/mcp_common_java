package org.openmcptools.common.server.toolgroup.impl.spring;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.InputSchemaGenerator;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.server.toolgroup.AsyncToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolGroupProvider;
import org.openmcptools.common.server.toolgroup.ToolGroupServerConfig;
import org.openmcptools.common.server.toolgroup.ToolProviderImpl;
import org.openmcptools.common.server.toolgroup.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import reactor.core.publisher.Mono;

@Component(factory = "SpringAsyncToolGroupServer", service = AsyncToolGroupServer.class)
public class AsyncToolGroupServerImpl extends
		AbstractToolGroupServerImpl<McpAsyncToolGroupServer, AsyncToolSpecification, McpAsyncServerExchange, Mono<CallToolResult>>
		implements AsyncToolGroupServer<McpAsyncToolGroupServer> {

	public AsyncToolGroupServerImpl() {
		super();
	}

	@Reference
	void setToolConverter(ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> toolConverter) {
		this.toolConverter = toolConverter;
	}

	@Override
	protected void closeServer() {
		if (this.server != null) {
			this.server.closeGracefully();
			this.server = null;
		}
	}

	@Override
	protected void addTools(List<AsyncToolSpecification> toolSpecs) {
		super.addTools(toolSpecs);
		this.server.endToolsUpdate();
	}

	@Override
	protected void addTool(McpAsyncToolGroupServer server, AsyncToolSpecification toolSpec) {
		server.addTool(toolSpec).block();
	}

	@Override
	protected void removeTool(McpAsyncToolGroupServer server, String toolName) {
		server.removeTool(toolName).block();
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		Object po = properties.get(SERVER_TOOL_GROUP_PROVIDER);
		if (po instanceof ToolGroupProvider) {
			this.toolGroupProvider = (ToolGroupProvider<AsyncToolSpecification, McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>>) po;
		} else {
			po = properties.get(SERVER_GENERATE_OUTPUT_SCHEMA);
			InputSchemaGenerator isg = new InputSchemaGeneratorImpl();
			OutputSchemaGenerator osg = null;
			if (po instanceof Boolean && ((Boolean) po).booleanValue()) {
				osg = new OutputSchemaGeneratorImpl.Async();
			}
			ToolProviderImpl toolProvider = new ToolProviderImpl(isg, osg);
			this.toolGroupProvider = new AsyncToolGroupProviderImpl(toolProvider, toolConverter, osg != null);
		}
		SpringAsyncToolGroupServerConfig tgServerConfig = (SpringAsyncToolGroupServerConfig) properties
				.get(ToolGroupServerConfig.TOOL_GROUP_SERVER_CONFIG);
		if (tgServerConfig == null) {
			tgServerConfig = new SpringAsyncToolGroupServerConfig(properties);
		}
		this.server = tgServerConfig.buildMcpAsyncToolGroupServer();
	}

	@Deactivate
	protected void deactivate() {
		if (this.server != null) {
			this.server.close();
			this.server = null;
		}
	}

	@Override
	protected ToolSpecification<AsyncToolSpecification> getToolSpecification(Tool tool,
			BiFunction<McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>> callHandler) {
		AsyncToolSpecification.Builder specBuilder = AsyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(callHandler);
		return new ToolSpecification<AsyncToolSpecification>(tool, specBuilder.build());
	}

	@Override
	protected void startToolsUpdate() {
		this.server.startToolsUpdate();
	}

	@Override
	protected void endToolsUpdate() {
		this.server.endToolsUpdate();
	}

}
