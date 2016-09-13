package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.AdminRole;
import com.xxl.dao.IAdminRoleDao;

/**
 * 后台角色
 * @author xuxueli
 */
@Repository
public class AdminRoleDaoImpl implements IAdminRoleDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 查询角色
	 * @see com.xxl.dao.IAdminRoleDao#getRole(int)
	 */
	@Override
	public AdminRole getRole(int userId, int roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("roleId", roleId);
		return sqlSessionTemplate.selectOne("AdminRoleMapper.getRole", params);
	}

	/*
	 * 查询所有角色
	 * @see com.xxl.dao.IAdminRoleDao#getAllRole()
	 */
	@Override
	public List<AdminRole> getAllRole() {
		return sqlSessionTemplate.selectList("AdminRoleMapper.getAllRole");
	}

	/*
	 * 新增
	 * @see com.xxl.dao.IAdminRoleDao#userAdd(java.lang.String, int)
	 */
	@Override
	public int roleAdd(String name, int order) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("order", order);
		return sqlSessionTemplate.insert("AdminRoleMapper.roleAdd", params);
	}

	/*
	 * 删除
	 * @see com.xxl.dao.IAdminRoleDao#roleDel(int[])
	 */
	@Override
	public int roleDel(int[] roleIds) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("roleIds", roleIds);
		return sqlSessionTemplate.delete("AdminRoleMapper.roleDel", params);
	}

	/*
	 * 更新
	 * @see com.xxl.dao.IAdminRoleDao#roleUpdate(int, java.lang.String, int)
	 */
	@Override
	public int roleUpdate(int roleId, String name, int order) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("roleId", roleId);
		params.put("name", name);
		params.put("order", order);
		return sqlSessionTemplate.update("AdminRoleMapper.roleUpdate", params);
	}

	/*
	 * 用户-角色,是否关联
	 * @see com.xxl.dao.IAdminRoleDao#ifRelUser(int)
	 */
	@Override
	public int ifRelUser(int roleId) {
		return sqlSessionTemplate.selectOne("AdminRoleMapper.ifRelUser", roleId);
	}

	/*
	 * 批量删除,角色-菜单关联
	 * @see com.xxl.dao.IAdminRoleDao#roleMenuDel(int[])
	 */
	@Override
	public int roleMenuDel(int[] roleIds) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("roleIds", roleIds);
		return sqlSessionTemplate.delete("AdminRoleMapper.roleMenuDel", params);
	}

	/*
	 * 查询
	 * @see com.xxl.dao.IAdminRoleDao#getRoleById(int)
	 */
	@Override
	public AdminRole getRoleById(int roleId) {
		return sqlSessionTemplate.selectOne("AdminRoleMapper.getRoleById", roleId);
	}
	
	/*
	 * 查询
	 * @see com.xxl.dao.IAdminRoleDao#getRoleByUserId(int)
	 */
	public List<AdminRole> getRoleByUserId(int userId) {
		return sqlSessionTemplate.selectList("AdminRoleMapper.getRoleByUserId", userId);
	}

	/*
	 * 角色-菜单,新增依赖
	 * @see com.xxl.dao.IAdminRoleDao#roleMenusAdd(int, java.util.Set)
	 */
	@Override
	public int roleMenusAdd(int roleId, Set<Integer> addMenudIds) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("roleId", roleId);
		params.put("addMenudIds", addMenudIds);
		
		return sqlSessionTemplate.insert("AdminRoleMapper.roleMenusAdd", params);
	}
	
	/*
	 * 角色-菜单,删除依赖
	 * @see com.xxl.dao.IAdminRoleDao#roleMenusDel(int, java.util.Set)
	 */
	@Override
	public int roleMenusDel(int roleId, Set<Integer> delMenudIds) {
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("roleId", roleId);
		params.put("delMenudIds", delMenudIds);
		
		return sqlSessionTemplate.insert("AdminRoleMapper.roleMenusDel", params);
	}

}
