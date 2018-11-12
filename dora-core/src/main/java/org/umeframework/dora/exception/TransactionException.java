/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * Database transaction exception inherit from ApplicationException.<br>
 * The framework use this kinds of exception to make current transaction roll back only.<br>
 *
 * @author Yue MA
 */
public class TransactionException extends ApplicationException {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -8052280248024078031L;

	/**
	 * Constructor
	 *
	 * @param cause
	 * @param message
	 * @param parameters
	 */
	public TransactionException(Throwable cause, String message, String...parameters) {
		super(cause, message, parameters);
	}

	/**
	 * Constructor
	 *
	 * @param message
	 */
	public TransactionException(String message, String...parameters) {
		super(message, parameters);
	}
}
