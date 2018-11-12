/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner;

import org.umeframework.dora.service.ServiceResponse;

/**
 * Service Runner 
 *
 * @author Yue MA
 *
 */
@SuppressWarnings("deprecation")
public interface ServiceRunner {

	/**
	 * executeService
	 * 
	 * @param serviceId
	 * @param params
	 * @return
	 * @throws Throwable
	 */
    ServiceResponse<Object> executeForServiceResponse(String serviceId, Object[] params) throws Throwable;

	/**
	 * execute
	 * 
	 * @param serviceId
	 * @param params
	 * @return
	 * @throws Throwable
	 */
	Object execute(String serviceId, Object[] params) throws Throwable;
}
