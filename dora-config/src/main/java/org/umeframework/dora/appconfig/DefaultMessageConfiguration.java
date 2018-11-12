/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.exception.SystemException;
import org.umeframework.dora.message.MessageProperties;

/**
 * Message configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultMessageConfiguration {
	/**
	 * systemPropertyConfiguration
	 */
	@Resource(name=BeanConfigConst.SYSTEM_PROPERTY_CONFIGURATION)
	private DefaultSystemPropertyConfiguration cfg;
	
	/**
	 * messageProperties
	 * 
	 * @param location
	 * @return
	 * @throws SystemException
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_MESSAGE_PROPERTIES) 
	public MessageProperties messageProperties() throws Exception {
		return new org.umeframework.dora.message.impl.MessagePropertiesImpl(cfg.getMessagePropertiesLocations());
	}
}
