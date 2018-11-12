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
 * Date time
 * 
 * @author Yue MA
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { TextFormatValidator.class })
public @interface TextFormat {
    /**
     * Category element declare
     */
    enum Category {
        /**
         * Date and time
         */
        Datetime,
        /**
         * Date
         */
        Date,
        /**
         * Time
         */
        Time,
        /**
         * Alpha characters
         */
        Alpha,
        /**
         * Numeric characters (Integer...)
         */
        Numeric,
        /**
         * Decimal characters (Real/Float...)
         */
        Decimal,
        /**
         * Alpha characters and numeric characters
         */
        AlphaNumeric,
        /**
         * Currency
         */
        Currency,
        /**
         * Telephone number
         */
        TelNumber,
        /**
         * Mobile number
         */
        MobileNumber,
        /**
         * ZIP code
         */
        ZipCode,
        /**
         * Email
         */
        Email,
        /**
         * ASCII characters
         */
        Ascii,
        /**
         * ASCII characters expect space
         */
        AsciiNoneSpace
    }

    /**
     * value
     * 
     * @return
     */
    Category value();

    /**
     * message
     * 
     * @return
     */
    String message() default "Incorrect text format.";

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
