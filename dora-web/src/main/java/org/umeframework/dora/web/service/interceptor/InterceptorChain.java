/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.service.interceptor;

import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.umeframework.dora.service.ServiceResponse;

/**
 * Interceptor Chain interface declare
 * 
 * @author Yue MA
 */
@Deprecated
public interface InterceptorChain {
    /**
     * Move to next intercept instance for process
     */
    void next() throws Throwable;

    /**
     * get system ID
     */
    String getSysId();

    /**
     * set system ID
     * 
     * @param sysId
     */
    void setSysId(
            String sysId);

    /**
     * get ServletContext
     */
    ServletContext getContext();

    /**
     * get HttpServletRequest
     */
    HttpServletRequest getRequest();

    /**
     * get HttpServletResponse
     */
    HttpServletResponse getResponse();

    /**
     * get Locale
     */
    Locale getLocale();

    /**
     * set ServletContext
     * 
     * @param context
     */
    void setContext(
            ServletContext context);

    /**
     * set HttpServletRequest
     * 
     * @param request
     */
    void setRequest(
            HttpServletRequest request);

    /**
     * set HttpServletResponse
     * 
     * @param response
     */
    void setResponse(
            HttpServletResponse response);

    /**
     * set Locale
     * 
     * @param locale
     */
    void setLocale(
            Locale locale);

    /**
     * get Service ID
     */
    String getServiceId();

    /**
     * set Service ID
     * 
     * @param serviceId
     */
    void setServiceId(
            String serviceId);

    /**
     * get InputData
     */
    String getAjaxInputData();

    /**
     * set InputData
     * 
     * @param inputData
     */
    void setAjaxInputData(
            String inputData);

    /**
     * get OutputData
     */
    String getAjaxOutputData();

    /**
     * set OutputData
     * 
     * @param outputData
     */
    void setAjaxOutputData(
            String outputData);

    /**
     * get Service Input Parameters
     */
    Object[] getServiceInputParams();

    /**
     * set Service Input Parameters
     * 
     * @param serviceParams
     */
    void setServiceInputParams(
            Object[] serviceParams);

    /**
     * get ServiceResponse object
     */
    ServiceResponse<Object> getServiceResponse();

    /**
     * get ServiceResponse object
     * 
     * @param serviceResponse
     */
    void setServiceResponse(
            ServiceResponse<Object> serviceResponse);

    /**
     * get Interceptor List
     */
    List<Interceptor> getInterceptorList();

    /**
     * set Interceptor List
     * 
     * @param interceptorList
     */
    void setInterceptorList(
            List<Interceptor> interceptorList);

}
