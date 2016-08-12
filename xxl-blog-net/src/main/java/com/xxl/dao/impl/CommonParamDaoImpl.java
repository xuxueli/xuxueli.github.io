package com.xxl.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.CommonParam;
import com.xxl.dao.ICommonParamDao;

/**
 * 通用配置
 * @author xuxueli
 */
@Repository
public class CommonParamDaoImpl implements ICommonParamDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 查询
	 * @see com.xxl.dao.ICommonParamDao#get(java.lang.String)
	 */
	@Override
	public CommonParam get(String key) {
		return sqlSessionTemplate.selectOne("CommonParamMapper.get", key);
	}

}
