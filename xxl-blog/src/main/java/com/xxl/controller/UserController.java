package com.xxl.controller;

import com.xxl.controller.annotation.PermessionLimit;
import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.result.ReturnT;
import com.xxl.service.IHtmlGenerateService;
import com.xxl.service.IUserService;
import com.xxl.service.helper.LoginIdentityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@Autowired
	private IUserService userService;
	@Autowired
	private IHtmlGenerateService htmlGenerateService;
	

	@RequestMapping("/login")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password){
		return userService.login(response, session, email, password);
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		LoginIdentityHelper.logout(request, response, session);
		return new ReturnT<String>();
	}
	
	@RequestMapping("/loginCheck")
	@ResponseBody
	@PermessionLimit
	public ReturnT<LoginIdentity> loginCheck(HttpServletRequest request, HttpSession session){
		LoginIdentity loginIdentity = LoginIdentityHelper.cacheCheck(request, session);
		return new ReturnT<LoginIdentity>(loginIdentity);
	}


	@RequestMapping(value="/generateHtml")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> test(){
		long start = System.currentTimeMillis();
		logger.info("全站静态化... start:{}", start);

		htmlGenerateService.generateNetHtml();

		long end = System.currentTimeMillis();
		logger.info("全站静态化... end:{}, cost:{}", end, end - start);
		return new ReturnT<String>(null);
	}

}
