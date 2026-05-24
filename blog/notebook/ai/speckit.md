<h2 style="color:#4db6ac !important" >AI规范编程：从SDD理念到Spec-Kit落地实践</h2>
> 【原创】2026/05/24

[TOCM]

[TOC]

## 一、SDD 诞生的背景：AI 时代软件工程的范式变革

### 2.1、传统开发范式的痛点

在 AI 编码助手（如 GitHub Copilot、Claude Code、Cursor）普及之前，软件开发遵循 "代码优先、文档次之" 的模式，面临三大核心痛点：


| 痛点             | 	具体表现	                                 | 影响                      |
|----------------|----------------------------------------|-------------------------|
| 意图与实现鸿沟	       | 需求文档模糊、变更频繁，代码与文档长期脱节                  | 	沟通成本高，重构风险大，维护困难       |
| 协作效率低下	        | 团队成员对需求理解不一致，缺乏统一 "事实来源"	              | 重复工作多，冲突频繁，交付周期长        |
| 质量保障滞后	        | 测试在编码后进行，缺陷发现晚，修复成本高	                  | 产品稳定性差，用户体验受损           |

随着 AI 编码工具的普及，这些问题被进一步放大：AI 能快速生成代码，但缺乏对整体系统架构和业务逻辑的理解，容易产生 "局部正确但全局错误" 的代码；同时，开发者过度依赖 AI 提示词，导致代码质量参差不齐，可维护性急剧下降。

### 2.2、SDD 的核心定义与价值

**规范驱动开发** (Spec-Driven Development, SDD) 是生成式 AI 时代下适配工程化开发的新型软件开发方法论，核心是先由技术人员定义简洁、可测试、形式化的系统规格说明 (Spec)，将其作为人、团队与 AI 之间的 "动态契约" 和开发过程的唯一事实来源。

SDD 带来三大革命性转变：
- **权力反转**：将软件开发的 "单一事实来源" 从易变的代码转移到人类意图的直接表达 —— 规范本身
- **流程重构**：从 "编码→测试→修复→文档" 转变为 "规范→设计→实现→验证" 的线性流程
- **人机协同**：规范成为 AI 的 "操作手册"，消除 AI 猜测意图的成本，提升代码生成质量

### 2.3、SDD 的发展历程

SDD 并非全新概念，而是在传统软件工程理论基础上的进化：
- 早期起源：可追溯至 20 世纪 80 年代的 "契约式设计"(Design by Contract) 和 "测试驱动开发"(TDD) 理念
- 现代演进：2025 年，GitHub 发布 Spec-Kit 工具，正式将 SDD 从理论推向实践
- 生态成熟：2026 年，OpenSpec、Superpowers 等工具相继出现，形成完整的 SDD 工具生态
- 行业认可：微软、亚马逊等科技巨头开始在内部推广 SDD 方法论，将其纳入官方培训课程


## 二、SDD 工具对比分析：Spec-Kit、OpenSpec 与 Superpowers

### 2.1 核心定位与设计理念对比

| 对比维度	      | Spec-Kit (GitHub)                | 	OpenSpec	                                 | Superpowers                |
|------------|----------------------------------|--------------------------------------------|----------------------------|
| 核心定位       | 	"蓝图派"：从零开始的完整规划，适合 0-1 项目	      | "园丁派"：现有系统的增量改造，适合 1-n 项目	                 | "全流程管家"：从需求到交付的完整开发流程管理    |
| 设计理念       | 	"规格即法律"(固定规则体系)，强调门控流程和详尽文档	    | "OPSX 灵活工作流"(动作而非阶段)，围绕变更提案，流程简洁	          | "测试优先，证据驱动"，系统化降低复杂度       |
| 哲学基础       | 	深度规范驱动，追求流程完整性和可控性	             | 轻量级规范驱动，追求快速迭代和极简主义	                       | 方法论驱动 (技能系统)，强调心理学引导 AI 行为 |
| 适用团队       | 	组织完善、有严格流程合规的大型团队	              | 敏捷团队、初创企业、需要快速迭代的项目	                       | 独立开发者、质量导向团队、AI 代理密集型项目    |

### 2.2 技术架构与功能特性对比

| 对比维度	        | Spec-Kit (GitHub)                                          | 	OpenSpec	                                   | Superpowers                                 |
|--------------|------------------------------------------------------------|----------------------------------------------|---------------------------------------------|
| 技术栈	         | Python (uv 包管理器)，CLI 驱动，支持多平台	                             | TypeScript (npm)，Web UI+CLI 双模式，轻量级	         | Markdown+JavaScript 插件，编辑器集成，跨平台            |
| 核心流程	        | 7 步线性流程：spec→plan→tasks→implement→verify→document→evolve	  | 3 步增量流程：propose→apply→archive，管理规范增量	        | 5 步闭环流程：brainstorm→isolate→plan→TDD→review  |
| 规范形式	        | 结构化 Markdown，支持复杂场景和约束条件	                                  | 极简 YAML，聚焦变更点，最小化文档量	                        | 自然语言 + 测试用例，强调可执行性和证据验证                     |
| AI 集成	       | 内置支持 15+AI 编码助手，统一接口管理	                                    | 专注 Claude 和 Copilot，强调轻量级集成	                 | 深度集成 GitHub Copilot，支持苏格拉底式对话               |
| 变更管理	        | 动态宪法机制，规范版本化，完整审计追踪	                                       | 变更隔离，共识驱动，风险控制优先	                            | 微任务隔离，自动冲突解决，快速回滚机制                         |
| 学习曲线	        | 陡峭，适合有流程意识的团队	                                             | 平缓，适合快速上手的场景	                                | 中等，需要理解测试驱动理念                               |

### 2.3 选型建议：根据场景选择合适的 SDD 工具

针对不同场景，建议结合项目类型、团队规模、规范复杂度等多维度因素，综合评估选择最适合的 SDD 工具：
- 选择Spec-Kit：适合新项目、企业级项目，强调流程规范和架构控制的团队，尤其是企业级项目和跨团队协作场景。
- 选择OpenSpec：适合遗留系统改造、兼容性要求高的项目，强调快速迭代和风险控制的敏捷团队。
- 选择Superpowers：适合质量优先的团队，强调自动化和测试驱动开发的项目，尤其适合原型开发和AI密集型项目。

| 决策因素	     | 选择 Spec-Kit	     | 选择 OpenSpec	       | 选择 Superpowers      |
|-----------|------------------|--------------------|---------------------|
| 项目类型	     | 全新项目、企业级项目	      | 增量变更、遗留系统	         | 原型开发、AI 密集型         |
| 团队规模	     | 10 人以上、跨团队	      | 3-10 人、敏捷团队	       | 1-3 人、独立开发者         |
| 规范复杂度	    | 高 (需要详细约束)	      | 低 (聚焦变更点)	         | 中 (强调测试用例)          |
| 流程要求	     | 严格 (门控机制)	       | 灵活 (增量流程)	         | 自动化 (全流程)           |
| 学习成本	     | 高 (需要培训)	        | 低 (快速上手)	          | 中 (理解测试驱动)          |


## 三、Spec-Kit 架构深度解读：规范驱动的工程化基石

### 3.1 核心特性：规范驱动的全流程管控

#### 3.1.1 可执行规范 (Executable Specifications)

Spec-Kit 的核心创新在于将规范从静态文档转变为可执行的开发指令：
- 规范采用结构化 Markdown 格式，包含需求、场景、约束、验收标准等要素
- 规范可直接驱动 AI 生成代码、测试用例和文档，无需人工翻译
- 规范变更自动触发代码和测试的更新，确保一致性

#### 3.1.2 门控流程 (Gated Workflow)

Spec-Kit 的核心是‌**七阶段门控开发流程‌**，每一阶段均对应可验证的工程产出，形成从需求到实现的强约束链。各环节严格顺序执行，未经前一阶段验收，不得进入下一阶段。

| 阶段编号 | 阶段名称 | 核心目标 | 产出文件 | 关键约束 |
| --- | --- | --- | --- | --- |
| 1 | Constitution | 建立项目宪法与技术红线 | constitution.md | 定义禁止项、审批项、允许范围；需团队共识签署 |
| 2 | Specify | 将自然语言需求转化为形式化规范 | spec.md | 必须使用结构化语义模板，禁止模糊表述 |
| 3 | Clarify | 消除歧义，确认边界与例外 | clarification.md | 所有模糊点必须由产品/架构师签字确认 |
| 4 | Plan | 设计技术实现路径与架构选型 | plan.md | 必须包含技术栈、数据流、依赖图、风险评估 |
| 5 | Tasks | 拆解为可执行、可追踪的开发任务 | tasks.md | 每项任务需绑定负责人、预估工时、验收标准 |
| 6 | Analyze | 验证规范一致性与变更影响 | 自动化分析报告 | 形式化验证通过率 ≥95%，影响范围必须可视化 |
| 7 | Implement | 由AI生成代码并提交审查 | 生成代码 + 测试用例 | 代码必须与 spec.md 保持双向追溯，禁止手动覆盖 |

>所有阶段均通过 specify CLI 工具驱动，每个环节的交付物自动纳入版本控制，形成‌可审计、可回溯、可验证‌的工程契约链。
>任意环节未通过门控检查，系统将阻断后续流程，确保“规范即权威”。


#### 3.1.3 动态宪法机制 (Dynamic Constitution)

Spec-Kit 引入项目宪法 (Project Constitution) 概念，作为项目的 "最高法律"：
- 宪法定义项目的架构原则、编码标准、安全规范、测试策略等核心规则
- 所有规范和代码必须符合宪法要求，宪法合规检查器自动验证
- 宪法支持版本化管理，变更需要团队共识，确保项目方向的一致性

### 3.2 核心概念：构建规范驱动的思维模型

#### 3.2.1 单一事实来源 (Single Source of Truth) 

Spec-Kit 的核心哲学是规范即唯一事实来源，所有开发活动都围绕规范展开：
- 规范是需求、设计、实现、测试、文档的唯一依据
- 消除代码与文档、设计与实现之间的不一致性
- 团队成员通过规范达成共识，减少沟通成本

#### 3.2.2 意图优先 (Intent-First)

Spec-Kit 强调先明确意图，再考虑实现，与传统 "实现优先" 模式形成鲜明对比：
- 规范阶段专注于业务需求和用户价值，不涉及技术细节
- 计划阶段才将业务意图转换为技术实现方案
- 避免过早陷入技术选型，确保解决方案符合业务目标

#### 3.2.3 人机协同 (Human-AI Collaboration)

Spec-Kit 重新定义了开发者与 AI 的协作模式，从 "AI 辅助编码" 升级为 "规范驱动 AI 开发"：
- 开发者负责定义清晰、精确的规范，AI 负责执行实现细节
- 规范成为开发者控制 AI 行为的 "护栏"，确保代码质量和一致性
- 开发者从繁琐的编码工作中解放出来，专注于架构设计和业务逻辑

### 3.3 技术架构：分层设计与组件化实现

Spec-Kit 采用模块化、可扩展的架构设计，核心分为四层，从底层到上层依次为：

#### 3.3.1 核心引擎层 (Core Engine)

- 规范解析器 (Spec Parser)：解析结构化 Markdown 规范，提取需求、约束、场景等关键信息
- 宪法合规检查器 (Constitutional Compliance Checker)：确保所有规范符合项目宪法 (Constitution) 定义的架构原则和编码标准
- 任务生成器 (Task Generator)：将规范和计划转换为 AI 可执行的原子任务，支持任务依赖和优先级管理
- 验证引擎 (Validation Engine)：执行自动化测试，验证代码实现是否符合规范要求

#### 3.3.2 工具集成层 (Tool Integration Layer)

- AI 代理适配器 (AI Agent Adapter)：提供统一接口，支持 15 + 主流 AI 编码助手，如 Claude Code、GitHub Copilot、Amazon Q 等
- 版本控制集成器 (VCS Integrator)：与 Git 无缝集成，自动管理规范和代码的版本历史，支持分支策略和合并冲突解决
- IDE 插件 (IDE Plugins)：提供 VS Code、IntelliJ 等主流编辑器的集成支持，实时规范检查和提示

#### 3.3.3 命令行界面层 (CLI Layer)

- specify CLI：核心命令行工具，提供 init、spec、plan、tasks、implement 等 7 个核心命令，引导开发流程
- 交互式终端 (Interactive Terminal)：支持自然语言交互，简化规范编写和任务执行流程
- 报告生成器 (Report Generator)：生成规范合规报告、测试覆盖率报告、代码质量报告等

#### 3.3.4 扩展层 (Extension Layer)

- 模板系统 (Template System)：提供规范模板、计划模板、任务模板，支持自定义扩展
- 插件框架 (Plugin Framework)：允许开发者编写自定义插件，扩展 Spec-Kit 功能，如集成特定测试工具、部署流程等
- 自定义规则引擎 (Custom Rule Engine)：支持添加项目特定的合规规则，如安全标准、性能指标等


## 四、Spec-Kit安装

Spec-Kit 是 GitHub 官方开源的‌规格驱动开发（Spec-Driven Development, SDD）‌工具包。 它通过标准化的工作流（Specify → Plan → Tasks → Implement），帮助开发者利用 AI 编码助手（如 claude、opencode 等）构建高质量软件，解决“氛围编码”（Vibe Coding）导致的代码质量不一和流程缺失问题。

- 官网：https://github.github.com/spec-kit/
- Github：https://github.com/github/spec-kit

### Step1：安装核心依赖 uv

Spec-Kit 基于Python 开发，依赖 uv 作为包管理器和工具运行器，因为它比传统的 pip/venv 更快且更易于管理。

```
# 安装 uv
curl -LsSf https://astral.sh/uv/install.sh | sh

# 验证安装
uv --version
```

### Step2：安装 Spec-Kit (Specify CLI)

```
# 使用 uv 安装 Spec-Kit
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git

# 验证安装
specify --version
```

### Step3：初始化项目

初始化现有项目：
```
# 在当前目录初始化
specify init .

# 指定特定的 AI 助手
specify init . --ai opencode
```

创建并初始化新项目:
```
# 基本初始化
specify init project01

# 指定特定的 AI 助手
specify init project01 --ai claude

# 指定脚本类型 (Mac/Linux默认sh，Windows默认ps，也可强制指定)
specify init project01 --script sh
```

‌初始化过程中的交互提示：‌
- ‌选择 AI Agent‌：如果你未在命令行指定 --ai，它会提示你选择已检测到的 AI 工具。
- ‌选择脚本类型‌：Mac/Linux 用户通常选择 sh (Bash)；Windows 用户通常选择 ps (PowerShell)。 
- ‌文件合并‌：如果目录下已有文件，Spec-Kit 会尝试合并或提示覆盖，请根据提示操作。
- 安全配置（重要）：初始化后，AI 助手可能会在项目根目录生成包含 API Key 或敏感信息的文件夹（例如 .opencode/, .claude/ 等）。务必将这些文件夹加入 .gitignore 防止敏感信息泄露：

```
# 编辑 .gitignore 文件，添加以下内容（根据你使用的 AI 工具调整）
.opencode
.omo
.claude
.specify
specs
```

### Step4：验证与开始使用

初始化完成后，打开你的 AI 编码助手（如在终端输入 opencode、claude 启动对应编程工具，或在 VS Code/Cursor 中打开项目）。

检查是否可用以下 Slash Commands（斜杠命令）：

| 命令                          | 功能描述                       | 阶段        |
|-----------------------------|----------------------------|-----------|
| /speckit.constitution [必须]  | 建立项目原则和非谈判性准则              | 0. 准备     |
| /speckit.specify [必须]       | 定义“做什么”和“为什么”，生成规格文档       | 1. 规格化    |
| /speckit.clarify            | 在规划前通过提问澄清模糊需求             | 1.5 澄清    |
| /speckit.plan [必须]          | 基于规格和技术栈生成技术实施计划           | 2. 规划     |
| /speckit.tasks [必须]         | 将计划分解为可执行的、有序的任务列表         | 3. 任务分解   |
| /speckit.analyze            | 分析任务一致性和覆盖率                | 3.5 分析    |
| /speckit.implement [必须]     | 执行任务，生成代码和测试               | 4. 实现     |

‌典型工作流示例：‌
- /speckit.constitution - 设定项目原则（例如：代码质量、测试标准）
- /speckit.specify - 描述你的需求（例如：“创建一个用户注册系统...”）
- /speckit.plan - 制定实施计划
- /speckit.tasks - 生成待办任务
- /speckit.implement - AI编写代码


问题：如何指定Spec文档的语言和风格 ？
   - 打开 .specify/memory/constitution.md。在文件中添加或修改以下规则（建议放在文件顶部或 "Communication" 部分）
```
## Communication Guidelines
- **Language**: All specifications, plans, tasks, code comments, and commit messages MUST be written in **Simplified Chinese (zh-CN); unless explicitly requested otherwise.
- **Tone**: Professional, concise, and direct.
```

## 五、Spec-Kit案例实践

我们以 "[XXL-API](https://github.com/xuxueli/xxl-api) 项目代码重构" 为例，展示 Spec-Kit 的完整实践流程。

本次改造项目为正式开源项目，对代码规范性与质量存在要求；另外，该重构需求涉及 “9个功能模块”、“前后端代码逻辑修改”，累计需要修改 130+ 项目文件，为中型颗粒度需求。本次改造需求存在一定复杂度。

#### Step1：Spec-Kit 初始化

进入项目根目录，执行如下命令生成 Spec-Kit 工程契约链：
```
specify init .
```

执行后，会生成 `.specify` 契约链文件目录：

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260524_01.jpg)

#### Step2：Constitution - 生成项目宪法

执行如下命令生成 “项目宪法”（例如：项目约定、技术栈与约束、开发工作流等），确保后续规范和代码实现的一致性和可控性。
```
# 创建项目宪法
/speckit.constitution 
 
# 创建项目宪法，补充指定中文约束（否则默认生成 英文 内容）
/speckit.constitution 补充1条规则：所有规范文档使用中文描述
```

执行后将会生成 `.specify/memory/constitution.md` 文件，内容如下：

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260524_02.jpg)

#### Step3：Specify - 生成功能规格

执行如下命令 + 填写功能需求描述，生成 “功能规格（Spec）”：

```
/speckit.specify  针对XXL-API项目按照如下要求重构：

1、按照下面项目规范结构，重构重构项目目录结构：

xxl-api-admin/src/main/java/com/xxl/api/admin
- /framework: 基础框架代码，包含公共的 登录、util、过滤器等组件。
- /business：业务代码，包含具体业务模块的 controller、service、model、mapper 子分层代码。
  xxl-api-admin/src/main/resources/templates
- /framework：基础模板，基础框架实体 对应的。
- /business：业务模板，业务领域实体对应的。
  xxl-api-admin/src/main/resources/mapper
- /framework：基础Mapper文件，基础框架实体 对应的。
- /business：业务Mapper文件，业务领域实体对应的。


2、业务代码分类判断逻辑：Java代码部分，当前 com/xxl/api/admin/controller/biz 下除了 UserController 都是 业务领域，保留User相关Java代码不变，其他按照规范调整。模板部分，当前 help.ftl、index.ftl、login.ftl 属于基础框架，其他属于业务领域；Mapper部分，当前 XxlApiUserMapper.xml 属于基础框架，其他属于业务部分。
```

执行后将会生成 `.specify/specs/001-project-structure-refactor/spec.md` 文件，内容如下：

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260524_03.jpg)

#### Step4：Plan - 生成技术规划

执行如下命令，生成 “技术规划”：
```
/speckit.plan
```

执行后将会生成 `.specify/plans/001-project-structure-refactor/plan.md` 文件，内容如下：

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260524_04.jpg)

#### Step4：Tasks - 拆解任务

执行如下命令，生成 “任务清单”：

```
/speckit.tasks
```

执行后将会生成 `.specify/tasks/001-project-structure-refactor/tasks.md` 文件，内容如下：

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260524_05.jpg)

#### Step5：Implement - 生成代码实现

执行如下命令，将会按照拆解任务（tasks.md）进行 “代码生成”：

```
/speckit.implement
```

Agent会按照任务清单（tasks.md）逐个实现，每个任务完成后自动对照`requirements.md`检查。

所有任务完成后，人工进行Review验收，确认符合要求后合并代码（当前该PR已合入 XXL-API master分支，交付符合预期）。

![img](https://www.xuxueli.com/blog/static/images/2026/img_20260524_06.jpg)

### Spec-Kit经验总结

- **项目宪法要保持稳定**：`constitution.md`一旦确定不要频繁修改，确保所有参与者都遵循统一规则
- **规格只写做什么不写怎么做**：`SPEC.md`避免过度约束实现细节，给AI留出技术优化空间
- **不要跳过澄清和检查阶段**：这两个阶段是减少返工的关键，提前发现模糊点和偏差比后期修改成本低很多
