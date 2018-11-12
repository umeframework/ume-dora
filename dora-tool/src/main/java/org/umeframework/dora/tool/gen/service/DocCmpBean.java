/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Component define bean

 * @author Yue MA
 *
 */
public class DocCmpBean {
	// Sheet name
    private String sheetName;
	// Define items
    private String id;
    private String name;
    private String comment;
    private String pkg;
    private String author;
    private String updateDate;
    private List<DocItemBean> funcList = new ArrayList<DocItemBean>();
    private List<String> refdtoNameList = new ArrayList<String>();
    private List<String> refdtoTypeList = new ArrayList<String>();
    private List<String> refdtoPkgList = new ArrayList<String>();
    private Set<String> classAnnotationList = new HashSet<String>();
    private Set<String> classImportList = new HashSet<String>();
    
    /**
     * getWSID
     * 
     * @param funcId
     * @return
     */
    public String getWSID(String funcId) {
        for (DocItemBean func : funcList) {
            if (func.getWsFlag() != null && func.getWsFlag().equals("YES") && funcId.equals(func.getId())) {
                return func.getWsId();
            }
        }
        return null;
    }
    
    /**
     * hasWS
     * 
     * @return
     */
    public boolean hasWS() {
        for (DocItemBean func : funcList) {
            if (func.getWsFlag() != null && func.getWsFlag().equals("YES")) {
                return true;
            }
        }
        return false;
    }

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(
            String name) {
        this.name = name;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     *            the comment to set
     */
    public void setComment(
            String comment) {
        this.comment = comment;
    }

    /**
     * @return the pkg
     */
    public String getPkg() {
        return pkg;
    }

    /**
     * @param pkg
     *            the pkg to set
     */
    public void setPkg(
            String pkg) {
        this.pkg = pkg;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     *            the author to set
     */
    public void setAuthor(
            String author) {
        this.author = author;
    }

    /**
     * @return the funcList
     */
    public List<DocItemBean> getFuncList() {
        return funcList;
    }

    /**
     * @param funcList
     *            the funcList to set
     */
    public void setFuncList(
            List<DocItemBean> funcList) {
        this.funcList = funcList;
    }

    /**
     * @return the refdtoNameList
     */
    public List<String> getRefdtoNameList() {
        return refdtoNameList;
    }

    /**
     * @param refdtoNameList
     *            the refdtoNameList to set
     */
    public void setRefdtoNameList(
            List<String> refdtoNameList) {
        this.refdtoNameList = refdtoNameList;
    }

    /**
     * @return the refdtoTypeList
     */
    public List<String> getRefdtoTypeList() {
        return refdtoTypeList;
    }

    /**
     * @param refdtoTypeList
     *            the refdtoTypeList to set
     */
    public void setRefdtoTypeList(
            List<String> refdtoTypeList) {
        this.refdtoTypeList = refdtoTypeList;
    }

    /**
     * @return the refdtoPkgList
     */
    public List<String> getRefdtoPkgList() {
        return refdtoPkgList;
    }

    /**
     * @param refdtoPkgList
     *            the refdtoPkgList to set
     */
    public void setRefdtoPkgList(
            List<String> refdtoPkgList) {
        this.refdtoPkgList = refdtoPkgList;
    }

    /**
     * @return the classAnnotationList
     */
    public Set<String> getClassAnnotationList() {
        return classAnnotationList;
    }

    /**
     * @param classAnnotationList
     *            the classAnnotationList to set
     */
    public void setClassAnnotationList(
            Set<String> classAnnotationList) {
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
    public void setClassImportList(
            Set<String> classImportList) {
        this.classImportList = classImportList;
    }

    /**
     * @return the sheetName
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * @param sheetName
     *            the sheetName to set
     */
    public void setSheetName(
            String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(
            String updateDate) {
        this.updateDate = updateDate;
    }

}
