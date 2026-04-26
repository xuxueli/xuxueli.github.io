<h2 style="color:#4db6ac !important" >AI编程实战：Claude Code + IDEA 的沉浸式编程方案</h2>
> 【原创】2026/04/26

[TOCM]

[TOC]

## 一、Claude Code介绍
Claude Code 是 Anthropic 推出的面向开发者的 AI 编程协作工具，与传统的Chat模式不同，Claude Code是一个能读项目、懂上下文、遵守约束的 AI 编程搭档。

Claude Code 核心目标是理解你的整个项目，并参与到真实的编码、修改和重构过程中，主要具备三点特征：
- 上下文感知：不仅理解单个文件，而是理解整个项目结构
- 工程化导向：关注可维护性、规范、测试，而不是一次性代码
- 可定制行为：通过 Skills（技能包）让 AI 遵守你的规则

## 二、Coding Plan选择

Claude Code官方支持通过 Claude订阅 或 Anthropic账户 开通使用，同时也支持第三方提供商；本文以第三方 Coding Plan（阿里云百炼）为例进行讲解。  

阿里云百炼是一站式大模型开发与应用平台，集成了千问及主流第三方模型（Qwen、GLM、Kimi、Minimax等），为开发者提供了兼容Anthropic协议的API及全链路模型服务。 
> PS：业界存在多家兼容Anthropic协议的模型供应商，可参考选择：阿里云百炼、‌智谱AI、DeepSeek ... 等。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260426_01.png)

## 三、Claude Code安装配置

### 3.1、Claude Code 命令安装

Claude Code 提供多种产品形态，Terminal形态提供功能完成的CLI，用于直接在终端中使用 Claude Code 编辑文件、运行命令，并从命令行管理整个项目。

针对Mac用户可选择 Brew 方式安装：   
```
brew install --cask claude-code
```

其他命令：
```
# 查看版本
claude --version

# 更新版本
claude update
```

### 3.2、Claude Code 配置 Coding Plan

**a、初始化「流程配置文件」**： `~/.claude/settings.json` 
```
vi ~/.claude/settings.json
```

**b、编辑「流程配置文件」**：

将 YOUR_API_KEY 替换为 Coding Plan 专属API Key；保存配置文件，重新打开一个终端即可生效。
```
{    
    "env": {
        "ANTHROPIC_AUTH_TOKEN": "YOUR_API_KEY",
        "ANTHROPIC_BASE_URL": "https://coding.dashscope.aliyuncs.com/apps/anthropic",
        "ANTHROPIC_MODEL": "qwen3.6-plus",
        "ANTHROPIC_SMALL_FAST_MODEL": "qwen3.6-plus",
        "ANTHROPIC_DEFAULT_HAIKU_MODEL": "qwen3.6-plus",
        "ANTHROPIC_DEFAULT_SONNET_MODEL": "qwen3.6-plus",
        "ANTHROPIC_DEFAULT_OPUS_MODEL": "qwen3.6-plus",
        "CLAUDE_CODE_SUBAGENT_MODEL": "qwen3.6-plus"
    }
}
```

**c、 编辑或新增「客户端配置文件」**： `~/.claude.json`

将hasCompletedOnboarding 字段的值设置为 true。该步骤可避免启动Claude Code时报错：Unable to connect to Anthropic services。
```
{
    "hasCompletedOnboarding": true
}
```

**d、开启 Agent Team功能（可选）**：

Agent Team 是 Claude Code 的实验性功能，需设置 CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS 环境变量为 1 来启用。变量设置方式：
```
{
  "env": {
    "CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS": "1",
    ...
  }
}
```

### 3.3、Claude Code 命令使用

打开终端，并进入项目所在的目录
```
cd path/to/your_project
claude
```

启动后，授权 Claude Code 执行文件
```
Quick safety check: Is This a project you created or one yuo trust? ... 
> 1. Yes, I trust this folder
  2. No, exit 
```

输入 ```/status``` 确认模型、Base URL、API Key 是否配置正确。然后，可以在 Claude Code 中对话使用。

### 3.4、Claude Code 常见命令

| 命令                | 说明                                      | 示例                        |
|:------------------|:----------------------------------------|:--------------------------|
| `/init`           | 在项目根目录生成 CLAUDE.md 文件，用于定义项目级指令和上下文。    | `/init`                   | 
| `/status`         | 查看当前模型、API Key、Base URL 等配置状态。          | `status`                  | 
| `/model <模型名称>`   | 切换模型。                                   | `/model qwen3-coder-next` | 
| `/clear`          | 清除对话历史，开始全新对话。                          | `clear`                   | 
| `/plan`           | 进入规划模式，仅分析和讨论方案，不修改代码。                  | `plan`                    | 
| `/compact`        | 压缩对话历史，释放上下文窗口空间。                       | `compact`                 | 
| `/config`         | 打开配置菜单，可设置语言、主题等。                       | `config`                  | 


## 四、IDEA 集成 Claude Code

Claude Code IDE 插件支持在 JetBrains 系列 IDE中使用。  
打开JetBrains扩展市场（Setting -> Plugins -> Marketplace），搜索 Claude Code 插件安装即可。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260426_02.jpg)

安装后重启IDE，单击右上角图标即可使用，可通过 `/model <模型名称>` 命令切换模型。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260426_03.jpg)


## 五、实战演示

### 5.1、简单任务

**a、发布任务：**

- 任务描述：“分析项目的 pom 依赖版本，帮我升级到最新版本。注意：先计划，找我确认后再执行”
- 补充说明：为避免任务执行偏离目标，强烈建议「先计划，再执行」，参考文末最佳实践建议。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260426_04.jpg)

**b、生成计划：**

Claude Code 接受任务后，将会按照要求生成执行计划：
- 1. 扫描项目依赖关系，并生成依赖树。
- 2. 使用 Web Search 查询 Maven Central，获取每个依赖的最新版本。
- 3. 生成修改建议，列出需要升级的依赖及其新版本。
- 4. 等待用户确认后，修改 pom.xml 文件，升级依赖版本。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260426_05.jpg)

**c、执行计划：**

Claude Code 生成执行计划后将输出修改建议，用户确认后 Claude Code 将会修改 pom.xml 文件，进行依赖版本升级。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260426_06.jpg)

### 5.2、复杂任务

略，更复杂的任务或使用方式，可参考文末官网文档。

## 六、最佳实践

### 6.1、上下文管理
- 及时清理： 使用 /clear 定期重置对话，防止旧的上下文干扰新任务并节省 Token。
- 主动压缩：使用 /compact 命令让 Claude 总结关键决策和修改的文件，保留核心记忆。
- 明确指定文件： 提问时使用 @ 引用文件（如 write a test for @auth.py），避免模型无效扫描整个项目。
- 善用子代理（Sub-agents）： 对于大规模任务，让 Claude 启动子代理执行。子代理完成任务后返回精炼结论，保护主对话的上下文空间。

### 6.2、先计划，再执行 
- 启用 Plan 模式：复杂任务前，先分析方案，不实际修改文件。
  - 快捷操作：连续按两次 Shift + Tab 进入 Plan Mode。
  - 提示词约束：提示词明确要求“先输出详细实施计划，经我确认后再修改文件”。
- 降低试错成本：确保逻辑闭环后再进行代码变更。

### 6.3、沉淀项目核心知识：编写 CLAUDE.md
- 包含关键信息：每次会话启动时自动加载CLAUDE.md，建议填入构建命令、代码规范及工作流等通用规则。
- 动态维护：内容应简短易读，仅记录广泛适用的全局约定，并随项目演进持续补充新规则。

### 6.4、扩展能力：MCP 与 Skills   
- [MCP](https://code.claude.com/docs/zh-CN/mcp)：安装成熟的 MCP Server，连接外部服务。例如：[添加联网搜索MCP](https://help.aliyun.com/zh/model-studio/web-search-for-coding-plan)。
- [Skills](https://code.claude.com/docs/zh-CN/skills)：编写详细的 Skill 描述文案。Claude 决定是否调用该工具，取决于对该工具用途的定义。例如：[添加视觉理解能力Skill](https://help.aliyun.com/zh/model-studio/add-vision-skill)。

### 6.5、自动化守护：Hooks 
- 使用 [Hooks](https://code.claude.com/docs/zh-CN/hooks)：Hooks 是确定性规则。它在 Claude 工作流的特定生命周期节点（如 PreToolUse 工具执行前校验等）自动运行本地脚本，确保关键校验或操作 100% 执行。
- 配置方式：
  - 运行 `/hooks` 进行交互式配置。
  - 直接编辑 `.claude/settings.json`
  - 让 Claude 帮你编写，如：“编写一个在每次文件编辑后运行 eslint 的 hook”。

### 6.6、建立自检闭环
- 强制验证： 要求 Claude 修改代码后，必须运行相关的测试用例（如 pytest 或 npm test）。
- 定义成功标准： “修改完成后，请确保编译通过，并且运行 curl 命令验证 API 返回值为 200”。
- 视觉反馈： 前端修改时，要求 Claude 截取浏览器截图来确认 UI 效果。


## 七、资料
- Claude官网地址：https://claude.com/product/claude-code
- Claude官方文档：https://code.claude.com/docs/zh-CN/overview
- Aliyun CodingPlan文档：https://help.aliyun.com/zh/model-studio/claude-code-coding-plan 
- Claude Code教程：https://www.runoob.com/claude-code/claude-code-tutorial.html 