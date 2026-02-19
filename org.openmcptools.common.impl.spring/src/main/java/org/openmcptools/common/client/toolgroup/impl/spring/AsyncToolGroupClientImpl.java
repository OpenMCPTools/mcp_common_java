package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.Map;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.toolgroup.client.AsyncToolGroupClient;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.spec.McpSchema.Implementation;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

@Component(factory = "AsyncToolGroupClientFactory", service = AsyncToolGroupClient.class)
public class AsyncToolGroupClientImpl extends
		AbstractToolGroupClientImpl<org.openmcptools.common.client.toolgroup.impl.spring.McpAsyncToolGroupClient>
		implements AsyncToolGroupClient<McpAsyncToolGroupClient> {

	public AsyncToolGroupClientImpl() {
		super();
	}

	@Reference
	void setToolConverter(ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> toolConverter) {
		this.toolConverter = toolConverter;
	}

	@Override
	protected void closeClient() {
		if (this.client != null) {
			this.client.closeGracefully().block();
			this.client = null;
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		this.client = new AsyncToolGroupClientConfig(properties).buildMcpAsyncToolGroupClient(clientListeners,
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
		io.modelcontextprotocol.spec.McpSchema.InitializeResult ir = this.client.initialize().block();
		Implementation i = ir.serverInfo();
		InitializeResult result = new InitializeResult(ir.protocolVersion(), i.name(), i.version(), ir.instructions(),
				ir.meta(), new SpringServerCapabilities(ir.capabilities()));
		ListToolsResult r = this.client.listTools().block();
		addToolsLocal(toolConverter.convertToTools(r.tools()));
		return result;
	}

}
