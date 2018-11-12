/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.util;

/**
 * HashUtil
 * 
 * @author Yue MA
 * 
 */
abstract public class HashUtil {
    /**
     * @param key
     * @return
     */
    public static int hashCode(
            String key) {
        return key.hashCode();
    }

    /**
     * @param str
     * @param prime
     * @return
     */
    public static int additiveHash(
            String str,
            int prime) {
        int hash, i;
        for (hash = str.length(), i = 0; i < str.length(); i++)
            hash += str.charAt(i);
        return (hash % prime);
    }

    /**
     * @param str
     * @param prime
     * @return
     */
    public static int rotatingHash(
            String str,
            int prime) {
        int hash, i;
        for (hash = str.length(), i = 0; i < str.length(); ++i)
            hash = (hash << 4) ^ (hash >> 28) ^ str.charAt(i);
        return (hash % prime);
    }

    /**
     * @param data
     * @return
     */
    public static int FNVHash1(
            byte[] data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (byte b : data)
            hash = (hash ^ b) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }

    /**
     * @param str
     * @return
     */
    public static long mixHash(
            String str) {
        long hash = str.hashCode();
        hash <<= 32;
        hash |= str.hashCode();
        return hash;
    }

    /**
     * @param str
     * @return
     */
    public static int intHash(
            int str) {
        str += ~(str << 15);
        str ^= (str >>> 10);
        str += (str << 3);
        str ^= (str >>> 6);
        str += ~(str << 11);
        str ^= (str >>> 16);
        return str;
    }

    /**
     * @param str
     * @return
     */
    public static int JSHash(
            String str) {
        int hash = 1315423911;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }

        return (hash & 0x7FFFFFFF);
    }
}
