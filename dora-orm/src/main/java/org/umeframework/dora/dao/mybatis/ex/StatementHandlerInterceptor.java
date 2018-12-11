/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.mybatis.ex;

import java.sql.Connection;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.log.LogUtil;
import org.umeframework.dora.log.Logger;

/**
 * @author Yue MA
 *
 */
@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class, Integer.class }) })
public class StatementHandlerInterceptor implements Interceptor {
	/**
	 * data logger
	 */
	@Resource(name=BeanConfigConst.DEFAULT_LOGGER)
	private Logger logger;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin
	 * .Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		StatementHandler handler = (StatementHandler) invocation.getTarget();
		
		
		
		MetaObject metaStatementHandler = SystemMetaObject.forObject(handler);

		String originalSqlId = (String) metaStatementHandler.getValue("delegate.mappedStatement.id");
		String originalSqlText = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		Object originalSqlParam = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
		
		if (getLogger() != null && getLogger().isDebugEnabled()) {
			logger.debug(originalSqlId, "=", LogUtil.toCompactFormat(LogUtil.toPlantText(originalSqlText)));
			logger.debug("Param=", LogUtil.toPlantText(originalSqlParam));
		}

		return invocation.proceed();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties properties) {
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
