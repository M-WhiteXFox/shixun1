package com.ynnz.store.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static String timeStampFmt = "yyyyMMddHHmmss";

	private static String dateTimeFmt = "yyyy-MM-dd HH:mm:ss";

	private static String dateTimeFmt1 = "yyyy年MM月dd日HH:mm:ss";

	private static String dateFmt1 = "yyyy-MM-dd";

	private static String dateFmt2 = "yyyy-M-d";

	private static String dateFmt3 = "yyyy-MM";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 取当前日期时间戳
	 *
	 * @return
	 */
	public static String getNowTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(timeStampFmt);
		String ts = sdf.format(date);
		return ts;
	}

	/**
	 * 根据指定字符串获得日期对象
	 *
	 * @param date
	 * @return
	 */
	public static Date getDate(String date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat(dateFmt1);
		Date d = null;
		if (date != null) {
			date = date.trim();
		}
		try {
			d = sdf1.parse(date);
		} catch (ParseException e) {
			d = null;
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat(dateFmt2);
		try {
			d = sdf2.parse(date);
		} catch (ParseException e) {
			d = null;
		}
		return d;
	}

	/**
	 * 根据指定日期对象转换为字符串类型
	 *
	 * @param date
	 * @return
	 */
	public static String getDate(Date date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat(dateFmt1);
		return sdf1.format(date);
	}

	/**
	 * 将传入的日期时间对象转换为字符串类型
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFmt);
		String ts = sdf.format(date);
		return ts;
	}

	/**
	 * 将传入的日期时间对象转换为字符串类型
	 * yyyy年MM月dd HH时mm分ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getChinaDateTime(Date date) {
		if (date == null) {
			return "";
		}
		return sdf.format(date);
	}

	/**
	 * 获取某一天的开始时间
	 *
	 * @param date
	 * @return
	 */
	public static String getDateStart(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFmt1);
		String d = sdf.format(date);
		d = d + " 00:00:00";
		return d;
	}

	/**
	 * 获取某一天的结束时间
	 *
	 * @param date
	 * @return
	 */
	public static String getDateEnd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFmt1);
		String d = sdf.format(date);
		d = d + " 23:59:59";
		return d;
	}

	public static String getMonthStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_MONTH) - 1;
		c.add(Calendar.DAY_OF_MONTH, -day);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFmt1);
		String d = sdf.format(c.getTime());
		d = d + " 00:00:00";
		return d;
	}

	public static String getMonthEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DAY_OF_MONTH, (days - day));
		SimpleDateFormat sdf = new SimpleDateFormat(dateFmt1);
		String d = sdf.format(c.getTime());
		d = d + " 23:59:59";
		return d;
	}

	/**
	 * 获取日期所属的月份
	 *
	 * @param d
	 * @return
	 */
	public static int getMonthOfDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期的yyyy-MM格式
	 *
	 * @param date
	 * @return
	 */
	public static String getYMOfDate(String date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat(dateFmt3);
		Date d = null;
		if (date != null) {
			date = date.trim();
		}
		try {
			d = sdf1.parse(date);
		} catch (ParseException e) {
			d = null;
		}
		String md = null;
		if (d != null) {
			md = sdf1.format(d);
		}
		return md;
	}

	public static void main(String[] args) {

		Date d = new Date();
		System.out.println(getDateStart(d));
		System.out.println(getDateEnd(d));
		System.out.println(getMonthStart(d));
		System.out.println(getMonthEnd(d));
	}

}
