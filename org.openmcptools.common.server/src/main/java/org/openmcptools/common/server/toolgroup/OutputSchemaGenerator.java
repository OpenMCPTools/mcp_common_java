package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;

public interface OutputSchemaGenerator {

	String generateOutputSchema(Method mcpToolMethod);

}
