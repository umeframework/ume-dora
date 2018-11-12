/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Message properties implementing by property file
 * 
 * @author Yue MA
 */
public class MessagePropertiesImpl extends BaseMessageProperties {
	/**
	 * default system locale
	 */
	protected Locale locale = Locale.getDefault();

	/**
	 * Constructor
	 * 
	 * @param msgResources
	 */
	public MessagePropertiesImpl(String msgResources) throws Exception {
		super(msgResources);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.core.properties.impl.AbstractMessageProperties#
	 * loadResourceAsMap(java.lang.String)
	 */
	protected Map<String, String> loadResourceAsMap(String messageFile) {
		if (messageFile.toLowerCase().startsWith("classpath:")) {
			messageFile = messageFile.substring(messageFile.indexOf(":") + 1);
		}
		if (messageFile.toLowerCase().endsWith(".properties")) {
			messageFile = messageFile.substring(0, messageFile.toLowerCase().lastIndexOf(".properties"));
		}
		
		ResourceBundle resourceBundle = ResourceBundle.getBundle(messageFile, getLocale());

		Map<String, String> singleResource = new LinkedHashMap<String, String>();
		Iterator<String> keys = resourceBundle.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			singleResource.put(key, resourceBundle.getString(key));
		}
		return singleResource;
	}

	/**
	 * getLocale
	 *
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * setLocale
	 *
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
