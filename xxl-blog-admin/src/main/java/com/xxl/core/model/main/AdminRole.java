package com.xxl.core.model.main;

/**
 * 角色表
 * @author xuxueli
 */
public class AdminRole {
	
	private int roleId;
	private String name;
	private int order;
	
	// 角色是否拥有
	private boolean selected;
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
}
