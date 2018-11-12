/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor;

/**
 * Intercept interface declare.<br>
 * 
 * @author Yue MA
 */
@Deprecated
public interface Interceptor {
    /**
     * Intercept entry
     * 
     * @param chain
     *            - InterceptorChain instance for all intercept instances use
     */
    void intercept(
            InterceptorChain chain) throws Throwable;
}
