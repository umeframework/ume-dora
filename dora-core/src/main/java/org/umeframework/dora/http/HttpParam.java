/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.http;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpParam
 *
 * @author Yue MA
 *
 */
public class HttpParam implements java.io.Serializable {

    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 7145651347427320994L;
	/**
     * entity message
     */
    private String message;
    /**
     * header parameter map
     */
    private Map<String, String> headers = new HashMap<String, String>();
    /**
     * parameter map
     */
    private Map<String, String> parameters = new HashMap<String, String>();
    

    /**
     * HttpParam
     */
    public HttpParam() {
    }

    /**
     * HttpParam
     *
     * @param entityStr
     */
    public HttpParam(
            String message) {
        this.message = message;
    }

    /**
     * add header parameter
     *
     * @param name
     * @param value
     */
    public void addHeader(
            String name,
            Object value) {
        headers.put(name, String.valueOf(value));
    }

    /**
     * add parameter
     *
     * @param name
     * @param value
     */
    public void addParameter(
            String name,
            Object value) {
        parameters.put(name, String.valueOf(value));
    }
    
    /**
     * @return the entityStr
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return the parameters
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[entityStr]\n");
        builder.append(message);

        builder.append("[headers]\n");
        int i = 0;
        for (Map.Entry<String, String> e : headers.entrySet()) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(e.getKey());
            builder.append("=");
            builder.append(e.getValue());
            i++;
        }
        builder.append("[parameters]\n");
        i = 0;
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(e.getKey());
            builder.append("=");
            builder.append(e.getValue());
            i++;
        }
        return builder.toString();
    }

    /**
     * @param message the message to set
     */
    public void setMessage(
            String message) {
        this.message = message;
    }
    
    /**
     * Http response
     */
    private org.apache.http.HttpResponse response;

    /**
     * getHeaderParam
     * 
     * @param key
     * @return
     */
    public String getHttpResponseHeader(
            String key) {
        org.apache.http.Header[] headers = response.getAllHeaders();
        if (headers != null) {
            for (org.apache.http.Header header : headers) {
                if (key.equals(header.getName())) {
                    return header.getValue();
                }
            }
        }
        return null;
    }

    /**
     * @return the response
     */
    public org.apache.http.HttpResponse getResponse() {
        return response;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(
            org.apache.http.HttpResponse response) {
        this.response = response;
    }
}
