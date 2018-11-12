/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * LeftChar
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LeftChar {
    /**
     * value
     * 
     * @return
     */
    public String value();

    /**
     * beforePadding
     * 
     * @return
     */
    public boolean beforePadding() default false;
}
