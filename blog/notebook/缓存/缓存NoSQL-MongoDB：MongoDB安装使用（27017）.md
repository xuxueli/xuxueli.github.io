### MongoDB
---

MongoDB 是由C++语言编写的，是一个基于分布式文件存储的开源数据库系统。
在高负载的情况下，添加更多的节点，可以保证服务器性能。
MongoDB 旨在为WEB应用提供可扩展的高性能数据存储解决方案。
MongoDB 将数据存储为一个文档，数据结构由键值(key=>value)对组成。MongoDB 文档类似于 JSON 对象。字段值可以包含其他文档，数组及文档数组。


[可视化工具robomongo](https://robomongo.org/download)

[官网](https://www.mongodb.org/downloads)


一些文档：

[菜鸟教程](http://www.runoob.com/mongodb/mongodb-intro.html)

[mongodb常用操作命令](http://www.jb51.net/article/48217.htm)

[mongodb之CRUD](http://snowolf.iteye.com/blog/1796749/)

[一处教程](http://www.cnblogs.com/huangxincheng/archive/2012/02/18/2356595.html)


### MongoDB使用 （mongo-java-driver 方式）
- 1、引入maven依赖
```
<dependency>
	 <groupId>org.mongodb</groupId>
	 <artifactId>mongo-java-driver</artifactId>
	 <version>2.13.2</version>
 </dependency>
```

-2 、配置mongodb地址 (mongodb.properties)
```
host=192.168.40.128
port=27017
```

-3、添加MongoDB工具类 (MongoDBUtil.java)
```
package com.xxl.util.core.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了
 * 注意Mongo已经实现了连接池，并且是线程安全的
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，
 * DB和DBCollection是绝对线程安全的
 *
 *
 *	 <!-- mongodb -->
	 <dependency>
		 <groupId>org.mongodb</groupId>
		 <artifactId>mongo-java-driver</artifactId>
		 <version>2.13.2</version>
	 </dependency>
 *
 * @author xuxueli 2015-7-16 20:00:18
 */
public class MongoDBUtil {
	private static Logger logger = LoggerFactory.getLogger(MongoDBUtil.class);
	public static String defauleDbName;	// 默认数据库名称
	
	/**
	 * 一个数据库链接池
	 */
	private static MongoClient client = getInstance();
	private static MongoClient getInstance(){
		if (client == null) {
			Properties prop = PropertiesUtil.loadProperties("mongodb.properties");
			String host = PropertiesUtil.getString(prop, "host");
			int port = PropertiesUtil.getInt(prop, "port");
			defauleDbName = "admin";
			try {
				client = new MongoClient(host, port);
			} catch (UnknownHostException e) {
				logger.info("{}", e);
			}
			
			// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
	        // List<ServerAddress> listHost = Arrays.asList(new ServerAddress("localhost", 27017),new ServerAddress("localhost", 27018));
	        // instance.mongoClient = new MongoClient(listHost);
			// 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
			//boolean auth = client.authenticate(myUserName, myPassword);
			
			Builder options = new MongoClientOptions.Builder();
			options.connectionsPerHost(300);		// 连接池设置为300个连接,默认为100
	        options.connectTimeout(15000);			// 连接超时，推荐>3000毫秒
	        options.maxWaitTime(5000); 
	        options.socketTimeout(0);				// 套接字超时时间，0无限制
	        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出 "Out of semaphores to get db"错误。
	        options.writeConcern(WriteConcern.SAFE);
	        options.build();
		}
		return client;
	}
	
	// 数据库实例操作------------------------------
	/**
	 * 获取DB实例 = 数据库实例
	 * 
	 * @param dbName
	 * @return
	 */
	public static DB getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
        	DB database = getInstance().getDB(dbName);
            return database;
        }
        return null;
    }
	
    /**
     * 查询链接下, 所有数据库实例名称
     * 
     * @return
     */
    public List<String> getAllDBNames() {
    	List<String> databaseNames = getInstance().getDatabaseNames();
        return databaseNames;
    }
    
    /**
     * 删除一个数据库实例
     */
    public void dropDB(String dbName) {
    	DB db = getDB(dbName);
    	if (db != null) {
    		db.dropDatabase();
    	}
    }
    
    /**
     * 查询DB下, 所有表名
     * 
     * @param dbName
     * @return
     */
    public Set<String> getAllCollections(String dbName) {
    	Set<String> colls = getDB(dbName).getCollectionNames();
        return colls;
    }
    
    // 表操作-----------------------------------------------
	/**
     * 获取DBCollection对象 = 表
     * 
     * @param collName
     * @return
     */
    public static DBCollection getCollection(String dbName, String collName) {
    	DB db = getDB(dbName);
    	if (db != null) {
    		DBCollection collection = db.getCollection(collName);
    		return collection;
    	}
        return null;
    }
    
    /**
     * 新增DBCollection对象 = 表
     * @param dbName
     * @param collName
     * @param options
     * @return
     */
    public static DBCollection addCollection(String dbName, String collName, DBObject options) {
    	DB db = getDB(dbName);
    	if (db != null) {
    		DBCollection collection = db.createCollection(collName, options);
    		return collection;
    	}
        return null;
    }
    
    // 数据操作---------------------------
    /**
     * 保存
     * @param dbName
     * @param dbObject
     */
    public static void save(String dbName, String collName, DBObject dbObject) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
    		collection.save(dbObject);
		}
    }
    
    /**
     * 删除
     * @param dbName
     * @param collName
     * @param dbObject
     */
    public static void delete(String dbName, String collName, DBObject dbObject) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
    		collection.remove(dbObject);
		}
    }
    
    // 此处不使用toArray()方法直接转换为List,是因为toArray()会把结果集直接存放在内存中，
    // 如果查询的结果集很大，并且在查询过程中某一条记录被修改了，就不能够反应到结果集中，从而造成"不可重复读"
    // 而游标是惰性获取数据
    /**
     * 条件查询, 列表, 限制pageSize
     * 
     * @param dbName
     * @param collName
     * @param query
     * @param fields
     * @param limit
     * @return
     */
    public static List<DBObject> find(String dbName, String collName, DBObject query, DBObject fields, int pageSize) {
        List<DBObject> list = new LinkedList<DBObject>();
        Cursor cursor = getCollection(dbName, collName).find(query, fields).limit(pageSize);
        while (cursor.hasNext()) {
            list.add(cursor.next());
        }
        return list.size() > 0 ? list : null;
    }
 
    /**
     * 条件查询, 分页数据
     * @param dbName
     * @param collName
     * @param query
     * @param fields
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static List<DBObject> find(String dbName, String collName, DBObject query, DBObject fields, DBObject orderBy, int pageNum, int pageSize) {
        List<DBObject> list = new ArrayList<DBObject>();
        Cursor cursor = getCollection(dbName, collName).find(query, fields).skip((pageNum - 1) * pageSize).limit(pageSize).sort(orderBy);
        while (cursor.hasNext()) {
            list.add(cursor.next());
        }
        return list.size() > 0 ? list : null;
    }
    
    /**
     * 分页查询,count
     * @param dbName
     * @param collName
     * @param query
     * @return
     */
    public static long count(String dbName, String collName, DBObject query) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
			return collection.count(query);
		}
        return -1;
    }
 
    /**
     * 条件查询, 单条
     * @param dbName
     * @param query
     * @param fields
     * @return
     */
    public static DBObject findOne(String dbName, String collName, DBObject query, DBObject fields) {
    	DBCollection collection = getCollection(dbName, collName);
    	if (collection != null) {
			return collection.findOne(query, fields);
		}
        return null;
    }
 
    /**
     * 更新
     * @param dbName
     * @param collName
     * @param query
     * @param update
     * @param upsert
     * @param multi
     */
    public static void update(String dbName, String collName, DBObject query, DBObject update, boolean upsert, boolean multi) {
    	getCollection(dbName, collName).update(query, update, upsert, multi);
    }
 
    /**
     * 查询出key字段,去除重复，返回值是{_id:value}形式的list
     * @param dbName
     * @param collName
     * @param key
     * @param query
     * @return
     */
    @SuppressWarnings("rawtypes")
	public List distinct(String dbName, String collName, String key, DBObject query) {
        return getCollection(dbName, collName).distinct(key, query);
    }
	
	public static void main(String[] args) {
		// 新建一张表
		DBCollection db = addCollection(defauleDbName, "test", null);
		System.out.println(db != null);

		// 新增一条记录
		DBObject dbObject = new BasicDBObject();
		dbObject.put("name", "jack");
		save(defauleDbName, "test", dbObject);
		
		// 查询该记录
		DBObject query = new BasicDBObject();
		query.put("name", "jack");
		DBObject fields = new BasicDBObject();
		DBObject result = findOne(defauleDbName, "test", query, fields);
		
		System.out.println(result!=null?result.get("name"):"查询失败");
		
	}
}
```

### MongoDB安装配置，CentOS环境
- 1、配置
```
// 下载，解压mongodb
cd /home/root/temp
wget https://fastdl.mongodb.org/linux/mongodb-linux-i686-2.6.10.tgz
tar -zxvf mongodb-linux-i686-2.6.10.tgz
mv mongodb-linux-i686-2.6.10 /home/root

// 创建mongodb目录
mkdir -p /home/root/mongodb-linux-i686-2.6.10/data
mkdir -p /home/root/mongodb-linux-i686-2.6.10/logs 
mkdir -p /home/root/mongodb-linux-i686-2.6.10/etc 

// 配置mongodb, 配置信息如下
vi /home/root/mongodb-linux-i686-2.6.10/etc/mongodb.conf

#################################
# 数据文件存放目录
dbpath = /home/root/mongodb-linux-i686-2.6.10/data
# 日志文件存放目录
logpath = /home/root/mongodb-linux-i686-2.6.10/logs/mongodb.log
# 端口
port = 27017
# 以守护程序的方式启用，即在后台运行
fork = true
nohttpinterface = true
#################################

// nohttpinterface = true：关闭Http访问端口，mongodb安装完之后，默认是启用了Http的访问端口，比mongodb监听的端口大1000，即28017
```
- 2、手动启动，从配置文件启动，后台运行：
```
/home/root/mongodb-linux-i686-2.6.10/bin/mongod --config /home/root/mongodb-linux-i686-2.6.10/etc/mongodb.conf
```

- 3、开机启动：
```
vi /etc/rc.d/rc.local

// 内容加上如下内容：
/home/root/mongodb-linux-i686-2.6.10/bin/mongod --config /home/root/mongodb-linux-i686-2.6.10/etc/mongodb.conf
```

- 4、测试：
```
/home/root/mongodb-linux-i686-2.6.10/bin/mongo
```

**启动报错解决：**
./mongod: cannot execute binary file

方案一：怀疑文件没有执行权限
添加可执行权限
```
# chmod +x /home/root/mongodb-linux-i686-2.6.10/bin/mongod
```
方案二：怀疑服务器位数和安装包位数，不一致
```
cat /etc/redhat-release
file /bin/ls // 果然，下载的64位，安装包为32位
```

### MongoDB安装配置，Windows环境

- 第一步：解压目录
    - 解压安装包到 D:\mongodb
    - 建立数据库目录 D:\mongodb\data
    - 建立日志目录 D:\mongodb\logs
    - 建立配置文件目录 D:\mongodb\etc

- 第二步：配置文件mongodb.conf (官方下载的安装包里面没有默认的配置文件, 下面参考配置中仅指定了几个常用项，更多详细配置请参考官方文)
```
###############################
# 数据库路径
dbpath=D:\mongodb\data
# 日志输出文件路径
logpath=D:\mongodb\logs\mongodb.log
# 错误日志采用追加模式，配置这个选项后mongodb的日志会追加到现有的日志文件，而不是从新创建一个新文件
logappend=true
# 启用日志文件，默认启用
journal=true
# 这个选项可以过滤掉一些无用的日志信息，若需要调试使用请设置为false
quiet=true
# 端口号 默认为27017
port=27017
###############################
```

- 第三步：手动启动
```
// 进入cmd执行
mongod --config D:\mongodb\etc\mongodb.conf
```
也可以安装为系统服务

```
// 方式一：安装/卸载为Windows服务，
cd D:\mongodb\bin
mongod --config D:\mongodb\etc\mongodb.conf --install
mongod --remove

// 如果2.6版 这种方式在win7、win8 64位版无法安装成功，采用下面这种方式（以管理员方式启动cmd，试一试应该是OK的）；


// 方式二：安装为Windows服务，使用SC安装
sc create mongodb binPath= "D:\mongodb\bin\mongod.exe --service --config=D:\mongodb\etc\mongodb.conf"

// 启动：
net start mongodb

// 测试：
访问：http://localhost:27017/ 可以看到显示信息：It looks like you are trying to access MongoDB over HTTP on the native driver port. 表示安装成功；
```

- 第四步：测试
```
cd cd D:\MongoDB\bin
mongo
>db.help(); // 查看db函数帮助
>db.version(); // 查看版本
>show dbs; // 查询所有数据库
>use test; // 切换数据库
>db.getMongo(); // 查看当前db的链接机器地址
>show collections; // 查看该库下所有的表
>db.createCollection('user_info'); // 新建一张表（提示{'ok':1}表示成功）
>db.userInfo.find(); // select * from user_info
>db.userInfo.find({"user_name":"jack"}); // select * from user_info where user_name = 'jack';

// 一些常用操作，设置超级管理员，新建数据库，新建表，CRUD，需要后期慢慢学习，暂不赘述；
```



