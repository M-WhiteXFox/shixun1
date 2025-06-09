package com.ynnz.store.util;

public class StringUtils {

	public static boolean isNotEmpty(String str) {
		if (str != null && !str.trim().equals("")) {
			return true;
		}
		return false;
	}
}
