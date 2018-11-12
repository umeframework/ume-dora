/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Exception process utilities.<br>
 * 
 * @author Yue MA
 */
abstract public class ExceptionUtil {
    /**
     * SERVLET_EXCEPTION_CLASS_NAME
     */
    private static final String SERVLET_EXCEPTION_CLASS_NAME = "javax.servlet.ServletException";

    /**
     * SERVLET_EXCEPTION_GET_ROOT_CAUSE
     */
    private static final String SERVLET_EXCEPTION_GET_ROOT_CAUSE = "getRootCause";

    /**
     * Get exception trace stack from exception object.<br>
     * 
     * @param throwable
     *            - input exception object
     * @return exception trace stack as string
     */
    public static String getStackTrace(
            Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (throwable != null) {
            baos.reset();
            throwable.printStackTrace(new PrintStream(baos));
            sb.append(baos.toString());
            Class<?> throwableClass = throwable.getClass();

            if (SERVLET_EXCEPTION_CLASS_NAME.equals(throwableClass.getName())) {
                try {
                    Method method = throwableClass.getMethod(SERVLET_EXCEPTION_GET_ROOT_CAUSE);
                    throwable = (Throwable) method.invoke(throwable);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throwable = throwable.getCause();
            }
        }
        return sb.toString();
    }
}
