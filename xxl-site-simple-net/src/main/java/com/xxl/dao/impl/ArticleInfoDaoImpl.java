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
	public List<ArticleInfo> pageList(int offset, int pagesize, int menuId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("menuId", menuId);
		return sqlSessionTemplate.selectList("ArticleInfoMapper.pageList", params);
	}


}
