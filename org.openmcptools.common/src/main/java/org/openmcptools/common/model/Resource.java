package org.openmcptools.common.model;

public class Resource extends AbstractLeaf {

	protected String uri;

	protected Long size;

	protected String mimeType;

	protected Annotations annotations;

	public Resource(String name) {
		super(name);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
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
		return "ResourceNode [name=" + name + ", fqName=" + getFullyQualifiedName() + ", title=" + title
				+ ", description=" + description + ", meta=" + meta + ", uri=" + uri + ", size=" + size + ", mimeType="
				+ mimeType + ", annotations=" + annotations + "]";
	}

}
