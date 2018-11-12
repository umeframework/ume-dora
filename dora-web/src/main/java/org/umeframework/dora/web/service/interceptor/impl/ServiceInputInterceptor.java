/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import javax.annotation.Resource;

import org.apache.wink.json4j.JSONArray;
import org.umeframework.dora.ajax.AjaxParser;
import org.umeframework.dora.ajax.ParserException;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * Service input parameter processor.<br>
 *
 * @author Yue MA
 */
@Deprecated
public class ServiceInputInterceptor extends BaseComponent implements Interceptor {
	/**
	 * JSON array start flag (using for direct parameters input)
	 */
	private static final String JSON_ARRAY_START_CHAR = "[";
	/**
	 * Use null value as when param not pass by json
	 */
	private boolean allowParamAbsence = true;
	/**
	 * json data parser
	 */
	@Resource(name = BeanConfigConst.DEFAULT_AJAX_PARSER)
	private AjaxParser<String> ajaxParser;
	/**
	 * Web service access helper
	 */
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
	private ServiceMapping serviceMapping;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.web.interceptor.Interceptor#intercept(tora. fw.web.interceptor.InterceptorChain)
	 */
	@Override
	public void intercept(InterceptorChain chain) throws Throwable {
		String serviceId = chain.getServiceId();
		String inputData = chain.getAjaxInputData();

		// Write debug log
		getLogger().debug("Service Input:", inputData);

		ServiceWrapper serviceRef = serviceMapping.getService(serviceId);
		if (serviceRef == null) {
			throw new ApplicationException("No find service " + serviceId);
		}
		Method serviceMethod = serviceRef.getServiceMethod();
		if (StringUtil.isNotEmpty(inputData) && serviceMethod.getParameterTypes().length > 0) {
			// Input is not empty and declared parameter existed
			Object[] paramList = parseInputParam(serviceMethod, inputData);
			chain.setServiceInputParams(paramList);
		} else if (StringUtil.isEmpty(inputData) && serviceMethod.getParameterTypes().length > 0) {
			// Input is empty and declared parameter existed
			chain.setServiceInputParams(new Object[] { null });
		} else {
			chain.setServiceInputParams(null);
		}
		chain.next();
	}

	/**
	 * Parse input parameters
	 * 
	 * @param serviceMethod
	 * @param inputData
	 * @return
	 * @throws Exception
	 */
	protected Object[] parseInputParam(Method serviceMethod, String inputData) throws Exception {
		Annotation[][] serviceMethodParamAnnos = serviceMethod.getParameterAnnotations();
		Class<?>[] inParamTypes = serviceMethod.getParameterTypes();
		Type[] genericParamTypes = serviceMethod.getGenericParameterTypes();

		if (inParamTypes.length == 0 && StringUtil.isNotEmpty(inputData)) {
			throw new ParserException("Inconsistent json parameters, service no decalred parameter but actual input not empty.");
		}
		Object[] paramList = new Object[inParamTypes.length];
		if (!inputData.startsWith(JSON_ARRAY_START_CHAR) && inParamTypes.length == 1) {
			// Input is JSON object and only one parameter declared for method
			Object paramObj = ajaxParser.parse(inputData, inParamTypes[0], genericParamTypes[0], serviceMethodParamAnnos[0]);
			paramList[0] = paramObj;
		} else if (inputData.startsWith(JSON_ARRAY_START_CHAR) && inParamTypes.length > 0) {
			// Parse the input parameters as JSON array format
			JSONArray jsonArr = new JSONArray(inputData);
			if (jsonArr.length() > inParamTypes.length) {
				throw new ParserException("Inconsistent json parameters, service decalred "
				        + inParamTypes.length
				        + " parameters but actual input "
				        + jsonArr.length());
			}

			// Input is JSON array and method parameters number >=1 (note its also include one parameter case)
			for (int i = 0; i < inParamTypes.length; i++) {
				Object jsonObj = null;
				if (i >= jsonArr.length()) {
					if (allowParamAbsence) {
						paramList[i] = null;
					} else {
						throw new ParserException("Inconsistent json parameters, expect "
						        + inParamTypes.length
						        + " but input are "
						        + jsonArr.length());
					}
				} else {
					jsonObj = jsonArr.get(i);
					String paramStr = jsonObj != null ? jsonObj.toString() : null;
					Object paramObj = ajaxParser.parse(paramStr, inParamTypes[i], genericParamTypes[i], serviceMethodParamAnnos[i]);
					paramList[i] = paramObj;
				}
			}
		}
		return paramList;
	}

	/**
	 * Get AJAXParser
	 * 
	 * @return the ajaxParser
	 */
	public AjaxParser<String> getAjaxParser() {
		return ajaxParser;
	}

	/**
	 * Set ajaxParser
	 * 
	 * @param ajaxParser
	 *            the ajaxParser to set
	 */
	public void setAjaxParser(AjaxParser<String> ajaxParser) {
		this.ajaxParser = ajaxParser;
	}

	/**
	 * @return the allowParamAbsence
	 */
	public boolean isAllowParamAbsence() {
		return allowParamAbsence;
	}

	/**
	 * @param allowParamAbsence
	 *            the allowParamAbsence to set
	 */
	public void setAllowParamAbsence(boolean allowParamAbsence) {
		this.allowParamAbsence = allowParamAbsence;
	}

	/**
	 * @return the serviceMapping
	 */
	public ServiceMapping getServiceMapping() {
		return serviceMapping;
	}

	/**
	 * @param serviceMapping
	 *            the serviceMapping to set
	 */
	public void setServiceMapping(ServiceMapping serviceMapping) {
		this.serviceMapping = serviceMapping;
	}

}
