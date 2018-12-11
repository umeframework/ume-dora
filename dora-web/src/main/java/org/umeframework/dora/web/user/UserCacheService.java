/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.web.user;

import java.util.Set;

import org.umeframework.dora.service.UserObject;

/**
 * User object cache service interface.<br>
 *
 * @author Yue MA
 */
public interface UserCacheService {
	/**
	 * Get user object by token
	 *
	 * @param token
	 * @return
	 */
	UserObject getUserObject(String token);

	/**
	 * Set user object by token
	 *
	 * @param token
	 * @param user
	 */
	void setUserObject(String token, UserObject user);

	/**
	 * Get all cached UID
	 *
	 * @return
	 */
	Set<String> getCachedUIDs();

	/**
	 * Get all cached tokens by UID
	 * 
	 * @param uid
	 * @return
	 */
	Set<String> getTokenByUID(String uid);

	/**
	 * Delete cached user object by token
	 *
	 * @param token
	 */
	void removeUserByToken(String token);

	/**
	 * Delete all cached tokens by UID
	 *
	 * @param uid
	 */
	int deleteTokenByUID(String uid);

	/**
	 * Delete inactive tokens in cache
	 *
	 * @param inactiveSecond
	 * @return
	 */
	int deleteInactiveToken(int inactiveSecond);

	/**
	 * Refresh cached token value
	 * 
	 * @param token
	 * @return
	 */
	String refreshToken(String oldToken, UserObject userObj);

	/**
	 * Return token expired time (seconds)
	 * 
	 * @return
	 */
	long getTokenExpire();

}
