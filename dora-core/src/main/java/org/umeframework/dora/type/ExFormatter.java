/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.type;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.umeframework.dora.bean.BeanFormatUtil;

/**
 * ExFormatter
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExFormatter {
    /**
     * value
     * 
     * @return
     */
    public Class<? extends BeanFormatUtil.Formatter> value();
}
