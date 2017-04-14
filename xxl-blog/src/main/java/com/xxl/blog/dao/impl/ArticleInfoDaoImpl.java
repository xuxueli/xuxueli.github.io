package com.xxl.blog.dao.impl;

import com.xxl.blog.core.model.ArticleInfo;
import com.xxl.blog.dao.IArticleInfoDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli
 */
@Repository
public class ArticleInfoDaoImpl implements IArticleInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(ArticleInfo articleInfo) {
		return sqlSessionTemplate.insert("ArticleInfoMapper.add", articleInfo);
	}

	@Override
	public int update(ArticleInfo articleInfo) {
		return sqlSessionTemplate.update("ArticleInfoMapper.update", articleInfo);
	}

	@Override
	public int delete(int id) {
		return sqlSessionTemplate.delete("ArticleInfoMapper.delete", id);
	}

	@Override
	public List<ArticleInfo> pageList(int offset, int pagesize, int groupId, int status) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("groupId", groupId);
		params.put("status", status);
		return sqlSessionTemplate.selectList("ArticleInfoMapper.pageList", params);
	}

}
