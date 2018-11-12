/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner;

//import org.umeframework.dora.service.ServiceResponse;

/**
 * AjaxServiceRunner
 *
 * @author Yue MA
 *
 */
public interface AjaxServiceRunner {
	/**
	 * executeAjax
	 * 
	 * @param serviceId
	 * @param ajaxInput
	 * @return
	 * @throws Throwable
	 */
	Object executeAjax(String serviceId, String ajaxInput) throws Throwable;
	
	/**
	 * render
	 * 
	 * @param outputObj
	 * @return
	 */
	String render(Object outputObj);

}
