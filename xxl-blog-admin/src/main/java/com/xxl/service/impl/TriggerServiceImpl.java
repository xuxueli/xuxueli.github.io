package com.xxl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.util.clawler.CrawlerThreadPollUtil;
import com.xxl.service.ITriggerService;
import com.xxl.service.IWallService;

/**
 * Trigger
 * @author xuxueli
 */
@Service("triggerService")
public class TriggerServiceImpl implements ITriggerService {
	private static transient Logger logger = LoggerFactory.getLogger(TriggerServiceImpl.class);
	
	@Autowired
	private IWallService wallService;

	/**
	 * 一面墙,内容抓取爬虫.start
	 */
	@Override
	public void wallClawlerStart() {
		logger.info("一面墙,内容抓取爬虫,多线程... start");
		
		wallService.freshTable();
		CrawlerThreadPollUtil.execQiubaiStart(true);
	}

	/**
	 * 一面墙,内容抓取爬虫.stop
	 */
	@Override
	public void wallClawlerStop() {
		logger.info("一面墙,内容抓取爬虫,多线程... stop");
		CrawlerThreadPollUtil.execQiubaiStart(false);
	}
	
}
