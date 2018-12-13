/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.contant.ContextConstants;
import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.message.MessageProperties;

/**
 * Common service component function define
 *
 * @author Yue MA
 */
public abstract class BaseComponent implements ContextConstants {
	/**
	 * logger
	 */
	@Autowired(required=false)
	@Qualifier(BeanConfigConst.DEFAULT_LOGGER)
	private Logger logger;

	/**
	 * message properties
	 */
    @Autowired(required=false)
    @Qualifier(BeanConfigConst.DEFAULT_MESSAGE_PROPERTIES)
	private MessageProperties messageProperties;

	/**
	 * Throw application exception
	 * 
	 * @param message - message ID or message content
	 * @param parameters - message options
	 */
	protected void createApplicationException(String message, Object... parameters) {
		throw new ApplicationException(message, parameters);
	}

	/**
	 * Throw application exception
	 * 
	 * @param cause - exception instance
	 * @param message - message ID or message content
	 * @param parameters - message options
	 */
	protected void createApplicationException(Throwable cause, String message, Object... parameters) {
		throw new ApplicationException(cause, message, parameters);
	}

	/**
	 * Get current user ID
	 *
	 * @return
	 */
	public String getUid() {
		return RequestContext.getCurrentContext().get(UID);
	}

	/**
	 * Get current system ID
	 *
	 * @return
	 */
	public String getSysId() {
        return RequestContext.getCurrentContext().get(SYS);
	}

	/**
	 * Get current service ID
	 *
	 * @return
	 */
	public String getServiceId() {
        return RequestContext.getCurrentContext().get(SID);
	}

	/**
	 * get transaction start time
	 *
	 * @return
	 */
	protected Timestamp getTransactionStartTime() {
		return RequestContext.getCurrentContext().get(TRANSACTION_START_TIME);
	}

	/**
	 * get current time
	 *
	 * @return
	 */
	protected Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return the messageProperties
	 */
	public MessageProperties getMessageProperties() {
		return messageProperties;
	}

	/**
	 * @param messageProperties
	 *            the messageProperties to set
	 */
	public void setMessageProperties(MessageProperties messageProperties) {
		this.messageProperties = messageProperties;
	}

	/**
	 * @return the appLogger
	 */
	public Logger getLogger() {
		return logger.getAppender(this.getClass());
	}

	/**
	 * @param appLogger
	 *            the appLogger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
