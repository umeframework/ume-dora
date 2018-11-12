/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * ConstValueValidator
 * 
 * @author Yue MA
 */
public class ConstValueValidator implements ConstraintValidator<ConstValue, Object> {
    /**
     * Constant value define by string
     */
    private String[]  constDefine;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            ConstValue constraint) {
        constDefine = constraint.value();
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

        return ValidatorUtil.isInConstCollection(value, constDefine);
    }
}
