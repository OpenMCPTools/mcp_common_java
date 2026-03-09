package org.openmcptools.common.toolgroup.server;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openmcptools.common.model.Tool;

public interface ToolGroupServer<SpecificationType> extends Closeable {

	public static final String SERVER_PREFIX = ToolGroupServer.class.getName();
	public static final String SERVER_NAME = SERVER_PREFIX + ".serverName";
	public static final String SERVER_TITLE = SERVER_PREFIX + ".serverTitle";
	public static final String SERVER_VERSION = SERVER_PREFIX + ".serverVersion";
	public static final String SERVER_TRANSPORT_PROVIDER = SERVER_PREFIX + ".serverTransportProvider";
	public static final String SERVER_CAPABILITIES = SERVER_PREFIX + ".serverCapabilities";
	public static final String SERVER_TOOL_GROUP_PROVIDER = SERVER_PREFIX + ".serverToolGroupProvider";
	public static final String SERVER_GENERATE_OUTPUT_SCHEMA = SERVER_PREFIX + ".outputSchemaGenerator";
	public static final String SERVER_COMPLETIONS = SERVER_PREFIX + ".serverCompletions";
	public static final String SERVER_INSTRUCTIONS = SERVER_PREFIX + ".serverInstructions";
	public static final String SERVER_JSONMAPPER = SERVER_PREFIX + ".jsonMapper";
	public static final String SERVER_JSONSCHEMAVALIDATOR = SERVER_PREFIX + ".jsonSchemaValidator";
	public static final String SERVER_PROMPT_SPECS = SERVER_PREFIX + ".promptSpecifications";
	public static final String SERVER_REQUEST_DURATION = SERVER_PREFIX + ".requestDuration";
	public static final String SERVER_RESOURCE_SPECS = SERVER_PREFIX + ".resourceSpecifications";
	public static final String SERVER_RESOURCE_TEMPLATE_SPECS = SERVER_PREFIX + ".resourceTemplateSpecifications";
	public static final String SERVER_TOOLS_SPECS = SERVER_PREFIX + ".toolSpecifications";
	public static final String SERVER_ROOTS_CHANGE_CONSUMERS = SERVER_PREFIX + "rootsChangeConsumers";

	public static final long DEFAULT_REQUEST_TIMEOUT = Long
			.parseLong(System.getProperty(ToolGroupServer.class.getName() + ".defaultRequestTimeout", "10"));
	public static final String SERVER_URI_TEMPLATE_MANAGER_FACTORY = SERVER_PREFIX + ".uriTemplateManagerFactory";
	public static final String SERVER_IMMEDIATE_EXECUTION = SERVER_PREFIX + ".immediateExecution";

	Tool removeTool(String toolName);

	default List<Tool> removeTools(List<String> toolNames) {
		return toolNames.stream().map(tn -> removeTool(tn)).filter(Objects::nonNull).toList();
	}

	List<Tool> addToolGroups(Map<Object, Class<?>[]> implementerToTypes);

	default List<Tool> addToolGroup(Object instance, Class<?>... classes) {
		return addToolGroups(Map.of(instance, classes));
	}

	Tool addToolImpl(ToolImpl toolImpl);

	default List<Tool> addToolImpls(List<ToolImpl> toolInvokers) {
		return toolInvokers.stream().map(ti -> addToolImpl(ti)).filter(Objects::isNull).toList();
	}

	Tool addToolSpecification(ToolSpecification<SpecificationType> toolSpec);

	default List<Tool> addToolSpecifications(List<ToolSpecification<SpecificationType>> toolSpecs) {
		return toolSpecs.stream().map(ts -> addToolSpecification(ts)).filter(Objects::isNull).toList();
	}

}
