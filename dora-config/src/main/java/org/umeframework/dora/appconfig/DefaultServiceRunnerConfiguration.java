/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.service.runner.AjaxServiceRunner;
import org.umeframework.dora.service.runner.ServiceRunner;
import org.umeframework.dora.service.runner.impl.AjaxServiceRunnerImpl;
import org.umeframework.dora.service.runner.impl.TransactionalServiceRunnerImpl;

/**
 * Service runner configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultServiceRunnerConfiguration {
	
	
	/**
	 * Ajax service runner
	 * 
	 * @return
	 */
	@Bean(name = BeanConfigConst.DEFAULT_SYS_ID)
	@Scope("prototype")
	public AjaxServiceRunner ajaxServiceRunner() {
		return new AjaxServiceRunnerImpl();
	}
	
	/**
	 * Service runner
	 * 
	 * @return
	 */
	@Bean(name = BeanConfigConst.DEFAULT_SERVICE_RUNNER)
	@Scope("prototype")
	public ServiceRunner serviceRunner() {
		return new TransactionalServiceRunnerImpl();
	}

}
