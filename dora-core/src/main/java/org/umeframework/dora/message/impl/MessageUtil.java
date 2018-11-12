/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

/**
 * Message format tool
 * 
 * @author Yue MA
 */
public abstract class MessageUtil {
    /**
     * Replace dynamic parameter in message text
     * 
     * @param message
     * @param parameters
     * @return String
     */
    public static String replaceMessageOptions(
            String message,
            Object[] parameters) {
        if (message == null) {
            return null;
        }
        if (message != null && parameters == null) {
            return message;
        }
        for (int i = 0; i < parameters.length; i++) {
            String flag = "{" + i + "}";
            String repStr = String.valueOf(parameters[i]);
            if (message.contains(flag)) {
                message = message.replace(flag, repStr);
            }
        }
        return message;
    }

}
