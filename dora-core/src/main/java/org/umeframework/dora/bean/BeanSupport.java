/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.umeframework.dora.contant.BeanConfigConst;

/**
 * BeanSupport
 * 
 * @author Yue MA
 */
public class BeanSupport {

	/**
	 * Basic data type define
	 */
	private static final Set<Class<?>> BASIC_TYPE_DEFINE = new HashSet<Class<?>>();
	static {
		BASIC_TYPE_DEFINE.add(java.lang.String.class);
		BASIC_TYPE_DEFINE.add(java.lang.Integer.class);
		BASIC_TYPE_DEFINE.add(java.lang.Short.class);
		BASIC_TYPE_DEFINE.add(java.lang.Long.class);
		BASIC_TYPE_DEFINE.add(java.lang.Double.class);
		BASIC_TYPE_DEFINE.add(java.lang.Float.class);
		BASIC_TYPE_DEFINE.add(java.lang.Boolean.class);
		BASIC_TYPE_DEFINE.add(java.lang.Character.class);
		BASIC_TYPE_DEFINE.add(java.lang.Byte.class);
		BASIC_TYPE_DEFINE.add(java.sql.Date.class);
		BASIC_TYPE_DEFINE.add(java.sql.Time.class);
		BASIC_TYPE_DEFINE.add(java.sql.Timestamp.class);
		BASIC_TYPE_DEFINE.add(java.util.Date.class);
		BASIC_TYPE_DEFINE.add(java.math.BigInteger.class);
		BASIC_TYPE_DEFINE.add(java.math.BigDecimal.class);
	}

	/**
	 * base package for identify business bean
	 */
	@Qualifier(BeanConfigConst.DEFAULT_BASE_BEAN_PACKAGE)
	@Autowired(required = false)
	private String beanBasePackage;

	/**
	 * isBasicType
	 * 
	 * @param clazz
	 * @return true/false
	 */
	public boolean isBasicType(Class<?> clazz) {
		if (BASIC_TYPE_DEFINE.contains(clazz) || clazz.isPrimitive()) {
			return true;
		}
		return false;
	}

	/**
	 * isBasicInstance
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isBasicInstance(Object obj) {
		if (obj instanceof java.lang.Number || obj instanceof java.util.Date) {
			return true;
		}
		Class<?> clazz = obj.getClass();
		return isBasicType(clazz);
	}

	/**
	 * isBeanType
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isBeanType(Class<?> clazz) {
		if (beanBasePackage == null) {
			// process all non-jdk class as java bean while beanBasePackage has not set
			String pkgName = clazz.getPackage().getName();
			return !pkgName.startsWith("java.") && !pkgName.startsWith("javax.");
		}
		// while beanBasePackage has been set
		if (beanBasePackage.contains(",")) {
			beanBasePackage = beanBasePackage.replace(',', ';');
		}
		String[] beanPackages = beanBasePackage.split(";");
		for (String prefix : beanPackages) {
			if (clazz.getName().startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * isBeanInstance
	 *
	 * @param obj
	 * @return
	 */
	public boolean isBeanInstance(Object obj) {
		Class<?> clazz = obj.getClass();
		return isBeanType(clazz);
	}

	/**
	 * isArrayType
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isArrayType(Class<?> clazz) {
		return clazz.isArray();
	}

	/**
	 * isArrayInstance
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isArrayInstance(Object obj) {
		Class<?> clazz = obj.getClass();
		return isArrayType(clazz);
	}

	/**
	 * isListType
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isListType(Class<?> clazz) {
		return Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * isListInstance
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isListInstance(Object obj) {
		return obj instanceof java.util.Collection;
	}

	/**
	 * isMapType
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isMapType(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}

	/**
	 * isMapInstance
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isMapInstance(Object obj) {
		return obj instanceof java.util.Map;
	}

}
