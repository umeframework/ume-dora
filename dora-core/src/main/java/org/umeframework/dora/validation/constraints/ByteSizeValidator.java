/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * ByteSizeValidator
 * 
 * @author Yue MA
 */
public class ByteSizeValidator implements ConstraintValidator<ByteSize, String> {
    /**
     * min
     */
    private int min;
    /**
     * max
     */
    private int max;
    /**
     * charset
     */
    private String charset;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            ByteSize constraint) {
        min = constraint.min();
        max = constraint.max();
        charset = constraint.charset();
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
        
        return ValidatorUtil.isByteInRange(value, charset, min, max);
    }
}
