/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.log;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.umeframework.dora.bean.BeanTransformer;

/**
 * Log Util
 *
 * @author Yue MA
 */
public class LogUtil {
    /**
     * system line separator
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * system line wrap
     */
    private static final String LINE_WRAP = "\n";
    /**
     * tab
     */
    private static final String TAB = "\t";
    /**
     * space
     */
    private static final String SPACE = " ";
    /**
     * double space
     */
    private static final String DOUBLE_SPACE = "  ";

    /**
     * Convert object to plant text format
     *
     * @param obj
     */
    public static String toPlantText(
            Object obj) {
        StringBuilder result = new StringBuilder();
        try {
            toPlantText(result, obj);
        } catch (Exception ex) {
            result.append(obj.toString());
        }
        return result.toString();
    }

    /**
     * Trim all line separator and TAB and double spaces
     *
     * @param text
     * @return
     */
    public static String toCompactFormat(String text) {
        text = text.replace(LINE_SEPARATOR, SPACE);
        text = text.replace(LINE_WRAP, SPACE);
        text = text.replace(TAB, SPACE);
        while (text.contains(DOUBLE_SPACE)) {
            text = text.replace(DOUBLE_SPACE, SPACE);
        }
        return text;
    }

    /**
     * toPlantText
     *
     * @param obj
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void toPlantText(
            StringBuilder result,
            Object obj) {
        if (obj == null) {
            result.append(String.valueOf(obj));
            return;
        }
        if (isBasicType(obj)) {
            result.append(String.valueOf(obj));
        } else if (obj instanceof Collection) {
            Iterator<?> itr = ((Collection<?>) obj).iterator();
            while (itr.hasNext()) {
                Object e = itr.next();
                toPlantText(result, e);
            }
        } else if (obj instanceof Map) {
            Iterator<Map.Entry> iter = ((Map) obj).entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry resultMap = iter.next();
                Object key = resultMap.getKey();
                Object value = resultMap.getValue();
                result.append(key + "=" + value + ",");
            }
        } else {
            Map dtoMap = BeanTransformer.getObjectFields(obj);
            Iterator<Map.Entry> iter = dtoMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Object> resultMap = iter.next();
                String key = resultMap.getKey();
                Object value = resultMap.getValue();
                result.append(key + "=" + value + ",");
            }

        }
    }
    /**
     * isBasicType
     *
     * @param obj
     * @return
     */
    private static boolean isBasicType(
            Object obj) {
        if (obj == null || obj.getClass().isPrimitive()) {
            return true;
        }

        String pkg = obj.getClass().getPackage().getName();
        if (pkg.startsWith("java.") || pkg.startsWith("javax.") || pkg.startsWith("org.")) {
            return true;
        }
        return false;
    }

}
