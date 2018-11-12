/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.exception.TransactionException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceResponse;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.runner.impl.ServiceRunnerImpl;
import org.umeframework.dora.transaction.TransactionManager;
import org.umeframework.dora.transaction.TransactionRequired;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * ServiceInterceptor
 *
 * @author Yue MA
 *
 */
@Deprecated
public class ServiceInterceptor extends BaseComponent implements Interceptor {
	/**
	 * Web Service initialize information handler
	 */
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
	private ServiceMapping serviceMapping;
	/**
	 * transaction manager
	 */
	@Resource(name = BeanConfigConst.DEFAULT_TRANSACTION_MANAGER)
	private TransactionManager transactionManager;
	
	
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_RUNNER)
	private ServiceRunnerImpl serviceRunner;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.web.interceptor.Interceptor#intercept(tora. fw.web.interceptor.InterceptorChain)
	 */
	@Override
	public void intercept(InterceptorChain chain) throws Throwable {
		getLogger().debug("Service starting...");
		String serviceId = chain.getServiceId();
		Object[] serviceParams = chain.getServiceInputParams();

		ServiceWrapper serviceWrapper = serviceMapping.getService(serviceId);
		if (serviceWrapper == null) {
			throw new ApplicationException("No find service " + serviceId);
		}

		ServiceResponse<Object> serviceResponse = null;
		serviceResponse = new ServiceResponse<Object>();
		try {
			beginTransaction(serviceWrapper);
			Object resultObj = serviceRunner.execute(serviceId, serviceParams);
			serviceResponse.setResultCode(ServiceResponse.SUCCESS);
			serviceResponse.setResultObject(resultObj);
			commitTransaction(serviceWrapper);
			getLogger().debug("Service completed.");
		} catch (Throwable e) {
			rollbackTransaction(serviceWrapper, e);

			if (e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			}
			throw e;
		} finally {
			try {
				List<String> messages = SessionContext.open().getMessages();
				if (messages != null) {
					for (String e : messages) {
						serviceResponse.addMessage(e);
					}
				}
			} catch (Throwable e) {
				throw e;
			}
			chain.setServiceResponse(serviceResponse);
			chain.next();
		}
	}

	/**
	 * beginTransaction
	 * 
	 * @param serviceWrapper
	 */
	protected void beginTransaction(ServiceWrapper serviceWrapper) {
		if (serviceWrapper.isTransactional() && transactionManager != null) {
			// Do transaction begin
			transactionManager.begin();
		}
	}

	/**
	 * commitTransaction
	 * 
	 * @param serviceWrapper
	 */
	protected void commitTransaction(ServiceWrapper serviceWrapper) {
		if (serviceWrapper.isTransactional() && transactionManager != null) {
			// Do transaction success
			transactionManager.commit();
		}
	}

	/**
	 * rollbackTransaction
	 * 
	 * @param serviceWrapper
	 * @param e
	 */
	protected void rollbackTransaction(ServiceWrapper serviceWrapper, Throwable e) {
		if (serviceWrapper.isTransactional() && !(e instanceof TransactionException) && transactionManager != null) {
			// Do transaction fail
			transactionManager.rollback();
		}
	}

	/**
	 * Is transaction required
	 * 
	 * @param serviceMethod
	 * @return
	 */
	protected boolean requireTransaction(Method serviceMethod) {
		TransactionRequired tx = serviceMethod.getAnnotation(TransactionRequired.class);
		return tx != null;
	}

	/**
	 * @return the transactionManager
	 */
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * @param transactionManager
	 *            the transactionManager to set
	 */
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
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
