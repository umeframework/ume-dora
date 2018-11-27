package org.umeframework.dora.dsm;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.cache.Cache;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * AbstractDataSourceManager
 * 
 * @author MA YUE
 */
public abstract class AbstractDataSourceManager<DAO, CFG> implements DataSourceManager<DAO, CFG> {
    /**
     * dataAccessBeanMap local cache instance<br>
     */
    private static ConcurrentHashMap<String, DataSourceBean<?>> localCachedMap = new ConcurrentHashMap<>();
    /**
     * dataSourceBean remote cache instance<br>
     */
    private Cache dataSourceBeanCache;

    /**
     * createDataSource
     * 
     * @param configInfo
     * @return
     * @throws SQLException
     */
    abstract public DataSource createDataSource(CFG configInfo) throws SQLException;

    /**
     * createDao
     * 
     * @param dataSource
     * @return
     * @throws Exception
     */
    abstract public DAO createDao(DataSource dataSource) throws Exception;

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DynamicDataSourceManager#getDataAccessor(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public DataSourceBean<DAO> getDataSourceBean(String key) {
        DataSourceBean<DAO> cachedObj = (DataSourceBean<DAO>) (localCachedMap.get(key));
        if (cachedObj == null) {
            Cache cache = this.getDataSourceBeanCache();
            if (cache != null) {
                cachedObj = (DataSourceBean<DAO>) cache.get(key);
            }
        }
        return cachedObj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.dsm.DataSourceManager#createDataSourceBean(java.lang.String, java.lang.Object)
     */
    @Override
    public DataSourceBean<DAO> createDataSourceBean(String key, CFG cfgInfo) throws Exception {
        return createDataSourceBean(key, cfgInfo, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DataSourceManager#createDataSourceBean(java.lang.String, org.springframework.core.env.PropertyResolver)
     */
    @Override
    public DataSourceBean<DAO> createDataSourceBean(String key, CFG cfgInfo, boolean cachedOnlyNotExist) throws Exception {
        DataSource dataSource = createDataSource(cfgInfo);
        DAO sqlSession = createDao(dataSource);
        PlatformTransactionManager transactionManager = createTransactionManager(dataSource);

        DataSourceBean<DAO> bean = new DataSourceBean<DAO>();
        bean.setDataSource(dataSource);
        bean.setDao(sqlSession);
        bean.setTransactionManager(transactionManager);

        localCachedMap.put(key, bean);
        Cache cache = this.getDataSourceBeanCache();
        if (cache != null) {
            if (cachedOnlyNotExist && cache.get(key) == null) {
                cache.put(key, bean);
            }
        }
        return bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DataSourceManager#refreshDataSourceBean(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void refreshDataSourceBean(String key) {
        Cache cache = this.getDataSourceBeanCache();
        if (cache != null) {
            DataSourceBean<DAO> cachedObj = (DataSourceBean<DAO>) cache.get(key);
            if (cachedObj != null) {
                localCachedMap.put(key, cachedObj);
            } else {
                localCachedMap.remove(key);
            }
        }
    }

    /**
     * createTransactionManager
     * 
     * @param dataSource
     * @return
     */
    public PlatformTransactionManager createTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * @return the dataSourceBeanCache
     */
    public Cache getDataSourceBeanCache() {
        return dataSourceBeanCache;
    }

    /**
     * @param dataSourceBeanCache
     *            the dataSourceBeanCache to set
     */
    public void setDataSourceBeanCache(Cache dataSourceBeanCache) {
        this.dataSourceBeanCache = dataSourceBeanCache;
    }

}
