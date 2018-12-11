/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConversionException;

/**
 * RequestContext
 * 
 * @author Yue MA
 */
public class RequestContext<T> {
    /**
     * ThreadLocal instance
     */
    private static final ThreadLocal<RequestContext<?>> contextHolder = new InheritableThreadLocal<RequestContext<?>>();
    /**
     * Sync control flag
     */
    private static final byte[] block = new byte[0];

    /**
     * value container
     */
    private Map<T, Object> valueMap;

    /**
     * Create new SessionContext instance
     *
     * @return
     */
    public static <E> RequestContext<E> open() {
        synchronized (block) {
            @SuppressWarnings("unchecked")
            RequestContext<E> context = (RequestContext<E>) contextHolder.get();
            if (context == null) {
                context = new RequestContext<E>();
                contextHolder.set(context);
            }
            return context;
        }
    }

    /**
     * Close current SessionContext instance
     */
    public static void close() {
        RequestContext<?> context = contextHolder.get();
        synchronized (block) {
            if (context != null) {
                context.valueMap.clear();
            }
            contextHolder.set(null);
            contextHolder.remove();
        }
    }

    /**
     * Copy from another SessionContext instance
     * 
     * @param tar
     */
    public static <E> RequestContext<E> openFrom(RequestContext<E> src) {
        synchronized (block) {
            RequestContext<E> context = open();
            context.valueMap = src.valueMap;
            return context;
        }
    }

    /**
     * copy
     * 
     * @return
     */
    public RequestContext<T> copy() {
        RequestContext<T> context = new RequestContext<T>();
        context.valueMap = this.valueMap;
        return context;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("unchecked")
    public RequestContext<T> clone() {
        RequestContext<T> context = new RequestContext<T>();
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
                context.valueMap = (Map<T, Object>) (in.readObject());
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
    }

    /**
     * set
     * 
     * @param key
     * @param value
     */
    public <E> void set(T key, E value) {
        this.valueMap.put(key, value);
    }

    /**
     * remove
     * 
     * @param key
     */
    public <E> void remove(T key) {
        this.valueMap.remove(key);
    }

    /**
     * get
     * 
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> E get(T key) {
        return (E) this.valueMap.get(key);
    }

    /**
     * keySet
     * 
     * @return
     */
    public Set<T> keySet() {
        return this.valueMap.keySet();
    }

}
