/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.mapping.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceWrapper;
import org.umeframework.dora.service.mapping.ServiceMapping;
import org.umeframework.dora.service.mapping.ServiceMappingDesc;

/**
 * Service status management.<br>
 *
 * @author Yue MA
 */
public class ServiceMappingDescImpl extends BaseComponent implements ServiceMappingDesc {
    /**
     * serviceMapping
     */
    @Resource(name = BeanConfigConst.DEFAULT_SERVICE_MAPPING)
    private ServiceMapping serviceMapping;
    /**
     * json data render
     */
    @Resource(name = BeanConfigConst.DEFAULT_AJAX_RENDER)
    private AjaxRender<String> ajaxRender;

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.WebServiceHelper#getEnableWebServices()
     */
    @Override
    public String[] getEnableServiceList() {
        Set<String> set = new HashSet<String>();
        for (String id : serviceMapping.serviceSet()) {
            ServiceWrapper sw = serviceMapping.getService(id);
            if (!sw.isDisable()) {
                set.add(id);
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.WebServiceHelper#getDisableWebServices()
     */
    @Override
    public String[] getDisableServiceList() {
        Set<String> set = new HashSet<String>();
        for (String id : serviceMapping.serviceSet()) {
            ServiceWrapper sw = serviceMapping.getService(id);
            if (sw.isDisable()) {
                set.add(id);
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.service.mapping.ServiceStatus#getDesc(java.lang.String[])
     */
    @Override
    public List<Map<String, Object>> getServiceStatus(String... serviceIdList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (serviceIdList == null) {
            Set<String> keySet = this.serviceMapping.serviceSet();
            serviceIdList = keySet.toArray(new String[keySet.size()]);
        }
        for (String serviceId : serviceIdList) {
            Map<String, Object> status = fetchStatus(serviceId);
            list.add(status);
        }
        return list;
    }

    /**
     * Fetch status by service ID.<br>
     * 
     * @param serviceId
     * @return
     */
    protected Map<String, Object> fetchStatus(String serviceId) {
        Map<String, Object> statusDesc = new LinkedHashMap<String, Object>();

        ServiceWrapper serviceRef = serviceMapping.getService(serviceId);
        // Class<?> serviceClazz = serviceRef.getServiceClazz();
        Method serviceMethod = serviceRef.getServiceMethod();
        Class<?>[] paramTypes = serviceMethod.getParameterTypes();
        Type[] genericParamTypes = serviceMethod.getGenericParameterTypes();

        HttpServletRequest request = RequestContext.open().get(HTTP_REQUEST);
        String url = (request != null ? request.getServerName() : null) + ":" + (request != null ? request.getServerPort() : null);
        url = url + (request != null ? request.getServletPath() : null);

        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        url = "http://" + url.substring(0, url.lastIndexOf("/") + 1) + serviceId + "/";
        statusDesc.put("URI", url);
        statusDesc.put("Status", serviceRef.isDisable() ? "Disable" : "Enable");
        statusDesc.put("Method", serviceRef.getHttpRequestMethodSet());
        // basic.put("mapping-to", serviceClazz.getName() + "." + serviceMethod.getName());

        List<String> in = null;
        if (paramTypes.length > 0) {
            in = new ArrayList<>(paramTypes.length);
            for (int i = 0; i < paramTypes.length; i++) {
                // Class<?> clazz = paramTypes[i];
                Type gtype = genericParamTypes[i];
                in.add(gtype.getTypeName());
            }
        }
        statusDesc.put("Input", in);
        statusDesc.put("Output", serviceMethod.getGenericReturnType().getTypeName());
        return statusDesc;
    }

}
