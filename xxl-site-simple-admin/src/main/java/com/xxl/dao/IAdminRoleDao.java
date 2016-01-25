package com.xxl.dao;

import java.util.List;
import java.util.Set;

import com.xxl.core.model.main.AdminRole;


/**
 * 后台角色
 * @author xuxueli
 */
public interface IAdminRoleDao {
	
	/**
	 * 查询角色
	 * @param userId
	 * @return
	 */
	public AdminRole getRole(int userId, int roleId);

	/**
	 * 查询所有角色
	 * @return
	 */
	public List<AdminRole> getAllRole();

	/**
	 * 新增
	 * @param name
	 * @param order
	 * @return
	 */
	public int roleAdd(String name, int order);

	/**
	 * 删除
	 * @param roleIds
	 * @return
	 */
	public int roleDel(int[] roleIds);

	/**
	 * 更新
	 * @param roleId
	 * @param name
	 * @param order
	 * @return
	 */
	public int roleUpdate(int roleId, String name, int order);

	/**
	 * 用户-角色,是否关联
	 * @param roleId
	 * @return
	 */
	public int ifRelUser(int roleId);

	/**
	 * 批量删除,角色-菜单关联
	 * @param roleIds
	 * @return
	 */
	public int roleMenuDel(int[] roleIds);

	/**
	 * 查询
	 * @param roleId
	 * @return
	 */
	public AdminRole getRoleById(int roleId);
	
	/**
	 * 查询
	 * @param userId
	 * @return
	 */
	public List<AdminRole> getRoleByUserId(int userId);

	/**
	 * 角色-菜单,新增依赖
	 * @param roleId
	 * @param addMenudIds
	 */
	public int roleMenusAdd(int roleId, Set<Integer> addMenudIds);

	/**
	 * 角色-菜单,删除依赖
	 * @param roleId
	 * @param delMenudIds
	 */
	public int roleMenusDel(int roleId, Set<Integer> delMenudIds);

}
