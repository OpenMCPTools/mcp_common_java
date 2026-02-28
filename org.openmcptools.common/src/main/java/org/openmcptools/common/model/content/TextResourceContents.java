package org.openmcptools.common.model.content;

import java.util.Map;

public class TextResourceContents implements ResourceContents {

	private final ContentType type = ContentType.RESOURCE;
	private final String uri;
	private final String mimeType;
	private final String text;
	private final Map<String, Object> meta;

	public TextResourceContents(String uri, String mimeType, String text, Map<String, Object> meta) {
		this.uri = uri;
		this.mimeType = mimeType;
		this.text = text;
		this.meta = meta;
	}

	public TextResourceContents(String uri, String mimeType, String text) {
		this(uri, mimeType, text, null);
	}

	public String getText() {
		return text;
	}

	@Override
	public ContentType getType() {
		return type;
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

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
