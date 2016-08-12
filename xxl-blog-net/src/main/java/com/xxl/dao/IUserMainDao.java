package com.xxl.dao;

import com.xxl.core.model.main.UserMain;

/**
 * 用户信息
 * @author xuxueli
 */
public interface IUserMainDao {

	/**
	 * 保存
	 * @param email
	 * @param password
	 * @param state
	 */
	public void insert(String email, String password, int state);

	/**
	 * 查询
	 * @param email		: 邮箱
	 * @param encrypt	: 密码 (md5加密)
	 * @return
	 */
	public UserMain getByEmailPwd(String email, String password);

	/**
	 * 查询
	 * @param email : 注册邮箱
	 * @return
	 */
	public UserMain getByEmail(String email);

	/**
	 * 查询
	 * @param userId	: 用户ID
	 * @return
	 */
	public UserMain getById(int userId);

	/**
	 * 更新
	 * @param userId : 用户ID
	 * @param state	 : 用户状态
	 * @return
	 */
	public int updateState(int userId, int state);

}
