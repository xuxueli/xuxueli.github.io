package com.xxl.dao;

import java.util.List;
import java.util.Set;

import com.xxl.core.model.main.AdminUser;

public interface IAdminUserDao {
	
	/**
	 * 账号密码-匹配查询
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public AdminUser getByPwd (String username, String password);

	/**
	 * 修改密码
	 * @param userName
	 * @param password
	 * @param newPwd
	 * @return
	 */
	public int modifyPwd(String userName, String password, String newPwd);

	/**
	 * 分页查询
	 * @param userName
	 * @return
	 */
	public List<AdminUser> queryUser(int offset,int pagesize, String userName, int roleId);

	/**
	 * 分页查询Count
	 * @param offset
	 * @param pagesize
	 * @param userName
	 * @return
	 */
	public int queryUserCount(int offset, int pagesize, String userName, int roleId);

	/**
	 * 新增
	 * @param userName
	 * @param password
	 * @return
	 */
	public int userAdd(String userName, String password);

	/**
	 * 删除
	 * @param userIds
	 * @return
	 */
	public int userDel(int[] userIds);

	/**
	 * 更新
	 * @param userId
	 * @param userName
	 * @param password
	 * @return
	 */
	public int userUpdate(int userId, String userName, String password);

	/**
	 * 查询
	 * @param userId
	 * @return
	 */
	public AdminUser getById(int userId);

	/**
	 * 用户-角色依赖,全部删除
	 * @param userIds
	 * @return
	 */
	public int userRoleAllDel(int[] userIds);

	/**
	 * 用户-角色依赖,批量删除
	 * @param userId
	 * @param delRoldIds
	 */
	public int userRoleDel(int userId, Set<Integer> delRoldIds);

	/**
	 * 用户-角色依赖,批量新增
	 * @param userId
	 * @param addRoldIds
	 */
	public int userRoleAdd(int userId, Set<Integer> addRoldIds);

}
