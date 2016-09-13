package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * 查询合法的父菜单
	 * @see com.xxl.dao.IArticleMenuDao#checkParentId(int)
	 */
	@Override
	public ArticleMenu getEffectParent(int menuId, int moduleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuId", menuId);
		params.put("moduleId", moduleId);
		return sqlSessionTemplate.selectOne("ArticleMenuMapper.getEffectParent", params);
	}

	/*
	 * 新增
	 * @see com.xxl.dao.IArticleMenuDao#insert(com.xxl.core.model.main.ArticleMenu)
	 */
	@Override
	public int insert(ArticleMenu menu) {
		return sqlSessionTemplate.insert("ArticleMenuMapper.insert", menu);
	}

	/*
	 * 删除
	 * @see com.xxl.dao.IArticleMenuDao#delete(int)
	 */
	@Override
	public int delete(int menuId) {
		return sqlSessionTemplate.delete("ArticleMenuMapper.delete", menuId);
	}

	/*
	 * 更新
	 * @see com.xxl.dao.IArticleMenuDao#update(com.xxl.core.model.main.ArticleMenu)
	 */
	@Override
	public int update(ArticleMenu menu) {
		return sqlSessionTemplate.insert("ArticleMenuMapper.update", menu);
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
