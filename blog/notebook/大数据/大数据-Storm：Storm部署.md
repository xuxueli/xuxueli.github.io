- [官方文档](http://storm.apache.org/releases/1.1.1/index.html)
- [文章1](http://www.cnblogs.com/quchunhui/p/5370191.html)
- [文章2](https://www.cnblogs.com/hd3013779515/p/6965311.html)

#### Storm简介
- Storm是开源的、分布式、流式计算系统。
- 分布式：将一个任务拆解给多个计算机去执行，让多机器共同完成同一个任务。

#### 流式计算和批量计算区别
-- | 批量计算(Hadoop为代表) | 流式计算(Storm为代表)
--- | --- | ---
数据到达 | 计算开始前数据已准备好 | 计算进行中数据持续到来
计算周期 | 计算完成后会结束计算 | 时效性要求低的场景
使用场景 | 一般会作为服务持续运行 | 时效性要求高的场景

#### MapReduce和Storm的工作流程对比
数据特点 | 实例特点 | 实现思想 | 技术选型 | 编程模型
--- | --- | --- | --- | ---
海量、固定规模 | 批量处理 | 分而治之 | Hadoop | Map + Reduce
海量、持续增加 | 流式实时处理 | 分而治之 | Storm | Spout + Bolt

- MapReduce：Hdfs》Mapper》Reducer》Hdfs
- Storm：DataSource》Spout》SplitBolt》CountBolt》DataSink

#### 细节
- Storm采用的是主从结构
    - Nimbus/主节点/老板：
        - 只负责整体分配工作
        - 不具体干活
    - Supervisor/从节点/小组经理
        - 直接管理干活的Worker
    - Worker：
        - 工作（执行TASK）进程
- 主从结构优点
    - 主从结构：简单、高效，主节点存在单点问题
    - 对称结构：复杂、低效，但无单点问题，更可靠
- Storm作业提交运行流程
    - 1、用户使用Storm的API来编写Storm Topology
    - 2、使用Storm的Client将Topology提交给Nimbus
    - 3、Nimbus收到之后，会将把这些Topology分配给足够的Supervisor
    - 4、Supervisor收到这些Topoligy之后，Nimbus会指派一些Task给这些Supervisor。
    - 5、Nimvus会指示Supervisor为这些Task生成一些Worker
    - 6、Worker来执行这些Task来完成计算任务
- Topology = 拓扑 = 作业：
    - 点和边组成的一个有向无环图
    - 结构
        - Spout：数据源节点（水龙头，发送Tuple给下游的Bolt）
        - Bolt：普通的计算节点（发送一个Tuple给下一个Bolt；可以执行一些写数据到外部存储；）
        - Stream：数据流，点之间的边
        - Tuple：数据流中的每一条记录
    
<img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409202101812-875140827.png" width="300" >
<img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409202522187-1038237110.png" width="300" >

#### “边”的分组
在实际运行的时候，每个Spout节点都可能有很多个实例，每个Bolt也有可能有很多个实例。
Spout和Bolt的这些边里面，用户可以设置多种的Grouping的方式。
有些类似SQL中的Group By。用来制定这些计算是怎么分组的。

分组名 | 备注
--- | ---
Shuffle Grouping | 随机分组
Fields Grouping | 按字段分组，保证同字段的数据必然分给同一个Bolt
All Grouping | 广播，所有下游Bolt都收到全部数据
Global Grouping | 全局分组，下游只有一个并发时使用
None Grouping | 预留，目前等价于Shuffle Grouping
Direct Grouping | 直接指明下游的分组，比较底层的API
Local or Shuffle Grouping | 功能上类似随机分组，但会尽可能发送给同一个Worker内的Bolt，减少网络传输

#### 组件说明
- Topologies：
    - 一个topology就是一个计算节点所组成的图
    - 每个处理节点都包含处理逻辑， 而节点之间的连接则表示数据流动的方向。
    - 把你所有的代码以及所依赖的jar打进一个jar包，并且把它提交给Nimbus。storm jar负责连接到nimbus并且上传jar文件。

- Stream：
    - “一个stream是一个没有边界的tuple序列”。
    - storm提供一些原语来分布式地、可靠地把一个stream传输进一个新的stream。（比如： 你可以把一个tweets流传输到热门话题的流。）
    - storm提供的最基本的处理stream的原语是spout和bolt。（你可以实现Spout和Bolt对应的接口以处理你的应用的逻辑。）
    - “spout是流的源头”。（可从Kestrel队列读取消息并发射成流；也可调用twitter的api并返回tweets发射成流；）
    - 通常Spout会从外部数据源（队列、数据库等）读取数据，然后封装成Tuple形式，之后发送到Stream中。
    - “Spout是一个主动的角色”，在接口内部有个nextTuple函数，“Storm框架会不停的调用该函数”。
    <img src="http://www.aboutyun.com/data/attachment/forum/201404/15/225642avl8cwe7bw9nc8fm.jpg" width="300" >
    - “bolt可以接收任意多个输入stream”， 作一些处理， 有些bolt可能还会发射一些新的stream。
    - Bolt可以做任何事情: 运行函数，过滤tuple，做一些聚合，做一些合并以及访问数据库等等。
    - Bolt处理输入的Stream，并产生新的输出Stream。
    - Bolt可以执行过滤、函数操作、Join、操作数据库等任何操作。
    - “Bolt是一个被动的角色”，其接口中有一个execute(Tuple input)方法，在接收到消息之后会调用此函数，用户可以在此方法中执行自己的处理逻辑。
    <img src="http://www.aboutyun.com/data/attachment/forum/201404/15/225643wq3b3babkpeqh5z5.jpg" width="300" > 
    - “spout和bolt所组成一个网络会被打包成topology”， topology是storm里面最高一级的抽象（类似 Job）， 你可以把topology提交给storm的集群来运行。
    <img src="http://www.aboutyun.com/data/attachment/forum/201404/15/225643pjbhjccbkt94cmst.png" width="300" >
    - “topology里面的每一个节点都是并行运行的”。 可以指定每个节点的并行度
    - “一个topology会一直运行直到你显式停止它”。storm自动重新分配一些运行失败的任务， 并且storm保证你不会有数据丢失， 即使在一些机器意外停机并且消息被丢掉的情况下。

- 数据模型(Data Model)：
    - storm使用tuple来作为它的数据模型。“每个tuple是一堆值，每个值有一个名字，并且每个值可以是任何类型。”（一个tuple可以看作一个没有方法的java对象（或者是一个表的字段））
    - 总体来看，storm支持所有的基本类型、字符串以及字节数组作为tuple的值类型。你也可以使用你自己定义的类型来作为值类型， 只要你实现对应的序列化器(serializer)。
    - 一个Tuple代表数据流中的一个基本的处理单元，例如一条cookie日志，它可以包含多个Field，每个Field表示一个属性。
    - Tuple本来应该是一个Key-Value的Map，由于各个组件间传递的tuple的字段名称已经事先定义好了，所以Tuple只需要按序填入各个Value，所以就是一个Value List。
    - “一个没有边界的、源源不断的、连续的Tuple序列就组成了Stream。” 
    <img src="http://www.aboutyun.com/data/attachment/forum/201404/15/225644rwcrghywysc7rm4d.jpg" width="400" >
    - topology里面的每个节点必须定义它要发射的tuple的每个字段。

- StormAPI使用
    <img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409212048562-577629867.png" width="400" >
    
- Storm的并发机制
    - Task数量（逻辑数量）：表示每个Spout或Bolt逻辑上有多少个并发。它影响输出结果。
    - Worker数量（进程数）：代表总共有几个JVM进程去执行我们的作业。
    - Executor数量（线程数）：表示每个Spout或Bolt启动几个线程来运行。
    （下面代码中的数字表示Executor数量，它不影响结果，影响性能。）
    <img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409213754297-1419280918.png" width="400" >
    （Worker的数量在Config中设置，下图代码中的部分表示Worker数量。）
        - 本地模式中，Worker数不生效，只会启动一个JVM进行来执行作业。
        - 只有在集群模式设置Worker才有效。而且集群模式的时候一定要设置才能体现集群的价值。
    <img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409213940328-48497149.png" width="400" >
    
- Storm数据可靠性
    - 分布式系统管理多台机器，任意Worker挂掉后，系统仍能正确处理
        - 不丢（数据正确的恢复）：Acker机制保证数据如未成功处理，可以即使发现，并通知Spout重发。
        - 不重（数据不被重复计算）：使用msgID去重；
            - Spout容错API：NextTuple中，emit时，指定MsgID。
            - Bolt容错API：1、emit时，锚定输入Tuple；2、Act输入Tuple。
            


#### Storm集群搭建
- 1、安装zookeeper集群

    - zookeeper是nimbus和supervisor进行交互的中介。
    - 1、nimbus通过在zookeeper上写状态信息来分配任务。supervisor则通过从zookeeper上读取这些状态信息，来领取任务。
    - 2、supervisor、task会发送心跳到zookeeper，使得nimbus可以监控整个集群的状态，从而在task执行失败时，可以重启他们。
    
- 2、下载安装Storm（Storm 1.1.0版本单节点部署）

    1、解压安装
    2、修改 "storm.yaml"
    ```
    storm.zookeeper.servers:
        - "192.168.0.102"
    ...
    nimbus.seeds: ["192.168.0.102"]
    ... 
    drpc.servers:
        - "192.168.0.102"
    ```
    3、启动关闭服务
    ```
    // 启动nimbus
    ./storm nimbus & 
    
    // 启动ui界面 ( 地址：http://localhost:8080 )
    ./storm ui & 
    
    // 启动supervisor 
    ./storm supervisor &
    
    // 关闭
    ./storm kill
    ```
    
### Hello World
[Storm Example](https://github.com/apache/storm/tree/master/examples)
```
// 01：ExclamationTopology.java
package com.xuxueli.demo.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class ExclamationTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("word", new WordSpout(), 1);
        builder.setBolt("exclaim", new ExclamationBolt(), 1).shuffleGrouping("word");   // Tuple流向：word 》 exclaim
        builder.setBolt("print", new PrintBolt(), 1).shuffleGrouping("exclaim");        // exclaim 》 print

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
        } else {

            LocalCluster cluster = new LocalCluster();      // storm依赖，<scope>provided</scope>--> 本地开发是注释掉 -->
            cluster.submitTopology("test3", conf, builder.createTopology());
            Utils.sleep(60 * 1000);
            cluster.killTopology("test3");
            cluster.shutdown();
        }
    }
}

// 02：WordSpout.java
package com.xuxueli.demo.storm;

import org.apache.storm.topology.OutputFieldsDeclarer;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordSpout extends BaseRichSpout {
    public static Logger logger = LoggerFactory.getLogger(WordSpout.class);
    SpoutOutputCollector _collector;

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
    }

    public void nextTuple() {
        Utils.sleep(10 * 1000);
        final String[] words = new String[]{"nathan", "mike", "jackson", "golda", "bertels"};
        final String word = words[new Random().nextInt(words.length)];
        _collector.emit(new Values(word));
        logger.info(">>>>>>>>>>> word spout：{}", word);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));   // 发射数据-发射字段："emit.Values"-"declare.Fields" 需要保持一致
    }
}

// 03：ExclamationBolt.java
package com.xuxueli.demo.storm;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclamationBolt extends BaseRichBolt {
    public static Logger logger = LoggerFactory.getLogger(ExclamationBolt.class);

    OutputCollector _collector;

    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    public void execute(Tuple tuple) {
        String value = tuple.getString(0) + "!!!";

        _collector.emit(tuple, new Values(value));
        _collector.ack(tuple);
        logger.info(">>>>>>>>>>> exclaim bolt：{}", value);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

}

// 04：PrintBolt.java
package com.xuxueli.demo.storm;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintBolt extends BaseRichBolt {
    public static Logger logger = LoggerFactory.getLogger(PrintBolt.class);

    OutputCollector _collector;

    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    public void execute(Tuple tuple) {
        String value = tuple.getString(0) + " Hello World!";

        _collector.ack(tuple);
        logger.info(">>>>>>>>>>> print bolt：{}", value);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
```



    