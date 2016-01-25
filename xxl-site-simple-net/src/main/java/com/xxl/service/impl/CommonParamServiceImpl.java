package com.xxl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.model.main.CommonParam;
import com.xxl.dao.ICommonParamDao;
import com.xxl.service.ICommonParamService;

/**
 * 通用配置
 * @author xuxueli
 */
@Service
public class CommonParamServiceImpl implements ICommonParamService {
	
	@Autowired
	private ICommonParamDao commonParamDao;

	/*
	 * 查询
	 * @see com.xxl.service.ICommonParamService#getInt(java.lang.String)
	 */
	@Override
	public Integer getInt(String key) {
		CommonParam param = commonParamDao.get(key);
		if (param == null) {
			return null;
		}
		return Integer.valueOf(param.getValue());
	}

	/*
	 * 查询
	 * @see com.xxl.service.ICommonParamService#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		CommonParam param = commonParamDao.get(key);
		if (param == null) {
			return null;
		}
		return String.valueOf(param.getValue());
	}
	
}
