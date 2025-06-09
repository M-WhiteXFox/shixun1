package com.ynnz.store.util;

import java.math.BigDecimal;

public class RoundTool {

	/**
	 *
	 * @param value        double数据.
	 * @param scale        精度位数(保留的小数位数).
	 * @param roundingMode 精度取值方式.
	 * @return
	 */
	public static double roundDouble(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		return d;
	}

	public static float roundFloat(float value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		float d = bd.floatValue();
		return d;
	}

	public static float roundBigDecimal(BigDecimal value, int scale, int roundingMode) {
		value = value.setScale(scale, roundingMode);
		float d = value.floatValue();
		return d;
	}

	public static void main(String[] args) {
		// 只要第2位后面存在大于0的小数，则第2位就+1
		System.out.println(roundDouble(12.3401, 2, BigDecimal.ROUND_UP));// 12.35
		System.out.println(roundDouble(-12.3401, 2, BigDecimal.ROUND_UP));// -12.35

	}
}
