/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.mapping;

import java.util.Set;

import org.umeframework.dora.exception.SystemException;
import org.umeframework.dora.service.ServiceWrapper;

/**
 * Service class and method naming rule identify interface
 * 
 * @author Yue MA
 * 
 */
public interface ServiceMapping {
	/**
	 * getServiceReference
	 * 
	 * @param id
	 * @return
	 */
	ServiceWrapper getService(String id);

	/**
	 * addService
	 * 
	 * @param id
	 * @param conf
	 * @throws SystemException
	 */
	void addService(String id, String conf) throws SystemException;

	/**
	 * enableService
	 * 
	 * @param id
	 */
	void enableService(String id);

	/**
	 * disableService
	 * 
	 * @param id
	 */
	void disableService(String id);
	
	/**
	 * serviceSet
	 * 
	 * @return
	 */
	Set<String> serviceSet();

}
