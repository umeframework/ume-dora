package org.umeframework.dora.dsm;

/**
 * JDBC数据源配置信息对象。<br>
 * 
 * @author MA YUE
 */
public class DataSourceConfigInfoBean implements java.io.Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1096714695407805007L;
    // 数据源配置项目信息
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxWait;
    private Boolean removeAbandoned;
    private Integer removeAbandonedTimeout;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;
    private String connectionProperties;
    private String filters;
    /**
     * @return the driverClassName
     */
    public String getDriverClassName() {
        return driverClassName;
    }
    /**
     * @param driverClassName the driverClassName to set
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * @return the initialSize
     */
    public Integer getInitialSize() {
        return initialSize;
    }
    /**
     * @param initialSize the initialSize to set
     */
    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }
    /**
     * @return the maxActive
     */
    public Integer getMaxActive() {
        return maxActive;
    }
    /**
     * @param maxActive the maxActive to set
     */
    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }
    /**
     * @return the minIdle
     */
    public Integer getMinIdle() {
        return minIdle;
    }
    /**
     * @param minIdle the minIdle to set
     */
    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
    /**
     * @return the maxWait
     */
    public Integer getMaxWait() {
        return maxWait;
    }
    /**
     * @param maxWait the maxWait to set
     */
    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }
    /**
     * @return the removeAbandoned
     */
    public Boolean getRemoveAbandoned() {
        return removeAbandoned;
    }
    /**
     * @param removeAbandoned the removeAbandoned to set
     */
    public void setRemoveAbandoned(Boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }
    /**
     * @return the removeAbandonedTimeout
     */
    public Integer getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }
    /**
     * @param removeAbandonedTimeout the removeAbandonedTimeout to set
     */
    public void setRemoveAbandonedTimeout(Integer removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }
    /**
     * @return the timeBetweenEvictionRunsMillis
     */
    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }
    /**
     * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    /**
     * @return the minEvictableIdleTimeMillis
     */
    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }
    /**
     * @param minEvictableIdleTimeMillis the minEvictableIdleTimeMillis to set
     */
    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    /**
     * @return the validationQuery
     */
    public String getValidationQuery() {
        return validationQuery;
    }
    /**
     * @param validationQuery the validationQuery to set
     */
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
    /**
     * @return the testWhileIdle
     */
    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }
    /**
     * @param testWhileIdle the testWhileIdle to set
     */
    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    /**
     * @return the testOnBorrow
     */
    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }
    /**
     * @param testOnBorrow the testOnBorrow to set
     */
    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    /**
     * @return the testOnReturn
     */
    public Boolean getTestOnReturn() {
        return testOnReturn;
    }
    /**
     * @param testOnReturn the testOnReturn to set
     */
    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    /**
     * @return the poolPreparedStatements
     */
    public Boolean getPoolPreparedStatements() {
        return poolPreparedStatements;
    }
    /**
     * @param poolPreparedStatements the poolPreparedStatements to set
     */
    public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }
    /**
     * @return the maxPoolPreparedStatementPerConnectionSize
     */
    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }
    /**
     * @param maxPoolPreparedStatementPerConnectionSize the maxPoolPreparedStatementPerConnectionSize to set
     */
    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }
    /**
     * @return the connectionProperties
     */
    public String getConnectionProperties() {
        return connectionProperties;
    }
    /**
     * @param connectionProperties the connectionProperties to set
     */
    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }
    /**
     * @return the filters
     */
    public String getFilters() {
        return filters;
    }
    /**
     * @param filters the filters to set
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
