/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * SizeValidator
 * Notes: only check min and max length, precision is out of check scope now.
 * 
 * @author Yue MA
 */
public class SizeValidator implements ConstraintValidator<Size, Object> {
    /**
     * min
     */
    private int min;
    /**
     * max
     */
    private int max;


    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            Size constraint) {
        min = constraint.min();
        max = constraint.max();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
     * javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(
            Object value,
            ConstraintValidatorContext ctx) {

        return ValidatorUtil.isSizeInRange(value, min, max);
    }
}
