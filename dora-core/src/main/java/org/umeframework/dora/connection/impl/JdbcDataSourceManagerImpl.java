/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.connection.impl;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.umeframework.dora.connection.JdbcDataSourceManager;
import org.umeframework.dora.exception.DataAccessException;
import org.umeframework.dora.util.StringUtil;

/**
 * JDBC Data Source manage default implementation.
 * 
 * @author Yue MA
 */
public class JdbcDataSourceManagerImpl implements JdbcDataSourceManager {
	/**
	 * Data source instance managed map
	 */
	private Map<String, DataSource> dsMap = new java.util.concurrent.ConcurrentHashMap<String, DataSource>();
	/**
	 * Data source properties managed map
	 */
	private Map<String, Properties> dsPropMap = new java.util.concurrent.ConcurrentHashMap<String, Properties>();
	/**
	 * Transaction status managed map
	 */
	private Map<String, Stack<TransactionStatus>> tsMap = new java.util.concurrent.ConcurrentHashMap<String, Stack<TransactionStatus>>();
	/**
	 * Transaction manager managed map
	 */
	private Map<String, DataSourceTransactionManager> tmMap = new java.util.concurrent.ConcurrentHashMap<String, DataSourceTransactionManager>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.umeframework.dora.pool.CommonDataSourcePool#beginTransaction(java.lang.String)
	 */
	public void beginTransaction(String dsId) {
		try {
			Stack<TransactionStatus> tsStack = tsMap.get(dsId);
			if (tsStack == null) {
				tsStack = new Stack<TransactionStatus>();
			}
			DefaultTransactionDefinition propagation = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);

			if (!tmMap.containsKey(dsId)) {
				DataSource ds = get(dsId);
				tmMap.put(dsId, new DataSourceTransactionManager(ds));
			}
			DataSourceTransactionManager tm = tmMap.get(dsId);
			TransactionStatus ts = tm.getTransaction(propagation);
			tsStack.push(ts);
			tsMap.put(dsId, tsStack);
		} catch (Exception e) {
			throw new DataAccessException(e, "Failed to begin JDBC transaction for " + dsId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.umeframework.dora.pool.CommonDataSourcePool#commitTransaction(java.lang.String)
	 */
	public void commitTransaction(String dsId) {
		try {
			Stack<TransactionStatus> tsStack = tsMap.get(dsId);
			if (tsStack != null) {
				TransactionStatus ts = tsStack.pop();
				DataSourceTransactionManager tm = tmMap.get(dsId);
				tm.commit(ts);
			}
		} catch (Exception e) {
			throw new DataAccessException(e, "Failed to commit JDBC transaction for " + dsId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.umeframework.dora.pool.CommonDataSourcePool#rollbackTransaction(java.lang.String)
	 */
	public void rollbackTransaction(String dsId) {
		try {
			Stack<TransactionStatus> tsStack = tsMap.get(dsId);
			if (tsStack != null) {
				TransactionStatus ts = tsStack.pop();
				DataSourceTransactionManager tm = tmMap.get(dsId);
				tm.rollback(ts);
			}
		} catch (Exception e) {
			throw new DataAccessException(e, "Failed to commit JDBC transaction for " + dsId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#get(java.lang.String)
	 */
	public DataSource get(String dsId) {
		DataSource ds = dsMap.get(dsId);
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#put(java.lang.String,
	 * java.util.Properties)
	 */
	@Override
	public synchronized void put(String dsId, DataSource dataSource) {
		dsMap.put(dsId, dataSource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#create(java.util.Properties)
	 */
	@Override
	public DataSource create(String dsId, Properties dsProp) {
		DataSource dataSource;
		try {
			if (StringUtil.isEmpty(dsProp.getProperty(JdbcDataSourceManager.DataSourceProperty.defaultAutoCommit.toString()))) {
				dsProp.setProperty(JdbcDataSourceManager.DataSourceProperty.defaultAutoCommit.toString(), "false");
			}
			dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(dsProp);
		} catch (Exception e) {
			throw new DataAccessException(e, "Failed to create JDBC data source for " + dsProp.getProperty(JdbcDataSourceManager.DataSourceProperty.url.toString()));
		}
		dsMap.put(dsId, dataSource);
		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#remove(java.lang.String)
	 */
	public synchronized void remove(String dsId) {
		if (dsMap.containsKey(dsId)) {
			BasicDataSource bds = (BasicDataSource) get(dsId);
			if (bds != null) {
				try {
					bds.close();
				} catch (SQLException e) {
					throw new DataAccessException(e, "Failed to close JDBC data source for " + dsId);
				}
			}
			// tsMap.remove(dsId);
			dsMap.remove(dsId);
			dsPropMap.remove(dsId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#clear()
	 */
	public synchronized void clear() {
		for (Map.Entry<String, DataSource> entry : dsMap.entrySet()) {
			BasicDataSource bds = (BasicDataSource) entry.getValue();
			if (bds != null) {
				try {
					bds.close();
				} catch (SQLException e) {
					throw new DataAccessException(e, "Failed to close JDBC data source for " + entry.getKey());
				}
			}
		}
		// tsMap.clear();
		dsMap.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#size()
	 */
	public int size() {
		return dsMap.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.pool.CommonDataSourcePool#getDatabaseType(java.lang.String)
	 */
	@Override
	public String typeOf(String dsId) {
		String token = null;
		DataSource ds = dsMap.get(dsId);
		if (ds instanceof BasicDataSource) {
			@SuppressWarnings("resource")
			BasicDataSource bds = (BasicDataSource) ds;
			token = bds.getUrl().toUpperCase();
			token = token.replaceFirst("JDBC:", "");
			token = token.contains(":") ? token.substring(0, token.indexOf(":")) : null;
		}
		return token;
	}

}
