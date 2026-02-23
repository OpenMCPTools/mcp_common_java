package org.openmcptools.common.toolgroup.server.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.openmcptools.common.toolgroup.server.ToolGroupServer;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncCompletionSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncPromptSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncResourceSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncResourceTemplateSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.AsyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.Root;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import reactor.core.publisher.Mono;

public class AsyncToolGroupServerConfig extends AbstractToolGroupServerConfig {

	public static final String SERVER_FACTORY_NAME = "AsyncToolGroupServerFactory";
	public static final String SERVER_CF_TARGET = "(component.factory=" + SERVER_FACTORY_NAME + ")";

	public AsyncToolGroupServerConfig setPromptSpecifications(
			Map<String, AsyncPromptSpecification> promptSpecifications) {
		this.promptSpecifications = promptSpecifications;
		return this;
	}

	public AsyncToolGroupServerConfig setRootsChangeConsumers(
			List<BiFunction<McpAsyncServerExchange, List<Root>, Mono<Void>>> rootsChangeConsumers) {
		this.rootsChangeConsumers = rootsChangeConsumers;
		return this;
	}

	public AsyncToolGroupServerConfig setResourceSpecifications(
			Map<String, AsyncResourceSpecification> resourceSpecifications) {
		this.resourceSpecifications = resourceSpecifications;
		return this;
	}

	public AsyncToolGroupServerConfig setResourceTemplateSpecifications(
			Map<String, McpServerFeatures.AsyncResourceTemplateSpecification> resourceTemplateSpecifications) {
		this.resourceTemplateSpecifications = resourceTemplateSpecifications;
		return this;
	}

	public AsyncToolGroupServerConfig setServerCompletions(
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

	public AsyncToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			McpServerTransportProvider transport, Long requestTimeout, String serverInstructions) {
		super(serverName, serverTitle, serverVersion, transport, requestTimeout, serverInstructions);
	}

	public AsyncToolGroupServerConfig(McpServerTransportProvider transport) {
		super(transport);
	}

	public AsyncToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			McpServerTransportProvider transport) {
		super(serverName, serverTitle, serverVersion, transport);
	}

	public AsyncToolGroupServerConfig(String serverName, String serverVersion, McpServerTransportProvider transport) {
		super(serverName, serverVersion, transport);
	}

	@SuppressWarnings("unchecked")
	public AsyncToolGroupServerConfig(Map<String, Object> properties) {
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
	}

	public McpServerFeatures.Async buildAsyncServerFeatures() {
		return new McpServerFeatures.Async(new McpSchema.Implementation(serverName, serverTitle, serverVersion),
				this.serverCapabilities, toolSpecifications, resourceSpecifications, resourceTemplateSpecifications,
				promptSpecifications, serverCompletions, rootsChangeConsumers, serverInstructions);
	}

	public SDKAsyncToolGroupServer buildMcpAsyncToolGroupServer() {
		return new SDKAsyncToolGroupServer(transport, jsonMapper, buildAsyncServerFeatures(),
				Duration.ofSeconds(requestTimeout), uriTemplateManagerFactory, jsonSchemaValidator);
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