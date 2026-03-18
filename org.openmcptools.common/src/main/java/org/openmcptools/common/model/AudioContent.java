package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents audio content within the MCP system.
 */
public class AudioContent implements Content {

	private final ContentType type = ContentType.AUDIO;
	private final String data;
	private final Annotations annotations;
	private final String mimeType;
	private final Map<String, Object> meta;

	/**
	 * Full constructor for AudioContent.
	 * 
	 * @param data the audio data (base64 encoded)
	 * @param annotations content annotations
	 * @param mimeType the MIME type of the audio
	 * @param meta additional metadata
	 */
	public AudioContent(String data, Annotations annotations, String mimeType, Map<String, Object> meta) {
		super();
		this.data = data;
		this.annotations = annotations;
		this.mimeType = mimeType;
		this.meta = meta;
	}

	/**
	 * Constructor with data, annotations, and mimeType.
	 * 
	 * @param data the audio data
	 * @param annotations content annotations
	 * @param mimeType the MIME type
	 */
	public AudioContent(String data, Annotations annotations, String mimeType) {
		this(data, annotations, mimeType, null);
	}

	/**
	 * Constructor with data and mimeType.
	 * 
	 * @param data the audio data
	 * @param mimeType the MIME type
	 */
	public AudioContent(String data, String mimeType) {
		this(data, null, mimeType, null);
	}

	@Override
	public ContentType getType() {
		return type;
	}

	/**
	 * Gets the audio data.
	 * 
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Gets the annotations.
	 * 
	 * @return the annotations
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * Gets the MIME type.
	 * 
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Gets the metadata.
	 * 
	 * @return the meta map
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "AudioContent [data=" + data + ", annotations=" + annotations + ", mimeType=" + mimeType + ", meta="
				+ meta + "]";
	}
}
