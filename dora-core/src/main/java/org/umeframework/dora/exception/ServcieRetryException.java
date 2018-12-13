/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

import org.umeframework.dora.context.RequestContext;

/**
 * Exception information define for application level error which should cause
 * service re-call in request level.<br>
 * 
 * @author Yue MA
 */
public class ServcieRetryException extends ApplicationException {
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 6971308932520505105L;

	/**
     * key string in request context
     */
    public static final String CONTEXT_KEY = ServcieRetryException.class.getName();

    /**
     * retry index
     */
    int retryIndex;
    /**
     * retry interval times (millisecond)
     */
    int retryInterval;
    /**
     * max retry times
     */
    int retryMaxTime;
    /**
     * retry service id
     */
    private String retryServiceId;

    /**
     * ServcieRetryException
     * 
     * @param messageId
     * @param parameters
     */
    public ServcieRetryException(
            int retryInterval,
            int retryMaxTime,
            Throwable cause,
            String messageId,
            Object[] parameters) {
        super(cause, messageId, parameters);

        ServcieRetryException sre = (ServcieRetryException) RequestContext.getCurrentContext().get(CONTEXT_KEY);
        if (sre != null) {
            this.retryInterval = sre.getRetryInterval();
            this.retryMaxTime = sre.getRetryMaxTime();
            this.retryIndex = sre.getRetryIndex();

        } else {
            this.retryInterval = retryInterval;
            this.retryMaxTime = retryMaxTime;
        }
    }

    /**
     * @return the retryInterval
     */
    public int getRetryInterval() {
        return retryInterval;
    }

    /**
     * @param retryInterval
     *            the retryInterval to set
     */
    public void setRetryInterval(
            int retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * @return the retryMaxTime
     */
    public int getRetryMaxTime() {
        return retryMaxTime;
    }

    /**
     * @param retryMaxTime
     *            the retryMaxTime to set
     */
    public void setRetryMaxTime(
            int retryMaxTime) {
        this.retryMaxTime = retryMaxTime;
    }

    /**
     * @return the retryServiceId
     */
    public String getRetryServiceId() {
        return retryServiceId;
    }

    /**
     * @param retryServiceId
     *            the retryServiceId to set
     */
    public void setRetryServiceId(
            String retryServiceId) {
        this.retryServiceId = retryServiceId;
    }

    /**
     * @return the retryIndex
     */
    public int getRetryIndex() {
        return retryIndex;
    }

    /**
     * @param retryIndex
     *            the retryIndex to set
     */
    public void setRetryIndex(
            int retryIndex) {
        this.retryIndex = retryIndex;
    }

}
