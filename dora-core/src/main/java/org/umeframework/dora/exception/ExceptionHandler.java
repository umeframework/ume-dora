/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

import org.umeframework.dora.service.ServiceErrorResponse;

/**
 * ExceptionHandler interface declare
 * 
 * @author Yue MA
 * 
 */
public interface ExceptionHandler {
    /**
     * ExceptionHandler function entry
     * 
     * @param serviceResponse
     *            ServiceResponse object
     * @param exception
     *            Exception object
     */
    void handleException(
            ServiceErrorResponse serviceResponse,
            Throwable exception);
}
