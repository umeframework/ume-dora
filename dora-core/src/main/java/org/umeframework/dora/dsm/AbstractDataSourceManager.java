package org.umeframework.dora.dsm;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * AbstractDataSourceManager
 * 
 * @author MA YUE
 */
public abstract class AbstractDataSourceManager<DAO, CFG extends Serializable> implements DataSourceManager<DAO, CFG> {
    /**
     * dataAccessBeanMap local cache instance<br>
     */
    private static ConcurrentHashMap<String, DataSourceBean<?, ?>> localCachedMap = new ConcurrentHashMap<>();
    /**
     * dataSourceBean remote cache instance<br>
     */
    private Cache cache;

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
    public DataSourceBean<DAO, CFG> getDataSourceBean(String key) throws Exception {
        DataSourceBean<DAO, CFG> localCachedObj = (DataSourceBean<DAO, CFG>) (localCachedMap.get(key));
        if (localCachedObj != null) {
            return localCachedObj;
        }
        if (cache != null) {
            DataSourceBean<DAO, CFG> bean = (DataSourceBean<DAO, CFG>) getCacheValue(key);
            if (bean != null) {
                // restore transient filed in DataSourceBean instance
                bean = restoreDataSourceBean(bean.getConfigInfo());
                // cache in local
                localCachedMap.put(key, bean);
                return bean;
            }
        }
        return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DataSourceManager#createDataSourceBean(java.lang.String, org.springframework.core.env.PropertyResolver)
     */
    @Override
    public DataSourceBean<DAO, CFG> createDataSourceBean(String key, CFG configInfo, boolean cachedOnlyNotExist) throws Exception {
        DataSourceBean<DAO, CFG> bean = restoreDataSourceBean(configInfo);
        localCachedMap.put(key, bean);
        if (cache != null) {
            if (cachedOnlyNotExist && getCacheValue(key) == null) {
                setCacheValue(key, bean);
            }
        }
        return bean;
    }

    /**
     * restoreDataSourceBean
     * 
     * @param configInfo
     * @return
     * @throws Exception
     */
    protected DataSourceBean<DAO, CFG> restoreDataSourceBean(CFG configInfo) throws Exception {
        DataSource dataSource = createDataSource(configInfo);
        PlatformTransactionManager transactionManager = createTransactionManager(dataSource);
        DAO dao = createDao(dataSource);
        DataSourceBean<DAO, CFG> bean = new DataSourceBean<DAO, CFG>();
        bean.setDataSource(dataSource);
        bean.setTransactionManager(transactionManager);
        bean.setDao(dao);
        bean.setConfigInfo(configInfo);
        return bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.dsm.DataSourceManager#createDataSourceBean(java.lang.String, java.lang.Object)
     */
    @Override
    public DataSourceBean<DAO, CFG> createDataSourceBean(String key, CFG cfgInfo) throws Exception {
        return createDataSourceBean(key, cfgInfo, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DataSourceManager#refreshDataSourceBean(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void refreshDataSourceBean(String key) {
        if (cache != null) {
            DataSourceBean<DAO, CFG> remoteCachedObj = (DataSourceBean<DAO, CFG>) getCacheValue(key);
            if (remoteCachedObj != null) {
                localCachedMap.put(key, remoteCachedObj);
            } else {
                localCachedMap.remove(key);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.dsm.DataSourceManager#removeDataSourceBean(java.lang.String)
     */
    public synchronized void removeDataSourceBean(String key) {
        if (cache != null) {
            cache.evict(key);
        }
        localCachedMap.remove(key);
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
     * getCacheValue
     * 
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> E getCacheValue(String key) {
        Cache cache = getCache();
        Object value = cache.get(key);
        if (value instanceof SimpleValueWrapper) {
            value = (E)((SimpleValueWrapper) value).get();
        }
        return (E)value;
    }
    
    /**
     * setCacheValue
     * 
     * @param key
     * @param value
     */
    public <E> void setCacheValue(String key, E value) {
        Cache cache = getCache();
        cache.put(key, value);
    }

    /**
     * @return the cache
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * @param cache
     *            the cache to set
     */
    public void setCache(Cache cache) {
        this.cache = cache;
    }

}
