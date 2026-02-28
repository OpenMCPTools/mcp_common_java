package org.openmcptools.common.toolgroup.server.impl.spring;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.openmcptools.common.impl.spring.ToolConverter;
import org.openmcptools.common.toolgroup.server.AbstractToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolGroupServer;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.Async;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncResourceSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.Sync;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public abstract class AbstractToolGroupServerImpl<SpecificationType, ExchangeType, CallToolResultType>
		extends AbstractToolGroupServer<SpecificationType, Tool, ExchangeType, CallToolRequest, CallToolResultType> {

	protected ToolConverter toolConverter;
	protected McpJsonDefaults jsonDefaults;

	protected void setMcpJsonDefaults(McpJsonDefaults jsonDefaults) {
		this.jsonDefaults = jsonDefaults;
	}

	protected void setToolConverter(ToolConverter toolConverter) {
		this.toolConverter = toolConverter;
	}

	protected Tool convertTool(org.openmcptools.common.model.Tool tool) {
		return this.toolConverter.convertFrom(tool);
	}

	protected McpServerFeatures.Async buildAsyncServerFeatures(McpSchema.Implementation serverInfo,
			McpSchema.ServerCapabilities serverCapabilities, List<McpServerFeatures.AsyncToolSpecification> tools,
			Map<String, AsyncResourceSpecification> resources,
			Map<String, McpServerFeatures.AsyncResourceTemplateSpecification> resourceTemplates,
			Map<String, McpServerFeatures.AsyncPromptSpecification> prompts,
			Map<McpSchema.CompleteReference, McpServerFeatures.AsyncCompletionSpecification> completions,
			List<BiFunction<McpAsyncServerExchange, List<McpSchema.Root>, Mono<Void>>> rootsChangeConsumers,
			String instructions) {
		return new McpServerFeatures.Async(serverInfo, serverCapabilities, tools, resources, resourceTemplates, prompts,
				completions, rootsChangeConsumers, instructions);
	}

	@SuppressWarnings("static-access")
	protected SDKAsyncToolGroupServer buildMcpAsyncToolGroupServer(McpServerTransportProvider mcpTransportProvider,
			Async mcpServerFeatures, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		return buildMcpAsyncToolGroupServer(mcpTransportProvider, this.jsonDefaults.getMapper(), mcpServerFeatures,
				requestTimeout, uriTemplateManagerFactory, this.jsonDefaults.getSchemaValidator());
	}

	protected SDKAsyncToolGroupServer buildMcpAsyncToolGroupServer(McpServerTransportProvider mcpTransportProvider,
			McpJsonMapper jsonMapper, Async mcpServerFeatures, Duration requestTimeout,
			McpUriTemplateManagerFactory uriTemplateManagerFactory, JsonSchemaValidator jsonSchemaValidator) {
		Objects.requireNonNull(mcpTransportProvider, "mcpTransportProvider must not be null");
		Objects.requireNonNull(mcpServerFeatures, "mcpServerFeatures must not be null");

		return new SDKAsyncToolGroupServer(mcpTransportProvider, jsonMapper, mcpServerFeatures, requestTimeout,
				uriTemplateManagerFactory, jsonSchemaValidator);
	}

	protected McpServerFeatures.Sync buildSyncServerFeatures(McpSchema.Implementation serverInfo,
			McpSchema.ServerCapabilities serverCapabilities, List<McpServerFeatures.SyncToolSpecification> tools,
			Map<String, SyncResourceSpecification> resources,
			Map<String, McpServerFeatures.SyncResourceTemplateSpecification> resourceTemplates,
			Map<String, McpServerFeatures.SyncPromptSpecification> prompts,
			Map<McpSchema.CompleteReference, McpServerFeatures.SyncCompletionSpecification> completions,
			List<BiConsumer<McpSyncServerExchange, List<McpSchema.Root>>> rootsChangeConsumers, String instructions) {
		return new McpServerFeatures.Sync(serverInfo, serverCapabilities, tools, resources, resourceTemplates, prompts,
				completions, rootsChangeConsumers, instructions);
	}

	@SuppressWarnings("static-access")
	protected SDKSyncToolGroupServer buildMcpSyncToolGroupServer(McpServerTransportProvider mcpTransportProvider,
			Sync mcpServerFeatures, Duration requestTimeout, McpUriTemplateManagerFactory uriTemplateManagerFactory,
			boolean immediateExecution) {
		return buildMcpSyncToolGroupServer(mcpTransportProvider, this.jsonDefaults.getMapper(), mcpServerFeatures,
				requestTimeout, uriTemplateManagerFactory, this.jsonDefaults.getSchemaValidator(), immediateExecution);
	}

	protected SDKSyncToolGroupServer buildMcpSyncToolGroupServer(McpServerTransportProvider mcpTransportProvider,
			McpJsonMapper jsonMapper, Sync mcpServerFeatures, Duration requestTimeout,
			McpUriTemplateManagerFactory uriTemplateManagerFactory, JsonSchemaValidator jsonSchemaValidator,
			boolean immediateExecution) {
		Objects.requireNonNull(mcpTransportProvider, "mcpTransportProvider must not be null");
		Objects.requireNonNull(mcpServerFeatures, "mcpServerFeatures must not be null");

		Duration requestTimeoutDuration = Duration.ofSeconds(ToolGroupServer.DEFAULT_REQUEST_TIMEOUT);

		McpServerFeatures.Async mcpAsyncServerFeatures = McpServerFeatures.Async.fromSync(mcpServerFeatures,
				immediateExecution);

		SDKAsyncToolGroupServer asyncServer = buildMcpAsyncToolGroupServer(mcpTransportProvider, mcpAsyncServerFeatures,
				requestTimeoutDuration, uriTemplateManagerFactory);

		return new SDKSyncToolGroupServer(asyncServer, immediateExecution);
	}

	protected abstract McpSchema.Tool findTool(String toolName);

}
