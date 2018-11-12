/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * Exception information define for application level error.<br>
 * Application should be aware of the kind of error and decide how to process
 * it.<br>
 *
 * @author Yue MA
 */
public class ApplicationException extends RuntimeException {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7695956946846030368L;
	/**
	 * error code
	 */
	private String messageId;
	/**
	 * error parameters
	 */
	private Object[] parameters;

	/**
	 * ApplicationException
	 *
	 * @param cause
	 *            - error cause
	 * @param message
	 *            - error code
	 * @param parameters
	 *            - error parameter
	 */
	public ApplicationException(Throwable cause, String message, Object[] parameters) {
		super(cause);
		this.messageId = message;
		this.parameters = parameters;
	}
	
	/**
	 * ApplicationException
	 *
	 * @param cause
	 *            - error cause
	 * @param message
	 *            - error code
	 */
	public ApplicationException(Throwable cause, String message) {
		super(cause);
		this.messageId = message;
	}


	/**
	 * ApplicationException
	 *
	 * @param message
	 *            - error code
	 * @param parameters
	 *            - error parameter
	 */
	public ApplicationException(String message, Object[] parameters) {
		super(message);
		this.messageId = message;
		this.parameters = parameters;
	}

	/**
	 * ApplicationException
	 * 
	 * @param message
	 */
	public ApplicationException(String message) {
		super(message);
		this.messageId = message;
	}

	/**
	 * get error code
	 *
	 * @return error code
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * get error parameters
	 *
	 * @return error parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		return super.getMessage() + " " + super.getCause();
	}
}
