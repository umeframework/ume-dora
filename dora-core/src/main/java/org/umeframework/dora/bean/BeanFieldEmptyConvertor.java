/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.lang.annotation.Annotation;

import org.umeframework.dora.validation.format.EmptyReplaceValue;

/**
 * Convert bean properties with empty string.<br>
 * 
 * @author Yue MA
 */
public class BeanFieldEmptyConvertor implements BeanFormatUtil.Formatter {
    /**
     * empty string default value
     */
    private static final String EMPTY_STR = "";

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.util.BeanFormatUtil.Formatter#format(java.lang.String,
     * java.lang.Object, java.lang.Class, java.lang.annotation.Annotation[])
     */
    public Object format(
            String fieldName,
            Object value,
            Class<?> fieldType,
            Annotation[] annotations) {
        Object resultValue = value;
        for (int i = 0; annotations != null && i < annotations.length; i++) {
            if (annotations[i].annotationType().equals(EmptyReplaceValue.class)) {
                if (value == null || EMPTY_STR.equals(value.toString())) {
                    resultValue = ((EmptyReplaceValue) annotations[i]).value();
                    break;
                }
            }
        }
        return resultValue;
    }
}
