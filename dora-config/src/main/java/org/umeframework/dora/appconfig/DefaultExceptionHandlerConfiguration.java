/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.exception.ExceptionHandler;
import org.umeframework.dora.exception.impl.ExceptionHandlerImpl;

/**
 * Intercept configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultExceptionHandlerConfiguration {

	/**
	 * exceptionHandler
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_EXCEPTION_HANDLER)
	public ExceptionHandler exceptionHandler() {
		ExceptionHandlerImpl instance = new ExceptionHandlerImpl();
		return instance;
	}

}
