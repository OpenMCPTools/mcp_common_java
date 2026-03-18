package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a prompt template that can be used by an AI model.
 * A prompt consists of a name and a set of arguments used to customize the output.
 */
public class Prompt extends AbstractLeaf {

	/** The list of arguments associated with this prompt. */
	protected List<PromptArgument> promptArguments = new CopyOnWriteArrayList<PromptArgument>();

	/**
	 * Constructs a new Prompt with the specified name.
	 * 
	 * @param name the name of the prompt
	 */
	public Prompt(String name) {
		super(name);
	}

	/**
	 * Gets the list of arguments defined for this prompt.
	 * 
	 * @return the list of {@link PromptArgument}s
	 */
	public List<PromptArgument> getPromptArguments() {
		return this.promptArguments;
	}

	/**
	 * Adds a new argument to the prompt if it doesn't already exist.
	 * 
	 * @param promptArgument the argument to add
	 * @return {@code true} if the argument was added, {@code false} if it was already present
	 * @throws NullPointerException if {@code promptArgument} is null
	 */
	public boolean addPromptArgument(PromptArgument promptArgument) {
		Objects.requireNonNull(promptArgument, "promptArgument must not be null");
		if (this.promptArguments.contains(promptArgument)) {
			return false;
		}
		return promptArguments.add(promptArgument);
	}

	/**
	 * Removes an argument from the prompt.
	 * 
	 * @param promptArgument the argument to remove
	 * @return {@code true} if the argument was removed, {@code false} otherwise
	 */
	public boolean removePromptArgument(PromptArgument promptArgument) {
		return promptArguments.remove(promptArgument);
	}

	@Override
	public String toString() {
		return "Prompt [promptArguments=" + promptArguments + ", name=" + name + ", fqName=" + getFullyQualifiedName()
				+ ", title=" + title + ", description=" + description + ", meta=" + meta + "]";
	}
}
