/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;

/**
 * JdbcDaoException
 */
public class JdbcDaoException extends RuntimeException {
    private static final long serialVersionUID = 39369274403958891L;

    /**
     * newInstance
     * 
     * @param message
     * @param cause
     * @param args
     * @return
     */
    public static JdbcDaoException newInstance(String message, Throwable cause, Object... args) {
        message = convertMessage(message, args);
        return new JdbcDaoException(message, cause);
    }

    /**
     * newInstance
     * 
     * @param message
     * @param args
     * @return
     */
    public static JdbcDaoException newInstance(String message, Object... args) {
        message = convertMessage(message, args);
        return new JdbcDaoException(message);
    }
    
    /**
     * @param code 异常码
     * @param message 异常信息
     * @param cause 异常
     * @param values 参数
     */
    public JdbcDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param code 异常码
     * @param message 异常信息
     */
    public JdbcDaoException(String message) {
        super(message);
    }

    /**
     * fillMessage
     * 
     * @param message
     * @param args
     * @return
     */
    private static String convertMessage(String message, Object[] args) {
        if (args != null && args.length > 0 && message.contains("{}")) {
            for (Object arg : args) {
                if (message.contains("{}")) {
                    message = message.replaceFirst("\\{\\}", String.valueOf(arg));
                }
            }
        }
        return message;
    }
}
