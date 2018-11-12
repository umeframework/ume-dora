/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * PatternValidator
 * 
 * @author Yue MA
 */
public class PatternValidator implements ConstraintValidator<Pattern, String> {
    /**
     * regExpFormat
     */
    private String[] regExps;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            Pattern constraint) {
        regExps = constraint.value();
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
        for (String regExp : regExps) {
            if (ValidatorUtil.matchedRegExp(value, regExp)) {
                return true;
            }
        }
        return false;
    }
}
