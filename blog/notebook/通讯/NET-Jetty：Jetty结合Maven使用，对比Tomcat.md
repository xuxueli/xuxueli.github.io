**简介**    
Tomcat和Jetty都是一种Servlet引擎，他们都支持标准的servlet规范和JavaEE的规范。

由于同样遵循servlet2.5和jdk1.5的标准，jetty和tomcat的使用针对项目是透明（没有差异）的。

**jdk版本对应关系**
jdk | jetty | tomcat
---|---|---
1.5+ | Jetty7 | Tomcat 6
1.6+ | Jetty8 | Tomcat 7
1.7+ | Jetty9 | Tomcat 8

**Jetty和Tomcat简单对比**
- 1、架构比较
    - Jetty的架构比Tomcat的更为简单
    - Jetty的架构是基于Handler来实现的，主要的扩展功能都可以用Handler来实现，扩展简单。
    - Tomcat的架构是基于容器设计的，进行扩展是需要了解Tomcat的整体设计结构，不易扩展。

- 2、性能比较
    - Jetty和Tomcat性能方面差异不大
    - Jetty可以同时处理大量连接而且可以长时间保持连接，适合于web聊天应用等等。
    - Jetty的架构简单，因此作为服务器，Jetty可以按需加载组件，减少不需要的组件，减少了服务器内存开销，从而提高服务器性能。
    - Jetty默认采用NIO结束在处理I/O请求上更占优势，在处理静态资源时，性能较高
    - Tomcat适合处理少数非常繁忙的链接，也就是说链接生命周期短的话，Tomcat的总体性能更高。
    - Tomcat默认采用BIO处理I/O请求，在处理静态资源时，性能较差。

- 3、其它比较
    - Jetty的应用更加快速，修改简单，对新的Servlet规范的支持较好。
    - Tomcat目前应用比较广泛，对JavaEE和Servlet的支持更加全面，很多特性会直接集成进来。


**jetty结合maven使用**
- 1、maven3中添加jetty插件：
```
<!-- jetty插件：开发调试时使用 -->
<plugin>
    <groupId>org.mortbay.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <version>7.6.16.v20140903</version>
    <configuration>
        <scanIntervalSeconds>5</scanIntervalSeconds>
        <webAppConfig>
            <contextPath>/test</contextPath>
        </webAppConfig>
        <connectors>
            <connector implementation="org.eclipse.jetty.server.nio.SelectChannelConnector">
                <port>8081</port>
                <maxIdleTime>60000</maxIdleTime>
            </connector>
        </connectors>
    </configuration>
</plugin>
```
说明：

    scanIntervalSecond：表示扫描项目变更的时间间隔，默认为0，表示不扫描。
    ContextPath：表示项目的访问路径，比如此：http://localhost:8081/test/
    port：表示绑定的端口号，默认监听的端口是8080

- 2、为了使用jetty命令，修改maven配置文件settings.xml（项目成功运行后，还原删除改配置也没问题，why？）
```
<pluginGroups>
    ……
    <pluginGroup>org.mortbay.jetty</pluginGroup>
    ……
</pluginGroups>
```
- 3、项目右键Run contifuration配置jetty命令：（如果步骤1中已经指明端口，此处不需要指明端口）
```
Goals：jetty:run    // 端口默认8080
Goals：jetty:run -Djetty.port=8081 // 指定绑定的端口号为8081
```

- 4、启动项目：
项目》右键》Run as》maven build （选中上一步中配置的那一项）

