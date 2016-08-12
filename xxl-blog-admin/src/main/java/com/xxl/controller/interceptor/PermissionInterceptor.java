package com.xxl.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.code.kaptcha.Constants;
import com.xxl.controller.annotation.PermessionType;
import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.constant.CommonDic.HttpSessionKeyDic;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.exception.WebException;
import com.xxl.core.util.HttpSessionUtil;

/**
 * “登陆+权限”拦截器
 * @author xuxueli
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	//private static transient Logger logger = LoggerFactory.getLogger(LoginPermissionInterceptor.class);
	
	/*
	 * Controller“执行时异步”“执行
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	/*
	 * Controller“执行后”执行
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	/*
	 * Controller“执行前”执行
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		
		// 请求非法
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		
		// 获取“权限注解”
		HandlerMethod method = (HandlerMethod)handler;
		PermessionType permission = method.getMethodAnnotation(PermessionType.class);
		if (permission == null) {
			throw new WebException(ReturnCodeEnum.FAIL.code(), "权限受限");
		}
		boolean loginState = permission.loginState();
		int permessionNum = permission.permessionNum();
		boolean randCode = permission.randCode();
		
		LoginIdentity identity = (LoginIdentity) HttpSessionUtil.get(session, HttpSessionKeyDic.LOGIN_IDENTITY);
		// 01：登陆拦截
		if (loginState) {
			if (identity == null) {
				//response.sendRedirect(request.getContextPath() + "/");
				//return false;
				throw new WebException(ReturnCodeEnum.FAIL.code(), "登录失效,请重新登录");
			}
		}
		// 02：权限拦截器
		if (permessionNum > 0) {
			if (identity == null || identity.getMenePermissionNums() == null || 
					!identity.getMenePermissionNums().contains(String.valueOf(permessionNum))) {
				throw new WebException(ReturnCodeEnum.FAIL.code(), "权限受限");
			}
		}
		
		// 03：随机验证码拦截
		if (randCode) {
			String randCodeCache = (String) HttpSessionUtil.get(session, Constants.KAPTCHA_SESSION_KEY);
			String randCodeParam = request.getParameter("randCode");
			if (StringUtils.isBlank(randCodeCache) || !StringUtils.equals(randCodeCache, randCodeParam)) {
				throw new WebException(ReturnCodeEnum.FAIL.code(), "验证码错误");
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
