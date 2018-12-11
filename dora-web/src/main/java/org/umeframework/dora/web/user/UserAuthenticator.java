/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.user;

import org.umeframework.dora.service.UserObject;

/**
 * User authentication interface declare.
 * 
 * @author Yue MA
 */
public interface UserAuthenticator<E extends UserObject> {

	/**
	 * Do authenticate by input login ID and password.<br>
	 * return non-empty UserObject instance (usually an child class inherit from UserObject) once authentication successful.<br>
	 * 
	 * @param loginId
	 *            - login ID
	 * @param loginPassword
	 *            - login password
	 * @param loginOptions
	 *            - login parameters
	 * @return actual UserObject instance
	 */
	E doAuthentication(String loginId, String loginPassword, String... loginOptions);
	
	/**
	 * Append user authorization information.<br>
	 * Query ACL data from "User-Role","Role","Role-Resource".<br>
	 * 
	 * @param userAclObj
	 * @param loginOptions
	 */
	void doAuthorization(E userAclObj, String... loginOptions);


}
