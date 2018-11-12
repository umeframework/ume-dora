/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.umeframework.dora.bean.BeanUtil;

/**
 * Collection type common process tool
 * 
 * @author Yue MA
 */
abstract public class CollectionUtil {
    /**
     * isEmpty
     * 
     * @param value
     * @return
     */
    public static boolean isEmpty(
            Collection<?> value) {
        return value == null || value.isEmpty();
    }

    /**
     * isNotEmpty
     * 
     * @param value
     * @return
     */
    public static boolean isNotEmpty(
            Collection<?> value) {
        return value != null && !value.isEmpty();
    }

    /**
     * isNotEmpty
     * 
     * @param value
     * @return
     */
    public static <E> boolean isNotEmpty(
            E[] value) {
        return value != null && value.length > 0;
    }

    /**
     * isEmpty
     * 
     * @param value
     * @return
     */
    public static <E> boolean isEmpty(
            E[] value) {
        return value == null || value.length == 0;
    }

    /**
     * sortAsc
     * 
     * @param value
     * @param keys
     * @return
     */
    public static <E extends Comparable<? super E>> List<E> sortAsc(
            List<E> value,
            final String... keys) {
        return sort(false, value, keys);
    }

    /**
     * sortDesc
     * 
     * @param value
     * @param keys
     * @return
     */
    public static <E extends Comparable<? super E>> List<E> sortDesc(
            List<E> value,
            final String... keys) {
        return sort(true, value, keys);
    }

    /**
     * sort
     * 
     * @param desc
     * @param value
     * @param keys
     * @return
     */
    private static <E extends Comparable<? super E>> List<E> sort(
            final boolean desc,
            List<E> value,
            final String... keys) {
        Collections.sort(value, new Comparator<E>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(
                    E element1,
                    E element2) {
                if (element1 == null || element2 == null) {
                    // return null when met null value
                    return 0;
                }

                if (keys == null) {
                    return element1.compareTo(element2);
                }

                for (int i = 0; i < keys.length; i++) {
                    Object obj1 = element1 instanceof Map ? ((Map<?, ?>) element1).get(keys[i]) : BeanUtil.getBeanProperty(element1, keys[i]);
                    Object obj2 = element2 instanceof Map ? ((Map<?, ?>) element2).get(keys[i]) : BeanUtil.getBeanProperty(element2, keys[i]);
                    if (obj1 == null || obj2 == null) {
                        continue;
                    }
                    if (obj1 instanceof Comparable<?> && obj2 instanceof Comparable<?>) {
                        @SuppressWarnings("rawtypes")
                        int result = ((Comparable) obj1).compareTo((Comparable) obj2);
                        if (result == 0) {
                            continue;
                        }
                        if (desc) {
                            return result > 0 ? -1 : 1;
                        } else {
                            return result > 0 ? 1 : -1;
                        }
                    }
                    // skill values which can not compare
                    continue;
                }
                if (desc) {
                    return element1.compareTo(element2) > 0 ? -1 : 1;
                } else {
                    return element1.compareTo(element2) > 0 ? 1 : -1;
                }
            }
        });
        return value;
    }

}
