/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

/**
 * Bean Access Exception.<Br>
 * 
 * @author Yue MA
 */
public class BeanException extends RuntimeException {

    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = -656075453255121723L;

    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public BeanException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     * 
     * @param message
     */
    public BeanException(
            String message) {
        super(message);
    }
}
