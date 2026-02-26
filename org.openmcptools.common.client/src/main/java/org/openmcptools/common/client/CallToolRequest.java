package org.openmcptools.common.client;

import java.util.Map;
import java.util.Objects;

import org.openmcptools.common.model.Tool;

public class CallToolRequest {

	private final Tool tool;
	private final Map<String, Object> arguments;
	private final Map<String, Object> meta;

	public CallToolRequest(Tool tool, Map<String, Object> arguments, Map<String, Object> meta) {
		super();
		Objects.requireNonNull(tool, "tool must not be null");
		this.tool = tool;
		this.arguments = arguments;
		this.meta = meta;
	}

	public CallToolRequest(Tool tool, Map<String, Object> arguments) {
		this(tool, arguments, null);
	}

	public Tool getTool() {
		return tool;
	}

	public Map<String, Object> getArguments() {
		return arguments;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "CallToolRequest [tool=" + tool + ", arguments=" + arguments + ", meta=" + meta + "]";
	}

}
