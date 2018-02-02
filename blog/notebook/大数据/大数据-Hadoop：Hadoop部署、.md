## 概念

- 官网：http://hadoop.apache.org/ 
- 文档：http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html

- Hadoop：
    - 一个由Apache基金会所开发的分布式系统基础架构。用户可以在不了解分布式底层细节的情况下，开发分布式程序。
    - 支持依靠横向扩展，通过不断增加廉价的商用服务器，来增加计算和存储能力。充分利用集群的威力进行高速运算和存储。
    - Hadoop的框架最核心的设计就是：HDFS和MapReduce。HDFS为海量的数据提供了存储，则MapReduce为海量的数据提供了计算。
- HDFS：Hadoop实现了一个分布式文件系统（Hadoop Distributed File System），简称HDFS。
    - HDFS有高容错性的特点，并且设计用来部署在低廉的（low-cost）硬件上。
    - 提供高吞吐量（high throughput）来访问应用程序的数据，适合那些有着超大数据集（large data set）的应用程序。
    - HDFS放宽了（relax）POSIX的要求，可以以流的形式访问（streaming access）文件系统中的数据。
- MapReduce：MapReduce为海量的数据提供了计算

- Hive：hive是基于Hadoop的一个数据仓库工具。
    - 可以将结构化的数据文件映射为一张数据库表，并提供简单的sql查询功能。
    - 可以将sql语句转换为MapReduce任务进行运行。
    - 学习成本低，可以通过类SQL语句快速实现简单的MapReduce统计，不必开发专门的MapReduce应用，十分适合数据仓库的统计分析。
    - Hive更适合于数据仓库的任务，Hive主要用于静态的结构以及需要经常分析的工作。Hive与SQL相似促使 其成为Hadoop与其他BI工具结合的理想交集。
- HBase：一个构建在HDFS上的分布式列存储系统。
    - 一个构建在HDFS上的分布式列存储系统。
    - Apache Hadoop生态系统中的重要一员，主要用于海量结构化数据存储。从逻辑上讲，HBase将数据按照表、行和列进行存储。
    - 大：一个表可以有数十亿行，上百万列；
    - 无模式：每行都有一个可排序的主键和任意多的列，列可以根据需要动态的增加，同一张表中不同的行可以有截然不同的列；
    - 面向列：面向列（族）的存储和权限控制，列（族）独立检索；
    - 稀疏：空（null）列并不占用存储空间，表可以设计的非常稀疏；
    - 数据多版本：每个单元中的数据可以有多个版本，默认情况下版本号自动分配，是单元格插入时的时间戳；
    - 数据类型单一：Hbase中的数据都是字符串，没有类型。
    
    
    
## 开发环境

环境部署：http://www.cnblogs.com/xia520pi/archive/2012/05/20/2510723.html

Eclipse安装hadoop插件：hadoop-eclipse-plugin-1.2.1.jar
(放到eclipse的plugins 目录下，重启 eclipse)

启动Hadoop，Eclipse配置Hadoop集群连接

## hadoop伪分布：CentOS + Hadoop

- 参考文档：http://blog.csdn.net/yinan9/article/details/16805275
- 官方文档：文档：http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html


    jdk：1.6.45（1.6系列最新稳定版）
    hadoop：hadoop-1.2.1（1.x系列最新稳定版）
    HBase：hbase-0.98.9
    Zookeeper：zookeeper-3.4.6（最新稳定版）
  
##### 前提、jdk安装，配置环境变量（忽略）；
      
```
# export JAVA_HOME=/usr/java/jdk1.6.0_45
# export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
# export PATH=$PATH:$JAVA_HOME/bin
```

##### 1、创建Hadoop用户及相关应用文件夹

- 1.1、同样使用root用户创建一个名为hadoop（hadoop_pwd）的新用户
```
# useradd hadoop
# passwd hadoop
```

- 1.2、创建应用文件夹，以便进行之后的hadoop配置
```
# mkdir /hadoop
# mkdir /hadoop/hdfs
# mkdir /hadoop/hdfs/data
# mkdir /hadoop/hdfs/name
# mkdir /hadoop/mapred
# mkdir /hadoop/mapred/local
# mkdir /hadoop/mapred/system
# mkdir /hadoop/tmp
```

- 1.3、将文件夹属主更改为hadoop用户
```
# chown -R hadoop /hadoop
```

##### 2、设置Hadoop用户使之可以免密码ssh到localhost：
```
# su - hadoop
# ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa  
# cat ~/.ssh/id_dsa.pub>> ~/.ssh/authorized_keys

# cd /home/hadoop/.ssh 
# chmod 600 authorized_keys
```

注意这里的权限问题，保证.ssh目录权限为700，authorized_keys为600

验证：
```    
# [hadoop@localhost .ssh]$ ssh localhost
Last login: Sun Nov 17 22:11:55 2013
```
命令ssh localhost之后无需输入密码就可以连接，配置OK

##### 3、安装配置hadoop：
```
重新切回root用户，创建目录并安装；
    # su
    # mkdir /opt/hadoop
下载，将安装文件移动到以上新建目录，确保其执行权限，然后执行
    # cd /home/hadoop
    # wget http://mirrors.hust.edu.cn/apache/hadoop/common/hadoop-1.2.1/hadoop-1.2.1.tar.gz       
    # tar -xzvf hadoop-1.2.1.tar.gz
    # mv /home/hadoop/hadoop-1.2.1  /opt/hadoop 
    # cd /opt/hadoop    
将hadoop安装目录的属主更改为hadoop用户
    # chown -R hadoop /opt/hadoop
```

##### 4、切换到hadoop用户，修改配置文件，这里根据前面创建的应用文件进行相关配置，依照各自情况而定

```
# su - hadoop
# cd /opt/hadoop/hadoop-1.2.1/conf

a、core-site.xml

<configuration>
    <property>
        <name>fs.default.name</name>
        <value>hdfs://localhost:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/hadoop/tmp</value>
    </property>
</configuration>

b、hdfs-site.xml

<configuration> 
  <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.name.dir</name>
        <value>/hadoop/hdfs/name</value>
    </property>
    <property>
        <name>dfs.data.dir</name>
        <value>/hadoop/hdfs/data</value>
    </property>
</configuration>

c、mapred-site.xml

<configuration>
  <property>
        <name>mapred.job.tracker</name>
        <value>localhost:9001</value>
    </property>
</configuration>

d、hadoop-env.sh

配置JAVA_HOME 与 HADOOP_HOME_WARN_SUPPRESS。
    PS：HADOOP_HOME_WARN_SUPPRESS这个变量可以避免某些情况下出现这样的提醒 "WARM: HADOOP_HOME is deprecated”
    # export JAVA_HOME = /usr/local/java/jdk1.6.0_45
    # export HADOOP_HOME_WARN_SUPPRESS="TRUE"    
使更新后的配置文件生效
    # source hadoop-env.sh
```

##### 5、重新配置 /etc/profile 文件，最终如：
```        
# su
# vi /etc/profile
//# export JAVA_HOME=/usr/java/jdk1.6.0_45
//# export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
# export HADOOP_HOME=/opt/hadoop/hadoop-1.2.1  
# export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

使更新后的配置文件生效

# source /etc/profile
   
测试hadoop安装：

# hadoop version  

显示 Hadoop 1.2.1 表示安装成功。
```

##### 6、启动HADOOP
```
    需要先格式化namenode，再启动所有服务
        # su hadoop  (面ssh，不需要重复输入账号密码)
        # cd /opt/hadoop/hadoop-1.2.1/bin
        # hadoop namenode -format
        # start-all.sh 
```

##### 查看进程
```
        # jps   
    显示如下
        6360 NameNode  
        6481 DataNode  
        6956 Jps  
        6818 TaskTracker  
        6610 SecondaryNameNode  
        6698 JobTracker 
    如果能找到这些服务，说明Hadoop已经成功启动了。
    如果有什么问题，可以去/opt/hadoop/hadoop-1.2.1/logs查看相应的日志
    
    问题01：namenode启动不了：
    解决方式1：重启
        # cd /opt/hadoop/hadoop-1.2.1/bin
        # stop-all.sh
        # rm  -rf /hadoop/tmp/*
        # hadoop namenode -format
        # start-all.sh 
    解决方式2：core-site.xml更该hadoop.tmp.dir的目录
        <value>/tmp/hadoop/hadoop-${user.name}</value>
    问题02：localhost:9000 not available yet, Zzzzz... 
    解决方式：
        # rm  -rf /hadoop/tmp/*
        修改/etc/hosts文件，新增映射：192.168.40.128 master    
        修改文件：core-site.xml 和 mapred-site.xml中的localhost为master
```

##### 7、最后就可以通过以下链接访问haddop服务了
```
-------开启端口：保存修改（save后永久）--
# /sbin/iptables -I INPUT -p tcp --dport 50030 -j ACCEPT
# /sbin/iptables -I INPUT -p tcp --dport 50060 -j ACCEPT
# /sbin/iptables -I INPUT -p tcp --dport 50070 -j ACCEPT
# /etc/rc.d/init.d/iptables save

http://localhost:50030/       for the Jobtracker
http://localhost:50060/       for the Tasktracker
http://localhost:50070/       for the Namenode
```


##### PS：完全分布式的安装与伪分布式安装大同小异，注意如下几点即可

    1.集群内ssh免用户登录
    2.配置文件中指定具体的ip地址(或机器名)，而不是localhost
    3.配置masters和slaves文件，加入相关ip地址(或机器名)即可
    （以上配置需要在各个节点上保持一致。）


## Hadoop分布式：CentOS + Hadoop完全分布式

官方文档：文档：http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html

