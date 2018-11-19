/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.ds.JdbcDataSourceManager;
import org.umeframework.dora.ds.impl.JdbcDataSourceManagerImpl;

/**
 * Data source configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultJdbcDataSourceManagerConfiguration {
	/**
	 * jdbcDataSourceManager
	 * 
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_JDBC_DATASOURCE_MANAGER)
	public JdbcDataSourceManager jdbcDataSourceManager() throws Exception {
		JdbcDataSourceManagerImpl instance = new JdbcDataSourceManagerImpl();
		return instance;
	}
}
