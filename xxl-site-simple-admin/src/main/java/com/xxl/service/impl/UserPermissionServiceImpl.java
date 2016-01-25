package com.xxl.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.constant.CommonDic.AdminRoleMenuDic;
import com.xxl.core.constant.CommonDic.HttpSessionKeyDic;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.model.main.AdminMenu;
import com.xxl.core.model.main.AdminRole;
import com.xxl.core.model.main.AdminUser;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.HttpSessionUtil;
import com.xxl.core.util.JacksonUtil;
import com.xxl.dao.IAdminMenuDao;
import com.xxl.dao.IAdminRoleDao;
import com.xxl.dao.IAdminUserDao;
import com.xxl.service.IUserPermissionService;
import com.xxl.service.helper.MenuModuleHelper;

/**
 * 用户-角色-权限
 * @author xuxueli
 */
@Service
public class UserPermissionServiceImpl implements IUserPermissionService {
	//private static transient Logger logger = LoggerFactory.getLogger(UserPermissionServiceImpl.class);
	
	@Autowired
	private IAdminUserDao adminUserDao;
	@Autowired
	private IAdminRoleDao adminRoleDao;
	@Autowired
	private IAdminMenuDao adminMenuDao;
	
	//-----------------------后台用户相关-----------------------
	
	/*
	 * 后台用户,分页查询
	 * @see com.xxl.service.IUserPermissionService#queryUser()
	 */
	@Override
	public Map<String, Object> userQuery(int page,int rows, String userName, int roleId) {
		int offset = page<1? 0 : (page-1)*rows;
		int pagesize = rows;
		
		List<AdminUser> rowsData = adminUserDao.queryUser(offset, pagesize, userName, roleId);
		int totalNumber = adminUserDao.queryUserCount(offset, pagesize, userName, roleId);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", rowsData);  
		resultMap.put("total", totalNumber);
		return resultMap;
	}

	/*
	 * 后台用户,新增
	 * @see com.xxl.service.IUserPermissionService#userAdd(java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<Integer> userAdd(String userName, String password) {
		ReturnT<Integer> result = new ReturnT<Integer>();
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("操作失败,用户参数不完整");
			return result;
		}
		int count = adminUserDao.userAdd(userName, password);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台用户,删除 
	 * @see com.xxl.service.IUserPermissionService#userDel(int[])
	 */
	@Override
	public ReturnT<Integer> userDel(HttpSession session, int[] userIds) {
		ReturnT<Integer> result = new ReturnT<Integer>();
		if (ArrayUtils.isEmpty(userIds)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("删除失败,未选中用户");
			return result;
		}
		
		LoginIdentity identity = (LoginIdentity) HttpSessionUtil.get(session, HttpSessionKeyDic.LOGIN_IDENTITY);
		for (int userId : userIds) {
			// 01：不允许删除,自个儿
			if (identity.getUserId() == userId) {
				result.setCode(ReturnCodeEnum.FAIL.code());
				result.setMsg("删除失败,不允许删除自个儿");
				return result;
			}
			// 02：不允许删除,不存在用户
			AdminUser user = adminUserDao.getById(userId);
			if (user == null) {
				result.setCode(ReturnCodeEnum.FAIL.code());
				result.setMsg("删除失败,用户[" + userId + "]不存在");
				return result;
			}
			// 03：管理员角色关联,不允许删除
			AdminRole superRole = adminRoleDao.getRole(user.getUserId(), AdminRoleMenuDic.SUPER_ROLE_ID);
			if (superRole != null) {
				result.setCode(ReturnCodeEnum.FAIL.code());
				result.setMsg("删除失败,用户[" + userId + "]为超级管理员");
				return result;
			}
		}
		
		// 用户-角色依赖,全部删除
		int count = adminUserDao.userRoleAllDel(userIds);
		// 批量删除-用户
		count = adminUserDao.userDel(userIds);
		
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台用户,更新
	 * @see com.xxl.service.IUserPermissionService#userUpdate(int, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<Integer> userUpdate(int userId, String userName, String password) {
		ReturnT<Integer> result = new ReturnT<Integer>();
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("操作失败,用户参数不完整");
			return result;
		}
		int count = adminUserDao.userUpdate(userId, userName, password);
		if (count < 1) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("操作失败");
			return result;
		}
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台,用户-角色查询
	 * @see com.xxl.service.IUserPermissionService#userRoleQuery(int)
	 */
	public String userRoleQuery(int userId) {
		List<AdminRole> allRoles = adminRoleDao.getAllRole();
		List<AdminRole> myRoles = adminRoleDao.getRoleByUserId(userId);
		// 设置选中
		for (AdminRole all : allRoles) {
			for (AdminRole my : myRoles) {
				if (all.getRoleId() == my.getRoleId()) {
					all.setSelected(true);
				}
			}
		}
		
		return JacksonUtil.writeValueAsString(allRoles);
	}
	
	/*
	 * 后台,用户-角色更新
	 * @see com.xxl.service.IUserPermissionService#userRoleUpdate(int, int[])
	 */
	public ReturnT<Integer> userRoleUpdate(HttpSession session, int userId, int[] roleIds) {
		
		AdminUser user = adminUserDao.getById(userId);
		if (user == null) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "操作失败,用户不存在");
		}
		
		// 旧角色
		Set<Integer> oldRoldIds = new HashSet<Integer>();
		List<AdminRole> oleRole = adminRoleDao.getRoleByUserId(userId);
		for (AdminRole old : oleRole) {
			oldRoldIds.add(old.getRoleId());
		}
		
		// 新角色
		Set<Integer> newRoldIds = new HashSet<Integer>();
		if (ArrayUtils.isNotEmpty(roleIds)) {
			for (Integer roleId : roleIds) {
				newRoldIds.add(roleId);
			}
		}
		
		// 批量操作ID
		Set<Integer> delRoldIds = new HashSet<Integer>(oldRoldIds);
		Set<Integer> addRoldIds = new HashSet<Integer>(newRoldIds);
		delRoldIds.removeAll(newRoldIds);
		addRoldIds.removeAll(oldRoldIds);
		
		LoginIdentity identity = (LoginIdentity) HttpSessionUtil.get(session, HttpSessionKeyDic.LOGIN_IDENTITY);
		if (identity.getUserId() == userId && delRoldIds.contains(AdminRoleMenuDic.SUPER_ROLE_ID)) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "操作失败,不允许删除自己的超级管理员角色");
		}
		
		// 批量操作
		if (CollectionUtils.isNotEmpty(delRoldIds)) {
			adminUserDao.userRoleDel(userId, delRoldIds);
		}
		if (CollectionUtils.isNotEmpty(addRoldIds)) {
			adminUserDao.userRoleAdd(userId, addRoldIds);
		}
		
		return new ReturnT<Integer>();
	}
	
	//-----------------------后台角色相关-----------------------
	
	/*
	 * 后台角色,查询
	 * @see com.xxl.service.IUserPermissionService#roleQuery()
	 */
	@Override
	public Map<String, Object> roleQuery() {
		List<AdminRole> rowsData = adminRoleDao.getAllRole();
		int totalNumber = rowsData!=null?rowsData.size():0;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", rowsData);  
		resultMap.put("total", totalNumber);
		return resultMap;
	}

	/*
	 * 后台角色,新增
	 * @see com.xxl.service.IUserPermissionService#roleAdd(java.lang.String, int)
	 */
	@Override
	public ReturnT<Integer> roleAdd(String name, int order) {
		ReturnT<Integer> result = new ReturnT<Integer>();
		if (StringUtils.isBlank(name)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("操作失败,角色名称为空");
			return result;
		}
		int count = adminRoleDao.roleAdd(name, order);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台角色,删除
	 * @see com.xxl.service.IUserPermissionService#roleDel(int[])
	 */
	@Override
	public ReturnT<Integer> roleDel(int[] roleIds) {
		ReturnT<Integer> result = new ReturnT<Integer>();
		if (ArrayUtils.isEmpty(roleIds)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("删除失败,未选中角色");
			return result;
		}
		
		for (int roleId : roleIds) {
			// 超级管理员,角色,不允许删除
			if (roleId == AdminRoleMenuDic.SUPER_ROLE_ID) {
				result.setCode(ReturnCodeEnum.FAIL.code());
				result.setMsg("删除失败,超级管理员不允许删除");
				return result;
			}
			// 用户-角色关联,不允许删除
			int count = adminRoleDao.ifRelUser(roleId);
			if (count > 0) {
				result.setCode(ReturnCodeEnum.FAIL.code());
				result.setMsg("删除失败,该角色被使用中");
				return result;
			}
		}
		
		// 角色-菜单关联,删除批量
		int count = adminRoleDao.roleMenuDel(roleIds);
		// 批量删除,角色
		count = adminRoleDao.roleDel(roleIds);
		
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台角色,更新
	 * @see com.xxl.service.IUserPermissionService#roleUpdate(int, java.lang.String, int)
	 */
	@Override
	public ReturnT<Integer> roleUpdate(int roleId, String name, int order) {
		ReturnT<Integer> result = new ReturnT<Integer>();
		if (StringUtils.isBlank(name)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("操作失败,角色名称为空");
			return result;
		}
		int count = adminRoleDao.roleUpdate(roleId, name, order);
		if (count < 1) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("操作失败");
			return result;
		}
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台角色-权限,查询
	 * @see com.xxl.service.IUserPermissionService#roleMenuQuery(int)
	 */
	@Override
	public String roleMenuQuery(int roleId) {
		List<AdminMenu> Allmenus = adminMenuDao.getAllMenus();
		List<AdminMenu> myMenus = adminMenuDao.getMyMenus(roleId);
		
		// 标志我的权限
		for (AdminMenu my : myMenus) {
			for (AdminMenu all : Allmenus) {
				if (my.getMenuId() == all.getMenuId()) {
					all.setChecked(true);
				}
			}
		}
		
		// 用户权限菜单, JSON tree (easyui tree格式)
		String result = MenuModuleHelper.initMenuModuleEasyJson(Allmenus);
		return result;
	}
	
	/*
	 * 后台角色-权限,更新
	 * @see com.xxl.service.IUserPermissionService#roleMenuUpdate(int, int[])
	 */
	@Override
	public ReturnT<String> roleMenuUpdate(int roleId, int[] menuIds) {
		
		if (roleId == AdminRoleMenuDic.SUPER_ROLE_ID) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "超级管理员不需要分配权限");
		}
		
		AdminRole role = adminRoleDao.getRoleById(roleId);
		if (role == null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "角色菜单更新失败,角色不存在");
		}
		
		// 旧依赖
		Set<Integer> oldMenudIds = new HashSet<Integer>();
		List<AdminMenu> oldMenus = adminMenuDao.getMyMenus(roleId);
		for (AdminMenu oldMenu : oldMenus) {
			oldMenudIds.add(oldMenu.getMenuId());
		}
		
		// 新依赖
		Set<Integer> newMenudIds = new HashSet<Integer>();
		if (menuIds != null) {
			for (int menuId : menuIds) {
				newMenudIds.add(menuId);
			}
		}
		
		// 批量操作ID  '''''::::::..... 
		Set<Integer> delMenudIds = new HashSet<Integer>(oldMenudIds);
		Set<Integer> addMenudIds = new HashSet<Integer>(newMenudIds);
		
		delMenudIds.removeAll(newMenudIds);
		addMenudIds.removeAll(oldMenudIds);
		
		// 批量操作
		if (CollectionUtils.isNotEmpty(delMenudIds)) {
			adminRoleDao.roleMenusDel(roleId, delMenudIds);
		}
		if (CollectionUtils.isNotEmpty(addMenudIds)) {
			adminRoleDao.roleMenusAdd(roleId, addMenudIds);
		}
		
		return new ReturnT<String>();
	}
	
	//-----------------------后台菜单相关-----------------------
	
	/*
	 * 后台菜单,查询
	 * @see com.xxl.service.IUserPermissionService#menuQuery()
	 */
	@Override
	public Map<String, Object> menuQuery() {
		List<AdminMenu> rowsData = adminMenuDao.getAllMenus();
		int totalNumber = rowsData!=null ? rowsData.size() : 0;
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rows", rowsData);  
		resultMap.put("total", totalNumber);
		return resultMap;
	}

	/*
	 * 后台菜单,新增
	 * @see com.xxl.service.IUserPermissionService#menuAdd(int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<Integer> menuAdd(int parentId, int order, String name, String url, int permessionNum) {
		// 校验父菜单
		if (parentId != AdminRoleMenuDic.MENU_MODULE_ID) {
			AdminMenu menu = adminMenuDao.getEffectParent(parentId, AdminRoleMenuDic.MENU_MODULE_ID);
			if (menu == null) {
				return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,菜单暂定位三级[模块-组-子项],子项不可最为父菜单");
			}
		}
		// 校验菜单名称
		if (StringUtils.isBlank(name)) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "新增失败,菜单名称为空");
		}
		
		int count = adminMenuDao.insert(parentId, order, name, url, permessionNum);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台菜单,删除
	 * @see com.xxl.service.IUserPermissionService#menuDel(int)
	 */
	@Override
	public ReturnT<Integer> menuDel(int menuId) {
		// 角色-菜单关联,不允许删除
		int count = adminMenuDao.ifRelRole(menuId);
		if (count > 0) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "删除失败,该菜单使用中");
		}
		
		// 存在子菜单,不允许删除
		List<AdminMenu> subList = adminMenuDao.getSubMenus(menuId);
		if (CollectionUtils.isNotEmpty(subList)) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "删除失败,存在子菜单依赖");
		}
		
		count = adminMenuDao.menuDel(menuId);
		return new ReturnT<Integer>(count);
	}

	/*
	 * 后台菜单,更新
	 * @see com.xxl.service.IUserPermissionService#menuUpdate(int, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<Integer> menuUpdate(int menuId, int parentId, int order,	String name, String url, int permessionNum) {
		// 校验父菜单
		if (parentId != AdminRoleMenuDic.MENU_MODULE_ID) {
			AdminMenu menu = adminMenuDao.getEffectParent(parentId, AdminRoleMenuDic.MENU_MODULE_ID);
			if (menu == null) {
				return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "更新失败,父菜单不合法");
			}
		}
		// 校验菜单名称
		if (StringUtils.isBlank(name)) {
			return new ReturnT<Integer>(ReturnCodeEnum.FAIL.code(), "更新失败,菜单名称为空");
		}
		
		int count = adminMenuDao.update(menuId, parentId, order, name, url, permessionNum);
		return new ReturnT<Integer>(count);
	}

}
