
### 常用小工具
***
- [Notepad++](https://notepad-plus-plus.org/) ： 代码编辑器
- [jd-gui](http://jd.benow.ca/) ： java反编译工具
- [beyond compare](http://www.scootersoftware.com/download.php?zz=dl3_en) ： 专业级的文件夹和文件对比工具（v3汉化比较好，注册码多）
- [SwitchHosts](https://github.com/oldj/SwitchHosts) ： Hosts快速切换小工具
- [Fiddler](http://www.telerik.com/fiddler) ： Http协议调试代理工具
- EditThisCookie ： chrom下cookie插件
- GAuth Authenticator：chrom下谷歌身份验证器插件；
- [jprofiler](http://www.ej-technologies.com/) ： Java剖析工具
- (MemoryAnalyzer + Jconsole + jmap) ： 内存分析，(1、jconsole监控;2、jmap -dump:format=b,file=文件名.bin [pid];3、MemoryAnalyzer打开dump的bin文件 )
- [WPS Office](http://www.wps.cn/) ： word/excel/ppt/pdf
- [Foxmail](http://www.foxmail.com/) ： 一款优秀的国产电子邮件客户端
- [极速PDF](http://www.jisupdf.com/) ： 一款闪电般好用的PDF阅读器
- knife for File ： 巨型文件切割/复原软件
- 小清新日历/人生日历 ： 桌面日历插件
- shaowsocket：科学上网

### Powerdesigner
[版本v15安装和破解参考](http://www.cnblogs.com/BigGuo/archive/2013/01/17/2865486.html)

**状态图、流程图**
- 状态图：File》new Model》Model types>》Bussiness Process Model》Bussiness Process Diagram
- 流程图：File》new Model》Model types>》Bussiness Process Model》Process Hierachy Diagram

**模型**
- OOM：对象模型；File》new Model》Model types》Physical Data Model
- PDM：物理模型；File》new Model》Model types》Object Oriented Model

**模型转换**
- 1、PDM  》》  OOM：Tools --- generate OOM
- 1、OOM  》》  PDM：Tools --- generate PDM

**导出文件**
- 1、PDM  》》  SQL：a、DateBase -- change dbms; b、Database generation;
- 1、OOM  》》  JAVA：Language -- generate Java Files

**PowerDesigner建表脚本中去掉对象双引号的方法**

打开cdm的情况下，进入Tools－Model Options－Naming Convention，把Name和Code的标签的Charcter case选项设置成Uppercase或者Lowercase，只要不是Mixed Case就行！

或者选择Database->Edit current database->Script->Sql->Format，有一项CaseSensitivityUsingQuote，它的comment为“Determines if the case sensitivity for identifiers is managed using double quotes”，表示是否适用双引号来规定标识符的大小写，可以看到右边的values默认值为“YES”,改为“No”即可！

或者在打开pdm的情况下，进入Tools－Model Options－Naming Convention，把Name和Code的标签的Charcter case选项设置成Uppercase就可以！

[UML-九种图学习](https://yq.aliyun.com/articles/12136)

[UML-六种关系](http://blog.csdn.net/sfdev/article/details/3906243)

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

### Microsoft Office Visio
Office Visio 是一款便于IT和商务专业人员就复杂信息、系统和流程进行可视化处理、分析和交流的软件。

关键字搜索 “Microsoft Office Visio 2007（含序列号）”，下载安装即可；

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

### Subversion + Subclipse（端口：3690）
免费SVN服务器：http://code.taobao.org/

##### 首先安装：
- 1、服务端程序subversion1.x.x；
- 2、客户端程序TortoiseSVN-1.x.x.xxxx（是两个网站）；

TortoiseSVN的语言包下载和TortoiseSVN的下载都在其官网的同一页,如果使用了eclipse并且用插件的话，则可以不用TortoiseSVN。

##### 一、Subversion服务器搭建（Windows）: 

- 1、[官网](http://subversion.apache.org/packages.html)下载
- 2、安装：直接点击安装；
- 3、建立版本库（Repository）：
    ```
    svnadmin create E:\svndemo\repository
    ```
我们也可以使用TortoiseSVN图形化的完成这一步： 在目录E:\svndemo\repository下"右键->TortoiseSVN->Create Repository here...“， 然后可以选择版本库模式， 这里使用默认即可， 然后就创建了一系列目录和文件。

- 4、配置用户和权限：
    
    在E:\svndemo\repository\conf目录下，修改svnserve.conf：

    ```
    # [general]
    # password-db = passwd
    //改为：
    [general]
    password-db = passwd 
    
    //然后修改同目录的passwd文件，去掉下面三行的注释：
    # [users]
    # harry = harryssecret
    # sally = sallyssecret
    //最后变成：
    [users]
    admin = 123456
    user = 123456
    ```
    
    在passwd文件中，“=”前的字符就是用户名，后面的就是密码。还要注意“[users]”前面的注释“#”一定要删除掉。
    
- 5，独立运行SVN服务器：
    ```
    // 任意目录下运行： 
    svnserve -d -r E:\workingfile\svnrepository     
    ```
    我们的服务器程序就已经启动了。注意不要关闭命令行窗口，关闭窗口也会把svnserve停止。
    
- 6，开机启动SVN服务器配置：
```
// 打开一个DOS窗口，在任意目录下执行下面的命令：
// 在命令中的每一个等号后面都要有一个空格否则命令执行失败
sc create SVNService binpath= "\"F:\ProgramFiles\Subversion\bin\svnserve.exe\" --service -r F:\WorkspaceFiles\subversionWorkspace" displayname= "SVNService" depend= Tcpip start= auto   
```

- （1）sc是windows自带的服务配置程序，MySVNServer 是服务的名称，似乎没什么用。
- （2）参数binPath表示svnserve可执行文件的安装路径，由于路径中的"Program Files"带有空格，因此整个路径需要用双引号引起来。而双引号本身是个特殊字符，需要进行转移，因此在路径前后的两个双引号都需要写成/" 。
- （3）--service参数表示以windows服务的形式运行，--r指明svn repository的位置，service参数与r参数都作为binPath的一部分，因此与svnserve.exe的路径一起被包含在一对双引号当中，而这对双引号不需要进行转义。
- （4）displayname表示在windows服务列表中显示的名字， depend =Tcpip 表示svnserve服务的运行需要tcpip服务，start=auto表示开机后自动运行。安装服务后，svnserve要等下次开机时才会自动运行。 - （5）binPath的等号前面无空格，等号后面有空格 displayname depend start也都一样
service前面是--，不是- ，而r前面是-
- （6）若要卸载svn服务，则执行 sc delete svnserve 即可。
- （7）从“sc”到“auto”是在同一个命令sc，必须写在同一行。
    创建成功后，可以在运行中键入service.msc察看系统服务，找到SVNService项，查看这项服务的属性，可以使用Windows提供的界面操作SVNService服务了。
    也可以使用命令行启动、停止服务。

7、启动、停止，服务：

    启动服务：net start svnservice  
    停止服务：net stop svnservice   
    删除创建的服务：sc delete svnservice  
    
8、Eclipse客户端，插件安装：Sublicpse，参考笔记“Eclipse插件”

##### 二、Subversion服务器搭建（CentOS）: 

1、安装（CentOS下yum即可方便的完成安装）
```
# yum install subversion
# svnserve --version      （回车显示版本说明安装成功）
```

2、建立版本库
```
# mkdir /home/root/subversion_repository        （创建svn数据目录（目录可自行制定））
# svnadmin create /home/root/subversion_repository/5i        （5i 就是版本库的名字，可以改变！）
```

3、配置svn配置文件

```
// svnserve.conf主配文件
// （为了方便管理，这里多个库调用相同的配置文件，每个版本库创建之后都会生成）
# vi  /home/root/subversion_repository/5i/conf/svnserve.conf
################
[general]
anon-access=none
auth-access=write
password-db=/home/root/subversion_repository/5i/conf/passwd
authz-db=/home/root/subversion_repository/5i/conf/authz
realm=5i
################

// （svn用户配置文件）
# vi /home/root/subversion_repository/5i/conf/passwd      
##############
[users]
admin=123456
xuxueli=123456
##############

// （svn权限控制配置文件）
# vi /home/root/subversion_repository/5i/conf/authz      
##############################
[aliases]
[groups]
super=admin,xuxueli
common=xuxueli
[/]
@super=rw
@common=rw
*=
##############################
```

4、启动/关闭，SVN服务器
```
# svnserve -d -r /home/root/subversion_repository/5i
-d表示以daemon方式（后台运行）运行
-r 指定根目录
# kill all svnserve

# ps aux | grep svnserve    （查看服务，端口：3690）
# netstat -lnp|grep svnserve
```

5、设置svn开机自启动

编写一个启动脚本：
```
# vi /home/root/svn_startup.sh
内容如下：
#!/bin/bash
killall svnserve
/usr/bin/svnserve -d -r /home/root/subversion_repository/5i
```

修改脚本执行权限：
```
# chmod ug+x  /home/root/svn_startup.sh
```

自动运行：
```
# vi /etc/rc.d/rc.local
末尾添加脚本的路径：
/home/root/svn_startup.sh
```

确认成功：
```
ps -ef|grep svnserve
```

tips：
    解决更新陷入死循环：右键》Team》update to version
    head 表示最新版 ，version这里可以选择某个版本，也是回退到某个版本
    
### GIT
Git是一款免费、开源的分布式版本控制系统，用于敏捷高效地处理任何或小或大的项目。

    波克-CVS；
    驴妈妈-SVN；
    dp-GIT；
    
版本管理svn,git,cvs比较
- RCS(Revision Control System) 修订控制系统：
    - 简单
    - 使用Lock机制防止多个开发人员对同一个文件同时进行修改.
- CVS(Cocurrent Version System)并发版本系统：
    建立在RCS基础上,最流行的开放源代码版本控制系统
    - 使用单一的主代码树,而不像RCS那样依赖多个目录.
    - 最大优点在于多名开发人员可以同时对一个文件进行修改.允许合并.
这就"并发"开发.
- svn：
    - 目录的版本控制：CVS 只能对文件进行版本控制，不能对目录进行版本控制.CVS 只能注意到，一个文件在一个位置被删除了，而在一个新位置创建了另外一个文件。由于它不会连接两个操作，因此也很容易使文件历史轨迹丢失.
    - SVN可以原子性提交：CVS 采用线性、串行的批量提交，即依次地，一个接一个地执行提交，每成功提交一个文件，该文件的一个新的版本即被记录到版本库中，提交时用户提供的日志信息被重复地存储到每一个被修改的文件的版本历史中。CVS串行批量提交模式的弊端在于 －当任何原因造成批量操作的中断时（典型原因包括：网络中断、客户端死机等），版本库往往处于一个不一致的状态：原本应该全部入库的文件只有一部分入库，很有可能版本库中的最新版本不能顺利编译，更为严重的是，随着其他的用户执行cvs update 操作，该不一致性将迅速在开发团队中扩散，从而严重影响团队的开发效率，并存在质量隐患。另外，假如该批量提交的中断没有被及时发现，开发团队往往要花更 多的时间进行软件调试和排错。
    - 

- git：Git 是用于 Linux 内核开发的版本控制工具。与常用的版本控制工具 CVS, Subversion 等不同，它采用了分布式版本库的方式，不必服务器端软件支持，使源代码的发布和交流极其方便。 Git 的速度很快，这对于诸如 Linux kernel 这样的大项目来说自然很重要。 Git 最为出色的是它的合并跟踪（merge tracing）能力。
    - git更加适合分布式开发项目。而svn（当然全称是subversion）则更适合于集中式大型开发项目。也有在git之上再使用一层svn的做法。

        
免费GIT服务器：

    https://github.com/xuxueli/
    https://git.oschina.net/xuxueli0323
    
centos搭建git服务器
```
# yum install git
```

windows客户端：

- [GIT下载window版-msysgit](http://git-scm.com/download/win)：
- [windows图形客户端-官网](https://git-for-windows.github.io/)
- [windows图形客户端-github](https://github.com/git-for-windows/git/releases/)


eclipse插件：egit

##### 日常总结

代码提交顺序：先pull，然后commit，然后push；
    
代码更新提交顺序：
- 1、Commit："本地代码 --> local服务器"；
- 2、Fetsh from Upstream： "remote服务器 --> local服务器"；
- 3、Merge：合并，如果有冲突，先解决，然后再次Commit；
- 4、Push to Upstream："local服务器 --> remote服务器"；

代码更新
- **Fetsh from Upstream**   ：只更新 "远程服务器 --> 本地服务器" 简称(Fetch)
当你更新后，你当前更新的项目与Git Repositories都会有类似一个向下的箭头，这代表“远程服务器”有东西更新到你的“本地服务器 ”，具体数量就是箭头隔壁的数字
- **Pull**                                ： 先 "远程服务器 --> 本地服务器" 后 " 本地服务器 --> 本地代码" 都执行  简称(Pull)
这个操作其实是面向懒人把Git当成SVN使用，一步更新，官方也是不推荐这样做，因为这样，你本地代码，可能一下子代码冲突很多，给开发人员带来很多不便。

提交代码
- **Commit**                    ：" 本地代码 --> 本地服务器"
在Git的作出修改，会有一个箭头指向
Push                         ："本地服务器 --> 远程服务器" 
- **Commit and Push**   ：" 本地代码 --> 本地服务器"  "本地服务器 --> 远程服务器" 有先后顺序
通常 Push  不了，无非就两个问题：代码冲突、网络问题

git reset：重置，让工作目录回到上次提交时的状态(last committed state)
git rebase：用于把一个分支的修改合并到当前分支







