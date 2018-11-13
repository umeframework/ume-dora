/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.property.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

import org.umeframework.dora.property.ConfigProperties;

/**
 * Configuration properties implementing by property file.<br>
 * Note the load priority define for properties as follow rules:<br>
 * <li>In case of value start with "classpath:" -  load form application classpath directory<br>
 * <li>In case of value not start with "classpath:" - firstly try load form application execution directory, then try from classpath once not found in execution directory.<br>
 * 
 * @author Yue MA
 */
public class ConfigPropertiesImpl implements ConfigProperties {
	/**
	 * properties file location path
	 */
	private String configLocation;

	/**
	 * properties store instance
	 */
	private Properties store;

	/**
	 * ConfigPropertiesImpl
	 * 
	 * @param configLocation
	 */
	public ConfigPropertiesImpl(String configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * initialize interface
	 * 
	 * @throws IOException
	 */
	synchronized public void init() throws IOException {
		this.load();
	}
	/**
	 * initialize interface
	 * 
	 * @throws IOException
	 */
	synchronized public void destroy() throws IOException {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.config.ConfigProperties#load()
	 */
	synchronized public void load() throws IOException {
        if (configLocation == null) {
            System.err.println("Warning:No found configuration resource.");
            return;
        }
	    
	    
		InputStream inStream = null;
		configLocation = configLocation.trim();
		configLocation = configLocation.toLowerCase().endsWith(".properties") ? configLocation : configLocation + ".properties";

		IOException ie = null;
		if (!configLocation.startsWith("classpath:")) {
			try {
				inStream = new FileInputStream(configLocation);
			} catch (IOException e) {
				ie = e;
			}
		}
		if (inStream == null) {
			String loc = configLocation;
			if (loc.startsWith("classpath:")) {
				loc = loc.substring(loc.indexOf(":") + 1);
			}
			inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(loc);
		}
		if (inStream == null) {
			if (ie != null) {
				throw ie;
			} else {
				throw new IOException("No found available configuration from " + configLocation);
			}
		}
		store = new Properties();
		store.load(inStream);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.config.ConfigProperties#save()
	 */
	synchronized public void save() throws IOException {
		if (!configLocation.startsWith("classpath:")) {
			// InputStream inStream = null;
			OutputStream outStream = null;
			try {
				// if (configLocation.startsWith("classpath:")) {
				// String loc = configLocation.replaceFirst("classpath:", "");
				// inStream =
				// Thread.currentThread().getContextClassLoader().getResourceAsStream(loc);
				// URL url =
				// Thread.currentThread().getContextClassLoader().getResource(loc);
				// url.openConnection().getOutputStream();
				// outStream = url.openConnection().getOutputStream();
				// } else {
				// outStream = new FileOutputStream(configLocation);
				// }
				outStream = new FileOutputStream(configLocation);
				store.store(outStream, "");
			} finally {
				outStream.close();
				// inStream.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.config.ConfigProperties#put(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	synchronized public void set(String key, String value) {
		store.setProperty(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.config.ConfigProperties#get(java.lang.String)
	 */
	@Override
	public String get(String key) {
		return store != null ? store.getProperty(key) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.config.ConfigProperties#keySet()
	 */
	@Override
	public Set<String> keySet() {
		return store != null ? store.stringPropertyNames() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.config.ConfigProperties#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String key) {
		return store != null ? store.containsKey(key) : false;
	}

	/**
	 * @return the configLocations
	 */
	public String getConfigLocation() {
		return configLocation;
	}

	/**
	 * @param configLocation
	 *            the configLocation to set
	 */
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

}
