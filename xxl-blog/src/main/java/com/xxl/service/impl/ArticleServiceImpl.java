package com.xxl.service.impl;

import com.xxl.core.model.main.ArticleInfo;
import com.xxl.core.model.main.ArticleMenu;
import com.xxl.dao.IArticleInfoDao;
import com.xxl.dao.IArticleMenuDao;
import com.xxl.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章菜单-文章
 * @author xuxueli
 */
@Service
public class ArticleServiceImpl implements IArticleService {

	public static final int ARTICLE_MENU = 0;	// 模块类型

	@Autowired
	private IArticleMenuDao articleMenuDao;
	@Autowired
	private IArticleInfoDao articleInfoDao;
	
	/*
	 * 模块
	 * @see com.xxl.service.IArticleService#articleMenuQuery()
	 */
	@Override
	public List<ArticleMenu> articleModule() {
		List<ArticleMenu> list = articleMenuDao.getByParentId(ARTICLE_MENU);
		return list;
	}

	/*
	 * 文章.分页查询
	 * @see com.xxl.service.IArticleService#articleQuery(int, int, java.lang.String)
	 */
	@Override
	public List<ArticleInfo> articlePageList(int offset, int pagesize, int menuId) {
		return articleInfoDao.pageList(offset, pagesize, menuId);
	}
	
}
