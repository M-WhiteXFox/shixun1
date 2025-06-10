package com.ynnz.store.util;

public class StringUtils {

	public static boolean isNotEmpty(String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}
}
