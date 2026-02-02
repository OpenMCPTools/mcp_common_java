package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface ToolNodeConverter<ToolType> {

	default List<ToolType> convertFromToolNodes(List<Tool> toolNodes) {
		return toolNodes.stream().map(tn -> {
			return convertFromToolNode(tn);
		}).filter(Objects::nonNull).toList();
	}

	ToolType convertFromToolNode(Tool toolNode);

	default List<Tool> convertToToolNodes(List<ToolType> tools) {
		return tools.stream().map(t -> {
			return convertToToolNode(t);
		}).filter(Objects::nonNull).toList();
	}

	Tool convertToToolNode(ToolType tool);

}
