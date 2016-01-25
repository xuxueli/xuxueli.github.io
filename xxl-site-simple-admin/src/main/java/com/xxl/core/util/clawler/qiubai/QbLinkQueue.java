package com.xxl.core.util.clawler.qiubai;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 糗百爬虫.URL队列
 * @author xuxueli
 */
public class QbLinkQueue {
	// 爬行队列
	private static LinkedBlockingQueue<String> unVisitedUrl;
	private static Set<String> visitedUrl;
	// 爬行跟地址 (防止跨站爬)
	private static String baseLink;
	
	public static void init(String baseLinks, String enterLink){
		unVisitedUrl = new LinkedBlockingQueue<String>();	// 未爬过的链接
		visitedUrl = new HashSet<String>();	// 已经爬过的链接
		baseLink = baseLinks; // 爬行跟地址 (防止跨站爬)
		add(enterLink); // 爬行入口
	}
	public static String getBaseLink(){
		return baseLink;
	}
	
	// 已经爬过长度
	public static int visitedUrlSize(){
		return visitedUrl.size();
	}

	// URL入队列
	public static void add(String link) {
		if (link != null 
				&& link.startsWith(baseLink)
				&& !unVisitedUrl.contains(link)
				&& !visitedUrl.contains(link)) {
			unVisitedUrl.add(link);
		}
	}
	// URL出队列
	public static String poll() {
		String link = unVisitedUrl.poll();
		if (link != null) {
			visitedUrl.add(link);
		}
		return link; 
	}
	
}