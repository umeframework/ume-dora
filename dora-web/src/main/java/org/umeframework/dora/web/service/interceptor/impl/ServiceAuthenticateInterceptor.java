/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.exception.AuthenticationException;
import org.umeframework.dora.property.ConfigProperties;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.UserObject;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.user.UserAccessValidator;
import org.umeframework.dora.service.user.UserCacheService;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.web.service.interceptor.Interceptor;
import org.umeframework.dora.web.service.interceptor.InterceptorChain;

/**
 * Service authenticate intercept.<br>
 *
 * @author Yue MA
 */
@Deprecated
public class ServiceAuthenticateInterceptor extends BaseComponent implements Interceptor {
	/**
	 * Web Service initialize manager
	 */
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
	private ServiceMapping serviceMapping;
	/**
	 * User cache service instance
	 */
	@Resource(name = BeanConfigConst.DEFAULT_USER_CACHE_SERVICE)
	private UserCacheService userCacheService;
	/**
	 * User access control resource checker
	 */
	private UserAccessValidator<UserObject> userAccessValidator;
	/**
	 * Token will pass by client for authenticate
	 */
	private boolean enableTokenClient = true;
	/**
	 * Token will store in http session, enableTokenClient is prefer while both enableTokenClient and enableTokenSession were true
	 */
	private boolean enableTokenSession = true;

	/**
	 * serviceWhiteListConfigProperties
	 */
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_WHITE_LIST_CONFIG_PROPERTIES)
	private ConfigProperties serviceWhiteListConfigProperties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.interceptor.Interceptor#intercept(org.umeframework.dora.interceptor. InterceptorChain)
	 */
	@Override
	public void intercept(InterceptorChain chain) throws Throwable {
		if (serviceWhiteListConfigProperties != null && serviceWhiteListConfigProperties.keySet().size() > 0) {
			// check white list if serviceWhiteListConfigProperties was provided
			String ipAddr = SessionContext.open().getClientAddress();// .get(SessionContext.Key.RequestHost);
			if (StringUtil.isEmpty(ipAddr)) {
				ipAddr = chain.getRequest().getRemoteAddr();
			}
			checkAccessibleAddress(ipAddr, chain.getSysId(), chain.getServiceId());
		}
		if (userCacheService == null) {
			// serviceAuthenticateProperties and userCacheService instance must
			// provide for this intercept
			throw new AuthenticationException("Authentication failed: login service was not setup.");
		}
		// // start context
		// Timestamp curremtTime = super.getCurrentTimestamp();

		SessionContext ctx = SessionContext.open();

		// get session if exist
		HttpSession session = chain.getRequest().getSession(false);
		String serviceId = chain.getServiceId();
		// String uid = null;
		String token = null;
		ServiceWrapper serviceRef = serviceMapping.getService(serviceId);

		if (serviceRef == null) {
			throw new AuthenticationException("Service " + serviceId + "  is not exist");
		}
		if (!serviceRef.isAuthenticate() && session == null) {
			// create HttpSession when service is non-authentication (first time access after open browse usually)
			session = chain.getRequest().getSession();
			ctx.setHttpSession(session); // .set(SessionContext.Key.HttpSession, session);
			getLogger().debug("Create new session for non-authenticated service.");
		}

		if (enableTokenClient) {
			// prefer use token pass from header when enableTokenClient
			token = chain.getRequest().getHeader(SessionContext.TOKEN);
			if (StringUtil.isEmpty(token)) {
				// try use token pass from parameter when no token in header
				token = chain.getRequest().getParameter(SessionContext.TOKEN);
			}
		}
		if (enableTokenSession && StringUtil.isEmpty(token) && session != null) {
			// try use token pass from session store when enableTokenSession
			token = String.valueOf(session.getAttribute(SessionContext.TOKEN));
		}
		if (StringUtil.isEmpty(token)) {
			throw new AuthenticationException("Authentication failed: current access was not authorized.");
		}

		try {
			if (serviceRef.isAuthenticate()) {
				UserObject userObj = userCacheService.getUserObject(token);
				if (userObj == null) {
					throw new AuthenticationException("Authentication failed: current user was not authorized.");
				}
				if (userAccessValidator != null && !userAccessValidator.isValid(userObj, token, chain.getSysId(), chain.getServiceId())) {
					throw new AuthenticationException("Authentication failed: User access validation error.");
				}
			}
			// set append common items in context
			ctx.setToken(token);// .set(SessionContext.Key.Token, token);
			// ctx.set(SessionContext.Key.UID, uid);
			// do normal process
			chain.next();

		} finally {
			// Response common items into HTTP header
			if (!serviceRef.isAuthenticate()) {
				// must get token again because login service could create new token and store in context
				token = ctx.getToken();// .get(SessionContext.Key.Token);
			}
			if (enableTokenClient) {
				chain.getResponse().addHeader(SessionContext.TOKEN, token);
			}
			if (enableTokenSession && session != null) {
				session.setAttribute(SessionContext.TOKEN, token);
			}
		}
	}

	/**
	 * Check accessible client address
	 * 
	 * @param address
	 * @param sysId
	 * @param serviceId
	 * @throws AuthenticationException
	 */
	protected void checkAccessibleAddress(String address, String sysId, String serviceId) throws AuthenticationException {
		super.getLogger().debug("Check accessible client address:" + address);
		String configValue = serviceWhiteListConfigProperties.get(address);
		super.getLogger().debug("serviceWhiteList config:" + configValue);
		if (StringUtil.isEmpty(configValue)) {
			throw new AuthenticationException("Prohibit access error: address " + address + " is invalid.");
		}
		if (!configValue.equalsIgnoreCase("true") && !configValue.equals("*")) {
			configValue = configValue.contains("|") ? configValue.replace("|", ",") : configValue;
			String[] values = configValue.contains(",") ? configValue.split(",") : new String[] { configValue };
			List<String> availableServices = new ArrayList<String>();
			for (String value : values) {
				availableServices.add(value);
			}
			if (!availableServices.contains(serviceId)) {
				throw new AuthenticationException("Prohibit access error: address " + address + " is invalid.");
			}
		}
	}

	/**
	 * @return the enableTokenClient
	 */
	public boolean isEnableTokenClient() {
		return enableTokenClient;
	}

	/**
	 * @param enableTokenClient
	 *            the enableTokenClient to set
	 */
	public void setEnableTokenClient(boolean enableTokenClient) {
		this.enableTokenClient = enableTokenClient;
	}

	/**
	 * @return the userAccessValidator
	 */
	public UserAccessValidator<UserObject> getUserAccessValidator() {
		return userAccessValidator;
	}

	/**
	 * @param userAccessValidator
	 *            the userAccessValidator to set
	 */
	public void setUserAccessValidator(UserAccessValidator<UserObject> userAccessValidator) {
		this.userAccessValidator = userAccessValidator;
	}

	/**
	 * @return the enableTokenSession
	 */
	public boolean isEnableTokenSession() {
		return enableTokenSession;
	}

	/**
	 * @param enableTokenSession
	 *            the enableTokenSession to set
	 */
	public void setEnableTokenSession(boolean enableTokenSession) {
		this.enableTokenSession = enableTokenSession;
	}

	/**
	 * @return the serviceWhiteListConfigProperties
	 */
	public ConfigProperties getServiceWhiteListConfigProperties() {
		return serviceWhiteListConfigProperties;
	}

	/**
	 * @param serviceWhiteListConfigProperties
	 *            the serviceWhiteListConfigProperties to set
	 */
	public void setServiceWhiteListConfigProperties(ConfigProperties serviceWhiteListConfigProperties) {
		this.serviceWhiteListConfigProperties = serviceWhiteListConfigProperties;
	}

	/**
	 * @return the serviceMapping
	 */
	public ServiceMapping getServiceMapping() {
		return serviceMapping;
	}

	/**
	 * @param serviceMapping
	 *            the serviceMapping to set
	 */
	public void setServiceMapping(ServiceMapping serviceMapping) {
		this.serviceMapping = serviceMapping;
	}

	/**
	 * @return the userCacheService
	 */
	public UserCacheService getUserCacheService() {
		return userCacheService;
	}

	/**
	 * @param userCacheService
	 *            the userCacheService to set
	 */
	public void setUserCacheService(UserCacheService userCacheService) {
		this.userCacheService = userCacheService;
	}

}
