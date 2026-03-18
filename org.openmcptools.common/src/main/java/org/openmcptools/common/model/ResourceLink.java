package org.openmcptools.common.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a reference to a resource that can be retrieved or explored.
 * Unlike {@link ResourceContents}, which contains the actual data, a ResourceLink
 * provides metadata and a URI to locate the resource.
 */
public class ResourceLink implements Content {

	private final ContentType type = ContentType.RESOURCE_LINK;

	private final String name;

	private final String title;

	private final String uri;

	private final String mimeType;

	private final String description;

	private final Long size;

	private final Annotations annotation;

	private final Map<String, Object> meta;

	/**
	 * Constructs a new ResourceLink.
	 *
	 * @param name        the name of the resource (required)
	 * @param title       a human-readable title for the resource
	 * @param uri         the URI of the resource (required)
	 * @param mimeType    the MIME type of the resource
	 * @param description a description of the resource
	 * @param size        the size of the resource in bytes, if known
	 * @param annotation  annotations associated with this link
	 * @param meta        metadata associated with this link
	 */
	public ResourceLink(String name, String title, String uri, String mimeType, String description, Long size,
			Annotations annotation, Map<String, Object> meta) {
		super();
		Objects.requireNonNull(uri, "uri must not be null");
		Objects.requireNonNull(name, "name must not be null");
		this.name = name;
		this.title = title;
		this.uri = uri;
		this.mimeType = mimeType;
		this.description = description;
		this.size = size;
		this.annotation = annotation;
		this.meta = meta;
	}

	/**
	 * Creates a new builder for {@link ResourceLink}.
	 *
	 * @return a new builder instance
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for {@link ResourceLink} instances.
	 */
	public static class Builder {

		private String name;

		private String title;

		private String uri;

		private String description;

		private String mimeType;

		private Annotations annotations;

		private Long size;

		private Map<String, Object> meta;

		/**
		 * Sets the name of the resource.
		 *
		 * @param name the resource name
		 * @return this builder
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}

		/**
		 * Sets the title of the resource.
		 *
		 * @param title the resource title
		 * @return this builder
		 */
		public Builder title(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Sets the URI of the resource.
		 *
		 * @param uri the resource URI
		 * @return this builder
		 */
		public Builder uri(String uri) {
			this.uri = uri;
			return this;
		}

		/**
		 * Sets the description of the resource.
		 *
		 * @param description the resource description
		 * @return this builder
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets the MIME type of the resource.
		 *
		 * @param mimeType the resource MIME type
		 * @return this builder
		 */
		public Builder mimeType(String mimeType) {
			this.mimeType = mimeType;
			return this;
		}

		/**
		 * Sets the annotations for the resource.
		 *
		 * @param annotations the annotations
		 * @return this builder
		 */
		public Builder annotations(Annotations annotations) {
			this.annotations = annotations;
			return this;
		}

		/**
		 * Sets the size of the resource.
		 *
		 * @param size the size in bytes
		 * @return this builder
		 */
		public Builder size(Long size) {
			this.size = size;
			return this;
		}

		/**
		 * Sets the metadata for the resource.
		 *
		 * @param meta the metadata map
		 * @return this builder
		 */
		public Builder meta(Map<String, Object> meta) {
			this.meta = meta;
			return this;
		}

		/**
		 * Builds a {@link ResourceLink} instance.
		 *
		 * @return the new ResourceLink
		 * @throws NullPointerException if uri or name is null
		 */
		public ResourceLink build() {
			Objects.requireNonNull(uri, "uri must not be null");
			Objects.requireNonNull(name, "name must not be null");
			return new ResourceLink(name, title, uri, mimeType, description, size, annotations, meta);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public ContentType getType() {
		return type;
	}

	/**
	 * Returns the name of the resource.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the title of the resource.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the URI of the resource.
	 *
	 * @return the URI
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Returns the MIME type of the resource.
	 *
	 * @return the MIME type
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Returns the description of the resource.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the size of the resource.
	 *
	 * @return the size in bytes, or null if unknown
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * Returns the annotations associated with this link.
	 *
	 * @return the annotations
	 */
	public Annotations getAnnotation() {
		return annotation;
	}

	/**
	 * Returns the metadata associated with this link.
	 *
	 * @return the metadata map
	 */
	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "ResourceLink [type=" + type + ", name=" + name + ", title=" + title + ", uri=" + uri + ", mimeType="
				+ mimeType + ", description=" + description + ", size=" + size + ", annotation=" + annotation
				+ ", meta=" + meta + "]";
	}

}
