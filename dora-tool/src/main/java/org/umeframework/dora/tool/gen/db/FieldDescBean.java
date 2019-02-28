/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

import java.util.ArrayList;
import java.util.List;

/**
 * DtoFieldBean
 */
public class FieldDescBean implements java.io.Serializable {
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 2385796524264601099L;
    /**
     * column sequence number
     */
    private String colNo;
    /**
     * column label
     */
    private String colName;
    /**
     * column id
     */
    private String colId;
    /**
     * column data type
     */
    private String colDataType;
    /**
     * column data type with length define
     */
    private String colDataTypeWithLength;
    /**
     * column reference
     */
    private String colReference;
    /**
     * column size
     */
    private String colLength;
    /**
     * column length size
     */
    private String colPrecisionLength;
    /**
     * column min size
     */
    private String colMinLength;
    /**
     * column max size
     */
    private String colMaxLength;
    /**
     * column byte size
     */
    private String colByteLength;
    /**
     * column max byte size
     */
    private String colMinByteLength;
    /**
     * column max byte size
     */
    private String colMaxByteLength;
    /**
     * column PK flag
     */
    private String colPK;
    /**
     * column not null flag
     */
    private String colNotNull;
    /**
     * column default name
     */
    private String colDefaultValue;
    /**
     * column fix value
     */
    private String colConstValue;
    /**
     * column text format
     */
    private String colTextFormat;
    /**
     * column value min limit
     */
    private String colMinValue;
    /**
     * column value max limit
     */
    private String colMaxValue;
    /**
     * column comment
     */
    private String colComment;

    /**
     * java field id
     */
    private String fieldId;
    /**
     * java field id which first char cap
     */
    private String fieldIdCap;
    /**
     * java field name
     */
    private String fieldName;

    /**
     * java field type
     */
    private String fieldType;
    /**
     * java annotation string container
     */
    private List<String> fieldAnnotationList = new ArrayList<String>();

    /**
     * @return the colNo
     */
    public String getColNo() {
        return colNo;
    }

    /**
     * @param colNo
     *            the colNo to set
     */
    public void setColNo(String colNo) {
        this.colNo = colNo;
    }

    /**
     * @return the colName
     */
    public String getColName() {
        return colName;
    }

    /**
     * @param colName
     *            the colName to set
     */
    public void setColName(String colName) {
        this.colName = colName;
    }

    /**
     * @return the colId
     */
    public String getColId() {
        return colId;
    }

    /**
     * @param colId
     *            the colId to set
     */
    public void setColId(String colId) {
        this.colId = colId;
    }

    /**
     * @return the colDataType
     */
    public String getColDataType() {
        return colDataType;
    }

    /**
     * @param colDataType
     *            the colDataType to set
     */
    public void setColDataType(String colDataType) {
        this.colDataType = colDataType;
    }

    /**
     * @return the colDataTypeWithLength
     */
    public String getColDataTypeWithLength() {
        return colDataTypeWithLength;
    }

    /**
     * @param colDataTypeWithLength
     *            the colDataTypeWithLength to set
     */
    public void setColDataTypeWithLength(String colDataTypeWithLength) {
        this.colDataTypeWithLength = colDataTypeWithLength;
    }

    /**
     * @return the colReference
     */
    public String getColReference() {
        return colReference;
    }

    /**
     * @param colReference
     *            the colReference to set
     */
    public void setColReference(String colReference) {
        this.colReference = colReference;
    }

    /**
     * @return the colLength
     */
    public String getColLength() {
        return colLength;
    }

    /**
     * @param colLength
     *            the colLength to set
     */
    public void setColLength(String colLength) {
        this.colLength = colLength;
    }

    /**
     * @return the colPrecisionLength
     */
    public String getColPrecisionLength() {
        return colPrecisionLength;
    }

    /**
     * @param colPrecisionLength
     *            the colPrecisionLength to set
     */
    public void setColPrecisionLength(String colPrecisionLength) {
        this.colPrecisionLength = colPrecisionLength;
    }

    /**
     * @return the colMinLength
     */
    public String getColMinLength() {
        return colMinLength;
    }

    /**
     * @param colMinLength
     *            the colMinLength to set
     */
    public void setColMinLength(String colMinLength) {
        this.colMinLength = colMinLength;
    }

    /**
     * @return the colMaxLength
     */
    public String getColMaxLength() {
        return colMaxLength;
    }

    /**
     * @param colMaxLength
     *            the colMaxLength to set
     */
    public void setColMaxLength(String colMaxLength) {
        this.colMaxLength = colMaxLength;
    }

    /**
     * @return the colByteLength
     */
    public String getColByteLength() {
        return colByteLength;
    }

    /**
     * @param colByteLength
     *            the colByteLength to set
     */
    public void setColByteLength(String colByteLength) {
        this.colByteLength = colByteLength;
    }

    /**
     * @return the colMinByteLength
     */
    public String getColMinByteLength() {
        return colMinByteLength;
    }

    /**
     * @param colMinByteLength
     *            the colMinByteLength to set
     */
    public void setColMinByteLength(String colMinByteLength) {
        this.colMinByteLength = colMinByteLength;
    }

    /**
     * @return the colMaxByteLength
     */
    public String getColMaxByteLength() {
        return colMaxByteLength;
    }

    /**
     * @param colMaxByteLength
     *            the colMaxByteLength to set
     */
    public void setColMaxByteLength(String colMaxByteLength) {
        this.colMaxByteLength = colMaxByteLength;
    }

    /**
     * @return the colPK
     */
    public String getColPK() {
        return colPK;
    }

    /**
     * @param colPK
     *            the colPK to set
     */
    public void setColPK(String colPK) {
        this.colPK = colPK;
    }

    /**
     * @return the colNotNull
     */
    public String getColNotNull() {
        return colNotNull;
    }

    /**
     * @param colNotNull
     *            the colNotNull to set
     */
    public void setColNotNull(String colNotNull) {
        this.colNotNull = colNotNull;
    }

    /**
     * @return the colDefaultValue
     */
    public String getColDefaultValue() {
        return colDefaultValue;
    }

    /**
     * @param colDefaultValue
     *            the colDefaultValue to set
     */
    public void setColDefaultValue(String colDefaultValue) {
        this.colDefaultValue = colDefaultValue;
    }

    /**
     * @return the colConstValue
     */
    public String getColConstValue() {
        return colConstValue;
    }

    /**
     * @param colConstValue
     *            the colConstValue to set
     */
    public void setColConstValue(String colConstValue) {
        this.colConstValue = colConstValue;
    }

    /**
     * @return the colTextFormat
     */
    public String getColTextFormat() {
        return colTextFormat;
    }

    /**
     * @param colTextFormat
     *            the colTextFormat to set
     */
    public void setColTextFormat(String colTextFormat) {
        this.colTextFormat = colTextFormat;
    }

    /**
     * @return the colMinValue
     */
    public String getColMinValue() {
        return colMinValue;
    }

    /**
     * @param colMinValue
     *            the colMinValue to set
     */
    public void setColMinValue(String colMinValue) {
        this.colMinValue = colMinValue;
    }

    /**
     * @return the colMaxValue
     */
    public String getColMaxValue() {
        return colMaxValue;
    }

    /**
     * @param colMaxValue
     *            the colMaxValue to set
     */
    public void setColMaxValue(String colMaxValue) {
        this.colMaxValue = colMaxValue;
    }

    /**
     * @return the colComment
     */
    public String getColComment() {
        return colComment;
    }

    /**
     * @param colComment
     *            the colComment to set
     */
    public void setColComment(String colComment) {
        this.colComment = colComment;
    }

    /**
     * @return the fieldId
     */
    public String getFieldId() {
        return fieldId;
    }

    /**
     * @param fieldId
     *            the fieldId to set
     */
    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * @return the fieldIdCap
     */
    public String getFieldIdCap() {
        return fieldIdCap;
    }

    /**
     * @param fieldIdCap
     *            the fieldIdCap to set
     */
    public void setFieldIdCap(String fieldIdCap) {
        this.fieldIdCap = fieldIdCap;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName
     *            the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the fieldType
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * @param fieldType
     *            the fieldType to set
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * @return the fieldAnnotationList
     */
    public List<String> getFieldAnnotationList() {
        return fieldAnnotationList;
    }

    /**
     * @param fieldAnnotationList
     *            the fieldAnnotationList to set
     */
    public void setFieldAnnotationList(List<String> fieldAnnotationList) {
        this.fieldAnnotationList = fieldAnnotationList;
    }

}
