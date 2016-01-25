package com.xxl.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.constant.CommonDic.ArticleMenuDic;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.model.main.ArticleInfo;
import com.xxl.core.model.main.ArticleMenu;
import com.xxl.core.result.ReturnT;
import com.xxl.dao.IArticleInfoDao;
import com.xxl.dao.IArticleMenuDao;
import com.xxl.service.IArticleService;

/**
 * 文章菜单-文章
 * @author xuxueli
 */
@Service
public class ArticleServiceImpl implements IArticleService {
	private static transient Logger logger = LoggerFactory.getLogger(UserPermissionServiceImpl.class);

	@Autowired
	private IArticleMenuDao articleMenuDao;
	@Autowired
	private IArticleInfoDao articleInfoDao;
	
	/*
	 * 文章菜单,查询
	 * @see com.xxl.service.IArticleService#articleMenuQuery()
	 */
	@Override
	public List<ArticleMenu> articleMenuQuery() {
		List<ArticleMenu> list = articleMenuDao.getByParentId(0);
		return list;
	}
	
	/*
	 * 文章菜单,新增
	 * @see com.xxl.service.IArticleService#articleMenuAdd(com.xxl.core.model.main.ArticleMenu)
	 */
	@Override
	public ReturnT<Integer> articleMenuAdd(ArticleMenu menu) {
		if (menu == null) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,参数为空");
		}
		if (StringUtils.isBlank(menu.getName())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,名称为空");
		}
		
		// 父菜单校验 (所属类型)
		if (menu.getParentId() != ArticleMenuDic.MENU_MODULE_ID) {
			ArticleMenu parent = articleMenuDao.getEffectParent(menu.getParentId(), ArticleMenuDic.MENU_MODULE_ID);
			if (parent == null) {
				return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,菜单暂定为二级[模块-组],组不可作为父菜单");
			}
		}
		
		int count = articleMenuDao.insert(menu);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 文章菜单,删除
	 * @see com.xxl.service.IArticleService#articleMenuDel(int)
	 */
	@Override
	public ReturnT<Integer> articleMenuDel(int menuId) {
		
		// 子菜单依赖,不允许删除
		List<ArticleMenu> children = articleMenuDao.getByParentId(menuId);
		if (!CollectionUtils.isEmpty(children)) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "删除失败,存在子菜单依赖");
		}
		
		// 文章依赖,不允许删除
		int menuRelNum = articleInfoDao.selectByMenuIdCount(menuId);
		if (menuRelNum > 0) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "删除失败,存在文章依赖");
		}
		
		int count = articleMenuDao.delete(menuId);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 文章菜单,更新
	 * @see com.xxl.service.IArticleService#articleMenuUpdate(com.xxl.core.model.main.ArticleMenu)
	 */
	@Override
	public ReturnT<Integer> articleMenuUpdate(ArticleMenu menu) {
		if (menu == null) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "更新失败,参数为空");
		}
		if (StringUtils.isBlank(menu.getName())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "更新失败,名称为空");
		}
		
		// 父菜单校验 (所属类型)
		if (menu.getParentId() != ArticleMenuDic.MENU_MODULE_ID) {
			ArticleMenu parent = articleMenuDao.getEffectParent(menu.getParentId(), ArticleMenuDic.MENU_MODULE_ID);
			if (parent == null) {
				return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "更新失败,父菜单ID不合法");
			}
		}
		
		int count = articleMenuDao.update(menu);
		return new ReturnT<Integer>(count);
	}

	//-----------------------文章相关-----------------------
	
	/*
	 * 文章,查询
	 * @see com.xxl.service.IArticleService#articleQuery(int, int, java.lang.String)
	 */
	@Override
	public Map<String, Object> articleQuery(int page, int rows, String title, int menuId) {
		try {	title = URLDecoder.decode(title, "utf-8");
		} catch (UnsupportedEncodingException e) {	logger.info("{}", e);	}
		
		int offset = page<1 ? 0 : (page-1)*rows;
		int pagesize = rows;
		
		List<ArticleInfo> rowsData = articleInfoDao.selectList(offset, pagesize, title, menuId);
		int totalNumber = articleInfoDao.selectListCount(offset, pagesize, title, menuId);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", rowsData);  
		resultMap.put("total", totalNumber);
		return resultMap;
	}

	/*
	 * 文章,新增
	 * @see com.xxl.service.IArticleService#articleAdd(com.xxl.core.model.main.ArticleInfo)
	 */
	@Override
	public ReturnT<Integer> articleAdd(ArticleInfo article) {
		if (StringUtils.isBlank(article.getTitle()) || article.getTitle().length() < 5 || article.getTitle().length() > 50) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,标题为空");
		}
		if (StringUtils.isBlank(article.getContent())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,内容为空");
		}
		
		ArticleMenu menu = articleMenuDao.getByMenuId(article.getMenuId());
		if (menu == null) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,内容所属菜单不存在");
		}
		
		article.setUserId(0);	// 后台文章,userId默认为0
		int count = articleInfoDao.insert(article);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 文章,删除
	 * @see com.xxl.service.IArticleService#articleAdd(int[])
	 */
	@Override
	public ReturnT<Integer> articleAdd(int[] articleIds) {
		if (ArrayUtils.isEmpty(articleIds)) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "删除失败,未选中待删除文章");
		}
		
		int count = articleInfoDao.delete(articleIds);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 文章,编辑
	 * @see com.xxl.service.IArticleService#articleEdit(com.xxl.core.model.main.ArticleInfo)
	 */
	@Override
	public ReturnT<Integer> articleEdit(ArticleInfo article) {
		if (StringUtils.isBlank(article.getTitle()) || article.getTitle().length() < 5 || article.getTitle().length() > 50) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,标题为空");
		}
		if (StringUtils.isBlank(article.getContent())) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,内容为空");
		}
		
		ArticleMenu menu = articleMenuDao.getByMenuId(article.getMenuId());
		if (menu == null) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,内容所属菜单不存在");
		}
		
		article.setUserId(0);	// 后台文章,userId默认为0
		int count = articleInfoDao.update(article);
		return new ReturnT<Integer>(count);
	}
	
}
