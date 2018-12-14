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
import java.util.HashMap;
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
    private Map<String, Object> valueMap;

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
            try {
                context.valueMap = (Map<String, Object>) (deepCopyObject(valueMap));
            } catch (Exception deepCopyError) {
                Map<String, Object> serializableValueMap = new HashMap<>();
                for (Map.Entry<String, Object> e : this.valueMap.entrySet()) {
                    Object value = e.getValue();
                    try {
                        deepCopyObject(value);
                        serializableValueMap.put(e.getKey(), value);
                    } catch (Exception deepCopyElementError) {
                        // Skip unSerializable object
                    }
                }
                try {
                    context.valueMap = (Map<String, Object>) (deepCopyObject(serializableValueMap));
                } catch (Exception deepCopySerializableValueMapError) {
                    throw new ConversionException(deepCopySerializableValueMapError);
                }
            }
        }
        return context;
    }

    /**
     * deepCopyObject
     * 
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object deepCopyObject(Object src) throws IOException, ClassNotFoundException {
        if (src == null) {
            return null;
        }
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(src);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return in.readObject();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
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
    public void set(String key, Object value) {
        valueMap.put(key, value);
    }

    /**
     * remove
     * 
     * @param key
     */
    public void remove(String key) {
        valueMap.remove(key);
    }

    /**
     * get
     * 
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <E> E get(String key) {
        return (E) valueMap.get(key);
    }

    /**
     * keySet
     * 
     * @return
     */
    public Set<String> keySet() {
        return valueMap.keySet();
    }
}
