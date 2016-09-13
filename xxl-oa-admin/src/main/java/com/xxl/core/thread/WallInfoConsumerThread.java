package com.xxl.core.thread;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.core.model.main.WallInfo;
import com.xxl.core.queue.WallInfoQueue;
import com.xxl.service.IWallService;
import com.xxl.service.ResourceBundle;

/**
 * 一面墙,队列,消费者线程
 * @author xuxueli
 */
public class WallInfoConsumerThread implements Runnable {
	private static final WallInfoQueue queue = WallInfoQueue.getInstance();
	private static transient Logger logger = LoggerFactory.getLogger(WallInfoConsumerThread.class);
	
	public void run() {
		while (true) {
			WallInfo wallInfo = queue.poll();
			if (wallInfo == null) {
				try {
					TimeUnit.SECONDS.sleep(5);
					continue;
				} catch (InterruptedException e) {
					logger.error("[5i net. email send consumer, interrupted exception:]", e);
				}
			}
			
			IWallService wallService = ResourceBundle.getInstance().getWallService();
			wallService.wallAdd(wallInfo);
			
			logger.info("[5i net. email send consumer, thread handle email : {}]", wallInfo.toString());
			
		}
	}

}
