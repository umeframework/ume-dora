/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import java.util.ArrayList;
import java.util.List;

import org.umeframework.dora.message.Message;

/**
 * Base service response object structure define
 * 
 * @author Yue MA
 */
@Deprecated
public class ServiceResponse<T> implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9075476354638484508L;
	/**
	 * service execution success
	 */
	public static final int SUCCESS = 0;
	/**
	 * service execution fail due to system exception
	 */
	public static final int SYSTEM_EXCEPTION = -1;
	/**
	 * service execution fail due to application exception
	 */
	public static final int APPLICATION_EXCEPTION = -2;
	/**
	 * service execution fail due to application exception
	 */
	public static final int VALIDATION_EXCEPTION = -3;

	/**
	 * Service execute successfully
	 */
	private int resultCode;
	/**
	 * Service execute result object
	 */
	private T resultObject;
	/**
	 * Messages of service execution
	 */
	private List<String> messages;
	/**
	 * Exceptions of service execution
	 */
	private List<Message> exceptions;

	/**
	 * getResultObject
	 * 
	 * @return
	 */
	public T getResultObject() {
		return resultObject;
	}
	/**
	 * setResultObject
	 * 
	 * @param
	 */
	public void setResultObject(T resultObject) {
		this.resultObject = resultObject;
	}
	
	/**
	 * addException
	 * 
	 * @param e
	 */
	public void addException(Message e) {
		if (exceptions == null) {
			exceptions = new ArrayList<Message>();
		}
		exceptions.add(e);
	}

	/**
	 * addMessage
	 * 
	 * @param e
	 */
	public void addMessage(String e) {
		if (messages == null) {
			messages = new ArrayList<String>();
		}
		messages.add(e);
	}

	/**
	 * getMessages
	 * 
	 * @return
	 */
	public List<String> getMessages() {
		return messages;
	}

	/**
	 * setMessages
	 * 
	 * @param messages
	 */
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	/**
	 * getExceptions
	 * 
	 * @return
	 */
	public List<Message> getExceptions() {
		return exceptions;
	}

	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode
	 *            the resultCode to set
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @param exceptions
	 *            the exceptions to set
	 */
	public void setExceptions(List<Message> exceptions) {
		this.exceptions = exceptions;
	}
}
