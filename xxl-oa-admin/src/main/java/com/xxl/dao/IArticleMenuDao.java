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
	 * 查询合法的父菜单
	 * @param parentId
	 * @param moduleId:菜单模块ID,参考用
	 * @return
	 */
	public ArticleMenu getEffectParent(int menuId, int moduleId);

	/**
	 * 新增
	 * @param menu
	 * @return
	 */
	public int insert(ArticleMenu menu);

	/**
	 * 删除
	 * @param menuId
	 * @return
	 */
	public int delete(int menuId);

	/**
	 * 更新
	 * @param menu
	 * @return
	 */
	public int update(ArticleMenu menu);

	/**
	 * 查询
	 * @param menuId
	 * @return
	 */
	public ArticleMenu getByMenuId(int menuId);
}
