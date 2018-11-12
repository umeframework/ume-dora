/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * AJAX data parse interface.<br>
 * 
 * @author Yue MA
 */
public interface AjaxParser<E> {
	/**
	 * Parse json input data to java object
	 * 
	 * @param inData
	 *            - input data
	 * @param clazz
	 *            - target java object type
	 * @param genericType
	 *            - target java object generic type
	 * @param annotations
	 *            - input param's annotations
	 * 
	 * @return - java object
	 * @throws Exception
	 */
	<T> T parse(E inData, Class<T> clazz, Type genericType, Annotation[] annotations) throws Exception;
	
	/**
     * Parse json input data to java object
	 * 
	 * @param jsonStr
	 * @param clazz
	 * @param actualTypeArgs
	 * @return
	 * @throws Exception
	 */
	<T> T parse(String jsonStr, Class<T> clazz, Type... actualTypeArgs) throws Exception;

}
