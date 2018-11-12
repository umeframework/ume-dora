/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.umeframework.dora.bean.BeanException;
import org.umeframework.dora.bean.BeanUtil;
import org.umeframework.dora.validation.format.Alias;

public class XmlRender {

    /**
     * nameMapping
     */
    private Map<String, String> nameMapping = new HashMap<String, String>();
    /**
     * addMapping
     * 
     * @param clazz
     * @param name
     */
    public void addMapping(Class<?> clazz, String name) {
        nameMapping.put(clazz.getName(), name);
    }
    /**
     * addMapping
     * 
     * @param clazz
     */
    public void addMapping(Class<?> clazz) {
        nameMapping.put(clazz.getName(), lowcaseFirstChar(clazz.getSimpleName()));
    }
    /**
     * addMapping
     * 
     * @param clazz
     * @param name
     */
    public void addMapping(String logicName, String name) {
        nameMapping.put(logicName, name);
    }
    

    /**
     * render
     * 
     * @param bean
     * @return
     * @throws BeanException
     */
    public String render(
            Object bean) throws BeanException {
        StringBuilder resultBuilder = new StringBuilder();
        try {
            render(resultBuilder, "bean", null, bean);
        } catch (Exception e) {
        	throw new BeanException("Failed in render bean to XML.", e);
        }
        return resultBuilder.toString();
    }

    /**
     * render
     * 
     * @param resultBuilder
     * @param name
     * @param alias
     * @param value
     * @throws Exception
     */
    protected void render(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Object value) throws Exception {
        if (value == null) {
            return;
        }
        alias = alias == null ? getClassNameMapping(name, value.getClass()) : alias;
        if (isPrimitive(value)) {
            renderPrimitive(resultBuilder, name, alias, value);
        } else if (isArray(value)) {
            renderArray(resultBuilder, name, alias, value);
        } else if (isCollection(value)) {
            renderCollection(resultBuilder, name, alias, (Collection<?>) value);
        } else if (isMap(value)) {
            renderMap(resultBuilder, name, alias, ((Map<?, ?>) value));
        } else if (isBean(value)) {
            renderBean(resultBuilder, name, alias, value);
        } else {
            renderOther(resultBuilder, name, alias, value);
        }
    }
    
    /**
     * getClassNameMapping
     * 
     * @param calzz
     * @return
     */
    protected String getClassNameMapping(String name, Class<?> calzz) {
        if (nameMapping.containsKey(name)) {
            return nameMapping.get(name);
        }
        if (nameMapping.containsKey(calzz.getName())) {
            return nameMapping.get(calzz.getName());
        }
        return calzz.getName();
    }

    /**
     * renderPrimitive
     * 
     * @param resultBuilder
     * @param name
     * @param alias
     * @param value
     */
    protected void renderPrimitive(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Object value) {
        resultBuilder.append("<");
        resultBuilder.append(alias);
        resultBuilder.append(">");
        resultBuilder.append(value);
        resultBuilder.append("</");
        resultBuilder.append(alias);
        resultBuilder.append(">");
    }

    /**
     * renderCollection
     * 
     * @param resultBuilder
     * @param name
     * @param alias
     * @param listObj
     * @throws Exception
     */
    protected void renderCollection(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Collection<?> listObj) throws Exception {
        resultBuilder.append("<");
        resultBuilder.append(alias);
        resultBuilder.append(">");

        Iterator<?> values = listObj.iterator();
        int i = 0;
        while (values.hasNext()) {
            Object value = values.next();
            String propLongName = name + "." + "[" + i + "]";
            render(resultBuilder, propLongName, null, value);
            i++;
        }
        resultBuilder.append("</");
        resultBuilder.append(alias);
        resultBuilder.append(">");
    }

    /**
     * renderArray
     * 
     * @param resultBuilder
     * @param name
     * @param alias
     * @param arrayObj
     * @throws Exception
     */
    protected void renderArray(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Object arrayObj) throws Exception {
        resultBuilder.append("<");
        resultBuilder.append(alias);
        resultBuilder.append(">");
        for (int i = 0; i < Array.getLength(arrayObj); i++) {
            Object value = Array.get(arrayObj, i);
            String propLongName = name + "." + "[" + i + "]";
            render(resultBuilder, propLongName, null, value);
        }
        resultBuilder.append("</");
        resultBuilder.append(alias);
        resultBuilder.append(">");
    }

    /**
     * renderMap
     * 
     * @param resultBuilder
     * @param name
     * @param alias
     * @param mapObj
     */
    protected void renderMap(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Map<?, ?> mapObj) {
        resultBuilder.append("<");
        resultBuilder.append(alias);
        resultBuilder.append(">");
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) mapObj).entrySet()) {
            resultBuilder.append("<property ");
            resultBuilder.append(" name=\"");
            resultBuilder.append(entry.getKey());
            resultBuilder.append("\"");
            resultBuilder.append(" value=\"");
            resultBuilder.append(entry.getValue());
            resultBuilder.append("\"");
            resultBuilder.append(" />");
        }
        resultBuilder.append("</");
        resultBuilder.append(alias);
        resultBuilder.append(">");
    }

    /**
     * renderBean
     * 
     * @param resultBuilder
     * @param name
     * @param beanObj
     * @throws Exception
     */
    protected void renderBean(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Object beanObj) throws Exception {
        if (beanObj == null) {
            return;
        }
        Alias anno = beanObj.getClass().getAnnotation(Alias.class);
        if (alias == null) {
            alias = anno != null ? anno.value() : getClassNameMapping(name, beanObj.getClass());
        }
        resultBuilder.append("<");
        resultBuilder.append(alias);
        resultBuilder.append(">");

        Map<String, Method> getters = BeanUtil.getGetters(beanObj.getClass());
        for (String propName : getters.keySet()) {
            if ("class".equals(propName)) {
                continue;
            }
            Field propField = beanObj.getClass().getDeclaredField(propName);
            propField.setAccessible(true);
            Alias propAliasAnno = propField.getAnnotation(Alias.class);
            Object propValue = propField.get(beanObj);
            String propLongName = name + "." + propName;
            String propAlias = propAliasAnno != null ? anno.value() : propName;
            render(resultBuilder, propLongName, propAlias, propValue);
        }
        resultBuilder.append("</");
        resultBuilder.append(alias);
        resultBuilder.append(">");
    }

    /**
     * renderPrimitive
     * 
     * @param resultBuilder
     * @param name
     * @param alias
     * @param value
     */
    protected void renderOther(
            StringBuilder resultBuilder,
            String name,
            String alias,
            Object value) {
        resultBuilder.append("<");
        resultBuilder.append(value.getClass().getName());
        resultBuilder.append(">");
        resultBuilder.append(value);
        resultBuilder.append("</");
        resultBuilder.append(value.getClass().getName());
        resultBuilder.append(">");
    }

    /**
     * isBean
     * 
     * @param clazz
     *            - class type
     * @return
     */
    protected boolean isBean(
            Object obj) {
        Class<?> clazz = obj.getClass();
        String pkg = clazz.getPackage().getName();
        if (pkg.startsWith("java.") || pkg.startsWith("javax.") || pkg.startsWith("org.")) {
            return false;
        }
        return true;
    }

    /**
     * isArray
     * 
     * @param obj
     * @return
     */
    protected boolean isArray(
            Object obj) {
        return obj.getClass().isArray();
    }

    /**
     * isCollection
     * 
     * @param obj
     * @return
     */
    protected boolean isCollection(
            Object obj) {
        return obj instanceof Collection;
    }

    /**
     * isMap
     * 
     * @param obj
     * @return
     */
    protected boolean isMap(
            Object obj) {
        return obj instanceof Map;
    }
    
    /**
     * lowcaseFirstChar
     * 
     * @param str
     * @return
     */
    protected String lowcaseFirstChar(String str) {
        return str.substring(0,1).toLowerCase() + str.substring(1);
    }

    /**
     * isPrimitive
     * 
     * @param obj
     * @return
     */
    protected boolean isPrimitive(
            Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.isPrimitive()) {
            return true;
        }

        if (SIMPLE_TYPES.contains(clazz)) {
            return true;
        }
        if (obj instanceof Number || obj instanceof java.util.Date) {
            return true;
        }
        return false;
    }

    /**
     * Define default format target types.
     */
    private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<Class<?>>(22);
    static {
        SIMPLE_TYPES.add(Boolean.class);
        SIMPLE_TYPES.add(Character.class);
        SIMPLE_TYPES.add(String.class);
        SIMPLE_TYPES.add(Long.class);
        SIMPLE_TYPES.add(Integer.class);
        SIMPLE_TYPES.add(Short.class);
        SIMPLE_TYPES.add(Double.class);
        SIMPLE_TYPES.add(Float.class);
        SIMPLE_TYPES.add(Byte.class);
    }

}
