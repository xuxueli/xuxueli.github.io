package com.xxl.core.util.clawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xxl.core.util.clawler.qiubai.QbCrawlerThread;
import com.xxl.core.util.clawler.qiubai.QbLinkQueue;

/**
 * 爬虫线程池,工具类
 * @author xuxueli
 */
public class CrawlerThreadPollUtil {
	
	/**
	 * 爬虫线程池
	 */
	private static ExecutorService execQiubai = Executors.newCachedThreadPool();
	
	/**
	 * 糗百爬虫.多线程启动器
	 * @param flag
	 */
	private static String baseLink = "http://www.qiushibaike.com"; 
	public static void execQiubaiStart(boolean flag){
		if (flag) {
			QbLinkQueue.init(baseLink, baseLink);
			QbCrawlerThread.isStop = false;
			for (int i = 0; i < 5; i++) {
				execQiubai.execute(new Thread(new QbCrawlerThread()));
			}
		} else {
			QbCrawlerThread.isStop = true;
		}
		
	}
	
	public static void main(String[] args) {
		execQiubaiStart(true);
	}
}
