package org.openmcptools.common.model;

/**
 * Interface representing content that can be included in messages or resources.
 */
public interface Content {

	/**
	 * Enumeration of supported content types.
	 */
	public enum ContentType {
		TEXT, IMAGE, AUDIO, RESOURCE, RESOURCE_LINK
	}

	/**
	 * Returns the type of the content.
	 * 
	 * @return the content type
	 */
	public ContentType getType();
}
