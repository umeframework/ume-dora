package org.umeframework.dora.dsm;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * 数据访问属性配置对象。<br>
 * 
 * @author MA YUE
 */
public class DataSourceBean<DAO> implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1096714695407805007L;
    /**
     * JDBC数据源实例<br>
     */
    private DataSource dataSource;
    /**
     * Mybatis DAO实例<br>
     */
    private DAO dao;
    /**
     * 事务管理器实例<br>
     */
    private DataSourceTransactionManager transactionManager;
    
    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * @return the transactionManager
     */
    public DataSourceTransactionManager getTransactionManager() {
        return transactionManager;
    }
    /**
     * @param transactionManager the transactionManager to set
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    /**
     * @return the dao
     */
    public DAO getDao() {
        return dao;
    }
    /**
     * @param dao the dao to set
     */
    public void setDao(DAO dao) {
        this.dao = dao;
    }
}
