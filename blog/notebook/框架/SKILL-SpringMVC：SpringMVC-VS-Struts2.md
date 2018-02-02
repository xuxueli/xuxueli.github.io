 ### SpringMVC vs Struts2S

#### 性能
![输入图片说明](https://static.oschina.net/uploads/img/201703/27180357_LUj5.png "在这里输入图片标题")

MVC框架性能比较几篇文章：
[Link1](http://lib.csdn.net/article/javaee/45332 )
[Link2](http://wenku.baidu.com/view/148d7e34eefdc8d376ee32ac.html)

#### 开发效率/体验
![输入图片说明](https://static.oschina.net/uploads/img/201703/27180921_q4mQ.png "在这里输入图片标题")

>提高springmvc可读性的一个建议：规定Controller的 package 路径和 URL 路径一致；
如 “/aaa/bbb”页面对应Controller的包名为："***.mvc.aaa.Controller#bbb"

#### 漏洞大事件
![输入图片说明](https://static.oschina.net/uploads/img/201703/27181556_funw.png "在这里输入图片标题")

>1、京东12G： 
2016-12-10晚间，京东被媒体爆料有12G的数据遭泄漏，外泄数据包括用户名、密码、邮箱、QQ号、电话号码、身份证等多种信息。京东集团回应称初步判断该数据源于2013年Struts2的安全漏洞问题；

>官方回复：京东信息安全部门答复，该数据源于2013年Struts 2的安全漏洞问题，当时国内几乎所有互联网公司及大量银行、政府机构都受到了影响，导致大量数据泄露。

>2、淘宝“拖库门”：
2013年7月22日传出来的消息，淘宝的数据库因为Strust2漏洞被拖。

>官方微博答复：淘宝网已于当天第一时间对该漏洞进行修复，并确认此漏洞并没有被成功利用，用户的数据安全和账号数据未见任何异常。

>3、 “乌云知识库查询-乌云公开漏洞”，基本是一年之前的历史漏洞；因为“SJJY-白帽子事件”之后，乌云网已经基本关闭了。

#### 漏洞攻击工具
![输入图片说明](https://static.oschina.net/uploads/img/201703/27182248_jI5x.png "在这里输入图片标题")

> 网络上流传着许多针对struts2流行漏洞的共计工具，攻击成本非常低。

#### 官方纰漏漏洞
![输入图片说明](https://static.oschina.net/uploads/img/201703/27182620_pTBk.png "在这里输入图片标题")

#### ONGL漏洞浅析

    struts2参数赋值原理：
    1、struts采用值栈存储请求和响应的数据：ValueStack ；
    2、值栈通过ONGL存取数据：OgnlValueStack;
    3、值栈中存取数据时会执行表达式：com.opensymphony.xwork2.ognl.OgnlUtil#compile
    4、 ParametersInterceptor为每个参数声明一个ONGL语句，并通过对象图导航调用getter/setter方法 

    如参数：?city.name=beijing，ONGL语句为：action.getCity().setName(“beijing”)，执行后完整参数赋值。

>1、OGNL(对象图导航语言)：
所谓对象图，即以任意一个对象为根，通过OGNL可以访问与这个对象关联的其它对象。
对象图的导航，必须通过getters方法进行导航；主要的功能就是赋值与取值；

>2、比较而言，springmvc通过参数解析器是将request对象内容进行解析成方法形参；（method 形参反射）

问题来了：假如参数中包含攻击代码如“rm -rf /*”呢？
(ParametersInterceptor可以拦截部分非法参数，但是将参数转码为unicode可绕过)

![输入图片说明](https://static.oschina.net/uploads/img/201703/27183009_hxnF.png "在这里输入图片标题")

感兴趣的同学参照以下代码体验一下OGNL语句；
![输入图片说明](https://static.oschina.net/uploads/img/201703/27183037_8Hl6.png "在这里输入图片标题")

#### 迭代、文档资料
![输入图片说明](https://static.oschina.net/uploads/img/201703/27183329_VOyn.png "在这里输入图片标题")

#### 趋势、社区
    1、spring boot：开箱即用，库的集合；
    2、spring cloud：基于Spring Boot的一整套实现微服务的框架。他提供了微服务开发所需的配置管理、服务发现、断路器、智能路由、微代理、控制总线、全局锁、决策竞选、分布式会话和集群状态管理等组件。 
        a、 spring-cloud-config：配置中心；
        b、 Spring Cloud Netflix：服务通讯 (注册中心/网关/负载均衡/监控/熔断器)
        c、 Spring Cloud Bus：消息队列；
        d、… … 
        （ spring cloud对于中小型互联网公司来说是一颗惊雷）

![输入图片说明](https://static.oschina.net/uploads/img/201703/27183607_oRH1.png "在这里输入图片标题") 