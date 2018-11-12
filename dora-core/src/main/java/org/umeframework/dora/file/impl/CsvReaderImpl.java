/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.file.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.umeframework.dora.bean.BeanUtil;
import org.umeframework.dora.exception.FileAccessException;
import org.umeframework.dora.file.FileConfig;
import org.umeframework.dora.file.FileReader;
import org.umeframework.dora.util.DateUtil;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.validation.format.Index;

/**
 * CSV File Reader implement class
 * 
 * @author Yue MA
 */
public class CsvReaderImpl implements FileReader {
    /**
     * setterMap
     */
    private Map<String, Method> setterMap;
    /**
     * fieldColumnMap
     */
    private Map<String, String> fieldColumnMap;

    /*
     * (non-Javadoc)
     * 
     * @see tora.file.FileReader#read(java.io.InputStream, java.lang.Class)
     */
    public <E> List<E> read(
            InputStream csvFile,
            Class<E> clazz) {
        return read(csvFile, clazz, new FileConfig());
    }

    /*
     * (non-Javadoc)
     * 
     * @see tora.file.FileReader#read(java.io.InputStream, java.lang.Class,
     * tora.file.FileConfig)
     */
    public <E> List<E> read(
            InputStream csvFile,
            Class<E> clazz,
            FileConfig config) {
        List<E> resultDtos = new ArrayList<E>();

        // line number count
        int lineNo = 1;
        // line object
        String line = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(csvFile, config.getCharSet()));
            // read line
            line = reader.readLine();

            // init dto class info
            init(clazz);

            while (line != null) {
                List<String> items = parseLine(line, config);

                // create dto instance
                E dto = (E) clazz.newInstance();
                // parsing line
                for (int i = 0; i < fieldColumnMap.size() && i < items.size(); i++) {
                    // get dto field name
                    String fieldName = fieldColumnMap.get(String.valueOf(i));
                    // get dto field setter
                    Method setter = setterMap.get(fieldName);
                    // get dto field setter parameter type
                    Class<?>[] paramTypes = setter.getParameterTypes();
                    if (paramTypes.length > 0) {
                        Class<?> paramType = paramTypes[0];
                        setPropertyValue(dto, setter, items.get(i).trim(), paramType);
                    }
                }

                resultDtos.add(dto);
                line = reader.readLine();
                lineNo++;
            }

        } catch (Exception e) {
            throw new FileAccessException(e, e.getMessage(), new Object[] {line, lineNo});
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new FileAccessException(e, line);
                }
            }
        }
        return resultDtos;
    }

    /**
     * init
     * 
     * @param clazz
     * @throws FileAccessException
     */
    protected void init(
            Class<?> clazz) {
        setterMap = BeanUtil.getSetters(clazz);
        fieldColumnMap = new HashMap<String, String>();

        for (String fieldName : setterMap.keySet()) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                throw new FileAccessException(e, e.getMessage());
            }

            Index columnIndexAnnotation = field.getAnnotation(Index.class);
            // Alias columnNameAnnotation = field.getAnnotation(Alias.class);

            String fieldNameKey = columnIndexAnnotation != null ? String.valueOf(columnIndexAnnotation.value()) : fieldName;
            fieldColumnMap.put(fieldNameKey, fieldName);
        }
    }

    /**
     * parseLine
     * 
     * @param line
     * @param config
     * @return
     */
    protected List<String> parseLine(
            String line,
            FileConfig config) {
        List<String> list = new ArrayList<String>();
        char[] chars = line.toCharArray();
        StringBuffer token = new StringBuffer();
        boolean isEscaped = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == config.getSeparatorChar().charAt(0)) {
                if (isEscaped) {
                    token.append(chars[i]);
                } else {
                    list.add(token.toString());
                    token = new StringBuffer();
                }
            } else if (chars[i] == config.getTransferChar().charAt(0)) {
                if ((i != chars.length - 1) && (chars[i + 1] == config.getTransferChar().charAt(0)) && isEscaped) {
                    token.append(chars[i]);
                    i++;
                } else {
                    isEscaped = !isEscaped;
                }
            } else {
                token.append(chars[i]);
            }

            if (i == chars.length - 1) {
                list.add(token.toString());
            }
        }
        return list;
    }

    /**
     * setPropertyValue
     * 
     * @param obj
     * @param setter
     * @param value
     * @param valueType
     */
    protected void setPropertyValue(
            Object obj,
            Method setter,
            String value,
            Class<?> valueType) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (value == null) {
            setter.invoke(obj, new Object[] { null });
            return;
        }
        value = value.trim();

        if (String.class.equals(valueType)) {
            setter.invoke(obj, new Object[] { value });
        } else if ((Integer.class.equals(valueType) || int.class.equals(valueType)) && !"".equals(value)) {
            setter.invoke(obj, new Object[] { Integer.valueOf(value) });
        } else if ((Long.class.equals(valueType) || long.class.equals(valueType)) && !"".equals(value)) {
            setter.invoke(obj, new Object[] { Long.valueOf(value) });
        } else if ((Short.class.equals(valueType) || short.class.equals(valueType)) && !"".equals(value)) {
            setter.invoke(obj, new Object[] { Short.valueOf(value) });
        } else if (Character.class.equals(valueType) || char.class.equals(valueType)) {
            setter.invoke(obj, new Object[] { value.charAt(0) });
        } else if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
            setter.invoke(obj, new Object[] { StringUtil.toBoolean(value) });
        } else if (Date.class.equals(valueType)) {
            setter.invoke(obj, new Object[] { DateUtil.parseToDate(value, DateUtil.FORMAT.YYYYMMDDHHMMSSMMM) });
        } else if (java.math.BigInteger.class.equals(valueType)) {
            setter.invoke(obj, new Object[] { new BigInteger(value) });
        } else if (java.math.BigDecimal.class.equals(valueType)) {
            setter.invoke(obj, new Object[] { new BigDecimal(value) });
        }
    }

}
