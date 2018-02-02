### Dubbo
DUBBO是一个分布式服务框架，致力于提供高性能和透明化的RPC远程服务调用方案，是阿里巴巴SOA服务化治理方案的核心框架，每天为2,000+个服务提供3,000,000,000+次访问量支持，并被广泛应用于阿里巴巴集团的各成员站点。

[官网](http://dubbo.io/)

[github地址](https://github.com/alibaba/dubbo)


### dubbo使用 (前提，已经存在可用的zookeeper环境)
##### maven依赖
```
<!-- dubbo -->
	<dependency>
		<groupId>com.alibaba</groupId>
		<artifactId>dubbo</artifactId>
		<version>2.5.3</version>
		<exclusions>
			<exclusion>
				<groupId>org.springframework</groupId>
				<artifactId>spring</artifactId>
			</exclusion>
		</exclusions>
	</dependency>
	<!-- zookeeper -->
	<dependency>
		<groupId>org.apache.zookeeper</groupId>
		<artifactId>zookeeper</artifactId>
		<version>3.4.6</version>
	</dependency>
	<dependency>
		<groupId>com.github.sgroschupf</groupId>
		<artifactId>zkclient</artifactId>
		<version>0.1</version>
	</dependency>
```

##### 1、api开发

接口开发
```
package com.xxl.dubbo.service;
import com.xxl.dubbo.model.User;

public interface IDemoService {
	
	public User findUser(String name);
}
```

对象实体开发（需要序列化）
```
package com.xxl.dubbo.model;
import java.io.Serializable;

public class User implements Serializable {
	private String name;
	private int age;
	
	public User(String name, int age) {
		super();
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

##### 2、provider开发

接口实现类开发
```
package com.xxl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.xxl.dubbo.model.User;
import com.xxl.dubbo.service.IDemoService;

@Service("demoService")
public class DemoServiceImpl implements IDemoService {
	private static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
	
	public User findUser(String name) {
		logger.info("server query param name:{}", name!=null?name:"null");
		
		if (name != null) {
			if ("jack".equals(name)) {
				return new User("杰克", 21);
			} else if ("lucy".equals(name)) {
				return new User("露丝", 18);
			}
		}
		return null;
	}

}
```

接口实现类，扫描为Spring的Bean
```
<context:component-scan base-package="com.xxl.service.impl, com.xxl.dao.impl" />
```

配置dubbo注册中心地址（dubbo.properties）   
（第一个地址必须配backup，否则报参数异常；）
```
# zookeeper://main.ip1:2181?backup=backup.ip1:2181,main.ip2:2182,main.ip3:2183
dubbo.registry.address=zookeeper://127.0.0.1:2181?backup=127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
```

配置dubbo服务provider（applicationcontext-dubbo-provider.xml）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	   http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd
	   ">
	   
	<dubbo:application name="dubbo-server" />

	<!--
		dubbo:registry 标签一些属性的说明：
		  1、register是否向此注册中心注册服务，如果设为false，将只订阅，不注册。
	      2、check注册中心不存在时，是否报错。
	      3、subscribe是否向此注册中心订阅服务，如果设为false，将只注册，不订阅。
	      4、timeout注册中心请求超时时间(毫秒)。
	      5、address可以Zookeeper集群配置，地址可以多个以逗号隔开等。
	      	集群配置：# zookeeper://main.ip1:2181?backup=backup.ip1:2181,main.ip2:2182,main.ip3:2183
	 -->
	<dubbo:registry address="${dubbo.registry.address}" />
	
	<dubbo:protocol name="dubbo" port="20880" />
	
	<!--
	   dubbo:service标签的一些属性说明：
	     1）interface服务接口的路径
	     2）ref引用对应的实现类的Bean的ID
	     3）registry向指定注册中心注册，在多个注册中心时使用，值为<dubbo:registry>的id属性，多个注册中心ID用逗号分隔，如果不想将该服务注册到任何registry，可将值设为N/A
	     4）register 默认true ，该协议的服务是否注册到注册中心。
	 -->
	<dubbo:service interface="com.xxl.dubbo.service.IDemoService" ref="demoService" />
	
</beans>
```

##### 3、consumer开发

配置dubbo注册中心地址（dubbo.properties）   
（第一个地址必须配backup，否则报参数异常；）
```
# zookeeper://main.ip1:2181?backup=backup.ip1:2181,main.ip2:2182,main.ip3:2183
dubbo.registry.address=zookeeper://127.0.0.1:2181?backup=127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
```

配置Dubbo的consumer (applicationcontext-context-consumer.xml)
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
	   http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd
	   ">
	   
	<dubbo:application name="dubbo-client" />
	<dubbo:registry address="${dubbo.registry.address}" />

	<!--
		dubbo:reference 的一些属性的说明：
	      1）interface调用的服务接口
	      2）check 启动时检查提供者是否存在，true报错，false忽略
	      3）registry 从指定注册中心注册获取服务列表，在多个注册中心时使用，值为<dubbo:registry>的id属性，多个注册中心ID用逗号分隔
	      4）loadbalance 负载均衡策略，可选值：random,roundrobin,leastactive，分别表示：随机，轮循，最少活跃调用 
	 -->
	<dubbo:reference id="demoService" interface="com.xxl.dubbo.service.IDemoService" />
				   
</beans>
```

在Controller中测试使用远程dubbo服务
```
@Autowired
private IDemoService demoService;

@RequestMapping("/test")
@ResponseBody
public ReturnT<String> test(String name){
	logger.info("client query param name:{}", name!=null?name:"null");
	
	User user = demoService.findUser(name);
	String resp;
	if (user != null) {
		resp = "找到用户：" + user.toString();
	} else {
		resp = "用户不存在！";
	}
	
	logger.info("client query result:{}", resp);
	return new ReturnT<String>(resp);
}
```

### "dubbo-admin" 管理控台平台

dubbo管理控制台开源部分主要包含： 提供者  路由规则  动态配置  访问控制  权重调节  负载均衡  负责人，等管理功能。

- 1、github获取dubbo-admin源码编译；
- 2、修改“pom.xml”中dubbo版本为 “2.5.3”，否则需要编译整个dubbo项目源码；
```
// maven中央仓库dubbo最新版本：
http://mvnrepository.com/artifact/com.alibaba/dubbo
```
- 3、配置“WEB-INF/dubbo.properties”
```
// 集群zookeeper地址配置（不知为何第一个地址必须配backup，否则报参数异常；）
dubbo.registry.address=zookeeper://127.0.0.1:2181?backup=127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183

// 登录账号，密码
dubbo.admin.root.password=root
dubbo.admin.guest.password=guest
```
- 4、部署启动即可；

### 原理简介
dubbo：zookeeper注册维护服务方地址列表，调用方获取调用地址，和服务方建立tcp连接，之后服务调用走tcp长连接的方式；


##### dubbo角色介绍:

    1、provider：服务提供方；
    2、consumer：服务消费方；
    3、registry：服务注册中心：注册和发现服务；
    4、monitor：服务监控中心：统计服务调用次数和调用时间；
    5、container：服务运行容器（Tomcat）；


##### dubbo调用流程：

    1. container负责启动，加载，运行provider；
    2. provider在启动时，向registry注册自己提供的服务；
    3. consumer在启动时，向registry订阅自己所需的服务；
    4. registry返回provider地址列表给consumer，如果有变更，注册中心将基于长连接推送变更数据给consumer；
    5. consumer，从provider地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
    6. consumer和provider，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。
    
#### dubbo注册中心，多种实现方式
- 1、multicast：不需要启动任何中心节点，只要广播地址一样，就可以互相发现；组播受网络结构限制，只适合小规模应用或开发阶段使用；
- 2、zookeeper（zkclient）：Zookeeper是Apacahe Hadoop的子项目，是一个树型的目录服务，支持变更推送，适合作为Dubbo服务的注册中心，工业强度较高，可用于生产环境，并推荐使用
- 3、redis：通过心跳的方式检测脏数据，服务器时间必须相同，并且对服务器有一定压力。
- 4、dubbo简易注册中心，数据库的，可参考官网文档；




