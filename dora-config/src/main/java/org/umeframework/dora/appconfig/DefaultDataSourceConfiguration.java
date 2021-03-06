/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.contant.BeanConfigConst;

/**
 * Data source configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultDataSourceConfiguration {
	/**
	 * systemPropertyConfiguration
	 */
	@Resource(name = BeanConfigConst.SYSTEM_PROPERTY_CONFIGURATION)
	private DefaultSystemPropertyConfiguration cfg;

	/**
	 * dataSource
	 * 
	 * @param driverClass
	 * @param url
	 * @param username
	 * @param password
	 * @param minIdle
	 * @param maxIdle
	 * @param maxActive
	 * @param initialSize
	 * @param maxWait
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_DATASOURCE)
	public DataSource dataSource() throws Exception {

		Properties dsProp = new Properties();
		dsProp.setProperty("url", cfg.getJdbcUrl());
		dsProp.setProperty("driverClassName", cfg.getJdbcDriverClass());
		dsProp.setProperty("username", cfg.getJdbcUsername());
		dsProp.setProperty("password", cfg.getJdbcPassword());
		String autoCommit = cfg.getJdbcDefaultAutoCommit();
		autoCommit = autoCommit != null && autoCommit.trim().toLowerCase().equals("true") ? "true" : "false";
		dsProp.setProperty("defaultAutoCommit", autoCommit);

		BasicDataSource ds = BasicDataSourceFactory.createDataSource(dsProp);
		ds.setInitialSize(Integer.parseInt(cfg.getJdbcInitialSize()));
		ds.setMaxTotal(Integer.parseInt(cfg.getJdbcMaxActive()));
		ds.setMinIdle(Integer.parseInt(cfg.getJdbcMinIdle()));
		ds.setMaxIdle(Integer.parseInt(cfg.getJdbcMaxIdle()));
		ds.setMaxWaitMillis(Long.valueOf(cfg.getJdbcMaxWait()));
		
		return ds;
	}

}
