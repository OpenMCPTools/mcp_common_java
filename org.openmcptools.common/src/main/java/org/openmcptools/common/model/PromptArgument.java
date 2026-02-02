package org.openmcptools.common.model;

public class PromptArgument extends AbstractBase {

	protected boolean required;

	public PromptArgument(String name) {
		super(name);
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isRequired() {
		return this.required;
	}

	@Override
	public String toString() {
		return "PromptArgumentNode [required=" + required + ", name=" + name + ", title=" + title + ", description="
				+ description + ", meta=" + meta + "]";
	}

	@Override
	public String getFullyQualifiedName() {
		return name;
	}

}
