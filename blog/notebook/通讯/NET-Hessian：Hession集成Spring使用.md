### Hessian
Hessian是一个轻量级的remoting onhttp工具，使用简单的方法提供了RMI的功能。 相比WebService，Hessian更简单、快捷。采用的是二进制RPC（Binary-RPC）协议，因为采用的是二进制协议，所以它很适合于发送二进制数据。

原理简单描述：动态代理 + 序列化 + http；

特点：
- 1、角色划分为：provider、consumer；
- 2、consumer需要指明provider的http服务地址，地址维护比较繁琐；
- 3、provider地址通过域名路由到nginx，可以实现provider的负载均衡，但是会导致“1+1（nginx+tomcat，而dubbo是tcp直连的，就不会出现该问题）”问题，产生单点压力，放大流量；


Binary-RPC 是一种和 RMI 类似的远程调用的协议，它和 RMI 的不同之处在于它以标准的二进制格式来定义请求的信息 ( 请求的对象、方法、参数等 ) ，这样的好处是什么呢，就是在跨语言通讯的时候也可以使用。
来看下 Binary -RPC 协议的一次远程通信过程：
- 1 、客户端发起请求，按照 Binary -RPC 协议将请求信息进行填充；
- 2 、填充完毕后将二进制格式文件转化为流，通过传输协议进行传输；
- 3 、接收到在接收到流后转换为二进制格式文件，按照 Binary -RPC 协议获取请求的信息并进行处理；
- 4 、处理完毕后将结果按照 Binary -RPC 协议写入二进制格式文件中并返回。

### 使用步骤
##### 1、maven依赖
```
<!-- hessian -->
<dependency>
	<groupId>com.caucho</groupId>
	<artifactId>hessian</artifactId>
	<version>4.0.7</version>
</dependency>
```

##### 2、公共api：接口和实体开发
```
// 接口文件
package com.xxl.core.hessian.service;
import com.xxl.core.hessian.model.User;

public interface ITestHessionServerService {
	
	public User queryUser(String name); 
	
}


// 数据传输实体（需要序列化）
package com.xxl.core.hessian.model;

import java.io.Serializable;

public class User implements Serializable{
	private String name;
	private int age;
	
	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + "]";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
}
```

##### 3、Server端：接口实现类开发，hessian接口配置

接口实现类开发
```
package com.xxl.core.hessian.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xxl.core.hessian.model.User;
import com.xxl.core.hessian.service.ITestHessionServerService;

@Service("testHessionServerService")
public class TestHessionServerServiceImpl implements ITestHessionServerService {
	
	private transient static Logger logger = LoggerFactory.getLogger(TestHessionServerServiceImpl.class);
	
	public User queryUser(String name) {
		if (name != null) {
			if ("jack".equals(name)) {
				return new User(name, 21);
			} else if ("lucy".equals(name)) {
				return new User(name, 20);
			}
		}
		logger.info("query fail, no user name:{}", name);
		return null;
	}

}
```

接口实现类，扫描为Spring的Bean
```
<context:component-scan base-package="com.xxl.core.hessian.service" />
```

“web.xml”配置hessian接口
```
<!-- hessian -->
<servlet>
	<servlet-name>hessian</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
	<servlet-name>hessian</servlet-name>
	<url-pattern>/hessian/*</url-pattern>
</servlet-mapping>
```

“web.xml”同目录下，“hessian-servlet.xml”配置
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-3.0.xsd">

	<bean name="/testHessionServer" class="org.springframework.remoting.caucho.HessianServiceExporter">
		<property name="serviceInterface" value="com.xxl.core.hessian.service.ITestHessionServerService" />
		<property name="service" ref="testHessionServerService" />
	</bean>

</beans>
```

##### 4、Client端：配置hessian接口client

配置hessian地址文件 “hessian.properties”
```
hessian.server=http://localhost:8080/hessian-server/
```

配置hessian的client端，接口代理配置
```
<!-- 加载配置文件 -->
<bean id="propertyConfigurer"
	class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
	<property name="locations">
		<list>
			<value>classpath*:hessian.properties</value>
		</list>
	</property>
</bean>

<bean id="testHessionClient" class="org.springframework.remoting.caucho.HessianProxyFactoryBean">
	<property name="serviceUrl" value="${hessian.server}hessian/testHessionServer" />
	<property name="serviceInterface" value="com.xxl.core.hessian.service.ITestHessionServerService" />
	<property name="chunkedPost" value="false" />
	<property name="overloadEnabled" value="true" />
</bean>
```

在Controller中测试使用
```
@Autowired
private ITestHessionServerService testHessionClient;

@RequestMapping(value = "/hello")
@ResponseBody
public ReturnT<String> tet(Model model, String name) {
	
	logger.info("client queryUser:{}", name);
	User user = testHessionClient.queryUser(name);
	String resp = user!=null?"查询成功："+user.toString():"查询失败：no body";
	
	LoggerUtils.info("server resp:" + resp);
	return new ReturnT<String>(resp);
}
```

