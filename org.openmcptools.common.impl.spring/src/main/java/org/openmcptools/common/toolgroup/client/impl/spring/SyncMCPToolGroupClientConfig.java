package org.openmcptools.common.toolgroup.client.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.openmcptools.transport.client.MCPClientTransport;

import io.modelcontextprotocol.client.McpClientFeatures;
import io.modelcontextprotocol.common.McpTransportContext;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ElicitRequest;
import io.modelcontextprotocol.spec.McpSchema.ElicitResult;
import io.modelcontextprotocol.spec.McpSchema.Implementation;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCMessage;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.ProgressNotification;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.Resource;
import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import reactor.core.publisher.Mono;

public class SyncMCPToolGroupClientConfig extends AbstractMCPToolGroupClientConfig {

	public static final String CLIENT_FACTORY_NAME = "SyncToolGroupClientFactory";
	public static final String CLIENT_CF_TARGET = "(component.factory=" + CLIENT_FACTORY_NAME + ")";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SyncMCPToolGroupClientConfig(
			MCPClientTransport transport) {
		super(transport, DEFAULT_CLIENT_REQUEST_TIMEOUT);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SyncMCPToolGroupClientConfig(
			MCPClientTransport transport,
			Long requestTimeout) {
		super(transport, requestTimeout);
	}

	public SyncMCPToolGroupClientConfig(String clientName, String clientVersion,
			MCPClientTransport<Mono<Void>, Mono<JSONRPCMessage>, Mono<JSONRPCMessage>, JSONRPCMessage> transport,
			Long requestTimeout) {
		super(clientName, clientVersion, transport, requestTimeout);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SyncMCPToolGroupClientConfig(String clientName, String clientTitle, String clientVersion,
			MCPClientTransport transport,
			Long requestTimeout) {
		super(clientName, clientTitle, clientVersion, transport, requestTimeout);
	}

	public SyncMCPToolGroupClientConfig setToolsChangeConsumers(
			List<Consumer<List<McpSchema.Tool>>> toolsChangeConsumers) {
		this.toolsChangeConsumers = toolsChangeConsumers;
		return this;
	}

	public SyncMCPToolGroupClientConfig setResourcesChangeConsumers(
			List<Consumer<List<McpSchema.Resource>>> resourcesChangeConsumers) {
		this.resourcesChangeConsumers = resourcesChangeConsumers;
		return this;
	}

	public SyncMCPToolGroupClientConfig setResourcesUpdateConsumers(
			List<Consumer<List<McpSchema.ResourceContents>>> resourcesUpdateConsumers) {
		this.resourcesUpdateConsumers = resourcesUpdateConsumers;
		return this;
	}

	public SyncMCPToolGroupClientConfig setPromptsChangeConsumers(
			List<Consumer<List<McpSchema.Prompt>>> promptsChangeConsumers) {
		this.promptsChangeConsumers = promptsChangeConsumers;
		return this;
	}

	public SyncMCPToolGroupClientConfig setLoggingConsumers(
			List<Consumer<McpSchema.LoggingMessageNotification>> loggingConsumers) {
		this.loggingConsumers = loggingConsumers;
		return this;
	}

	public SyncMCPToolGroupClientConfig setProgressConsumers(
			List<Consumer<McpSchema.ProgressNotification>> progressConsumers) {
		this.progressConsumers = progressConsumers;
		return this;
	}

	public SyncMCPToolGroupClientConfig setSamplingHandler(
			Function<McpSchema.CreateMessageRequest, McpSchema.CreateMessageResult> samplingHandler) {
		this.samplingHandler = samplingHandler;
		return this;
	}

	public SyncMCPToolGroupClientConfig setElicitationHandler(
			Function<McpSchema.ElicitRequest, McpSchema.ElicitResult> elicitationHandler) {
		this.elicitationHandler = elicitationHandler;
		return this;
	}

	public SyncMCPToolGroupClientConfig setContextProvider(Supplier<McpTransportContext> contextProvider) {
		this.contextProvider = contextProvider;
		return this;
	}

	private List<Consumer<List<McpSchema.Tool>>> toolsChangeConsumers;
	private List<Consumer<List<McpSchema.Resource>>> resourcesChangeConsumers;
	private List<Consumer<List<McpSchema.ResourceContents>>> resourcesUpdateConsumers;
	private List<Consumer<List<McpSchema.Prompt>>> promptsChangeConsumers;
	private List<Consumer<McpSchema.LoggingMessageNotification>> loggingConsumers;
	private List<Consumer<McpSchema.ProgressNotification>> progressConsumers;
	private Function<McpSchema.CreateMessageRequest, McpSchema.CreateMessageResult> samplingHandler;
	private Function<McpSchema.ElicitRequest, McpSchema.ElicitResult> elicitationHandler;
	private Supplier<McpTransportContext> contextProvider;

	@SuppressWarnings("unchecked")
	public SyncMCPToolGroupClientConfig(Map<String, Object> properties) {
		super(properties);
		this.toolsChangeConsumers = (List<Consumer<List<Tool>>>) properties.get(CLIENT_TOOLS_CHANGE_CONSUMERS);
		this.resourcesChangeConsumers = (List<Consumer<List<Resource>>>) properties
				.get(CLIENT_RESOURCES_CHANGE_CONSUMERS);
		this.resourcesUpdateConsumers = (List<Consumer<List<ResourceContents>>>) properties
				.get(CLIENT_RESOURCES_UPDATE_CONSUMERS);
		this.promptsChangeConsumers = (List<Consumer<List<Prompt>>>) properties.get(CLIENT_PROMPT_CHANGE_CONSUMERS);
		this.loggingConsumers = (List<Consumer<LoggingMessageNotification>>) properties.get(CLIENT_LOGGING_CONSUMERS);
		this.progressConsumers = (List<Consumer<ProgressNotification>>) properties.get(CLIENT_PROGRESS_CONSUMERS);
		this.samplingHandler = (Function<McpSchema.CreateMessageRequest, McpSchema.CreateMessageResult>) properties
				.get(CLIENT_SAMPLING_HANDLER);
		this.elicitationHandler = (Function<ElicitRequest, ElicitResult>) properties.get(CLIENT_ELICITATION_HANDLER);
		contextProvider = (Supplier<McpTransportContext>) properties.get(CLIENT_CONTEXTPROVIDER);
		if (contextProvider == null) {
			contextProvider = () -> {
				return McpTransportContext.EMPTY;
			};
		}
	}

	public McpClientFeatures.Sync buildAsyncClientFeatures() {
		return new McpClientFeatures.Sync(new Implementation(clientName, clientTitle, clientVersion),
				clientCapabilities, rootsChangeConsumers, toolsChangeConsumers, resourcesChangeConsumers,
				resourcesUpdateConsumers, promptsChangeConsumers, loggingConsumers, progressConsumers, samplingHandler,
				elicitationHandler, enableCallToolSchemaCaching);
	}

	protected SDKAsyncToolGroupClient buildMcpAsyncToolGroupClient(LocalToolGroupClient<Tool> localToolGroupClient) {
		return new SDKAsyncToolGroupClient((McpClientTransport) transport, Duration.ofSeconds(requestTimeout),
				Duration.ofSeconds(initializationTimeout), jsonSchemaValidator,
				McpClientFeatures.Async.fromSync(buildAsyncClientFeatures()), this.clientListeners,
				localToolGroupClient);
	}

	public SDKSyncToolGroupClient buildMcpSyncToolGroupClient(LocalToolGroupClient<Tool> localToolGroupClient) {
		localToolGroupClient.setToolGroupClientListeners(clientListeners);
		return new SDKSyncToolGroupClient(buildMcpAsyncToolGroupClient(localToolGroupClient), contextProvider);
	}

	@Override
	public Dictionary<String, Object> asProperties() {
		Dictionary<String, Object> d = super.asProperties();
		if (promptsChangeConsumers != null) {
			d.put(CLIENT_PROMPT_CHANGE_CONSUMERS, promptsChangeConsumers);
		}
		if (toolsChangeConsumers != null) {
			d.put(CLIENT_TOOLS_CHANGE_CONSUMERS, toolsChangeConsumers);
		}
		if (resourcesChangeConsumers != null) {
			d.put(CLIENT_RESOURCES_CHANGE_CONSUMERS, resourcesChangeConsumers);
		}
		if (this.resourcesUpdateConsumers != null) {
			d.put(CLIENT_RESOURCES_UPDATE_CONSUMERS, resourcesUpdateConsumers);
		}
		if (promptsChangeConsumers != null) {
			d.put(CLIENT_PROMPT_CHANGE_CONSUMERS, promptsChangeConsumers);
		}
		if (loggingConsumers != null) {
			d.put(CLIENT_LOGGING_CONSUMERS, loggingConsumers);
		}
		if (progressConsumers != null) {
			d.put(CLIENT_PROGRESS_CONSUMERS, progressConsumers);
		}
		if (samplingHandler != null) {
			d.put(CLIENT_SAMPLING_HANDLER, samplingHandler);
		}
		if (elicitationHandler != null) {
			d.put(CLIENT_ELICITATION_HANDLER, elicitationHandler);
		}
		if (contextProvider != null) {
			d.put(CLIENT_CONTEXTPROVIDER, contextProvider);
		}
		return d;
	}
}