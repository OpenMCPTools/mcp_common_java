package org.openmcptools.common.toolgroup.server.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.openmcptools.common.toolgroup.server.ToolGroupServer;
import org.openmcptools.transport.server.MCPServerTransportProvider;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncCompletionSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncPromptSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncResourceSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncResourceTemplateSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.JSONRPCMessage;
import io.modelcontextprotocol.spec.McpSchema.Root;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

public class AsyncMCPToolGroupServerConfig extends AbstractMCPToolGroupServerConfig {

	public static final String SERVER_FACTORY_NAME = "AsyncToolGroupServerFactory";
	public static final String SERVER_CF_TARGET = "(component.factory=" + SERVER_FACTORY_NAME + ")";

	public AsyncMCPToolGroupServerConfig(
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider) {
		super(transportProvider);
	}

	public AsyncMCPToolGroupServerConfig(String serverName, String serverTitle,
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider) {
		super(serverName, serverTitle, transportProvider);
	}

	public AsyncMCPToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider, Long requestTimeout,
			String serverInstructions) {
		super(serverName, serverTitle, serverVersion, transportProvider, requestTimeout, serverInstructions);
	}

	public AsyncMCPToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			MCPServerTransportProvider<Mono<Void>, Mono<?>, JSONRPCMessage> transportProvider) {
		super(serverName, serverTitle, serverVersion, transportProvider);
	}

	public AsyncMCPToolGroupServerConfig setPromptSpecifications(
			Map<String, AsyncPromptSpecification> promptSpecifications) {
		this.promptSpecifications = promptSpecifications;
		return this;
	}

	public AsyncMCPToolGroupServerConfig setRootsChangeConsumers(
			List<BiFunction<McpAsyncServerExchange, List<Root>, Mono<Void>>> rootsChangeConsumers) {
		this.rootsChangeConsumers = rootsChangeConsumers;
		return this;
	}

	public AsyncMCPToolGroupServerConfig setResourceSpecifications(
			Map<String, AsyncResourceSpecification> resourceSpecifications) {
		this.resourceSpecifications = resourceSpecifications;
		return this;
	}

	public AsyncMCPToolGroupServerConfig setResourceTemplateSpecifications(
			Map<String, McpServerFeatures.AsyncResourceTemplateSpecification> resourceTemplateSpecifications) {
		this.resourceTemplateSpecifications = resourceTemplateSpecifications;
		return this;
	}

	public AsyncMCPToolGroupServerConfig setServerCompletions(
			Map<McpSchema.CompleteReference, McpServerFeatures.AsyncCompletionSpecification> serverCompletions) {
		this.serverCompletions = serverCompletions;
		return this;
	}

	private Map<String, AsyncPromptSpecification> promptSpecifications;
	private List<BiFunction<McpAsyncServerExchange, List<Root>, Mono<Void>>> rootsChangeConsumers;
	private List<AsyncToolSpecification> toolSpecifications;
	private Map<String, AsyncResourceSpecification> resourceSpecifications;
	private Map<String, McpServerFeatures.AsyncResourceTemplateSpecification> resourceTemplateSpecifications;
	private Map<McpSchema.CompleteReference, McpServerFeatures.AsyncCompletionSpecification> serverCompletions;

	@SuppressWarnings("unchecked")
	public AsyncMCPToolGroupServerConfig(Map<String, Object> properties) {
		super(properties);
		resourceSpecifications = (Map<String, AsyncResourceSpecification>) properties
				.get(ToolGroupServer.SERVER_RESOURCE_SPECS);
		if (resourceSpecifications != null) {
			this.serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
		} else {
			resourceSpecifications = Map.of();
		}
		List<AsyncResourceTemplateSpecification> rtSpecsList = (List<AsyncResourceTemplateSpecification>) properties
				.get(ToolGroupServer.SERVER_RESOURCE_TEMPLATE_SPECS);
		resourceTemplateSpecifications = Map.of();
		if (rtSpecsList != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
			for (var resourceTemplate : rtSpecsList) {
				resourceTemplateSpecifications.put(resourceTemplate.resourceTemplate().uriTemplate(), resourceTemplate);
			}
		}
		this.promptSpecifications = (Map<String, AsyncPromptSpecification>) properties
				.get(ToolGroupServer.SERVER_PROMPT_SPECS);
		if (promptSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().prompts(true).build();
		} else {
			promptSpecifications = Map.of();
		}

		List<AsyncCompletionSpecification> serverCompletionsList = (List<AsyncCompletionSpecification>) properties
				.get(ToolGroupServer.SERVER_COMPLETIONS);
		serverCompletions = Map.of();
		if (serverCompletionsList != null) {
			for (McpServerFeatures.AsyncCompletionSpecification completion : serverCompletionsList) {
				serverCompletions.put(completion.referenceKey(), completion);
			}
		}
		rootsChangeConsumers = (List<BiFunction<McpAsyncServerExchange, List<McpSchema.Root>, Mono<Void>>>) properties
				.get(ToolGroupServer.SERVER_ROOTS_CHANGE_CONSUMERS);
		if (rootsChangeConsumers == null) {
			rootsChangeConsumers = List.of();
		}
		toolSpecifications = (List<McpServerFeatures.AsyncToolSpecification>) properties
				.get(ToolGroupServer.SERVER_TOOLS_SPECS);
		toolSpecifications = (toolSpecifications == null) ? List.of() : toolSpecifications;

		jsonMapper = (McpJsonMapper) properties.get(AbstractToolGroupServerImpl.SERVER_JSONMAPPER);
		if (jsonMapper == null) {
			jsonMapper = McpJsonDefaults.getMapper();
		}

		uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties
				.get(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY);
		jsonSchemaValidator = (JsonSchemaValidator) properties
				.get(AbstractToolGroupServerImpl.SERVER_JSONSCHEMAVALIDATOR);
		if (jsonSchemaValidator == null) {
			jsonSchemaValidator = McpJsonDefaults.getSchemaValidator();
		}

	}

	public McpServerFeatures.Async buildAsyncServerFeatures() {
		return new McpServerFeatures.Async(new McpSchema.Implementation(serverName, serverTitle, serverVersion),
				this.serverCapabilities, toolSpecifications, resourceSpecifications, resourceTemplateSpecifications,
				promptSpecifications, serverCompletions, rootsChangeConsumers, serverInstructions);
	}

	public SDKAsyncToolGroupServer buildMcpAsyncToolGroupServer() {
		return new SDKAsyncToolGroupServer(transportProvider, Duration.ofSeconds(requestTimeout), jsonMapper,
				jsonSchemaValidator, buildAsyncServerFeatures(), uriTemplateManagerFactory);
	}

	@Override
	public Dictionary<String, Object> asProperties() {
		Dictionary<String, Object> d = super.asProperties();
		if (promptSpecifications != null) {
			d.put(ToolGroupServer.SERVER_PROMPT_SPECS, promptSpecifications);
		}
		if (rootsChangeConsumers != null) {
			d.put(ToolGroupServer.SERVER_ROOTS_CHANGE_CONSUMERS, rootsChangeConsumers);
		}
		if (toolSpecifications != null) {
			d.put(ToolGroupServer.SERVER_TOOLS_SPECS, toolSpecifications);
		}
		if (resourceSpecifications != null) {
			d.put(ToolGroupServer.SERVER_RESOURCE_SPECS, resourceSpecifications);
		}
		if (resourceTemplateSpecifications != null) {
			d.put(ToolGroupServer.SERVER_RESOURCE_TEMPLATE_SPECS, resourceTemplateSpecifications);
		}
		if (serverCompletions != null) {
			d.put(ToolGroupServer.SERVER_COMPLETIONS, serverCompletions);
		}
		return d;
	}
}