package org.openmcptools.common.toolgroup.server.impl.spring;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.openmcptools.common.impl.spring.ToolConverter;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.toolgroup.server.SyncToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolGroupProvider;
import org.openmcptools.common.toolgroup.server.ToolProviderImpl;
import org.openmcptools.common.toolgroup.server.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

@Component(factory = SyncToolGroupServerConfig.SERVER_FACTORY_NAME, service = SyncToolGroupServer.class)
public class SyncToolGroupServerImpl
		extends AbstractToolGroupServerImpl<SyncToolSpecification, McpSyncServerExchange, CallToolResult>
		implements SyncToolGroupServer<SyncToolSpecification> {

	private SDKSyncToolGroupServer server;

	public SyncToolGroupServerImpl() {
		super();
	}

	@Reference
	protected void setToolConverter(ToolConverter toolConverter) {
		super.setToolConverter(toolConverter);
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		this.toolGroupProvider = (ToolGroupProvider<SyncToolSpecification, McpSyncServerExchange, CallToolRequest, CallToolResult>) properties
				.get(SERVER_TOOL_GROUP_PROVIDER);
		if (toolGroupProvider == null) {
			Boolean po = (Boolean) properties.get(SERVER_GENERATE_OUTPUT_SCHEMA);
			OutputSchemaGenerator osg = (po != null && po.booleanValue()) ? new OutputSchemaGeneratorImpl.Async()
					: null;
			this.toolGroupProvider = new SyncToolGroupProviderImpl(
					new ToolProviderImpl(new InputSchemaGeneratorImpl(), osg), toolConverter, osg != null);
		}
		this.server = new SyncToolGroupServerConfig(properties).buildMcpSyncToolGroupServer();
	}

	@Deactivate
	protected void deactivate() {
		if (this.server != null) {
			this.server.close();
		}
	}

	@Override
	public Tool removeTool(String toolName) {
		McpSchema.Tool t = findTool(toolName);
		if (t != null) {
			this.server.removeTool(toolName);
			return this.toolConverter.convertTo(t);
		}
		return null;
	}

	@Override
	public Tool addToolSpecification(ToolSpecification<SyncToolSpecification> toolSpec) {
		SyncToolSpecification ts = toolSpec.getSpecification();
		Tool tool = toolSpec.getTool();
		SyncToolSpecification.Builder specBuilder = SyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(ts.callHandler());
		this.server.addTool(specBuilder.build());
		return tool;
	}

	@Override
	public void close() throws IOException {
		this.server.closeGracefully();
	}

	@Override
	protected McpSchema.Tool findTool(String toolName) {
		Optional<McpSchema.Tool> opt = server.listTools().stream().filter(t -> t.name().equals(toolName)).findAny();
		return opt.isPresent() ? opt.get() : null;
	}

}
