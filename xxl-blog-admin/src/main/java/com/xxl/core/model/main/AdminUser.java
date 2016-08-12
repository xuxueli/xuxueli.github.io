package com.xxl.core.model.main;

/**
 * 后台用户信息
 * @author xuxueli
 *
 */
public class AdminUser {
	
	private int userId;
	private String userName;
	private String password;
	private String userToken;
	private String realName;
	
	@Override
	public String toString() {
		return "AdminUser [userId=" + userId + ", userName=" + userName
				+ ", password=" + password + ", userToken=" + userToken
				+ ", realName=" + realName + "]";
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	
}
