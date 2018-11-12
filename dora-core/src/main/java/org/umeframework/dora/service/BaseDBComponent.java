/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import javax.annotation.Resource;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.dao.RdbDao;

/**
 * Common data access service component function define
 *
 * @author Yue MA
 */
public abstract class BaseDBComponent extends BaseComponent {
	/**
	 * RdbDao
	 */
	@Resource(name = BeanConfigConst.DEFAULT_RDB_DAO)
	private RdbDao dao;

	/**
	 * @return the dao
	 */
	public RdbDao getDao() {
		return dao;
	}

}
