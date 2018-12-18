/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.umeframework.dora.exception.SystemException;
import org.umeframework.dora.message.MessageProperties;

/**
 * Message properties access common logic implementing class
 *
 * @author Yue MA
 */
public abstract class BaseMessageProperties implements MessageProperties {
    /**
     * Split char between messageId and message
     */
    public static final String MESSAGE_ID_SPLIT_CHAR = "::";
    /**
     * message resource map
     */
    private Map<String, String> resourceMap;
    /**
     * String msgResources
     */
    private String msgResources;

    /**
     * AbstractMessageProperties
     *
     * @param msgResources
     */
    public BaseMessageProperties(String msgResources) {
        this.msgResources = msgResources;
    }

    /**
     * initialize
     *
     * @param msgResources
     * @throws SystemException
     */
    synchronized public void init() throws Exception {
        if (msgResources == null) {
            System.err.println("Warning:No found message resource.");
            return;
        }
        msgResources = msgResources.contains(",") ? msgResources.replace(',', ';') : msgResources;
        String[] msgResourceElements = msgResources.split(";");
        resourceMap = new ConcurrentHashMap<String, String>();
        for (String messageFile : msgResourceElements) {
            Map<String, String> resourceProperties = null;
            try {
                resourceProperties = loadResourceAsMap(messageFile);
                if (resourceProperties != null) {
                    for (Map.Entry<String, String> e : resourceProperties.entrySet()) {
                        String key = e.getKey();
                        String value = e.getValue();
                        if (resourceMap.containsKey(key)) {
                            throw new Exception("Found duplicate key define in resource file " + messageFile + ",key=" + key);
                        }
                        resourceMap.put(key, value);
                    }
                }
            } catch (Exception e) {
                System.err.println("Warning:Can not load resource " + messageFile);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.prop.MessageProperties#get(java.lang.String, java.lang.Object[])
     */
    @Override
    public String get(String messageId, Object... parameters) {
        String message = null;

        if (messageId.contains(MESSAGE_ID_SPLIT_CHAR)) {
            message = messageId;
            messageId = messageId.substring(0, messageId.indexOf(MESSAGE_ID_SPLIT_CHAR));
            if (resourceMap.containsKey(messageId)) {
                message = resourceMap.get(messageId);
            }
        } else {
            message = resourceMap.get(messageId);
        }

        if (message != null) {
            message = MessageUtil.replaceMessageOptions(message, parameters);
        }
        return message;
    }

    /**
     * loadResourceAsMap
     *
     * @param messageFile
     * @return
     */
    abstract protected Map<String, String> loadResourceAsMap(String messageFile) throws SystemException;

    /**
     * @return the msgResources
     */
    public String getMsgResources() {
        return msgResources;
    }

    /**
     * @param msgResources
     *            the msgResources to set
     */
    public void setMsgResources(String msgResources) {
        this.msgResources = msgResources;
    }
    
    /**
     * keySet
     * 
     * @return
     */
    public Set<String> keySet() {
        return resourceMap.keySet();
    }

}
