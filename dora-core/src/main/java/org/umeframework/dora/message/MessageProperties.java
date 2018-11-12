/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message;

/**
 * Message resource manage class for whole code-value with dynamic parameters
 * structure resources.<br>
 * 
 * @author Yue MA
 */
public interface MessageProperties {
    /**
     * Get message by message code and parameters
     * 
     * @param id
     *            - message code
     * @param parameters
     *            - message parameters
     * @return - message contents
     */
    String get(
            String id,
            Object... parameters);



}
