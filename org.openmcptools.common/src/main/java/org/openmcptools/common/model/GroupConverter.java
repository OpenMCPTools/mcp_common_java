package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface GroupConverter<GroupType> {

	default List<GroupType> convertFromGroupNodes(List<Group> groupNodes) {
		return groupNodes.stream().map(gn -> {
			return convertFromGroupNode(gn);
		}).filter(Objects::nonNull).toList();
	}

	GroupType convertFromGroupNode(Group groupNode);

	default List<Group> convertToGroupNodes(List<GroupType> groups) {
		return groups.stream().map(g -> {
			return convertToGroupNode(g);
		}).filter(Objects::nonNull).toList();
	}

	Group convertToGroupNode(GroupType group);

}
