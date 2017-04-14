package com.xxl.blog.dao.impl;

import com.xxl.blog.core.model.User;
import com.xxl.blog.dao.IUserDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 用户信息
 * @author xuxueli
 */
@Repository
public class UserDaoImpl implements IUserDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(User user) {
		return sqlSessionTemplate.insert("UserMapper.add", user);
	}

	@Override
	public int update(User user) {
		return sqlSessionTemplate.update("UserMapper.update", user);
	}

	@Override
	public User findByUserName(String userName){
		return sqlSessionTemplate.selectOne("UserMapper.findByUserName", userName);
	}

}
