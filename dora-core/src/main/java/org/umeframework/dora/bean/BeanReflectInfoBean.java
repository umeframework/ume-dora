/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * BeanReflectInfoBean
 * 
 * @author Yue MA
 */
public class BeanReflectInfoBean implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6942453830755737827L;
	
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
