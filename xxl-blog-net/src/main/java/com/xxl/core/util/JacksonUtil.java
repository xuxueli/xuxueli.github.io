package com.xxl.core.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson工具类
 * 
 * 1、obj need private and set/get；
 * 2、do not support inner class；
 * 
 * @author xuxueli 2015-9-25 18:02:56
 */
public class JacksonUtil {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * bean、array、List、Map --> json
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static String writeValueAsString(Object obj) throws Exception {
        return getInstance().writeValueAsString(obj);
    }

    /**
     * string --> bean、Map、List(array)
     * 
     * @param jsonStr
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T readValue(String jsonStr, Class<T> clazz) throws Exception {
    	return getInstance().readValue(jsonStr, clazz);
    }
    public static <T> T readValueRefer(String jsonStr, Class<T> clazz) throws Exception {
    	return getInstance().readValue(jsonStr, new TypeReference<T>() { });	
    }

    public static void main(String[] args) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("aaa", "111");
			map.put("bbb", "222");
			System.out.println(writeValueAsString(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
