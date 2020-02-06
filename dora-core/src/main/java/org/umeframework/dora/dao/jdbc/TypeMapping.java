/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;


import java.util.HashMap;
import java.util.Map;

/**
 * TypeMapping
 *
 * @author Yue MA
 */
public class TypeMapping {
    /**
     * Type mapping from database to JDBC
     */
    private static final Map<String, Integer> db2JdbcMap = new HashMap<>();
    static {
        db2JdbcMap.put("VARCHAR2", java.sql.Types.VARCHAR);
        db2JdbcMap.put("NVARCHAR2", java.sql.Types.NVARCHAR);
        
        db2JdbcMap.put("VARCHAR", java.sql.Types.VARCHAR);
        db2JdbcMap.put("NVARCHAR", java.sql.Types.NVARCHAR);
        db2JdbcMap.put("LONGVARCHAR", java.sql.Types.LONGVARCHAR);
        db2JdbcMap.put("CHARACTER", java.sql.Types.CHAR);
        db2JdbcMap.put("CHAR", java.sql.Types.CHAR);
        db2JdbcMap.put("NUMBER", java.sql.Types.DECIMAL);
        db2JdbcMap.put("DECIMAL", java.sql.Types.DECIMAL);
        db2JdbcMap.put("NUMERIC", java.sql.Types.NUMERIC);
        db2JdbcMap.put("DOUBLE", java.sql.Types.DOUBLE);
        db2JdbcMap.put("FLOAT", java.sql.Types.FLOAT);
        db2JdbcMap.put("TIMESTAMP", java.sql.Types.TIMESTAMP);
        db2JdbcMap.put("DATETIME", java.sql.Types.TIMESTAMP);
        db2JdbcMap.put("DATE", java.sql.Types.DATE);
        db2JdbcMap.put("TIME", java.sql.Types.TIME);
        db2JdbcMap.put("INT", java.sql.Types.INTEGER);
        db2JdbcMap.put("INTEGER", java.sql.Types.INTEGER);
        db2JdbcMap.put("SMALLINT", java.sql.Types.SMALLINT);
        db2JdbcMap.put("BIGINT", java.sql.Types.BIGINT);
        db2JdbcMap.put("BOOLEAN", java.sql.Types.BOOLEAN);
        db2JdbcMap.put("NULL", java.sql.Types.NULL);

        db2JdbcMap.put("CLOB", java.sql.Types.CLOB);
        db2JdbcMap.put("BLOB", java.sql.Types.BLOB);
        db2JdbcMap.put("MEDIUMBLOB", java.sql.Types.LONGVARBINARY);
        db2JdbcMap.put("IMAGE", java.sql.Types.BLOB);
        db2JdbcMap.put("FILE", java.sql.Types.BLOB);
    }

    /**
     * Type mapping from JDBC to Java
     */
    private static final Map<Integer, Class<?>> jdbc2JavaMap = new HashMap<>();
    static {
        jdbc2JavaMap.put(java.sql.Types.CHAR, java.lang.String.class);
        jdbc2JavaMap.put(java.sql.Types.VARCHAR, java.lang.String.class);
        jdbc2JavaMap.put(java.sql.Types.NVARCHAR, java.lang.String.class);
        jdbc2JavaMap.put(java.sql.Types.LONGNVARCHAR, java.lang.String.class);
        jdbc2JavaMap.put(java.sql.Types.DECIMAL, java.math.BigDecimal.class);
        jdbc2JavaMap.put(java.sql.Types.NUMERIC, java.math.BigDecimal.class);
        jdbc2JavaMap.put(java.sql.Types.DOUBLE, java.lang.Double.class);
        jdbc2JavaMap.put(java.sql.Types.FLOAT, java.lang.Float.class);
        jdbc2JavaMap.put(java.sql.Types.REAL, java.lang.Float.class);
        jdbc2JavaMap.put(java.sql.Types.INTEGER, java.lang.Integer.class);
        jdbc2JavaMap.put(java.sql.Types.SMALLINT, java.lang.Short.class);
        jdbc2JavaMap.put(java.sql.Types.BIGINT, java.math.BigInteger.class);
        jdbc2JavaMap.put(java.sql.Types.TINYINT, java.lang.Byte.class);
        jdbc2JavaMap.put(java.sql.Types.BOOLEAN, java.lang.Boolean.class);
        jdbc2JavaMap.put(java.sql.Types.BIT, java.lang.Boolean.class);
        jdbc2JavaMap.put(java.sql.Types.TIMESTAMP, java.sql.Timestamp.class);
        jdbc2JavaMap.put(java.sql.Types.DATE, java.sql.Date.class);
        jdbc2JavaMap.put(java.sql.Types.TIME, java.sql.Time.class);

        jdbc2JavaMap.put(java.sql.Types.NULL, null);
        jdbc2JavaMap.put(java.sql.Types.BINARY, byte[].class);
        jdbc2JavaMap.put(java.sql.Types.VARBINARY, byte[].class);
        jdbc2JavaMap.put(java.sql.Types.LONGVARBINARY, byte[].class);

        jdbc2JavaMap.put(java.sql.Types.CLOB, java.sql.Clob.class);
        jdbc2JavaMap.put(java.sql.Types.BLOB, java.sql.Blob.class);
        jdbc2JavaMap.put(java.sql.Types.ARRAY, java.lang.reflect.Array.class);
        jdbc2JavaMap.put(java.sql.Types.STRUCT, java.sql.Struct.class);
        jdbc2JavaMap.put(java.sql.Types.DATALINK, java.net.URL.class);
    }
    
    /**
     * isBasicType
     *
     * @param obj
     * @return true/false
     */
    public static boolean isBasicType(
        Object obj) {
        return obj.getClass().isPrimitive() || obj instanceof String  || obj instanceof Number || obj instanceof java.util.Date || obj instanceof Character;
    }


    /**
     * mapDB2JDBC
     * 
     * @param dbType
     * @return
     */
    public static int mapDB2JDBC(
            String type) {
        return db2JdbcMap.containsKey(type) ? db2JdbcMap.get(type) : 0;
    }

    /**
     * mapJava2DB
     * 
     * @param dbType
     * @return
     */
    public static Class<?> mapJDBC2Java(
            Integer type) {
        return jdbc2JavaMap.containsKey(type) ? jdbc2JavaMap.get(type) : null;
    }

}
