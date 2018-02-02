### Apache CXF简介
Apache CXF 是一个开源的 Services 框架，CXF 帮助您利用 Frontend 编程 API 来构建和开发 Services ，像 JAX-WS 。这些 Services 可以支持多种协议，比如：SOAP、XML/HTTP、RESTful HTTP 或者 CORBA ，并且可以在多种传输协议上运行，比如：HTTP、JMS 或者 JBI，CXF 大大简化了 Services 的创建，同时它继承了 XFire 传统，一样可以天然地和 Spring 进行无缝集成。

**[WebService-李刚](http://www.ibeifeng.com/down.php?id=36799)**

[Apache CXF 简介](http://www.ibm.com/developerworks/cn/education/java/j-cxf/)

### 使用步骤：
##### 1、maven依赖
```
<!-- apache-cxf start -->
<dependency>
	<groupId>org.apache.cxf</groupId>
	<artifactId>cxf-rt-frontend-jaxws</artifactId>
	<version>2.7.15</version>
</dependency>
<dependency>
	<groupId>org.apache.cxf</groupId>
	<artifactId>cxf-rt-transports-http</artifactId>
	<version>2.7.15</version>
</dependency>
<!-- apache-cxf stop -->
```

##### 2、服务端开发

接口，接口实现，开发
```
// 接口开发
package com.xxl.service;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface IHelloWorld {
    //@WebParam(name="arg0")可有可无，为了增强可读性
    public String sayHello(@WebParam(name = "arg0") String text);
}  

// 接口实现，开发
package com.xxl.service.impl;
import com.xxl.service.IHelloWorld;
import javax.jws.WebService;
  
@WebService(endpointInterface="com.xxl.service.IHelloWorld")
public class HelloWorldImpl implements IHelloWorld {

    public String sayHello(String text) {     
        return "Hello" + text ;  
    }

}  
```

“web.xml” 配置cxf的servlet入口
```
<servlet>
	<servlet-name>CXFServlet</servlet-name>
	<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>CXFServlet</servlet-name>
	<url-pattern>/cxf/*</url-pattern>
</servlet-mapping>
```

配置Provider的Spring配置“applicationcontext-cxf-provider.xml”
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jaxws="http://cxf.apache.org/jaxws"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.0.xsd
		http://cxf.apache.org/jaxws
		http://cxf.apache.org/schemas/jaxws.xsd">

	<!-- 在cxf-xxx.jar包里面 -->
	<import resource="classpath:META-INF/cxf/cxf.xml" />
	<import resource="classpath:META-INF/cxf/cxf-servlet.xml" />

	<!--
		implementor支持两种方式加载实现类: "#Bean名称" ; "实现类的包名地址"
	-->
	<bean id="hello" class="com.xxl.service.impl.HelloWorldImpl"/>

	<jaxws:endpoint id="helloWorld" address="/HelloWorld" implementor="#hello"  />
	<!--<jaxws:endpoint id="helloWorld" address="/HelloWorld" implementor="com.xxl.service.impl.HelloWorldImpl" />-->

</beans>
```

至此，服务端开发完成，访问以下链接，可以查看所有可提供服务的CXF实现的WebService列表：http://localhost:8080/cxf

    ：可以查看SOPA、RESTful两种WebService可用服务列表：
    1、Available SOAP services:
    2、Available RESTful services:

##### 3、客户端开发

配置Provider的Spring配置“applicationcontext-cxf-consumer.xml”（前提是接口代码已经共享）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:context="http://www.springframework.org/schema/context"
	   xmlns:jaxws="http://cxf.apache.org/jaxws"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.0.xsd
		http://cxf.apache.org/jaxws
		http://cxf.apache.org/schemas/jaxws.xsd">


	<!-- 在cxf-xxx.jar包里面 -->
	<import resource="classpath:META-INF/cxf/cxf.xml" />
	<import resource="classpath:META-INF/cxf/cxf-servlet.xml" />

	<jaxws:client id="remoteHelloService" serviceClass="com.xxl.service.IHelloWorld"
		address="http://localhost:8080/cxf/HelloWorld" />

</beans>
```

CXF远程服务注入到Controller中使用
```
@Resource
private IHelloWorld remoteHelloService;

@RequestMapping("/test")
@ResponseBody
private String index(String name) {
	return remoteHelloService.sayHello(name);
}
```

测试：访问以下地址可进行测试，内部计算逻辑已经走cxf的WebService方式：http://localhost:8080/test?name=jack

##### cxf客户端调用方式汇总
- A、可以获取服务器段代码； 200次/2s
    - 1、cxf + spring：jaxws:client方式
    - 2、JaxWsProxyFactoryBean方式
- B、动态模式 200次/28s
    - 3、JaxWsDynamicClientFactory
- C、wsdl2java生成客户端代码
    - 4、cxf之wsdl2java（**Service.get***Port()方式）

##### CXF之wsdl2java生成调用webservice的客户端
- 1、Client 开发者拥有Web服务端的 class：能拿到服务端的接口 Class 和 Entity 类及 aegis 配置文件 ；

- 2、根据 WSDL 生成 Client Stub；
    - 首先当前是从官网下载cxf组件：http://cxf.apache.org/download.html；
    - 下载后解压，在这里主要是用到解压后的bin目录中的wsdl2java.bat该批处理文件；

```
// 直接运行的以下命令：
wsdl2java -p com.xxl.service -d d:\cxfoutput\src -all http://localhost:8080/cxf/HelloWorld?wsdl

// 参数说明:
-p 也就是package 对应java中的包
-d 输入目录,生成.java文件会在该目录,会自动添加-p参数配置的包路径
-client 生成客户端测试web service的代码.
-server 生成服务器启动web service的代码.
-impl 生成web service的实现代码.
-ant 生成build.xml文件.
-all 生成上面-client -server -impl -ant 对应的所有文件.
```
