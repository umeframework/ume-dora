/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.interceptor;

import java.sql.Timestamp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.exception.AuthenticationException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.UserObject;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.user.UserAccessValidator;
import org.umeframework.dora.service.user.UserCacheService;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.web.controller.CommonApiController;

/**
 * Global authenticate intercept handler.<br>
 *
 * @author Yue MA
 */
public class GlobalAuthenticateInterceptor extends BaseComponent implements HandlerInterceptor {
	/**
	 * Service mapping instance
	 */
	@Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
	private ServiceMapping serviceMapping;
	/**
	 * User cache instance
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AuthenticationException {
		super.getLogger().debug("SessionContext Open...");

		SessionContext ctx = SessionContext.open().reset();
		ctx.setRequest(request);
		ctx.setResponse(response);
		ctx.setTransactionTime(new Timestamp(System.currentTimeMillis()));
		String token = request.getHeader(SessionContext.TOKEN);
		if (StringUtil.isEmpty(token)) {
			token = request.getParameter(SessionContext.TOKEN);
			if (StringUtil.isEmpty(token)) {
				// try use token pass from parameter when no token in header
				token = request.getParameter(SessionContext.TOKEN);
			}
		}
		ctx.setToken(token);
		HttpSession session = request.getSession(false);
		ctx.setHttpSession(session);

		if (!(handler instanceof HandlerMethod)) {
			// Skip in case of No HandlerMethod Input
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		RequestMapping requestMapping = handlerMethod.getMethod().getDeclaredAnnotation(RequestMapping.class);
		if (requestMapping == null) {
			// Skip in case of No @RequestMapping declared
			return true;
		}

		boolean isCommonRequestMapping = false;
		String servletPath = request.getServletPath();
		String sysId = null;
		String serviceId = null;
		String commonApiPathStart = "/" + CommonApiController.COMMON_API_ROOT_MAPPING + "/";
		String commonApiPath = CommonApiController.COMMON_API_SERVICE_MAPPING;
		for (String e : requestMapping.value()) {
			if (e.contains(commonApiPath)) {
				isCommonRequestMapping = true;
				
				String partPath = servletPath.substring(servletPath.indexOf(commonApiPathStart));
				String[] names = partPath.split("/");
				sysId = names[2];
				serviceId = names[3];
				break;
			}
		}
		
		ctx.setSysId(sysId);
		ctx.setServiceId(serviceId);

		super.getLogger().debug("SysId:" + sysId);
		super.getLogger().debug("ServiceId:" + serviceId);
		
		if (!isCommonRequestMapping) {
			// Skip in case of No Common request mapping define such as "/capi/{system}/{resource}"
			return true;
		}
		// start process for common request mapping
		ServiceWrapper serviceWrapper = serviceMapping.getService(serviceId);

		if (serviceWrapper == null) {
			throw new AuthenticationException("Service " + serviceId + "  is not exist.");
		}
		if (!serviceWrapper.isAuthenticate()) {
			if (session == null) {
				// create HttpSession when service is non-authentication (first time access after open browse usually)
				session = request.getSession();
				ctx.setHttpSession(session);
				getLogger().info("Create new session for non-authenticated service.");
			}
			return true;
		}

		if (enableTokenSession && StringUtil.isEmpty(token) && session != null) {
			// try use token pass from session store when enableTokenSession
			Object tokenInSession = session.getAttribute(SessionContext.TOKEN);
			if (tokenInSession != null) {
				token = tokenInSession.toString();
				ctx.setToken(token);
			}
		}

		if (StringUtil.isEmpty(token)) {
			throw new AuthenticationException("Authentication failed: current access was not authorized.");
		}
		if (userCacheService == null) {
			// serviceAuthenticateProperties and userCacheService instance must provide for this intercept
			throw new AuthenticationException("Authentication failed: login service was not setup.");
		}
		UserObject userObj = userCacheService.getUserObject(token);
		if (userObj == null) {
			throw new AuthenticationException("Authentication failed: current user was not authorized.");
		}
		if (userAccessValidator != null && !userAccessValidator.isValid(userObj, token, sysId, serviceId)) {
			throw new AuthenticationException("Authentication failed: User access validation error.");
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		try {
			String serviceId = SessionContext.open().getServiceId();
			String token = SessionContext.open().getToken();
			HttpSession session = SessionContext.open().getHttpSession();
			if (serviceId != null) {
				ServiceWrapper serviceRef = serviceMapping.getService(serviceId);
				// Response common items into HTTP header
				if (!serviceRef.isAuthenticate()) {
					// must get token again because login service could create new token and store in context
					token = SessionContext.open().getToken();
				}
				if (enableTokenClient) {
					response.addHeader(SessionContext.TOKEN, token);
				}
				if (enableTokenSession && session != null) {
					session.setAttribute(SessionContext.TOKEN, token);
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// close context
			SessionContext.close();
			super.getLogger().debug("SessionContext Closed.");
		}
	}

	/**
	 * @param enableTokenClient
	 *            the enableTokenClient to set
	 */
	public void setEnableTokenClient(boolean enableTokenClient) {
		this.enableTokenClient = enableTokenClient;
	}

	/**
	 * @param userAccessValidator
	 *            the userAccessValidator to set
	 */
	public void setUserAccessValidator(UserAccessValidator<UserObject> userAccessValidator) {
		this.userAccessValidator = userAccessValidator;
	}

	/**
	 * @param enableTokenSession
	 *            the enableTokenSession to set
	 */
	public void setEnableTokenSession(boolean enableTokenSession) {
		this.enableTokenSession = enableTokenSession;
	}

	/**
	 * @param serviceMapping
	 *            the serviceMapping to set
	 */
	public void setServiceMapping(ServiceMapping serviceMapping) {
		this.serviceMapping = serviceMapping;
	}

	/**
	 * @param userCacheService
	 *            the userCacheService to set
	 */
	public void setUserCacheService(UserCacheService userCacheService) {
		this.userCacheService = userCacheService;
	}
}
