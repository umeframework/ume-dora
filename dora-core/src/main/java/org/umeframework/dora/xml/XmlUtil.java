/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.xml;

import java.io.StringWriter;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;

/**
 * OXMSupport
 * 
 * @author Yue MA
 */
abstract public class XmlUtil {
    /**
     * default XStream instance for XML mapping
     */
    private static XStream xstream;
    static {
        xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
    }

    /**
     * @param xstream
     * @param xml
     * @return
     */
    public static <E> E fromXML(
            XStream xstream,
            String xml) {
        @SuppressWarnings("unchecked")
        E obj = (E) xstream.fromXML(xml);
        return obj;
    }

    /**
     * @param xml
     * @return
     */
    public static <E> E fromXML(
            String xml) {
        return fromXML(xstream, xml);
    }

    /**
     * @param xstream
     * @param obj
     * @param compactMode
     * @return
     */
    public static <E> String toXML(
            XStream xstream,
            E obj,
            boolean compactMode) {
        String xml = null;
        if (compactMode) {
            Writer writer = new StringWriter();
            xstream.marshal(obj, new CompactWriter(writer));
            xml = writer.toString();
        } else {
            xml = xstream.toXML(obj);
        }
        return xml;
    }

    /**
     * @param xstream
     * @param obj
     * @return
     */
    public static <E> String toXML(
            XStream xstream,
            E obj) {
        return toXML(xstream, obj, true);
    }

    /**
     * @param obj
     * @return
     */
    public static <E> String toXML(
            E obj) {
        return toXML(xstream, obj, true);
    }
}
