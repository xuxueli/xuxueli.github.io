package com.xxl.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.model.main.ArticleInfo;
import com.xxl.core.model.main.ArticleMenu;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.JacksonUtil;
import com.xxl.service.IArticleService;

/**
 * 文章
 * @author xuxueli
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
	
	@Autowired
	private IArticleService articleService;
	
	//-----------------------文章菜单相关-----------------------
	@RequestMapping("/articleMenuMain")
	@PermessionType(permessionNum = 1000400)
	public String articleMenuMain() {
		return "article/article.menu.main";
	}
	
	@RequestMapping("/articleMenuQuery")
	@ResponseBody
	@PermessionType(permessionNum = 1000400)
	public List<ArticleMenu> articleMenuQuery() {
		return articleService.articleMenuQuery();
	}
	
	@RequestMapping("/articleMenuAdd")
	@ResponseBody
	@PermessionType(permessionNum = 1000400)
	public ReturnT<Integer> articleMenuAdd(ArticleMenu menu) {
		return articleService.articleMenuAdd(menu);
	}
	
	@RequestMapping("/articleMenuDel")
	@ResponseBody
	@PermessionType(permessionNum = 1000400)
	public ReturnT<Integer> articleMenuDel(int menuId) {
		return articleService.articleMenuDel(menuId);
	}
	
	@RequestMapping("/articleMenuUpdate")
	@ResponseBody
	@PermessionType(permessionNum = 1000400)
	public ReturnT<Integer> articleMenuUpdate(ArticleMenu menu) {
		return articleService.articleMenuUpdate(menu);
	}
	
	//-----------------------文章相关-----------------------
	@RequestMapping("/articleMain")
	@PermessionType(permessionNum = 1000500)
	public String articleMain(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		// 文章菜单
		List<ArticleMenu> articleMenu = articleService.articleMenuQuery();
		String articleMenuJson = JacksonUtil.writeValueAsString(articleMenu);
		articleMenuJson = articleMenuJson.replace("menuId", "id");	// easyui tree json 数据格式化
		articleMenuJson = articleMenuJson.replace("name", "text");
		//articleMenuJson = articleMenuJson.replace("children", "children");
		model.addAttribute("articleMenuJson", articleMenuJson);
		return "article/article.main";
	}
	
	@RequestMapping("/articleQuery")
	@ResponseBody
	@PermessionType(permessionNum = 1000500)
	public Map<String, Object> articleQuery(@RequestParam(required=false, defaultValue="0")int page,
			@RequestParam(required=false, defaultValue="0")int rows, String title, 
			@RequestParam(required=false, defaultValue="0")int menuId) {
		
		Map<String, Object> resultMap = articleService.articleQuery(page, rows, title, menuId);
		return resultMap;
	}
	
	@RequestMapping("/articleAdd")
	@ResponseBody
	@PermessionType(permessionNum = 1000500)
	public ReturnT<Integer> articleAdd(ArticleInfo article) {
		return articleService.articleAdd(article);
	}
	
	@RequestMapping("/articleDel")
	@ResponseBody
	@PermessionType(permessionNum = 1000500)
	public ReturnT<Integer> articleDel(@RequestParam("articleIds[]") int[] articleIds) {
		return articleService.articleAdd(articleIds);
	}
	
	@RequestMapping("/articleEdit")
	@ResponseBody
	@PermessionType(permessionNum = 1000500)
	public ReturnT<Integer> articleEdit(ArticleInfo article) {
		return articleService.articleEdit(article);
	}

}
