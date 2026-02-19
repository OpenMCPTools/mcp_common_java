package org.openmcptools.common.client.toolgroup.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.openmcptools.common.toolgroup.client.ToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClientConfig;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;

import io.modelcontextprotocol.client.McpClientFeatures;
import io.modelcontextprotocol.common.McpTransportContext;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ClientCapabilities;
import io.modelcontextprotocol.spec.McpSchema.ElicitRequest;
import io.modelcontextprotocol.spec.McpSchema.ElicitResult;
import io.modelcontextprotocol.spec.McpSchema.Implementation;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.ProgressNotification;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.Resource;
import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
import io.modelcontextprotocol.spec.McpSchema.Root;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;

public class SyncToolGroupClientConfig extends ToolGroupClientConfig<McpClientTransport> {

	public static final String CLIENT_FACTORY_NAME = "SyncToolGroupClientFactory";
	public static final String CLIENT_CF_TARGET = "(component.factory=" + CLIENT_FACTORY_NAME + ")";

	public static final Long DEFAULT_INITIALIZATION_TIMEOUT = (Long) Long.parseLong(System
			.getProperty(SyncToolGroupClientConfig.class.getName() + ".defaultInitializationTimeout", "30"));

	public SyncToolGroupClientConfig(String serverName, String serverVersion, McpClientTransport transport, Long requestTimeout) {
		super(serverName, serverVersion, transport, requestTimeout);
	}

	public SyncToolGroupClientConfig(String clientName, String clientTitle, String clientVersion,
			McpClientTransport transport, Long requestTimeout) {
		super(clientName, clientTitle, clientVersion, transport, requestTimeout);
	}

	public SyncToolGroupClientConfig(McpClientTransport transport, Long requestTimeout) {
		super(transport, requestTimeout);
	}

	public SyncToolGroupClientConfig(McpClientTransport transport) {
		super(transport, DEFAULT_CLIENT_REQUEST_TIMEOUT);
	}

	public SyncToolGroupClientConfig setClientCapabilities(McpSchema.ClientCapabilities clientCapabilities) {
		this.clientCapabilities = clientCapabilities;
		return this;
	}

	public SyncToolGroupClientConfig setRootsChangeConsumers(Map<String, McpSchema.Root> rootsChangeConsumers) {
		this.rootsChangeConsumers = rootsChangeConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setToolsChangeConsumers(
			List<Consumer<List<McpSchema.Tool>>> toolsChangeConsumers) {
		this.toolsChangeConsumers = toolsChangeConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setResourcesChangeConsumers(
			List<Consumer<List<McpSchema.Resource>>> resourcesChangeConsumers) {
		this.resourcesChangeConsumers = resourcesChangeConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setResourcesUpdateConsumers(
			List<Consumer<List<McpSchema.ResourceContents>>> resourcesUpdateConsumers) {
		this.resourcesUpdateConsumers = resourcesUpdateConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setPromptsChangeConsumers(
			List<Consumer<List<McpSchema.Prompt>>> promptsChangeConsumers) {
		this.promptsChangeConsumers = promptsChangeConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setLoggingConsumers(
			List<Consumer<McpSchema.LoggingMessageNotification>> loggingConsumers) {
		this.loggingConsumers = loggingConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setProgressConsumers(
			List<Consumer<McpSchema.ProgressNotification>> progressConsumers) {
		this.progressConsumers = progressConsumers;
		return this;
	}

	public SyncToolGroupClientConfig setSamplingHandler(
			Function<McpSchema.CreateMessageRequest, McpSchema.CreateMessageResult> samplingHandler) {
		this.samplingHandler = samplingHandler;
		return this;
	}

	public SyncToolGroupClientConfig setElicitationHandler(
			Function<McpSchema.ElicitRequest, McpSchema.ElicitResult> elicitationHandler) {
		this.elicitationHandler = elicitationHandler;
		return this;
	}

	public SyncToolGroupClientConfig setEnableCallToolSchemaCaching(Boolean enableCallToolSchemaCaching) {
		this.enableCallToolSchemaCaching = enableCallToolSchemaCaching;
		return this;
	}

	public SyncToolGroupClientConfig setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	public SyncToolGroupClientConfig setUriTemplateManagerFactory(
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
		return this;
	}

	public SyncToolGroupClientConfig setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
		this.jsonSchemaValidator = jsonSchemaValidator;
		return this;
	}

	public SyncToolGroupClientConfig setInitializationTimeout(long initializationTimeout) {
		this.initializationTimeout = initializationTimeout;
		return this;
	}

	public SyncToolGroupClientConfig setContextProvider(Supplier<McpTransportContext> contextProvider) {
		this.contextProvider = contextProvider;
		return this;
	}

	private McpSchema.ClientCapabilities clientCapabilities;
	private Map<String, McpSchema.Root> rootsChangeConsumers;
	private List<Consumer<List<McpSchema.Tool>>> toolsChangeConsumers;
	private List<Consumer<List<McpSchema.Resource>>> resourcesChangeConsumers;
	private List<Consumer<List<McpSchema.ResourceContents>>> resourcesUpdateConsumers;
	private List<Consumer<List<McpSchema.Prompt>>> promptsChangeConsumers;
	private List<Consumer<McpSchema.LoggingMessageNotification>> loggingConsumers;
	private List<Consumer<McpSchema.ProgressNotification>> progressConsumers;
	private Function<McpSchema.CreateMessageRequest, McpSchema.CreateMessageResult> samplingHandler;
	private Function<McpSchema.ElicitRequest, McpSchema.ElicitResult> elicitationHandler;
	private Boolean enableCallToolSchemaCaching = false;
	private McpJsonMapper jsonMapper;
	private McpUriTemplateManagerFactory uriTemplateManagerFactory;
	private JsonSchemaValidator jsonSchemaValidator;
	private long initializationTimeout = DEFAULT_INITIALIZATION_TIMEOUT;
	private Supplier<McpTransportContext> contextProvider;

	@SuppressWarnings("unchecked")
	public SyncToolGroupClientConfig(Map<String, Object> properties) {
		super(properties);
		this.clientCapabilities = (ClientCapabilities) properties.get(ToolGroupClient.CLIENT_CAPABILITIES);
		if (clientCapabilities == null) {
			clientCapabilities = ClientCapabilities.builder().build();
		}
		this.rootsChangeConsumers = (Map<String, Root>) properties.get(ToolGroupClient.CLIENT_ROOTS_CHANGE_CONSUMERS);
		this.toolsChangeConsumers = (List<Consumer<List<Tool>>>) properties
				.get(ToolGroupClient.CLIENT_TOOLS_CHANGE_CONSUMERS);
		this.resourcesChangeConsumers = (List<Consumer<List<Resource>>>) properties
				.get(ToolGroupClient.CLIENT_RESOURCES_CHANGE_CONSUMERS);
		this.resourcesUpdateConsumers = (List<Consumer<List<ResourceContents>>>) properties
				.get(ToolGroupClient.CLIENT_RESOURCES_UPDATE_CONSUMERS);
		this.promptsChangeConsumers = (List<Consumer<List<Prompt>>>) properties
				.get(ToolGroupClient.CLIENT_PROMPT_CHANGE_CONSUMERS);
		this.loggingConsumers = (List<Consumer<LoggingMessageNotification>>) properties
				.get(ToolGroupClient.CLIENT_LOGGING_CONSUMERS);
		this.progressConsumers = (List<Consumer<ProgressNotification>>) properties
				.get(ToolGroupClient.CLIENT_PROGRESS_CONSUMERS);
		this.samplingHandler = (Function<McpSchema.CreateMessageRequest, McpSchema.CreateMessageResult>) properties
				.get(ToolGroupClient.CLIENT_SAMPLING_HANDLER);
		this.elicitationHandler = (Function<ElicitRequest, ElicitResult>) properties
				.get(ToolGroupClient.CLIENT_ELICITATION_HANDLER);
		Boolean sc = (Boolean) properties.get(ToolGroupClient.CLIENT_ENABLE_CALL_TOOL_SCHEMA_CACHING);
		if (sc != null) {
			this.enableCallToolSchemaCaching = sc;
		}
		jsonMapper = (McpJsonMapper) properties.get(ToolGroupClient.CLIENT_JSONMAPPER);
		uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties
				.get(ToolGroupClient.CLIENT_URI_TEMPLATE_MANAGER_FACTORY);
		jsonSchemaValidator = (JsonSchemaValidator) properties.get(ToolGroupClient.CLIENT_JSONSCHEMAVALIDATOR);
		Long initializationTimeout = (Long) properties.get(ToolGroupClient.CLIENT_INITIALIZATION_TIMEOUT);
		if (initializationTimeout != null) {
			this.initializationTimeout = initializationTimeout;
		}
		contextProvider = (Supplier<McpTransportContext>) properties.get(ToolGroupClient.CLIENT_CONTEXTPROVIDER);
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

	protected McpAsyncToolGroupClient buildMcpAsyncToolGroupClient(List<ToolGroupClientListener> listeners,
			LocalToolGroupClient<Tool> localToolGroupClient) {
		return new McpAsyncToolGroupClient(transport, Duration.ofSeconds(requestTimeout),
				Duration.ofSeconds(initializationTimeout), jsonSchemaValidator,
				McpClientFeatures.Async.fromSync(buildAsyncClientFeatures()), listeners, localToolGroupClient);
	}

	public McpSyncToolGroupClient buildMcpSyncToolGroupClient(List<ToolGroupClientListener> listeners,
			LocalToolGroupClient<Tool> localToolGroupClient) {
		return new McpSyncToolGroupClient(buildMcpAsyncToolGroupClient(listeners, localToolGroupClient),
				contextProvider);
	}

	@Override
	public Dictionary<String, Object> asProperties() {
		Dictionary<String, Object> d = super.asProperties();
		d.put(ToolGroupClient.CLIENT_CAPABILITIES, clientCapabilities);
		if (rootsChangeConsumers != null) {
			d.put(ToolGroupClient.CLIENT_ROOTS_CHANGE_CONSUMERS, rootsChangeConsumers);
		}
		if (promptsChangeConsumers != null) {
			d.put(ToolGroupClient.CLIENT_PROMPT_CHANGE_CONSUMERS, promptsChangeConsumers);
		}
		if (toolsChangeConsumers != null) {
			d.put(ToolGroupClient.CLIENT_TOOLS_CHANGE_CONSUMERS, toolsChangeConsumers);
		}
		if (resourcesChangeConsumers != null) {
			d.put(ToolGroupClient.CLIENT_RESOURCES_CHANGE_CONSUMERS, resourcesChangeConsumers);
		}
		if (this.resourcesUpdateConsumers != null) {
			d.put(ToolGroupClient.CLIENT_RESOURCES_UPDATE_CONSUMERS, resourcesUpdateConsumers);
		}
		if (promptsChangeConsumers != null) {
			d.put(ToolGroupClient.CLIENT_PROMPT_CHANGE_CONSUMERS, promptsChangeConsumers);
		}
		if (loggingConsumers != null) {
			d.put(ToolGroupClient.CLIENT_LOGGING_CONSUMERS, loggingConsumers);
		}
		if (progressConsumers != null) {
			d.put(ToolGroupClient.CLIENT_PROGRESS_CONSUMERS, progressConsumers);
		}
		if (samplingHandler != null) {
			d.put(ToolGroupClient.CLIENT_SAMPLING_HANDLER, samplingHandler);
		}
		if (elicitationHandler != null) {
			d.put(ToolGroupClient.CLIENT_ELICITATION_HANDLER, elicitationHandler);
		}
		if (jsonMapper != null) {
			d.put(ToolGroupClient.CLIENT_JSONMAPPER, jsonMapper);
		}
		if (uriTemplateManagerFactory != null) {
			d.put(ToolGroupClient.CLIENT_URI_TEMPLATE_MANAGER_FACTORY, uriTemplateManagerFactory);
		}
		if (jsonSchemaValidator != null) {
			d.put(ToolGroupClient.CLIENT_JSONSCHEMAVALIDATOR, jsonSchemaValidator);
		}
		if (contextProvider != null) {
			d.put(ToolGroupClient.CLIENT_CONTEXTPROVIDER, contextProvider);
		}
		return d;
	}
}