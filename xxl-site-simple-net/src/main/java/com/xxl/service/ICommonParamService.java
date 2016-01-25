package com.xxl.service;

/**
 * 通用配置
 * @author xuxueli
 */
public interface ICommonParamService {
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public Integer getInt(String key);
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public String getString(String key);
	
}
