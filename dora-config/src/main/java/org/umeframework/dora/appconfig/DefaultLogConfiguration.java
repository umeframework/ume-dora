/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.log.Logger;

/**
 * Log configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultLogConfiguration {
	/**
	 * logger
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_LOGGER)
	public Logger logger() {
		return new org.umeframework.dora.log.impl.Log4j2Impl();
	}

}
