/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.http;

import java.util.Map;

/**
 * HttpProxy
 * 
 * @author Yue MA
 */
public interface HttpProxy {
    /**
     * Do post request
     * 
     * @param url
     * @param msg
     * @return
     */
    String doPost(
            String url,
            String msg);
    
    /**
     * Do post request
     * 
     * @param url
     * @param msg
     * @param header
     * @return
     */
    String doPost(
            String url,
            HttpParam param);


    /**
     * Do get request
     * 
     * @param url
     * @param msg
     * @return
     */
    String doGet(
            String url);
    
    /**
     * Do get request
     * 
     * @param url
     * @param param
     * @return
     */
    String doGet(
            String url,
            HttpParam param);
    
    /**
     * Download remote file to locate disk via Http Client
     * 
     * @param url
     * @param saveToPath
     * @param headParams
     * @param downloadProgress
     * @return
     */
    long doDownload(
            String url,
            String saveToPath,
            Map<String, String> headParams,
            HttpProcessProgress downloadProgress);
    
    /**
     * Upload local file to remote server
     * 
     * @param serverUrl
     * @param localFile
     * @param serverFieldName
     * @param params
     * @return
     * @throws Exception
     */
    String doUpload(
            String serverUrl,
            String localFile,
            String serverFieldName,
            Map<String, String> params);
    
	/**
	 * @return the appContentType
	 */
	String getAppContentType();

	/**
	 * @param appContentType
	 *            the appContentType to set
	 */
	void setAppContentType(String appContentType);

	/**
	 * @return the entityContentType
	 */
	String getEntityContentType();

	/**
	 * @param entityContentType
	 *            the entityContentType to set
	 */
	void setEntityContentType(String entityContentType);

	/**
	 * @return the entityCharset
	 */
	String getEntityCharset();

	/**
	 * @param entityCharset
	 *            the entityCharset to set
	 */
	void setEntityCharset(String entityCharset);

	/**
	 * @return the paramCharset
	 */
	String getParamCharset();

	/**
	 * @param paramCharset
	 *            the paramCharset to set
	 */
	void setParamCharset(String paramCharset);

	/**
	 * @return the useSSL
	 */
	boolean isUseSSL();

	/**
	 * @param useSSL
	 *            the useSSL to set
	 */
	void setUseSSL(boolean useSSL);


}
