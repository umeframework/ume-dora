/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.message.impl;

/**
 * XMLMessageBean
 * 
 * @author Yue MA
 */
public class XmlMessageBean implements java.io.Serializable {

    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 4241902196018085410L;
	/**
     * id
     */
    private String id;
    /**
     * text
     */
    private String text;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(
            String id) {
        this.id = id;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(
            String text) {
        this.text = text;
    }
}
