/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

/**
 * DataAccessException
 * 
 * @author Yue MA/DC.YANG
 */
public class DataAccessException extends ApplicationException {
    
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 7420321810402490381L;

	/**
     * DataAccessException
     * 
     * @param cause
     * @param message
     * @param parameters
     */
    public DataAccessException(Throwable cause, String message, Object[] parameters) {
		super(cause, message, parameters);
	}
    
    /**
     * DataAccessException
     * 
     * @param cause
     * @param message
     * @param parameters
     */
    public DataAccessException(Throwable cause, String message) {
		super(cause, message);
	}

    /**
     * DataAccessException
     * 
     * @param message
     * @param parameters
     */
    public DataAccessException(String message, Object[] parameters) {
		super(message, parameters);
	}
    
    /**
     * DataAccessException
     * 
     * @param message
     */
    public DataAccessException(String message) {
		super(message);
	}

}
