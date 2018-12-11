/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner.impl;

/**
 * Service Runner
 *
 * @author Yue MA
 *
 */
public class ServiceRunnerImpl extends AbstractServcieRunner<Object[], Object> {

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.runner.impl.AbstractServcieRunner#parseInput(java.lang.Object)
     */
    @Override
    protected Object[] parseInput(String serviceId, Object[] params) {
        return params;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.runner.impl.AbstractServcieRunner#renderOutput(java.lang.Object)
     */
    @Override
    protected Object renderOutput(String serviceId, Object result) {
        return result;
    }
}
