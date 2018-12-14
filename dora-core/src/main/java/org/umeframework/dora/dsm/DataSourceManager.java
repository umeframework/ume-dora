package org.umeframework.dora.dsm;

import java.io.Serializable;

/**
 * 动态数据源管理接口类。<br>
 * 
 * @author MA YUE
 */
public interface DataSourceManager<DAO, CFG extends Serializable> {

    /**
     * 根据分区编号获取数据源信息。<br>
     * 
     * @param key
     * @return
     * @throws Exception
     */
    DataSourceBean<DAO, CFG> getDataSourceBean(String key) throws Exception;

    /**
     * 根据分区编号移除数据源信息。<br>
     * 
     * @param key
     */
    void removeDataSourceBean(String key);

    /**
     * 根据配置信息创建数据源。<br>
     * 
     * @param key
     * @param cfgInfo
     * @param createCacheIfNotExist
     * @return
     * @throws Exception
     */
    DataSourceBean<DAO, CFG> createDataSourceBean(String key, CFG cfgInfo, boolean createCacheIfNotExist) throws Exception;

    /**
     * 根据配置信息创建数据源。<br>
     * 
     * @param key
     * @param cfgInfo
     * @return
     * @throws Exception
     */
    DataSourceBean<DAO, CFG> createDataSourceBean(String key, CFG cfgInfo) throws Exception;

    /**
     * refreshDataSourceBean<br>
     * 
     * @param key
     */
    void refreshDataSourceBean(String key);
}
