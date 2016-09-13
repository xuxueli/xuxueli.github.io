package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.ArticleInfo;

/**
 * 文章
 * @author xuxueli
 */
public interface IArticleInfoDao {

	/**
	 * 分页查询
	 * @param offset
	 * @param pagesize
	 * @param title
	 * @return
	 */
	public List<ArticleInfo> selectList(int offset, int pagesize, String title, int menuId);

	/**
	 * 分页查询count
	 * @param offset
	 * @param pagesize
	 * @param title
	 * @return
	 */
	public int selectListCount(int offset, int pagesize, String title, int menuId);

	/**
	 * 新增
	 * @param article
	 * @return
	 */
	public int insert(ArticleInfo article);

	/**
	 * 删除
	 * @param articleIds
	 * @return
	 */
	public int delete(int[] articleIds);

	/**
	 * 编辑
	 * @param article
	 * @return
	 */
	public int update(ArticleInfo article);

	/**
	 * 菜单ID查询count
	 * @param menuId
	 * @return
	 */
	public int selectByMenuIdCount(int menuId);

}
