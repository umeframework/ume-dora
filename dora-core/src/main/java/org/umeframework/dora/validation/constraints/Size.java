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
 * Size
 * 
 * @author Yue MA
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SizeValidator.class})
public @interface Size {
    /**
     * min
     * 
     * @return
     */
    int min() default 0;
    /**
     * max
     * 
     * @return
     */
    int max() default Integer.MAX_VALUE;
    /**
     * precision
     * 
     * @return
     */
    int precision()  default 0;

    /**
     * message
     * 
     * @return
     */
    String message() default "This value is out of size limit.";
    
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
