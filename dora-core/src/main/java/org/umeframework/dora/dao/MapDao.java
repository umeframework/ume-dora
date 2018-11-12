/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao;

import java.util.List;
import java.util.Map;

/**
 * Data access interface for key - value types store.<br>
 * Currently, F/W provide the default NO SQL database support base on MongoDB.<br>
 * 
 * @author Yue MA
 */
public interface MapDao {
    /**
     * Insert object to database
     * 
     * @param table
     *            - table/collection name
     * @param map
     *            - value to insert
     * @return count value after insert
     */
    long put(
            String table,
            Map<String, Object> map);

    /**
     * Save object to database (update when exist same ID)
     * 
     * @param table
     *            - table/collection name
     * @param map
     *            - value to insert
     * @return count value after insert
     */
    long save(
            String table,
            Map<String, Object> map);

    /**
     * Find all match objects by condition
     * 
     * @param table
     *            - table/collection name
     * @param map
     *            - search condition
     * @return search results
     */
    List<Map<String, Object>> find(
            String table,
            Map<String, Object> params);

    /**
     * Find first match object by condition
     * 
     * @param table
     *            - table/collection name
     * @param map
     *            - search condition
     * @return search result
     */
    Map<String, Object> findOne(
            String table,
            Map<String, Object> params);

}
