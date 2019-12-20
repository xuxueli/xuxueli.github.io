- [Spring 系列: Spring 框架简介](https://www.ibm.com/developerworks/cn/java/wa-spring1/)
- [@RequestParam @RequestBody @PathVariable 等参数绑定注解详解](http://blog.csdn.net/walkerjong/article/details/7946109)
- [Spring MVC 教程,快速入门,深入分析](http://elf8848.iteye.com/blog/875830)
- [spring 3.0 应用springmvc 构造RESTful URL 详细讲解](http://badqiu.iteye.com/blog/473301)
- [SpringMVC中使用Interceptor拦截器](http://haohaoxuexi.iteye.com/blog/1750680)
- [SpringMVC视图解析器](http://haohaoxuexi.iteye.com/blog/1770554)
- [SpringMVC视频教程-李守宏](http://wenku.baidu.com/view/d36df46665ce0508763213e0.html)
- [属性编译器-Spring表单提交日期类型绑定](http://gaobusi.iteye.com/blog/1210701)
- [属性编译器-使用 Spring 2.5 基于注解驱动的 Spring MVC-清单13](http://www.ibm.com/developerworks/cn/java/j-lo-spring25-mvc/)
- [原理，SpringMVC对比Struts2](http://blog.csdn.net/liou825/article/details/24422113)
-  [SpringMVC中的文件上传](http://www.iqiyi.com/lvyou/xfunchjlb.html)


##### restful风格，spring mvc不匹配html后缀文件：
- 场景：项目web.xml配置spring mvc为<url-pattern>/</url-pattern>；整站静态化首页为index.html;
- 问题：通过域名访问：http://localhost:8080/ 不会跳转到index.html页面，而是跳转到404；
- 原因：  
    - <url-pattern>*.do</url-pattern> ：后缀型url匹配；
    - <url-pattern>/</url-pattern> ：路径型url匹配；不会主动匹配到后缀请求；
    - <url-pattern>/*</url-pattern>          ：路径型 + 后缀型的url匹配 ( 包括/login, *.jsp, *.js和 *.html等 )；

##### 前端样式，html过滤
```
<mvc:resources mapping="/favicon.ico" location="/favicon.ico" />
<!-- 不拦截static目录下 -->
<mvc:resources mapping="/static/**" location="/static/" />	
<!-- 不拦截.html后缀 -->
<mvc:resources mapping="/**/*.html" location="/" />	
```

##### 解决@ResponseBody中文乱码
```
@RequestMapping(value = "/hello2")
@ResponseBody
public String hello2(Model model) {
    return "ResponseBody 直接返回《中文String字符串》，乱码";
}
 
@RequestMapping(value = "/hello3")
@ResponseBody
public ReturnT<String> hello3(Model model) {
    return new ReturnT<String>("@ResponseBody 传递中文乱码，解决方案A：不可使用《中文String字符串》返回值，使用《自定义封装对象》");
}
 
@RequestMapping(value = "/hello4")
public String hello4(Model model) {
    model.addAttribute("resp", "@ResponseBody 传递中文乱码，解决方案A：不可使用《中文String字符串》返回值，使用《freemarker接收传参》");
    return "comm.result";
}
```

##### Spring4的Restful配置
```
<mvc:annotation-driven />
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
	<property name="messageConverters">
		<list >
			<bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
				<property name="supportedMediaTypes">
					<list>
						<value>text/html;charset=UTF-8</value>
					</list>
				</property>
			</bean>
		</list>
	</property>
</bean>
```

##### @RestController	
从Spring4开始支持，等价于Controller类上面的注解： @RequestMapping + @ResponseBody

##### spring mvc 和 struts2 区别
    1. 机制。spring mvc 的入口是serclet， 而struts是filter（这里要指出，filter和servlet是不同的。以前认为filter是servlet的一种特殊），这样就导致了二者的机制不同，
    这里就牵涉到servlet和filter的区别了，我其他博客 会专门写一写 servlet和filter之间的区别。
    2.性能。 spring会稍微比struts快。spring mvc 是基于方法的设计，而sturts是基于类，每次发一次请求都会实例一個 action ，每个action都会被注入属性， 而spring基于方法，粒度更细，但要小心把握像在servlet控制数据一样。
    3.参数传递。 struts是在接受参数的时候，可以用属性来接受参数， 这就说明参数是让多个方法共享的。
    4.设计思想上。struts 更加符合oop的编程思想， spring就比较谨慎，在servlet上扩展，
    5.intercepter的实现机制。struts有以自己的interceptor机制，spring mvc 用的是独立的AOP方式。这样导致struts的配置文件量还是比spring mvc大，虽然struts的配置能继承，所以我觉得论使用上来讲，spring mvc使用更加简洁。