package com.xxl.service;

import java.util.List;

import com.xxl.core.model.main.EmailSendInfo;

/**
 * 邮件发送
 * @author xuxueli
 */
public interface IEmailSendService {

	/**
	 * 查询邮件列表,根据status
	 * @param pagesize
	 * @return
	 */
	public List<EmailSendInfo> getListByStatus(int emailStatus, int pagesize);

	/**
	 * 发送邮件
	 * @param emailInfo
	 */
	public void send(EmailSendInfo emailInfo);

}
