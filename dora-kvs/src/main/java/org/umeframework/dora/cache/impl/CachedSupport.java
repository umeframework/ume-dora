/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.cache.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.umeframework.dora.service.BaseComponent;

/**
 * Cached common function provider
 *
 * @author Yue MA
 */
public abstract class CachedSupport extends BaseComponent {
    /**
     * Define allowable keys which can put into cache
     */
    private List<String> allowableKeys;
    /**
     * default expire time
     */
    private int expire = 0;

    /**
     * CachedSupport
     *
     * @param expire
     */
    public CachedSupport(int expire) {
        this.expire = expire;
    }

    /**
     * do set interface
     *
     * @param key
     * @param expire
     * @param value
     */
    abstract protected void doSet(
            String key,
            int expire,
            Object value);

    /**
     * do get interface
     *
     * @param key
     * @return
     */
    abstract protected Object doGet(
            String key);

    /**
     * do remove interface
     *
     * @param key
     * @return
     */
    abstract protected void doRemove(
            String key);

    /**
     * set common process
     *
     * @param key
     * @param expire
     * @param value
     * @return
     */
    public <E> long set(
            String key,
            int expire,
            E value) {
        if (!isAllowable(key) || value == null) {
            getLogger().warn("MemCache attempt cache unallowable object " + key + "=" + value);
            return 0;
        }
        doSet(key, expire, value);

        if (get(key) != null) {
            getLogger().debug("MemCache cached " + key + "=" + value);
            return 1;
        }
        return 0;
    }

    /**
     * set common process
     *
     * @param key
     * @param value
     * @return
     */
    public <E> long set(
            String key,
            E value) {
        return set(key, getExpire(), value);
    }

    /**
     * get common process
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> E get(
            String key) {
        Object obj = doGet(key);
        return (E) obj;
    }

    /**
     * remove common process
     *
     * @param key
     * @return
     */
    public long remove(
            String key) {
        Object value = get(key);
        if (value == null) {
            getLogger().warn("MemCache attempt remove non-cached object " + key);
            return 0;
        }
        doRemove(key);
        getLogger().debug("MemCache removed " + key + "=" + value);
        return 1;
    }

    /**
     * check if key is allowable
     *
     * @param key
     * @return
     */
    protected boolean isAllowable(
            String key) {
        if (allowableKeys == null) {
            return true;
        }
        return allowableKeys.contains(key);
    }

    /**
     * serialize
     *
     * @param object
     * @return
     */
    protected byte[] serialize(
            Object object) {
        ObjectOutputStream objOS = null;
        ByteArrayOutputStream byteOS = null;
        try {
            byteOS = new ByteArrayOutputStream();
            objOS = new ObjectOutputStream(byteOS);
            objOS.writeObject(object);
            byte[] bytes = byteOS.toByteArray();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * unserialize
     *
     * @param bytes
     * @return
     */
    protected Object unserialize(
            byte[] bytes) {
        ByteArrayInputStream byteIS = null;
        try {
            byteIS = new ByteArrayInputStream(bytes);
            ObjectInputStream objIS = new ObjectInputStream(byteIS);
            return objIS.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the allowableKeys
     */
    public List<String> getAllowableKeys() {
        return allowableKeys;
    }

    /**
     * @param allowableKeys
     *            the allowableKeys to set
     */
    public void setAllowableKeys(
            List<String> allowableKeys) {
        this.allowableKeys = allowableKeys;
    }

    /**
     * @return the expire
     */
    public int getExpire() {
        return expire;
    }

    /**
     * @param expire
     *            the expire to set
     */
    public void setExpire(
            int expire) {
        this.expire = expire;
    }

}
