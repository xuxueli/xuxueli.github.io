package com.xxl.core.util;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * Memcached客户端工具类(Base on Xmemcached)
 * @author xuxueli $2015-4-14 20:13:11
 */
public final class XMemcachedUtil {
	
	// MemcachedClient
	private static final int cacheExpireTime = 7200;
	private static MemcachedClient memcachedClient;
	private static MemcachedClient getInstance() {
		if (memcachedClient == null) {
			// 构建分布式权重client
			synchronized (MemcachedClient.class) {
				String propertyFileName = "memcached.properties";
				Properties prop = PropertiesUtil.loadProperties(propertyFileName);
				// client地址
				String serverAddress = PropertiesUtil.getString(prop, "server.address").replace(",", " ");
				// client权重
				String[] weightsArr = PropertiesUtil.getString(prop, "server.weights").split(",");
				int[] weights = new int[weightsArr.length];
				for (int i = 0; i < weightsArr.length; i++) {
					weights[i] = Integer.parseInt(weightsArr[i]);
				}

				// 连接池：高负载下nio单连接有瓶颈,设置连接池可分担memcached请求负载,从而提高系统吞吐量
				MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddressMap(serverAddress), weights);
				builder.setConnectionPoolSize(5);	// 设置连接池大小，即客户端个数 NIO
				builder.setFailureMode(true);		// 宕机报警 
				builder.setSessionLocator(new KetamaMemcachedSessionLocator());	//  分布策略:一致性哈希

				// 客户端
				try {
					memcachedClient = builder.build();
					memcachedClient.setPrimitiveAsString(true);	// 
					memcachedClient.setConnectTimeout(3000L);	// 连接超时
					memcachedClient.setOpTimeout(1500L);		// 全局等待时间
					//memcachedClient.addStateListener(new MemcachedListener());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		if (memcachedClient == null) {
			throw new NullPointerException("Null MemcachedClient,please check memcached has been started");
		}
		return memcachedClient;
	}
	
	/**
	 * 存储 (默认7200秒)
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 */
	public static void set(String key, Object value) {
		try {
			getInstance().set(key, cacheExpireTime, value);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 存储
	 * @param key		:存储的key名称
	 * @param value		:实际存储的数据
	 * @param expTime	:expire时间 (单位秒,超过这个时间,memcached将这个数据替换出,0表示永久存储(默认是一个月))
	 */
	public static void set(String key, Object value, int expTime) {
		try {
			getInstance().set(key, expTime, value);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		try {
			Object value = getInstance().get(key);
			return value;
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 删除
	 * @param key
	 */
	public static void delete(String key) {
		try {
			getInstance().delete(key);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 递增
	 * @param key
	 */
	public static void incr(String key) {
		try {			
			getInstance().incr(key, 1);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		set("asd", 500);
		System.out.println(get("asd"));
	}
	
}
