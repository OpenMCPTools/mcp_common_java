package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents resource contents where the data is in plain text format.
 */
public class TextResourceContents implements ResourceContents {

	private final ContentType type = ContentType.RESOURCE;

	private final String uri;

	private final String mimeType;

	private final String text;

	private final Map<String, Object> meta;

	/**
	 * Constructs a new TextResourceContents with all fields.
	 *
	 * @param uri      the resource URI
	 * @param mimeType the MIME type (e.g., "text/plain")
	 * @param text     the actual text content
	 * @param meta     metadata associated with the content
	 */
	public TextResourceContents(String uri, String mimeType, String text, Map<String, Object> meta) {
		this.uri = uri;
		this.mimeType = mimeType;
		this.text = text;
		this.meta = meta;
	}

	/**
	 * Constructs a new TextResourceContents with URI, MIME type, and text.
	 *
	 * @param uri      the resource URI
	 * @param mimeType the MIME type
	 * @param text     the actual text content
	 */
	public TextResourceContents(String uri, String mimeType, String text) {
		this(uri, mimeType, text, null);
	}

	/**
	 * Returns the text content of the resource.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContentType getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUri() {
		return uri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "TextResourceContents [type=" + type + ", uri=" + uri + ", mimeType=" + mimeType + ", text=" + text
				+ ", meta=" + meta + "]";
	}

}
