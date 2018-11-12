/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.mapping;

import java.util.List;
import java.util.Map;

/**
 * ServiceStatus
 * 
 * @author mmayye
 *
 */
public interface ServiceMappingDesc {
	
	/**
	 * getServiceStatus
	 * 
	 * @param serviceIdList
	 * @return
	 */
	List<Map<String, Object>> getServiceStatus(String... serviceIdList);
    /**
     * getEnableWebServiceList
     * 
     * @return
     */
    String[] getEnableServiceList();

    /**
     * getDisableWebServiceList
     * 
     * @return
     */
    String[] getDisableServiceList();

}
