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
import org.umeframework.dora.exception.ValidationException;

/**
 * Parse text JSON as java bean object with 'ValidationException' handle logic.<br>
 * 
 * @author Yue MA
 */
@Deprecated
public class JsonParserWithExceptionHandlerImpl extends JsonParserImpl implements AjaxParser<String> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.ajax.AJAXParser#parse(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T parse(String jsonStr, Class<T> clazz, Type genericType, Annotation[] annotations) {
		if (clazz == null) {
			throw new ParserException("No specific class type.");
		}
		// String jsonStr = (String) inData;
		ValidationException ve = new ValidationException();
		T bean = (T) parseObjectFromJson(ve, clazz.getSimpleName(), jsonStr, clazz, genericType, annotations);
		if (ve.size() > 1) {
			throw ve;
		}
		return bean;
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
	protected Object parseObject(ValidationException ve, String name, Object obj, Class<?> clazz, Type genericType, Annotation[] annotations) {
		String elementStr = obj == null ? null : obj.toString();
		Object value = null;
		if (clazz.equals(java.lang.Object.class) && obj instanceof String) {
			value = parseObjectFromJson(ve, name, elementStr, String.class, genericType, annotations);
		} else if (clazz.equals(java.lang.Object.class) && obj instanceof Integer) {
			value = parseObjectFromJson(ve, name, elementStr, Integer.class, genericType, annotations);
		} else if (clazz.equals(java.lang.Object.class) && obj instanceof Long) {
			value = parseObjectFromJson(ve, name, elementStr, Long.class, genericType, annotations);
		} else if (clazz.equals(java.lang.Object.class) && obj instanceof Boolean) {
			value = parseObjectFromJson(ve, name, elementStr, Boolean.class, genericType, annotations);
		} else if (clazz.equals(java.lang.Object.class) && obj instanceof Double) {
			value = parseObjectFromJson(ve, name, elementStr, Double.class, genericType, annotations);
		} else {
			value = parseObjectFromJson(ve, name, elementStr, clazz, genericType, annotations);
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
	protected Object parseObjectFromJson(ValidationException ve, String name, String jsonStr, Class<?> clazz, Type genericType, Annotation[] annotations) {
		Object bean = null;
		if (jsonStr == null) {
			return null;
		}

		try {
			if (isBasicType(clazz)) {
				bean = parseBasic(ve, name, jsonStr, clazz, annotations);
			} else if (isArrayType(clazz)) {
				JSONArray jsonArr = new JSONArray(jsonStr);
				bean = parseArray(ve, name, jsonArr, clazz.getComponentType(), annotations);
			} else if (isListType(clazz)) {
				JSONArray jsonArr = new JSONArray(jsonStr);
				bean = parseList(ve, name, jsonArr, clazz, genericType, annotations);
			} else if (isMapType(clazz)) {
				JSONObject jsonObj = new JSONObject(jsonStr);
				bean = parseMap(ve, name, jsonObj, clazz, genericType, annotations);
			} else if (isBeanType(clazz)) {
				JSONObject jsonObj = new JSONObject(jsonStr);
				bean = parseBean(ve, name, jsonObj, clazz, annotations);
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
	protected Object parseBean(ValidationException ve, String beanName, JSONObject jsonObj, Class<?> beanClazz, Annotation[] beanAnnotations) {
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
						System.err.println("PropertyDescriptor.getPropertyType represent as "
						        + propClazz
						        + " but field's type is "
						        + propClazz);
					}
				} catch (Exception re) {
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
					nestObj = parseBasic(ve, name, jsonObjPropValueStr, propClazz, propAnnotations);
				} else if (isArrayType(propClazz)) {
					JSONArray jsonArrPropValue = jsonObj.getJSONArray(name);
					nestObj = parseArray(ve, name, jsonArrPropValue, propClazz.getComponentType(), propAnnotations);
				} else if (isListType(propClazz)) {
					Type genericType = setter.getGenericParameterTypes()[0];
					JSONArray jsonArrPropValue = jsonObj.getJSONArray(name);
					nestObj = parseList(ve, name, jsonArrPropValue, propClazz, genericType, propAnnotations);
				} else if (isMapType(propClazz)) {
					Type genericType = setter.getGenericParameterTypes()[0];
					JSONObject jsonObjPropValue = jsonObj.getJSONObject(name);
					nestObj = parseMap(ve, name, jsonObjPropValue, propClazz, genericType, propAnnotations);
				} else if (isBeanType(propClazz)) {
					JSONObject jsonObjPropValue = jsonObj.getJSONObject(name);
					nestObj = parseBean(ve, name, jsonObjPropValue, propClazz, propAnnotations);
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
	protected Object parseBasic(ValidationException ve, String name, String strObj, Class<?> clazz, Annotation[] propAnnotations) {

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
			return java.sql.Date.valueOf(strObj);
		} else if (clazz.equals(java.sql.Time.class)) {
			return java.sql.Time.valueOf(strObj);
		} else if (clazz.equals(java.sql.Timestamp.class)) {
			return java.sql.Timestamp.valueOf(strObj);
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
	protected Object parseArray(ValidationException ve, String name, JSONArray jsonArr, Class<?> elementClazz, Annotation[] propAnnotations) {

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
			Object value = parseObject(ve, name + "[" + i + "]", elementObj, elementClazz, null, elementClazz.getAnnotations());
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
	protected Object parseList(
	        ValidationException ve,
	        String name,
	        JSONArray jsonArr,
	        Class<?> listClazz,
	        Type genericType,
	        Annotation[] propAnnotations) {

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
		Type[] actualTypes = getActualTypes(genericType);

		Class<?> elementClazz = null;
		Type elementGenericType = null;
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
			//String elementStr = elementObj == null ? null : elementObj.toString();
			// Object value = parseObject(ve, name + "[" + i + "]", elementStr, elementClazz, elementGenericType, elementClazz.getAnnotations());
			Object value = parseObject(ve, name + "[" + i + "]", elementObj, elementClazz, elementGenericType, elementClazz.getAnnotations());
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
	protected Object parseMap(
	        ValidationException ve,
	        String name,
	        JSONObject jsonObj,
	        Class<?> mapClazz,
	        Type genericType,
	        Annotation[] propAnnotations) {

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

		for (Object jsonObjKey : jsonObj.keySet()) {
			String jsonKeyStr = jsonObjKey.toString();
			Object jsonValue = null;
			try {
				jsonValue = jsonObj.get(jsonKeyStr);
			} catch (JSONException e) {
				throw new ParserException("Failed to get map value of " + jsonObj, e);
			}
			String jsonValueStr = jsonValue == null ? null : jsonValue.toString();
			// parse key
			Object keyObj = parseObjectFromJson(ve, name + "[key]", jsonKeyStr, keyClazz, keyGenericType, keyClazz.getAnnotations());
			// parse value
			Object valueObj = parseObjectFromJson(ve, name + "[value]", jsonValueStr, valueClazz, valueGenericType, valueClazz.getAnnotations());
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
