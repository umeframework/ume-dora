/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.transaction.TransactionManager;
import org.umeframework.dora.transaction.impl.TransactionManagerImpl;

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
	 * logger
	 */
	@Resource(name = BeanConfigConst.DEFAULT_LOGGER)
	private Logger logger;

	/**
	 * transactionManager
	 * 
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_TRANSACTION_MANAGER)
	public TransactionManager transactionManager() throws Exception {
		// if (dataSource == null) {
		// this.logger.warn("TransactionManager instance was inject as null because no found available dataSource.");
		// return null;
		// }

		TransactionManagerImpl instance = new TransactionManagerImpl();
		org.springframework.jdbc.datasource.DataSourceTransactionManager ptx = new org.springframework.jdbc.datasource.DataSourceTransactionManager();
		ptx.setDataSource(dataSource);
		instance.setPlatformTransactionManager(ptx);
		return instance;
	}

}
