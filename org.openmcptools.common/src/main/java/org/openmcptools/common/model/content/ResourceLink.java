package org.openmcptools.common.model.content;

import java.util.Map;
import java.util.Objects;

import org.openmcptools.common.model.Annotations;

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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String name;

		private String title;

		private String uri;

		private String description;

		private String mimeType;

		private Annotations annotations;

		private Long size;

		private Map<String, Object> meta;

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder uri(String uri) {
			this.uri = uri;
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

		public Builder annotations(Annotations annotations) {
			this.annotations = annotations;
			return this;
		}

		public Builder size(Long size) {
			this.size = size;
			return this;
		}

		public Builder meta(Map<String, Object> meta) {
			this.meta = meta;
			return this;
		}

		public ResourceLink build() {
			Objects.requireNonNull(uri, "uri must not be null");
			Objects.requireNonNull(name, "name must not be null");
			return new ResourceLink(name, title, uri, description, mimeType, size, annotations, meta);
		}

	}

	public ContentType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getUri() {
		return uri;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getDescription() {
		return description;
	}

	public Long getSize() {
		return size;
	}

	public Annotations getAnnotation() {
		return annotation;
	}

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
