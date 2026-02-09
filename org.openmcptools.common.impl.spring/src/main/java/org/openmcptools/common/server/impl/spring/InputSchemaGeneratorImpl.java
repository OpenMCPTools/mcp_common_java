package org.openmcptools.common.server.impl.spring;

import java.lang.reflect.Method;

import org.openmcptools.common.server.InputSchemaGenerator;
import org.springaicommunity.mcp.method.tool.utils.JsonSchemaGenerator;

public class InputSchemaGeneratorImpl implements InputSchemaGenerator {

	public String generateInputSchema(Method mcpToolMethod) {
		return JsonSchemaGenerator.generateForMethodInput(mcpToolMethod);
	}

}
