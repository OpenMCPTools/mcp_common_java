package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface ResourceConverter<ResourceType> {

	default List<ResourceType> convertFromResources(List<Resource> resources) {
		return resources.stream().map(rn -> {
			return convertFromResource(rn);
		}).filter(Objects::nonNull).toList();
	}

	ResourceType convertFromResource(Resource resource);

	default List<Resource> convertToResources(List<ResourceType> resources) {
		return resources.stream().map(rn -> {
			return convertToResource(rn);
		}).filter(Objects::nonNull).toList();
	}

	Resource convertToResource(ResourceType resource);

}
