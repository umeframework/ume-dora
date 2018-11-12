/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.interceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * FileDownloadObject
 * 
 * @author mayue
 *
 */
public class FileDownloadEntity extends DownloadEntity implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7830379736319795275L;
	
	/**
	 * FileDownloadObject
	 * 
	 * @param fileName
	 * @param internalResource
	 * @throws IOException 
	 */
	public FileDownloadEntity(String fileName, boolean internalResource) throws IOException {
		this(fileName, internalResource, "utf-8", "application/octet-stream");
	}

	/**
	 * FileDownloadObject
	 * 
	 * @param fileName
	 */
	public FileDownloadEntity(String fileName) throws IOException{
		this(fileName, false, "utf-8", "application/octet-stream");
	}

	/**
	 * FileDownloadObject
	 * 
	 * @param fileName
	 * @param characterEncoding
	 * @param contentType
	 * @throws IOException 
	 */
	public FileDownloadEntity(String fileName, boolean internalResource, String characterEncoding, String contentType) throws IOException {
		super(null, characterEncoding, contentType);
		InputStream inputStream = this.getInputStream(fileName, internalResource);
		super.setInputStream(inputStream);
	}

	/**
	 * init
	 * 
	 * @param fileName
	 * @param internalResource
	 * @param characterEncoding
	 * @param contentType
	 */
	protected InputStream getInputStream(String fileName, boolean internalResource) throws IOException {
		fileName = fileName.contains("\\") ? fileName.replace("\\", "/") : fileName;
		File file = null;
		if (internalResource) {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			file = new File(classloader.getResource(fileName).getFile());
		} else {
			file = new File(fileName);
		}
		InputStream inputStream = null;
		if (file.exists()) {
			inputStream = new FileInputStream(file);
		} else {
			throw new IOException("Download file is not exist, " + fileName);
		}
		return inputStream;
	}

}
