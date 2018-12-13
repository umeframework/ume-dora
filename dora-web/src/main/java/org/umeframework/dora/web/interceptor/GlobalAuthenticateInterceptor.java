/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.interceptor;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.contant.HttpSessionConstants;
import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.exception.AuthenticationException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.UserObject;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.web.controller.CommonApiController;
import org.umeframework.dora.web.user.UserAccessValidator;
import org.umeframework.dora.web.user.UserCacheService;

/**
 * Global authenticate intercept handler.<br>
 *
 * @author Yue MA
 */
public class GlobalAuthenticateInterceptor extends BaseComponent implements HandlerInterceptor {
    /**
     * Service mapping instance
     */
    @Autowired(required = false)
    @Qualifier(BeanConfigConst.DEFAULT_SERVICE_MAPPING)
    private ServiceMapping serviceMapping;
    /**
     * User cache instance
     */
    @Autowired(required = false)
    @Qualifier(BeanConfigConst.DEFAULT_USER_CACHE_SERVICE)
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
        RequestContext.reset();
        super.getLogger().debug("SessionContext Open...");
        String sys = null;
        String sid = null;
        String uid = null;
        try {
            RequestContext.getCurrentContext().set(HTTP_REQUEST, request);
            RequestContext.getCurrentContext().set(HTTP_RESPONSE, response);
            RequestContext.getCurrentContext().set(TRANSACTION_START_TIME, new Timestamp(System.currentTimeMillis()));
            String token = request.getHeader(HttpSessionConstants.TOKEN_ID);
            if (StringUtil.isEmpty(token)) {
                token = request.getParameter(HttpSessionConstants.TOKEN_ID);
                if (StringUtil.isEmpty(token)) {
                    // try use token pass from parameter when no token in header
                    token = request.getParameter(HttpSessionConstants.TOKEN_ID);
                }
            }
            RequestContext.getCurrentContext().set(TOKEN, token);
            HttpSession session = request.getSession(false);
            RequestContext.getCurrentContext().set(HTTP_SESSION, session);

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
            String commonApiPathStart = "/" + CommonApiController.COMMON_API_ROOT_MAPPING + "/";
            String commonApiPath = CommonApiController.COMMON_API_SERVICE_MAPPING;
            for (String e : requestMapping.value()) {
                if (e.contains(commonApiPath)) {
                    isCommonRequestMapping = true;

                    String partPath = servletPath.substring(servletPath.indexOf(commonApiPathStart));
                    String[] names = partPath.split("/");
                    sys = names[2];
                    sid = names[3];
                    break;
                }
            }

            RequestContext.getCurrentContext().set(SYS, sys);
            RequestContext.getCurrentContext().set(SID, sid);

            super.getLogger().debug("SYS:" + sys);
            super.getLogger().debug("SID:" + sid);

            if (!isCommonRequestMapping || serviceMapping == null) {
                // Skip in case of No Common request mapping define such as "/capi/{system}/{resource}"
                return true;
            }
            // start process for common request mapping
            ServiceWrapper serviceWrapper = serviceMapping.getService(sid);

            if (serviceWrapper == null) {
                throw new AuthenticationException("Service " + sid + "  is not exist.");
            }
            if (!serviceWrapper.isAuthenticate()) {
                if (session == null) {
                    // create HttpSession when service is non-authentication (first time access after open browse usually)
                    session = request.getSession();
                    RequestContext.getCurrentContext().set(HTTP_SESSION, session);
                    getLogger().info("Create new session for non-authenticated service.");
                }
                return true;
            }

            if (enableTokenSession && StringUtil.isEmpty(token) && session != null) {
                // try use token pass from session store when enableTokenSession
                Object tokenInSession = session.getAttribute(HttpSessionConstants.TOKEN_ID);
                if (tokenInSession != null) {
                    token = tokenInSession.toString();
                    RequestContext.getCurrentContext().set(TOKEN, token);
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
            uid = userObj.getUid();
            if (userAccessValidator != null && !userAccessValidator.isValid(userObj, token, sys, sid)) {
                throw new AuthenticationException("Authentication failed: User access validation error.");
            }
            return true;
        } finally {
            ThreadContext.put("system", sys);
            ThreadContext.put("service", sid);
            ThreadContext.put("client", parseIpAddrFromServletRequest(request));
            ThreadContext.put("user", uid);
        }
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
            String serviceId = RequestContext.getCurrentContext().get(SID);
            String token = RequestContext.getCurrentContext().get(TOKEN);
            HttpSession session = RequestContext.getCurrentContext().get(HTTP_SESSION);
            if (serviceId != null && serviceMapping != null) {
                ServiceWrapper serviceRef = serviceMapping.getService(serviceId);
                // Response common items into HTTP header
                if (!serviceRef.isAuthenticate()) {
                    // must get token again because login service could create new token and store in context
                    token = RequestContext.getCurrentContext().get(TOKEN);
                }
                if (enableTokenClient) {
                    response.addHeader(HttpSessionConstants.TOKEN_ID, token);
                }
                if (enableTokenSession && session != null) {
                    session.setAttribute(HttpSessionConstants.TOKEN_ID, token);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            super.getLogger().debug("SessionContext Closed.");
            // close context
            RequestContext.close();
        }
    }

    /**
     * parseIpAddrFromServletRequest
     * 
     * @param request
     * @return
     */
    private String parseIpAddrFromServletRequest(HttpServletRequest request) {
        String ipAddr = request.getHeader("x-forwarded-for");
        if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
            ipAddr = request.getRemoteAddr();
        }
        return ipAddr;
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
