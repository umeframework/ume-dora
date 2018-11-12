/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.connection;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * JDBC Data Source manage interface declare.
 * 
 * @author Yue MA
 */
public interface JdbcDataSourceManager {
    /**
     * Data source properties elements define
     */
    enum DataSourceProperty {
        databaseType, driverClassName, url, password, username, defaultAutoCommit, initialSize, maxActive, minIdle, maxIdle, maxWait, removeAbandoned, removeAbandonedTimeout, testOnBorrow, logAbandoned
    }

    /**
     * Get data source instance by dsId
     * 
     * @param dsId
     * @return
     */
    DataSource get(
            String dsId);

    /**
     * Get database type of this data source
     * 
     * @param dsId
     * @return
     */
    String typeOf(
            String dsId);

    /**
     * Put data source instance into pool
     * 
     * @param dsId
     * @param dsProp
     */
    void put(
            String dsId,
            DataSource dataSource);

    /**
     * Remove data source instance from pool
     * 
     * @param dsId
     */
    void remove(
            String dsId);

    /**
     * Release all data source instances in pool
     */
    void clear();

    /**
     * Begin transaction on data source
     * 
     * @param dsId
     */
    void beginTransaction(
            String dsId);

    /**
     * Commit transaction on data source
     * 
     * @param dsId
     */
    void commitTransaction(
            String dsId);

    /**
     * Roll back transaction on data source
     * 
     * @param dsId
     */
    void rollbackTransaction(
            String dsId);
    
    /**
     * size
     * 
     * @return
     */
    int size();
    
    /**
     * create
     * 
     * @param dsProp
     * @return
     */
    DataSource create(String dsId, Properties dsProp);

}
