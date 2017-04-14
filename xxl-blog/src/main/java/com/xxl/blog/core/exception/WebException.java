package com.xxl.blog.core.exception;

/**
 * 自定义异常
 * @author xuxueli 2014-5-7 22:19:54
 */
public class WebException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;

	public WebException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public WebException(String msg) {
		super(msg);
		this.code = 500;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
