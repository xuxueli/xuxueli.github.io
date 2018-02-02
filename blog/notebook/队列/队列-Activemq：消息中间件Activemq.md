
[官网](http://activemq.apache.org/download.html)

### ActiveMQ
ActiveMQ 是Apache出品，最流行的、功能强大的即时通讯和集成模式的开源服务器。ActiveMQ是一个完全支持JMS1.1和J2EE 1.4规范的 JMS Provider实现。提供客户端支持跨语言和协议，带有易于在充分支持JMS 1.1和1.4使用J2EE企业集成模式和许多先进的功能。

特性

    1、 多种语言和协议编写客户端。语言： Java、C、C++、C#、Ruby、Perl、Python、PHP。应用协议：OpenWire、Stomp REST、WS Notification、XMPP、AMQP
    2、完全支持JMS1.1和J2EE 1.4规范 （持久化，XA消息，事务)
    3、对Spring的支持，ActiveMQ可以很容易内嵌到使用Spring的系统里面去，而且也支持Spring2.0的特性
    4、通过了常见J2EE服务器（如 Geronimo、JBoss 4、GlassFish、WebLogic)的测试，其中通过JCA 1.5 resource adaptors的配置，可以让ActiveMQ可以自动的部署到任何兼容J2EE 1.4 商业服务器上
    5、支持多种传送协议：in-VM、TCP、SSL、NIO、UDP、JGroups、JXTA
    6、支持通过JDBC和journal提供高速的消息持久化
    7、从设计上保证了高性能的集群，客户端-服务器，点对点
    8、支持Ajax
    9、支持与Axis的整合
    10、可以很容易得调用内嵌JMS provider，进行测试
    
Jms规范里的两种message传输方式Topic和Queue，两者的对比如下：


- Topic
    - Publish Subscribe messaging 发布订阅消息
    - topic数据默认不落地，是无状态的。
    - 并不保证publisher发布的每条数据，Subscriber都能接受到。
    - 一般来说publisher发布消息到某一个topic时，只有正在监听该topic地址的sub能够接收到消息；如果没有sub在监听，该topic就丢失了。
    - 一对多的消息发布接收策略，监听同一个topic地址的多个sub都能收到publisher发送的消息。Sub接收完通知mq服务器
- Quene
    - Point-to-Point 点对点
    - Queue数据默认会在mq服务器上以文件形式保存，比如Active MQ一般保存在$AMQ_HOME\data\kr-store\data下面。也可以配置成DB存储。
    - Queue保证每条数据都能被receiver接收。
    - Sender发送消息到目标Queue，receiver可以异步接收这个Queue上的消息。Queue上的消息如果暂时没有receiver来取，也不会丢失。
    - 一对一的消息发布接收策略，一个sender发送的消息，只能有一个receiver接收。receiver接收完后，通知mq服务器已接收，mq服务器对queue里的消息采取删除或其他操作。


### Spring整合ActiveMQ
- 1、maven依赖
```
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jms</artifactId>
    <version>${spring.version}</version>
</dependency>
……
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-all</artifactId>
    <version>5.10.2</version>
</dependency>
……
<dependency>
    <groupId>commons-pool</groupId>
    <artifactId>commons-pool</artifactId>
    <version>1.6</version>
</dependency>
```

- 2、开发ActIMEMQ监听：TransportListener.java
```
package com.xxl.core.listener;

import java.io.IOException;

import org.apache.activemq.transport.TransportListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JmsTransportListener implements TransportListener{
	public static int isConnected=1;
	private transient static Log logger = LogFactory.getLog(JmsTransportListener.class);
	@Override
	public void onCommand(Object arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onException(IOException arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void transportInterupted() {
		// TODO Auto-generated method stub
		isConnected=0;
		logger.info("与jms服务器链接断开");
	}

	@Override
	public void transportResumed() {
		// TODO Auto-generated method stub
		isConnected=1;
		logger.info("与jms服务器链接恢复");
	}
}
```
- 3、生产者Spring配置：applicationcontext-jms-send.xml （Topic广播、Quene队列，两种方式）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p" xmlns="http://www.springframework.org/schema/beans"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd
http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd">

	<!-- 复用ConnectionFactory -->

	<!-- simpleTopic:PUB -->
	<bean id="simpleTopicPubJmsTemplate" class="org.springframework.jms.core.JmsTemplate">
		<property name="connectionFactory" ref="connectionFactoryReceive" />
		<property name="defaultDestination">
			<bean class="org.apache.activemq.command.ActiveMQTopic">
				<constructor-arg value="simpleTopic" />
			</bean>
		</property>
	</bean>

	<!-- simpleQuenu:Producer -->
	<bean id="simpleQuenuProducerJmsTemplate" class="org.springframework.jms.core.JmsTemplate">
		<property name="connectionFactory" ref="connectionFactoryReceive" />
		<property name="defaultDestination">
			<bean class="org.apache.activemq.command.ActiveMQQueue">
				<constructor-arg value="simpleQuenu" index="0" />
			</bean>
		</property>
	</bean>

</beans>
```

- 4、消费者Spring配置：applicationcontext-jms-receive.xml（Topic广播、Quene队列，两种方式）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns="http://www.springframework.org/schema/beans"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd
http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd
http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd">

	<!-- 配置ConnectionFactory -->
	<bean id="connectionFactoryReceive" class="org.apache.activemq.pool.PooledConnectionFactory">
		<property name="connectionFactory">
			<bean class="org.apache.activemq.ActiveMQConnectionFactory">
				<!-- 单机配置 -->
				<!-- <property name="brokerURL"	value="failover:(tcp://192.168.1.15:61616)" /> -->
				<!-- 集群配置 -->
				<property name="brokerURL"	value="failover:(tcp://127.0.0.1:61611,tcp://127.0.0.1:61612,tcp://127.0.0.1:61613)?initialReconnectDelay=1000" />
				<property name="transportListener">
   					<bean class="com.xxl.core.listener.JmsTransportListener"/>
  				</property>					
			</bean>
		</property>
	</bean>

	<!-- simpleTopic:SUB -->
	<bean id="simpleTopicSub" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
		<property name="connectionFactory" ref="connectionFactoryReceive" />
		<property name="destination">
			<bean class="org.apache.activemq.command.ActiveMQTopic">
				<constructor-arg value="simpleTopic" />
			</bean>
		</property>
		<property name="messageListener">
			<bean class="org.springframework.jms.listener.adapter.MessageListenerAdapter">
				<constructor-arg ref="jmsReceiveService" />
				<property name="defaultListenerMethod" value="simpleTopicSub" />
			</bean>
		</property>
		<property name="idleTaskExecutionLimit" value="2" />
		<property name="maxConcurrentConsumers" value="1" />	<!-- 订阅者,实例数量,默认为1 (每个实例单独消费消息,因此单次执行的订阅消息时必须为1) -->
	</bean>
	
	<!-- simpleQuenu:Consumer -->
	<bean id="simpleQuenuConsumer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
		<property name="connectionFactory" ref="connectionFactoryReceive" />
		<property name="destination" >
			<bean class="org.apache.activemq.command.ActiveMQQueue">
				<constructor-arg value="simpleQuenu" />
			</bean>
		</property>
		<property name="messageListener">
			<bean class="org.springframework.jms.listener.adapter.MessageListenerAdapter">
				<constructor-arg ref="jmsReceiveService" />
				<property name="defaultListenerMethod" value="simpleQuenuConsumer" />
			</bean>
		</property>
		<property name="idleTaskExecutionLimit" value="2" />
		<property name="maxConcurrentConsumers" value="1" />	<!-- 消费者,实例数量,默认为1 (多实例即开启多线程) -->
	</bean>

</beans>
```
- 5、生产者Service开发：JmsSendServiceImpl.java
```
// 接口
package com.xxl.service;

/**
 * JMS.SEND
 * @author xuxueli
 */
public interface IJmsSendService {
	
	/**
	 * simpleTopic发布
	 * @param message
	 */
	public void simpleTopicPub(String message);

	/**
	 * simpleQuenu生产者
	 * @param msg
	 */
	public void simpleQuenuProduct(String message);
	
}

// 实现类
package com.xxl.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.xxl.service.IJmsSendService;

/**
 * JMS.SEND
 * @author xuxueli
 */
@Service
public class JmsSendServiceImpl implements IJmsSendService {
	private transient static Logger logger = LoggerFactory.getLogger(JmsSendServiceImpl.class);
	
	@Resource
	private JmsTemplate simpleTopicPubJmsTemplate;
	
	@Resource
	private JmsTemplate simpleQuenuProducerJmsTemplate;
	
	/*
	 * simpleTopic发布
	 * @see com.xxl.service.IJmsSendService#sendString(java.lang.String)
	 */
	@Override
	public void simpleTopicPub(String message) {
		logger.info("jms simpleTopicPub:{}", message);
		simpleTopicPubJmsTemplate.convertAndSend(message);
	}

	/*
	 * simpleQuenu生产者
	 * @see com.xxl.service.IJmsSendService#simpleQuenuProduct(java.lang.String)
	 */
	@Override
	public void simpleQuenuProduct(String message) {
		logger.info("jms simpleQuenuProduct:{}", message);
		simpleQuenuProducerJmsTemplate.convertAndSend(message);
	}
	
}

```

- 6、消费者Service开发：JmsReceiveServiceImpl.java
```
// 接口
package com.xxl.service;

/**
 * JMS.RECEIVE
 * @author xuxueli
 */
public interface IJmsReceiveService {

	/**
	 * simpleTopic订阅
	 * @param message
	 */
	public void simpleTopicSub(String message);
	
	/**
	 * simpleQuenu消费者
	 * @param message
	 */
	public void simpleQuenuConsumer(String message);
	
}

// 实现
package com.xxl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xxl.service.IJmsReceiveService;

/**
 * JMS.RECEIVE
 * @author xuxueli
 */

@Service("jmsReceiveService")
public class JmsReceiveServiceImpl implements IJmsReceiveService {
	private transient static Logger logger = LoggerFactory.getLogger(JmsReceiveServiceImpl.class);

	/*
	 * simpleTopic订阅
	 * @see com.xxl.service.IJmsReceiveService#simpleReceive(java.lang.String)
	 */
	@Override
	public void simpleTopicSub(String message) {
		logger.info("jms simpleReceive:{}", message);
	}

	/*
	 * simpleQuenu消费者
	 * @see com.xxl.service.IJmsReceiveService#simpleQuenuConsumer(java.lang.String)
	 */
	@Override
	public void simpleQuenuConsumer(String message) {
		logger.info("jms simpleQuenuConsumer:{}", message);
	}
	
}

```


### ActiveMQ 服务部署，CentOS环境

真诚总结一句：官方文档和官方教程是最便捷的学习途径。

一开始，CentOS上安装ActiviMq总是无法启动，郁闷了整整一天，直到晚上，在官网看到“Using ActiveMQ > Getting Started ”才明白是因为下载的最新版本要求高版本JDK7导致；

##### 单机部署
- 1、下载，解压，移动至运行目录：
```
wget http://mirrors.cnnic.cn/apache/activemq/5.10.2/apache-activemq-5.10.2-bin.tar.gz （版本5.11+需要jdk7+）
tar zxvf apache-activemq-5.10.2-bin.tar.gz
mv apache-activemq-5.10.2 /usr/local/activemq
cd /usr/local/activemq
```
- 2、修改默认分配内存：（默认1G，有时候太大，内存不足报错）
```
/usr/local/activemq/bin/activemq console （控制台启动，报错）
Error occurred during initialization of VM
Could not reserve enough space for object heap

原因：内存不足
查看activemq文件发现：

# Set jvm memory configuration
if [ -z "$ACTIVEMQ_OPTS_MEMORY" ] ; then
ACTIVEMQ_OPTS_MEMORY="-Xms1G -Xmx1G"
fi

我的虚拟机最大内存是512M，加上虚拟内存也不够；
解决：更改分配内存大小512M

cp /usr/local/activemq/bin/activemq /usr/local/activemq/bin/activemq.bak
vi /usr/local/activemq/bin/activemq

找到：ACTIVEMQ_OPTS_MEMORY="-Xms1G -Xmx1G"
改为：ACTIVEMQ_OPTS_MEMORY="-Xms256m -Xmx512m"
```

- 3、控制台、守护进程，启动/停止
```
// 启动
/usr/local/activemq/bin/activemq console （控制台启动）

// 守护进程启动
mkdir /home/root/activemq_log
nohup /usr/local/activemq/bin/activemq start > /home/root/activemq_log/smlog

// 检查启动
netstat -ln | grep 61616

ActiveMQ默认采用61616端口提供JMS服务，使用8161端口提供管理控制台服务，执行以下命令以便检验是否已经成功启动ActiveMQ服务。

// 停止
ps -ef | grep activemq
kill -9 5259

// 首先需要找到activemq进程的PID，然后，杀死activemq的进程（其中 -9表示强制终止）
```

- 4、控制台，web界面查看：http://127.0.0.1:8161/admin/
```
// 控制台的登录用户名密码保存在：conf/jetty-realm.properties
# username: password [,rolename ...]
admin: admin, admin
user: user, user
```

- 5、脚本启动
```
chmod 775 /usr/local/activemq/bin/activemq
mkdir /xuxueli/activemq_log/

// 新建启动脚本
vi /xuxueli/activemq_startup.sh
#####################
nohup /usr/local/activemq/bin/activemq start > /xuxueli/activemq_log/smlog 2>&1
#####################

// 脚本启动权限
chmod 755 /xuxueli/activemq_startup.sh

// 开机启动
vi /etc/rc.d/rc.local
末尾添加脚本的路径：
/xuxueli/activemq_startup.sh

// 启动：
sh /xuxueli/activemq_startup.sh
// 停止：
ps -ef|grep activemq
kill -9 29624
```

##### 集群部署
本文采用levelDB来进行持久化，并使用zookeeper实现集群的高可用。本集群**仅提供主备功能，避免单点故障，没有负载均衡功能**。

**activemq + levelDB + zookeeper工作原理：** 

使用Apach Zookeeper去协调集群中的那个节点成为master.被选择为master的节点开始工作并接收客户端的连接。其他的节点进入slave模式并连接到master同步他们的持久状态。slave节点不接受客户端的连接。所有持久操作被复制到连接的slave节点上。如果master节点死了，带着最新更新数据的slave节点晋升为master节点。失败的节点然后能够回到在线并且他进入slave模式。

所有需要同步到硬盘的消息的操作在他完成前将等待所有法定人数的节点复制完成。因此，如果你配置 replicas=”3″，那么法定人数的值是(3/2+1=2)。

master节点在他报告成功之前将在本地存储完最新的数据并等待1个其他slave节点存储完最新的数据。

当一个新的master节点要被选择的时候，你也需要至少法定人数的节点在线为能够找到一个带着最新的更新数据的节点，那个带着最新的更新数据的节点将变成新的master。因此，建议你至少运行3个重复节点以至你能down掉一个节点不影响服务的输出。

- 第1步：zookeeper集群部署；

    zookeeper监控：taokeeper-monitor、node-zk-browser

- 第2步：activemq集群配置 集群部署（levelDB会activemq自带有,所以不需要下载）

    - 配置：activemq.xml
    
        - brokerName 配置： 将broker标签的brokerName属性设置为统一的值，zookeeper才能识别它们属于同一个集群；
        - persistenceAdapter的配置：主要有三种方式：kahaDB（默认方式）、数据库持久化、levelDB（v5.9.0提供支持）
        ```
        // 首先注释掉原来kahaDB的持久化方式，然后配置levelDB+zookeeper的持久化方式
        // 注意上述配置中的hostname属性值，不同的activemq实例对应不同的hostname值，其他两个实例配置的hostname值分别为：192.168.2.145, 192.168.2.146；
        // zkAddress 为zookeeper集群地址；
        
        <!--
        <persistenceAdapter>
        <kahaDB directory="${activemq.data}/kahadb"/>
        </persistenceAdapter>
        -->
        <persistenceAdapter>
        <replicatedLevelDB
        directory="${activemq.data}/leveldb"
        replicas="3"
        bind="tcp://0.0.0.0:0"
        zkAddress="192.168.2.161:2181,192.168.2.145:2181,192.168.2.146:2181"
        zkPassword="password"
        hostname="192.168.2.161"
        sync="local_disk"
        zkPath="/activemq/leveldb-stores"
        />
        </persistenceAdapter>
        ```
    - 配置：jetty.xml：jettyPort下port：配置http监控中心端口；
    - 处理一处bug：删除lib/pax-url-aether-1.5.2.jar；注释掉配置文件中的日志配置activemq.xml中logQuery节点；这个BUG地址是https://issues.apache.org/jira/browse/AMQ-5225，希望可以在下个版本顺利解决
    
- 第三步：客户端TCP连接，改为配置集群地址

```
// 单节点（非集群方式）：
<property name="brokerURL"	value="failover:(tcp://192.168.1.15:61616)" />
// 客户端连接使用failover方案（集群方式）：节点宕掉超过N/2N+1就会整理宕掉；集群恢复后，项目会自动重连，不必重启项目；
<property name="brokerURL"	value="failover:(tcp://127.0.0.1:61611,tcp://127.0.0.1:61612,tcp://127.0.0.1:61613)?initialReconnectDelay=1000" />
```

- 注意：
    - 该模式下还是单节点负载；
    - 只是由于引入了zookeeper的监测机制。保证多个activemq服务在同一时间内只有一个服务对外开放。
    - 这种配置方案能够实现(n-1)/2的容错率，也就是三台服务器允许挂一台，五个能当掉2个，依次类推。
    - 节点宕掉超过N/2N+1就会整理宕掉；集群恢复后，项目会自动重连，不必重启项目；


### ActiveMQ 服务部署，Windows环境
- 1、下载windows版本ActiveMQ发行包；
- 2、解压安装；
- 3、启动：双击 “activemq.bat”
- 4、端口和管理
    - 默认采用“61616” 提供JMS服务；
    - 默认采用8161端口提供管理控制台服务，地址：http://127.0.0.1:8161/admin/
-5、只是修改控制台密码：修改文件“conf/jetty-realm.properties”
    ```
    # username: password [,rolename ...]
    admin: admin, admin
    user: user, user
    ```
