### 封装ReturnT
> 简介：项目底层，封装ReturnT（code、msg、content为泛型），逻辑流程中返回“封装ReturnT”，controller中根据返回值

优点：
- 1：设计角度，符合人们日常习惯；
- 2：封装ReturnT，灵活简单；

缺点：
- 调用方需要校验ReturnT状态，比较繁琐；

##### ReturnT源码
```
package com.xxl.util.core.skill.flowcontrol;

/**
 * common return
 * @author xuxueli 2015-12-4 16:32:31
 * @param <T>
 */
public class ReturnT<T> {
	public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
	public static final ReturnT<String> FAIL = new ReturnT<String>(500, null);
	
	private int code;
	private String msg;
	private T content;
	
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(T content) {
		this.code = 200;
		this.content = content;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
	}

}

```

##### SpringMVC使用ReturnT

```
@RequestMapping(value="/flowControl")
@ResponseBody
public ReturnT<String> flowControl() {
	if (new Random().nextBoolean()) {
		throw new WebException("");
	}
	return new ReturnT<String>("流程控制");
}
```

### 自定义异常WebException：
> 简介：项目底层“异常机制”统一catch进行“自定义异常”处理，逻辑流程中抛出“自定义异常”，然后跳转统一error页或返回统一json返回值（WebException转json），这样在controller中不用写一堆的try/catch，甚至不需要定义和处理返回值，灵活方便；

优点：
- 调用方不需要校验ReturnT状态，使用简单；
- 方便进行事物异常回滚；

缺点：
- 1：设计角度来说，稍微不太优雅；
- 2：异常比常规流程控制，耗费资源，稍微多一些；

##### WebException源码
```
package com.xxl.util.core.skill.flowcontrol;

/**
 * 自定义异常
 * @author xuxueli 2014-5-7 22:19:54
 */
public class WebException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String msg;
	
	public WebException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public WebException(String msg) {
		super(msg);
		this.code = 500;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
```

##### 异常解析器
```
package com.xxl.util.core.skill.flowcontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.xxl.util.core.util.JacksonUtil;

/**
 * 异常解析器
 * @author xuxueli 2016-7-7 22:33:00
 */
public class WebExceptionResolver implements HandlerExceptionResolver {
	private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("common.result");
		
		if (ex instanceof WebException) {
			// 是否JSON返回
			HandlerMethod method = (HandlerMethod)handler;
			ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
			if (responseBody != null) {
				mv.addObject("json", true);
				mv.addObject("data", JacksonUtil.writeValueAsString(new ReturnT<String>(((WebException) ex).getCode(), ((WebException) ex).getMsg())));
			} else {
				mv.addObject("data", ((WebException) ex).getMsg());	
			}
			
		} else {
			mv.addObject("data", ex.toString().replaceAll("\n", "<br/>"));	
			mv.setViewName("common.result");
			
			logger.info("==============异常开始=============");
			logger.info("system exceptionMsg:{}", ex);
			logger.info("==============异常结束=============");
		}
		return mv;
	}

	
}
```

##### spring mvc配置中，配置异常解析器
```
<bean id="exceptionResolver" class="com.xxl.util.core.skill.flowcontrol.WebExceptionResolver" />
```



