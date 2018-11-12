/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.fileupd;

/**
 * File upload/download constraint setting define
 * 
 * @author Yue MA
 * 
 */
public class FileConstraint {
    /**
     * Default all upload/download service as control scope
     */
	private static final String DEFAULT_TARGET_SERVICE_ID = "ALL";
    /**
     * Memory size limit
     */
    private static final Integer DEDAULT_SIZE_THRESHOLD = 3 * 1024 * 1024;
    /**
     * Temporary save path
     */
    private static final String DEDAULT_REPOSITORY = "";
    /**
     * Max size
     */
    private static final Long DEDAULT_SIZE_MAX = Long.valueOf(30 * 1024 * 1024);
    /**
     * Max file size
     */
    private static final Long DEDAULT_FILE_SIZE_MAX = Long.valueOf(3 * 1024 * 1024);
    /**
     * Max file number
     */
    private static final Integer DEDAULT_FILE_COUNT = 10;

    /**
     * targetServiceId
     */
    private String targetServiceId;
    /**
     * sizeThreshold
     */
    private Integer sizeThreshold;
    /**
     * repository
     */
    private String repository;
    /**
     * sizeMax
     */
    private Long sizeMax;
    /**
     * fileSizeMax
     */
    private Long fileSizeMax;
    /**
     * fileCount
     */
    private Integer fileCount;

    /**
     * FileConstraint
     */
    public FileConstraint() {
        super();
        this.targetServiceId = DEFAULT_TARGET_SERVICE_ID;
        this.sizeThreshold = DEDAULT_SIZE_THRESHOLD;
        this.repository = DEDAULT_REPOSITORY;
        this.sizeMax = DEDAULT_SIZE_MAX;
        this.fileSizeMax = DEDAULT_FILE_SIZE_MAX;
        this.fileCount = DEDAULT_FILE_COUNT;
    }

    /**
     * FileConstraint
     * 
     * @param sizeThreshold
     * @param repository
     * @param sizeMax
     * @param fileSizeMax
     */
    public FileConstraint(
            int sizeThreshold,
            String repository,
            Long sizeMax,
            Long fileSizeMax) {
        super();
        this.targetServiceId = DEFAULT_TARGET_SERVICE_ID;
        this.sizeThreshold = sizeThreshold;
        this.repository = repository;
        this.sizeMax = sizeMax;
        this.fileSizeMax = fileSizeMax;
        this.fileCount = (int) (sizeMax / fileSizeMax);
    }

    /**
     * FileConstraint
     * 
     * @param sizeThreshold
     * @param repository
     * @param fileCount
     * @param fileSizeMax
     */
    public FileConstraint(
            int sizeThreshold,
            String repository,
            int fileCount,
            Long fileSizeMax) {
        super();
        this.targetServiceId = DEFAULT_TARGET_SERVICE_ID;
        this.sizeThreshold = sizeThreshold;
        this.repository = repository;
        this.fileCount = fileCount;
        this.fileSizeMax = fileSizeMax;
        this.sizeMax = fileSizeMax * fileCount;
    }

    /**
     * FileConstraint
     * 
     * @param targetServiceId
     * @param sizeThreshold
     * @param repository
     * @param sizeMax
     * @param fileSizeMax
     */
    public FileConstraint(
            String targetServiceId,
            int sizeThreshold,
            String repository,
            Long sizeMax,
            Long fileSizeMax) {
        super();
        this.targetServiceId = targetServiceId;
        this.sizeThreshold = sizeThreshold;
        this.repository = repository;
        this.sizeMax = sizeMax;
        this.fileSizeMax = fileSizeMax;
        this.fileCount = (int) (sizeMax / fileSizeMax);
    }

    /**
     * FileConstraint
     * 
     * @param targetServiceId
     * @param sizeThreshold
     * @param repository
     * @param fileCount
     * @param fileSizeMax
     */
    public FileConstraint(
            String targetServiceId,
            Integer sizeThreshold,
            String repository,
            Integer fileCount,
            Long fileSizeMax) {
        super();
        this.targetServiceId = targetServiceId;
        this.sizeThreshold = sizeThreshold;
        this.repository = repository;
        this.fileCount = fileCount;
        this.fileSizeMax = fileSizeMax;
        this.sizeMax = fileSizeMax * fileCount;
    }

    /**
     * getTargetServiceId
     */
    public String getTargetServiceId() {
        return targetServiceId;
    }

    /**
     * setTargetServiceId
     * 
     * @param targetServiceId
     */
    public void setTargetServiceId(
            String targetServiceId) {
        this.targetServiceId = targetServiceId;
    }

    /**
     * getSizeThreshold
     */
    public Integer getSizeThreshold() {
        return sizeThreshold;
    }

    /**
     * setSizeThreshold
     * 
     * @param sizeThreshold
     */
    public void setSizeThreshold(
            Integer sizeThreshold) {
        this.sizeThreshold = sizeThreshold;
    }

    /**
     * getRepository
     */
    public String getRepository() {
        return repository;
    }

    /**
     * setRepository
     * 
     * @param repository
     */
    public void setRepository(
            String repository) {
        this.repository = repository;
    }

    /**
     * getSizeMax
     * 
     */
    public Long getSizeMax() {
        return sizeMax;
    }

    /**
     * setSizeMax
     * 
     * @param sizeMax
     */
    public void setSizeMax(
            Long sizeMax) {
        this.sizeMax = sizeMax;
    }

    /**
     * getFileSizeMax
     */
    public Long getFileSizeMax() {
        return fileSizeMax;
    }

    /**
     * setFileSizeMax
     * 
     * @param fileSizeMax
     */
    public void setFileSizeMax(
            Long fileSizeMax) {
        this.fileSizeMax = fileSizeMax;
    }

    /**
     * getFileCount
     */
    public Integer getFileCount() {
        return fileCount;
    }

    /**
     * setFileCount
     * 
     * @param fileCount
     */
    public void setFileCount(
            Integer fileCount) {
        this.fileCount = fileCount;
    }
}
