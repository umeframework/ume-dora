/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao;

/**
 * StoredProcedure invoke interface for RDB.<br>
 * 
 * @author Yue MA
 * 
 */
public interface ProcedureDao {

    /**
     * @param queryId
     *            - query ID
     * @param queryParam
     *            - query parameters
     */
    <E> E execute(
            String queryId,
            Object queryParam);

}
