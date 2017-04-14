package com.xxl.blog.service.impl;

import com.xxl.blog.controller.ArticleController;
import com.xxl.blog.core.model.ArticleGroup;
import com.xxl.blog.core.model.ArticleInfo;
import com.xxl.blog.core.util.HtmlTemplateUtil;
import com.xxl.blog.dao.IArticleGroupDao;
import com.xxl.blog.dao.IArticleInfoDao;
import com.xxl.blog.service.IHtmlGenerateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * html generate
 * @author xuxueli
 */
@Service("htmlGenerateService")
public class HtmlGenerateServiceImpl implements IHtmlGenerateService {

	@Autowired
	private IArticleGroupDao articleGroupDao;
	@Autowired
	private IArticleInfoDao articleInfoDao;
	@Resource
	private FreeMarkerConfigurer freemarkerConfig;

	/**
	 * 全站静态化
	 */
	public void generateNetHtml() {

		// 文章结构：模块-组-文章
		List<ArticleGroup> articleModule = articleGroupDao.getByParentId(ArticleController.ARTICLE_GROUP_TOP);
		
		// HTML：首页
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("articleModule", articleModule);
		HtmlTemplateUtil.generate(freemarkerConfig, params, "index.ftl", "/index.html");
		
		//HTML： 文章列表
		if (CollectionUtils.isNotEmpty(articleModule)) {
			for (ArticleGroup module : articleModule) {
				if (CollectionUtils.isNotEmpty(module.getChildren())) {
					for (ArticleGroup group : module.getChildren()) {
						
						// 组页面（	需分页）
						List<ArticleInfo> groupList = articleInfoDao.pageList(0, 1000, Arrays.asList(group.getId()), -1);
						params.clear();
						params.put("articleModule", articleModule);
						params.put("module", module);
						params.put("group", group);
						HtmlGenerateServiceImpl.generateHtmlPagination(freemarkerConfig, groupList, params, 20, "article/article.group.ftl", "article/group/", String.valueOf(group.getId()));	// 组html分页
						
						// 文章详情页
						if (CollectionUtils.isNotEmpty(groupList)) {
							for (ArticleInfo article : groupList) {
								params.clear();
								params.put("articleModule", articleModule);
								params.put("module", module);
								params.put("group", group);
								params.put("article", article);
								HtmlTemplateUtil.generate(freemarkerConfig, params, "article/article.info.ftl", "/article/article/" + article.getId() + ".html");	// 文章.detail
							}
						}
						
					}
				}
			}
		}

		// HTNL：安全中心
		params.clear();
		params.put("articleModule", articleModule);
		HtmlTemplateUtil.generate(freemarkerConfig, params, "safe/email.activate.ftl", "/safe/emailActivate.html");	// 邮箱激活
		HtmlTemplateUtil.generate(freemarkerConfig, params, "safe/email.find.pwd.ftl", "/safe/emailFindPwd.html");	// 找回密码
		
	}
	
	/**
	 * html分页工具
	 * @param allList			:	html分页列表
	 * @param pagesize			:	每页显示记录数量
	 * @param templatePathName	:	模板页面,完整地址 (相相对于freeamrker配置根目录)
	 * @param filePath			:	html文件,路径目录	--/-/
	 * @param index				:	html文件,默认前缀	index
	 */
	private static void generateHtmlPagination(FreeMarkerConfigurer freemarkerConfig, List<?> allList, Map<String, Object> paramMap,int pagesize, String templatePathName , String filePath , String index){
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
				HtmlTemplateUtil.generate(freemarkerConfig, params, templatePathName, filePath + fileName + ".html");
			}
		} else {
			params.put("pageNumAll", 0);
			HtmlTemplateUtil.generate(freemarkerConfig, params, templatePathName, filePath + index + ".html");
		}
	}
	
}
