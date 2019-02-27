/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

import java.util.HashMap;
import java.util.Map;

/**
 * DataTypeUtil
 */
public class DataTypeUtil {

    /**
     * Default type mapping for Java
     */
    public static final Map<String, String> dataTypeMap2Java_Default = new HashMap<String, String>();
    static {
        dataTypeMap2Java_Default.put("文本", "String");
        dataTypeMap2Java_Default.put("定长文本", "String");
        dataTypeMap2Java_Default.put("字符", "Character");
        dataTypeMap2Java_Default.put("长文本", "String");
        dataTypeMap2Java_Default.put("整数", "Integer");
        dataTypeMap2Java_Default.put("长整数", "Long");
        dataTypeMap2Java_Default.put("短整数", "Short");
        dataTypeMap2Java_Default.put("数值", "Double");
        dataTypeMap2Java_Default.put("浮点数值", "Float");
        dataTypeMap2Java_Default.put("日期", "java.sql.Date");
        dataTypeMap2Java_Default.put("时间", "java.sql.Time");
        dataTypeMap2Java_Default.put("时间戳", "java.sql.Timestamp");
        dataTypeMap2Java_Default.put("日期时间", "java.sql.Timestamp");
        dataTypeMap2Java_Default.put("大整数", "java.math.BigInteger");
        dataTypeMap2Java_Default.put("大数值", "java.math.BigDecimal");
        dataTypeMap2Java_Default.put("二进制对象", "java.sql.Blob");
        dataTypeMap2Java_Default.put("字符大对象", "java.sql.Clob");
        dataTypeMap2Java_Default.put("自增序列", "Long");
        dataTypeMap2Java_Default.put("图片", "java.sql.Blob");

        // REST
        dataTypeMap2Java_Default.put("TEXT", "String");
        dataTypeMap2Java_Default.put("LONG TEXT", "String");
        dataTypeMap2Java_Default.put("LONGTEXT", "String");
        dataTypeMap2Java_Default.put("NVARCHAR2", "String");
        dataTypeMap2Java_Default.put("FIT LENGTH TEXT", "String");
        dataTypeMap2Java_Default.put("INT", "Integer");
        dataTypeMap2Java_Default.put("LONG", "Long");
        dataTypeMap2Java_Default.put("SHORT", "Short");
        dataTypeMap2Java_Default.put("DOUBLE", "Double");
        dataTypeMap2Java_Default.put("FLOAT", "Float");
        dataTypeMap2Java_Default.put("DATE", "java.sql.Date");
        dataTypeMap2Java_Default.put("TIME", "java.sql.Time");
        dataTypeMap2Java_Default.put("TIMESTAMP", "java.sql.Timestamp");
        dataTypeMap2Java_Default.put("TIMESTAMP(6)", "java.sql.Timestamp");
        dataTypeMap2Java_Default.put("SEQUENCE", "Long");
        dataTypeMap2Java_Default.put("BIG INT", "java.math.BigInteger");
        dataTypeMap2Java_Default.put("BIG DECIMAL", "java.math.BigDecimal");
        dataTypeMap2Java_Default.put("PHOTO", "java.sql.Blob");
        dataTypeMap2Java_Default.put("PICTURE", "java.sql.Blob");
        dataTypeMap2Java_Default.put("BLOB", "java.sql.Blob");
        dataTypeMap2Java_Default.put("CLOB", "java.sql.Clob");
        // Oracle
        // dataTypeMap2Java.put("二进制对象", "byte[]");
        // dataTypeMap2Java.put("字符大对象", "char[]");
    }
    /**
     * Default type mapping for DB
     */
    private static final Map<String, String> dataTypeMap2DB_Default = new HashMap<String, String>();
    static {
        dataTypeMap2DB_Default.put("文本", "VARCHAR");
        dataTypeMap2DB_Default.put("定长文本", "CHAR");
        dataTypeMap2DB_Default.put("长文本", "LONGTEXT");
        dataTypeMap2DB_Default.put("整数", "INT");
        dataTypeMap2DB_Default.put("长整数", "INT");
        dataTypeMap2DB_Default.put("短整数", "INT");
        dataTypeMap2DB_Default.put("数值", "DECIMAL");
        dataTypeMap2DB_Default.put("浮点数值", "DECIMAL");
        dataTypeMap2DB_Default.put("日期", "DATE");
        dataTypeMap2DB_Default.put("时间", "TIME");
        dataTypeMap2DB_Default.put("时间戳", "TIMESTAMP");
        dataTypeMap2DB_Default.put("日期时间", "DATETIME");
        dataTypeMap2DB_Default.put("大整数", "BIGINT");
        dataTypeMap2DB_Default.put("大数值", "DECIMAL");
        dataTypeMap2DB_Default.put("二进制对象", "BLOB");
        dataTypeMap2DB_Default.put("字符大对象", "TEXT");
        dataTypeMap2DB_Default.put("自增序列", "AUTO-INCREMENT");
        dataTypeMap2DB_Default.put("图片", "MEDIUMBLOB");
        dataTypeMap2DB_Default.put("TEXT", "VARCHAR");
        dataTypeMap2DB_Default.put("LONGTEXT", "LONGTEXT");
        dataTypeMap2DB_Default.put("FIT LENGTH TEXT", "CHAR");
        dataTypeMap2DB_Default.put("INT", "INT");
        dataTypeMap2DB_Default.put("LONG", "INT");
        dataTypeMap2DB_Default.put("SHORT", "INT");
        dataTypeMap2DB_Default.put("DOUBLE", "DECIMAL");
        dataTypeMap2DB_Default.put("FLOAT", "DECIMAL");
        dataTypeMap2DB_Default.put("DATE", "DATE");
        dataTypeMap2DB_Default.put("TIME", "TIME");
        dataTypeMap2DB_Default.put("TIMESTAMP", "TIMESTAMP");
        dataTypeMap2DB_Default.put("DATETIME", "DATETIME");
        dataTypeMap2DB_Default.put("SEQUENCE", "AUTO-INCREMENT");
        dataTypeMap2DB_Default.put("BIG INT", "BIGINT");
        dataTypeMap2DB_Default.put("BIG DECIMAL", "DECIMAL");
        dataTypeMap2DB_Default.put("BLOB", "BLOB");
        dataTypeMap2DB_Default.put("CLOB", "CLOB");
        dataTypeMap2DB_Default.put("PHOTO", "BLOB");
        dataTypeMap2DB_Default.put("PICTURE", "BLOB");
    }

    private static final Map<String, String> dataTypeMap2DB_Oracle = new HashMap<String, String>();
    static {
        dataTypeMap2DB_Oracle.put("文本", "VARCHAR2");
        dataTypeMap2DB_Oracle.put("长文本", "VARCHAR2");
        dataTypeMap2DB_Oracle.put("整数", "NUMBER");
        dataTypeMap2DB_Oracle.put("长整数", "NUMBER");
        dataTypeMap2DB_Oracle.put("短整数", "NUMBER");
        dataTypeMap2DB_Oracle.put("大整数", "NUMBER");
        dataTypeMap2DB_Oracle.put("数值", "NUMBER");
        dataTypeMap2DB_Oracle.put("字符大对象", "CLOB");
        dataTypeMap2DB_Oracle.put("二进制对象", "BLOB");
        dataTypeMap2DB_Oracle.put("图片", "BLOB");
        dataTypeMap2DB_Oracle.put("定长文本", "CHAR");
        dataTypeMap2DB_Oracle.put("日期", "DATE");
        dataTypeMap2DB_Oracle.put("时间", "DATE");
        dataTypeMap2DB_Oracle.put("时间戳", "TIMESTAMP");
        dataTypeMap2DB_Oracle.put("NVARCHAR2", "NVARCHAR2");
        dataTypeMap2DB_Oracle.put("VARCHAR2", "VARCHAR2");
        dataTypeMap2DB_Oracle.put("VARCHAR", "VARCHAR");
        dataTypeMap2DB_Oracle.put("CHAR", "CHAR");
        dataTypeMap2DB_Oracle.put("NUMBER", "NUMBER");
        dataTypeMap2DB_Oracle.put("TIMESTAMP(6)", "TIMESTAMP(6)");
        dataTypeMap2DB_Oracle.put("BLOB", "BLOB");
        dataTypeMap2DB_Oracle.put("CLOB", "CLOB");
    }

    /**
     * Default type mapping for DB
     */
    private static final Map<String, String> dataTypeMap2JDBC_Oracle = new HashMap<String, String>();
    static {
        dataTypeMap2DB_Oracle.put("文本", "VARCHAR");
        dataTypeMap2DB_Oracle.put("长文本", "LONGVARCHAR");
        dataTypeMap2DB_Oracle.put("整数", "INTEGER");
        dataTypeMap2DB_Oracle.put("长整数", "BIGINT");
        dataTypeMap2DB_Oracle.put("短整数", "SMALLINT");
        dataTypeMap2DB_Oracle.put("大整数", "BIGINT");
        dataTypeMap2DB_Oracle.put("数值", "DECIMAL");
        dataTypeMap2DB_Oracle.put("字符大对象", "CLOB");
        dataTypeMap2DB_Oracle.put("二进制对象", "BLOB");
        dataTypeMap2DB_Oracle.put("图片", "BLOB");
        dataTypeMap2DB_Oracle.put("定长文本", "CHAR");
        dataTypeMap2DB_Oracle.put("日期", "DATE");
        dataTypeMap2DB_Oracle.put("时间", "TIME");
        dataTypeMap2DB_Oracle.put("时间戳", "TIMESTAMP");
        dataTypeMap2DB_Oracle.put("NVARCHAR2", "VARCHAR");
        dataTypeMap2DB_Oracle.put("VARCHAR2", "VARCHAR");
        dataTypeMap2DB_Oracle.put("VARCHAR", "VARCHAR");
        dataTypeMap2DB_Oracle.put("CHAR", "CHAR");
        dataTypeMap2DB_Oracle.put("NUMBER", "DECIMAL");
        dataTypeMap2DB_Oracle.put("TIMESTAMP(6)", "TIMESTAMP");
        dataTypeMap2DB_Oracle.put("BLOB", "BLOB");
        dataTypeMap2DB_Oracle.put("CLOB", "CLOB");
    }
    
    /**
     * Database type mapping to text description
     */
    private static final Map<String, String> dbTypeMap2Text = new HashMap<String, String>();
    static {
        dbTypeMap2Text.put("CHAR", "定长文本");
        dbTypeMap2Text.put("VARCHAR", "文本");
        dbTypeMap2Text.put("VARCHAR2", "文本");
        dbTypeMap2Text.put("NVARCHAR2", "NVARCHAR2");
        dbTypeMap2Text.put("TIMESTAMP(6)", "TIMESTAMP(6)");
        dbTypeMap2Text.put("INT", "整数");
        dbTypeMap2Text.put("INTEGER", "整数");
        dbTypeMap2Text.put("DECIMAL", "数值");
        dbTypeMap2Text.put("NUMBER", "数值");
        dbTypeMap2Text.put("DATE", "日期");
        dbTypeMap2Text.put("TIME", "时间");
        dbTypeMap2Text.put("TIMESTAMP", "时间戳");
        dbTypeMap2Text.put("DATETIME", "日期时间");
        dbTypeMap2Text.put("BIGINT", "大整数");
        dbTypeMap2Text.put("LONGTEXT", "长文本");
        dbTypeMap2Text.put("BLOB", "BLOB");
        dbTypeMap2Text.put("CLOB", "CLOB");
        dbTypeMap2Text.put("TEXT", "TEXT");
        dbTypeMap2Text.put("AUTO-INCREMENT", "自增序列");
        dbTypeMap2Text.put("MEDIUMBLOB", "MEDIUMBLOB");
    }

    /**
     * checkDataType
     *
     * @param type
     * @return
     */
    public static boolean checkDataType(String type) {
        type = type.trim();
        if (dataTypeMap2Java_Default.containsKey(type)) {
            return true;
        }
        return false;
    }

    /**
     * getJavaType
     * 
     * @param type
     * @param refField
     * @return
     */
    public static String getJavaType(String type, FieldDescBean refField, String databaseCategory) {
        type = type.trim();
        if (dataTypeMap2Java_Default.containsKey(type)) {
            return dataTypeMap2Java_Default.get(type);
        }
        throw new RuntimeException("Found Unknow Data Type: [" + type + "] ," + refField.getColId() + ", " + refField.getColName());
    }

    /**
     * getDBDataType
     *
     * @param type
     * @param refField
     * @return
     */
    public static String getDBDataType(String type, FieldDescBean refField, String databaseCategory) {
        type = type.trim();
        String result = null;
        if (databaseCategory.toLowerCase().equals("oracle")) {
            result = dataTypeMap2DB_Oracle.get(type);
        } else {
            result = dataTypeMap2DB_Default.get(type);
        }
        if (result == null) {
            throw new RuntimeException("Found Unknow Data Type: [" + type + "] ," + refField.getColId() + ", " + refField.getColName());
        }
        return result;
    }

    /**
     * getDBDataType
     *
     * @param type
     * @param refField
     * @return
     */
    public static String getJdbcType(String type, FieldDescBean refField, String databaseCategory) {
        type = type.trim();
        String result = null;
        if (databaseCategory.toLowerCase().equals("oracle")) {
            result = dataTypeMap2JDBC_Oracle.get(type);
        } else {
            throw new RuntimeException("Found matched JDBC Data Type: [" + type + "] ," + refField.getColId() + ", " + refField.getColName());
        }
        return result;
    }
    
    /**
     * getTextDescFromType
     * 
     * @param type
     * @param refField
     * @return
     */
    public static String getTextDescFromType(String type) {
        type = type.toUpperCase().trim();
        if (dbTypeMap2Text.containsKey(type)) {
            return dbTypeMap2Text.get(type);
        }
        return type;
    }

    /**
     * isNotEmpty
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !str.trim().equals("") && !str.trim().toLowerCase().equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * isStrType
     *
     * @param colType
     * @return
     */
    public static boolean isStrType(String colType) {
        colType = colType.trim().toUpperCase();
        if (colType.startsWith("CHAR") || colType.startsWith("VARCHAR")) {
            return true;
        }
        return false;
    }

    /**
     * isPK
     *
     * @param field
     * @return
     */
    public static boolean isPK(FieldDescBean field) {
        if (field.getColPK() == null) {
            return false;
        }
        if ("YES".equalsIgnoreCase(field.getColPK().trim()) || "Y".equalsIgnoreCase(field.getColPK().trim()) || "○".equalsIgnoreCase(field.getColPK().trim())) {
            return true;
        }
        return false;
    }

    /**
     * dbName2JavaName
     *
     * @param name
     * @return
     */
    public static String dbName2JavaName(String name) {
        if (!name.contains("_")) {
            return name.toLowerCase();
        }
        String[] nameSplit = name.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nameSplit.length; i++) {
            String word = nameSplit[i].toLowerCase();
            if (i == 0) {
                builder.append(word);
            } else {
                if (!"".equals(word)) {
                    builder.append(upperCaseFirstChar(word));
                }

            }
        }
        return builder.toString();
    }

    /**
     * dbName2JavaName4GetterSetter
     *
     * @param name
     * @return
     */
    public static String dbName2JavaGetterSetterName(String name) {
        if (!name.contains("_")) {
            return upperCaseFirstChar(name.toLowerCase());
        }
        String[] nameSplit = name.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nameSplit.length; i++) {
            String word = nameSplit[i].toLowerCase();
            if (i == 0 && word.length() == 1) {
                builder.append(word);
            } else {
                if (!"".equals(word)) {
                    builder.append(upperCaseFirstChar(word));
                }

            }
        }
        return builder.toString();
    }

    /**
     * capFirstChar
     *
     * @param name
     * @return
     */
    public static String upperCaseFirstChar(String name) {
        return name.length() > 1 ? String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1) : String.valueOf(name.charAt(0)).toUpperCase();
    }

    /**
     * lowFirstChar
     *
     * @param name
     * @return
     */
    public static String lowFirstChar(String name) {
        return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
    }
}
