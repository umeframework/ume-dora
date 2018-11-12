/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.dao.DaoHelper;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.service.TableObject;
import org.umeframework.dora.util.StringUtil;

/**
 * BatisDaoSupport
 *
 * @author Yue MA
 *
 */
public abstract class BatisDaoSupport extends SqlSessionDaoSupport {
	/**
	 * theDivision key when use for Map
	 */
	private String divisionKey = "theDivision";
	/**
	 * Dao Helper
	 */
	private DaoHelper daoHelper;
	/**
	 * Logger
	 */
	@Resource(name = BeanConfigConst.DEFAULT_LOGGER)
	private Logger logger;

	/**
	 * escapeSql
	 * 
	 * @param queryParam
	 */
	protected void escapeSql(Object queryParam) {
		if (queryParam != null) {
			if (queryParam instanceof TableObject) {
				TableObject tableObj = (TableObject) queryParam;
				if (StringUtil.isNotEmpty(tableObj.getTheDivision())) {
					tableObj.setTheDivision(StringEscapeUtils.escapeSql(tableObj.getTheDivision()));
				}
				if (StringUtil.isNotEmpty(tableObj.getTheGroupByCondition())) {
					tableObj.setTheGroupByCondition(StringEscapeUtils.escapeSql(tableObj.getTheGroupByCondition()));
				}
				if (StringUtil.isNotEmpty(tableObj.getTheOrderByCondition())) {
					tableObj.setTheOrderByCondition(StringEscapeUtils.escapeSql(tableObj.getTheOrderByCondition()));
				}
				if (StringUtil.isNotEmpty(tableObj.getTheSchema())) {
					tableObj.setTheSchema(StringEscapeUtils.escapeSql(tableObj.getTheSchema()));
				}
				if (StringUtil.isNotEmpty(tableObj.getTheSQLCondition())) {
					tableObj.setTheSQLCondition(StringEscapeUtils.escapeSql(tableObj.getTheSQLCondition()));
				}
			} else if (queryParam instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> mapObj = (Map<String, Object>) queryParam;
				String[] properties = new String[] {
				        TableObject.Property.theDivision,
				        TableObject.Property.theFetchSize,
				        TableObject.Property.theFetchStart,
				        TableObject.Property.theGroupByCondition,
				        TableObject.Property.theOrderByCondition,
				        TableObject.Property.theSchema
				        };
				for (String e : properties) {
					Object value = mapObj.get(e);
					if (value != null && value instanceof String && !value.toString().trim().equals("")) {
						mapObj.put(e, StringEscapeUtils.escapeSql(value.toString()));
					}
				}
			}
		}
	}

	/**
	 * @param queryParam
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setupDivisionParam(Object queryParam) {
		String division = null;
		if (daoHelper != null) {
			division = daoHelper.getTheDivision(queryParam);
		}
		if (!StringUtil.isEmpty(division)) {
			if (queryParam instanceof TableObject) {
				((TableObject) queryParam).setTheDivision(division);
			} else if (queryParam instanceof Map) {
				((Map) queryParam).put(divisionKey, division);
			}
		}
	}

	/**
	 * @return the daoHelper
	 */
	public DaoHelper getDaoHelper() {
		return daoHelper;
	}

	/**
	 * @param daoHelper
	 *            the daoHelper to set
	 */
	public void setDaoHelper(DaoHelper daoHelper) {
		this.daoHelper = daoHelper;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the divisionKey
	 */
	public String getDivisionKey() {
		return divisionKey;
	}

	/**
	 * @param divisionKey
	 *            the divisionKey to set
	 */
	public void setDivisionKey(String divisionKey) {
		this.divisionKey = divisionKey;
	}
}
