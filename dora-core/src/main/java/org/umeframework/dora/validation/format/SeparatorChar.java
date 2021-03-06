/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SeparatorChar
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SeparatorChar {
    /**
     * COMMA
     */
    public final String COMMA = ",";

    /**
     * value
     * 
     * @return
     */
    public String value();
}
