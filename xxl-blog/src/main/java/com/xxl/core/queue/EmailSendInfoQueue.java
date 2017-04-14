package com.xxl.core.queue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.xxl.core.model.main.EmailSendInfo;

/**
 * 邮件发送队列
 * @author xuxueli
 */
public class EmailSendInfoQueue {	
	private static BlockingQueue<EmailSendInfo> queue;
	// 短信优先级队列
	private static final EmailSendInfoQueue presentDelayQueue = new EmailSendInfoQueue();	
	// 优先级队列初始大小
	private static final int init_queue_size = 0xfff8;	
	private EmailSendInfoQueue() {
		queue = new LinkedBlockingQueue<EmailSendInfo>(init_queue_size);
	}	
	public static EmailSendInfoQueue getInstance() {
		return presentDelayQueue;
	}
	
	/**
	 * 入队列 
	 * @param message
	 */
	public void addAll(List<EmailSendInfo> emailList) {
		queue.addAll(emailList);
	}
	
	/**
	 * 入队列 (添加一个元素,如果队列已满,则抛出一个IIIegaISlabEepeplian异常)
	 * @param message
	 */
	public void add(EmailSendInfo emailInfo) {
		queue.add(emailInfo);
	}

	/**
	 * 入队列 (添加一个元素并返回true,如果队列已满，则返回false)
	 * @param o
	 * @return
	 */
	public boolean offer(EmailSendInfo emailInfo) {
		return queue.offer(emailInfo);
	}

	/**
	 * 出队列 (返回队列头部的元素,如果队列为空，则返回null)
	 * @return
	 */
	public EmailSendInfo peek() {
		return queue.peek();
	}

	/**
	 * 出队列 (移除并返问队列头部的元素,如果队列为空，则返回null)
	 * @return
	 */
	public EmailSendInfo poll() {
		return queue.poll();
	}
	
}
