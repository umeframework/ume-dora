/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.user;

import org.umeframework.dora.service.UserObject;

/**
 * User ACL validate interface declare.
 * 
 * @author Yue MA
 *
 */
public interface UserAccessValidator<U extends UserObject> {
    /**
     * isValid
     * 
     * @param userObj
     * @param token
     * @param sysId
     * @param serviceId
     * @return
     */
    boolean isValid(U userObj, String token, String sysId, String serviceId);

}
