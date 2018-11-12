/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ReflectUtil
 * 
 * @author IBM
 */
abstract public class ReflectUtil {
    /**
     * getNonBridgeMethod
     * 
     * @param clazz
     * @param methodName
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getNonBridgeMethod(
            Class<?> clazz,
            String methodName) {
        //Method[] methods = clazz.getDeclaredMethods();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && !method.isBridge()) {
                return method;
            }
        }
        return null;
    }

    /**
     * getNonBridgeMethods
     * 
     * @param clazz
     * @param methodName
     * @return
     * @throws NoSuchMethodException
     */
    public static List<Method> getNonBridgeMethods(
            Class<?> clazz,
            String methodName) {
    	List<Method> result = new ArrayList<Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && !method.isBridge()) {
            	result.add(method);
            }
        }
        return result;
    }
}
