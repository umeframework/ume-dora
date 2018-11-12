/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import javax.annotation.Resource;

import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceResponse;
import org.umeframework.dora.util.SecurityUtil;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * Service security output parameter processor.<br>
 * 
 * @author Yue MA
 * 
 */
@Deprecated
public class ServiceOutputEncryotInterceptor extends BaseComponent implements Interceptor {
	/**
	 * secretKey
	 */
	private String secretKey = "a4ba45ae9f32885c";

	/**
	 * json data render
	 */
	@Resource(name = BeanConfigConst.DEFAULT_AJAX_RENDER)
	private AjaxRender<String> ajaxRender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.web.interceptor.Interceptor#intercept(tora. fw.web.interceptor.InterceptorChain)
	 */
	@Override
	public void intercept(InterceptorChain chain) throws Throwable {
		ServiceResponse<Object> serviceResponse = chain.getServiceResponse();
		Object resultObj = serviceResponse.getResultObject();
		if (resultObj != null) {
			String resultObjectAsEncryptStr = ajaxRender.render(resultObj);
			SecurityUtil securityUtil = new SecurityUtil(secretKey);
			resultObjectAsEncryptStr = securityUtil.encrypt(resultObjectAsEncryptStr);
			serviceResponse.setResultObject(resultObjectAsEncryptStr);
			chain.setServiceResponse(serviceResponse);
		}
		chain.next();
	}

	/**
	 * @param secretKey
	 *            the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
