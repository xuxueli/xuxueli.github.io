package com.xxl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.constant.CommonDic.EmailStatus;
import com.xxl.core.constant.CommonDic.EmailType;
import com.xxl.core.model.main.EmailSendInfo;
import com.xxl.core.model.main.UserMain;
import com.xxl.core.util.HtmlTemplateUtil;
import com.xxl.core.util.MailUtil;
import com.xxl.dao.IEmailSendInfoDao;
import com.xxl.dao.IUserMainDao;
import com.xxl.service.IEmailSendService;

/**
 * 邮件发送
 * @author xuxueli
 */
@Service("emailSendService")
public class EmailSendServiceImpl implements IEmailSendService {
	
	@Autowired
	private IUserMainDao userMainDao;
	@Autowired
	private IEmailSendInfoDao emailSendInfoDao;
	
	/*
	 * 查询邮件列表,根据status
	 * @see com.xxl.service.IEmailSendService#getListByStatus(int, int)
	 */
	@Override
	public List<EmailSendInfo> getListByStatus(int emailStatus, int pagesize) {
		return emailSendInfoDao.getListByStatus(emailStatus, pagesize);
	}

	
	/*
	 * 发送邮件
	 * @see com.xxl.service.IEmailSendService#send(com.xxl.core.model.main.EmailSendInfo)
	 */
	@Override
	public void send(EmailSendInfo emailInfo) {
		
		int ret = emailSendInfoDao.updateStatus(emailInfo.getId(), EmailStatus.SEND.value());
		if (ret > 0) {
			if (EmailType.EMAIL_ACTIVATE.type() == emailInfo.getEmailType()) {
				UserMain userMain = userMainDao.getById(emailInfo.getUserId());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("userMain", userMain);
				params.put("sendCode", emailInfo.getSendCode());
				
				String contentStr = HtmlTemplateUtil.generateString(params, "net/emailtemplate/email.activate.template.ftl");
				boolean sendFlag = MailUtil.sendMailSpring(userMain.getEmail(), "《我爱》账号激活邮件", contentStr, true, null);
				if (!sendFlag) {
					emailSendInfoDao.updateStatus(emailInfo.getId(), EmailStatus.UN_SEND.value());
				}
			}
			
		}
		
	}
	
}
