package com.xxl.core.model.main;

import java.io.Serializable;

/**
 * 通用配置
 * @author xuxueli
 */
@SuppressWarnings("serial")
public class CommonParam implements Serializable {
	private String key;
	private String value;
	private String desc;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
