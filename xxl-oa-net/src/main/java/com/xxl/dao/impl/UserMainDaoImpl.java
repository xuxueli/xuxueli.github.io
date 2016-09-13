package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.UserMain;
import com.xxl.dao.IUserMainDao;

/**
 * 用户信息
 * @author xuxueli
 */
@Repository
public class UserMainDaoImpl implements IUserMainDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/*
	 * 保存
	 * @see com.xxl.dao.IUserMailDao#insert(java.lang.String, java.lang.String, int)
	 */
	@Override
	public void insert(String email, String password, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("password", password);
		params.put("state", state);
		
		sqlSessionTemplate.insert("UserMainMapper.insert", params);
	}

	/*
	 * 查询
	 * @see com.xxl.dao.IUserMainDao#getByEmailPwd(java.lang.String, java.lang.String)
	 */
	@Override
	public UserMain getByEmailPwd(String email, String password){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("password", password);
		return sqlSessionTemplate.selectOne("UserMainMapper.getByEmailPwd", params);
	}

	/*
	 * 查询,注册email
	 * @see com.xxl.dao.IUserMainDao#getByEmail(java.lang.String)
	 */
	@Override
	public UserMain getByEmail(String email) {
		return sqlSessionTemplate.selectOne("UserMainMapper.getByEmail", email);
	}

	/*
	 * 查询,根据ID
	 * @see com.xxl.dao.IUserMainDao#getById(int)
	 */
	@Override
	public UserMain getById(int userId) {
		return sqlSessionTemplate.selectOne("UserMainMapper.getById", userId);
	}

	/*
	 * 更新
	 * @see com.xxl.dao.IUserMainDao#updateState(int, int)
	 */
	@Override
	public int updateState(int userId, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("state", state);
		return sqlSessionTemplate.update("UserMainMapper.updateState", params);
	}
	

}
