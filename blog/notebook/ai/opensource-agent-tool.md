<h2 style="color:#4db6ac !important" >开源 Agent 工具：OpenCode、Pi 与 DSH</h2>
> 【原创】2026/10/02

[TOCM]

[TOC]

## 序言

> 截至 2026M10，AI 编程 Agent 已经走过了“对话补全”阶段，进入“自主执行”阶段：它不再只是给你一段代码，而是能读取项目、编辑文件、运行命令、执行多步任务。与此同时，闭源工具的厂商锁定问题也越来越突出——模型、协议、扩展方式都被绑定在单一厂商的生态里。
>
> 开源阵营给出了三种不同答案：**OpenCode** 走“开放不绑定模型”的通用编程 Agent 路线；**Pi** 走“极简内核 + 可扩展”的终端 Agent 路线；**DSH（DeepSeek Harness）** 走“一切皆插件”的 Agent 框架路线。三者的共同点是：代码开源、模型可换、能力可扩展；区别在于它们对“Agent 到底是什么”的回答。
>
> 本文先整体介绍三款工具，再逐章从**定位、原理、架构、扩展机制到安装使用**做多维度拆解，最后给出选型建议。

## 第一章：开源 Agent 工具全景

### 1.1 从“模型”到“Harness”

要理解这些工具，先要分清三个概念：

- **Model（模型）**：负责推理与决策，决定“下一步该做什么”。
- **Tool（工具）**：程序提供给模型的能力封装，比如读文件、执行命令、联网检索。
- **Harness（运行时/框架）**：把模型、工具、上下文、会话、权限连接起来的执行引擎，决定“这件事怎么落地”。

一句话概括三者的关系：**Agent = Model + Harness**。模型负责“想”，Harness 负责“做”。同一个模型换一个 Harness，执行质量、成本、可控性可能天差地别。

一个最小 Agent 的核心其实只有一个循环：

```text
用户问题
  ↓
模型：返回文本，或提出一次工具调用
  ↓
程序：校验参数 → 执行工具 → 把结果放回上下文
  ↓
模型：基于新上下文继续判断，直到给出最终答案
```

真正的工程难点不在这个循环本身，而在循环之外的细节：流式输出、多轮引导、上下文压缩、工具权限、项目信任、会话分支、扩展机制。这三款工具的差异，本质上是对这些细节的不同取舍。

### 1.2 三款工具速览

| 维度 | **OpenCode** | **Pi** | **DSH（DeepSeek Harness）** |
|:---|:---|:---|:---|
| **定位** | 开源、不绑定模型的通用编程 Agent | 可扩展、极简内核的终端 Agent | “一切皆插件”的通用 Agent 框架 |
| **出品方** | 开源社区 / anomalyco | Earendil Inc. | DeepSeek |
| **许可** | MIT 开源 | 开源 | MIT 开源（开发者预览） |
| **技术栈** | TypeScript（共享后台服务 + Client/Server） | TypeScript（Monorepo 四层） | TypeScript（Cordis 插件内核） |
| **模型策略** | 任意 Provider，自由切换，支持模型变体 | Provider 可替换，支持本地模型 | DeepSeek + 任意 OpenAI 兼容端点 |
| **架构核心** | 单例后台服务 + Client/Server | Agent Loop + streamFn 注入 | 一切皆插件（含 UI 与 Agent 循环） |
| **扩展方式** | V2 插件 + MCP + Skill + Agent | 扩展 + Skill + MCP + SDK | npm 插件（`dsh plugin add`） |
| **交互形态** | TUI / Mini / Web / 桌面 / ACP | TUI / print / JSON / RPC / SDK | Web UI / TUI / Headless / 桌面 / Python SDK |
| **安全模型** | 有序 permissions + policies | 项目信任（Project Trust） | 插件沙箱 + 工作区边界 |
| **成熟度** | V2 迭代中、生态成熟 | 稳定迭代、内核克制 | 开发者预览、迭代最快 |

### 1.3 三条技术路线

三款工具对“如何把模型接入真实世界”给出了三种不同侧重：

- **OpenCode —— 把 Harness 做成服务**：共享后台服务 + 多端客户端，能力只实现一次、处处可用，胜在“好用”。
- **Pi —— 把 Harness 做成内核**：四层包解耦、`streamFn` 注入，追求最小内核与最清晰边界，胜在“好读好嵌”。
- **DSH —— 把 Harness 做成插件系统**：连 UI 与 Agent 循环都可替换，追求极致可改造，胜在“好改”。

> 三者的完整对比与最终选型见第五章；下面三章逐一展开各自的定位、原理、架构与用法。

---

## 第二章：OpenCode —— 开放不绑定的编程 Agent

> 本章以 **OpenCode V2** 为准。V2 与 V1 有两处不兼容改动（插件 API、Server API），安装命令与配置形态也已升级，下文均使用 V2 写法。

### 2.1 工具简介

OpenCode 是一个完全开源的 AI 编程 Agent，提供终端 TUI、桌面应用与 Web 应用三种形态。它不是“生成代码给你复制”的对话框，而是真正能操作开发环境的任务执行者：你描述需求，它主动扫描项目结构、理解现有代码，然后完成文件读写、命令执行、Git 操作，最终交付可运行的结果。

- 官网：https://opencode.ai
- V2 文档：https://opencode.ai/v2/docs/
- GitHub：https://github.com/anomalyco/opencode

它区别于闭源编程 Agent 的关键在于三个设计抉择：

- **代码开源可控**：采用 MIT 许可证，核心逻辑对社区透明，可审计、可修改、可内网部署。
- **LLM 开放不绑定**：支持任意主流 Provider 与自定义端点，模型可用 `provider/model#variant` 形式选择变体，避免厂商锁定。
- **可插拔分层架构**：共享后台服务 + Client/Server 分层，V2 插件可注册工具、Hook、Provider、命令、Agent 等，扩展能力很强。

### 2.2 设计原理：一个共享后台服务

OpenCode V2 最底层的取舍，是**把“Agent 运行时”做成一个进程外的后台服务**：

- 默认情况下，OpenCode 会为当前用户**发现或启动一个共享后台服务**；每个本地 OpenCode 客户端都连接到它。
- 这个服务**独占**会话、配置、集成、权限与工具执行——也就是说，Agent 循环并不跑在 TUI 进程里。
- TUI、桌面端、Web 端、ACP 都只是连接到同一服务的不同客户端。

```
┌────────────────────────────────────────────────┐
│                  Client Layer                  │
│    TUI  |  Desktop  |  Web  |  ACP  |  SDK     │
└───────────────────────┬────────────────────────┘
                        │ HTTP / WebSocket
┌───────────────────────┴────────────────────────┐
│           OpenCode Background Service          │
│    Sessions | Config | Plugins | Permissions   │
│    Tools | Providers | MCP | Skills | VCS      │
└────────────────────────────────────────────────┘
```

这个设计的价值在于：**能力只实现一次，所有端共享**。同时也带来一个副作用——排查问题时要先判断是客户端、共享服务还是某个项目的问题；日志在 `~/.local/share/opencode/log/opencode.log`，可分别过滤 `role=cli` 与 `role=server`。

### 2.3 核心能力

OpenCode V2 的配置项几乎覆盖了 Agent 运行的每个环节：

| 能力 | 说明 |
|:---|:---|
| **工具** | `read`/`edit`/`write`/`patch`、`shell`、`glob`/`grep`、`webfetch`/`websearch`、`skill`、`subagent`、`question` 等 |
| **Agents** | 内置 Build/Plan/General/Explore，支持自定义 primary/subagent/all |
| **Providers** | 任意 Provider 与自定义端点，支持模型变体与原生/摘要两种压缩 |
| **MCP** | `mcp.servers` 配置本地/远程 MCP，远程默认 OAuth |
| **Skills** | `.opencode/skills/` 自动发现，兼容 `.claude/skills`、`.agents/skills` |
| **Commands** | `commands` 定义可复用斜杠命令模板 |
| **References** | 把本地目录或 Git 仓库作为命名上下文 |
| **Permissions / Policies** | 有序权限规则 + 硬拒绝策略 |
| **Compaction / Snapshots / Warming** | 上下文压缩、文件快照（撤销/回退）、会话保温 |

### 2.4 Agent 与权限模型

V2 内置四个可见 Agent：

- **Build**（primary）：默认编码 Agent，工具默认允许；读取敏感 `.env` 和访问工作区外会请求批准。
- **Plan**（primary）：探索与规划，不编辑普通项目文件（可按需写 OpenCode 计划文件）。
- **General**（subagent）：研究与多步任务，工具权限较宽，但不能继续派生 subagent。
- **Explore**（subagent）：只读搜索代码或网页，不编辑文件。

此外还有隐藏的 `compaction`、`title`、`summary` 维护型 Agent。自定义 Agent 放在 `.opencode/agents/<name>.md`，用 frontmatter 声明 `mode`、`model`、`permissions` 等：

```md
---
description: Reviews changes for correctness and regressions
mode: subagent
model: anthropic/claude-sonnet-4-5#high
permissions:
  - action: edit
    resource: "*"
    effect: deny
  - action: shell
    resource: "*"
    effect: deny
---

Review the current changes. List findings in severity order with file and line references.
```

**权限**在 V2 里是一组**有序规则**，字段为 `action`、`resource`、`effect`（`allow` / `ask` / `deny`），**最后一条匹配的规则生效**：

```jsonc
{
  "permissions": [
    { "action": "shell", "resource": "*", "effect": "ask" },
    { "action": "shell", "resource": "git status *", "effect": "allow" },
    { "action": "shell", "resource": "git push *", "effect": "deny" }
  ]
}
```

注意 V2 的 action 命名变化：`bash` → `shell`，`task` → `subagent`，`write`/`patch` → `edit`。没有任何规则匹配时默认 `ask`。用户选择“总是允许”会保存为项目级的 `allow` 规则，但永远不会覆盖已配置的 `deny`。`experimental.policies` 还能在权限检查之后做**硬拒绝**，用于组织级管控。

### 2.5 扩展体系（V2 重点）

V2 的插件 API 是全新设计，V1 插件实现**不能直接在 V2 运行**。一个 V2 插件就是导出一个 `Plugin.define` 的 TypeScript 模块，`setup(ctx)` 在加载时执行，并可返回清理函数：

```ts
import { Plugin } from "@opencode/plugin"

export default Plugin.define({
  id: "example",
  async setup(ctx) {
    await ctx.storage.set("loaded", true)
  },
})
```

`ctx` 本质上是一个 OpenCode 服务端客户端，能力包括：

- **transforms**：注册/改写 `provider`、`model`、`agent`、`mcp`、`tool`、`skill`、`command`、`reference`、`vcs`、`worktree`、`websearch` 等领域状态。
- **hooks**：拦截会话的 prompt 准入、模型请求（`context` / `compaction` / `generate` / `title`）、原生 HTTP 请求响应、重试策略等。
- **storage / events**：持久化插件数据、订阅服务端事件流。
- **注册工具**：用 `ctx.tool.transform` 注册带 JSON Schema 的自定义工具。

插件配置与加载：

```jsonc
{
  "$schema": "https://opencode.ai/config.json",
  "plugins": [
    "opencode-acme-plugin@1.2.0",
    "@acme/opencode-plugin",
    "./plugins/local",
    { "package": "@acme/opencode-plugin", "options": { "strict": true } }
  ]
}
```

- `.opencode/plugins/` 下的本地插件会被自动发现；全局插件在 `~/.config/opencode/plugins/`。
- CLI 管理：`opencode plugin add / list / check / update / remove`。
- 仅终端 UI 的插件写在 `cli.json` 的 `plugins` 里，连接到远程服务时仍然生效。
- 两个内置插件 `opencode.config.policy` 与 `opencode.provider.opencode` 故意不可被移除，以保证策略强制与组织策略下发。

**MCP** 统一在 `mcp.servers` 下配置，推荐用 CLI 添加（远程默认 OAuth）：

```bash
opencode mcp add playwright --global --url https://example.com/mcp
opencode mcp list
```

**Skills** 放在 `.opencode/skills/<skill-id>/SKILL.md`，OpenCode 自动发现并兼容 `.claude/skills`、`.agents/skills`；也可用 `skills` 数组追加本地目录或 HTTP catalog。Skill 的 ID 由路径决定（大小写敏感），frontmatter 的 `name` 只是显示名。

### 2.6 安装与模型配置

OpenCode V2 提供多种安装方式（Windows 包管理器暂不支持，请下载 Windows 独立二进制包）：

```bash
# Homebrew（注意是 opencode-v2）
brew install anomalyco/tap/opencode-v2

# npm
npm install -g @opencode/cli

# 一键脚本（macOS / Linux）
curl -fsSL https://opencode.ai/v2/install | bash

# 升级
opencode update
```

配置使用 JSON 或 JSONC，建议带上 `$schema`。全局配置在 `~/.config/opencode/opencode.json(c)`，项目配置在 `opencode.json(c)` 或 `.opencode/opencode.json(c)`；OpenCode 从当前目录向上搜索到根目录并按“远到近、`.opencode` 覆盖直接配置”的顺序合并。**终端偏好（主题、快捷键）单独放在 `~/.config/opencode/cli.json`**，不再和项目配置混在一起。

```
# jsonc title="opencode.jsonc"
{
  "$schema": "https://opencode.ai/config.json",
  "model": "anthropic/claude-sonnet-4-5",
  "default_agent": "build"
}
```

模型接入可在 TUI 里用 `/connect` 直接连接 Provider；也可以走 **OpenCode Console**，或 **OpenCode Go**（10 美元/月的开源模型订阅）。模型以 `provider/model` 引用，带 `#variant` 可选变体。

### 2.7 使用实践

```bash
# 打开全屏 TUI（默认）
opencode

# 在指定项目目录启动
opencode ~/code/project

# 极简交互界面
opencode mini

# 非交互一次性执行，适合脚本 / CI
opencode run "Explain this repository"

# 打印配对链接，用浏览器访问 Web 界面
opencode pair
```

启动后在终端描述需求即可。例如：

```text
设计一个企业网站，体现科技感与动态效果；网站内容维护在 Markdown 中，动态加载更新。
```

OpenCode 会自行扫描项目、规划任务、派生 subagent 执行，最后可直接运行项目验证。它对自动化也很友好：`opencode api get /api/info` 可直接调用服务端 HTTP API（OpenAPI 文档位于运行中服务的 `/openapi.json`），`@opencode/client` 与 SDK 则用于把 OpenCode 嵌进应用。

> 迁移提示：V1 的配置、Agent、命令、Skill 等文件在 V2 下**基本兼容**，V2 会在内存中归一化；但 **V1 插件不兼容**，且 **Server API 有破坏性变更**。升级后可让 OpenCode 自行执行“迁移到原生 V2 格式”。社区多 Agent 编排插件（如 OMO）需使用其适配 V2 的版本。

---

## 第三章：Pi —— 可扩展的极简终端 Agent

### 3.1 工具简介

Pi 是一个运行在终端里的可扩展 AI Agent：给它一个目标和一个工作目录，它就能查看文件、运行命令、编辑内容，完成多步骤任务。可以用于软件开发、研究笔记、写作项目、数据处理等。

- 官网：https://pi.dev
- 中文文档：https://pi-doc.com
- npm 包：`@earendil-works/pi-coding-agent`

Pi 的定位不是“功能最多”，而是“内核最克制、边界最清晰”。它把产品拆成四个各司其职的包，任何一个环节都可以替换。

### 3.2 设计原理：从一个最小循环说起

Pi 的源码深入文档用一个“查天气”的例子讲清了 Agent 的本质。普通聊天只有一条直线：

```text
用户问题 → LLM → 文本回答
```

Agent 的变化不是“模型突然会做事”，而是程序给了模型一组可调用的工具。工具包含三部分：名字、参数规则、真正执行的函数。**模型不能直接执行工具，它只能提出结构化的工具请求；程序检查请求后才真正执行。**

于是最小 Agent 只需要一个循环：

```typescript
async function runAgent(question: string, tools: Tool[]) {
  const messages = [{ role: 'user', content: question }];

  while (true) {
    const response = await llm.chat(messages, {
      tools: tools.map(({ name, description }) => ({ name, description })),
    });

    if (response.type === 'text') return response.text;

    const tool = tools.find((item) => item.name === response.toolCall.name);
    if (!tool) throw new Error(`未知工具：${response.toolCall.name}`);

    const result = await tool.execute(response.toolCall.arguments);
    messages.push({ role: 'assistant', content: response.toolCall });
    messages.push({ role: 'tool', name: tool.name, content: result });
  }
}
```

Pi 的重要取舍是：**Agent Loop 不直接知道 API Key，也不写死任何 Provider**。它只依赖一个能流式调用模型的 `streamFn`。换模型、加请求头、做重试，都发生在注入的合同之外。

### 3.3 核心架构：四层包

Pi 的主路径是 `AgentSession → Agent → runAgentLoop → streamFn → ModelRuntime`。读源码从四个包开始：

| 包 | 职责 | 不做什么 |
|:---|:---|:---|
| `pi-ai` | 统一调用各家 LLM，把不同 SSE 收成同一组事件 | 不跑工具循环，不保存会话 |
| `pi-agent-core` | Agent Loop 与工具执行协议、事件 | 不解析 CLI，不直接读终端 |
| `pi-coding-agent` | 把上面两层组装成产品（CLI、项目信任、扩展、会话、TUI） | 不自己实现 LLM 协议 |
| `pi-tui` | 差分渲染、组件、编辑器 | 不了解 Agent 业务 |

无论底层是 Anthropic 还是 OpenAI，`pi-ai` 都归一为同一组 assistant 事件：`start`、`text_delta`、`thinking_delta`、`toolcall_delta`、`done`、`error`。Agent Loop 无需关心底层是哪家协议。

默认内置工具是 `read`、`bash`、`edit`、`write`，另有 `grep`、`find`、`ls`、`powershell`；两个内置扩展 `codemode`（在 QuickJS 沙箱里运行调用其他工具的 JavaScript）与 `tool_search`（搜索未声明的工具）默认关闭，按需启用。

### 3.4 会话与上下文：树、分支与压缩

Pi 对会话的设计很有代表性：

- **会话是一棵树**：每条消息/事件都是树节点，指向父节点；从根到当前节点的路径就是**活动分支**，为下一次模型请求提供历史。
- **分支不删除历史**：继续、Fork、Clone 都会在同一个会话文件中产生新分支，被放弃的分支仍然保留。模型上下文从活动分支重建。
- **压缩（Compaction）**：历史超过窗口时，Pi 插入一条摘要条目，在后续请求中替换更早的消息，但原始条目仍留在会话树中。
- **持久化格式**：会话是 JSONL 文件，便于导出、分支和分享。

### 3.5 扩展体系：扩展、Skill、MCP、SDK

Pi 的可扩展性分层清晰，按“从轻到重”选择：

| 需求 | 机制 |
|:---|:---|
| 给某个文件夹持久指令 | `AGENTS.md` 上下文文件 |
| 复用 `/` 菜单里的 Prompt | Prompt 模板 |
| 任务特定指令 + 配套文件 | Skill（`SKILL.md`） |
| 可执行工具、命令、事件处理器 | 扩展（TypeScript 模块） |
| 连接外部 MCP 服务器 | MCP（stdio / HTTP + OAuth） |
| 在应用内嵌 Pi、程序化驱动 | TypeScript SDK（`createAgentSession`） |
| 连接未支持的模型服务 | 自定义 Provider |
| 安装或分发多个资源 | Pi 包 |

- **扩展**：导出一个默认工厂函数，接收 `ExtensionAPI`，可注册工具、命令、快捷键、Provider、事件处理器、渲染器和终端 UI。扩展在 Pi 进程内运行，拥有相同操作系统权限，因此只应加载可信来源。

```typescript
import type { ExtensionAPI } from '@earendil-works/pi-coding-agent';

export default function (pi: ExtensionAPI) {
  pi.registerCommand('hello', {
    description: 'Show a greeting',
    handler: async (name, ctx) => {
      ctx.ui.notify(`Hello, ${name || 'world'}!`, 'info');
    },
  });
}
```

- **Skill**：实现 Agent Skills 规范，目录内含 `SKILL.md`。Pi 只在系统提示中列出名称与描述，任务匹配时才加载完整指令，避免长期占用上下文。
- **MCP**：通过 stdio 或 streamable HTTP 连接 MCP 服务器，支持 OAuth。工具按 `codemode`（默认）/`deferred`/`direct`/`hidden` 控制**暴露方式**——这是一种很聪明的设计：大型 MCP 服务器默认不把工具塞进模型声明，而是让脚本按需调用，既省 token 又保持能力完整。
- **SDK**：`createAgentSession()` 可以在 Node.js/Bun 进程内嵌 Pi，直接访问 Agent、会话、工具、模型与资源，适合把 Pi 做成自研应用的执行内核。

### 3.6 安全模型：项目信任是安全边界

Pi 会加载项目目录里的 `.pi/`、`AGENTS.md`、项目扩展，这些东西能执行任意代码。因此 Pi 在加载项目资源**之前**先确定**项目信任（Project Trust）**：未信任的项目，本地扩展不会执行。启用的工具使用 Pi 进程的操作系统权限，扩展也在该进程内执行。这是 Pi 对“会执行代码的 Agent 如何面对陌生仓库”的答案。

### 3.7 安装与使用

安装（Node.js 22.19+）：

```bash
# npm 方式
npm install -g --ignore-scripts @earendil-works/pi-coding-agent

# 或 安装器方式（macOS / Linux）
curl -fsSL https://pi.dev/install.sh | sh

# 验证
pi --version

# 升级
pi update
```

启动并登录模型：

```bash
cd /path/to/folder
pi

# 在交互界面中：
/login     # 选择 Provider 并登录
/model     # 选择模型
```

给 Pi 一个任务：

```text
Explain how this repository is structured and how to run its checks.
```

Pi 会显示它执行的每次文件读取、搜索、命令和编辑。会话自动保存，`pi --continue` 恢复最近会话，`/resume` 选择其他会话。除交互 TUI 外，Pi 还提供多种自动化接口：

```bash
# 一次执行后退出
pi --print "Summarize this repository"          

# JSON 事件流
pi --mode json "Inspect this repo" > events.jsonl  

# RPC 模式，stdin 收命令、stdout 出事件
pi --mode rpc                                   
```

模型方面，Pi 支持受支持订阅、API Key、本地 llama.cpp router，以及 Ollama / LM Studio / vLLM 等 OpenAI 兼容端点（通过 `models.json` 配置）。

---

## 第四章：DSH（DeepSeek Harness）—— 一切皆插件

### 4.1 工具简介

DeepSeek Harness（DSH）是 DeepSeek 于 2026 年 8 月发布的开源 Agent 框架（开发者预览版），核心理念是 **Agent = Model + Harness**：模型负责推理，Harness 负责把模型接入真实世界（文件、终端、浏览器）。它最特别的地方在于——**Harness 的每个组成部分都是插件，连 UI 本身也是插件**。

- 官网：https://www.deepseek.com/en/harness/
- GitHub：https://github.com/deepseek-ai/deepseek-harness
- npm 包：`@deepseek-ai/dsh`
- 开发者文档：https://deepseek-harness.github.io/deepseek-harness/
- 插件话题：https://github.com/topics/dsh-plugin

它采用 MIT 许可证，明确对标 Claude Code，目标是把能力全部开放出来：你可以读 Agent 循环、fork 它、替换核心部件。

### 4.2 设计原理：Cordis 插件内核

DSH 构建在 **Cordis**（cordiverse/cordis）的 **“一切皆插件”（Everything is a plugin）** 架构之上。传统 Agent 工具里，工具、UI、会话存储、沙箱都是硬编码的；DSH 把它们全部抽象成可插拔的 npm 插件：

- **工具、技能、会话、沙箱、Agent 循环** → npm 插件，可自由拼装替换。
- **默认安装即带 100+ 一方插件**，覆盖文件、终端、浏览器、搜索等能力。
- **依赖注入 + 生命周期托管**：插件通过导出的 `inject` 声明依赖，注册到 `ctx` 的能力在插件卸载时自动清理——这才是“可装卸、可自进化”的机制基础，而不只是“把代码做成 npm 包”。
- **运行中装卸插件、改造自身**：Agent 可以在运行过程中加载/卸载插件来扩展自己，这是“自进化”的由来。
- **基于 Cordis 内核**，有公开论文支撑（arXiv:2608.25512）。

这个理念的取舍很鲜明：换来的是一致且极致的可扩展性，代价是生态早期、质量参差，需要使用者自己当质量把关人。

### 4.3 架构与插件模型

一个 DSH 插件就是导出 `apply` 函数的 TypeScript 模块。框架加载时调用 `apply`，传入上下文对象 `ctx`，你通过它注册能力；插件卸载时，通过 `ctx` 注册的东西会被自动清理（生命周期托管）。

```typescript
import type { Context } from '@deepseek-ai/cordis'

export const name = 'my-plugin'

export function apply(ctx: Context) {
  // 在这里注册工具、命令等能力
}
```

注册一个模型可调用的工具：

```typescript
import { defineTool } from '@deepseek-ai/dsh-tools'

ctx.tools.register(defineTool({
  name: 'greet',
  description: 'Greet someone by name.',
  parameters: { name: { type: 'string', required: true, description: 'The name' } },
  output: { schema: { type: 'string' }, render: (_a, v) => [{ type: 'text', text: v }] },
  async execute(args) { return `Hello, ${args.name}!` },
}))
```

插件以 **profile** 为单位组织：`dsh plugin --profile web add <包名>` 会把 npm 包写进该 profile 的 `package.json`（既是依赖，也进 `dsh.profile.bundles`），下次启动自动加载。插件管理命令底层转发给 `pnpm`。

### 4.4 多种运行形态

DSH 同一套 Agent API 暴露多种入口，按使用场景选择：

- **Web UI**：`dsh web`，默认 `http://127.0.0.1:3080`，含插件管理、轨迹查看（trajectory view）、定时任务等，适合日常交互。
- **无头模式（Headless）**：跑单个任务后退出，适合脚本与 CI。
- **Python SDK**：把同一套 agent API 暴露给 Python 程序（Python 3.10+，目前仅 Linux/macOS）。
- **桌面客户端**：官方与社区都提供了桌面封装，开箱即用。

### 4.5 安装与模型配置

DSH 的核心运行时以 npm 包 `@deepseek-ai/dsh` 发布（官方发布渠道为 npm registry 与 GitHub 源码仓库），并另有官方桌面客户端下载。唯一前置依赖是 Node.js（LTS 18+）。

```bash
# 推荐：直接用 npx 启动 Web UI
npx @deepseek-ai/dsh web

# 长期使用：安装为全局命令
npm install -g @deepseek-ai/dsh
dsh web

# 升级
npm update -g @deepseek-ai/dsh
```

CLI / 无头模式的密钥写在 `$DSH_HOME/.credentials.yaml`（默认 `~/.dsh/.credentials.yaml`）：

```yaml
DEEPSEEK_API_KEY: sk-your-key-here
```

模型方面，DSH 支持 DeepSeek、Anthropic、OpenAI 等目录 Provider，以及 Bedrock/Vertex/Azure 等原生凭据；自定义 Provider 只需小写 ID + 基础 URL + 协议 + 凭据 + 至少一个模型。模型变更下一次请求即生效，无需重启。

### 4.6 使用实践

**Web UI 首次使用**：启动后浏览器打开 `http://127.0.0.1:3080`，在 **设置 → 模型** 填入 API Key，点击 **选择工作区** 添加项目目录，然后发送任务：

```text
Summarize this repository and identify its main packages.
```

**脚本 / CI**：用无头模式跑单个任务后退出。

```bash
dsh --profile headless "Inspect the repository and fix the failing tests."
```

**嵌入自研应用**：用 Python SDK 以代码驱动同一套 agent API。

```bash
python -m pip install deepseek-harness-sdk
export DEEPSEEK_API_KEY=sk-your-key-here
python examples/jsonrpc-agent/minimal.py \
  --workspace /absolute/path/to/workspace \
  "Inspect the repository and fix the failing tests."
```

### 4.7 生态与定位（vs Claude Code）

DSH 的生态完全建立在“npm 插件”之上：官方与社区能力都发布为 npm 包，仓库打上 [`dsh-plugin`](https://github.com/topics/dsh-plugin) 话题即可被生态发现；`dsh plugin --profile web add <包名>` 安装后会写进 profile 的 `package.json`，下次启动自动加载。默认安装就带 100+ 一方插件，覆盖文件、终端、浏览器、搜索等能力，社区插件规模也在快速增长。

不过开放的另一面是质量参差：与有厂商审核的市场不同，DSH 把“质量把关”交回给使用者——装上的插件不一定能跑通、也不一定安全。这正是它“完全掌控”的代价，也决定了它更适合愿意读源码、改内部的开发者。

DSH 从发布起就明确对标 Claude Code。两者都能读写文件、运行命令、执行多步任务，但架构、许可与扩展模型根本不同：

| 对比项 | DeepSeek Harness | Claude Code |
|:---|:---|:---|
| **许可** | MIT，完全开源 | 闭源 |
| **扩展模型** | 一切皆插件（npm） | MCP 服务器 + 插件 |
| **内核** | Cordis 插件内核 | 私有 |
| **界面** | Web UI、Headless、TUI、Python SDK | CLI、Web、IDE 扩展 |
| **语言** | TypeScript | TypeScript/Node |
| **成熟度** | 开发者预览（快速迭代） | 成熟、广泛部署 |
| **成本** | 免费 + 模型 API | 订阅 + 模型 API |
| **模型** | DeepSeek + 任意 OpenAI 兼容 | Claude + Bedrock/Vertex/自定义 |

**插件 vs MCP 的本质区别**：dsh 插件是带 `apply` 函数、跑在 dsh 进程内的 TypeScript 模块，**更容易写、更好分享**（就是 npm 包）；MCP 服务器是独立进程、说标准协议，**更可移植**（一个服务器可在 Claude Code、Cursor 等任意 MCP 客户端通用）。

> 关于成本还需补一句：有社区基准指出，同一个 DeepSeek 模型在不同 harness 下，因请求形态（尤其是否针对前缀缓存优化）差异，单次任务成本会出现明显差距。该数据来自非官方实测，仅作参考，但足以说明**“换 Harness”本身也是一种成本变量**。

---

## 第五章：总结与选型

三款工具代表了开源 Agent 的三种思路：

| 视角 | OpenCode | Pi | DSH |
|:---|:---|:---|:---|
| **设计哲学** | 开放、多端、生态优先 | 极简、解耦、可嵌入 | 一切皆插件、可自进化 |
| **最擅长** | 日常开发、模型自由切换 | 读源码、二次开发、嵌入应用 | 完全掌控、自托管、插件改造 |
| **扩展单位** | 插件 / MCP / Agent | TypeScript 扩展 / Skill / MCP | npm 插件（含 UI 与循环） |
| **上手成本** | 低 | 中 | 中（需接受预览版打磨成本） |
| **长期风险** | 生态依赖社区维护 | 相对小众 | 迭代快、可能有破坏性变更 |

它们共享的“开源 Agent 共性”同样值得记住：

1. **模型与 Harness 解耦**：三者都支持多 Provider/本地模型，避免厂商锁定。
2. **能力以“工具”为边界**：模型只提议，程序负责校验与执行——这是安全的根基。
3. **上下文是稀缺资源**：压缩、分支、工具按需暴露（Pi 的 codemode、DSH 的插件按需加载）都是围绕 token 与缓存做的优化。
4. **安全靠边界而非信任**：OpenCode 的项目级权限、Pi 的项目信任、DSH 的工作区边界与插件沙箱，本质都是“默认最小权限”。

**落地建议**：

- 个人/团队想立刻用起来 → **OpenCode**。
- 想把 Agent 嵌进自研产品或做深度定制 → **Pi**（读四层包 + 用 TypeScript SDK）。
- 想完全掌控、改 UI、自托管、玩插件生态 → **DSH**（接受预览版的不稳定，换来极致的可改造性）。

开源 Agent 的竞争，最终不是比谁模型强，而是比谁把“模型接入真实世界”这件事做得**既可控又易扩展**。这三款工具，正是同一命题下的三种优秀答卷。
