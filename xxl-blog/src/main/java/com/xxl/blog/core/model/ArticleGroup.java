package com.xxl.blog.core.model;

import java.util.List;

/**
 * @author xuxueli
 */
public class ArticleGroup {

	private int id;
	private int parentId;
	private int order;
	private String name;
	private String	desc;

	// 关联子菜单
	private List<ArticleGroup> children;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ArticleGroup> getChildren() {
		return children;
	}

	public void setChildren(List<ArticleGroup> children) {
		this.children = children;
	}

}
