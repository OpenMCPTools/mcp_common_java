package org.openmcptools.common.toolgroup.server.impl.spring;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.openmcptools.common.toolgroup.server.ToolGroupServer;
import org.openmcptools.common.toolgroup.server.ToolGroupServerConfig;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpServerFeatures.SyncCompletionSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceTemplateSpecification;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;

public class SyncToolGroupServerConfig extends ToolGroupServerConfig<McpServerTransportProvider> {

	public static final String SERVER_FACTORY_NAME = "SyncToolGroupServerFactory";
	public static final String SERVER_CF_TARGET = "(component.factory=" + SERVER_FACTORY_NAME + ")";

	public SyncToolGroupServerConfig setServerCapabilities(ServerCapabilities serverCapabilities) {
		this.serverCapabilities = serverCapabilities;
		return this;
	}

	public SyncToolGroupServerConfig setPromptSpecifications(
			Map<String, SyncPromptSpecification> promptSpecifications) {
		this.promptSpecifications = promptSpecifications;
		return this;
	}

	public SyncToolGroupServerConfig setRootsChangeConsumers(
			List<BiConsumer<McpSyncServerExchange, List<McpSchema.Root>>> rootsChangeConsumers) {
		this.rootsChangeConsumers = rootsChangeConsumers;
		return this;
	}

	public SyncToolGroupServerConfig setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	public SyncToolGroupServerConfig setToolSpecifications(List<SyncToolSpecification> toolSpecifications) {
		this.toolSpecifications = toolSpecifications;
		return this;
	}

	public SyncToolGroupServerConfig setUriTemplateManagerFactory(
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
		return this;
	}

	public SyncToolGroupServerConfig setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
		this.jsonSchemaValidator = jsonSchemaValidator;
		return this;
	}

	public SyncToolGroupServerConfig setResourceSpecifications(
			Map<String, SyncResourceSpecification> resourceSpecifications) {
		this.resourceSpecifications = resourceSpecifications;
		return this;
	}

	public SyncToolGroupServerConfig setResourceTemplateSpecifications(
			Map<String, McpServerFeatures.SyncResourceTemplateSpecification> resourceTemplateSpecifications) {
		this.resourceTemplateSpecifications = resourceTemplateSpecifications;
		return this;
	}

	public SyncToolGroupServerConfig setServerCompletions(
			Map<McpSchema.CompleteReference, McpServerFeatures.SyncCompletionSpecification> serverCompletions) {
		this.serverCompletions = serverCompletions;
		return this;
	}

	public SyncToolGroupServerConfig setImmediateExecution(Boolean immediateExecution) {
		this.immediateExecution = immediateExecution;
		return this;
	}

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

	public SyncToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			McpServerTransportProvider transport, Long requestTimeout, String serverInstructions) {
		super(serverName, serverTitle, serverVersion, transport, requestTimeout, serverInstructions);
	}

	public SyncToolGroupServerConfig(String serverName, String serverTitle, String serverVersion,
			McpServerTransportProvider transport) {
		super(serverName, serverTitle, serverVersion, transport);
	}

	public SyncToolGroupServerConfig(McpServerTransportProvider transport) {
		super(transport);
	}

	public SyncToolGroupServerConfig(String serverName, String serverVersion, McpServerTransportProvider transport) {
		super(serverName, serverVersion, transport);
	}

	@SuppressWarnings("unchecked")
	public SyncToolGroupServerConfig(Map<String, Object> properties) {
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
				resourceTemplateSpecifications.put(resourceTemplate.resourceTemplate().uriTemplate(), resourceTemplate);
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
		if (jsonMapper == null) {
			jsonMapper = McpJsonDefaults.getMapper();
		}
		toolSpecifications = (List<McpServerFeatures.SyncToolSpecification>) properties
				.get(ToolGroupServer.SERVER_TOOLS_SPECS);
		toolSpecifications = (toolSpecifications == null) ? List.of() : toolSpecifications;
		uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties
				.get(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY);
		jsonSchemaValidator = (JsonSchemaValidator) properties
				.get(AbstractToolGroupServerImpl.SERVER_JSONSCHEMAVALIDATOR);
		if (jsonSchemaValidator == null) {
			jsonSchemaValidator = McpJsonDefaults.getSchemaValidator();
		}
		Boolean im = (Boolean) properties.get(AbstractToolGroupServerImpl.SERVER_IMMEDIATE_EXECUTION);
		if (im != null) {
			this.immediateExecution = im;
		}
	}

	protected McpServerFeatures.Sync buildServerFeatures() {
		return new McpServerFeatures.Sync(new McpSchema.Implementation(serverName, serverVersion), serverCapabilities,
				toolSpecifications, resourceSpecifications, resourceTemplateSpecifications, promptSpecifications,
				serverCompletions, rootsChangeConsumers, serverInstructions);
	}

	protected SDKAsyncToolGroupServer buildAsyncToolGroupServer() {
		McpServerFeatures.Async mcpAsyncServerFeatures = McpServerFeatures.Async.fromSync(buildServerFeatures(),
				immediateExecution);
		return new SDKAsyncToolGroupServer(transport, jsonMapper, mcpAsyncServerFeatures,
				Duration.ofSeconds(requestTimeout), uriTemplateManagerFactory, jsonSchemaValidator);
	}

	public SDKSyncToolGroupServer buildMcpSyncToolGroupServer() {
		return new SDKSyncToolGroupServer(buildAsyncToolGroupServer(), immediateExecution);
	}

	@Override
	public Dictionary<String, Object> asProperties() {
		Dictionary<String, Object> d = super.asProperties();
		if (serverCapabilities != null) {
			d.put(ToolGroupServer.SERVER_CAPABILITIES, serverCapabilities);
		}
		if (promptSpecifications != null) {
			d.put(ToolGroupServer.SERVER_PROMPT_SPECS, promptSpecifications);
		}
		if (rootsChangeConsumers != null) {
			d.put(ToolGroupServer.SERVER_ROOTS_CHANGE_CONSUMERS, rootsChangeConsumers);
		}
		if (jsonMapper != null) {
			d.put(ToolGroupServer.SERVER_JSONMAPPER, jsonMapper);
		}
		if (toolSpecifications != null) {
			d.put(ToolGroupServer.SERVER_TOOLS_SPECS, toolSpecifications);
		}
		if (uriTemplateManagerFactory != null) {
			d.put(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY, uriTemplateManagerFactory);
		}
		if (jsonSchemaValidator != null) {
			d.put(ToolGroupServer.SERVER_JSONSCHEMAVALIDATOR, jsonSchemaValidator);
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
		if (immediateExecution != null) {
			d.put(ToolGroupServer.SERVER_IMMEDIATE_EXECUTION, immediateExecution);
		}
		return d;
	}

}