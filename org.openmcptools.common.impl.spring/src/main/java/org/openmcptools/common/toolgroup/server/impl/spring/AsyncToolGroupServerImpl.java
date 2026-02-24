package org.openmcptools.common.toolgroup.server.impl.spring;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.openmcptools.common.impl.spring.ToolConverter;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.toolgroup.server.AsyncToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolGroupProvider;
import org.openmcptools.common.toolgroup.server.ToolProviderImpl;
import org.openmcptools.common.toolgroup.server.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import reactor.core.publisher.Mono;

@Component(factory = AsyncToolGroupServerConfig.SERVER_FACTORY_NAME, service = AsyncToolGroupServer.class)
public class AsyncToolGroupServerImpl extends
		AbstractToolGroupServerImpl<SDKAsyncToolGroupServer, AsyncToolSpecification, McpAsyncServerExchange, Mono<CallToolResult>>
		implements AsyncToolGroupServer<SDKAsyncToolGroupServer> {

	public AsyncToolGroupServerImpl() {
		super();
	}

	@Reference
	protected void setToolConverter(ToolConverter toolConverter) {
		super.setToolConverter(toolConverter);
	}

	@Override
	protected void closeServer() {
		if (this.server != null) {
			this.server.closeGracefully();
			this.server = null;
		}
	}

	@Override
	protected void addTool(SDKAsyncToolGroupServer server, AsyncToolSpecification toolSpec) {
		server.addTool(toolSpec).block();
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		this.toolGroupProvider = (ToolGroupProvider<AsyncToolSpecification, McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>>) properties
				.get(SERVER_TOOL_GROUP_PROVIDER);
		if (toolGroupProvider == null) {
			Boolean po = (Boolean) properties.get(SERVER_GENERATE_OUTPUT_SCHEMA);
			OutputSchemaGenerator osg = (po != null && po.booleanValue()) ? new OutputSchemaGeneratorImpl.Async()
					: null;
			this.toolGroupProvider = new AsyncToolGroupProviderImpl(
					new ToolProviderImpl(new InputSchemaGeneratorImpl(), osg), toolConverter, osg != null);
		}
		this.server = new AsyncToolGroupServerConfig(properties).buildMcpAsyncToolGroupServer();
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
	protected io.modelcontextprotocol.spec.McpSchema.Tool findTool(SDKAsyncToolGroupServer server, String toolName) {
		Optional<io.modelcontextprotocol.spec.McpSchema.Tool> optTool = server.listTools().toStream()
				.filter(t -> toolName.equals(t.name())).findFirst();
		return optTool.isPresent() ? optTool.get() : null;
	}

}
