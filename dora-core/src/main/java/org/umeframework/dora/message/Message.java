/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message;

/**
 * Message resource define class for whole code-value with dynamic parameters
 * structure resources.<br>
 * 
 * @author Yue MA
 */
public class Message implements java.io.Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 6639059006240667118L;
	/**
	 * message contents
	 */
	private String message;
	/**
	 * message id
	 */
	private String id;
	/**
	 * message parameters
	 */
	private Object[] parameters;

	/**
	 * Message
	 */
	public Message() {
	}

	/**
	 * Constructor
	 * 
	 * @param messageId
	 *            - message code
	 * @param parameters
	 *            - message parameters
	 */
	public Message(String messageId) {
		this.id = messageId;
	}

	/**
	 * Constructor
	 * 
	 * @param messageId
	 *            - message code
	 * @param parameters
	 *            - message parameters
	 */
	public Message(String messageId, Object[] parameters) {
		this.id = messageId;
		this.parameters = parameters;
	}

	/**
	 * Constructor
	 * 
	 * @param messageId
	 *            - message code
	 * @param message
	 *            - message contents
	 */
	public Message(String messageId, String message) {
		this.id = messageId;
		this.message = message;
	}

	/**
	 * Constructor
	 * 
	 * @param messageId
	 *            - message code
	 * @param message
	 *            - message contents
	 * @param parameters
	 *            - message parameters
	 */
	public Message(String messageId, String message, Object[] parameters) {
		this.id = messageId;
		this.message = message;
		this.parameters = parameters;
	}

	/**
	 * Get message contents
	 * 
	 * @return
	 */
	public String getMessage() {
		return message == null ? id : message;
	}

	/**
	 * Set message contents
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Get message code
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set message code
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get message parameters
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * Set message parameters
	 * 
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id + ":" + message;
	}
}
