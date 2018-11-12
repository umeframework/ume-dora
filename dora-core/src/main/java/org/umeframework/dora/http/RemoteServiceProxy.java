/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.http;

import org.umeframework.dora.ajax.AjaxParser;
import org.umeframework.dora.ajax.AjaxRender;

/**
 * RemoteServiceProxy
 * 
 * @author Yue MA
 * 
 */
public interface RemoteServiceProxy {
	/**
	 * postForEntity
	 * 
	 * @param url
	 * @param inputObj
	 * @param resultObjClazz
	 * @return
	 */
	<E> E postForEntity(String url, Object inputObj, Class<E> resultObjClazz);

	/**
	 * getForEntity
	 * 
	 * @param url
	 * @param resultObjClazz
	 * @return
	 */
	<E> E getForEntity(String url, Class<E> resultObjClazz);

	/**
	 * postForString
	 * 
	 * @param url
	 * @param inputJson
	 * @return
	 */
	String postForString(String url, String inputJson);

	/**
	 * getForString
	 * 
	 * @param url
	 * @return
	 */
	String getForString(String url);

	/**
	 * fromJson
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	<E> E fromJson(String json, Class<E> clazz) throws Exception;

	/**
	 * toJson
	 * 
	 * @param javaObj
	 *            - java object
	 * @return - json text string
	 */
	<E> String toJson(E javaObj);

	/**
	 * @return the httpProxy
	 */
	HttpProxy getHttpProxy();

	/**
	 * @param httpProxy
	 *            the httpProxy to set
	 */
	void setHttpProxy(HttpProxy httpProxy);

	/**
	 * @return the ajaxParser
	 */
	AjaxParser<String> getAjaxParser();

	/**
	 * @param ajaxParser
	 *            the ajaxParser to set
	 */
	void setAjaxParser(AjaxParser<String> ajaxParser);

	/**
	 * @return the ajaxRender
	 */
	AjaxRender<String> getAjaxRender();

	/**
	 * @param ajaxRender
	 *            the ajaxRender to set
	 */
	void setAjaxRender(AjaxRender<String> ajaxRender);

}
