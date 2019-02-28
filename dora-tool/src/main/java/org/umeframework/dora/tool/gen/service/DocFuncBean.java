/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DocFuncBean
 */
public class DocFuncBean {
    // Define items
    private String sheetName;
    private String id;
    private String wsId;
    private String name;
    private String comment;
    private String author;
    private String updateDate;
    private List<String> details;
    private String pkg;

    private List<DocItemBean> inParamList = new ArrayList<DocItemBean>();
    private List<DocItemBean> outParamList = new ArrayList<DocItemBean>();

    private Set<String> classImportList = new HashSet<String>();
    private Set<String> inParamClassImportList = new HashSet<String>();
    private Set<String> outParamClassImportList = new HashSet<String>();

    /**
     * getOutParam
     * 
     * @return
     */
    public DocItemBean getOutParam() {
        DocItemBean nullDocItemBean = new DocItemBean();
        nullDocItemBean.setJavaType("Object");
        return outParamList != null && outParamList.size() > 0 ? outParamList.get(0) : nullDocItemBean;
    }

    /**
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
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
    public void setName(String name) {
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
    public void setComment(String comment) {
        this.comment = comment;
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
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the inParamList
     */
    public List<DocItemBean> getInParamList() {
        return inParamList;
    }

    /**
     * @param inParamList
     *            the inParamList to set
     */
    public void setInParamList(List<DocItemBean> inParamList) {
        this.inParamList = inParamList;
    }

    /**
     * @return the outParamList
     */
    public List<DocItemBean> getOutParamList() {
        return outParamList;
    }

    /**
     * @param outParamList
     *            the outParamList to set
     */
    public void setOutParamList(List<DocItemBean> outParamList) {
        this.outParamList = outParamList;
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
    public void setSheetName(String sheetName) {
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
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the details
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * @param details
     *            the details to set
     */
    public void setDetails(List<String> details) {
        this.details = details;
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
    public void setPkg(String pkg) {
        this.pkg = pkg;
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
     * @return the wsId
     */
    public String getWsId() {
        return wsId;
    }

    /**
     * @param wsId
     *            the wsId to set
     */
    public void setWsId(String wsId) {
        this.wsId = wsId;
    }

    /**
     * @return the inParamClassImportList
     */
    public Set<String> getInParamClassImportList() {
        return inParamClassImportList;
    }

    /**
     * @param inParamClassImportList
     *            the inParamClassImportList to set
     */
    public void setInParamClassImportList(Set<String> inParamClassImportList) {
        this.inParamClassImportList = inParamClassImportList;
    }

    /**
     * @return the outParamClassImportList
     */
    public Set<String> getOutParamClassImportList() {
        return outParamClassImportList;
    }

    /**
     * @param outParamClassImportList
     *            the outParamClassImportList to set
     */
    public void setOutParamClassImportList(Set<String> outParamClassImportList) {
        this.outParamClassImportList = outParamClassImportList;
    }

}
