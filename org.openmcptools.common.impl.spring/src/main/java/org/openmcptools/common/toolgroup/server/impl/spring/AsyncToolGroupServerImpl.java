package org.openmcptools.common.toolgroup.server.impl.spring;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.openmcptools.common.impl.spring.ToolConverter;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.toolgroup.server.AsyncToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolGroupProvider;
import org.openmcptools.common.toolgroup.server.ToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolProviderImpl;
import org.openmcptools.common.toolgroup.server.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import reactor.core.publisher.Mono;

@Component(factory = AsyncToolGroupServerConfig.SERVER_FACTORY_NAME, service = AsyncToolGroupServer.class)
public class AsyncToolGroupServerImpl
		extends AbstractToolGroupServerImpl<AsyncToolSpecification, McpAsyncServerExchange, Mono<CallToolResult>>
		implements AsyncToolGroupServer<AsyncToolSpecification> {

	private SDKAsyncToolGroupServer server;

	public AsyncToolGroupServerImpl() {
		super();
	}

	@Reference
	protected void setToolConverter(ToolConverter toolConverter) {
		super.setToolConverter(toolConverter);
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		this.toolGroupProvider = (ToolGroupProvider<AsyncToolSpecification, McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>>) properties
				.get(ToolGroupServer.SERVER_TOOL_GROUP_PROVIDER);
		if (toolGroupProvider == null) {
			Boolean po = (Boolean) properties.get(ToolGroupServer.SERVER_GENERATE_OUTPUT_SCHEMA);
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

	public Tool removeTool(String toolName) {
		McpSchema.Tool t = findTool(toolName);
		if (t != null) {
			this.server.removeTool(toolName).block();
			return this.toolConverter.convertTo(t);
		}
		return null;
	}

	public Tool addToolSpecification(ToolSpecification<AsyncToolSpecification> toolSpec) {
		AsyncToolSpecification ts = toolSpec.getSpecification();
		Tool tool = toolSpec.getTool();
		AsyncToolSpecification.Builder specBuilder = AsyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(ts.callHandler());
		this.server.addTool(specBuilder.build()).block();
		return tool;
	}

	@Override
	protected McpSchema.Tool findTool(String toolName) {
		Optional<McpSchema.Tool> opt = server.listTools().collectList().block().stream()
				.filter(t -> t.name().equals(toolName)).findAny();
		return opt.isPresent() ? opt.get() : null;
	}

	@Override
	public void close() throws IOException {
		this.server.closeGracefully().block();
	}

}
