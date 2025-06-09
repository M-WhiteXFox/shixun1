package com.ynnz.store.util;

import javax.swing.table.DefaultTableModel;

public class FrameUtil {

	/**
	 * 清空表里的数据
	 *
	 * @param modelo
	 */
	public static void removeAllRows(DefaultTableModel modelo) {
		int a = modelo.getRowCount();
		for (int i = 0; i < a; i++) {
			modelo.removeRow(0);
		}
	}
}
