package com.xxl.controller.interceptor;

import com.xxl.controller.annotation.PermessionLimit;
import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.exception.WebException;
import com.xxl.core.result.ReturnT;
import com.xxl.service.helper.LoginIdentityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * “登陆+权限”拦截器
 * @author xuxueli
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	private static transient Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();

		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		
		// need login
		boolean loginState = true;
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		loginState = permission!=null?permission.needLogin():false;

		// 01：登陆拦截
		if (loginState) {
			LoginIdentity identity = LoginIdentityHelper.cacheCheck(request, session);
			if (identity == null) {
				//response.sendRedirect(request.getContextPath() + "/loginCheck");
				//return false;
				throw new WebException(ReturnT.FAIL, "登录失效,请重新登录");
			}
		}

		return super.preHandle(request, response, handler);
	}
	
}
