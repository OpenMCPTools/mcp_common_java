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
	protected int primaryParentGroupIndex = -1;

	public boolean addParentGroup(Group parentGroup) {
		Objects.requireNonNull(parentGroup, "parentGroup must not be null");
		if (parentGroups.contains(parentGroup)) {
			return false;
		}
		boolean addResult = parentGroups.add(parentGroup);
		if (addResult && primaryParentGroupIndex == -1) {
			primaryParentGroupIndex = 0;
		}
		return addResult;
	}

	public boolean removeParentGroup(Group parentGroup) {
		int currentIndex = parentGroups.indexOf(parentGroup);
		if (currentIndex == -1) {
			return false;
		}
		if (currentIndex == this.primaryParentGroupIndex) {
			boolean result = parentGroups.remove(currentIndex) != null;
			this.primaryParentGroupIndex = -1;
			return result;
		}
		return parentGroups.remove(parentGroup);
	}

	public List<Group> getParentGroups() {
		return this.parentGroups;
	}

	public List<Group> getParentGroupRoots() {
		return this.parentGroups.stream().map(Group::getRoot).toList();
	}

	protected String getPrimaryParentName() {
		return this.primaryParentGroupIndex > -1
				? this.parentGroups.get(primaryParentGroupIndex).getFullyQualifiedName()
				: null;
	}

	@Override
	public String getFullyQualifiedName() {
		String firstParentName = getPrimaryParentName();
		return firstParentName == null ? getName() : firstParentName + this.nameSeparator + this.name;
	}

}
