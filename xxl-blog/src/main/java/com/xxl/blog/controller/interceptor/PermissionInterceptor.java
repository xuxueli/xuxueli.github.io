package com.xxl.blog.controller.interceptor;

import com.xxl.blog.controller.annotation.PermessionLimit;
import com.xxl.blog.core.exception.WebException;
import com.xxl.blog.core.model.User;
import com.xxl.blog.core.util.HttpSessionUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截, 简易版
 * @author xuxueli 2015-12-12 18:09:04
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

        HandlerMethod method = (HandlerMethod)handler;
        PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
        if (permission != null) {
            User user = (User) HttpSessionUtil.get(request.getSession(), "login_user");
            if (user == null) {
                throw new WebException("登录失效");
            }
        }

		return super.preHandle(request, response, handler);
	}
	
}
