package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;

import org.openmcptools.annotation.McpTool;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Tool;

public interface ToolProvider {

	public static final String SEPARATOR = ".";

	Tool getTool(McpTool mcpToolAnnotation, Method mcpToolMethod, Group group, boolean generateOutputSchema);

}
