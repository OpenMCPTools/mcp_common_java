package org.openmcptools.common.client.toolgroup.impl.spring;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.toolgroup.client.AsyncToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import io.modelcontextprotocol.client.McpClientFeatures;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ClientCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Implementation;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

@Component(factory = "SpringAsyncToolGroupClient", service = AsyncToolGroupClient.class)
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

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		String clientName = (String) properties.get(CLIENT_NAME);
		if (clientName == null) {
			clientName = ToolGroupClient.CLIENT_DEFAULT_NAME;
		}
		String clientVersion = (String) properties.get(CLIENT_VERSION);
		if (clientVersion == null) {
			clientVersion = CLIENT_DEFAULT_VERSION;
		}
		McpSchema.Implementation clientImpl = new McpSchema.Implementation(clientName, clientVersion);

		ClientCapabilities clientCapabilities = (ClientCapabilities) properties.get(CLIENT_CAPABILITIES);
		if (clientCapabilities == null) {
			clientCapabilities = ClientCapabilities.builder().build();
		}

		McpClientTransport transport = (McpClientTransport) properties.get(CLIENT_TRANSPORT);
		Objects.requireNonNull(transport, CLIENT_TRANSPORT + " property must not be null");

		List<ToolGroupClientListener> clientListeners = (List<ToolGroupClientListener>) properties
				.get(CLIENT_LISTENERS);
		if (clientListeners != null) {
			this.clientListeners.addAll(clientListeners);
		}

		JsonSchemaValidator jsonSchemaValidator = (JsonSchemaValidator) properties.get(CLIENT_JSONSCHEMAVALIDATOR);

		// Create client with transport
		McpClientFeatures.Async asyncFeatures = new McpClientFeatures.Async(clientImpl, clientCapabilities, null, null,
				null, null, null, null, null, null, null, false);

		this.client = new McpAsyncToolGroupClient(transport, Duration.ofSeconds(10), Duration.ofSeconds(30),
				jsonSchemaValidator, asyncFeatures, clientListeners, new SpringLocalToolGroupClient());
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
		InitializeResult result = new InitializeResult(ir.protocolVersion(), i.name(), i.version(), ir.instructions(), ir.meta(),
				new SpringServerCapabilities(ir.capabilities()));
		ListToolsResult r = this.client.listTools().block();
		addToolsLocal(toolConverter.convertToTools(r.tools()));
		return result;
	}

}
