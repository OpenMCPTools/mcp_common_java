package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface PromptNodeConverter<PromptType> {

	default List<PromptType> convertFromPromptNodes(List<Prompt> promptNodes) {
		return promptNodes.stream().map(pn -> {
			return convertFromPromptNode(pn);
		}).filter(Objects::nonNull).toList();
	}

	PromptType convertFromPromptNode(Prompt promptNode);

	default List<Prompt> convertToPromptNodes(List<PromptType> prompts) {
		return prompts.stream().map(p -> {
			return convertToPromptNode(p);
		}).filter(Objects::nonNull).toList();
	}

	Prompt convertToPromptNode(PromptType prompt);

}
