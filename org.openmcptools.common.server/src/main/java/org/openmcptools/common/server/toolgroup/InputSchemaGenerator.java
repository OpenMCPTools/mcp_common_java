package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;

public interface InputSchemaGenerator {

	String generateInputSchema(Method mcpToolMethod);
}
