/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.beanutils.ConversionException;

/**
 * CloneUtil
 * 
 * @author Yue MA
 * 
 */
public class CloneUtil {
    /**
     * deepCopy
     * 
     * @param original
     * @return
     * @throws Exception
     */
    public static Object deepCopy(
            Object original) throws Exception {
        if (original == null) {
            return null;
        }

        Object copy = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(original);
            out.flush();

            in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            copy = in.readObject();
        } catch (IOException e) {
            throw new ConversionException(e);
        } catch (ClassNotFoundException e) {
            throw new ConversionException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new ConversionException(e);
            }
        }
        return copy;
    }

}
