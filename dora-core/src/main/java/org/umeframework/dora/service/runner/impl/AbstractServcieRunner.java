/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.runner.ServiceRunner;
import org.umeframework.dora.transaction.DataSourceTransactionExecutor;

/**
 * Service Runner
 *
 * @author Yue MA
 *
 */
public abstract class AbstractServcieRunner<IN, OUT> extends BaseComponent implements ServiceRunner<IN, OUT> {
    /**
     * Web Service initialize information handler
     */
    private ServiceMapping serviceMapping;
    /**
     * PlatformTransactionManager
     */
    private PlatformTransactionManager transactionManager;
    
    /**
     * parseInput
     * 
     * @param params
     * @return
     */
    abstract protected Object[] parseInput(String serviceId, IN params) throws Exception ;

    /**
     * renderOutput
     * 
     * @param result
     * @return
     */
    abstract protected OUT renderOutput(String serviceId, Object result) throws Exception ;

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.runner.ServiceRunner#execute(java.lang.String, java.lang.Object[])
     */
    @Override
    public OUT execute(String serviceId, IN in) throws Throwable {
        Object[] params = parseInput(serviceId, in);
        Object out = null;
        if (transactionManager != null) {
            Method serviceMethod = this.getServiceWrapper(serviceId).getServiceMethod();
            Transactional transactional = serviceMethod.getAnnotation(Transactional.class);
            if (transactional == null) {
                out = new DataSourceTransactionExecutor(transactionManager).execute(Propagation.REQUIRED, Exception.class, new DataSourceTransactionExecutor.Callbacker() {
                    @Override
                    public Object call() throws Throwable {
                        return executeServiceMethod(serviceId, params);
                    }
                });
            } else {
                out = new DataSourceTransactionExecutor(transactionManager).execute(transactional, new DataSourceTransactionExecutor.Callbacker() {
                    @Override
                    public Object call() throws Throwable {
                        return executeServiceMethod(serviceId, params);
                    }
                });
            }
        } else {
            out = executeServiceMethod(serviceId, params);
        }
        return renderOutput(serviceId, out);
    }

    /**
     * executeServiceMethod
     * 
     * @param serviceId
     * @param params
     * @return
     * @throws Throwable
     */
    protected Object executeServiceMethod(String serviceId, Object[] params) throws Throwable {
        ServiceWrapper serviceWrapper = getServiceWrapper(serviceId);
        Method serviceMethod = serviceWrapper.getServiceMethod();
        Object serviceInstance = serviceWrapper.getServiceInstance();
        Object resultObj = null;
        try {
            resultObj = serviceMethod.invoke(serviceInstance, params);
            return resultObj;
        } catch (InvocationTargetException e) {
            Throwable te = e.getTargetException();
            while (te instanceof InvocationTargetException) {
                te = ((InvocationTargetException) te).getTargetException();
            }
            throw te;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.runner.ServiceRunner#getServiceWrapper(java.lang.String)
     */
    public ServiceWrapper getServiceWrapper(String serviceId) {
        ServiceWrapper serviceWrapper = serviceMapping.getService(serviceId);
        if (serviceWrapper == null) {
            throw new ApplicationException("No find '" + serviceId + "' in service mapping configuration.");
        }
        return serviceWrapper;
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

    /**
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * @param transactionManager the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
