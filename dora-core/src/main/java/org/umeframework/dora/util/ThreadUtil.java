/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.lang.reflect.Method;

import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.log.Logger;

/**
 * ThreadUtil
 * 
 * @author Yue Ma
 *
 */
public class ThreadUtil extends Thread {
    /**
     * Instance for execute in sub thread
     */
    private Object instance;
    /**
     * Function for execute in sub thread
     */
    private String function;
    /**
     * Actual parameters
     */
    private Object[] parameters;
    /**
     * RequestContext of invoker side
     */
    private RequestContext context;
    /**
     * logger
     */
    private Logger logger;
    /**
     * result
     */
    private Object result; 

    /**
     * ThreadUtil
     * 
     * @param instance
     * @param function
     * @param parameters
     * @param callback
     */
    public ThreadUtil(Object instance, String function, Object[] parameters, Logger logger) {
        this.instance = instance;
        this.function = function;
        this.parameters = parameters;
        this.context = RequestContext.getCurrentContext();
        this.logger = logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        RequestContext.cloneFrom(context);
        if (logger != null) {
            logger.info("New thread to execute: " + function + " of " + instance);
        }
        try {
            Method method = ReflectUtil.getNonBridgeMethod(instance.getClass(), this.function);
            result = method.invoke(instance, parameters);
        } catch (Throwable e) {
            logger.error("Exception in thread:" + this.function + "," + this.instance, e);
        } finally {
            RequestContext.close();
        }
    }

    /**
     * @return the instance
     */
    public Object getInstance() {
        return instance;
    }

    /**
     * @param instance
     *            the instance to set
     */
    public void setInstance(Object instance) {
        this.instance = instance;
    }

    /**
     * @return the function
     */
    public String getFunction() {
        return function;
    }

    /**
     * @param function
     *            the function to set
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * @return the parameters
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     *            the parameters to set
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * @param logger
     *            the logger to set
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * @return the result
     */
    public Object getResult() {
        return result;
    }

}
