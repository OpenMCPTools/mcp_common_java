package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract class for leaf entities in the MCP hierarchy.
 * Leaf entities can belong to multiple parent groups.
 */
public class AbstractLeaf extends AbstractBase {

	/**
	 * List of parent groups this leaf belongs to.
	 */
	protected List<Group> parentGroups = new CopyOnWriteArrayList<Group>();

	/**
	 * Index of the primary parent group in the list.
	 */
	protected int primaryParentGroupIndex = -1;

	/**
	 * Constructs an AbstractLeaf with the given name.
	 * 
	 * @param name the name of the leaf
	 */
	protected AbstractLeaf(String name) {
		super(name);
	}

	/**
	 * Constructs an AbstractLeaf with the given name and separator.
	 * 
	 * @param name the name of the leaf
	 * @param nameSeparator the separator for the fully qualified name
	 */
	protected AbstractLeaf(String name, String nameSeparator) {
		super(name, nameSeparator);
	}

	/**
	 * Adds a parent group to this leaf.
	 * 
	 * @param parentGroup the group to add
	 * @return true if added, false if it already exists
	 * @throws NullPointerException if parentGroup is null
	 */
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

	/**
	 * Removes a parent group from this leaf.
	 * 
	 * @param parentGroup the group to remove
	 * @return true if removed, false otherwise
	 */
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

	/**
	 * Returns the list of parent groups.
	 * 
	 * @return the parent groups
	 */
	public List<Group> getParentGroups() {
		return this.parentGroups;
	}

	/**
	 * Returns the root groups of all parent groups.
	 * 
	 * @return the root parent groups
	 */
	public List<Group> getParentGroupRoots() {
		return this.parentGroups.stream().map(Group::getRoot).toList();
	}

	/**
	 * Returns the fully qualified name of the primary parent.
	 * 
	 * @return the primary parent name or null
	 */
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

