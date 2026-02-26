package org.openmcptools.common.model;

import java.util.Map;

public class ResourceContent implements Content {

	private final ContentType type = ContentType.RESOURCE;

	protected final String uri;
	private final String mimeType;
	private final Map<String, Object> meta;

	public ResourceContent(String uri, String mimeType, Map<String, Object> meta) {
		super();
		this.uri = uri;
		this.mimeType = mimeType;
		this.meta = meta;
	}

	public ResourceContent(String uri, String mimeType) {
		this(uri, mimeType, null);
	}

	public ContentType getType() {
		return type;
	}

	public String getUri() {
		return uri;
	}

	public String getMimeType() {
		return mimeType;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "ResourceContent [getType()=" + getType() + ", getUri()=" + getUri() + ", getMimeType()=" + getMimeType()
				+ ", getMeta()=" + getMeta() + "]";
	}

}
