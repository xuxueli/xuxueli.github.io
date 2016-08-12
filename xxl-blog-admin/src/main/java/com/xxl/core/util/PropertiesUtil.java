package com.xxl.core.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties工具类
 * 
 * @author xuxueli
 */
public class PropertiesUtil {
	protected static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	/**
	 * 加载Properties
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			URL url = loder.getResource(propertyFileName); // 方式2：配置更新不需要重启JVM
			in = new FileInputStream(url.getPath());
			// in = loder.getResourceAsStream(propertyFileName); //
			// 方式2：配置更新需重启JVM
			if (in != null) {
				prop.load(in);
			}
		} catch (IOException e) {
			logger.error("load {} error!", propertyFileName);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("close {} error!", propertyFileName);
				}
			}
		}
		return prop;
	}

	/**
	 * 获取配置String
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		return prop.getProperty(key);
	}

	/**
	 * 获取配置int
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected static int getInt(Properties prop, String key) {
		return Integer.parseInt(getString(prop, key));
	}

	
	public static void main(String[] args) {
		Properties prop = loadProperties("memcached.properties");
		System.out.println(getString(prop, "server.address"));
	}

}
