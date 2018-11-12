/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.code;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Code resource manage class for whole code-value with dynamic parameters
 * structure resources.<br>
 * 
 * @author Yue MA
 */
public interface CodeProperties {
    /**
     * get all category list 
     * 
     * @return
     */
    Set<String> getCategoryList();

    /**
     * get all code structure list by category, return as map which index by 'code' key
     * 
     * @param category
     * @return
     */
    Map<String, Map<String, Object>> getCodeMap(
            String category);
    
    /**
     * get all code structure list by category
     * 
     * @param category
     * @return
     */
    Collection<Map<String, Object>> getCodeList(
            String category);
    
    /**
     * get code data structure by input category and code name
     * 
     * @param category
     * @param code
     * @return
     */
    Map<String, Object> getCode(
            String category, String code);
    
    
    /**
     * get code value by input category and code name
     * 
     * @param category
     * @param code
     * @return
     */
    Object getCodeValue(
            String category, String code);

    /**
     * get code desc by input category and code name
     * 
     * @param category
     * @param code
     * @return
     */
    Object getCodeDesc(
            String category, String code);

}
