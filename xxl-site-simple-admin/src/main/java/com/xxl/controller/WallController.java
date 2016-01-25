package com.xxl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.model.main.WallInfo;
import com.xxl.core.result.ReturnT;
import com.xxl.service.IWallService;

/**
 * 一面墙
 * 
 * @author xuxueli
 */
@Controller
@RequestMapping("/wall")
public class WallController {

	@Autowired
	private IWallService wallService;

	@RequestMapping("/wallMain.do")
	@PermessionType(permessionNum = 1000600)
	public String wallMain() {
		return "wall/wall.main";
	}

	@RequestMapping("/wallQuery.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000600)
	public Map<String, Object> wallQuery(@RequestParam(required=false, defaultValue="0")int page,
			@RequestParam(required=false, defaultValue="0")int rows, String content) {
		
		return wallService.wallQuery(page, rows, content);
	}

	@RequestMapping("/wallAdd.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000600)
	public ReturnT<Integer> wallAdd(WallInfo wallInfo) {
		return wallService.wallAdd(wallInfo);
	}

	@RequestMapping("/wallDel.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000600)
	public ReturnT<Integer> wallDel(@RequestParam("ids[]") int[] ids) {
		return wallService.wallDel(ids);
	}

	@RequestMapping("/wallUpdate.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000600)
	public ReturnT<Integer> wallUpdate(WallInfo wallInfo) {
		return wallService.wallUpdate(wallInfo);
	}

}
