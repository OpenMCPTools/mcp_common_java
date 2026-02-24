package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbstractLeaf extends AbstractBase {

	protected AbstractLeaf(String name) {
		super(name);
	}

	protected AbstractLeaf(String name, String nameSeparator) {
		super(name, nameSeparator);
	}

	protected List<Group> parentGroups = new CopyOnWriteArrayList<Group>();

	public boolean addParentGroup(Group parentGroup) {
		Objects.requireNonNull(parentGroup, "parentGroup must not be null");
		return parentGroups.add(parentGroup);
	}

	public boolean removeParentGroup(Group parentGroup) {
		return parentGroups.remove(parentGroup);
	}

	public List<Group> getParentGroups() {
		return this.parentGroups;
	}

	public List<Group> getParentGroupRoots() {
		return this.parentGroups.stream().map(Group::getRoot).toList();
	}

	protected String getFirstParentName() {
		return this.parentGroups.size() > 0 ? this.parentGroups.get(0).getFullyQualifiedName() : null;
	}

	@Override
	public String getFullyQualifiedName() {
		String firstParentName = getFirstParentName();
		return firstParentName == null ? getName() : firstParentName + this.nameSeparator + this.name;
	}

}
