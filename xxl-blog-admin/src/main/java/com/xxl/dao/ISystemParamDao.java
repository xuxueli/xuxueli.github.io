package com.xxl.dao;

import com.xxl.core.model.main.SystemParam;

/**
 * @author xuxueli
 */
public interface ISystemParamDao {
	
	public SystemParam load(String key);
	
	public int save(SystemParam systemParam);

	public int update(SystemParam systemParam);
	
}
