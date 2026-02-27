package org.openmcptools.common.model;

import java.util.Map;
import java.util.Objects;

public class Resource extends AbstractLeaf {

	private final String uri;

	protected Long size;

	protected String mimeType;

	protected Annotations annotations;

	public Resource(String name, String uri) {
		super(name);
		Objects.requireNonNull(uri, "uri must not be null");
		this.uri = uri;
	}

	public static class Builder {

		private String uri;

		private String name;

		private String title;

		private String description;

		private String mimeType;

		private Long size;

		private Annotations annotations;

		private Map<String, Object> meta;

		public Builder uri(String uri) {
			this.uri = uri;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Builder mimeType(String mimeType) {
			this.mimeType = mimeType;
			return this;
		}

		public Builder size(Long size) {
			this.size = size;
			return this;
		}

		public Builder annotations(Annotations annotations) {
			this.annotations = annotations;
			return this;
		}

		public Builder meta(Map<String, Object> meta) {
			this.meta = meta;
			return this;
		}

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

	public String getUri() {
		return uri;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Annotations getAnnotations() {
		return annotations;
	}

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
