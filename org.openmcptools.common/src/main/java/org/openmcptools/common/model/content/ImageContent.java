package org.openmcptools.common.model.content;

import java.util.Map;

import org.openmcptools.common.model.Annotations;

public class ImageContent implements Content {

	private final ContentType type = ContentType.IMAGE;
	private final String data;
	private final Annotations annotations;
	private final String mimeType;
	private final Map<String, Object> meta;

	public ImageContent(String data, Annotations annotations, String mimeType, Map<String, Object> meta) {
		super();
		this.data = data;
		this.annotations = annotations;
		this.mimeType = mimeType;
		this.meta = meta;
	}

	public ImageContent(String data, Annotations annotations, String mimeType) {
		this(data, annotations, mimeType, null);
	}

	public ImageContent(String data, String mimeType) {
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
		return "ImageContent [data=" + data + ", annotations=" + annotations + ", mimeType=" + mimeType + ", meta="
				+ meta + "]";
	}

}
