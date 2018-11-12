/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * TableDesc
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TableDesc {
    /**
     * table ID
     * 
     * @return
     */
    public String name() default "";
    /**
     * table label name
     * 
     * @return
     */
    public String label() default "";
    /**
     * table type
     * 
     * @return
     */
    public String type() default "";
}
