package org.openmcptools.common.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class Group extends AbstractBase {

	protected Group parent;

	protected final List<Group> childGroups;

	protected final List<Tool> childTools;

	protected final List<Prompt> childPrompts;

	protected final List<Resource> childResources;

	protected Function<Group, ?> converter;

	public Group(String name) {
		this(name, DEFAULT_SEPARATOR);
	}

	public Group(String name, String nameSeparator) {
		super(name, nameSeparator);
		this.childGroups = new CopyOnWriteArrayList<Group>();
		this.childTools = new CopyOnWriteArrayList<Tool>();
		this.childPrompts = new CopyOnWriteArrayList<Prompt>();
		this.childResources = new CopyOnWriteArrayList<Resource>();
	}

	public Group getParent() {
		return this.parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}

	public Group getRoot() {
		Group parent = this.parent;
		if (parent == null) {
			return this;
		} else {
			return parent.getRoot();
		}
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public boolean addChildGroup(Group childGroup) {
		boolean added = childGroups.add(childGroup);
		if (added) {
			childGroup.parent = this;
			return true;
		}
		return false;
	}

	public boolean removeChildGroup(Group childGroup) {
		if (childGroups.remove(childGroup)) {
			childGroup.parent = null;
			return true;
		}
		return false;
	}

	public List<Group> getChildrenGroups() {
		return this.childGroups;
	}

	public boolean addChildTool(Tool childTool) {
		boolean added = childTools.add(childTool);
		if (added) {
			childTool.addParentGroup(this);
			return true;
		}
		return false;
	}

	public boolean removeChildTool(Tool childTool) {
		boolean removed = childTools.remove(childTool);
		if (removed) {
			childTool.removeParentGroup(this);
			return true;
		}
		return false;
	}

	public List<Tool> getChildrenTools() {
		return this.childTools;
	}

	public boolean addChildPrompt(Prompt childPrompt) {
		boolean added = childPrompts.add(childPrompt);
		if (added) {
			childPrompt.addParentGroup(this);
			return true;
		}
		return false;
	}

	public boolean removeChildPrompt(Prompt childPrompt) {
		boolean removed = childPrompts.remove(childPrompt);
		if (removed) {
			childPrompt.removeParentGroup(this);
			return true;
		}
		return false;
	}

	public List<Resource> getChildrenResources() {
		return this.childResources;
	}

	public boolean addChildResource(Resource childResource) {
		boolean added = childResources.add(childResource);
		if (added) {
			childResource.addParentGroup(this);
			return true;
		}
		return false;
	}

	public boolean removeChildPrompt(Resource childResource) {
		boolean removed = childResources.remove(childResource);
		if (removed) {
			childResource.removeParentGroup(this);
			return true;
		}
		return false;
	}

	public List<Prompt> getChildrenPrompts() {
		return this.childPrompts;
	}

	protected String getFullyQualifiedName(StringBuffer sb, Group tg) {
		Group parent = tg.getParent();
		if (parent != null) {
			String parentName = getFullyQualifiedName(sb, parent);
			return new StringBuffer(parentName).append(this.nameSeparator).append(tg.getName()).toString();
		}
		return tg.getName();
	}

	@Override
	public String getFullyQualifiedName() {
		return getFullyQualifiedName(new StringBuffer(), this);
	}

	@Override
	public String toString() {
		return "GroupNode [name=" + name + ", fqName=" + getFullyQualifiedName() + ", isRoot=" + isRoot() + ", title="
				+ title + ", description=" + description + ", meta=" + meta + ", childGroups=" + childGroups
				+ ", childTools=" + childTools + ", childPrompts=" + childPrompts + "]";
	}

}
