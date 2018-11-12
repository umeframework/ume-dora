/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * Exception information define for system level error.<br>
 * The kind of error use to process by framework, no need application's
 * interpose.<br>
 *
 * @author Yue MA
 */
public class SystemException extends Exception {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -1912514431193290117L;
	/**
	 * error code
	 */
	private String messageId;
	/**
	 * error parameters
	 */
	private Object[] parameters;

	/**
	 * SystemException
	 *
	 * @param message
	 * @param parameters
	 */
	public SystemException(String message, Object[] parameters) {
		this.messageId = message;
		this.parameters = parameters;
	}

	/**
	 * SystemException
	 *
	 * @param cause
	 * @param message
	 * @param parameters
	 */
	public SystemException(Throwable cause, String message, Object[] parameters) {
		super(cause);
		this.messageId = message;
		this.parameters = parameters;
	}
	
	/**
	 * SystemException
	 *
	 * @param cause
	 * @param message
	 */
	public SystemException(Throwable cause, String message) {
		super(cause);
		this.messageId = message;
	}

	/**
	 * SystemException
	 *
	 * @param message
	 */
	public SystemException(String message) {
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
