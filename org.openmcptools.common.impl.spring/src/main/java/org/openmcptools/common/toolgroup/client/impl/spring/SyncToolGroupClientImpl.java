package org.openmcptools.common.toolgroup.client.impl.spring;

import java.util.List;
import java.util.Map;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.impl.spring.ToolConverter;
import org.openmcptools.common.toolgroup.client.SyncToolGroupClient;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.spec.McpSchema.Implementation;

@Component(factory = "SyncToolGroupClientFactory", service = SyncToolGroupClient.class)
public class SyncToolGroupClientImpl extends AbstractToolGroupClientImpl<SDKSyncToolGroupClient>
		implements SyncToolGroupClient<SDKSyncToolGroupClient> {

	public SyncToolGroupClientImpl() {
		super();
	}

	@Reference
	void setToolConverter(ToolConverter toolConverter) {
		this.toolConverter = toolConverter;
	}

	@Override
	protected void closeClient() {
		if (this.client != null) {
			this.client.closeGracefully();
			this.client = null;
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		this.localClient = new SpringLocalToolGroupClient();
		this.client = new SyncToolGroupClientConfig(properties).buildMcpSyncToolGroupClient(this.localClient);
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
		List<org.openmcptools.common.model.Tool> aTools = toolConverter.convertTo(this.client.listTools().tools());
		addToolsLocal(aTools);
		this.localClient.fireToolGroupClientAddEvent(aTools);
		return result;
	}

}
