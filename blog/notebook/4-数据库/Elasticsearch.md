<h2 style="color:#4db6ac !important" >Elasticsearch</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]


## 一、Elasticsearch 介绍

### 1、什么是 Elasticsearch
Elasticsearch 是一个基于 Lucene 的搜索和分析引擎。它提供了一个分布式多用户能力的全文搜索引擎，基于 RESTful web 接口。Elasticsearch 是用 Java 开发的，并作为 Apache 许可条款下的开放源码发布，是当前流行的企业级搜索引擎。

**Github**：https://github.com/elastic/elasticsearch

### 2、主要功能
- **全文搜索**：Elasticsearch 能够快速高效地处理全文搜索。这意味着它不仅仅能够搜索关键词，还能处理复杂的查询，比如匹配短语、布尔查询（与或非）、范围查询等。
- **数据分析**：除了搜索，Elasticsearch 还能够对数据进行深入分析。它提供了强大的聚合功能，允许你进行各种统计分析，比如求平均值、最大值、最小值、分布情况等。
- **实时数据处理**：Elasticsearch 可以处理和分析实时数据。这使得它非常适合用于日志分析和监控系统。通过与其他工具（如 Beats 和 Logstash）的集成，可以从各种数据源收集数据，并实时发送到 Elasticsearch 进行分析。
- **扩展性和高可用性**：由于其分布式架构，Elasticsearch 可以轻松扩展以处理更多的数据和查询负载。你只需增加更多的服务器节点，就能提升其处理能力。此外，Elasticsearch 通过分片和副本机制确保数据的高可用性和容错能力。

### 3、应用场景
- **日志和事件数据分析**： Elasticsearch 经常用于收集和分析日志数据。通过与 Kibana（一个可视化工具）的结合，可以创建直观的仪表盘，帮助用户实时监控和分析系统日志，快速发现和解决问题。
- **全文搜索**：许多网站和应用程序使用 Elasticsearch 作为其搜索引擎，提供快速和精准的全文搜索功能。例如，电商网站可以使用 Elasticsearch 让用户快速找到他们想要购买的商品。
- **监控和安全分析**： Elasticsearch 也用于实时监控和安全分析。通过收集和分析系统日志、网络流量等数据，可以实时监控系统运行状态，并检测和响应潜在的安全威胁。
- **数据存储和检索**： 除了搜索和分析，Elasticsearch 还可以作为一个高效的数据存储和检索系统，特别适合需要快速访问的大规模数据集。


## 二、Elasticsearch 特点与优势

### 1、分布式架构
Elasticsearch 采用分布式架构设计，数据被分成多个分片（shards）并分布存储在多个节点上。每个分片可以有一个或多个副本（replicas），以提高数据的可用性和容错性。
- **分片**：将数据分成更小的块，分布在集群中的不同节点上，提高查询和索引的性能。
- **副本**：每个分片可以有多个副本，分布在不同节点上，确保数据的高可用性和容错性。

这种分布式设计使得 Elasticsearch 能够轻松扩展，通过增加节点来处理更多的数据和更高的查询负载。

### 2. 近实时搜索
Elasticsearch 的一个显著特点是其近实时（Near Real-Time, NRT）搜索能力。当新数据被索引后，几乎立即就可以被搜索到。这对于需要快速处理和分析新数据的应用场景非常重要，例如日志分析和监控系统。

### 3. 强大的全文搜索能力
Elasticsearch 基于 Apache Lucene，具备强大的全文搜索功能。它能够处理各种复杂的查询需求，包括：
- **布尔查询**：支持 AND、OR、NOT 等逻辑操作。
- **短语查询**：能够搜索精确的短语匹配。
- **范围查询**：支持对数值、日期等范围的查询。
- **分词器和倒排索引**：通过分词器将文本分成词条，并创建倒排索引，使得全文搜索快速高效。

### 4. 丰富的分析功能
除了搜索，Elasticsearch 还提供了强大的数据分析功能。通过聚合（aggregation）功能，可以对大规模数据进行复杂的统计分析，例如：
- **计数**：计算文档的数量。
- **求和**：计算数值字段的总和。
- **平均值**：计算数值字段的平均值。
- **最大值和最小值**：找到数值字段的最大值和最小值。
- **直方图和分布**：创建数据分布的直方图和分桶统计。

### 5. 灵活的 RESTful API
Elasticsearch 提供了灵活的 RESTful API，支持通过 HTTP 请求与其交互。这使得它的使用和集成变得非常简单：
- **索引数据**：通过简单的 HTTP PUT 或 POST 请求将数据存储到 Elasticsearch 中。
- **执行查询**：通过 HTTP GET 请求执行各种搜索查询。
- **集群管理**：通过 API 进行集群的配置和管理。

### 6. 扩展性和高可用性
由于其分布式架构，Elasticsearch 具有很高的扩展性和高可用性：
- **线性扩展**：通过增加节点，可以线性地增加存储和处理能力。
- **容错能力**：通过分片和副本机制，即使某个节点出现故障，其他节点仍然可以继续工作，确保系统的高可用性。

这种设计使得 Elasticsearch 能够处理从几百兆到几百TB的数据量，适应各种规模的应用需求。

### 7. 安全性和权限控制
Elasticsearch 提供了多种安全功能，确保数据的安全性和访问控制：
- **身份验证和授权**：通过 X-Pack 插件提供用户身份验证和基于角色的访问控制。
- **加密**：支持数据在传输和存储中的加密，确保数据安全。
- **审计**：记录所有访问和操作日志，方便进行安全审计和合规检查。

### 8. 丰富的生态系统
Elasticsearch 拥有一个丰富的生态系统，包括：
- Beats：轻量级的数据收集器，可以从各种数据源收集数据并发送到 Elasticsearch。
- Logstash：强大的数据处理管道，支持数据收集、转换和加载（ETL）操作。
- Kibana：可视化工具，提供强大的数据展示和分析功能，可以创建各种仪表盘和图表。

这些工具与 Elasticsearch 紧密集成，提供了一个完整的数据收集、处理、存储、分析和可视化解决方案。

### 9. 活跃的社区和持续的发展
作为一个开源项目，Elasticsearch 拥有一个活跃的社区。社区的活跃度带来了持续的发展和改进：
- 社区支持：活跃的用户社区提供了丰富的文档、教程和论坛支持，帮助新用户快速上手。
- 持续更新：开发者不断贡献新的功能和改进，使得 Elasticsearch 保持在技术的前沿。


## 三、Elasticsearch 核心概念

### 1、索引与文档
在了解 Elasticsearch 的过程中，索引（Index）和文档（Document）是两个最基本也是最重要的概念。理解它们的作用和关系，有助于更好地掌握如何使用 Elasticsearch 存储、搜索和分析数据。

<div align="center"> <img src="https://www.xuxueli.com/blog/static/images/img_282.png" width="350px"> </div><br>

（Type，注：Elasticsearch 7.0以后弃用了Type）

#### 1.1、索引 定义
索引在 Elasticsearch 中是一个逻辑存储单元，类似于关系型数据库中的“数据库”概念。 它是一个文档的集合，这些文档具有相似的特性或属于同一逻辑分类。
- **分片**：索引可以包含一个或多个分片（Shards），每个分片都是一个 Lucene 实例，可以独立地进行搜索和存储操作。分片允许 Elasticsearch 在多个服务器上水平扩展，从而处理更多的数据和查询。
- **副本**：每个分片可以有零个或多个副本（Replicas），副本是分片的完整拷贝，用于提供数据的冗余和容错性。当某个分片所在的服务器出现故障时，可以从其副本中恢复数据。

#### 1.2、数据存储
索引中的文档被存储为 JSON 格式，这使得 Elasticsearch 能够存储结构化和非结构化数据。
Elasticsearch 使用倒排索引（Inverted Index）技术来实现高效的全文搜索。倒排索引将文档中的单词与其在文档中的位置信息关联起来，从而可以快速定位包含特定单词的文档。

#### 1.3、文档 定义
文档是 Elasticsearch 中的数据单位。每个文档是一组键值对（键是字段名，值是字段值），并且存储在某个索引中。文档的格式通常是 JSON 格式，这使得它非常灵活和易于使用。

- **记录**：在传统的关系数据库中，表中的一行就是一条记录。
- **文档**：在 Elasticsearch 中，索引中的一个文档就是一个数据条目。

例如，一个描述商品的文档可能如下：
```
{
  "product_id": "123",
  "name": "Laptop",
  "description": "A high-performance laptop",
  "price": 999.99,
  "stock": 50
}
```
这个文档包含了商品的 ID、名称、描述、价格和库存量等信息。

以下是关于 Elasticsearch 文档的一些重要概念和特点：
- **结构化数据**：文档是结构化的数据对象，由多个字段组成。每个字段都有一个名称和对应的值，可以是简单的数据类型（如文本、数字、日期等）或复杂的数据结构（如嵌套对象、数组等）。
- **唯一标识**：每个文档都有一个唯一标识符（ID），用于在索引中唯一标识该文档。ID 可以由 Elasticsearch 自动生成，也可以由用户指定。
- **索引存储**：文档被存储在索引中，每个索引可以包含多个文档。索引是文档的集合，类似于关系型数据库中的表。
- **字段映射**：文档的字段映射定义了每个字段的数据类型、分析器等属性。字段映射可以手动指定，也可以由 Elasticsearch 根据插入的文档自动推断生成。
- **全文搜索**：Elasticsearch 支持全文搜索，可以对文档中的文本字段进行全文检索。全文搜索可以根据关键词、词语匹配度等条件快速定位到符合条件的文档。
- **CRUD 操作**：文档支持 CRUD 操作，即创建（Create）、读取（Retrieve）、更新（Update）和删除（Delete）。通过 Elasticsearch 的 API 可以对文档进行增删改查操作。
- **版本控制**：Elasticsearch 支持文档的版本控制，每个文档可以有多个版本。当对文档进行更新操作时，Elasticsearch 会自动创建新版本，并保存历史版本的数据。

文档是 Elasticsearch 中存储和组织数据的基本单位，具有灵活的数据模型、强大的全文搜索和分析功能，是构建分布式搜索引擎和分布式数据存储系统的核心组件之一。

#### 1.4、索引与文档的关系
"索引与文档" 的关系类似于 "数据库与记录（相当于 MySQL 中的行（Row））的关系。一个索引包含多个文档，而每个文档属于某个索引。

#### 1.5、Elasticsearch与RDBMS的对应关系
关系型数据库和 ES 有如下对应关系：

- 关系型数据库中的表（Table）对应 ES 中的索引（Index）
- 关系型数据库中的每条记录（Row）对应 ES 中的文档（Document）
- 关系型数据库中的字段（Column）对应 ES 中的字段（Filed）
- 关系型数据库中的表定义（Schema）对应着 ES 中的映射（Mapping）
- 关系型数据库中可以通过 SQL 进行查询等操作，在 ES 中也提供了 DSL 进行查询等操作

当进行全文检索或者对搜索结果进行算分的时候，ES 比较合适，但如果对数据事务性要求比较高的时候，会把关系型数据库和 ES 结合使用。

### 2、分布式架构
Elasticsearch 的分布式架构由几个关键组件组成：集群、节点、索引、分片和副本。

#### 2.1、集群
在 Elasticsearch 中，集群（Cluster）是由一个或多个节点（Node）组成的分布式系统。这些节点协同工作，共同存储、索引和搜索数据，提供高可用性、可伸缩性和容错性。
集群是由一个或多个节点组成的集合，这些节点协同工作，共同存储数据并提供索引和搜索功能。每个集群都有一个唯一的名称，用于标识集群中的所有节点。集群中的所有节点通过网络相互通信，形成一个整体。

- **集群**：类似于一群合作完成任务的计算机。
- **节点的集合**：集群是由多个节点组成的集合。每个节点都是一个独立的 Elasticsearch 实例，可以独立运行，也可以加入到一个集群中。
- **数据分片和副本**：集群中的数据被分成多个分片（Shard），每个分片可以在集群的不同节点上进行存储和复制。分片的复制称为副本（Replica），用于提高数据的可用性和容错性。
- **负载均衡**：集群可以自动进行负载均衡，将搜索请求和索引请求分配到各个节点上，以实现数据的均衡存储和处理。
- **故障检测和容错**：集群可以检测到节点的故障并进行处理，例如自动将丢失的分片复制到其他节点上，以确保数据的完整性和可用性。
- **主节点**：集群中的主节点（Master Node）负责集群的管理和协调工作，例如分配分片、故障检测、节点加入和退出等。
- **集群状态**：集群的状态可以是健康的（Green）、部分健康的（Yellow）或者不健康的（Red），根据集群中分片的分布和副本的状态来判断。
- **动态扩展**：集群可以根据需要动态扩展，可以增加节点、增加分片副本或者增加集群中的分片数量。

<div align="center"> <img src="https://www.xuxueli.com/blog/static/images/img_283.png" width="350px"> </div><br>

#### 2.2、节点
Elasticsearch中的节点（Node）指的是Elasticsearch实例的运行实例，即一个独立的Elasticsearch服务进程。每个节点都是一个独立的工作单元，负责存储数据、参与数据处理（如索引、搜索、聚合等）以及参与集群的协调工作。
节点是集群中的一个单独服务器，它存储数据并参与集群的索引和搜索操作。每个节点都有一个唯一的名称，并且可以承担不同的角色，例如主节点（负责集群管理）或数据节点（存储数据并处理搜索请求）。节点：类似于集群中的一个成员，每个成员都有特定的任务和职责。

通过多个节点（Node）,可以组成Elasticsearch高可用集群 节点可以承担多种角色，包括但不限于：
- **主节点**（Master Node）：负责集群范围内的元数据管理和变更，如索引创建、删除、分片分配等。
- **数据节点**（Data Node）：存储实际数据和相关的索引文件，参与数据的索引、搜索和恢复过程。
- **协调节点**（Coordinating Node）：接收客户端请求，将请求路由至适当的节点，并将结果汇总返回给客户端。每个节点都可以充当协调节点，也可以专门设置某些节点仅作为协调节点。

节点可以在物理或虚拟机上单独部署，也可以在同一台机器上运行多个节点（但需注意资源分配）。节点通过HTTP协议进行通信，共同管理集群的状态和数据。在Elasticsearch集群中，多个节点协同工作，共同提供高效、可靠的数据存储和搜索服务。

#### 2.3、索引、分片和副本
在 Elasticsearch 中，数据存储在索引中。每个索引可以被分为多个分片（shards），每个分片可以有一个或多个副本（replicas）。 在 Elasticsearch 中，分片（Shard）和副本（Replica）是两个重要的概念，它们在集群中起着不同的作用。

##### 2.3.1、分片
分片是索引的一部分，是数据的基本存储单元。分片允许将索引的数据分布存储在多个节点上，从而实现数据的并行处理和存储。 每个分片是一个独立的 Lucene 实例，可以单独进行搜索和索引操作。

- **分片**：类似于将一本书拆分成多个章节，每个章节可以单独存放和阅读。
- 分片的主要作用是实现数据的分布和并行处理。通过将索引数据分成多个分片存储在不同的节点上，可以提高搜索和索引操作的并发性和吞吐量。
- 分片是 Elasticsearch 中存储数据的基本单位，每个索引（Index）都被分成多个分片，每个分片是一个独立的 Lucene 索引。
- 分片的数量在索引创建时就确定了，一旦确定就不能修改。默认情况下，每个索引会被分配 5 个主分片（Primary Shard），可以通过配置来修改。

##### 2.3.2、副本
副本是分片的复制品，用于提高数据的可用性和搜索性能。如果某个节点发生故障，副本可以提供数据的冗余存储，确保数据不会丢失。同时，副本还可以分担搜索请求的负载，提高查询性能。

- **副本**：类似于书的备份副本，确保即使原书丢失，你仍然有备份可用。
- 副本是分片的拷贝，每个分片可以有多个副本。副本的数量在索引创建时可以指定，也可以后续动态修改。
- 副本的主要作用是提高数据的可用性和容错性。当某个节点上的分片不可用时，集群可以从其它节点上的副本中提供服务，确保数据的完整性和可用性。
- 默认情况下，每个分片会有一个副本，可以通过配置来修改副本的数量。副本的数量可以根据集群的规模、性能需求和容错需求来灵活调整。


### 3、分布式架构
Elasticsearch 的分布式架构使得数据存储和查询变得高效和可靠。以下是一些关键操作及其工作方式：

- **数据分发**：当向索引中添加文档时，Elasticsearch 会自动将文档分配到不同的分片中。分片存储在不同的节点上，实现数据的分布式存储。这种分发机制确保数据可以并行处理，提高了存储和索引的速度。
- **查询分发**：当执行搜索查询时，Elasticsearch 会将查询请求分发到所有包含相关分片的节点上。这些节点并行处理查询，并将结果返回给协调节点，后者汇总所有结果并返回最终的查询结果。这种并行查询机制大大提高了搜索性能。
- **自动故障恢复**：如果集群中的某个节点发生故障，Elasticsearch 会自动检测并将分片的副本提升为主分片，确保数据的高可用性。同时，集群会重新分配分片，确保负载均衡和数据冗余。

### 4、倒排索引
倒排索引（Inverted Index）是 Elasticsearch 和其他搜索引擎的核心数据结构，用于实现高效的全文搜索。倒排索引用于存储文档集合中的每个文档的词频和位置信息，以便快速检索包含特定关键词的文档集合。

倒排索引的主要工作原理是将文档中的词汇映射到包含这些词汇的文档列表。以下是倒排索引的构建步骤和基本原理：
- 文档分词：首先，将文档中的文本分割成单独的词汇（即分词）。这一步通常使用分词器（Tokenizer）来完成。
- 去除停用词：分词后，通常会去除一些常见但无意义的词汇，如 "the"、"is" 等，这些词汇称为停用词（Stop Words）。
- 建立词汇表：创建一个包含所有唯一词汇的词汇表。
- 建立倒排列表：对于每个词汇，创建一个倒排列表，记录该词汇在哪些文档中出现以及出现的位置。

### 5、分词器
Elasticsearch 使用分词器和分析器来处理文档中的文本。分词器将文本分割成词汇，而分析器则包括分词器和一系列过滤器，用于进一步处理词汇（如去除停用词、转换为小写等）。

在创建索引之前，会对文档中的字符串进行分词。ES中字符串有两种类型，keyword和text。
- **keyword** 类型的字符串不会被分词，搜索时全匹配查询
- **text** 类型的字符串会被分词，搜索时是包含查询

不同的分词器对相同字符串分词的结果大有不同，选择不同的分词器对索引的创建有很大的影响


## 四、Elasticsearch RESTful API 基本操作示例
RESTful API 是一种遵循 REST（Representational State Transfer）架构风格的应用程序接口。它利用 HTTP 协议的各种方法（如 GET、POST、PUT、DELETE 等）进行资源的创建、读取、更新和删除（CRUD 操作）。在 Elasticsearch 中，资源通常是索引、文档和搜索查询等。

### 1、索引操作
在 Elasticsearch 中，索引类似于数据库中的表，用于存储一类相似的文档。

#### 1.1、创建索引（不设置模板）
```
## 新建索引（不设置模板）
PUT /my_article
```

响应示例：
```
{
  "acknowledged": true,
  "shards_acknowledged": true,
  "index": "my_article"
}
```

#### 1.2、新建索引（设置模板）
```
PUT /my_article
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 1
  },
  "mappings": {
    "properties": {
      "id": { 
        "type": "long" 
      },
      "title": {
        "type": "text"
      },
      "content": {
        "type": "text"
      },
      "author": {
        "type": "keyword"
      },
      "publish_date": {
        "type": "date"
      }
    }
  }
}
```

#### 1.3、更新索引模板（新增字段）
Elasticsearch 只允许在索引模板中动态添加字段，不允许修改现有字段的类型。
- 新增字段：允许，参考如下命令。
- 修改字段类型：不允许。字段类型一旦创建不能直接修改，需要重建索引（新建索引 → 迁移数据 → 别名切换）。
- 删除字段：不允许。可忽略该字段（查询时不返回），或重建索引。

```
PUT /my_article/_mapping
{
  "properties": {
    "click": {
      "type": "long"
    }
  }
}
```

#### 1.5、删除索引
```
DELETE /my_article
```

#### 1.4、查询索引模板映射
```
GET my_article/_mapping
```


### 2、文档操作

#### 2.1、添加文档（单条）
使用 POST 请求将文档添加到索引中。文档以 JSON 格式表示。
```
POST /my_article/_doc/1
{
  "id": 1,
  "title": "Elasticsearch Introduction",
  "content": "Elasticsearch is a powerful search engine.",
  "author": "John Doe",
  "publish_date": "2024-06-23"
}
```

响应示例：
```
{
  "_index": "my_article",
  "_type": "_doc",
  "_id": "1",
  "_version": 1,
  "result": "created",
  "_shards": {
    "total": 2,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 0,
  "_primary_term": 1
}
```

#### 2.2、添加文档（批量添加）
（索引“_id”内部存为字符串，值加引号；文档内容 "id"结合时机情况，此处为long直接写数值；结合时机情况写两处。）
```
POST /my_article/_bulk
{"index":{"_id":"1"}}
{
    "id": 1,
    "title": "Elasticsearch Introduction",
    "content": "Elasticsearch is a powerful search engine.",
    "author": "John Doe",
    "publish_date": "2024-06-23"
}
{"index":{"_id":"2"}}
{
    "id": 2,
    "title": "Java Introduction",
    "content": "Java is a cross-platform language.",
    "author": "Jack",
    "publish_date": "2020-01-01"
}
{"index":{"_id":"3"}}
{
    "id": 3,
    "title": "Lucene Introduction",
    "content": "Lucene is a full-text search engine.",
    "author": "Lucy",
    "publish_date": "2022-01-01"
}
```

#### 2.3、更新文档
使用 POST 请求更新已存在的文档。更新操作会覆盖指定字段的内容。
```
POST /my_article/_update/1
{
  "doc": {
    "content": "Elasticsearch is a powerful and flexible search engine."
  }
}
```

响应示例：
```
{
  "_index": "my_article",
  "_type": "_doc",
  "_id": "1",
  "_version": 2,
  "result": "updated",
  "_shards": {
    "total": 2,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 1,
  "_primary_term": 1
}
```

#### 2.4、删除文档
使用 DELETE 请求删除特定文档。
```
DELETE /my_article/_doc/1
```

响应示例：
```
{
  "_index": "my_article",
  "_type": "_doc",
  "_id": "1",
  "_version": 3,
  "result": "deleted",
  "_shards": {
    "total": 2,
    "successful": 1,
    "failed": 0
  },
  "_seq_no": 2,
  "_primary_term": 1
}
```

### 3、文档查询

ElasticSearch组合查询：
- **must**：文档 必须 匹配这些条件才能被包含进来。相当于sql中的 and。
- **must_not**：文档 必须不 匹配这些条件才能被包含进来。相当于sql中的 not。
- **should**：如果满足这些语句中的任意语句，将增加 _score ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。相当于sql中的or。
- **filter**：必须 匹配，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档。

构造查询条件：
- **term** ：查询条件中的字符串不会被分词，即使字符串中间有空格分隔，比如"This is a test"，也是作为一个整体进行查询。
    - term查询keyword字段： term查询条件不分词，keyword字段也不分词，所以需要完全匹配才能查询到。
    - term查询text字段：因为text字段会分词，而term不分词，所以term查询的条件必须是text字段分词后的某一个。
- **match** ：match查询时，对查询字符串会进行分词处理。
    - match查询keyword字段：match会被分词，而keyword不会被分词，match的分词结果跟keyword的完全匹配可以。
    - match查询text字段：match分词，text也分词，只要match的分词结果和text的分词结果有相同的就匹配。
- **match_phrase** ：
    - match_phrase匹配keyword字段：match_phrase会被分词，而keyword不会被分词，match_phrase的需要跟keyword的完全匹配才可以。
    - match_phrase匹配text字段： match_phrase是分词的，text也是分词的。match_phrase的分词结果必须在text字段分词中都包含，而且顺序必须相同，而且必须都是连续的。
- **query_string** ：这里的参数字符串是一个查询语句。在搜索之前ES会检查查询语句的语法，如果有语法错误会直接报错。
    - query_string查询key字段：query_string查询keyword字段，需要完全匹配才能查询。
    - query_string查询text字段：和match_phrase区别的是，query_string查询text类型字段，不需要连续，顺序还可以调换。
    - 说明：query_string里面还支持更加复杂的写法：
        - name: acchu nagesh：查询name包含acchu和nagesh其中的任意一个
        - book.\*:(quick OR brown)：book的任何子字段比如book.title和book.content，包含quick或者brown
        - _exists_: title：title字段包含非null值
        - name: acch*：通配符，匹配任何acch开头的字段
        - name:/joh?n(ath[oa]n)/：正则表达式，需要把内容放到两个斜杠/中间
        - name: acch~：模糊匹配，默认编辑距离为2，不过80%的情况编辑距离为1就能解决问题name: acch~1
        - count:[1 TO 5]：范围查询，或者count: >10

查询方式：简单请求参数都在URL中，用 GET；复杂条件查询，用 POST；


#### 3.1、查询文档（查询单条）
使用 GET 请求查询特定文档或执行搜索查询。
```
GET /my_article/_doc/1
```

响应示例：
```
{
  "_index": "my_article",
  "_type": "_doc",
  "_id": "1",
  "_version": 1,
  "_seq_no": 0,
  "_primary_term": 1,
  "found": true,
  "_source": {
    "title": "Elasticsearch Introduction",
    "content": "Elasticsearch is a powerful search engine.",
    "author": "John Doe",
    "publish_date": "2024-06-23"
  }
}
```

#### 3.2、查询文档（查询全部）
```
POST /my_article/_search
{
  "query": {
    "match_all": {}
  }
}
```

#### 3.3、查询文档（全文搜索）
使用 GET 请求执行全文搜索查询。
```
POST /my_article/_search
{
  "query": {
    "match": {
      "content": "search engine"
    }
  }
}
```

响应示例：
```
{
  "took": 12,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1,
      "relation": "eq"
    },
    "max_score": 0.2876821,
    "hits": [
      {
        "_index": "my_article",
        "_type": "_doc",
        "_id": "1",
        "_score": 0.2876821,
        "_source": {
          "title": "Elasticsearch Introduction",
          "content": "Elasticsearch is a powerful search engine.",
          "author": "John Doe",
          "publish_date": "2024-06-23"
        }
      }
    ]
  }
}
```

#### 3.4、查询文档（复杂条件 + 分页查询）
```
POST /my_article/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "content": "engine"
          }
        },
        {
          "range": {
            "publish_date": {
              "gte": "2000-01-01",
              "lte": "2025-12-31" 
            }
          }
        }
      ]
    }
  },
  "from": 0,  // 偏移量
  "size": 10, // 每页数量
  "sort": [
    {
      "publish_date": {
        "order": "desc"
      }
    }
  ]
}
```

#### 3.5、查询文档（复杂条件，与或非）
```
POST /my_article/_search
{
  "query": {
    "bool": {
      "must": [    // 必须满足的条件 (AND逻辑)
        { "match": { "title": "elasticsearch" } }  // 标题必须包含"elasticsearch"(分词匹配)
      ],
      "should": [  // 至少满足一个条件会提高文档评分 (OR逻辑，影响相关性评分)
        { "match": { "content": "教程" } },       // 内容最好包含"教程"（分词）
        { "term": { "author": "张三" } }          // 作者最好是"张三" (精确匹配)
      ],
      "must_not": [ // 必须不满足的条件 (NOT逻辑)
        { "range": { "publish_date": { "lt": "2023-01-01" } } }  // 排除2023年之前发布的
      ]
    }
  }
}
```

#### 3.6、查询文档（复杂条件，与或非）
```
POST /my_article/_search
{
  "query": {
    "bool": {
      "must": [    // 必须同时满足的条件(AND逻辑)
        { "match": { "title": "教程" } },   // 标题必须包含"教程"(分词匹配)
        { "range": { "id": { "gt": 1000 } } }  // ID必须大于1000
      ],
      "filter": [  // 过滤条件(精确匹配，不影响评分)
        { "term": { "author": "李四" } }    // 作者必须精确等于"李四"
      ]
    }
  }
}
```


## 五、Elasticsearch 安装与配置
下文讲解docker方式安装Elasticsearch。

### 1、创建自定义网络
```
docker network create my_network
```

### 2、启动 Elasticsearch 容器
```
docker run -d \
 --name elasticsearch \
 --net my_network \
 -p 9200:9200 \
 -p 9300:9300 \
 -e "discovery.type=single-node" \
 -e "ELASTIC_PASSWORD=elastic" \
 -e "KIBANA_PASSWORD=kibana_system" \
 -e "xpack.security.enabled=false" \
 elasticsearch:8.17.0
```

### 3、在容器内执行创建用户命令
```
## 进入容器
docker exec -it elasticsearch bash
## 新建用户
bin/elasticsearch-users useradd myuser -p myuser -r superuser

## 查看集群状态
curl -u myuser:myuser -XGET "http://localhost:9200/_cluster/health?pretty"
```

### 4、启动 Kibana 容器
```
docker run -d \
 --name kibana \
 --net my_network \
 -p 5601:5601 \
 -e "ELASTICSEARCH_HOSTS=http://elasticsearch:9200" \
 -e "ELASTICSEARCH_USERNAME=kibana_system" \
 -e "ELASTICSEARCH_PASSWORD=kibana_system" \
 -e "I18N_LOCALE=zh-CN" \
 -e "TZ=Asia/Shanghai" \
 kibana:8.17.0
```

## 其他

### 1、IDEA DataBase 连接ES

操作步骤：
- a、下载驱动：选择版本与ES版本一致：[下载地址](https://elastic.ac.cn/downloads/jdbc-client)
- b、IDEA配置驱动地址：删除老旧版本，配置新下载驱动：ES实例 > General > Goto Driver > 替换驱动；
- c、配置ES地址：配置 ip、port以及ssl（可选）。
- d、ES集群版本修改：从 “basic” 改为 “trial”，30天内允许jdbc连接。

```
## 修改为使用版本 
curl -X POST "localhost:9200/_license/start_trial?acknowledge=true&pretty"
## 响应
{
  "acknowledged" : true,
  "trial_was_started" : true,
  "type" : "trial"
}
```

```
## 查看license信息
curl -XGET http://localhost:9200/_license
```

### 2、IDEA 插件（EDQL）

插件名称：elasticsearch-EDQLs
插件地址：https://github.com/chengpohi/edql


## 资料：
https://ruanyifeng.com/blog/2017/08/elasticsearch.html
https://www.cnblogs.com/hxjcore/p/18182067
