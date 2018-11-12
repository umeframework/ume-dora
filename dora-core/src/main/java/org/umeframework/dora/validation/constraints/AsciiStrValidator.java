/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * AsciiStrValidator
 * 
 * @author Yue MA
 */
public class AsciiStrValidator implements ConstraintValidator<AsciiStr, String> {
    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            AsciiStr constraint) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     * javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext ctx) {
        
        if (value == null) {
            return true;
        }
        return ValidatorUtil.isAscii(value);
    }
}
