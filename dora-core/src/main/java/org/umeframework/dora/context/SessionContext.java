/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.context;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * SessionContext
 * 
 * @author Yue MA
 */
public class SessionContext {
    /**
     * Constants 'Token' string
     */
    public static final String TOKEN = "Token";
    /**
     * ThreadLocal instance
     */
    private static final ThreadLocal<SessionContext> contextHolder = new InheritableThreadLocal<SessionContext>();
    /**
     * Sync control flag
     */
    private static final byte[] block = new byte[0];

    /**
     * HttpServletRequest reference
     */
    private HttpServletRequest request;
    /**
     * HttpServletResponse reference
     */
    private HttpServletResponse response;
    /**
     * HttpSession reference
     */
    private HttpSession httpSession;
    /**
     * Token
     */
    private String token;
    /**
     * Transaction time
     */
    private Timestamp transactionTime;
    /**
     * Service retry exception
     */
    private RuntimeException serviceRetryException;
    /**
     * System ID
     */
    private String sysId;
    /**
     * Service ID
     */
    private String serviceId;
    /**
     * Unique ID
     */
    private String uid;
    /**
     * Request path parameters
     */
    private String[] servicePathParameters;
    /**
     * Any data
     */
    private Map<String, Object> dataMap;

    /**
     * Create new SessionContext instance
     *
     * @return
     */
    public static SessionContext open() {
        SessionContext context = contextHolder.get();
        synchronized (block) {
            if (context == null) {
                context = new SessionContext();
                contextHolder.set(context);
            }
        }
        return context;
    }

    /**
     * Close current SessionContext instance
     */
    public static void close() {
        SessionContext context = contextHolder.get();
        synchronized (block) {
            if (context != null) {
                context.setHttpSession(null);
                context.setRequest(null);
                context.setResponse(null);
                context.setServiceId(null);
                context.setServicePathParameters(null);
                context.setServiceRetryException(null);
                context.setSysId(null);
                context.setToken(null);
                context.setTransactionTime(null);
                context.setUid(null);
                context.dataMap.clear();
            }
            contextHolder.set(null);
            contextHolder.remove();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public SessionContext clone() {
        SessionContext context = new SessionContext();
        context.setHttpSession(this.getHttpSession());
        context.setRequest(this.getRequest());
        context.setResponse(this.getResponse());
        context.setServiceId(this.getServiceId());
        context.setServicePathParameters(this.getServicePathParameters());
        context.setServiceRetryException(this.getServiceRetryException());
        context.setSysId(this.getSysId());
        context.setToken(this.getToken());
        context.setTransactionTime(this.getTransactionTime());
        context.setUid(this.getUid());
        context.dataMap = this.getDataMap();
        return context;
    }

    /**
     * Copy from another SessionContext instance
     * 
     * @param tar
     */
    public static SessionContext openFrom(SessionContext src) {
        synchronized (block) {
            SessionContext context = open();
            context.setHttpSession(src.getHttpSession());
            context.setRequest(src.getRequest());
            context.setResponse(src.getResponse());
            context.setServiceId(src.getServiceId());
            context.setServicePathParameters(src.getServicePathParameters());
            context.setServiceRetryException(src.getServiceRetryException());
            context.setSysId(src.getSysId());
            context.setToken(src.getToken());
            context.setTransactionTime(src.getTransactionTime());
            context.setUid(src.getUid());
            context.dataMap = src.getDataMap();
            return context;
        }
    }

    /**
     * RequestContext
     */
    private SessionContext() {
        this.dataMap = new HashMap<>();
    }

    /**
     * getIpAddr
     * 
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
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
     * @param dataMap
     *            the dataMap to set
     */
    public <E> void setData(String key, E value) {
        this.dataMap.put(key, value);
    }

    /**
     * @param dataMap
     *            the dataMap to set
     */
    @SuppressWarnings("unchecked")
    public <E> E getData(String key) {
        return (E) this.dataMap.get(key);
    }

    /**
     * @return the dataMap
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    /**
     * @return the servletRequest
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * @param servletRequest
     *            the servletRequest to set
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return the servletResponse
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * @param servletResponse
     *            the servletResponse to set
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * @return the httpSession
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * @param httpSession
     *            the httpSession to set
     */
    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the transactionTime
     */
    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    /**
     * @param transactionTime
     *            the transactionTime to set
     */
    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * @return the sysId
     */
    public String getSysId() {
        return sysId;
    }

    /**
     * @param sysId
     *            the sysId to set
     */
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId
     *            the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the servicePathParameters
     */
    public String[] getServicePathParameters() {
        return servicePathParameters;
    }

    /**
     * @param servicePathParameters
     *            the servicePathParameters to set
     */
    public void setServicePathParameters(String[] servicePathParameters) {
        this.servicePathParameters = servicePathParameters;
    }

    /**
     * @return the serviceRetryException
     */
    public RuntimeException getServiceRetryException() {
        return serviceRetryException;
    }

    /**
     * @param serviceRetryException
     *            the serviceRetryException to set
     */
    public void setServiceRetryException(RuntimeException serviceRetryException) {
        this.serviceRetryException = serviceRetryException;
    }

    /**
     * getClientAddress
     * 
     * @return
     */
    public String getClientAddress() {
        return request != null ? getIpAddr(request) : null;
    }

    /**
     * getLocalAddr
     * 
     * @return
     */
    public String getLocalAddr() {
        return request != null ? request.getLocalAddr() : null;
    }

    /**
     * getLocalName
     * 
     * @return
     */
    public String getLocalName() {
        return request != null ? request.getLocalName() : null;
    }

    /**
     * getLocalPort
     * 
     * @return
     */
    public Integer getLocalPort() {
        return request != null ? request.getLocalPort() : null;
    }

    /**
     * getServerName
     * 
     * @return
     */
    public String getServerName() {
        return request != null ? request.getServerName() : null;
    }

    /**
     * getServerPort
     * 
     * @return
     */
    public Integer getServerPort() {
        return request != null ? request.getServerPort() : null;
    }

    /**
     * getRequestContextPath
     * 
     * @return
     */
    public String getRequestContextPath() {
        return request != null ? request.getContextPath() : null;
    }

    /**
     * getRequestServletPath
     * 
     * @return
     */
    public String getRequestServletPath() {
        return request != null ? request.getServletPath() : null;
    }

    /**
     * getRequestPathInfo
     * 
     * @return
     */
    public String getRequestPathInfo() {
        return request != null ? request.getPathInfo() : null;
    }

    /**
     * getRequestCategory
     * 
     * @return
     */
    public String getRequestCategory() {
        return request != null ? request.getMethod().toUpperCase() : null;
    }

    /**
     * getRequestParameterMap
     * 
     * @return
     */
    public Map<String, String[]> getRequestParameterMap() {
        return request != null ? request.getParameterMap() : null;
    }

}
