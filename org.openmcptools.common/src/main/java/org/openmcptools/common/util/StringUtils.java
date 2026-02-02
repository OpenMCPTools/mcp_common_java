package org.openmcptools.common.util;

public class StringUtils {

	public static boolean hasText(String str) {
		return (str != null && !str.isBlank());
	}

	public static String cleanAnnotationString(String annotationString) {
		return hasText(annotationString) ? annotationString : null;
	}
}
