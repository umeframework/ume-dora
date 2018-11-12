/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * DataType
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDesc {
    /**
     * column index
     * 
     * @return
     */
    public int index() default 0;
    /**
     * column ID
     * 
     * @return
     */
    public String name() default "";
    /**
     * column label name
     * 
     * @return
     */
    public String label() default "";
    /**
     * primary key flag
     * 
     * @return
     */
    public boolean key() default false;
    /**
     * RDB type
     * 
     * @return
     */
    public String type() default "";
}
