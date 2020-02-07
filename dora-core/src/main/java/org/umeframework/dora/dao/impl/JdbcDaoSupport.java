///*
// * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
// */
//package org.umeframework.dora.dao.impl;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
////import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import org.springframework.jdbc.datasource.DataSourceUtils;
//import org.umeframework.dora.exception.DataAccessException;
//import org.umeframework.dora.property.ConfigProperties;
//import org.umeframework.dora.type.Blob;
//import org.umeframework.dora.type.Clob;
//
///**
// * JdbcDaoSupport
// *
// * @author Yue MA
// */
//public class JdbcDaoSupport extends JdbcTypeMapping {
//    /**
//     * Dynamic parameter identify for JDBC Sample: INSERT INTO USERTEST(ID,NAME,AGE,BIRTH)VALUES(?, ?, ?, ?)
//     */
//    private static final char JDBC_PARAM_FLAG = '?';
//    /**
//     * Data source
//     */
//    private DataSource dataSource;
//    /**
//     * Properties to define SQL statement
//     */
//    private ConfigProperties sqlMapProperties;
//
//    /**
//     * Get connection instance.<br>
//     *
//     * @return
//     */
//    protected Connection getConnection() {
//        Connection conn = null;
//        try {
//            // must use spring DataSourceUtils get connection while transaction managed by spring
//            conn = DataSourceUtils.doGetConnection(dataSource);
//        } catch (Exception e) {
//            throw new DataAccessException(e, "Fail to get connection for JDBC Dao ");
//        }
//        return conn;
//    }
//
//    /**
//     * Close Statement
//     *
//     * @param statement
//     */
//    protected void closeStatement(
//            Statement statement,
//            Connection connection) {
//        try {
//            if (statement != null) {
//                statement.close();
//            }
//            if (connection != null) {
//                // must use spring DataSourceUtils release connection while transaction managed by spring
//                DataSourceUtils.doReleaseConnection(connection, dataSource);
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e, "Failed to close JDBC resource.");
//        }
//    }
//
//    /**
//     * createStatement
//     *
//     * @param connection
//     * @param sqlIdOrText
//     * @param paramObject
//     * @return
//     * @throws SQLException
//     */
//    protected PreparedStatement createStatement(
//            Connection connection,
//            String sqlIdOrText,
//            Object paramObject) throws SQLException {
//        if (sqlMapProperties != null && sqlMapProperties.containsKey(sqlIdOrText)) {
//            sqlIdOrText = sqlMapProperties.get(sqlIdOrText);
//        }
//
//        List<String> paramNameList = null;
//        String sqlText = null;
//
//        // if (!SQLConfigUtil.isExistId(sqlIdOrText)) {
//        // parse sqlId as SQL text while configuration file does not exist current SQL
//        SqlMapItem sqlCfgItem = parseSqlInputText(sqlIdOrText);
//        sqlText = sqlCfgItem.getSqlJdbcText();
//        paramNameList = sqlCfgItem.getSqlParams();
//        // } else {
//        // // get SQL text from configuration file
//        // sqlText = SQLConfigUtil.getSqlJdbcText(sqlIdOrText);
//        // paramNameList = SQLConfigUtil.getSqlParams(sqlIdOrText);
//        // }
//
//        Map<String, Object> valueMap = new HashMap<String, Object>();
//        Map<String, String> typeMap = new HashMap<String, String>();
//
//        if (paramObject != null) {
//            if (isBasicType(paramObject.getClass())) {
//                valueMap.put(paramNameList.get(0), paramObject);
//                typeMap.put(paramNameList.get(0), mapJava2DB(paramObject == null ? null : paramObject.getClass()));
//            } else {
//                getPropertiesAndValues(paramObject, valueMap, typeMap);
//            }
//        }
//
//        PreparedStatement statement = null;
//        if (valueMap.size() == 0) {
//            // parameter not exist
//            statement = connection.prepareStatement(sqlText);
//            return statement;
//        }
//
//        // create statement
//        statement = connection.prepareStatement(sqlText);
//        // set parameter to statement
//        for (int i = 0; i < paramNameList.size(); i++) {
//            String paramName = paramNameList.get(i);
//            Object val = valueMap.get(paramName);
//            String dbType = typeMap.get(paramName);
//            setStatement(i + 1, statement, val, dbType);
//        }
//        return statement;
//    }
//
//    /**
//     * Set Parameter with JDBC Statement
//     *
//     * @param index
//     * @param statement
//     * @param value
//     * @throws SQLException
//     */
//    protected void setStatement(
//            int index,
//            PreparedStatement statement,
//            Object value,
//            String type) throws SQLException {
//        if (value == null) {
//            // null check
//            // statement.setNull(index, mapDB2JDBC(type));
//            statement.setObject(index, null);
//            return;
//        }
//
//        Class<?> clazz = value.getClass();
//        if (String.class.equals(clazz)) {
//            statement.setString(index, (String) value);
//        } else if (char.class.equals(clazz) || Character.class.equals(clazz)) {
//            statement.setString(index, String.valueOf(value));
//        } else if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
//            statement.setString(index, String.valueOf(value));
//        } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
//            statement.setInt(index, (Integer) value);
//        } else if (short.class.equals(clazz) || Short.class.equals(clazz)) {
//            statement.setShort(index, (Short) value);
//        } else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
//            statement.setLong(index, (Long) value);
//        } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
//            statement.setDouble(index, (Double) value);
//        } else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
//            statement.setDouble(index, (Float) value);
//        } else if (java.math.BigInteger.class.equals(clazz)) {
//            statement.setBigDecimal(index, new java.math.BigDecimal((java.math.BigInteger) value));
//        } else if (java.math.BigDecimal.class.equals(clazz)) {
//            statement.setBigDecimal(index, (java.math.BigDecimal) value);
//        } else if (java.util.Date.class.equals(clazz)) {
//            java.sql.Timestamp timestampValue = new java.sql.Timestamp(((java.util.Date) value).getTime());
//            statement.setTimestamp(index, timestampValue);
//        } else if (java.sql.Date.class.equals(clazz)) {
//            statement.setDate(index, (java.sql.Date) value);
//        } else if (java.sql.Timestamp.class.equals(clazz)) {
//            statement.setTimestamp(index, (java.sql.Timestamp) value);
//        } else if (java.sql.Time.class.equals(clazz)) {
//            statement.setTime(index, (java.sql.Time) value);
//        } else if (org.umeframework.dora.type.Blob.class.equals(clazz) || java.sql.Blob.class.equals(clazz)) {
//            setBlobStatement(statement, index, (org.umeframework.dora.type.Blob) value);
//        } else if (org.umeframework.dora.type.Clob.class.equals(clazz) || java.sql.Clob.class.equals(clazz)) {
//            setClobStatement(statement, index, (org.umeframework.dora.type.Clob) value);
//        } else if (byte[].class.equals(clazz)) {
//            statement.setBytes(index, (byte[]) value);
//        } else if (Byte[].class.equals(clazz)) {
//            statement.setBytes(index, (byte[]) value);
//        } else {
//            throw new DataAccessException("The type[" + clazz + "] is not support in current JdbcDaoSupport scope.");
//        }
//    }
//
//    /**
//     * Get Value from JDBC ResultSet
//     *
//     * @param resultset
//     * @param columnIndex
//     * @param colType
//     * @return
//     * @throws SQLException
//     */
//    protected Object getResultset(
//            ResultSet resultset,
//            int columnIndex,
//            int colType) throws SQLException {
//        Object value = null;
//        switch (colType) {
//        case java.sql.Types.CHAR:
//        case java.sql.Types.VARCHAR:
//        case java.sql.Types.LONGVARCHAR:
//        case java.sql.Types.LONGNVARCHAR: {
//            value = resultset.getString(columnIndex);
//            break;
//        }
//        case java.sql.Types.DATE: {
//            value = resultset.getDate(columnIndex);
//            break;
//        }
//        case java.sql.Types.TIMESTAMP: {
//            value = resultset.getTimestamp(columnIndex);
//            break;
//        }
//        case java.sql.Types.TIME: {
//            value = resultset.getTime(columnIndex);
//            break;
//        }
//        case java.sql.Types.DECIMAL:
//        case java.sql.Types.NUMERIC: {
//            value = resultset.getBigDecimal(columnIndex);
//            break;
//        }
//        case java.sql.Types.SMALLINT: {
//            value = resultset.getShort(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.TINYINT: {
//            value = resultset.getByte(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.INTEGER: {
//            value = resultset.getInt(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.BIGINT: {
//            BigDecimal decimalValue = resultset.getBigDecimal(columnIndex);
//            value = decimalValue != null ? decimalValue.toBigInteger() : null;
//            break;
//        }
//        case java.sql.Types.FLOAT: {
//            value = resultset.getFloat(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.DOUBLE: {
//            value = resultset.getDouble(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.REAL: {
//            value = resultset.getFloat(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.BOOLEAN:
//        case java.sql.Types.BIT: {
//            value = resultset.getBoolean(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.LONGVARBINARY:
//        case java.sql.Types.VARBINARY:
//        case java.sql.Types.BINARY: {
//            value = resultset.getBytes(columnIndex);
//            value = resultset.wasNull() ? null : value;
//            break;
//        }
//        case java.sql.Types.BLOB: {
//            value = getBlobResult(resultset, columnIndex);
//            break;
//        }
//        case java.sql.Types.CLOB: {
//            value = getClobResult(resultset, columnIndex);
//            break;
//        }
//        default: {
//            value = resultset.getString(columnIndex);
//            break;
//        }
//        }
//        return value;
//    }
//
//    /**
//     * Get DB Data Type of JDBC Type
//     *
//     * @param jdbcType
//     * @return
//     */
//    protected String getDataType(
//            int jdbcType) {
//        String type = null;
//        switch (jdbcType) {
//        case java.sql.Types.CHAR: {
//            type = "CHAR";
//            break;
//        }
//        case java.sql.Types.VARCHAR: {
//            type = "VARCHAR";
//            break;
//        }
//        case java.sql.Types.LONGNVARCHAR: {
//            type = "LONGNVARCHAR";
//            break;
//        }
//        case java.sql.Types.DATE: {
//            type = "DATE";
//            break;
//        }
//        case java.sql.Types.TIMESTAMP: {
//            type = "TIMESTAMP";
//            break;
//        }
//        case java.sql.Types.TIME: {
//            type = "TIME";
//            break;
//        }
//        case java.sql.Types.DECIMAL: {
//            type = "DECIMAL";
//            break;
//        }
//        case java.sql.Types.NUMERIC: {
//            type = "NUMERIC";
//            break;
//        }
//        case java.sql.Types.SMALLINT: {
//            type = "SMALLINT";
//            break;
//        }
//        case java.sql.Types.TINYINT: {
//            type = "TINYINT";
//            break;
//        }
//        case java.sql.Types.INTEGER: {
//            type = "INTEGER";
//            break;
//        }
//        case java.sql.Types.BIGINT: {
//            type = "BIGINT";
//            break;
//        }
//        case java.sql.Types.FLOAT: {
//            type = "FLOAT";
//            break;
//        }
//        case java.sql.Types.DOUBLE: {
//            type = "DOUBLE";
//            break;
//        }
//        case java.sql.Types.REAL: {
//            type = "REAL";
//            break;
//        }
//        case java.sql.Types.BIT: {
//            type = "BIT";
//            break;
//        }
//        case java.sql.Types.BLOB: {
//            type = "BLOB";
//            break;
//        }
//        case java.sql.Types.CLOB: {
//            type = "CLOB";
//            break;
//        }
//        }
//        return type;
//    }
//
//    /**
//     * Convert Java Bean to Map
//     *
//     * @param paramObject
//     * @return
//     */
//    protected void getPropertiesAndValues(
//            Object paramObject,
//            Map<String, Object> valueMap,
//            Map<String, String> typeMap) {
//
//        if (paramObject instanceof Map) {
//            @SuppressWarnings("unchecked")
//            Map<String, Object> mapObject = (Map<String, Object>) paramObject;
//            for (Map.Entry<String, Object> entry : mapObject.entrySet()) {
//                String propName = entry.getKey();
//                Object propValue = entry.getValue();
//                String propDBType = mapJava2DB(propValue == null ? null : propValue.getClass());
//                valueMap.put(propName, propValue);
//                typeMap.put(propName, propDBType);
//            }
//        } else {
//            for (Method method : paramObject.getClass().getDeclaredMethods()) {
//                String methodName = method.getName();
//                if (!methodName.startsWith("get")) {
//                    continue;
//                }
//                String propName = methodName.substring(3);
//                propName = String.valueOf(propName.charAt(0)).toLowerCase() + propName.substring(1);
//                Object propValue = null;
//                String propDBType = null;
//                try {
//                    propValue = method.invoke(paramObject, new Object[] {});
//                    //Field propField = paramObject.getClass().getField(propName);
//                    //ColumnDesc colDesc = propField.getAnnotation(ColumnDesc.class);
//                    //propDBType = colDesc != null ? colDesc.type() : mapJava2DB(propValue == null ? null : propValue.getClass());
//                    propDBType = mapJava2DB(propValue == null ? null : propValue.getClass());
//                } catch (Exception e) {
//                    propValue = null;
//                }
//                valueMap.put(propName, propValue);
//                typeMap.put(propName, propDBType);
//            }
//        }
//    }
//
//    /**
//     * getBlobResult
//     * 
//     * @param resultSet
//     * @param i
//     * @return
//     * @throws SQLException
//     */
//    protected Blob getBlobResult(
//            ResultSet resultSet,
//            int i) throws SQLException {
//        Blob result = null;
//        try {
//            java.sql.Blob blob = resultSet.getBlob(i);
//            if (blob != null) {
//                InputStream is = blob.getBinaryStream();
//                byte[] content = new byte[is.available()];
//                int temp;
//                int index = 0;
//                while ((temp = is.read()) != -1) {
//                    content[index++] = Byte.valueOf((byte) temp);
//                }
//                result = new Blob(content);
//            }
//        } catch (IOException e) {
//            throw new DataAccessException(e, "Failed to access BLOB data in BlobTypeHandler.");
//        }
//        return result;
//    }
//
//    /**
//     * setBlobStatement
//     * 
//     * @param statement
//     * @param paramIndex
//     * @param value
//     * @throws SQLException
//     */
//    protected void setBlobStatement(
//            PreparedStatement statement,
//            int paramIndex,
//            Blob value) throws SQLException {
//        statement.setBlob(paramIndex, value);
//    }
//
//    /**
//     * getClobResult
//     * 
//     * @param resultSet
//     * @param i
//     * @return
//     * @throws SQLException
//     */
//    protected Clob getClobResult(
//            ResultSet resultSet,
//            int i) throws SQLException {
//        Clob result = null;
//        try {
//            java.sql.Clob clob = resultSet.getClob(i);
//            if (clob != null) {
//                BufferedReader reader = new BufferedReader(clob.getCharacterStream());
//                StringBuilder contentStr = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    contentStr.append(line);
//                }
//                char[] content = contentStr.toString().toCharArray();
//                result = new Clob(content);
//            }
//        } catch (IOException e) {
//            throw new DataAccessException(e, "Failed to access CLOB data in ClobTypeHandler.");
//        }
//        return result;
//    }
//
//    /**
//     * setClobStatement
//     * 
//     * @param statement
//     * @param index
//     * @param value
//     * @throws SQLException
//     */
//    protected void setClobStatement(
//            PreparedStatement statement,
//            int index,
//            Clob value) throws SQLException {
//        statement.setClob(index, value);
//    }
//
//    /**
//     * Create JDBC Statement for input SQL text.<br>
//     * <p>
//     *
//     * @param sqlText
//     * @return
//     */
//    protected SqlMapItem parseSqlInputText(
//            String sqlText) {
//        if (sqlText == null || sqlText.trim().equals("")) {
//            // NULL check
//            return null;
//        }
//        // prepare new SQLConfigItem instance
//        SqlMapItem sqlConfigItem = new SqlMapItem();
//        // prepare sqlParams container
//        List<String> sqlParams = new ArrayList<String>();
//        // prepare SQL container for convert
//        StringBuilder sqlbuilder = new StringBuilder();
//        int index = 0;
//        while (index < sqlText.length()) {
//            if (isSqlParamBegin(sqlText.charAt(index))) {
//                // Match dynamic parameter
//                index++;
//                StringBuilder parambuilder = new StringBuilder();
//                while (!isSqlParamEnd(sqlText.charAt(index)) && index < sqlText.length()) {
//                    // Process SQL text till the next end flag of dynamic
//                    // parameter
//                    parambuilder.append(sqlText.charAt(index++));
//                }
//                sqlParams.add(parambuilder.toString().trim());
//                // Replace dynamic parameter with JDBC style
//                sqlbuilder.append(JDBC_PARAM_FLAG);
//                parambuilder = new StringBuilder();
//                if (index == sqlText.length()) {
//                    break;
//                } else {
//                    index++;
//                }
//            } else {
//                sqlbuilder.append(sqlText.charAt(index++));
//            }
//        }
//        // Set JDBC parameter
//        sqlConfigItem.setSqlText(sqlText);
//        sqlConfigItem.setSqlJdbcText(sqlbuilder.toString());
//        sqlConfigItem.setSqlParams(sqlParams);
//        return sqlConfigItem;
//    }
//
//    /**
//     * Dynamic start flag identify.<br>
//     * <p>
//     *
//     * @param c
//     * @return
//     */
//    protected boolean isSqlParamBegin(
//            char c) {
//        if (c == '{') {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Dynamic end flag identify.<br>
//     * <p>
//     *
//     * @param c
//     * @return
//     */
//    protected boolean isSqlParamEnd(
//            char c) {
//        if (c == '}') {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * @return the dataSource
//     */
//    public DataSource getDataSource() {
//        return dataSource;
//    }
//
//    /**
//     * @param dataSource
//     *            the dataSource to set
//     */
//    public void setDataSource(
//            DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    /**
//     * @return the sqlMapProperties
//     */
//    public ConfigProperties getSqlMapProperties() {
//        return sqlMapProperties;
//    }
//
//    /**
//     * @param sqlMapProperties
//     *            the sqlMapProperties to set
//     */
//    public void setSqlMapProperties(
//    		ConfigProperties sqlMapProperties) {
//        this.sqlMapProperties = sqlMapProperties;
//    }
//
//}
