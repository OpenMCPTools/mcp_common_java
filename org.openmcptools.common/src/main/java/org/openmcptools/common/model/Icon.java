package org.openmcptools.common.model;

import java.util.List;

/**
 * Icon definition
 */
public class Icon {
	private String src;
	private String mimeType;
	private List<String> sizes;
	private String theme;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public List<String> getSizes() {
		return sizes;
	}

	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Override
	public String toString() {
		return "Icon [src=" + src + ", mimeType=" + mimeType + ", sizes=" + sizes + ", theme=" + theme + "]";
	}
	
}
