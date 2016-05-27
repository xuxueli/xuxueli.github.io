package com.xxl.core.chat.socketio.data;

/**
 * 通讯类
 * @author xuxueli
 */
public class SioChatObject {

    private String userName;
    private String message;

    public SioChatObject() {
    }

    public SioChatObject(String userName, String message) {
        super();
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

	@Override
	public String toString() {
		return "ChatObject [userName=" + userName + ", message=" + message
				+ "]";
	}

}
