/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.log;

/**
 * Logger interface declare
 *
 * @author Yue MA
 */
public interface Logger {
    
    /**
     * getAppender
     * 
     * @param name
     * @return
     */
    Logger getAppender(String name);
    /**
     * getAppender
     * 
     * @param clazz
     * @return
     */
    Logger getAppender(Class<?> clazz);

    /**
     * debug
     *
     * @param messages
     *            target messages
     */
    void debug(
            java.lang.Object... messages);
    
    /**
     * debug
     * 
     * @param message
     *            target message
     */
    void debug(
            java.lang.Object message);

    /**
     * info
     *
     * @param message
     *            target message
     */
    void info(
            java.lang.Object... messages);

    /**
     * info
     * 
     * @param message
     *            target message
     */
    void info(
            java.lang.Object message);

    /**
     * warn
     *
     * @param message
     *            target message
     */
    void warn(
            java.lang.Object message);

    /**
     * warn
     *
     * @param message
     *            target message
     * @param ex
     *            cause exception
     */
    void warn(
            java.lang.Object message,
            java.lang.Throwable ex);

    /**
     * error
     *
     * @param message
     *            target message
     */
    void error(
            java.lang.Object message);

    /**
     * error
     *
     * @param message
     *            target message
     * @param ex
     *            cause exception
     */
    void error(
            java.lang.Object message,
            java.lang.Throwable ex);

    /**
     * fatal
     *
     * @param message
     *            target message
     */
    void fatal(
            java.lang.Object message);

    /**
     * fatal
     *
     * @param message
     *            target message
     * @param ex
     *            cause exception
     */
    void fatal(
            java.lang.Object message,
            java.lang.Throwable ex);

    /**
     * isDebugEnabled
     * 
     * @return
     */
    boolean isDebugEnabled();
    
    /**
     * isInfoEnabled
     * 
     * @return
     */
    boolean isInfoEnabled();
}
