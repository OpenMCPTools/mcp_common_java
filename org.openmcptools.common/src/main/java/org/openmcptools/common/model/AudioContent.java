package org.openmcptools.common.model;

import java.util.Map;

public class AudioContent implements Content {

	private final ContentType type = ContentType.TEXT;
	private final String data;
	private final Annotations annotations;
	private final String mimeType;
	private final Map<String, Object> meta;

	public AudioContent(String data, Annotations annotations, String mimeType, Map<String, Object> meta) {
		super();
		this.data = data;
		this.annotations = annotations;
		this.mimeType = mimeType;
		this.meta = meta;
	}

	public AudioContent(String data, Annotations annotations, String mimeType) {
		this(data, annotations, mimeType, null);
	}

	public AudioContent(String data, String mimeType) {
		this(data, null, mimeType, null);
	}

	public ContentType getType() {
		return type;
	}

	public String getData() {
		return data;
	}

	public Annotations getAnnotations() {
		return annotations;
	}

	public String getMimeType() {
		return mimeType;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "AudioContent [data=" + data + ", annotations=" + annotations + ", mimeType=" + mimeType + ", meta="
				+ meta + "]";
	}

}
