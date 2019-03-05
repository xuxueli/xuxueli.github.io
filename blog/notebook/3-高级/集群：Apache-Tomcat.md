### Apache+ jk + tomcat 集群

##### 第一步：安装软件准备；
- 1、[Apache](http://httpd.apache.org/download.cgi)
- 2、[Tomcat](http://tomcat.apache.org/)
- 3、[JK](http://mirror.bjtu.edu.cn/apache/tomcat/tomcat-connectors/jk/binaries/windows/)

##### 第二部：配置Apache，JK插件相关

- 1、Apacha，加入JK插件

将下载的JK插件“mod_jk.so”复制到Apache安装目录的“modules”目录下 ；

- 2、Apacha，新增JK集群配置“workers.properties”

内容如下：
```
#---start---
#server
worker.list = controller
#========tomcat1========
worker.tomcat1.port=11009
worker.tomcat1.host=localhost
worker.tomcat1.type=ajp13
worker.tomcat1.lbfactor = 1
#========tomcat2========
worker.tomcat2.port=12009
worker.tomcat2.host=localhost
worker.tomcat2.type=ajp13
worker.tomcat2.lbfactor = 1
#========tomcat3========
worker.tomcat3.port=13009
worker.tomcat3.host=localhost
worker.tomcat3.type=ajp13
worker.tomcat3.lbfactor = 1
#========tomcat3========
#worker.tomcat3.port=13009
#worker.tomcat3.host=192.168.0.80 //在我的虚拟机中的，可以算远程的吧
#worker.tomcat3.type=ajp13
#worker.tomcat3.lbfactor = 1
#========controller,负载均衡控制器========
worker.controller.type=lb
worker.controller.balanced_workers=tomcat1,tomcat2,tomcat3
worker.controller.sticky_session=false
worker.controller.sticky_session_force=1
#worker.controller.sticky_session=1
#---stop---
```

- 3、Apacha，新建JK配置（"F:\ProgramFiles\Apache2.2.25\conf\mod_jk.conf"）

内容如下：
```
#---start---
LoadModule jk_module modules/mod_jk.so
JkWorkersFile conf/workers.properties
#指定那些请求交给tomcat处理,"controller"为在workers.propertise里指定的负载分配控制器名
JkMount /*.jsp controller
#---stop---
```

- 4、Apache，修改配置（改“F:\ProgramFiles\Apache2.2.25\conf\httpd.conf”）

在文件的最后一行添加：
```
#include "F:\ProgramFiles\Apache2.2.25\conf\mod_jk.conf"
include conf\mod_jk.conf
```

##### 第三步：配置Tomcat

修改每一个Tomcat的的server.xml配置：

- 1、Server 的 port
```
<Server port="8005" shutdown="SHUTDOWN">
```

- 2、Connector 为 HTTP/1.1 的 port
```
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

- 3、Connector 为 AJP/1.3 的 port，以及内部配置

该端口为“workers.properties”配置端口
```
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

- 4、Engine 配置 jvmRoute参数
```
<Engine name="Catalina" defaultHost="localhost" jvmRoute="jvm1">
```

- 5、加上Cluster配置
```
<Cluster className="org.apache.catalina.ha.tcp.SimpleTcpCluster"/>
```

##### 第四部：建立测试项目
- 1、建立 Test项目，需要在项目的web.xml中添加
```
<distributable/>
```

- 2、建立 /index.jsp，内容如下：
```
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>Cluster App Test</title>
</head>
<body>
Server Info:
<%
out.println(request.getLocalAddr() + " : " + request.getLocalPort()+"<br>");%>
<%
out.println("<br> ID " + session.getId()+"<br>");
// 如果有新的 Session 属性设置
String dataName = request.getParameter("dataName");
if (dataName != null && dataName.length() > 0) {
String dataValue = request.getParameter("dataValue");
session.setAttribute(dataName, dataValue);
}
out.println("<b>Session 列表</b><br>");
System.out.println("============================");
Enumeration e = session.getAttributeNames();
while (e.hasMoreElements()) {
String name = (String)e.nextElement();
String value = session.getAttribute(name).toString();
out.println( name + " = " + value+"<br>");
System.out.println( name + " = " + value);
}
%>
<form action="index.jsp" method="POST">
名称:<input type=text size=20 name="dataName"> <br> 值:<input
type=text size=20 name="dataValue"> <br> <input
type=submit>
</form>
</body>
</html>
```

##### 第五部：Session测试
- 1、将项目部署到3个服务器 ；
- 2、然后分别启动Apache和3个Tocmat服务器，这些Tomcat启动顺序随意 ；
- 3、访问

    - 【Apache】http://localhost:8080/
    - 【分布式Apache + Tomcat】http://localhost:8080/Test/index.jsp
    - 【Tomcat1】http://localhost:8081/Test/index.jsp
    - 【Tomcat2】http://localhost:8081/Test/index.jsp
    - 【Tomcat3】http://localhost:8081/Test/index.jsp



### Apache和Tomcat区别

    Apache：全球应用最广泛的http服务器，免费，出自apache基金组织 
    Tomcat：应用也算非常广泛的web 服务器，支持部分j2ee，免费，出自 apache基金组织 
    JBoss：开源的应用服务器，比较受人喜爱，免费（文档要收费） 
    weblogic：应该说算是业界第一的app server，全部支持j2ee1.4，对于开发者，有免费使用一年的许可证，用起来比较舒服，出资BEA公司，呵呵，我用的就是这个，所以比较熟悉 
    
    jboss也支持j2ee
    JBoss和WebLogic都含有Jsp和Servlet容器,也就可以做web容器, 
    JBoss和WebLogic也包含EJB容器,是完整的J2EE应用服务器
    Tomcat 只能做jsp和servlet的container


### Nginx和Apache比较
##### nginx 优于 apache： 
- nginx轻量级，占用更少的内存及资源
- nginx抗并发，nginx 处理请求是异步非阻塞的（多个连接（万级别）可以对应一个进程 ），而apache 则是阻塞型的（同步多进程模型，一个连接对应一个进程），在高并发下nginx 能保持低资源低消耗高性能
- nginx高度模块化的设计，编写模块相对简单
- nginx社区活跃，各种高性能模块出品迅速啊
- Nginx 配置简洁, Apache 复杂 
- Nginx 静态处理性能比 Apache 高 3倍以上 

##### apache 优于 nginx： 
- apache的rewrite ，比nginx 更强大
- apache模块超多，基本想到的都可以找到
- apache少bug ，nginx 的bug 相对较多
- apache超稳定
- Apache 对 PHP 支持比较简单，Nginx 需要配合其他后端用 
- Apache 的组件比 Nginx 多 
现在 Nginx 才是 Web 服务器的首选

[Apache与Nginx的优缺点比较](http://www.cnblogs.com/huangye-dream/p/3550328.htm)