<h2 style="color:#4db6ac !important" >Java开发环境</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]

## JDK安装
[JDK下载地址](http://www.oracle.com/index.html )

**Mac下安装**

```
# 查看jdk安装目录 
/Library/Java/JavaVirtualMachines/jdk-1.8.jdk/Contents/Home

# 用户目录下，新建 .bash_profile 文件
cd ~
touch .bash_profile
open .bash_profile

# 配置jdk环境变量
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-1.8.jdk/Contents/Home

# 配置maven环境变量
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置path
PATH=$PATH:$JAVA_HOME/bin
PATH=$PATH:$MAVEN_HOME/bin
export PATH

# 配置生效
source .bash_profile
```

**Windows下安装**

- 安装JDK：自定义安装目录，注意jdk和jre需要放在不同的文件夹中。
- 环境变量：（位置在 我的电脑->属性->高级->环境变量）环境变量一般是指在操作系统中用来指定操作系统运行环境的一些参数，比如临时文件夹位置和系统文件夹位置等。这点有点类似于DOS时期的默认路径，当你运行某些程序时除了在当前文件夹中寻找外，还会到设置的默认路径中去查找。简单地说这里的“Path”就是一个变量，里面存储了一些常用命令所存放的目录路径。
    - JAVA_HOME：Eclipse/Tomcat等JAVA开发的软件就是通过搜索JAVA_HOME变量来找到并使用安装好的JDK，如果你没有配置JAVA_HOME变量，你会发现Tomcat无法正常启动。
    - PATH：PATH指向搜索命令路径，如果没有配置这个PATH变量指向JDK的命令路径，会发现在命令行下。无法运行javac、java等命令。
    - CLASSPATH：CLASSPAH指向类搜索路径，.;表示在当前目录搜索，由于java程序经常要用到lib目录下的dt.jar和tools.jar下类，所以这两项也要加进来，如果在命令行编译和运行的程序还需要用到第三方的jar文件，则也需要把第三方JAR文件加入进来。

```
JAVA_HOME：d:/jdk    
PATH：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin    
CLASSPATH：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar    
``` 

** CentOS下安装 **

```
### 卸载旧版本jdk
java -version
rpm -qa | grep java
rpm -e --nodeps java-1.6.0-openjdk-1.6.0.0-1.45.1.11.1.el6.x86_64

### 下载JDK
wget http://download.oracle.com/otn/java/jdk/6u45-b06/jdk-6u45-linux-i586.bin?AuthParam=1432052036_e95492a0a833fa9ce7be7b3ce1e9427e
mkdir -p /usr/local/java
cp jdk-6u45-linux-x64.bin /usr/local/java/    
cd /usr/local/java/

### 安装JDK (默认安装位置：/usr/local/java/)
chmod u+x jdk-6u45-linux-x64.bin    （或：chmod 777 jdk-6u45-linux-x64.bin）
./jdk-6u45-linux-x64.bin            （rpm文件安装方式：rpm -ivh jdk-6u45-linux-x64.bin.rpm）

### 配置JDK环境变量
vi /etc/profile         （向文件里面追加以下内容：）

export JAVA_HOME=/usr/local/java/jdk1.6.0_45
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

### 使修改立即生效
#source /etc/profile 

### 验证安装
java
javac
java -version
echo $PATH 
```

## Maven安装
- [官网](http://maven.apache.org/download.cgi)
- [官方教程](http://maven.apache.org/guides/getting-started/index.html)
- [官方五分钟入门](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

**前提**
>正确安装JDK，并配置环境变量

**Mac下安装**

```
# 用户目录下，新建.bash_profile文件
cd ~
touch .bash_profile
open .bash_profile

# 配置maven环境变量（maven解压目录）
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置path
PATH=$PATH:$MAVEN_HOME/bin
export PATH

# 配置生效
source .bash_profile
```

>验证：在terminal下 "mvn -version" 确认
>配置本地仓库：在Maven解压安装目录下 “\conf\settings.xml” 文件中配置

```
<localRepository>/Users/xuxueli/workspaces/maven-libs</localRepository>
```

**Windows下安装**
>解压安装，并配置Windows环境变量：

```
MAVEN_HOME=D://maven
PATH=%MAVEN_HOME%\bin;  + 其他
```

验证：在dos下 "mvn -version" 确认
配置本地仓库：在Maven解压安装目录下 “\conf\settings.xml” 文件中配置
```
<localRepository>e:/work/maven_lib</localRepository>
```

** 常用命令 **
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

// etc
mvn -Dhttps.protocols=TLSv1.2   // 启用 TLSv1.2 协议，maven仓库要求
```

**Maven 参数**

    -D 传入属性参数 
    -P 使用pom中指定的配置 
    -e 显示maven运行出错的信息 
    -o 离线执行命令,即不去远程仓库更新包 
    -X 显示maven允许的debug信息 
    -U 强制去远程参考更新snapshot包 
    例如 mvn install -Dmaven.test.skip=true -Poracle 
    其他参数可以通过mvn help 获取

**maven scope（依赖范围控制）说明**

在POM 4中，<dependency>中还引入了<scope>，它主要管理依赖的部署。目前<scope>可以使用5个值：

- **compile （编译范围）**：compile是默认的范围；如果没有提供一个范围，那该依赖的范围就是编译范围。编译范围依赖在所有的classpath 中可用，同时它们也会被打包。
- **provided （已提供范围）**：provided 依赖只有在当JDK 或者一个容器已提供该依赖之后才使用。例如， 如果你开发了一个web 应用，你可能在编译 classpath 中需要可用的Servlet API 来编译一个servlet，但是你不会想要在打包好的WAR 中包含这个Servlet API；这个Servlet API JAR 由你的应用服务器或者servlet 容器提供。已提供范围的依赖在编译classpath （不是运行时）可用。它们不是传递性的，也不会被打包。
- **runtime （运行时范围）**：runtime 依赖在运行和测试系统的时候需要，但在编译的时候不需要。比如，你可能在编译的时候只需要JDBC API JAR，而只有在运行的时候才需要JDBC 驱动实现。
- **test （测试范围）**：test范围依赖 在一般的编译和运行时都不需要，它们只有在测试编译和测试运行阶段可用。
- **system （系统范围）** ：system范围依赖与provided 类似，但是你必须显式的提供一个对于本地系统中JAR 文件的路径。这么做是为了允许基于本地对象编译，而这些对象是系统类库的一部分。这样的构件应该是一直可用的，Maven 也不会在仓库中去寻找它。如果你将一个依赖范围设置成系统范围，你必须同时提供一个 systemPath 元素。注意该范围是不推荐使用的（你应该一直尽量去从公共或定制的 Maven 仓库中引用依赖）。

## GIT安装
- [Git 常用命令速查表(图文+表格)](http://www.jb51.net/article/55442.htm)
- [Git 教程](http://www.runoob.com/git/git-tutorial.html)

**安装方式1：Command Line Tools（内置Git）**     
终端中输入 ```git``` 命令会弹出对话框提示安装 "Command Line Tools"，选择确认后自动安装，该工具内置Git、Gcc和Make等。

**安装方式2：homebrew 安装**
终端proxy & 安装homebrew
```
# 终端启用代理命令(确定端口：SS》高级设置》socks端口)
export http_proxy='http://localhost:{端口}'
export https_proxy='http://localhost:{端口}'

# 安装homebrew安装领命（安装命令：https://brew.sh/ ）
```

brew方式安装git
```
sudo brew install git
```

**GIT常用命令汇总**

```
// 初始化
cd git-workspace
git init
git clone http://xxxxx.git
cd xxxxx

// 查看
git status
git branch -a
git branch -r

// 拉取master分支到本地
git fetch origin master:master
git checkout master

// 获取远程分支master并merge到当前分支 
git fetch origin master
git pull origin master 

// 获取所有远程分支，并merge到本地分支
git fetch
git pull

// 在master基础上，新建分支，推送分支
git checkout master
git fetch origin master
git checkout -b XXX
git push origin XXX

// 加入缓存，提交代码，并push分支
git add xxx.java
git commit -m "init project"
git push orgin XXX

// 在 branch_a 分支上 merge 分支 master
git checkout branch_a
git merge master
git push orgin branch_a

// 在 branch_a 分支上 rebase 分支 master （不推荐）
git checkout branch_a
git rebase master
git push orgin branch_a
// (merge操作会生成一个新的节点，之前的提交分开显示。而rebase操作不会生成新的节点，是将两个分支融合成一个线性的提交，之前分支就没有了。)

// 删除分支，大写D强制删除，push远程删除
git branch -d XXX
git branch -D XXX
git push origin  :XXXX

// 文件加入/移除stage（加入stage才可commit和push）
git add xxx.imi
git reset HEAD xxl.imi

// .gitignore文件
加入.gitingore文件中的文件，不会被 “git status(检测未被git管理、git管理下被修改但未被commit和push的文件)”检测到；
git rm --cached file/path/to/be/ignored

// 冲突解决
add 
commit

// 撤消上一个commit，但保留add的文件
git reset --soft HEAD~1

// 生成公钥，默认位置：~/.ssh
$ ssh-keygen -t rsa -C "xxx@gmail.com"
cat .\.ssh\id_rsa.pub
》》New SSH key
ssh -T git@github.com

// 更新仓库地址
git remote set-url origin remote_git_address

// 更新config
git config --list
git config user.name
git config user.email   // query
git config user.email "email info"  // update each
git config --global user.email "email info"  // update global

// 回滚commit
git log
git reset --hard <commit_id>
git push origin HEAD --force

// 放弃本地的修改，用远程的库覆盖本地
git fetch --all
git reset --hard origin/master

// 强制覆盖推送
git push -f origin/bbbbbb
```

**Git常用命令**

```
// 常用命令汇总
git clone <url>	clone远程版本库
git status	查看状态
git diff	查看变更内容
git add .	跟踪所有改动过的文件
git add <file>	跟踪指定的文件
git mv <old> <new>	文件改名
git rm <file>	删除文件
git rm --cached <file>	停止跟踪文件但不删除
git commit -m "commit message"	提交所有更新过的文件
git log	查看提交历史
git reset --hard HEAD	撤销工作目录中所有未提交文件的修改内容
git checkout HEAD <file>	撤销指定的未提交文件的修改内容
git revert <commit>	撤销指定的提交
git branch	显示所有本地分支
git checkout <branch/tag>	切换到指定分支或标签
git branch <new-branch>	创建新分支
git branch -d <branch>	删除本地分支
git merge <branch>	合并指定分支到当前分支
git rebase <branch>	Rebase指定分支到当前分支
git remote -v	查看远程版本库信息
git remote show <remote>	查看指定远程版本库信息
git remote add <remote> <url>	添加远程版本库
git fetch <remote>	从远程库获取代码
git pull <remote> <branch>	下载代码合并到当前分支
git push <remote> <branch>	上传代码到远程
git push <remote> :<branch/tag>	删除远程分支或标签

```

## Intellij安装

**安装注册**
- [下载地址](http://www.jetbrains.com/idea/)

* 1、JDK：
 
开发版本/JDK：
```
File》Other Setting》Default Project Structure》Platform Settings》SDKs》+JDK》选择全局jdk目录;
File》Project Structure》Platform Settings》SDKs》+JDK》选择项目jdk目录
```
编译版本/Compiler：
```
    Preferences》Default Settings》Build、Execution、Deployment》Java Compiler》Project bytecode version》置空，编译版本自动和开发版本一致；
    File》Other Settint》Default Settings》Build、Execution、Deployment》Java Compiler》Project bytecode version》置空，编译版本自动和开发版本一致；
```
* 2、maven设置：

maven配置
```
File》Other Setting》Default Setting》Build、Execution、Deployment》Build Tools》Maven》配置maven安装目录和settin文件
File》Setting》Build、Execution、Deployment》Build Tools》Maven》配置maven安装目录和settin文件
```
配置项
```
Maven home directory：maven安装目录 (Override)
User settings file：setting文件目录 (Override)
Local repository： 默认即可
```

Tips：推荐依赖树查看插件 Maven Helper


* 3、Git配置：

```
配置GIT：安装Git，配置Git：File》Setting》Version Control》Git》Path to Git executable选择git.exe地址；

下载Git仓库：VCS》Checkout from Version Control》Git》...

导入Git项目：File》New》Module from exists Sources》Git仓库中项目；

非Git仓库顶级目录下项目，关联Git：VCS》Version Control Operation

多git仓库同时使用：File》Setting》Version Control》点击+新增git本地仓库地址；

取消git自动tracked文件，导致gitignore失效：File》Setting》Version Control》When fils are created》勾选Do not add;
```

* 4、idea热部署
  >1.Build,Excution,Deployment>>Make project automatically（勾选）
  >2.CTRL + SHIFT + A --> 查找Registry;  勾选  compiler.automake.allow.when.app.running;
  springboot自动部署参考文档：https://blog.csdn.net/a1273022039/article/details/79590681
  >3. Edit Configurations --> 'On Update Action' 勾 'update classes and resources'


* 5、快捷键
官网文档很完善：Help》KeyMap Preference》双击即可查看快捷键表；

* 6、VM options参数调优

```
-Xmx2048m           // 堆内存最大值
```


## Mac根目录创建 data 目录

参考：[link](https://www.cnblogs.com/Alandre/p/15474716.html)