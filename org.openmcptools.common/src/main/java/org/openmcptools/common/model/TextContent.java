package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents a piece of text content in an MCP message or resource.
 */
public class TextContent implements Content {

	private final ContentType type = ContentType.TEXT;

	private final String text;

	private final Annotations annotations;

	private final Map<String, Object> meta;

	/**
	 * Constructs a new TextContent with text, annotations, and metadata.
	 *
	 * @param text        the actual text content
	 * @param annotations annotations for the text
	 * @param meta        metadata associated with the content
	 */
	public TextContent(String text, Annotations annotations, Map<String, Object> meta) {
		this.annotations = annotations;
		this.text = text;
		this.meta = meta;
	}

	/**
	 * Constructs a new TextContent with text and annotations.
	 *
	 * @param text        the actual text content
	 * @param annotations annotations for the text
	 */
	public TextContent(String text, Annotations annotations) {
		this(text, annotations, null);
	}

	/**
	 * Constructs a new TextContent with text only.
	 *
	 * @param text the actual text content
	 */
	public TextContent(String text) {
		this(text, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public ContentType getType() {
		return type;
	}

	/**
	 * Returns the text content.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Returns the annotations for this text content.
	 *
	 * @return the annotations
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * Returns the metadata for this text content.
	 *
	 * @return the metadata map
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "TextContent [text=" + text + ", annotations=" + annotations + ", meta=" + meta + "]";
	}

}
