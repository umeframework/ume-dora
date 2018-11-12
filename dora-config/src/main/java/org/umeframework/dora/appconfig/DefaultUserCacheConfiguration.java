/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.cache.CacheManager;
import org.umeframework.dora.cache.impl.TempMemoryCachedImpl;
import org.umeframework.dora.service.user.UserCacheService;
import org.umeframework.dora.service.user.impl.UserCacheServiceImpl;

/**
 * Default login service configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultUserCacheConfiguration {
	/**
	 * User Cache Manager
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_USER_CACHE_MANAGER, initMethod = "init", destroyMethod = "shutdown")
	public CacheManager accessTokenCacher() {
		TempMemoryCachedImpl instance = new TempMemoryCachedImpl(3600 * 24 * 90);
		return instance;
	}
	/**
	 * userCacheService
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_USER_CACHE_SERVICE)
	public UserCacheService userCacheService() {
		UserCacheServiceImpl instance = new UserCacheServiceImpl();
		instance.setTokenExpire(604800);
		instance.setSingleLogin(false);
		return instance;
	}

}
