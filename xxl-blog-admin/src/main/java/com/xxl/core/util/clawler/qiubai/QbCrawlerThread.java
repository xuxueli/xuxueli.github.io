package com.xxl.core.util.clawler.qiubai;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.core.constant.CommonDic;
import com.xxl.core.model.main.WallInfo;
import com.xxl.core.queue.WallInfoQueue;
import com.xxl.core.util.RegexUtil;
import com.xxl.core.util.URLEncoderUtil;
import com.xxl.core.util.clawler.JsoupUtlls;

/**
 * 糗百爬虫.线程
 * @author xuxueli
 */
public class QbCrawlerThread implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(QbCrawlerThread.class);
	
	// 是否关闭,volatile确保本条指令不会因编译器的优化而省略，且要求每次直接读值;优化器在用到这个变量时必须每次都小心地重新读取这个变量的值，而不是使用保存在寄存器里的备份;
	public volatile static boolean isStop;
	
	@Override
	public void run() {
		while (!isStop) {
			
			// 已爬过1W+页面,主动停掉爬虫
			if (QbLinkQueue.visitedUrlSize() > 10000) {
				isStop = true;
			}
			
			// 爬行节点URL获取：
			String link = QbLinkQueue.poll();
			if (link == null) {
				try {
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			// 爬取节点数据：解析糗百页面,存入"一面墙"队列
			parseQiubaiToWallQueen(link);
			
			// 爬取子节点：爬取子链接 (FIFO队列,广度优先)
			Set<String> links = JsoupUtlls.findLinks(QbLinkQueue.getBaseLink(), link);
			if (links!=null && links.size() > 0) {
				for (String item : links) {
					if (item!=null && !RegexUtil.ifContainChinese(URLEncoderUtil.decoderUTF8(item)) && item.indexOf("/tag/") == -1) {
						QbLinkQueue.add(item);
					}
				}
			}
		}
	}
	
	/**
	 * 解析糗百页面,存入"一面墙"队列
	 * @return
	 */
	private void parseQiubaiToWallQueen (String link){
		
		// 组装规则
		Map<Integer, Set<String>> tagMap = new HashMap<Integer, Set<String>>();
		Set<String> tagNameList = new HashSet<String>();
		tagNameList.add("article");
		tagMap.put(2, tagNameList);
		
		// 获取html
		Elements resultAll = JsoupUtlls.loadParse(link, null, null, false, tagMap);
		if (resultAll == null || resultAll.size() == 0) {
			return;
		}
		
		// 解析html
		Set<String> result = new HashSet<String>();
		for (Element item : resultAll) {
			
			// 处理超链接
			Elements aTags = item.getElementsByTag("a");
			for (Element element : aTags) {
				String href = element.attr("href");
				if (href.startsWith("/")) {
					href = QbLinkQueue.getBaseLink() + href;
				}
				element.attr("href",  href);
			}
			
			// 获取关键点数据
			String content = item.getElementsByClass("content").html();
			String thumb = item.getElementsByClass("thumb").html();
			String video_holder = item.getElementsByClass("video_holder").html();
			
			// 拼装返回
			StringBuffer buffer = new StringBuffer();
			buffer.append(content);
			if (StringUtils.isNotBlank(thumb)) {
				buffer.append("<hr>");
				buffer.append(thumb);
			}
			if (StringUtils.isNotBlank(video_holder)) {
				buffer.append("<hr>");
				buffer.append(video_holder);
			}
			String str = buffer.toString();
			if (StringUtils.isNotBlank(str)) {
				result.add(str);
			}
		}
		
		// 存入"一面墙"队列
		if (result!=null && result.size() > 0) {
			for (String content : result) {
				logger.error("---------------------------:" + link);
				logger.error(content);
				// 数据入库
				WallInfo wallInfo = new WallInfo();
				wallInfo.setId(0);
				wallInfo.setStatus(CommonDic.WallDic.WALL_STATUS_ORIGIN);
				wallInfo.setSource(CommonDic.WallDic.WALL_SOURCE_CRAWlER);
				wallInfo.setUserId(0);
				wallInfo.setContent(content);
				wallInfo.setImage(null);
				WallInfoQueue.getInstance().add(wallInfo);
			}
		}
		
	}

}