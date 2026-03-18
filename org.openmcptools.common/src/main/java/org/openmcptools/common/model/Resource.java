package org.openmcptools.common.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a resource available to the system, such as a file, data stream, or external reference.
 * Resources are identified by a URI and can include size, MIME type, and annotations.
 */
public class Resource extends AbstractLeaf {

	/** The unique URI identifying the resource. */
	private final String uri;

	/** The size of the resource in bytes. */
	protected Long size;

	/** The MIME type of the resource content. */
	protected String mimeType;

	/** Annotations providing additional context for the resource. */
	protected Annotations annotations;

	/**
	 * Constructs a new Resource with a name and URI.
	 * 
	 * @param name the name of the resource
	 * @param uri the URI of the resource
	 * @throws NullPointerException if {@code uri} is null
	 */
	public Resource(String name, String uri) {
		super(name);
		Objects.requireNonNull(uri, "uri must not be null");
		this.uri = uri;
	}

	/**
	 * Creates a new {@link Builder} for creating {@link Resource} instances.
	 * 
	 * @param name the resource name
	 * @param uri the resource URI
	 * @return a new Builder
	 */
	public static Builder builder(String name, String uri) {
		return new Builder(name, uri);
	}

	/**
	 * Fluent builder for {@link Resource}.
	 */
	public static class Builder {

		private String uri;
		private String name;
		private String title;
		private String description;
		private String mimeType;
		private Long size;
		private Annotations annotations;
		private Map<String, Object> meta;

		/**
		 * Constructs a Builder with required name and URI.
		 * 
		 * @param name the resource name
		 * @param uri the resource URI
		 */
		public Builder(String name, String uri) {
			this.name = name;
			this.uri = uri;
		}

		/**
		 * Sets the title of the resource.
		 * @param title the title
		 * @return this builder
		 */
		public Builder title(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Sets the description of the resource.
		 * @param description the description
		 * @return this builder
		 */
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		/**
		 * Sets the MIME type of the resource.
		 * @param mimeType the MIME type
		 * @return this builder
		 */
		public Builder mimeType(String mimeType) {
			this.mimeType = mimeType;
			return this;
		}

		/**
		 * Sets the size of the resource.
		 * @param size the size in bytes
		 * @return this builder
		 */
		public Builder size(Long size) {
			this.size = size;
			return this;
		}

		/**
		 * Sets the annotations for the resource.
		 * @param annotations the annotations
		 * @return this builder
		 */
		public Builder annotations(Annotations annotations) {
			this.annotations = annotations;
			return this;
		}

		/**
		 * Sets the metadata for the resource.
		 * @param meta the metadata map
		 * @return this builder
		 */
		public Builder meta(Map<String, Object> meta) {
			this.meta = meta;
			return this;
		}

		/**
		 * Builds a new {@link Resource} instance.
		 * @return the constructed Resource
		 */
		public Resource build() {
			Resource result = new Resource(name, uri);
			result.setTitle(title);
			result.setDescription(description);
			result.setSize(size);
			result.setMeta(meta);
			result.setMimeType(mimeType);
			result.setAnnotations(annotations);
			return result;
		}
	}

	/**
	 * Gets the URI of the resource.
	 * 
	 * @return the URI
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Gets the size of the resource.
	 * 
	 * @return the size in bytes, or {@code null} if unknown
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * Sets the size of the resource.
	 * 
	 * @param size the size in bytes
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * Gets the MIME type of the resource.
	 * 
	 * @return the MIME type
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the MIME type of the resource.
	 * 
	 * @param mimeType the MIME type to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Gets the annotations for the resource.
	 * 
	 * @return the annotations, or {@code null} if none
	 */
	public Annotations getAnnotations() {
		return annotations;
	}

	/**
	 * Sets the annotations for the resource.
	 * 
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(Annotations annotations) {
		this.annotations = annotations;
	}

	@Override
	public String toString() {
		return "Resource [name=" + name + ", fqName=" + getFullyQualifiedName() + ", title=" + title + ", description="
				+ description + ", meta=" + meta + ", uri=" + uri + ", size=" + size + ", mimeType=" + mimeType
				+ ", annotations=" + annotations + "]";
	}
}
