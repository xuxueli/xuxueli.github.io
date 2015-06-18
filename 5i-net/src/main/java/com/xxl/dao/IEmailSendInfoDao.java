package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.EmailSendInfo;

/**
 * 邮件发送信息
 * @author xuxueli
 */
public interface IEmailSendInfoDao {

	/**
	 * 保存
	 * @param email
	 * @return
	 */
	public int insert(EmailSendInfo email);

	/**
	 * 邮件列表 (时间顺序)
	 * @param emailStatus	: 邮件状态
	 * @param pagesize		: 查询数量
	 * @return
	 */
	List<EmailSendInfo> getListByStatus(int emailStatus, int pagesize);

	/**
	 * 更新状态
	 * @param id
	 * @param emailStatus
	 * @return
	 */
	public int updateStatus(int id, int emailStatus);

	/**
	 * 查询邮件
	 * @param userId
	 * @param emailType	: 邮件类型
	 * @return
	 */
	public EmailSendInfo getEmail(int userId, int emailType);

}
