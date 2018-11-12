/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import java.util.ArrayList;
import java.util.List;

import org.umeframework.dora.message.Message;

/**
 * Base service response object structure define
 * 
 * @author Yue MA
 */
public class ServiceErrorResponse implements java.io.Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 6064124683788167706L;
	/**
	 * service execution fail due to system exception
	 */
	public static final int SYSTEM_EXCEPTION = -1;
	/**
	 * service execution fail due to application exception
	 */
	public static final int APPLICATION_EXCEPTION = -2;
	/**
	 * service execution fail due to application exception
	 */
	public static final int VALIDATION_EXCEPTION = -3;

	/**
	 * Http status code
	 */
    private int statusCode;
    /**
     * Service exception type code
     */
    private int errorType;
	/**
	 * Exceptions of service execution
	 */
	private List<Message> exceptions;

	/**
	 * addException
	 * 
	 * @param e
	 */
	public void addException(Message e) {
		if (exceptions == null) {
			exceptions = new ArrayList<Message>();
		}
		exceptions.add(e);
	}

	/**
	 * getExceptions
	 * 
	 * @return
	 */
	public List<Message> getExceptions() {
		return exceptions;
	}

	/**
	 * @param exceptions
	 *            the exceptions to set
	 */
	public void setExceptions(List<Message> exceptions) {
		this.exceptions = exceptions;
	}

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the errorType
     */
    public int getErrorType() {
        return errorType;
    }

    /**
     * @param errorType the errorType to set
     */
    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }
}
