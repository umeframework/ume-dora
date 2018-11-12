/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.util.Iterator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.umeframework.dora.exception.ValidationException;

/**
 * BeanValidator
 * 
 * @author Yue MA
 *
 */
public class BeanValidator {
	/**
	 * factory
	 */
	static private ValidatorFactory factory = null;

	/**
	 * validate
	 * 
	 * @param bean
	 * @throws ValidationException
	 */
    public <T> void validate(T bean) throws ValidationException {
        if (factory == null) {
            factory = Validation.buildDefaultValidatorFactory();
        }
        Validator validator = factory.getValidator();        
        Iterator<ConstraintViolation<T>> violations = validator.validate(bean).iterator();
        ValidationException vex = new ValidationException();
        while (violations.hasNext()) {
            ConstraintViolation<T> violation = violations.next();
            String prop = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            vex.add(message, prop);
        }
        
        if (vex.size() > 0) {
            throw vex;
        }
    }

}
