package org.openmcptools.common.model.content;

import java.util.Map;

public class BlobResourceContents implements ResourceContents {

	private final String blob;
	private final ContentType type = ContentType.RESOURCE;
	private final String uri;
	private final String mimeType;
	private final Map<String, Object> meta;

	public BlobResourceContents(String uri, String mimeType, String blob, Map<String, Object> meta) {
		this.uri = uri;
		this.mimeType = mimeType;
		this.blob = blob;
		this.meta = meta;
	}

	public BlobResourceContents(String uri, String mimeType, String blob) {
		this(uri, mimeType, blob, null);
	}

	public String getBlob() {
		return blob;
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
		return "BlobResourceContents [blob=" + blob + ", type=" + type + ", uri=" + uri + ", mimeType=" + mimeType
				+ ", meta=" + meta + "]";
	}

}
