package org.openmcptools.common.server.toolgroup.impl.spring;

import java.util.Map;
import java.util.function.BiFunction;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.InputSchemaGenerator;
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

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpServerTransportProvider;

@Component(factory = "SpringSyncToolGroupServer", service = SyncToolGroupServer.class)
public class SyncToolGroupServerImpl
		extends AbstractToolGroupServerImpl<McpSyncServer, SyncToolSpecification, McpSyncServerExchange, CallToolResult>
		implements SyncToolGroupServer {

	public SyncToolGroupServerImpl() {
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
	protected void addTool(McpSyncServer server, SyncToolSpecification toolSpec) {
		server.addTool(toolSpec);
	}

	@Override
	protected void removeTool(McpSyncServer server, String toolName) {
		server.removeTool(toolName);
	}

	@Override
	public void removeTool(Tool tool) {
		this.server.removeTool(tool.getName());
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
		Object po = properties.get(SERVER_TOOL_GROUP_PROVIDER);
		if (po instanceof ToolGroupProvider) {
			this.toolGroupProvider = (ToolGroupProvider<SyncToolSpecification, McpSyncServerExchange, CallToolRequest, CallToolResult>) po;
		} else {
			po = properties.get(SERVER_GENERATE_OUTPUT_SCHEMA);
			InputSchemaGenerator isg = new InputSchemaGeneratorImpl();
			OutputSchemaGenerator osg = null;
			if (po instanceof Boolean && ((Boolean) po).booleanValue()) {
				osg = new OutputSchemaGeneratorImpl.Sync();
			}
			ToolProviderImpl toolProvider = new ToolProviderImpl(isg, osg);
			this.toolGroupProvider = new SyncToolGroupProviderImpl(toolProvider, toolConverter, osg != null);
		}
		this.server = buildServerFromProperties(properties);
	}

	@Deactivate
	protected void deactivate() {
		if (this.server != null) {
			this.server.close();
		}
	}

	@Override
	protected McpSyncServer buildServer(String serverName, String serverVersion, ServerCapabilities serverCapabilities,
			McpServerTransportProvider transport) {
		return McpServer.sync(transport).serverInfo(serverName, serverVersion).capabilities(serverCapabilities).build();
	}

}
