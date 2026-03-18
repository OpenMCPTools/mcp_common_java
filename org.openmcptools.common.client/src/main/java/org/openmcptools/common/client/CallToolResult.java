package org.openmcptools.common.client;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openmcptools.common.model.Content;

public class CallToolResult {

	private final List<Content> content;
	private final Boolean isError;
	private final Object structuredContent;
	private final Map<String, Object> meta;

	public CallToolResult(List<Content> content, Boolean isError, Object structuredContent, Map<String, Object> meta) {
		super();
		Objects.requireNonNull(content, "content must not be null");
		this.content = content;
		this.isError = isError;
		this.structuredContent = structuredContent;
		this.meta = meta;
	}

	public CallToolResult(List<Content> content, Boolean isError, Object structuredContent) {
		this(content, isError, structuredContent, null);
	}

	public List<Content> getContent() {
		return content;
	}

	public Boolean getIsError() {
		return isError;
	}

	public Object getStructuredContent() {
		return structuredContent;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "CallToolResult [content=" + content + ", isError=" + isError + ", structuredContent="
				+ structuredContent + ", meta=" + meta + "]";
	}

}
