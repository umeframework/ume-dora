/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.cache.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.umeframework.dora.cache.CacheManager;
import org.umeframework.dora.util.ThreadUtil;

/**
 * Temp memory cache implementation
 *
 * @author Yue MA
 */
public class TempMemoryCachedImpl extends CachedSupport implements CacheManager {
	/**
	 * Constant define
	 */
	private static final String KEY_DATA = "D";
	/**
	 * Constant define
	 */
	private static final String KEY_TIMESTAMP = "T";
	/**
	 * Constant define
	 */
	private static final String KEY_EXPIRED = "E";

	/**
	 * Cache instance
	 */
	private Map<String, Map<String, Object>> dataContainer;
	/**
	 * Check expired thread enable flag
	 */
	private boolean checkExpiredOn = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#init()
	 */
	@Override
	synchronized public void init() {
		if (dataContainer != null) {
			dataContainer.clear();
		}
		dataContainer = new ConcurrentHashMap<String, Map<String, Object>>();

		getLogger().info("Temp memory cache instance start.");

		if (super.getExpire() > 0) {
			new ThreadUtil(this, "checkExpired", new Object[] { super.getExpire() }, null, this.getLogger()).start();
			getLogger().info("Temp memory cache monitor thread start.");
		}
	}

	/**
	 * checkExpired
	 * 
	 * @param checkInterval(seconds)
	 */
	public void checkExpired(int checkInterval) {
		while (checkExpiredOn) {
			try {
				Thread.sleep((long) checkInterval * 1000);
			} catch (InterruptedException e1) {
				super.getLogger().error("Thread sleep exception.");
				return;
			}
			for (Map.Entry<String, Map<String, Object>> e : this.dataContainer.entrySet()) {
				String cachedKey = e.getKey();
				Map<String, Object> cachedData = e.getValue();
				if (cachedData != null) {
					long ts = (long) cachedData.get(KEY_TIMESTAMP);
					int expire = (int) cachedData.get(KEY_EXPIRED);
					if (expire > 0) {
						long pass = System.currentTimeMillis() - ts;
						if (pass / 1000 >= expire) {
							doRemove(cachedKey);
							super.getLogger().info("Remove expired instance from cache, key is " + cachedKey);
						}
					}
				}
			}
		}
	}

	/**
	 * TempMemoryCachedImpl
	 */
	public TempMemoryCachedImpl() {
		super(0);
	}

	/**
	 * TempMemoryCachedImpl
	 *
	 * @param expire
	 */
	public TempMemoryCachedImpl(int expire) {
		super(expire);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#set(java.lang.String, int, java.lang.Object)
	 */
	@Override
	synchronized public void doSet(String key, int expire, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KEY_DATA, value);
		map.put(KEY_EXPIRED, expire);
		map.put(KEY_TIMESTAMP, System.currentTimeMillis());
		dataContainer.put(key, map);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#get(java.lang.String)
	 */
	@Override
	public Object doGet(String key) {
		Map<String, Object> map = dataContainer.get(key);
		if (map == null) {
			return null;
		}
		Object value = map.get(KEY_DATA);
		long ts = (long) map.get(KEY_TIMESTAMP);
		int expire = (int) map.get(KEY_EXPIRED);
		if (expire > 0) {
			long pass = System.currentTimeMillis() - ts;
			if (pass / 1000 >= expire) {
				doRemove(key);
				return null;
			}
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#remove(java.lang.String)
	 */
	@Override
	synchronized public void doRemove(String key) {
		dataContainer.remove(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#shutdown()
	 */
	@Override
	synchronized public void shutdown() {
		this.checkExpiredOn = false;
		if (dataContainer != null) {
			dataContainer.clear();
			getLogger().info("Temp memory cache instance shutdown.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#keys()
	 */
	@Override
	public Set<String> keys() {
		return dataContainer.keySet();
	}

	/**
	 * @param checkExpiredEnable
	 *            the checkExpiredEnable to set
	 */
	public void setCheckExpiredOn(boolean checkExpiredEnable) {
		this.checkExpiredOn = checkExpiredEnable;
	}
}
