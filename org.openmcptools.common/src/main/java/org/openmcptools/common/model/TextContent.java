package org.openmcptools.common.model;

import java.util.Map;

public class TextContent implements Content {

	private final ContentType type = ContentType.TEXT;
	private final String text;
	private final Annotations annotations;
	private final Map<String, Object> meta;

	public TextContent(String text, Annotations annotations, Map<String, Object> meta) {
		this.annotations = annotations;
		this.text = text;
		this.meta = meta;
	}

	public TextContent(String text, Annotations annotations) {
		this(text, annotations, null);
	}

	public TextContent(String text) {
		this(text, null, null);
	}

	public ContentType getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	public Annotations getAnnotations() {
		return annotations;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "TextContent [text=" + text + ", annotations=" + annotations + ", meta=" + meta + "]";
	}

}
