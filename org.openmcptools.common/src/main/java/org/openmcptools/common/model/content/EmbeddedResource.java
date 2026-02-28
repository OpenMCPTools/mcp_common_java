package org.openmcptools.common.model.content;

import java.util.Map;

import org.openmcptools.common.model.Annotations;

public class EmbeddedResource implements Content {

	private final ContentType type = ContentType.RESOURCE;

	private final ResourceContents resource;
	private final Annotations annotations;
	private final Map<String, Object> meta;

	public EmbeddedResource(ResourceContents resource, Annotations annotations, Map<String, Object> meta) {
		this.annotations = annotations;
		this.resource = resource;
		this.meta = meta;
	}

	public EmbeddedResource(ResourceContents resource, Annotations annotations) {
		this(resource, annotations, null);
	}

	public EmbeddedResource(ResourceContents resource) {
		this(resource, null, null);
	}

	public ResourceContents getResource() {
		return resource;
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
