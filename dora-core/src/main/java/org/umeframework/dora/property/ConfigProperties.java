/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.property;

import java.io.IOException;
import java.util.Set;

/**
 * ConfigProperties
 * 
 * @author Yue MA
 */
public interface ConfigProperties {
	/**
	 * load
	 * 
	 * @throws IOException
	 */
	void load() throws IOException;
	
	/**
	 * save
	 * 
	 * @throws IOException
	 */
	void save() throws IOException;
	
    /**
     * set
     * 
     * @param key
     * @param value
     * @return
     */
    void set(
    		String key,
            String value);

    /**
     * get
     * 
     * @param key
     */
    String get(
    		String key);

    /**
     * Get all keys as Set
     * 
     * @return
     */
    Set<String> keySet();

    /**
     * Contains Key
     * 
     * @return
     */
    boolean containsKey(
    		String key);
}
