/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;


import java.util.List;

/**
 * SQLMapItem
 * 
 * @author Yue MA
 *
 */
public class JdbcParam implements java.io.Serializable {
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 4839379493892674640L;
    /**
     * SQL text
     */
    private String sqlText;
    /**
     * SQL text for JDBC style
     */
    private String sqlJdbcText;
    /**
     * SQL parameters
     */
    private List<String> sqlParams;
    
    /**
     * @return the sqlText
     */
    public String getSqlText() {
        return sqlText;
    }
    /**
     * @param sqlText the sqlText to set
     */
    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }
    /**
     * @return the sqlJdbcText
     */
    public String getSqlJdbcText() {
        return sqlJdbcText;
    }
    /**
     * @param sqlJdbcText the sqlJdbcText to set
     */
    public void setSqlJdbcText(String sqlJdbcText) {
        this.sqlJdbcText = sqlJdbcText;
    }
    /**
     * @return the sqlParams
     */
    public List<String> getSqlParams() {
        return sqlParams;
    }
    /**
     * @param sqlParams the sqlParams to set
     */
    public void setSqlParams(List<String> sqlParams) {
        this.sqlParams = sqlParams;
    }


}
