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
	 * database type
	 */
	public static DatabaseType databaseType = DatabaseType.MySQL;
	/**
	 * Default type mapping for Java
	 */
	public static final Map<String, String> dataTypeMap2Java = new HashMap<String, String>();
	static {
		dataTypeMap2Java.put("文本", "String");
		dataTypeMap2Java.put("定长文本", "String");
		dataTypeMap2Java.put("长文本", "String");
		dataTypeMap2Java.put("整数", "Integer");
		dataTypeMap2Java.put("长整数", "Long");
		dataTypeMap2Java.put("短整数", "Short");
		dataTypeMap2Java.put("数值", "Double");
		dataTypeMap2Java.put("浮点数值", "Float");
		dataTypeMap2Java.put("日期", "java.sql.Date");
		dataTypeMap2Java.put("时间", "java.sql.Time");
		dataTypeMap2Java.put("时间戳", "java.sql.Timestamp");
        dataTypeMap2Java.put("日期时间", "java.sql.Timestamp");
		dataTypeMap2Java.put("大整数", "java.math.BigInteger");
		dataTypeMap2Java.put("大数值", "java.math.BigDecimal");
		dataTypeMap2Java.put("二进制对象", "java.sql.Blob");
		dataTypeMap2Java.put("字符大对象", "java.sql.Clob");
		dataTypeMap2Java.put("自增序列", "Long");
		dataTypeMap2Java.put("图片", "java.sql.Blob");

		// Non Chinese Description
		dataTypeMap2Java.put("文字", "String");
		dataTypeMap2Java.put("長文字", "String");
		dataTypeMap2Java.put("整数", "Integer");
		dataTypeMap2Java.put("数値", "Double");
		dataTypeMap2Java.put("日付", "java.sql.Date");
		dataTypeMap2Java.put("時間", "java.sql.Time");
		dataTypeMap2Java.put("時刻(TIMESTAMP)", "java.sql.Timestamp");
		dataTypeMap2Java.put("採番", "Long");
		dataTypeMap2Java.put("動画", "java.sql.Blob");
		dataTypeMap2Java.put("画像", "java.sql.Blob");
		dataTypeMap2Java.put("TEXT", "String");
		dataTypeMap2Java.put("LONG TEXT", "String");
        dataTypeMap2Java.put("LONGTEXT", "String");
		dataTypeMap2Java.put("FIT LENGTH TEXT", "String");
		dataTypeMap2Java.put("INT", "Integer");
		dataTypeMap2Java.put("LONG", "Long");
		dataTypeMap2Java.put("SHORT", "Short");
		dataTypeMap2Java.put("DOUBLE", "Double");
		dataTypeMap2Java.put("FLOAT", "Float");
		dataTypeMap2Java.put("DATE", "java.sql.Date");
		dataTypeMap2Java.put("TIME", "java.sql.Time");
		dataTypeMap2Java.put("TIMESTAMP", "java.sql.Timestamp");
		dataTypeMap2Java.put("SEQUENCE", "Long");
		dataTypeMap2Java.put("BIG INT", "java.math.BigInteger");
		dataTypeMap2Java.put("BIG DECIMAL", "java.math.BigDecimal");
		dataTypeMap2Java.put("BLOB", "java.sql.Blob");
		dataTypeMap2Java.put("CLOB", "java.sql.Clob");
		dataTypeMap2Java.put("PHOTO", "java.sql.Blob");
		dataTypeMap2Java.put("PICTURE", "java.sql.Blob");

	}
	/**
	 * Default type mapping for DB
	 */
	private static final Map<String, String> dataTypeMap2DB = new HashMap<String, String>();
	static {
		dataTypeMap2DB.put("文本", "VARCHAR");
		dataTypeMap2DB.put("定长文本", "CHAR");
		dataTypeMap2DB.put("长文本", "LONGTEXT");
		dataTypeMap2DB.put("整数", "INT");
		dataTypeMap2DB.put("长整数", "INT");
		dataTypeMap2DB.put("短整数", "INT");
		dataTypeMap2DB.put("数值", "DECIMAL");
		dataTypeMap2DB.put("浮点数值", "DECIMAL");
		dataTypeMap2DB.put("日期", "DATE");
		dataTypeMap2DB.put("时间", "TIME");
		dataTypeMap2DB.put("时间戳", "TIMESTAMP");
        dataTypeMap2DB.put("日期时间", "DATETIME");
		dataTypeMap2DB.put("大整数", "BIGINT");
		dataTypeMap2DB.put("大数值", "DECIMAL");
		dataTypeMap2DB.put("二进制对象", "BLOB");
		dataTypeMap2DB.put("字符大对象", "TEXT");
		dataTypeMap2DB.put("自增序列", "AUTO-INCREMENT");
		dataTypeMap2DB.put("图片", "MEDIUMBLOB");

		// Non Chinese Description
		dataTypeMap2DB.put("文字", "VARCHAR");
		dataTypeMap2DB.put("長文字", "LONGTEXT");
		dataTypeMap2DB.put("日付", "DATE");
		dataTypeMap2DB.put("時間", "TIME");
		dataTypeMap2DB.put("時刻(TIMESTAMP)", "TIMESTAMP");
		dataTypeMap2DB.put("採番", "AUTO-INCREMENT");
		dataTypeMap2DB.put("動画", "BLOB");
		dataTypeMap2DB.put("画像", "BLOB");
		dataTypeMap2DB.put("TEXT", "VARCHAR");
		//dataTypeMap2DB.put("LONG TEXT", "TEXT");
        dataTypeMap2DB.put("LONGTEXT", "LONGTEXT");
		dataTypeMap2DB.put("FIT LENGTH TEXT", "CHAR");
		dataTypeMap2DB.put("INT", "INT");
		dataTypeMap2DB.put("LONG", "INT");
		dataTypeMap2DB.put("SHORT", "INT");
		dataTypeMap2DB.put("DOUBLE", "DECIMAL");
		dataTypeMap2DB.put("FLOAT", "DECIMAL");
		dataTypeMap2DB.put("DATE", "DATE");
		dataTypeMap2DB.put("TIME", "TIME");
		dataTypeMap2DB.put("TIMESTAMP", "TIMESTAMP");
        dataTypeMap2DB.put("DATETIME", "DATETIME");
		dataTypeMap2DB.put("SEQUENCE", "AUTO-INCREMENT");
		dataTypeMap2DB.put("BIG INT", "BIGINT");
		dataTypeMap2DB.put("BIG DECIMAL", "DECIMAL");
		dataTypeMap2DB.put("BLOB", "BLOB");
		dataTypeMap2DB.put("CLOB", "CLOB");
		dataTypeMap2DB.put("PHOTO", "BLOB");
		dataTypeMap2DB.put("PICTURE", "BLOB");
	}
	/**
	 * Database type mapping to text description
	 */
	private static final Map<String, String> dbTypeMap2Text = new HashMap<String, String>();
	static {
		dbTypeMap2Text.put("VARCHAR", "文本");
		dbTypeMap2Text.put("VARCHAR2", "文本");
		dbTypeMap2Text.put("INT", "长整数");
		dbTypeMap2Text.put("INTEGER", "长整数");
		dbTypeMap2Text.put("DECIMAL", "数值");
		dbTypeMap2Text.put("NUMBER", "数值");
		dbTypeMap2Text.put("DATE", "日期");
		dbTypeMap2Text.put("TIME", "时间");
		dbTypeMap2Text.put("TIMESTAMP", "时间戳");
        dbTypeMap2Text.put("DATETIME", "日期时间");
		dbTypeMap2Text.put("BIGINT", "大整数");
		dbTypeMap2Text.put("CHAR", "定长文本");
		dbTypeMap2Text.put("LONGTEXT", "长文本");
        //dbTypeMap2Text.put("TEXT", "长文本");
		dbTypeMap2Text.put("BLOB", "二进制对象");
		dbTypeMap2Text.put("CLOB", "字符大对象");
		dbTypeMap2Text.put("TEXT", "字符大对象");
		dbTypeMap2Text.put("AUTO-INCREMENT", "自增序列");
		dbTypeMap2Text.put("MEDIUMBLOB", "图片");

	}

	/**
	 * Database enum
	 */
	public static enum DatabaseType {
		MySQL, Oracle, DB2, H2
	}

	/**
	 * Switch database type for genarate DDL and SQL Map
	 * 
	 * @param dbType
	 */
	public static void setDatabaseType(DataTypeUtil.DatabaseType databaseType) {
		DataTypeUtil.databaseType = databaseType;
		switch (databaseType) {
		case Oracle: {
			// Database type mapping
			dataTypeMap2DB.put("文本", "VARCHAR2");
			dataTypeMap2DB.put("长文本", "VARCHAR2");
			dataTypeMap2DB.put("整数", "NUMBER");
			dataTypeMap2DB.put("长整数", "NUMBER");
			dataTypeMap2DB.put("短整数", "NUMBER");
			dataTypeMap2DB.put("大整数", "NUMBER");
			dataTypeMap2DB.put("数值", "NUMBER");
			dataTypeMap2DB.put("时间", "DATE");
			dataTypeMap2DB.put("字符大对象", "CLOB");
			dataTypeMap2DB.put("图片", "BLOB");
			// Java type mapping
			dataTypeMap2Java.put("二进制对象", "byte[]");
			dataTypeMap2Java.put("字符大对象", "char[]");
			break;
		}
		case DB2: {
			dataTypeMap2DB.put("字符大对象", "CLOB");
			dataTypeMap2DB.put("图片", "BLOB");
			break;

		}
		default: {
			break;
		}
		}
	}

	/**
	 * checkDataType
	 *
	 * @param type
	 * @return
	 */
	public static boolean checkDataType(String type) {
		type = type.trim();
		if (dataTypeMap2Java.containsKey(type)) {
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
	public static String getJavaType(String type, FieldDescBean refField) {
		type = type.trim();
		if (dataTypeMap2Java.containsKey(type)) {
			return dataTypeMap2Java.get(type);
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
	public static String getDBDataType(String type, FieldDescBean refField) {
		type = type.trim();
		if (dataTypeMap2DB.containsKey(type)) {
			return dataTypeMap2DB.get(type);
		}
		throw new RuntimeException("Found Unknow Data Type: [" + type + "] ," + refField.getColId() + ", " + refField.getColName());
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
		if ("YES".equalsIgnoreCase(field.getColPK().trim())
		        || "Y".equalsIgnoreCase(field.getColPK().trim())
		        || "○".equalsIgnoreCase(field.getColPK().trim())) {
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
