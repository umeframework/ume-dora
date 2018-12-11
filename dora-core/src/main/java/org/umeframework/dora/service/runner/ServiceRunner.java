/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.runner;

import org.springframework.transaction.PlatformTransactionManager;
import org.umeframework.dora.service.mapping.ServiceMapping;

/**
 * Service Runner
 *
 * @author Yue MA
 *
 */
public interface ServiceRunner<IN, OUT> {
    /**
     * execute
     * 
     * @param serviceId
     * @param params
     * @return
     * @throws Throwable
     */
    OUT execute(String serviceId, IN params) throws Throwable;
    /**
     * getTransactionManager
     * 
     * @return
     */
    PlatformTransactionManager getTransactionManager();
    /**
     * setTransactionManager
     * 
     * @param transactionManager
     */
    void setTransactionManager(PlatformTransactionManager transactionManager);
    /**
     * getServiceMapping
     * 
     * @return
     */
    ServiceMapping getServiceMapping();
    /**
     * setServiceMapping
     * 
     * @param serviceMapping
     */
    void setServiceMapping(ServiceMapping serviceMapping);
}
