package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.AdminMenu;


public interface IAdminMenuDao {
	
	/**
	 * 查询权限菜单
	 * @return
	 */
	public List<AdminMenu> getMyMenus(int roleId);

	/**
	 * 查询所有权限菜单
	 * @return
	 */
	public List<AdminMenu> getAllMenus();
	
	/**
	 * 根据合法父菜单 (菜单模块、菜单组)
	 * @param parent_id
	 * @return
	 */
	public AdminMenu getEffectParent(int parentId, int moduleId);

	/**
	 * 保存
	 * @param parent_id
	 * @param order
	 * @param name
	 * @param url
	 * @return
	 */
	public int insert(int parentId, int order, String name, String url, int permessionNum);

	/**
	 * 删除
	 * @param menuId
	 * @return
	 */
	public int menuDel(int menuId);

	/**
	 * 查询子菜单
	 * @param menuId
	 * @return
	 */
	public List<AdminMenu> getSubMenus(int menuId);

	/**
	 * 菜单是否使用
	 * @param menuId
	 * @return
	 */
	public int ifRelRole(int menuId);

	/**
	 * 更新
	 * @param menuId
	 * @param parentId
	 * @param order
	 * @param name
	 * @param url
	 * @return
	 */
	public int update(int menuId, int parentId, int order, String name, String url, int permessionNum);
	
}
