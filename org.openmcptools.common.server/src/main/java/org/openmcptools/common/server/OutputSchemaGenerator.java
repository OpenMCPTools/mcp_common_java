package org.openmcptools.common.server;

import java.lang.reflect.Method;

public interface OutputSchemaGenerator {

	String generateOutputSchema(Method mcpToolMethod);

}
