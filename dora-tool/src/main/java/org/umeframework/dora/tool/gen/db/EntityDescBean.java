/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Bean properties define for Table/View Dto generating.<br>
 */
public class EntityDescBean extends TableDescBean implements java.io.Serializable {

    /**
     * serial version UID
     */
    private static final long serialVersionUID = 8595565147534114190L;
    /**
     * Base package
     */
    private String basePackage;
    /**
     * Dto class identify name (ID)
     */
    private String classId;
    /**
     * Dto class identify name (ID)
     */
    private String classOriId;
    /**
     * Dto class identify name (ID low case)
     */
    private String classOriIdInLowCase;
    /**
     * Dto class identify name (ID upper case)
     */
    private String classOriIdInUpperCase;
    /**
     * Dto class name
     */
    private String className;
    /**
     * Dto class comment
     */
    private String classComment;
    
    /**
     * Dto Crud interface class package
     */
    private String tableCrudServiceInterfacePackage;
    /**
     * Dto Crud implementation class package
     */
    private String tableCrudServicePackage;
    /**
     * Dto Crud Api class package
     */
    private String tableCrudApiPackage;
    /**
     * Dto class package
     */
    private String tableDtoPackage;
    /**
     * Mapper class package
     */
    private String tableMapperPackage;
    /**
     * Dto Crud implementation class ID
     */
    private String tableCrudServiceClass;
    /**
     * Dto Crud interface class ID
     */
    private String tableCrudServiceInterface;
    /**
     * Dto Crud Api class ID
     */
    private String tableCrudApiClass;
    /**
     * Dto Mapper class ID
     */
    private String tableMapperClass;
    /**
     * Dto class ID
     */
    private String tableDtoClass;
    /**
     * Dto Condition class ID
     */
    private String tableDtoConditionClass;
    /**
     * Dto Criteria class ID
     */
    private String tableDtoCriteriaClass;
    /**
     * Sql Map XML file name
     */
    private String sqlMapXmlFile;
    
    /**
     * auto increment columns
     */
    private List<String> autoIncrementColumnList = new ArrayList<String>();
    /**
     * primary key fields
     */
    private List<FieldDescBean> primaryKeyFieldList = new ArrayList<FieldDescBean>();
    /**
     * non-primary key fields
     */
    private List<FieldDescBean> nonPrimaryKeyFieldList = new ArrayList<FieldDescBean>();
    /**
     * collection for Dto annotation content build
     */
    private List<String> classAnnotationList = new ArrayList<String>();
    /**
     * collection for Dto import content build
     */
    private Set<String> classImportList;

    /**
     * @return the classPackage
     */
    public String getBasePackage() {
        return basePackage;
    }

    /**
     * @param classPackage
     *            the classPackage to set
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * @return the classId
     */
    public String getClassId() {
        return classId;
    }

    /**
     * @param classId
     *            the classId to set
     */
    public void setClassId(String classId) {
        this.classId = classId;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the classComment
     */
    public String getClassComment() {
        return classComment;
    }

    /**
     * @param classComment
     *            the classComment to set
     */
    public void setClassComment(String classComment) {
        this.classComment = classComment;
    }

    /**
     * @return the tableCrudServiceInterfacePackage
     */
    public String getTableCrudServiceInterfacePackage() {
        return tableCrudServiceInterfacePackage;
    }

    /**
     * @param tableCrudServiceInterfacePackage
     *            the tableCrudServiceInterfacePackage to set
     */
    public void setTableCrudServiceInterfacePackage(String tableCrudServiceInterfacePackage) {
        this.tableCrudServiceInterfacePackage = tableCrudServiceInterfacePackage;
    }

    /**
     * @return the tableCrudServicePackage
     */
    public String getTableCrudServicePackage() {
        return tableCrudServicePackage;
    }

    /**
     * @param tableCrudServicePackage
     *            the tableCrudServicePackage to set
     */
    public void setTableCrudServicePackage(String tableCrudServicePackage) {
        this.tableCrudServicePackage = tableCrudServicePackage;
    }

    /**
     * @return the tableCrudServiceClass
     */
    public String getTableCrudServiceClass() {
        return tableCrudServiceClass;
    }

    /**
     * @param tableCrudServiceClass
     *            the tableCrudServiceClass to set
     */
    public void setTableCrudServiceClass(String tableCrudServiceClass) {
        this.tableCrudServiceClass = tableCrudServiceClass;
    }

    /**
     * @return the tableCrudServiceInterface
     */
    public String getTableCrudServiceInterface() {
        return tableCrudServiceInterface;
    }

    /**
     * @param tableCrudServiceInterface
     *            the tableCrudServiceInterface to set
     */
    public void setTableCrudServiceInterface(String tableCrudServiceInterface) {
        this.tableCrudServiceInterface = tableCrudServiceInterface;
    }

    /**
     * @return the autoIncrementColumnList
     */
    public List<String> getAutoIncrementColumnList() {
        return autoIncrementColumnList;
    }

    /**
     * @param autoIncrementColumnList
     *            the autoIncrementColumnList to set
     */
    public void setAutoIncrementColumnList(List<String> autoIncrementColumnList) {
        this.autoIncrementColumnList = autoIncrementColumnList;
    }

    /**
     * @return the primaryKeyFieldList
     */
    public List<FieldDescBean> getPrimaryKeyFieldList() {
        return primaryKeyFieldList;
    }

    /**
     * @param primaryKeyFieldList
     *            the primaryKeyFieldList to set
     */
    public void setPrimaryKeyFieldList(List<FieldDescBean> primaryKeyFieldList) {
        this.primaryKeyFieldList = primaryKeyFieldList;
    }

    /**
     * @return the nonPrimaryKeyFieldList
     */
    public List<FieldDescBean> getNonPrimaryKeyFieldList() {
        return nonPrimaryKeyFieldList;
    }

    /**
     * @param nonPrimaryKeyFieldList
     *            the nonPrimaryKeyFieldList to set
     */
    public void setNonPrimaryKeyFieldList(List<FieldDescBean> nonPrimaryKeyFieldList) {
        this.nonPrimaryKeyFieldList = nonPrimaryKeyFieldList;
    }

    /**
     * @return the classAnnotationList
     */
    public List<String> getClassAnnotationList() {
        return classAnnotationList;
    }

    /**
     * @param classAnnotationList
     *            the classAnnotationList to set
     */
    public void setClassAnnotationList(List<String> classAnnotationList) {
        this.classAnnotationList = classAnnotationList;
    }

    /**
     * @return the classImportList
     */
    public Set<String> getClassImportList() {
        return classImportList;
    }

    /**
     * @param classImportList
     *            the classImportList to set
     */
    public void setClassImportList(Set<String> classImportList) {
        this.classImportList = classImportList;
    }

    /**
     * @return the classOriId
     */
    public String getClassOriId() {
        return classOriId;
    }

    /**
     * @param classOriId
     *            the classOriId to set
     */
    public void setClassOriId(String classOriId) {
        this.classOriId = classOriId;
    }

    /**
     * @return the classOriIdInLowCase
     */
    public String getClassOriIdInLowCase() {
        return classOriIdInLowCase;
    }

    /**
     * @param classOriIdInLowCase
     *            the classOriIdInLowCase to set
     */
    public void setClassOriIdInLowCase(String classOriIdInLowCase) {
        this.classOriIdInLowCase = classOriIdInLowCase;
    }

    /**
     * @return the classOriIdInUpperCase
     */
    public String getClassOriIdInUpperCase() {
        return classOriIdInUpperCase;
    }

    /**
     * @param classOriIdInUpperCase
     *            the classOriIdInUpperCase to set
     */
    public void setClassOriIdInUpperCase(String classOriIdInUpperCase) {
        this.classOriIdInUpperCase = classOriIdInUpperCase;
    }

    /**
     * @return the tableCrudApiPackage
     */
    public String getTableCrudApiPackage() {
        return tableCrudApiPackage;
    }

    /**
     * @param tableCrudApiPackage
     *            the tableCrudApiPackage to set
     */
    public void setTableCrudApiPackage(String tableCrudApiPackage) {
        this.tableCrudApiPackage = tableCrudApiPackage;
    }

    /**
     * @return the tableCrudApiClass
     */
    public String getTableCrudApiClass() {
        return tableCrudApiClass;
    }

    /**
     * @param tableCrudApiClass
     *            the tableCrudApiClass to set
     */
    public void setTableCrudApiClass(String tableCrudServiceApi) {
        this.tableCrudApiClass = tableCrudServiceApi;
    }

    /**
     * @return the tableMapperClass
     */
    public String getTableMapperClass() {
        return tableMapperClass;
    }

    /**
     * @param tableMapperClass the tableMapperClass to set
     */
    public void setTableMapperClass(String tableMapperClass) {
        this.tableMapperClass = tableMapperClass;
    }

    /**
     * @return the tableDtoPackage
     */
    public String getTableDtoPackage() {
        return tableDtoPackage;
    }

    /**
     * @param tableDtoPackage the tableDtoPackage to set
     */
    public void setTableDtoPackage(String tableDtoPackage) {
        this.tableDtoPackage = tableDtoPackage;
    }

    /**
     * @return the tableMapperPackage
     */
    public String getTableMapperPackage() {
        return tableMapperPackage;
    }

    /**
     * @param tableMapperPackage the tableMapperPackage to set
     */
    public void setTableMapperPackage(String tableMapperPackage) {
        this.tableMapperPackage = tableMapperPackage;
    }

    /**
     * @return the tableDtoClass
     */
    public String getTableDtoClass() {
        return tableDtoClass;
    }

    /**
     * @param tableDtoClass the tableDtoClass to set
     */
    public void setTableDtoClass(String tableDtoClass) {
        this.tableDtoClass = tableDtoClass;
    }

    /**
     * @return the tableDtoConditionClass
     */
    public String getTableDtoConditionClass() {
        return tableDtoConditionClass;
    }

    /**
     * @param tableDtoConditionClass the tableDtoConditionClass to set
     */
    public void setTableDtoConditionClass(String tableDtoConditionClass) {
        this.tableDtoConditionClass = tableDtoConditionClass;
    }

    /**
     * @return the tableDtoCriteriaClass
     */
    public String getTableDtoCriteriaClass() {
        return tableDtoCriteriaClass;
    }

    /**
     * @param tableDtoCriteriaClass the tableDtoCriteriaClass to set
     */
    public void setTableDtoCriteriaClass(String tableDtoCriteriaClass) {
        this.tableDtoCriteriaClass = tableDtoCriteriaClass;
    }

    /**
     * @return the sqlMapXmlFile
     */
    public String getSqlMapXmlFile() {
        return sqlMapXmlFile;
    }

    /**
     * @param sqlMapXmlFile the sqlMapXmlFile to set
     */
    public void setSqlMapXmlFile(String sqlMapXmlFile) {
        this.sqlMapXmlFile = sqlMapXmlFile;
    }
}
