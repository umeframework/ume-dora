/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.cache.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

import org.umeframework.dora.cache.CacheManager;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

/**
 * SpyMemcached cache implementation
 *
 * @author Yue MA
 */
public class SpyMemcachedImpl extends CachedSupport implements CacheManager {
	/**
	 * Block for synchronized thread
	 */
	private static final byte[] block = new byte[0];
	/**
	 * cached server host address
	 */
	private String host;
	/**
	 * cached server port number
	 */
	private int port;
	/**
	 * cached server user name
	 */
	private String username;
	/**
	 * cached server user password
	 */
	private String password;

	/**
	 * client instance
	 */
	private MemcachedClient client;

	/**
	 * initialized flag
	 */
	private boolean initialized = false;

	/**
	 * SpyMemcachedImpl
	 *
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param expire
	 */
	public SpyMemcachedImpl(String host, int port, String username, String password, int expire) {

		super(expire);

		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Initialized MemcachedClient instance
	 *
	 * @throws IOException
	 */
	@Override
	public void init() {
		synchronized (block) {
			try {
				String[] authDescTypes = new String[] { "PLAIN" };
				PlainCallbackHandler plainCallbackHandler = new PlainCallbackHandler(username, password);
				AuthDescriptor authDescriptor = new AuthDescriptor(authDescTypes, plainCallbackHandler);
				ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
				factoryBuilder.setProtocol(Protocol.BINARY);
				factoryBuilder.setAuthDescriptor(authDescriptor);
				ConnectionFactory connFactory = factoryBuilder.build();
				List<InetSocketAddress> addressList = AddrUtil.getAddresses(host + ":" + port);
				client = new MemcachedClient(connFactory, addressList);
				getLogger().info("SpyMemcached instance start @", host + ":" + port);
				initialized = true;
			} catch (Exception ex) {
				getLogger().error("SpyMemcached instance start failed. " + host + ":" + port, ex);
			}
		}
	}

	/**
	 * Shutdown MemcachedClient instance
	 */
	@Override
	public void shutdown() {
		if (client != null) {
			client.shutdown();
			getLogger().info("SpyMemcached instance shutdown.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#set(java.lang.String, int, java.lang.Object)
	 */
	@Override
	public void doSet(String key, int expire, Object value) {
		synchronized (block) {
			if (!initialized) {
				init();
			}
			client.set(key, expire, value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#get(java.lang.String)
	 */
	@Override
	public Object doGet(String key) {
		return client.get(key);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.umeframework.dora.cache.CacheManager#remove(java.lang.String)
	 */
	@Override
	public void doRemove(String key) {
		synchronized (block) {
			if (doGet(key) != null) {
				client.delete(key);
			}
		}
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.umeframework.dora.cache.CacheManager#keys()
	 */
	@Override
	public Set<String> keys() {
		return null;
	}
}
