
PHP：Hypertext Preprocessor超文本预处理器，天然热部署；

- PHP官网：http://php.net/
- PHP官方安装教程：http://php.net/manual/zh/install.php
- Apache官网：http://httpd.apache.org/
- Mysql官网：http://www.mysql.com/
- Eclipse for PHP：http://www.eclipse.org/downloads/

PHP、JAVA比较：

    可复用性：Java》PHP
    开发速度：PHP》JAVA
    易维护性：JAVA》PHP
    可移植性：Java = PHP
    安全性：Java》PHP
    开发费用便宜：PHP》JAVA
    多层架构：JAVA》PHP
    数据库访问统一性：JAVA》PHP
    可扩展性：JAVA》PHP
    面向对象：JAVA》PHP
    
PHP适合于快速开发，中小型应用系统，开发成本低，能够对变动的需求作出快速的反应。而Java适合于开发大型的应用系统，应用的前景比较广阔，系统易维护、可复用性较好。

##### Win7下整合PHP + Apache + Mysql环境

```
1、PHP安装配置
    》下载发行包：配合apache使用需要下载safe版本
    》安装：解压压缩包至指定目录
    》配置：复制php.ini-production命名为php.ini并配置如下
            1.1. 设置PHP扩展包的具体目录：找到
                        ; On windows:
　　　　　　　　; extension_dir = "ext"
                   改为：
　　　　　　　　; On windows:
　　　　　　　　extension_dir = "php安装目录下的ext文件夹的绝对路径（去掉注释符号;）（路径地址中\需要改为为/）"
            1.2. 开启相应的库功能，找到需要开启的库的所在行
　　　　　　　　;extension=php_curl.dll 
　　　　　　　　;extension=php_gd2.dll 
　　　　　　　　;extension=php_mbstring.dll 
　　　　　　　　;extension=php_mysql.dll 
　　　　　　　　;extension=php_xmlrpc.dll 
　　　　　　   去掉前面的分号(注释)，即改为 
　　　　　　　　extension=php_curl.dll 
　　　　　　　　extension=php_gd2.dll 
　　　　　　　　extension=php_mbstring.dll 
　　　　　　　　extension=php_mysql.dll 
　　　　　　　　extension=php_xmlrpc.dll
            1.3. 设置时区，找到
　　　　　　　　;date.timezone =
　　　　　　　 改为 
　　　　　　　　date.timezone = Asia/Shanghai

2、Apache安装配置
    》下载安装包：官网只提供源码，不提供编译后的Windows安装包，可前往官网外链第三方网站下载编译后的win安装包
    （http://httpd.apache.org/download.cgi》选择版本点击Files for Microsoft Windows》点击ApacheHaus》选择版本下载即可）
    （编译版本VC9是指用VS2008编译的代码，而VC11是用VS2012编译的，而用VS2012编译的无法在windows xp和server 2003中使用）
    》安装：解压至指定目录
    》配置：修改conf/httpd.conf文件
            2.1. 扩展Apache支持PHP解析：找到
　　　　　　　　#LoadModule vhost_alias_module modules/mod_vhost_alias.so
　　　　　 在下一行添加 (绿色的位置是根据PHP的所在目录而定的) 
　　　　　　　　LoadModule php5_module "PHP解压目录/php5apache2_2.dll" 
　　　　　　　　PHPIniDir "PHP解压目录"
　　　　　　　　AddType application/x-httpd-php .php .html .htm
            2.2. Apache目录首页支持PHP文件：找到
　　　　　　　　DirectoryIndex index.html
　　　　　 改为 
　　　　　　　　DirectoryIndex index.php index.html
            2.3. 配置Apache服务目录，供配置使用：找到
                        Define SRVROOT "/Apache24"
                改为
                        Define SRVROOT "Apache解压目录"
            2.4. 配置站点目录：找到
　　　　　　　　DocumentRoot "${SRVROOT}/htdocs"
　　　　　　改为 
　　　　　　　　DocumentRoot "E:/programfiles/php-env/php-workspace"
　　　　　　再找到 
　　　　　　　　<Directory "${SRVROOT}/htdocs">
　　　　　　改为 
　　　　　　　　<Directory "E:/programfiles/php-env/php-workspace">
            2.5. 修改端口号：Listen 80
    》启动：双击/bin/httpd.exe
    》注册Windows系统服务，并启动：
            注册服务：
                    # cd e:E:\programfiles\php-env\Apache24\bin
                    # httpd.exe  -k install -n apache
                    查看启动日志，其中，Errors reported here must be corrected before the service can be started.意思是，若该句话后面有错误信息，则表示服务安装失败，需要先改正错误。若没有，则成功。
            卸载服务：sc delete apache
            启动：双击/bin/ApacheMonitor.exe
3、Mysql数据库安装配置：略

测试：Hello World
在站点目录下新建文件index.php，输入内容
<?php
    phpinfo();
?>
打开浏览器输入http://localhost 显示 mysql表格行Client API version即正常且可访问mysql；

Eclipse for PHP 安装配置：
    》下载、解压至指定目录；
    》配置PHP：Windows》Preferences》PHP》PHP Executables》Add》Executables Path指定PHP.exe即可；
    》配置Apache：Windows》Preferences》PHP》Servers》New》配置如下：
            Server Name：Default PHP Web Server
            Base URL：http://localhost 
            Document Root：E:\programfiles\php-env\php-workspace    （该目录即Apache的httpd.conf配置中配置的Directory目录）
            （由于PHP天然热部署，Apache在开发时需要保持启动状态，IDE中只需要配置Apache的Directory目录地址和URL端口即可）
    》新建项目：File》New PHP Project》
```

##### Centos下整合PHP + Apache + Mysql环境

- yum安装php + apache
```
》更换阿里云yum源 （http://mirrors.aliyun.com/help/centos）
# mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup
# wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo 
# yum makecache

》安装apache服务器
# yum install httpd httpd-devel
# /etc/init.d/httpd start
Apache文件目录：/var/www/html

修改配置文件
# vi /usr/local/apache/conf
    找到：
    AddType  application/x-compress .Z
    AddType application/x-gzip .gz .tgz
    在后面添加：
    AddType application/x-httpd-php .php（使Apcche支持PHP）
    AddType application/x-httpd-php-source .php5  
    找到：
    <IfModule dir_module>
    DirectoryIndex index.html
    </IfModule>
    添加：
    <IfModule dir_module>
    DirectoryIndex index.html index.php
    </IfModule>    
    找到：
    ＃ServerName www.example.com:80
    修改为：
    ServerName 127.0.0.1:80或者ServerName localhost:80
    记得要去掉前面的“＃”   

》安装php
# yum install php php-devel
# php -version 
安装php扩展
# yum install php-mysql php-gd php-imap php-ldap php-odbc php-pear php-xml php-xmlrpc
# /etc/init.d/httpd restart    （安装之后重启apache）

mysql-libs 和 MySQL-server冲突  （下载网址：http://rpm.pbone.net/index.php3?stat=3&search=MySQL-shared-compat&srodzaj=3）
那边给出链接：wget  ftp://mirror.switch.ch/pool/1/mirror/mysql/Downloads/MySQL-6.0/MySQL-shared-compat-6.0.11-0.glibc23.x86_64.rpm  
以后运转 rpm -ivh  MySQL-shared-compat-6.0.11-0.glibc23.x86_64.rpm

如果发现php版本陈旧，可卸载
# yum list installed | grep php
# yum remove php.x86_64 php-cli.x86_64 php-common.x86_64 php-gd.x86_64 php-ldap.x86_64 php-mbstring.x86_64 php-mcrypt.x86_64 php-mysql.x86_64 php-pdo.x86_64
```

- 编译安装php5.6.8 + apache2.4

```
》编译安装apache服务器
从  http://httpd.apache.org/download.cgi#apache24  下载apache源码包 httpd-2.4.18.tar.bz2 
从  http://apr.apache.org/download.cgi  下载  apr-1.5.2.tar.gz 和 apr-util-1.5.4.tar.gz 
从  http://sourceforge.net/projects/pcre/files/pcre/8.32/  下载  pcre-8.32.tar.gz 

安装apr:
tar -zvxf apr-1.5.2.tar.gz
cd apr-1.5.2
./configure --prefix=/usr/local/apr
make && make install
安装apr-util
tar -zvxf apr-util-1.5.4.tar.gz
cd apr-util-1.5.4
./configure --prefix=/usr/local/apr-util --with-apr=/usr/local/apr
make && make install
安装pcre
tar -zvxf pcre-8.32.tar.gz
cd pcre-8.32
./configure
make && make install
安装apache 一定要先装上面那三个不然编译不了
tar -jvxf httpd-2.4.18.tar.bz2
cd httpd-2.4.18
./configure --prefix=/usr/local/apache --with-apr-util=/usr/local/apr-util  --with-apr=/usr/local/apr
make && make install

修改配置文件
# vi /usr/local/apache/conf
    找到：
    AddType  application/x-compress .Z
    AddType application/x-gzip .gz .tgz
    在后面添加：
    AddType application/x-httpd-php .php（使Apcche支持PHP）
    AddType application/x-httpd-php-source .php5  
    找到：
    <IfModule dir_module>
    DirectoryIndex index.html
    </IfModule>
    添加：
    <IfModule dir_module>
    DirectoryIndex index.html index.php
    </IfModule>    
    找到：
    ＃ServerName www.example.com:80
    修改为：
    ServerName 127.0.0.1:80或者ServerName localhost:80
    记得要去掉前面的“＃”    

启动apache服务器：
/usr/local/apache/bin/apachectl restart

》编译安装php
安装libmcrypt,去网站http://mcrypt.hellug.gr/lib/index.html 的下载源码，我下的版本是libmcrypt-2.5.7.tar.gz；
# tar -zxvf libmcrypt-2.5.7.tar.gz
# ./configure prefix=/usr/local/libmcrypt/
# make && make install

PHP源码安装：
# yum -y install libxml2-devel     （不装这个编译不了PHP5.5）
# yum install mysql-devel
#  tar -jxvf php-5.6.18.tar.bz2     （php官网下载）
# cd php-5.6.18
# ./configure --prefix=/usr/local/php --with-apxs2=/usr/local/apache/bin/apxs --with-config-file-path=/usr/local/lib --enable-fpm --enable-mbstring --with-curl --with-gd --enable-mysqlnd --with-pdo-mysql=mysqlnd  
# make && make install 
```

问题解决
```
php不支持mysql，未解决
配置文件：
# cp /data/temp/php-5.6.18/php.ini-production /usr/local/lib/php.ini 

添加 PHP 命令到环境变量
# vi /etc/profile
末尾新增： export PATH=$PATH:/usr/local/php/bin
# source /etc/profile
# php -version

php-fpm 服务，开机启动
# cp /usr/local/php/etc/php-fpm.conf.default  /usr/local/php/etc/php-fpm.conf          （设置配置文件）
# cp /data/temp/php-5.6.18/sapi/fpm/init.d.php-fpm /etc/init.d/php-fpm                 （源文件目录下已经生成启动脚本）
# chmod +x /etc/init.d/php-fpm
# service php-fpm start        （php-fpm 可用参数 start|stop|force-quit|restart|reload|status）
# chkconfig php-fpm on     （设置开机启动 ）
# chkconfig --del php-fpm    

》HelloWorld测试
在 /var/www/html/ 目录下新增php文件 写入：<?php phpinfo(); ?> 访问即可；
```

##### wordpress搭建：
```
官网：https://wordpress.org/download/
官网CH：https://cn.wordpress.org/ 
文档：http://codex.wordpress.org/zh-cn:Main_Page （安装说明见压缩包中readme.html文件）

》安装步骤
    1. 准备好PHP环境，Apache环境，Mysql实例；
    2. 将WordPress解压在Apache目录中，启动Apache；
    3. 访问WordPress：http://localhost/wordpress/ 
    4. 按照提示》输入Mysql链接信息和分配的实例名称，将会自动建表》下一步，输入管理账号及邮件等管理信息》Finish，擦，如此简单；
Tips：和node博客hexo或者ghost相比，完善很多，大概就是完整CMS和个人博客的区别，但是，WordPress速度真的是太慢了。
        
》安装主题：
    方式一：登陆后台，外观》主题》上传主题压缩包即可；
    方式二：登陆后台，外观》主题》添加》搜索》点击安装即可；
    方式三：下载主题压缩包，上传至\wordpress\wp-content\themes目录下，进入后台主题功能启用即可；
```

