package com.xxl.service;

import javax.servlet.http.HttpSession;

import com.xxl.core.result.ReturnT;

public interface ILoginService {

	/**
	 * 登陆
	 * 
	 * @param session
	 * @param username
	 * @param password
	 * @return
	 */
	public ReturnT<String> login (HttpSession session, String username, String password, int roleId);

	/**
	 * 注销登陆
	 * @param session
	 * @return
	 */
	public ReturnT<String> logout(HttpSession session);

	/**
	 * 修改密码
	 * @param session
	 * @param password
	 * @param newPwd
	 * @param reNewPwd
	 * @return
	 */
	public ReturnT<String> modifyPwd(HttpSession session, String password, String newPwd, String reNewPwd);

}
