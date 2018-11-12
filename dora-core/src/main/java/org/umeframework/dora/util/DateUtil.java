/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.validator.GenericTypeValidator;

/**
 * Common tool for Date type handling
 *
 * @author Yue MA
 */
abstract public class DateUtil {
    /**
     * empty string
     */
    private static final String EMPTY = "";

    /**
     * default max date value:1000-01-01
     */
    private static final String MAX_DATE = "9999-12-31";

    /**
     * default max date value:1000-01-01
     */
    private static final String MIN_DATE = "1000-01-01";

    /**
     * default max time stamp:9999-12-31 23:59:59.999999
     */
    private static final String MAX_TIMESTAMP = "9999-12-31 23:59:59.999999";

    /**
     * default min time stamp:1000-01-01 00:00:00.000000
     */
    private static final String MIN_TIMESTAMP = "1000-01-01 00:00:00.000000";

    /**
     * Date format pre-define
     */
    public static enum FORMAT {
        /**
         * [yyyy-MM-dd HH:mm:ss.SSS]
         */
        YYYYMMDDHHMMSS_SSSHyphen {
            @Override
            public String toString() {
                return "yyyy-MM-dd HH:mm:ss.SSS";
            }
        },
        /**
         * [yyyyMMddHHmmssSSS]
         */
        YYYYMMDDHHMMSSMMM {
            @Override
            public String toString() {
                return "yyyyMMddHHmmssSSS";
            }
        },
        /**
         * [yyMMddHHmmssSSS]
         */
        YYMMDDHHMMSSMMM {
            @Override
            public String toString() {
                return "yyMMddHHmmssSSS";
            }
        },
        /**
         * [yyyyMMddHHmmss]
         */
        YYYYMMDDHHMMSS {
            @Override
            public String toString() {
                return "yyyyMMddHHmmss";
            }
        },
        /**
         * [yyMMddHHmmss]
         */
        YYMMDDHHMMSS {
            @Override
            public String toString() {
                return "yyMMddHHmmss";
            }
        },
        /**
         * [yyyyMMdd]
         */
        YYYYMMDD {
            @Override
            public String toString() {
                return "yyyyMMdd";
            }
        },
        /**
         * [yyMMdd]
         */
        YYMMDD {
            @Override
            public String toString() {
                return "yyMMdd";
            }
        },
        /**
         * [HH:mm:ss]
         */
        HHcolonMMcolonSS {
            @Override
            public String toString() {
                return "HH:mm:ss";
            }
        },
        /**
         * [yyyy-MM-dd]
         */
        YYYYMMDDHyphen {
            @Override
            public String toString() {
                return "yyyy-MM-dd";
            }
        },
        /**
         * [yyyy-MM-dd HH:mm:ss]
         */
        YYYYMMDDHHMMSSHyphen {
            @Override
            public String toString() {
                return "yyyy-MM-dd HH:mm:ss";
            }
        }
    };

    /**
     * Convert string to date.
     *
     * @param value
     *            - string date object
     * @param pattern
     *            - date format pattern
     * @param strict
     *            - convert strict
     * @return date object
     */
    public static Date toDate(
            String value,
            String pattern,
            String strict) {
        if (value == null || value.trim().equals(EMPTY)) {
            return null;
        }

        Date result = null;

        if (pattern != null && pattern.length() > 0) {
            result = GenericTypeValidator.formatDate(value, pattern, false);
        } else if (strict != null && strict.length() > 0) {
            result = GenericTypeValidator.formatDate(value, strict, true);
        } else {
            throw new IllegalArgumentException("datePattern or datePatternStrict must be specified.");
        }

        return result;
    }

    /**
     * Convert time stamp to date
     *
     * @param value
     *            - time stamp object
     * @return java.sql.Date object
     */
    public static java.sql.Date toDate(
            Timestamp value) {
        java.sql.Date result = null;

        if (value != null) {
            result = new java.sql.Date(value.getTime());
        }

        return result;
    }

    /**
     * Convert date to string
     *
     * @param value
     *            - date object
     * @param format
     *            - date format
     * @param locale
     *            - locale
     * @return string date object
     */
    public static String dateToString(
            java.util.Date value,
            FORMAT format,
            Locale locale) {
        locale = locale == null ? Locale.ENGLISH : locale;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format.toString(), locale);
        dateFormat.setLenient(false);
        String result = null;
        try {
            result = dateFormat.format(value);
        } catch (Exception e) {
            result = null;
        }
        return result != null ? result.toUpperCase() : result;
    }

    /**
     * convert date to string
     *
     * @param value
     *            - date object
     * @param format
     *            - date format
     * @return string date object
     */
    public static String dateToString(
            java.util.Date value,
            FORMAT format) {
        return dateToString(value, format, null);
    }

    /**
     * Parse string to date
     *
     * @param value
     *            - string object
     * @param format
     *            - date format
     * @param locale
     *            - locale
     * @return date object
     */
    public static java.util.Date parseToDate(
            String value,
            FORMAT format,
            Locale locale) {
        locale = locale == null ? Locale.ENGLISH : locale;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format.toString(), locale);
    	dateFormat.setLenient(false);
        java.util.Date result = null;
        try {
            result = dateFormat.parse(value);
        } catch (ParseException e) {
            result = null;
        }
        return result;
    }

    /**
     * Parse string to date
     *
     * @param value
     *            - string object
     * @param format
     *            - date format
     * @return date object
     */
    public static java.util.Date parseToDate(
            String value,
            FORMAT format) {
        return parseToDate(value, format, null);
    }

    /**
     * Parse string to time
     *
     * @param value
     *            - string object
     * @param format
     *            - time format
     * @return time object
     */
    public static java.sql.Time parseToTime(
            String value,
            FORMAT format) {

        return new java.sql.Time(parseToDate(value, format, null).getTime());
    }

    /**
     * Parse string to time stamp
     *
     * @param value
     *            - string object
     * @param format
     *            - time stamp format
     * @return time stamp object
     */
    public static java.sql.Timestamp parseToTimestamp(
            String value,
            FORMAT format) {

        return new java.sql.Timestamp(parseToDate(value, format, null).getTime());
    }

    /**
     * Convert date string by input from format and to format
     *
     * @param value
     *            - date string object
     * @param fromFormat
     *            - from format
     * @param toFormat
     *            - to format
     * @return date string after convert
     */
    public static String convertFormat(
            String value,
            FORMAT fromFormat,
            FORMAT toFormat) {
        java.util.Date date = parseToDate(value, fromFormat, null);
        return dateToString(date, toFormat);
    }

    /**
     * Convert date string by input from format and to format
     *
     * @param value
     *            - date string object
     * @param fromFormat
     *            - from format
     * @param toFormat
     *            - to format
     * @param locale
     *            - locale
     * @return date string after convert
     */
    public static String convertFormat(
            String value,
            FORMAT fromFormat,
            FORMAT toFormat,
            Locale locale) {
        java.util.Date date = parseToDate(value, fromFormat, locale);
        return dateToString(date, toFormat);
    }

    /**
     * Compare string date object
     *
     * @param strDate1
     *            - 1st string object
     * @param strDate2
     *            - 2nd string object
     * @param format
     *            - date format
     * @return compare result
     */
    public static int compare(
            String strDate1,
            String strDate2,
            FORMAT format) {
        // compare null
        if (strDate1 == null || strDate2 == null) {
            return 0;
        }
        java.util.Date date1 = parseToDate(strDate1, format, null);
        java.util.Date date2 = parseToDate(strDate2, format, null);
        return date1.compareTo(date2);
    }

    /**
     * Compare date object
     *
     * @param date1
     *            - 1st date object
     * @param date2
     *            - 2nd date object
     * @return compare result
     */
    public static int compare(
            java.util.Date date1,
            java.util.Date date2) {
        // compare null
        if (date1 == null || date2 == null) {
            return 0;
        }
        return date1.compareTo(date2);
    }

    /**
     * Compare time object
     *
     * @param time1
     *            - 1st time object
     * @param time2
     *            - 2nd time object
     * @return compare result
     */
    public static int compare(
            java.sql.Time time1,
            java.sql.Time time2) {
        // compare null
        if (time1 == null || time2 == null) {
            return 0;
        }
        java.sql.Time newTime1 = parseToTime(dateToString(time1, FORMAT.HHcolonMMcolonSS), FORMAT.HHcolonMMcolonSS);
        java.sql.Time newTime2 = parseToTime(dateToString(time2, FORMAT.HHcolonMMcolonSS), FORMAT.HHcolonMMcolonSS);
        return newTime1.compareTo(newTime2);
    }

    /**
     * Compare time stamp object
     *
     * @param time1
     *            - 1st time stamp object
     * @param time2
     *            - 2nd time stamp object
     * @return compare result
     */
    public static int compare(
            java.sql.Timestamp timestamp1,
            java.sql.Timestamp timestamp2) {
        // compare null
        if (timestamp1 == null || timestamp2 == null) {
            return 0;
        }
        return timestamp1.compareTo(timestamp2);
    }

    /**
     * Get year string from date string
     *
     * @param value
     *            - date string object
     * @param format
     *            - date format
     * @return year part string
     */
    public static String getYear(
            String value,
            FORMAT format) {
        String str = convertFormat(value, format, FORMAT.YYYYMMDDHHMMSSMMM);
        return str.substring(0, 4);
    }

    /**
     * Get year string from date
     *
     * @param value
     *            - date object
     * @return year part string
     */
    public static String getYear(
            Date value) {
        return getYear(dateToString(value, FORMAT.YYYYMMDDHHMMSSMMM), FORMAT.YYYYMMDDHHMMSSMMM);
    }

    /**
     * Get month string from date string
     *
     * @param value
     *            - date string object
     * @param format
     *            - date format
     * @return month part string
     */
    public static String getMonth(
            String value,
            FORMAT format) {
        String str = convertFormat(value, format, FORMAT.YYYYMMDDHHMMSSMMM);
        return str.substring(4, 6);
    }

    /**
     * Get month string from date
     *
     * @param value
     *            - date object
     * @return month part string
     */
    public static String getMonth(
            Date value) {
        return getMonth(dateToString(value, FORMAT.YYYYMMDDHHMMSSMMM), FORMAT.YYYYMMDDHHMMSSMMM);
    }

    /**
     * Get day string from date string
     *
     * @param value
     *            - date string object
     * @param format
     *            - date format
     * @return day part string
     */
    public static String getDay(
            String value,
            FORMAT format) {
        String str = convertFormat(value, format, FORMAT.YYYYMMDDHHMMSSMMM);
        return str.substring(6, 8);
    }

    /**
     * Get day string from date
     *
     * @param value
     *            - date object
     * @return day part string
     */
    public static String getDay(
            Date value) {
        return getDay(dateToString(value, FORMAT.YYYYMMDDHHMMSSMMM), FORMAT.YYYYMMDDHHMMSSMMM);
    }

    /**
     * Get hour string from date string
     *
     * @param value
     *            - date string object
     * @param format
     *            - date format
     * @return hour part string
     */
    public static String getHour(
            String value,
            FORMAT format) {
        String str = convertFormat(value, format, FORMAT.YYYYMMDDHHMMSS);
        return str.substring(8, 10);
    }

    /**
     * Get hour string from date
     *
     * @param value
     *            - date object
     * @return hour part string
     */
    public static String getHour(
            Date value) {
        return getHour(dateToString(value, FORMAT.YYYYMMDDHHMMSSMMM), FORMAT.YYYYMMDDHHMMSSMMM);
    }

    /**
     * Get minute string from date string
     *
     * @param value
     *            - date string object
     * @param format
     *            - date format
     * @return minute part string
     */
    public static String getMinute(
            String value,
            FORMAT format) {
        String str = convertFormat(value, format, FORMAT.YYYYMMDDHHMMSS);
        return str.substring(10, 12);
    }

    /**
     * Get minute string from date
     *
     * @param value
     *            - date object
     * @return minute part string
     */
    public static String getMinute(
            Date value) {
        return getMinute(dateToString(value, FORMAT.YYYYMMDDHHMMSSMMM), FORMAT.YYYYMMDDHHMMSSMMM);
    }

    /**
     * Parse date string to calendar object
     *
     * @param value
     *            - date string object
     * @param format
     *            - date format
     * @return calendar object
     */
    public static Calendar parseToCalendar(
            String value,
            FORMAT format) {
        Date date = parseToDate(value, format, null);
        Calendar calender = null;
        if (date != null) {
            calender = new GregorianCalendar();
            calender.setTime(date);
        }
        return calender;
    }

    /**
     * Get date object after input date and intervals
     *
     * @param date
     *            - old date
     * @param day
     *            - intervals
     * @return new date
     */
    public static java.sql.Date getDateAfterDay(
            java.util.Date date,
            int day) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.DAY_OF_MONTH, day);
        return new java.sql.Date(cale.getTimeInMillis());
    }

    /**
     * Get date object after input date and intervals
     *
     * @param date
     *            - old date
     * @param month
     *            - intervals
     * @return new date
     */
    public static java.sql.Date getDateAfterMonth(
            java.util.Date date,
            int month) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, month);
        return new java.sql.Date(cale.getTimeInMillis());
    }

    /**
     * Get date object after input date and intervals
     *
     * @param date
     *            - old date
     * @param year
     *            - intervals
     * @return new date
     */
    public static java.sql.Date getDateAfterYear(
            java.util.Date date,
            int year) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.YEAR, year);
        return new java.sql.Date(cale.getTimeInMillis());
    }

    /**
     * Get difference days
     *
     * @param fromDate
     *            - 1st date
     * @param toDate
     *            - 2nd date
     * @return difference days
     */
    public static int getDifferenceDay(
            java.util.Date fromDate,
            java.util.Date toDate) {

        final int int1000 = 1000;
        final int int60 = 60;
        final int int24 = 24;

        boolean minus = false;
        long gap = toDate.getTime() - fromDate.getTime();
        if (gap < 0) {
            minus = true;
            gap = 0 - gap;
        }
        int rtn = (int) (gap / (int1000 * int60 * int60 * int24));
        if (minus) {
            rtn = 0 - rtn;
        }
        return rtn;
    }

    /**
     * Get difference days (only day part)
     *
     * @param fromDate
     *            - 1st date
     * @param toDate
     *            - 2nd date
     * @return difference days
     */
    public static int getDifferenceDayOnly(
            java.util.Date fromDate,
            java.util.Date toDate) {

        Integer ageDay = -1;

        if (fromDate == null || toDate == null) {
            return 0;
        }

        if (fromDate.equals(toDate)) {
            return 0;
        }
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar beforeEnd = Calendar.getInstance();
        start.setTime(fromDate);
        end.setTime(toDate);
        beforeEnd.setTime(DateUtil.getDateAfterMonth(toDate, -1));
        if (end.after(start)) {
            Integer startMonthDay = start.get(Calendar.DAY_OF_MONTH);
            Integer endMonthDay = end.get(Calendar.DAY_OF_MONTH);
            ageDay = endMonthDay - startMonthDay;
            if (startMonthDay > endMonthDay) {
                ageDay = beforeEnd.getActualMaximum(Calendar.DAY_OF_MONTH) - startMonthDay + endMonthDay;
            }
        } else {
            ageDay = -getDifferenceDayOnly(toDate, fromDate);
        }
        return ageDay;
    }

    /**
     * Get difference months
     *
     * @param fromDate
     *            - 1st date
     * @param toDate
     *            - 2nd date
     * @return difference months
     */
    public static int getDifferenceMonth(
            java.util.Date fromDate,
            java.util.Date toDate) {
        if (fromDate.compareTo(toDate) <= 0) {
            return computedifMonth(fromDate, toDate);
        } else {
            return -computedifMonth(toDate, fromDate);
        }
    }

    /**
     * Get difference years
     *
     * @param fromDate
     *            - 1st date
     * @param toDate
     *            - 2nd date
     * @return difference years
     */
    public static int getDifferenceYear(
            java.util.Date fromDate,
            java.util.Date toDate) {
        if (fromDate.compareTo(toDate) <= 0) {
            return compDate(fromDate, toDate);
        } else {
            return -compDate(toDate, fromDate);
        }
    }

    /**
     * Compare dates
     *
     * @param date1
     *            - 1st date object
     * @param date2
     *            - 2nd date object
     * @return
     */
    private static int compDate(
            Date date1,
            Date date2) {
        Calendar sCal = Calendar.getInstance();
        sCal.setTime(date1);
        Calendar eCal = Calendar.getInstance();
        eCal.setTime(date2);
        int result = eCal.get(Calendar.YEAR) - sCal.get(Calendar.YEAR);
        sCal.set(Calendar.YEAR, eCal.get(Calendar.YEAR));
        int ret = sCal.compareTo(eCal);
        if (0 < ret) {
            result -= 1;
        }
        return result;
    }

    /**
     * Get difference months
     *
     * @param fromDate
     *            - 1st date
     * @param toDate
     *            - 2nd date
     * @return difference months
     */
    private static int computedifMonth(
            Date fromDate,
            Date toDate) {
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(fromDate);
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(toDate);
        int fromDay = calendarFrom.get(Calendar.DAY_OF_MONTH);
        int toDay = calendarTo.get(Calendar.DAY_OF_MONTH);
        int fromDtMonth = getMonthNum(fromDate);
        int toDtMonth = getMonthNum(toDate);
        if (fromDay <= toDay) {
            return (toDtMonth - fromDtMonth);
        } else {
            if ((toDtMonth - fromDtMonth) == 0) {
                return 0;
            } else {
                return (toDtMonth - fromDtMonth - 1);
            }
        }
    }

    /**
     * Compute months of date
     *
     * @param date
     *            - date object
     * @return months
     */
    private static int getMonthNum(
            Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int intNum = 12;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return year * intNum + month;
    }

    /**
     * Get last day of month
     *
     * @param date
     *            - date object
     * @return
     */
    public static Date getEndOfMonth(
            Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int end = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DATE, end);
        return cal.getTime();

    }

    /**
     * Get max date object
     *
     * @return date
     */
    public static java.sql.Date getMaxDate() {
        return java.sql.Date.valueOf(MAX_DATE);

    }

    /**
     * Get min date object
     *
     * @return date
     */
    public static java.sql.Date getMinDate() {
        return java.sql.Date.valueOf(MIN_DATE);

    }

    /**
     * Get max time stamp object
     *
     * @return time stamp
     */
    public static java.sql.Timestamp getMaxTimestamp() {
        return java.sql.Timestamp.valueOf(MAX_TIMESTAMP);

    }

    /**
     * Get min time stamp object
     *
     * @return time stamp
     */
    public static java.sql.Timestamp getMinTimestamp() {
        return java.sql.Timestamp.valueOf(MIN_TIMESTAMP);

    }

}
