/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.dao.RdbDao;
import org.umeframework.dora.dao.impl.BatisDaoImpl;
import org.umeframework.dora.dao.mybatis.ex.StatementHandlerInterceptor;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.util.StringUtil;

/**
 * Dao configuration.<br>
 *
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultDaoConfiguration {
	/**
	 * systemPropertyConfiguration
	 */
	@Resource(name = BeanConfigConst.SYSTEM_PROPERTY_CONFIGURATION)
	private DefaultSystemPropertyConfiguration systemPropertyConfiguration;

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
	 * RdbDao
	 * 
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_RDB_DAO)
	public RdbDao dao() throws Exception {
		// if (dataSource == null) {
		// this.logger.warn("RdbDao instance was inject as null because no found available dataSource.");
		// return null;
		// }

		RdbDao dao = new BatisDaoImpl();
		((SqlSessionDaoSupport) dao).setSqlSessionFactory(createSqlSessionFactory(dataSource, logger));
		return dao;
	}

	/**
	 * createSqlSessionFactory
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	protected SqlSessionFactory createSqlSessionFactory(DataSource dataSource, Logger logger) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		// set dataSource
		sessionFactoryBean.setDataSource(dataSource);
		// set configLocation
		String mybatisConfigLocation = systemPropertyConfiguration.getMybatisConfigLocation();
		org.springframework.core.io.Resource configLocation = new DefaultResourceLoader().getResource(mybatisConfigLocation);
		sessionFactoryBean.setConfigLocation(configLocation);

		// set mapperLocations
		String mybatisMapperLocations = systemPropertyConfiguration.getMybatisMapperLocations();
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		List<org.springframework.core.io.Resource> resources = new ArrayList<org.springframework.core.io.Resource>();
		if (StringUtil.isNotEmpty(mybatisMapperLocations)) {
			String[] values = null;
			if (mybatisMapperLocations.contains(MULTI_LOCATIONS_SPLIT_CHAR)) {
				values = mybatisMapperLocations.split(MULTI_LOCATIONS_SPLIT_CHAR);
			} else {
				values = new String[] { mybatisMapperLocations };
			}
			for (String e : values) {
				org.springframework.core.io.Resource[] mappers = resourceResolver.getResources(e);
				resources.addAll(Arrays.asList(mappers));
			}
			sessionFactoryBean.setMapperLocations(resources.toArray(new org.springframework.core.io.Resource[resources.size()]));
		}

		// set StatementHandlerInterceptor plug in
		StatementHandlerInterceptor statementHandlerInterceptor = new StatementHandlerInterceptor();
		statementHandlerInterceptor.setLogger(logger);
		StatementHandlerInterceptor[] plugins = new StatementHandlerInterceptor[] { statementHandlerInterceptor };
		sessionFactoryBean.setPlugins(plugins);
		return sessionFactoryBean.getObject();
	}

	/**
	 * Location split flag
	 */
	private static final String MULTI_LOCATIONS_SPLIT_CHAR = ";";

}
