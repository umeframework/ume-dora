/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.mapping.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.BeansException;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.bean.BeanFactory;
import org.umeframework.dora.exception.SystemException;
import org.umeframework.dora.property.ConfigProperties;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.util.ReflectUtil;

/**
 * Initial web service define information base service define in serviceMapping.properties
 *
 * @author Yue MA
 */
public class ServiceMappingImpl extends BaseComponent implements ServiceMapping {
    /**
     * split char of service ID and request category (method) define
     */
    private static final String SERVICE_ID_REQUEST_CATEGORY_SPLIT = ",";
    /**
     * split char of service multiple request category (method) define
     */
    private static final String SERVICE_MULTI_REQUEST_CATEGORY_SPLIT = "\\|";
    /**
     * split char of service class package
     */
    private static final String SERVICE_PACKAGE_SPLIT = ".";
    /**
     * split char of service properties elements define
     */
    private static final String SERVICE_PROPERTY_SPLIT_CHAR = ",";
    /**
     * split char of service class name and method name
     */
    private static final String SERVICE_CLASS_METHOD_SPLIT = "#";
    /**
     * service define and status management map
     */
    private final Map<String, ServiceWrapper> serviceContainer = new HashMap<String, ServiceWrapper>();

    /**
     * beanFactory
     */
    @Resource(name = BeanConfigConst.DEFAULT_BEAN_FACTORY)
    private BeanFactory beanFactory;

    /**
     * serviceMappingProperties
     */
    @Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING_CONFIG_PROPERTIES)
    private ConfigProperties serviceMappingConfigProperties;

    /**
     * @throws SystemException
     */
    public void init() throws SystemException {
        try {
            // Initialize all service instance define in
            // serviceMappingProperties
            for (String serviceId : serviceMappingConfigProperties.keySet()) {
                String serviceValue = serviceMappingConfigProperties.get(serviceId);
                initServiceMapping(serviceId, serviceValue);
                // ServiceWrapper serviceWrapper = this.getService(serviceId);
                getLogger().info("Initialized service '" + serviceId + "'.");
            }

            getLogger().info("Service mapping initialize successful.");

        } catch (Exception e) {
            getLogger().error("Service mapping initialize failed.", e);
            throw new SystemException(e, "service mapping initialize failed.");
        } finally {
        }
    }

    /**
     * destroy
     */
    public void destroy() {
        try {
            // destroy serviceMapping resources
            serviceContainer.clear();
            getLogger().info("[Service destroy] service mapping destroy successful.");
        } catch (Exception e) {
            getLogger().error("[Service destroy] service mapping destroy failed.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.WebServiceHelper#addWebService(java.lang.String, java.lang.String)
     */
    @Override
    public void addService(String serviceId, String serviceValue) throws SystemException {
        if (!serviceMappingConfigProperties.containsKey(serviceId)) {
            serviceMappingConfigProperties.set(serviceId, serviceValue);
        }
        initServiceMapping(serviceId, serviceValue);
        saveServiceMapping();
    }

    /**
     * initialize service mapping from properties file
     * 
     * @param serviceId
     * @param serviceValue
     * @throws SystemException
     */
    protected void initServiceMapping(String serviceId, String serviceValue) throws SystemException {
        serviceId = serviceId.trim();
        if (!isValidServiceId(serviceId)) {
            getLogger().error("Service mapping initialize error: id contain non-alpha-number char.");
            throw new SystemException("Service mapping initialize error: id contain non-alpha-number char.");
        }
        if (this.serviceContainer.containsKey(serviceId)) {
            getLogger().error("Service mapping initialize error: found duplicate id in service mapping define.");
            throw new SystemException("Service mapping initialize error: found duplicate id in service mapping define.");
        }

        String[] elements = null;
        if (serviceValue.contains(SERVICE_PROPERTY_SPLIT_CHAR)) {
            elements = serviceValue.split(SERVICE_PROPERTY_SPLIT_CHAR);
        } else {
            elements = new String[] { serviceValue.trim() };
        }

        String serviceClassMethod = elements[0];
        String serviceBeanName = serviceClassMethod.split(SERVICE_CLASS_METHOD_SPLIT)[0];
        String serviceMethodName = serviceClassMethod.split(SERVICE_CLASS_METHOD_SPLIT)[1];

        Class<?> serviceClazz = null;
        Method serviceMethod = null;
        Object serviceInstance = null;
        if (!serviceBeanName.contains(SERVICE_PACKAGE_SPLIT)) {
            try {
                serviceInstance = beanFactory.getBean(serviceBeanName);
                serviceClazz = serviceInstance.getClass();
            } catch (BeansException e) {
                throw new SystemException(e, "No found bean instance in factory:" + serviceBeanName);
            }
        } else {
            try {
                serviceClazz = Thread.currentThread().getContextClassLoader().loadClass(serviceBeanName);
                serviceInstance = beanFactory.autowireCapableCreateBean(serviceClazz);
            } catch (Exception e) {
                throw new SystemException(e, "Failed in create instance for service class:" + serviceBeanName);
            }
        }
        serviceMethod = ReflectUtil.getNonBridgeMethod(serviceClazz, serviceMethodName);
        if (serviceMethod == null) {
            throw new SystemException("No found service method:" + serviceBeanName + "." + serviceMethodName);
        }

        ServiceWrapper serviceWrapper = new ServiceWrapper(serviceInstance, serviceMethod);

        if (elements.length > 1) {
            if (elements[1].trim().equalsIgnoreCase("false")) {
                serviceWrapper.setAuthenticate(false);
            }
            if (elements[1].trim().equalsIgnoreCase("disable")) {
                serviceWrapper.setDisable(true);
            }
        }
        if (elements.length > 2) {
            if (elements[2].trim().equalsIgnoreCase("false")) {
                serviceWrapper.setAuthenticate(false);
            }
            if (elements[2].trim().equalsIgnoreCase("disable")) {
                serviceWrapper.setDisable(true);
            }
        }

        Set<String> httpRequestMethodSet = new HashSet<>();
        if (serviceId.contains(SERVICE_ID_REQUEST_CATEGORY_SPLIT)) {
            String[] idAndReqCategories = serviceId.split(SERVICE_ID_REQUEST_CATEGORY_SPLIT);
            serviceId = idAndReqCategories[0].trim();
            String[] reqCategories = idAndReqCategories[1].split(SERVICE_MULTI_REQUEST_CATEGORY_SPLIT);
            for (String e : reqCategories) {
                e = e.trim().toUpperCase();
                httpRequestMethodSet.add(e);
            }
        } else {
            httpRequestMethodSet.add("POST");
            httpRequestMethodSet.add("GET");
        }
        serviceWrapper.setServiceId(serviceId);
        serviceWrapper.setHttpRequestMethodSet(httpRequestMethodSet);
        serviceContainer.put(serviceId, serviceWrapper);
    }

    /**
     * isValidServiceId
     * 
     * @param serviceId
     * @return
     */
    protected boolean isValidServiceId(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            return false;
        }
        String regexp = "^([0-9]|[a-z]|[A-Z]|-|_|,|\\|)*$";
        if (!GenericValidator.matchRegexp(serviceId, regexp)) {
            return false;
        }
        return true;
    }

    /**
     * Save service mapping into properties file
     */
    protected void saveServiceMapping() {
        try {
            serviceMappingConfigProperties.save();
        } catch (Exception e) {
            getLogger().error("[Service save] service mapping save failed.", e);
        } finally {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.WebServiceHelper#getWebServiceProxy(java.lang.String)
     */
    @Override
    public ServiceWrapper getService(String id) {
        return serviceContainer.get(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.WebServiceHelper#enableWebService(java.lang.String)
     */
    @Override
    public void enableService(String serviceId) {
        this.getService(serviceId).setDisable(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.WebServiceHelper#disableWebService(java.lang.String)
     */
    @Override
    public void disableService(String serviceId) {
        this.getService(serviceId).setDisable(true);
    }

    /**
     * @return the beanFactory
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * @param beanFactory
     *            the beanFactory to set
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * @return the serviceMappingConfigProperties
     */
    public ConfigProperties getServiceMappingConfigProperties() {
        return serviceMappingConfigProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.mapping.ServiceMapping#serviceSet()
     */
    @Override
    public Set<String> serviceSet() {
        return this.serviceContainer.keySet();
    }
}
