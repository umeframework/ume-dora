/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.runner.ServiceRunner;
import org.umeframework.dora.service.runner.impl.AjaxServiceRunnerImpl;

/**
 * Service runner configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultServiceRunnerConfiguration {
    /**
     * transaction manager
     */
    @Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
    private ServiceMapping serviceMapping;
    /**
     * transaction manager
     */
    @Resource(name = BeanConfigConst.DEFAULT_TRANSACTION_MANAGER)
    private PlatformTransactionManager transactionManager;

	
	/**
	 * Ajax service runner
	 * 
	 * @return
	 */
	@Bean(name = BeanConfigConst.DEFAULT_SYS_ID)
	@Scope("prototype")
	public ServiceRunner<String, String> ajaxServiceRunner() {
	    ServiceRunner<String, String> serviceRunner = new AjaxServiceRunnerImpl();
	    serviceRunner.setServiceMapping(serviceMapping);
	    serviceRunner.setTransactionManager(transactionManager);
		return serviceRunner;
	}
	
}
