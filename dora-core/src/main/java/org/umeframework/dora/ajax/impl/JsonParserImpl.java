/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax.impl;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;
import org.umeframework.dora.ajax.AjaxParser;
import org.umeframework.dora.ajax.ParserException;
import org.umeframework.dora.bean.BeanUtil;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * Parse text JSON as java bean object.<br>
 * 
 * @author Yue MA
 */
@SuppressWarnings("restriction")
public class JsonParserImpl extends JsonSupport implements AjaxParser<String> {
    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ajax.AJAXParser#parse(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[])
     */
    @Override
    public <T> T parse(String jsonStr, Class<T> clazz, Type... actualTypeArgs) {
        ParameterizedType parameterizedType = null;
        if (actualTypeArgs != null) {
            parameterizedType = ParameterizedTypeImpl.make(clazz, actualTypeArgs, null);
        }
        return (T) parse(jsonStr, clazz, parameterizedType, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.ajax.AjaxParser#parse(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T parse(String jsonStr, Class<T> clazz, Type genericType, Annotation[] annotations) {
        if (clazz == null) {
            throw new ParserException("No specific class type.");
        }
        return (T) parseObjectFromJson(clazz.getSimpleName(), jsonStr, clazz, genericType, annotations);
    }

    /**
     * parseObject
     * 
     * @param ve
     * @param name
     * @param obj
     * @param clazz
     * @param genericType
     * @param annotations
     * @return
     */
    protected Object parseObject(String name, Object obj, Class<?> clazz, Type genericType, Annotation[] annotations) {
        String elementStr = obj == null ? null : obj.toString();
        Object value = null;
        if (clazz.equals(java.lang.Object.class) && obj instanceof String) {
            value = parseObjectFromJson(name, elementStr, String.class, genericType, annotations);
        } else if (clazz.equals(java.lang.Object.class) && obj instanceof Integer) {
            value = parseObjectFromJson(name, elementStr, Integer.class, genericType, annotations);
        } else if (clazz.equals(java.lang.Object.class) && obj instanceof Long) {
            value = parseObjectFromJson(name, elementStr, Long.class, genericType, annotations);
        } else if (clazz.equals(java.lang.Object.class) && obj instanceof Boolean) {
            value = parseObjectFromJson(name, elementStr, Boolean.class, genericType, annotations);
        } else if (clazz.equals(java.lang.Object.class) && obj instanceof Double) {
            value = parseObjectFromJson(name, elementStr, Double.class, genericType, annotations);
        } else {
            value = parseObjectFromJson(name, elementStr, clazz, genericType, annotations);
        }
        return value;
    }

    /**
     * parseObjectFromJson
     * 
     * @param ve
     * @param name
     * @param jsonStr
     * @param clazz
     * @param genericType
     * @param annotations
     * @return
     */
    protected Object parseObjectFromJson(String name, String jsonStr, Class<?> clazz, Type genericType, Annotation[] annotations) {
        Object bean = null;
        if (jsonStr == null) {
            return null;
        }

        try {
            if (isBasicType(clazz)) {
                bean = parseBasic(name, jsonStr, clazz, annotations);
            } else if (isArrayType(clazz)) {
                JSONArray jsonArr = new JSONArray(jsonStr);
                bean = parseArray(name, jsonArr, clazz.getComponentType(), annotations);
            } else if (isListType(clazz)) {
                JSONArray jsonArr = new JSONArray(jsonStr);
                bean = parseList(name, jsonArr, clazz, genericType, annotations);
            } else if (isMapType(clazz)) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                bean = parseMap(name, jsonObj, clazz, genericType, annotations);
            } else if (isBeanType(clazz)) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                bean = parseBean(name, jsonObj, clazz, annotations);
            } else {
                bean = null;
            }

        } catch (JSONException e) {
            throw new ParserException("Fail to create JSONObject:" + jsonStr + "," + clazz, e);
        }
        return bean;
    }

    /**
     * parseBean
     * 
     * @param jsonObj
     * @param beanClazz
     * @param validationException
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws JSONException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    protected Object parseBean(String beanName, JSONObject jsonObj, Class<?> beanClazz, Annotation[] beanAnnotations) {
        if (beanClazz == null) {
            throw new ParserException("No specific class type.");
        }
        if (jsonObj == null) {
            return null;
        }

        Object instance = null;
        try {
            instance = beanClazz.newInstance();
        } catch (Exception e) {
            throw new ParserException("Fail to create instance for class " + beanClazz, e);
        }

        Map<String, Method> setters = BeanUtil.getSetters(beanClazz);
        for (Map.Entry<String, Method> entry : setters.entrySet()) {
            String name = entry.getKey();
            Method setter = entry.getValue();
            if (setter == null || !jsonObj.containsKey(name)) {
                continue;
            }
            Class<?> propClazz = setter.getParameterTypes()[0];
            if (propClazz.equals(Object.class)) {
                Field propField = null;
                try {
                    propField = beanClazz.getDeclaredField(name);
                    if (!propField.getClass().equals(Object.class)) {
                        propClazz = propField.getType();
                        // ignore non-support type and print warning information
                        System.err.println("PropertyDescriptor.getPropertyType represent as " + propClazz + " but field's type is " + propClazz);
                    }
                } catch (Exception re) {
                    // ignore error and print warning information
                    System.err.println("No found field [" + name + "] in " + beanClazz);
                }
            }
            if (Class.class.equals(propClazz)) {
                continue;
            }

            Annotation[] propAnnotations = getAnnotations(beanClazz, name);
            Object nestObj = null;
            try {
                if (jsonObj.get(name) == null) {
                    // Skip case which match to {"name":null} data
                    continue;
                }

                if (isBasicType(propClazz)) {
                    Object jsonObjPropValue = jsonObj.get(name);
                    String jsonObjPropValueStr = jsonObjPropValue == null ? null : jsonObjPropValue.toString();
                    nestObj = parseBasic(name, jsonObjPropValueStr, propClazz, propAnnotations);
                } else if (isArrayType(propClazz)) {
                    JSONArray jsonArrPropValue = jsonObj.getJSONArray(name);
                    nestObj = parseArray(name, jsonArrPropValue, propClazz.getComponentType(), propAnnotations);
                } else if (isListType(propClazz)) {
                    Type genericType = setter.getGenericParameterTypes()[0];
                    JSONArray jsonArrPropValue = jsonObj.getJSONArray(name);
                    nestObj = parseList(name, jsonArrPropValue, propClazz, genericType, propAnnotations);
                } else if (isMapType(propClazz)) {
                    Type genericType = setter.getGenericParameterTypes()[0];
                    JSONObject jsonObjPropValue = jsonObj.getJSONObject(name);
                    nestObj = parseMap(name, jsonObjPropValue, propClazz, genericType, propAnnotations);
                } else if (isBeanType(propClazz)) {
                    JSONObject jsonObjPropValue = jsonObj.getJSONObject(name);
                    nestObj = parseBean(name, jsonObjPropValue, propClazz, propAnnotations);
                } else {
                    nestObj = null;
                }

                setter.invoke(instance, new Object[] { nestObj });

            } catch (Exception e) {
                throw new ParserException("Fail to set value to " + instance + "," + setter, e);
            }

        }

        return instance;
    }

    /**
     * get filed annotation define information from current class and its super class.<br>
     * 
     * @param beanClazz
     * @param fieldName
     * @return
     */
    protected Annotation[] getAnnotations(Class<?> beanClazz, String fieldName) {
        Annotation[] propAnnotations = null;
        Field propField = null;
        try {
            propField = beanClazz.getDeclaredField(fieldName);
        } catch (Exception e) {
            // Catch the exception to continue seeking filed in parent class
        }
        if (propField == null && beanClazz.equals(Object.class)) {
            return getAnnotations(beanClazz.getSuperclass(), fieldName);
        }
        if (propField != null) {
            propAnnotations = propField.getAnnotations();
        }
        return propAnnotations;
    }

    /**
     * parseBasic
     * 
     * @param strObj
     * @param clazz
     * @return
     * @throws ParseException
     */
    protected Object parseBasic(String name, String strObj, Class<?> clazz, Annotation[] propAnnotations) {

        if (clazz == null) {
            throw new ParserException("No specific class type.");
        }
        if (strObj == null) {
            return null;
        }

        if (clazz.equals(String.class)) {
            return strObj;
        } else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
            return strObj.charAt(0);
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return Boolean.valueOf(strObj);
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.valueOf(strObj);
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return Integer.valueOf(strObj);
        } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return Short.valueOf(strObj);
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.valueOf(strObj);
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.valueOf(strObj);
        } else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return Byte.valueOf(strObj);
        } else if (clazz.equals(java.math.BigDecimal.class)) {
            return new java.math.BigDecimal(strObj);
        } else if (clazz.equals(java.math.BigInteger.class)) {
            return new java.math.BigInteger(strObj);
        } else if (clazz.equals(java.sql.Date.class)) {
            try {
                return java.sql.Date.valueOf(strObj);
            } catch (Exception ee) {
                throw new ParserException("Date convertion error:" + strObj + "," + clazz, ee);
            }
        } else if (clazz.equals(java.sql.Time.class)) {
            try {
                return java.sql.Time.valueOf(strObj);
            } catch (Exception ee) {
                throw new ParserException("Time convertion error:" + strObj + "," + clazz, ee);
            }
        } else if (clazz.equals(java.sql.Timestamp.class)) {
            try {
                return getDefaultDatetimeFormat().parse(strObj);
            } catch (ParseException e) {
                try {
                    return java.sql.Timestamp.valueOf(strObj);
                } catch (Exception ee) {
                    throw new ParserException("Timestamp convertion error:" + strObj + "," + clazz, ee);
                }
            }
        } else if (clazz.equals(java.util.Date.class)) {
            try {
                return getDefaultDatetimeFormat().parse(strObj);
            } catch (ParseException e) {
                throw new ParserException("Date convertion error:" + strObj + "," + clazz, e);
            }
        } else {
            throw new ParserException("No support class type:" + strObj + "," + clazz);
        }
    }

    /**
     * parseArray
     * 
     * @param jsonArr
     * @param elementClazz
     * @param validationException
     * @return
     */
    protected Object parseArray(String name, JSONArray jsonArr, Class<?> elementClazz, Annotation[] propAnnotations) {

        if (elementClazz == null) {
            throw new ParserException("No specific class type.");
        }
        if (jsonArr == null) {
            return null;
        }

        Object arrInstance = Array.newInstance(elementClazz, jsonArr.length());
        for (int i = 0; i < jsonArr.length(); i++) {
            // Array parsing
            Object elementObj = jsonArr.get(i);
            Object value = parseObject(name + "[" + i + "]", elementObj, elementClazz, null, elementClazz.getAnnotations());
            Array.set(arrInstance, i, value);
        }
        return arrInstance;
    }

    /**
     * parseList
     * 
     * @param jsonArr
     * @param listClazz
     * @param genericType
     * @param validationException
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object parseList(String name, JSONArray jsonArr, Class<?> listClazz, Type genericType, Annotation[] propAnnotations) {

        if (listClazz == null) {
            throw new ParserException("No specific class type.");
        }
        if (jsonArr == null) {
            return null;
        }
        if (genericType == null) {
            throw new ParserException("No specific generic type of list " + listClazz);
        }

        // get actual type
        Class<?> elementClazz = null;
        Type elementGenericType = null;
        Type[] actualTypes = this.getActualTypes(genericType);
        if (actualTypes[0] instanceof Class) {
            elementClazz = (Class<?>) actualTypes[0];
        } else {
            elementClazz = (Class<?>) ((ParameterizedType) actualTypes[0]).getRawType();
            elementGenericType = ((ParameterizedType) actualTypes[0]);
        }

        Collection<Object> listInstance;
        // create list instance
        if (java.util.List.class.equals(listClazz)) {
            listInstance = new java.util.ArrayList<Object>(jsonArr.size());
        } else if (java.util.Set.class.equals(listClazz)) {
            listInstance = new java.util.HashSet<Object>(jsonArr.size());
        } else if (java.util.Collection.class.equals(listClazz)) {
            listInstance = new java.util.ArrayList<Object>(jsonArr.size());
        } else {
            try {
                listInstance = (Collection) listClazz.newInstance();
            } catch (Exception e) {
                throw new ParserException("Failed to create instance for " + listClazz);
            }
        }

        for (int i = 0; i < jsonArr.length(); i++) {
            Object elementObj = jsonArr.get(i);
            // String elementStr = elementObj == null ? null : elementObj.toString();
            // Object value = parseObject(name + "[" + i + "]", elementStr, elementClazz, elementGenericType, elementClazz.getAnnotations());
            Object value = parseObject(name + "[" + i + "]", elementObj, elementClazz, elementGenericType, elementClazz.getAnnotations());
            listInstance.add(value);
        }

        return listInstance;
    }

    /**
     * parseMap
     * 
     * @param jsonObj
     * @param mapClazz
     * @param genericType
     * @param validationException
     * @return
     */
    protected Object parseMap(String name, JSONObject jsonObj, Class<?> mapClazz, Type genericType, Annotation[] propAnnotations) {

        if (mapClazz == null) {
            throw new ParserException("No specific class type.");
        }
        if (jsonObj == null) {
            return null;
        }
        if (genericType == null) {
            throw new ParserException("No specific generic type of map:" + mapClazz);
        }

        Map<Object, Object> mapInstance;
        // create map instance
        if (java.util.HashMap.class.equals(mapClazz)) {
            mapInstance = new java.util.HashMap<Object, Object>(jsonObj.size());
        } else if (java.util.LinkedHashMap.class.equals(mapClazz)) {
            mapInstance = new java.util.LinkedHashMap<Object, Object>(jsonObj.size());
        } else if (java.util.TreeMap.class.equals(mapClazz)) {
            mapInstance = new java.util.TreeMap<Object, Object>();
        } else if (java.util.Hashtable.class.equals(mapClazz)) {
            mapInstance = new java.util.Hashtable<Object, Object>(jsonObj.size());
        } else {
            mapInstance = new java.util.LinkedHashMap<Object, Object>(jsonObj.size());
        }

        Type[] actualTypes = getActualTypes(genericType);

        Class<?> keyClazz = null;
        Type keyGenericType = null;
        Class<?> valueClazz = null;
        Type valueGenericType = null;
        if (actualTypes[0] instanceof Class) {
            keyClazz = (Class<?>) actualTypes[0];
        } else {
            keyClazz = (Class<?>) ((ParameterizedType) actualTypes[0]).getRawType();
            keyGenericType = ((ParameterizedType) actualTypes[0]);
        }
        if (actualTypes[1] instanceof Class) {
            valueClazz = (Class<?>) actualTypes[1];
        } else {
            valueClazz = (Class<?>) ((ParameterizedType) actualTypes[1]).getRawType();
            valueGenericType = ((ParameterizedType) actualTypes[1]);
        }

        for (Object key : jsonObj.keySet()) {
            // String k = key.toString();
            Object value = null;
            try {
                value = jsonObj.get(key);
            } catch (Exception e) {
                throw new ParserException("Failed to get map value of " + jsonObj, e);
            }
            // String jsonValueStr = v == null ? null : v.toString();
            // parse key
            Object keyObj = parseObject(name + "[key]", key, keyClazz, keyGenericType, keyClazz.getAnnotations());
            // parse value
            Object valueObj = parseObject(name + "[value]", value, valueClazz, valueGenericType, valueClazz.getAnnotations());
            // add to map
            mapInstance.put(keyObj, valueObj);
        }

        return mapInstance;
    }

    /**
     * getActualTypes
     * 
     * @param genericType
     * @return
     */
    public Type[] getActualTypes(Type genericType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        return parameterizedType.getActualTypeArguments();
    }

}
