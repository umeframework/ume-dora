/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.umeframework.dora.service.BaseComponent;

/**
 * JdbcTypeMapping
 *
 * @author Yue MA
 */
public class JdbcTypeMapping extends BaseComponent {
    /**
     * Type mapping from java data to database
     */
    private static final Map<Class<?>, String> MAP_JAVA_2_DB = new HashMap<Class<?>, String>();
    static {
        MAP_JAVA_2_DB.put(java.lang.String.class, "VARCHAR");
        MAP_JAVA_2_DB.put(java.lang.Integer.class, "INTEGER");
        MAP_JAVA_2_DB.put(java.lang.Short.class, "SMALLINT");
        MAP_JAVA_2_DB.put(java.lang.Long.class, "BIGINT");
        MAP_JAVA_2_DB.put(java.lang.Double.class, "DOUBLE");
        MAP_JAVA_2_DB.put(java.lang.Float.class, "FLOAT");
        MAP_JAVA_2_DB.put(java.lang.Boolean.class, "BOOLEAN");
        MAP_JAVA_2_DB.put(java.lang.Character.class, "CHAR");
        MAP_JAVA_2_DB.put(java.lang.Byte.class, "BYTE");
        MAP_JAVA_2_DB.put(java.sql.Date.class, "DATE");
        MAP_JAVA_2_DB.put(java.sql.Time.class, "TIME");
        MAP_JAVA_2_DB.put(java.sql.Timestamp.class, "TIMESTAMP");
        MAP_JAVA_2_DB.put(java.util.Date.class, "DATE");
        MAP_JAVA_2_DB.put(java.math.BigInteger.class, "BIGINT");
        MAP_JAVA_2_DB.put(java.math.BigDecimal.class, "DECIMAL");

        MAP_JAVA_2_DB.put(org.umeframework.dora.type.Blob.class, "BLOB");
        MAP_JAVA_2_DB.put(org.umeframework.dora.type.Clob.class, "CLOB");
    }

    /**
     * Type mapping from database to JDBC
     */
    private static final Map<String, Integer> MAP_DB_2_JDBC = new HashMap<String, Integer>();
    static {
        MAP_DB_2_JDBC.put("CHARACTER", java.sql.Types.CHAR);
        MAP_DB_2_JDBC.put("CHAR", java.sql.Types.CHAR);
        MAP_DB_2_JDBC.put("VARCHAR", java.sql.Types.VARCHAR);
        MAP_DB_2_JDBC.put("LONGVARCHAR", java.sql.Types.LONGVARCHAR);
        MAP_DB_2_JDBC.put("DECIMAL", java.sql.Types.DECIMAL);
        MAP_DB_2_JDBC.put("NUMERIC", java.sql.Types.NUMERIC);
        MAP_DB_2_JDBC.put("DOUBLE", java.sql.Types.DOUBLE);
        MAP_DB_2_JDBC.put("FLOAT", java.sql.Types.FLOAT);
        MAP_DB_2_JDBC.put("TIMESTAMP", java.sql.Types.TIMESTAMP);
        MAP_DB_2_JDBC.put("DATETIME", java.sql.Types.TIMESTAMP);
        MAP_DB_2_JDBC.put("DATE", java.sql.Types.DATE);
        MAP_DB_2_JDBC.put("TIME", java.sql.Types.TIME);
        MAP_DB_2_JDBC.put("INT", java.sql.Types.INTEGER);
        MAP_DB_2_JDBC.put("INTEGER", java.sql.Types.INTEGER);
        MAP_DB_2_JDBC.put("SMALLINT", java.sql.Types.SMALLINT);
        MAP_DB_2_JDBC.put("BIGINT", java.sql.Types.BIGINT);
        MAP_DB_2_JDBC.put("BOOLEAN", java.sql.Types.BOOLEAN);
        MAP_DB_2_JDBC.put("NULL", java.sql.Types.NULL);

        MAP_DB_2_JDBC.put("CLOB", java.sql.Types.CLOB);
        MAP_DB_2_JDBC.put("BLOB", java.sql.Types.BLOB);
        MAP_DB_2_JDBC.put("MEDIUMBLOB", java.sql.Types.LONGVARBINARY);
        MAP_DB_2_JDBC.put("IMAGE", java.sql.Types.BLOB);
        MAP_DB_2_JDBC.put("FILE", java.sql.Types.BLOB);
    }

    /**
     * Type mapping from JDBC to Java
     */
    private static final Map<Integer, Class<?>> MAP_JDBC_2_JAVA = new HashMap<Integer, Class<?>>();
    static {
        MAP_JDBC_2_JAVA.put(java.sql.Types.CHAR, java.lang.String.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.VARCHAR, java.lang.String.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.LONGVARCHAR, java.lang.String.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.DECIMAL, java.math.BigDecimal.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.NUMERIC, java.math.BigDecimal.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.DOUBLE, java.lang.Double.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.FLOAT, java.lang.Float.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.REAL, java.lang.Float.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.INTEGER, java.lang.Integer.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.SMALLINT, java.lang.Short.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.BIGINT, java.math.BigInteger.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.TINYINT, java.lang.Byte.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.BOOLEAN, java.lang.Boolean.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.BIT, java.lang.Boolean.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.TIMESTAMP, java.sql.Timestamp.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.DATE, java.sql.Date.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.TIME, java.sql.Time.class);

        MAP_JDBC_2_JAVA.put(java.sql.Types.NULL, null);
        MAP_JDBC_2_JAVA.put(java.sql.Types.BINARY, byte[].class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.VARBINARY, byte[].class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.LONGVARBINARY, byte[].class);

        MAP_JDBC_2_JAVA.put(java.sql.Types.CLOB, java.sql.Clob.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.BLOB, java.sql.Blob.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.ARRAY, java.lang.reflect.Array.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.STRUCT, java.sql.Struct.class);
        MAP_JDBC_2_JAVA.put(java.sql.Types.DATALINK, java.net.URL.class);
    }
    
    /**
     * isBasicType
     *
     * @param clazz
     * @return true/false
     */
    public boolean isBasicType(
            Class<?> clazz) {
        if (MAP_JAVA_2_DB.containsKey(clazz) || clazz.isPrimitive()) {
            return true;
        }
        return false;
    }

    /**
     * mapDB2JDBC
     * 
     * @param dbType
     * @return
     */
    public int mapDB2JDBC(
            String type) {
        return MAP_DB_2_JDBC.containsKey(type) ? MAP_DB_2_JDBC.get(type) : 0;
    }

    /**
     * mapJava2DB
     * 
     * @param dbType
     * @return
     */
    public String mapJava2DB(
            Class<?> type) {
        return MAP_JAVA_2_DB.containsKey(type) ? MAP_JAVA_2_DB.get(type) : null;
    }

    /**
     * mapJava2DB
     * 
     * @param dbType
     * @return
     */
    public Class<?> mapJDBC2Java(
            Integer type) {
        return MAP_JDBC_2_JAVA.containsKey(type) ? MAP_JDBC_2_JAVA.get(type) : null;
    }

}
