/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.lang.annotation.Annotation;

/**
 * Trim all properties for bean instance.<br>
 * 
 * @author Yue MA
 */
public class BeanFieldTrimmer implements BeanFormatUtil.Formatter {
    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.util.BeanFormatUtil.Formatter#format(java.lang.String,
     * java.lang.Object, java.lang.Class, java.lang.annotation.Annotation[])
     */
    public Object format(
            String name,
            Object value,
            Class<?> fieldType,
            Annotation[] annotations) {
        if (value != null && value instanceof String) {
            return ((String) value).trim();
        }
        return value;
    }
}
