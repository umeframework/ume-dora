/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.UrlValidator;

/**
 * CommonValidationUtil
 *
 * @author Yue MA
 */
@SuppressWarnings("deprecation")
public class ValidatorUtil {
	/**
	 * isEmpty
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNull(
			Object value) {
		return value == null;
	}

	/**
	 * isEmpty
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(
			Object value) {
		return value == null || "".equals(value.toString());
	}

	/**
	 * isInRange
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isInRange(
			Object value,
			String min,
			String max) {
		if (value == null) {
			return true;
		}
		if (value instanceof String) {
			String sv = (String) value;
			return sv.compareTo(min) >= 0 && sv.compareTo(max) <= 0;
		} else if (value instanceof Float || value instanceof Double) {
			Double v = Double.valueOf(value.toString());
			Double vmin = Double.valueOf(min);
			Double vmax = Double.valueOf(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		} else if (value instanceof Short || value instanceof Integer || value instanceof Long) {
			Long v = Long.valueOf(value.toString());
			Long vmin = Long.valueOf(min);
			Long vmax = Long.valueOf(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		} else if (value instanceof BigDecimal) {
			BigDecimal v = (BigDecimal) value;
			BigDecimal vmin = new BigDecimal(min);
			BigDecimal vmax = new BigDecimal(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		} else if (value instanceof BigInteger) {
			BigInteger v = (BigInteger) value;
			BigInteger vmin = new BigInteger(min);
			BigInteger vmax = new BigInteger(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		} else if (value instanceof java.sql.Date) {
			java.sql.Date v = (java.sql.Date) value;
			java.sql.Date vmin = java.sql.Date.valueOf(min);
			java.sql.Date vmax = java.sql.Date.valueOf(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		} else if (value instanceof java.sql.Time) {
			java.sql.Time v = (java.sql.Time) value;
			java.sql.Time vmin = java.sql.Time.valueOf(min);
			java.sql.Time vmax = java.sql.Time.valueOf(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		} else if (value instanceof java.sql.Timestamp) {
			java.sql.Timestamp v = (java.sql.Timestamp) value;
			java.sql.Timestamp vmin = java.sql.Timestamp.valueOf(min);
			java.sql.Timestamp vmax = java.sql.Timestamp.valueOf(max);
			return v.compareTo(vmin) >= 0 && v.compareTo(vmax) <= 0;
		}
		return false;
	}

	/**
	 * isAlphaNumericString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAlphaNumeric(
			String value) {
		return matchedRegExp(value, "^([0-9]|[a-z]|[A-Z])*$");
	}

	/**
	 * isAllAlphaNumericString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAllAlphaNumeric(
			String value) {
		return matchedRegExp(value, "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])");
	}

	/**
	 * isHasAlphaNumericString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean hasAlphaNumeric(
			String value) {
		return matchedRegExp(value, "^(?=.*[0-9])(?=.*[a-zA-Z])");
	}

	/**
	 * isHankakuString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isHankaku(
			String value) {
		return matchedRegExp(value, "[ -~锝�-锞焆");
	}

	/**
	 * isZenkakuString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isZenkaku(
			String value) {
		return matchedRegExp(value, "[^ -~锝�-锞焆");
	}

	/**
	 * isUpperAlphaNumericString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAlphaNumericUpperCase(
			String value) {
		return matchedRegExp(value, "^([0-9]|[A-Z])*$");
	}

	/**
	 * isAlphaNumericLowCase
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAlphaNumericLowCase(
			String value) {
		return matchedRegExp(value, "^([0-9]|[a-z])*$");
	}

	/**
	 * isNumericString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isNumeric(
			String value) {
		return matchedRegExp(value, "^-?([0-9])*$");
	}

	/**
	 * isDecimalString
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDecimal(
			String value) {
		value = value.contains(".") ? value.replace(".", "") : value;
		return isNumeric(value);
	}

	/**
	 * isInteger
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isInteger(
			String value) {
		return matchedRegExp(value, "^-?([0-9])*$");
	}

	/**
	 * isMoneyString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isCurrency(
			String value) {
		boolean ret = matchedRegExp(value, "^[0-9]+[\\.][0-9]{0,2}$");
		if (!ret) {
			ret = matchedRegExp(value, "^([0-9])*$");
		}
		return ret;

	}

	/**
	 * isDateString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isDate(
			String value) {
		return matchedRegExp(value, "^([0-9]{4})-([0-9]{2})-([0-9]{2})$");
	}

	/**
	 * isNumber
	 *
	 * @param value
	 * @param integerLength
	 * @param isAccordedInteger
	 * @param scaleLength
	 * @param isAccordedScale
	 * @return true/false
	 */
	public static boolean isNumber(
			BigDecimal value,
			int integerLength,
			boolean isAccordedInteger,
			int scaleLength,
			boolean isAccordedScale) {
		if (value == null) {
			return true;
		}
		BigInteger bi = value.toBigInteger().abs();
		int length = bi.toString().length();
		if (!checkNumberFigures(length, integerLength, isAccordedInteger)) {
			return false;
		}

		int scale = value.scale();
		if (!checkNumberFigures(scale, scaleLength, isAccordedScale)) {
			return false;
		}

		return true;
	}

	/**
	 * matchedDateFormat
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static boolean matchedDateFormat(
			String value,
			String format) {
		if (value == null) {
			return true;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setLenient(false);
			dateFormat.parse(value);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * checkNumberFigures
	 *
	 * @param length
	 * @param checkLength
	 * @param accorded
	 * @return true/false
	 */
	protected static boolean checkNumberFigures(
			int length,
			int checkLength,
			boolean accorded) {
		if (length > checkLength) {
			return false;
		}

		if (accorded) {
			if (length != checkLength) {
				return false;
			}
		}
		return true;
	}

	/**
	 * hasNotProhibitedChar
	 *
	 * @param value
	 * @param prohibitedChars
	 * @return true/false
	 */
	public static boolean hasNotProhibitedChar(
			String value,
			String prohibitedChars) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}

		char[] chars = value.toCharArray();

		if (prohibitedChars == null || "".equals(prohibitedChars)) {
			return true;
		}

		for (int i = 0; i < chars.length; i++) {
			if (prohibitedChars.indexOf(chars[i]) >= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * hasOnlyPermissionChar
	 *
	 * @param value
	 * @param permissionChars
	 * @return true/false
	 */
	public static boolean hasOnlyPermissionChar(
			String value,
			String permissionChars) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}

		char[] chars = value.toCharArray();

		if (permissionChars == null || "".equals(permissionChars)) {
			return true;
		}

		char[] permissionArray = permissionChars.toCharArray();

		boolean permissionFlag = false;
		for (Character targetChar : chars) {
			permissionFlag = false;
			for (Character permissionChar : permissionArray) {
				if (permissionChar.equals(targetChar)) {
					permissionFlag = true;
					break;
				}
			}
			if (!permissionFlag) {
				break;
			}

		}

		return permissionFlag;
	}

	/**
	 * isArrayInRange
	 *
	 * @param obj
	 * @param min
	 * @param max
	 * @return true/false
	 */
	public static boolean isArrayInRange(
			Object obj,
			int min,
			int max) {
		int targetLength = 0;
		if (obj == null) {
			targetLength = 0;
		} else if (obj instanceof Collection) {
			targetLength = ((Collection<?>) obj).size();
		} else if (obj.getClass().isArray()) {
			targetLength = Array.getLength(obj);
		} else {
			throw new IllegalArgumentException(obj.getClass().getName() + " is neither Array nor Collection.");
		}

		if (!GenericValidator.isInRange(targetLength, min, max)) {
			return false;
		}
		return true;
	}

	/**
	 * isUrl
	 *
	 * @param value
	 * @param allowallschemes
	 * @param allow2slashes
	 * @param nofragments
	 * @param schemesVar
	 * @return true/false
	 */
	public static boolean isURL(
			String value,
			boolean allowallschemes,
			boolean allow2slashes,
			boolean nofragments,
			String schemesVar) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}

		int options = 0;
		if (allowallschemes) {
			options += UrlValidator.ALLOW_ALL_SCHEMES;
		}
		if (allow2slashes) {
			options += UrlValidator.ALLOW_2_SLASHES;
		}
		if (nofragments) {
			options += UrlValidator.NO_FRAGMENTS;
		}

		if (options == 0 && schemesVar == null) {
			if (GenericValidator.isUrl(value)) {
				return true;
			}
			return false;
		}

		String[] schemes = null;
		if (schemesVar != null) {

			StringTokenizer st = new StringTokenizer(schemesVar, ",");
			schemes = new String[st.countTokens()];

			int i = 0;
			while (st.hasMoreTokens()) {
				schemes[i++] = st.nextToken().trim();
			}
		}
		UrlValidator urlValidator = new UrlValidator(schemes, options);
		if (urlValidator.isValid(value)) {
			return true;
		}
		return false;
	}

	/**
	 * isByteInRange
	 *
	 * @param value
	 * @param charset
	 * @param min
	 * @param max
	 * @return true/false
	 */
	public static boolean isByteInRange(
			String value,
			String charset,
			int min,
			int max) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}

		// get byte size
		int bytesLength = 0;
		try {
			bytesLength = StringUtil.getByteLength(value, charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		// check logic
		if (!GenericValidator.isInRange(bytesLength, min, max)) {
			return false;
		}
		return true;
	}

	/**
	 * isSizeInRange
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isSizeInRange(
			Object value,
			int min,
			int max) {
		if (value == null) {
			return true;
		}
		int size = value.toString().length();
		return size >= min && size <= max;
	}

	/**
	 * isAsciiChar
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAsciiChar(
			char value) {
		return value >= '\u0020' && value <= '\u007e';
	}

	/**
	 * isAsciiNSChar
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAsciiNSChar(
			char value) {
		return value >= '\u0021' && value <= '\u007e';
	}

	/**
	 * isAlphaString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAlpha(
			String value) {
		return matchedRegExp(value, "^([a-z]|[A-Z])*$");
	}

	/**
	 * isHexNumericString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isHexNumeric(
			String value) {
		return matchedRegExp(value, "^([0-9]|[a-f]|[A-F])*$");
	}

	/**
	 * isAsciiString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAscii(
			String value) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!isAsciiChar(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isAsciiNSString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isAsciiNS(
			String value) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!isAsciiNSChar(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * isTelNumberString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isTelNumber(
			String value) {
		if (StringUtils.isEmpty(value)) {
			return true;
		}
		// remove <* #> from head
		while (value.startsWith("*") || value.startsWith("#")) {
			value = value.substring(1);
		}
		// remove <* #> from end
		while (value.endsWith("*") || value.endsWith("#")) {
			value = value.substring(0, value.length() - 1);
		}
		return isNumeric(value);
	}

	/**
	 * isEmailString
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isEmail(
			String value) {
		if (!StringUtils.isEmpty(value) && !EmailValidator.getInstance().isValid(value)) {
			return false;
		}
		return true;
	}

	/**
	 * isZipcode
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isZipCode(
			String value) {
		return matchedRegExp(value, "^(銆抺0,1}[0-9]{3}-[0-9]{4})$");
	}

	/**
	 * isMobilePhone
	 *
	 * @param value
	 * @return true/false
	 */
	public static boolean isMobileNumber(
			String value) {
		return matchedRegExp(value, "^([0-9]{3}-[0-9]{4}-[0-9]{4})$") || matchedRegExp(value, "^([0-9]{3}[0-9]{4}[0-9]{4})$") || matchedRegExp(value, "^([0-9]{3} [0-9]{4} [0-9]{4})$");
	}

	/**
	 * matchRegexp
	 *
	 * @param value
	 * @param regexp
	 * @return true/false
	 */
	public static boolean matchedRegExp(
			String value,
			String regexp) {
		if (!StringUtils.isEmpty(value) && !GenericValidator.matchRegexp(value, regexp)) {
			return false;
		}
		return true;
	}

	/**
	 * isInConstCollection
	 * 
	 * @param value
	 * @param constValues
	 * @return
	 */
	public static boolean isInConstCollection(
			Object value,
			String... constValues) {
		if (value == null) {
			return true;
		}
		for (String constValue : constValues) {
			if (constValue.equals(value.toString())) {
				return true;
			}
		}
		return false;
	}
}
