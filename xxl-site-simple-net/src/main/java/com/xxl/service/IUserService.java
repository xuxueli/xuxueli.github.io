package com.xxl.service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xxl.core.result.ReturnT;

/**
 * 用户信息
 * @author xuxueli
 */
public interface IUserService {

	/**
	 * 注册
	 * @param email
	 * @param password
	 * @param rePassword
	 * @return
	 */
	ReturnT<String> reg(String email, String password,	String rePassword);

	/**
	 * 登陆
	 * @param email
	 * @param password
	 * @return
	 */
	ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password);

	/**
	 * 邮箱激活
	 * @param email
	 * @param email
	 * @param sendCode
	 * @return
	 */
	ReturnT<String> emailActivate(String email, String sendCode);
	
}
