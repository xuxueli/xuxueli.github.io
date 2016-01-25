package com.xxl.core.model.main;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限菜单[三级:模块-组-子项]
 * @author xuxueli
 */
public class AdminMenu {

	private int menuId;
	private int parentId;
	private int order;
	private String name;
	private String url;
	private int permessionNum;
	
	// 关联角色ID
	private boolean checked;
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	// 父节点：用于easyui展示树形结构
	@SuppressWarnings("unused")
	private int _parentId;
	public int get_parentId() {
		return this.parentId;
	}
	public void set_parentId(int _parentId) {
		this._parentId = _parentId;
	}
	
	// 子菜单：用于手动生成权限菜单
	private List<AdminMenu> subMenus = new ArrayList<AdminMenu>();
	public List<AdminMenu> getSubMenus() {
		return subMenus;
	}
	public void setSubMenus(List<AdminMenu> subMenus) {
		this.subMenus = subMenus;
	}
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public int getPermessionNum() {
		return permessionNum;
	}
	public void setPermessionNum(int permessionNum) {
		this.permessionNum = permessionNum;
	}
}
