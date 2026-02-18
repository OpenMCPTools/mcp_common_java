package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface ToolConverter<ProviderToolType> {

	default List<ProviderToolType> convertFromTools(List<Tool> tools) {
		return tools.stream().map(tn -> {
			return convertFromTool(tn);
		}).filter(Objects::nonNull).toList();
	}

	ProviderToolType convertFromTool(Tool tool);

	default List<Tool> convertToTools(List<ProviderToolType> addedTools) {
		return addedTools.stream().map(t -> {
			return convertToTool(t);
		}).filter(Objects::nonNull).toList();
	}

	Tool convertToTool(ProviderToolType tool);

}
