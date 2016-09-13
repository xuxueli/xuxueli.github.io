package com.xxl.core.util;

import javax.servlet.ServletContext;

/**
 * ServletContext操作
 * @author Administrator
 *
 */
public class ServletContextUtil {

	/**
	 * 存入
	 * @param application
	 * @param key
	 * @param value
	 */
	public static void set(ServletContext application, String key, Object value){
		application.setAttribute(key, value);
	}
	
	/**
	 * 取出
	 * @param application
	 * @param key
	 * @return
	 */
	public static Object get(ServletContext application, String key){
		return application.getAttribute(key);
	}
	
	/**
	 * 移除
	 * @param application
	 * @param key
	 */
	public static void remove(ServletContext application, String key){
		application.removeAttribute(key);
	}
	
}
