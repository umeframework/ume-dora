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
 * Range
 * 
 * @author Yue MA
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RangeValidator.class})
public @interface Range {
    /**
     * min
     * 
     * @return
     */
    String min() default "";

    /**
     * max
     * 
     * @return
     */
    String max() default "";
    
    /**
     * message
     * 
     * @return
     */
    String message() default "This value is out of range.";
    
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
