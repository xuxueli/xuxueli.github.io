
### 内存分析
***
- [jprofiler](http://www.ej-technologies.com/) ： Java剖析工具
- (MemoryAnalyzer + Jconsole + jmap) ： 内存分析
    - 1、jconsole监控;
    - 1.1、jmap -histo[:live] <pid> | more （在线打印堆栈对象数量、大小；）；
    - 2、jmap -dump:format=b,file=文件名.bin [pid];
    - 3、MemoryAnalyzer打开dump的bin文件 (或：jhat -J-Xmx1024M [file] 并查看 "Other>instance count、heap class" 属性排查；) ； )


### Apache JMeter压力测试
[官网](http://jmeter.apache.org/download_jmeter.cgi)

Apache JMeter是Apache组织开发的基于Java的压力测试工具。用于对软件做压力测试，它最初被设计用于Web应用测试但后来扩展到其他测试领域。 它可以用于测试静态和动态资源例如静态文件、Java小服务程序、CGI脚本、Java 对象、数据库， FTP服务器, 等等。JMeter 可以用于对服务器、网络或对象模拟巨大的负载，来在不同压力类别下测试它们的强度和分析整体性能。另外，JMeter能够对应用程序做功能/回归测试，通过创建带有断言的脚本来验证你的程序返回了你期望的结果。为了最大限度的灵活性，JMeter允许使用正则表达式创建断言。

**各属性如下：**
- Sample：每个请求的序号
- Start Time：每个请求开始时间
- Thread Name：每个线程的名称
- Label：Http请求名称
- Sample Time：每个请求所花时间，单位毫秒
- Status：请求状态，如果为勾则表示成功，如果为叉表示失败。
- Bytes：请求的字节数
如果Status为叉，那很显然请求是失败了，但如果是勾，也并不能认为请求就一定完全成功了，因为还得看Bytes的字节数是否是所请求网页的正常大小值，如果不是则说明发生了丢包现象，也不是完全成功。

**在下面还有几个参数：**
- 样本数目：也就是上面所说的请求个数，成功的情况下等于你设定的并发数目乘以循环次数。
- 平均：每个线程请求的平均时间
- 最新样本：表示服务器响应最后一个请求的时间
- 偏离：服务器响应时间变化、离散程度测量值的大小，或者，换句话说，就是数据的分布（这个我不是很理解）。

**术语：**
- 1、线程组：测试里每个任务都要线程去处理，所有我们后来的任务必须在线程组下面创建。可以在“Test Plan（鼠标右击） -> 添加  ->Threads(Users) -> 线程组”来建立它，然后在线程组面板里有几个输入栏：线程数、Ramp-Up Period(in seconds)、循环次数，其中Ramp-Up Period(in seconds)表示在这时间内创建完所有的线程。如有8个线程，Ramp-Up = 200秒，那么线程的启动时间间隔为200/8=25秒，这样的好处是：一开始不会对服务器有太大的负载。
- 2、取样器（Sampler）：可以认为所有的测试任务都由取样器承担，有很多种，如：HTTP请求。
- 3、断言：对取样器返回的请求结果给出判断是否正确。
- 4、monitor：它的功能是对取样器的请求结果显示、统计一些数据（吞吐量、KB/S……）等。

**Web压力测试DEMO：**

启动JMeter：/bin/jmeter.bat；

**1、建立Web测试计划：**文件》Templates》Select Template》Building a Web Test Plan；

**2、建立线程组**：编辑》添加》Threads》线程组；

    线程数：模拟用户数；
    Ramp-Up Period（秒）：每个线程，执行间隔；
    循环次数：每个线程执行次数；
    
**3、建立HTTP请**求：选中线程组》添加》Sampler》Http请求；

    服务器名称或IP：测试网站跟地址（www.qiushibaike.com），不要包含http://；
    端口号：80
    协议：http
    方法：GET
    Centent encoding：UTF-8
    路径（可添加多路径，相对地址）：选项》函数助手对话框》_StringFromFile》名称test_qiubai，值待测试url列表，格式为跟地址相对地址，换行表示，如下
        /
        /imgrank
        /late
    点击生成：${_StringFromFile(,E:\ProgramFiles\utils\apache-jmeter-2.13\workspace\qiubai\qiubai_test.txt)}
    当我们并非请求的时候，就会从中随机选择url来进行压力测试
    
**4、查看运行结果：**

    选中线程组》添加》监听器》用表格查询结果
    选中线程组》添加》监听器》图形结果
    
**5、运行即可；**

### SonarQube代码质量管理平台

[官网](http://www.sonarqube.org/downloads/)
[两分钟入门](http://docs.sonarqube.org/display/SONAR/Get+Started+in+Two+Minutes)

静态代码检查工具，帮助检查代码缺陷，改善代码质量，提高开发速度

##### 一、安装sonarqube
- 1、官网下载安装包：sonarqube-4.5.4.zip；
- 2、直接解压即可（前提是拥有JDK环境）；
- 3、进入解压目录下：bin\windows-x86-32，双击“StartSonar.bat”文件即可开启sonar；
- 4、浏览器输入网址即可访问：http://localhost:9000/； 右上角登录，默认账号密码：admin/admin；

##### 二、代码分析  （可参考官网入门文档）
- 1、官网下载SonarQube Scanner；
- 2、直接解压，配置path目录，加入：E:\ProgramFiles\sonar-runner-2.4\bin;；
- 3、为“目标工程”新增配置文件：sonar-project.properties
```
# Required metadata
sonar.projectKey=5i-admin
sonar.projectName=5i-admin
sonar.projectVersion=1.0
# Comma-separated paths to directories with sources (required)
sonar.sources=src
sonar.binaries=target
# Language
sonar.language=java
# Encoding of the source files
sonar.sourceEncoding=UTF-8
```
说明：

    projectKey与projectName和工程名字一样即可
    sources为源码目录
    binaries为编译后的classes目录，这个目录要注意一下，如果都在bin下面或者目录还有很多层级才到classe目录，可以直接为sonar.binaries=bin，sonar会自动遍历所有目录
    language为分析的语言
    sourceEncoding源码编码格式

4、执行分析命令：cmd进入workspace“目标工程”代码路径，输入命令：sonar-scanner.bat；

##### 三、中文语言插件
- 1、下载中文插件，JAR包：
http://docs.codehaus.org/display/SONAR/Chinese+Pack 
- 2、放到sonar安装目录下：……\sonarqube-4.5.4\extensions\plugins；
- 3、重启sonar，祖国的语言，好亲切！

其他：

    扩展1：配置数据库（内嵌数据库不稳定）
    扩展2：关联Eclipse
    扩展3：关联maven
    扩展4：关联svn








