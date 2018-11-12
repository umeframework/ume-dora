/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.file;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * CSV file write interface.<br>
 * 
 * @author Yue MA
 */
public interface FileWriter {
    /**
     * Write bean list to file
     * 
     * @param fileOutStream
     *            - file output stream instance
     * @param beanList
     *            - bean instance list
     * @throws FileAccessException
     */
    <E> void write(
            OutputStream fileOutStream,
            List<E> beanList) throws IOException;

    /**
     * Write bean list to file by FileConfig information
     * 
     * @param fileOutStream
     *            - file output stream instance
     * @param beanList
     *            - bean instance list
     * @param fileConfig
     *            - file configuration instance
     * @throws FileAccessException
     */
    <E> void write(
            OutputStream fileOutStream,
            List<E> beanList,
            FileConfig fileConfig) throws IOException;

}
