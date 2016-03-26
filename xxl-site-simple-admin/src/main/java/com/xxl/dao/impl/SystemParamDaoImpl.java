package com.xxl.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.SystemParam;
import com.xxl.dao.ISystemParamDao;

@Repository
public class SystemParamDaoImpl implements ISystemParamDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public SystemParam load(String key) {
		return sqlSessionTemplate.selectOne("SystemParamMapper.load", key);
	}

	@Override
	public int save(SystemParam systemParam) {
		return sqlSessionTemplate.insert("SystemParamMapper.save", systemParam);
	}

	@Override
	public int update(SystemParam systemParam) {
		return sqlSessionTemplate.update("SystemParamMapper.update", systemParam);
	}
	
}
