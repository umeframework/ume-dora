/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;

/**
 * Declare basic data mapping interface between XML and bean. Provide the
 * default XStream instance creating and abstract mapping method for subclass
 * inherit.
 *
 * @author Yue MA
 *
 */
public abstract class XmlMapper<E> {
    /**
     * default line separator char of system
     */
    protected static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * use compact mode for XML output style, default is 'true'
     */
    private boolean compactMode = true;

    /**
     * default XStream instance for XML mapping
     */
    private XStream xstream;

    /**
     * XMLDataMapper
     */
    public XmlMapper() {
        xstream = new EtlXstream();
        mapper(xstream);
    }

    /**
     * define mapping rule by this interface
     *
     * @param xstream
     */
    protected abstract void mapper(
            XStream xstream);

    /**
     * define the specific mapping rule only for parse (from XML)
     *
     * @param xstream
     */
    protected void mapperForParse(
            XStream xstream) {
    }

    /**
     * define the specific mapping rule only for render (to XML)
     *
     * @param xstream
     */
    protected void mapperForRender(
            XStream xstream) {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.umeframework.dora.ajax.AJAXParser#parse(java.lang.String, java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public E parse(
            String inStr) {
        E obj = null;
        BufferedReader br = null;
        try {
            mapperForParse(xstream);
            br = new BufferedReader(new StringReader(inStr));
            obj = (E) xstream.fromXML(br);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
            }
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.umeframework.dora.ajax.AJAXRender#render(java.lang.Object)
     */
    public String render(
            E javaObj) {
        String xml = null;
        try {
            mapperForRender(xstream);

            if (compactMode) {
                Writer writer = new StringWriter();
                xstream.marshal(javaObj, new CompactWriter(writer));
                xml = writer.toString();
            } else {
                xml = xstream.toXML(javaObj);
            }
        } finally {
        }
        return xml;
    }

    /**
     * @return the compactMode
     */
    public boolean getCompactMode() {
        return compactMode;
    }

    /**
     * @param compactMode
     *            the compactMode to set
     */
    public void setCompactMode(
            boolean compactMode) {
        this.compactMode = compactMode;
    }

}
