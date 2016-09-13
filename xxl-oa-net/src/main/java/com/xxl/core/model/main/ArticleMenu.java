package com.xxl.core.model.main;

import java.util.List;

/**
 * 文章菜单[两级:模块-组]
 * @author xuxueli
 */
public class ArticleMenu {
	
	private int menuId;
	private int parentId;
	private int order;
	private String name;
	private String	desc;
	private int clickCount;
	
	// 关联子菜单
	private List<ArticleMenu> children;
	
	@Override
	public String toString() {
		return "ArticleMenu [menuId=" + menuId + ", parentId=" + parentId
				+ ", order=" + order + ", name=" + name + ", desc=" + desc
				+ ", clickCount=" + clickCount + ", children=" + children + "]";
	}
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
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
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public List<ArticleMenu> getChildren() {
		return children;
	}
	public void setChildren(List<ArticleMenu> children) {
		this.children = children;
	}
}
