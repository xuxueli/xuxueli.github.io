<h2 style="color:#4db6ac !important" >TiDB</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]

## 一、TiDB 简介 
TiDB 是 PingCAP 公司自主设计、研发的开源分布式关系型数据库，是一款同时支持在线事务处理与在线分析处理 (Hybrid Transactional and Analytical Processing, HTAP) 的融合型分布式数据库产品，具备水平扩容或者缩容、金融级高可用、实时 HTAP、云原生的分布式数据库、兼容 MySQL 协议和 MySQL 生态等重要特性。目标是为用户提供一站式 OLTP (Online Transactional Processing)、OLAP (Online Analytical Processing)、HTAP 解决方案。TiDB 适合高可用、强一致要求较高、数据规模较大等各种应用场景。

**Github**：https://github.com/pingcap/tidb
**文档地址**：https://docs.pingcap.com/zh/tidb/stable/overview/

## 二、核心特性及应用场景

### 2.1、五大核心特性

- 1、**一键水平扩缩容**：得益于 TiDB 存储计算分离的架构的设计，可按需对计算、存储分别进行在线扩容或者缩容，扩容或者缩容过程中对应用运维人员透明。
- 2、**金融级高可用**：数据采用多副本存储，数据副本通过 Multi-Raft 协议同步事务日志，多数派写入成功事务才能提交，确保数据强一致性且少数副本发生故障时不影响数据的可用性。可按需配置副本地理位置、副本数量等策略，满足不同容灾级别的要求。
- 3、**实时 HTAP**：提供行存储引擎 TiKV、列存储引擎 TiFlash 两款存储引擎，TiFlash 通过 Multi-Raft Learner 协议实时从 TiKV 复制数据，确保行存储引擎 TiKV 和列存储引擎 TiFlash 之间的数据强一致。TiKV、TiFlash 可按需部署在不同的机器，解决 HTAP 资源隔离的问题。
- 4、**云原生的分布式数据库**：专为云而设计的分布式数据库，通过 TiDB Operator 可在公有云、私有云、混合云中实现部署工具化、自动化。
- 5、**兼容 MySQL 协议和 MySQL 生态**： 兼容 MySQL 协议、MySQL 常用的功能、MySQL 生态，应用无需或者修改少量代码即可从 MySQL 迁移到 TiDB。提供丰富的数据迁移工具帮助应用便捷完成数据迁移。

### 2.2、四大核心应用场景

- **金融行业场景**： 金融行业对数据一致性及高可靠、系统高可用、可扩展性、容灾要求较高。传统的解决方案的资源利用率低，维护成本高。TiDB 采用多副本 + Multi-Raft 协议的方式将数据调度到不同的机房、机架、机器，确保系统的 RTO <= 30s 及 RPO = 0。
- **海量数据及高并发的 OLTP 场景**： 传统的单机数据库无法满足因数据爆炸性的增长对数据库的容量要求。TiDB 是一种性价比高的解决方案，采用计算、存储分离的架构，可对计算、存储分别进行扩缩容，计算最大支持 512 节点，每个节点最大支持 1000 并发，集群容量最大支持 PB 级别。
- **实时 HTAP 场景**： TiDB 适用于需要实时处理的大规模数据和高并发场景。TiDB 在 4.0 版本中引入列存储引擎 TiFlash，结合行存储引擎 TiKV 构建真正的 HTAP 数据库，在增加少量存储成本的情况下，可以在同一个系统中做联机交易处理、实时数据分析，极大地节省企业的成本。
- **数据汇聚、二次加工处理的场景**： TiDB 适用于将企业分散在各个系统的数据汇聚在同一个系统，并进行二次加工处理生成 T+0 或 T+1 的报表。与 Hadoop 相比，TiDB 要简单得多，业务通过 ETL 工具或者 TiDB 的同步工具将数据同步到 TiDB，在 TiDB 中可通过 SQL 直接生成报表。

## 三、TiDb 整体架构

TiDB 集群主要分为三个组件：
- １、**TiDB Server**：TiDB Server 负责接收 SQL 请求，处理 SQL 相关的逻辑，并通过 PD 找到存储计算所需数据的 TiKV 地址，与 TiKV 交互获取数据，最终返回结果。 TiDB Server是无状态的，其本身并不存储数据，只负责计算，可以无限水平扩展，可以通过负载均衡组件（如LVS、HAProxy 或F5）对外提供统一的接入地址。
- ２、**PD Server**：Placement Driver (简称 PD) 是整个集群的管理模块，其主要工作有三个： 一是存储集群的元信息（某个 Key 存储在哪个 TiKV 节点）；二是对 TiKV 集群进行调度和负载均衡（如数据的迁移、Raft group leader的迁移等）；三是分配全局唯一且递增的事务 ID。PD 是一个集群，需要部署奇数个节点，一般线上推荐至少部署 3 个节点。
- ３、**TiKV Server**：TiKV Server 负责存储数据，从外部看 TiKV 是一个分布式的提供事务的 Key-Value 存储引擎。存储数据的基本单位是 Region，每个 Region 负责存储一个 Key Range （从 StartKey 到EndKey 的左闭右开区间）的数据，每个 TiKV 节点会负责多个 Region 。TiKV 使用 Raft协议做复制，保持数据的一致性和容灾。副本以 Region 为单位进行管理，不同节点上的多个 Region 构成一个 RaftGroup，互为副本。数据在多个 TiKV 之间的负载均衡由 PD 调度，这里也是以 Region 为单位进行调度。

<div align="center"> <img src="https://www.xuxueli.com/blog/static/images/img_284.png" width="350px"> </div><br>

## 三、核心特性

### 1、水平扩展  
无限水平扩展是 TiDB 的一大特点，这里说的水平扩展包括两方面：计算能力和存储能力。
TiDB Server 负责处理 SQL 请求，随着业务的增长，可以简单的添加 TiDB Server 节点，提高整体的处理能力，提供更高的吞吐。TiKV 负责存储数据，随着数据量的增长，可以部署更多的 TiKV Server 节点解决数据 Scale 的问题。
PD 会在 TiKV 节点之间以 Region 为单位做调度，将部分数据迁移到新加的节点上。所以在业务的早期，可以只部署少量的服务实例（推荐至少部署 3 个 TiKV， 3 个 PD，2 个 TiDB），随着业务量的增长，按照需求添加 TiKV 或者 TiDB 实例。

### 2、高可用   
高可用是 TiDB 的另一大特点，TiDB/TiKV/PD 这三个组件都能容忍部分实例失效，不影响整个集群的可用性。下面分别说明这三个组件的可用性、单个实例失效后的后果以及如何恢复。

**TiDB：**
TiDB 是无状态的，推荐至少部署两个实例，前端通过负载均衡组件对外提供服务。当单个实例失效时，会影响正在这个实例上进行的 Session，从应用的角度看，会出现单次请求失败的情况，重新连接后即可继续获得服务。单个实例失效后，可以重启这个实例或者部署一个新的实例。

**PD**：
PD 是一个集群，通过 Raft 协议保持数据的一致性，单个实例失效时，如果这个实例不是 Raft 的 leader，那么服务完全不受影响；如果这个实例是 Raft 的 leader，会重新选出新的 Raft leader，自动恢复服务。PD 在选举的过程中无法对外提供服务，这个时间大约是3秒钟。推荐至少部署三个 PD 实例，单个实例失效后，重启这个实例或者添加新的实例。

**TiKV**：
TiKV 是一个集群，通过 Raft 协议（raft一致性哈算法以及Raft 为什么是更易理解的分布式一致性算法 ）保持数据的一致性（副本数量可配置，默认保存三副本），并通过 PD 做负载均衡调度。单个节点失效时，会影响这个节点上存储的所有 Region。对于 Region 中的 Leader 结点，会中断服务，等待重新选举；对于 Region 中的 Follower 节点，不会影响服务。当某个 TiKV 节点失效，并且在一段时间内（默认 30 分钟）无法恢复，PD 会将其上的数据迁移到其他的 TiKV 节点上。


_更多技术内幕：_
- 1、[三篇文章了解 TiDB 技术内幕 – 说存储](https://cn.pingcap.com/blog//tidb-internal-1/)
- 2、[三篇文章了解 TiDB 技术内幕 – 说计算](https://cn.pingcap.com/blog//tidb-internal-2/)
- 3、[三篇文章了解 TiDB 技术内幕 – 谈调度](https://cn.pingcap.com/blog//tidb-internal-3/)


## 四、TiDB 安装与配置

```
// a、拉取 docker 镜像
docker pull pingcap/tidb:v8.5.0

// b、新建本地目录
cd /Users/admin/program/docker/instance/tidb
mkdir -p ./{data,logs}

// c、启动容器
docker run -d --name tidb-standalone -p 4000:4000 -p 10080:10080 -p 2379:2379 -v $PWD/data:/tmp/tidb -v $PWD/logs:/var/log  pingcap/tidb:v8.5.0
```


