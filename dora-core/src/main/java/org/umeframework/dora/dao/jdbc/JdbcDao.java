/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.umeframework.dora.log.LogUtil;
import org.umeframework.dora.service.BaseComponent;

/**
 * JDBC DB access implementation class.<br>
 *
 * Sample:
 * <li>1) DB update with insert SQL<br>
 * <dd>SQL text： INSERT INTO USER (ID,NAME) VALUES ({id}, {name})<br>
 * <dd>Parameter as java bean which values id=100, name="Tom"<br>
 *
 * <li>2) DB retrieve with select SQL<br>
 * <dd>SQL text： SELECT ID AS "id", NAME AS "name" FROM USER<br>
 *
 * <li>3) DB retrieve with select SQL by primary key<br>
 * <dd>SQL text： SELECT ID AS "id", NAME AS "name" FROM USER WHERE ID = {id} <br>
 * <p>
 *
 * @author Yue MA
 */
public class JdbcDao extends BaseComponent {
    private Integer maxPageSize = 1000;
    private BeanHelper beanHelper = new BeanHelper();
    private DataSource dataSource;

    /**
     * update
     * 
     * @param <E>
     * @param sql
     * @param param
     * @return
     */
    public <E> int update(String sql, E param) {
        int result = -1;
        PreparedStatement statement = null;
        Connection connection = getConnection();
        try {
            statement = createStatement(connection, sql, param);
            result = statement.executeUpdate();
        } catch (Exception e) {
            throw JdbcDaoException.newInstance("JDBC update error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(statement, connection);
        }
        return result;
    }

    /**
     * queryForObjectList
     * 
     * @param <E>
     * @param sql
     * @param param
     * @param resultClass
     * @return
     */
    public <E> List<E> queryForObjectList(String sql, Object param, Class<? extends E> resultClass) {
        List<E> resultList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            ResultSetMetaData rsMetaData = resultset.getMetaData();
            int colCount = rsMetaData.getColumnCount();
            int rowStart = this.getFetchStart(param);
            int rowEnd = this.getFetchSize(param) + rowStart;
            if (rowEnd > 0) {
                resultset.setFetchSize(rowEnd);
            }
            int rowNum = 0;
            while (rowNum < rowEnd) {
                resultset.next();
                if (rowNum++ < rowStart) {
                    continue;
                }
                E obj = beanHelper.createBean(resultClass);
                for (int i = 1; i <= colCount; i++) {
                    String colName = rsMetaData.getColumnLabel(i);
                    int colType = rsMetaData.getColumnType(i);
                    Object colValue = getResultset(resultset, i, colType);
                    beanHelper.setBeanProperty(obj, colName, colValue);
                }
                resultList.add(obj);
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC queryForObjectList error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return resultList;
    }

    /**
     * queryForMapList
     * 
     * @param sql
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryForMapList(String sql, Object param) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            ResultSetMetaData rsMetaData = resultset.getMetaData();
            int colCount = rsMetaData.getColumnCount();
            int rowStart = this.getFetchStart(param);
            int rowEnd = this.getFetchSize(param) + rowStart;
            if (rowEnd > 0) {
                resultset.setFetchSize(rowEnd);
            }
            int rowNum = 0;
            while (rowNum < rowEnd) {
                resultset.next();
                if (rowNum++ < rowStart) {
                    continue;
                }
                Map<String, Object> map = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String colName = rsMetaData.getColumnLabel(i);
                    int colType = rsMetaData.getColumnType(i);
                    Object colValue = getResultset(resultset, i, colType);
                    map.put(colName, colValue);
                }
                resultList.add(map);
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC queryForMapList error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return resultList;
    }

    /**
     * queryForObject
     * 
     * @param <E>
     * @param sql
     * @param param
     * @param resultClass
     * @return
     */
    public <E> E queryForObject(String sql, Object param, Class<? extends E> resultClass) {
        E obj = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            ResultSetMetaData rsMetaData = resultset.getMetaData();
            int colCount = rsMetaData.getColumnCount();
            if (resultset.next()) {
                obj = beanHelper.createBean(resultClass);
                for (int i = 1; i <= colCount; i++) {
                    String colName = rsMetaData.getColumnLabel(i);
                    int colType = rsMetaData.getColumnType(i);
                    Object colValue = getResultset(resultset, i, colType);
                    beanHelper.setBeanProperty(obj, colName, colValue);
                }
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC queryForObject error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return obj;
    }

    /**
     * queryForMap
     * 
     * @param sql
     * @param param
     * @return
     */
    public Map<String, Object> queryForMap(String sql, Object param) {
        Map<String, Object> map = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            ResultSetMetaData rsMetaData = resultset.getMetaData();
            int colCount = rsMetaData.getColumnCount();
            if (resultset.next()) {
                map = new LinkedHashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String colName = rsMetaData.getColumnLabel(i);
                    int colType = rsMetaData.getColumnType(i);
                    Object colValue = getResultset(resultset, i, colType);
                    map.put(colName, colValue);
                }
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC queryForMap error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return map;
    }
    
    /**
     * queryForString
     * 
     * @param sql
     * @param param
     * @return
     */
    public String queryForString(String sql, Object param) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            if (resultset.next()) {
                return resultset.getString(1);
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC queryForString error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return null;
    }
    
    /**
     * queryForInt
     * 
     * @param sql
     * @param param
     * @return
     */
    public int queryForInt(String sql, Object param) {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            if (resultset.next()) {
                return resultset.getInt(1);
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC queryForInt error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return -1;
    }

    /**
     * count
     * 
     * @param sql
     * @param param
     * @return
     */
    public int count(String sql, Object param) {
        return queryForInt(sql, param);
    }

    /**
     * queryForTableDesc
     * 
     * @param sql
     * @param param
     * @return
     */
    public Map<String, Map<String, Object>> queryForTableDesc(String sql, Object param) {
        Map<String, Map<String, Object>> colCfgMap = new LinkedHashMap<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        try {
            statement = createStatement(connection, sql, param);
            resultset = statement.executeQuery();
            ResultSetMetaData rsMetaData = resultset.getMetaData();
            int colCount = rsMetaData.getColumnCount();
            if (resultset.next()) {
                for (int i = 1; i <= colCount; i++) {
                    Map<String, Object> colCfg = new LinkedHashMap<>();
                    // Get column define information
                    String tableId = rsMetaData.getTableName(i);
                    String schemaId = rsMetaData.getSchemaName(i);
                    String colId = rsMetaData.getColumnName(i);
                    String colName = rsMetaData.getColumnLabel(i);
                    int colType = rsMetaData.getColumnType(i);
                    int colPrecision = rsMetaData.getPrecision(i);
                    int colScale = rsMetaData.getScale(i);
                    // Create column configuration
                    colCfg.put("tableId", tableId);
                    colCfg.put("schemaId", schemaId);
                    colCfg.put("colId", colId);
                    colCfg.put("colName", colName);
                    colCfg.put("colType", String.valueOf(colType));
                    colCfg.put("colPrecision", String.valueOf(colPrecision));
                    colCfg.put("colScale", String.valueOf(colScale));
                    // Add to column configuration map
                    colCfgMap.put(colId, colCfg);
                }
            }
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC execution error, SQL={}, Param={}", e, sql, param);
        } finally {
            closeStatement(resultset, statement, connection);
        }
        return colCfgMap;
    }

    /**
     * Get connection instance.<br>
     *
     * @return
     */
    protected Connection getConnection() {
        Connection conn = null;
        try {
            // must use spring DataSourceUtils get connection while transaction managed by spring
            conn = DataSourceUtils.doGetConnection(dataSource);
        } catch (Exception e) {
            throw  JdbcDaoException.newInstance("JDBC connection get error", e);
        }
        return conn;
    }

    /**
     * Close Statement
     *
     * @param statement
     */
    protected void closeStatement(Statement statement, Connection connection) {
        closeStatement(null, statement, connection);
    }

    protected void closeStatement(ResultSet resultset, Statement statement, Connection connection) {
        try {
            if (resultset != null) {
                resultset.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                // must use spring DataSourceUtils release connection while transaction managed by spring
                DataSourceUtils.doReleaseConnection(connection, dataSource);
            }
        } catch (SQLException e) {
            throw  JdbcDaoException.newInstance("JDBC statement close error", e);
        }
    }

    /**
     * createStatement
     *
     * @param connection
     * @param sql
     * @param param
     * @return
     * @throws SQLException
     */
    protected PreparedStatement createStatement(Connection connection, String sql, Object param) throws SQLException {
        JdbcParam jdbcParam = parseSqlInputText(sql);
        String sqlJdbcText = jdbcParam.getSqlJdbcText();
        List<String> sqlParamList = jdbcParam.getSqlParams();
        Map<String, Object> valueMap = new HashMap<>();
        if (param != null) {
            if (TypeMapping.isBasicType(param)) {
                valueMap.put(sqlParamList.get(0), param);
            } else {
                getPropertiesAndValues(param, valueMap);
            }
        }
        PreparedStatement statement = null;
        if (valueMap.size() == 0) {
            // parameter not exist
            statement = connection.prepareStatement(sqlJdbcText);
            return statement;
        }
        // create prepareStatement
        statement = connection.prepareStatement(sqlJdbcText);
        // set parameter to prepareStatement
        for (int i = 0; i < sqlParamList.size(); i++) {
            String name = sqlParamList.get(i);
            Object val = valueMap.get(name);
            setStatement(i + 1, statement, val);
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("SQL={0},Param={1}", sql, LogUtil.toPlantText(param));
        }
        return statement;
    }

    /**
     * Set Parameter with JDBC Statement
     *
     * @param index
     * @param statement
     * @param value
     * @throws SQLException
     */
    protected void setStatement(int index, PreparedStatement statement, Object value) throws SQLException {
        if (value == null) {
            statement.setObject(index, null);
            return;
        }
        Class<?> clazz = value.getClass();
        if (String.class.equals(clazz)) {
            statement.setString(index, (String) value);
        } else if (char.class.equals(clazz) || Character.class.equals(clazz)) {
            statement.setString(index, String.valueOf(value));
        } else if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
            statement.setString(index, String.valueOf(value));
        } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
            statement.setInt(index, (Integer) value);
        } else if (short.class.equals(clazz) || Short.class.equals(clazz)) {
            statement.setShort(index, (Short) value);
        } else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
            statement.setLong(index, (Long) value);
        } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
            statement.setDouble(index, (Double) value);
        } else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
            statement.setDouble(index, (Float) value);
        } else if (value instanceof java.math.BigInteger) {
            statement .setBigDecimal(index, new java.math.BigDecimal((java.math.BigInteger) value));
        } else if (value instanceof java.math.BigDecimal) {
            statement.setBigDecimal(index, (java.math.BigDecimal) value);
        } else if (value instanceof java.sql.Timestamp) {
            statement.setTimestamp(index, (java.sql.Timestamp) value);
        } else if (value instanceof java.sql.Time) {
            statement.setTime(index, (java.sql.Time) value);
        } else if (value instanceof java.sql.Date) {
            statement.setDate(index, (java.sql.Date) value);
        } else if (java.util.Date.class.equals(value.getClass())) {
            java.sql.Timestamp timestampValue = new java.sql.Timestamp(((java.util.Date) value).getTime());
            statement.setTimestamp(index, timestampValue);
        } else if (value instanceof java.sql.Clob) {
            statement.setClob(index, (java.sql.Clob)value);
        } else if (value instanceof java.sql.Blob) {
            statement.setBlob(index, (java.sql.Blob)value);
        } else if (value instanceof byte[]) {
            statement.setBytes(index, (byte[]) value);
        } else if (value instanceof Byte[]) {
            statement.setBytes(index, (byte[]) value);
        } else {
            throw  JdbcDaoException.newInstance("The type " + clazz + " is not support in current JdbcDao implementation.");
        }
    }

    /**
     * Get Value from JDBC ResultSet
     *
     * @param resultset
     * @param columnIndex
     * @param colType
     * @return
     * @throws SQLException
     * @throws IOException 
     */
    protected Object getResultset(ResultSet resultset, int columnIndex, int colType) throws SQLException, IOException {
        Object value = null;
        switch (colType) {
        case java.sql.Types.CHAR:
        case java.sql.Types.VARCHAR:
        case java.sql.Types.LONGVARCHAR:
        case java.sql.Types.LONGNVARCHAR: 
            value = resultset.getString(columnIndex);
            break;
        case java.sql.Types.DATE: 
            value = resultset.getDate(columnIndex);
            break;
        case java.sql.Types.TIMESTAMP: 
            value = resultset.getTimestamp(columnIndex);
            break;
        case java.sql.Types.TIME: 
            value = resultset.getTime(columnIndex);
            break;
        case java.sql.Types.DECIMAL:
        case java.sql.Types.NUMERIC: 
            value = resultset.getBigDecimal(columnIndex);
            break;
        case java.sql.Types.SMALLINT: 
            value = resultset.getShort(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.TINYINT: 
            value = resultset.getByte(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.INTEGER: 
            value = resultset.getInt(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.BIGINT: 
            BigDecimal decimalValue = resultset.getBigDecimal(columnIndex);
            value = decimalValue != null ? decimalValue.toBigInteger() : null;
            break;
        case java.sql.Types.REAL: 
        case java.sql.Types.FLOAT: 
            value = resultset.getFloat(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.DOUBLE: 
            value = resultset.getDouble(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.BOOLEAN:
        case java.sql.Types.BIT: 
            value = resultset.getBoolean(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.LONGVARBINARY:
        case java.sql.Types.VARBINARY:
        case java.sql.Types.BINARY: 
            value = resultset.getBytes(columnIndex);
            value = resultset.wasNull() ? null : value;
            break;
        case java.sql.Types.BLOB: 
            value = getBlobResult(resultset, columnIndex);
            break;
        case java.sql.Types.CLOB: 
            value = getClobResult(resultset, columnIndex);
            break;
        default: 
            value = resultset.getString(columnIndex);
        }
        return value;
    }

    /**
     * Get DB Data Type of JDBC Type
     *
     * @param jdbcType
     * @return
     */
    protected String getDataType(int jdbcType) {
        String type = null;
        switch (jdbcType) {
        case java.sql.Types.CHAR: 
            type = "CHAR";
            break;
        case java.sql.Types.VARCHAR: 
            type = "VARCHAR2";
            break;
        case java.sql.Types.LONGNVARCHAR: 
            type = "LONGNVARCHAR";
            break;
        case java.sql.Types.DATE: 
            type = "DATE";
            break;
        case java.sql.Types.TIMESTAMP: 
            type = "TIMESTAMP";
            break;
        case java.sql.Types.TIME: 
            type = "TIME";
            break;
        case java.sql.Types.DECIMAL: 
            type = "DECIMAL";
            break;
        case java.sql.Types.NUMERIC: 
            type = "NUMERIC";
            break;
        case java.sql.Types.SMALLINT: 
            type = "SMALLINT";
            break;
        case java.sql.Types.TINYINT: 
            type = "TINYINT";
            break;
        case java.sql.Types.INTEGER: 
            type = "INTEGER";
            break;
        case java.sql.Types.BIGINT: {
            type = "BIGINT";
            break;
        }
        case java.sql.Types.FLOAT: 
            type = "FLOAT";
            break;
        case java.sql.Types.DOUBLE: 
            type = "DOUBLE";
            break;
        case java.sql.Types.REAL: 
            type = "REAL";
            break;
        case java.sql.Types.BIT: 
            type = "BIT";
            break;
        case java.sql.Types.BLOB: 
            type = "BLOB";
            break;
        case java.sql.Types.CLOB: 
            type = "CLOB";
            break;
        default:
            type = null;
        }
        return type;
    }

    /**
     * Convert Java Bean to Map
     *
     * @param paramObject
     * @return
     */
    protected void getPropertiesAndValues(Object paramObject, Map<String, Object> valueMap) {

        if (paramObject instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> mapObject = (Map<String, Object>) paramObject;
            for (Map.Entry<String, Object> entry : mapObject.entrySet()) {
                String propName = entry.getKey();
                Object propValue = entry.getValue();
                valueMap.put(propName, propValue);
            }
        } else {
            for (Method method : paramObject.getClass().getDeclaredMethods()) {
                String methodName = method.getName();
                if (!methodName.startsWith("get")) {
                    continue;
                }
                String propName = methodName.substring(3);
                propName = String.valueOf(propName.charAt(0)).toLowerCase() + propName.substring(1);
                Object propValue = null;
                try {
                    propValue = method.invoke(paramObject);
                } catch (Exception e) {
                    propValue = null;
                }
                valueMap.put(propName, propValue);
            }
        }
    }

    /**
     * getBlobResult
     * 
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    protected byte[] getBlobResult(ResultSet resultSet, int i) throws SQLException, IOException {
        byte[] result = null;
        java.sql.Blob blob = resultSet.getBlob(i);
        if (blob != null) {
            try (InputStream is = blob.getBinaryStream()) {
                result = new byte[is.available()];
                int b;
                int index = 0;
                while ((b = is.read()) != -1) {
                    result[index++] = (byte) b;
                }
            }
        }
        return result;
    }
    /**
     * getClobResult
     * 
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    protected String getClobResult(ResultSet resultSet, int i) throws SQLException, IOException {
        String result = null;
        java.sql.Clob clob = resultSet.getClob(i);
        if (clob != null) {
            try (BufferedReader reader = new BufferedReader(clob.getCharacterStream())) {
                StringBuilder contentStr = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    contentStr.append(line);
                }
                char[] content = contentStr.toString().toCharArray();
                result = new String(content);
            }
        }
        return result;
    }

    /**
     * Create JDBC Statement for input SQL text.<br>
     * <p>
     *
     * @param sqlText
     * @return
     */
    protected JdbcParam parseSqlInputText(String sqlText) {
        if (sqlText == null || sqlText.trim().equals("")) {
            // NULL check
            return null;
        }
        // prepare new SQLConfigItem instance
        JdbcParam sqlConfigItem = new JdbcParam();
        // prepare sqlParams container
        List<String> sqlParams = new ArrayList<>();
        // prepare SQL container for convert
        StringBuilder sqlbuilder = new StringBuilder();
        int index = 0;
        while (index < sqlText.length()) {
            if (isSqlParamBegin(sqlText.charAt(index))) {
                // Match dynamic parameter
                index++;
                StringBuilder parambuilder = new StringBuilder();
                while (!isSqlParamEnd(sqlText.charAt(index)) && index < sqlText.length()) {
                    // Process SQL text till the next end flag of dynamic
                    // parameter
                    parambuilder.append(sqlText.charAt(index++));
                }
                sqlParams.add(parambuilder.toString().trim());
                // Replace dynamic parameter with JDBC style
                sqlbuilder.append('?');
                if (index == sqlText.length()) {
                    break;
                } else {
                    index++;
                }
            } else {
                sqlbuilder.append(sqlText.charAt(index++));
            }
        }
        // Set JDBC parameter
        sqlConfigItem.setSqlText(sqlText);
        sqlConfigItem.setSqlJdbcText(sqlbuilder.toString());
        sqlConfigItem.setSqlParams(sqlParams);
        return sqlConfigItem;
    }

    /**
     * Dynamic start flag identify.<br>
     * <p>
     *
     * @param c
     * @return
     */
    protected boolean isSqlParamBegin(char c) {
        return c == '{';
    }

    /**
     * Dynamic end flag identify.<br>
     * <p>
     *
     * @param c
     * @return
     */
    protected boolean isSqlParamEnd(char c) {
        return c == '}';
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource
     *            the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * getFetchStart
     * 
     * @param queryParam
     * @return
     */
    protected int getFetchStart(Object queryParam) {
        Object value = beanHelper.getBeanProperty(queryParam, "pageStart");
        return value == null ? 0 : (Integer) value;
    }

    /**
     * getFetchSize
     * 
     * @param queryParam
     * @return
     */
    protected int getFetchSize(Object queryParam) {
        Object value = beanHelper.getBeanProperty(queryParam, "pageSize");
        return value == null ? maxPageSize : (Integer) value;
    }

    /**
     * @return the maxPageSize
     */
    public Integer getMaxPageSize() {
        return maxPageSize;
    }

    /**
     * @param maxPageSize the maxPageSize to set
     */
    public void setMaxPageSize(Integer maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

}
