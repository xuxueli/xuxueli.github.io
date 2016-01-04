package com.xxl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.model.main.ArticleInfo;
import com.xxl.core.model.main.ArticleMenu;
import com.xxl.core.model.main.WallInfo;
import com.xxl.core.util.HtmlTemplateUtil;
import com.xxl.dao.IWallInfoDao;
import com.xxl.service.IArticleService;
import com.xxl.service.IHtmlGenerateService;

/**
 * html generate
 * @author xuxueli
 */
@Service
public class HtmlGenerateServiceImpl implements IHtmlGenerateService {
	
	@Autowired
    private IWallInfoDao wallInfoDao;
	@Autowired
    private IArticleService articleService;
	
	/**
	 * 全站静态化
	 */
	public void generateNetHtml() {
		Map<String, Object> params = new HashMap<String, Object>();

		// 首页
		HtmlTemplateUtil.generate(new HashMap<String, Object>(), "net/index.ftl", "/index.html");
				
		// 宅吧 (文章)
		List<ArticleMenu> articleModule = articleService.articleModule();
		params.put("articleModule", articleModule);
		HtmlTemplateUtil.generate(params, "net/zhai/index.ftl", "/zhai/index.html");	// 首页:模块-组,结构
		
		if (CollectionUtils.isNotEmpty(articleModule)) {
			for (ArticleMenu module : articleModule) {
				if (CollectionUtils.isNotEmpty(module.getChildren())) {
					for (ArticleMenu group : module.getChildren()) {
						
						// 组页面,html分页
						List<ArticleInfo> groupList = articleService.articlePageList(0, 1000, group.getMenuId());
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("module", module);
						paramMap.put("group", group);
						
						generateHtmlPagination(groupList, paramMap, 5, "net/zhai/article.group.ftl", "zhai/group/", String.valueOf(group.getMenuId()));	// 组html分页
						
						// 文章详情页
						if (CollectionUtils.isNotEmpty(groupList)) {
							for (ArticleInfo article : groupList) {
								params = new HashMap<String, Object>();
								params.put("article", article);
								HtmlTemplateUtil.generate(params, "net/zhai/article.info.ftl", "/zhai/article/" + article.getArticleId() + ".html");	// 文章.detail
							}
						}
						
					}
				}
			}
		}

		// 一面墙,html分页
		List<WallInfo> wallInfoList = wallInfoDao.getPageList(0, 10000);
		generateHtmlPagination(wallInfoList, null, 20, "net/wall/index.ftl", "wall/", "index");
		
		// 安全中心
		HtmlTemplateUtil.generate(params, "net/safe/email.activate.ftl", "/safe/emailActivate.html");	// 邮箱激活
		HtmlTemplateUtil.generate(params, "net/safe/email.find.pwd.ftl", "/safe/emailFindPwd.html");	// 找回密码
		
	}
	
	/**
	 * html分页工具
	 * @param allList			:	html分页列表
	 * @param pagesize			:	每页显示记录数量
	 * @param templatePathName	:	模板页面,完整地址 (相相对于freeamrker配置根目录)
	 * @param filePath			:	html文件,路径目录	--/-/
	 * @param index				:	html文件,默认前缀	index
	 */
	private void generateHtmlPagination(List<?> allList, Map<String, Object> paramMap,int pagesize, String templatePathName , String filePath , String index){
		int allCount = allList!=null?allList.size():0;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageNumAll", 1);
		params.put("pageNum", 1);
		params.put("filePath", filePath);
		params.put("index", index);
		
		if (MapUtils.isNotEmpty(paramMap)) {
			params.putAll(paramMap);
		}
		
		if (allCount > 0) {
			int pageNumAll = allCount%pagesize==0 ? allCount/pagesize : allCount/pagesize + 1;
			for (int pageNum = 1; pageNum <= pageNumAll; pageNum++) {
				params.put("pageNumAll", pageNumAll);
				params.put("pageNum", pageNum);
				
				int from = (pageNum-1)*pagesize;
				int to = (from + pagesize) <= allCount ? (from + pagesize) : allCount;
				params.put("pageList", allList.subList(from, to));
				
				String fileName = (pageNum==1) ? index : (index + "_" + pageNum);
				HtmlTemplateUtil.generate(params, templatePathName, filePath + fileName + ".html");
			}
		} else {
			params.put("pageNumAll", 0);
			HtmlTemplateUtil.generate(params, templatePathName, filePath + index + ".html");
		}
	}
	
}
