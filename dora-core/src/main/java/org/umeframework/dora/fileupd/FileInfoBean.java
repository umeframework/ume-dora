/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.fileupd;

import org.umeframework.dora.validation.format.Alias;

/**
 * Upload file information bean
 * 
 * @author Yue MA
 */
public class FileInfoBean implements java.io.Serializable {

    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = -7484308278796293595L;

	/**
     * fileName
     */
    @Alias("FileName")
    private String fileName;

    /**
     * fileType
     */
    @Alias("FileType")
    private String fileType;

    /**
     * charEncoding
     */
    @Alias("FileCharSet")
    private String charEncoding;

    /**
     * fileContent
     */
    @Alias("FileContent")
    private byte[] fileContent;

    /**
     * encodedFileContent (BASE64 Encoded)
     */
    @Alias("EncodedFileContent")
    private String encodedFileContent;

    /**
     * relatedTable
     */
    @Alias("RefTable")
    private String relatedTable;

    /**
     * relatedColumn
     */
    @Alias("RefColumn")
    private String relatedColumn;

    /**
     * getFileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * setFileName
     * 
     * @param fileName
     */
    public void setFileName(
            String fileName) {
        this.fileName = fileName;
    }

    /**
     * getFileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * setFileType
     * 
     * @param fileType
     */
    public void setFileType(
            String fileType) {
        this.fileType = fileType;
    }

    /**
     * getCharEncoding
     */
    public String getCharEncoding() {
        return charEncoding;
    }

    /**
     * setCharEncoding
     * 
     * @param charEncoding
     */
    public void setCharEncoding(
            String charEncoding) {
        this.charEncoding = charEncoding;
    }

    /**
     * getFileContent
     */
    public byte[] getFileContent() {
        return fileContent;
    }

    /**
     * setFileContent
     * 
     * @param fileContent
     */
    public void setFileContent(
            byte[] fileContent) {
        this.fileContent = fileContent;
    }

    /**
     * getEncodedFileContent
     */
    public String getEncodedFileContent() {
        return encodedFileContent;
    }

    /**
     * getEncodedFileContent
     * 
     * @param encodedFileContent
     */
    public void setEncodedFileContent(
            String encodedFileContent) {
        this.encodedFileContent = encodedFileContent;
    }

    /**
     * getRelatedTable
     */
    public String getRelatedTable() {
        return relatedTable;
    }

    /**
     * setRelatedTable
     * 
     * @param relatedTable
     */
    public void setRelatedTable(
            String relatedTable) {
        this.relatedTable = relatedTable;
    }

    /**
     * getRelatedColumn
     */
    public String getRelatedColumn() {
        return relatedColumn;
    }

    /**
     * setRelatedColumn
     * 
     * @param relatedColumn
     */
    public void setRelatedColumn(
            String relatedColumn) {
        this.relatedColumn = relatedColumn;
    }

}