package com.xxl.controller.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.xxl.core.constant.CommonDic.CommonViewName;
import com.xxl.core.exception.WebException;
import com.xxl.core.result.ReturnT;
import com.xxl.core.util.JacksonUtil;

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
		
		// 异常信息
		if (ex instanceof WebException) {
			String exceptionKey = ((WebException) ex).getExceptionKey();
			String exceptionMsg = ((WebException) ex).getExceptionMsg();
			
			// 是否JSON返回
			HandlerMethod method = (HandlerMethod)handler;
			ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
			if (responseBody != null) {
				mv.addObject("result", JacksonUtil.writeValueAsString(new ReturnT<String>(exceptionKey, exceptionMsg)));
				mv.setViewName(CommonViewName.COMMON_RESULT);
			} else {
				mv.addObject("exceptionMsg", exceptionMsg);	
				mv.setViewName(CommonViewName.COMMON_EXCEPTION);
			}
			
		} else {
			mv.addObject("exceptionMsg", ex.toString().replaceAll("\n", "<br/>"));	
			mv.setViewName(CommonViewName.COMMON_EXCEPTION);
			
			logger.info("==============异常开始=============");
			logger.info("system exceptionMsg:{}", ex);
			logger.info("==============异常结束=============");
		}
		
		return mv;
	}

	
}
