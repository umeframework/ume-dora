/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * Authentication Exception
 *
 * @author Yue MA
 *
 */
public class AuthenticationException extends ApplicationException {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -5410251781190711280L;

	/**
	 * AuthenticationException
	 *
	 * @param message
	 * @param parameters
	 */
	public AuthenticationException(String message, Object[] parameters) {
		super(message, parameters);
	}

	/**
	 * AuthenticationException
	 *
	 * @param message
	 */
	public AuthenticationException(String message) {
		super(message);
	}
}
