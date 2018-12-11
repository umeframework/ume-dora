/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.http.HttpProxy;
import org.umeframework.dora.http.RemoteServiceProxy;
import org.umeframework.dora.http.impl.HttpProxyImpl;
import org.umeframework.dora.http.impl.RemoteServiceProxyImpl;

/**
 * HTTP proxy and remote service invoke configuration.<br>
 * 
 * @author Yue Ma
 */
@Configuration
public class DefaultHttpProxyConfiguration {
	/**
	 * httpProxy
	 *
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_HTTP_PROXY)
	public HttpProxy httpProxy() throws Exception {
		HttpProxyImpl instance = new HttpProxyImpl();
		instance.setAppContentType("application/json");
		instance.setEntityContentType("text/json");
		instance.setEntityCharset("UTF-8");
		instance.setParamCharset("UTF-8");
		instance.setUseSSL(false);
		return instance;
	}
	
	/**
	 * httpProxy[json]
	 *
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_HTTP_PROXY_JSON)
	public HttpProxy httpProxyJson() throws Exception {
		HttpProxyImpl instance = new HttpProxyImpl();
		instance.setAppContentType("application/json");
		instance.setEntityContentType("text/json");
		instance.setEntityCharset("UTF-8");
		instance.setParamCharset("UTF-8");
		instance.setUseSSL(false);
		return instance;
	}
	
	/**
	 * httpProxy[xml]
	 *
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_HTTP_PROXY_XML)
	public HttpProxy httpProxyXml() throws Exception {
		HttpProxyImpl instance = new HttpProxyImpl();
		instance.setAppContentType("application/xml");
		instance.setEntityContentType("text/xml");
		instance.setEntityCharset("UTF-8");
		instance.setParamCharset("UTF-8");
		instance.setUseSSL(false);
		return instance;
	}
	
	/**
	 * httpProxy[x-www-form-urlencoded]
	 *
	 * @return
	 * @throws Exception
	 */
	@Scope("singleton")
	@Bean(name = BeanConfigConst.DEFAULT_HTTP_PROXY_X_WWW_FORM_URLENCODED)
	public HttpProxy httpProxyJsonXWwwFormUrlencoded() throws Exception {
		HttpProxyImpl instance = new HttpProxyImpl();
		instance.setAppContentType("application/x-www-form-urlencoded");
		instance.setEntityContentType("application/x-www-form-urlencoded");
		instance.setEntityCharset("UTF-8");
		instance.setParamCharset("UTF-8");
		instance.setUseSSL(false);
		return instance;
	}

    /**
     * remoteServiceProxy
     * 
     * @param httpProxy
     * @return
     * @throws Exception
     */
	@Scope("singleton")
    @Bean(name = BeanConfigConst.DEFAULT_REMOTE_SERVICE_CLIENT)
    public RemoteServiceProxy remoteServiceProxy() throws Exception {
        RemoteServiceProxy instance = new RemoteServiceProxyImpl(httpProxy());
        return instance;
    }
	
}
