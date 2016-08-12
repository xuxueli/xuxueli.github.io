package com.xxl.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.xxl.dao.IAdminMenuDao;
import com.xxl.dao.IAdminRoleDao;
import com.xxl.dao.IAdminUserDao;
import com.xxl.service.ILoginService;
import com.xxl.service.helper.LoginIdentityHelper;

/**
 * 后台用户
 * @author xuxueli
 */
@Service
public class LoginServiceImpl implements ILoginService {
	private static transient Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private IAdminUserDao adminUserDao;
	@Autowired
	private IAdminRoleDao adminRoleDao;
	@Autowired
	private IAdminMenuDao adminMenuDao;
	
	/*
	 * 登陆
	 * @see com.xxl.service.IAdminUserService#loginDo(javax.servlet.http.HttpSession, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<String> login(HttpSession session, String username, String password, int roleId) {
		ReturnT<String> result= new ReturnT<String>();
		
		// 参数校验
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("用户名或密码为空");
			return result;
		}
		
		// 用户校验--账号密码校验
		AdminUser user = adminUserDao.getByPwd(username, password);
		if (user == null) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("用户名或密码错误");
			return result;
		}
		
		// 角色校验
		AdminRole role = adminRoleDao.getRole(user.getUserId(), roleId);
		if (role == null) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("对不起,角色权限不足");
			return result;
		}
		
		// 菜单校验
		List<AdminMenu> menus = null;
		if (role.getRoleId() == AdminRoleMenuDic.SUPER_ROLE_ID) {
			menus = adminMenuDao.getAllMenus();
		} else {
			menus = adminMenuDao.getMyMenus(role.getRoleId());
		}
		
		if (CollectionUtils.isEmpty(menus)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("对不起,菜单权限不足");
			return result;
		}
		
		// “用户登陆信息” + “用户权限信息” -- 初始化
		LoginIdentityHelper.login(session, user, menus);
				
		logger.info("后台登录成功：username:{}", new Object[]{username});
		return result;
	}
	
	/*
	 * 注销登陆
	 * @see com.xxl.service.IAdminUserService#logout(javax.servlet.http.HttpSession)
	 */
	@Override
	public ReturnT<String> logout(HttpSession session) {
		ReturnT<String> result= new ReturnT<String>();
		
		// “用户登陆信息” + “用户权限信息” -- 注销
		LoginIdentityHelper.logout(session);
		
		return result;
	}

	/*
	 * 修改密码
	 * @see com.xxl.service.IAdminUserService#modifyPwd(javax.servlet.http.HttpSession, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<String> modifyPwd(HttpSession session, String password, String newPwd, String reNewPwd) {
		ReturnT<String> result= new ReturnT<String>();
		
		// 旧密码校验
		if (StringUtils.isBlank(password) || password.trim().length() < 6 || password.trim().length() > 16) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("请正确输入旧密码");
			return result;
		}
		
		LoginIdentity identity = (LoginIdentity) HttpSessionUtil.get(session, HttpSessionKeyDic.LOGIN_IDENTITY);
		if (!password.equals(identity.getPassword())) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("您输入的旧密码错误");
			return result;
		}
		
		// 新密码校验
		if (StringUtils.isBlank(newPwd) || newPwd.trim().length() < 6 || newPwd.trim().length() > 16) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("新密码格式不正确[6-16位]");
			return result;
		}
		
		if (!newPwd.equals(reNewPwd)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("新密码和确认密码不一致");
			return result;
		}
		
		if (password.equals(newPwd)) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("新密码不允许和旧密码相同");
			return result;
		}
		
		// 修改密码
		int count = adminUserDao.modifyPwd(identity.getUserName(), password, newPwd);
		if (count < 1) {
			result.setCode(ReturnCodeEnum.FAIL.code());
			result.setMsg("密码修改失败，请稍后重试");
			return result;
		}
		
		// “用户登陆信息”-注销
		LoginIdentityHelper.logout(session);
		
		logger.info("后台修改密码成功：username:{}", new Object[]{identity.getUserName()});
		return result;
	}

}
