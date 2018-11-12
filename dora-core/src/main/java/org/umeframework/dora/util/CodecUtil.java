/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.umeframework.dora.exception.ApplicationException;

/**
 * codec common process tool
 * 
 * @author Yue MA
 */
abstract public class CodecUtil {
	// /**
	// * encodeMD5Hex
	// *
	// * @param data
	// * @return
	// */
	// public static String encodeMD5Hex(byte[] data) {
	// return DigestUtils.md5Hex(data);
	// }

	/**
	 * encodeMD5Str
	 * 
	 * @param data
	 * @return
	 */
	public static String encodeMD5Hex(String data) {
		return DigestUtils.md5Hex(data);
	}

	// /**
	// * encodeMD5
	// *
	// * @param data
	// * @return
	// */
	// public static byte[] encodeMD5(
	// byte[] data) {
	// return DigestUtils.md5(data);
	// }
	//
	// /**
	// * encodeMD5
	// *
	// * @param data
	// * @return
	// */
	// public static byte[] encodeMD5(
	// String data) {
	// return DigestUtils.md5(data);
	// }

	/**
	 * encodeBase64
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] encodeBase64(byte[] data) {
		byte[] bs = Base64.encodeBase64(data);
		return bs;
	}

	/**
	 * decodeBase64
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decodeBase64(String data) {
		byte[] bs = Base64.decodeBase64(data);
		return bs;
	}

	/**
	 * encodeBase64
	 * 
	 * @param data
	 * @return
	 * @throws SQLException
	 */
	public static byte[] encodeBase64(Blob data) throws SQLException {

		byte[] bs = null;
		byte[] lobByte = data.getBytes(1, (int) data.length());
		bs = Base64.encodeBase64(lobByte);

		return bs;
	}

	/**
	 * encodeUTF8
	 * 
	 * @param data
	 * @return
	 */
	public static String encodeAsUTF8(String data) {
		try {
			data = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ApplicationException(e, "UTF-8 encode fail due to unsupport encoding exception: " + data);
		}
		return data;
	}

	/**
	 * decodeUTF8
	 * 
	 * @param data
	 * @return
	 */
	public static String decodeAsUTF8(String data) {
		try {
			data = URLDecoder.decode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ApplicationException(e, "UTF-8 decode fail due to unsupport encoding exception: " + data);
		}
		return data;
	}

	/**
	 * base64ToBlob
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static java.sql.Blob base64ToBlob(String value) {
		if (value == null) {
			return null;
		}
		byte[] bytes = CodecUtil.decodeBase64(value);
		return new org.umeframework.dora.type.Blob(bytes);
	}

	/**
	 * blobToBase64
	 * 
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws SQLException
	 */
	public static String blobToBase64(Object value) {
		if (value == null) {
			return null;
		}
		String strValue = null;
		if (value instanceof byte[]) {
			byte[] b64bytes = CodecUtil.encodeBase64((byte[]) value);
			try {
				strValue = StringUtil.bytesToString(b64bytes);
			} catch (UnsupportedEncodingException e) {
				throw new ApplicationException(e, "Failed in convert blob to base64");
			}
		} else if (value instanceof java.sql.Blob) {
			byte[] b64bytes;
			try {
				b64bytes = CodecUtil.encodeBase64((java.sql.Blob) value);
				strValue = StringUtil.bytesToString(b64bytes);
			} catch (Exception e) {
				throw new ApplicationException(e, "Failed in convert blob to base64");
			}
		} else {
			strValue = value.toString();
		}
		return strValue;
	}

}