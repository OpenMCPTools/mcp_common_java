package org.openmcptools.common.client.toolgroup.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.openmcptools.common.toolgroup.client.ToolGroupClient;
import org.openmcptools.common.toolgroup.client.ToolGroupClientConfig;
import org.openmcptools.common.toolgroup.client.ToolGroupClientListener;

import io.modelcontextprotocol.client.McpClientFeatures;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ClientCapabilities;
import io.modelcontextprotocol.spec.McpSchema.CreateMessageRequest;
import io.modelcontextprotocol.spec.McpSchema.CreateMessageResult;
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
import reactor.core.publisher.Mono;

public class AsyncToolGroupClientConfig extends ToolGroupClientConfig<McpClientTransport> {

	public static final String CLIENT_FACTORY_NAME = "AsyncToolGroupClientFactory";
	public static final String CLIENT_CF_TARGET = "(component.factory=" + CLIENT_FACTORY_NAME + ")";

	public static final Long DEFAULT_INITIALIZATION_TIMEOUT = (Long) Long.parseLong(
			System.getProperty(AsyncToolGroupClientConfig.class.getName() + ".defaultInitializationTimeout", "30"));

	public AsyncToolGroupClientConfig(String clientName, String clientVersion, McpClientTransport transport,
			Long requestTimeout) {
		super(clientName, DEFAULT_CLIENT_TITLE, clientVersion, transport, requestTimeout);
	}

	public AsyncToolGroupClientConfig(String clientName, String clientTitle, String clientVersion,
			McpClientTransport transport, Long requestTimeout) {
		super(clientName, clientTitle, clientVersion, transport, requestTimeout);
	}

	public AsyncToolGroupClientConfig(McpClientTransport transport, Long requestTimeout) {
		super(transport, requestTimeout);
	}

	public AsyncToolGroupClientConfig(McpClientTransport transport) {
		super(transport, DEFAULT_CLIENT_REQUEST_TIMEOUT);
	}

	public AsyncToolGroupClientConfig setClientCapabilities(McpSchema.ClientCapabilities clientCapabilities) {
		this.clientCapabilities = clientCapabilities;
		return this;
	}

	public AsyncToolGroupClientConfig setRootsChangeConsumers(Map<String, McpSchema.Root> rootsChangeConsumers) {
		this.rootsChangeConsumers = rootsChangeConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setToolsChangeConsumers(
			List<Function<List<McpSchema.Tool>, Mono<Void>>> toolsChangeConsumers) {
		this.toolsChangeConsumers = toolsChangeConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setResourcesChangeConsumers(
			List<Function<List<McpSchema.Resource>, Mono<Void>>> resourcesChangeConsumers) {
		this.resourcesChangeConsumers = resourcesChangeConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setResourcesUpdateConsumers(
			List<Function<List<McpSchema.ResourceContents>, Mono<Void>>> resourcesUpdateConsumers) {
		this.resourcesUpdateConsumers = resourcesUpdateConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setPromptsChangeConsumers(
			List<Function<List<McpSchema.Prompt>, Mono<Void>>> promptsChangeConsumers) {
		this.promptsChangeConsumers = promptsChangeConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setLoggingConsumers(
			List<Function<McpSchema.LoggingMessageNotification, Mono<Void>>> loggingConsumers) {
		this.loggingConsumers = loggingConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setProgressConsumers(
			List<Function<McpSchema.ProgressNotification, Mono<Void>>> progressConsumers) {
		this.progressConsumers = progressConsumers;
		return this;
	}

	public AsyncToolGroupClientConfig setSamplingHandler(
			Function<McpSchema.CreateMessageRequest, Mono<McpSchema.CreateMessageResult>> samplingHandler) {
		this.samplingHandler = samplingHandler;
		return this;
	}

	public AsyncToolGroupClientConfig setElicitationHandler(
			Function<McpSchema.ElicitRequest, Mono<McpSchema.ElicitResult>> elicitationHandler) {
		this.elicitationHandler = elicitationHandler;
		return this;
	}

	public void setEnableCallToolSchemaCaching(Boolean enableCallToolSchemaCaching) {
		this.enableCallToolSchemaCaching = enableCallToolSchemaCaching;
	}

	public void setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public void setUriTemplateManagerFactory(McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
	}

	public void setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
		this.jsonSchemaValidator = jsonSchemaValidator;
	}

	public void setInitializationTimeout(long initializationTimeout) {
		this.initializationTimeout = initializationTimeout;
	}

	private McpSchema.ClientCapabilities clientCapabilities;
	private Map<String, McpSchema.Root> rootsChangeConsumers;
	private List<Function<List<McpSchema.Tool>, Mono<Void>>> toolsChangeConsumers;
	private List<Function<List<McpSchema.Resource>, Mono<Void>>> resourcesChangeConsumers;
	private List<Function<List<McpSchema.ResourceContents>, Mono<Void>>> resourcesUpdateConsumers;
	private List<Function<List<McpSchema.Prompt>, Mono<Void>>> promptsChangeConsumers;
	private List<Function<McpSchema.LoggingMessageNotification, Mono<Void>>> loggingConsumers;
	private List<Function<McpSchema.ProgressNotification, Mono<Void>>> progressConsumers;
	private Function<McpSchema.CreateMessageRequest, Mono<McpSchema.CreateMessageResult>> samplingHandler;
	private Function<McpSchema.ElicitRequest, Mono<McpSchema.ElicitResult>> elicitationHandler;
	private Boolean enableCallToolSchemaCaching = false;
	private McpJsonMapper jsonMapper;
	private McpUriTemplateManagerFactory uriTemplateManagerFactory;
	private JsonSchemaValidator jsonSchemaValidator;
	private long initializationTimeout = DEFAULT_INITIALIZATION_TIMEOUT;

	@SuppressWarnings("unchecked")
	public AsyncToolGroupClientConfig(Map<String, Object> properties) {
		super(properties);
		this.clientCapabilities = (ClientCapabilities) properties.get(ToolGroupClient.CLIENT_CAPABILITIES);
		if (clientCapabilities == null) {
			clientCapabilities = ClientCapabilities.builder().build();
		}
		this.rootsChangeConsumers = (Map<String, Root>) properties.get(ToolGroupClient.CLIENT_ROOTS_CHANGE_CONSUMERS);
		this.toolsChangeConsumers = (List<Function<List<Tool>, Mono<Void>>>) properties
				.get(ToolGroupClient.CLIENT_TOOLS_CHANGE_CONSUMERS);
		this.resourcesChangeConsumers = (List<Function<List<Resource>, Mono<Void>>>) properties
				.get(ToolGroupClient.CLIENT_RESOURCES_CHANGE_CONSUMERS);
		this.resourcesUpdateConsumers = (List<Function<List<ResourceContents>, Mono<Void>>>) properties
				.get(ToolGroupClient.CLIENT_RESOURCES_UPDATE_CONSUMERS);
		this.promptsChangeConsumers = (List<Function<List<Prompt>, Mono<Void>>>) properties
				.get(ToolGroupClient.CLIENT_PROMPT_CHANGE_CONSUMERS);
		this.loggingConsumers = (List<Function<LoggingMessageNotification, Mono<Void>>>) properties
				.get(ToolGroupClient.CLIENT_LOGGING_CONSUMERS);
		this.progressConsumers = (List<Function<ProgressNotification, Mono<Void>>>) properties
				.get(ToolGroupClient.CLIENT_PROGRESS_CONSUMERS);
		this.samplingHandler = (Function<CreateMessageRequest, Mono<CreateMessageResult>>) properties
				.get(ToolGroupClient.CLIENT_SAMPLING_HANDLER);
		this.elicitationHandler = (Function<ElicitRequest, Mono<ElicitResult>>) properties
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
	}

	public McpClientFeatures.Async buildAsyncClientFeatures() {
		return new McpClientFeatures.Async(new Implementation(clientName, clientTitle, clientVersion),
				clientCapabilities, rootsChangeConsumers, toolsChangeConsumers, resourcesChangeConsumers,
				resourcesUpdateConsumers, promptsChangeConsumers, loggingConsumers, progressConsumers, samplingHandler,
				elicitationHandler, enableCallToolSchemaCaching);
	}

	public McpAsyncToolGroupClient buildMcpAsyncToolGroupClient(List<ToolGroupClientListener> listeners,
			LocalToolGroupClient<Tool> localToolGroupClient) {
		return new McpAsyncToolGroupClient(transport, Duration.ofSeconds(requestTimeout),
				Duration.ofSeconds(initializationTimeout), jsonSchemaValidator, buildAsyncClientFeatures(), listeners,
				localToolGroupClient);
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
		return d;
	}
}