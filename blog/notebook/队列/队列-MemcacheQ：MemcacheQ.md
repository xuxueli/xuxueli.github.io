
### MemcacheQ 消息队列 (新浪微博)
MemcacheQ 是一个轻量级的分布式消息队列服务。

- 一，MEMCACHEQ的应用背景：
    
    由于在高并发环境下，由于来不及同步处理，请求往往会发生堵塞，比如说，大量的insert，update之类的请求同时到达mysql，直接导致无数的行锁表锁，甚至最后请求会堆积过多，从而触发too manyconnections错误。通过使用消息队列，我们可以异步处理请求，从而缓解系统的压力。在Web2.0的时代，高并发的情况越来越常见，从而使消息队列有成为居家必备的趋势，相应的也涌现出了很多实现方案，像Twitter以前就使用RabbitMQ实现消息队列服务，现在又转而使用Kestrel来实现消息队列服务，此外还有很多其他的选择，比如说：ActiveMQ，ZeroMQ等。

    上述消息队列的软件中，大多为了实现AMQP，STOMP，XMPP之类的协议，变得极其重量级，但在很多Web应用中的实际情况是：我们只是想找到一个缓解高并发请求的解决方案，不需要杂七杂八的功能，一个轻量级的消息队列实现方式才是我们真正需要的。
    
- 二，MEMCACHEQ的特性
    - 1 简单易用
    - 2 处理速度快
    - 3 多条队列
    - 4 并发性能好
    - 5 与memcache的协议兼容。这就意味着只要装了memcache的extension就可以了，不需要额外的插件。

### 客户端使用方式
- 引入maven依赖
```
<!-- xmemcached -->
<dependency>
    <groupId>com.googlecode.xmemcached</groupId>
    <artifactId>xmemcached</artifactId>
    <version>2.0.0</version>
</dependency>
```

- 2、新增Client工具

```
// MemcacheqUtil.java
package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * Memcacheq Client
 * @author xuxueli
 */
public class MemcacheqUtil {

	private static XMemcachedClient client;
	private static XMemcachedClient getInstance(){
		if (client == null) {
			try {
				URL url = Thread.currentThread().getContextClassLoader().getResource("memcacheq.properties");
				Properties props = new Properties();
				props.load(new InputStreamReader(new FileInputStream(url.getPath()), "UTF-8"));

				String serverAddress = props.getProperty("address");
				client = new XMemcachedClient(AddrUtil.getAddresses(serverAddress));
				client.setOpTimeout(2000);
				client.setPrimitiveAsString(true);
				// 解决连接memcacheq，取出来的消息比放进去的多
				// 所谓multi get优化是指xmemcached会将连续的单个get请求合并成一个multi get请求作批量获取，提高效率
				client.setOptimizeGet(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	/**
	 * 存入
	 * @param queue
	 * @param object
	 * @return
	 */
	public static boolean set(String queue, Object object) {
		try {
			return getInstance().set(queue, 0, object);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 取出
	 * @param queue
	 * @return
	 */
	public static synchronized Object get(String queue) {
		try {
			return getInstance().get(queue);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

// memcacheq.properties
#memcached服务端地址配置
address=192.168.40.128:11212

// 测试代码：生产者
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import util.MemcacheqUtil;

public class TestCustom {
	public static String quenuName = "quenuName";
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			exec.execute(new Thread(new TestCustomThread()));
		}
	}
}

/**
 * 队列消费者线程
 */
class TestCustomThread implements Runnable {
	@Override
	public void run() {
		while (true) {
			String[] quenuValue = (String[]) MemcacheqUtil.get(TestCustom.quenuName);
			if (quenuValue == null || quenuValue.length < 2) {
				try {
					TimeUnit.SECONDS.sleep(2);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println(String.format("--------- load quene data, %s:%s", quenuValue[0], quenuValue[1]));
		}
	}
}

// 测试代码：消费者
import util.MemcacheqUtil;

public class TestProduct {
	public static void main(String[] args) {
		
		for (int i = 0; i <500; i++) {
			String[] temp = new String[2];
			temp[0] = "userId-" + i;
			temp[1] = "Time-" + System.currentTimeMillis();
			MemcacheqUtil.set(TestCustom.quenuName, temp);
		}
		System.out.println("------end");
	}
}
```

### MemcacheQ 安装，CentOS环境
MEMCACHEQ依赖于libevent和BerkleyDB。其中libevent如果你安装过memcached就已经安装。

BerkleyDB用于持久化存储队列的数据。这样在MEMCACHEQ崩溃或者服务器挂掉的时候，不至于造成数据的丢失。这一点很重要，很重要。

##### 1. 先检查libevent, libevent-devel是否已经安装：
```
// 查询是否安装libevent, libevent-deve;否则，使用以下命令安装：
rpm -qa|grep libevent 
yum install libevent
yum install libevent-devel
```

注意事项：libevent, libevent-devel优先使用yum安装源，光盘镜像中的rpm包安装，这样稳定性和兼容性可得到保证，网上流传的使用源码安装libevent的方法会有问题，因为很可能系统已经安装libevent,再使用源码安装，必然导致冲突，造成意外问题，所以一定要使用上述命令检查系统是否已经安装相应的库

##### 2、安装 BerkeleyDB
[官网下载](http://www.oracle.com/technetwork/products/berkeleydb/downloads/index.html)

```
// 安装
wget http://download.oracle.com/berkeley-db/db-6.0.30.tar.gz
tar -zxvf db-6.0.30.tar.gz  (根据自身的情况解压到目录)
$ cd db-6.0.30/build_unix
../dist/configure --prefix=/usr/local/berkeleyDB
sudo make && make install
```

配置动态连接器(ld)运行时邦定 
```
vi /etc/ld.so.conf

// 添加以下内容
/usr/local/libevent/lib
/usr/local/berkeleyDB/lib

// 然后执行命令
/sbin/ldconfig
```

##### 3、安装 MemcacheQ
[官网](http://memcachedb.org/memcacheq/)

[github地址](https://github.com/stvchu/memcacheq)

```
// 安装
wget https://codeload.github.com/stvchu/memcacheq/tar.gz/v0.2.1
tar -zxvf memcacheq-0.2.1.tar.gz 
cd memcacheq-0.2.1
./configure --prefix=/usr/local/memcacheq --enable-threads --with-libevent=/usr/local/libevent --with-bdb=/usr/local/berkeleyDB
sudo make && make install

// 测试是否安装成功：
/usr/local/memcacheq/bin/memcacheq -h
```

##### 4、启动服务

建立相关目录：
```
mkdir /home/root/memcacheq/logs
mkdir /home/root/memcacheq/data

chmod 775 /home/root/memcacheq/logs
chmod 775 /home/root/memcacheq/data
```

启动服务：
```
/usr/local/memcacheq/bin/memcacheq -d -r -uroot -p22201 -H  /home/root/memcacheq/data  -N -R -v -L 1024 -B 1024 >  /home/root/memcacheq/logs/mq_error.log 2>&1
/usr/local/memcacheq/bin/memcacheq -d -r -u nobody  -H /home/root/memcacheq/data -N -R -v -L 1024 -B 1024 > /home/root/memcacheq/logs/mq_error.log 2>&1
```

参数说明：

    -A : 数据页大小
    -B : 队列中每条数据的最大长度（字节）
    -c 选项是最大运行的并发连接数，默认是1024 
    -d 选项是启动一个守护进程， 
    -H : 数据保存目录
    -l 是监听的服务器IP地址，默认为所有网卡。 （默认是22201）
    -L ：日志缓存大小（默认是32K，1024表示1024K）
    -m 是分配给Memcache使用的内存数量，单位是MB，默认64MB
    -M return error on memory exhausted (rather than removing items)
    -N : 使用内存缓冲方式保存数据至磁盘，从而获得极高性能。若无此参数，性能会很差
    -p 是设置Memcache的TCP监听的端口，最好是1024以上的端口
    -P 是设置保存Memcache的pid文件
    -R : 自动清理过期的日志
    -u 是运行Memcache的用户，如果当前为root 的话，需要使用此参数指定用户。

##### 5、设置开机自启动（开放端口：22201）：
- 1、编写一个启动脚本：
```
vi /home/root/memcacheq_startup.sh

// 内容如下
####################
#!/bin/bash

/usr/local/memcacheq/bin/memcacheq -d -r -uroot -p22201 -H /home/root/memcacheq/data -N -R -v -L 1024 -B 1024 > /home/root/memcacheq/logs/mq_error.log 2>&1

#################### 
```

- 2、修改脚本执行权限：
```
chmod ug+x /home/root/memcacheq_startup.sh
```

- 3、脚本自动运行

```
vi /etc/rc.d/rc.local

// 末尾添加脚本的路径：
/home/root/memcacheq_startup.sh
```

##### 6、使用命令行连接测试

```
// ------ 连接 ------
telnet 127.0.0.1 11212

// ------ 添加 ------
// 格式
set <queuename> <flags> 0 <message_len>
<putyourmessagebodyhere>
STORED

// 示例：
set test_queue 0 0 6
abcdef
STORED

// ------ 获取 ------
// 格式：
get <queuename>
VALUE <queuename> <flags> <message_len>
<yourmessagebodywillcomehere>
END

// 示例：
get test_queue
VALUE test_queue 0 13
first_message
END

// ------ 查看队列 ------
stats queue
STAT test_queue 20/10#表示队列test_queue里面有20条信息,读取了10条
END

// ------ 删除整个队列 ------
// 格式：
delete <queue name>

// 示例：
delete test_queue
DELETED
```



