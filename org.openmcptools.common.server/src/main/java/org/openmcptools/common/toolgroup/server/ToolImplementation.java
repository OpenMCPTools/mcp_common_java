package org.openmcptools.common.toolgroup.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.openmcptools.common.model.Tool;

public class ToolImplementation {

	private final Tool tool;
	private final Object instance;
	private final Method method;
	private final boolean outputSchema;

	public ToolImplementation(Tool tool, Object instance, Method method, boolean outputSchema) {
		Objects.requireNonNull(tool, "tool must not be null");
		this.tool = tool;
		Objects.requireNonNull(method, "method must not be null");
		this.method = method;
		Objects.requireNonNull(instance, "instance must not be null");
		this.instance = instance;
		this.outputSchema = outputSchema;
	}

	public ToolImplementation(Tool tool, Object instance, Method method) {
		Objects.requireNonNull(tool, "tool must not be null");
		this.tool = tool;
		Objects.requireNonNull(method, "method must not be null");
		this.method = method;
		Objects.requireNonNull(instance, "instance must not be null");
		this.instance = instance;
		this.outputSchema = true;
	}

	public Object invoke(Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.method.invoke(instance, args);
	}

	public Tool getTool() {
		return tool;
	}

	public Method getMethod() {
		return method;
	}

	public Object getInstance() {
		return instance;
	}

	public boolean getOutputSchema() {
		return outputSchema;
	}
}
