/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.umeframework.dora.bean.BeanConfigConst;

/**
 * Transaction configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultTransactionManagerConfiguration {
	/**
	 * dataSource
	 */
	@Resource(name = BeanConfigConst.DEFAULT_DATASOURCE)
	private DataSource dataSource;

	/**
	 * transactionManager
	 * 
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_TRANSACTION_MANAGER)
	public PlatformTransactionManager transactionManager() throws Exception {
		DataSourceTransactionManager ptx = new DataSourceTransactionManager();
		ptx.setDataSource(dataSource);
		return ptx;
	}

}
