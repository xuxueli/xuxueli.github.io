<h2 style="color:#4db6ac !important" >AI 通用知识手册</h2>
> 【原创】2026/09/06（持续更新中）

本文为「AI 通用知识」长期维护的知识手册，聚焦通用性、稳定性的基础规范与协议类知识，包括 OpenAI API 协议、MCP 格式规范、SKILL 格式规范、RAG 知识库、Spring AI 等。
随技术演进持续更新，用于后续系列文章的统一引用与兜底。

[TOCM]

[TOC]

---

### 一、基础概念

| 术语 | 说明 |
|------|------|
| LLM | 大型语言模型（Large Language Model），基于海量文本训练的生成式模型 |
| Token | 模型处理文本的最小单位，中文约 1 字 ≈ 1~2 Token，API 按 Token 计费 |
| 上下文窗口 | Context Window，模型单次可处理的 Token 上限（输入+输出） |
| Temperature | 采样温度，控制输出随机性，0 偏确定，越高越发散 |
| Top-p | 核采样，仅从累计概率达 p 的候选中采样，与 Temperature 二选一调优 |
| Embedding | 将文本映射为高维向量，语义相近则向量距离相近，是知识库/检索的基础 |
| 向量数据库 | 存储与检索向量的数据库，如 Milvus、Qdrant、pgvector、Chroma |
| Function Calling | 让模型按声明输出结构化函数调用参数，由外部系统执行后再回填结果 |
| Structured Output | 结构化输出，约束模型输出严格符合 JSON Schema |
| RAG | 检索增强生成，先检索外部知识再生成，缓解幻觉、支撑私域知识 |
| Agent | 具备「感知-规划-行动-反思」循环、可自主调用工具的智能体 |
| MCP | 模型上下文协议，LLM 应用与外部数据/工具间统一交互的开放协议 |
| Skill | 轻量可复用的「能力包」，通常以 Markdown 封装任务说明供 AI 按需加载 |

### 二、OpenAI API 协议

#### 1、协议概览

OpenAI API 采用 RESTful + JSON 风格，通过 HTTP 请求完成模型调用，当前核心为两类：

- Chat Completions API：`/v1/chat/completions`，通用对话与工具调用
- Responses API：`/v1/responses`，新一代统一接口，规划逐步替代前者

由于协议事实标准地位，DeepSeek、Qwen（DashScope 兼容模式）、Ollama、vLLM 等大量开源模型与本地推理框架均提供「OpenAI 兼容」端点，客户端代码基本可无缝切换（仅需更换 Base URL 与模型名）。

#### 2、请求与消息结构

**请求核心字段**：

```json
{
  "model": "gpt-4o",
  "messages": [
    { "role": "system", "content": "你是一个得力的助手" },
    { "role": "user", "content": "西安今天的天气怎么样？" }
  ],
  "temperature": 0.7,
  "stream": false
}
```

**role（消息角色）**：

| 角色 | 含义 |
|------|------|
| system | 系统指令，设定人设、约束与输出规范 |
| user | 用户输入 |
| assistant | 模型历史输出；工具调用时承载 tool_calls，或承载 tool 执行结果回填 |
| tool | 工具（函数）执行结果，需通过 tool_call_id 关联对应调用 |

#### 3、流式（Streaming / SSE）

开启 `stream: true` 后，服务端以 Server-Sent Events 逐片返回增量，每个 chunk 为一行 `data: {...}`，结束后发送 `data: [DONE]`：

```
data: {"id":"chatcmpl-xxx","choices":[{"delta":{"content":"西"},"finish_reason":null}]}

data: {"id":"chatcmpl-xxx","choices":[{"delta":{"content":"安"},"finish_reason":null}]}

data: {"id":"chatcmpl-xxx","choices":[{"delta":{},"finish_reason":"stop"}]}

data: [DONE]
```

**非流式返回核心字段**：`choices[].message.content`（输出文本）、`choices[].message.tool_calls`（工具调用）、`choices[].finish_reason`（stop / length / tool_calls 等）、`usage`（prompt_tokens / completion_tokens / total_tokens）。

#### 4、Function Calling（工具调用）

通过声明 `tools` 让模型自主决定调用，两轮配合完成：

**第一轮：声明工具并请求**，模型返回结构化 `tool_calls` 而非直接答案：

```json
{
  "model": "gpt-4o",
  "messages": [{ "role": "user", "content": "北京天气如何？" }],
  "tools": [{
    "type": "function",
    "function": {
      "name": "get_weather",
      "description": "根据城市名称获取天气预报",
      "parameters": {
        "type": "object",
        "properties": { "city": { "type": "string", "description": "城市名称" } },
        "required": ["city"]
      }
    }
  }]
}
```

返回示例（示意）：

```json
{
  "choices": [{
    "message": {
      "role": "assistant",
      "content": null,
      "tool_calls": [{
        "id": "call_abc123",
        "type": "function",
        "function": { "name": "get_weather", "arguments": "{\"city\":\"北京\"}" }
      }]
    }
  }]
}
```

**第二轮：回填执行结果**，将结果以 `role: "tool"` 且带 `tool_call_id` 的消息追加，模型据此生成最终答案：

```json
{
  "messages": [
    { "role": "user", "content": "北京天气如何？" },
    {
      "role": "assistant",
      "content": null,
      "tool_calls": [{ "id": "call_abc123", "type": "function", "function": { "name": "get_weather", "arguments": "{\"city\":\"北京\"}" } }]
    },
    { "role": "tool", "tool_call_id": "call_abc123", "content": "小雨，18℃" }
  ]
}
```

#### 5、结构化输出

通过 `response_format` 约束输出严格符合 JSON Schema：

```json
{
  "model": "gpt-4o",
  "messages": [{ "role": "user", "content": "将下面内容总结为卡片：..." }],
  "response_format": {
    "type": "json_schema",
    "json_schema": {
      "name": "summary_card",
      "strict": true,
      "schema": {
        "type": "object",
        "properties": {
          "title": { "type": "string" },
          "points": { "type": "array", "items": { "type": "string" } }
        },
        "required": ["title", "points"],
        "additionalProperties": false
      }
    }
  }
}
```

#### 6、Embedding 与其余能力

- Embedding：`POST /v1/embeddings`，入参 `input` + `model`，返回向量数组，用于语义检索与聚类
- Images：文生图（images/generations）、图像理解（多模态 input）
- TTS / STT：语音合成与转写
- Realtime API：基于 WebSocket 的低延迟语音对话

#### 7、最小调用示例（兼容端点通用）

```bash
curl http://localhost:11434/v1/chat/completions \
  -H "Content-Type: application/json" \
  -d '{
    "model": "qwen2.5",
    "messages": [{ "role": "user", "content": "你好" }]
  }'
```

```python
from openai import OpenAI

client = OpenAI(base_url="https://api.openai.com/v1", api_key="sk-xxx")
resp = client.chat.completions.create(
    model="gpt-4o",
    messages=[{"role": "user", "content": "讲个冷笑话"}],
)
print(resp.choices[0].message.content)
```

> 切换 DeepSeek / Qwen / Ollama / vLLM 等兼容服务时，仅需替换 `base_url`（如 `https://api.deepseek.com`、`http://localhost:11434/v1`）与 `model` 即可，SDK 无需改动。

### 三、MCP 格式规范（Model Context Protocol）

#### 1、定位与架构

MCP（Model Context Protocol）是一种开放协议，类似 AI 领域的「USB-C」，统一 LLM 应用与外部数据源、工具、服务的接入方式，与具体模型解耦。核心组成：

- **Host（主机）**：期望从服务器获取能力的 AI 应用（IDE、聊天客户端等）
- **Client（客户端）**：与服务器一一对应的连接代理，负责协商与消息路由
- **Server（服务器）**：以工具、资源、提示模板方式暴露能力的轻量程序
- **基础协议**：定义消息格式、生命周期与传输机制

#### 2、消息格式与生命周期

MCP 消息基于 JSON-RPC 2.0：请求（request / response）、通知（notification）。协议握手需按顺序完成 initialize → 通知已初始化（notifications/initialized）→ 运行期能力调用 → 关闭。

典型运行期方法：

- `tools/list`：获取服务器暴露的工具清单及输入 JSON Schema
- `tools/call`：调用指定工具
- `resources/list` / `resources/read`：暴露并读取数据资源
- `prompts/list` / `prompts/get`：暴露可复用的提示模板

#### 3、传输方式

| 传输 | 场景 | 说明 |
|------|------|------|
| stdio | 本地进程 | 通过标准输入/输出与子进程通信，启动成本低 |
| SSE | HTTP（旧） | 基于 Server-Sent Events 的单向推送 + POST 请求 |
| Streamable HTTP | HTTP（新） | 推荐的标准 HTTP 传输，支持双向流式 |

#### 4、客户端配置格式

主流客户端（Cherry Studio、Claude Desktop、Cline、opencode 等）使用如下 JSON 声明 MCP 服务：

```json
{
  "mcpServers": {
    "weather": {
      "command": "npx",
      "args": ["-y", "@some/mcp-weather"],
      "env": { "API_KEY": "xxx" }
    }
  }
}
```

远程 HTTP 服务亦可使用 URL 方式：

```json
{
  "mcpServers": {
    "remote": { "url": "https://mcp.example.com/mcp" }
  }
}
```

> 详细实践（使用 Spring AI 开发 MCP Server 并与 Qwen 集成）见本站《[使用SpringAI实现MCP服务并与Qwen集成使用](?blog=ai/springai-mcp-qwen )》。

### 四、SKILL 格式规范

#### 1、定位与结构

Skill 是一种轻量、可复用的「能力包」，以 Markdown 封装特定任务的说明、步骤、示例与注意事项，供 AI 按需发现与加载。常见目录约定为：技能市场/项目内 `.agents/skills/<skill-name>/`，以 `SKILL.md` 作为主入口。

```
.agents/skills/
└── <skill-name>/
    ├── SKILL.md      # 主说明，含 frontmatter 元数据 + 使用指引
    └── ...           # 可选：references/ 参考资料、scripts/ 脚本、示例等
```

#### 2、Frontmatter 元数据（示例）

```markdown
---
name: weather-query
description: 根据城市名称查询实时天气。当用户询问天气时使用。
version: 1.0.0
metadata:
  author: xxl
  license: MIT
---
```

字段要求：`name` 唯一且具描述性；`description` 精炼说明功能与触发场景（AI 依赖其做技能匹配，应包含触发关键词与边界）；`version` 等按需补充。

#### 3、生态变体

| 体系 | 说明 |
|------|------|
| Anthropic Claude Skills | Agent Skills 规范，`SKILL.md` + frontmatter，Claude 系与 Claude Code 使用 |
| opencode / Codex agents skills | 编程智能体技能，置于项目 `.agents/skills/`，可结合 Spec/SDD 承载工程规范 |
| npx-skills / ClawHub | npx 技能分发与市场，支持一键安装、跨 Agent 复用 |
| opencode OMO 插件 | 多 Agent 编排增强（Oh My OpenAgent） |

> 实操见本站《[扩展 AI 能力：npx skills 实战手册](?blog=ai/npx-skills )》《[开源AI Agent：OpenCode集成OMO原理及实践](?blog=ai/opencode-omo )》。

#### 4、编写最佳实践

- ✅ 聚焦单一目的：一个 Skill 只做一件事
- ✅ description 写明触发条件与限制，便于自动发现
- ✅ 步骤最小可执行、示例输入输出明确
- ✅ 用 frontmatter 管理版本与兼容 Agent，便于演进回溯

### 五、知识库（RAG）

#### 1、RAG 链路

RAG（检索增强生成）是私域/实时知识问答的通用范式，核心链路：

1. **文档解析**：PDF、Word、网页等转纯文本，清洗无效内容
2. **分块（Chunking）**：按段落/语义/固定窗口切分，兼顾块内语义完整与块间重叠
3. **向量化（Embedding）**：将分块文本转为向量并入库
4. **检索（Retrieval）**：查询向量化后相似度召回 Top-K
5. **增强注入**：将召回片段拼入 Prompt，交由 LLM 生成并标注引用来源

#### 2、检索优化

- **混合检索（Hybrid）**：向量召回 + 关键词（BM25）召回并融合，兼顾语义与精确词匹配
- **重排（Rerank）**：对召回候选做精排，提升 Top-K 命中质量
- **图增强（GraphRAG）**：构建实体关系图谱支持多跳推理与全局性问题
- **引文溯源**：输出携带来源片段，便于核对与信任

#### 3、向量数据库选型

| 方案 | 特点 |
|------|------|
| Milvus | 分布式、规模大，适合生产级 |
| Qdrant / Weaviate | 功能完整、易于托管 |
| pgvector | 基于 PostgreSQL，与业务库同栈 |
| Chroma / FAISS | 轻量、适合原型与本地 |

> 实操见本站《[使用Milvus搭配Ollama搭建RAG知识库](?blog=ai/milvus-ollama-rag )》。

### 六、Spring AI

#### 1、生态定位

Spring AI 是 Spring 官方 AI 应用框架，为 Java 生态提供统一抽象：支持多模型商接入、结构化输出、函数调用、RAG（Embedding + Vector Store）与 MCP（官方 Java SDK 实现），可将 AI 能力以 Bean 化、声明式方式集成进 Spring 应用。

#### 2、Maven 依赖

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
```

#### 3、最小调用示例

```java
@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String msg) {
        return chatClient.prompt(msg).call().content();
    }
}
```

```yaml
spring:
  ai:
    openai:
      base-url: https://api.openai.com
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: gpt-4o
```

#### 4、Tool 函数调用与 MCP

- `@Tool` / `@ToolParam` 注解声明工具方法，`ToolCallbackProvider` 注册
- `spring-ai-starter-mcp-server-*` 快速构建 MCP Server（stdio / WebMvc）
- `McpClient` 同步/异步接入远端 MCP Server，作为 ChatClient Tool 使用

```java
@Service
public class WeatherService {

    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherByCity(@ToolParam(description = "城市名称") String city) {
        return Map.of("西安", "晴天", "北京", "小雨")
                .getOrDefault(city, "未查询到对应城市");
    }
}

@Bean
ToolCallbackProvider weatherTools(WeatherService weatherService) {
    return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
}
```

> 详细落地见本站《[使用SpringAI实现MCP服务并与Qwen集成使用](?blog=ai/springai-mcp-qwen )》。

### 七、附录

#### 1、更新日志

| 日期 | 内容 |
|------|------|
| 2026-09-06 | 初版：基础概念、OpenAI API 协议、MCP 规范、SKILL 规范、RAG、Spring AI |

#### 2、站内相关文章

- [使用SpringAI实现MCP服务并与Qwen集成使用](?blog=ai/springai-mcp-qwen )
- [扩展 AI 能力：npx skills 实战手册](?blog=ai/npx-skills )
- [使用Milvus搭配Ollama搭建RAG知识库](?blog=ai/milvus-ollama-rag )
- [AI规范编程：从SDD理念到Spec-Kit落地实践](?blog=ai/speckit )
- [开源AI Agent：OpenCode集成OMO原理及实践](?blog=ai/opencode-omo )
- [AI范式越迁：使用 XXL-BOOT SKILL 实现一句话直生业务](?blog=ai/aicoding-xxl-boot )

#### 3、官方参考

1、https://platform.openai.com/docs/api-reference
2、https://modelcontextprotocol.io
3、https://docs.claude.com/en/docs/agents-and-tools/agent-skills
4、https://docs.spring.io/spring-ai/reference
5、https://github.com/spring-projects/spring-ai-examples
