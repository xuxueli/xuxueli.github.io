<h2 style="color:#4db6ac !important" >使用Milvus搭配Ollama搭建RAG知识库</h2>
> 【原创】2026/03/07

[TOCM]

[TOC]

### 1、RAG介绍
RAG（Retrieval-Augmented Generation，检索增强生成）是一种结合信息检索与文本生成的先进AI架构，其核心在于让大语言模型在回答问题前，先从外部知识库中“查找资料”，再基于查到的信息生成准确、有依据的回答。这种方法有效缓解了大模型常见的知识过时、幻觉等问题。

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_289.jpeg "在这里输入图片标题")

#### 1.1、RAG基本原理
RAG的工作流程可分为三个关键阶段：数据准备 → 检索 → 生成，形成一个“先查后答”的闭环机制。

- **数据准备**（索引阶段）：将企业文档、网页、PDF等非结构化数据加载并切分为小块（chunking），例如每段300–800字符。 使用嵌入模型（如text-embedding-3-small）将文本块转化为向量，并存储于向量数据库中，便于后续语义检索。
- **检索阶段**（Retrieval）： 当用户提问时，系统将问题也转化为向量。 在向量数据库中通过相似度匹配（如余弦相似度）检索出最相关的若干个文本片段。 可结合关键词检索（BM25）与语义检索（DPR）进行多路召回，提升召回率与精准度。
- **生成阶段**（Generation）： 将检索到的相关片段与原始问题拼接成提示词（Prompt），输入大语言模型。 模型基于这些“参考资料”生成最终回答，确保内容有据可依，减少虚构风险。

- 关键认知：RAG的准确率瓶颈本质上是“检索上下文质量”的瓶颈。如果检索不到正确信息，再强的生成模型也无法给出正确答案。

#### 1.2、RAG应用场景
RAG因其灵活性和高准确性，已在多个领域实现落地应用，尤其适合需要专业性、实时性、可解释性的场景。

- **企业知识库问答**：员工可通过自然语言查询内部制度、产品手册、项目文档。 无需人工整理，系统自动检索并生成摘要，提升信息获取效率。
- **智能客服与售后服务**：客户咨询产品功能、退换货政策时，RAG可实时检索最新服务条款，避免因信息滞后导致误答。 支持个性化回复，如结合用户历史订单生成定制化建议。
- **医疗与法律辅助决策**：医生可输入患者症状，系统检索最新诊疗指南或临床研究，辅助诊断。律师查询合同条款时，RAG能从历史案例或法规库中提取相关判例，提升合规性。
- **学术研究与文献综述**：研究者提出研究问题后，RAG可快速检索大量论文摘要，并生成初步综述框架。节省查阅资料时间，提高科研效率。
- **动态内容生成与新闻撰写**：结合实时数据（如股市行情、体育赛事结果），RAG可生成带最新信息的报告或新闻稿。适用于财经、体育、舆情监控等对时效性要求高的领域。

#### 1.3、RAG核心技术
RAG的核心技术组成主要包括以下几个关键部分：
- **信息检索模块**（Retrieval Module）：负责从大规模文档库中检索与用户查询最相关的文档片段。通常使用向量数据库（如Faiss、Pinecone、Milvus）存储文档的向量表示，通过计算查询向量与文档向量的相似度来实现快速检索。检索算法可以是基于关键词的（如BM25）或基于语义的（如DPR、Sentence-BERT）。
- **嵌入模型**（Embedding Model）：用于将文本（文档和查询）转换为固定长度的向量表示，以便进行语义相似度计算。常用的嵌入模型包括：Sentence-BERT（SBERT）、OpenAI的text-embedding模型、通义千问的QwenEmbedding等。嵌入模型的质量直接影响检索效果。
- **生成模型**（Generation Model）：通常基于大语言模型（LLM），如GPT系列、通义千问、Llama等。接收检索到的相关文档片段和原始查询作为输入，生成最终的回答。生成模型需要具备良好的上下文理解和语言生成能力。
- **检索-生成融合机制**（Retrieval-Generation Fusion）：将检索到的文档片段与原始查询组合成提示（Prompt），输入到生成模型中。这个过程可以是简单的拼接，也可以是更复杂的融合策略，如注意力机制。
- **向量数据库**（Vector Database）：用于高效存储和检索高维向量数据。支持快速的近似最近邻（ANN）搜索，是实现大规模文档检索的关键。常见的向量数据库包括：Faiss、Pinecone、Milvus、Weaviate等。
- **数据预处理与后处理**：数据预处理包括文档清洗、分块、去除无关内容等，以提高检索效率和质量。后处理可能包括答案过滤、格式化输出、引用标注等，以提升最终回答的可读性和可信度。这些组件协同工作，使得RAG能够在保持大语言模型强大生成能力的同时，通过外部知识库提供更准确、更可靠的问答结果。

通过上述组件协同工作，使得RAG能够在保持大语言模型强大生成能力的同时，通过外部知识库提供更准确、更可靠的问答结果。
本文选型 “Milvus（向量数据库）、Qwen3.5（生成模型）、qwen3-embedding（嵌入模型）及SpringAI” 讲述及实践。

### 2、向量数据库
向量数据库是专门用于存储、管理和高效检索高维向量数据的新型数据库系统，它能将文本、图像、音频等非结构化数据，通过AI模型转化为蕴含语义特征的向量序列，再基于向量间的相似度实现“语义级检索”，解决传统数据库在非结构化数据处理上的局限性，为RAG智能问答、多模态搜索、智能推荐等AI应用提供底层支撑。

#### 2.1、核心工作步骤
- **数据向量化**：生成“特征指纹”。这是向量数据库的前置核心环节，需借助Embedding模型将原始非结构化数据转化为高维向量，同时要平衡向量维度：维度越高特征表达越精细、检索精度越高，但存储和计算成本会指数级增长；维度越低效率越高，但可能丢失关键特征导致精度下降，工业级常规选择文本768-1536维、图像512-2048维。
    - 文本数据：可选用OpenAI的text-embedding-ada-002（通用场景最优，1536维）、国产开源的BGE（性价比之选，768维）、微调后的BERT（细分领域首选）等模型。
    - 图像数据：CLIP（支持文本搜图的多模态适配模型）、ResNet（纯图像特征提取模型）是常用工具。
    - 音频数据：Wav2Vec2（语音转向量）、VGGish（音频场景特征提取）可满足需求。
- **存储与索引构建**：加速相似性计算。向量数据库会将生成的高维向量存储起来，并构建特殊索引结构来提升检索效率，常见索引算法有IVF（倒排文件）、HNSW（分层可导航小世界图）等，它们能大幅降低相似性计算的耗时。
- **相似性检索**：找“最近邻”。当用户发起查询时，系统先将查询内容转为向量，再在数据库中寻找与其“距离最近”的Top-K个向量，常用的距离度量方式有三种：
  - 余弦相似度：最常用，只关注向量方向、忽略长度，适合语义级对比，如文本检索，计算结果取值范围[-1,1]，越接近1相似度越高。
  - 欧氏距离：计算两个向量的直线距离，同时考虑方向与长度，适合关注绝对特征差异的场景，如图像检索，值越小相似度越高，需提前对向量进行归一化处理。
  - 点积相似度：计算速度最快，但受向量长度影响大，对向量进行L2归一化后，结果等价于余弦相似度，适合高并发低延迟场景，如实时推荐。

#### 2.2、常见向量数据库对比

| 数据库名称 | 类型 | 规模能力 | 运维复杂度 | 核心特性 | 适用场景 | 优劣势 |
| --- | --- | --- | --- | --- | --- | --- |
| Milvus | 原生开源 | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | 支持IVF、HNSW等多种索引，横向可伸缩至1000+节点，支持动态字段、分区、并行查询 | 大规模、多并发检索；需要复杂过滤、实时增量写入的生产环境 | 优势：企业级SLA，海量数据（10+亿级）性能出色，社区活跃生态完善；劣势：运维相对复杂，对小规模单机部署略显笨重 |
| Qdrant | 原生开源 | ⭐⭐⭐⭐ | ⭐⭐ | Rust内核，支持多向量字段和分段索引，二级过滤与布尔查询能力强 | 对过滤/布尔查询依赖度高的语义搜索；中小规模集群 | 优势：延迟低、吞吐高，对复杂过滤条件性能友好；劣势：大规模分布式部署方案较新，需自行打磨 |
| Weaviate | 原生开源/托管 | ⭐⭐⭐ | ⭐⭐⭐⭐ | 兼顾文本与向量检索，内置CLIP、OpenAIEmbeddings等模型，原生GraphQL接口，自动schema管理 | PoC验证、多模态实验、快速上线MVP；知识图谱、语义搜索场景 | 优势：上手快无需额外模型接入，多模态查询统一API；劣势：集群扩展性弱于Milvus，企业级性能需商业版 |
| PGVector | 传统数据库扩展 | ⭐⭐ | ⭐ | 作为PostgreSQL的扩展，可直接对接现有PostgreSQL生态 | 已在使用PostgreSQL，需要快速集成向量检索能力的场景 | 优势：无需额外部署，快速集成；劣势：规模能力有限，功能相对基础 |
| Redis Vector | 传统数据库扩展 | ⭐⭐ | ⭐⭐⭐ | 基于Redis的向量检索扩展，支持hybridsearch、namespace多租户 | 实时召回、对低延迟要求高的场景 | 优势：依托Redis的高性能，实时性出色；劣势：功能聚焦向量检索，生态相对单一 |
| Elasticsearch | 搜索类扩展 | ⭐⭐⭐ | ⭐⭐⭐⭐ | 兼具全文搜索与向量检索能力，可对接现有Elasticsearch体系 | 已有Elasticsearch架构，需要融合搜索与向量检索的场景 | 优势：搜索能力成熟，生态完善；劣势：向量检索性能弱于原生向量数据库 |
| FAISS | 算法库 | ⭐⭐⭐⭐ | ⭐ | 由Facebook开发的高效相似性搜索库，支持多种索引算法 | 算法研究、小规模数据向量检索测试 | 优势：算法丰富，灵活度高；劣势：无分布式支持，不适合生产级大规模场景 |

选型速览建议：
- 海量数据+平台化需求：优先选Milvus
- 高吞吐低延迟+工程友好：优先选Qdrant
- 已在用PostgreSQL：优先选PGVector
- 快速集成向量检索：优先选Redis Vector
- 融合全文搜索与向量检索：优先选Elasticsearch
- 算法研究或小规模测试：优先选FAISS

### 3、向量核心概念
#### 3.1、向量
向量是向量数据库的核心数据载体，本质是用一组有序数值（如[0.1, 0.5, -0.3]）表示的对象特征，每个数值对应一个维度，维度数量取决于数据的复杂度：
- 文本向量：通过Embedding模型将文字转化为向量，比如“猫”和“狗”的向量在语义维度上距离较近；
- 图像向量：提取图像的颜色、纹理、形状等特征转化为高维向量，常见维度为512-2048维；
- 音频/视频向量：通过特征提取算法将时序数据转化为固定维度的向量。

向量的核心价值是把非结构化数据转化为机器可计算的数值形式，让计算机能通过数值运算理解数据的语义关联。

#### 3.2、向量与AI
向量是AI理解非结构化数据的核心工具，它将文字、图像、声音等转化为高维空间中的数字列表，使AI能够“计算”语义相似性，支撑推荐系统、语义搜索和大模型的智能表现。

**AI的“通用语言”**：
AI无法直接处理自然语言或图像像素，必须通过嵌入模型（如BERT、CLIP）将它们转化为向量。这个过程就像给每段内容发放一张“数字身份证”，让机器可以比较、分类和检索。
例如，“猫”可能被表示为 [0.92, 0.88, 0.85, ..., 0.40] 这样的768维向量，其中每个数值代表“毛茸茸”“独立性”等隐含特征的强度。

**向量如何驱动AI应用？**：
- 语义搜索：传统搜索依赖关键词匹配，而向量搜索能理解“适合跑步的舒适鞋”与“慢跑运动鞋”的语义关联，提升意图识别准确率。
- 个性化推荐：用户和商品都被表示为向量，平台通过计算相似度，为用户推荐最可能感兴趣的内容。
- 大模型增强：大语言模型（LLM）结合向量数据库实现检索增强生成（RAG），在回答问题时调用外部知识库，避免“幻觉”并提升准确性。

**向量数据库：AI的“长期记忆体”**：
当AI需要记住大量信息时，传统数据库无法高效处理高维向量。向量数据库（如Milvus、Pinecone）专为此设计，支持毫秒级相似性搜索，成为AI系统的“外挂大脑”。
其核心能力包括：
近似最近邻搜索（ANN）：使用HNSW、IVF等算法，在亿级数据中快速找到最相似结果。
- 多模态支持：统一处理文本、图像、音频的向量表示，实现“以图搜图”“以文搜音”等功能。

#### 3.3、Embedding（嵌入）
Embedding是将非结构化数据（文本、图像、音频等）转化为向量的技术过程，是向量数据库的“数据预处理核心”：
- 核心原理：通过深度学习模型（如Transformer、CNN）学习数据的语义特征，将高维度的原始数据压缩为低维度的稠密向量，同时保留数据间的语义关联，比如“北京”和“中国”的向量距离会比“北京”和“巴黎”更近；
- 常见类型：
  - 文本Embedding：如OpenAI的text-embedding-ada-002、Sentence-BERT；
  - 图像Embedding：如CLIP、ResNet；
  - 多模态Embedding：如支持文本+图像联合检索的CLIP模型；
- 关键作用：决定了向量的质量，高质量的Embedding能让向量数据库的相似性搜索更精准，是影响最终业务效果的核心因素之一。

#### 3.4、相似性测量 (Similarity Measurement)
相似性测量是计算两个向量之间语义关联程度的方法，是向量数据库判断数据“相似性”的核心逻辑，常见算法及适用场景如下：

| 算法类型 | 计算逻辑 | 适用场景 |
| --- | --- | --- |
| 余弦相似度 | 计算两个向量的夹角余弦值，值越接近1越相似 | 文本、图像等高维向量场景，是主流选择 |
| 欧氏距离 | 计算两个向量的直线距离，值越小越相似 | 低维向量或数值型特征场景 |
| 曼哈顿距离 | 计算两个向量各维度差值的绝对值之和 | 稀疏向量或特征权重差异大的场景 |
| 内积 | 计算两个向量的点积，值越大越相似 | 高维稠密向量的快速匹配场景 |

#### 3.5、相似性搜索 (Similarity Search)
相似性搜索是向量数据库的核心功能，指根据输入的查询向量，在海量向量数据中快速找到语义最相近的Top-K个向量，核心分为两种类型：
- 精确相似性搜索：遍历所有向量计算相似度，结果精准但性能低，仅适用于小规模数据（百万级以内）；
- 近似相似性搜索（ANN）：通过索引算法（如HNSW、IVF）缩小搜索范围，牺牲极小精度换取超高性能，是大规模向量数据库的主流方案，比如Milvus、Qdrant都默认采用ANN算法。
相似性搜索的典型应用场景包括：RAG知识库问答、电商商品推荐、图像检索、文档去重等。

### 4、Milvus介绍
Milvus 是一款专为高维向量数据设计的云原生向量数据库，广泛应用于人工智能、机器学习和相似性搜索场景。它采用存储与计算分离的架构，具备高可用性、高性能和弹性扩展能力。

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_290.jpeg "在这里输入图片标题")

#### 4.1、核心架构层次 
Milvus 的系统架构分为四个主要层次：
- **接入层**（Access Layer）：作为系统的入口，由一组无状态的 Proxy 组件构成，负责请求路由和负载均衡。
- **协调服务**（Coordinator Service）：管理元数据、任务调度和状态同步，包括 Root Coordinator、Data Coordinator 和 Index Coordinator 等。
- **执行节点**（Worker Node）：处理实际的数据插入、查询和索引构建等操作，包含 Query Node、Index Node 和 Data Node。
- **存储层**（Storage Layer）：负责持久化存储，使用对象存储（如 S3、MinIO）来保存向量数据和索引文件，同时通过 etcd 和 Pulsar/Kafka 管理元数据和日志。

#### 4.2、核心概念与定位：

| 维度 | Milvus | 关系型数据库（如 MySQL） | 说明 |
| --- | --- | --- | --- |
| 设计目标 | 专为高维向量数据设计，用于相似性搜索 | 处理结构化数据，支持复杂事务与关联查询 | Milvus 专注于语义级检索，关系库聚焦精确匹配 |
| 数据类型 | 向量（FLOAT_VECTOR、BINARY_VECTOR）+ 标量（INT、VARCHAR） | 标量字段（INT、VARCHAR、DATE 等） | Milvus 以向量为核心，支持标量过滤；关系库以结构化字段为主 |
| 数据模型 | Collection（集合）+ Entity（实体）+ Field（字段） | Table（表）+ Row（行）+ Column（列） | Milvus 的 Collection 类似表，但支持向量索引；关系库以表结构组织数据 |

#### 4.3、数据模型与存储机制:

| 维度 | Milvus | 关系型数据库 | 说明 |
| --- | --- | --- | --- |
| 数据组织结构 | Database → Collection → Partition → Segment → Entity | Database → Table → Row | Milvus 以 Segment 为最小存储单元，支持分片；关系库以页或块为单位 |
| 存储介质 | 对象存储（S3/MinIO）+ 元数据存储（etcd）+ 消息队列（Pulsar/Kafka） | 磁盘文件 + 日志（Redo Log） | Milvus 使用对象存储持久化数据，元数据由 etcd 管理；关系库依赖本地存储 |
| 索引机制 | 支持多种 ANN 索引（HNSW、IVF、FLAT 等） | B-tree、Hash、Bitmap 等 | Milvus 为高维向量优化索引，支持近似搜索；关系库为低维结构化字段设计 |

#### 4.4、查询与检索方式:

| 维度 | Milvus | 关系型数据库 | 说明 |
| --- | --- | --- | --- |
| 查询类型 | 向量相似性搜索（ANN）、标量过滤、混合查询 | 精确匹配、范围查询、JOIN、聚合 | Milvus 支持语义相似性搜索，可结合标量条件过滤；关系库支持复杂 SQL 查询 |
| 查询语言 | SDK/REST API + 过滤表达式 | SQL | Milvus 无标准 SQL，依赖 API；关系库使用 SQL 作为标准查询语言 |
| 结果输出 | 相似度得分 + 实体元数据 | 匹配结果 + 字段值 | Milvus 返回排序列表；关系库返回匹配记录 |

#### 4.5、术语映射关系:

| Milvus 术语 | 关系型数据库术语 | 说明                                                                                                                         |
| --- | --- |----------------------------------------------------------------------------------------------------------------------------|
| Database | Database | 数据库是组织和管理数据的逻辑单元。为了提高数据安全性并实现多租户，你可以创建多个数据库，为不同的应用程序或租户从逻辑上隔离数据。Milvus 在集合之上引入了数据库层，为管理和组织数据提供了更有效的方式，同时支持多租户              |
| Collection | Table | 数据集合，定义字段结构。用于存储和管理实体的主要逻辑对象。                                                                                              |
| Partition | Partition | 集合内的物理分区                                                                                                                   |
| Segment | Page / Block | 定义数据类型和数据属性的元信息。每个 Collections 都有自己的 Collections Schema，该 Schema 定义了 Collections 的所有字段、自动 ID（主键）分配启用和 Collection 说明 |
| Field | Column | 字段类型支持标量与向量                                                                                                                |
| Entity | Row | 单条数据记录                                                                                                                     |
| Index | Index | 向量索引，类型多样                                                                                                                  |

### 5、Milvus本地部署

#### 5.1、Docker Compose 部署
Milvus 提供了 Docker Compose 配置文件，[安装命令](https://milvus.io/docs/zh/install_standalone-docker-compose.md)：

```shell
// 切换目录
cd /Users/admin/program/docker/instance/milvus

// 下载安装
wget https://github.com/milvus-io/milvus/releases/download/v2.6.11/milvus-standalone-docker-compose.yml -O docker-compose.yml

sudo docker compose up -d

Creating milvus-etcd  ... done
Creating milvus-minio ... done
Creating milvus-standalone ... done

// 关闭容器
sudo docker-compose down
```

启动完成后可以访问 Milvus WebUI网址（ http://127.0.0.1:9091/webui/ ）了解有关 Milvus 实例的更多信息。
有关详细信息，请参阅 [Milvus WebUI](https://milvus.io/docs/zh/milvus-webui.md#Milvus-WebUI)。

部署注意事项：
a、若"docker compose" 如果无法识别：可以切换为 "docker-compose" 命令。
```
sudo docker-compose up -d
sudo docker-compose down
```
b、若启动报错："docker-credential-desktop": executable file not found in $PATH：可执行如下命令
```
cat ~/.docker/config.json
---
方式1、查找并删除或修改 credsStore 字段：
方式2、或者将 credsStore 改为 credStore：
```

#### 5.2、Attu（可视化工具）安装

[Attu](https://github.com/zilliztech/attu) 是 Milvus 官方推出的图形化管理工具，提供直观的可视化界面，方便用户查看和管理向量数据库。通过 Attu，用户可以轻松完成数据库架构设计、数据操作、向量搜索等复杂任务，大大降低 Milvus 的使用门槛。

**a、Docker 单独部署** 

Docker 是最简单且跨平台的安装方式，可执行如下命令安装。

```shell
docker run -d --name milvus-attu \
  -p 8000:3000 \
  -e MILVUS_URL=localhost:19530 \
  zilliz/attu:v2.6
```

**b、Docker Compose 集成部署**

通过 Docker Compose 与 Milvus 一并部署，在 milvus-standalone-docker-compose.yml 中添加如下内容即可：

```yaml
attu:
  container_name: attu
  image: zilliz/attu:v2.6
  environment:
    MILVUS_URL: standalone:19530
  ports:
    - "8000:3000"
  depends_on:
    - "standalone"
```

Attu 启动完成后可以访问（ http://localhost:8000 ），以图形化方式查看和管理Milvus 实例。
有关详细信息，请参阅 [文档](https://milvus.io/docs/zh/quickstart_with_attu.md#Quick-Start-with-Attu-Desktop)。

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_291.jpeg "在这里输入图片标题")

### 6、模型本地安装

RAG系统依赖Embedding与Generation两类模型：
- **嵌入模型**（Embedding Model）：用于将文本（文档和查询）转换为固定长度的向量表示，以便进行语义相似度计算。常用的嵌入模型包括：Sentence-BERT（SBERT）、OpenAI的text-embedding模型、通义千问的QwenEmbedding等。嵌入模型的质量直接影响检索效果。
- **生成模型**（Generation Model）：通常基于大语言模型（LLM），如GPT系列、通义千问、Llama等。接收检索到的相关文档片段和原始查询作为输入，生成最终的回答。生成模型需要具备良好的上下文理解和语言生成能力。

本文分别选择 “qwen3-embedding” 与 “qwen3.5” 作为嵌入模型与生成模型，Ollama本地安装如下；
```
admin@Mac-miniM4 milvus % ollama list
NAME                                ID              SIZE      MODIFIED    
qwen3.5:2b                          324d162be6ca    2.7 GB    3 hours ago    
qwen3-embedding:0.6b                ac6da0dfba84    639 MB    4 hours ago    
```

### 7、Java接入示例

#### 7.1、Maven依赖引入

使用SpringAI进行模型集成，需要添加如下依赖：

```
<!-- milvus -->
<dependency>
    <groupId>io.milvus</groupId>
    <artifactId>milvus-sdk-java</artifactId>
    <version>2.6.14</version>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
    <version>2.0.0-M2</version>
</dependency>
```

#### 7.2、Embedding 代码示例

使用Ollama集成Embedding模型 "qwen3-embedding"，针对内容进行向量转化。示例代码如下：

```java
    @Test
    public void test() {

        // 1、测试文本
        String testText = "Java 跨平台面向对象语言，强类型安全与自动内存管理，支撑企业级应用与Android开发。";

        // 生成嵌入向量
        logger.info("【测试文本】Embedding: ");
        float[] embeddedVector = embed(List.of(testText)).get(0);
        logger.info("\n Text：{} \n Vector:{}", testText, embeddedVector);

        // 2、批量生成嵌入向量
        logger.info("【批量测试文本】Embedding: ");
        List<String> testTexts = List.of(
                "Java 一次编写到处运行，丰富的开发工具链，适合构建高并发、可扩展的后端服务。",
                "Java 多线程与网络编程支持完善，生态涵盖Spring、Hibernate等框架，适配各类系统开发。",
                "MySQL 开源关系型数据库，支持事务与数据完整性，广泛用于Web应用与数据分析场景。"
        );

        List<float[]> embeddings = embed(testTexts);
        for (int i = 0; i < testTexts.size(); i++) {
            logger.info("\n Text - {}：{} \n Vector:{}", (i+1), testTexts.get(i), embeddings.get(i));
        }

    }

    public static List<float[]> embed(List<String> texts) {
        return getEmbeddingModel().embed(texts);
    }

    public static EmbeddingModel getEmbeddingModel() {
        // 1、创建Ollama API实例
        OllamaApi ollamaApi = OllamaApi.builder().baseUrl("http://localhost:11434").build();

        // 2、创建Ollama嵌入模型实例
        OllamaEmbeddingModel embeddingModel = OllamaEmbeddingModel
                .builder()
                .defaultOptions(OllamaEmbeddingOptions
                        .builder()
                        .model("qwen3-embedding:0.6b")
                        .dimensions(1024)
                        .build())
                .ollamaApi(ollamaApi)
                .build();
        return embeddingModel;
    }
```

#### 7.3、Milvus集成Embedding模型示例

集成Milvus与Embedding模型，初始化Milvus数据库与集合。预先针对待查询数据进行向量处理与存储，然后针对查询输入数据进行向量转化并发起查询。示例代码如下：

```
    @Test
    public void test41() throws InterruptedException {

        // param
        String bookCollection = "book_collection_01";

        // 1、build 连接
        MilvusClientV2 client = buildClient();

        // 2、初始化数据库
        initDatabase(client, DB_NAME);
        client.useDatabase(DB_NAME);

        // 3、初始化 Collections
        initCollection(client, bookCollection);

        // 4、初始化数据（向量）
        initData(client, bookCollection);

        // 5、向量查询
        vectorQuery(client, bookCollection);

    }

    private void vectorQuery(MilvusClientV2 client, String bookCollection) {
        // param
        String keyword = "Java";
        logger.info("搜索关键词:{}", keyword);

        // 1、embed
        float[] keyVectors = OllamaVectorTest1.embed(Arrays.asList(keyword)).get(0);

        // 2、FloatVec
        FloatVec queryVector = new FloatVec(keyVectors);
        SearchReq searchReq = SearchReq.builder()
                .collectionName(bookCollection)
                .data(Collections.singletonList(queryVector))
                .annsField("description")
                .outputFields(Arrays.asList("bookId", "title", "description"))
                .topK(3)
                .build();

        // 3、search
        SearchResp searchResp = client.search(searchReq);

        List<List<SearchResp.SearchResult>> searchResults = searchResp.getSearchResults();
        logger.info("向量搜索结果:{}", searchResp);
        for (List<SearchResp.SearchResult> results : searchResults) {
            System.out.println("TopK results:");
            for (SearchResp.SearchResult result : results) {
                Book book = new Book((Long) result.getEntity().get("bookId"), (String) result.getEntity().get("title"), null);
                System.out.println(book);
            }
        }
    }

    private void initData(MilvusClientV2 client, String bookCollection) {

        // 1、java data
        List<Book> books = Arrays.asList(
                new Book(1, "Java核心技术", "深入解析Java语言特性与开发技巧，涵盖多线程、内存管理等核心内容，适合中高级开发者提升技能。"),
                new Book(2, "Effective Java", "通过52个实用技巧优化Java代码质量，减少常见错误，提升程序健壮性与可维护性。"),
                new Book(3, "Java并发编程实战", "系统讲解并发编程原理与实践，包括线程池、锁机制等，助力高并发场景开发。"),
                new Book(4, "MySQL数据库设计与优化", "从基础语法到高级优化，结合实例解析索引策略、分库分表等，提升数据库性能。"),
                new Book(5, "运动生理学", "科学解析运动中的身体机能变化，涵盖能量代谢、肌肉训练原理，适合健身爱好者与专业运动员。")
        );

        // 2、查询数据
        List<Object> bookIds = books.stream().map(Book::getBookId).collect(Collectors.toList());
        GetReq getReq = GetReq.builder()
                .collectionName(bookCollection)
                .ids(bookIds)
                .outputFields(Arrays.asList("bookId", "title", "description"))
                .build();

        GetResp getResp = client.get(getReq);
        List<QueryResp.QueryResult> results = getResp.getGetResults();
        if (results.size() == books.size()) {
            logger.info("books 数据全部已存在");
            return;
        }
        /*for (QueryResp.QueryResult result : results) {
            System.out.println(result.getEntity());
        }*/

        // 3、embedding
        Gson gson = new Gson();
        List<JsonObject> data = books.stream().map(book -> {
            // embedding
            float[] descriptionVector = OllamaVectorTest1.embed(Arrays.asList(book.getDescription())).get(0);
            // map
            Map<String, Object> map = Map.of(
                    "bookId", book.getBookId(),
                    "title", book.getTitle(),
                    "description", descriptionVector
            );
            // json
            return gson.toJsonTree(map).getAsJsonObject();
        }).toList();

        // 4、do upsert
        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(bookCollection)
                .data(data)
                .build();

        UpsertResp upsertResp = client.upsert(upsertReq);
        logger.info("books 数据初始化成功 :{}", upsertResp);
    }

    private void initCollection(MilvusClientV2 client, String bookCollection) {
        // 0、check if exists
        Boolean hasCollection = client.hasCollection(HasCollectionReq
                .builder()
                .collectionName(bookCollection)
                .build());

        if (hasCollection) {
            logger.info("Collection {} 已存在.", bookCollection);
            return;
        }

        // 1、Collection Schema
        CreateCollectionReq.CollectionSchema schema = client.createSchema();
        schema.addField(AddFieldReq.builder()
                .fieldName("bookId")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("title")
                .dataType(DataType.VarChar)
                .maxLength(512)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("description")
                .dataType(DataType.FloatVector)
                .dimension(1024)
                .build());


        // 2、Index Param
        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName("bookId")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .build();

        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("description")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .metricType(IndexParam.MetricType.COSINE)
                .build();

        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(indexParamForIdField);
        indexParams.add(indexParamForVectorField);

        // 3、创建 Collections
        CreateCollectionReq customizedSetupReq1 = CreateCollectionReq.builder()
                .collectionName(bookCollection)
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build();

        client.createCollection(customizedSetupReq1);
        logger.info("Collection {} 创建成功.", bookCollection);
    }

    private void initDatabase(MilvusClientV2 client, String databaseName) {

        // 查看全部数据库，判断存在
        ListDatabasesResp listDatabasesResp = client.listDatabases();
        if (listDatabasesResp.getDatabaseNames().contains(DB_NAME)) {
            logger.info("database（{}）已存在", databaseName);
            return;
        }

        // 新建数据库
        CreateDatabaseReq createDatabaseReq = CreateDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build();
        client.createDatabase(createDatabaseReq);
        logger.info("database（{}）新建成功", databaseName);
    }
    
    private MilvusClientV2 buildClient() {
        ConnectConfig config = ConnectConfig.builder()
                .uri("http://localhost:19530")
                .build();
        MilvusClientV2 client = new MilvusClientV2(config);
        return client;
    }

    public static class Book {
        private long bookId;
        private String title;
        private String description;

        public Book(long bookId, String title, String description) {
            this.bookId = bookId;
            this.title = title;
            this.description = description;
        }

        public long getBookId() {
            return bookId;
        }

        public void setBookId(long bookId) {
            this.bookId = bookId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Book{" +
                    "bookId=" + bookId +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
```

#### 7.4、Milvus常规操作示例

针对Milvus的数据库、集合、数据、索引等操作，示例代码如下：

```
    // ******************** db ********************
    private static String DB_NAME = "my_database_1";

    // 测试连接
    @Test
    public void test01() {
        MilvusClientV2 client = buildClient();

        // 测试连接
        CheckHealthResp checkHealthResp = client.checkHealth();
        logger.info("checkHealthResp:{}", checkHealthResp);
    }

    private MilvusClientV2 buildClient() {
        ConnectConfig config = ConnectConfig.builder()
                .uri("http://localhost:19530")
                .build();
        MilvusClientV2 client = new MilvusClientV2(config);
        return client;
    }

    // 创建数据库
    @Test
    public void test02() {
        MilvusClientV2 client = buildClient();

        // 创建数据库
        CreateDatabaseReq createDatabaseReq = CreateDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build();
        client.createDatabase(createDatabaseReq);
    }

    // 查看数据库
    @Test
    public void test03() {
        MilvusClientV2 client = buildClient();

        // 查看全部数据库
        ListDatabasesResp listDatabasesResp = client.listDatabases();
        logger.info("listDatabasesResp:{}", listDatabasesResp);

        // 查看单个数据库
        DescribeDatabaseResp descDBResp = client.describeDatabase(DescribeDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build()
        );
        logger.info("descDBResp:{}", descDBResp);
    }

    // 修改数据库属性
    @Test
    public void test04() {
        MilvusClientV2 client = buildClient();

        // 修改数据库属性
        client.alterDatabaseProperties(AlterDatabasePropertiesReq.builder()
                .databaseName(DB_NAME)
                .property("database.max.collections", "10")
                .build());

        // 查看数据库
        DescribeDatabaseResp descDBResp = client.describeDatabase(DescribeDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build()
        );
        logger.info("descDBResp:{}", descDBResp);
    }

    // 删除数据库属性
    @Test
    public void test05() {
        MilvusClientV2 client = buildClient();

        // 删除数据库属性
        client.dropDatabaseProperties(DropDatabasePropertiesReq.builder()
                .databaseName(DB_NAME)
                .propertyKeys(Collections.singletonList("database.max.collections"))
                .build());


        // 查看数据库
        DescribeDatabaseResp descDBResp = client.describeDatabase(DescribeDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build()
        );
        logger.info("descDBResp:{}", descDBResp);
    }

    // 删除数据库
    @Test
    public void test06() {
        MilvusClientV2 client = buildClient();

        // 删除数据库属性
        client.dropDatabase(DropDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build());



        // 查看数据库
        DescribeDatabaseResp descDBResp = client.describeDatabase(DescribeDatabaseReq.builder()
                .databaseName(DB_NAME)
                .build()
        );
        logger.info("descDBResp:{}", descDBResp);
    }

    // ******************** Collections ********************
    private static String COLLECTION_NAME = "collection_1";

    // 创建 Schema + 设置索引参数 + 创建 Collections
    @Test
    public void test11() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 创建 Schema
        // 3. Create a collection in customized setup mode

        // 3.1 Create schema
        CreateCollectionReq.CollectionSchema schema = client.createSchema();

        // 3.2 Add fields to schema
        schema.addField(AddFieldReq.builder()
                .fieldName("my_id")
                .dataType(DataType.Int64)
                .isPrimaryKey(true)
                .autoID(false)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("my_vector")
                .dataType(DataType.FloatVector)
                .dimension(5)
                .build());

        schema.addField(AddFieldReq.builder()
                .fieldName("my_varchar")
                .dataType(DataType.VarChar)
                .maxLength(512)
                .build());

        // 设置索引参数
        // 3.3 Prepare index parameters
        IndexParam indexParamForIdField = IndexParam.builder()
                .fieldName("my_id")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .build();

        IndexParam indexParamForVectorField = IndexParam.builder()
                .fieldName("my_vector")
                .indexType(IndexParam.IndexType.AUTOINDEX)
                .metricType(IndexParam.MetricType.COSINE)
                .build();

        List<IndexParam> indexParams = new ArrayList<>();
        indexParams.add(indexParamForIdField);
        indexParams.add(indexParamForVectorField);

        // 创建 Collections
        // 3.4 Create a collection with schema and index parameters
        CreateCollectionReq customizedSetupReq1 = CreateCollectionReq.builder()
                .collectionName(COLLECTION_NAME)
                .collectionSchema(schema)
                .indexParams(indexParams)
                .build();

        client.createCollection(customizedSetupReq1);

        // 3.5 Get load state of the collection
        GetLoadStateReq customSetupLoadStateReq1 = GetLoadStateReq.builder()
                .collectionName(COLLECTION_NAME)
                .build();

        Boolean loaded = client.getLoadState(customSetupLoadStateReq1);
        System.out.println(loaded);

    }

    // 查看 Collections
    @Test
    public void test12() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 查看 Collections
        ListCollectionsResp resp = client.listCollections();
        logger.info("ListCollectionsResp:{}", resp.getCollectionNames());
    }

    // 删除 Collections
    @Test
    public void test13() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 删除 Collections
        DropCollectionReq dropQuickSetupParam = DropCollectionReq.builder()
                .collectionName(COLLECTION_NAME)
                .build();

        client.dropCollection(dropQuickSetupParam);
    }

    // ******************** 实体：修改 ********************

    // 插入数据
    @Test
    public void test21() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 插入数据
        Gson gson = new Gson();
        List<JsonObject> data = Arrays.asList(
                gson.fromJson("""
                        {
                            "my_id": 0, 
                            "my_vector": [0.3580376395471989, -0.6023495712049978, 0.18414012509913835, -0.26286205330961354, 0.9029438446296592], 
                            "my_varchar": "pink_8682"
                        }""", JsonObject.class),
                gson.fromJson("""
                        {
                            "my_id": 1, 
                            "my_vector": [0.19886812562848388, 0.06023560599112088, 0.6976963061752597, 0.2614474506242501, 0.838729485096104], 
                            "my_varchar": "red_7025"
                        }""", JsonObject.class),
                gson.fromJson("""
                        {
                            "my_id": 2,
                            "my_vector": [0.43742130801983836, -0.5597502546264526, 0.6457887650909682, 0.7894058910881185, 0.20785793220625592],
                            "my_varchar": "orange_6781"
                        }""", JsonObject.class),
                gson.fromJson("""
                        {
                            "my_id": 3, 
                            "my_vector": [0.3172005263489739, 0.9719044792798428, -0.36981146090600725, -0.4860894583077995, 0.95791889146345], 
                            "my_varchar": "pink_9298"
                        }""", JsonObject.class),
                gson.fromJson("""
                        {
                            "my_id": 4, 
                            "my_vector": [0.4452349528804562, -0.8757026943054742, 0.8220779437047674, 0.46406290649483184, 0.30337481143159106], 
                            "my_varchar": "red_4794"
                        }""", JsonObject.class)
        );

        InsertReq insertReq = InsertReq.builder()
                .collectionName(COLLECTION_NAME)
                .data(data)
                .build();

        InsertResp insertResp = client.insert(insertReq);
        logger.info("InsertResp:{}", insertResp);
    }

    // 更新实体
    @Test
    public void test22() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 更新实体
        Gson gson = new Gson();
        List<JsonObject> data = Arrays.asList(
                gson.fromJson("""
                        {
                            "my_id": 0, 
                            "my_vector": [0.3580376395471989, -0.6023495712049978, 0.18414012509913835, -0.26286205330961354, 0.9029438446296592], 
                            "my_varchar": "pink_8682_bbb"
                        }""", JsonObject.class)

        );

        UpsertReq upsertReq = UpsertReq.builder()
                .collectionName(COLLECTION_NAME)
                .data(data)
                .build();

        UpsertResp upsertResp = client.upsert(upsertReq);
        logger.info("UpdateResp:{}", upsertResp);
    }

    // 删除实体
    @Test
    public void test24() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 删除实体
        DeleteResp deleteResp = client.delete(DeleteReq.builder()
                .collectionName(COLLECTION_NAME)
                .ids(Arrays.asList(0))
                .build());

        logger.info("DeleteResp:{}", deleteResp);
    }

    // ******************** 实体：搜索 ********************

    // 搜索：单向量搜索
    @Test
    public void test31() throws InterruptedException {
        MilvusClientV2 client = buildClient();
        client.useDatabase(DB_NAME);

        // 搜索实体
        FloatVec queryVector = new FloatVec(new float[]{0.3580376395471989f, -0.6023495712049978f, 0.18414012509913835f, -0.26286205330961354f, 0.9029438446296592f});
        SearchReq searchReq = SearchReq.builder()
                .collectionName(COLLECTION_NAME)
                .data(Collections.singletonList(queryVector))
                .annsField("my_vector")
                .topK(3)
                .build();

        SearchResp searchResp = client.search(searchReq);

        List<List<SearchResp.SearchResult>> searchResults = searchResp.getSearchResults();
        for (List<SearchResp.SearchResult> results : searchResults) {
            logger.info("TopK results:");
            for (SearchResp.SearchResult result : results) {
                logger.info("{}", result);
            }
        }
    }
```

### 8、RAG知识库代码

代码见：https://github.com/xuxueli/xxl-boot

RAG知识库管理：

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_292.png "在这里输入图片标题")

RAG知识相似度检索：

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_293.png "在这里输入图片标题")



