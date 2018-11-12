/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.lang.annotation.Annotation;

import org.umeframework.dora.validation.constraints.Size;

/**
 * Filling bean properties with input char.<br>
 * 
 * @author Yue MA
 */
public class BeanFieldFiller implements BeanFormatUtil.Formatter {
    /**
     * default filling char
     */
    private char fillChar;

    /**
     * Constructor
     */
    public BeanFieldFiller() {
        this.fillChar = ' ';
    }

    /**
     * Constructor with filling char setup
     * 
     * @param fillChar
     *            - filling char
     */
    public BeanFieldFiller(
            char fillChar) {
        this.fillChar = fillChar;
    }

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
        if (!String.class.equals(fieldType) || value != null) {
            return value;
        }

        Integer length = null;
        for (Annotation anno : annotations) {
            if (Size.class.equals(anno.annotationType())) {
                length = ((Size) anno).max();
                break;
            }
        }
        if (length == null) {
            return value;
        }

        StringBuilder fillingStr = new StringBuilder();
        for (int i = 0; i < length; i++) {
            fillingStr.append(fillChar);
        }
        return fillingStr.toString();
    }
}
