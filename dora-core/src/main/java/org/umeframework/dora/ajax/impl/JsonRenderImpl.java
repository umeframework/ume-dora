/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.umeframework.dora.ajax.AjaxRender;
import org.umeframework.dora.ajax.ParserException;
import org.umeframework.dora.util.StringUtil;

/**
 * Json render interface implementation.<br>
 * 
 * @author Yue MA
 */
public class JsonRenderImpl extends JsonSupport implements AjaxRender<String> {
    /**
     * ignoreNull
     */
    private boolean ignoreNull = true;
    /**
     * ignoreSpace
     */
    private boolean ignoreSpace = true;
    /**
     * arrayMaxRenderSize
     */
    private int arrayMaxRenderSize = Short.MAX_VALUE;

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#render(java.lang.Object)
     */
    @Override
    public String render(Object obj) {
        if (obj == null) {
            return JSON_NULL_STR;
        }

        StringBuilder buf = new StringBuilder();
        try {
            String json = null;
            if (isBasicInstance(obj)) {
                json = renderBasic(obj);
                buf.append(json);
            } else if (isArrayInstance(obj)) {
                json = renderArray(obj);
                buf.append(json);
            } else if (isListInstance(obj)) {
                json = renderList((Collection<?>) obj);
                buf.append(json);
            } else if (isMapInstance(obj)) {
                json = renderMap((Map<?, ?>) obj);
                buf.append(json);
            } else {
                json = renderBean(obj);
                buf.append(json);
                // // Do not render for Unsupported JSON type
                // buf.append(JSON_NULL_STR);
            }
        } catch (Exception e) {
            throw new ParserException("Unexpected exception in JSONRender." + e);
        }

        return buf.toString();
    }

    /**
     * renderBean
     * 
     * @param beanObj
     * @return
     * @throws Exception
     */
    protected String renderBean(Object beanObj) throws Exception {
        if (beanObj == null) {
            return JSON_NULL_STR;
        }
        Class<?> beanClazz = beanObj.getClass();
        BeanInfo info;

        info = Introspector.getBeanInfo(beanClazz);
        PropertyDescriptor[] propDescriptors = info.getPropertyDescriptors();

        StringBuilder buf = new StringBuilder();

        buf.append(JSON_MAP_BEGIN);
        int sequence = 0;
        for (PropertyDescriptor property : propDescriptors) {
            String name = property.getName();
            Class<?> propClazz = property.getPropertyType();
            if (Class.class.equals(propClazz)) {
                // ignore value which extends of java.lang.Object
                continue;
            }
            Method getter = property.getReadMethod();
            if (getter == null) {
                continue;
            }
            Object value = property.getReadMethod().invoke(beanObj, NULL_GETTER_PARAMS);

            if (value == null && ignoreNull) {
                continue;
            }

            if (sequence++ > 0) {
                buf.append(JSON_ELEMENT_SEP);
            }
            buf.append(JSON_PROPERTY_AROUND);
            buf.append(name);
            buf.append(JSON_PROPERTY_AROUND);
            buf.append(JSON_PROPERTY_SUFFIX);

            if (value == null) {
                buf.append(JSON_NULL_STR);
                continue;
            }

            String json = null;

            if (isBasicInstance(value)) {
                json = renderBasic(value);
                buf.append(json);
            } else if (isArrayInstance(value)) {
                json = renderArray(value);
                buf.append(json);
            } else if (isListInstance(value)) {
                json = renderList((Collection<?>) value);
                buf.append(json);
            } else if (isMapInstance(value)) {
                json = renderMap((Map<?, ?>) value);
                buf.append(json);
            } else {
                json = renderBean(value);
                buf.append(json);
                // buf.append("Unsupport data type in JSONRender:" + beanClazz +
                // "." + name + " [" + value.getClass() + "].");
            }
        }

        buf.append(JSON_MAP_END);
        return buf.toString();
    }

    /**
     * renderBasic
     * 
     * @param value
     * @return
     * @throws Exception
     */
    protected String renderBasic(Object value) throws Exception {
        if (value == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        if (value instanceof java.lang.String || value instanceof java.lang.Character || value.getClass().equals(char.class)) {
            buf.append(JSON_TEXT_VALUE_AROUND);
            if (ignoreSpace) {
                buf.append(stringToJson(StringUtil.trimR(String.valueOf(value))));
            } else {
                buf.append(stringToJson(String.valueOf(value)));
            }
            buf.append(JSON_TEXT_VALUE_AROUND);
        } else if (java.util.Date.class.equals(value.getClass())) {
            buf.append(JSON_TEXT_VALUE_AROUND);
            buf.append(getDefaultDatetimeFormat().format((java.util.Date) value));
            buf.append(JSON_TEXT_VALUE_AROUND);
        } else if (value instanceof java.sql.Date) {
            buf.append(JSON_TEXT_VALUE_AROUND);
            buf.append(String.valueOf(value));
            buf.append(JSON_TEXT_VALUE_AROUND);
        } else if (value instanceof java.sql.Time) {
            buf.append(JSON_TEXT_VALUE_AROUND);
            buf.append(String.valueOf(value));
            buf.append(JSON_TEXT_VALUE_AROUND);
        } else if (value instanceof java.sql.Timestamp) {
            buf.append(JSON_TEXT_VALUE_AROUND);
            // buf.append(String.valueOf(value));
            buf.append(getDefaultDatetimeFormat().format((java.util.Date) value));
            buf.append(JSON_TEXT_VALUE_AROUND);
        } else {
            buf.append(String.valueOf(value));
        }
        return buf.toString();
    }

    /**
     * stringToJson
     * 
     * @param strValue
     * @return
     */
    protected String stringToJson(String strValue) {
        if (strValue == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strValue.length(); i++) {
            char ch = strValue.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                // if (ch <= '\u001F' || ch >= '\u007F') {
                // String ss = Integer.toHexString(ch);
                // sb.append("\\u");
                // for (int k = 0; k < 4 - ss.length(); k++) {
                // sb.append('0');
                // }
                // sb.append(ss.toUpperCase());
                // } else {
                sb.append(ch);
                // }
            }
        }
        return sb.toString();
    }

    /**
     * renderArray
     * 
     * @param arrObj
     * @return
     * @throws Exception
     */
    protected String renderArray(Object arrObj) throws Exception {
        StringBuilder buf = new StringBuilder();

        buf.append(JSON_ARRAY_BEGIN);
        for (int i = 0; i < Array.getLength(arrObj); i++) {
            if (i > 0) {
                buf.append(JSON_ELEMENT_SEP);
            }
            if (i >= arrayMaxRenderSize) {
                break;
            }
            Object value = Array.get(arrObj, i);
            buf.append(render(value));
        }

        buf.append(JSON_ARRAY_END);
        return buf.toString();
    }

    /**
     * renderList
     * 
     * @param lstObj
     * @return
     * @throws Exception
     */
    protected String renderList(Collection<?> lstObj) throws Exception {
        return renderArray(lstObj.toArray());
    }

    /**
     * renderMap
     * 
     * @param mapObj
     * @return
     * @throws Exception
     */
    protected String renderMap(Map<?, ?> mapObj) throws Exception {
        StringBuilder buf = new StringBuilder();
        // Append begin flag
        buf.append(JSON_MAP_BEGIN);

        int sequence = 0;
        for (Map.Entry<?, ?> entry : mapObj.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if ("class".equals(key)) {
                continue;
            }

            if (value == null && ignoreNull) {
                // ignore null value
                continue;
            }

            if (sequence++ > 0) {
                buf.append(JSON_ELEMENT_SEP);
            }
            // buf.append(render(key));
            buf.append(render(key.toString()));
            buf.append(JSON_PROPERTY_SUFFIX);
            buf.append(render(value));
        }
        // Append end flag
        buf.append(JSON_MAP_END);
        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#isIgnoreNull()
     */
    public boolean isIgnoreNull() {
        return ignoreNull;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#setIgnoreNull(boolean)
     */
    public void setIgnoreNull(boolean ignoreNull) {
        this.ignoreNull = ignoreNull;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#isIgnoreSpace()
     */
    public boolean isIgnoreSpace() {
        return ignoreSpace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#setIgnoreSpace(boolean)
     */
    public void setIgnoreSpace(boolean ignoreSpace) {
        this.ignoreSpace = ignoreSpace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#getArrayMaxRenderSize()
     */
    public int getArrayMaxRenderSize() {
        return arrayMaxRenderSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.core.json.AJAXRender#setArrayMaxRenderSize(int)
     */
    public void setArrayMaxRenderSize(int arrayMaxRenderSize) {
        this.arrayMaxRenderSize = arrayMaxRenderSize;
    }

}
