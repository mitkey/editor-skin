package com.libgdx.skin.editor.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @作者 Mitkey
 * @时间 2016年9月7日 上午10:00:06
 * @类说明:日期工具类
 * @版本 xx
 */
public class DateUtils {

	private static Calendar calendar = Calendar.getInstance();

	public enum DefaultPattern {
		yyyyMMdd_jian("yyyy-MM-dd"), //
		yyyyMMdd_jian_HHmmss_mao("yyyy-MM-dd HH:mm:ss"), //
		yyyyMMdd_jian_HHmm_mao("yyyy-MM-dd HH:mm"), //
		yyyyMMdd_chu("yyyy/MM/dd"), //
		yyyyMMdd_chu_HHmmss_mao("yyyy/MM/dd HH:mm:ss"), //
		yyyyMMdd_chu_HHmm_mao("yyyy/MM/dd HH:mm"), //
		yyyyMMdd_chinese_HHmm_chinese("yyyy年MM月dd日  HH小时mm分"), //
		MMdd_chinese("MM月dd日"), //
		HHmmss_mao("HH:mm:ss"),//
		;

		public String pattern;
		private DefaultPattern(String pattern) {
			this.pattern = pattern;
		}
	}

	/**
	 * 字符日期转换为 Date
	 */
	public static Date str2Data(String dateStr, DefaultPattern pattern) {
		return str2Data(dateStr, pattern.pattern);
	}

	/**
	 * 字符日期转换为 Date
	 */
	public static Date str2Data(String dateStr, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 得到当前日期字符串格式
	 */
	public static String getDate(String pattern) {
		return getData(getSysDate(), pattern);
	}

	/**
	 * 得到当前日期字符串格式
	 */
	public static String getDate(DefaultPattern pattern) {
		return getDate(pattern.pattern);
	}

	/**
	 * 使用指定日期格式得到给定日期字符串
	 */
	public static String getData(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 使用指定日期格式得到给定日期字符串
	 */
	public static String getData(Date date, DefaultPattern pattern) {
		return new SimpleDateFormat(pattern.pattern).format(date);
	}

	/**
	 * 获取距离当前时间过去的天数
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取给定时间，当天 00:00:00 时间的 Date
	 */
	public static Date getDateStart(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(getData(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取给定时间，当天 23:59:59 时间的 Date
	 */
	public static Date getDateEnd(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(getData(date, "yyyy-MM-dd") + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/** 获取年龄 */
	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}

	public static String getYear() {
		return calendar.get(Calendar.YEAR) + "";
	}

	public static String getMonth() {
		int month = calendar.get(Calendar.MONTH) + 1;
		return month + "";
	}

	public static String getDay() {
		return calendar.get(Calendar.DATE) + "";
	}

	public static String get24Hour() {
		return calendar.get(Calendar.HOUR_OF_DAY) + "";
	}

	public static String getMinute() {
		return calendar.get(Calendar.MINUTE) + "";
	}

	public static String getSecond() {
		return calendar.get(Calendar.SECOND) + "";
	}

	/**
	 * 判断字符串是否是日期
	 */
	public static boolean isDateFormat(String timeString) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setLenient(false);
		try {
			format.parse(timeString);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否为今天
	 */
	public static boolean isToday(Date date) {
		return getDate(DefaultPattern.yyyyMMdd_chu).equals(getData(date, DefaultPattern.yyyyMMdd_chu));
	}

	/**
	 * 获取系统时间Date
	 */
	public static Date getSysDate() {
		return new Date();
	}

}