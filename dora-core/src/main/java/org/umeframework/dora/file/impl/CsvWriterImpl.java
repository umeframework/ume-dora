/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.file.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.umeframework.dora.bean.BeanUtil;
import org.umeframework.dora.exception.FileAccessException;
import org.umeframework.dora.file.FileConfig;
import org.umeframework.dora.file.FileWriter;
import org.umeframework.dora.util.DateUtil;
import org.umeframework.dora.util.StringUtil;
import org.umeframework.dora.validation.format.Alias;
import org.umeframework.dora.validation.format.Index;

/**
 * CSV File Writer implement class
 * 
 * @author Yue MA
 */
public class CsvWriterImpl implements FileWriter {
    /**
     * titles
     */
    private List<String> titles;
    /**
     * getterMap
     */
    private Map<String, Method> getterMap;
    /**
     * fieldColumnMap
     */
    private Map<String, String> fieldColumnMap;

    /*
     * (non-Javadoc)
     * 
     * @see tora.file.FileWriter#write(java.io.OutputStream, java.util.List)
     */
    public <E> void write(
            OutputStream csvFile,
            List<E> dtos) throws IOException {
        write(csvFile, dtos, new FileConfig());
    }

    /*
     * (non-Javadoc)
     * 
     * @see tora.file.FileWriter#write(java.io.OutputStream, java.util.List,
     * tora.file.FileConfig)
     */
    public <E> void write(
            OutputStream csvFile,
            List<E> dtos,
            FileConfig config) throws IOException {
        if (dtos == null || dtos.size() == 0) {
            return;
        }

        int lineNo = 1;
        List<Object> lineItems = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(csvFile, config.getCharSet()));

            for (E dto : dtos) {
                if (dto != null) {
                    init(dto.getClass());
                    break;
                }
            }

            for (E dto : dtos) {
                lineItems = new ArrayList<Object>();
                readValues(lineItems, dto, config);
                writeLine(writer, formatLine(lineItems, config));
            }
        } catch (Exception e) {
            StringBuilder exLine = new StringBuilder();
            if (lineItems != null) {
                for (Object i : lineItems) {
                    exLine.append(String.valueOf(i));
                }
            }
            throw new FileAccessException(e, e.getMessage(), new Object[] {exLine.toString(), lineNo});
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * readValues
     * 
     * @param resultline
     * @param dto
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private void readValues(
            List<Object> values,
            Object dtoObject,
            FileConfig config) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (dtoObject == null) {
            return;
        }
        List<String> titles = config.getUsedColumns(dtoObject.getClass());
        titles = titles == null || titles.size() == 0 ? this.titles : titles;
        for (String title : titles) {
            String fieldName = fieldColumnMap.get(title);
            Method getter = getterMap.get(fieldName);
            Object value = getter.invoke(dtoObject, new Object[0]);
            values.add(value);
        }
    }

    /**
     * formatLine
     * 
     * @param lineValues
     * @param config
     * @return
     * @throws IOException
     */
    private String formatLine(
            List<Object> lineValues,
            FileConfig config) throws IOException {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < lineValues.size(); i++) {
            Object value = lineValues.get(i);
            String strValue = null;
            if (value instanceof Date) {
                strValue = DateUtil.dateToString((Date) value, DateUtil.FORMAT.YYYYMMDDHHMMSSMMM);
            } else {
                strValue = StringUtil.objectToStr(value);
            }
            strValue = strValue.contains(config.getSeparatorChar()) ? config.getTransferChar() + strValue + config.getTransferChar() : strValue;

            if (i > 0) {
                buf.append(config.getSeparatorChar());
            }
            buf.append(strValue);
        }
        return buf.toString();
    }

    /**
     * writeLine
     * 
     * @param line
     */
    protected void writeLine(
            BufferedWriter writer,
            String line) throws IOException {
        writer.write(line);
        writer.write(FileConfig.LINE_SEPARATOR);
        writer.flush();
    }

    /**
     * init
     * 
     * @param clazz
     */
    protected void init(
            Class<?> clazz) {
        getterMap = BeanUtil.getGetters(clazz);
        fieldColumnMap = new HashMap<String, String>();
        Map<Integer, String> titleMap = new HashMap<Integer, String>();

        for (String fieldName : getterMap.keySet()) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                throw new FileAccessException(e, e.getMessage());
            }
            Index columnIndexAnnotation = field.getAnnotation(Index.class);
            Alias columnNameAnnotation = field.getAnnotation(Alias.class);
            String fieldNameKey = columnNameAnnotation != null ? columnNameAnnotation.value() : fieldName;
            fieldColumnMap.put(fieldNameKey, fieldName);
            titleMap.put(columnIndexAnnotation.value(), fieldNameKey);
        }

        List<Integer> titleIndexList = new ArrayList<Integer>(titleMap.size());
        for (Map.Entry<Integer, String> entry : titleMap.entrySet()) {
            titleIndexList.add(entry.getKey());
        }
        Collections.sort(titleIndexList);
        titles = new ArrayList<String>(titleIndexList.size());
        for (Integer index : titleIndexList) {
            titles.add(titleMap.get(index));
        }
    }

}
