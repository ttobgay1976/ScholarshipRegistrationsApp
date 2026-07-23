package com.sprms.registration.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	private static String format = "dd-MMM-yyyy";
	private static String formatWithTime = "dd-MMM-yyyy HH:mm:ss";
	private static String timeFormat = "HH:mm:ss";

	private DateUtil() {
	}

//	get the current date with time
	public static LocalDateTime getCurrentDateTime() {
		return LocalDateTime.now();
	}

	public static String toString(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static String toString(Date date, String format) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);

	}

	public static Date toDate(String date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			LOGGER.error("ERROR @ toDate(String date) Method", e);
		}
		return null;
	}

	public static Date toDate(String date, String format) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			LOGGER.error("ERROR @ toDate(String date,String format) Method", e);
		}
		return null;
	}

	public static Date applicationDateWithServerTime(Date applicationDate) {
		String applicationDateWithTime = DateUtil.getApplicationDateWithTime(toString(applicationDate));
		return DateUtil.toDateWithTime(applicationDateWithTime);
	}

	public static String getApplicationDateWithTime(String applicationDate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);
		Long currentTime = Calendar.getInstance().getTime().getTime();
		// formatted time as per Time Format
		String formattedTime = dateFormat.format(currentTime);
		String formattedDateWithTime = applicationDate + " " + formattedTime;

		return formattedDateWithTime;
	}

	public static Date now() {
		Date now = new Date();
		String systemDateWithTime = DateUtil.getApplicationDateWithTime(toString(now));
		return DateUtil.toDateWithTime(systemDateWithTime);
	}

	public static Date toDateWithTime(String date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(formatWithTime);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			try {
				dateFormat = new SimpleDateFormat(format);
				return dateFormat.parse(date);
			} catch (Exception e1) {
				LOGGER.error("ERROR @ toDateWithTime(String date) Method", e);
			}
		}

		return null;
	}

	public static String getCurrentDate() {

		Date dt = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String date = sdf.format(dt);

		return date;
	}

	// GET THE APPLICATION UNIQUE ID
	// IF PASS THE PARAMETER TO THIS
	// String registrationNo = DateUtil.getUniqueID("S");
	// S20260707034411

	// WITHOUT PARAMETER
	// String id = DateUtil.getUniqueID();
	// 20260707034411

	public static String getUniqueID() {
		return getUniqueID("");
	}

	public static String getUniqueID(String prefix) {

		Date dt = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);

		int year = cal.get(Calendar.YEAR);

		SimpleDateFormat ft = new SimpleDateFormat("MMddHHmmss");

		return prefix + year + ft.format(dt);
	}

	public static Integer getCurrentYear() {

		Date dt = new Date();
		int resultYear = -1;
		if (dt != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			resultYear = cal.get(Calendar.YEAR);
		}

		Integer curYear = resultYear;

		return curYear;
	}

	/**
	 * @author [TObgay]
	 * @description convert to string date [2021-01-02 to string date [02-01-2020]
	 * @param date
	 * @return Str/ng/
	 */
	public static String changeFormateToDDMMYYYY(String date) {
		if (date == null) {
			return "";
		}
		String[] date1 = date.split(" ");
		String[] date2 = date1[0].split("-");

		if (date2.length > 2) {
			String day = date2[2];
			String month = date2[1];
			String year = date2[0];

			return day + "-" + month + "-" + year;
		}
		return "";
	}

	/**
	 * @author [TObgay]
	 * @description convert to string date [2021-01-02 to string date [02/01/2020]
	 * @param date
	 * @return Str/ng/
	 */
	public static String changeFormateToDDMMYYYYwithSlash(String date) {
		if (date == null) {
			return "";
		}
		String[] date1 = date.split(" ");
		String[] date2 = date1[0].split("-");

		if (date2.length > 2) {
			String day = date2[2];
			String month = date2[1];
			String year = date2[0];

			return day + "/" + month + "/" + year;
		}
		return "";
	}

	/**
	 * @author [TObgay]
	 * @description convert to string date [16-5-2012] to string date [2012-05-16]
	 * @param date
	 * @return String
	 */
	public static String changeFormateToYYYYMMDD(String date) {
		if (date == null) {
			return "";
		}
		String[] date1 = date.split(" ");
		String[] date2 = date1[0].split("-");

		if (date2.length > 2) {
			String year = date2[2];
			String month = date2[1];
			String day = date2[0];

			return year + "-" + month + "-" + day;
		}
		return "";
	}

	/**
	 * @author [TObgay]
	 * @description convert to string date [16/5/2012] to string date [2012-05-16]
	 * @param date
	 * @return String
	 */
	public static String changeDateToYYYYMMDD(String date) {
		if (date == null) {
			return "";
		}
		String[] date1 = date.split(" ");
		String[] date2 = date1[0].split("/");

		if (date2.length > 2) {
			String year = date2[2];
			String month = date2[1];
			String day = date2[0];

			return year + "-" + month + "-" + day;
		}
		return "";
	}

	/**
	 * @author Tshering
	 * @description make Expire Date
	 * @return
	 */
	public static Date getExpiryDate(Date date) {
		Date expireDate = null;
		// current Date
		Date currentDate = DateUtil.applicationDateWithServerTime(DateUtil.now());
		// application Date
		Date applicationDate = DateUtil.applicationDateWithServerTime(date);
		// change format into dd-MMM-yyyy
		String appformatDate = DateUtil.toString(applicationDate);
		String curformatDate = DateUtil.toString(currentDate);
		Calendar cal2, cal3;
		int dayDifference, dayDifference1;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));

		try {
			// setting number of years to next available quota
			Integer expireDays = 2555;
			// calculating the date difference
			cal2 = parseTimestamp(curformatDate);
			cal3 = parseTimestamp(appformatDate);

			Date curDate = cal2.getTime();
			Date appDate = cal3.getTime();

			dayDifference = (int) ((curDate.getTime() - appDate.getTime()) / (1000 * 60 * 60 * 24));

			System.out.println("@@@APplicaiton Date value :" + appDate);
			System.out.println("@@@curDate value :" + curDate);
			System.out.println("@@@Difference in Days :" + dayDifference);

			dayDifference1 = (expireDays - dayDifference);
			System.out.println("@@@Difference in Days after deduction :" + dayDifference1);
			if (expireDays != null) {

				// cal.add(Calendar.DATE, expireDays);
				cal.add(Calendar.DATE, dayDifference1);
				Date d = new Date(cal.getTime().getTime());

				SimpleDateFormat formatter = new SimpleDateFormat(format);
				String formattedDate = formatter.format(d);
				// convert string to date type
				expireDate = DateUtil.toDate(formattedDate);

			}
		} catch (Exception e) {
			LOGGER.error("ERROR @ getExpiryDate Method", e);
			e.printStackTrace();
		}
		return expireDate;
	}

	private static Date getNow() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Calendar parseTimestamp(String timestamp) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date d = sdf.parse(timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal;
	}

	/*
	 * Function to get current year and generate unique ID by current year plus
	 * taking max number created on dated 30/07/2020
	 */

	public static Integer getCurrentYearPlusInteger() {

		Date dt = new Date();
		int resultYear = -1;
		if (dt != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			resultYear = cal.get(Calendar.YEAR);
		}

		Integer currentYear = resultYear;

		return currentYear;
	}

	// this procedure will return Year from the date provided as parameter
	// Created on date : 15/09/2020
	// Place : eCMS Office
	public static Integer getNumberOfYear(Date date, Date cCurrent_Date) {

		// application Date
		Integer nNumberOfYears = null;
		Date applicationDate = DateUtil.applicationDateWithServerTime(date);
		Date cCurrentDate = DateUtil.applicationDateWithServerTime(cCurrent_Date);

		// change format into dd-MMM-yyyy
		String appformatDate = DateUtil.toString(applicationDate);
		String cCurrentDate_Format = DateUtil.toString(cCurrentDate);

		Calendar cal2, cal3;
		int dDays;
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));

		try {
			// setting number of years to next available quota
			cal3 = parseTimestamp(appformatDate);
			cal2 = parseTimestamp(cCurrentDate_Format);
			Date curDate = cal2.getTime();
			Date appDate = cal3.getTime();

			// dayDifference = (int) ((curDate.getTime() - appDate.getTime()) / (1000 * 60 *
			// 60 * 24));
			dDays = (int) ((curDate.getTime() - appDate.getTime()) / (1000 * 60 * 60 * 24));
			nNumberOfYears = dDays / 365;

			System.out.println("@@@No. of Days :" + dDays);
			/*
			 * int ddDay=(1000 * 60 * 60 * 24); int dddDays=(int) appDate.getTime();
			 * System.out.println("@@@No. of Days :"+ddDay);
			 * System.out.println("@@@No. of ddDays :"+dddDays);
			 * System.out.println("@@@Applicaiton Date value :" + appDate);
			 * System.out.println("@@@Difference in Days :" + dDays);
			 * System.out.println("@@@DIfferent of two Date :"+(curDate.getTime() -
			 * appDate.getTime()));
			 * 
			 * System.out.println("@@@ No. of Years :"+ dDays/365);
			 * System.out.println("@@@ No. of Months :"+ (dDays % 365) / 30);
			 * System.out.println("@@@ No. of modulus :"+ (dDays % 365) % 30);
			 * System.out.println("@@@ No. of weeks :"+ (dDays % 365) / 7);
			 * System.out.println("@@@ No. of Days :"+ (dDays % 365) % 7);
			 */

			return nNumberOfYears;

		} catch (Exception e) {
			LOGGER.error("ERROR @ getNumberOfYear Method", e);
			e.printStackTrace();
			return null;
		}

	}
}
