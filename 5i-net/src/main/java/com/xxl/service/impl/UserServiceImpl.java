package com.xxl.service.impl;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.constant.CommonDic.EmailStatus;
import com.xxl.core.constant.CommonDic.EmailType;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.constant.CommonDic.UserState;
import com.xxl.core.model.main.EmailSendInfo;
import com.xxl.core.model.main.UserMain;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.Md5Util;
import com.xxl.core.util.RandomUtil;
import com.xxl.dao.IEmailSendInfoDao;
import com.xxl.dao.IUserMainDao;
import com.xxl.service.IUserService;
import com.xxl.service.helper.LoginIdentityHelper;

/**
 * 用户信息
 * @author xuxueli
 */
@Service()
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserMainDao userMainDao;
	@Autowired
	private IEmailSendInfoDao emailSendInfoDao;
	
	/*
	 * 注册
	 * @see com.xxl.service.IUserService#reg(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<String> reg(String email, String password, String rePassword) {
		// 参数校验
		if (StringUtils.isBlank(email)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "注册邮箱不可为空");
		}
		if (StringUtils.isBlank(password)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "密码不可为空");
		}
		if (password.length() < 6 || password.length() > 18) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "密码长度不合法[6-18位]");
		}
		if (!password.equals(rePassword)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "密码和确认密码输入不一致");
		}
		
		// 邮箱唯一性校验
		UserMain userMain = userMainDao.getByEmail(email);
		if (userMain != null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "该邮箱已被使用,请更换邮箱重新注册");
		}
		
		// 保存用户信息
		userMainDao.insert(email, Md5Util.encrypt(password), UserState.INACTIVE.state());
		userMain = userMainDao.getByEmail(email);
		
		// 保存注册邮件
		EmailSendInfo emailInfo = new EmailSendInfo();
		emailInfo.setUserId(userMain.getUserId());
		emailInfo.setEmailType(EmailType.EMAIL_ACTIVATE.type());
		emailInfo.setEmailStatus(EmailStatus.UN_SEND.value());
		emailInfo.setSendCode(RandomUtil.randLowerChars(6));
		emailSendInfoDao.insert(emailInfo);
		
		return new ReturnT<String>();
	}

	/*
	 * 登陆
	 * @see com.xxl.service.IUserService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password) {
		if (StringUtils.isBlank(email)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "登陆失败,请输入账号邮箱");
		}
		if (StringUtils.isBlank(password)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "登陆失败,请输入密码");
		}
		UserMain userMain = userMainDao.getByEmailPwd(email, Md5Util.encrypt(password));
		if (userMain == null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "登陆失败,账号邮箱或密码错误");
		}
		
		LoginIdentityHelper.login(response, session, userMain);
		return new ReturnT<String>();
	}

	/*
	 * 邮箱激活
	 * @see com.xxl.service.IUserService#emailActivate(java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnT<String> emailActivate(String email, String sendCode) {
		// 参数校验
		if (StringUtils.isBlank(email)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "请输入邮箱");
		}
		if (StringUtils.isBlank(sendCode)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "请输入激活码");
		}
		// 邮箱校验
		UserMain userMain = userMainDao.getByEmail(email);
		if (userMain == null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "邮箱账号不存在");
		}
		if (userMain.getState() == UserState.NORMAL.state()) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "账号已激活");
		}
		// 邮件,激活码校验
		EmailSendInfo emailInfo = emailSendInfoDao.getEmail(userMain.getUserId(), EmailType.EMAIL_ACTIVATE.type());
		if (emailInfo == null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "激活邮件不存在或已过期");
		}
		if (!StringUtils.equals(emailInfo.getSendCode(), sendCode)) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "激活码错误");
		}
		// 激活
		int ret = userMainDao.updateState(userMain.getUserId(), UserState.NORMAL.state());
		if (ret < 1) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "激活失败");
		}
		return new ReturnT<String>();
	}
	
}
