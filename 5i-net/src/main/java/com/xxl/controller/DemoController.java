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
public class DemoController {
	
	@RequestMapping("/index")
	@PermessionType
	public String index(){
		return "index";
	}
	
	@RequestMapping("/wall")
	@PermessionType
	public String wall(){
		return "index";
	}
	
}
