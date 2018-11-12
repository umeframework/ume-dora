/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.umeframework.dora.context.SessionContext;

/**
 * DownloadEntity
 * 
 * @author Yue MA
 *
 */
public class DownloadEntity implements Serializable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7830379736319795275L;
	/**
	 * contentTypes
	 */
	private static final Map<String, String> contentTypes = new HashMap<String, String>();
	static {
		contentTypes.put("json", "application/json");
		contentTypes.put("xml", "application/xml");
		contentTypes.put("octet", "application/octet-stream");
		contentTypes.put("pdf", "application/pdf");
		contentTypes.put("msword", "application/msword");
		contentTypes.put("form", "multipart/form-data");
		contentTypes.put("gif", "image/gif");
		contentTypes.put("jpeg", "image/jpeg");
		contentTypes.put("png", "image/png");
		contentTypes.put("text", "text/plain");
	}

	/**
	 * Input stream
	 */
	private InputStream inputStream;
	/**
	 * File char set for download use
	 */
	private String characterEncoding = "utf-8";
	/**
	 * Download content type
	 */
	private String contentType = "application/octet-stream";
	/**
	 * Download default use file name
	 */
	private String downloadName;

	/**
	 * FileDownloadObject
	 * 
	 * @param fileName
	 */
	public DownloadEntity(InputStream inputStream) {
		init(inputStream, "utf-8", "application/octet-stream");
	}

	/**
	 * FileDownloadObject
	 * 
	 * @param fileName
	 * @param characterEncoding
	 * @param contentType
	 */
	public DownloadEntity(InputStream inputStream, String characterEncoding, String contentType) {
		init(inputStream, characterEncoding, contentType);
	}

	/**
	 * init
	 * 
	 * @param fileName
	 * @param internalResource
	 * @param characterEncoding
	 * @param contentType
	 */
	protected void init(InputStream inputStream, String characterEncoding, String contentType) {
		if (contentTypes.containsKey(contentType.toLowerCase())) {
			contentType = contentTypes.get(contentType.toLowerCase());
		}
		this.inputStream = inputStream;
		this.characterEncoding = characterEncoding;
		this.contentType = contentType;
	}

	/**
	 * download
	 * 
	 * @throws IOException
	 */
	public void download() throws IOException {
		HttpServletResponse response = SessionContext.open().getResponse();
		if (response != null) {
			OutputStream outputStream = null;
			try {
				if (inputStream != null) {
					String downloadName = this.getDownloadName();
					response.reset();
					response.setHeader("Content-Disposition", "attachment;fileName=" + downloadName);
					response.setContentType(this.getContentType());
					response.setCharacterEncoding(this.getCharacterEncoding());
					outputStream = response.getOutputStream();
					byte[] buffer = new byte[2048];
					int length;
					while ((length = inputStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, length);
					}
				}
			} catch (IOException e) {
				throw e;
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					throw e;
				}
			}

		}
	}

	/**
	 * @return the characterEncoding
	 */
	public String getCharacterEncoding() {
		return characterEncoding;
	}

	/**
	 * @param characterEncoding
	 *            the characterEncoding to set
	 */
	public void setCharacterEncoding(String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the downloadName
	 */
	public String getDownloadName() {
		return downloadName;
	}

	/**
	 * @param downloadName
	 *            the downloadName to set
	 */
	public void setDownloadName(String downloadName) {
		this.downloadName = downloadName;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream
	 *            the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

}
