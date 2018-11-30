/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.bean.BeanFactory;
import org.umeframework.dora.bean.impl.BeanFactoryImpl;

/**
 * Bean Factory configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultBeanFactoryConfiguration {
	/**
	 * beanFactory
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_BEAN_FACTORY)
	public BeanFactory beanFactory() {
		BeanFactoryImpl instance = new BeanFactoryImpl();
		return instance;
	}
}
