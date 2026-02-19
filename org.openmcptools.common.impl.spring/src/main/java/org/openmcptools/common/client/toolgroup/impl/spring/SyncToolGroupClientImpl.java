package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.List;
import java.util.Map;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.toolgroup.client.SyncToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.spec.McpSchema.Implementation;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

@Component(factory = "SyncToolGroupClientFactory", service = SyncToolGroupClient.class)
public class SyncToolGroupClientImpl extends AbstractToolGroupClientImpl<McpSyncToolGroupClient>
		implements SyncToolGroupClient<McpSyncToolGroupClient> {

	public SyncToolGroupClientImpl() {
		super();
	}

	@Reference
	void setToolConverter(ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> toolConverter) {
		this.toolConverter = toolConverter;
	}

	@Override
	protected void closeClient() {
		if (this.client != null) {
			this.client.closeGracefully();
			this.client = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		List<ToolGroupClientListener> clientListeners = (List<ToolGroupClientListener>) properties
				.get(CLIENT_LISTENERS);
		if (clientListeners != null) {
			this.clientListeners.addAll(clientListeners);
		}
		this.client = new SyncToolGroupClientConfig(properties).buildMcpSyncToolGroupClient(this.clientListeners,
				new SpringLocalToolGroupClient());
	}

	@Deactivate
	protected void deactivate() {
		if (this.client != null) {
			this.client.close();
			this.client = null;
		}
	}

	@Override
	public InitializeResult initialize() {
		io.modelcontextprotocol.spec.McpSchema.InitializeResult ir = this.client.initialize();
		Implementation i = ir.serverInfo();
		InitializeResult result = new InitializeResult(ir.protocolVersion(), i.name(), i.version(), ir.instructions(),
				ir.meta(), new SpringServerCapabilities(ir.capabilities()));
		ListToolsResult r = this.client.listTools();
		addToolsLocal(toolConverter.convertToTools(r.tools()));
		return result;
	}

}
