/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service;

import org.umeframework.dora.exception.ApplicationException;

/**
 * Table entity to describe some common items which use for F/W
 *
 * @author Yue MA
 */
public class TableObject implements java.io.Serializable {
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1682111394148261969L;
	/**
     * table schema, default is empty
     */
    private String theSchema;
    /**
     * table division, default is empty
     */
    private String theDivision;
    /**
     * SQL Order By condition parameter
     */
    private String theOrderByCondition;
    /**
     * SQL Group By condition parameter
     */
    private String theGroupByCondition;
    /**
     * Dynamic SQL query condition parameter
     */
    private String theSQLCondition;
    /**
     * fetch max size
     */
    private Integer theFetchSize;
    /**
     * fetch begin index
     */
    private Integer theFetchStart;
    
    /**
     * Constant declare: TableObject default property filed name.<br>
     */
    public static class Property {
        public static final String theGroupByCondition = "theGroupByCondition";
        public static final String theOrderByCondition = "theOrderByCondition";
        public static final String theSchema = "theSchema";
        public static final String theDivision = "theDivision";
        public static final String theFetchSize = "theFetchSize";
        public static final String theFetchStart = "theFetchStart";
    }

    /**
     * verifySQLRisk
     *
     * @param condPart
     */
    public void verifySQLRisk(
            String condPart) {

    }
    
    /**
     * clearDefaultProperties
     */
    public void clearDefaultProperties() {
        this.setTheSchema(null);
        this.setTheDivision(null);
        this.setTheOrderByCondition(null);
        this.setTheGroupByCondition(null);
        this.setTheSQLCondition(null);
        this.setTheFetchSize(null);
        this.setTheFetchStart(null);
    }

    /**
     * @return the theSchema
     */
    public String getTheSchema() {
        return theSchema;
    }

    /**
     * @param theSchema
     *            the theSchema to set
     */
    public void setTheSchema(
            String theSchema) {
        this.theSchema = theSchema;
    }

    /**
     * @return the theDivision
     */
    public String getTheDivision() {
        return theDivision;
    }

    /**
     * @param theDivision
     *            the theDivision to set
     */
    public void setTheDivision(
            String theDivision) {
        if (theDivision != null && theDivision.contains(" ")) {
            throw new ApplicationException("Found illegal SQL characters input for setTheDivision:" + theDivision);
        }
        this.theDivision = theDivision;
    }

    /**
     * @return the theOrderByCondition
     */
    public String getTheOrderByCondition() {
        return theOrderByCondition;
    }

    /**
     * @param theOrderByCondition
     *            the theOrderByCondition to set
     */
    public void setTheOrderByCondition(
            String theOrderByCondition) {

        this.theOrderByCondition = theOrderByCondition;
    }

    /**
     * @return the theFetchSize
     */
    public Integer getTheFetchSize() {
        return theFetchSize;
    }

    /**
     * @param theFetchSize
     *            the theFetchSize to set
     */
    public void setTheFetchSize(
            Integer theFetchSize) {
        this.theFetchSize = theFetchSize;
    }

    /**
     * @return the theGroupByCondition
     */
    public String getTheGroupByCondition() {
        return theGroupByCondition;
    }

    /**
     * @param theGroupByCondition
     *            the theGroupByCondition to set
     */
    public void setTheGroupByCondition(
            String theGroupByCondition) {
        this.theGroupByCondition = theGroupByCondition;
    }

    /**
     * @return the theSQLCondition
     */
    public String getTheSQLCondition() {
        return theSQLCondition;
    }

    /**
     * @param theSQLCondition the theSQLCondition to set
     */
    public void setTheSQLCondition(
            String theSQLCondition) {
        this.theSQLCondition = theSQLCondition;
    }

    /**
     * @return the theFetchStart
     */
    public Integer getTheFetchStart() {
        return theFetchStart;
    }

    /**
     * @param theFetchStart the theFetchStart to set
     */
    public void setTheFetchStart(
            Integer theFetchStart) {
        this.theFetchStart = theFetchStart;
    }

}
