package com.xxl.core.model.main;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件发送INFO
 * @author xuxueli
 */
@SuppressWarnings("serial")
public class EmailSendInfo implements Serializable {
	
	private int id;
	private int userId;
	private int emailType;
	private int emailStatus;
	private Date sendTime;
	private String sendCode;
	private String sendContent;
	
	@Override
	public String toString() {
		return "EmailSendInfo [id=" + id + ", userId=" + userId
				+ ", emailType=" + emailType + ", emailStatus=" + emailStatus
				+ ", sendTime=" + sendTime + ", sendCode=" + sendCode
				+ ", sendContent=" + sendContent + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getEmailType() {
		return emailType;
	}
	public void setEmailType(int emailType) {
		this.emailType = emailType;
	}
	public int getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getSendCode() {
		return sendCode;
	}
	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}
	public String getSendContent() {
		return sendContent;
	}
	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}
	
}
