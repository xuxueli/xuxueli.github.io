package com.xxl.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.constant.CommonDic.HttpApplicationDic;
import com.xxl.core.model.main.AdminRole;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.JacksonUtil;
import com.xxl.core.util.ServletContextUtil;
import com.xxl.dao.IAdminRoleDao;
import com.xxl.service.IUserPermissionService;

/**
 * 权限相关
 * @author xuxueli
 */
@Controller
@RequestMapping("/userPermission")
public class UserPermissionController {
	
	@Autowired
	private IUserPermissionService userPermissionService;
	@Autowired
	private IAdminRoleDao adminRoleDao;
	
	//-----------------------后台用户相关-----------------------
	@RequestMapping("/userMain.do")
	@PermessionType(permessionNum = 1000100)
	public String userMain(ModelMap model) {
		
		// 构建角色搜索下拉框
		List<AdminRole> allRole = adminRoleDao.getAllRole();
		AdminRole role = new AdminRole();
		role.setRoleId(0);
		role.setName("全部");
		allRole.add(0, role);
		
		model.put("allRole", JacksonUtil.writeValueAsString(allRole));
		return "userPermission/userMain";
	}
	
	@RequestMapping("/userQuery.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000100)
	public Map<String, Object> userQuery(@RequestParam(required=false, defaultValue="0")int page,@RequestParam(required=false, defaultValue="0")int rows, String userName, int roleId) {
		Map<String, Object> resultMap = userPermissionService.userQuery(page, rows, userName, roleId);
		return resultMap;
	}
	
	@RequestMapping("/userAdd.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000100)
	public ReturnT<Integer> userAdd(String userName, String password) {
		return userPermissionService.userAdd(userName, password);
	}
	
	@RequestMapping("/userDel.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000100)
	public ReturnT<Integer> userDel(HttpSession session, @RequestParam("userIds[]") int[] userIds) {
		return userPermissionService.userDel(session, userIds);
	}
	
	@RequestMapping("/userUpdate.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000100)
	public ReturnT<Integer> userUpdate(int userId, String userName, String password) {
		return userPermissionService.userUpdate(userId, userName, password);
	}
	
	@RequestMapping("/userRoleQuery.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000100)
	public String userRoleQuery(int userId) {
		return userPermissionService.userRoleQuery(userId);
	}
	
	@RequestMapping("/userRoleUpdate.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000100)
	public ReturnT<Integer> userRoleUpdate(HttpSession session, int userId, @RequestParam("roleIds[]") int[] roleIds) {
		return userPermissionService.userRoleUpdate(session, userId, roleIds);
	}
	
	//-----------------------后台角色相关-----------------------
	@RequestMapping("/roleMain.do")
	@PermessionType(permessionNum = 1000200)
	public String roleMain() {
		return "userPermission/roleMain";
	}
	
	@RequestMapping("/roleQuery.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000200)
	public Map<String, Object> roleQuery() {
		Map<String, Object> resultMap = userPermissionService.roleQuery();
		return resultMap;
	}
	
	@RequestMapping("/roleAdd.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000200)
	public ReturnT<Integer> roleAdd(HttpSession session, String name, int order) {
		ServletContextUtil.remove(session.getServletContext(), HttpApplicationDic.LOGIN_ROLE_LIST);	// 移除,全局角色列表
		return userPermissionService.roleAdd(name, order);
	}
	
	@RequestMapping("/roleDel.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000200)
	public ReturnT<Integer> roleDel(HttpSession session, @RequestParam("roleIds[]") int[] roleIds) {
		ServletContextUtil.remove(session.getServletContext(), HttpApplicationDic.LOGIN_ROLE_LIST);	// 移除,全局角色列表
		return userPermissionService.roleDel(roleIds);
	}
	
	@RequestMapping("/roleUpdate.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000200)
	public ReturnT<Integer> roleUpdate(HttpSession session, int roleId, String name, int	order) {
		ServletContextUtil.remove(session.getServletContext(), HttpApplicationDic.LOGIN_ROLE_LIST);	// 移除,全局角色列表
		return userPermissionService.roleUpdate(roleId, name, order);
	}
	
	@RequestMapping("/roleMenuQuery.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000200)
	public String roleMenuQuery(int roleId) {
		return userPermissionService.roleMenuQuery(roleId);
	}
	
	@RequestMapping("/roleMenuUpdate.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000200)
	public ReturnT<String> roleMenuUpdate(int roleId, @RequestParam(value="menuIds[]") int[] menuIds) {
		return userPermissionService.roleMenuUpdate(roleId, menuIds);
	}
	
	//-----------------------后台菜单相关-----------------------
	@RequestMapping("/menuMain.do")
	@PermessionType(permessionNum = 1000300)
	public String menuMain() {
		return "userPermission/menuMain";
	}
	
	@RequestMapping("/menuQuery.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000300)
	public Map<String, Object> menuQuery() {
		Map<String, Object> resultMap = userPermissionService.menuQuery();
		return resultMap;
	}
	
	@RequestMapping("/menuAdd.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000300)
	public ReturnT<Integer> menuAdd(int parentId, int order, String name, String url, int permessionNum) {
		return userPermissionService.menuAdd(parentId, order, name, url, permessionNum);
	}
	
	@RequestMapping("/menuDel.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000300)
	public ReturnT<Integer> menuDel(int menuId) {
		return userPermissionService.menuDel(menuId);
	}
	
	@RequestMapping("/menuUpdate.do")
	@ResponseBody
	@PermessionType(permessionNum = 1000300)
	public ReturnT<Integer> menuUpdate(int menuId, int parentId, int order, String name, String url, int permessionNum) {
		return userPermissionService.menuUpdate(menuId, parentId, order, name, url, permessionNum);
	}
	
}
