# 客户端工具
Navicat Premium
- 下载：官网下载即可，注册码激活即可；
- 全平台：Windows、Mac均兼容；
- 官方已经汉化：官网，右上角切换中文站，可下载中文版本；

[Navicate官网](https://www.navicat.com.cn/download/navicat-premium)

- Windows注册
    - [方式1：绿色，复制文件即可](http://download.csdn.net/download/yuzhouneng/9960916)
- Mac注册
    - [方式1：Mac定制版本](http://xclient.info/s/navicat-premium.html) （提示已损坏，其实是因为权限导致，页面有解决方案）
    - [方式2.1：Mac注册1](http://blog.csdn.net/sunnyboy9/article/details/44704415)
    - [方式2.2：Mac注册2](http://bbs.feng.com/read-htm-tid-8156171.html)

（推荐使用正版，上述内容来源网络仅作为学习使用）

# 1. Win7环境
---
### 下载：
- Mysql下载地址：http://www.mysql.com/downloads/mysql/
- Sohu镜像站下载地址：http://mirrors.sohu.com/mysql/ 

Tips：  

    版本，选择和线上版本一致；格式，选择msi安装包；机器位数，32/64位；

版本的选择：
    
    1. MySQL Community Server 社区版本，开源免费，但不提供官方技术支持。
    2. MySQL Enterprise Edition 企业版本，需付费，可以试用30天。
    3. MySQL Cluster 集群版，开源免费。可将几个MySQL Server封装成一个Server。
    4. MySQL Cluster CGE 高级集群版，需付费。
    5. MySQL Workbench（GUI TOOL）一款专为MySQL设计的ER/数据库建模工具。它是著名的数据库设计工具DBDesigner4的继任者。MySQL Workbench又分为两个版本，分别是社区版（MySQL Workbench OSS）、商用版（MySQL Workbench SE）。
    MySQL Community Server 是开源免费的，这也是我们通常用的MySQL的版本。根据不同的操作系统平台细分为多个版本，下面我们以windows平台为例来说明。

### 安装：
- 1、双击msi进入安装界面，接受条款；
- 2、Choose Setup Type：Typical是指典型安装，Complete是完全安装，Custom是自定义安装，那么我们选择自定义安装；
- 3、安装项选择 “Mysql Server”,右下角指定安装目录，数据目录，相同数据目录下次安装可恢复数据；
- 4、安装完成，勾选Launch the Mysql Instance Configuration Wizard，点击Finish进去配置界面；
- 6、选择Server类型，端口号，服务名称：开发测试，mysql 占用很少资源；Server Machine：服务器类型，mysql占用较多资源；Dedicated MySQL Server Machine：专门的数据库服务器，mysql占用所有可用资源；我选择”Developer Machine“，点击Next；
- 7、输入Root账号密码；
- 8、选择用途：“Multifunctional Database”：通用多功能型；“Transactional Database Only”：服务器类型，专注于事务处理；“Non-Transactional Database Only”：非事务处理型，较简单，主要做一些监控、记数用，对MyISAM数据类型的支持仅限于non-transactional；我选择”Multifunctional Database“；
- 9、“Ennable TCP、/IP Networking”，是否启用TCP/IP链接；“Port Number”，默认端口3306；“Add firewall exception for this port”，将监听端口，加入防火墙之外，避免防火墙阻断；“Enable Strict Mode（启用标准模式）”，选中，Mysql不会允许细小的语法错误，尽量使用标准模式，降低非法数据可能，养成好习惯；
- 10、“Standard Charater Set”，西文编码Latin1；“Best Support For Multilingualism”，多字节的通用utf8编码；"Manual Selected Default Charater Set/ Clllation"，自定义编码；我选择UTF-8，它是国际编码，通用性好，而且线上也是此编码；
- 11、“Install As Windows Service”，勾选，安装位windows服务，可自定义服务名称；“Launch the Mysql Server aotumatically”，开机启动；“Include Bin Directory in Window Path”，勾选后，cmd中可直接使用目录下命令，而不必切换到该目录；我选择全部勾选；
- 12、”Enable root access from remotemachines“表示是否允许root 用户在其它的机器上登陆，如果要安全，就不要勾上，根据需要；“Create An Anonymous Account”表示新建一个匿名用户，匿名用户可以连接数据库但不能操作数据库，包括查询，一般就不用勾了，然后“Next”；
- 13、点击”Execute“。成功页点击“Finish”就OK了。
- 14、在开始菜单找到刚刚新安装的mysql,选择command line client - Unicode。输入密码，此时出现的界面表示成功安装，输入show databases:验证；

### 乱码解决
安装目录（数据目录）下》my.ini》修改两处
```
default-character-set=utf8
……
character-set-server=utf8
```
重启

# 2. Mac环境
---
### 下载
[官网MySQL地址](http://www.mysql.com/downloads/mysql/)

Tips：版本，选择和线上服务一直5.6；格式，选择dmg；（mysql-5.6.31-osx10.11-x86_64.dmg）

### 安装
[Mac OS X Yosemite 上安装 MySql 5.6.26的几个坑](http://my.oschina.net/u/211651/blog/508268)

    1、双击dmg文件，双击安装包界面中的“mysql-5.6.31-osx10.11-x86_64.pkg”图标；
    2、点击Next，最终“系统偏好设置”中会出现MySQL图标；
    3、点击设置中MySQL图片，可启动/关闭MySQL，设置自启动等；

### 配置Mysql环境变量（为了方便执行命令行）
```
# 配置jdk环境变量
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

# 配置maven环境变量
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置mysql
export MYSQL=/usr/local/mysql/bin

# 配置path
PATH=$JAVA_HOME/bin:$PATH
PATH=$MAVEN_HOME/bin:$PATH
PATH=$MYSQL:$PATH
export PATH

#alias
alias ll='ls -al'
```

### 设置用户root密码（默认root没有密码）
```
mysql -u root mysql
UPDATE mysql.user SET Password = PASSWORD('root_pwd') WHERE User = 'root';
FLUSH PRIVILEGES;
```

### 解决中文乱码问题
```
// 修改my.cnf配置文件,1、在[mysqld]字段里加入character-set-server=utf8，如下：

cd /usr/local/mysql/support-files
sudo cp my-default.cnf /etc/my.cnf

// 在 [client] 后添加
[client]
default-character-set=utf8

// 在 [mysqld] 后添加
[mysqld]
default-storage-engine=INNODB
character-set-server=utf8
collation-server=utf8_general_ci

// 重启mysql即可 (推荐在系统设置中启动，该方法启动不会被系统设置跟踪到)
#cd /usr/local/mysql/support-files
#./mysql.server restart
```

# 3、CentOS

Tips：yum方式安装mysql（yum install mysql）简单，略过；下文为rpm源码方式安装；

### 卸载旧版本
```
rpm -qa | grep -i mysql
rpm -e --nodeps MySQL-server-5.6.24-1.el6.i686
yum -y remove mysql-libs*
```

### 下载
- Mysql下载地址：http://www.mysql.com/downloads/mysql/
- Sohu镜像站下载地址：http://mirrors.sohu.com/mysql/ 

```
mkdir /usr/local/mysql
cd  /usr/local/mysql
wget http://mirrors.sohu.com/mysql/MySQL-5.6/MySQL-devel-5.6.24-1.el6.i686.rpm
wget http://mirrors.sohu.com/mysql/MySQL-5.6/MySQL-client-5.6.24-1.el6.i686.rpm
wget http://mirrors.sohu.com/mysql/MySQL-5.6/MySQL-server-5.6.24-1.el6.i686.rpm
```

MySQL的默认安装位置

    /var/lib/mysql/           #数据库目录
    /usr/share/mysql        #配置文件目录
    /usr/bin                       #相关命令目录
    /etc/init.d/mysql         #启动脚本

mysql数据库的主要配置文件   

    1./etc/my.cnf 这是mysql的主配置文件
    2./var/lib/mysql   mysql数据库的数据库文件存放位置
    3./var/log mysql数据库的日志输出存放位置
    
### 安装

```
rpm -ivh MySQL-devel-5.6.24-1.el6.i686.rpm
rpm -ivh MySQL-client-5.6.24-1.el6.i686.rpm
rpm -ivh MySQL-server-5.6.24-1.el6.i686.rpm
```

### 初始化MySQL（安装server的时候如果自动安装，则不需要执行）
```
/usr/bin/mysql_install_db   
```

报错： libc.so.6 is neede
```
//按照所需，yum install或者yum update即可
yum install libc.so.6
```

### 设置初始密码：

```
// 查看root账号原始密码，随机密码：_yuR9K_aSihIUYW6
cat /root/.mysql_secret

// 用原始密码登录
mysql -uroot -p
Enter password: 上面的随机密码

# 设置新密码
mysql> SET PASSWORD = PASSWORD('root_pwd');
mysql> flush privileges;
mysql> exit;

// 用新密码登录
# mysql -uroot -p
Enter password: root_pwd
```

报错： Can't connect to local MySQL server through socket '/var/lib/mysql/mysql.sock' 
```
# chown -R mysql:mysql /var/lib/mysql
```
重启

### 重置密码
1、修改安装目录
下my.ini文件，添加：skip-grant-tables    

2、重启mysql；net stop mysql/net start mysql;

3、执行命令：“mysql -uroot -p”回车，再回车，可直接进去mysql界面；

4、执行以下命令：
```
use mysql;
update user set password = password('root_pwd') where user = 'root';
flush privileges;
quit;
```
5、删掉my.ini中加入哪行，重启mysql；

### 开启远程访问：
```
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root_pwd' WITH GRANT OPTION;
FLUSH PRIVILEGES;
exit
```

### 修改字符集utf8和数据存储路径

```
// 查看字符集
mysql> show variables like '%char%';

// 修改字符集
cp /usr/share/doc/MySQL-server-5.6.24/my-default.cnf  /etc/my.cnf
vi  /etc/my.cnf

修改my.cnf配置文件
1、在[mysqld]字段里加入character-set-server=utf8，如下：
[mysqld]
port = 3306
socket=/var/lib/mysql/mysql.sock
character-set-server=utf8

2、在[client]字段里加入default-character-set=utf8，如下：
[client] 
port = 3306
socket=/var/lib/mysql/mysql.sock
default-character-set=utf8
```

### Unknown table engine ‘InnoDb’的解决办法
缺少InnoDb数据库引擎
```
mysql> show engines;
// 显示 InnoDB 的support为NO；确实是InnoDB引擎没启用。

// 修改MySQL安装目录下的my.ini，在skip-innodb前增加#，重启MySQL，再次通过命令行查看结果为：

mysql> show engines;
// 显示 InnoDB 的support为YES；
// 启用InnoDB引擎会消耗内存和硬盘空间，如非必要不建议启用。

```


### 启动mysql
如果我们是第一次启动mysql服务，提示非常多的信息，mysql服务器首先会进行初始化的配置
```
// 启动mysql服务
service mysqld start
// 重启：
service mysqld restart
```

### 开机启动  
```
chkconfig --list | grep mysql
chkconfig mysql on
service mysql start
```

### 防火墙，开放3306端口
```
// 安装防火墙
yum install -y iptables

// 启动/关闭防火墙（临时，重启失效）
service iptables start
service iptables stop

// 启动/关闭防火墙（永久）
chkconfig iptables on
chkconfig iptables off

// 查看防火墙端口
/etc/init.d/iptables status    
service iptables status

// 开启端口（临时，save后永久）
/sbin/iptables -I INPUT -p tcp --dport 22 -j ACCEPT
/sbin/iptables -I INPUT -p tcp --dport 3306 -j ACCEPT

// 删除端口：删除INPUT链中的iptables规则（临时，save后永久）
// 删除指定的第1行规则
iptables -D INPUT 1     

// 保存修改（save后永久）
/etc/rc.d/init.d/iptables save
```

### mysql定时备份

Myql备份shell脚本
```
###################
#! /bin/bash
curr=`date +%Y%m%d%H%M%S`
mkdir /home/root/mysql_bak/$curr
mysqldump -uroot -p123456 test     > /home/root/mysql_bak/$curr/test-$curr.sql    
mysqldump -uroot -p123456 test02 > /home/root/mysql_bak/$curr/test02-$curr.sql    
###################

vi /home/root/mysql_bak.sh
chmod +x mysql_bak.sh 
```

配置Crontab
```
// 安装crontabs
yum install -y vixie-cron
yum install crontabs

// 1、手动备份： 
sh mysql_bak.sh

// 2、crond定时启动
vi /etc/crontab
// 加入下面这行，每天在1点钟执行备份
0 0 1 * * * root /home/root/mysql_bak.sh

// 重启crond生效
/etc/rc.d/init.d/crond restart
service crond stop

// crontabs开机启动
chkconfig –level 35 crond on
```

[Crontab命令的书写格式](http://www.centoscn.com/CentOS/help/2014/0820/3524.html)

### mysql数据还原
```
mysql -uroot -p123456 test < /home/root/mysql_bak/20150728045101/test-20150728045101.sql
```
