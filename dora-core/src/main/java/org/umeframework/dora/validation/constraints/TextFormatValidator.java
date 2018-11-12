/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.umeframework.dora.util.ValidatorUtil;

/**
 * AlphaNumStrValidator
 * 
 * @author Yue MA
 */
public class TextFormatValidator implements ConstraintValidator<TextFormat, String> {
    /**
     * text category
     */
    TextFormat.Category category;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
     * Annotation)
     */
    @Override
    public void initialize(
            TextFormat constraint) {
        category = constraint.value();
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

        boolean result = false;
        switch (category) {
        case Datetime: {
            result = ValidatorUtil.matchedDateFormat(value, "yyyy-MM-dd HH:mm:ss.SSS") || ValidatorUtil.matchedDateFormat(value, "yyyy-MM-dd HH:mm:ss");
            break;
        }
        case Date: {
            result = ValidatorUtil.matchedDateFormat(value, "yyyy-MM-dd");
            break;
        }
        case Time: {
            result = ValidatorUtil.matchedDateFormat(value, "HH:mm:ss");
            break;
        }
        case Alpha: {
            result = ValidatorUtil.isAlpha(value);
            break;
        }
        case Numeric: {
            result = ValidatorUtil.isNumeric(value);
            break;
        }
        case Decimal: {
            result = ValidatorUtil.isDecimal(value);
            break;
        }
        case AlphaNumeric: {
            result = ValidatorUtil.isAlphaNumeric(value);
            break;
        }
        case Currency: {
            result = ValidatorUtil.isCurrency(value);
            break;
        }
        case TelNumber: {
            result = ValidatorUtil.isTelNumber(value);
            break;
        }
        case MobileNumber: {
            result = ValidatorUtil.isMobileNumber(value);
            break;
        }
        case ZipCode: {
            result = ValidatorUtil.isZipCode(value);
            break;
        }
        case Email: {
            result = ValidatorUtil.isEmail(value);
            break;
        }
        case Ascii: {
            result = ValidatorUtil.isAscii(value);
            break;
        }
        case AsciiNoneSpace: {
            result = ValidatorUtil.isAsciiNS(value);
            break;
        }
        }
        return result;
    }
}
