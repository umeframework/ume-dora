/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.umeframework.dora.exception.SystemException;

/**
 * Message properties implementing by XML file
 * 
 * @author Yue MA
 */
public class MessageXmlImpl extends BaseMessageProperties {
    /**
     * XML rule define
     */
    private static final String RULE_MAPPING_DEFINE = MessageXmlImpl.class.getPackage().getName().replace(".", "/") + "/xmlMessageRule.xml";
    /**
     * Digester instance prepare
     */
    private static final Digester defaultDigester = DigesterLoader.createDigester(MessageXmlImpl.class.getClassLoader().getResource(RULE_MAPPING_DEFINE));

    /**
     * Constructor
     * 
     * @param msgResources
     */
    public MessageXmlImpl(
            String msgResources) throws SystemException {
        super(msgResources);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.properties.impl.AbstractMessageProperties#
     * loadResourceAsMap(java.lang.String)
     */
    @Override
    protected Map<String, String> loadResourceAsMap(
            String msgResources) throws SystemException {
        // Get url
        URL resourceUrl = Thread.currentThread().getContextClassLoader().getResource(msgResources + ".xml");
        // Parse XML file
        XmlMessageBeans beans = null;
        try {
            beans = (XmlMessageBeans) defaultDigester.parse(resourceUrl);
            Map<String, String> msgs = new HashMap<String, String>();
            for (XmlMessageBean bean : beans.getBeans()) {
                String key = bean.getId();
                String value = bean.getText();
                msgs.put(key, value);
            }
            return msgs;
        } catch (Exception e) {
            throw new SystemException(e, "Failed to load XML message resource[" + msgResources + "].");
        }
    }

}
