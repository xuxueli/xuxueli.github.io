package com.xxl.blog.dao;

import com.xxl.blog.core.model.ArticleGroup;

import java.util.List;

public interface IArticleGroupDao {

	public int add(ArticleGroup articleGroup);
	public int update(ArticleGroup articleGroup);
	public int delete(int id);

	public List<ArticleGroup> getByParentId(int parentId);
	public ArticleGroup load(int id);
}
