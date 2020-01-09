<h2 style="color:#4db6ac !important" >Hession远程通讯</h2>
> 【原创】2015/08/05

[TOCM]

[TOC]

## Hessian介绍
Hessian是一个轻量级的remoting on http工具,采用的是Binary RPC协议，所以它很适合于发送二进制数据,同时又具有防火墙穿透能力。Hessian一般是通过Web应用来提供服务，因此非常类似于平时我们用的 WebService。只是它不使用SOAP协议,但相比webservice而言更简单、快捷。

Hessian官网：http://hessian.caucho.com/

Hessian 可通过Servlet提供远程服务，需要将匹配某个模式的请求映射到Hessian服务。也可Spring框架整合，通过它的 DispatcherServlet可以完成该功能，DispatcherServlet可将匹配模式的请求转发到Hessian服务。Hessian的server端提供一个servlet基类, 用来处理发送的请求，而Hessian的这个远程过程调用，完全使用动态代理来实现的,，建议采用面向接口编程，Hessian服务通过接口暴露。
原理简单描述：动态代理 + 序列化 + http；
特点：
- 1、角色划分为：provider、consumer；
- 2、consumer需要指明provider的http服务地址，地址维护比较繁琐；
- 3、provider地址通过域名路由到nginx，可以实现provider的负载均衡，但是会导致“1+1（nginx+tomcat，而dubbo是tcp直连的，就不会出现该问题）”问题，产生单点压力，放大流量；




## 应用级协议Binary-RPC
Binary-RPC(Remote Procedure Call Protocol，远程过程调用协议)是一种和RMI(Remote Method Invocation，远程方法调用)类似的远程调用的协议，它和RMI 的不同之处在于它以标准的二进制格式来定义请求的信息 ( 请求的对象、方法、参数等 ) ，这样的好处是什么呢，就是在跨语言通讯的时候也可以使用。

Binary -RPC 协议的一次远程通信过程：
- 1 、客户端发起请求，按照 Binary -RPC 协议将请求信息进行填充；
- 2 、填充完毕后将二进制格式文件转化为流，通过传输协议进行传输；
- 3 、接收到在接收到流后转换为二进制格式文件，按照 Binary -RPC 协议获取请求的信息并进行处理；
- 4 、处理完毕后将结果按照 Binary -RPC 协议写入二进制格式文件中并返回。


## 使用步骤
### 1、maven依赖
```
<!-- https://mvnrepository.com/artifact/com.caucho/hessian -->
<dependency>
    <groupId>com.caucho</groupId>
    <artifactId>hessian</artifactId>
    <version>{last release version}</version>
</dependency>
```

### 2、公共Api：接口 + DTO

接口
```
package com.xxl.core.hessian.service;
import com.xxl.core.hessian.model.User;

public interface DemoHessionService {
	public User queryUser(String name); 
}
```

DTO文件（需要序列化）
```
package com.xxl.core.hessian.model;
import java.io.Serializable;

public class User implements Serializable{
    private static final long serialVersionUID = 42L;
    
	private String name;
	private int age;
	
	public User(String name, int age) {
		this.name = name;
		this.age = age;
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
	
	@Override
    public String toString() {
        return "User [name=" + name + ", age=" + age + "]";
    }
}
```

### 3、Server端：接口实现类 + HessianServiceExporter配置

接口实现类
```
package com.xxl.core.hessian.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xxl.core.hessian.model.User;
import com.xxl.core.hessian.service.DemoHessionService;

@Service("demoHessionServiceImpl")
public class DemoHessionServiceImpl implements DemoHessionService {
	private transient static Logger logger = LoggerFactory.getLogger(DemoHessionServiceImpl.class);
	
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

HessianServiceExporter配置
```
@Autowired
private DemoHessionService demoHessionService;

@Bean(name = "/hessian/demoHessionService")
public HessianServiceExporter accountService(){
    HessianServiceExporter exporter = new HessianServiceExporter();
    exporter.setService(demoHessionService);
    exporter.setServiceInterface(DemoHessionService.class);
    return exporter;
}
```

### 4、Client端：HessianProxyFactoryBean配置 + 注入使用

HessianProxyFactoryBean配置
```
@Bean
public HessianProxyFactoryBean helloClient(){
    HessianProxyFactoryBean factoryBean = new HessianProxyFactoryBean();
    factoryBean.setServiceUrl("http://localhost:8080/hessian/demoHessionService");
    factoryBean.setServiceInterface(HelloWorldService.class);
    return factoryBean;
}
```

注入使用
```
@Autowired
private DemoHessionService demoHessionService;

@RequestMapping(value = "/hello")
@ResponseBody
public ReturnT<String> tet(Model model, String name) {
	
	logger.info("client queryUser:{}", name);
	User user = demoHessionService.queryUser(name);
	String resp = user!=null?"查询成功："+user.toString():"查询失败：no body";
	
	LoggerUtils.info("server resp:" + resp);
	return new ReturnT<String>(resp);
}
```

