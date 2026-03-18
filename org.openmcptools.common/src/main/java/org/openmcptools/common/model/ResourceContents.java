package org.openmcptools.common.model;

import java.util.Map;

/**
 * Represents the contents of a resource in the MCP (Model Context Protocol).
 * This interface defines the basic structure for different types of resource data,
 * such as text or binary (blob) content.
 */
public interface ResourceContents extends Content {

	/**
	 * Returns the URI of the resource.
	 *
	 * @return the unique resource identifier
	 */
	String getUri();

	/**
	 * Returns the MIME type of the resource content.
	 *
	 * @return the IANA media type (e.g., "text/plain", "application/json")
	 */
	String getMimeType();

	/**
	 * Returns a map of metadata associated with the resource content.
	 *
	 * @return a map containing metadata key-value pairs, or null if no metadata is present
	 */
	Map<String, Object> getMeta();

}
