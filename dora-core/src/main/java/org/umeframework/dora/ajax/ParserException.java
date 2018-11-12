/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax;

/**
 * AJAXParserException
 * 
 * @author Yue MA
 */
public class ParserException extends RuntimeException {
    /**
     * default serial version UID
     */
	private static final long serialVersionUID = -8978088019448865329L;

	/**
     * Constructor
     * 
     * @param message
     */
    public ParserException(
            String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param ex
     */
    public ParserException(
            String message,
            Exception ex) {
        super(message, ex);
    }
}
