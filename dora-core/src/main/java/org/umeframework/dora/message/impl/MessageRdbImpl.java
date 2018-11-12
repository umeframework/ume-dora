/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.umeframework.dora.dao.RdbDao;
import org.umeframework.dora.exception.SystemException;

/**
 * Message properties implementing by RDB
 *
 * @author Yue MA
 */
public class MessageRdbImpl extends BaseMessageProperties {
	/**
	 * queryDao
	 */
	private RdbDao queryDao;
	/**
	 * msgKeyAlias
	 */
	private String msgKeyAlias = "msgKey";
	/**
	 * msgValueAlias
	 */
	private String msgValueAlias = "msgValue";

	/**
	 * Constructor
	 *
	 * @param queryDao
	 * @param queryId/querySql
	 */
	public MessageRdbImpl(RdbDao queryDao, String queryId) {
		super(queryId);
		this.queryDao = queryDao;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.core.properties.impl.AbstractMessageProperties# loadResourceAsMap(java.lang.String)
	 */
	@Override
	protected Map<String, String> loadResourceAsMap(String queryId) throws SystemException {
		try {
			List<Map<String, Object>> sysParams = queryDao.queryForMapList(queryId, null);
			Map<String, String> paramMap = new LinkedHashMap<String, String>();
			for (Map<String, Object> sysParam : sysParams) {
				String key = (String.valueOf(sysParam.get(msgKeyAlias))).trim();
				String value = (String.valueOf(sysParam.get(msgValueAlias))).trim();
				paramMap.put(key, value);
			}
			return paramMap;
		} catch (Exception e) {
			throw new SystemException(e, "Failed to load RDB message resource[" + queryId + "].");
		}
	}

	/**
	 * @return the msgKeyAlias
	 */
	public String getMsgKeyAlias() {
		return msgKeyAlias;
	}

	/**
	 * @param msgKeyAlias
	 *            the msgKeyAlias to set
	 */
	public void setMsgKeyAlias(String msgKeyAlias) {
		this.msgKeyAlias = msgKeyAlias;
	}

	/**
	 * @return the msgValueAlias
	 */
	public String getMsgValueAlias() {
		return msgValueAlias;
	}

	/**
	 * @param msgValueAlias
	 *            the msgValueAlias to set
	 */
	public void setMsgValueAlias(String msgValueAlias) {
		this.msgValueAlias = msgValueAlias;
	}

}
