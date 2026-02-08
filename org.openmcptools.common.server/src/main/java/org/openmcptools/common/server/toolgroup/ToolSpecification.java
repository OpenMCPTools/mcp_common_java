package org.openmcptools.common.server.toolgroup;

import java.util.Objects;

import org.openmcptools.common.model.Tool;

public class ToolSpecification<SpecificationType> {

	protected final Tool tool;
	protected final SpecificationType specification;

	public ToolSpecification(Tool tool, SpecificationType specification) {
		Objects.requireNonNull(tool, "tool must not be null");
		this.tool = tool;
		this.specification = specification;
	}

	public Tool getTool() {
		return this.tool;
	}

	public SpecificationType getSpecification() {
		return this.specification;
	}
}
