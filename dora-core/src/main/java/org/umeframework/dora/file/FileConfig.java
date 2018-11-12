/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Text file configuration information define
 * 
 * @author Yue MA
 */
public class FileConfig {
    /**
     * DEFAULT_CHARSET
     */
    public static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * LINE_SEPARATOR
     */
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * DELIMIT_HT
     */
    public static final String DELIMIT_HT = "\t";

    /**
     * DELIMIT_COMMA
     */
    public static final String DELIMIT_COMMA = ",";

    /**
     * DOUBLE_QUOTE
     */
    public static final String DOUBLE_QUOTE = "\"";

    /**
     * ESCAPED_DOUBLE_QUOTE
     */
    public static final String ESCAPED_DOUBLE_QUOTE = "\"\"";

    /**
     * LOCK_NONE
     */
    public static final String LOCK_NONE = "NONE";

    /**
     * LOCK_SHARE
     */
    public static final String LOCK_SHARE = "SHARE";

    /**
     * LOCK_EXCLUSIVE
     */
    public static final String LOCK_EXCLUSIVE = "EXCLUSIVE";
    /**
     * charSet
     */
    String charSet = DEFAULT_CHARSET;

    /**
     * separatorChar
     */
    String separatorChar = DELIMIT_COMMA;

    /**
     * transferChar
     */
    String transferChar = DOUBLE_QUOTE;

    /**
     * usedColumns
     */
    Map<Class<?>, List<String>> usedColumns = new HashMap<Class<?>, List<String>>();

    /**
     * Using columns
     * 
     * @param dtoClazz
     */
    public List<String> getUsedColumns(
            Class<?> dtoClazz) {
        return usedColumns.get(dtoClazz);
    }

    /**
     * Add using column
     * 
     * @param dtoClazz
     * @param columns
     */
    public synchronized void addUsedColumns(
            Class<?> dtoClazz,
            List<String> columns) {
        List<String> target = usedColumns.get(dtoClazz);
        if (target == null) {
            usedColumns.put(dtoClazz, new ArrayList<String>());
            target = usedColumns.get(dtoClazz);
        }
        target.addAll(columns);
    }

    /**
     * Remove useless column
     * 
     * @param dtoClazz
     */
    public synchronized void removeUsedColumns(
            Class<?> dtoClazz) {
        usedColumns.remove(dtoClazz);
    }

    /**
     * getCharSet
     */
    public String getCharSet() {
        return charSet;
    }

    /**
     * setCharSet
     * 
     * @param charSet
     */
    public void setCharSet(
            String charSet) {
        this.charSet = charSet;
    }

    /**
     * getSeparatorChar
     */
    public String getSeparatorChar() {
        return separatorChar;
    }

    /**
     * setSeparatorChar
     * 
     * @param separatorChar
     */
    public void setSeparatorChar(
            String separatorChar) {
        this.separatorChar = separatorChar;
    }

    /**
     * getTransferChar
     */
    public String getTransferChar() {
        return transferChar;
    }

    /**
     * transferChar
     * 
     * @param transferChar
     */
    public void setTransferChar(
            String transferChar) {
        this.transferChar = transferChar;
    }
}
