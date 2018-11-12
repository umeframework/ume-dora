/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.service.user.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.umeframework.dora.bean.BeanConfigConst;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.exception.AuthenticationException;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.service.UserObject;
import org.umeframework.dora.service.user.UserAuthenticator;
import org.umeframework.dora.service.user.UserCacheService;
import org.umeframework.dora.service.user.UserLoginService;
import org.umeframework.dora.util.CodecUtil;
import org.umeframework.dora.util.DateUtil;

/**
 * Service login/logout common implementation.<br>
 *
 * @author Yue MA
 */
public class UserLoginServiceImpl extends BaseComponent implements UserLoginService {
	/**
	 * User authentication instance.<br>
	 */
	@Qualifier(BeanConfigConst.DEFAULT_USER_AUTHENTICATOR)
	@Autowired(required = false)
	private UserAuthenticator<UserObject> userAuthenticator;
	/**
	 * User cache manager instance.<br>
	 */
	@Qualifier(BeanConfigConst.DEFAULT_USER_CACHE_SERVICE)
	@Autowired(required = false)
	private UserCacheService userCacheService;
	/**
	 * Disable user login in on multiple client.<br>
	 */
	private boolean singleLogin = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserLoginService#login(java.lang.String, java.lang.String)
	 */
	@Override
	synchronized public String login(String loginId, String loginPassword, String... loginOptions) {
		if (userAuthenticator == null) {
			throw new AuthenticationException("Authentication failed: user authenticator configuration was not setup.");
		}
		UserObject userObj = userAuthenticator.doAuthentication(loginId, loginPassword, loginOptions);
		if (userObj == null) {
			throw new AuthenticationException("Authentication failed: no user object return from authentication.");
		}
		if (userObj.getUid() == null) {
			userObj.setUid(loginId);
		}
		if (userObj.getLastTransactionTime() == null) {
			userObj.setLastTransactionTime(super.getTransactionStartTime());
		}

		// append user authentication properties (ACL)
		userAuthenticator.doAuthorization(userObj, loginOptions);

		// use default token generate rule if no business token provided
		String token = createToken(userObj);
		// Set internal token into context
		SessionContext.open().setToken(token);
		// caching user object
		if (singleLogin) {
			// check multiple login
			int cnt = userCacheService.deleteTokenByUID(userObj.getUid());
			if (cnt > 0) {
				super.getLogger().info("Remove previous login status of current user.");
			}
		}
		userCacheService.setUserObject(token, userObj);
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.service.com.UserLoginService#logout(java.lang.String)
	 */
	@Override
	synchronized public void logout(String loginId) {
		userCacheService.deleteTokenByUID(loginId);
		// updateLogoutStatus(sysUserId);
		HttpSession hs = SessionContext.open().getHttpSession();
		if (hs != null) {
			hs.removeAttribute(SessionContext.TOKEN);
		}
		
		super.createMessage(loginId + " exit system.");
		super.getLogger().info("User logout: ", loginId);
	}

	/**
	 * createCode
	 * 
	 * @param userObj
	 * @return
	 */
	protected String createToken(UserObject userObj) {
		String token = CodecUtil.encodeMD5Hex(userObj.getUid()
		        + DateUtil.dateToString(super.getTransactionStartTime(), DateUtil.FORMAT.YYYYMMDDHHMMSSMMM));
		return token;
	}

	/**
	 * @return the userAuthenticator
	 */
	public UserAuthenticator<UserObject> getUserAuthenticator() {
		return userAuthenticator;
	}

	/**
	 * @param userAuthenticator
	 *            the userAuthenticator to set
	 */
	public void setUserAuthenticator(UserAuthenticator<UserObject> userAuthenticator) {
		this.userAuthenticator = userAuthenticator;
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

}
