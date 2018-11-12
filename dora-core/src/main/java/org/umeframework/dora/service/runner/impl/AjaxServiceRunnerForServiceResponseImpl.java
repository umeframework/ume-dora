/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.wink.json4j.JSONArray;
import org.umeframework.dora.ajax.AjaxParser;
import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.ajax.ParserException;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.bean.BeanUtil;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.service.ServiceResponse;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.runner.AjaxServiceRunner;
import org.umeframework.dora.util.StringUtil;

/**
 * AjaxServiceRunnerImpl
 *
 * @author Yue MA
 *
 */
@Deprecated
public class AjaxServiceRunnerForServiceResponseImpl extends TransactionalServiceRunnerImpl implements AjaxServiceRunner {
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
	 * json data render
	 */
	@Resource(name = BeanConfigConst.DEFAULT_AJAX_RENDER)
	private AjaxRender<String> ajaxRender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.runner.AjaxServiceRunner#executeAjax(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceResponse<Object> executeAjax(String serviceId, String ajaxInput) throws Throwable {
		ServiceWrapper serviceWrapper = super.getServiceMapping().getService(serviceId);
		Method serviceMethod = serviceWrapper.getServiceMethod();
		Object[] params = null;

		Map<String, String[]> requestParams = SessionContext.open().getRequestParameterMap();;
		String[] pathParams = SessionContext.open().getServicePathParameters();
		if (serviceMethod.getParameterTypes().length > 0) {
			if (StringUtil.isNotEmpty(ajaxInput)) {
				// Input is not empty
				params = parseInputParam(serviceMethod, ajaxInput);
			} else {
				if (pathParams != null && pathParams.length > 0) {
					if (pathParams.length == 1
					        && pathParams[0].startsWith("[")
					        && !serviceMethod.getParameterTypes()[0].isArray()
					        && !Collection.class.isAssignableFrom(serviceMethod.getParameterTypes()[0])) {
						// Path parameter is JSON array
						params = parseInputParam(serviceMethod, pathParams[0]);
					} else {
						// Path parameter is not empty
						if (pathParams.length > serviceMethod.getParameterTypes().length) {
							throw new ParserException("Method decalred "
							        + serviceMethod.getParameterTypes().length
							        + " parameters but actual input "
							        + pathParams.length);
						}
						params = new Object[serviceMethod.getParameterTypes().length];
						for (int i = 0; i < pathParams.length; i++) {
							params[i] = ajaxParser.parse(pathParams[i], serviceMethod.getParameterTypes()[i], serviceMethod.getGenericParameterTypes()[i], serviceMethod.getParameterAnnotations()[i]);
						}
					}
				} else {
					params = new Object[serviceMethod.getParameterTypes().length];
				}
			}
			// Check if append request parameters
			if (requestParams != null && requestParams.size() > 0 && serviceMethod.getParameterTypes().length == 1) {
				if (params == null) {
					params = new Object[serviceMethod.getParameterTypes().length];
				}
				params[0] = this.parseRequestParams(params[0], serviceMethod.getParameterTypes()[0], requestParams);
			}
		} else {
			params = null;
		}
		ServiceResponse<Object> serviceResponse = super.executeForServiceResponse(serviceId, params);
		return serviceResponse;
	}

	/**
	 * render
	 * 
	 * @param serviceResponse
	 * @return
	 */
	public String render(Object serviceResponse) {
		String ajaxOutput = ajaxRender.render(serviceResponse);
		return ajaxOutput;
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
		} else if (inputData.startsWith(JSON_ARRAY_START_CHAR) && inParamTypes.length == 1 && inParamTypes[0].isArray()) {
			// Input is JSON array and only one array parameter declared for method
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
	 * parseRequestParams
	 * 
	 * @param paramObj
	 * @param paramClass
	 * @param requestParams
	 * @return
	 */
	protected Object parseRequestParams(Object paramObj, Class<?> paramClass, Map<String, String[]> requestParams) {
		try {
			Map<String, Method> getters = BeanUtil.getGetters(paramClass);
			for (Map.Entry<String, String[]> e : requestParams.entrySet()) {
				String key = e.getKey();
				String[] values = e.getValue();
				String value = null;
				if (values != null) {
					for (String v : values) {
						if (StringUtil.isNotEmpty(v)) {
							value = v;
							break;
						}
					}
					if (value != null && getters.containsKey(key)) {
						if (paramObj == null) {
							paramObj = paramClass.newInstance();
							BeanUtil.setBeanProperty(paramObj, key, value);
						} else {
							if (BeanUtil.getBeanProperty(paramObj, key) == null) {
								BeanUtil.setBeanProperty(paramObj, key, value);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			super.getLogger().warn(e);
			super.getLogger().warn(requestParams);
		}
		return paramObj;
	}

}
