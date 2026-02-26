package org.openmcptools.common.model;

import java.util.Map;

public class TextResourceContents extends ResourceContent {

	private final String text;

	public TextResourceContents(String uri, String mimeType, String text, Map<String, Object> meta) {
		super(uri, mimeType, meta);
		this.text = text;
	}

	public TextResourceContents(String uri, String mimeType, String text) {
		super(uri, mimeType);
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
