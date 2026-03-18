package org.openmcptools.common.model;

import java.util.List;

/**
 * Represents an icon definition for tools or resources.
 * Icons can have multiple sizes, themes, and MIME types.
 */
public class Icon {
	/** The source URL or path of the icon. */
	private String src;
	
	/** The MIME type of the icon image. */
	private String mimeType;
	
	/** A list of supported sizes for the icon (e.g., "32x32", "64x64"). */
	private List<String> sizes;
	
	/** The theme associated with the icon (e.g., "light", "dark"). */
	private String theme;

	/**
	 * Gets the source URL or path of the icon.
	 * 
	 * @return the icon source
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * Sets the source URL or path of the icon.
	 * 
	 * @param src the icon source to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}

	/**
	 * Gets the MIME type of the icon.
	 * 
	 * @return the MIME type
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets the MIME type of the icon.
	 * 
	 * @param mimeType the MIME type to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Gets the list of supported sizes for the icon.
	 * 
	 * @return a list of sizes
	 */
	public List<String> getSizes() {
		return sizes;
	}

	/**
	 * Sets the supported sizes for the icon.
	 * 
	 * @param sizes the list of sizes to set
	 */
	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}

	/**
	 * Gets the theme associated with the icon.
	 * 
	 * @return the icon theme
	 */
	public String getTheme() {
		return theme;
	}

	/**
	 * Sets the theme associated with the icon.
	 * 
	 * @param theme the icon theme to set
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Override
	public String toString() {
		return "Icon [src=" + src + ", mimeType=" + mimeType + ", sizes=" + sizes + ", theme=" + theme + "]";
	}
}