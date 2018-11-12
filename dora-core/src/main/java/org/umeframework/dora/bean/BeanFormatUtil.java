/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.umeframework.dora.bean.BeanException;
import org.umeframework.dora.bean.BeanUtil;

/**
 * Inspect all bean's properties and process each property with Formatter
 * instance.<br>
 * 
 * @author Yue MA
 */
abstract public class BeanFormatUtil {
    /**
     * Bean Formatter interface declare.
     */
    public interface Formatter {
        /**
         * entry method of format
         * 
         * @param fieldName
         *            - name of field
         * @param fieldValue
         *            - value of field
         * @param fieldType
         *            - java type of field
         * @param annotations
         *            - annotations define on field
         * @return formatted field value
         */
        public Object format(
                String fieldName,
                Object fieldValue,
                Class<?> fieldType,
                Annotation[] annotations);
    }

    /**
     * Entry method of BeanFormatUtil.<br>
     * 
     * @param bean
     *            - bean instance for format
     * @param formatters
     *            - formatters implementation list
     * @return formatted bean instance
     * @throws BeanException
     */
    public static <E> E format(
            E bean,
            Formatter... formatters) throws BeanException {
        E result = null;
        try {
            result = formatProperties(null, bean, null, null, formatters);
        } catch (Exception e) {
            throw new BeanException("Fail to format bean properties " + bean, e);
        }
        return result;
    }

    /**
     * Internal format process for bean instance.<br>
     * 
     * @param objName
     *            - name of object
     * @param objValue
     *            - value of object
     * @param objClazz
     *            - type of object
     * @param annos
     *            - annotations define on object
     * @param formatters
     *            - formatters
     * @return formatted object value
     */
    @SuppressWarnings("unchecked")
    private static <E> E formatProperties(
            String objName,
            E objValue,
            Class<?> objClazz,
            Annotation[] annos,
            Formatter[] formatters) {
        if (objClazz == null && objValue == null) {
            return null;
        }

        objClazz = objClazz == null ? objValue.getClass() : objClazz;
        if (formatTargets.contains(objClazz)) {
            for (Formatter formatter : formatters) {
                try {
                    objValue = (E) formatter.format(objName, objValue, objClazz, annos);
                } catch (Exception ex) {
                    throw new BeanException("Error during format " + objName + ":" + objValue + " by " + formatter.getClass().getName(), ex);
                }
            }
        } else if (objClazz.isArray()) {
            Object array = objValue;
            for (int i = 0; i < Array.getLength(array); i++) {
                Object obj = Array.get(array, i);
                Class<?> objCls = obj == null ? null : obj.getClass();
                obj = formatProperties(objName + "(" + i + ")", obj, objCls, annos, formatters);
                Array.set(array, i, obj);
            }
        } else if (LIST_TYPES.contains(objClazz)) {
            List<Object> list = (List<Object>) objValue;
            for (int i = 0; i < list.size(); i++) {
                Object obj = list.get(i);
                Class<?> objCls = obj == null ? null : obj.getClass();
                obj = formatProperties(objName + "(" + i + ")", obj, objCls, annos, formatters);
                list.set(i, obj);
            }
        } else if (MAP_TYPES.contains(objClazz)) {
            Map<Object, Object> map = (Map<Object, Object>) objValue;

            for (Object entry : map.entrySet()) {
                Object key = ((Map.Entry<Object, Object>) entry).getKey();
                Object obj = ((Map.Entry<Object, Object>) entry).getValue();
                Class<?> objCls = obj == null ? null : obj.getClass();
                obj = formatProperties(objName + "(" + key + ")", obj, objCls, null, formatters);
                map.put(key, obj);
            }
        } else if (isNotBasicType(objClazz)) {
            try {
                Map<String, Method> getters = BeanUtil.getGetters(objClazz);

                for (Map.Entry<String, Method> entry : getters.entrySet()) {
                    String name = entry.getKey();
                    if ("class".equals(name)) {
                        continue;
                    }

                    Field field = null;
                    try {
                        field = objClazz.getDeclaredField(name);
                    } catch (NoSuchFieldException e) {
                        Class<?> clazz = objClazz.getSuperclass();
                        while (!clazz.equals(java.lang.Object.class) && field == null) {
                            field = clazz.getDeclaredField(name);
                            clazz = clazz.getSuperclass();
                        }
                    }
                    if (field != null) {
                        field.setAccessible(true);
                        Class<?> fieldType = field.getType();
                        Annotation[] fieldAnnos = field.getAnnotations();
                        Object fieldValue = field.get(objValue);
                        fieldValue = formatProperties(field.getName(), fieldValue, fieldType, fieldAnnos, formatters);
                        field.set(objValue, fieldValue);
                    }
                }
            } catch (Exception ex) {
                throw new BeanException("Fail to format bean properties ", ex);
            }
        }
        return (E) objValue;
    }

    /**
     * Check if input object is the business bean class.<br>
     * 
     * @param clazz
     *            - class type
     * @return
     */
    public static boolean isNotBasicType(
            Class<?> clazz) {
        String pkg = clazz.getPackage().getName();
        if (pkg.startsWith("java.") || pkg.startsWith("javax.") || pkg.startsWith("org.")) {
            return false;
        }
        return true;
    }

    /**
     * Define default format target types.
     */
    private static final Set<Class<?>> formatTargets = new HashSet<Class<?>>(22);
    static {
        formatTargets.add(String.class);
        formatTargets.add(int.class);
        formatTargets.add(long.class);
        formatTargets.add(short.class);
        formatTargets.add(double.class);
        formatTargets.add(float.class);
        formatTargets.add(char.class);
        formatTargets.add(boolean.class);
        formatTargets.add(byte.class);

        formatTargets.add(BigInteger.class);
        formatTargets.add(Long.class);
        formatTargets.add(Integer.class);
        formatTargets.add(Short.class);
        formatTargets.add(BigDecimal.class);
        formatTargets.add(Double.class);
        formatTargets.add(Float.class);
        formatTargets.add(Byte.class);
        formatTargets.add(Character.class);

        formatTargets.add(java.util.Date.class);
        formatTargets.add(java.sql.Date.class);
        formatTargets.add(java.sql.Time.class);
        formatTargets.add(java.sql.Timestamp.class);
    }

    /**
     * Define default format target types for List type.
     */
    private static final Set<Class<?>> LIST_TYPES = new HashSet<Class<?>>(3);
    static {
        LIST_TYPES.add(List.class);
        LIST_TYPES.add(ArrayList.class);
        LIST_TYPES.add(LinkedList.class);
    }

    /**
     * Define default format target types for Map type.
     */
    private static final Set<Class<?>> MAP_TYPES = new HashSet<Class<?>>(3);
    static {
        MAP_TYPES.add(Map.class);
        MAP_TYPES.add(HashMap.class);
        MAP_TYPES.add(LinkedHashMap.class);
    }
}
