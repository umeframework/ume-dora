/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Padding
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Padding {
    /**
     * LEFT
     */
    public final char LEFT = 'L';
    /**
     * RIGHT
     */
    public final char RIGHT = 'R';
    /**
     * ZERO
     */
    public final char ZERO = '0';
    /**
     * SPACE
     */
    public final char SPACE = ' ';

    /**
     * type
     * 
     * @return
     */
    public char type();

    /**
     * value
     * 
     * @return
     */
    public char value() default SPACE;
}
