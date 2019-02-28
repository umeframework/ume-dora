/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Table define infromation.<br>
 */
public class TableDescBean implements java.io.Serializable {

    /**
     * serial version UID
     */
    private static final long serialVersionUID = -2945682002269108154L;
    /**
     * If generate default field for each table
     */
    private int generateDefaultTableField;
    /**
     * current generate date
     */
    private String currentDate;
    /**
     * table comment in DDL define
     */
    private String tblName;
    /**
     * table identify name (ID)
     */
    private String tblId;
    /**
     * table identify name (ID)
     */
    private String tblAlias;
    /**
     * table devision code while using devision
     */
    private String tblDivision;
    /**
     * table space define 1
     */
    private String tblSpace1;
    /**
     * table space define 2
     */
    private String tblSpace2;
    /**
     * table history enable
     */
    private String tblHistory;
    /**
     * docName
     */
    private String docName;
    /**
     * auto increment columns
     */
    /**
     * all Dto fields
     */
    private List<FieldDescBean> fieldList = new ArrayList<FieldDescBean>();

    /**
     * @return the tblName
     */
    public String getTblName() {
        return tblName;
    }

    /**
     * @param tblName
     *            the tblName to set
     */
    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    /**
     * @return the tblId
     */
    public String getTblId() {
        return tblId;
    }

    /**
     * @param tblId
     *            the tblId to set
     */
    public void setTblId(String tblId) {
        this.tblId = tblId;
    }

    /**
     * @return
     */
    public String getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate
     */
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the tblDivision
     */
    public String getTblDivision() {
        return tblDivision;
    }

    /**
     * @param tblDivision
     *            the tblDivision to set
     */
    public void setTblDivision(String tblDivision) {
        this.tblDivision = tblDivision;
    }

    // /**
    // * @return the autoIncrementColumnList
    // */
    // public List<String> getAutoIncrementColumnList() {
    // return autoIncrementColumnList;
    // }
    //
    // /**
    // * @param autoIncrementColumnList the autoIncrementColumnList to set
    // */
    // public void setAutoIncrementColumnList(List<String> autoIncrementColumnList) {
    // this.autoIncrementColumnList = autoIncrementColumnList;
    // }

    /**
     * @return the fieldList
     */
    public List<FieldDescBean> getFieldList() {
        return fieldList;
    }

    /**
     * @param fieldList
     *            the fieldList to set
     */
    public void setFieldList(List<FieldDescBean> fieldList) {
        this.fieldList = fieldList;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName
     *            the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @return the tblSpace1
     */
    public String getTblSpace1() {
        return tblSpace1;
    }

    /**
     * @param tblSpace1
     *            the tblSpace1 to set
     */
    public void setTblSpace1(String tblSpace1) {
        this.tblSpace1 = tblSpace1;
    }

    /**
     * @return the tblSpace2
     */
    public String getTblSpace2() {
        return tblSpace2;
    }

    /**
     * @param tblSpace2
     *            the tblSpace2 to set
     */
    public void setTblSpace2(String tblSpace2) {
        this.tblSpace2 = tblSpace2;
    }

    /**
     * @return the tblHistory
     */
    public String getTblHistory() {
        return tblHistory;
    }

    /**
     * @param tblHistory
     *            the tblHistory to set
     */
    public void setTblHistory(String tblHistory) {
        this.tblHistory = tblHistory;
    }

    /**
     * @return the generateDefaultTableField
     */
    public int getGenerateDefaultTableField() {
        return generateDefaultTableField;
    }

    /**
     * @param generateDefaultTableField
     *            the generateDefaultTableField to set
     */
    public void setGenerateDefaultTableField(int generateDefaultTableField) {
        this.generateDefaultTableField = generateDefaultTableField;
    }

    /**
     * @return the tblAlias
     */
    public String getTblAlias() {
        return tblAlias;
    }

    /**
     * @param tblAlias
     *            the tblAlias to set
     */
    public void setTblAlias(String tblAlias) {
        this.tblAlias = tblAlias;
    }

}
