package com.kiyoung.cloud.storage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	public static String getDateStr(Date date) {
		return getDateStr(date, "yyyyMMdd");
	}
	
	public static String getDateStr(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date getDate(String str) {
		return getDate(str, "yyyyMMdd");
	}
	
	public static Date getDate(String str, String pattern) {
		try {
			return	org.apache.commons.lang.time.DateUtils.parseDate(str, new String[]{pattern});
		} catch (ParseException e) {
			throw new RuntimeException("DateFormatError", e.getCause());
		}
	}
	
	public static Date getFirstDayOfMonth(Date date) {
		return org.apache.commons.lang.time.DateUtils.setDays(date, 1);
	}
	
	public static Date getLastDayOfMonth(Date date) {
		date = org.apache.commons.lang.time.DateUtils.addMonths(date, 1);
		date = getFirstDayOfMonth(date);
		return org.apache.commons.lang.time.DateUtils.addDays(date, -1);
	}
	
	public static Date getLastYear(Date date) {
		date = org.apache.commons.lang.time.DateUtils.addYears(date, -1);
		return date;
	}
	
	public static String getLastYear(String date, String pattern) {
		Date temp = DateUtils.getDate(date, pattern);
		temp = DateUtils.getLastYear(temp);
		return DateUtils.getDateStr(temp, pattern);
	}
	
	public static String getNow() {
		Date date = new Date();
		return getDateStr(date, "yyyyMMddHHmmss");
	}
	
	public static String getToday() {
		Date date = new Date();
		return getDateStr(date, "yyyyMMdd");
	}
	
	public static String getThisMonth() {
		Date date = new Date();
		return getDateStr(date, "yyyyMM");
	}
	
	public static long getBetweenSeconds(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.SECOND, false);
	}
	
	public static long getBetweenMinutes(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.MINUTE, false);
	}
	
	public static long getBetweenHours(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.HOUR, false);
	}
	
	public static long getBetweenDays(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.DATE, false);
	}
	
	public static long getBetweenTruncatedSeconds(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.SECOND, true);
	}
	
	public static long getBetweenTruncatedMinutes(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.MINUTE, true);
	}
	
	public static long getBetweenTruncatedHours(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.HOUR, true);
	}
	
	public static long getBetweenTruncatedDays(Date date1, Date date2) {
		return getBetweenTimes(date1, date2, Calendar.DATE, true);
	}
	
	private static long getBetweenTimes(Date date1, Date date2, int field, boolean isTruncated) {
		if (date1 == null || date2 == null)
			return 0;

		if (isTruncated) {
			date1 = truncate(date1, field);
			date2 = truncate(date2, field);
		}
		
		long unit = 0;
		switch (field) {
		case Calendar.SECOND:
			unit = 1000;
			break;
		case Calendar.MINUTE:
			unit = 1000 * 60;
			break;
		case Calendar.HOUR:
			unit = 1000 * 60 * 60;
			break;
		case Calendar.DATE:
			unit = 1000 * 60 * 60 * 24;
			break;
		}
		long time = date1.getTime() - date2.getTime();
		return time / unit;
	}
	
	public static Date getAddDayDate(int addDay) {
		Date date = new Date();
		/*long time = date.getTime();
		System.out.println("-->");
		System.out.println(date);
		long setTime = (addDay * 24*60*60*1000) + time;
		
		Date date2 = new Date(setTime);*/
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDay);
		date = cal.getTime();
		return date;
	}
	
	public static Date getAddHourDate(int addDay) {
		Date date = new Date();
		/*long time = date.getTime();
		System.out.println("-->");
		System.out.println(date);
		long setTime = (addDay * 24*60*60*1000) + time;
		
		Date date2 = new Date(setTime);*/
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, addDay);
		date = cal.getTime();
		return date;
	}
	
}
