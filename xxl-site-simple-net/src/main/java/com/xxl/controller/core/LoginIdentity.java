package com.xxl.controller.core;

import java.io.Serializable;
import java.util.Date;

/**
 * 登陆信息缓存
 * @author xuxueli
 */
@SuppressWarnings("serial")
public class LoginIdentity implements Serializable {
	
	private int userId;
	private String email;
	private String password;
	private String userToken;
	private String realName;
	private int state;
	private Date regTime;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	
}
