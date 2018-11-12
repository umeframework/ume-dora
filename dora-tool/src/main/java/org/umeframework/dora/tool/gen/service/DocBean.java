/* 
 * Copyright 2014-2017 UME Framework Group, GNU General Public License 
 */
package org.umeframework.dora.tool.gen.service;

import java.util.ArrayList;
import java.util.List;

/**
 * DocBean
 * 
 * @author Yue MA
 *
 */
public class DocBean {
	// document name
    private String docName;
    // component define bean
    private DocCmpBean cmpBean;
    // DTO define bean list
    private List<DocDtoBean> dtoBeanList = new ArrayList<DocDtoBean>();
    // Function define bean list
    private List<DocFuncBean> funcBeanList = new ArrayList<DocFuncBean>();

    /**
     * @return the dtoBeanList
     */
    public List<DocDtoBean> getDtoBeanList() {
        return dtoBeanList;
    }

    /**
     * @param dtoBeanList
     *            the dtoBeanList to set
     */
    public void setDtoBeanList(
            List<DocDtoBean> dtoBeanList) {
        this.dtoBeanList = dtoBeanList;
    }

    /**
     * @return the funcBeanList
     */
    public List<DocFuncBean> getFuncBeanList() {
        return funcBeanList;
    }

    /**
     * @param funcBeanList
     *            the funcBeanList to set
     */
    public void setFuncBeanList(
            List<DocFuncBean> funcBeanList) {
        this.funcBeanList = funcBeanList;
    }

    /**
     * @return the cmpBean
     */
    public DocCmpBean getCmpBean() {
        return cmpBean;
    }

    /**
     * @param cmpBean
     *            the cmpBean to set
     */
    public void setCmpBean(
            DocCmpBean cmpBean) {
        this.cmpBean = cmpBean;
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
    public void setDocName(
            String docName) {
        this.docName = docName;
    }

}
