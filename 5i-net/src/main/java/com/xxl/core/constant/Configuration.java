package com.xxl.core.constant;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.core.util.DateFormatUtil;
import com.xxl.core.util.PropertiesUtil;

/**
 * 反射加载配置
 * @author xuxueli
 *
 */
public class Configuration {	
	private static transient Logger logger = LoggerFactory.getLogger(Configuration.class);
	static {
		init();
	}
	
	/**
	 * Configuration初始化
	 */
	public static void init() {
		
		String propertyFileName = "configuration.properties";
		Properties prop = PropertiesUtil.loadProperties(propertyFileName);
		
		Field[] allFields = Configuration.class.getDeclaredFields();
		for (Field field : allFields) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
				field.setAccessible(true);
				
				Class<?> clazz = field.getType();
				String value = PropertiesUtil.getString(prop, field.getName());
				if (clazz == String.class) {
					try {
						field.set(Configuration.class, value);
					} catch (IllegalArgumentException e) {
						logger.error("[5i-admin init field value illegal argument exception.]", e);
					} catch (IllegalAccessException e) {
						logger.error("[5i-admin init field value illegal access exception.]", e);
					}
				} else if (clazz == Integer.TYPE){
					try {
						field.set(Configuration.class, Integer.parseInt(value));
					} catch (IllegalArgumentException e) {
						logger.error("[5i-admin init field value illegal argument exception.]", e);
					} catch (IllegalAccessException e) {
						logger.error("[5i-admin init field value illegal access exception.]", e);
					}
				} else if (clazz == Long.TYPE) {
					try {
						field.set(Configuration.class, Long.parseLong(value));
					} catch (IllegalArgumentException e) {
						logger.error("[5i-admin init field value illegal argument exception.]", e);
					} catch (IllegalAccessException e) {
						logger.error("[5i-admin init field value illegal access exception.]", e);
					}
				} else if (clazz == Date.class) {
					try {
						field.set(Configuration.class, DateFormatUtil.parseDateTime(value) );
					}  catch (IllegalAccessException e) {
						logger.error("[5i-admin init field value illegal access exception.]", e);
					} catch (ParseException e) {
						logger.error("[5i-admin init field value illegal access exception.]", e);
					}
				}
			}			
		}
		
	}
	
	// ---------------------------------------------
	public static String qiniu_accesskey;
	public static String qiniu_secretkey;
	
}
