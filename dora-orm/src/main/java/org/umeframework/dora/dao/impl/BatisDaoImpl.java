/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.umeframework.dora.bean.BeanUtil;
import org.umeframework.dora.dao.RdbDao;

/**
 * MyBatis Dao implementation class
 *
 * @author Yue MA
 */
public class BatisDaoImpl extends BatisDaoSupport implements RdbDao {
    /*
     * (non-Javadoc)
     *
     * @see tora.dao.UpdateDao#update(java.lang.String, java.lang.Object)
     */
    @Override
    public <E> int update(
            String updateId,
            E updateParam) {
    	
    	escapeSql(updateParam);
        setupDivisionParam(updateParam);

        int result = getSqlSession().update(updateId, updateParam);
        
        if (getLogger() != null && result == 0) {
            getLogger().debug("No found record to update, SQL ID is ", updateId);
        }

        return result;
    }
    
    /* (non-Javadoc)
     * 
     * @see org.umeframework.dora.dao.RdbDao#updateMulti(java.lang.String, java.util.List)
     */
    @Override
    public <E> int[] updateMulti(
            String updateId,
            List<E> updateParams) {
        
        if (updateParams == null) {
            return null;
        }
        int[] result = new int[updateParams.size()];
        for (int i = 0; i < updateParams.size(); i++) {
            result[i] = update(updateId, updateParams.get(i));
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see tora.dao.QueryDao#queryForObject(java.lang.String, java.lang.Object)
     */
    @Override
    public <E> E queryForObject(
            String queryId,
            Object queryParam,
            Class<? extends E> clazz) {

    	escapeSql(queryParam);
        setupDivisionParam(queryParam);

        E result = getSqlSession().selectOne(queryId, queryParam);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.dao.RdbDao#queryForMap(java.lang.String, java.lang.Object)
     */
    @SuppressWarnings({
            "unchecked", "rawtypes" })
    @Override
    public Map<String, Object> queryForMap(
            String queryId,
            Object queryParam) {
        
    	escapeSql(queryParam);
        setupDivisionParam(queryParam);

        Object resultObj = getSqlSession().selectOne(queryId, queryParam);
        if (resultObj instanceof Map) {
            return (Map) resultObj;
        } else {
            return BeanUtil.beanToMap(resultObj);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see tora.dao.QueryDao#queryForObjectList(java.lang.String, java.lang.Object)
     */
    @Override
    public <E> List<E> queryForObjectList(
            String queryId,
            Object queryParam,
            Class<? extends E> clazz) {

    	escapeSql(queryParam);
        setupDivisionParam(queryParam);

        List<E> result = getSqlSession().selectList(queryId, queryParam);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.dao.RdbDao#queryForMapList(java.lang.String, java.lang.Object)
     */
    @SuppressWarnings({
            "unchecked", "rawtypes" })
    @Override
    public List<Map<String, Object>> queryForMapList(
            String queryId,
            Object queryParam) {
        
    	escapeSql(queryParam);
        setupDivisionParam(queryParam);

        List<Object> resultObjList = getSqlSession().selectList(queryId, queryParam);
        if (resultObjList == null) {
            return null;
        }
        
        if (resultObjList.size() == 0) {
            return new ArrayList<Map<String, Object>>(0);
        }
        List<Map<String, Object>> resultMapList = new ArrayList<Map<String,Object>>(resultObjList.size()); 
        for (Object resultObj : resultObjList) {
            if (resultObj instanceof Map) {
                resultMapList.add((Map) resultObj);
            } else {
                resultMapList.add(BeanUtil.beanToMap(resultObj));
            }
        }
        
        return resultMapList;
    }

    /*
     * (non-Javadoc)
     *
     * @see tora.dao.QueryDao#count(java.lang.String, java.lang.Object)
     */
    @Override
    public <E> int count(
            String queryId,
            Object queryParam) {

    	escapeSql(queryParam);
        setupDivisionParam(queryParam);

        int result = getSqlSession().selectOne(queryId, queryParam);

        return result;
    }

    /**
     * @param dataSource
     */
    @Override
    public void setDataSource(
            DataSource dataSource) {
        // No use in mybatis Dao
    }

}
