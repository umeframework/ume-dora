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
 * @author mayue
 *
 */
public class ThreadUtil extends Thread {
    /**
     * Block for synchronized thread
     */
    private static final byte[] block = new byte[0];

    /**
     * Thread call back interface
     */
    public static interface ThreadHandler {

        /**
         * doBefore
         * 
         * @param parentContext
         * @param parameters
         */
        void doBefore(RequestContext<?> parentContext, Object[] parameters);

        /**
         * doAfter
         * 
         * @param parentContext
         * @param result
         * @return
         */
        Object doAfter(RequestContext<?> parentContext, Object result);

        /**
         * doException
         * 
         * @param parentContext
         * @param e
         * @return
         */
        Object doException(RequestContext<?> parentContext, Throwable e);

        /**
         * doFinally
         * 
         * @param parentContext
         */
        void doFinally(RequestContext<?> parentContext);
    }

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
     * Call back instance for result handling
     */
    private ThreadHandler handler;
    /**
     * RequestContext of invoker side
     */
    private RequestContext<?> parentContext;
    /**
     * logger
     */
    private Logger logger;

    /**
     * ThreadUtil
     * 
     * @param instance
     * @param function
     * @param parameters
     * @param callback
     */
    public ThreadUtil(Object instance, String function, Object[] parameters, ThreadHandler callback, Logger logger) {
        this.instance = instance;
        this.function = function;
        this.parameters = parameters;
        this.handler = callback;
        this.parentContext = RequestContext.open();
        this.logger = logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        synchronized (block) {
            if (logger != null) {
                logger.info("New thread to execute: " + function + " of " + instance);
            }

            RequestContext<?> childContext = RequestContext.openFrom(parentContext);

            Object result = null;
            try {
                Method method = ReflectUtil.getNonBridgeMethod(instance.getClass(), this.function);
                if (handler != null) {
                    handler.doBefore(childContext, parameters);
                }
                result = method.invoke(instance, parameters);
                if (handler != null) {
                    result = handler.doAfter(childContext, result);
                }
            } catch (Throwable e) {
                logger.error("Exception in thread:" + this.function + "," + this.instance, e);
                if (handler != null) {
                    result = handler.doException(childContext, e);
                }
            } finally {
                if (handler != null) {
                    handler.doFinally(childContext);
                }
            }
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
     * @return the handler
     */
    public ThreadHandler getHandler() {
        return handler;
    }

    /**
     * @param handler
     *            the handler to set
     */
    public void setHandler(ThreadHandler handler) {
        this.handler = handler;
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

}
