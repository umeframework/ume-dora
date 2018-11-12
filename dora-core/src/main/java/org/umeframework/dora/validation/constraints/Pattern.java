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
 * Pattern
 * 
 * @author Yue MA
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PatternValidator.class})
public @interface Pattern {
    /**
     * regular expression 
     * 
     * @return
     */
    String[] value();

    /**
     * message
     * 
     * @return
     */
    String message() default "Incorrect format.";
    
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
