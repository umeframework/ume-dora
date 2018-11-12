/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner.impl;

import javax.annotation.Resource;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.exception.TransactionException;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.transaction.TransactionManager;

/**
 * TransactionalServiceRunner
 *
 * @author Yue MA
 *
 */
public class TransactionalServiceRunnerImpl extends ServiceRunnerImpl {
	/**
	 * transaction manager
	 */
	@Resource(name = BeanConfigConst.DEFAULT_TRANSACTION_MANAGER)
	private TransactionManager transactionManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.interceptor.impl.ServiceInterceptor#beginTransaction(org.umeframework.dora.service.ServiceReference)
	 */
	@Override
	protected void doBefore(ServiceWrapper serviceWrapper, Object[] params) {
		// Do transaction begin
		TransactionManager transactionManager = this.getTransactionManager();
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
	protected Object doAfter(ServiceWrapper serviceWrapper, Object[] params, Object resultObj) {
		// Do transaction success
		TransactionManager transactionManager = this.getTransactionManager();
		if (transactionManager != null && serviceWrapper.isTransactional()) {
			transactionManager.commit();
		}
		return resultObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.interceptor.impl.ServiceInterceptor#rollbackTransaction(org.umeframework.dora.service.ServiceReference, java.lang.Throwable)
	 */
	@Override
	protected void doException(ServiceWrapper serviceWrapper, Object[] params, Throwable ex) {
		// Do transaction fail
		if (!(ex instanceof TransactionException)) {
			TransactionManager transactionManager = this.getTransactionManager();
			if (transactionManager != null && serviceWrapper.isTransactional()) {
				transactionManager.rollback();
			}
		}
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

}
