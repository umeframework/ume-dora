/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.sql.Blob;
import java.sql.SQLException;

/**
 * String common process tool
 * 
 * @author Yue MA
 */
abstract public class LobUtil {
    /**
     * blobToStr
     * 
     * @param blob
     * @return
     * @throws SQLException
     */
    public static String blobToStr(
            Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        return new String(blob.getBytes((long) 1, (int) blob.length()));
    }

    /**
     * blobToBytes
     * 
     * @param blob
     * @return
     * @throws SQLException
     */
    public static byte[] blobToBytes(
            Blob blob) throws SQLException {
        if (blob == null) {
            return null;
        }
        return blob.getBytes((long) 1, (int) blob.length());
    }

}
