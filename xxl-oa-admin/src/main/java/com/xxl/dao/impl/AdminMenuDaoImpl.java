package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.AdminMenu;
import com.xxl.dao.IAdminMenuDao;

@Repository
public class AdminMenuDaoImpl implements IAdminMenuDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/*
	 * 查询权限菜单
	 * @see com.xxl.dao.IAdminMenuDao#getMenus()
	 */
	@Override
	public List<AdminMenu> getMyMenus(int roleId) {
		return sqlSessionTemplate.selectList("AdminMenuMapper.getMyMenus", roleId);
	}

	/*
	 * 查询所有权限菜单
	 * @see com.xxl.dao.IAdminMenuDao#getAllMenus()
	 */
	@Override
	public List<AdminMenu> getAllMenus() {
		return sqlSessionTemplate.selectList("AdminMenuMapper.getAllMenus");
	}

	/*
	 * 根据合法父菜单 (菜单模块、菜单组)
	 * @see com.xxl.dao.IAdminMenuDao#getByMenuId(int)
	 */
	@Override
	public AdminMenu getEffectParent(int parentId, int moduleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		params.put("moduleId", moduleId);
		return sqlSessionTemplate.selectOne("AdminMenuMapper.getEffectParent", params);
	}

	/*
	 * 保存
	 * @see com.xxl.dao.IAdminMenuDao#insert(int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public int insert(int parentId, int order, String name, String url, int permessionNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		params.put("order", order);
		params.put("name", name);
		params.put("url", url);
		params.put("permessionNum", permessionNum);
		return sqlSessionTemplate.insert("AdminMenuMapper.insert", params);
	}

	/*
	 * 删除
	 * @see com.xxl.dao.IAdminMenuDao#menuDel(int)
	 */
	@Override
	public int menuDel(int menuId) {
		return sqlSessionTemplate.delete("AdminMenuMapper.menuDel", menuId);
	}

	/*
	 * 查询子菜单
	 * @see com.xxl.dao.IAdminMenuDao#getSubMenus(int)
	 */
	@Override
	public List<AdminMenu> getSubMenus(int menuId) {
		return sqlSessionTemplate.selectList("AdminMenuMapper.getSubMenus", menuId);
	}

	/*
	 * 菜单是否使用
	 * @see com.xxl.dao.IAdminMenuDao#ifRelRole(int)
	 */
	@Override
	public int ifRelRole(int menuId) {
		return sqlSessionTemplate.selectOne("AdminMenuMapper.ifRelRole", menuId);
	}

	/*
	 * 更新
	 * @see com.xxl.dao.IAdminMenuDao#update(int, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public int update(int menuId, int parentId, int order, String name, String url, int permessionNum) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("menuId", menuId);
		params.put("parentId", parentId);
		params.put("order", order);
		params.put("name", name);
		params.put("url", url);
		params.put("permessionNum", permessionNum);
		return sqlSessionTemplate.update("AdminMenuMapper.update", params);
	}
	
}
