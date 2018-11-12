/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.dao;

/**
 * DaoHelper
 * 
 * @author mayue
 *
 */
public interface DaoHelper {
    /**
     * Compute the table division by UID or input parameters.
     * 
     * @param uid
     * @param params
     * @return
     */
    String getTheDivision(Object...params);

}
