package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.AdminUser;
import com.xxl.dao.IAdminUserDao;

@Repository
public class AdminUserDaoImpl implements IAdminUserDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/*
	 * 账号密码-匹配查询
	 * @see com.xxl.dao.IAdminUserDao#getByPwd(java.lang.String, java.lang.String)
	 */
	@Override
	public AdminUser getByPwd (String username, String password) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", password);
		
		return sqlSessionTemplate.selectOne("AdminUserMapper.getByPwd", params);
	}

	/*
	 * 修改密码
	 * @see com.xxl.dao.IAdminUserDao#modifyPwd(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int modifyPwd(String userName, String password, String newPwd) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		params.put("password", password);
		params.put("newPwd", newPwd);
		
		return sqlSessionTemplate.update("AdminUserMapper.modifyPwd", params);
	}

	/*
	 * 后台用户,分页查询
	 * @see com.xxl.dao.IAdminUserDao#queryUser(java.lang.String)
	 */
	@Override
	public List<AdminUser> queryUser(int offset,int pagesize, String userName, int roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("userName", userName);
		params.put("roleId", roleId);
		
		return sqlSessionTemplate.selectList("AdminUserMapper.queryUser", params);
	}

	/*
	 * 后台用户,分页查询Count
	 * @see com.xxl.dao.IAdminUserDao#queryUserCount(int, int, java.lang.String)
	 */
	@Override
	public int queryUserCount(int offset, int pagesize, String userName, int roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("userName", userName);
		params.put("roleId", roleId);
		
		return sqlSessionTemplate.selectOne("AdminUserMapper.queryUserCount", params);
	}

	/*
	 * 新增
	 * @see com.xxl.dao.IAdminUserDao#userAdd(java.lang.String, java.lang.String)
	 */
	@Override
	public int userAdd(String userName, String password) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		params.put("password", password);
		
		return sqlSessionTemplate.insert("AdminUserMapper.userAdd", params);
	}

	/*
	 * 删除
	 * @see com.xxl.dao.IAdminUserDao#userDel(int[])
	 */
	@Override
	public int userDel(int[] userIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIds", userIds);
		return sqlSessionTemplate.delete("AdminUserMapper.userDel", params);
	}

	/*
	 * 更新
	 * @see com.xxl.dao.IAdminUserDao#userUpdate(int, java.lang.String, java.lang.String)
	 */
	@Override
	public int userUpdate(int userId, String userName, String password) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("userName", userName);
		params.put("password", password);
		return sqlSessionTemplate.update("AdminUserMapper.userUpdate", params);
	}

	/*
	 * 查询
	 * @see com.xxl.dao.IAdminUserDao#getById(int)
	 */
	@Override
	public AdminUser getById(int userId) {
		return sqlSessionTemplate.selectOne("AdminUserMapper.getById", userId);
	}

	/*
	 * 用户-角色依赖,全部删除
	 * @see com.xxl.dao.IAdminUserDao#userRoleDel(int[])
	 */
	@Override
	public int userRoleAllDel(int[] userIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userIds", userIds);
		return sqlSessionTemplate.delete("AdminUserMapper.userRoleAllDel", params);
	}

	/*
	 * 用户-角色依赖,批量删除
	 * @see com.xxl.dao.IAdminUserDao#userRoleDel(int, java.util.Set)
	 */
	@Override
	public int userRoleDel(int userId, Set<Integer> delRoldIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("delRoldIds", delRoldIds);
		return sqlSessionTemplate.delete("AdminUserMapper.userRoleDel", params);
	}

	/*
	 * 用户-角色依赖,批量新增
	 * @see com.xxl.dao.IAdminUserDao#userRoleAdd(int, java.util.Set)
	 */
	@Override
	public int userRoleAdd(int userId, Set<Integer> addRoldIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("addRoldIds", addRoldIds);
		return sqlSessionTemplate.delete("AdminUserMapper.userRoleAdd", params);
	}

}
