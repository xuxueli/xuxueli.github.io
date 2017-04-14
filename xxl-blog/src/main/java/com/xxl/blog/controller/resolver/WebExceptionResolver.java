package com.xxl.blog.controller.resolver;

import com.xxl.blog.core.util.JacksonUtil;
import com.xxl.blog.core.exception.WebException;
import com.xxl.blog.core.result.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常解析器
 * 
 * @author xuxueli
 */
public class WebExceptionResolver implements HandlerExceptionResolver {
	private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();
		
		// 异常封装
		ReturnT<String> result = new ReturnT<String>(null);
		if (ex instanceof WebException) {
			result = new ReturnT<String>(((WebException) ex).getCode(), ((WebException) ex).getMsg());
		} else {
			result = new ReturnT<String>(ReturnT.FAIL_CODE, ex.toString().replaceAll("\n", "<br/>"));

			logger.info("==============异常开始=============");
			logger.info("system catch exception:{}", ex);
			logger.info("==============异常结束=============");
		}
				
		// 是否JSON返回
		HandlerMethod method = (HandlerMethod)handler;
		ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
		if (responseBody != null) {
			response.setContentType("application/json;charset=UTF-8");
			mv.addObject("result", JacksonUtil.writeValueAsString(result));
			mv.setViewName("common/common.result.body");
		} else {
			mv.addObject("exceptionMsg", result.getMsg());	
			mv.setViewName("common/common.result.exception");
		}
		
		return mv;
	}

	
}
