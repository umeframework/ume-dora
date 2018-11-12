/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * XMLMessageBeans
 * 
 * @author Yue MA
 */
public class XmlMessageBeans implements java.io.Serializable {

    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7000498194891632604L;
	/**
	 * beans
     */
    private List<XmlMessageBean> beans = new ArrayList<XmlMessageBean>();

    /**
     * addBean
     * 
     * @param bean
     */
    public void addBean(
            XmlMessageBean bean) {
        beans.add(bean);
    }

    /**
     * @return the beans
     */
    public List<XmlMessageBean> getBeans() {
        return beans;
    }

    /**
     * @param beans
     *            the beans to set
     */
    public void setBeans(
            List<XmlMessageBean> beans) {
        this.beans = beans;
    }
}
