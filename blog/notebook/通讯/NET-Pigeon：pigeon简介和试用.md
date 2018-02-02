[微博的RPC-motan](https://github.com/weibocom/motan)

### pigeon
Pigeon是一个分布式服务通信框架（RPC），在大众点评内部广泛使用，是大众点评最基础的底层框架之一。现在已经开源。

[github地址1](https://github.com/dianping/pigeon)

[github地址2](https://github.com/wu-xiang/pigeon)

[用户手册](https://github.com/wu-xiang/pigeon/blob/master/USER_GUIDE.md)

- 优点：
支撑点评主要业务系统，经过严格考验；
结合cat、lion，从RPC通讯到实时监控，形成一整套完善的服务治理框架；

- 缺点：
目前仅于点评内部使用，用户少，缺少文档；
更新缓慢，与cat、lion耦合性较高，如果需要监控，也要引入cat和lion，后面两个文档也较少，未形成系统教程；

综上述所得，与dubbo相比，我倾向于使用后者，因为后者用户较多，网络上教程文档较多，已经形成一整台完成的服务治理体系（驴妈妈也是其用户之一，反响很不错）；


##### 一次sync调用流程：
```
PigeonServiceFactory.getRemoteService 【easyUtil】
》ProxyFactory.init 【dpsf-net】
》DefaultAbstractSerializer.proxyRequest
》InvokerProcessHandlerFactory.selectInvocationHandler
》RemoteCallInvokeFilter.invoke
》InvokerUtils.sendRequest
》AbstractClient.write
》HttpInvokerClient.doWrite
》HttpInvokerExecutor.executeRequest
》最终执行逻辑如下：
postMethod.setRequestEntity(new ByteArrayRequestEntity(baos.toByteArray(), this.getContentType()));
httpClient.executeMethod(postMethod);
```

### 部署流程
```
// maven依赖
<!-- dpsf-net -->
<dependency>
    <groupId>com.dianping.dpsf</groupId>
    <artifactId>dpsf-net</artifactId>
    <version>2.5.4</version>
</dependency>
<!-- lion-client -->
<dependency>
    <groupId>com.dianping.lion</groupId>
    <artifactId>lion-client</artifactId>
    <version>0.5.3</version>
</dependency>
<!-- easyUtil -->
<dependency>
 	<groupId>com.dianping</groupId>
 	<artifactId>easyUtil</artifactId>
 	<version>0.0.2-SNAPSHOT</version>
 	<scope>compile</scope>
 </dependency>

// 服务接口
package com.xxl.service;
public interface IDpsfService {
	public String sayHi(String name);
}

// 服务实现
package com.xxl.service.impl;
import org.springframework.stereotype.Service;
import com.xxl.service.IDpsfService;

@Service("dpsfService")
public class DpsfServiceImpl implements IDpsfService {
	@Override
	public String sayHi(String name) {
		return "hi, i am pegin. to " + name;
	}
}

// 服务提供方，配置
<bean id="shopBusinessServer" class="com.dianping.dpsf.spring.ServiceRegistry" init-method="init" lazy-init="false">
	<!-- <property name="port" value="${shopbusiness-server.remote.shopBusinessService.port}"/> -->
	<property name="port" value="2055"/>
	<property name="services">
		<map>
            <!-- service注册列表,map结构,entry -->
            <entry key="http://service.xxl.com/demo/dpsfService_1.0.0" value-ref="dpsfService" />
        </map>
	</property>
</bean>

// 服务消费方，配置
<bean id="peginDpsfService" class="com.dianping.dpsf.spring.ProxyBeanFactory" init-method="init">
	<property name="serviceName" value="http://service.xxl.com/demo/dpsfService_1.0.0" />
	<property name="iface" value="com.xxl.service.IDpsfService" />
	<property name="serialize" value="hessian" />
	<property name="callMethod" value="sync" />
	<property name="timeout" value="5000" />
</bean>

// main方法测试
private static String LOCAL_SERVICE_HOSTS = "127.0.0.1:3022"; 
public static void main(String[] args) throws Exception {
	IDpsfService dpsfService = new PigeonServiceFactory<IDpsfService>(){}.getRemoteService("http://service.xxl.com/demo/dpsfService_1.0.0", LOCAL_SERVICE_HOSTS);
	System.out.println(dpsfService.sayHi("jack"));
}

// 注入到Controller测试
@Autowired
private IDpsfService dpsfService;

@Autowired
private IDpsfService peginDpsfService;

@RequestMapping("")
@ResponseBody
public String index(String name){
	return dpsfService.sayHi(name);
}

@RequestMapping("/pegin")
@ResponseBody
public String pegin(String name){
	return peginDpsfService.sayHi(name);
}
```





