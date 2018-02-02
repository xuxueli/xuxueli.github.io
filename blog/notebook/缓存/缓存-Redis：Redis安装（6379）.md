### redis简介
---
redis是一个key-value存储系统。和Memcached类似，它支持存储的value类型相对更多，包括string(字符串)、list(链表)、set(集合)、zset(sorted set --有序集合)和hash（哈希类型）。

这些数据类型都支持push/pop、add/remove及取交集并集和差集及更丰富的操作，而且这些操作都是原子性的。

在此基础上，redis支持各种不同方式的排序。与memcached一样，为了保证效率，数据都是缓存在内存中。区别的是redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了master-slave(主从)同步。

Redis 是一个高性能的key-value数据库。

redis的出现，很大程度补偿了memcached这类key/value存储的不足，在部 分场合可以对关系数据库起到很好的补充作用。它提供了Java，C/C++，C#，PHP，JavaScript，Perl，Object-C，Python，Ruby，Erlang等客户端，使用很方便。


[官网](http://redis.io/)    
[github地址](https://github.com/antirez/redis)  
[redis中文api文档](http://doc.redisfans.com/)

### redis实现多级缓存
技术栈：redis缓存 + redis广播 + ehcache

    机器A操作缓存：redis.set + redis.puh
    机器B更新缓存：redis.sub + ehcache.set
    机器B查询缓存：ehcache.get + redis.get


### memcached、redis、mongodb区别与联系
##### mongodb：
mongodb是文档型的非关系型数据库，其优势在于查询功能比较强大，能存储海量数据。
mongodb和memcached/redis不是一个范畴内的东西，不存在谁替换谁的问题。

##### memcached、redis相同点：
内存内、键值数据存储方案

优缺点：
- 数据保存在内存中，通过tcp直接存取，优势是速度快，并发高，
- 缺点是数据类型有限，查询功能不强，一般用作缓存。

##### memcached、redis不同点：
- 1、redis具有持久化机制，可以定期将内存中的数据持久化到硬盘上。
- 2、redis具备binlog功能，可以将所有操作写入日志，当redis出现故障，可依照binlog进行数据恢复。
- 3、redis原生支持的数据类型更多，使用的想象空间更大。
- 4、Redis是单线程运行，所以和Memcached的多线程相比，整体性能肯定会偏低。因为是单线程运行，所以IO是串行化的，网络IO和内存IO，因此当单条数据太大时，由于需要等待一个命令的所有IO完成才能进行后续的命令，所以性能会受影响。
- 5、Memcached的数据回收机制使用的是LRU(即最低近期使用量)算法，而且往往会比较武断地直接删除掉与新数据体系相近的原有内容。
相比之下，Redis允许用户更为精准地进行细化控制，利用六种不同回收策略确切提高缓存资源的实际利用率。Redis还采用更为复杂的内存管理与回收对象备选方案。redis支持virtual memory，可以限定内存使用大小，当数据超过阈值，则通过类似LRU的算法把内存中的最不常用数据保存到硬盘的页面文件中。
- 6、Memcached只支持一种数据类型，字符串（内存管理机制虽然不像Redis的那样复杂，但却更具实际效率），在处理元数据时所消耗的内存资源相对更少，非常适合用于保存那些只需要进行读取操作的数据，小型静态数据进行缓存处理，最具代表性的例子就是HTML代码片段。
- 7、Memcached将键名限制在250字节，值也被限制在不超过1MB，且只适用于普通字符串。
相比之下，Redis则将键名与值的最大上限各自设定为512MB，且支持二进制格式。Redis支持六种数据类型。
- 8、Redis散列机制的存在保证开发人员无需经历获取完整字符串、反序列化、更新值、对象重新序列化并在每次值更新后利用其替代缓存内完整字符串这一系列复杂的流程——这也意味着资源消耗量得以降低、性能表现迎来显著提升。Redis所支持的其它数据类型，例如Lists以及Sets——也可被用于实现更加复杂的缓存管理模式。


### redis项目中应用
---
参考xxl-search项目 [地址](https://github.com/xuxueli/xxl-incubator)

如需操作集合，可查看 [教程](http://javacrazyer.iteye.com/blog/1840161)

### redis安装，yum方式（centos需要epel支持）
---
##### EPEL安装步骤
[EPEL官网](https://fedoraproject.org/wiki/EPEL/zh-cn)
- 1、安装yum优先级插件
```
yum install yum-priorities
```
- 2、安装epel
```
// 命令安装
yum -y install epel-release
// 或者：手动安装，选择对应版本的地址
rpm -vih http://dl.fedoraproject.org/pub/epel/7/x86_64/e/epel-release-7-2.noarch.rpm

```
- 3、检查是否安装成功
```
rpm -q epel-release
```
- 4、更新yum缓存
```
yum clean all && yum makecache
```

##### yum安装redis
- 1、安装
```
yum install redis

// 操作
service redis start
service redis restart
service redis stop
```
- 2、测试
```
redis-cli
set key "hello world"
get key
```

-3、修改redis配置，修改限制访问IP
```
// 默认位置“/etc/redis.conf”
vi /etc/redis.conf

注释掉下面一行，本行功能限制指定IP访问
#bind 127.0.0.1
```

- 4、开放Redis端口：6379（默认）


### redis安装，源码编译方式
---
安装编译工具：
```
yum install wget make gcc gcc-c++ zlib-devel openssl openssl-devel pcre-devel kernel keyutils patch perl
```
安装Redis需要tcl支持：
```
yum install -y tcl
```
下载安装redis：
```
wget http://download.redis.io/releases/redis-3.0.2.tar.gz
tar xvzf redis-3.0.2.tar.gz
mv redis-3.0.2 /home/root
cd /home/root/redis-3.0.2

make
make test
make install
```

在make成功以后，会在src目录下多出一些可执行文件：redis-server，redis-cli等等
测试通过后安装，安装后会自动把redis-server,redis-cli,redis-benchmark,redis-check-aof,redis-check-dump复制到/usr/local/bin目录下。


**控制台启动（默认端口6379）：**    
```
// 配置文件见安装目录下：redis.conf
/home/root/redis-3.0.2/src/redis-server
```

**系统服务(守护进程) + 开机启动：**     
- 1、安装的install的时候，redis的命令会被拷贝到/usr/local/bin下面

- 2、配置 redis.conf ：
```
// 将配置“redis.conf”拷贝到“/usr/local/bin”目录下
cp /home/root/redis-3.0.2/redis.conf /usr/local/bin
cd /usr/local/bin

// 修改redis配置
vi redis.conf
将 “daemonize no“改成”daemonize yes”，即支持后台启动
```

3、redis自启动脚本：
    
新建脚本：
```
vi /etc/init.d/redis
```
脚本内容：
```
###########################
#chkconfig: 2345 10 90
#description: Start and Stop redis
PATH=/usr/local/bin:/sbin:/usr/bin:/bin
REDISPORT=6379
EXEC=/usr/local/bin/redis-server
REDIS_CLI=/usr/local/bin/redis-cli
PIDFILE=/var/run/redis.pid
CONF="/usr/local/bin/redis.conf"
case "$1" in
start)
if [ -f $PIDFILE ]
then
echo "$PIDFILE exists, process is already running or crashed"
else
echo "Starting Redis server..."
$EXEC $CONF
fi
if [ "$?"="0" ]
then
echo "Redis is running..."
fi
;;
stop)
if [ ! -f $PIDFILE ]
then
echo "$PIDFILE does not exist, process is not running"
else
PID=$(cat $PIDFILE)
echo "Stopping ..."
$REDIS_CLI -p $REDISPORT SHUTDOWN
while [ -x ${PIDFILE} ]
do
echo "Waiting for Redis to shutdown ..."
sleep 1
done
echo "Redis stopped"
fi
;;
restart|force-reload)
${0} stop
${0} start
;;
*)
echo "Usage: /etc/init.d/redis {start|stop|restart|force-reload}" >&2
exit 1
esac
##############################
```

执行权限
```
chmod +x /etc/init.d/redis
```

开机自启动
```
sudo chkconfig redis on
```

启动或停止redis
```
service redis start
service redis stop
service redis stop
```

开放防火墙端口6379即可：INPUT开放，save即可；


### redis安装，windows环境
---
Redis官方是不支持windows的，只是 Microsoft Open Tech group 在 GitHub上开发了一个Win64的版本。
[项目地址](https://github.com/MSOpenTech/redis)

解压后的bin目录下有以下这些文件：

    redis-benchmark.exe #基准测试
    redis-check-aof.exe # aof
    redis-check-dump.exe # dump
    redis-cli.exe # 客户端
    redis-server.exe # 服务器
    redis.windows.conf # 配置文件
    
配置：修改配置文件redis.windows.conf

    .......启动内存.......
    # maxheap <bytes>
    maxheap 1024000000
    .......
    Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose

启动/停止：

    方式一：双击"redis-server.exe"，关闭黑框即关闭
    方式二：批处理命令文件【startup.bat】：redis-server redis.windows.conf，关闭黑框即关闭
    
测试使用：

    a、help查看：> help
    b、help查看String：> help @String
    c、双击redis-cli.exe或者如下
        > telnet 127.0.0.1 6379
        > set name jack
        > get name
