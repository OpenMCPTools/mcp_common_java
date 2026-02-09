package org.openmcptools.common.server;

import java.lang.reflect.Method;

public interface InputSchemaGenerator {

	String generateInputSchema(Method mcpToolMethod);
}
