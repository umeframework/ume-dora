/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;


/**
 * 异常信息处理类
 */
public class BeanHelperException extends RuntimeException {
    private static final long serialVersionUID = 39369274403958891L;
    /**
     * BeanHelperException
     * 
     * @param message
     * @param e
     */
    public BeanHelperException(String message, Throwable e) {
        super(message, e);
    }
    /**
     * BeanHelperException
     * 
     * @param message
     */
    public BeanHelperException(String message) {
        super(message);
    }
}
