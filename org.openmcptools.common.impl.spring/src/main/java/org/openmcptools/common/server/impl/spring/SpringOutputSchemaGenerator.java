package org.openmcptools.common.server.impl.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import org.openmcptools.common.server.toolgroup.AbstractOutputSchemaGenerator;
import org.springaicommunity.mcp.method.tool.ReactiveUtils;
import org.springaicommunity.mcp.method.tool.utils.ClassUtils;
import org.springaicommunity.mcp.method.tool.utils.JsonSchemaGenerator;

import io.modelcontextprotocol.spec.McpSchema.CallToolResult;

public class SpringOutputSchemaGenerator {

	public static class Sync extends AbstractOutputSchemaGenerator<Type> {

		@Override
		protected Type getReturnTypeArgument(Method mcpToolMethod) {
			Class<?> methodReturnType = mcpToolMethod.getReturnType();
			if (methodReturnType != null && methodReturnType != CallToolResult.class && methodReturnType != Void.class
					&& methodReturnType != void.class && !ClassUtils.isPrimitiveOrWrapper(methodReturnType)
					&& !ClassUtils.isSimpleValueType(methodReturnType)) {
				return mcpToolMethod.getGenericReturnType();
			}
			return null;
		}

		@Override
		protected String generateOutputSchema(Type returnTypeArgument) {
			return JsonSchemaGenerator.generateFromType(returnTypeArgument);
		}

	}

	public static class Async extends AbstractOutputSchemaGenerator<Class<?>> {

		@Override
		protected Class<?> getReturnTypeArgument(Method mcpToolMethod) {
			if (!ReactiveUtils.isReactiveReturnTypeOfVoid(mcpToolMethod)
					&& !ReactiveUtils.isReactiveReturnTypeOfCallToolResult(mcpToolMethod)) {

				Optional<Type> opt = ReactiveUtils.getReactiveReturnTypeArgument(mcpToolMethod);
				if (opt.isPresent()) {
					Type typeArgument = opt.get();
					Class<?> methodReturnType = typeArgument instanceof Class<?> ? (Class<?>) typeArgument : null;
					if (!ClassUtils.isPrimitiveOrWrapper(methodReturnType)
							&& !ClassUtils.isSimpleValueType(methodReturnType)) {
						return (Class<?>) typeArgument;
					}
				}
			}
			return null;
		}

		@Override
		protected String generateOutputSchema(Class<?> returnTypeArgument) {
			return JsonSchemaGenerator.generateFromType(returnTypeArgument);
		}

	}

}
