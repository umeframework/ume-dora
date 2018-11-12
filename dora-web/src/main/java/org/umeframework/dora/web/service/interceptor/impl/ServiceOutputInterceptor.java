/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceResponse;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * Service output parameter processor.<br>
 * 
 * @author Yue MA
 * 
 */
@Deprecated
public class ServiceOutputInterceptor extends BaseComponent implements Interceptor {

	/**
	 * json data render
	 */
	@Resource(name=BeanConfigConst.DEFAULT_AJAX_RENDER)
	private AjaxRender<String> ajaxRender;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.web.interceptor.Interceptor#intercept(tora.
	 * fw.web.interceptor.InterceptorChain)
	 */
	@Override
	public void intercept(InterceptorChain chain) throws Throwable {
		ServiceResponse<Object> serviceResponse = chain.getServiceResponse();
		String jsonOutput = ajaxRender.render(serviceResponse);
		chain.setAjaxOutputData(jsonOutput);
		
		chain.getResponse().setContentType(MediaType.APPLICATION_JSON);

		chain.next();
		// Write debug log
		getLogger().debug("Service Output:", jsonOutput);
	}

	/**
	 * @return the ajaxRender
	 */
	public AjaxRender<String> getAjaxRender() {
		return ajaxRender;
	}

	/**
	 * @param ajaxRender
	 *            the ajaxRender to set
	 */
	public void setAjaxRender(AjaxRender<String> ajaxRender) {
		this.ajaxRender = ajaxRender;
	}

}
