package org.openmcptools.common.client.toolgroup.impl.spring;

import java.util.Dictionary;
import java.util.Map;

import org.openmcptools.common.toolgroup.client.ToolGroupClientConfig;

import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.json.schema.JsonSchemaValidator;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ClientCapabilities;
import io.modelcontextprotocol.spec.McpSchema.Root;
import io.modelcontextprotocol.util.McpUriTemplateManagerFactory;

public class AbstractToolGroupClientConfig extends ToolGroupClientConfig<McpClientTransport> {

	public static final String CLIENT_CAPABILITIES = CLIENT_PREFIX + ".clientCapabilities";
	public static final String CLIENT_JSONSCHEMAVALIDATOR = CLIENT_PREFIX + ".jsonSchemaValidator";
	public static final String CLIENT_CONTEXTPROVIDER = CLIENT_PREFIX + ".clientContextProvider";
	public static final String CLIENT_ROOTS_CHANGE_CONSUMERS = CLIENT_PREFIX + ".clientRoots";
	public static final String CLIENT_TOOLS_CHANGE_CONSUMERS = CLIENT_PREFIX + ".clientToolsChangeConsumers";
	public static final String CLIENT_RESOURCES_CHANGE_CONSUMERS = CLIENT_PREFIX + ".clientResourcesChangeConsumers";
	public static final String CLIENT_RESOURCES_UPDATE_CONSUMERS = CLIENT_PREFIX + ".clientResourcesUpdateConsumers";
	public static final String CLIENT_PROMPT_CHANGE_CONSUMERS = CLIENT_PREFIX + ".clientPromptChangeConsumers";
	public static final String CLIENT_LOGGING_CONSUMERS = CLIENT_PREFIX + ".clientLoggingConsumers";
	public static final String CLIENT_PROGRESS_CONSUMERS = CLIENT_PREFIX + ".clientProgressConsumers";
	public static final String CLIENT_SAMPLING_HANDLER = CLIENT_PREFIX + ".clientSamplingHandler";
	public static final String CLIENT_ELICITATION_HANDLER = CLIENT_PREFIX + ".clientElicitationHandler";
	public static final String CLIENT_ENABLE_CALL_TOOL_SCHEMA_CACHING = CLIENT_PREFIX
			+ ".clientEnableCallToolSchemaCaching";
	public static final String CLIENT_JSONMAPPER = CLIENT_PREFIX + ".clientJsonMapper";
	public static final String CLIENT_URI_TEMPLATE_MANAGER_FACTORY = CLIENT_PREFIX + ".clientUriTemplateManagerFactory";
	public static final String CLIENT_INITIALIZATION_TIMEOUT = CLIENT_PREFIX + ".clientInitializationTimeout";

	public static final Long DEFAULT_INITIALIZATION_TIMEOUT = (Long) Long.parseLong(
			System.getProperty(SyncToolGroupClientConfig.class.getName() + ".defaultInitializationTimeout", "30"));

	protected McpSchema.ClientCapabilities clientCapabilities;
	protected Map<String, McpSchema.Root> rootsChangeConsumers;
	protected Boolean enableCallToolSchemaCaching = false;
	protected McpJsonMapper jsonMapper;
	protected McpUriTemplateManagerFactory uriTemplateManagerFactory;
	protected JsonSchemaValidator jsonSchemaValidator;
	protected long initializationTimeout = DEFAULT_INITIALIZATION_TIMEOUT;

	public AbstractToolGroupClientConfig(McpClientTransport transport, Long requestTimeout) {
		super(transport, requestTimeout);
	}

	public AbstractToolGroupClientConfig(String clientName, String clientVersion, McpClientTransport transport,
			Long requestTimeout) {
		super(clientName, clientVersion, transport, requestTimeout);
	}

	public AbstractToolGroupClientConfig(String clientName, String clientTitle, String clientVersion,
			McpClientTransport transport, Long requestTimeout) {
		super(clientName, clientTitle, clientVersion, transport, requestTimeout);
	}

	public AbstractToolGroupClientConfig setClientCapabilities(McpSchema.ClientCapabilities clientCapabilities) {
		this.clientCapabilities = clientCapabilities;
		return this;
	}

	public AbstractToolGroupClientConfig setRootsChangeConsumers(Map<String, McpSchema.Root> rootsChangeConsumers) {
		this.rootsChangeConsumers = rootsChangeConsumers;
		return this;
	}

	public AbstractToolGroupClientConfig setEnableCallToolSchemaCaching(Boolean enableCallToolSchemaCaching) {
		this.enableCallToolSchemaCaching = enableCallToolSchemaCaching;
		return this;
	}

	public AbstractToolGroupClientConfig setJsonMapper(McpJsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
		return this;
	}

	public AbstractToolGroupClientConfig setUriTemplateManagerFactory(
			McpUriTemplateManagerFactory uriTemplateManagerFactory) {
		this.uriTemplateManagerFactory = uriTemplateManagerFactory;
		return this;
	}

	public AbstractToolGroupClientConfig setJsonSchemaValidator(JsonSchemaValidator jsonSchemaValidator) {
		this.jsonSchemaValidator = jsonSchemaValidator;
		return this;
	}

	public AbstractToolGroupClientConfig setInitializationTimeout(long initializationTimeout) {
		this.initializationTimeout = initializationTimeout;
		return this;
	}

	@SuppressWarnings("unchecked")
	public AbstractToolGroupClientConfig(Map<String, Object> properties) {
		super(properties);
		this.clientCapabilities = (ClientCapabilities) properties.get(CLIENT_CAPABILITIES);
		if (clientCapabilities == null) {
			clientCapabilities = ClientCapabilities.builder().build();
		}
		this.rootsChangeConsumers = (Map<String, Root>) properties.get(CLIENT_ROOTS_CHANGE_CONSUMERS);
		Boolean sc = (Boolean) properties.get(CLIENT_ENABLE_CALL_TOOL_SCHEMA_CACHING);
		if (sc != null) {
			this.enableCallToolSchemaCaching = sc;
		}
		jsonMapper = (McpJsonMapper) properties.get(CLIENT_JSONMAPPER);
		uriTemplateManagerFactory = (McpUriTemplateManagerFactory) properties.get(CLIENT_URI_TEMPLATE_MANAGER_FACTORY);
		jsonSchemaValidator = (JsonSchemaValidator) properties.get(CLIENT_JSONSCHEMAVALIDATOR);
		Long initializationTimeout = (Long) properties.get(CLIENT_INITIALIZATION_TIMEOUT);
		if (initializationTimeout != null) {
			this.initializationTimeout = initializationTimeout;
		}
	}

	public Dictionary<String, Object> asProperties() {
		Dictionary<String, Object> d = super.asProperties();
		if (clientCapabilities == null) {
			clientCapabilities = ClientCapabilities.builder().build();
		}
		d.put(CLIENT_CAPABILITIES, clientCapabilities);
		if (rootsChangeConsumers != null) {
			d.put(CLIENT_ROOTS_CHANGE_CONSUMERS, rootsChangeConsumers);
		}
		if (jsonMapper != null) {
			d.put(CLIENT_JSONMAPPER, jsonMapper);
		}
		if (uriTemplateManagerFactory != null) {
			d.put(CLIENT_URI_TEMPLATE_MANAGER_FACTORY, uriTemplateManagerFactory);
		}
		if (jsonSchemaValidator != null) {
			d.put(CLIENT_JSONSCHEMAVALIDATOR, jsonSchemaValidator);
		}

		return d;
	}
}
