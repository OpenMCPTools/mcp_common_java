package org.openmcptools.common.server.toolgroup;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

import org.openmcptools.common.model.Tool;

public interface ToolGroupServer<ServerType> extends Closeable {

	static final String SERVER_PROP_PREFIX = ToolGroupServer.class.getName();
	public static final String SERVER_NAME_PROP = SERVER_PROP_PREFIX + ".serverName";
	public static final String SERVER_VERSION_PROP = SERVER_PROP_PREFIX + ".serverVersion";
	public static final String SERVER_TRANSPORT_PROP = SERVER_PROP_PREFIX + ".serverTransport";
	public static final String SERVER_CAPABILITIES_PROP = SERVER_PROP_PREFIX + ".serverCapabilities";
	public static final String SERVER_TOOL_GROUP_PROVIDER = SERVER_PROP_PREFIX + ".serverToolGroupProvider";
	public static final String SERVER_GENERATE_OUTPUT_SCHEMA = SERVER_PROP_PREFIX + ".outputSchemaGenerator";
	public static final String SERVER_COMPLETIONS_PROP = SERVER_PROP_PREFIX + ".serverCompletions";
	public static final String SERVER_INSTRUCTIONS_PROP = SERVER_PROP_PREFIX + ".serverInstructions";
	public static final String SERVER_JSONMAPPER_PROP = SERVER_PROP_PREFIX + ".jsonMapper";
	public static final String SERVER_JSONSCHEMAVALIDATOR_PROP = SERVER_PROP_PREFIX + ".jsonSchemaValidator";
	public static final String SERVER_PROMPT_SPECIFICATIONS_PROP = SERVER_PROP_PREFIX + ".promptSpecifications";
	public static final String SERVER_REQUEST_DURATION_PROP = SERVER_PROP_PREFIX + ".requestDuration";
	public static final String SERVER_RESOURCESPECIFICATIONS_PROP = SERVER_PROP_PREFIX + ".resourceSpecifications";
	public static final String SERVER_RESOURCE_TEMPLATE_SPECIFICATIONS_PROP = SERVER_PROP_PREFIX
			+ ".resourceTemplateSpecifications";
	public static final String SERVER_TOOLS_SPECIFICATIONS = SERVER_PROP_PREFIX + ".toolSpecifications";
	public static final String SERVER_ROOTS_CHANGE_CONSUMERS = SERVER_PROP_PREFIX + "rootsChangeConsumers";

	public static final long DEFAULT_REQUEST_TIMEOUT = Long
			.parseLong(System.getProperty(ToolGroupServer.class.getName() + ".defaultRequestTimeout", "10"));
	public static final String SERVER_URI_TEMPLATE_MANAGER_FACTORY = SERVER_PROP_PREFIX + ".uriTemplateManagerFactory";
	public static final String SERVER_IMMEDIATE_EXECUTION = SERVER_PROP_PREFIX + ".immediateExecution";

	void removeTools(List<String> toolNames);

	List<Tool> addToolGroups(Map<Object,Class<?>[]> implementerToTypes);
	
	default List<Tool> addToolGroup(Object instance, Class<?>...classes) {
		return addToolGroups(Map.of(instance,classes));
	}
	
	List<Tool> addToolInvokers(List<ToolImplementation> toolInvokers);
	
	ServerType getServer();
	
}
