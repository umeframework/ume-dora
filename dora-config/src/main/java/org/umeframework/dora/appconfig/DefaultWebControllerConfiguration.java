/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.web.interceptor.GlobalAuthenticateInterceptor;

/**
 * Default web controller and global intercept configuration.<br>
 *
 * @author Yue Ma
 */
@Configuration
@ComponentScan(basePackages = "org.umeframework.dora")
public class DefaultWebControllerConfiguration implements WebMvcConfigurer {
	/**
	 * System properties
	 */
	@Resource(name = BeanConfigConst.SYSTEM_PROPERTY_CONFIGURATION)
	private DefaultSystemPropertyConfiguration cfg;

	/**
	 * Create SessionContextInterceptor instance
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_AUTHENTICATE_INTERCEPTOR)
	public GlobalAuthenticateInterceptor authenticateInterceptor() {
		return new GlobalAuthenticateInterceptor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry)
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticateInterceptor()).addPathPatterns("/**");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addViewControllers(org.springframework.web.servlet.config.annotation.ViewControllerRegistry)
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		String defaultPage = cfg.getDefaultPage();
		if (defaultPage != null) {
			defaultPage = defaultPage.startsWith("/") ? defaultPage : "/" + defaultPage;
			registry.addViewController("/").setViewName("forward:" + defaultPage);
			registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		}
	}

}
