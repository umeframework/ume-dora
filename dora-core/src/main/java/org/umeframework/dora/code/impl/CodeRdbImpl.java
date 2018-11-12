/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.code.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.umeframework.dora.code.CodeProperties;
import org.umeframework.dora.dao.RdbDao;
import org.umeframework.dora.exception.SystemException;

/**
 * Code implementing by RDB<br>
 * Different to MessageRDBImpl is the implementation use one single SQL to retrieve the common Code Define table.<br>
 * Return all code define elements from code table and grouping these code by "CATEGORY_ID".<br>
 * 
 * @author Yue MA
 */
public class CodeRdbImpl implements CodeProperties {
    /**
     * categoryAlias
     */
    private String categoryAlias = "category";
    /**
     * codeAlias
     */
    private String codeAlias = "code";
    /**
     * codeValueAlias
     */
    private String codeValueAlias = "codeValue";
    /**
     * codeDesc
     */
    private String codeDescAlias = "codeDesc";
    /**
     * RdbDao
     */
    private RdbDao queryDao;
    /**
     * Query SQL ID
     */
    private String queryId;
    /**
     * code list map store by category
     * the key is category ID and the value is code object as a map list
     */
    private Map<String, Map<String, Map<String, Object>>> mapListByCategory;

    /**
     * Constructor
     * 
     * @param queryDao
     * @param queryId/querySql
     */
    public CodeRdbImpl(
            RdbDao queryDao,
            String queryId) {
        this.queryDao = queryDao;
        this.queryId = queryId;
    }

    /**
     * initialize
     *
     * @param msgResources
     */
    public void init() throws SystemException {
        try {
            //Map<String, Map<String, Object>>
            mapListByCategory = new LinkedHashMap<String, Map<String, Map<String, Object>>>();
            List<Map<String, Object>> results = queryDao.queryForMapList(queryId, null);
            for (Map<String, Object> result : results) {
                String category = String.valueOf(result.get(categoryAlias)).trim();
                String code = String.valueOf(result.get(codeAlias)).trim();
                if (!mapListByCategory.containsKey(category)) {
                    mapListByCategory.put(category, new LinkedHashMap<String, Map<String, Object>>());
                }
                Map<String, Map<String, Object>> categoryData = mapListByCategory.get(category);
                categoryData.put(code, result);
            }
        } catch (Exception e) {
            throw new SystemException(e, "Failed to load RDB code resource[" + queryId + "].");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.message.CodeProperties#getCodeList(java.lang.String)
     */
    public Map<String, Map<String, Object>> getCodeMap(
            String category) {
        return mapListByCategory.get(category);
    }
    
    /* (non-Javadoc)
     * @see org.umeframework.dora.message.CodeProperties#getCodeList(java.lang.String)
     */
    @Override
    public Collection<Map<String, Object>> getCodeList(
            String category) {
        return getCodeMap(category).values();
    }
    
    /* (non-Javadoc)
     * @see org.umeframework.dora.message.CodeProperties#getCode(java.lang.String, java.lang.String)
     */
    public Map<String, Object> getCode(
            String category, String code) {
        Map<String, Map<String, Object>> codeMap = getCodeMap(category);
        return codeMap.get(code);
    }

    /* (non-Javadoc)
     * @see org.umeframework.dora.message.CodeProperties#getCodeValue(java.lang.String, java.lang.String)
     */
    public Object getCodeValue(
            String category, String code) {
        Map<String, Object> rowDate = getCode(category, code);
        return rowDate.get(codeValueAlias);
    }
    
    /* (non-Javadoc)
     * @see org.umeframework.dora.message.CodeProperties#getCodeDesc(java.lang.String, java.lang.String)
     */
    public Object getCodeDesc(
            String category, String code) {
        Map<String, Object> rowDate = getCode(category, code);
        return rowDate.get(codeDescAlias);
    }

    /* (non-Javadoc)
     * @see org.umeframework.dora.message.CodeProperties#getCategoryList()
     */
    public Set<String> getCategoryList() {
        return mapListByCategory.keySet();
    }

    /**
     * @return the queryDao
     */
    public RdbDao getRdbDao() {
        return queryDao;
    }

    /**
     * @param queryDao
     *            the queryDao to set
     */
    public void setRdbDao(
    		RdbDao queryDao) {
        this.queryDao = queryDao;
    }

    /**
     * @return the queryId
     */
    public String getQueryId() {
        return queryId;
    }

    /**
     * @param queryId
     *            the queryId to set
     */
    public void setQueryId(
            String queryId) {
        this.queryId = queryId;
    }
    /**
     * @return the categoryAlias
     */
    public String getCategoryAlias() {
        return categoryAlias;
    }

    /**
     * @param categoryAlias the categoryAlias to set
     */
    public void setCategoryAlias(
            String categoryAlias) {
        this.categoryAlias = categoryAlias;
    }

    /**
     * @return the codeAlias
     */
    public String getCodeAlias() {
        return codeAlias;
    }

    /**
     * @param codeAlias the codeAlias to set
     */
    public void setCodeAlias(
            String codeAlias) {
        this.codeAlias = codeAlias;
    }

    /**
     * @return the codeValueAlias
     */
    public String getCodeValueAlias() {
        return codeValueAlias;
    }

    /**
     * @param codeValueAlias the codeValueAlias to set
     */
    public void setCodeValueAlias(
            String codeValueAlias) {
        this.codeValueAlias = codeValueAlias;
    }

    /**
     * @return the codeDescAlias
     */
    public String getCodeDescAlias() {
        return codeDescAlias;
    }

    /**
     * @param codeDescAlias the codeDescAlias to set
     */
    public void setCodeDescAlias(
            String codeDescAlias) {
        this.codeDescAlias = codeDescAlias;
    }

}
