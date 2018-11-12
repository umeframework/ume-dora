/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * FileAccessException
 * 
 * @author Yue MA/DC.YANG
 */
public class FileAccessException extends ApplicationException {

    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = -3595859961596837091L;

	/**
     * FileAccessException
     * 
     * @param cause
     * @param message
     * @param parameters
     */
    public FileAccessException(Throwable cause, String message, Object[] parameters) {
		super(cause, message, parameters);
	}
    
    /**
     * FileAccessException
     * 
     * @param cause
     * @param message
     * @param parameters
     */
    public FileAccessException(Throwable cause, String message) {
		super(cause, message);
	}

    /**
     * FileAccessException
     * 
     * @param message
     * @param parameters
     */
    public FileAccessException(String message, Object[] parameters) {
		super(message, parameters);
	}
    
    /**
     * FileAccessException
     * 
     * @param message
     */
    public FileAccessException(String message) {
		super(message);
	}

}
