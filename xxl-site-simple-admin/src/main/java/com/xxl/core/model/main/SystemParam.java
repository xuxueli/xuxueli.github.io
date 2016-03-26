package com.xxl.core.model.main;

import java.io.Serializable;

/**
 * @author xuxueli
 */
public class SystemParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String key;
	private String value;
	private String title;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
