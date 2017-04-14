package com.xxl.blog.dao;

import com.xxl.blog.core.model.User;

/**
 * @author xuxueli
 */
public interface IUserDao {

	public int add(User user);
	public int update(User user);
	public User findByUserName(String userName);

}
