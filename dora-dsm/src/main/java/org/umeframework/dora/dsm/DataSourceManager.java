package org.umeframework.dora.dsm;

import org.springframework.core.env.PropertyResolver;

/**
 * 动态数据源管理接口类。<br>
 * 
 * @author MA YUE
 */
public interface DataSourceManager {
    
    /**
     * 根据分区编号获取数据源信息。<br>
     * 
     * @param key
     * @return
     * @throws Exception
     */
    DataSourceBean getDataSourceBean(String key);
    
    /**
     * 根据配置信息创建数据源。<br>
     * 
     * @param key
     * @param cfgInfo
     * @return
     * @throws Exception
     */
    DataSourceBean createDataSourceBean(String key, PropertyResolver cfgInfo) throws Exception;
    
    
    /**
     * refreshDataSourceBean<br>
     * 
     * @param key
     */
    void refreshDataSourceBean(String key);
}
