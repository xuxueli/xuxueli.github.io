package com.xxl.core.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.xxl.core.model.main.WallInfo;

/**
 * 一面墙,队列
 * @author xuxueli
 */
public class WallInfoQueue {	
	private static BlockingQueue<WallInfo> queue;
	// 短信优先级队列
	private static final WallInfoQueue wallQueue = new WallInfoQueue();	
	// 优先级队列初始大小
	private static final int init_queue_size = 0xfff8;	
	private WallInfoQueue() {
		queue = new LinkedBlockingQueue<WallInfo>(init_queue_size);
	}	
	public static WallInfoQueue getInstance() {
		return wallQueue;
	}
	
	/**
	 * 入队列 (添加一个元素,如果队列已满,则抛出一个IIIegaISlabEepeplian异常)
	 * @param message
	 */
	public void add(WallInfo wallInfo) {
		queue.add(wallInfo);
	}

	/**
	 * 出队列 (移除并返问队列头部的元素,如果队列为空，则返回null)
	 * @return
	 */
	public WallInfo poll() {
		return queue.poll();
	}
	
}
