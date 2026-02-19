package org.openmcptools.common.toolgroup.client;

import java.io.Closeable;
import java.util.List;

import org.openmcptools.common.client.InitializeResult;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;

public interface ToolGroupClient<ClientType> extends Closeable {

	static final String CLIENT_PREFIX = ToolGroupClient.class.getName();
	public static final String CLIENT_NAME = CLIENT_PREFIX + ".clientName";
	public static final String CLIENT_TITLE = CLIENT_PREFIX + ".clientTitle";
	public static final String CLIENT_VERSION = CLIENT_PREFIX + ".clientVersion";
	public static final String CLIENT_TRANSPORT = CLIENT_PREFIX + ".clientTransport";
	public static final String CLIENT_CAPABILITIES = CLIENT_PREFIX + ".clientCapabilities";
	public static final String CLIENT_JSONSCHEMAVALIDATOR = CLIENT_PREFIX + ".jsonSchemaValidator";
	public static final String CLIENT_CONTEXTPROVIDER = CLIENT_PREFIX + ".clientContextProvider";
	public static final String CLIENT_LISTENERS = CLIENT_PREFIX + ".clientListeners";
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
	public static final String CLIENT_REQUEST_TIMEOUT = CLIENT_PREFIX + ".clientRequestTimeout";
	public static final String CLIENT_INITIALIZATION_TIMEOUT = CLIENT_PREFIX + ".clientInitializationTimeout";

	InitializeResult initialize();

	List<Tool> getTools();

	List<Group> getGroupRoots();

	ClientType getClient();
}
