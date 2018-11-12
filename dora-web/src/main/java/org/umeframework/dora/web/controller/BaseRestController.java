/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.bean.BeanFactory;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.exception.ServcieRetryException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.runner.AjaxServiceRunner;

/**
 * Entry rest controller implementation.<br>
 *
 * @author Yue MA 
 */
public abstract class BaseRestController extends BaseComponent {
	/**
	 * Default bean factory instance.
	 */
	@Resource(name = BeanConfigConst.DEFAULT_BEAN_FACTORY)
	private BeanFactory beanFactory;

	/**
	 * execute HTTP request by the defined service intercept chains.
	 * 
	 * @param requestCategory
	 * @param request
	 * @param response
	 * @param system
	 * @param serviceId
	 * @param jsonInput
	 * @param interceptorChainName
	 * @param printWriter
	 * @return
	 * @throws Throwable
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response, String system, String serviceId, String jsonInput)
	        throws Throwable {
		long startTime = System.currentTimeMillis();
		AjaxServiceRunner serviceRunner = null;
		try {
			serviceRunner = beanFactory.getBean(system);
		} catch (Exception e) {
			getLogger().error("No found service runner:" + system, e);
			throw e;
		}
		String jsonOutput = null;
		try {
		    Object result = serviceRunner.executeAjax(serviceId, jsonInput);
			jsonOutput = serviceRunner.render(result);
		} catch (Throwable cause) {
			// get actual exception instance
			while (cause.getClass().equals(InvocationTargetException.class)) {
				cause = ((InvocationTargetException) cause).getTargetException();
			}
			if (cause instanceof ServcieRetryException) {
				ServcieRetryException retryException = (ServcieRetryException) cause;
				try {
					int interal = retryException.getRetryInterval();
					int maxTime = retryException.getRetryMaxTime();
					serviceId = retryException.getRetryServiceId() == null ? serviceId : retryException.getRetryServiceId();

					for (int i = 1; i <= maxTime; i++) {
						try {
							retryException.setRetryIndex(retryException.getRetryIndex() + 1);
							getLogger().warn("Restart service: " + serviceId + " round " + retryException.getRetryIndex());
							Thread.sleep(interal);
							SessionContext.open().setServiceRetryException(retryException);
							Object retryRst = serviceRunner.executeAjax(retryException.getRetryServiceId(), jsonInput);
							jsonOutput = serviceRunner.render(retryRst);
							SessionContext.open().setServiceRetryException(null);
							getLogger().warn("Restart service: " + serviceId + " round " + retryException.getRetryIndex() + " successful.");
							break;
						} catch (ServcieRetryException e) {
							// do next loop when met same ServcieRetryException errors.
							SessionContext.open().setServiceRetryException(retryException);
							getLogger().warn("Restart service: " + serviceId + " round " + retryException.getRetryIndex() + " failed.");
						}
					}
				} catch (Throwable e) {
					getLogger().error("Servcie retry error:" + serviceId, e);
					throw e;
				}
			}
			getLogger().error("Servcie exception return:" + serviceId, cause);
			// Throw cause to global exception.
			throw cause;

		} finally {
			// Write debug log
			getLogger().debug("Service output:", jsonOutput);
			// Write application log
			long useTime = System.currentTimeMillis() - startTime;
			getLogger().info("Service take time:", useTime);
		}
		return jsonOutput;
	}

	/**
	 * URL decode
	 * 
	 * @param msg
	 * @param encode
	 * @return
	 */
	protected String decode(String msg, String encode) {
		try {
			msg = URLDecoder.decode(msg, encode);
		} catch (Exception e) {
			getLogger().error("URL decode error:" + msg, e);
		}
		return msg;
	}

}
