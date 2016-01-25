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
	 * @param menuId
	 * @return
	 */
	public List<ArticleInfo> pageList(int offset, int pagesize, int menuId);

}
