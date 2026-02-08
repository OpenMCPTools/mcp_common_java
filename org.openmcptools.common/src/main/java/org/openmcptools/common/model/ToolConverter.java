package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface ToolConverter<ToolType> {

	default List<ToolType> convertFromTools(List<Tool> tools) {
		return tools.stream().map(tn -> {
			return convertFromTool(tn);
		}).filter(Objects::nonNull).toList();
	}

	ToolType convertFromTool(Tool tool);

	default List<Tool> convertToTools(List<ToolType> tools) {
		return tools.stream().map(t -> {
			return convertToTool(t);
		}).filter(Objects::nonNull).toList();
	}

	Tool convertToTool(ToolType tool);

}
