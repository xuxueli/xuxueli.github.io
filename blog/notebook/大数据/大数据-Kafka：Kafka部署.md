- [官网](http://kafka.apache.org/)
- [官方文档](http://kafka.apache.org/documentation)
- [文章1](http://blog.csdn.net/colorant/article/details/12081909/)
- [文章2](http://my.oschina.net/ielts0909/blog/110280)

### Kafka
Kafka是由Linkedin开发的一个分布式的消息队列系统(Message Queue)

kafka开发的主要初衷目标是构建一个用来处理海量日志，用户行为和网站运营统计等的数据处理框架。在结合了数据挖掘，行为分析，运营监控等需求的情况下，需要能够满足各种实时在线和批量离线处理应用场合对低延迟和批量吞吐性能的要求。从需求的根本上来说，高吞吐率是第一要求，其次是实时性和持久性。

kafka的工作方式和其他MQ基本相同，只是在一些名词命名上有些不同。为了更好的讨论，这里对这些名词做简单解释。通过这些解释应该可以大致了解kafka MQ的工作方式。
- Producer （P）：就是网kafka发消息的客户端
- Consumer （C）：从kafka取消息的客户端
- Topic （T）：可以理解为一个队列
- Consumer Group （CG）：这是kafka用来实现一个topic消息的广播（发给所有的consumer）和单播（发给任意一个consumer）的手段。一个 topic可以有多个CG。topic的消息会复制（不是真的复制，是概念上的）到所有的CG，但每个CG只会把消息发给该CG中的一个 consumer。如果需要实现广播，只要每个consumer有一个独立的CG就可以了。要实现单播只要所有的consumer在同一个CG。用CG还 可以将consumer进行自由的分组而不需要多次发送消息到不同的topic。
- Broker （B）：一台kafka服务器就是一个broker。一个集群由多个broker组成。一个broker可以容纳多个topic。
- Partition（P）：为了实现扩展性，一个非常大的topic可以分布到多个broker（即服务器）上。kafka只保证按一个partition中的顺序将消息发给consumer，不保证一个topic的整体（多个partition间）的顺序。
- Key：Kafka根据传递消息的key来进行分区的分配，即hash(key) % numPartitions；保证了相同key的消息一定会被路由到相同的分区。

kafka的集群有多个Broker服务器组成，每个类型的消息被定义为topic，同一topic内部的消息按照一定的key和算法被分区(partition)存储在不同的Broker上，消息生产者producer和消费者consumer可以在多个Broker上生产/消费topic

### 使用教程
引入maven依赖
```xml
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>1.0.0</version>
</dependency>
```

生产者代码
```java
package com.xuxueli.demo.kafka;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * kafka producer
 *
 * @author xuxueli 2017-11-11
 */
public class KafkaProducerTest {
	public static void main(String[] args) {

		// build producer
		Properties props = new Properties();
		props.put("bootstrap.servers", "127.0.0.1:9092");        // 此处配置的是kafka的端口
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");        // 配置key的序列化类
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");    // 配置value的序列化类
		props.put("acks", "all");            // 等待确认：0 = 不等待/默认、1 = 等待leader确认、-1/all = 等待所有followers确认；
		props.put("retries", 1);

		Producer<String, String> producer = new KafkaProducer<String, String>(props);

		// pub msg
		String topic = "demo_topic";
		producer.send(new ProducerRecord<String, String>(topic, "Hello"));
		producer.send(new ProducerRecord<String, String>(topic, "World"), new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception e) {
				if (e != null) {
					e.printStackTrace();
				} else {
					System.out.println("offset = " + metadata.offset());
					System.out.println("metadata = " + metadata.toString());
				}
			}
		});
		producer.flush();
		producer.close();

	}
}
```

消费者代码
```java
package com.xuxueli.demo.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * kafka consumer （producer.topic --> (1:n as topic)group.id --> (1:1 as queue)consumer）
 *
 * @author xuxueli 2017-11-11
 */
public class KafkaConsumerTest {

	public static void main(String[] args) {

		// build consumer
		Properties props = new Properties();
		props.put("bootstrap.servers", "127.0.0.1:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");		// 配置value的序列化类
		props.setProperty("group.id", "0");				// group 代表一个消费组（可根据之，实现queue队列或者topic广播）
		props.setProperty("enable.auto.commit", "true");
		props.setProperty("auto.offset.reset", "earliest");

		Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// sub msg
		String topic = "demo_topic";
		consumer.subscribe(Arrays.asList(topic));

		for (int i = 0; i < 100; i++) {
			ConsumerRecords<String, String> records = consumer.poll(1000);
			System.out.println(records.count());
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("record = " + record);
			}
		}

		//  close consumer
		consumer.close();

	}
}
```

### Kafka 安装，CentOS环境

安装文件准备：
```
cd /data/temp
wget https://archive.apache.org/dist/kafka/0.8.0/kafka_2.8.0-0.8.0.tar.gz
tar zxvf kafka_2.8.0-0.8.0.tar.gz （0.8.0版本对应jdk1.6）
mv kafka_2.8.0-0.8.0 /data/appdata/kafka_servers
cd /data/appdata/kafka_servers
cp -rf kafka_2.8.0-0.8.0 kafka01
// 修改启动jvm内存分配：修改kafka-server-start.sh即可
```

##### CentOS 单机部署

单机启动 （window路径格式：D:\\**\\**）
```
cd /data/appdata/kafka_servers/kafka01
// 启动内嵌zookeeper服务和kafka服务：
sh bin/zookeeper-server-start.sh config/zookeeper.properties
sh bin/kafka-server-start.sh config/server.properties
```

测试
```
// 创建一个topic
# bin/kafka-create-topic.sh --zookeeper localhost:2181 --replica 1 --partition 1 --topic test

// 查看topic列表
# bin/kafka-list-topic.sh --zookeeper localhost:2181

// 生产topic消息
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

// 消费topic消息
# bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning
```

##### CentOS 集群部署
- 1、集群部署zookeeper，并启动

    推荐独立安装：如：“192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183”
    
- 2、集群部署kafka，并启动
    - 分别配置“config/server.properties”
    ```
    broker.id=1     （唯一，填数字）
    host.name=192.168.56.101    （唯一，填服务器IP）
    port=9093       （端口）
    log.dir=/tmp/kafka-logs-1   （日志地址）
    zookeeper.connect=192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183    （zookeeper集群地址）
    ```
    - 分别启动kafka
    ```
    bin/kafka-server-start.sh config/server.properties  
    ```
    - 测试
    ```
    // 创建topic
    # bin/kafka-create-topic.sh --zookeeper 192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183 --replica 3 --partition 1 --topic my-replicated-topic
    // 查看broker节点状态
    # bin/kafka-list-topic.sh --zookeeper 192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183
    // 生成topic消息
    # bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
    // 消费topic消息
    # bin/kafka-console-consumer.sh --zookeeper 192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183 --from-beginning --topic my-replicated-topic
    // 测试容错，干掉master节点
    # pkill -9 -f server-1.properties
    # bin/kafka-list-topic.sh --zookeeper 192.168.56.101:2181,192.168.56.101:2182,192.168.56.101:2183
    ```




