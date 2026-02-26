package org.openmcptools.common.model;

import java.util.Map;

public class BlobResourceContents extends ResourceContent {

	private final String blob;

	public BlobResourceContents(String uri, String mimeType, String blob, Map<String, Object> meta) {
		super(uri, mimeType, meta);
		this.blob = blob;
	}

	public BlobResourceContents(String uri, String mimeType, String blob) {
		super(uri, mimeType);
		this.blob = blob;
	}

	public String getBlob() {
		return blob;
	}

	@Override
	public String toString() {
		return "BlobResourceContents [getText()=" + getBlob() + ", getType()=" + getType() + ", getUri()=" + getUri()
				+ ", getMimeType()=" + getMimeType() + ", getMeta()=" + getMeta() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
