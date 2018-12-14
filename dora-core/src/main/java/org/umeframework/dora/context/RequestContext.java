/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConversionException;

/**
 * RequestContext
 * 
 * @author Yue MA
 */
public class RequestContext implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2364647643350116822L;
    /**
     * ThreadLocal instance
     */
    private static final ThreadLocal<RequestContext> contextHolder = new InheritableThreadLocal<RequestContext>();
    /**
     * Sync control flag
     */
    private static final byte[] block = new byte[0];

    /**
     * value container
     */
    private Map<String, Serializable> valueMap;

    /**
     * transient value container
     */
    private Map<String, Object> transientValueMap;

    /**
     * copyFrom
     * 
     * @param src
     * @return
     */
    public static RequestContext copyFrom(RequestContext src) {
        synchronized (block) {
            contextHolder.set(src.copy());
            return contextHolder.get();
        }
    }

    /**
     * openFrom
     * 
     * @param src
     * @return
     */
    public static RequestContext cloneFrom(RequestContext src) {
        synchronized (block) {
            contextHolder.set(src.deepCopy());
            return contextHolder.get();
        }
    }

    /**
     * Close current instance
     * 
     * @return
     */
    public static RequestContext getCurrentContext() {
        synchronized (block) {
            RequestContext context = contextHolder.get();
            if (context == null) {
                contextHolder.set(new RequestContext());
                context = contextHolder.get();
            }
            return context;
        }
    }

    /**
     * Close current instance
     */
    public static void close() {
        synchronized (block) {
            // Can not clear valueMap because Sub Threads in using when start task
            // RequestContext context = contextHolder.get();
            // if (context != null) {
            // context.valueMap.clear();
            // }
            contextHolder.set(null);
            contextHolder.remove();
        }
    }

    /**
     * Reset current instance values
     */
    public static void reset() {
        synchronized (block) {
            RequestContext context = contextHolder.get();
            if (context != null) {
                context.valueMap.clear();
                context.transientValueMap.clear();
            }
        }
    }

    /**
     * copy
     * 
     * @return
     */
    public RequestContext copy() {
        RequestContext context = new RequestContext();
        context.valueMap = this.valueMap;
        context.transientValueMap = this.transientValueMap;
        return context;
    }

    /**
     * deepCopy
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public RequestContext deepCopy() {
        RequestContext context = new RequestContext();
        // Deep copy
        if (this.valueMap == null) {
            context.valueMap = null;
        } else {
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                out = new ObjectOutputStream(bos);
                out.writeObject(this.valueMap);
                out.flush();
                in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
                context.valueMap = (Map<String, Serializable>) (in.readObject());
            } catch (Exception e) {
                throw new ConversionException(e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    throw new ConversionException(e);
                }
            }
        }
        return context;
    }

    /**
     * RequestContext
     */
    private RequestContext() {
        this.valueMap = new HashMap<>();
        this.transientValueMap = new HashMap<>();
    }

    /**
     * set
     * 
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        if (value instanceof Serializable) {
            valueMap.put(key, (Serializable) value);
        } else {
            transientValueMap.put(key, value);
        }
    }

    /**
     * remove
     * 
     * @param key
     */
    public void remove(String key) {
        if (valueMap.containsKey(key)) {
            valueMap.remove(key);
        }
        if (transientValueMap.containsKey(key)) {
            transientValueMap.remove(key);
        }
    }

    /**
     * get
     * 
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> E get(String key) {
        if (valueMap.containsKey(key)) {
            return (E) valueMap.get(key);
        }
        if (transientValueMap.containsKey(key)) {
            return (E) transientValueMap.get(key);
        }
        return null;
    }

    /**
     * keySet
     * 
     * @return
     */
    public List<String> keySet() {
        Set<String> keySet1 = valueMap.keySet();
        Set<String> keySet2 = transientValueMap.keySet();
        List<String> keySet = new ArrayList<>(keySet1.size() + keySet2.size());
        keySet.addAll(keySet1);
        keySet.addAll(keySet2);
        return keySet;
    }
}
