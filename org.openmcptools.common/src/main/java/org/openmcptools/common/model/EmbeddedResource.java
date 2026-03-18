package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents a resource that is embedded directly within content.
 * Implements the {@link Content} interface.
 */
public class EmbeddedResource implements Content {

	/**
	 * The content type of this object, which is always {@link ContentType#RESOURCE}.
	 */
	private final ContentType type = ContentType.RESOURCE;

	/**
	 * The actual contents of the embedded resource.
	 */
	private final ResourceContents resource;

	/**
	 * Annotations associated with the embedded resource.
	 */
	private final Annotations annotations;

	/**
	 * Additional metadata associated with the embedded resource.
	 */
	private final Map<String, Object> meta;

	/**
	 * Constructs a new EmbeddedResource with resource contents, annotations, and metadata.
	 *
	 * @param resource    the resource contents
	 * @param annotations annotations for the resource (can be null)
	 * @param meta        metadata for the resource (can be null)
	 */
	public EmbeddedResource(ResourceContents resource, Annotations annotations, Map<String, Object> meta) {
		this.annotations = annotations;
		this.resource = resource;
		this.meta = meta;
	}

	/**
	 * Constructs a new EmbeddedResource with resource contents and annotations.
	 *
	 * @param resource    the resource contents
	 * @param annotations annotations for the resource (can be null)
	 */
	public EmbeddedResource(ResourceContents resource, Annotations annotations) {
		this(resource, annotations, null);
	}

	/**
	 * Constructs a new EmbeddedResource with only resource contents.
	 *
	 * @param resource the resource contents
	 */
	public EmbeddedResource(ResourceContents resource) {
		this(resource, null, null);
	}

	/**
	 * Gets the contents of the embedded resource.
	 *
	 * @return the resource contents
	 */
	public ResourceContents getResource() {
		return resource;
	}

	/**
	 * Gets the content type.
	 *
	 * @return {@link ContentType#RESOURCE}
	 */
	public ContentType getType() {
		return type;
	}

	/**
	 * Gets the annotations associated with this resource.
	 *
	 * @return the annotations, or null if none
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * Gets the metadata associated with this resource.
	 *
	 * @return a map of metadata, or null if none
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "EmbeddedResource [resource=" + resource + ", annotations=" + annotations + ", meta=" + meta + "]";
	}

}
