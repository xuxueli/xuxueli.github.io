package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.ArticleInfo;
import com.xxl.dao.IArticleInfoDao;

/**
 * 文章
 * @author xuxueli
 */
@Repository
public class ArticleInfoDaoImpl implements IArticleInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 分页查询
	 * @see com.xxl.dao.IArticleInfoDao#selectList(int, int, java.lang.String)
	 */
	@Override
	public List<ArticleInfo> selectList(int offset, int pagesize, String title, int menuId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("title", title);
		params.put("menuId", menuId);
		return sqlSessionTemplate.selectList("ArticleInfoMapper.selectList", params);
	}

	/*
	 * 分页查询count
	 * @see com.xxl.dao.IArticleInfoDao#selectListCount(int, int, java.lang.String)
	 */
	@Override
	public int selectListCount(int offset, int pagesize, String title, int menuId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("title", title);
		params.put("menuId", menuId);
		return sqlSessionTemplate.selectOne("ArticleInfoMapper.selectListCount", params);
	}

	/*
	 * 新增
	 * @see com.xxl.dao.IArticleInfoDao#insert(com.xxl.core.model.main.ArticleInfo)
	 */
	@Override
	public int insert(ArticleInfo article) {
		return sqlSessionTemplate.insert("ArticleInfoMapper.insert", article);
	}

	/*
	 * 删除
	 * @see com.xxl.dao.IArticleInfoDao#delete(int[])
	 */
	@Override
	public int delete(int[] articleIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("articleIds", articleIds);
		return sqlSessionTemplate.delete("ArticleInfoMapper.delete", params);
	}

	/*
	 * 编辑
	 * @see com.xxl.dao.IArticleInfoDao#update(com.xxl.core.model.main.ArticleInfo)
	 */
	@Override
	public int update(ArticleInfo article) {
		return sqlSessionTemplate.insert("ArticleInfoMapper.update", article);
	}

	/*
	 * 菜单ID查询count
	 * @see com.xxl.dao.IArticleInfoDao#selectCount(com.xxl.core.model.main.ArticleInfo)
	 */
	@Override
	public int selectByMenuIdCount(int menuId) {
		return sqlSessionTemplate.selectOne("ArticleInfoMapper.selectByMenuIdCount", menuId);
	}

}
