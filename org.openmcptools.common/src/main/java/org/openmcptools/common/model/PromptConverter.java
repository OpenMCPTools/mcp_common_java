package org.openmcptools.common.model;

import java.util.List;
import java.util.Objects;

public interface PromptConverter<PromptType> {

	default List<PromptType> convertFromPrompts(List<Prompt> prompts) {
		return prompts.stream().map(pn -> {
			return convertFromPrompt(pn);
		}).filter(Objects::nonNull).toList();
	}

	PromptType convertFromPrompt(Prompt prompt);

	default List<Prompt> convertToPrompts(List<PromptType> prompts) {
		return prompts.stream().map(p -> {
			return convertToPrompt(p);
		}).filter(Objects::nonNull).toList();
	}

	Prompt convertToPrompt(PromptType prompt);

}
