/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * DateFormatValidator
 * 
 * @author Yue MA
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {
    /**
     * Date format
     */
    private String[] formats;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            DateFormat constraint) {
        formats = constraint.value();
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
        
        for (String format : formats) {
            if (ValidatorUtil.matchedDateFormat(value, format)) {
                return true;
            }
        }
        return false;
    }
}
