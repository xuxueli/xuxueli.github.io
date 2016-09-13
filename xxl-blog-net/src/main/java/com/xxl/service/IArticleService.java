package com.xxl.service;

import java.util.List;

import com.xxl.core.model.main.ArticleInfo;
import com.xxl.core.model.main.ArticleMenu;


/**
 * 文章
 * @author xuxueli
 */
public interface IArticleService {

	/**
	 * 模块
	 * @return
	 */
	public List<ArticleMenu> articleModule();


	/**
	 * 文章.分页查询
	 * @param page
	 * @param rows
	 * @param title
	 * @return
	 */
	public List<ArticleInfo> articlePageList(int offset, int pagesize, int menuId);

}
