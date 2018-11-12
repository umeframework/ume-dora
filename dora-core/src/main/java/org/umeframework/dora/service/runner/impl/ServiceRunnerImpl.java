/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceResponse;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.runner.ServiceRunner;

/**
 * Service Runner
 *
 * @author Yue MA
 *
 */
@SuppressWarnings("deprecation")
public class ServiceRunnerImpl extends BaseComponent implements ServiceRunner {
	/**
	 * Web Service initialize information handler
	 */
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
	private ServiceMapping serviceMapping;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.runner.ServiceRunner#executeService(java.lang.String, java.lang.Object[])
	 */
	@Override
	public ServiceResponse<Object> executeForServiceResponse(String serviceId, Object[] params) throws Throwable {
		Object resultObj = null;
		ServiceResponse<Object> serviceResponse = new ServiceResponse<Object>();
		try {
			resultObj = this.execute(serviceId, params);
			serviceResponse.setResultCode(ServiceResponse.SUCCESS);
			serviceResponse.setResultObject(resultObj);
		} finally {
			List<String> messages = SessionContext.open().getMessages();
			super.getLogger().debug("Add SessionContext messages.");
			if (messages != null) {
				for (String e : messages) {
					serviceResponse.addMessage(e);
				}
			}
		}
		return serviceResponse;
	}

	/**
	 * execute
	 * 
	 * @param serviceId
	 * @param params
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object execute(String serviceId, Object[] params) throws Throwable {
		ServiceWrapper serviceWrapper = serviceMapping.getService(serviceId);
        if (serviceWrapper == null) {
            throw new ApplicationException("No find '" + serviceId + "' in service mapping configuration.");
        }
		try {
			doBefore(serviceWrapper, params);

			Method serviceMethod = serviceWrapper.getServiceMethod();
			Object serviceInstance = serviceWrapper.getServiceInstance();
			Object resultObj = null;
			try {
				resultObj = serviceMethod.invoke(serviceInstance, params);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
			doAfter(serviceWrapper, params, resultObj);
			return resultObj;
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			}
			doException(serviceWrapper, params, e);
			throw e;
		}
	}

	/**
	 * doBefore
	 * 
	 * @param serviceWrapper
	 * @param params
	 * @throws Throwable
	 */
	protected void doBefore(ServiceWrapper serviceWrapper, Object[] params) throws Throwable {
	}

	/**
	 * doAfter
	 * 
	 * @param serviceWrapper
	 * @param params
	 * @param resultObj
	 * @return
	 * @throws Throwable
	 */
	protected Object doAfter(ServiceWrapper serviceWrapper, Object[] params, Object resultObj) throws Throwable {
		return resultObj;
	}

	/**
	 * doException
	 * 
	 * @param serviceWrapper
	 * @param params
	 * @param ex
	 * @throws Throwable
	 */
	protected void doException(ServiceWrapper serviceWrapper, Object[] params, Throwable ex) throws Throwable {
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
