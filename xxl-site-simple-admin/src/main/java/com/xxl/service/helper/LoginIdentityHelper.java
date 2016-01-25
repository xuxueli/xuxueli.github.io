package com.xxl.service.helper;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.constant.CommonDic.HttpSessionKeyDic;
import com.xxl.core.model.main.AdminMenu;
import com.xxl.core.model.main.AdminUser;
import com.xxl.core.util.HttpSessionUtil;

/**
 * 用户登陆信息，操作相关
 * @author Administrator
 *
 */
public class LoginIdentityHelper {
	
	/**
	 * 填充“用户登录信息”
	 * @param identity
	 * @param user
	 */
	private static void fillin(LoginIdentity identity, AdminUser user, List<AdminMenu> menus){
		//BeanUtils.copyProperties(identity, user);
		
		identity.setUserId(user.getUserId());
		identity.setUserName(user.getUserName());
		identity.setPassword(user.getPassword());
		identity.setUserToken(user.getUserToken());
		identity.setRealName(user.getRealName());
		
		// 权限菜单
		identity.setMenePermissionNums(MenuModuleHelper.initMenePermissionNums(menus));
		identity.setMenuModuleJson(MenuModuleHelper.initMenuModuleJson(menus));
	}

	/**
	 * “用户登陆信息”-初始化
	 * @param user
	 */
	public static void login(HttpSession session, AdminUser user, List<AdminMenu> menus) {
		// 初始化用户登陆信息
		LoginIdentity identity = new LoginIdentity();
		LoginIdentityHelper.fillin(identity, user, menus);
		HttpSessionUtil.set(session, HttpSessionKeyDic.LOGIN_IDENTITY, identity);
	}

	/**
	 * “用户登陆信息”-注销
	 * @param session
	 */
	public static void logout(HttpSession session) {
		// SESSION移除 “用户登陆信息”
		HttpSessionUtil.remove(session, HttpSessionKeyDic.LOGIN_IDENTITY);
	}
}
