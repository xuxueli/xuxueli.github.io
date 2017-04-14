package com.xxl.blog.dao;

import com.xxl.blog.core.model.ArticleInfo;

import java.util.List;

/**
 * @author xuxueli
 */
public interface IArticleInfoDao {

	public int add(ArticleInfo articleInfo);
	public int update(ArticleInfo articleInfo);
	public int delete(int id);

	public List<ArticleInfo> pageList(int offset, int pagesize, List<Integer> groupIds, int status);
	public int pageListCount(int offset, int pagesize, List<Integer> groupIds, int status);

}
