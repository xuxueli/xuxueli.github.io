package com.xxl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.result.ReturnT;
import com.xxl.service.ITriggerService;

/**
 * 爬虫管理
 * @author xuxueli
 */
@Controller
@RequestMapping("/net")
public class DemoController {
	
	@Autowired
    private ITriggerService triggerService;
	
	@RequestMapping("/netMain")
	@PermessionType(permessionNum = 1000700)
	public String wallMain() {
		return "net/net.main";
	}
	
	@RequestMapping("/wallClaw")
	@ResponseBody
	@PermessionType(permessionNum = 1000700)
	public ReturnT<Integer> wallClawRun(boolean runType) {
		if (runType) {
			triggerService.wallClawlerStart();
		} else {
			triggerService.wallClawlerStop();
		}
		return new ReturnT<Integer>();
	}
	
}
