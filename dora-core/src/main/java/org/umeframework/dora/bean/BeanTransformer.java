/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Convert value between bean instances.<br>
 * 
 * @author Yue MA
 */
abstract public class BeanTransformer {
    /**
     * Cache of bean property descriptors.
     */
    private static final Map<Class<?>, Map<String, PropertyDescriptor>> beanInfos = new HashMap<Class<?>, Map<String, PropertyDescriptor>>();

    /**
     * Convert source bean list to target bean list.<br>
     * Null return while input is null.<br>
     * 
     * @param sources
     *            - input bean list
     * @param clazz
     *            - input bean type
     * @param startIndex
     *            - convert start index
     * @param maxLimit
     *            - max convert size
     * @return - output bean list after convert
     */
    public static <T> List<T> fromBeanList(
            List<?> sources,
            Class<T> clazz,
            int startIndex,
            int maxLimit) {
        if (sources == null) {
            return null;
        }
        List<T> targets = new ArrayList<T>(maxLimit == Integer.MAX_VALUE ? sources.size() : maxLimit);
        for (int i = 0; i < sources.size(); i++) {
            if (i >= startIndex && i < startIndex + maxLimit) {
                T target = fromBean(sources.get(i), clazz);
                targets.add(target);
            }
        }
        return targets;
    }

    /**
     * Convert source bean array to target bean array.<br>
     * Null return while input is null.<br>
     * 
     * @param sources
     *            - input bean array
     * @param clazz
     *            - input bean type
     * @param startIndex
     *            - convert start index
     * @param maxLimit
     *            - max convert size
     * @return - output bean array after convert
     */
    public static <T> Object fromBeanArray(
            Object sources,
            Class<T> clazz,
            int startIndex,
            int maxLimit) {
        if (sources == null) {
            return null;
        }
        int length = Array.getLength(sources);
        Object targets = Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            if (i >= startIndex && i < startIndex + maxLimit) {
                T target = fromBean(Array.get(sources, i), clazz);
                Array.set(targets, i, target);
            }
        }
        return targets;
    }

    /**
     * Convert source bean array to target bean array.<br>
     * Null return while input is null.<br>
     * 
     * @param sources
     *            - input bean array
     * @param clazz
     *            - input bean type
     * @return - output bean array after convert
     */
    public static <T> Object fromBeanArray(
            Object sources,
            Class<T> clazz) {
        if (sources == null) {
            return null;
        }
        return fromBeanArray(sources, clazz, 0, Array.getLength(sources));
    }

    /**
     * Convert source bean list to target bean list.<br>
     * Null return while input is null.<br>
     * 
     * @param sources
     *            - input bean list
     * @param clazz
     *            - input bean type
     * @return - output bean list after convert
     */
    public static <T> List<T> fromBeanList(
            List<?> sources,
            Class<T> clazz) {
        if (sources == null) {
            return null;
        }
        return fromBeanList(sources, clazz, 0, sources.size());
    }

    /**
     * Convert source bean to target bean.<br>
     * Null return while input is null.<br>
     * 
     * @param source
     *            - input bean
     * @param clazz
     *            - input bean type
     * @return - output bean after convert
     */
    public static <T> T fromBean(
            Object source,
            Class<T> clazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = clazz.newInstance();
            populateObjectALL(target, source);
            return target;
        } catch (Exception e) {
            throw new BeanException("Fail to transform bean properties " + clazz, e);
        }
    }

    /**
     * Auto copy bean property between source bean and target bean.<br>
     * No update to target property while no found same property in source bean.<br>
     * 
     * @param target
     *            - output bean
     * @param source
     *            - input bean
     */
    public static void populateObject(
            Object target,
            Object source) {
        Collection<PropertyDescriptor> targetProerties = getBeanProperties(target.getClass());
        Iterator<PropertyDescriptor> itr = targetProerties.iterator();
        while (itr.hasNext()) {
            PropertyDescriptor tarProp = itr.next();
            if (tarProp.getWriteMethod() == null) {
                continue;
            }
            PropertyDescriptor srcProp = getBeanProperty(source.getClass(), tarProp.getName());
            if (srcProp != null && srcProp.getReadMethod() != null) {
                if (tarProp.getPropertyType().equals(srcProp.getPropertyType())) {
                    try {
                        Object value = srcProp.getReadMethod().invoke(source, new Object[0]);
                        tarProp.getWriteMethod().invoke(target, new Object[] { value });
                    } catch (Exception e) {
                        throw new BeanException("Fail to transform bean properties during populate " + source + "," + target, e);
                    }
                }
            }
        }
    }
    
    /**
     * Auto copy bean property between source bean and target bean.<br>
     * No update to target property while no found same property in source bean.<br>
     * 
     * @param target
     *            - output bean
     * @param source
     *            - input bean
     */
    public static void populateObject(
            Object target,
            Object source,
            boolean ignoreNullValue) {
        Collection<PropertyDescriptor> targetProerties = getBeanProperties(target.getClass());
        Iterator<PropertyDescriptor> itr = targetProerties.iterator();
        while (itr.hasNext()) {
            PropertyDescriptor tarProp = itr.next();
            if (tarProp.getWriteMethod() == null) {
                continue;
            }
            PropertyDescriptor srcProp = getBeanProperty(source.getClass(), tarProp.getName());
            if (srcProp != null && srcProp.getReadMethod() != null) {
                if (tarProp.getPropertyType().equals(srcProp.getPropertyType())) {
                    try {
                        Object value = srcProp.getReadMethod().invoke(source, new Object[0]);
                        if (ignoreNullValue && value != null) {
                            tarProp.getWriteMethod().invoke(target, new Object[] { value });
                        }
                    } catch (Exception e) {
                        throw new BeanException("Fail to transform bean properties during populate " + source + "," + target, e);
                    }
                }
            }
        }
    }


    /**
     * Auto copy bean property between source bean and target bean.<br>
     * Set target property with null while no found same property in source
     * bean.<br>
     * 
     * @param target
     *            - output bean
     * @param source
     *            - input bean
     */
    public static void populateObjectALL(
            Object target,
            Object source) {
        Collection<PropertyDescriptor> targetProerties = getBeanProperties(target.getClass());
        Iterator<PropertyDescriptor> itr = targetProerties.iterator();
        while (itr.hasNext()) {
            PropertyDescriptor tarProp = itr.next();
            if (tarProp.getWriteMethod() == null) {
                continue;
            }
            PropertyDescriptor srcProp = getBeanProperty(source.getClass(), tarProp.getName());
            if (srcProp != null && srcProp.getReadMethod() != null) {
                if (tarProp.getPropertyType().equals(srcProp.getPropertyType())) {
                    try {
                        Object value = srcProp.getReadMethod().invoke(source, new Object[0]);
                        tarProp.getWriteMethod().invoke(target, new Object[] { value });
                    } catch (Exception e) {
                        throw new BeanException("Fail to transform bean properties during populate " + source + "," + target, e);
                    }
                } else {
                    try {
                        Object srcValue = srcProp.getReadMethod().invoke(source, new Object[0]);
                        if (srcValue instanceof Object[]) {
                            Object value = fromBeanArray(srcValue, tarProp.getPropertyType().getComponentType());
                            tarProp.getWriteMethod().invoke(target, new Object[] { value });
                        } else {
                            Object value = fromBean(srcValue, tarProp.getPropertyType());
                            tarProp.getWriteMethod().invoke(target, new Object[] { value });
                        }

                    } catch (Exception e) {
                        throw new BeanException("Fail to transform bean properties during populate " + source + "," + target, e);
                    }

                }
            }
        }
    }

    /**
     * Get bean property:value into map instance.<br>
     * 
     * @param obj
     *            - input bean
     * @return map instance of property:value pairs
     */
    public static HashMap<String, Object> getObjectFields(
            Object obj) {

        HashMap<String, Object> resultMap = null;
        if (obj != null) {
            resultMap = new HashMap<String, Object>();
            Collection<PropertyDescriptor> targetProerties = getBeanProperties(obj.getClass());
            Iterator<PropertyDescriptor> itr = targetProerties.iterator();
            while (itr.hasNext()) {
                PropertyDescriptor tarProp = itr.next();
                if (tarProp.getReadMethod() == null) {
                    continue;
                }
                try {
                    resultMap.put(tarProp.getName(), tarProp.getReadMethod().invoke(obj, new Object[0]));
                } catch (Exception e) {
                    throw new BeanException("Fail to transform bean properties " + obj, e);
                }
            }
        }

        return resultMap;
    }

    /**
     * Get bean property descriptor list.<br>
     * 
     * @param clazz
     * @return
     */
    private static Collection<PropertyDescriptor> getBeanProperties(
            Class<?> clazz) {
        if (!beanInfos.containsKey(clazz)) {
            initBeanInfo(clazz);
        }
        return beanInfos.get(clazz).values();
    }

    /**
     * Get bean property value by name.<br>
     * 
     * @param clazz
     *            - type instance
     * @param propName
     *            - property name
     * @return property value
     */
    private static PropertyDescriptor getBeanProperty(
            Class<?> clazz,
            String propName) {
        if (!beanInfos.containsKey(clazz)) {
            initBeanInfo(clazz);
        }
        return beanInfos.get(clazz).get(propName);
    }

    /**
     * Load bean descriptor into cache.<br>
     * 
     * @param clazz
     *            - type instance
     */
    private static void initBeanInfo(
            Class<?> clazz) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            Map<String, PropertyDescriptor> propertiesMap = new HashMap<String, PropertyDescriptor>(beanInfo.getPropertyDescriptors().length);
            for (PropertyDescriptor propDesc : beanInfo.getPropertyDescriptors()) {
                propertiesMap.put(propDesc.getName(), propDesc);
            }
            beanInfos.put(clazz, propertiesMap);
        } catch (IntrospectionException e) {
        }
    }
}
