package com.xxl.blog.dao.impl;

import com.xxl.blog.core.model.ArticleGroup;
import com.xxl.blog.dao.IArticleGroupDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleGroupDaoImpl implements IArticleGroupDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(ArticleGroup articleGroup) {
		return sqlSessionTemplate.insert("ArticleGroupMapper.add", articleGroup);
	}

	@Override
	public int update(ArticleGroup articleGroup) {
		return sqlSessionTemplate.update("ArticleGroupMapper.update", articleGroup);
	}

	@Override
	public int delete(int id) {
		return sqlSessionTemplate.delete("ArticleGroupMapper.delete", id);
	}

	@Override
	public List<ArticleGroup> getByParentId(int parentId) {
		return sqlSessionTemplate.selectList("ArticleGroupMapper.getByParentId", parentId);
	}

	@Override
	public ArticleGroup load(int id) {
		return sqlSessionTemplate.selectOne("ArticleGroupMapper.load", id);
	}

}
