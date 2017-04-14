package com.xxl.core.result;

/**
 * 封装返回
 * 
 * @author xuxueli 2015-3-29 18:27:32
 * @param <T>
 */
public class ReturnT<T> {
	public static final String SUCCESS = "S";
	public static final String FAIL = "E";

	/** 状态码 */
	private String code;
	/** 状态提示 */
	private String msg;
	/** 数据对象 */
	private T returnContent;
	
	public ReturnT() {
		super();
		this.code = SUCCESS;
	}
	public ReturnT(T returnContent) {
		super();
		this.code = SUCCESS;
		this.returnContent = returnContent;
	}
	public ReturnT(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", returnContent="
				+ returnContent + "]";
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getReturnContent() {
		return returnContent;
	}
	public void setReturnContent(T returnContent) {
		this.returnContent = returnContent;
	}
	
}
