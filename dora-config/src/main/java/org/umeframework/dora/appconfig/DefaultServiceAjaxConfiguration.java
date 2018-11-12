/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.ajax.AjaxParser;
import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.bean.BeanConfigConst;

/**
 * Json perse/render configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultServiceAjaxConfiguration {
	/**
	 * ajaxParser
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_AJAX_PARSER)
	public AjaxParser<String> ajaxParser() {
		return new org.umeframework.dora.ajax.impl.JsonParserImpl();
	}

	/**
	 * ajaxRender
	 * 
	 * @return
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_AJAX_RENDER)
	public AjaxRender<String> ajaxRender() {
		return new org.umeframework.dora.ajax.impl.UnicodeJsonRenderImpl();
	}
}
