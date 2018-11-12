/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CloseChar
 * 
 * @author Yue MA
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CloseChar {
    /**
     * EOF
     */
    public final char EOF = '\n';

    /**
     * value
     * 
     * @return
     */
    public char value();
}
