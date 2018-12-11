/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User basic entity object.<br>
 *
 * @author Yue MA
 */
public class UserObject implements Serializable {
    /**
	 * serial version ID
	 */
	private static final long serialVersionUID = -7782561093844234342L;
	/**
     * user identify ID
     */
    private String uid;
    /**
     * login time
     */
    private Timestamp lastTransactionTime;
    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }
    /**
     * @param uid the uid to set
     */
    public void setUid(
            String uid) {
        this.uid = uid;
    }
    /**
     * @return the lastTransactionTime
     */
    public Timestamp getLastTransactionTime() {
        return lastTransactionTime;
    }
    /**
     * @param lastTransactionTime the lastTransactionTime to set
     */
    public void setLastTransactionTime(
            Timestamp lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }
}
