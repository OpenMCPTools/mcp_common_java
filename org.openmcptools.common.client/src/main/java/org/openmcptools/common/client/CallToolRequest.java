package org.openmcptools.common.client;

import java.util.Map;
import java.util.Objects;

public class CallToolRequest {

	private final String toolName;
	private final Map<String, Object> arguments;
	private final Map<String, Object> meta;

	public CallToolRequest(String toolName, Map<String, Object> arguments, Map<String, Object> meta) {
		super();
		Objects.requireNonNull(toolName, "toolName must not be null");
		this.toolName = toolName;
		this.arguments = arguments;
		this.meta = meta;
	}

	public CallToolRequest(String toolName, Map<String, Object> arguments) {
		this(toolName, arguments, null);
	}

	public String getToolName() {
		return toolName;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "CallToolRequest [toolName=" + toolName + ", arguments=" + arguments + ", meta=" + meta + "]";
	}

}
