package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface ResourceNodeConverter<ResourceType> {

	default List<ResourceType> convertFromResourceNodes(List<Resource> resourceNodes) {
		return resourceNodes.stream().map(rn -> {
			return convertFromResourceNode(rn);
		}).filter(Objects::nonNull).toList();
	}

	ResourceType convertFromResourceNode(Resource resourceNode);

	default List<Resource> convertToResourceNodes(List<ResourceType> resources) {
		return resources.stream().map(rn -> {
			return convertToResourceNode(rn);
		}).filter(Objects::nonNull).toList();
	}

	Resource convertToResourceNode(ResourceType resource);

}
