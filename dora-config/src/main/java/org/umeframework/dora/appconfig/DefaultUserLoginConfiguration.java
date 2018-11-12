/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.service.user.UserLoginService;
import org.umeframework.dora.service.user.impl.UserLoginServiceImpl;

/**
 * Default login service configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
public class DefaultUserLoginConfiguration {

	/**
	 * userLoginService
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_USER_LOGIN_SERVICE)
	public UserLoginService userLoginService() {
		UserLoginServiceImpl instance = new UserLoginServiceImpl();
		instance.setSingleLogin(false);
		return instance;
	}

}
