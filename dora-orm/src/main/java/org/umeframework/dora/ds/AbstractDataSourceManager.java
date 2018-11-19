package org.umeframework.dora.ds;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.cache.Cache;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * AbstractDataSourceManager
 * 
 * @author MA YUE
 */
public abstract class AbstractDataSourceManager implements DataSourceManager {
    /**
     * dataAccessBeanMap local cache instance<br>
     */
    private static ConcurrentHashMap<String, DataSourceBean> localCachedMap = new ConcurrentHashMap<>();
    /**
     * dataSourceBean remote cache instance<br>
     */
    private Cache dataSourceBeanCache;

    /**
     * createDataSource
     * 
     * @param cfgInfo
     * @return
     * @throws SQLException
     */
    abstract protected DataSource createDataSource(PropertyResolver cfgInfo) throws SQLException;

    /**
     * getMybatisConfigLocation
     * 
     * @return
     */
    abstract protected String getMybatisConfigLocation();

    /**
     * getMybatisMapperLocations
     * 
     * @return
     */
    abstract protected String getMybatisMapperLocations();

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DynamicDataSourceManager#getDataAccessor(java.lang.String)
     */
    @Override
    public DataSourceBean getDataSourceBean(String key) {
        DataSourceBean cachedObj = localCachedMap.get(key);
        if (cachedObj == null) {
            Cache cache = this.getDataSourceBeanCache();
            if (cache != null) {
                cachedObj = (DataSourceBean) cache.get(key);
            }
        }
        return cachedObj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DataSourceManager#createDataSourceBean(java.lang.String, org.springframework.core.env.PropertyResolver)
     */
    public DataSourceBean createDataSourceBean(String key, PropertyResolver cfgInfo) throws Exception {
        DataSource dataSource = createDataSource(cfgInfo);
        SqlSession sqlSession = createSqlSession(dataSource);
        DataSourceTransactionManager transactionManager = createTransactionManager(dataSource);

        DataSourceBean bean = new DataSourceBean();
        bean.setDataSource(dataSource);
        bean.setSqlSession(sqlSession);
        bean.setTransactionManager(transactionManager);

        localCachedMap.put(key, bean);
        Cache cache = this.getDataSourceBeanCache();
        if (cache != null) {
            cache.put(key, bean);
        }
        return bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ds.DataSourceManager#refreshDataSourceBean(java.lang.String)
     */
    public synchronized void refreshDataSourceBean(String key) {
        Cache cache = this.getDataSourceBeanCache();
        if (cache != null) {
            DataSourceBean cachedObj = (DataSourceBean) cache.get(key);
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
    protected DataSourceTransactionManager createTransactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * createSqlSessionTemplate
     * 
     * @param dataSource
     * @return
     * @throws Exception
     */
    protected SqlSession createSqlSession(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        String configLocationPath = getMybatisConfigLocation();
        org.springframework.core.io.Resource configLocation = new DefaultResourceLoader().getResource(configLocationPath);
        sqlSessionFactoryBean.setConfigLocation(configLocation);
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        String mapperLocationsPath = getMybatisMapperLocations();
        org.springframework.core.io.Resource[] mappers = resourceResolver.getResources(mapperLocationsPath);
        if (mappers == null || mappers.length < 1) {
            // 如果找不到的话(比如说作为JAR嵌入到某个程序中), 那么寻找所有JAR包中的MAPPER文件
            mapperLocationsPath = mapperLocationsPath.replaceAll("classpath:", "classpath*:");
            mappers = resourceResolver.getResources(mapperLocationsPath);
        }
        sqlSessionFactoryBean.setMapperLocations(mappers);
        sqlSessionFactoryBean.setDataSource(dataSource);
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        SqlSessionTemplate sqlSession = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSession;
    };

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
