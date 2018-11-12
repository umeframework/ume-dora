/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.cache.impl;

import java.util.Set;

import org.umeframework.dora.cache.CacheManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis cache implementation
 *
 * @author Yue MA
 */
public class JedisCachedImpl extends CachedSupport implements CacheManager {
	/**
	 * Block for synchronized thread
	 */
	private static final byte[] block = new byte[0];

	/**
	 * JedisCachedImpl
	 *
	 * @param expire
	 */
	public JedisCachedImpl(String host, int port, int expire) {
		super(expire);
	}

	/**
	 * cached server host address
	 */
	private String host;
	/**
	 * cached server port number
	 */
	private int port;
	
	/**
	 * connection pool
	 */
	private JedisPool pool;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#init()
	 */
	@Override
	synchronized public void init() {
		JedisPoolConfig cfg = new JedisPoolConfig();
		pool = new JedisPool(cfg, host, port);
		getLogger().info("Jedis cache instance start.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#shutdown()
	 */
	@Override
	synchronized public void shutdown() {
		pool.destroy();
		getLogger().info("Jedis cache instance shutdown.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#set(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void doSet(String key, int expire, Object value) {
		synchronized (block) {
			byte[] bkey = key.getBytes();
			byte[] bvalue = serialize(value);
			Jedis writer = pool.getResource();
			if (writer.exists(key)) {
				// Only set the key if it already exist
				writer.set(bkey, bvalue, "XX".getBytes(), "PX".getBytes(), expire);
			} else {
				// Only set the key if it does not already exist
				writer.set(bkey, bvalue, "NX".getBytes(), "PX".getBytes(), expire);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#get(java.lang.String)
	 */
	@Override
	public Object doGet(String key) {
		byte[] kbytes = key.getBytes();
		Jedis reader = pool.getResource();
		byte[] vbytes = reader.get(kbytes);
		Object obj = unserialize(vbytes);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#keys()
	 */
	@Override
	public Set<String> keys() {
		Jedis reader = pool.getResource();
		return reader.keys("*");
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#remove(java.lang.String)
	 */
	@Override
	public void doRemove(String key) {
		synchronized (block) {
			byte[] kbytes = key.getBytes();
			Jedis writer = pool.getResource();
			if (writer.exists(key)) {
				writer.del(kbytes);
			}
		}
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
