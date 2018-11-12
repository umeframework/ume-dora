/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.user.impl;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.cache.CacheManager;
import org.umeframework.dora.exception.TimeoutException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.UserObject;
import org.umeframework.dora.service.user.UserCacheService;
import org.umeframework.dora.util.CodecUtil;
import org.umeframework.dora.util.DateUtil;

/**
 * User object cache service implementation.<br>
 *
 * @author Yue MA
 */
public class UserCacheServiceImpl extends BaseComponent implements UserCacheService {
	/**
	 * Cache manager instance
	 */
	@Resource(name = BeanConfigConst.DEFAULT_USER_CACHE_MANAGER)
	private CacheManager userCacheManager;
	/**
	 * Disable user login in on multiple browser
	 */
	private boolean singleLogin = false;
	/**
	 * Token expire time, 0 mean never expired, unit is second
	 */
	private long tokenExpire = 3600 * 24 * 30;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#get(java.lang.String)
	 */
	@Override
	public UserObject getUserObject(String token) {
		// Get cached user object
		UserObject userObj = userCacheManager.get(token);
		if (userObj != null) {
			// check token expire time
			Timestamp curremtTime = super.getCurrentTimestamp();
			long timeInterval = curremtTime.getTime() - userObj.getLastTransactionTime().getTime();
			if (tokenExpire != 0 && timeInterval != 0 && timeInterval > tokenExpire * 1000) {
				getLogger().warn("Token expired of " + userObj.getUid());
				this.removeUserByToken(token);
				throw new TimeoutException("Login user expired.");
			}
			// update last transaction time
			userObj.setLastTransactionTime(curremtTime);
			this.setUserObject(token, userObj);
		}
		return userObj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#set(java.lang.String, org.umeframework.dora.service.UserObject)
	 */
	@Override
	synchronized public void setUserObject(String token, UserObject user) {
		userCacheManager.set(token, user);
		getLogger().debug("Updated user " + token + "," + user.getUid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#getCachedUIDs()
	 */
	@Override
	public Set<String> getCachedUIDs() {
		return userCacheManager.keys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#deleteToken(java.lang.String)
	 */
	@Override
	synchronized public void removeUserByToken(String token) {
		userCacheManager.remove(token);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.user.UserCacheService#refreshToken(java.lang.String)
	 */
	synchronized public String refreshToken(String oldToken, UserObject userObj) {
		if (userObj == null) {
			userObj = userCacheManager.get(oldToken);
		}
		if (userObj == null) {
			getLogger().error("No found effective token.");
			return oldToken;
		}
		String newToken = createTokenStr(userObj);
		userCacheManager.set(newToken, userObj);
		userCacheManager.remove(oldToken);
		getLogger().info("Refresh token:" + oldToken);
		return newToken;
	}

	/**
	 * createTokenStr
	 * 
	 * @param userObj
	 * @return
	 */
	protected String createTokenStr(UserObject userObj) {
		String token = CodecUtil.encodeMD5Hex(userObj.getUid()
		        + DateUtil.dateToString(super.getTransactionStartTime(), DateUtil.FORMAT.YYYYMMDDHHMMSSMMM));
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#deleteTokenByUID(java.lang.String)
	 */
	@Override
	public int deleteTokenByUID(String uid) {
		int count = 0;

		Set<String> keys = userCacheManager.keys();
		for (String key : keys) {
			UserObject user = userCacheManager.get(key);
			if (user != null && user.getUid().equals(uid)) {
				userCacheManager.remove(key);
				count++;
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#getTokenByUID(java.lang.String)
	 */
	@Override
	public Set<String> getTokenByUID(String uid) {
		Set<String> tokens = new HashSet<String>();
		Set<String> keys = userCacheManager.keys();
		for (String key : keys) {
			UserObject user = userCacheManager.get(key);
			if (user != null && user.getUid().equals(uid)) {
				tokens.add(key);
			}
		}
		return tokens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserCacheService#deleteInactiveTokens(int)
	 */
	@Override
	public int deleteInactiveToken(int inactiveSecond) {
		int count = 0;
		long inactiveMillisecond = inactiveSecond * 1000;
		long currentTime = getCurrentTimestamp().getTime();

		Set<String> keys = userCacheManager.keys();
		for (String key : keys) {
			UserObject user = userCacheManager.get(key);

			long dvalue = currentTime - user.getLastTransactionTime().getTime();
			if (dvalue >= inactiveMillisecond) {
				userCacheManager.remove(key);
				count++;
			}
		}
		return count;
	}

	/**
	 * @return the singleLogin
	 */
	public boolean isSingleLogin() {
		return singleLogin;
	}

	/**
	 * @param singleLogin
	 *            the singleLogin to set
	 */
	public void setSingleLogin(boolean singleLogin) {
		this.singleLogin = singleLogin;
	}

	/**
	 * @return the tokenExpire
	 */
	public long getTokenExpire() {
		return tokenExpire;
	}

	/**
	 * @param tokenExpire
	 *            the tokenExpire to set
	 */
	public void setTokenExpire(long tokenExpire) {
		this.tokenExpire = tokenExpire;
	}

}
