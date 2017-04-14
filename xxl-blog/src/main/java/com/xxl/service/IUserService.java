package com.xxl.service;

import com.xxl.core.result.ReturnT;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户信息
 * @author xuxueli
 */
public interface IUserService {

	/**
	 * 登陆
	 * @param email
	 * @param password
	 * @return
	 */
	ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password);

}
