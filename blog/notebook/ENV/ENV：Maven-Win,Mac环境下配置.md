- [官网](http://maven.apache.org/download.cgi)
- [官方教程](http://maven.apache.org/guides/getting-started/index.html)
- [官方五分钟入门](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)


### 1、前提
- 正确安装JDK，并配置环境变量

### 2、Windows下安装配置Maven
* 解压安装：
    >前往[官网](http://maven.apache.org/download.cgi)下载Maven，并解压安装；
* 配置Windows环境变量；
```
    MAVEN_HOME=D://maven
    PATH=%MAVEN_HOME%\bin;  + 其他
```
* 验证：
    >在dos下mvn -version确认
* 配置本地仓库
    >在Maven解压安装目录下 “\conf\settings.xml” 文件中配置

    ```
    在windows下
    <localRepository>e:/work/maven_lib</localRepository>
    在Mac下
    <localRepository>/Users/xuxueli/workspaces/maven-libs</localRepository>
    ```
* 配置中央仓库
    > 不配置时，默认使用中央仓库（网速好的话，推荐使用）；

    > 国内网速质量差时，可配置OSChina提供的仓库镜像；

* （注意，maven3.3 需要jdk1.7+）
 
### Mac下安装配置maven

```
# 用户目录下，新建.bash_profile文件
cd ~
touch .bash_profile     (如果该文件不存在，将创建一个空文件)
open .bash_profile      (调用记事本编辑该文件)

# 配置maven环境变量
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置path
PATH=$PATH:$MAVEN_HOME/bin
export PATH

# 配置生效
source .bash_profile
```

- 验证：
    >在terminal下mvn -version确认
- 配置本地仓库
    >在Maven解压安装目录下 “\conf\settings.xml” 文件中配置

    ```
    <localRepository>/Users/xuxueli/workspaces/maven-libs</localRepository>
    ```

### 常用命令
```
mvn validate //验证工程是否正确，所有需要的资源是否可用
mvn compile//编译项目的源代码
mvn test-compile  //编译项目测试代码
mvn test  //使用已编译的测试代码，测试已编译的源代码
mvn package    //已发布的格式，如jar，将已编译的源代码打包
mvn integration-test //在集成测试可以运行的环境中处理和发布包
mvn verify //运行任何检查，验证包是否有效且达到质量标准
mvn install //把包安装在本地的repository中，可以被其他工程作为依赖来使用
mvn deploy //在整合或者发布环境下执行，将最终版本的包拷贝到远程的repository，使得其他的开发者或者工程可以共享
mvn generate-sources //产生应用需要的任何额外的源代码，如xdoclet
mvn archetype:generate //创建 Maven 项目
mvn compile //编译源代码
mvn test-compile//编译测试代码
mvn test //运行应用程序中的单元测试
mvn site //生成项目相关信息的网站
mvn clean //清除目标目录中的生成结果
mvn package //依据项目生成 jar 文件
mvn install //在本地 Repository 中安装 jar
mvn eclipse:eclipse //生成 Eclipse 项目文件
```

##### Maven 参数 

    -D 传入属性参数 
    -P 使用pom中指定的配置 
    -e 显示maven运行出错的信息 
    -o 离线执行命令,即不去远程仓库更新包 
    -X 显示maven允许的debug信息 
    -U 强制去远程参考更新snapshot包 
    例如 mvn install -Dmaven.test.skip=true -Poracle 
    其他参数可以通过mvn help 获取

### maven scope（依赖范围控制）说明
>在POM 4中，<dependency>中还引入了<scope>，它主要管理依赖的部署。目前<scope>可以使用5个值：

* compile （编译范围） ：compile是默认的范围；如果没有提供一个范围，那该依赖的范围就是编译范围。编译范围依赖在所有的classpath 中可用，同时它们也会被打包。 

* provided （已提供范围） ：provided 依赖只有在当JDK 或者一个容器已提供该依赖之后才使用。例如， 如果你开发了一个web 应用，你可能在编译 classpath 中需要可用的Servlet API 来编译一个servlet，但是你不会想要在打包好的WAR 中包含这个Servlet API；这个Servlet API JAR 由你的应用服务器或者servlet 容器提供。已提供范围的依赖在编译classpath （不是运行时）可用。它们不是传递性的，也不会被打包。 

* runtime （运行时范围） ：runtime 依赖在运行和测试系统的时候需要，但在编译的时候不需要。比如，你可能在编译的时候只需要JDBC API JAR，而只有在运行的时候才需要JDBC 
驱动实现。

* test （测试范围） ：test范围依赖 在一般的编译和运行时都不需要，它们只有在测试编译和测试运行阶段可用。 

* system （系统范围） ：system范围依赖与provided 类似，但是你必须显式的提供一个对于本地系统中JAR 文件的路径。这么做是为了允许基于本地对象编译，而这些对象是系统类库的一部分。这样的构件应该是一直可用的，Maven 也不会在仓库中去寻找它。如果你将一个依赖范围设置成系统范围，你必须同时提供一个 systemPath 元素。注意该范围是不推荐使用的（你应该一直尽量去从公共或定制的 Maven 仓库中引用依赖）。


### Maven中央仓库——你可能不知道的细节 

* 地址 —— 目前来说，http://repo1.maven.org/maven2/ 是真正的Maven中央仓库的地址，该地址内置在Maven的源码中，其它地址包括著名的 ibiblio.org ，都是镜像。

* 规模 —— 每周有超过来自250,000开发者的70,000,000次访问，2010年的总访问量很有可能超过4,000,000,000。中央仓库存储了超过200,000,000的构件。

* 索引 —— 中央仓库带有索引文件以方便用户对其进行搜索，完整的索引文件大小约为60M，索引每周更新一次。

* 黑名单 —— 如果某个IP地址恶意的下载中央仓库内容，例如全公司100台机器使用同一个IP反复下载，这个IP（甚至是IP段）会进入黑名单，因此稍有规模的使用Maven时，应该用Nexus架设私服。

* 垃圾内容 —— 由于各种历史原因，中央仓库里面确实存在很多垃圾内容，例如不完整的POM，错误的maven-metadata.xml，主要的责任是开源项目上传内容时不太小心，目前中央仓库正致力于更规范的流程以防止新的垃圾内容进入。

* 背后的公司 —— Maven的托管在Apache的，但中央仓库不是Apache的资源，中央仓库是由Sonatype出资维护的。
提交内容 —— 只要你的项目是开源的，而且你能提供完备的POM等信息，你就可以提交项目文件至中央仓库，这可以通过Sonatype提供的开源Maven仓库托管服务实现。

* 非Maven用户 —— 除Maven之外，其它工具如Ivy和Gradle也使用Maven中央仓库。

### 阿里云maven镜像
修改setting.xml配置
```
<mirrors>
    <mirror>
        <id>alimaven</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <mirrorOf>central</mirrorOf>        
    </mirror>
</mirrors>
```
