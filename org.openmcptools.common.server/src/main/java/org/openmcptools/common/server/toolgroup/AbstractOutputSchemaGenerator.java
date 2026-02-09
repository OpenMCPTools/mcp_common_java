package org.openmcptools.common.server.toolgroup;

import java.lang.reflect.Method;

import org.openmcptools.common.server.OutputSchemaGenerator;

public abstract class AbstractOutputSchemaGenerator<TypeType> implements OutputSchemaGenerator {

	@Override
	public String generateOutputSchema(Method mcpToolMethod) {
		TypeType returnTypeArgument = getReturnTypeArgument(mcpToolMethod);
		if (returnTypeArgument != null) {
			return generateOutputSchema(returnTypeArgument);
		}
		return null;
	}

	protected abstract TypeType getReturnTypeArgument(Method mcpToolMethod);

	protected abstract String generateOutputSchema(TypeType returnTypeArgument);
}
