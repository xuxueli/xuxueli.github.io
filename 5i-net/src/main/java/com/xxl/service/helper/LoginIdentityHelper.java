package com.xxl.service.helper;

import javax.servlet.http.HttpSession;

import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.constant.CommonDic.HttpSessionKeyDic;
import com.xxl.core.model.main.UserMain;
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
	private static void fillin(LoginIdentity identity, UserMain userMain){
		
		identity.setUserId(userMain.getUserId());
		identity.setUserName(userMain.getUserName());
		identity.setPassword(userMain.getPassword());
		identity.setUserToken(userMain.getUserToken());
		identity.setRealName(userMain.getRealName());
	}

	/**
	 * “用户登陆信息”-初始化
	 * @param user
	 */
	public static void login(HttpSession session, UserMain userMain) {
		LoginIdentity identity = new LoginIdentity();
		LoginIdentityHelper.fillin(identity, userMain);
		HttpSessionUtil.set(session, HttpSessionKeyDic.LOGIN_IDENTITY, identity);
	}

	/**
	 * “用户登陆信息”-注销
	 * @param session
	 */
	public static void logout(HttpSession session) {
		HttpSessionUtil.remove(session, HttpSessionKeyDic.LOGIN_IDENTITY);
	}
}
