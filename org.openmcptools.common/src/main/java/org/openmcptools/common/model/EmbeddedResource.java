package org.openmcptools.common.model;

import java.util.Map;

public class EmbeddedResource implements Content {

	private final ContentType type = ContentType.RESOURCE;

	private final ResourceContent resource;
	private final Annotations annotations;
	private final Map<String, Object> meta;

	public EmbeddedResource(ResourceContent resource, Annotations annotations, Map<String, Object> meta) {
		this.annotations = annotations;
		this.resource = resource;
		this.meta = meta;
	}

	public EmbeddedResource(ResourceContent resource, Annotations annotations) {
		this(resource, annotations, null);
	}

	public EmbeddedResource(ResourceContent resource) {
		this(resource, null, null);
	}

	public ContentType getType() {
		return type;
	}

	public Annotations getAnnotations() {
		return annotations;
	}

	public Map<String, Object> getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return "EmbeddedResource [resource=" + resource + ", annotations=" + annotations + ", meta=" + meta + "]";
	}

}
