package org.umeframework.dora.dsm;

/**
 * 动态数据源管理接口类。<br>
 * 
 * @author MA YUE
 */
public interface DataSourceManager<DAO, CFG> {
    
    /**
     * 根据分区编号获取数据源信息。<br>
     * 
     * @param key
     * @return
     * @throws Exception
     */
    DataSourceBean<DAO> getDataSourceBean(String key);
    
    /**
     * 根据配置信息创建数据源。<br>
     * 
     * @param key
     * @param cfgInfo
     * @return
     * @throws Exception
     */
    DataSourceBean<DAO> createDataSourceBean(String key, CFG cfgInfo) throws Exception;

    /**
     * refreshDataSourceBean<br>
     * 
     * @param key
     */
    void refreshDataSourceBean(String key);
}
