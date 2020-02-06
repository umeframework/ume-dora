/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao.jdbc;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

/**
 * BeanHelper<br>
 * 
 * @author Yue MA
 */
@Component
public class BeanHelper {
    /**
     * Bean descriptor cacher
     */
    private final Map<Class<?>, BeanReflectInfoInfo> beanReflectInfoMap = new ConcurrentHashMap<>();

    /**
     * Clear bean descriptor cache.
     */
    public synchronized void clearCache() {
        beanReflectInfoMap.clear();
    }

    /**
     * Add bean descriptor into cache.
     * 
     * @param clazz
     */
    public synchronized void addToCache(Class<?> clazz) {
        BeanReflectInfoInfo reflectInfo = new BeanReflectInfoInfo();
        Map<String, Method> setters = new LinkedHashMap<>();
        Map<String, Method> getters = new LinkedHashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propDescs = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propDesc : propDescs) {
                if (propDesc.getWriteMethod() != null) {
                    setters.put(propDesc.getName(), propDesc.getWriteMethod());
                }
                if (propDesc.getReadMethod() != null) {
                    getters.put(propDesc.getName(), propDesc.getReadMethod());
                }
            }

            reflectInfo.setCachedTime(System.currentTimeMillis());
            reflectInfo.setGetters(getters);
            reflectInfo.setSetters(setters);

            beanReflectInfoMap.put(clazz, reflectInfo);
        } catch (IntrospectionException e) {
            // Ignore
        }
    }

    /**
     * Set property value to bean instance
     * 
     * @param bean
     *            - bean instance
     * @param property
     *            - property name
     * @param value
     *            - property value
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws BeanHelperException
     */
    @SuppressWarnings({
        "unchecked"
    })
    public void setBeanProperty(Object bean, String property, Object value) {
            if (bean instanceof Map) {
                ((Map<String, Object>) bean).put(property, value);
            } else {
                Method method = getSetters(bean.getClass()).get(property);
                try {
                    method.invoke(bean, value);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new BeanHelperException("Fail to set bean property " + property + " of " + bean.getClass().getSimpleName(), e);
                }
            }
    }
    
    /**
     * invokeMethod
     * 
     * @param bean
     * @param methodName
     * @param values
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T invokeMethod(Object bean, String methodName, Object... values) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (bean != null) {
            Class<?>[] parameterTypes = new Class<?>[values.length];
            for (int i = 0; i < values.length; i++) {
                if(values[i]!=null){
                    if(values[i].getClass()==HashMap.class){
                        parameterTypes[i]=Map.class;
                    }else if(values[i].getClass()==ArrayList.class){
                        parameterTypes[i]=List.class;
                    }else{
                        parameterTypes[i]=values[i].getClass();
                    }
                }else{
                    parameterTypes[i]=null;
                }
            }
            Method method = bean.getClass().getDeclaredMethod(methodName, parameterTypes);
            return (T) method.invoke(bean, values);
        }
        return null;
    }

    /**
     * Get bean property
     * 
     * @param bean
     *            - bean instance
     * @param property
     *            - property name
     * @return property value
     * @throws BeanHelperException
     */
    @SuppressWarnings("unchecked")
    public Object getBeanProperty(Object bean, String property) {

        Object value = null;
        try {
            if (bean instanceof Map) {
                value = ((Map<String, Object>) bean).get(property);
            } else {
                Method method = getGetters(bean.getClass()).get(property);
                value = method.invoke(bean);
            }
        } catch (Exception e) {
            throw new BeanHelperException("Fail to get bean property " + property + " of " + bean.getClass().getSimpleName(), e);
        }
        return value;
    }

    /**
     * Get bean property data type
     * 
     * @param bean
     *            - bean instance
     * @param property
     *            - property name
     * @return property data type
     * @throws BeanHelperException
     */
    public Class<?> getBeanPropertyType(Object bean, String property) {
        try {
            Class<?> type = null;
            if (bean instanceof DynaBean) {
                DynaProperty descriptor = ((DynaBean) bean).getDynaClass().getDynaProperty(property);
                if (descriptor != null) {
                    type = descriptor.getType();
                }
            } else {
                type = PropertyUtils.getPropertyType(bean, property);
            }
            return type;
        } catch (Exception e) {
            throw new BeanHelperException("Fail to get bean property type " + property + " of " + bean.getClass().getSimpleName(), e);
        }
    }

    /**
     * Get bean setters as Map instance
     * 
     * @param clazz
     *            - bean class
     * @return setters
     */
    public Map<String, Method> getSetters(Class<?> clazz) {
        if (!beanReflectInfoMap.containsKey(clazz)) {
            addToCache(clazz);
        }
        BeanReflectInfoInfo data = beanReflectInfoMap.get(clazz);
        return data.getSetters();
    }

    /**
     * Get bean getters as Map instance
     * 
     * @param clazz
     *            - bean class
     * @return getters
     */
    public Map<String, Method> getGetters(Class<?> clazz) {
        if (!beanReflectInfoMap.containsKey(clazz)) {
            addToCache(clazz);
        }
        BeanReflectInfoInfo data = beanReflectInfoMap.get(clazz);
        return data.getGetters();
    }

    /**
     * Get bean properties
     * 
     * @param bean
     *            - bean instance
     * @param getters
     *            - getter methods Map
     * @return values Map
     * @throws Exception
     */
    public Map<String, Object> getBeanProperties(Object bean, Map<String, Method> getters) {
        Map<String, Object> props = new LinkedHashMap<>();

        Class<?> clazz = bean.getClass();
        if (getters == null) {
            getters = getGetters(clazz);
        }
        for (Map.Entry<String, Method> entry : getters.entrySet()) {
            String prop = entry.getKey();
            Method getter = entry.getValue();
            Object value;
            try {
                value = getter.invoke(bean);
            } catch (Exception e) {
                throw new BeanHelperException(e.getMessage(), e);
            }
            props.put(prop, value);
        }

        return props;
    }

    /**
     * Set bean properties
     * 
     * @param bean
     *            - bean instance
     * @param setters
     *            - getter methods Map
     * @param values
     *            - values Map
     * @throws Exception
     */
    public void setBeanProperties(Object bean, Map<String, Method> setters, Map<String, ? extends Object> values) {
        Class<?> clazz = bean.getClass();
        if (setters == null) {
            setters = getSetters(clazz);
        }

        for (Map.Entry<String, ?> entry : values.entrySet()) {
            String prop = entry.getKey();
            Object value = entry.getValue();
            Method setter = setters.get(prop);
            if (setter != null) {
                if (value == null) {
                    try {
                        setter.invoke(bean);
                    } catch (Exception e) {
                        throw new BeanHelperException("Setter invoke error, property=" + prop + ",setter=" + setter.getName()+",value=" +value , e);
                    }
                } else {
                    Class<?> setterParamType = setter.getParameterTypes()[0];
                    if (!value.getClass().equals(setterParamType)) {
                        value = convertValueType(value, value.getClass(), setterParamType);
                    }
                    if (setterParamType.isInstance(value)) {
                        try {
                            setter.invoke(bean, value);
                        } catch (Exception e) {
                            throw new BeanHelperException("Setter invoke error, property=" + prop + ",setter=" + setter.getName()+",value=" +value , e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Convert property values
     * 
     * @param fromValue
     *            - source Object before convert
     * @param fromType
     *            - source Object type
     * @param toType
     *            - target Object type
     * @return target Object
     */
    public Object convertValueType(Object fromValue, Class<?> fromType, Class<?> toType) {
        if (fromValue == null || fromType == null || toType == null) {
            return fromValue;
        }
        if (toType.equals(String.class) && !fromType.equals(String.class)) {
            // Non String Type→String Type
            return StringUtil.objectToStr(fromValue);
        }
        if (fromType.equals(String.class) && !toType.equals(String.class)) {
            // String Type→Non String Type
            return StringUtil.toObject((String) fromValue, toType);
        }

        if (!toType.equals(String.class) && fromType.equals(BigDecimal.class)) {
            // BigDecimal Type→Non String Type
            if (toType.equals(BigInteger.class)) {
                // BigDecimal Type→BigInteger Type
                return BigInteger.valueOf(((BigDecimal) fromValue).longValue());
            } else if (toType.equals(Integer.class)) {
                // BigDecimal Type→Integer Type
                return ((BigDecimal) fromValue).intValue();
            } else if (toType.equals(Long.class)) {
                return ((BigDecimal) fromValue).longValue();
            } else if (toType.equals(Short.class)) {
                // BigDecimal Type→Short Type
                return ((BigDecimal) fromValue).shortValue();
            } else if (toType.equals(Double.class)) {
                // BigDecimal Type→Double Type
                return ((BigDecimal) fromValue).doubleValue();
            } else if (toType.equals(Float.class)) {
                // BigDecimal Type→Float Type
                return ((BigDecimal) fromValue).floatValue();
            } else {
                return fromValue;
            }
        } else if (toType.equals(BigDecimal.class) && fromValue instanceof Number) {
            // Number Type→BigDecimal Type
            return new BigDecimal(fromValue.toString());
        } else if (!toType.equals(String.class) && fromType.equals(BigInteger.class)) {
            // BigInteger Type→Non String Type
            if (toType.equals(Integer.class)) {
                // BigInteger Type→Integer Type
                return ((BigInteger) fromValue).intValue();
            } else if (toType.equals(Long.class)) {
                // BigInteger Type→Long Type
                return ((BigInteger) fromValue).longValue();
            } else if (toType.equals(Short.class)) {
                // BigInteger Type→Short Type
                return (short) (((BigInteger) fromValue).intValue());
            } else if (toType.equals(Double.class)) {
                // BigInteger Type→Double Type
                return ((BigInteger) fromValue).doubleValue();
            } else if (toType.equals(Float.class)) {
                // BigInteger Type→Float Type
                return ((BigInteger) fromValue).floatValue();
            } else {
                return fromValue;
            }
        } else if (toType.equals(BigInteger.class) && (fromType.equals(Integer.class) || fromType.equals(Long.class) || fromType
            .equals(Short.class))) {
            // Integer Type→BigInteger Type
            return new BigInteger(fromValue.toString());
        } else {
            return fromValue;
        }
    }

    /**
     * Load bean class by class name
     * 
     * @param className
     * @return
     * @throws BeanHelperException
     */
    public Class<?> getClass(String className) {
        Class<?> clazz = null;
        Thread t = Thread.currentThread();
        ClassLoader cl = t.getContextClassLoader();
        try {
            clazz = cl.loadClass(className);
        } catch (Exception e) {
            throw new BeanHelperException("Fail to load bean class " + className, e);
        }
        return clazz;
    }

    /**
     * Create bean instance by class name and constructor parameters
     * 
     * @param clazz
     *            - bean class
     * @param constructorParameter
     *            - constructor parameters
     * @return bean instance
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws BeanHelperException
     */
    public <T> T createBean(Class<T> clazz, Object... constructorParameter) {
        if (constructorParameter == null) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new BeanHelperException("Fail to create bean instance " + clazz, e);
            }
        }
        try {
            Class<?>[] constructorParameterType = new Class<?>[constructorParameter.length];
            for (int i = 0; i < constructorParameter.length; i++) {
                constructorParameterType[i] = constructorParameter[i] != null ? constructorParameter[i].getClass() :  null;
            }
            Constructor<T> constructor = clazz.getConstructor(constructorParameterType);
            return constructor.newInstance(constructorParameter);
        } catch (Exception e) {
            throw new BeanHelperException("Fail to get bean instance " + clazz, e);
        }
    }

    /**
     * Convert Map's key-value pair to bean instance
     * 
     * @param mapObject
     * @param clazz
     * @return
     */
    public <T> T mapToBean(Map<String, ? extends Object> mapObject, Class<T> clazz) {
        T bean = createBean(clazz);
        Map<String, Method> setters = getSetters(bean.getClass());
        try {
            setBeanProperties(bean, setters, mapObject);
        } catch (Exception e) {
            bean = null;
        }
        return bean;
    }

    /**
     * @param bean
     * @return
     * @throws Exception
     */
    public <T> Map<String, Object> beanToMap(T bean) {
        if (bean == null) {
            return null;
        }

        Map<String, Method> getters = getGetters(bean.getClass());
        try {
            return getBeanProperties(bean, getters);
        } catch (Exception e) {
            throw new BeanHelperException(e.getMessage(), e);
        }
    }

    /**
     * beanToBean
     * 
     * @param from
     * @param to
     * @param fieldMapping
     * @param valueConvertors
     */
    public <T1, T2> void beanToBean(T1 from, T2 to, Map<String, String> fieldMapping) {
        if (from == null || to == null) {
            return;
        }
        Map<String, Method> getters = getGetters(from.getClass());
        Map<String, Method> setters = getSetters(to.getClass());
        for (Map.Entry<String, Method> entry : getters.entrySet()) {
            String fromName = entry.getKey();
            String toName = fromName;
            if (fieldMapping != null && fieldMapping.containsKey(fromName)) {
                toName = fieldMapping.get(fromName);
            }
            Object value = getBeanProperty(from, fromName);
            Method setter = setters.get(toName);
            if (setter != null && value != null) {
                    Class<?> setterParamType = setter.getParameterTypes()[0];
                    if (!value.getClass().equals(setterParamType)) {
                        value=JavaValueConvertor.convert(value,setterParamType);
                    }
                    if (setterParamType.isInstance(value)) {
                        try {
                            setter.invoke(to, value);
                        } catch (Exception e) {
                            throw new BeanHelperException("Error during set data from " + from.getClass() + "," + fromName + "<" + value
                                .getClass() + ":" + value + "> to " + to.getClass() + "." + toName + "<" + setterParamType + ">", e);
                        }
                    }
            }
        }
    }
    
    /**
     * convertBean
     * 
     * @param from
     * @param toType
     * @param fieldMapping
     * @param valueConvertors
     */
    public <T1, T2> T2 convertBean(T1 from, Class<T2> toType, Map<String, String> fieldMapping) {
        T2 to = createBean(toType);
        beanToBean(from, to, fieldMapping);
        return to;
    }

    /**
     * convertBeanList
     * 
     * @param fromList
     * @param toType
     * @param fieldMapping
     * @param valueConvertors
     * @return
     */
    public <T1, T2> List<T2> convertBeanList(List<T1> fromList, Class<T2> toType, Map<String, String> fieldMapping) {
        List<T2> toList = new ArrayList<>();
        for (T1 from : fromList) {
            T2 to = createBean(toType);
            beanToBean(from, to, fieldMapping);
            toList.add(to);
        }
        return toList;
    }

    /**
     * BeanReflectInfoBean
     */
    class BeanReflectInfoInfo {
        /**
         * Class instance
         */
        private Class<?> clazz;
        /**
         * Setter map
         */
        private Map<String, Method> setters;
        /**
         * Getter map
         */
        private Map<String, Method> getters;
        /**
         * Cached time
         */
        private Long cachedTime;

        /**
         * @return the clazz
         */
        public Class<?> getClazz() {
            return clazz;
        }

        /**
         * @param clazz the clazz to set
         */
        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        /**
         * @return the setters
         */
        public Map<String, Method> getSetters() {
            return setters;
        }

        /**
         * @param setters the setters to set
         */
        public void setSetters(Map<String, Method> setters) {
            this.setters = setters;
        }

        /**
         * @return the getters
         */
        public Map<String, Method> getGetters() {
            return getters;
        }

        /**
         * @param getters the getters to set
         */
        public void setGetters(Map<String, Method> getters) {
            this.getters = getters;
        }

        /**
         * @return the cachedTime
         */
        public Long getCachedTime() {
            return cachedTime;
        }

        /**
         * @param cachedTime the cachedTime to set
         */
        public void setCachedTime(Long cachedTime) {
            this.cachedTime = cachedTime;
        }
    }
}
