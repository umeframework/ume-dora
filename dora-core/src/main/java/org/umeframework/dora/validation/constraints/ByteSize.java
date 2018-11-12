/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * ByteLength
 * 
 * @author Yue MA
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ByteSizeValidator.class})
public @interface ByteSize {
    /**
     * min
     * 
     * @return
     */
    int min() default -1;

    /**
     * max
     * 
     * @return
     */
    int max() default -1;
    
    /**
     * default
     * 
     * @return
     */
    String charset() default "UTF-8";
    
    /**
     * message
     * 
     * @return
     */
    String message() default "This value is out of byte limit.";
    
    /**
     * groups
     * 
     * @return
     */
    Class<?>[] groups() default {};
    
    /**
     * payload
     * 
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
