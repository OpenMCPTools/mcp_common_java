package org.openmcptools.common.server.toolgroup.impl.spring;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.InputSchemaGenerator;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.server.toolgroup.SyncToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolGroupProvider;
import org.openmcptools.common.server.toolgroup.ToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolGroupServerConfig;
import org.openmcptools.common.server.toolgroup.ToolProviderImpl;
import org.openmcptools.common.server.toolgroup.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

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
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.CompleteReference;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;

@Component(factory = "SpringSyncToolGroupServer", service = SyncToolGroupServer.class)
public class SyncToolGroupServerImpl extends
		AbstractToolGroupServerImpl<McpSyncToolGroupServer, SyncToolSpecification, McpSyncServerExchange, CallToolResult>
		implements SyncToolGroupServer<McpSyncToolGroupServer> {

	public SyncToolGroupServerImpl() {
		super();
	}

	@Reference
	void setToolConverter(ToolConverter<io.modelcontextprotocol.spec.McpSchema.Tool> toolConverter) {
		this.toolConverter = toolConverter;
	}

	@Override
	protected void closeServer() {
		if (this.server != null) {
			this.server.closeGracefully();
			this.server = null;
		}
	}

	@Override
	protected void addTool(McpSyncToolGroupServer server, SyncToolSpecification toolSpec) {
		server.addTool(toolSpec);
	}

	@Override
	protected void removeTool(McpSyncToolGroupServer server, String toolName) {
		server.removeTool(toolName);
	}

	@Override
	protected ToolSpecification<SyncToolSpecification> getToolSpecification(Tool tool,
			BiFunction<McpSyncServerExchange, CallToolRequest, CallToolResult> callHandler) {
		SyncToolSpecification.Builder specBuilder = SyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(callHandler);
		return new ToolSpecification<SyncToolSpecification>(tool, specBuilder.build());
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		Object po = properties.get(SERVER_TOOL_GROUP_PROVIDER);
		if (po instanceof ToolGroupProvider) {
			this.toolGroupProvider = (ToolGroupProvider<SyncToolSpecification, McpSyncServerExchange, CallToolRequest, CallToolResult>) po;
		} else {
			po = properties.get(SERVER_GENERATE_OUTPUT_SCHEMA);
			InputSchemaGenerator isg = new InputSchemaGeneratorImpl();
			OutputSchemaGenerator osg = null;
			if (po instanceof Boolean && ((Boolean) po).booleanValue()) {
				osg = new OutputSchemaGeneratorImpl.Sync();
			}
			ToolProviderImpl toolProvider = new ToolProviderImpl(isg, osg);
			this.toolGroupProvider = new SyncToolGroupProviderImpl(toolProvider, toolConverter, osg != null);
		}
		SpringSyncToolGroupServerConfig tgServerConfig = (SpringSyncToolGroupServerConfig) properties
				.get(ToolGroupServerConfig.TOOL_GROUP_SERVER_CONFIG);
		if (tgServerConfig == null) {
			tgServerConfig = new SpringSyncToolGroupServerConfig(properties);
		}
		this.server = tgServerConfig.buildMcpSyncToolGroupServer();
	}

	@Deactivate
	protected void deactivate() {
		if (this.server != null) {
			this.server.close();
		}
	}

	@SuppressWarnings("unchecked")
	protected McpSyncToolGroupServer buildServerFromProperties(Map<String, Object> properties) {
		String serverName = (String) properties.get(ToolGroupServer.SERVER_NAME);
		Objects.requireNonNull(serverName, ToolGroupServer.SERVER_NAME + "property must not be null");
		String serverVersion = (String) properties.get(ToolGroupServer.SERVER_VERSION);
		Objects.requireNonNull(serverVersion, ToolGroupServer.SERVER_VERSION + " property must not be null");
		McpSchema.Implementation serverImpl = new McpSchema.Implementation(serverName, serverVersion);

		ServerCapabilities serverCapabilities = (ServerCapabilities) properties
				.get(ToolGroupServer.SERVER_CAPABILITIES);
		if (serverCapabilities == null) {
			serverCapabilities = ServerCapabilities.builder().tools(true).build();
		} else {
			serverCapabilities = serverCapabilities.mutate().tools(true).build();
		}

		List<McpServerFeatures.SyncToolSpecification> toolSpecifications = (List<McpServerFeatures.SyncToolSpecification>) properties
				.get(ToolGroupServer.SERVER_TOOLS_SPECS);
		toolSpecifications = (toolSpecifications == null) ? List.of() : toolSpecifications;

		Map<String, SyncResourceSpecification> resourceSpecifications = (Map<String, SyncResourceSpecification>) properties
				.get(ToolGroupServer.SERVER_RESOURCE_SPECS);
		if (resourceSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
		} else {
			resourceSpecifications = Map.of();
		}

		List<SyncResourceTemplateSpecification> rtSpecsList = (List<SyncResourceTemplateSpecification>) properties
				.get(ToolGroupServer.SERVER_RESOURCE_TEMPLATE_SPECS);
		Map<String, SyncResourceTemplateSpecification> resourceTemplateSpecifications = Map.of();
		if (rtSpecsList != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
			for (var resourceTemplate : rtSpecsList) {
				resourceTemplateSpecifications.put(resourceTemplate.resourceTemplate().uriTemplate(), resourceTemplate);
			}
		}

		Map<String, SyncPromptSpecification> promptSpecifications = (Map<String, SyncPromptSpecification>) properties
				.get(ToolGroupServer.SERVER_PROMPT_SPECS);
		if (promptSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().prompts(true).build();
		} else {
			promptSpecifications = Map.of();
		}

		List<SyncCompletionSpecification> serverCompletionsList = (List<SyncCompletionSpecification>) properties
				.get(ToolGroupServer.SERVER_COMPLETIONS);
		Map<CompleteReference, SyncCompletionSpecification> serverCompletions = Map.of();
		if (serverCompletionsList != null) {
			for (McpServerFeatures.SyncCompletionSpecification completion : serverCompletionsList) {
				serverCompletions.put(completion.referenceKey(), completion);
			}
		}

		List<BiConsumer<McpSyncServerExchange, List<McpSchema.Root>>> rootsChangeConsumers = (List<BiConsumer<McpSyncServerExchange, List<McpSchema.Root>>>) properties
				.get(ToolGroupServer.SERVER_ROOTS_CHANGE_CONSUMERS);
		if (rootsChangeConsumers == null) {
			rootsChangeConsumers = List.of();
		}

		String serverInstructions = (String) properties.get(ToolGroupServer.SERVER_INSTRUCTIONS);

		McpServerTransportProvider transport = (McpServerTransportProvider) properties
				.get(ToolGroupServer.SERVER_TRANSPORT);
		Objects.requireNonNull(transport, SERVER_TRANSPORT + " property must not be null");

		McpJsonMapper jsonMapper = (McpJsonMapper) properties.get(ToolGroupServer.SERVER_JSONMAPPER);

		McpServerFeatures.Sync serverFeatures = buildSyncServerFeatures(serverImpl, serverCapabilities,
				toolSpecifications, resourceSpecifications, resourceTemplateSpecifications, promptSpecifications,
				serverCompletions, rootsChangeConsumers, serverInstructions);

		Long requestTimeout = (Long) properties.get(SERVER_REQUEST_DURATION);
		if (requestTimeout == null) {
			requestTimeout = 10L;
		}

		McpUriTemplateManagerFactory uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties
				.get(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY);

		JsonSchemaValidator jsonSchemaValidator = (JsonSchemaValidator) properties
				.get(ToolGroupServer.SERVER_JSONSCHEMAVALIDATOR);

		Boolean immediateExecution = (Boolean) properties.get(ToolGroupServer.SERVER_IMMEDIATE_EXECUTION);
		if (immediateExecution == null) {
			immediateExecution = false;
		}
		return buildMcpSyncToolGroupServer(transport, jsonMapper, serverFeatures, Duration.ofSeconds(requestTimeout),
				uriTemplateManagerFactory, jsonSchemaValidator, immediateExecution);
	}

	@Override
	protected void startToolsUpdate() {
		this.server.startToolsUpdate();
	}

	@Override
	protected void endToolsUpdate() {
		this.server.endToolsUpdate();
	}

}
