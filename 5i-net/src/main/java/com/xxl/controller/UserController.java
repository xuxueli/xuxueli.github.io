package com.xxl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.result.ReturnT;

/**
 * 用户操作
 * @author xuxueli
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping("/login")
	@ResponseBody
	@PermessionType
	public ReturnT<String> login(String userName, String password){
		System.out.println(userName);
		System.out.println(password);
		return new ReturnT<String>();
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	@PermessionType
	public ReturnT<String> logout(){
		return new ReturnT<String>();
	}
	
	@RequestMapping("/reg")
	@ResponseBody
	@PermessionType
	public ReturnT<String> reg(String userName, String password, String rePassword){
		System.out.println(userName);
		System.out.println(password);
		System.out.println(rePassword);
		return new ReturnT<String>();
	}
	
}
