package com.xxl.core.util.clawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Jsoup工具类 (获取并解析HTML)
 * @author xuxueli
 */
public class JsoupUtlls {
	
	/**
	 * 是否合法url
	 */
	public static boolean isUrl(String url) {
		if (url!=null && url.trim().length() > 0 && url.startsWith("http")) {
			return true;
		}
		return false;
	}

	/**
	 * 按照规则,加载解析html
	 * @param url		：加载URL
	 * @param paramMap	：请求参数
	 * @param cookieMap	：请求cookie
	 * @param ifPost	：是否使用post请求
	 * @param tagMap	：解析规则[0-选择器方式、1-id方式、2-class方式、3-tag]
	 * @return
	 */
	public static Elements loadParse(String url, Map<String, String> paramMap, Map<String, String> cookieMap, 
			boolean ifPost, Map<Integer, Set<String>> tagMap) {
		if (!isUrl(url)) {
			return null;
		}
		try {
			// 请求设置
			Connection conn = Jsoup.connect(url);
			conn.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
			if (paramMap != null && !paramMap.isEmpty()) {
				conn.data(paramMap);	
			}
			if (cookieMap != null && !cookieMap.isEmpty()) {
				conn.cookies(cookieMap);
			}
			conn.timeout(5000);

			// 发出请求
			Document html = null;
			if (ifPost) {
				html = conn.post();
			} else {
				html = conn.get();
			}
			
			// 过滤元素
			Elements resultAll = new Elements();
			if (tagMap != null && !tagMap.isEmpty()) {
				for (Entry<Integer, Set<String>> tag : tagMap.entrySet()) {
					int tagType = tag.getKey();
					Set<String> tagNameList = tag.getValue();
					if (tagNameList != null && tagNameList.size() > 0) {
						for (String tagName : tagNameList) {
							if (tagType == 0) {
								Elements resultSelect = html.select(tagName);	// 选择器方式
								resultAll.addAll(resultSelect);
							} else if (tagType == 1) {
								Element resultId = html.getElementById(tagName);	// 元素ID方式
								resultAll.add(resultId);
							} else if (tagType == 2) {
								Elements resultClass = html.getElementsByClass(tagName);	// ClassName方式
								resultAll.addAll(resultClass);
							} else if (tagType == 3) {
								Elements resultTag = html.getElementsByTag(tagName);	// html标签方式
								resultAll.addAll(resultTag);
							}
						}
					}
					
				}
				
			} else {
				resultAll = html.getElementsByTag("body");
			}
			return resultAll;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 获取.a标签href
	 * 
	 * @param baseLink	: 爬行跟URL,页面"/"会replace为改URL
	 * @param indexLink : 待爬行URL
	 * @return
	 */
	public static Set<String> findLinks(String baseLink, String indexLink) {
		// 组装规则
		Map<Integer, Set<String>> tagMap = new HashMap<Integer, Set<String>>();
		Set<String> tagNameList = new HashSet<String>();
		tagNameList.add("a");
		tagMap.put(3, tagNameList);
		
		// 获取html
		Elements resultAll = loadParse(indexLink, null, null, false, tagMap);
		
		// 解析html
		Set<String> links = new HashSet<String>();
		if (resultAll!=null && resultAll.size() > 0) {
			for (Element item : resultAll) {
				String href = item.attr("href");
				if (href!=null && href.startsWith("/")) {
					href = baseLink + href;
				}
				if (isUrl(href)) {
					links.add(href);
				}
			}
		}
		return links;
	}

	public static void main(String[] args) {
		
		Set<String> result = findLinks("http://www.qiushibaike.com", "http://www.qiushibaike.com");
		if (result!=null && result.size() > 0) {
			for (String str : result) {
				System.out.println(str);
			}
		} else {
			System.out.println("findLinks result is null!");
		}
		
	}
}
