package org.openmcptools.common.model.content;

public interface Content {

	public enum ContentType {
		TEXT, IMAGE, AUDIO, RESOURCE, RESOURCE_LINK
	}

	public ContentType getType();
}
