/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.ajax;

/**
 * AJAX data rending interface.<br>
 * 
 * @author Yue MA
 */
public interface AjaxRender<E> {
    /**
     * Render java object to json text
     * 
     * @param javaObj
     *            - java object
     * @return - json text string
     */
    E render(
            Object javaObj);
}
