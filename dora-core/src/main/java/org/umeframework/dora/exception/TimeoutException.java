/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * TimeoutException
 * 
 * @author Yue MA
 *
 */
public class TimeoutException extends ApplicationException {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 4809286154167801279L;

	/**
	 * TimeoutException
	 *
	 * @param message
	 * @param parameters
	 */
	public TimeoutException(String message, Object[] parameters) {
		super(message, parameters);
	}
	
	/**
	 * TimeoutException
	 *
	 * @param message
	 */
	public TimeoutException(String message) {
		super(message);
	}

}
