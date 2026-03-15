[nginx官网](http://nginx.org/en/download.html)

[tomcat官网](http://tomcat.apache.org/)


##### upstream分配方式
[Nginx深入详解之upstream分配方式](http://blog.chinaunix.net/uid-22312037-id-4081140.html)

Nginx的upstream支持5种分配方式，下面将会详细介绍，其中，前三种为Nginx原生支持的分配方式，后两种为第三方支持的分配方式：

- 1、轮询: 轮询是upstream的默认分配方式，即每个请求按照时间顺序轮流分配到不同的后端服务器，如果某个后端服务器down掉后，能自动剔除。

- 2、weight: 轮询的加强版，即可以指定轮询比率，weight和访问几率成正比，主要应用于后端服务器异质的场景下。

- 3、ip_hash: 每个请求按照访问ip（即Nginx的前置服务器或者客户端IP）的hash结果分配，这样每个访客会固定访问一个后端服务器，可以解决session一致问题。

- 4、fair: fair顾名思义，公平地按照后端服务器的响应时间（rt）来分配请求，响应时间短即rt小的后端服务器优先分配请求。

- 5、url_hash : 与ip_hash类似，但是按照访问url的hash结果来分配请求，使得每个url定向到同一个后端服务器，主要应用于后端服务器为缓存时的场景下。
其中，hash_method为使用的hash算法，需要注意的是：此时，server语句中不能加weight等参数。


### Nginx安装配置（centos + yum）(推荐)

- 1、下载当前系统版本nginx的rpm包，建立nginx的yum依赖仓库（centos系统库中默认是没有nginx的rpm包的）
```
rpm -ivh http://nginx.org/packages/centos/6/noarch/RPMS/nginx-release-centos-6-0.el6.ngx.noarch.rpm
```
- 2、查看yum的nginx依赖库信息
```
# yum info nginx
```
- 3、安装并启动nignx
```
yum install nginx
service nginx start
```

默认的配置文件在 /etc/nginx 路径下，使用该配置已经可以正确地运行nginx；如需要自定义，修改其下的 nginx.conf 等文件即可。

在浏览器地址栏中输入部署nginx环境的机器的IP，如果一切正常，应该访问。

### Nginx安装配置（centos + 源码编译）
- 1、安装脚本
```
// 下载nginx源码包
yum install wget
wget -c http://nginx.org/download/nginx-1.8.0.tar.gz

// 解压
# cd /下载目录
# tar -zxv -f nginx-1.8.0.tar.gz

// 删除源码包，移动nginx文件夹路径
rm -rf nginx-1.8.0.tar.gz
mv nginx-1.8.0 /usr/local/nginx

// 安装依赖，编译源码需要
yum install gcc-c++
yum -y install zlib zlib-devel openssl openssl--devel pcre pcre-devel

// 源码编译
cd /usr/local/nginx
./configure --prefix=/usr/local/nginx --conf-path=/usr/local/nginx/nginx.conf
make && make install
```

- 2、启动/重启/停止/
```
// 启动：
/usr/local/nginx/sbin/nginx

// 重新加载：
/usr/local/nginx/sbin/nginx -s reload

// 停止：
/usr/local/nginx/sbin/nginx -s stop

// 查询nginx主进程号：
ps -ef | grep nginx

// 停止进程：
kill -QUIT 主进程号 

// 快速停止:
kill -TERM 主进程号 

// 强制停止：
pkill -9 nginx
```

- 3、开机启动，启动脚本
###### 1、启动脚本位置：
```
# vi /etc/init.d/nginx
```
###### 2、脚本内容如下：
```
################################
#!/bin/bash
# nginx Startup script for the Nginx HTTP Server
# it is v.0.0.2 version.
# chkconfig: - 85 15
# description: Nginx is a high-performance web and proxy server.
# It has a lot of features, but it's not for everyone.
# processname: nginx
# pidfile: /var/run/nginx.pid
# config: /usr/local/nginx/conf/nginx.conf
nginxd=/usr/local/nginx/sbin/nginx
nginx_config=/usr/local/nginx/conf/nginx.conf
nginx_pid=/var/run/nginx.pid
RETVAL=0
prog="nginx"
# Source function library.
. /etc/rc.d/init.d/functions
# Source networking configuration.
. /etc/sysconfig/network
# Check that networking is up.
[ ${NETWORKING} = "no" ] && exit 0
[ -x $nginxd ] || exit 0
# Start nginx daemons functions.
start() {
if [ -e $nginx_pid ];then
echo "nginx already running...."
exit 1
fi
echo -n $"Starting $prog: "
daemon $nginxd -c ${nginx_config}
RETVAL=$?
echo
[ $RETVAL = 0 ] && touch /var/lock/subsys/nginx
return $RETVAL
}
# Stop nginx daemons functions.
stop() {
echo -n $"Stopping $prog: "
killproc $nginxd
RETVAL=$?
echo
[ $RETVAL = 0 ] && rm -f /var/lock/subsys/nginx /var/run/nginx.pid
}
# reload nginx service functions.
reload() {
echo -n $"Reloading $prog: "
#kill -HUP `cat ${nginx_pid}`
killproc $nginxd -HUP
RETVAL=$?
echo
}
# See how we were called.
case "$1" in
start)
start
;;
stop)
stop
;;
reload)
reload
;;
restart)
stop
start
;;
status)
status $prog
RETVAL=$?
;;
*)
echo $"Usage: $prog {start|stop|restart|reload|status|help}"
exit 1
esac
exit $RETVAL
#######################
```

###### 3、更改脚本权限：
```
// (a+x ==> all user can execute所有用户可执行)
chmod a+x /etc/init.d/nginx

或者
chmod 775 /etc/init.d/nginx
```
    
###### 4、设置开机启动：
```
vi /etc/rc.local 
加入一行
/etc/init.d/nginx start

// 保存并退出，下次机器重启会自动启动
```

##### 5、这样在控制台就很容易的操作nginx了
```
/etc/init.d/nginx status
/etc/init.d/nginx start
/etc/init.d/nginx stop
/etc/init.d/nginx restart
/etc/init.d/nginx reload
```

### Nginx + Tomcat，集群配置
##### Nginx，集群配置
```
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

# 001：日志
error_log  /data/applog/nginx_log/error.log	info;

#pid        logs/nginx.pid;

# 002：pid文件
pid        /var/run/nginx.pid;

events {
	# 003：使用网络IO模型linux建议epoll，FreeBSD建议采用kqueue，window下不指定。  
	use epoll;
    worker_connections  1024;
}


http {
    #include       mime.types;
	# 004：设定mime类型
	include       /usr/local/nginx/conf/mime.types;
    default_type  application/octet-stream;

	# 005：设定请求缓冲
	client_header_buffer_size 16k;
	large_client_header_buffers 	4 64K;
	
    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';
	
	# 006：日志格式
	log_format  main  '$remote_addr - $remote_user [$time_local] "$http_host" "$request" "$request_body"'
				  '$status $body_bytes_sent "$http_referer" '
				  '"$http_user_agent" "$http_x_forwarded_for" "$upstream_addr" "$uid_got" "$uid_set"';

    #access_log  logs/access.log  main;
	# 007：日志地址
	access_log	/data/applog/nginx_log/access.log  main;
	
    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;
	
	# 008：负载配置
	upstream admin_servers {
		server 127.0.0.1:8201 weight=5;
    }
	upstream net_servers {
		server 127.0.0.1:8211 weight=5;
		server 127.0.0.1:8212 weight=5;
	}

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

		# 009：路径匹配
		location /admin/ {
            proxy_pass http://admin_servers/admin/;
        }
		location / {
            proxy_pass http://net_servers/;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```

##### Tomcat解压多份
```
// 下载，解压
wget http://mirrors.cnnic.cn/apache/tomcat/tomcat-6/v6.0.44/bin/apache-tomcat-6.0.44.tar.gz
tar -zxvf apache-tomcat-6.0.44.tar.gz

// 创建server目录和log目录
mkdir /data/appdata/tomcat_servers
mkdir /data/appdata/tomcat_roots

// 移动tomcat到指定目录
mv apache-tomcat-6.0.44 /xuxueli/tomcat_servers
cd /xuxueli/tomcat_servers/

// 复制三份
cp -rf apache-tomcat-6.0.44 tomcat01
cp -rf apache-tomcat-6.0.44 tomcat02
cp -rf apache-tomcat-6.0.44 tomcat03
```

##### Tomcat配置server.xml
- 1、四个主要端口配置，在默认值上自增即可；
```
vi /data/appdata/tomcat_servers/tomcat01/conf/server.xml

// 端口1：关闭指令端口，默认8005
<Server port="8005" shutdown="SHUTDOWN">

// 端口2：http端口，默认8080
<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
              
// 端口3：https端口，默认8443
<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
和
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
               
// 端口4：Ajp端口，默认8009
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

- 2、配置server.xml虚拟路径
```
格式：
<Context path="/发布路径" docBase="项目的WebContent" reloadable="false" caseSensitive="false" debug="0"></Context>

例如：
<Context path="/xxl-cfg" docBase="/data/appdata/tomcat_roots/xxl-cfg/ROOT" caseSensitive="false" debug="0"></Context>

// path：项目发布路径，如“/xxl-job”或者“”空表示根路径；
// docBase：项目War包解压目录，即WebContent目录；
// caseSensitive：决定tocmat是否进行大小写检查，默认为true
// reloadable（默认即可）：默认值为false。设置tocmat是否应该监视/WEB-INF/classes和/WEB-INF/lib中的变化，如果有发生改变，则自动重新部署；
// debug=0表示尽可能少的调试信息
```

##### Tomcat启动脚本：

新建启动脚本：
```
# vi /data/tomcat_startup.sh
```

内容如下：
```
###############################################
tomcat_home=/data/appdata/tomcat_servers/$1
start() {
echo -n "Starting tomcat: "
#rm $tomcat_home/logs/* -rf
rm $tomcat_home/work/* -rf
$tomcat_home/bin/startup.sh
tail -f $tomcat_home/logs/catalina.out
echo "tomcat start ok."
}
stop() {
echo -n "Shutting down tomcat: "
$tomcat_home/bin/shutdown.sh
echo "tomcat stop ok."
}
# See how we were called
case "$2" in
start)
start
;;
stop)
stop
;;
restart)
stop
sleep 3
start
;;
*)
echo "Usage: $0 {start|stop|restart}"
esac
exit 0
######################################################
```

添加执行权限：
```
chmod 777 /data/tomcat_startup.sh
```

启动项目：
```
sh /data/tomcat_startup.sh xxl-cfg start
```


### Window注册tomcat为系统服务：

进入tomcat的bin目录下，执行以下命令（service.bat不存在，下载64bit，里面有）：

```
注册服务：service.bat install tomcat8080
删除服务：service.bat uninstall tomcat8080
启动服务：net start tomcat8080
停止服务：net stop tomcat8080 
```

报错：Nonalpha 95   
原因：Windows2003操作系统的服务名称中不能出现下划线"_" ；








