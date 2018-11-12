/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception information define for input data validating error.<br>
 * Application should be aware of the kind of error and decide how to process
 * it.<br>
 * 
 * @author Yue MA
 */
public class ValidationException extends ApplicationException {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7359511042557052759L;

	/**
	 * validation message list
	 */
	private List<String> messageIdList = new ArrayList<String>();

	/**
	 * validation message list
	 */
	private List<Object[]> parametersList = new ArrayList<Object[]>();

	/**
	 * ValidationException
	 * 
	 * @param messageId
	 * @param parameters
	 */
	public ValidationException(String messageId, Object[] parameters) {
		super(messageId, parameters);
	}

	/**
	 * ValidationException
	 */
	public ValidationException() {
		super("validate error:");
	}

	/**
	 * ValidationException
	 */
	public ValidationException(Throwable e) {
		super("validate error:", new Object[] { e.getMessage() });
	}

	/**
	 * validation message size
	 * 
	 * @return message number
	 */
	public int size() {
		return messageIdList.size();
	}

	/**
	 * add
	 * 
	 * @param messageId
	 */
	public void add(String messageId) {
		messageIdList.add(messageId);
	}
	
	/**
	 * add
	 * 
	 * @param messageId
	 * @param parameters
	 */
	public void add(String messageId, Object... parameters) {
		messageIdList.add(messageId);
		parametersList.add(parameters);
	}

	/**
	 * addError
	 * 
	 * @param ve
	 */
	public void add(ValidationException ve) {
		this.messageIdList.addAll(ve.getMessageIdList());
		this.parametersList.addAll(ve.getParametersList());
	}

	/**
	 * @return the messageIdList
	 */
	public List<String> getMessageIdList() {
		return messageIdList;
	}

	/**
	 * @return the parametersList
	 */
	public List<Object[]> getParametersList() {
		return parametersList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.exception.ApplicationException#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (messageIdList.size() > 0) {
			for (int i = 0; i < messageIdList.size(); i++) {
				if (i > 0) {
					result.append(System.getProperty("line.separator"));
				}
				result.append(messageIdList.get(i));
			}
		}
		return result.toString();
	}
}
