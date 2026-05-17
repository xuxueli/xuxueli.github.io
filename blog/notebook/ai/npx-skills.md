<h2 style="color:#4db6ac !important" >扩展 AI 能力：npx skills 实战手册</h2>
> 【原创】2026/05/17

[TOCM]

[TOC]

---

### 一、Skill 概念与设计理念

Skill 是一种轻量、可复用的"能力包"，通常以 Markdown（或兼容的文本格式）封装。每个 Skill 聚焦一个明确的任务或流程（例如：生成 PPT、代码重构、编写部署脚本、格式化缺陷报告等），将执行该任务所需的说明、步骤、示例与注意事项结构化地记录下来，便于 AI 或自动化工具按需加载和执行。

**核心要素**：
- **名称与描述**（name, description）：便于发现与检索
- **输入输出约定**（inputs/outputs）：清晰说明需要哪些输入和会产生什么结果
- **步骤与示例**（step-by-step & examples）：给出可执行的流程与最小示例
- **注意事项与最佳实践**（pitfalls/best-practices）：避免常见错误或误用
- **元数据**（frontmatter）：用于版本、依赖、兼容 Agent 等管理信息

**设计目标与优势**：
- **可组合**：多个 Skill 可以串联成更复杂的工作流
- **可重用**：同一 Skill 可被不同 Agent 或项目重复使用
- **易维护**：通过 frontmatter 管理版本与兼容性，便于演进与回溯
- **可审计**：把决策逻辑和示例都写入文本，便于人为 review 与复现

**典型应用场景**：
- **对话增强**：在对话中按需加载 Skill，快速完成结构化任务
- **自动化集成**：在 CI / 自动化脚本中调用 Skill，实现流程标准化
- **团队知识库**：将专家经验模块化，降低新成员学习门槛

**简短示例**（概念级）：
- `pptx-generator`：根据提纲自动生成 PPT 模板
- `bug-report`：将日志和堆栈信息格式化为规范缺陷单

**设计最佳实践**：
- ✅ **聚焦单一目的**：一个 Skill 应只做一件事，避免耦合无关流程
- ✅ **保持示例简洁**：保持示例最小可运行、输入明确
- ✅ **完善元数据**：在 frontmatter 中声明必需字段（如 name、description、version、inputs schema），便于自动发现与验证

> **总结**：通过 Skill，把常见操作与专业流程模板化，既能提升 AI 的任务能力，也方便团队复用与治理。

---

### 二、npx skills 工具详解

npx skills 是基于 Vercel Labs Skills CLI 的轻量调用方式（通过 npx 运行，无需全局安装），用于管理本地或项目级的 Skill：发现、安装、更新、移除和审查技能包。把它想象成专门针对 Skill 的**包管理器**——操作简单、按需安装，并支持多种来源（GitHub、HTTP 或本地目录）。

**核心能力概览**：
- 🔍 **搜索/发现**：在远程仓库或 registry 中查找可用 Skill
- 📦 **安装/移除**：把 Skill 安装到项目或用户目录，支持符号链接或复制
- 📋 **列表/审查**：查看当前已安装的 Skill、版本与来源
- 🔄 **更新/锁定**：检查并更新 Skill，支持生成锁定文件以保证一致性
- 🛠️ **初始化**：快速创建 Skill 模板，便于编写与发布

**快速上手**（常用命令示例）：

```bash
# 从 GitHub 仓库安装单个 skill（示例：pptx）
npx skills add https://github.com/anthropics/skills --skill pptx

# 安装整个仓库的 skills（简写）
npx skills add vercel-labs/agent-skills

# 列出当前项目已安装的 skills
npx skills list

# 查找远程技能
npx skills find design

# 查看帮助
npx skills -h
```

**来源与兼容性**：
- **支持的安装源**：直接从 GitHub 仓库、HTTPS 地址或本地目录安装
- **Agent 兼容性**：可以为不同 Agent（如 Claude Code、GitHub Copilot、OpenCode 等）指定兼容性或安装范围
- **安装方式**：可选择符号链接（默认或推荐，便于同步更新）或复制（隔离部署）

**常用选项说明**：

| 选项 | 说明 | 示例 |
|------|------|------|
| `-s, --skill <skills...>` | 仅安装指定的 skill 名称 | `--skill pptx` |
| `-a, --agent <agents>` | 指定目标 Agent（多个以逗号或重复参数） | `-a claude-code` |
| `-g, --global` | 全局安装到用户目录（跨项目共享） | `-g` |
| `-l, --list` | 仅列出可用的技能而不安装 | `--list` |
| `--copy` | 使用复制而非符号链接 | `--copy` |
| `-y, --yes` | 跳过确认提示 | `-y` |
| `--all` | 安装仓库中的所有技能 | `--all` |

**安全与注意事项**：

⚠️ **源可信性**：从 GitHub 或第三方仓库安装时，优先选择可信来源并审查 Skill 的 frontmatter 与示例

⚠️ **权限与执行**：Skill 可能包含脚本或生成器，注意其写入路径与权限请求

⚠️ **符号链接 vs 复制**：开发时推荐符号链接（便于更新），生产或受管环境可选复制以确保稳定性

⚠️ **锁定依赖**：使用 `npx skills generate-lock` 生成锁文件，避免无意中随时间升级带来的不兼容

**常见操作示例**（实用场景）：

```bash
# 场景1：本地开发 Skill
# 在 Skill 源目录运行，将本地 Skill 链接到项目以便调试
npx skills add ./my-local-skill

# 场景2：为特定 Agent 安装
# 只为 claude-code 安装 foo 技能
npx skills add repo --skill foo -a claude-code

# 场景3：批量安装（谨慎使用）
# 安装仓库内所有技能到所有 Agent
npx skills add repo --all
```

**社区资源**：
- 📖 CLI 源码与文档：https://github.com/vercel-labs/skills
- 🔍 Skill 浏览与发现：https://www.skills.sh/ （在线目录）
- 💡 Vercel 官方技能仓库：https://github.com/vercel-labs/agent-skills
- 🤖 Anthropic 技能仓库：https://github.com/anthropics/skills

> **小结**：npx skills 提供了一个轻量且灵活的方式来管理 Skill，适合个人在本地或项目中按需安装、测试和分享能力包。正确使用符号链接、锁文件与审查流程，可以在提高效率的同时控制风险。

---

### 三、工作原理：渐进式加载架构

Skills 采用**渐进式加载（Progressive Disclosure）**架构，极大减少了上下文窗口的占用：

**工作流程**：

1. **启动阶段**（轻量扫描）
  - 扫描技能目录
  - 解析 frontmatter
  - 仅加载技能名称和简短描述

2. **任务匹配阶段**（智能判断）
  - 分析当前任务需求
  - 根据当前任务判断需要哪个技能
  - 确定需要加载的技能列表

3. **按需加载阶段**（完整加载）
  - 只加载匹配的完整技能内容
  - 注入到 AI 上下文窗口
  - 执行任务

**核心优势**：
- 🚀 **减少 Token 消耗**：只加载必要的技能内容
- ⚡ **提升响应速度**：避免一次性加载大量无关信息
- 🎯 **精准匹配**：根据任务动态选择最相关的技能

---

### 四、核心命令与使用指南

#### 4.1 技能发现：`npx skills find`

在远程仓库或 registry 中搜索可用技能。

```bash
npx skills find [query]
```

**使用示例**：
```bash
# 查找 pptx 相关的技能
npx skills find pptx
```

#### 4.2 技能安装：`npx skills add`

核心命令，支持多种安装源格式。

**安装源格式对比**：

```bash
# 格式1：简写安装（最常用）
# 拉取 Vercel 官方公开技能仓库"全套技能"
npx skills add vercel-labs/agent-skills

# 格式2：HTTPS 完整仓库地址
# 明确指定 GitHub 仓库，拉取全套技能（功能同格式1）
npx skills add https://github.com/vercel-labs/agent-skills

# 格式3：指定技能名称安装（单个技能）
# 从 GitHub 仓库安装技能，只安装仓库里 pptx 单个技能
npx skills add https://github.com/anthropics/skills --skill pptx

# 格式4：精准安装单个技能（子目录路径）
# 仅安装仓库中的 web-design-guidelines 子技能
npx skills add https://github.com/vercel-labs/agent-skills/tree/main/skills/web-design-guidelines

# 格式5：本地技能安装（本地技能）
# 直接加载当前目录下的本地技能文件夹，用于开发/测试自定义技能
npx skills add ./my-local-skills
```

**常用选项**：

| 选项 | 说明 |
|------|------|
| `-s, --skill <skills...>` | 指定要安装的技能名称（可指定多个） |
| `-a, --agent <agents>` | 指定目标 AI 助手 |
| `-g, --global` | 全局安装（用户级别），默认是项目级别 |
| `-l, --list` | 仅列出可用技能，不实际安装 |
| `--copy` | 使用复制而非符号链接安装 |
| `-y, --yes` | 跳过所有确认提示 |
| `--all` | 安装所有技能到所有 Agent |

**常用组合示例**：

```bash
# 1、指定 AI 工具：
npx skills add vercel-labs/agent-skills --skill pptx -a opencode                  # 安装 pptx 技能，到 opencode
npx skills add vercel-labs/agent-skills --skill pptx -a opencode -a claude-code   # 安装 pptx 技能，到 opencode 和 claude-code
npx skills add vercel-labs/skills --skill find-skills --agent '*'                 # 安装 find-skills 技能，到所有 Agent

# 2、全局、项目级安装：
npx skills add vercel-labs/agent-skills --skill pptx -g              # 全局安装
npx skills add vercel-labs/agent-skills --skill pptx                 # 项目级 

# 3、安装多个指定技能：
npx skills add vercel-labs/agent-skills --skill pptx --skill skill-creator

# 4、安装所有技能（谨慎使用）
npx skills add vercel-labs/agent-skills --all

# 5、仅列出可用技能，不实际安装
npx skills add vercel-labs/agent-skills --list
```

#### 4.3 其他常用命令

```bash
# 1、列出已安装Skill
npx skills list                # 项目级
npx skills ls -g               # 全局级
npx skills ls -a opencode      # 按 AI 工具过滤
npx skills ls --json           # JSON 输出，方便脚本

# 2、搜索Skill
npx skills find                # 交互式搜索
npx skills find [query]        # 带关键词搜索

# 3、更新Skill

npx skills update              # 更新所有项目级 Skill
npx skills update [skill-name] # 只更新某个
npx skills update -g           # 只更新全局 Skill
npx skills update -p           # 只更新项目级 Skill
npx skills upgrade             # 同 update

# 4、删除Skill
npx skills remove              # 交互式选择要删的
npx skills remove [skills]     # 按名称删除
npx skills remove -g           # 从全局删除
npx skills rm [skills]         # 简写 
npx skills rm --all            # 删除所有（需确认）

# 5、检查技能更新
npx skills check

# 6、创建新技能模板
npx skills init [name]

# 7、生成锁定文件用于更新跟踪
npx skills generate-lock
```

**命令速查表**：

| 命令 | 简写 | 说明 |
|------|------|------|
| `find` | - | 搜索技能 |
| `add` | - | 安装技能 |
| `list` | `ls` | 列出已安装技能 |
| `remove` | `rm` | 移除已安装的技能 |
| `check` | - | 检查技能更新 |
| `update` | - | 更新技能 |
| `init` | - | 创建新技能模板 |
| `generate-lock` | - | 生成锁定文件 |

---

### 五、SKILL.md 格式规范

一个标准的 SKILL.md 包含两部分：**Frontmatter 元数据** + **正文内容**

#### 5.1 Frontmatter 元数据

**必填字段**：
- `name`：技能名称（小写字母、数字、连字符）
- `description`：简短描述，说明适用场景

**可选字段**：
- `metadata.internal`：内部技能标记
- `version`：版本号，便于追踪更新
- `inputs`：输入参数 schema 定义
- `outputs`：输出结果说明
- `compatibleAgents`：兼容的 Agent 列表

**示例**：

```yaml
---
name: my-skill
description: 这个技能的简短描述，说明适用场景
---
```

**命名规范**：
- ✅ 正确：`my-skill`、`bug-report-v2`、`pptx-generator`
- ❌ 错误：`MySkill`、`bug_report`、`pptx generator`（含空格或大写）

#### 5.2 正文内容

标准 Markdown 格式，建议包含以下章节：
- **Overview**：技能概述，说明用途和适用场景
- **Step-by-step**：分步骤的执行流程
- **Examples**：最小可运行的示例
- **Pitfalls**：常见错误和注意事项
- **Best Practices**：最佳实践建议
- **References**：相关资源链接

#### 5.3 安装范围与路径

安装技能时有两种范围可选：

| 范围 | Flag | 位置 | 适用场景 |
|------|------|------|----------|
| **Project**（默认） | (无) | `./<agent>/skills/` | 项目级别，随项目版本控制 |
| **Global** | `-g` | `~/<agent>/skills/` | 全局范围，跨项目共享 |

**不同 Agent 的全局路径**：

| Agent             | 全局路径                         |
|-------------------|------------------------------|
| 标准路径              | `~/.agent/skills/`           |
| Claude Code       | `~/.claude/skills/`          |
| GitHub Copilot    | `~/.copilot/skills/`         |
| OpenCode          | `~/.config/opencode/skills/` |
| Cursor            | `~/.cursor/skills/`          |


**安装方式对比**：

| 方式 | 优点 | 缺点 | 适用场景 |
|------|------|------|----------|
| **符号链接**（推荐） | 更新一处即可同步所有 Agent | 依赖原始路径存在 | 开发环境、团队协作 |
| **复制** | 独立部署，不受源影响 | 更新需重新安装 | 生产环境、稳定部署 |

> **提示**：符号链接的优点是更新一处即可同步所有 Agent，推荐在开发环境中使用。

---

### 六、实战演练：从零开始

本章节提供完整的实战流程，帮助您快速上手 npx skills。

#### 6.1 场景1：首次安装并使用技能

**步骤1：验证工具可用**

无需安装，直接使用 npx 运行：

```bash
npx skills -h
```

**步骤2：浏览可用技能**

```bash
# 搜索感兴趣的技能
npx skills find design
npx skills find frontend
```

**步骤3：安装技能到项目**

```bash
# 安装 Vercel 官方技能仓库（推荐新手）
npx skills add vercel-labs/agent-skills

# 或仅安装特定技能
npx skills add vercel-labs/agent-skills --skill frontend-design
```

**步骤4：验证安装**

```bash
# 查看已安装的技能列表
npx skills list
```

**步骤5：在 AI 助手中使用**

- **Claude Code**：技能会自动加载到 `~/.claude/skills/` 或项目目录
- **GitHub Copilot**：技能会出现在 `~/.copilot/skills/`
- 在对话中提及技能名称，AI 会自动识别并加载对应技能

#### 6.2 场景2：创建自定义技能

**步骤1：初始化技能模板**

```bash
# 创建名为 my-custom-skill 的技能模板
npx skills init my-custom-skill
```

这会生成基础目录结构：

```
my-custom-skill/
├── SKILL.md          # 技能主文件
└── assets/           # 可选：资源文件目录
    └── example.png
```

**步骤2：编辑 SKILL.md**

参考第五章节的格式规范，编写您的技能内容。

**步骤3：本地测试**

```bash
# 将本地技能链接到项目进行测试
npx skills add ./my-custom-skill

# 验证是否安装成功
npx skills list
```

**步骤4：分享给团队**

将技能目录推送到 Git 仓库：

```bash
git add my-custom-skill
git commit -m "Add custom skill: my-custom-skill"
git push
```

团队成员可以通过以下方式安装：

```bash
npx skills add https://github.com/your-org/your-repo --skill my-custom-skill
```

#### 6.3 场景3：团队协作与版本管理

**步骤1：生成锁文件**

```bash
# 在项目根目录生成锁文件
npx skills generate-lock
```

这会创建 `skills-lock.json`，记录所有技能的版本信息。

**步骤2：提交到版本控制**

```bash
git add skills-lock.json
git commit -m "Add skills lock file"
```

**步骤3：团队成员同步**

团队成员克隆项目后，根据锁文件安装相同版本的技能。

**步骤4：定期检查更新**

```bash
# 检查是否有新版本
npx skills check

# 更新所有技能
npx skills update

# 更新后重新生成锁文件
npx skills generate-lock
```

#### 6.4 场景4：多 Agent 环境配置

**问题**：团队使用不同的 AI 助手（Claude Code、Copilot、Cursor 等）

**解决方案**：全局安装 + 符号链接

```bash
# 全局安装到所有 Agent
npx skills add vercel-labs/agent-skills -a claude-code -a copilot -a cursor -g

# 验证各 Agent 的技能目录
ls ~/.claude/skills/
ls ~/.copilot/skills/
ls ~/.cursor/skills/
```

**优势**：
- ✅ 一次安装，所有 Agent 共享
- ✅ 使用符号链接，更新一处即可同步
- ✅ 节省磁盘空间

#### 6.5 常见问题与解决方案

**Q1：安装后 AI 助手没有识别到技能？**

**解决方案**：
1. 确认技能安装在正确的 Agent 目录：`npx skills list`
2. 重启 AI 助手应用
3. 检查 SKILL.md 的 frontmatter 格式是否正确
4. 确认 name 和 description 字段存在

**Q2：如何卸载技能？**

```bash
# 移除指定技能
npx skills remove skill-name
# 或简写
npx skills rm skill-name
```

**Q3：符号链接失效怎么办？**

```bash
# 重新安装技能（会重建符号链接）
npx skills add repo --skill skill-name

# 或使用复制方式
npx skills add repo --skill skill-name --copy
```

**Q4：如何查看技能的详细内容？**

直接打开 SKILL.md 文件：

```bash
# 项目级别
cat ./.claude/skills/skill-name/SKILL.md

# 全局级别
cat ~/.claude/skills/skill-name/SKILL.md
```

**Q5：技能更新后如何回滚？**

如果有锁文件：

```bash
# 查看锁文件中的版本信息
cat skills-lock.json

# 手动安装指定版本（如果支持）
# 或从 Git 历史恢复旧的锁文件
```

**Q6：npx安装的技能，opencode 如何关联使用？**

- 全部安装方式：默认技能安装在 `~/.agent/skills/` 或 `~/.config/opencode/skills/` 目录下，opencode 会自动识别并加载这些技能，无需额外配置。
- 项目安装方式：默认技能安装在 `项目/.agent/skills/` 目录下，不是默认路径，需要在 skills.paths 中添加项目路径，示例：

```json
{
  "skills": {
    "paths": [
      "./.agent/skills"
    ]
  }
}
```

---

### 七、最佳实践总结

#### 7.1 技能开发最佳实践

1. **单一职责原则**
  - 一个技能只做一件事
  - 避免功能耦合
  - 易于测试和维护

2. **清晰的文档结构**
  - 使用标准化的章节结构
  - 提供最小可运行示例
  - 明确说明输入输出

3. **完善的元数据**
  - 填写完整的 frontmatter
  - 添加版本号便于追踪
  - 声明兼容的 Agent

4. **安全性考虑**
  - 避免在技能中包含敏感信息
  - 明确说明权限需求
  - 提供回滚方案

#### 7.2 技能管理最佳实践

1. **项目级别 vs 全局级别**
  - 项目特定技能 → 项目级别（便于版本控制）
  - 通用技能 → 全局级别（跨项目共享）

2. **符号链接 vs 复制**
  - 开发环境 → 符号链接（便于更新）
  - 生产环境 → 复制（稳定性优先）

3. **版本管理**
  - 定期生成锁文件
  - 提交锁文件到版本控制
  - 更新前备份旧版本

4. **团队协作**
  - 建立团队技能仓库
  - 制定技能命名规范
  - 定期审查和更新技能

#### 7.3 性能优化建议

1. **减少上下文占用**
  - 利用渐进式加载特性
  - 避免在技能中包含大量静态内容
  - 将大型资源放在 assets 目录

2. **提高匹配精度**
  - 使用清晰的 description
  - 在 overview 中包含关键词
  - 避免过于泛化的描述

3. **缓存策略**
  - 频繁使用的技能可考虑复制方式
  - 定期清理未使用的技能
  - 监控技能加载性能

---

### 八、总结与展望

#### 8.1 核心价值

npx skills 为 AI 助手提供了一种标准化的能力扩展机制：

- 🎯 **标准化**：统一的技能格式和管理方式
- 🔄 **可复用**：一次开发，多处使用
- 📦 **轻量化**：按需加载，减少资源消耗
- 🤝 **协作友好**：便于团队共享和治理

#### 8.2 适用人群

- **开发者**：扩展 AI 编程助手的能力
- **团队负责人**：建立团队知识库和最佳实践
- **技术 writer**：将专业知识转化为可执行技能
- **DevOps 工程师**：标准化运维流程和脚本

#### 8.3 未来展望

随着 AI 助手的普及，Skill 生态系统将会：

- 🌱 **生态丰富**：更多开源技能仓库出现
- 🔧 **工具完善**：更好的开发、测试、调试工具
- 🏢 **企业应用**：企业内部技能市场和治理平台
- 🤖 **自动发现**：AI 更智能地推荐和使用技能

#### 8.4 快速开始清单

✅ 安装验证：`npx skills -h`  
✅ 浏览技能：`npx skills find [query]`  
✅ 安装技能：`npx skills add vercel-labs/agent-skills`  
✅ 查看列表：`npx skills list`  
✅ 创建技能：`npx skills init my-skill`  
✅ 生成锁文件：`npx skills generate-lock`

---

**参考资料**：

- Vercel Skills CLI: https://github.com/vercel-labs/skills
- Skills 在线目录: https://www.skills.sh/
- Vercel Agent Skills: https://github.com/vercel-labs/agent-skills
- Anthropic Skills: https://github.com/anthropics/skills
- npx skills 命令行指南：https://01mvp.com/docs/skills/npx-skills-cli

---

> **提示**：本文持续更新，欢迎关注最新版本。
