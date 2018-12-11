/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.property.impl.ConfigPropertiesImpl;

/**
 * DefaultSystemPropertyConfiguration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultSystemPropertyConfiguration implements Serializable {
	/**
	 * serialVersionUID.<br>
	 */
	private static final long serialVersionUID = -7891532311302380076L;

	/**
	 * configuration properties file locations.<br>
	 */
	private static final String configLocations = "config.properties;application.properties";

	/**
	 * logger.<br>
	 */
	@Resource(name = BeanConfigConst.DEFAULT_LOGGER)
	private Logger logger;

	/**
	 * Effective configurations.<br>
	 */
	private Map<String, String> effectiveConfig;

	/**
	 * DefaultSystemPropertyConfiguration instance declare.<br>
	 * 
	 * @return
	 * @throws IOException
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.SYSTEM_PROPERTY_CONFIGURATION, initMethod = "init")
	static public DefaultSystemPropertyConfiguration systemPropertyConfiguration() throws IOException {
		// Declare as static method to avoid "factory-bean reference points back to the same bean definition" exceptions.
		DefaultSystemPropertyConfiguration instance = new DefaultSystemPropertyConfiguration();
		return instance;
	}

	/**
	 * Initialize process.<br>
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		effectiveConfig = new java.util.concurrent.ConcurrentHashMap<String, String>();
		for (String configLocation : configLocations.split(";")) {

			ConfigPropertiesImpl config = new ConfigPropertiesImpl(configLocation);
			try {
				config.init();
				for (String key : config.keySet()) {
					if (!effectiveConfig.containsKey(key)) {
						effectiveConfig.put(key, config.get(key));
						logger.info("Load system property '" + key + "' from " + configLocation);
					}
				}

			} catch (IOException e) {
				// ignore errors
			}
		}
		if (effectiveConfig.size() == 0) {
			throw new IOException("No found any system configuration in location " + configLocations);
		}
	}

	/**
	 * Get configuration value by key.<br>
	 * 
	 * @param key
	 * @return
	 */
	protected String get(String key) {
		String value = effectiveConfig.get(key);
		if (value == null && key.startsWith("ume.")) {
			String tryKey = key.substring(4);

			if (!effectiveConfig.containsKey(tryKey)) {
				logger.warn("No found properties key define in system effective configuration:" + key);
			}
			value = effectiveConfig.get(tryKey);
		}
		if (value == null) {
			logger.warn("No found properties value define in system effective configuration:" + key);
		}
		return value;
	}

	/**
	 * getMessagePropertiesLocations.<br>
	 * 
	 * @return
	 */
	public String getMessagePropertiesLocations() {
		return get("ume.messageProperties.locations");
	}

	/**
	 * getServiceMappingLocation.<br>
	 * 
	 * @return
	 */
	public String getServiceMappingLocation() {
		return get("ume.serviceMapping.location");
	}

	/**
	 * getServiceWhiteListLocation.<br>
	 * 
	 * @return
	 */
	public String getServiceWhiteListLocation() {
		return get("ume.serviceWhiteList.location");
	}

	/**
	 * getMybatisConfigLocation.<br>
	 * 
	 * @return
	 */
	public String getMybatisConfigLocation() {
		return get("ume.mybatis.config.location");
	}

	/**
	 * getMybatisMapperLocations.<br>
	 * 
	 * @return
	 */
	public String getMybatisMapperLocations() {
		return get("ume.mybatis.mapper.locations");
	}

	/**
	 * getJdbcDriverClass.<br>
	 * 
	 * @return
	 */
	public String getJdbcDriverClass() {
		return get("ume.jdbc.driverClassName");
	}

	/**
	 * getJdbcUrl.<br>
	 * 
	 * @return
	 */
	public String getJdbcUrl() {
		return get("ume.jdbc.url");
	}

	/**
	 * getJdbcUsername.<br>
	 * 
	 * @return
	 */
	public String getJdbcUsername() {
		return get("ume.jdbc.username");
	}

	/**
	 * getJdbcPassword.<br>
	 * 
	 * @return
	 */
	public String getJdbcPassword() {
		return get("ume.jdbc.password");
	}

	/**
	 * getJdbcDefaultAutoCommit.<br>
	 * 
	 * @return
	 */
	public String getJdbcDefaultAutoCommit() {
		return get("ume.jdbc.defaultAutoCommit");
	}

	/**
	 * getJdbcInitialSize.<br>
	 * 
	 * @return
	 */
	public String getJdbcInitialSize() {
		return get("ume.jdbc.initialSize");
	}

	/**
	 * getJdbcMinIdle.<br>
	 * 
	 * @return
	 */
	public String getJdbcMinIdle() {
		return get("ume.jdbc.minIdle");
	}

	/**
	 * getJdbcMaxIdle.<br>
	 * 
	 * @return
	 */
	public String getJdbcMaxIdle() {
		return get("ume.jdbc.maxIdle");
	}

	/**
	 * getJdbcMaxActive.<br>
	 * 
	 * @return
	 */
	public String getJdbcMaxActive() {
		return get("ume.jdbc.maxActive");
	}

	/**
	 * getJdbcMaxWait.<br>
	 * 
	 * @return
	 */
	public String getJdbcMaxWait() {
		return get("ume.jdbc.maxWait");
	}
	// JDBC properties configuration end

	/**
	 * getFileUploadDir
	 * 
	 * @return
	 */
	public String getFileUploadDir() {
		return get("ume.file.upload.dir");
	}

	/**
	 * getFileUploadExtension
	 * 
	 * @return
	 */
	public String getFileUploadExtension() {
		return get("ume.file.upload.extension");
	}

	/**
	 * getFileUploadSize
	 * 
	 * @return
	 */
	public String getFileUploadSize() {
		return get("ume.file.upload.size");
	}

	/**
	 * getDefaultPage
	 * 
	 * @return
	 */
	public String getDefaultPage() {
		return get("ume.default.page");
	}

}
