package com.xxl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.service.ITriggerService;

/**
 * 控制面板
 * @author xuxueli
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
	
	@Autowired
    private ITriggerService triggerService;
	
	@RequestMapping(value="/generateHtml")
	@ResponseBody
	@PermessionType
	public String test(String secretKey, int secretCode){
		triggerService.generateNetHtml();
		return "200";
	}
	
	
}
