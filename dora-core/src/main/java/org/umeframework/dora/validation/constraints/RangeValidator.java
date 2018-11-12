/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * RangeValidator
 * 
 * @author Yue MA
 */
public class RangeValidator implements ConstraintValidator<Range, Object> {
    /**
     * min
     */
    private String min;
    /**
     * max
     */
    private String max;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            Range constraint) {
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

        return ValidatorUtil.isInRange(value, min, max);
    }
}
