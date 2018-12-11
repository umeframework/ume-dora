/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.log.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.umeframework.dora.log.Logger;

/**
 * Logger implement class
 *
 * @author Yue MA
 */
public class Log4j2Impl implements Logger {
    /**
     * appender
     */
    private org.apache.logging.log4j.Logger appender;

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#getAppender(java.lang.String)
     */
    public Logger getAppender(String name) {
        appender = LogManager.getLogger(name);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#getAppender(java.lang.Class)
     */
    public Logger getAppender(Class<?> clazz) {
        return getAppender(clazz.getSimpleName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#debug(java.lang.Object[])
     */
    @Override
    public void debug(Object... messages) {
        if (messages != null && appender != null) {
            StringBuilder message = new StringBuilder();
            for (Object e : messages) {
                message.append(e);
            }
            appender.debug(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#debug(java.lang.Object)
     */
    public void debug(Object message) {
        if (appender != null) {
            appender.debug(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#info(java.lang.Object[])
     */
    @Override
    public void info(Object... messages) {
        if (messages != null && appender != null) {
            StringBuilder message = new StringBuilder();
            for (Object e : messages) {
                message.append(e);
            }
            appender.info(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#info(java.lang.Object)
     */
    public void info(Object message) {
        if (appender != null) {
            appender.info(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.log.Logger#warn(java.lang.Object)
     */
    @Override
    public void warn(Object message) {
        if (appender != null) {
            appender.warn(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.log.Logger#warn(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void warn(Object message, Throwable ex) {
        if (appender != null) {
            appender.warn(buildThreadContext(String.valueOf(message) + "," + ex));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.log.Logger#error(java.lang.Object)
     */
    @Override
    public void error(Object message) {
        if (appender != null) {
            appender.error(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.log.Logger#error(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void error(Object message, Throwable ex) {
        if (appender != null) {
            appender.error(buildThreadContext(String.valueOf(message)), ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.log.Logger#fatal(java.lang.Object)
     */
    @Override
    public void fatal(Object message) {
        if (appender != null) {
            appender.fatal(buildThreadContext(String.valueOf(message)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.log.Logger#fatal(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void fatal(Object message, Throwable ex) {
        if (appender != null) {
            appender.fatal(buildThreadContext(String.valueOf(message)), ex);
        }
    }

    /**
     * Build append log parameters base on ThreadContext properties.<br>
     * 
     * @param message
     * @return
     */
    protected String buildThreadContext(String message) {
        String thread = String.valueOf(Thread.currentThread().getId());
        ThreadContext.put("thread", thread);
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#isDebugEnabled()
     */
    @Override
    public boolean isDebugEnabled() {
        return appender.isDebugEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.log.Logger#isInfoEnabled()
     */
    @Override
    public boolean isInfoEnabled() {
        return appender.isInfoEnabled();
    }

}
