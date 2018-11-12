/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax.impl;

import java.text.SimpleDateFormat;

import org.umeframework.dora.bean.BeanSupport;

/**
 * Const define for Json data format handling
 * 
 * @author Yue MA
 */
public abstract class JsonSupport extends BeanSupport {
    /**
     * JSON null show value
     */
    protected static final String JSON_NULL_STR = "null";
    /**
     * JSON property separate char
     */
    protected static final char JSON_ELEMENT_SEP = ',';
    /**
     * JSON property scope char
     */
    protected static final char JSON_PROPERTY_AROUND = '\"';
    /**
     * JSON property:value separate char
     */
    protected static final char JSON_PROPERTY_SUFFIX = ':';
    /**
     * JSON array begin flag
     */
    protected static final char JSON_ARRAY_BEGIN = '[';
    /**
     * JSON array end flag
     */
    protected static final char JSON_ARRAY_END = ']';
    /**
     * JSON data begin flag
     */
    protected static final char JSON_MAP_BEGIN = '{';
    /**
     * JSON data end flag
     */
    protected static final char JSON_MAP_END = '}';
    /**
     * JSON data separate char
     */
    protected static final char JSON_TEXT_VALUE_AROUND = '\"';
    /**
     * Null getter parameter
     */
    protected static final Object[] NULL_GETTER_PARAMS = new Object[0];
    /**
     * JSON array start flag (using for direct parameters input)
     */
    protected static final String JSON_ARRAY_START_CHAR = "[";
    /**
     * JSON object (map/bean) start flag (using for parameters which wrap by map or bean)
     */
    protected static final String JSON_OBJECT_START_CHAR = "{";

    /**
     * defaultDatetimeFormat
     */
    private SimpleDateFormat defaultDatetimeFormat;

    /**
     * JsonSupport
     */
    public JsonSupport() {
        defaultDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        defaultDatetimeFormat.setLenient(false);
    }

    /**
     * @return the defaultDatetimeFormat
     */
    public SimpleDateFormat getDefaultDatetimeFormat() {
        return defaultDatetimeFormat;
    }

    /**
     * @param defaultDatetimeFormat the defaultDatetimeFormat to set
     */
    public void setDefaultDatetimeFormat(SimpleDateFormat defaultDatetimeFormat) {
        this.defaultDatetimeFormat = defaultDatetimeFormat;
    }
}
