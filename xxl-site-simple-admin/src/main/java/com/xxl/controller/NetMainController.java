package com.xxl.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.constant.CommonDic.SystemParamEnum;
import com.xxl.core.model.main.SystemParam;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.HttpUtil;
import com.xxl.dao.ISystemParamDao;
import com.xxl.service.ITriggerService;

/**
 * 爬虫管理
 * @author xuxueli
 */
@Controller
@RequestMapping("/net")
public class NetMainController {
	
	@Autowired
    private ITriggerService triggerService;
	@Resource
	private ISystemParamDao systemParamDao;
	
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
	
	@RequestMapping("/netHtml")
	@ResponseBody
	@PermessionType(permessionNum = 1000700)
	public ReturnT<Integer> netHtml() {
		
		SystemParam systemParam = systemParamDao.load(SystemParamEnum.NET_ADDRESS.name());
		if (systemParam==null || StringUtils.isBlank(systemParam.getValue())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "请配置官网地址");
		}
		
		StringBuffer sBuffer = new StringBuffer();
		int failNum = 0;
		String[] urls = systemParam.getValue().split(",");
		for (String reqURL : urls) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("secretKey", "999");
			params.put("secretCode", "999");
			String ret;
			try {
				ret = HttpUtil.post(reqURL, params);
			} catch (Exception e) {
				e.printStackTrace();
				ret = e.getMessage();
			}
			if (!"200".equals(ret)) {
				sBuffer.append("[").append(reqURL).append("]；");
				failNum++;
			}
		}
		
		if (failNum > 0) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), 
					"静态化共计[" + urls.length + "]个站点，[" + failNum + "]个失败。<br>失败列表：" + sBuffer.toString());
		}
		return new ReturnT<Integer>();
	}
	
	@RequestMapping("/netAddressLoad")
	@ResponseBody
	@PermessionType(permessionNum = 1000700)
	public ReturnT<SystemParam> netAddressLoad() {
		SystemParam systemParam = systemParamDao.load(SystemParamEnum.NET_ADDRESS.name());
		return new ReturnT<SystemParam>(systemParam);
	}
	
	@RequestMapping("/netAddressSave")
	@ResponseBody
	@PermessionType(permessionNum = 1000700)
	public ReturnT<SystemParam> netAddressSave(String netAddress) {
		SystemParam systemParam = systemParamDao.load(SystemParamEnum.NET_ADDRESS.name());
		if (systemParam!=null) {
			systemParam.setValue(netAddress);
			systemParamDao.update(systemParam);
		} else {
			systemParam = new SystemParam();
			systemParam.setKey(SystemParamEnum.NET_ADDRESS.name());
			systemParam.setValue(netAddress);
			systemParam.setTitle(SystemParamEnum.NET_ADDRESS.getTitle());
			systemParamDao.save(systemParam);
		}
		return new ReturnT<SystemParam>(systemParam);
	}
	
}
