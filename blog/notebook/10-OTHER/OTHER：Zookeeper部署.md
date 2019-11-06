- Zookeeper官方：https://zookeeper.apache.org/   
- ZK查看工具：[ZooInspector](https://github.com/apache/zookeeper/tree/master/zookeeper-contrib/zookeeper-contrib-zooinspector)
- ZK-CI常用命令：如 " echo wchs | nc 127.0.0.1 2181 " 查看watch信息 [博客](http://www.blogjava.net/ideame/archive/2016/10/10/431878.html )

## Zookeeper介绍
Zookeeper是一个树形数据结构的高性能的分布式应用程序的协调服务。

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_PeVb.jpg "在这里输入图片标题")

拥有以下功能：

- 1、命名服务（naming）:

是指通过指定的名字来获取资源或者服务的地址，提供者的信息。保证服务全局唯一。

- 2、配置管理（configuration management）：

将配置信息保存在Zookeeper的某个目录节点中，然后将所有需要修改的应用机器监控配置信息的状态，一旦配置信息发生变化，每台应用机器就会收Zookeeper的通知，然后从Zookeeper获取新的配置信息应用到系统中。

- 3、分布式锁（synchronization）：

解决分布式资源访问冲突的问题。

- 4、集群管理（group services）:

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_o7SQ.jpg "在这里输入图片标题")

多台 Server 组成一个服务集群，那么必须要一个“总管”知道当前集群中每台机器的服务状态，一旦有机器不能提供服务，集群中其它集群必须知道，从而做出调整重新分配服务策略。同样当增加集群的服务能力时，就会增加一台或多台 Server，同样也必须让“总管”知道。

## ZK之watcher普及

    1、可以注册watcher的方法：getData、exists、getChildren。
    2、可以触发watcher的方法：create、delete、setData。连接断开的情况下触发的watcher会丢失。
    3、一个Watcher实例是一个回调函数，被回调一次后就被移除了。如果还需要关注数据的变化，需要再次注册watcher。
    4、New ZooKeeper时注册的watcher叫default watcher，它不是一次性的，只对client的连接状态变化作出反应。(推荐ZK初始化时, 主动Watcher如exists)
    5、实现永久监听: 由于zookeeper是一次性监听，所以我们必须在wather的process方法里面再设置监听。
    6、getChildren("/path")监视/path的子节点，如果（/path）自己删了，也会触发NodeDeleted事件。

《操作--事件》 | event For “/path” | 	event For “/path/child”
--- | --- | ---
create(“/path”) | EventType.NodeCreated | 无
delete(“/path”) |   EventType.NodeDeleted | 无
setData(“/path”) |  EventType.NodeDataChanged | 无
create(“/path/child”) | EventType.NodeChildrenChanged（getChild） | EventType.NodeCreated
delete(“/path/child”) | EventType.NodeChildrenChanged（getChild） | EventType.NodeDeleted
setData(“/path/child”) | 无 | EventType.NodeDataChanged


《事件--Watch方式》 | Default Watcher | exists(“/path”) | getData(“/path”) | 	getChildren(“/path”)
--- | --- | --- | --- | ---
EventType.None  | 触发 | 触发 | 触发 | 触发 
EventType.NodeCreated  |  | 触发 | 触发 |  
EventType.NodeDeleted  |  | 触发 | 触发 | 
EventType.NodeDataChanged  |  | 触发 | 触发 | 
EventType.NodeChildrenChanged  |  |  |  | 触发 

## tips
- zookeeper使用HashMap<Path,Watcher>维护了所有路径的watcher，不论注册多少次，都只会有一个watcher存在。当watcheEvent产生的时候，会移除对应path的watcher，并且回调。
- 当type=None的时候，就不会移除watcher，会向所有watcher发送事件。
    - 初始化的sessionState=Disconnected，
      所以第一次ping成功，（zookeeper client会不断的给server发送ping指令）
      会产生一个watchEvent:State=SyncConnected，type=None，
      此时sessionState=SyncConnected，所以后续的ping就不会产生事件了。
      
    - 当zk监听watcher的时候，如果发生网络断链，且在sessionTimeout/2的时间内都没有恢复连接。
      那么所有注册的watcher都会接收到
      state=Disconnected,type=None,path=Null的watchEvent
      此时sessionState=Disconnected..
        - 如果在剩下sessionTimeout/2的时间内恢复连接，即ping通了
          那么就会收到watchEvent:state=SyncConnected,type=None，path=Null
        - 如果超过sessionTimeout时间恢复连接，那么就会收到
          watchEvent:state=Expired,type=None,path=Null
          此时表示zookeeper客户端真正与服务端失去连接，就需要重建zookeeper的客户端了。
        - 如果超过sessionTimeout时间也没恢复连接，只有等恢复连接才会收到Expired事件。
- 所以对于watcheEvent的事件的处理方式是:
    - DisConneted 无视，因为连不上你做啥事都没用，也就改改某些状态，能连上的话要么收到SyncConneted事件，要么收到Expired 事件
    - Expired重新构建zookeeper客户端。
    - SyncConnected
        - 对于type!=None重新注册watcher.
        - 对于监听的type做后续处理
    
      
## ZooKeeper的一个性能测试

[测试数据来自阿里中间件团队](http://jm.taobao.org/2011/07/15/1070/)

ZK集群情况: 3台ZooKeeper服务器。8核64位jdk1.6；log和snapshot放在不同磁盘;

- 场景一: pub创建NODE,随后删除
    - 操作: 同一个目录下，先create EPHEMERAL node，再delete；create和delete各计一次更新。没有订阅。一个进程开多个连接，每个连接绑定一个线程，在多个path下做上述操作；不同的连接操作的path不同
    - 结果数据: "dataSize(字节)-TPS-响应时间(ms)" 统计结果为: 255-14723-82, 1024-7677-280, 4096-2037-1585;

- 场景二: pub创建NODE, sub订阅并获取数据
    - 操作: 一个进程开多个连接，每连接一个线程，每个连接在多个path下做下述操作；不同的连接操作的path不同。每个path有3个订阅者连接，一个修改者连接。先全部订阅好。然后每个修改者在自己的每个path下创建一个EPHEMERAL node，不删除；创建前记录时间，订阅者收到event后记录时间(eventStat)；重新get到数据后再记录时间(dataStat)。共1000个pub连接，3000个sub连接，20W条数据。收到通知后再去读取数据，五台4核client机器。
    - 结果汇总: getAfterNotify=false（只收事件，受到通知后不去读取数据）；五台4核client机器
    - 结果数据: "dataSize(字节)-TPS-响应时间(ms)" 统计结果为: 255-1W+-256ms, 1024-1W+-256, 2048-1W+-270, 4096-8000+-520;

- 场景三: pub创建NODE,随后设置数据
    - 一个进程开多个连接，每连接一个线程，每个连接在多个path下做下述操作；不同的连接操作的path不同。每个path有一个修改者连接，没有订阅者。每个修改者在自己的每个path下设置数据。
    - 结果汇总: getAfterNotify=false（只收事件，受到通知后不去读取数据）；五台4核client机器
    - 结果数据: "dataSize(字节)-TPS-响应时间(ms)" 统计结果为: 255-14723-82, 1024-7677-280, 4096-2037-1585 ;
    
总结: 由于一致性协议带来的额外网络交互，消息开销，以及本地log的IO开销，再加上ZK本身每1000条批量处理1次的优化策略，写入的平均响应时间总会在50-60ms之上。但是整体的TPS还是可观的。单个写入数据的体积越大，响应时间越长，TPS越低，这也是普遍规律了。压测过程中log文件对磁盘的消耗很大。实际运行中应该使用自动脚本定时删除历史log和snapshot文件。

## Zookeeper几种经典使用场景
#### 1、ZK在 "服务注册（XXL-RPC）" 中的应用

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_cK09.png "在这里输入图片标题")

- 1、服务注册节点：每个服务在ZK中对应一个唯一注册节点，如图"iface name"节点。
- 2、注册服务：每个服务提供者接入ZK后，将会在对应的服务注册节点下，新建一个子节点，如图中"192.168.0.1:9999"、"192.168.0.2:9999"和"192.168.0.3:9999"。EPHMERAL类型节点有个特点，当client和zookeeper集群断掉连接后节点将会被移除。可以实现服务的自动注册和自动注销。
- 3、服务发现：每个服务消费者启动之后，可感知已注册服务节点并获取服务提供者信息，如IP端口等。同时会watch对应的服务注册节点，当服务提供者进行注册或者注销时，消费者将会监听到服务变动通知，可实时获取最新的服务提供者注册信息。可实现服务发现。

“服务提供方” 相关代码：
```
String path = getInstance().create("服务注册节点/IP:PORT", address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
```

“服务消费方” 相关代码：
```
if ((event.getType() == Event.EventType.NodeChildrenChanged && event.getPath()!=null && event.getPath().startsWith(Environment.ZK_SERVICES_PATH)) ||
		event.getType() == Event.EventType.None) {
	try {
		discoverServices(); // reload 所需服务信息
	} catch (Exception e) {
		logger.error("", e);
	}
}
```

#### 2、ZK在 "配置推送（XXL-CONF）" 中的应用
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_fM68.png "在这里输入图片标题")

- 1、配置项节点：配置系统在ZK集群中占用一个根目录, 每新增一条配置项, 将会在该目录下新增一个子节点，如上图 "redis.address" 节点。
- 2、配置更新：当需要更新一个配置项的配置信息时，需要对该配置相对应的节点进行赋值操作，该操作将会触发该节点的数据变更通知，如"NodeDataChanged"。
- 3、配置推送：client端在项目启动时，将会watch用到的配置项对应的节点，当节点变动时，将会实时获取通知并refresh local cache，使项目中的配置项信息和ZK集群保持一致。实现了配置推送的功能。

“配置中心” 相关代码：
```
// 配置新增
zooKeeper.create("配置项节点", new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

// 配置更新
zooKeeper.setData("配置项节点", data.getBytes(),stat.getVersion());
```

“配置client端” 相关代码：
```
// 获取配置（同时将会主动watch配置节点）
String znodeValue = null;
byte[] resultData = getInstance().getData("配置项节点", true, null);
if (resultData != null) {
	znodeValue = new String(resultData);
}
return znodeValue;

// 配置推送（监听watch事件）
} else if (eventType == EventType.NodeDeleted) {
	String path = watchedEvent.getPath();
	zooKeeper.exists(path, true);

	String key = pathToKey(path);
	if (key == null) {
		return;
	}

	XxlConfClient.remove(key);
} else if (eventType == EventType.NodeDataChanged) {
	String path = watchedEvent.getPath();
	zooKeeper.exists(path, true);

	String key = pathToKey(path);
	if (key == null) {
		return;
	}
	String data = getPathDataByKey(key);

	XxlConfClient.update(key, data);
}
```

#### 3、ZK在 "分布式锁（XXL-MQ）" 中的应用

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_PeVb.jpg "在这里输入图片标题")

- 1、竞争者：分布式锁，目的是为了解决分布式环境下资源访问冲突的问题。每个进行资源竞争的对象，可以视为一个竞争者。如一个秒杀队列的消费者机器。
- 2、竞争者注册路径：需要为多个竞争者一个竞争者注册路径，每个新加入的竞争者都会在该路径下自动注册一个EPHEMERAL节点。竞争者离线后将会自动注销节点。
- 3、竞争者动态感知：每个注册的竞争者需要watch竞争者注册路径，从而在竞争者变动时得到通知并refresh local caceh。每个竞争者，可以实时感知在线的所有竞争者。
- 4、竞争者竞争规则：竞争规则，即资源分配规则，分布式环境下多个竞争者将按照该规则决定哪一个有权限获得资源。如根据节点注册顺序排序，或者按照节点内容自然排序等，节点最大或者最小等。
- 5、分布式锁：每个竞争者，按照竞争规则，竞争失败的进入休眠状态，竞争成功的保持存活状态。从而实现分布式锁功能。

相关代码：
```
// watch 竞争者注册路径
String path = getInstance().create(registryKeyAddressPath, address.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

// 竞争者动态感知
if ((event.getType() == Event.EventType.NodeChildrenChanged && event.getPath()!=null && event.getPath().startsWith(Environment.ZK_CONSUMER_PATH)) ||
		event.getType() == Event.EventType.None) {
	try {
		discoverConsumers();    // reload 竞争者
	} catch (Exception e) {
		logger.error("", e);
	}
}

// 分布式锁校验
Set<String> addressSet = consumerAddress.get(name);
if (addressSet == null) {
	consumerAddress.put(name, new HashSet<String>());
	discoverConsumers();
	addressSet = consumerAddress.get(name);
}
if (addressSet.size()==0) {
	return null;
}

TreeSet<String> sortSet = new TreeSet<String>(addressSet);
int index = 0;
for (String item: sortSet) {
	if (item.equals(localAddressRandom)) {
		break;
	}
	index++;
}
if (index == 0) {
    // 锁定成功
}	
```

#### 4、ZK在 "消息广播（XXL-MQ）" 中的应用

- 1、消息监听节点：每个Topic下的消息发布者和订阅者双方约定一个指定的消息监听节点。
- 2、消息发布者：通过setData将消息数据赋值给消息监听节点，完成消息发布逻辑，将会触发该节点的NodeDataChanged广播。
- 3、消息订阅者，实现需要已经订阅（watch）消息监听节点，接收到消息广播后，解析节点数据并生成一条完整的消息数据，分发给消息执行模块。从而,实现了消息广播功能;

消息发布者，相关代码：
```
// 发布消息
Stat ret = zooKeeper.setData(topicKeyPath, data.getBytes(), topicKeyPathStat.getVersion());
```

消息订阅者，相关代码：
```
// 订阅消息
getInstance().exists(topicKeyPath, true);

// 监听广播消息
String name = path.substring(Environment.ZK_CONSUMER_PATH.length()+1, path.length());
String data = null;
try {
	byte[] resultData = zooKeeper.getData(path, true, null);
	if (resultData != null) {
		data = new String(resultData);
	}
} catch (Exception e) {
	logger.error("", e);
}
XxlMqConsumer.pushTopicMessage(name, data); // 消息分发
```

---

## 环境搭建
### 1、zookeeper依赖Java环境，部署机器需要提前安装JDK；
### 2、zookeeper配置文件详解
> Zookeeper 在启动时会找“/conf/zoo.cfg”这个文件作为默认配置文件,所以我们复制“/conf/zoo_sample.cfg”定制一个名称为zoo.cfg的文件。

配置项汇总：

    1、clientPort：监听客户端连接的端口，默认2181。
    2、dataDir：存储快照文件snapshot的目录。默认情况下，事务日志也会存储在这里。建议同时配置参数dataLogDir, 事务日志的写性能直接影响zk性能。
    3、dataLogDir：事务日志输出目录。尽量给事务日志的输出配置单独的磁盘或是挂载点，这将极大的提升ZK性能。（不设置则使用dataDir目录） 
    4、tickTime：ZK中的一个时间单元。ZK中所有时间都是以这个时间单元为基础，进行整数倍配置的。例如，session的最小超时时间是2*tickTime。
    5、syncLimit：设置2标示2*tickTime；在运行过程中，Leader负责与ZK集群中所有机器进行通信，例如通过一些心跳检测机制，来检测机器的存活状态。如果L发出心跳包在syncLimit之后，还没有从F那里收到响应，那么就认为这个F已经不在线了。注意：不要把这个参数设置得过大;
    6、initLimit：Follower在启动过程中，会从Leader同步所有最新数据，然后确定自己能够对外服务的起始状态。Leader允许F在initLimit时间内完成这个工作。通常情况下，我们不用太在意这个参数的设置。如果ZK集群的数据量确实很大了，F在启动的时候，从Leader上同步数据的时间也会相应变长，因此在这种情况下，有必要适当调大这个参数了。
    7、server.id=host:port:port ：单机可忽视。Clusters配置说明：“ server.id=host:port:port. ”指示了不同的 ZooKeeper 服务器的自身标识，作为集群的一部分的机器应该知道 ensemble 中的其它机器。用户可以从“ server.id=host:port:port. ”中读取相关的信息。 在服务器的 data（ dataDir 参数所指定的目录）目录下创建一个文件名为 myid 的文件，这个文件中仅含有一行的内容，指定的是自身的 id 值。比如，服务器“ 1 ”应该在 myid 文件中写入“ 1 ”。这个 id 值必须是 ensemble 中唯一的，且大小在 1 到 255 之间。这一行配置中，第一个端口（ port ）是从（ follower ）机器连接到主（ leader ）机器的端口，第二个端口是用来进行 leader 选举的端口。例如可设置每台机器使用三个端口，分别是： clientPort ，2181 ； port ， 2888 ； port ， 3888 。

>** ”myid“ **： 如果集群部署ZK，需要在配置文件中配置的“dataDir”指定的目录下面，创建一个“myid”文件，里面内容为一个数字，用来标识当前主机；单机可忽略，集群部署时才需要；

### 2、单机模式部署ZK（Centos环境为例，Mac和Windows环境基本一致）    

- 1、解压安装包，初始化配置文件 zoo.cfg
```
cd /zookeeper/conf
cp zoo_sample.cfg zoo.cfg
```
- 2、配置 zoo.cfg 中 dataDir， 其他默认即可
```
dataDir=/zookeeper/data        （新建并制定数据目录）
```

### 3、集群模式部署ZK（假设三台机器，分别进行以下操作）
- 1、初始化配置文件 zoo.cfg
```
cd /zookeeper/conf
cp zoo_sample.cfg zoo.cfg
```
- 2、配置 zoo.cfg
```
clientPort=2181
...
dataDir=/zookeeper/data     (新建目录)
dataLogDir=/zookeeper/log       (新建目录)
...
# Clusters，myid 参考第三步
server.1=ip1:2888:3888
server.2=ip2:2888:3888
server.3=ip3:2888:3888
```
- 3、在“dataDir”目录下新建“myid”文件
```
cd /zookeeper/data
touch myid    （文件内容为机器ID）
```
>这个文件中仅含有一行的内容，指定的是自身的 id 值。比如，服务器“ 1 ”应该在 myid 文件中写入“ 1 ”。这个 id 值必须是 ensemble 中唯一的，且大小在 1 到 255 之间。该值用于设置第二部的service.myid的值；

### 4、启动，关闭和查看状态
- 1、常规脚本方式启动
```
./zkServer.sh start
./zkServer.sh stop
./zkServer.sh status
```
- 2、伪集群部署，启动脚本（如机器部署多台ZK，可快速操作其中一台）

```
// 新建 zookeeper 启动脚本
vi /data/zookeeper_startup.sh

// 脚本内容如下
#########################
zookeeper_home=/data/appdata/zookeeper_servers/$1
# See how we were called
case "$2" in
start)
$zookeeper_home/bin/zkServer.sh start
#tail -f $tomcat_home/log/catalina.out
;;
stop)
$zookeeper_home/bin/zkServer.sh stop
;;
restart)
$zookeeper_home/bin/zkServer.sh restart
;;
status)
$zookeeper_home/bin/zkServer.sh status
;;
*)
echo "Usage: $0 {start|stop|restart}"
esac
exit 0
#########################

// 执行脚本
sh /data/zookeeper_startup.sh zookeeper01 start 
```
