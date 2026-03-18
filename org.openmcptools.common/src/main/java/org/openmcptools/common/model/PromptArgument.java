package org.openmcptools.common.model;

/**
 * Represents an argument for a {@link Prompt}.
 * Defines whether the argument is required and provides metadata inherited from {@link AbstractBase}.
 */
public class PromptArgument extends AbstractBase {

	/** Indicates whether this argument is mandatory. */
	protected boolean required = false;

	/**
	 * Constructs a new PromptArgument with the specified name.
	 * 
	 * @param name the name of the argument
	 */
	public PromptArgument(String name) {
		super(name);
	}

	/**
	 * Sets whether this argument is required.
	 * 
	 * @param required {@code true} if the argument is required, {@code false} otherwise
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Checks if this argument is required.
	 * 
	 * @return {@code true} if required, {@code false} otherwise
	 */
	public boolean isRequired() {
		return this.required;
	}

	@Override
	public String toString() {
		return "PromptArgument [required=" + required + ", name=" + name + ", title=" + title + ", description="
				+ description + ", meta=" + meta + "]";
	}

	/**
	 * Gets the fully qualified name of the argument, which is simply the name.
	 * 
	 * @return the name of the argument
	 */
	@Override
	public String getFullyQualifiedName() {
		return name;
	}
}
