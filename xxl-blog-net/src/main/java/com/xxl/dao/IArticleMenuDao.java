package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.ArticleMenu;


public interface IArticleMenuDao {
	
	/**
	 * 根据parentId查询
	 * @param parentId
	 * @return
	 */
	public List<ArticleMenu> getByParentId(int parentId);

	/**
	 * 查询
	 * @param menuId
	 * @return
	 */
	public ArticleMenu getByMenuId(int menuId);
}
