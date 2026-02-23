package org.openmcptools.common.toolgroup.client.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import io.modelcontextprotocol.client.McpClientFeatures;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
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
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public class AsyncToolGroupClientConfig extends AbstractToolGroupClientConfig {

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

	public AsyncToolGroupClientConfig setEnableCallToolSchemaCaching(Boolean enableCallToolSchemaCaching) {
		this.enableCallToolSchemaCaching = enableCallToolSchemaCaching;
		return this;
	}

	public AsyncToolGroupClientConfig setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	public AsyncToolGroupClientConfig setUriTemplateManagerFactory(
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
		return this;
	}

	public AsyncToolGroupClientConfig setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
		this.jsonSchemaValidator = jsonSchemaValidator;
		return this;
	}

	public AsyncToolGroupClientConfig setInitializationTimeout(long initializationTimeout) {
		this.initializationTimeout = initializationTimeout;
		return this;
	}

	private List<Function<List<McpSchema.Tool>, Mono<Void>>> toolsChangeConsumers;
	private List<Function<List<McpSchema.Resource>, Mono<Void>>> resourcesChangeConsumers;
	private List<Function<List<McpSchema.ResourceContents>, Mono<Void>>> resourcesUpdateConsumers;
	private List<Function<List<McpSchema.Prompt>, Mono<Void>>> promptsChangeConsumers;
	private List<Function<McpSchema.LoggingMessageNotification, Mono<Void>>> loggingConsumers;
	private List<Function<McpSchema.ProgressNotification, Mono<Void>>> progressConsumers;
	private Function<McpSchema.CreateMessageRequest, Mono<McpSchema.CreateMessageResult>> samplingHandler;
	private Function<McpSchema.ElicitRequest, Mono<McpSchema.ElicitResult>> elicitationHandler;
	@SuppressWarnings("unchecked")
	public AsyncToolGroupClientConfig(Map<String, Object> properties) {
		super(properties);
		this.resourcesChangeConsumers = (List<Function<List<Resource>, Mono<Void>>>) properties
				.get(CLIENT_RESOURCES_CHANGE_CONSUMERS);
		this.resourcesUpdateConsumers = (List<Function<List<ResourceContents>, Mono<Void>>>) properties
				.get(CLIENT_RESOURCES_UPDATE_CONSUMERS);
		this.promptsChangeConsumers = (List<Function<List<Prompt>, Mono<Void>>>) properties
				.get(CLIENT_PROMPT_CHANGE_CONSUMERS);
		this.loggingConsumers = (List<Function<LoggingMessageNotification, Mono<Void>>>) properties
				.get(CLIENT_LOGGING_CONSUMERS);
		this.progressConsumers = (List<Function<ProgressNotification, Mono<Void>>>) properties
				.get(CLIENT_PROGRESS_CONSUMERS);
		this.samplingHandler = (Function<CreateMessageRequest, Mono<CreateMessageResult>>) properties
				.get(CLIENT_SAMPLING_HANDLER);
		this.elicitationHandler = (Function<ElicitRequest, Mono<ElicitResult>>) properties
				.get(CLIENT_ELICITATION_HANDLER);
	}

	public McpClientFeatures.Async buildAsyncClientFeatures() {
		return new McpClientFeatures.Async(new Implementation(clientName, clientTitle, clientVersion),
				clientCapabilities, rootsChangeConsumers, toolsChangeConsumers, resourcesChangeConsumers,
				resourcesUpdateConsumers, promptsChangeConsumers, loggingConsumers, progressConsumers, samplingHandler,
				elicitationHandler, enableCallToolSchemaCaching);
	}

	public SDKAsyncToolGroupClient buildMcpAsyncToolGroupClient(LocalToolGroupClient<Tool> localToolGroupClient) {
		return new SDKAsyncToolGroupClient(transport, Duration.ofSeconds(requestTimeout),
				Duration.ofSeconds(initializationTimeout), jsonSchemaValidator, buildAsyncClientFeatures(),
				this.clientListeners, localToolGroupClient);
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
		return d;
	}
}