/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * RefelctAsm tool
 * 
 * @author Yue MA
 */
abstract public class RefelctAsmUtil {
	// /**
	// * MethodAccess cache container.
	// */
	// private static final Map<Class<?>, MethodAccess> methodCache = new ConcurrentHashMap<Class<?>, MethodAccess>();
	// /**
	// * Clear cache container.
	// */
	// public static synchronized void clearMethodCache() {
	// methodCache.clear();
	// }
	// /**
	// * Count cache object number.
	// */
	// public static int getMethodCacheNum() {
	// return methodCache.size();
	// }
	// /**
	// * Get cached MethodAccess object.
	// *
	// * @param clazz
	// * @return
	// */
	// public static MethodAccess getMethodAccess(Class<?> clazz) {
	// if (methodCache.containsKey(clazz)) {
	// return methodCache.get(clazz);
	// }
	// MethodAccess method = MethodAccess.get(clazz);
	// methodCache.put(clazz, method);
	// return method;
	// }
	//
	// /**
	// * invoke
	// *
	// * @param object
	// * @param methodName
	// * @param args
	// * @return
	// */
	// public static Object invoke(Object object, String methodName, Object...args) {
	// MethodAccess accessor = getMethodAccess(object.getClass());
	// return accessor.invoke(object, methodName, args);
	// }

}
