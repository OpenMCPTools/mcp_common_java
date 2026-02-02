package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class Prompt extends AbstractLeaf {

	protected List<PromptArgument> promptArguments = new CopyOnWriteArrayList<PromptArgument>();

	public Prompt(String name) {
		super(name);
	}

	public List<PromptArgument> getPromptArguments() {
		return this.promptArguments;
	}

	public boolean addPromptArgument(PromptArgument promptArgument) {
		Objects.requireNonNull(promptArgument, "promptArgument must not be null");
		return promptArguments.add(promptArgument);
	}

	public boolean removeParentGroup(PromptArgument promptArgument) {
		return promptArguments.remove(promptArgument);
	}

	@Override
	public String toString() {
		return "PromptNode [promptArguments=" + promptArguments + ", name=" + name + ", fqName="
				+ getFullyQualifiedName() + ", title=" + title + ", description=" + description + ", meta=" + meta
				+ "]";
	}

}
