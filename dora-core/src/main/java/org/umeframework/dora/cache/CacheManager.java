/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.cache;

import java.util.Set;

/**
 * CacheManager
 *
 * @author Yue MA
 */
public interface CacheManager {
    /**
     * set object to cache
     *
     * @param key
     * @param value
     * @return
     */
    <E> long set(
            String key,
            E value);

    /**
     * set object to cache
     *
     * @param key
     * @param expire
     * @param value
     */
    <E> long set(
            String key,
            int expire,
            E value);

    /**
     * Get object instances from cache
     *
     * @param key
     * @return
     */
    <E> E get(
            String key);

    /**
     * Remove object instances from cache
     *
     * @param key
     * @return
     */
    long remove(
            String key);

    /**
     * Show all keys
     * 
     * @return
     */
    Set<String> keys();

    /**
     * init
     */
    void init();

    /**
     * shutdown
     */
    void shutdown();
}
