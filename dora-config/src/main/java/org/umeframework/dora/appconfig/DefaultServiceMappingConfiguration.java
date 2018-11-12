/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.property.ConfigProperties;
import org.umeframework.dora.property.impl.ConfigPropertiesImpl;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.mapping.ServiceMappingDesc;
import org.umeframework.dora.service.mapping.impl.ServiceMappingDescImpl;
import org.umeframework.dora.service.mapping.impl.ServiceMappingImpl;

/**
 * Service mapping configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultServiceMappingConfiguration {
	/**
	 * systemPropertyConfiguration
	 */
	@Resource(name=BeanConfigConst.SYSTEM_PROPERTY_CONFIGURATION)
	private DefaultSystemPropertyConfiguration systemPropertyConfiguration;

	/**
	 * serviceMappingConfigProperties
	 * 
	 * @param location
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING_CONFIG_PROPERTIES, initMethod = "init")
	public ConfigProperties serviceMappingConfigProperties() {
		ConfigPropertiesImpl instance = new ConfigPropertiesImpl(systemPropertyConfiguration.getServiceMappingLocation());
		return instance;
	}
	/**
	 * serviceWhiteListConfigProperties
	 * 
	 * @param location
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_SERVICE_WHITE_LIST_CONFIG_PROPERTIES, initMethod = "init")
	public ConfigProperties serviceWhiteListConfigProperties() {
		ConfigPropertiesImpl instance = new ConfigPropertiesImpl(systemPropertyConfiguration.getServiceWhiteListLocation());
		return instance;
	}
	/**
	 * serviceMapping
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING, initMethod = "init")
	public ServiceMapping serviceManager() {
		ServiceMappingImpl instance = new ServiceMappingImpl();
		return instance;
	}
	
	/**
	 * serviceMappingDesc
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING_DESC)
	public ServiceMappingDesc serviceMappingDesc() {
		ServiceMappingDescImpl instance = new ServiceMappingDescImpl();
		return instance;
	}
	
}
