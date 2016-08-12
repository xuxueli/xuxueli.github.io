package com.xxl.core.thread;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.core.constant.CommonDic.EmailStatus;
import com.xxl.core.model.main.EmailSendInfo;
import com.xxl.core.queue.EmailSendInfoQueue;
import com.xxl.service.IEmailSendService;
import com.xxl.service.ResourceBundle;

/**
 * 邮件发送,生产者线程
 * @author xuxueli
 */
public class EmailSendProductThread implements Runnable {
	private static final EmailSendInfoQueue queue = EmailSendInfoQueue.getInstance();
	private static transient Logger logger = LoggerFactory.getLogger(EmailSendProductThread.class);
	
	public void run() {
		while (true) {
			EmailSendInfo emailInfo = queue.peek();
			if (emailInfo != null) {
				try {
					TimeUnit.SECONDS.sleep(5);
					continue;
				} catch (InterruptedException e) {
					logger.error("[5i net. email send pruduct, interrupted exception:]", e);
				}
			}
			
			IEmailSendService emailSendService = ResourceBundle.getInstance().getEmailSendService();
			List<EmailSendInfo> sendList = emailSendService.getListByStatus(EmailStatus.UN_SEND.value(), 200);
			if (CollectionUtils.isEmpty(sendList)) {
				try {
					TimeUnit.SECONDS.sleep(5);
					continue;
				} catch (InterruptedException e) {
					logger.error("[5i net. email send pruduct, interrupted exception:]", e);
				}
			}
			
			// 线程队列,填充数据
			queue.addAll(sendList);
			logger.info("[5i net. email send pruduct, thread addAll size : {}]", sendList.size());
			
		}
	}

}
