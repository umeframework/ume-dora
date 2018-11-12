/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.file;

import java.io.InputStream;
import java.util.List;

/**
 * CSV file read interface.<br>
 * 
 * @author Yue MA
 */
public interface FileReader {
    /**
     * Read file into bean list
     * 
     * @param fileInStream
     *            - file input stream instance
     * @param beanClazz
     *            - bean class type
     * @return bean instance list
     */
    <E> List<E> read(
            InputStream fileInStream,
            Class<E> beanClazz);

    /**
     * Read file into bean list by FileConfig information
     * 
     * @param fileInStream
     *            - file input stream instance
     * @param beanClazz
     *            - bean class type
     * @param fileConfig
     *            - file configuration instance
     * @return bean instance list
     */
    <E> List<E> read(
            InputStream fileInStream,
            Class<E> beanClazz,
            FileConfig fileConfig);
}
