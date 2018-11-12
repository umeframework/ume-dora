/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.impl;

import org.umeframework.dora.dao.ProcedureDao;

/**
 * ProcedureDao implementation class
 * 
 * @author Yue MA
 */
public class BatisProcedureDao extends BatisDaoSupport implements ProcedureDao {

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.dao.ProcedureDao#execute(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public <E> E execute(
            String queryId,
            Object queryParam) {

        setupDivisionParam(queryParam);

        E result = getSqlSession().selectOne(queryId, queryParam);
        return result;
    }

}
