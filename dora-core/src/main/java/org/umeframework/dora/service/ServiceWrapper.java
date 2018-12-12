/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import java.lang.reflect.Method;
import java.util.Set;

//import org.umeframework.dora.transaction.TransactionRequired;

/**
 * ServiceWrapper
 * 
 * @author Yue MA
 * 
 */
public class ServiceWrapper implements java.io.Serializable {
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = -1506280058787497970L;
    /**
     * serviceId
     */
    private String serviceId;
	/**
     * serviceInstance
     */
    private Object serviceInstance;
    /**
     * serviceClazz
     */
    private Class<?> serviceClazz;
    /**
     * serviceMethod
     */
    private Method serviceMethod;
    /**
     * httpRequestMethodSet
     */
    private Set<String> httpRequestMethodSet;
    /**
     * authenticate flag
     */
    private boolean authenticate = true;
    /**
     * disable flag
     */
    private boolean disable = false;

    /**
     * WebServiceProxy
     * 
     * @param serviceInstance
     * @param serviceMethod
     */
    public ServiceWrapper(
            Object serviceInstance,
            Method serviceMethod) {
        this.serviceInstance = serviceInstance;
        this.serviceMethod = serviceMethod;
        this.serviceClazz = serviceInstance.getClass();
    }

//    /**
//     * required transaction judge
//     * 
//     * @return
//     */
//    public boolean isTransactional() {
//        TransactionRequired tx = serviceMethod.getAnnotation(TransactionRequired.class);
//        return tx != null;
//    }

    /**
     * getServiceMethod
     * 
     * @return
     */
    public Method getServiceMethod() {
        return serviceMethod;
    }

    /**
     * getServiceInstance
     * 
     * @return
     */
    public Object getServiceInstance() {
        return serviceInstance;
    }

    /**
     * @return the authenticate
     */
    public boolean isAuthenticate() {
        return authenticate;
    }

    /**
     * @param authenticate the authenticate to set
     */
    public void setAuthenticate(
            boolean authenticate) {
        this.authenticate = authenticate;
    }

    /**
     * @return the disable
     */
    public boolean isDisable() {
        return disable;
    }

    /**
     * @param disable the disable to set
     */
    public void setDisable(
            boolean disable) {
        this.disable = disable;
    }

    /**
     * @return the serviceClazz
     */
    public Class<?> getServiceClazz() {
        return serviceClazz;
    }

    /**
     * @return the httpRequestMethodSet
     */
    public Set<String> getHttpRequestMethodSet() {
        return httpRequestMethodSet;
    }

    /**
     * @param httpRequestMethodSet the httpRequestMethodSet to set
     */
    public void setHttpRequestMethodSet(Set<String> httpRequestMethodSet) {
        this.httpRequestMethodSet = httpRequestMethodSet;
    }

    /**
     * @return the serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId the serviceId to set
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }


}
