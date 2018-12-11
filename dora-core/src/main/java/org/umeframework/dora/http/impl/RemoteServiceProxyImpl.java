/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.http.impl;

import javax.annotation.Resource;

import org.umeframework.dora.ajax.AjaxParser;
import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.http.HttpProxy;
import org.umeframework.dora.http.RemoteServiceProxy;
import org.umeframework.dora.service.BaseComponent;

/**
 * RemoteServiceClientImpl
 * 
 * @author Yue MA
 * 
 */
public class RemoteServiceProxyImpl extends BaseComponent implements RemoteServiceProxy {

	/**
	 * json data parser
	 */
	@Resource(name = BeanConfigConst.DEFAULT_AJAX_PARSER)
	private AjaxParser<String> ajaxParser;
	/**
	 * json data render
	 */
	@Resource(name = BeanConfigConst.DEFAULT_AJAX_RENDER)
	private AjaxRender<String> ajaxRender;
	/**
	 * httpProxy
	 */
	private HttpProxy httpProxy;

	/**
	 * RemoteServiceProxyImpl
	 */
	public RemoteServiceProxyImpl() {
	}

	/**
	 * RemoteServiceProxyImpl
	 * 
	 * @param httpProxy
	 */
	public RemoteServiceProxyImpl(HttpProxy httpProxy) {
		this.httpProxy = httpProxy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.RemoteServiceProxy#postForEntity(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public <E> E postForEntity(String url, Object inputObj, Class<E> resultObjClazz) {
		try {
			String inputJson = ajaxRender.render(inputObj);
			String resultJson = postForString(url, inputJson);
			E resultObj = ajaxParser.parse(resultJson, resultObjClazz, null, null);
			return resultObj;
		} catch (Exception e) {
			throw new ApplicationException(e, "Error on invoke remote service.");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.RemoteServiceProxy#getForEntity(java.lang.String, java.lang.Class)
	 */
	@Override
	public <E> E getForEntity(String url, Class<E> resultObjClazz) {
		try {
			String resultJson = getForString(url);
			E resultObj = ajaxParser.parse(resultJson, resultObjClazz, null, null);
			return resultObj;
		} catch (Exception e) {
			throw new ApplicationException(e, "Error on invoke remote service.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.RemoteServiceProxy#postForString(java.lang.String, java.lang.String)
	 */
	@Override
	public String postForString(String url, String inputJson) {
		try {
			String resultJson = httpProxy.doPost(url, inputJson);
			return resultJson;
		} catch (Exception e) {
			throw new ApplicationException(e, "Error on invoke remote service.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.RemoteServiceProxy#getForString(java.lang.String)
	 */
	@Override
	public String getForString(String url) {
		try {
			String resultJson = httpProxy.doGet(url);
			return resultJson;
		} catch (Exception e) {
			throw new ApplicationException(e, "Error on invoke remote service.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.RemoteServiceProxy#parseJson(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T fromJson(String inData, Class<T> clazz) throws Exception {
		return ajaxParser.parse(inData, clazz, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.http.RemoteServiceProxy#renderObject(java.lang.Object)
	 */
	@Override
	public <E> String toJson(E javaObj) {
		return ajaxRender.render(javaObj);
	}

	/**
	 * @return the httpProxy
	 */
	@Override
	public HttpProxy getHttpProxy() {
		return httpProxy;
	}

	/**
	 * @param httpProxy
	 *            the httpProxy to set
	 */
	@Override
	public void setHttpProxy(HttpProxy httpProxy) {
		this.httpProxy = httpProxy;
	}

	/**
	 * @return the ajaxParser
	 */
	@Override
	public AjaxParser<String> getAjaxParser() {
		return ajaxParser;
	}

	/**
	 * @param ajaxParser
	 *            the ajaxParser to set
	 */
	@Override
	public void setAjaxParser(AjaxParser<String> ajaxParser) {
		this.ajaxParser = ajaxParser;
	}

	/**
	 * @return the ajaxRender
	 */
	@Override
	public AjaxRender<String> getAjaxRender() {
		return ajaxRender;
	}

	/**
	 * @param ajaxRender
	 *            the ajaxRender to set
	 */
	@Override
	public void setAjaxRender(AjaxRender<String> ajaxRender) {
		this.ajaxRender = ajaxRender;
	}

}
