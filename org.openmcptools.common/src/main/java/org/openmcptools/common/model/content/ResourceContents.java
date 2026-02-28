package org.openmcptools.common.model.content;

import java.util.Map;

public interface ResourceContents extends Content {

	String getUri();

	String getMimeType();

	Map<String, Object> getMeta();

}
