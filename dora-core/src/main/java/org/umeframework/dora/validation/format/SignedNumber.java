/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SignedNumber
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SignedNumber {
    /**
     * Type
     */
    public static enum Type {
        ORIGIN, PLUS_APPEND, PLUS_REMOVE
    }

    /**
     * value
     * 
     * @return
     */
    public Type value() default Type.ORIGIN;
}
