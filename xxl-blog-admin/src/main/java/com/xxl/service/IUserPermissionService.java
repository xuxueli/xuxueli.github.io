package com.xxl.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.xxl.core.result.ReturnT;

/**
 * 用户-角色-权限
 * @author xuxueli
 */
public interface IUserPermissionService {
	
	/**
	 * 后台用户,分页查询
	 * @return
	 */
	public Map<String, Object> userQuery(int page,int rows, String userName, int roleId);

	/**
	 * 后台用户,新增
	 * @return
	 */
	public ReturnT<Integer> userAdd(String userName, String password);

	/**
	 * 后台用户,删除
	 * @param userIds
	 * @return
	 */
	public ReturnT<Integer> userDel(HttpSession session, int[] userIds);

	/**
	 * 后台用户,更新
	 * @param userId
	 * @param userName
	 * @param password
	 * @return
	 */
	public ReturnT<Integer> userUpdate(int userId, String userName,	String password);
	
	/**
	 * 后台,用户-角色查询
	 * @param userId
	 * @return
	 */
	public String userRoleQuery(int userId);
	
	/**
	 * 后台,用户-角色更新
	 * @param userId
	 * @param userIds
	 * @return
	 */
	public ReturnT<Integer> userRoleUpdate(HttpSession session, int userId, int[] roleIds);

	/**
	 * 后台角色,查询
	 * @return
	 */
	public Map<String, Object> roleQuery();

	/**
	 * 后台角色,新增
	 * @param name
	 * @param order
	 * @return
	 */
	public ReturnT<Integer> roleAdd(String name, int order);

	/**
	 * 后台角色,删除
	 * @param roleIds
	 * @return
	 */
	public ReturnT<Integer> roleDel(int[] roleIds);

	/**
	 * 后台角色,更新
	 * @param roleId
	 * @param name
	 * @param order
	 * @return
	 */
	public ReturnT<Integer> roleUpdate(int roleId, String name, int order);
	
	/**
	 * 后台角色-权限,查询
	 * @param roleId
	 * @return
	 */
	public String roleMenuQuery(int roleId);
	
	/**
	 * 后台角色-权限,更新
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	public ReturnT<String> roleMenuUpdate(int roleId, int[] menuIds);

	/**
	 * 后台菜单,查询
	 * @return
	 */
	public Map<String, Object> menuQuery();

	/**
	 * 后台菜单,新增
	 * @param parent_id
	 * @param order
	 * @param name
	 * @param url
	 * @return
	 */
	public ReturnT<Integer> menuAdd(int parentId, int order, String name, String url, int permessionNum);

	/**
	 * 后台菜单,删除
	 * @param menuId
	 * @return
	 */
	public ReturnT<Integer> menuDel(int menuId);

	/**
	 * 后台菜单,更新
	 * @param menuId
	 * @param parentId
	 * @param order
	 * @param name
	 * @param url
	 * @return
	 */
	public ReturnT<Integer> menuUpdate(int menuId, int parentId, int order, String name, String url, int permessionNum);
}
