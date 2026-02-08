package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface GroupConverter<GroupType> {

	default List<GroupType> convertFromGroups(List<Group> groups) {
		return groups.stream().map(gn -> {
			return convertFromGroup(gn);
		}).filter(Objects::nonNull).toList();
	}

	GroupType convertFromGroup(Group group);

	default List<Group> convertToGroups(List<GroupType> groups) {
		return groups.stream().map(g -> {
			return convertToGroup(g);
		}).filter(Objects::nonNull).toList();
	}

	Group convertToGroup(GroupType group);

}
