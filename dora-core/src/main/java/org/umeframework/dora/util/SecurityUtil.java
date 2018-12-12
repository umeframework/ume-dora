/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.util;


import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.umeframework.dora.exception.ApplicationException;

//import com.itextpdf.text.pdf.codec.Base64;

/**
 * SecurityUtil
 * 
 * @author Yue MA
 * 
 */
public class SecurityUtil {
	/**
	 * secretKeySpec
	 */
	private String secretKeySpec = "AES";
	/**
	 * secretCharset
	 */
	private String secretCharset = "utf-8";
	/**
	 * secretCipher
	 */
	private String secretCipher = "AES/ECB/PKCS5Padding";
	/**
	 * secretKey
	 */
	private String secretKey;
	
	/**
	 * SecurityUtil
	 */
	public SecurityUtil() {}
	
	/**
	 * SecurityUtil
	 * 
	 * @param secretKey
	 */
	public SecurityUtil(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @param ssrc
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String ssrc) throws Exception {
		if (getSecretKey() == null) {
			return ssrc;
		}
		byte[] raw = getSecretKey().getBytes(secretCharset);
		SecretKeySpec skeySpec = new SecretKeySpec(raw, secretKeySpec);
		Cipher cipher = Cipher.getInstance(secretCipher);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(ssrc.getBytes(secretCharset));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	/**
	 * @param ssrc
	 * @return
	 */
	public String decrypt(String ssrc) {
		if (getSecretKey() == null) {
			return ssrc;
		}
		String originalString = null;
		try {
			byte[] raw = getSecretKey().getBytes(secretCharset);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, secretKeySpec);
			Cipher cipher = Cipher.getInstance(secretCipher);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = Base64.getDecoder().decode(ssrc);
			byte[] original = cipher.doFinal(encrypted1);
			originalString = new String(original, secretCharset);
		} catch (Exception e) {
			throw new ApplicationException(e, "Decrypt error during json parsing.");
		}
		return originalString;
	}

	/**
	 * @return the secretKeySpec
	 */
	public String getSecretKeySpec() {
		return secretKeySpec;
	}

	/**
	 * @param secretKeySpec
	 *            the secretKeySpec to set
	 */
	public void setSecretKeySpec(String secretKeySpec) {
		this.secretKeySpec = secretKeySpec;
	}

	/**
	 * @return the secretCharset
	 */
	public String getSecretCharset() {
		return secretCharset;
	}

	/**
	 * @param secretCharset
	 *            the secretCharset to set
	 */
	public void setSecretCharset(String secretCharset) {
		this.secretCharset = secretCharset;
	}

	/**
	 * @return the secretCipher
	 */
	public String getSecretCipher() {
		return secretCipher;
	}

	/**
	 * @param secretCipher
	 *            the secretCipher to set
	 */
	public void setSecretCipher(String secretCipher) {
		this.secretCipher = secretCipher;
	}

	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey
	 *            the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
