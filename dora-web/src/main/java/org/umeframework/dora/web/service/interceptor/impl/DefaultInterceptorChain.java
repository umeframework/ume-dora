/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.umeframework.dora.service.ServiceResponse;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * DefaultInterceptorChain
 *
 * @author Yue MA
 */
@Deprecated
public class DefaultInterceptorChain implements InterceptorChain {
	/**
	 * intercept list
	 */
	private List<Interceptor> interceptorList;

	/**
	 * intercept count
	 */
	private int count = -1;
	/**
	 * sys id
	 */
	private String sysId;
	/**
	 * context
	 */
	private ServletContext context;
	/**
	 * locale
	 */
	private Locale locale;
	/**
	 * HTTP request
	 */
	private HttpServletRequest request;
	/**
	 * HTTP response
	 */
	private HttpServletResponse response;
	/**
	 * service id
	 */
	private String serviceId;
	/**
	 * service input parameters
	 */
	private Object[] serviceInput;
	/**
	 * service out response
	 */
	private ServiceResponse<Object> serviceResponse;
	/**
	 * input data
	 */
	private String jsonInputData;
	/**
	 * output data
	 */
	private String jsonOutputData;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#next()
	 */
	@Override
	public void next() throws Throwable {

		count++;
		if (count >= interceptorList.size()) {
			return;
		}
		Interceptor interceptor = interceptorList.get(count);
		interceptor.intercept(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setContext(javax.servlet .ServletContext)
	 */
	public void setContext(ServletContext context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setInterceptorList(java .util.List)
	 */
	public void setInterceptorList(List<Interceptor> interceptorList) {
		this.interceptorList = interceptorList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setLocale(java.util. Locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setRequest(javax.servlet .http.HttpServletRequest)
	 */
	@Override
	public void setRequest(HttpServletRequest request) {
		this.request = request;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setResponse(javax.servlet .http.HttpServletResponse)
	 */
	@Override
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getContext()
	 */
	@Override
	public ServletContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getRequest()
	 */
	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getResponse()
	 */
	@Override
	public HttpServletResponse getResponse() {
		return response;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getServiceId()
	 */
	public String getServiceId() {
		return serviceId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setServiceId(java.lang .String)
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.interceptor.InterceptorChain#getInputData()
	 */
	public String getAjaxInputData() {
		return jsonInputData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.interceptor.InterceptorChain#setInputData(java.lang.String)
	 */
	public void setAjaxInputData(String inputData) {
		this.jsonInputData = inputData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.interceptor.InterceptorChain#getOutputData()
	 */
	public String getAjaxOutputData() {
		return jsonOutputData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.interceptor.InterceptorChain#setOutputData(java.lang.String)
	 */
	public void setAjaxOutputData(String outputData) {
		this.jsonOutputData = outputData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getInterceptorList()
	 */
	public List<Interceptor> getInterceptorList() {
		return interceptorList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getServiceInput()
	 */
	public Object[] getServiceInputParams() {
		return serviceInput;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setServiceInput(java .lang.Object)
	 */
	public void setServiceInputParams(Object[] serviceInput) {
		this.serviceInput = serviceInput;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#getServiceOutput()
	 */
	public ServiceResponse<Object> getServiceResponse() {
		return serviceResponse;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.InterceptorChain#setServiceOutput(com .ibm.org.umeframework.dora.core.service.ServiceResponse)
	 */
	public void setServiceResponse(ServiceResponse<Object> serviceOutput) {
		this.serviceResponse = serviceOutput;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.core.interceptor.InterceptorChain#getSysId()
	 */
	@Override
	public String getSysId() {
		return sysId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.core.interceptor.InterceptorChain#setSysId(java.lang. String)
	 */
	@Override
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
}
