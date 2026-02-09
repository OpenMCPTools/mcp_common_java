package org.openmcptools.common.server.toolgroup;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.List;

import org.openmcptools.common.model.Tool;

public interface ToolGroupServer extends Closeable {

	static final String SERVER_PROP_PREFIX = ToolGroupServer.class.getName();
	public static final String SERVER_NAME_PROP = SERVER_PROP_PREFIX + ".serverName";
	public static final String SERVER_VERSION_PROP = SERVER_PROP_PREFIX + ".serverVersion";
	public static final String SERVER_TRANSPORT_PROP = SERVER_PROP_PREFIX + ".serverTransport";
	public static final String SERVER_CAPABILITIES_PROP = SERVER_PROP_PREFIX + ".serverCapabilities";
	public static final String SERVER_TOOL_GROUP_PROVIDER = SERVER_PROP_PREFIX + ".serverToolGroupProvider";
	public static final String SERVER_GENERATE_OUTPUT_SCHEMA = SERVER_PROP_PREFIX + ".outputSchemaGenerator";

	default void removeTools(List<Tool> tools) {
		tools.forEach(tn -> {
			removeTool(tn);
		});
	}

	void removeTool(Tool tool);

	List<Tool> addToolGroup(Object instance, Class<?>... classes);

	void addTool(Tool tool, Method toolMethod, Object instance);
}
