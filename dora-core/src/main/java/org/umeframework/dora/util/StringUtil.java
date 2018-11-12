/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * String common process tool 
 *
 * @author Yue MA
 */
abstract public class StringUtil {
	/**
	 * Escape character of SQL clause
	 */
	private static final String LIKE_ESC_CHAR = "~";
	/**
	 * Text "1"
	 */
	private static final String STR_ONE = "1";
	/**
	 * Text "2"
	 */
	private static final String STR_TWO = "2";
	/**
	 * Text "0"
	 */
	private static final String ZERO = "0";
	/**
	 * Text "true"
	 */
	private static final String TRUE = "true";
	/**
	 * Empty text
	 */
	public static final String EMPTY = "";
	/**
	 * Text "+"
	 */
	private static final String PLUS_STR = "+";
	/**
	 * Text "-"
	 */
	private static final String MINUS_STR = "-";
	/**
	 * Space char
	 */
	private static final char SPACE_CHR = ' ';
	/**
	 * UTF-8 string constant
	 */
	private static final String DEFAULT_CHAR_SET = "UTF-8";
	/**
	 * Windows line wrap char sequence
	 */
	private static final String WINDOWS_LINE_SEPARATOR_EXPR = "\\\\r\\\\n";
	/**
	 * Windows LF char sequence
	 */
	private static final String LF = "\n";
	/**
	 * Space string
	 */
	public static final String SPACE = " ";

	/**
	 * isEmpty
	 *
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if (value == null || value.trim().equals(EMPTY)) {
			return true;
		}
		return false;
	}

	/**
	 * isNotEmpty
	 *
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		if (value != null && !value.trim().equals(EMPTY)) {
			return true;
		}
		return false;
	}

	/**
	 * isAllEmpty
	 *
	 * @param values
	 * @return
	 */
	public static boolean isAllEmpty(String... values) {
		for (String value : values) {
			if (value != null && !value.trim().equals(EMPTY)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isAnyEmpty
	 *
	 * @param values
	 * @return
	 */
	public static boolean isAnyEmpty(String... values) {
		for (String value : values) {
			if (value == null || value.trim().equals(EMPTY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * isSpace
	 *
	 * @param value
	 * @return
	 */
	public static boolean isSpace(char value) {
		return value == SPACE_CHR;
	}

	/**
	 * isNumeric
	 *
	 * @param value
	 * @return
	 */
	public static boolean isNumeric(String value) {
		if (value == null || EMPTY.equals(value)) {
			return false;
		}
		for (int i = value.length(); --i >= 0;) {
			if (!Character.isDigit(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * trim
	 *
	 * @param value
	 * @return
	 */
	public static String trim(String value) {
		if (value == null) {
			return null;
		}
		return value.trim();
	}

	/**
	 * trimR
	 *
	 * @param value
	 * @return
	 */
	public static String trimR(String value) {
		if (value == null) {
			return null;
		}

		int length = value.length();
		while ((0 < length) && isSpace(value.charAt(length - 1))) {
			length--;
		}

		return length < value.length() ? value.substring(0, length) : value;
	}

	/**
	 * trimL
	 *
	 * @param value
	 * @return
	 */
	public static String trimL(String value) {
		if (value == null) {
			return null;
		}

		int start = 0;
		int length = value.length();
		while ((start < length) && isSpace(value.charAt(start))) {
			start++;
		}

		return start > 0 ? value.substring(start, length) : value;
	}

	/**
	 * removeAllSpace
	 *
	 * @param value
	 * @return
	 */
	public static String removeAllSpace(String value) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) != SPACE_CHR) {
				builder.append(value.charAt(i));
			}
		}
		return builder.toString();
	}

	/**
	 * toHexString
	 *
	 * @param byteArray
	 * @param delim
	 * @return
	 */
	public static String toHexStr(byte[] byteArray, String delim) {
		if (delim == null) {
			delim = EMPTY;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < byteArray.length; i++) {
			if (i > 0) {
				builder.append(delim);
			}
			String hex = Integer.toHexString(byteArray[i] & 0x00ff).toUpperCase();
			if (hex.length() < 2) {
				builder.append(ZERO);
			}
			builder.append(hex);
		}
		return builder.toString();
	}

	/**
	 * capitalizeInitial
	 *
	 * @param value
	 * @return
	 */
	public static String capitalizeInitial(String value) {
		if (value == null || EMPTY.equals(value)) {
			return value;
		}
		char[] chars = value.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	/**
	 * escapeXML
	 *
	 * @param str
	 * @return
	 */
	public static String escapeXML(String str) {
		char[] cbuf = str.toCharArray();
		StringBuilder sbui = new StringBuilder();
		for (int i = 0; i < cbuf.length; i++) {
			if (cbuf[i] == '&') {
				sbui.append("&amp;");
			} else if (cbuf[i] == '<') {
				sbui.append("&lt;");
			} else if (cbuf[i] == '>') {
				sbui.append("&gt;");
			} else if (cbuf[i] == '"') {
				sbui.append("&quot;");
			} else if (cbuf[i] == '\'') {
				sbui.append("&apos;");
			} else {
				sbui.append(cbuf[i]);
			}
		}
		return sbui.toString();
	}

    /**
     * escapeXML
     *
     * @param str
     * @return
     */
    public static String escapeHTML(String str) {
        char[] cbuf = str.toCharArray();
        StringBuilder sbui = new StringBuilder();
        for (int i = 0; i < cbuf.length; i++) {
            if (cbuf[i] == '&') {
                sbui.append("&amp;");
            } else if (cbuf[i] == '<') {
                sbui.append("&amp;lt;");
            } else if (cbuf[i] == '>') {
                sbui.append("&amp;gt;");
            } else if (cbuf[i] == '"') {
                sbui.append("&amp;quot;");
            } else if (cbuf[i] == '\'') {
                sbui.append("&amp;apos;");
            } else {
                sbui.append(cbuf[i]);
            }
        }
        return sbui.toString();
    }

	/**
	 * escapeSqlLike
	 *
	 * @param condition
	 * @return
	 */
	public static String escapeSqlLike(String condition) {
		final char esc = (LIKE_ESC_CHAR.toCharArray())[0];
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < condition.length(); i++) {
			char chr = condition.charAt(i);
			if (chr == esc) {
				result.append(esc);
				result.append(esc);
			} else if (chr == '%' || chr == '_') {
				result.append(esc);
				result.append(chr);
			} else {
				result.append(chr);
			}
		}
		result.append('%');
		return result.toString();
	}

	/**
	 * getByteLength
	 *
	 * @param value
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static int getByteLength(String value, String charSet) throws UnsupportedEncodingException {
		if (value == null || EMPTY.equals(value)) {
			return 0;
		}

		byte[] bytes = null;
		if (charSet == null || EMPTY.equals(charSet)) {
			bytes = value.getBytes();
		} else {
			try {
				bytes = value.getBytes(charSet);
			} catch (UnsupportedEncodingException e) {
				throw e;
			}
		}
		return bytes == null ? 0 : bytes.length;
	}

	/**
	 * toInteger
	 *
	 * @param value
	 * @return
	 */
	public static Integer toInteger(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;
		return Integer.valueOf(value);
	}

	/**
	 * toLong
	 *
	 * @param value
	 * @return
	 */
	public static Long toLong(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;
		return Long.valueOf(value);
	}

	/**
	 * toShort
	 *
	 * @param value
	 * @return
	 */
	public static Short toShort(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;
		return Short.valueOf(value);
	}

	/**
	 * toDouble
	 *
	 * @param value
	 * @return
	 */
	public static Double toDouble(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;
		return Double.valueOf(value);
	}

	/**
	 * toFloat
	 *
	 * @param value
	 * @return
	 */
	public static Float toFloat(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;
		return Float.valueOf(value);
	}

	/**
	 * toBigInteger
	 *
	 * @param value
	 * @return
	 */
	public static BigInteger toBigInteger(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;
		return new BigInteger(value);
	}

	/**
	 * toBigDecimal
	 *
	 * @param value
	 * @return
	 */
	public static BigDecimal toBigDecimal(String value) {
		if (isEmpty(value)) {
			return null;
		}
		value = StringUtil.removeAllSpace(value);
		value = value.startsWith(PLUS_STR) ? value = value.substring(1) : value;

		if (isEmpty(value) || value.endsWith(MINUS_STR)) {
			return new BigDecimal(0);
		}
		return new BigDecimal(value);
	}

	/**
	 * toBool
	 *
	 * @param value
	 * @return
	 */
	public static Boolean toBoolean(String value) {
		if (isEmpty(value)) {
			return false;
		}
		value = value.trim();
		if (STR_ONE.equals(value) || TRUE.equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

    /**
     * toDate
     * 
     * @param value
     * @return
     */
    public static Date toDate(String value) {
        if (isEmpty(value)) {
            return null;
        }
        value = value.trim();
        return Date.valueOf(value);
    }
    
    /**
     * toTime
     * 
     * @param value
     * @return
     */
    public static Time toTime(String value) {
        if (isEmpty(value)) {
            return null;
        }
        value = value.trim();
        return Time.valueOf(value);
    }

    /**
     * toTimestamp
     * 
     * @param value
     * @return
     */
    public static Timestamp toTimestamp(String value) {
        if (isEmpty(value)) {
            return null;
        }
        value = value.trim();
        return Timestamp.valueOf(value);
    }
    
    /**
     * toByte
     * 
     * @param value
     * @return
     */
    public static Byte toByte(String value) {
        if (isEmpty(value)) {
            return null;
        }
        value = value.trim();
        return Byte.valueOf(value);
    }
    
    /**
	 * toObject
	 *
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toObject(String value, Class<T> clazz) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		// convert data type
		if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return (T) toBoolean(value);
		} else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return (T) toInteger(value);
		} else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			return (T) toLong(value);
		} else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
			return (T) toShort(value);
		} else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			return (T) toFloat(value);
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return (T) toDouble(value);
		} else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
			return (T) (Character) value.charAt(0);
		} else if (clazz.equals(BigInteger.class)) {
			return (T) toBigInteger(value);
		} else if (clazz.equals(BigDecimal.class)) {
			return (T) toBigDecimal(value);
        } else if (clazz.equals(Date.class)) {
            return (T) toDate(value);
        } else if (clazz.equals(Time.class)) {
            return (T) toTime(value);
        } else if (clazz.equals(Timestamp.class)) {
            return (T) toTimestamp(value);
		} else if (clazz.equals(String.class)) {
			return (T) value;
		} else {
			// return null for unidentified type
			return null;
		}
	}

	/**
	 * objectToStr
	 *
	 * @param value
	 * @return
	 */
	public static String objectToStr(Object value) {
		if (value == null) {
			return EMPTY;
		}
		if (value instanceof BigDecimal) {
			return ((BigDecimal) value).toPlainString();
		} else {
			return value.toString();
		}
	}

	/**
	 * toBytes
	 *
	 * @param value
	 * @param charSet
	 * @return
	 * @throws IOException
	 */
	public static byte[] toBytes(String value, String charSet) throws IOException {
		if (value == null) {
			return null;
		}
		return value.getBytes(charSet);
	}

	/**
	 * bytesToStr
	 *
	 * @param value
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String bytesToStr(byte[] value, String charSet) throws UnsupportedEncodingException {
		if (value == null) {
			return null;
		}

		return new String(value, charSet);
	}

	/**
	 * toBytes
	 *
	 * @param value
	 * @return
	 * @throws IOException
	 */
	public static byte[] toBytes(String value) throws IOException {
		if (value == null) {
			return null;
		}
		return value.getBytes(DEFAULT_CHAR_SET);
	}

	/**
	 * bytesToString
	 *
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String bytesToString(byte[] value) throws UnsupportedEncodingException {
		if (value == null) {
			return null;
		}
		return new String(value, DEFAULT_CHAR_SET);
	}

	/**
	 * bytesToString
	 *
	 * @param value
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String bytesToStringByCharSet(byte[] value, String charset) throws UnsupportedEncodingException {
		if (value == null) {
			return null;
		}
		return new String(value, charset);
	}

	/**
	 * paddingR
	 *
	 * @param value
	 * @param size
	 * @param useChar
	 * @return
	 */
	public static String paddingR(String value, int size, char useChar) {
		if (value.length() >= size) {
			return value;
		}
		StringBuilder builder = new StringBuilder(value);
		for (int i = 0; i < size - value.length(); i++) {
			builder.append(useChar);
		}
		return builder.toString();
	}

	/**
	 * paddingR
	 *
	 * @param value
	 * @param size
	 * @return
	 */
	public static String paddingR(String value, int size) {
		return paddingR(value, size, SPACE_CHR);
	}

	/**
	 * paddingL
	 *
	 * @param value
	 * @param size
	 * @param useChar
	 * @return
	 */
	public static String paddingL(String value, int size, char useChar) {
		if (value.length() >= size) {
			return value;
		}
		StringBuilder builder = new StringBuilder(size);
		for (int i = 0; i < size - value.length(); i++) {
			builder.append(useChar);
		}
		builder.append(value);
		return builder.toString();
	}

	/**
	 * paddingL
	 *
	 * @param value
	 * @param size
	 * @return
	 */
	public static String paddingL(String value, int size) {
		return paddingL(value, size, SPACE_CHR);
	}

	/**
	 * paddingZR
	 *
	 * @param value
	 * @param num
	 * @return
	 */
	public static String paddingZR(String value, int num) {
		return paddingZR(value, num, SPACE_CHR);
	}

	/**
	 * rpaddingForChar
	 *
	 * @param value
	 * @param num
	 * @param useChar
	 * @return
	 */
	public static String paddingZR(String value, int num, char useChar) {
		if (value.length() >= num) {
			return value;
		}
		int zenkakuSize = value.getBytes().length - value.length();
		int useSize = 1;
		if (String.valueOf(useChar).getBytes().length > 1) {
			useSize = 2;
		}

		StringBuilder builder = new StringBuilder(value);
		for (int i = 0; i < num - zenkakuSize / 2 - value.length(); i = i + useSize) {
			builder.append(String.valueOf(useChar));
		}
		return builder.toString();
	}

	/**
	 * paddingZL
	 *
	 * @param value
	 * @param num
	 * @return
	 */
	public static String paddingZL(String value, int num) {
		return paddingZL(value, num, SPACE_CHR);
	}

	/**
	 * paddingZL
	 *
	 * @param value
	 * @param num
	 * @param useChar
	 * @return
	 */
	public static String paddingZL(String value, int num, char useChar) {
		if (value.length() >= num) {
			return value;
		}
		int zenkakuSize = value.getBytes().length - value.length();
		int useSize = 1;
		if (String.valueOf(useChar).getBytes().length > 1) {
			useSize = 2;
		}
		StringBuilder builder = new StringBuilder(num);
		for (int i = 0; i < num - zenkakuSize / 2 - value.length(); i = i + useSize) {
			builder.append(useChar);
		}
		builder.append(value);
		return builder.toString();
	}

	/**
	 * toHashSHA1
	 *
	 * @param value
	 * @return
	 */
	public static byte[] toHashSHA1(String value) {
		try {
			return toHash("SHA1", value);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * toHash
	 *
	 * @param rule
	 * @param value
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] toHash(String rule, String value) throws NoSuchAlgorithmException {
		if (rule == null || value == null) {
			return null;
		}
		MessageDigest md = MessageDigest.getInstance(rule.toUpperCase());
		return md.digest(value.getBytes());
	}

	/**
	 * bytesToHexStr
	 *
	 * @param src
	 * @return
	 */
	public static String bytesToHexStr(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * hexStrToBytes
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] hexStrToBytes(String hexStr) {
		if (hexStr == null || hexStr.equals("")) {
			return null;
		}
		hexStr = hexStr.toUpperCase();
		int length = hexStr.length() / 2;
		char[] hexChars = hexStr.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			if (charToByte(hexChars[pos]) == -1) {
				return null;
			}
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * charToByte
	 *
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * getArrayByLinefeedCode
	 *
	 * @param input
	 * @return
	 */
	public static String[] getArrayByLinefeedCode(String input) {
		return input == null ? null : input.split(WINDOWS_LINE_SEPARATOR_EXPR);
	}

	/**
	 * removeLinefeedCode
	 *
	 * @param input
	 * @return
	 */
	public static String removeLinefeedCode(String input) {
		return input == null ? null : input.replace(LF, EMPTY);
	}

	/**
	 * splitByNum
	 *
	 * @param input
	 * @param num
	 * @return
	 */
	public static String[] splitByNum(String input, int num) {

		int i = Integer.valueOf(new BigDecimal(input.length()).divide(new BigDecimal(num), BigDecimal.ROUND_UP).toString());
		String[] output = new String[i];

		for (int j = 0; j < i; j++) {
			if (num > input.length()) {
				output[j] = input;
			} else {
				output[j] = input.substring(0, num);
				input = input.substring(num);
			}

		}
		return output;
	}

	/**
	 * splitByByteNum
	 *
	 * @param input
	 * @param num
	 * @return
	 */
	public static String[] splitByByteNum(String input, int num) {

		byte[] inputByteArray = input.getBytes();
		int i = Integer.valueOf(new BigDecimal(inputByteArray.length).divide(new BigDecimal(num), BigDecimal.ROUND_UP).toString());
		String[] output = new String[i];
		int index = 0;
		for (int j = 0; j < i; j++) {
			if (index + num > inputByteArray.length) {
				output[j] = new String(inputByteArray, index, inputByteArray.length - index);
			} else {
				output[j] = new String(inputByteArray, index, num);
				index = index + num;
			}

		}
		return output;
	}

	/**
	 * trimStr
	 *
	 * @param value
	 * @param mode
	 * @param str
	 * @return
	 */
	public static String trimStr(String value, int mode, String str) {

		if (value == null || str == null || str.length() > value.length()) {
			return value;
		}
		if (value.indexOf(str) == 0 && mode == 0) {
			value = value.replaceFirst(str, EMPTY);
		} else if (value.lastIndexOf(str) == value.length() - str.length() && mode == 1) {
			value = value.substring(0, value.length() - str.length());
		} else if (mode == 2) {
			value = value.replace(str, EMPTY);
		}

		return value;
	}

	/**
	 * mergeString
	 *
	 * @param template
	 * @param keys
	 * @return
	 */
	public static String mergeStr(String template, final String... keys) {
		if (keys == null) {
			return null;
		}
		StringBuilder resultString = new StringBuilder();
		List<Integer> tempList = new ArrayList<Integer>();
		for (int i = 0; i < keys.length; i++) {
			if (template == null) {
				resultString.append(keys[i]);
			} else {
				for (int j = 0; j < keys[i].length(); j++) {
					char c = keys[i].charAt(j);
					tempList.add(template.indexOf(c));
				}
			}
		}
		if (CollectionUtil.isNotEmpty(tempList)) {
			List<Integer> sortedList = CollectionUtil.sortAsc(tempList);
			for (int k = 0; k < sortedList.size(); k++) {
				int index = sortedList.get(k);
				if (index != -1) {
					resultString.append(template.charAt(index));
				}
			}
		}
		return resultString.toString();
	}

	/**
	 * mergeStrToList
	 *
	 * @param mode
	 * @param inputArrays
	 * @return
	 */
	public static List<String> mergeStrToList(String mode, String[]... inputArrays) {
		List<String> outList = null;
		if (inputArrays != null) {
			outList = new ArrayList<String>();
			for (String[] input : inputArrays) {
				if (input != null) {
					for (String value : input) {
						if (isNotEmpty(value) && !outList.contains(trimR(value))) {
							outList.add(trimR(value));
						}
					}
				}
			}
		}
		if (STR_ONE.equals(mode)) {
			outList = CollectionUtil.sortAsc(outList);
		} else if (STR_TWO.equals(mode)) {
			outList = CollectionUtil.sortDesc(outList);
		}

		return outList;
	}
}
