### Windows环境，JDK安装
***
> **开发环境：同生产环境；生产环境：推荐使用最新稳定版的上一个稳定版本；**

* 1、 下载JDK：[地址](http://www.oracle.com/index.html )
* 2、 安装JDK：自定义安装目录，注意jdk和jre需要放在不同的文件夹中。
* 3、 配置JDK环境变量。位置在 我的电脑->属性->高级->环境变量

```
JAVA_HOME：d:/jdk    
PATH：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin    
CLASSPATH：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar    
```

> 测试JDK是否安装成功：运行->cmd ->java -version

> 环境变量： 环境变量一般是指在操作系统中用来指定操作系统运行环境的一些参数，比如临时文件夹位置和系统文件夹位置等。这点有点类似于DOS时期的默认路径，当你运行某些程序时除了在当前文件夹中寻找外，还会到设置的默认路径中去查找。简单地说这里的“Path”就是一个变量，里面存储了一些常用命令所存放的目录路径。 

> JAVA_HOME：Eclipse/Tomcat等JAVA开发的软件就是通过搜索JAVA_HOME变量来找到并使用安装好的JDK，如果你没有配置JAVA_HOME变量，你会发现Tomcat无法正常启动。 

> PATH：PATH指向搜索命令路径，如果没有配置这个PATH变量指向JDK的命令路径，会发现在命令行下。无法运行javac、java等命令。

> CLASSPATH：CLASSPAH指向类搜索路径，.;表示在当前目录搜索，由于java程序经常要用到lib目录下的dt.jar和tools.jar下类，所以这两项也要加进来，如果在命令行编译和运行的程序还需要用到第三方的jar文件，则也需要把第三方JAR文件加入进来。

### Mac环境，JDK安装
***
* 1、 下载JDK：[地址](http://www.oracle.com/index.html )
* 2、 安装JDK：双击即可。
* 3、 配置JDK环境变量。位置在 我的电脑->属性->高级->环境变量

```
# 查看jdk安装目录 
# /usr/libexec/java_home 
Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home

# 用户目录下，新建.bash_profile文件
cd ~
touch .bash_profile     (如果该文件不存在，将创建一个空文件)
open .bash_profile      (调用记事本编辑该文件)

# 配置jdk环境变量
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

# 配置maven环境变量
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置path
PATH=$PATH:$JAVA_HOME/bin
PATH=$PATH:$MAVEN_HOME/bin
export PATH

# 配置生效
source .bash_profile

```

> 测试JDK是否安装成功：
java -version
mvn -version

### CentOS环境，JDK手动安装
*** 

> 卸载旧版本jdk
```
# java -version
# rpm -qa | grep java
# rpm -e --nodeps java-1.6.0-openjdk-1.6.0.0-1.45.1.11.1.el6.x86_64
```

> 下载JDK
```
# wget http://download.oracle.com/otn/java/jdk/6u45-b06/jdk-6u45-linux-i586.bin?AuthParam=1432052036_e95492a0a833fa9ce7be7b3ce1e9427e
# mkdir -p /usr/local/java
# cp jdk-6u45-linux-x64.bin /usr/local/java/    
# cd /usr/local/java/
```

> 安装JDK
```
#  chmod u+x jdk-6u45-linux-x64.bin  
（或：chmod 777 jdk-6u45-linux-x64.bin）
#  ./jdk-6u45-linux-x64.bin 
（rpm文件安装方式：rpm -ivh jdk-6u45-linux-x64.bin.rpm）
JDK默认安装位置：/usr/local/java/
```

> 配置JDK环境变量
```
#  vi /etc/profile
（向文件里面追加以下内容：）
export JAVA_HOME=/usr/local/java/jdk1.6.0_45
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

# source /etc/profile 
（使修改立即生效）
```

> 验证安装
```
# java
# javac
# java -version
# echo $PATH 
（恭喜，安装成功！）
```


