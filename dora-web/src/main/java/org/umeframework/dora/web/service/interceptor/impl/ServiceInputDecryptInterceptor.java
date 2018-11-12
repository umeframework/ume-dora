/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.util.SecurityUtil;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * Service security input parameter processor.<br>
 *
 * @author Yue MA
 */
@Deprecated
public class ServiceInputDecryptInterceptor extends BaseComponent implements Interceptor {
	/**
	 * secretKey
	 */
	private String secretKey = "a4ba45ae9f32885c";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.interceptor.Interceptor#intercept(org.umeframework.dora.service.interceptor.InterceptorChain)
	 */
	@Override
	public void intercept(InterceptorChain chain) throws Throwable {
		String inputData = chain.getAjaxInputData();
		Object serviceParams = chain.getServiceInputParams();
		if (inputData != null && serviceParams == null) {
			SecurityUtil securityUtil = new SecurityUtil(secretKey);
			inputData = securityUtil.decrypt(inputData);
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
