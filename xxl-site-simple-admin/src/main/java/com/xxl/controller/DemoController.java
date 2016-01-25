package com.xxl.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.result.ReturnT;
import com.xxl.service.ITriggerService;

/**
 * 用户操作
 * @author xuxueli
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
	
	@Autowired
    private ITriggerService triggerService;
	
	@RequestMapping(value="/{secretKey}/{funCode}")
	@ResponseBody
	@PermessionType
	public ReturnT<String> test(@PathVariable("secretKey")String secretKey, @PathVariable("funCode")int funCode){
		if (!StringUtils.equals("xuxueli", secretKey)) {
			return null;
		}

		switch (funCode) {
		case 5000:
			triggerService.wallClawlerStart();
			break;
		case 5001:
			triggerService.wallClawlerStop();
			break;
		default:
			break;
		}
		
		return new ReturnT<String>();
	}
	
}
