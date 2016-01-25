package com.xxl.core.thread;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.core.model.main.EmailSendInfo;
import com.xxl.core.queue.EmailSendInfoQueue;
import com.xxl.service.IEmailSendService;
import com.xxl.service.ResourceBundle;

/**
 * 邮件发送,消费者线程
 * @author xuxueli
 */
public class EmailSendConsumerThread implements Runnable {
	private static final EmailSendInfoQueue queue = EmailSendInfoQueue.getInstance();
	private static transient Logger logger = LoggerFactory.getLogger(EmailSendConsumerThread.class);
	
	public void run() {
		while (true) {
			EmailSendInfo emailInfo = queue.poll();
			if (emailInfo == null) {
				try {
					TimeUnit.SECONDS.sleep(5);
					continue;
				} catch (InterruptedException e) {
					logger.error("[5i net. email send consumer, interrupted exception:]", e);
				}
			}
			
			IEmailSendService emailSendService = ResourceBundle.getInstance().getEmailSendService();
			emailSendService.send(emailInfo);
			logger.info("[5i net. email send consumer, thread handle email : {}]", emailInfo.toString());
			
		}
	}

}
