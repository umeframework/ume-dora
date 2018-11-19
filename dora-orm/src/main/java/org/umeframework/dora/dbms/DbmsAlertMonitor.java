/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dbms;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.umeframework.dora.exception.DataAccessException;
import org.umeframework.dora.service.BaseComponent;

/**
 * DBMS alert monite thread implementation class.
 * 
 * @author mayue
 */
public class DbmsAlertMonitor extends BaseComponent { // implements Runnable {
    /**
     * Callback interface
     */
    public interface MessageHandler {
        /**
         * process
         * 
         * @param messageId
         * @param message
         */
        public void process(
                String messageId,
                String message);
    }

    /**
     * Constant define
     */
    public final static String DBMS_SINGAL_DECLARE_REGISTE_ALERT = "#DORA_DBMSALERT_REGISTE";
    /**
     * Constant define
     */
    public final static String DBMS_SINGAL_DECLARE_REMOVE_ALERT = "#DORA_DBMSALERT_REMOVE";
    /**
     * messageId
     */
    private String messageId;
    /**
     * messageTimeout
     */
    private int messageTimeout;
    /**
     * callbacker
     */
    private MessageHandler messageHandler;

    /**
     * stop flag
     */
    private boolean stopFlag = false;
    /**
     * Data source
     */
    private DataSource dataSource;

    /**
     * DbmsAlertThread
     * 
     * @param messageId
     * @param messageTimeout
     * @param callbacker
     */
    public DbmsAlertMonitor(
            DataSource dataSource,
            int messageTimeout,
            String messageId,
            MessageHandler callbacker) {
        this.dataSource = dataSource;
        this.messageId = messageId;
        this.messageTimeout = messageTimeout;
        this.messageHandler = callbacker;
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see java.lang.Runnable#run()
    // */
    // @Override
    // public void run() {
    // this.registe(this.messageId);
    // this.execute(this.messageTimeout, this.callbacker);
    // }

    /**
     * registeAndExecute
     */
    public void registeAndExecute() {
        this.registe();
        this.execute();
    }

    /**
     * registe
     * 
     * @param messageId
     */
    public void registe() {
        try {
            CallableStatement smtRegister = getConnection().prepareCall("{call DBMS_ALERT.REGISTER(?)}");
            smtRegister.setString(1, messageId);
            smtRegister.executeUpdate();
            getConnection().commit();
            smtRegister.close();
                getLogger().info("REGISTED DDBMS_ALERT: " + messageId);
        } catch (Exception e) {
            throw new DataAccessException(e, "Error during registing DBMS alert.");
        }
    }

    /**
     * remove
     * 
     * @param messageId
     */
    public void remove() {
        try {
            CallableStatement smtRemove = getConnection().prepareCall("{call DBMS_ALERT.REMOVE(?)}");
            smtRemove.setString(1, messageId);
            smtRemove.executeUpdate();
            getConnection().commit();
            smtRemove.close();
                getLogger().info("REMOVED DDBMS_ALERT: " + messageId);
        } catch (Exception e) {
            throw new DataAccessException(e, "Error during removing DBMS alert.");
        }
    }

    /**
     * execute
     * 
     * @param timeout
     */
    public void execute() {
        Connection conn = null;
        CallableStatement smtWaitany = null;
        try {
            conn = getConnection();
            smtWaitany = conn.prepareCall("{call DBMS_ALERT.WAITANY(?,?,?,?)}");
            smtWaitany.registerOutParameter(1, Types.VARCHAR);
            smtWaitany.registerOutParameter(2, Types.VARCHAR);
            smtWaitany.registerOutParameter(3, Types.INTEGER);
            smtWaitany.setInt(4, messageTimeout);
            while (!stopFlag) {
                smtWaitany.executeUpdate();
                if (smtWaitany.getInt(3) == 1) {
                    getLogger().info("DBMS alert time out: " + messageTimeout + " SECONDS");
                    continue;
                }
                String messageId = smtWaitany.getString(1);
                String message = smtWaitany.getString(2);

                getLogger().info("Reverived DDBMS alert:" + messageId + ":" + message);

                if (messageId.equals(DBMS_SINGAL_DECLARE_REGISTE_ALERT)) {
                    this.registe();
                } else if (messageId.equals(DBMS_SINGAL_DECLARE_REMOVE_ALERT)) {
                    this.remove();
                } else {
                    if (messageHandler != null) {
                        messageHandler.process(messageId, message);
                    }
                }
            }
            smtWaitany.close();
            getConnection();
        } catch (Exception e) {
            throw new DataAccessException(e, "Error during executing DBMS CallableStatement.");
        } finally {
            try {
                if (smtWaitany != null) {
                    smtWaitany.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e, "Failed to close JDBC statement/connection.");
            }

        }
    }

    /**
     * Get connection instance
     *
     * @return
     */
    protected Connection getConnection() {
        try {
            // must use spring DataSourceUtils get connection while transaction managed by spring
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (Exception e) {
            throw new DataAccessException(e, "Fail to get JDBC connection");
        }
    }

    /**
     * Close Statement
     *
     * @param statement
     */
    protected void releaseConnection(
            Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e, "Failed to close JDBC connection.");
        }
    }

    /**
     * @return the stopFlag
     */
    public boolean isStopFlag() {
        return stopFlag;
    }

    /**
     * @param stopFlag
     *            the stopFlag to set
     */
    public void setStopFlag(
            boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

}
