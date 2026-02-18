package org.openmcptools.common.server.toolgroup.impl.spring;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolConverter;
import org.openmcptools.common.server.InputSchemaGenerator;
import org.openmcptools.common.server.OutputSchemaGenerator;
import org.openmcptools.common.server.impl.spring.InputSchemaGeneratorImpl;
import org.openmcptools.common.server.impl.spring.OutputSchemaGeneratorImpl;
import org.openmcptools.common.server.toolgroup.AsyncToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolGroupProvider;
import org.openmcptools.common.server.toolgroup.ToolGroupServer;
import org.openmcptools.common.server.toolgroup.ToolProviderImpl;
import org.openmcptools.common.server.toolgroup.ToolSpecification;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

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
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.CompleteReference;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpServerTransportProvider;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;
import reactor.core.publisher.Mono;

@Component(factory = "SpringAsyncToolGroupServer", service = AsyncToolGroupServer.class)
public class AsyncToolGroupServerImpl extends
		AbstractToolGroupServerImpl<McpAsyncToolGroupServer, AsyncToolSpecification, McpAsyncServerExchange, Mono<CallToolResult>>
		implements AsyncToolGroupServer<McpAsyncToolGroupServer> {

	public AsyncToolGroupServerImpl() {
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
	protected void addTools(List<AsyncToolSpecification> toolSpecs) {
		super.addTools(toolSpecs);
		this.server.endToolsUpdate();
	}

	@Override
	protected void addTool(McpAsyncToolGroupServer server, AsyncToolSpecification toolSpec) {
		server.addTool(toolSpec).block();
	}

	@Override
	protected void removeTool(McpAsyncToolGroupServer server, String toolName) {
		server.removeTool(toolName).block();
	}

	@SuppressWarnings("unchecked")
	@Activate
	protected void activate(Map<String, Object> properties) {
		Object po = properties.get(SERVER_TOOL_GROUP_PROVIDER);
		if (po instanceof ToolGroupProvider) {
			this.toolGroupProvider = (ToolGroupProvider<AsyncToolSpecification, McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>>) po;
		} else {
			po = properties.get(SERVER_GENERATE_OUTPUT_SCHEMA);
			InputSchemaGenerator isg = new InputSchemaGeneratorImpl();
			OutputSchemaGenerator osg = null;
			if (po instanceof Boolean && ((Boolean) po).booleanValue()) {
				osg = new OutputSchemaGeneratorImpl.Async();
			}
			ToolProviderImpl toolProvider = new ToolProviderImpl(isg, osg);
			this.toolGroupProvider = new AsyncToolGroupProviderImpl(toolProvider, toolConverter, osg != null);
		}
		this.server = buildServerFromProperties(properties);
	}

	@Deactivate
	protected void deactivate() {
		if (this.server != null) {
			this.server.close();
			this.server = null;
		}
	}

	@Override
	protected ToolSpecification<AsyncToolSpecification> getToolSpecification(Tool tool,
			BiFunction<McpAsyncServerExchange, CallToolRequest, Mono<CallToolResult>> callHandler) {
		AsyncToolSpecification.Builder specBuilder = AsyncToolSpecification.builder().tool(convertTool(tool))
				.callHandler(callHandler);
		return new ToolSpecification<AsyncToolSpecification>(tool, specBuilder.build());
	}

	@SuppressWarnings("unchecked")
	protected McpAsyncToolGroupServer buildServerFromProperties(Map<String, Object> properties) {
		String serverName = (String) properties.get(SERVER_NAME_PROP);
		Objects.requireNonNull(serverName, SERVER_NAME_PROP + "property must not be null");
		String serverVersion = (String) properties.get(SERVER_VERSION_PROP);
		Objects.requireNonNull(serverVersion, SERVER_VERSION_PROP + " property must not be null");
		McpSchema.Implementation serverImpl = new McpSchema.Implementation(serverName, serverVersion);

		ServerCapabilities serverCapabilities = (ServerCapabilities) properties.get(SERVER_CAPABILITIES_PROP);
		if (serverCapabilities == null) {
			serverCapabilities = ServerCapabilities.builder().tools(true).build();
		} else {
			serverCapabilities = serverCapabilities.mutate().tools(true).build();
		}

		List<McpServerFeatures.AsyncToolSpecification> toolSpecifications = (List<McpServerFeatures.AsyncToolSpecification>) properties
				.get(ToolGroupServer.SERVER_TOOLS_SPECIFICATIONS);
		toolSpecifications = (toolSpecifications == null) ? List.of() : toolSpecifications;

		Map<String, AsyncResourceSpecification> resourceSpecifications = (Map<String, AsyncResourceSpecification>) properties
				.get(SERVER_RESOURCESPECIFICATIONS_PROP);
		if (resourceSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
		} else {
			resourceSpecifications = Map.of();
		}

		List<AsyncResourceTemplateSpecification> rtSpecsList = (List<AsyncResourceTemplateSpecification>) properties
				.get(SERVER_RESOURCE_TEMPLATE_SPECIFICATIONS_PROP);
		Map<String, AsyncResourceTemplateSpecification> resourceTemplateSpecifications = Map.of();
		if (rtSpecsList != null) {
			serverCapabilities = serverCapabilities.mutate().resources(true, true).build();
			for (var resourceTemplate : rtSpecsList) {
				resourceTemplateSpecifications.put(resourceTemplate.resourceTemplate().uriTemplate(), resourceTemplate);
			}
		}

		Map<String, AsyncPromptSpecification> promptSpecifications = (Map<String, AsyncPromptSpecification>) properties
				.get(SERVER_PROMPT_SPECIFICATIONS_PROP);

		if (promptSpecifications != null) {
			serverCapabilities = serverCapabilities.mutate().prompts(true).build();
		} else {
			promptSpecifications = Map.of();
		}

		List<AsyncCompletionSpecification> serverCompletionsList = (List<AsyncCompletionSpecification>) properties
				.get(SERVER_COMPLETIONS_PROP);
		Map<CompleteReference, AsyncCompletionSpecification> serverCompletions = Map.of();
		if (serverCompletionsList != null) {
			for (McpServerFeatures.AsyncCompletionSpecification completion : serverCompletionsList) {
				serverCompletions.put(completion.referenceKey(), completion);
			}
		}

		List<BiFunction<McpAsyncServerExchange, List<McpSchema.Root>, Mono<Void>>> rootsChangeConsumers = (List<BiFunction<McpAsyncServerExchange, List<McpSchema.Root>, Mono<Void>>>) properties
				.get(ToolGroupServer.SERVER_ROOTS_CHANGE_CONSUMERS);
		if (rootsChangeConsumers == null) {
			rootsChangeConsumers = List.of();
		}

		String serverInstructions = (String) properties.get(SERVER_INSTRUCTIONS_PROP);

		McpServerTransportProvider transport = (McpServerTransportProvider) properties.get(SERVER_TRANSPORT_PROP);
		Objects.requireNonNull(transport, SERVER_TRANSPORT_PROP + " property must not be null");

		McpJsonMapper jsonMapper = (McpJsonMapper) properties.get(SERVER_JSONMAPPER_PROP);

		McpServerFeatures.Async features = buildAsyncServerFeatures(serverImpl, serverCapabilities, toolSpecifications,
				resourceSpecifications, resourceTemplateSpecifications, promptSpecifications, serverCompletions,
				rootsChangeConsumers, serverInstructions);

		McpUriTemplateManagerFactory uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties
				.get(ToolGroupServer.SERVER_URI_TEMPLATE_MANAGER_FACTORY);

		JsonSchemaValidator jsonSchemaValidator = (JsonSchemaValidator) properties.get(SERVER_JSONSCHEMAVALIDATOR_PROP);

		Long requestTimeout = (Long) properties.get(SERVER_REQUEST_DURATION_PROP);
		if (requestTimeout == null) {
			requestTimeout = 10L;
		}

		return buildMcpAsyncToolGroupServer(transport, jsonMapper, features, Duration.ofSeconds(requestTimeout),
				uriTemplateManagerFactory, jsonSchemaValidator);
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
