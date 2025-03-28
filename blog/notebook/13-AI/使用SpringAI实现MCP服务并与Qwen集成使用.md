<h2 style="color:#4db6ac !important" >使用SpringAI实现MCP服务并与Qwen集成使用</h2>
> 【原创】2025/03/29

[TOCM]

[TOC]

### 1、MCP 介绍

MCP（Model Context Protocol，模型上下文协议）是一种开放协议，旨在实现 大型语言模型（LLM） 应用与外部数据源、工具和服务之间的无缝集成，类似于网络中的 HTTP 协议或邮件中的 SMTP 协议。
MCP 协议通过标准化模型与外部资源的交互方式，提升 LLM 应用的功能性、灵活性和可扩展性。

![img](https://www.xuxueli.com/blog/static/images/img_278.png)

**MCP核心概念**

MCP 的核心是 模型上下文，即 LLM 在运行过程中所需的所有外部信息和工具。MCP 通过定义标准化的接口和协议，使 LLM 能够动态访问和集成以下内容：
- 1、外部数据源：如数据库、API、文档库等，为 LLM 提供实时或历史数据。
- 2、工具和服务：如计算工具、搜索引擎、第三方服务等，扩展 LLM 的功能。
- 3、上下文管理：动态维护 LLM 的对话上下文，确保连贯性和一致性。

**MCP 架构**

MCP 的架构由四个关键部分组成：
- 1、主机（Host）：主机是期望从服务器获取数据的人工智能应用，例如一个集成开发环境（IDE）、聊天机器人等。主机负责初始化和管理客户端、处理用户授权、管理上下文聚合等。
- 2、客户端（Client）：客户端是主机与服务器之间的桥梁。它与服务器保持一对一的连接，负责消息路由、能力管理、协议协商和订阅管理等。客户端确保主机和服务器之间的通信清晰、安全且高效。
- 3、服务器（Server）：服务器是提供外部数据和工具的组件。它通过工具、资源和提示模板为大型语言模型提供额外的上下文和功能。例如，一个服务器可以提供与Gmail、Slack等外部服务的API调用。
- 4、基础协议（Base Protocol）：基础协议定义了主机、客户端和服务器之间如何通信。它包括消息格式、生命周期管理和传输机制等。

MCP 就像 USB-C 一样，可以让不同设备能够通过相同的接口连接在一起。

![img](https://www.xuxueli.com/blog/static/images/img_279.jpeg)

**MCP 工作原理**

MCP 通过定义标准化的数据格式和通信协议，实现 LLM 与外部资源的交互。 MCP 使用 JSON-RPC 2.0 作为消息格式，通过标准的请求、响应和通知消息进行通信。
MCP 支持多种传输机制，包括本地的标准输入/输出（Stdio）和基于HTTP的服务器发送事件（SSE）。MCP的生命周期包括初始化、运行和关闭三个阶段，确保连接的建立、通信和终止都符合协议规范。

- 上下文请求：LLM 应用向外部资源发送上下文请求，包含所需的数据或服务类型。
- 上下文集成：LLM 应用将外部资源返回的上下文数据集成到模型中，用于生成响应或执行任务。
- 上下文管理：MCP 支持动态管理 LLM 的对话上下文，确保多轮对话的连贯性。

**MCP 关键特性**

- 1、标准化接口：定义统一的接口和协议，确保 LLM 与外部资源的兼容性。
- 2、动态集成：支持 LLM 动态访问和集成外部数据源和工具。
- 3、上下文感知：支持动态管理对话上下文，提升多轮对话的连贯性。
- 4、开放性和可扩展性：支持第三方开发者为 LLM 应用扩展功能和资源。

**MCP 应用场景**

MCP 广泛应用于以下场景：
- 1、增强型问答系统：通过集成外部数据源，提供实时、准确的答案。
- 2、智能助手：通过集成工具和服务，执行复杂任务（如预订、计算、搜索等）。
- 3、知识管理：通过集成文档库和数据库，提供专业领域的知识支持。
- 4、多轮对话：通过上下文管理，实现连贯的多轮对话。

**MCP 与 Function Calling 的区别**

这两种技术都旨在增强 AI 模型与外部数据的交互能力，但 MCP 不止可以增强 AI 模型，还可以是其他的应用系统。
1、MCP（Model Context Protocol），模型上下文协议
2、Function Calling，函数调用


### 2、SpringAI MCP 介绍

Spring AI MCP，它是模型上下文协议（Model Context Protocol，MCP）的 Java SDK 实现。Spring AI 生态系统的这一新成员为 Java 平台带来了标准化的 AI 模型集成能力。


**总体结构**

MCP 的核心是客户端-服务器（CS）架构，一个应用可以连接多个服务器。
![img](https://www.xuxueli.com/blog/static/images/img_277.png)

Spring AI MCP 采用模块化架构，包含以下组件：
- Spring AI 应用： 使用 Spring AI 框架构建希望通过 MCP 访问数据的生成式 AI 应用。
- Spring MCP 客户端：与服务器保持 1:1 连接的 MCP 协议的 Spring AI 实现。
- Spring MCP 服务器：轻量级程序，每个程序都通过标准化的模型上下文协议公开特定功能。
- 本地数据源：MCP 服务器可安全访问的计算机文件、数据库和服务。
- 远程服务：MCP 服务器可通过互联网（如 API）连接的外部系统。

该架构支持广泛的用例，从简单的文件系统访问到复杂的多模型人工智能与数据库和互联网连接的交互。

### 3、使用 SpringAI 开发 MCP 服务

第一步：引入 spring-ai 依赖
```
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.ai</groupId>
            <artifactId>spring-ai-bom</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

第二步：开发 MCP Tool，并注册暴露 MCP Server
```
// 开发 MCP Tool
@Service
public class WeatherService {

    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherByCity(@ToolParam(description = "城市名称") String city) {
        if (Objects.isNull(city)) {
            return "抱歉：城市名称不能为空！";
        }

        // 模拟天气数据
        Map<String, String> mockData = Map.of(
                "西安", "晴天",
                "北京", "小雨",
                "上海", "大雨"
        );
        return mockData.getOrDefault(city, "抱歉：未查询到对应城市！");
    }

}

// 注册暴露 MCP Server
@Bean
public ToolCallbackProvider weatherTools(WeatherService weatherService) {
    return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
}
```

第三步：自测验证（SSE方式）

- 启动 MCP 服务（SSE方式需要手动启动）
``` 
public static void main(String[] args) {
    SpringApplication.run(ServerApplication.class, args);
}
```
- 发起 SSE 请求
```
// 初始化 SSE Client
var client = McpClient.sync(new HttpClientSseClientTransport("http://localhost:8080")).build(

// 发起请求
CallToolResult getWeatherByCity = client.callTool(new CallToolRequest("getWeatherByCity", Map.of("city", "上海")));
System.out.println("上海天气是：" + getWeatherByCity);
```

第四步：自测验证（STDIO方式）
```
// 初始化 STDIO Client
var stdioParams = ServerParameters.builder("java")
        .args("-Dspring.ai.mcp.server.stdio=true", "-Dspring.main.web-application-type=none",
                "-Dlogging.pattern.console=", "-jar",
                "/Users/admin/program/git-space/project/abc-demo/spring-ai-mcp/mcp-server/target/mcp-server-1.0.0-SNAPSHOT.jar")
        .build();
var client = McpClient.sync(new StdioClientTransport(stdioParams)).build(

// 发起请求
CallToolResult getWeatherByCity = client.callTool(new CallToolRequest("getWeatherByCity", Map.of("city", "上海")));
System.out.println("上海天气是：" + getWeatherByCity);
```

第四步：查看日志输出
```
// MCP服务 Tools列表：
Available Tools = ListToolsResult[tools=[Tool[name=getWeatherByCity, description=根据城市名称获取天气预报, inputSchema=JsonSchema[type=object, properties={arg0={type=string, description=城市名称}}, required=[arg0], additionalProperties=false]]], nextCursor=null]
// MCP服务 天气查询结果：
上海 天气为：CallToolResult[content=[TextContent[audience=null, priority=null, text="小雨"]], isError=false]
```

### 4、Qwen集成 MCP 服务

当前，大型语言模型（LLM）并非普遍支持模型上下文协议（MCP），本文选择已支持MCP的 Qwen2.5 模型基座，使用 Cherry Studio 作为模型工具。
另外，由于上文示例过于简单，为确保演示效果从 glama.ai（已收集1K+MCP服务）选择2个开源 MCP服务进行集成演示。
- 1、mcp-trends-hub：该MCP服务，针对全网热点趋势一站式聚合，并提供检索查询服务。
- 2、mcp-npx-fetch：该MCP服务，支持获取各种格式的web内容，包括HTML、JSON、纯文本和Markdown等。

第一步：添加 MCP 服务配置
```
{
  "mcpServers": {
    "trends-hub": {
      "isActive": true,
      "description": "针对全网热点趋势一站式聚合，并提供检索查询服务",
      "command": "npx",
      "args": [
        "-y",
        "mcp-trends-hub@1.6.0"
      ]
    },
    "fetch": {
      "isActive": true,
      "description": "支持获取各种格式的web内容，包括HTML、JSON、纯文本和Markdown等。",
      "command": "npx",
      "args": [
        "-y",
        "@tokenizin/mcp-npx-fetch"
      ]
    }
  }
}
```

第二步：配置 Qwen2.5 模型

![img](https://www.xuxueli.com/blog/static/images/img_279.png)

第三步：开启MCP并使用

![img](https://www.xuxueli.com/blog/static/images/img_280.png)

![img](https://www.xuxueli.com/blog/static/images/img_281.png)


### 5、总结思考
随着MCP和推理模型的不断成熟，大模型应用架构逐渐在从 “工作流+技能” 向 “推理LLM+MCP”演进，为AI应用带来更大想象空间与可能性。

维度     | 	工作流+技能   |	推理LLM+MCP
--------|-----------|-------
决策机制	| 规则驱动	     |  自主推理‌
扩展性	| 有限的内置技能库	 |  动态的工具注册‌
交互方式 | 单向指令执行    |	双向数据流（主机↔服务器）‌
典型系统	| Dify、ragflow	    |  Claude Desktop、Cherry Studio、OpenAI Agents SDK



参考：     
1、https://spring.io/blog/2024/12/11/spring-ai-mcp-announcement          
2、https://www.runoob.com/np/mcp-protocol.html       
3、https://mcp-docs.cn/quickstart        
4、https://github.com/spring-projects/spring-ai-examples/tree/main/model-context-protocol        
5、https://springdoc.cn/spring-ai-mcp-announcement/      


