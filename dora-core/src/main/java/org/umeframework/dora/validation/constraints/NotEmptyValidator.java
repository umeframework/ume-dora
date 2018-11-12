/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * NotEmptyValidator
 * 
 * @author Yue MA
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {
    
    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(
            NotEmpty constraint) {
    }

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(
            Object value,
            ConstraintValidatorContext context) {
        
        return !ValidatorUtil.isEmpty(value);
    }

}
