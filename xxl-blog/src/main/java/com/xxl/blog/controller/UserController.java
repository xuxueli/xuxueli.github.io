package com.xxl.blog.controller;

import com.xxl.blog.controller.annotation.PermessionLimit;
import com.xxl.blog.core.model.User;
import com.xxl.blog.core.result.ReturnT;
import com.xxl.blog.core.util.HttpSessionUtil;
import com.xxl.blog.dao.IUserDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户操作
 * @author xuxueli
 */
@Controller
@RequestMapping("/user")
public class UserController {
	private static transient Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	private IUserDao userDao;

	@RequestMapping("/login")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> login(HttpServletResponse response, HttpSession session, String userName, String password){

		if (StringUtils.isBlank(userName)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入登录账号");
		}
		if (StringUtils.isBlank(password)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入登录密码");
		}

		User user = userDao.findByUserName(userName);
		if (user == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "用户不存在");
		}

		if (!password.equals(user.getPassword())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "登录密码错误");
		}

		HttpSessionUtil.set(session, "login_user", user);
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		HttpSessionUtil.remove(session, "login_user");
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/loginCheck")
	@ResponseBody
	@PermessionLimit
	public ReturnT<User> loginCheck(HttpSession session){
		User user = (User) HttpSessionUtil.get(session, "login_user");
		return user!=null?new ReturnT<User>(user):new ReturnT<User>(ReturnT.FAIL_CODE, "登录失效");
	}

}
