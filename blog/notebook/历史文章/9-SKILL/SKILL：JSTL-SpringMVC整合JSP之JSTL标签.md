### maven依赖
```
<!-- jstl：C标签 -->
<dependency>
	<groupId>jstl</groupId>
	<artifactId>jstl</artifactId>
	<version>1.2</version>
</dependency>
<dependency>
	<groupId>taglibs</groupId>
	<artifactId>standard</artifactId>
	<version>1.1.2</version>
</dependency>
```

### SpringMVC配置JSP模板（springmvc-context.xml）
```
<bean id="viewResolver" 
		class="org.springframework.web.servlet.view.InternalResourceViewResolver"
        p:prefix="/WEB-INF/template/"
        p:suffix=".jsp" />
```

### Controller层代码
```
@Autowired
private IUserService userService;

@RequestMapping("index")
public String index(Model model){
	
	List<User> list = userService.list();
	model.addAttribute("list", list);
	return "index";
}
```

### JSP模板（index.jsp）
```
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach items="${list}" var="user" varStatus="status" >
		用户ID：${user.userId} --
		用户名：${user.username} --
		密码：${user.password} --
		index：${status.index} --
		<hr>
	</c:forEach> 
</body>
</html>
```



