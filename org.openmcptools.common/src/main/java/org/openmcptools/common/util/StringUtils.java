package org.openmcptools.common.util;

/**
 * Utility class for string operations.
 */
public class StringUtils {

	/**
	 * Checks if a string has non-blank text.
	 * 
	 * @param str the string to check
	 * @return true if string is not null and not blank
	 */
	public static boolean hasText(String str) {
		return (str != null && !str.isBlank());
	}

	/**
	 * Cleans an annotation string, returning null if it's empty or blank.
	 * 
	 * @param annotationString the string to clean
	 * @return the cleaned string or null
	 */
	public static String cleanAnnotationString(String annotationString) {
		return hasText(annotationString) ? annotationString : null;
	}
}
