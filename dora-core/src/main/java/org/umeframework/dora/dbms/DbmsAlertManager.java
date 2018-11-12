/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dbms;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.umeframework.dora.dbms.DbmsAlertMonitor.MessageHandler;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.util.ThreadUtil;

/**
 * DbmsAlertManager
 * 
 * @author mayue
 */
public class DbmsAlertManager extends BaseComponent {
    /**
     * Data source
     */
    private DataSource dataSource;
    /**
     * threadHolder
     */
    private Map<String, DbmsAlertMonitor> threadHolder = new HashMap<String, DbmsAlertMonitor>();

    /**
     * Start a DBMS monite thread to watch input message ID
     * 
     * @param messageId
     * @param messageTimeout
     * @param callbacker
     */
    public void start(
            int messageTimeout,
            String messageId,
            MessageHandler callbacker) {
        DbmsAlertMonitor monitor = new DbmsAlertMonitor(dataSource, messageTimeout, messageId, callbacker);
        threadHolder.put(messageId, monitor);
        new ThreadUtil(threadHolder.get(messageId), "registeAndExecute", null, null, getLogger()).start();
    }

    /**
     * stop
     * 
     * @param stop
     */
    public void stop(
            String messageId,
            boolean stop) {
        DbmsAlertMonitor thread = threadHolder.get(messageId);
        thread.setStopFlag(true);
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
    public void setDataSource(
            DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
