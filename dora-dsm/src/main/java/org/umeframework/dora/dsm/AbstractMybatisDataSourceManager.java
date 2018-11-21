package org.umeframework.dora.dsm;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * AbstractMybatisDataSourceManager
 * 
 * @author MA YUE
 */
public abstract class AbstractMybatisDataSourceManager<CFG> extends AbstractDataSourceManager<SqlSession, CFG> {
    /**
     * getMybatisConfigLocation
     * 
     * @return
     */
    abstract public String getMybatisConfigLocation();

    /**
     * getMybatisMapperLocations
     * 
     * @return
     */
    abstract public String getMybatisMapperLocations();

    /**
     * createDao
     * 
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Override
    public SqlSession createDao(DataSource dataSource) throws Exception {
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

}
