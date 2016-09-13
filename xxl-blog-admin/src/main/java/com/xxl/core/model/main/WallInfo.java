package com.xxl.core.model.main;

import java.io.Serializable;
import java.util.Date;

/**
 * 一面墙
 * @author xuxueli
 */

@SuppressWarnings("serial")
public class WallInfo implements Serializable{
	
	private int id;
	private int status;
	private int source;
	private int userId;
	private String content;
	private String image;
	private Date createTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Override
	public String toString() {
		return "WallInfo [id=" + id + ", status=" + status + ", source="
				+ source + ", userId=" + userId + ", content=" + content
				+ ", image=" + image + ", createTime=" + createTime + "]";
	}
	
}
