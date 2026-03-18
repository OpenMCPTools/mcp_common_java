package org.openmcptools.common.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a logical grouping of MCP entities like tools, prompts, resources, and other groups.
 * Extends {@link AbstractBase} to inherit common properties like name, title, and description.
 */
public class Group extends AbstractBase {

	/**
	 * The parent group of this group. Null if this is a root group.
	 */
	protected Group parent;

	/**
	 * List of nested child groups.
	 */
	protected final List<Group> childGroups;

	/**
	 * List of tools contained within this group.
	 */
	protected final List<Tool> childTools;

	/**
	 * List of prompts contained within this group.
	 */
	protected final List<Prompt> childPrompts;

	/**
	 * List of resources contained within this group.
	 */
	protected final List<Resource> childResources;

	/**
	 * Constructs a new Group with the given name and default separator.
	 *
	 * @param name the name of the group
	 */
	public Group(String name) {
		this(name, DEFAULT_SEPARATOR);
	}

	/**
	 * Constructs a new Group with the given name and name separator.
	 *
	 * @param name          the name of the group
	 * @param nameSeparator the separator used for fully qualified names
	 */
	public Group(String name, String nameSeparator) {
		super(name, nameSeparator);
		this.childGroups = new CopyOnWriteArrayList<Group>();
		this.childTools = new CopyOnWriteArrayList<Tool>();
		this.childPrompts = new CopyOnWriteArrayList<Prompt>();
		this.childResources = new CopyOnWriteArrayList<Resource>();
	}

	/**
	 * Creates a new Builder for creating a Group.
	 *
	 * @param name the name of the group
	 * @return a new Group builder instance
	 */
	public static Builder builder(String name) {
		return new Builder(name);
	}

	/**
	 * Builder class for creating {@link Group} instances.
	 */
	public static class Builder {

		private String name;
		private String title;
		private String description;
		private Group parent;
		private Map<String, Object> meta;

		/**
		 * Constructs a Builder with the specified name.
		 *
		 * @param name the name of the group
		 */
		public Builder(String name) {
			this.name = name;
		}

		/**
		 * Sets the title for the group.
		 *
		 * @param title the title to set
		 * @return this builder instance
		 */
		public Builder title(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Sets the description for the group.
		 *
		 * @param description the description to set
		 * @return this builder instance
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets the parent group.
		 *
		 * @param parent the parent group to set
		 * @return this builder instance
		 */
		public Builder parent(Group parent) {
			this.parent = parent;
			return this;
		}

		/**
		 * Sets the metadata for the group.
		 *
		 * @param meta the metadata map to set
		 * @return this builder instance
		 */
		public Builder meta(Map<String, Object> meta) {
			this.meta = meta;
			return this;
		}

		/**
		 * Builds and returns a new {@link Group} instance.
		 *
		 * @return a configured Group
		 */
		public Group build() {
			Group result = new Group(name);
			result.setTitle(title);
			result.setDescription(description);
			result.setMeta(meta);
			result.setParent(parent);
			return result;
		}

	}

	/**
	 * Gets the parent group.
	 *
	 * @return the parent group, or null if none
	 */
	public Group getParent() {
		return this.parent;
	}

	/**
	 * Sets the parent group for this group.
	 *
	 * @param parent the parent group to set
	 */
	public void setParent(Group parent) {
		this.parent = parent;
	}

	/**
	 * Traverses up the hierarchy to find the root group.
	 *
	 * @return the root group of the hierarchy
	 */
	public Group getRoot() {
		Group parent = this.parent;
		if (parent == null) {
			return this;
		} else {
			return parent.getRoot();
		}
	}

	/**
	 * Checks if this group is a root group (has no parent).
	 *
	 * @return true if this is a root group, false otherwise
	 */
	public boolean isRoot() {
		return this.parent == null;
	}

	/**
	 * Adds a child group to this group.
	 *
	 * @param childGroup the group to add as a child
	 * @return true if the group was added, false if it was already present
	 */
	public boolean addChildGroup(Group childGroup) {
		if (childGroups.contains(childGroup)) {
			return false;
		}
		boolean added = childGroups.add(childGroup);
		if (added) {
			childGroup.parent = this;
			return true;
		}
		return false;
	}

	/**
	 * Removes a child group from this group.
	 *
	 * @param childGroup the group to remove
	 * @return true if the group was removed, false otherwise
	 */
	public boolean removeChildGroup(Group childGroup) {
		if (childGroups.remove(childGroup)) {
			childGroup.parent = null;
			return true;
		}
		return false;
	}

	/**
	 * Returns a list of all child groups.
	 *
	 * @return the list of child groups
	 */
	public List<Group> getChildrenGroups() {
		return this.childGroups;
	}

	/**
	 * Adds a tool to this group.
	 *
	 * @param childTool the tool to add
	 * @return true if the tool was added, false if it was already present
	 */
	public boolean addChildTool(Tool childTool) {
		if (this.childTools.contains(childTool)) {
			return false;
		}
		boolean added = childTools.add(childTool);
		if (added) {
			childTool.addParentGroup(this);
			return true;
		}
		return false;
	}

	/**
	 * Removes a tool from this group.
	 *
	 * @param childTool the tool to remove
	 * @return true if the tool was removed, false otherwise
	 */
	public boolean removeChildTool(Tool childTool) {
		boolean removed = childTools.remove(childTool);
		if (removed) {
			childTool.removeParentGroup(this);
			return true;
		}
		return false;
	}

	/**
	 * Returns a list of all tools in this group.
	 *
	 * @return the list of child tools
	 */
	public List<Tool> getChildrenTools() {
		return this.childTools;
	}

	/**
	 * Adds a prompt to this group.
	 *
	 * @param childPrompt the prompt to add
	 * @return true if the prompt was added, false if it was already present
	 */
	public boolean addChildPrompt(Prompt childPrompt) {
		if (this.childPrompts.contains(childPrompt)) {
			return false;
		}
		boolean added = childPrompts.add(childPrompt);
		if (added) {
			childPrompt.addParentGroup(this);
			return true;
		}
		return false;
	}

	/**
	 * Removes a prompt from this group.
	 *
	 * @param childPrompt the prompt to remove
	 * @return true if the prompt was removed, false otherwise
	 */
	public boolean removeChildPrompt(Prompt childPrompt) {
		boolean removed = childPrompts.remove(childPrompt);
		if (removed) {
			childPrompt.removeParentGroup(this);
			return true;
		}
		return false;
	}

	/**
	 * Returns a list of all resources in this group.
	 *
	 * @return the list of child resources
	 */
	public List<Resource> getChildResources() {
		return this.childResources;
	}

	/**
	 * Adds a resource to this group.
	 *
	 * @param childResource the resource to add
	 * @return true if the resource was added, false if it was already present
	 */
	public boolean addChildResource(Resource childResource) {
		if (this.childResources.contains(childResource)) {
			return false;
		}
		boolean added = childResources.add(childResource);
		if (added) {
			childResource.addParentGroup(this);
			return true;
		}
		return false;
	}

	/**
	 * Removes a resource from this group.
	 *
	 * @param childResource the resource to remove
	 * @return true if the resource was removed, false otherwise
	 */
	public boolean removeChildResource(Resource childResource) {
		boolean removed = childResources.remove(childResource);
		if (removed) {
			childResource.removeParentGroup(this);
			return true;
		}
		return false;
	}

	/**
	 * Returns a list of all prompts in this group.
	 *
	 * @return the list of child prompts
	 */
	public List<Prompt> getChildPrompts() {
		return this.childPrompts;
	}

	/**
	 * Recursively builds the fully qualified name by appending parents' names.
	 *
	 * @param sb the string buffer to build the name in
	 * @param tg the group to get the name for
	 * @return the fully qualified name string
	 */
	protected String getFullyQualifiedName(StringBuffer sb, Group tg) {
		Group parent = tg.getParent();
		if (parent != null) {
			String parentName = getFullyQualifiedName(sb, parent);
			return new StringBuffer(parentName).append(this.nameSeparator).append(tg.getName()).toString();
		}
		return tg.getName();
	}

	/**
	 * Gets the fully qualified name of this group.
	 *
	 * @return the fully qualified name string
	 */
	@Override
	public String getFullyQualifiedName() {
		return getFullyQualifiedName(new StringBuffer(), this);
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", fqName=" + getFullyQualifiedName() + ", isRoot=" + isRoot() + ", title="
				+ title + ", description=" + description + ", meta=" + meta + ", childGroups=" + childGroups
				+ ", childTools=" + childTools + ", childPrompts=" + childPrompts + "]";
	}

}
