/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.user;

/**
 * Service login/logout common interface declare.<br>
 *
 * @author Yue MA
 */
public interface UserLoginService {
    /**
     * Implement the common login service once UserAuthenticator has been injected.
     * 
     * @param loginId
     * @param loginPassword
     * @return
     */
    String login(
            String loginId,
            String loginPassword,
            String...options);

    /**
     * Implement the common logout service once UserAuthenticator has been injected.
     * 
     * @param loginId
     */
    void logout(
            String loginId);

}
