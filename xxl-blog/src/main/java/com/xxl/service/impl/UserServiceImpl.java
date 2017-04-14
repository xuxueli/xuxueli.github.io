package com.xxl.service.impl;

import com.xxl.core.model.main.UserMain;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.Md5Util;
import com.xxl.dao.IUserMainDao;
import com.xxl.service.IUserService;
import com.xxl.service.helper.LoginIdentityHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户信息
 * @author xuxueli
 */
@Service()
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserMainDao userMainDao;


	/*
	 * 登陆
	 * @see com.xxl.service.IUserService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password) {
		if (StringUtils.isBlank(email)) {
			return new ReturnT<String>(ReturnT.FAIL, "登陆失败,请输入账号邮箱");
		}
		if (StringUtils.isBlank(password)) {
			return new ReturnT<String>(ReturnT.FAIL, "登陆失败,请输入密码");
		}
		UserMain userMain = userMainDao.getByEmailPwd(email, Md5Util.encrypt(password));
		if (userMain == null) {
			return new ReturnT<String>(ReturnT.FAIL, "登陆失败,账号邮箱或密码错误");
		}
		
		LoginIdentityHelper.login(response, session, userMain);
		return new ReturnT<String>();
	}
	
}
