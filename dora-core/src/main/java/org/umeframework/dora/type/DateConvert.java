/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * DateConvert
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DateConvert {
    /**
     * from
     * 
     * @return
     */
    public String from();

    /**
     * to
     * 
     * @return
     */
    public String to();
}
