package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents image content within a message or resource.
 * Contains the raw image data, MIME type, and optional annotations or metadata.
 */
public class ImageContent implements Content {

	/** The type of content, which is always {@link ContentType#IMAGE}. */
	private final ContentType type = ContentType.IMAGE;
	
	/** The base64 encoded data or URI of the image. */
	private final String data;
	
	/** Optional annotations providing additional context for the image. */
	private final Annotations annotations;
	
	/** The MIME type of the image. */
	private final String mimeType;
	
	/** Optional metadata associated with the image content. */
	private final Map<String, Object> meta;

	/**
	 * Constructs a new ImageContent with all available fields.
	 * 
	 * @param data the image data
	 * @param annotations the annotations for the image
	 * @param mimeType the MIME type of the image
	 * @param meta the metadata for the image
	 */
	public ImageContent(String data, Annotations annotations, String mimeType, Map<String, Object> meta) {
		super();
		this.data = data;
		this.annotations = annotations;
		this.mimeType = mimeType;
		this.meta = meta;
	}

	/**
	 * Constructs a new ImageContent without metadata.
	 * 
	 * @param data the image data
	 * @param annotations the annotations for the image
	 * @param mimeType the MIME type of the image
	 */
	public ImageContent(String data, Annotations annotations, String mimeType) {
		this(data, annotations, mimeType, null);
	}

	/**
	 * Constructs a new ImageContent with only data and MIME type.
	 * 
	 * @param data the image data
	 * @param mimeType the MIME type of the image
	 */
	public ImageContent(String data, String mimeType) {
		this(data, null, mimeType, null);
	}

	/**
	 * Gets the content type.
	 * 
	 * @return {@link ContentType#IMAGE}
	 */
	public ContentType getType() {
		return type;
	}

	/**
	 * Gets the raw image data.
	 * 
	 * @return the image data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Gets the annotations for the image.
	 * 
	 * @return the annotations, or {@code null} if none
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * Gets the MIME type of the image.
	 * 
	 * @return the MIME type
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Gets the metadata associated with the image.
	 * 
	 * @return the metadata map, or {@code null} if none
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "ImageContent [data=" + data + ", annotations=" + annotations + ", mimeType=" + mimeType + ", meta="
				+ meta + "]";
	}
}
