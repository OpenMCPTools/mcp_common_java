package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents the contents of a resource that is stored as a binary large object (BLOB).
 * This class implements {@link ResourceContents} to provide metadata and the actual data content.
 */
public class BlobResourceContents implements ResourceContents {

	/**
	 * The binary data content encoded as a string.
	 */
	private final String blob;

	/**
	 * The content type of this object, which is always {@link ContentType#RESOURCE}.
	 */
	private final ContentType type = ContentType.RESOURCE;

	/**
	 * The unique identifier (URI) for the resource.
	 */
	private final String uri;

	/**
	 * The MIME type of the binary data.
	 */
	private final String mimeType;

	/**
	 * Additional metadata associated with the resource contents.
	 */
	private final Map<String, Object> meta;

	/**
	 * Constructs a new BlobResourceContents with all specified attributes.
	 *
	 * @param uri      the URI of the resource
	 * @param mimeType the MIME type of the resource
	 * @param blob     the binary data content
	 * @param meta     additional metadata (can be null)
	 */
	public BlobResourceContents(String uri, String mimeType, String blob, Map<String, Object> meta) {
		this.uri = uri;
		this.mimeType = mimeType;
		this.blob = blob;
		this.meta = meta;
	}

	/**
	 * Constructs a new BlobResourceContents without metadata.
	 *
	 * @param uri      the URI of the resource
	 * @param mimeType the MIME type of the resource
	 * @param blob     the binary data content
	 */
	public BlobResourceContents(String uri, String mimeType, String blob) {
		this(uri, mimeType, blob, null);
	}

	/**
	 * Gets the binary data content.
	 *
	 * @return the blob content string
	 */
	public String getBlob() {
		return blob;
	}

	/**
	 * Gets the content type.
	 *
	 * @return {@link ContentType#RESOURCE}
	 */
	@Override
	public ContentType getType() {
		return type;
	}

	/**
	 * Gets the resource URI.
	 *
	 * @return the URI string
	 */
	@Override
	public String getUri() {
		return uri;
	}

	/**
	 * Gets the MIME type of the content.
	 *
	 * @return the MIME type string
	 */
	@Override
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Gets the metadata associated with this resource.
	 *
	 * @return a map of metadata, or null if none
	 */
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
