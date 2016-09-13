package com.xxl.dao;

import com.xxl.core.model.main.CommonParam;

/**
 * 通用配置
 * @author xuxueli
 */
public interface ICommonParamDao {
	
	/**
	 * 查询
	 * @param key
	 * @return
	 */
	public CommonParam get(String key);
}
