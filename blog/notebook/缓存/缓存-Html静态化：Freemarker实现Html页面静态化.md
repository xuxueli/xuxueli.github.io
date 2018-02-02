使用Freemarker实现HTML静态化；

### 1、引入maven依赖
```
<!-- freemarker -->
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.20</version>
</dependency>
```

### 2、Freemarker实现HTML静态化工具类（HtmlTemplateUtil.java）（可参考项目：xxl-blog）

功能简介：

    1、根据Ftl生成Html字符串；
    2、根据Ftl生成Html文件（网站静态化）；
    3、Ftl支持使用静态类方法；

- 1、配置FreeMarkerConfigurer（推荐托管在Spring容器，和复用SpringMVC同一套配置），见applicationcontext-base.xml文件配置
```
<bean id="freemarkerConfig" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
	<property name="freemarkerSettings">
		<bean class="org.springframework.beans.factory.config.PropertiesFactoryBean">
			<property name="location" value="classpath:freemarker.properties" />
		</bean>
	</property>
	<property name="templateLoaderPath" value="/WEB-INF/template/" />
	<property name="freemarkerVariables">
		<bean
			class="org.springframework.beans.factory.config.PropertiesFactoryBean">
			<property name="location" value="classpath:freemarker.variables.properties" />
		</bean>
	</property>
</bean>
```
- 2、引入HtmlTemplateUtil.java文件
```
package com.xxl.util.core.util;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Map;

/**
 * HTML模板.Util
 *
 * 	功能简介：
 * 		1、根据Ftl生成Html字符串；
 * 		2、根据Ftl生成Html文件（网站静态化）；
 * 		3、Ftl支持使用静态类方法；
 *
 * @author xuxueli
 */
public class HtmlTemplateUtil  {
	
	private static FreeMarkerConfigurer freemarkerConfig;
	
	public static FreeMarkerConfigurer loadConfig(){
		if (freemarkerConfig == null) {
			freemarkerConfig = (FreeMarkerConfigurer) SpringContentAwareUtil.getBeanByName("freemarkerConfig");
		}
		return freemarkerConfig;
	}
	
	/**
	 * generate static model
	 * @param packageName
	 * @return
	 */
	public static TemplateHashModel generateStaticModel(String packageName) {
		try {
			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();  
			TemplateHashModel staticModels = wrapper.getStaticModels();
			TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
			return fileStatics;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成HTML字符串
	 * 
	 * @param content			: 页面传参
	 * @param templateName		: 模板名称路径,相对于模板目录
	 */
	public static String generateString(Map<?, ?> content, String templateName) {
		String htmlText = "";
		try {
			// 通过指定模板名获取FreeMarker模板实例
			Template tpl = loadConfig().getConfiguration().getTemplate(templateName);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlText;
	}

	/**
	 * 生成HTML文件
	 * 
	 * @param content			: 页面传参
	 * @param templatePathName	: 模板名称路径,相对于模板目录
	 * @param filePathName		: 文件名称路径,相对于项目跟目录
	 * DEMO:HtmlTemplateUtil.generate(freemarkerConfig, new HashMap<String, Object>(), "net/index.ftl", "/index.html");
	 */
	public static void generateFile(Map<?, ?> content, String templatePathName, String filePathName) {
		Writer out = null;
		try {
			// mkdirs
			File htmlFile = new File(WebPathUtil.webPath() + filePathName);
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            // process
            Template template = loadConfig().getConfiguration().getTemplate(templatePathName);
			out = new OutputStreamWriter(new FileOutputStream(WebPathUtil.webPath() + filePathName), "UTF-8");
			template.process(content, out);
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		String temp = generateString(null, "hello.ftl");
		System.out.println(temp);
		
		/*// generate String
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("WebPathUtil", HtmlTemplateUtil.generateStaticModel(WebPathUtil.class.getName()));

		String result = HtmlTemplateUtil.generateString(params, "freemarker-test.ftl");

		// generate Html File
		HtmlTemplateUtil.generateFile(params, "freemarker-test.ftl", "freemarker-test.html");*/
	}
	
}

```

要点：
    
    1、“freemarkerConfig”中的配置“templateLoaderPath”，为include新ftl文件时的根目录“/”位置（省去了繁琐的相对路径配置，相当方便）；
    2、“freemarkerConfig”必须放在spring中，springMVC和静态化UTIL公用；因为web.xml加载顺序为：web.xml加载顺序，listener（spring） -> filter -> servlet（springMVC）；因此，如果放在springMVC中，Service中注入不了该config，静态化UTIL注入时还未初始化bean会报错；
    

### 3、freemarker静态化分页（可参考项目：xxl-blog）
- 1、分页代码：HtmlGenerateServiceImpl.java

```
// 调用处：一面墙,html分页
List<WallInfo> wallInfoList = wallInfoDao.getPageList(0, 10000);
generateHtmlPagination(wallInfoList, null, 20, "net/wall/index.ftl", "wall/", "index");


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
		HtmlTemplateUtil.generate(params, templatePathName, filePath + index + ".html");
	}
}
```

2、分页模板：index.ftl
```
<#list pageList as item>
    <div class="well text-justify">${item.content}</div>
</#list>

<!--html分页-->
<#import "/net/common/common.html.pagination.ftl" as pagination>
<@pagination.htmlPaging pageNumAll=pageNumAll pageNum=pageNum html_base_url=base_url+filePath index=index />
```

3、分页模板，公共分页标签：common.html.pagination.ftl
```
<#-- 
html分页模板,文件名称
------------------
pageNum			:	当前页数、(1-max)
html_base_url	:	html文件路径、
-->
<#macro htmlPagingName pageNum html_base_url index  >
	<#if pageNum == 1 >${html_base_url}${index}.html
	<#else>${html_base_url}${index}_${pageNum}.html</#if>
</#macro>
<#-- 
html分页模板
------------------
pageNumAll		:	总页数、
pageNum			:	当前页数、
html_base_url	:	html文件路径、
-->
<#macro htmlPaging pageNumAll pageNum html_base_url index >
	<ul class="pagination">
		<!--pre-->
		<#if pageNum-1 gte 1><li><a href="<@htmlPagingName pageNum=pageNum-1 html_base_url=html_base_url index=index />" >&laquo;</a></li>
		<#else><li class="disabled"><a>&laquo;</a></li></#if>
		<!--every pre-->
		<#if pageNum-1 gte 5>
			<li><a href="<@htmlPagingName pageNum=1 html_base_url=html_base_url index=index />" >1</a></li>
			<li><a href="<@htmlPagingName pageNum=2 html_base_url=html_base_url index=index />" >2</a></li>
			<li><a>...</a></li>
			<li><a href="<@htmlPagingName pageNum=pageNum-2 html_base_url=html_base_url index=index />" >${pageNum-2}</a></li>
			<li><a href="<@htmlPagingName pageNum=pageNum-1 html_base_url=html_base_url index=index />" >${pageNum-1}</a></li>
		<#elseif 1 lte (pageNum-1) >
			<#list 1..(pageNum-1) as item>
				<li><a href="<@htmlPagingName pageNum=item html_base_url=html_base_url index=index />" >${item}</a></li>
			</#list>
		</#if>
		<!--every now-->
		<li class="active" ><a href="<@htmlPagingName pageNum=pageNum html_base_url=html_base_url index=index />" >${pageNum}</a></li>
		<!--every next-->
		<#if pageNumAll-pageNum gte 5>
			<li><a href="<@htmlPagingName pageNum=pageNum+1 html_base_url=html_base_url index=index />" >${pageNum+1}</a></li>
			<li><a href="<@htmlPagingName pageNum=pageNum+2 html_base_url=html_base_url index=index />" >${pageNum+2}</a></li>
			<li><a>...</a></li>
			<li><a href="<@htmlPagingName pageNum=pageNumAll-1 html_base_url=html_base_url index=index />" >${pageNumAll-1}</a></li>
			<li><a href="<@htmlPagingName pageNum=pageNumAll html_base_url=html_base_url index=index />" >${pageNumAll}</a></li>
		<#elseif (pageNum+1) lte pageNumAll >
			<#list (pageNum+1)..pageNumAll as item>
				<li><a href="<@htmlPagingName pageNum=item html_base_url=html_base_url index=index />" >${item}</a></li>
			</#list>
		</#if>
	  	<!--next-->
	  	<#if pageNum+1 lte pageNumAll><li><a href="<@htmlPagingName pageNum=pageNum+1 html_base_url=html_base_url index=index  />" >&raquo;</a></li>
	  	<#else><li class="disabled"><a>&raquo;</a></li></#if>
	</ul>
</#macro>
```

### freemarker动态分页（底部标签）

- 1、动态分页对象：Pager.java
```
package com.xxl.core.model.vo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Pager<T> implements Serializable{
	
	private int page;		// 入参-第N页
	private int pagesize;	// 入参-每页长度
	@SuppressWarnings("unused")
	private int offset; 	// 起始行号【return this.page-1<0 ? 0 : (this.page-1)*this.rows;】
	private List<T> data;	// 查询-分页数据
	private int total;		// 查询-总记录数
	@SuppressWarnings("unused")
	private int totalPage;	// 查询-总页数【return (total + pagesize - 1)/pagesize;】
	
	public Pager(int page, int pagesize){
		this.page = page;
		this.pagesize = pagesize;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getOffset() {
		return this.page-1<0 ? 0 : (this.page-1)*this.pagesize;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotalPage() {
		return (total + pagesize - 1)/pagesize;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
```
- 2、后端分页代码
```
// Controller层代码
@RequestMapping("/orderList")
@PermessionType
public String orderList(HttpServletRequest request, Model model,
		@RequestParam(required=false, defaultValue="1")int page, 
		@RequestParam(required=false, defaultValue="20")int pagesize){
	
	Pager<OrderInfo> pager = new Pager<OrderInfo>(page, pagesize);
	orderService.selectPage(pager, identity);
	
	model.addAttribute("pager", pager);
	return "net/order/orderList";
}

// Service层代码
@Override
public void selectPage(Pager<OrderInfo> pager, LoginIdentity identity) {
	List<OrderInfo> data = orderInfoDao.selectPage(pager.getOffset(), pager.getPagesize(), identity.getUserId());
	int total = orderInfoDao.selectPageCount(pager.getOffset(), pager.getPagesize(), identity.getUserId());
	pager.setData(data);
	pager.setTotal(total);
}
```

3、前端模板
```
<!-- 分页数据 -->
<#if pager?exists && pager.data?exists >
<#list pager.data as item>
<tr>
	<td>${item.orderId}</td>
	<td>${item.userId}</td>
	<td>${item.prodId}</td>
	<td>${item.orderTime?string('yyyy-MM-dd')}</td>
</tr>
</#list>
</#if>
<!-- 分页标签 -->
<@netCommon.pager pager=pager baseUrl=base_url+'order/orderList.do' />
```

- 4、分页标签，公共模板：net.common.ftl
```
<#macro pager pager baseUrl>
	<!--pre-->
	<#if pager.page gt 1><a href="${baseUrl}?page=${pager.page - 1}" >上页</a>
	<#else>上页</#if>
	
	<!--every pre-->
	<#if pager.page-1 gte 5>
		<a href="${baseUrl}?page=1" >1</a>
		<a href="${baseUrl}?page=2" >2</a>
		<a>...</a>
		<a href="${baseUrl}?page=${pager.page-2}" >${pager.page-2}</a>
		<a href="${baseUrl}?page=${pager.page-1}" >${pager.page-1}</a>
	<#elseif 1 lte (pager.page-1) >
		<#list 1..(pager.page-1) as item>
			<a href="${baseUrl}?page=${item}" >${item}</a>
		</#list>
	</#if>
	
	<!--every now-->
	${pager.page}
	
	<!--every next-->
	<#if pager.totalPage-pager.page gte 5>
		<a href="${baseUrl}?page=${pager.page}" >${pager.page+1}</a>
		<a href="${baseUrl}?page=${pager.page}" >${pager.page+2}</a>
		<a>...</a>
		<a href="${baseUrl}?page=${pager.page}" >${pager.page-1}</a>
		<a href="${baseUrl}?page=${pager.page}" >${pager.page}</a>
	<#elseif (pager.page+1) lte pager.totalPage >
		<#list (pager.page+1)..pager.totalPage as item>
			<a href="${baseUrl}?page=${pager.page}" >${item}</a>
		</#list>
	</#if>
	
  	<!--next-->
  	<#if pager.page lt pager.totalPage><a href="${baseUrl}?page=${pager.page+1}" >下页</a>
  	<#else>下页</#if>
</#macro>
```

