///*
// * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
// */
//package org.umeframework.dora.dao.impl;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.umeframework.dora.bean.BeanUtil;
//import org.umeframework.dora.dao.RdbDao;
//import org.umeframework.dora.exception.DataAccessException;
//import org.umeframework.dora.log.LogUtil;
//import org.umeframework.dora.service.TableObject;
//
///**
// * JDBC DB access implementation class.<br>
// *
// * Sample:
// * <li>1) DB update with insert SQL<br>
// * <dd>SQL text： INSERT INTO USER (ID,NAME) VALUES ({id}, {name})<br>
// * <dd>Parameter as java bean which values id=100, name="Tom"<br>
// *
// * <li>2) DB retrieve with select SQL<br>
// * <dd>SQL text： SELECT ID AS "id", NAME AS "name" FROM USER<br>
// *
// * <li>3) DB retrieve with select SQL by primary key<br>
// * <dd>SQL text： SELECT ID AS "id", NAME AS "name" FROM USER WHERE ID = {id} <br>
// * <p>
// *
// * @author Yue MA
// */
//public class JdbcDaoImpl extends JdbcDaoSupport implements RdbDao {
//
//	/**
//	 * MAX query size of default setting
//	 */
//	private Integer queryMaxLimit = 4096;
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.ut.fw.CommonDao#update(java.lang.String, java.lang.Object)
//	 */
//	public <E> int update(String updateId, E updateParam) {
//		int result = -1;
//		PreparedStatement statement = null;
//		Connection connection = getConnection();
//		try {
//			// Create JDBC Statement
//			statement = createStatement(connection, updateId, updateParam);
//
//				getLogger().debug("JDBC.SQL=", updateId);
//				getLogger().debug("JDBC.Param=", LogUtil.toPlantText(updateParam));
//
//			result = statement.executeUpdate();
//
//		} catch (Exception e) {
//			throw new DataAccessException(e, "JDBC Dao update failed.");
//		} finally {
//			closeStatement(statement, connection);
//		}
//		return result;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.umeframework.dora.dao.RdbDao#updateMulti(java.lang.String, java.util.List)
//	 */
//	public <E> int[] updateMulti(String updateId, List<E> updateParams) {
//
//		if (updateParams == null) {
//			return null;
//		}
//		int[] result = new int[updateParams.size()];
//		for (int i = 0; i < updateParams.size(); i++) {
//			result[i] = update(updateId, updateParams.get(i));
//		}
//		return result;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.umeframework.dora.dao.QueryDao#queryForObjectList(java.lang.String, java.lang.Object, java.lang.Class)
//	 */
//	@Override
//	public <E> List<E> queryForObjectList(String queryId, Object queryParam, Class<? extends E> clazz) {
//
//		List<E> objList = new ArrayList<E>();
//		PreparedStatement statement = null;
//		Connection connection = getConnection();
//		try {
//			// Create JDBC Statement
//			statement = createStatement(connection, queryId, queryParam);
//
//			getLogger().debug("JDBC.SQL=", queryId);
//			getLogger().debug("JDBC.Param=", LogUtil.toPlantText(queryParam));
//
//			// Execute JDBC Statement
//			ResultSet resultset = statement.executeQuery();
//			// Get MetaData
//			ResultSetMetaData rsMetaData = resultset.getMetaData();
//			int colCount = rsMetaData.getColumnCount();
//			int rowIndex = 0;
//			int theFetchStart = this.getFetchStart(queryParam);
//			int theFetchSize = this.getFetchSize(queryParam);
//			if (theFetchSize > 0) {
//				resultset.setFetchSize(theFetchSize);
//			}
//			while (resultset.next()) {
//				if (rowIndex < theFetchStart) {
//					continue;
//				}
//				if (rowIndex >= theFetchStart + theFetchSize) {
//					break;
//				}
//				E obj = clazz.newInstance();
//				for (int i = 1; i <= colCount; i++) {
//					// Get column name
//					String colName = rsMetaData.getColumnLabel(i);
//					int colType = rsMetaData.getColumnType(i);
//					Object colValue = getResultset(resultset, i, colType);
//					try {
//						BeanUtil.setBeanProperty(obj, colName, colValue);
//					} catch (Exception e) {
//						throw new DataAccessException(e, "JDBC Dao query for object failed during set bean properties.");
//					}
//				}
//				objList.add(obj);
//				rowIndex++;
//			}
//		} catch (Exception e) {
//			throw new DataAccessException(e, "JDBC Dao query failed.");
//		} finally {
//			closeStatement(statement, connection);
//		}
//		return objList;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.umeframework.dora.dao.RdbDao#queryForMapList(java.lang.String, java.lang.Object)
//	 */
//	@Override
//	public List<Map<String, Object>> queryForMapList(String queryId, Object queryParam) {
//		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//		PreparedStatement statement = null;
//		Connection connection = getConnection();
//		try {
//			// Create JDBC Statement
//			statement = createStatement(connection, queryId, queryParam);
//
//			getLogger().debug("JDBC.SQL=", queryId);
//			getLogger().debug("JDBC.Param=", LogUtil.toPlantText(queryParam));
//
//			// Execute JDBC Statement
//			ResultSet resultset = statement.executeQuery();
//			// Get MetaData
//			ResultSetMetaData rsMetaData = resultset.getMetaData();
//			int colCount = rsMetaData.getColumnCount();
//			int rowIndex = 0;
//			int theFetchStart = this.getFetchStart(queryParam);
//			int theFetchSize = this.getFetchSize(queryParam);
//			if (theFetchSize > 0) {
//				resultset.setFetchSize(theFetchSize);
//			}
//			while (resultset.next()) {
//				if (rowIndex < theFetchStart) {
//					continue;
//				}
//				if (rowIndex >= theFetchStart + theFetchSize) {
//					break;
//				}
//				Map<String, Object> map = new LinkedHashMap<String, Object>();
//				for (int i = 1; i <= colCount; i++) {
//					// Get column name
//					String colName = rsMetaData.getColumnLabel(i);
//					int colType = rsMetaData.getColumnType(i);
//					Object colValue = getResultset(resultset, i, colType);
//					try {
//						map.put(colName, colValue);
//					} catch (Exception e) {
//						throw new DataAccessException(e, "JDBC Dao query for map list failed during set map values.");
//					}
//				}
//				mapList.add(map);
//			}
//		} catch (Exception e) {
//			throw new DataAccessException(e, "JDBC Dao query failed.");
//		} finally {
//			closeStatement(statement, connection);
//		}
//		return mapList;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.umeframework.dora.dao.QueryDao#queryForObject(java.lang.String, java.lang.Object, java.lang.Class)
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public <E> E queryForObject(String queryId, Object queryParam, Class<? extends E> clazz) {
//		E obj = null;
//		PreparedStatement statement = null;
//		Connection connection = getConnection();
//		try {
//			// Create JDBC Statement
//			statement = createStatement(connection, queryId, queryParam);
//
//			getLogger().debug("JDBC.SQL=", queryId);
//			getLogger().debug("JDBC.Param=", LogUtil.toPlantText(queryParam));
//
//			// Execute JDBC Statement
//			ResultSet resultset = statement.executeQuery();
//			// Get MetaData
//			ResultSetMetaData rsMetaData = resultset.getMetaData();
//			int colCount = rsMetaData.getColumnCount();
//
//			if (resultset.next()) {
//				if (isBasicType(clazz)) {
//					int i = 1;
//					int colType = rsMetaData.getColumnType(i);
//					obj = (E) getResultset(resultset, i, colType);
//				} else {
//					obj = clazz.newInstance();
//					for (int i = 1; i <= colCount; i++) {
//						// Get column name
//						String colName = rsMetaData.getColumnLabel(i);
//						int colType = rsMetaData.getColumnType(i);
//						Object colValue = getResultset(resultset, i, colType);
//						// Put result into map
//						try {
//							BeanUtil.setBeanProperty(obj, colName, colValue);
//						} catch (Exception e) {
//							throw new DataAccessException(e, "JDBC Dao query for object failed during set bean properties.");
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			throw new DataAccessException(e, "JDBC Dao query failed.");
//		} finally {
//			closeStatement(statement, connection);
//		}
//		return obj;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.umeframework.dora.dao.RdbDao#queryForMap(java.lang.String, java.lang.Object)
//	 */
//	@SuppressWarnings("unchecked")
//	@Override
//	public Map<String, Object> queryForMap(String queryId, Object queryParam) {
//		return queryForObject(queryId, queryParam, LinkedHashMap.class);
//	}
//
//	/**
//	 * queryForTableDesc
//	 * 
//	 * @param queryId
//	 * @param queryParam
//	 * @return
//	 */
//	public Map<String, Map<String, Object>> queryForTableDesc(String queryId, Object queryParam) {
//		Map<String, Map<String, Object>> colCfgMap = new LinkedHashMap<String, Map<String, Object>>();
//		PreparedStatement statement = null;
//		Connection connection = getConnection();
//		try {
//			// Create JDBC Statement
//			statement = createStatement(connection, queryId, queryParam);
//			if (getLogger() != null && getLogger().isDebugEnabled()) {
//				getLogger().debug("JDBC.SQL=", queryId);
//				getLogger().debug("JDBC.Param=", LogUtil.toPlantText(queryParam));
//			}
//
//			// Execute JDBC Statement
//			ResultSet resultset = statement.executeQuery();
//			// Get MetaData
//			ResultSetMetaData rsMetaData = resultset.getMetaData();
//			int colCount = rsMetaData.getColumnCount();
//			if (resultset.next()) {
//				for (int i = 1; i <= colCount; i++) {
//					Map<String, Object> colCfg = new LinkedHashMap<String, Object>();
//					// Get column define information
//					String tableId = rsMetaData.getTableName(i);
//					String schemaId = rsMetaData.getSchemaName(i);
//					String colId = rsMetaData.getColumnName(i);
//					String colName = rsMetaData.getColumnLabel(i);
//					int colType = rsMetaData.getColumnType(i);
//					int colPrecision = rsMetaData.getPrecision(i);
//					int colScale = rsMetaData.getScale(i);
//					// Create column configuration
//					colCfg.put("tableId", tableId);
//					colCfg.put("schemaId", schemaId);
//					colCfg.put("colId", colId);
//					colCfg.put("colName", colName);
//					colCfg.put("colType", String.valueOf(colType));
//					colCfg.put("colPrecision", String.valueOf(colPrecision));
//					colCfg.put("colScale", String.valueOf(colScale));
//					// Add to column configuration map
//					colCfgMap.put(colId, colCfg);
//				}
//			}
//		} catch (Exception e) {
//			throw new DataAccessException(e, "JDBC Dao query DESC failed.");
//		} finally {
//			closeStatement(statement, connection);
//		}
//		return colCfgMap;
//	}
//
//	/**
//	 * getFetchStart
//	 * 
//	 * @param queryParam
//	 * @return
//	 */
//	protected int getFetchStart(Object queryParam) {
//		Integer theFetchStart = null;
//		if (queryParam instanceof TableObject) {
//			theFetchStart = ((TableObject) queryParam).getTheFetchStart();
//		}
//		if (theFetchStart == null) {
//			theFetchStart = 0;
//		}
//		return theFetchStart;
//	}
//
//	/**
//	 * getFetchSize
//	 * 
//	 * @param queryParam
//	 * @return
//	 */
//	protected int getFetchSize(Object queryParam) {
//		Integer theFetchSize = null;
//		if (queryParam instanceof TableObject) {
//			theFetchSize = ((TableObject) queryParam).getTheFetchSize();
//		}
//		if (theFetchSize == null) {
//			theFetchSize = queryMaxLimit == null ? 1000 : queryMaxLimit;
//		}
//		return theFetchSize;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.umeframework.dora.dao.QueryDao#count(java.lang.String, java.lang.Object)
//	 */
//	@Override
//	public <E> int count(String queryId, Object queryParam) {
//		return queryForObject(queryId, queryParam, java.lang.Integer.class);
//	}
//
//	/**
//	 * @return the queryMaxLimit
//	 */
//	public Integer getQueryMaxLimit() {
//		return queryMaxLimit;
//	}
//
//	/**
//	 * @queryParam queryMaxLimit the queryMaxLimit to set
//	 */
//	public void setQueryMaxLimit(Integer queryMaxLimit) {
//		this.queryMaxLimit = queryMaxLimit;
//	}
//
//}
