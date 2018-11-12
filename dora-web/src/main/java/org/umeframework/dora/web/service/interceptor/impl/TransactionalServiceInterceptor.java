/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.transaction.TransactionManager;
import org.umeframework.dora.web.service.interceptor.Interceptor;

/**
 * TransactionalServiceInterceptor
 *
 * @author Yue MA
 *
 */
@Deprecated
public class TransactionalServiceInterceptor extends ServiceInterceptor implements Interceptor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.interceptor.impl.ServiceInterceptor#beginTransaction(org.umeframework.dora.service.ServiceReference)
	 */
	@Override
	protected void beginTransaction(ServiceWrapper serviceWrapper) {
		// Do transaction begin
		TransactionManager transactionManager = super.getTransactionManager();
		if (transactionManager != null && serviceWrapper.isTransactional()) {
			transactionManager.begin();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.interceptor.impl.ServiceInterceptor#commitTransaction(org.umeframework.dora.service.ServiceReference)
	 */
	@Override
	protected void commitTransaction(ServiceWrapper serviceWrapper) {
		// Do transaction success
		TransactionManager transactionManager = super.getTransactionManager();
		if (transactionManager != null && serviceWrapper.isTransactional()) {
			transactionManager.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.interceptor.impl.ServiceInterceptor#rollbackTransaction(org.umeframework.dora.service.ServiceReference, java.lang.Throwable)
	 */
	@Override
	protected void rollbackTransaction(ServiceWrapper serviceWrapper, Throwable e) {
		// Do transaction fail
		TransactionManager transactionManager = super.getTransactionManager();
		if (transactionManager != null && serviceWrapper.isTransactional()) {
			transactionManager.rollback();
		}
	}

}
