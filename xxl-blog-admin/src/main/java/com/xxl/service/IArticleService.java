package com.xxl.service;

import java.util.List;
import java.util.Map;

import com.xxl.core.model.main.ArticleInfo;
import com.xxl.core.model.main.ArticleMenu;
import com.xxl.core.result.ReturnT;


/**
 * 文章菜单-文章
 * @author xuxueli
 */
public interface IArticleService {

	/**
	 * 文章菜单,查询
	 * @return
	 */
	public List<ArticleMenu> articleMenuQuery();

	/**
	 * 文章菜单,新增
	 * @param menu
	 * @return
	 */
	public ReturnT<Integer> articleMenuAdd(ArticleMenu menu);

	/**
	 * 文章菜单,删除
	 * @param menuId
	 * @return
	 */
	public ReturnT<Integer> articleMenuDel(int menuId);

	/**
	 * 文章菜单,更新
	 * @param menu
	 * @return
	 */
	public ReturnT<Integer> articleMenuUpdate(ArticleMenu menu);

	/**
	 * 文章,查询
	 * @param page
	 * @param rows
	 * @param title
	 * @return
	 */
	public Map<String, Object> articleQuery(int page, int rows, String title, int menuId);

	/**
	 * 文章,新增
	 * @param article
	 * @return
	 */
	public ReturnT<Integer> articleAdd(ArticleInfo article);

	/**
	 * 文章,删除
	 * @param articleIds
	 * @return
	 */
	public ReturnT<Integer> articleAdd(int[] articleIds);

	/**
	 * 文章,编辑
	 * @param article
	 * @return
	 */
	public ReturnT<Integer> articleEdit(ArticleInfo article);

}
