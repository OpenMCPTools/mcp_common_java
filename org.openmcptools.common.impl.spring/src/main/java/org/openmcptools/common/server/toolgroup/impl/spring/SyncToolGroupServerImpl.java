package org.openmcptools.common.server.toolgroup.impl.spring;

import java.util.Map;
import java.util.function.BiFunction;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.server.toolgroup.SyncToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolGroupProvider;
import org.openmcptools.common.server.toolgroup.ToolProviderImpl;
import org.openmcptools.common.server.toolgroup.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

@Component(factory = SyncToolGroupServerConfig.SERVER_FACTORY_NAME, service = SyncToolGroupServer.class)
public class SyncToolGroupServerImpl extends
		AbstractToolGroupServerImpl<McpSyncToolGroupServer, SyncToolSpecification, McpSyncServerExchange, CallToolResult>
		implements SyncToolGroupServer<McpSyncToolGroupServer> {

	public SyncToolGroupServerImpl() {
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
	protected void addTool(McpSyncToolGroupServer server, SyncToolSpecification toolSpec) {
		server.addTool(toolSpec);
	}

	@Override
	protected void removeTool(McpSyncToolGroupServer server, String toolName) {
		server.removeTool(toolName);
	}

	@Override
	protected ToolSpecification<SyncToolSpecification> getToolSpecification(Tool tool,
			BiFunction<McpSyncServerExchange, CallToolRequest, CallToolResult> callHandler) {
		SyncToolSpecification.Builder specBuilder = SyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(callHandler);
		return new ToolSpecification<SyncToolSpecification>(tool, specBuilder.build());
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
	protected void startToolsUpdate() {
		this.server.startToolsUpdate();
	}

	@Override
	protected void endToolsUpdate() {
		this.server.endToolsUpdate();
	}

}
