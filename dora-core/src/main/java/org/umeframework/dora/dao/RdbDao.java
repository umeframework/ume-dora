/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Data query interface for RDB.<br>
 *
 * @author Yue MA
 */
public interface RdbDao {

    /**
     * Query for single record
     *
     * @param queryId
     *            - query ID
     * @param queryParam
     *            - query parameters
     * @param clazz
     *            - query return type
     * @return query record as bean
     */
    <E> E queryForObject(
            String queryId,
            Object queryParam,
            Class<? extends E> clazz);
    
    /**
     * Query for single record and return as map instance
     *
     * @param queryId
     *            - query ID
     * @param queryParam
     *            - query parameters
     * @return query record as linked hash map
     */
    Map<String, Object> queryForMap(
            String queryId,
            Object queryParam);

    /**
     * Query for multiple record
     *
     * @param <E>
     *            type of query return object
     * @param queryId
     *            - query ID
     * @param queryParam
     *            - query parameters
     * @param clazz
     *            - query return type
     * @return query record as bean list
     */
    <E> List<E> queryForObjectList(
            String queryId,
            Object queryParam,
            Class<? extends E> clazz);
    
    /**
     * Query for multiple record and return as map list
     *
     * @param <E>
     *            type of query return object
     * @param queryId
     *            - query ID
     * @param queryParam
     *            - query parameters
     * @return query record as bean list
     */
    List<Map<String, Object>> queryForMapList(
            String queryId,
            Object queryParam);
    
    /**
     * Return query record number
     *
     * @param queryId
     *            - query ID
     * @param queryParam
     *            - query parameters
     * @return record number
     */
    <E> int count(
            String queryId,
            Object queryParam);

    /**
     * Update bean instance to table
     *
     * @param updateId
     *            - SQL ID
     * @param updateParam
     *            - SQL Parameter
     * @return - updated record number
     */
    <E> int update(
            String updateId,
            E updateParam);
    
    /**
     * Update bean instance list to table
     *
     * @param updateId
     *            - SQL ID
     * @param updateParams
     *            - SQL Parameters
     * @return - updated record number as array
     */
    <E> int[] updateMulti(
            String updateId,
            List<E> updateParams);
    
    /**
     * Set data source
     * 
     * @param dataSource
     */
    void setDataSource(
            DataSource dataSource);
}
