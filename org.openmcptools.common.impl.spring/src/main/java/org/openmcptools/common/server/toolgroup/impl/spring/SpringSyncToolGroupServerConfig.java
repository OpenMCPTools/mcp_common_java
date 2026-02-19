package org.openmcptools.common.server.toolgroup.impl.spring;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.openmcptools.common.server.toolgroup.ToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolGroupServerConfig;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures.SyncCompletionSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceTemplateSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;

public class SpringSyncToolGroupServerConfig extends ToolGroupServerConfig<McpServerTransportProvider> {

	public static final String SERVER_FACTORY_NAME = "SpringSyncToolGroupServer";
	public static final String SERVER_CF_TARGET = "(component.factory=" + SERVER_FACTORY_NAME + ")";

	private ServerCapabilities serverCapabilities;
	private Map<String, SyncPromptSpecification> promptSpecifications;
	private List<BiConsumer<McpSyncServerExchange, List<McpSchema.Root>>> rootsChangeConsumers;
	private McpJsonMapper jsonMapper;
	private List<SyncToolSpecification> toolSpecifications;
	private McpUriTemplateManagerFactory uriTemplateManagerFactory;
	private JsonSchemaValidator jsonSchemaValidator;
	private Map<String, SyncResourceSpecification> resourceSpecifications;
	private Map<String, McpServerFeatures.SyncResourceTemplateSpecification> resourceTemplateSpecifications;
	private Map<McpSchema.CompleteReference, McpServerFeatures.SyncCompletionSpecification> serverCompletions;
	private Boolean immediateExecution = false;

	public SpringSyncToolGroupServerConfig(String serverName, String serverVersion,
			McpServerTransportProvider transport, Long requestTimeout, String serverInstructions) {
		super(serverName, serverVersion, transport, requestTimeout, serverInstructions);
	}

	public SpringSyncToolGroupServerConfig(McpServerTransportProvider transport) {
		super(transport);
	}

	public SpringSyncToolGroupServerConfig(String serverName, String serverVersion,
			McpServerTransportProvider transport) {
		super(serverName, serverVersion, transport);
	}


	@SuppressWarnings("unchecked")
	public SpringSyncToolGroupServerConfig(Map<String, Object> properties) {
		super(properties);
		this.serverCapabilities = (ServerCapabilities) properties.get(AbstractToolGroupServerImpl.SERVER_CAPABILITIES);
		if (serverCapabilities == null) {
			serverCapabilities = ServerCapabilities.builder().tools(true).build();
		} else {
			serverCapabilities = serverCapabilities.mutate().tools(true).build();
		}
		resourceSpecifications = (Map<String, SyncResourceSpecification>) properties
				.get(AbstractToolGroupServerImpl.SERVER_RESOURCE_SPECS);
		if (resourceSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
		} else {
			resourceSpecifications = Map.of();
		}
		List<SyncResourceTemplateSpecification> rtSpecsList = (List<SyncResourceTemplateSpecification>) properties
				.get(AbstractToolGroupServerImpl.SERVER_RESOURCE_TEMPLATE_SPECS);
		resourceTemplateSpecifications = Map.of();
		if (rtSpecsList != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
			for (var resourceTemplate : rtSpecsList) {
				resourceTemplateSpecifications.put(resourceTemplate.resourceTemplate().uriTemplate(),
						resourceTemplate);
			}
		}
		this.promptSpecifications = (Map<String, SyncPromptSpecification>) properties
				.get(AbstractToolGroupServerImpl.SERVER_PROMPT_SPECS);
		if (promptSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().prompts(true).build();
		} else {
			promptSpecifications = Map.of();
		}
		List<SyncCompletionSpecification> serverCompletionsList = (List<SyncCompletionSpecification>) properties
				.get(AbstractToolGroupServerImpl.SERVER_COMPLETIONS);
		serverCompletions = Map.of();
		if (serverCompletionsList != null) {
			for (McpServerFeatures.SyncCompletionSpecification completion : serverCompletionsList) {
				serverCompletions.put(completion.referenceKey(), completion);
			}
		}
		rootsChangeConsumers = (List<BiConsumer<McpSyncServerExchange, List<McpSchema.Root>>>) properties
				.get(ToolGroupServer.SERVER_ROOTS_CHANGE_CONSUMERS);
		if (rootsChangeConsumers == null) {
			rootsChangeConsumers = List.of();
		}
		jsonMapper = (McpJsonMapper) properties.get(AbstractToolGroupServerImpl.SERVER_JSONMAPPER);
		toolSpecifications = (List<McpServerFeatures.SyncToolSpecification>) properties
				.get(ToolGroupServer.SERVER_TOOLS_SPECS);
		toolSpecifications = (toolSpecifications == null) ? List.of() : toolSpecifications;
		uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties
				.get(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY);
		jsonSchemaValidator = (JsonSchemaValidator) properties.get(AbstractToolGroupServerImpl.SERVER_JSONSCHEMAVALIDATOR);
		Boolean im = (Boolean) properties.get(AbstractToolGroupServerImpl.SERVER_IMMEDIATE_EXECUTION);
		if (im != null) {
			this.immediateExecution = im;
		}
	}

	protected McpServerFeatures.Sync buildServerFeatures() {
		return new McpServerFeatures.Sync(
				new McpSchema.Implementation(serverName, serverVersion),
				serverCapabilities, toolSpecifications, resourceSpecifications, resourceTemplateSpecifications,
				promptSpecifications, serverCompletions, rootsChangeConsumers, serverInstructions);
	}

	protected McpAsyncToolGroupServer buildAsyncToolGroupServer() {
		McpServerFeatures.Async mcpAsyncServerFeatures = McpServerFeatures.Async.fromSync(buildServerFeatures(),
				immediateExecution);
		return new McpAsyncToolGroupServer(transport, jsonMapper, mcpAsyncServerFeatures,
				Duration.ofSeconds(requestTimeout), uriTemplateManagerFactory, jsonSchemaValidator);
	}

	public McpSyncToolGroupServer buildMcpSyncToolGroupServer() {
		return new McpSyncToolGroupServer(buildAsyncToolGroupServer(), immediateExecution);
	}

}