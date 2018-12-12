/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.dao.util;

/**
 * ThreadCallStackUtil
 * 
 * @author Yue MA
 * 
 */
public class ThreadCallStackUtil {
    /**
     * getInvokedMethod
     * 
     * @param layer
     * @return
     */
    public static String getInvokedMethod(
            int layer) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks.length > layer) {
            return stacks[layer].getClassName() + "." + stacks[layer].getMethodName();
        }
        return null;
    }

    /**
     * getInvokedClass
     * 
     * @param layer
     * @return
     */
    public static String getInvokedClass(
            int layer) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks.length > layer) {
            return stacks[layer].getClassName();
        }
        return null;
    }

    /**
     * getInvokedClassPackage
     * 
     * @param layer
     * @return
     */
    public static String getInvokedClassPackage(
            int layer) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks.length > layer) {
            String clazzName = stacks[layer].getClassName();
            return clazzName.substring(0, clazzName.lastIndexOf("."));
        }
        return null;
    }

}
