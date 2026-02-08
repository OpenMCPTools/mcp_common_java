package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface ToolAnnotationsConverter<ToolAnnotationsType> {

	default List<ToolAnnotationsType> convertFromToolAnnotations(List<ToolAnnotations> toolAnnotations) {
		return toolAnnotations.stream().map(tn -> {
			return convertFromToolAnnotations(tn);
		}).filter(Objects::nonNull).toList();
	}

	ToolAnnotationsType convertFromToolAnnotations(ToolAnnotations tool);

	default List<ToolAnnotations> convertToToolAnnotations(List<ToolAnnotationsType> toolAnnotations) {
		return toolAnnotations.stream().map(t -> {
			return convertToToolAnnotations(t);
		}).filter(Objects::nonNull).toList();
	}

	ToolAnnotations convertToToolAnnotations(ToolAnnotationsType toolAnnotations);

}
