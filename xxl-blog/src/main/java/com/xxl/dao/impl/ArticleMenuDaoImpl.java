package com.xxl.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.ArticleMenu;
import com.xxl.dao.IArticleMenuDao;

@Repository
public class ArticleMenuDaoImpl implements IArticleMenuDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 根据parentId查询
	 * @see com.xxl.dao.IArticleMenuDao#getByParentId(int)
	 */
	@Override
	public List<ArticleMenu> getByParentId(int parentId) {
		return sqlSessionTemplate.selectList("ArticleMenuMapper.getByParentId", parentId);
	}

	/*
	 * 查询
	 * @see com.xxl.dao.IArticleMenuDao#getByMenuId(int)
	 */
	@Override
	public ArticleMenu getByMenuId(int menuId) {
		return sqlSessionTemplate.selectOne("ArticleMenuMapper.getByMenuId", menuId);
	}

}
