/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.exception.impl;

import org.umeframework.dora.exception.ApplicationException;
import org.umeframework.dora.exception.ExceptionHandler;
import org.umeframework.dora.exception.SystemException;
import org.umeframework.dora.exception.ValidationException;
import org.umeframework.dora.message.Message;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.ServiceErrorResponse;

/**
 * Default exception handler implementation.<br>
 * 
 * @author Yue MA
 */
public class ExceptionHandlerImpl extends BaseComponent implements ExceptionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.umeframework.dora.exception.ExceptionHandler#handleException(org.umeframework.dora.service.
	 * ServiceResponse, java.lang.Throwable,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handleException(ServiceErrorResponse errorResult, Throwable e) {

		if (e instanceof ValidationException) {
			errorResult.setErrorType(ServiceErrorResponse.VALIDATION_EXCEPTION);
			ValidationException actualException = (ValidationException) e;
			for (int i = 0; i < actualException.size(); i++) {
				String messageId = actualException.getMessageIdList().get(i);
				Object[] messageParams = actualException.getParametersList().get(i);
				String message = getMessageProperties().get(messageId, messageParams);
				if (message == null) {
					message = messageId;
					messageId = null;
				}
				errorResult.addException(new Message(messageId, message, messageParams));
				getLogger().error(message);
			}
		} else if (e instanceof ApplicationException) {
			errorResult.setErrorType(ServiceErrorResponse.APPLICATION_EXCEPTION);
			ApplicationException actualException = (ApplicationException) e;
			String messageId = actualException.getMessageId();
			Object[] messageParams = actualException.getParameters();
			String message = getMessageProperties().get(messageId, messageParams);
			if (message == null) {
				message = messageId;
				messageId = null;
			}
			errorResult.addException(new Message(messageId, message));
			getLogger().error(message, actualException.getCause());
		} else if (e instanceof SystemException) {
			errorResult.setErrorType(ServiceErrorResponse.SYSTEM_EXCEPTION);
			SystemException actualException = (SystemException) e;
			String messageId = actualException.getMessageId();
			Object[] messageParams = actualException.getParameters();
			String message = getMessageProperties().get(messageId, messageParams);
			if (message == null) {
				message = messageId;
				messageId = null;
			}
			errorResult.addException(new Message(messageId, message));
			getLogger().error(message, actualException.getCause());
		} else {
			errorResult.setErrorType(ServiceErrorResponse.SYSTEM_EXCEPTION);
			Message msg = new Message(null, "Runtime error: please see detail information in log.");
			errorResult.addException(msg);
			getLogger().error(e.getMessage(), e);
		}
	}

}
