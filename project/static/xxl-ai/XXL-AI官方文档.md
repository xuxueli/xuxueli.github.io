## 《AI应用开发平台XXL-AI》

[![Actions Status](https://github.com/xuxueli/xxl-ai/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-ai/actions)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-ai.svg)](https://github.com/xuxueli/xxl-ai/releases)
[![GitHub stars](https://img.shields.io/github/stars/xuxueli/xxl-ai)](https://github.com/xuxueli/xxl-ai/)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![donate](https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square)](https://www.xuxueli.com/page/donate.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述

XXL-AI 是一个「**云本结合**」的 AI Agent 开发平台，提供两种同源同品牌、能力互补的交付形态，可按团队规模与使用场景独立选用，也可组合使用：

> - **云版**：以 Agent 编排为中枢，标准化扩展「MCP + SKILL + RAG」，配套多供应商、空间隔离、RBAC 权限与流式对话等工程化底座，可快速构建并一键发布 Agent。开源、开箱即用，支持集群部署与生产落地。
> - **本地版**：跨平台桌面客户端：面向个人的本地工作台，围绕本机项目目录提供对话、文件读写、终端命令、文件 / 浏览器侧边任务等能力；安装即用、离线可用，数据仅落本地 SQLite。

| 项 | 云版（Web 服务端） | 本地版（Desk 桌面端） |
|---|---|---|
| 形态 | B/S Web 应用，浏览器访问 | C/S 桌面应用，一键安装（mac / win / linux） |
| 面向 | 团队——多用户、多业务空间（Tenant） | 个人——单用户本地工作台 |
| 定位 | Agent 编排、一键发布、权限治理 | 本地优先，直接操作本机项目与系统能力 |
| 扩展 | MCP + SKILL + RAG，多供应商，流式对话 | 本地文件 / 终端 / 浏览器面板，Plan / Build 模式 |
| 模块 | `xxl-ai-api` + `xxl-ai-ui`（+ `xxl-ai-sample`） | `xxl-ai-desk` |
| 依赖 | MySQL + Redis + Milvus | 零依赖（仅本地 SQLite） |


### 1.2 特性

#### 1.2.1 云版

- **Agent 编排与一键发布（核心亮点）**

- 1、Agent 编排：模型 + 系统指令 + 知识库 + MCP + SKILL 组合为 Agent，各类资源均支持多选绑定；
- 2、一键发布：发布后生成 UUID 公开访问地址，管理端可查看该 Agent 的访客对话与消息记录；
- 3、流式对话：SSE 流式输出（思考过程 / 回复内容），基于 Redis Stream 无状态化，支持集群部署与断线 / 刷新续传；

- **MCP + SKILL + RAG，让 Agent 真能干活（标准化扩展）**

- 4、RAG 知识库：知识库 + 文档管理，文档分片向量化入库（Milvus），对话时检索上下文自动注入；
- 5、MCP 工具：支持远程（Streamable HTTP）与本地（stdio）MCP 服务接入，工具自动装配给 Agent，另附示例 MCP 服务 `xxl-ai-sample`；
- 6、SKILL 技能：以 `SKILL.md` + 文件树沉淀领域知识与脚本，自动物化为 Agent 可执行的技能目录；

- **多供应商与工程化底座，支持稳定上线**

- 7、多模型供应商：统一接入 OpenAI 兼容协议（Deepseek、智谱GLM、Ollama、OpenCode 等），支持供应商与模型两级管理、连通性测试与远程模型导入（区分对话 / 嵌入模型）；
- 8、空间隔离：多业务空间（Tenant）隔离数据，用户按空间授权，管理端与公开端共享权限体系；
- 9、账号安全：基于 XXL-SSO 登录认证，登录态（token）存于 Redis，支持集群部署与 SSO 集成；
- 10、权限管控：基于 RBAC 的菜单 / 按钮级权限，动态菜单下发、零路由改动；
- 11、系统管理：用户、系统配置、审计日志在线管理；
- 12、一键部署：随带 Docker Compose 一键部署栈（mysql + redis + milvus + api + sample）；

- **研发与架构**

- 13、Monorepo + 前后端分离：一套仓库统一托管 后端 API 与 前端 UI，统一版本与依赖管理；开发期前后端独立启动，部署期前端产物内嵌进 API jar 单包单端口发布；
- 14、AI + SKILL 驱动：内置开发 SKILL，AI 编程助手一键加载，按平台规范直生业务代码并落位，显著加速业务开发；
- 15、响应式 UI 与国际化：Vue3 + Element Plus + TypeScript，提供中文 / 英文两种语言；
- 16、可扩展架构：标准分层分包、业务模块自包含，模型 / 对话 / RAG / MCP / SKILL 运行时统一收口支撑层；

#### 1.2.2 本地版

- **本地优先的桌面 Agent 工作台（核心亮点）**

- 1、跨平台一键安装：基于 Electron + Vue3 + TypeScript 构建，支持 mac / win / linux，开箱即用、离线可用；
- 2、项目即工作区：项目 1:1 强绑定本地磁盘目录，会话归属项目，Agent 直接在本机工程上干活；
- 3、本地数据自治：单文件 SQLite（better-sqlite3 + Drizzle），数据不出本机，运行时数据目录可自定义；

- **Plan / Build + 本地系统能力，让 Agent 真能干活（亮点）**

- 4、Plan / Build 双模式：Plan 只读安全探索、Build 全量读写落地改造，随会话持久化，默认 Build；
- 5、项目目录沙箱：文件操作限定当前项目目录，越界弹原生对话框「允许本次 / 本会话允许 / 拒绝」，支持会话级白名单；
- 6、直达工作现场：终端面板（node-pty + xterm）、文件面板（目录树 + 编辑 + 预览，跟随本地变更）、浏览器面板（多标签 `webview`）；

- **执行过程全程可视（亮点）**

- 7、时间线渲染：助手消息按**片段发生顺序**交错呈现思考 → 工具 → 正文，不再是「思考堆一起、结果一次性吐出」；
- 8、工具调用可视化：独立成行展示动作 / 参数 / 状态 / 实时耗时，可展开入参与结果；
- 9、流式对话：思考过程折叠、Markdown 实时渲染、GitHub 风格代码高亮（语言标签 + 复制）；

- **多供应商接入与工程化底座**

- 10、多供应商模型：兼容 OpenAI 协议（OpenCodeGo / Ollama / Deepseek / 智谱GLM 等），支持 `{session}` 请求头占位，首次启动预置四个供应商；
- 11、运行时进程隔离：Pi（`pi-ai` + `pi-agent-core`）运行于独立 `utilityProcess`，主进程仅做 IPC 网关与越界审批，长会话 / 工具重活不阻塞 UI；
- 12、个性化与国际化：应用名称 / Slogan / 自定义指令，浅色（默认）/ 深色主题，中 / 英双语。

### 1.3 下载

#### 文档地址

- [中文文档](https://www.xuxueli.com/xxl-ai/)

#### 源码仓库

| 源码仓库地址                                                                     | Release Download                                            |
|----------------------------------------------------------------------------------|-------------------------------------------------------------|
| [https://github.com/xuxueli/xxl-ai](https://github.com/xuxueli/xxl-ai)       | [Download](https://github.com/xuxueli/xxl-ai/releases)    |

#### 技术交流
- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.4 环境

- Maven：3+
- Jdk：17+
- Mysql：8.0+
- Redis：7.0+
- NodeJs：22+
- Milvus：2.6+（可选：RAG 知识库向量化需要）

### 1.5 发展历程

于2026年6月，整合 XXL-BOOT 中的AI插件模块，升级为独立的 AI应用开发平台 XXL-AI。

于2026年9月，发布 1.0.0 版本，提供 Agent 编排、RAG 知识库、MCP 工具、SKILL 技能等核心功能，支持一键发布与流式对话。


## 二、快速入门

### 2.1 云版

#### 2.1.1 环境准备

- 后端：JDK 17+、Maven 3+、MySQL 8.0+、Redis 7.0+（RAG 向量化另需 Milvus 2.6+）；
- 前端：Node.js 22+

#### 2.1.2 初始化数据库

下载项目源码并解压，获取 "数据库初始化SQL脚本" 并执行即可。数据库初始化SQL脚本 位置为:

```
/doc/db/
    - tables_xxl_ai.sql      ：数据库初始化SQL脚本
```

#### 2.1.3 源码编译

项目为 Monorepo 仓库，后端服务 与 前端工程 维护在同一个代码仓库中，通过不同目录模块隔离维护。解压源码，按 Maven 格式将源码导入 IDE，使用 Maven 编译即可，源码结构如下：

```
- xxl-ai/
    - xxl-ai-api              ：【前后端分离】后端API服务
    - xxl-ai-ui               ：【前后端分离】前端UI服务
    - xxl-ai-sample           ：示例 MCP 服务（可选）
```

#### 2.1.4 方式一：本地开发（前后端分离）

- 运行形态：前后端分开启动——后端 `xxl-ai-api`（8080）提供 API，前端 `xxl-ai-ui`（3000）提供页面，浏览器访问前端 `http://localhost:3000`。
- 项目说明：前端 `/api` 请求由 Vite 开发代理转发至后端 `8080`；本地开发**不涉及前端产物内嵌**（无需 `sync:dist`），内嵌单包部署见「2.5 方式二」「2.6 方式三」。

##### 步骤一：启动后端服务

后端配置文件地址：

```
/xxl-ai/xxl-ai-api/src/main/resources/application.properties
```

配置内容说明（数据库配置，与 ”2.2 初始化数据库“ 章节初始化的数据库保持一致）：

```
### xxl-ai, datasource。 数据库配置
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_ai?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=root_pwd
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

### xxl-ai, redis。 缓存配置
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.database=0
spring.data.redis.password=

### xxl-ai, milvus。向量数据库配置
xxl-ai.milvus.uri=http://127.0.0.1:19530
xxl-ai.milvus.username=
xxl-ai.milvus.password=
xxl-ai.milvus.database=default
```

补充说明：
- 后端服务默认端口为 `8080`，可通过 `server.port` 调整；
- 前后端分离项目依赖 Redis，部署前需确保 Redis 服务可用。

后端启动方式：

```
# 编译后端服务（本地开发无需前端产物）
cd /xxl-ai/xxl-ai-api
mvn clean package -Dmaven.test.skip=true

# 启动后服务
mvn spring-boot:run    
```

##### 步骤二：前端环境配置

配置文件地址（按环境区分，位于前端工程根目录）：

```
/xxl-ai/xxl-ai-ui/.env.development   # 开发环境
/xxl-ai/xxl-ai-ui/.env.staging       # 预发布环境
/xxl-ai/xxl-ai-ui/.env.production    # 生产环境
```

配置内容说明（以开发环境 `.env.development` 为例，`.env.production` 见下方说明）：

```
# 前端端口号
VITE_APP_PORT=3000

# 后端API地址（仅开发模式代理目标）
VITE_API_URL=http://localhost:8080
# 后端路由前缀（开发：/api，由 Vite 代理剥离后转发；生产：为空）
VITE_APP_BASE_API='/api'
```

补充说明：
- `VITE_API_URL`：后端 API 服务地址，仅开发模式下由 Vite 代理转发；
- `VITE_APP_BASE_API`：后端路由前缀。开发环境为 `/api`，Vite 代理时剥离后转发至后端；生产环境为空字符串，前端与 API 同源、接口直接走根路径（拍平）；
- 前端采用 **Hash 路由**（`/#/xxx`），hash 段不发送至服务端，故无需服务端 History 回退配置。

##### 步骤三：启动前端项目

开发模式下，进入前端目录，安装依赖并启动即可：

```
# 进入前端目录，安装依赖
cd /xxl-ai/xxl-ai-ui
npm install

# 启动开发服务器（开发期，3000）
npm run dev
```

启动后访问 `http://localhost:3000`，开发服务器会将 `/api` 前缀的请求自动代理至 `VITE_API_URL` 指定的后端服务。


#### 2.1.5 方式二：人工部署（生产部署）

部署期无需单独部署前端：先执行 `npm run build` 构建前端，再执行 `npm run sync:dist`（清理 `xxl-ai-api` 旧静态资源并复制最新产物），然后打包 API jar，`java -jar` 启动即同时提供页面与接口（无需 Nginx，也无需服务端 History 回退）：

```
# 1、构建前端并同步产物到后端静态资源目录
cd xxl-ai-ui && npm install && npm run build && npm run sync:dist && cd ..

# 2、打包含前端的内嵌 jar
mvn clean package

# 3、启动服务（页面与接口同在 8080）
java -jar xxl-ai-api/target/xxl-ai-api-*.jar
```

项目部署完成后，可通过如下地址及账号进行登录。
- 访问地址：http://localhost:8080 （按实际部署配置调整）
- 默认登录账号："admin/123456"

#### 2.1.6 方式三：Docker Compose 部署（生产部署）

支持 Docker Compose 一键部署（api 镜像直接打包"已内嵌前端"的 jar，页面与接口同在 8080）：

```
# 第一步：代码clone本部 + 前往仓库目录
git clone https://github.com/xuxueli/xxl-ai.git
cd ./xxl-ai

# 第二步：构建前端并同步产物（npm run build 构建 dist，npm run sync:dist 复制到 xxl-ai-api 静态资源目录）
cd xxl-ai-ui && npm install && npm run build && npm run sync:dist && cd ..

# 第三步：构建后端
mvn clean package

# 第四步：进入 docker 目录（支持自定义 .env 配置，如修改 MYSQL_PATH 配置设置 Mysql 数据持久化目录）
cd ./docker/
cat .env

# 第五步：启动/停止项目
docker compose up -d
docker compose down
```

### 2.2 本地版

#### 2.2.1 安装依赖

```bash
cd xxl-ai-desk
npm install                                   # 一次性完成：postinstall 自动下载 Electron 二进制 + 重建原生模块
```

补充说明：
- `postinstall` 已串行执行 `install-electron`（下载 Electron 二进制并写入 `node_modules/electron/path.txt`）、`electron-builder install-app-deps`（重建 better-sqlite3 / node-pty）与 `scripts/fix-node-pty-perms.cjs`。
- 仅当使用 `--ignore-scripts` 安装、或下载失败（`npm run dev` 报 `Electron uninstall`）时，才需补执行：

```bash
npx install-electron                          # 补下载 Electron 二进制（等价于 node node_modules/electron/install.js）
npx electron-builder install-app-deps         # 补重建原生模块
```

#### 2.2.2 开发与构建

```bash
npm run dev                                   # 开发模式（HMR）
npm run type-check                            # 主进程 + 渲染进程类型检查
npm run build                                 # 类型检查 + 三进程构建
npm run pack                                  # 免安装打包（当前平台）

npm run build:mac                             # 打包对应平台安装包
npm run build:win
npm run build:linux
```

客户端启动顺序：`app.whenReady → 初始化数据库 → 预置供应商 → 注册 IPC → 创建窗口`；单实例锁防止多开争库。

#### 2.2.3 首次使用

1. 首次启动自动预置 **OpenCodeGo / Ollama / Deepseek / 智谱GLM** 四个供应商（与云版种子一致），并默认选中首个；
2. 进入 **设置 → 模型供应商** 新增或修改供应商（详见 3.2）；
3. 在 **设置 → 常规** 配置主题 / 语言，在 **设置 → 个性化** 配置名称 / Slogan / 自定义指令；
4. 新建项目并选择本地目录，回到对话页即可开始聊天。

## 三、操作指南

### 3.1 云版操作指南

> 本章以「从零跑通一个可对话的 Agent」为主线，按 **登录 → 空间 → 供应商/模型 → MCP / SKILL / 知识库 → Agent 编排 → 发布对话** 的顺序，结合界面截图逐步说明。所有管理操作均在管理端完成（开发 `http://localhost:3000`；合并部署 `http://localhost:8080`），默认账号 `admin/123456`。

#### 3.1 操作总览


| 步骤 | 菜单入口 | 操作目标 | 关键产出 |
|---|---|---|---|
| 1 | 登录页 | 登录管理端 | 进入工作台 |
| 2 | 系统管理 → 业务空间 / 用户管理 | 划分空间并授权用户 | 空间隔离生效 |
| 3 | 供应商模型 → 供应商 / 模型 | 接入 OpenAI 兼容供应商 | 可用对话 / 嵌入模型 |
| 4 | MCP工具 | 接入远程 / 本地 MCP 服务 | 可被 Agent 调用的工具 |
| 5 | SKILL技能 | 沉淀领域知识与脚本 | 可被 Agent 执行的技能 |
| 6 | RAG知识库 | 文档向量化入库 | 可检索的知识上下文 |
| 7 | Agent管理 | 编排并发布 Agent | 公开访问 UUID |
| 8 | `/#/chat/{uuid}` | 访客免登录对话 | 流式问答 |

> 依赖关系：步骤 3 的「对话模型」是步骤 7 的必选项，步骤 3 的「嵌入模型」是步骤 6 向量化的前提；步骤 4 / 5 / 6 产出的 MCP / SKILL / 知识库可在步骤 7 中按需多选绑定。**最小可用路径**为「步骤 1 → 3 → 7 → 8」。

#### 3.2 登录与工作台

- 打开管理端，输入账号 / 密码与验证码完成登录；验证码开关由系统配置 `system.login.captcha.enabled` 控制。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_01.png "登录页：账号 / 密码 / 验证码")

- 登录态基于 XXL-SSO 存储于 Redis（key 前缀 `xxl_sso_user:`），支持集群部署共享；登录后默认进入**工作台**（首页）。
- 工作台聚合 Agent 数量、SKILL 数量、MCP 数量、供应商模型数等关键指标，并提供 Agent 会话消息趋势 / 占比图表，便于快速掌握平台资源与用量。
- 顶部导航提供 **空间切换器** 与主题 / 语言等全局设置：管理员可见全部空间，普通用户仅见已授权空间；切换空间后前端请求自动携带 `xxl-space-id` 请求头，业务数据按空间隔离。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_02.png "工作台：资源统计与会话趋势")

#### 3.3 业务空间与用户管理

- 「系统管理 → 业务空间」维护多业务空间（Tenant）。新增时填写空间名称、空间编码、状态与备注；系统内置 `默认空间（default）`。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_03.png "业务空间管理：默认空间")

- 「系统管理 → 用户管理」维护用户账号，可新增 / 修改 / 启停用户，并通过「更多」为用户分配可见空间；普通用户仅能访问已授权空间的数据。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_04.png "用户管理：账号与状态")

#### 3.4 配置供应商与模型

- 「供应商模型 → 供应商」新增供应商，填写：供应商名称、接口地址（OpenAI 兼容 `base_url`）、API 密钥，以及可选的自定义请求 Header（value 支持 `{session}` 占位，用于按会话透传）。列表支持搜索 / 修改 / 删除，勾选记录后可执行「连通测试」。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_05.png "供应商管理：OpenCodeGo / Ollama / Deepseek / 智谱GLM")

- 进入供应商的「模型」（或「供应商模型 → 模型」）查看 / 维护其下模型：可点击「自动导入」拉取远程模型列表批量导入，也可「新增」手工配置。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_06.png "模型管理：对话模型列表（含自动导入）")

- 在弹出的「选择要导入的模型」中输入模型标识过滤、勾选目标模型后「确定」；已导入的模型会标记「已导入」，避免重复导入。
- 模型类型区分 **对话模型 / 嵌入模型**：对话模型用于 Agent 对话，嵌入模型用于知识库向量化。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_07.png "导入模型：勾选远程模型（已导入标记）")

#### 3.5 接入 MCP 工具

- 「MCP工具」新增 MCP 服务：
  - **远程**：协议类型选「远程」（Streamable HTTP），填写服务地址 URL 与 Headers；
  - **本地**：协议类型选「本地」（stdio），填写 `command / args / env / cwd`。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_08.png "MCP工具管理：远程示例 + 本地 Fetch / Filesystem")

- 保存后点击「测试连接」发起连通性测试，可查看服务名称、版本、可用工具数量、耗时及工具清单（名称 / 标题 / 介绍）；测试通过的工具即可在 Agent 中绑定启用。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_09.png "MCP 连通性测试：发现可用工具清单")

#### 3.6 编写 SKILL 技能

- 「SKILL技能」新增技能，填写名称与描述；系统自动播种固定骨架 `SKILL.md` + `scripts/` + `reference/`。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_10.png "SKILL技能管理：技能列表与内容管理入口")

- 进入「内容管理」，以 **文件树 + Markdown 编辑器** 维护技能内容，支持新增目录 / 文件、重命名、移动、编辑与预览。
- `SKILL.md` 与 `scripts/`、`reference/` 为固定节点（列表中标记「固定」），禁止删除 / 改名 / 移动；其余内容可自由扩展。技能内容变更会刷新更新时间，Agent 下次使用时自动重新物化到本地技能目录。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_11.png "SKILL内容管理：文件树 + SKILL.md 编辑")

#### 3.7 创建 RAG 知识库

- 「RAG知识库」新增知识库：选择向量化供应商与嵌入模型，设置分片大小 / 重叠 / 检索数量等参数。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_12.png "RAG知识库管理：知识库列表")

- 进入知识库的「文档管理」，可「新增」（粘贴文本）或「上传文档」（txt / md）；勾选文档后执行「向量化」或「全库向量化」写入 Milvus，状态变为「已向量化」，并展示分片数。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_13.png "知识文档管理：已向量化文档与分片数")

- 点击「向量检索」进行检索测试：输入检索内容，返回命中文档、相似度与命中内容，用于验证分片与召回效果。向量化后的知识库可在 Agent 中绑定，对话时自动检索并注入上下文。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_14.png "向量检索：返回相似度与命中内容")

#### 3.8 创建并发布 Agent

- 「Agent管理」新增 Agent，填写 Agent 名称与介绍；列表展示模型供应商、发布状态与访问 URL。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_15.png "Agent管理：Agent 列表与发布开关")

- 在编辑弹窗中完成 Agent 编排：
  - **系统指令**：定义 Agent 的角色、语气与行为约束；
  - **模型供应商 / 对话模型**：选择 Agent 使用的对话模型（必选）；
  - **关联知识库 / 关联 MCP / 关联 Skill**：均支持多选绑定，运行时自动装配对应的 RAG、工具与技能。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_16.png "Agent 编排：指令 + 模型 + 知识库 / MCP / SKILL 关联")

- 保存后回到列表，打开「发布」开关：系统生成访问 UUID，发布后方可公开访问（未发布 / 停用的 Agent 不可公开访问）。管理端可查看该 Agent 的访客对话与消息记录。

#### 3.9 公开端对话

- 点击「前往Agent」或直接访问公开地址 `/#/chat/{uuid}`（免登录），页面自动创建 / 切换会话；输入问题即时流式返回，支持「新建对话」与历史会话切换。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_17.png "Agent 发布页：免登录对话入口")

- 对话支持 **思考过程折叠展示**（「深度思考」可展开 / 收起）与 **Markdown 实时渲染**；下图为 Agent 自我介绍示例。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_18.png "对话实例：Agent 自我介绍")

- 下图为 Agent 调用 MCP 工具（网页抓取）汇总「今日热点社会新闻」的多工具协作示例。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/img_19.png "对话实例：多工具协作汇总热点新闻")

- 生成中刷新页面或网络中断会自动**断点续传**，不丢失已生成内容。

### 3.2 本地版操作指南

> 本章按 **供应商 → 项目 → 对话 → 侧边任务 → 设置** 的顺序说明本地版使用方式。

#### 3.2.1 界面总览

- 左侧**会话栏**（可折叠为窄图标栏）：项目分组 + 会话列表，底部为新建对话、设置、折叠 / 展开；
- 右侧**内容区**：顶栏（标题 + 模型切换）、消息流、输入框（Composer）；
- 右侧**侧边任务面板**：文件 / 浏览器面板，可放大占满正文区、拖拽宽度；
- 对话页右下可展开**终端面板**。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_01.jpg)

#### 3.2.2 配置模型供应商

进入 **设置 → 模型供应商** 新增 / 修改供应商：

- 名称：如 `DeepSeek`；
- 接口地址：OpenAI 兼容端点（如 `https://api.deepseek.com/v1`，裸地址自动补 `/v1`）；
- API Key：本地存储；
- 请求 Header：可选 JSON 对象，value 支持 `{session}` 占位符，对话时替换为当前会话 ID（如 OpenCode 使用 `{"x-opencode-session":"{session}"}`）；
- 模型列表：每行一个模型 ID，如 `deepseek-v4-flash`。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_02.jpg)

#### 3.2.3 项目与会话

- 侧栏「项目」区维护本地项目：项目 1:1 强绑定本地磁盘目录，可新建（目录选择）、重命名、删除（级联删除会话）、在文件管理器中显示；
- 会话必须归属某项目，新建对话需先选择项目；侧栏按项目分组会话，支持搜索、重命名、删除与排序；
- 首条消息即时生成会话标题；生成中切换会话再切回不丢内容（会话级生成缓存）。

#### 3.2.4 对话与模式

- 输入框左侧提供 **Plan / Build 模式选择器**（随会话持久化，默认 Build）：
  - **Plan（只读）**：仅装配只读工具（`read_file` / `list_directory` / `glob_files` / `search_files` / `get_current_time`）；
  - **Build（读写）**：在只读基础上追加 `write_file` / `edit_file` / `run_command`；
- 文件工具统一限定当前项目目录，越界操作弹原生对话框，可选「允许本次 / 本会话允许 / 拒绝」；
- 助手消息按**片段发生顺序**渲染时间线（思考 → 工具 → 正文交错），思考运行中实时展开、完成后收起；工具调用独立成行，展示动作、参数、状态与耗时，点击可展开入参 / 结果；
- 代码块为 GitHub 风格（语言标签 + 复制），接入语法高亮。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_03.jpg)

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_04.jpg)

#### 3.2.5 侧边栏

- **侧边栏工具菜单**：工具菜单含 **文件** 与 **浏览器** 两个面板，支持放大占满正文区、拖拽宽度；

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_05.jpg)

- **文件面板**：项目目录树（懒加载 / 过滤）+ 代码编辑（行号、高亮、`⌘/Ctrl+S` 保存）+ 预览，随本地文件变更自动刷新，支持「打开所在文件夹 / 用指定应用打开」；

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_06.jpg)

- **浏览器面板**：内嵌 `webview`，多标签、前进 / 后退 / 刷新、地址栏搜索兜底、系统浏览器打开；

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_07.jpg)

#### 3.2.6 终端

- **终端面板**：基于 node-pty + xterm，支持终端命令行操作。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_08.jpg)

#### 3.2.7 设置与运行数据

- **设置 → 常规**：主题、语言即时生效；**设置 → 个性化**：配置应用名称、Slogan、自定义指令（作为新对话系统指令）；
- **数据目录**：默认位于系统 userData 下的 `xxl-ai-desk.sqlite`，可在设置中查看 / 浏览 / 打开 / 修改，修改后重启生效；
- 会话 / 消息 / 供应商 / 项目 / 设置均落本地 SQLite，API Key 本地存储。

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_09.jpg)

![图片](https://www.xuxueli.com/project/static/xxl-ai/images/desk_10.jpg)

## 四、云版 vs 本地版

XXL-AI 提供「**云本结合**」两种交付形态，二者同源同品牌、能力互补，可按团队规模与使用场景独立选用，也可组合使用：

- **云版（Web 服务端）**：由 `xxl-ai-api` + `xxl-ai-ui` 组成，B/S 架构、浏览器访问，面向团队的多用户 AI Agent 平台，强调统一管理、权限治理与一键发布；
- **本地版（Desk 桌面端）**：`xxl-ai-desk`，基于 Electron + Vue3 + Pi 的跨平台桌面客户端，本地优先、开箱即用，面向个人的本地工作台，强调对本机项目与系统能力的直接操作。

> 命名说明：官方文档中「云版」对应 Web/服务端版本（部署在服务器、浏览器访问），「本地版」对应 Desk 桌面客户端版本（安装在本机）。两版命名以本章为准，后文统一称 **云版** 与 **本地版（Desk）**。

### 4.1 版本定位一览

| 项 | 云版（Web 服务端） | 本地版（Desk 桌面端） |
|---|---|---|
| 仓库模块 | `xxl-ai-api` + `xxl-ai-ui`（Monorepo，含 `xxl-ai-sample`） | `xxl-ai-desk`（独立子工程） |
| 产品形态 | B/S Web 应用，浏览器访问（开发 `:3000`，部署 `:8080` 单包） | C/S 桌面应用，Electron 一键安装（mac / win / linux） |
| 运行位置 | 服务器（集群可水平扩展） | 用户本机（单机本地优先） |
| 服务对象 | 多用户 / 多业务空间（Tenant）团队 | 单用户本地工作台 |
| 与其它模块关系 | 依赖 MySQL / Redis / Milvus | **零依赖**：不连 api / ui / sample 及其 MySQL / Redis / Milvus |

### 4.2 特性对比

| 能力维度 | 云版（Web 服务端） | 本地版（Desk 桌面端） |
|---|---|---|
| Agent 编排与发布 | 模型 + 系统指令 + 知识库 + MCP + SKILL 组合，一键发布生成免登录公开地址 `/#/chat/{uuid}`，管理端可查看访客对话与消息记录 | 以「项目」为中心的本地 Agent：按项目绑定本地目录，会话归属项目，支持个性化（名称 / Slogan / 自定义指令） |
| 多模型供应商 | 供应商 + 模型两级管理，连通性测试、远程模型自动导入，区分对话 / 嵌入模型 | 供应商 + 模型本地配置，首次启动预置 OpenCodeGo / Ollama / Deepseek / 智谱GLM，支持 `{session}` 请求头占位 |
| 流式对话 | SSE + Redis Stream 无状态化，支持集群部署与断线 / 刷新续传，思考折叠 + Markdown 渲染 | 主进程 / 运行时进程 IPC 事件流，思考 / 工具 / 正文按发生顺序时间线渲染，思考折叠 + 代码高亮 |
| MCP 工具 | 远程（Streamable HTTP）与本地（stdio）在线管理、连通测试，运行时自动装配 | / |
| SKILL 技能 | 在线管理 `SKILL.md` + 文件树，自动物化为可执行技能目录 | / |
| RAG 知识库 | 知识库 + 文档管理，分片向量化入 Milvus，对话自动检索注入 | / |
| 本地系统能力 | 不直接操作本机文件 / 终端 / 浏览器 | **核心差异**：本地文件读写与检索、终端命令（node-pty + xterm）、文件面板（编辑 + 保存 + 跟随本地变更）、浏览器面板（多标签 `webview`） |
| 读 / 写安全控制 | 空间归属 + RBAC + 审计日志 | **Plan / Build 模式**：Plan 只读（read/list/glob/grep/时间），Build 全量读写；文件操作限定当前项目目录，越界弹原生对话框「允许本次 / 本会话允许 / 拒绝」 |
| 会话与上下文 | 会话 / 消息落 MySQL，按空间隔离；公开端以 `uuid` + `visitorId` 隔离 | 会话 / 消息落本地 SQLite，1:1 归属项目；生成中会话内存缓存，切换不丢流式增量 |
| 数据存储 | MySQL（业务 + 平台表）、Redis（登录态 + 对话流）、Milvus（向量） | 单文件 SQLite（better-sqlite3 + Drizzle），运行时数据目录可自定义 |
| 登录与权限 | XXL-SSO 登录（Redis 存储登录态）、RBAC 菜单 / 按钮权限、动态菜单零路由改动 | 无登录 / 无角色体系，纯本地应用 |
| 多语言与主题 | Vue3 + Element Plus + TypeScript，中 / 英 i18n，动态菜单 | Vue3 + Element Plus + TypeScript，中 / 英 i18n，浅色（默认）/ 深色主题 |
| 部署与运维 | 前后端分离开发、前端内嵌单包部署、Docker Compose 一键部署 | electron-builder 三平台打包（dmg / nsis / AppImage 等），免安装可 `pack` |
| 内核技术栈 | SpringBoot + MyBatis + spring-ai + XXL-SSO + Redis Stream | Electron + Vue3 + **Pi**（`pi-ai` + `pi-agent-core`，独立 `utilityProcess` 运行时） |

### 4.3 核心差异剖析

1. **架构定位差异**：云版是「平台」——多租户、多用户、多空间，资源（供应商 / 知识库 / MCP / SKILL / Agent）集中管理、统一发布；本地版是「客户端」——单机单用户、本地优先，围绕本机项目目录工作。
2. **数据与部署差异**：云版依赖 MySQL + Redis + Milvus 三件套，支持集群扩展与高可用；本地版仅需一个 SQLite 单文件，零服务依赖，安装即用、离线可用。
3. **运行时差异**：云版基于 spring-ai `ChatClient`，对话生成 worker 经 Redis Stream 解耦，支持无状态转发与断点续传；本地版基于 Pi Agent 运行时，独立 `utilityProcess` 承载模型调用与工具执行，主进程只做 IPC 网关与越界审批，保证流式与 UI 顺滑。
4. **能力侧重差异**：云版强在「资源编排 + 一键发布 + 权限治理 + 团队协作」；本地版强在「本地系统操作」——直接读写项目文件、执行终端命令、内嵌浏览器 / 文件编辑器，是真正落地到编码场景的本地 Agent。
5. **安全控制差异**：云版以空间 + RBAC + 审计做边界；本地版以项目目录沙箱 + Plan/Build 模式 + 越界人工审批做边界（Plan 只读、Build 读写，默认 Build）。

### 4.4 使用场景与选型建议

| 场景 | 推荐版本 | 理由 |
|---|---|---|
| 企业内部统一建设 AI 应用平台，多团队 / 多业务线 | **云版** | 多空间隔离、RBAC 权限、集中管理供应商与知识、审计日志 |
| 将 Agent 作为对外服务发布给访客或第三方系统 | **云版** | 一键发布 UUID 公开地址，管理端可观测访客对话（v1.2.0 规划 OpenAPI） |
| 需要集群部署、高并发、断线续传的生产级部署 | **云版** | SSE 无状态化 + Redis Stream，任一节点可服务任一连接 |
| 个人开发者本地写代码、改项目、跑命令 | **本地版（Desk）** | 项目目录沙箱 + 终端 + 文件面板，直接在本地工程上干活 |
| 隐私 / 离线 / 数据不出本机的场景 | **本地版（Desk）** | 零服务依赖、数据仅存本地 SQLite，不连外部数据库 |
| 想要开箱即用的桌面 Agent 体验（类 ChatGPT Desktop） | **本地版（Desk）** | 跨平台一键安装，预置多供应商，快速开始对话 |

> 组合使用建议：团队用**云版**做统一平台与对外发布，个人上手**本地版（Desk）**做本地编码工作台；两版各自独立、互不依赖，可分别安装使用。

## 五、总体设计（云版）

### 5.1、Monorepo 仓库

项目采用 Monorepo 仓库模式，将 后端服务 与 前端工程 维护在同一个代码仓库中，通过不同目录模块隔离维护，统一版本管理与依赖管理，便于协同开发与一键构建。

- 后端统一通过 Maven 父工程管理，根目录 `pom.xml` 集中维护模块依赖版本（如 SpringBoot、Mybatis、MySQL、XXL-SSO 等），子模块 `xxl-ai-api` 继承使用；
- 前端模块 `xxl-ai-ui` 独立通过 npm 管理依赖（Vue3/Vite/ElementPlus/TypeScript），与后端 Maven 工程解耦；
- `doc/db` 集中管理数据库初始化脚本（建库 + 全量框架表 + 种子数据单文件初始化）；
- `.agents/skills` 集中管理 开发 SKILL（xxl-ai），为 AI 辅助开发提供平台级规范。

仓库目录结构如下：

```
xxl-ai/
│
├── pom.xml                                    # 父工程Maven配置：统一管理模块及依赖版本
├── README.md                                  # 项目说明与快速开始
├── AGENTS.md                                  # 开发规范与 Skill 使用指南
├── .agents/skills/                            # 【AI 开发 SKILL 目录】
│   └── xxl-ai/SKILL.md                        # 开发 Skill（name: xxl-ai）
├── xxl-ai-spec/                               # 需求落盘目录（plan.md + 建表/初始化 SQL）
│
├── doc/                                       # 文档目录
│   ├── db/                                    # 数据库脚本目录
│   │   ├── tables_xxl_ai.sql                  # 建库 + 框架表 + 业务表 + 种子数据【必须】
│   └── XXL-AI官方文档.md                      # 官方文档
│
├── docker/                                    # Docker Compose 编排目录（mysql + redis + milvus + api(内嵌前端) + sample）
│   ├── docker-compose.yml                     # 一键部署编排
│   └── .env                                   # 部署环境变量
│
├── xxl-ai-api/                              # 后端API服务（8080；部署期内嵌前端产物，单包单端口）
│   ├── pom.xml                                # Maven配置（继承父工程；前端产物经 xxl-ai-ui 的 sync:dist 同步内嵌）
│   ├── Dockerfile                             # 容器构建配置
│   └── src/main/
│       ├── java/com/xxl/ai/api/
│       │   ├── XxlAiApiApplication.java       # 启动类
│       │   ├── framework/                     # 平台内置：系统管理、登录鉴权、审计日志、工具组件等
│       │   └── business/                      # 业务扩展包：space/supplier/knowledge/mcp/skill/agent/chat
│       │       └── harness/                   # 运行时支撑层：llm/chat/rag/mcp/skill/supplier（无 controller）
│       └── resources/
│           ├── application.properties         # 主配置文件
│           ├── mapper/
│           │   ├── framework/system/          # 平台内置 MyBatis 映射文件
│           │   └── business/{module}/         # 【扩展点】业务扩展 MyBatis 映射文件（按模块平铺）
│           └── i18n/                          # 后端国际化资源（message_{zh,en}.properties）
│
├── xxl-ai-sample/                         # 示例 MCP 服务（spring-ai @McpTool，Streamable HTTP，8091）
│   ├── pom.xml                                # Maven配置（继承父工程）
│   └── src/main/java/com/xxl/ai/api/sample/   # 启动类 + SampleMcpTool
│
└── xxl-ai-ui/                               # 前端UI工程（开发 3000；Hash 路由）
    ├── package.json                           # 前端依赖配置
    ├── vite.config.ts                         # Vite构建配置
    └── src/
        ├── main.ts                            # 入口文件
        ├── modules/                           # 模块自包含目录（页面/接口/类型聚合）
        │   ├── framework/                     # 平台内置模块（auth/system/dashboard/help/common）
        │   └── business/                      # 业务模块（space/supplier/knowledge/mcp/skill/agent/chat）
        ├── composables/                       # 组合式函数（usePageParams/useEnumOption 等）
        ├── components/                        # 通用组件（RightToolbar/Pagination/Editor 等）
        ├── directive/                         # 自定义指令（v-hasPermi/v-hasRole）
        ├── i18n/                              # 文案中心（locales/{zh,en}.json）
        ├── layout/ · router/                  # 布局与路由
        ├── store/                             # 状态管理
        ├── utils/                             # 工具类
        ├── types/                             # 全局基础类型
        └── default-settings.ts                # 全局配置
```

补充说明：
- 构建：后端模块在仓库根目录执行 `mvn clean package` 即可一键编译全部 Maven 模块；前端模块进入 `xxl-ai-ui` 目录执行 `npm install`、`npm run dev` 即可本地启动；
- 部署：前端产物内嵌进 `xxl-ai-api` 单包发布（先 `cd xxl-ai-ui && npm run build && npm run sync:dist` 构建并同步产物到后端静态资源目录，再 `mvn clean package`），页面与接口同在 8080；前端工程 `xxl-ai-ui` 开发期 `npm run dev`，示例 MCP 服务 `xxl-ai-sample` 为可选联调组件；
- 扩展：新增业务模块时，可在各模块 `business` 扩展包中开发，并配套放置 Mapper 映射文件、模板文件及配置文件。

### 5.2、开发分离 / 部署合并运行模式

XXL-AI 开发期前后端分离、部署期合并：前端产物内嵌进 API jar，单进程单端口对外，共享同一套数据库与权限体系：

```
┌──────────────────────────────────────────────────────────────────────────────────────┐
│                                   浏览器 / 客户端                                    │
│ 管理端 admin（登录后使用）        ·        公开端访客（/#/chat/{uuid}，免登录）        │
└──────────────────────────────────────────────────────────────────────────────────────┘
                                            │  HTTP / SSE
┌──────────────────────────────────────────────────────────────────────────────────────┐
│  前端  xxl-ai-ui   ·   Vue3 + Vite + Element Plus + TypeScript   ·   开发 :3000      │
│ · 开发：Vite 代理 /api → 8080；生产：产物内嵌进 xxl-ai-api，随 :8080 一并对    │
│ · 后端下发动态菜单 · 零路由改动（Hash） · 中 / 英 i18n                               │
│ · 列表 / 表单 CRUD · SSE 流式对话：思考折叠 · Markdown 渲染 · 断线 / 刷新续传         │
└──────────────────────────────────────────────────────────────────────────────────────┘
                                            │  生产同源：页面与接口同端口（8080）
┌──────────────────────────────────────────────────────────────────────────────────────┐
│          后端  xxl-ai-api   ·   SpringBoot + MyBatis + XXL-SSO   ·   :8080           │
│ · 静态资源：classpath:/static/（内嵌前端产物，/** 提供页面）                          │
│ · framework ：登录鉴权 / RBAC 菜单按钮权限 / 系统管理 / 审计日志                     │
│ · business  ：space · supplier · knowledge · mcp · skill · agent · chat              │
│ · harness   ：llm · chat · rag · mcp · skill · supplier（运行时支撑，无 Controller） │
│ · 统一响应 Response{code,msg,data}；@XxlSso 鉴权；按 xxl-space-id 空间隔离           │
└──────────────────────────────────────────────────────────────────────────────────────┘
                                            │  JDBC / Redis 协议 / gRPC / HTTP(S)
┌──────────────────────────────────────────────────────────────────────────────────────┐
│                                  基础设施与外部依赖                                  │
│ · MySQL    ：xxl_ai 库 —— 平台表 + 业务表（按 space_id 空间隔离）                    │
│ · Redis    ：SSO 登录态（xxl_sso_user:）+ 对话任务队列 / 结果流（Redis Stream）      │
│ · Milvus   ：RAG 向量库（知识库分片向量化，可选）                                    │
│ · 外部服务 ：OpenAI 兼容供应商（对话 / 嵌入模型）、远程 / 本地 MCP 服务              │
└──────────────────────────────────────────────────────────────────────────────────────┘
```

- 后端：`xxl-ai-api`（8080），承载 登录鉴权、RBAC 权限、系统管理、AI 运行时（模型 / RAG / MCP / SKILL），部署期同时托管内嵌前端静态资源；
- 前端：`xxl-ai-ui` 工程基于 Vue3 + Element Plus + TypeScript，菜单由后端下发、`loadView` 自动映射页面、零路由改动；开发期 `npm run dev`（3000，`/api` 代理），部署期产物内嵌进 API jar；
- 协作形态：开发期前后端独立启动、独立迭代；部署期单包（单进程单端口）发布，无需 Nginx 与 History 回退。

前后端共享：数据库表结构、空间与 RBAC 权限模型、登录鉴权（XXL-SSO）、系统管理能力、统一响应规范与开发 SKILL 规范。

### 5.3、安全登录验证

项目进行安全的登录验证防护设计，基于 XXL-SSO 登录认证体系（依赖 `com.xuxueli:xxl-sso-core`），支持集群部署与 SSO 单点登录集成。针对需要登录验证的接口，统一使用 XXL-SSO 提供的 `@XxlSso` 注解进行鉴权（登录态校验通过后自动注入登录用户）：

```
// 1、业务接口统一加 @XxlSso，鉴权通过后自动注入登录态
@XxlSso
@RequestMapping("/system/message/pageList")
public Response<PageModel<MessageDTO>> pageList(...) { ... }
```

登录态说明：
- 登录后登录态（token）存于 Redis（`xxl_sso_user:` keyprefix），支持集群部署共享；
- 未登录访问受保护接口时，XXL-SSO 拦截并返回统一登录失效提示；
- 需要强权限校验（RBAC 按钮级）的接口，配合业务权限标识二次校验（前端 `v-hasPermi`）；

### 5.4、AI + Skill 辅助开发设计

为让 AI 编程助手也能产出平台级规范代码，仓库在 `.agents/skills/` 内置 开发 SKILL，作为 AI 的“项目内专业规范”：

```
.agents/skills/
└── xxl-ai/SKILL.md            # 开发 Skill（name: xxl-ai）
```

每个 SKILL 均内置如下内容，保证 AI 产物符合平台规范：

- 工程结构速览与通用规范引用；
- 后端落位清单（实体 / Mapper / Service / Controller 件套、包路径、方法顺序、分页与校验约定）；
- 前端落位清单（types/api/pages）与列表页代码骨架；
- 菜单 / 按钮权限注册模板（`XxlRoleEnum` 枚举资源）与「校验清单」；
- 参考样例文件绝对路径。

工作原理：AI 编程助手检测到任务时自动加载 SKILL，按 “建表 → 后端 → 前端 → 菜单权限 → 验证” 标准流程直生代码并落位，最后按校验清单自检交付。（平台内置代码生成器已下线，统一以 SKILL 直生等价代码。）

### 5.5、流式对话（SSE）方案

`/chat/**` 公开对话用 SSE 流式交互。为支持多节点集群与断线续传，生成与下发解耦：**请求节点只做「校验落库 + 转发」，LLM 生成由 worker 消费 Redis Stream 异步执行**，任一节点可服务任一连接，无需粘性会话。

关键约定：**助手占位主键 `msgId` 同时作为结果流标识**（结果流 key `xxl:ai:chat:result:{msgId}`），一轮对话 = 一条助手消息 = 一条结果流。

整体链路：

```
POST /chat/send
  └─ 请求节点：校验会话 → 落库用户消息 + 助手占位 → XADD 任务 → XREAD 结果流转发 SSE
                                    │
                     Redis Stream 任务队列（消费组）
                                    ▼
                worker（任意节点，可独立扩容）：执行 LLM → 增量 XADD 结果流
                                    │
       任意节点 XREAD 结果流 → SSE（事件带 id）→ 客户端；断线走 /chat/resume 从 lastEventId 续传
```

组件划分：

| 层 | 组件（目录） | 职责 |
|---|---|---|
| 接入 | `ChatController`（business/chat/controller） | `/chat/**` 路由：元数据与 `send`/`resume` 均走 `ChatService` |
| 应用 | `ChatService`（business/chat/service） | 会话元数据 CRUD + 会话校验（单一校验源）+ 流式发送/续传编排（落库一轮 → 投递任务 → 打开 SSE 连接） |
| 生成器 | `ChatStreamTool`（harness/chat） | 对话生成 worker（`SmartLifecycle`）：任务队列 + 生成消费 + 结果流 + SSE 转发 + 生成编排（装配上下文/工具/RAG → `LlmChatTool` → 回填消息 status=1/2） |
| 生成引擎 | `LlmChatTool`（harness/llm） | 按已装配的上下文/工具/RAG 执行一次流式对话，增量经回调输出（与传输层解耦） |

发送流转：

```
前端 sendStream → ChatController.send → ChatService.send
  ├─ 校验 + 落用户消息(status=1) + 助手占位(status=0) → chatStreamTool.submit(任务)
  └─ chatStreamTool.open(msgId)：XREAD 结果流 → SSE
worker（消费组）：XREADGROUP 任务 → ChatStreamTool.handleTask（校验 / 装配历史与工具 / 调 LlmChatTool）
  ├─ 增量经回调 → appendResult(msgId, thinking/message)（本地累积，供失败回填）
  └─ 回填 updateAssistant(内容, status)；写终态 done/error + ack
```

续传流转：

```
前端发现助手消息 status=0（刷新/断线）→ resumeStream(msgId, lastEventId)
  └─ ChatService.resume → chatStreamTool.open → 从 lastEventId 之后 XREAD 重放，不重新生成
```

SSE 事件协议（`ChatStreamTool` 的 `EVENT_*` 常量）：

| 事件 | 数据 | 说明 |
|---|---|---|
| `stream` | `msgId` | 连接建立即下发，客户端据此断线续传 |
| `thinking` | 思考过程增量 | 推理模型 `reasoning_content` |
| `message` | 回复内容增量 | Markdown 文本增量 |
| `ping` | `ping` | 空闲心跳保活 |
| `done` | 空 | 生成结束（终态） |
| `error` | 错误提示 | 生成失败（终态） |

除 `stream` / `ping` 外的事件均带结果流条目 id（SSE `id:`），客户端以最后一个 id 作为 `lastEventId`。

接口与续传：

- `POST /chat/send?uuid&visitorId&convId&content`：落库用户消息 + 助手占位后投递任务并转发；
- `POST /chat/resume?msgId&lastEventId`：从结果流 `lastEventId` 之后继续转发，不重新生成；
- `xxl_ai_chat_msg.status`（0-生成中、1-完成、2-失败）：发送即落助手占位；刷新后前端发现 `status=0` 即按 msgId 自动续传，生成结果不丢失。

集群与可靠性：

- **无状态转发**：结果流存于 Redis，任意节点可转发，节点重启/扩缩容不影响在途生成；
- **至少一次消费**：消费组竞争消费；worker 宕机后超时未确认任务由其他节点认领（认领阈值 > 单次生成最长耗时，避免误抢在途任务）；
- **连接容错**：转发线程池满时回退「服务繁忙」；Redis 读取连续失败达阈值才中断，客户端可再续传；
- **保留窗口**：结果流按 TTL（默认 600s，追加时续期）保留，即断线/刷新的续传时间窗；
- **生成不受连接影响**：客户端断开后 worker 仍完成生成并落库。

相关配置（`application.properties`）：

```
xxl-ai.chat.stream.timeout=180000   # 单连接/单次生成最长时长(ms)
xxl-ai.chat.stream.ttl=600          # 结果流保留时长(s)
xxl-ai.chat.worker.count=4          # 单节点生成 worker 并发数
xxl-ai.chat.sse.max=64              # 单节点 SSE 转发最大并发连接数
xxl-ai.chat.history.limit=50        # 附加给模型的最近历史消息条数上限
```

内部实现：XREAD 阻塞窗口 `5000ms`、单次批量 `50` 条；SSE 线程池核心数 `sse.max / 8`、队列容量 `0`（`SynchronousQueue`，确保并发扩到 `max` 且不排队长连接）；宕机认领阈值 = `chat.stream.timeout + 60s`。

### 5.6、业务数据模型与空间隔离

数据库 `xxl_ai`，统一约定：表名前缀 `xxl_ai_`、字段下划线命名、`id` 主键自增、`add_time` / `update_time` 公共字段、状态字段 `TINYINT`（0-正常 / 1-停用）、全表 `utf8mb4`、唯一索引 `i_` 前缀、所有字段带 `COMMENT`；初始脚本 `doc/db/tables_xxl_ai.sql`（建库 + 全量表 + 种子数据，`SET NAMES utf8mb4`）。

表按域分组：

| 域 | 表 | 说明 |
|---|---|---|
| 平台 | `xxl_ai_user`、`xxl_ai_config`、`xxl_ai_log` | 用户、系统配置、审计日志 |
| 空间 | `xxl_ai_space`、`xxl_ai_user_space` | 业务空间、用户-空间授权 |
| 供应商 | `xxl_ai_supplier`、`xxl_ai_supplier_model` | 供应商、模型（对话 / 嵌入） |
| 知识库 | `xxl_ai_knowledge_base`、`xxl_ai_knowledge_doc` | 知识库、文档（向量化状态） |
| MCP | `xxl_ai_mcp` | MCP 服务配置 |
| SKILL | `xxl_ai_skill`、`xxl_ai_skill_file` | 技能、技能文件树 |
| Agent | `xxl_ai_agent`、`xxl_ai_chat_conv`、`xxl_ai_chat_msg` | Agent、对话、消息 |

**空间隔离**：除平台表外，业务表均带 `space_id`；管理端当前空间由请求头 `xxl-space-id` 传入，后端按空间过滤；管理员可见全部空间，普通用户按 `xxl_ai_user_space` 授权。公开对话端以 Agent 的 `uuid` + 访客 `visitorId` 隔离会话。所有关联均为应用层维护（无数据库外键）。

**权限模型**：平台菜单 / 按钮由枚举 `XxlRoleEnum#buildRoleResources` 按角色定义（已下线资源 / 角色关联表），角色 `admin` / `user`；新增页面在对应角色分支追加即可、无需改路由与数据库，浏览器按钮权限用 `v-hasPermi`。

### 5.7、AI 运行时与工具装配

- **模型工厂 `LlmModelFactory`**：按供应商配置程序化构建 OpenAI 兼容的 `OpenAiChatModel` / `OpenAiEmbeddingModel`（`spring.ai.model.*=none` 关闭自动装配），按「供应商 + 模型（+ 会话，仅当自定义 Header 含 `{session}` 占位时）」LRU 缓存，Header value 支持 `{session}` 占位；
- **对话编排 `LlmChatTool`**：按已装配的「系统指令 + 历史消息 + 当前提问 + 工具 + RAG Advisor」，经 `ChatClient` 流式对话，思考过程（`reasoningContent`）与回复内容经回调增量输出；
- **RAG `RagTool`**：每知识库对应一个 Milvus 集合 `kb_base_{baseId}`（COSINE / FLAT），文档分片向量化写入、检索经 `QuestionAnswerAdvisor` 自动注入上下文；内聚嵌入模型解析、向量存储缓存与文本分片；
- **MCP `McpToolFactory`**：`McpClientTool` 基于官方 Java MCP SDK（stdio / Streamable HTTP），将 MCP 工具转换为 spring-ai `ToolCallback`；仓库内附示例 MCP 服务 `xxl-ai-sample`（spring-ai `@McpTool`，Streamable HTTP），供「MCP管理」连通测试联调；
- **SKILL `SkillToolFactory`**：将 DB 技能文件树物化为 `{skill.root}/agent_{agentId}/{skillName}/`，构建 `SkillsTool` 及配套 shell / 文件执行工具（bash、Read/Write/Edit、Glob、Grep、List），技能内容变更按更新时间指纹自动重建；
- **工具装配顺序**：`buildTools` 依次装配 MCP 工具 + Skill 工具 + 执行工具，统一以 `Object` 列表随请求传入，spring-ai 自动解析注册。


## 六、总体设计（本地版）

### 6.1 技术选型

| 维度 | 方案 |
|---|---|
| 桌面框架 | Electron 44 + electron-vite 5 |
| 前端 | Vue 3 + TypeScript + Element Plus |
| Agent 运行时 | **Pi**（`@earendil-works/pi-ai` + `pi-agent-core`），独立 `utilityProcess` |
| 本地存储 | better-sqlite3 + Drizzle ORM（SQLite 单文件） |
| 内容渲染 | markdown-it + DOMPurify + highlight.js |
| 终端 | node-pty + xterm |
| 打包分发 | electron-builder（mac / win / linux） |

### 6.2 三进程架构

```
┌────────────────────────────────────────────────────────────┐
│ Renderer（Vue3 + Element Plus，无 Node 直连）               │
│  会话列表 · 对话流 · Composer · 设置 · 侧边任务面板          │
└───────────────▲────────────────────────────────────────────┘
                │ contextBridge（preload 暴露 window.desk.*）
┌───────────────┴────────────────────────────────────────────┐
│ Main（Electron 主进程，Node / CJS）                          │
│  窗口/生命周期 · IPC 网关 · SQLite(Drizzle) 持久化           │
│  Agent 运行时管理 · 越界审批弹窗 · 文件/终端/浏览器服务       │
└───────────────┬────────────────────────────────────────────┘
                │ utilityProcess + parentPort 结构化消息
┌───────────────▼────────────────────────────────────────────┐
│ Agent 运行时进程（utilityProcess，Node / CJS）              │
│  Pi Runtime（pi-agent-core + pi-ai）                         │
│  多供应商 LLM · Agent 循环 · 工具调用 · 文件沙箱 · 事件流     │
└────────────────────────────────────────────────────────────┘
```

- **安全**：`contextIsolation: true`、`nodeIntegration: false`；渲染层仅经 preload 白名单 IPC 访问能力；
- **运行时隔离**：Pi 运行时独立运行在 `utilityProcess`，主进程只做 IPC 网关与审批弹窗，LLM 流式与工具重活不占用主进程事件循环；
- **越界审批**：运行时进程内的文件沙箱无法弹窗，经 `parentPort` 发 `approval-request` 给主进程，由主进程弹原生对话框后回传结果。

### 6.3 本地数据模型（SQLite）

| 表 | 说明 |
|---|---|
| `desk_project` | 本地项目（`id / name / path / add_time / update_time`，路径 1:1 唯一） |
| `desk_session` | 会话（`id / title / provider_id / model_id / system_prompt / mode / project_id / add_time / update_time`） |
| `desk_message` | 消息（`id / session_id / seq / role / content / data / add_time`，`data` 为 Agent 原始消息 JSON） |
| `desk_provider` | 供应商（`name / base_url / api_key / headers / models / enabled / sort`） |
| `desk_setting` | 设置（`key / value`） |

- 旧库兼容：`PRAGMA table_info` 检测缺列后 `ALTER TABLE` 补齐（SQLite 无 `ADD COLUMN IF NOT EXISTS`）；
- 运行时数据目录默认 `app.getPath('userData')/xxl-ai-desk.sqlite`，可自定义（`userData/desk-config.json`），修改后重启生效。

### 6.4 工程结构

```
xxl-ai-desk/
├── electron.vite.config.ts        # 三进程构建配置
├── electron-builder.yml           # 打包配置
└── src/
    ├── main/                      # 主进程（Node）
    │   ├── index.ts               # 入口：窗口 / 生命周期
    │   ├── ipc.ts                 # IPC 路由（含 chat:send 编排）
    │   ├── security.ts            # API Key 本地编解码
    │   ├── db/                    # SQLite + Drizzle（schema / 初始化 / 迁移）
    │   ├── agent/                 # Pi 运行时：runtime / worker / models / tools / sandbox / host
    │   └── services/              # settings / provider / session / project / fs / watch / terminal
    ├── preload/index.ts           # contextBridge 暴露 window.desk
    ├── shared/ipc.ts              # 主/预加载/渲染共享类型
    └── renderer/src/              # 渲染进程（Vue3）
        ├── components/            # Sidebar / MessageItem / ChatComposer / TerminalPanel / panel/*
        ├── stores/                # settings / chat / layout / project
        ├── modules/               # chat / settings 页面
        ├── i18n/                  # zh / en 文案
        └── styles/                # 设计令牌与全局样式
```

### 6.5 安全设计

- **文件沙箱**：所有文件工具经统一 `guardPath` 闸门，路径规范化（解析软链接）后限定当前项目目录；
- **越界审批**：越界操作由主进程弹原生对话框，可选「允许本次 / 本会话允许 / 拒绝」，会话级白名单记入内存；
- **模式隔离**：Plan 模式不装配写 / 命令工具，从工具层面限制只读；
- **进程隔离**：渲染进程无 Node 能力，运行时进程无 Electron 能力，敏感能力（密钥、原生弹窗）留在主进程。


## 六、版本更新日志

### v1.0.0 Release Notes[2026-09-27]

> 首个正式版本：一个可接工具、可接知识、可一键发布、可生产落地的开源 AI Agent 平台。

- 1、【重点】**Agent 编排 + 一键发布（重点）**：模型 / 系统指令 / 知识库 / MCP / SKILL 自由组合、多选绑定；发布即生成免登录公开地址 `/#/chat/{uuid}`，管理端可查看访客对话与消息记录；对话支持思考过程折叠与 Markdown 实时渲染。
- 2、【亮点】**MCP + SKILL + RAG 三位一体（让 Agent 真能干活）**：
  - MCP工具：支持 远程（Streamable HTTP）/ 本地（stdio）MCP 服务在线管理及连通测试，Agent 运行时自动装配可用工具；
  - SKILL 技能：支持在线管理 `SKILL.md` 及文件树内容与脚本，自动物化为可执行技能目录，Agent 运行时自动装配可用技能；
  - RAG 知识库：支持多类型文档托管、解析与向量化，文档分片向量化入 Milvus，对话时自动检索并注入上下文。
- 3、【亮点】**流式对话 SSE 无状态化（架构亮点）**：生成与下发经 Redis Stream 解耦——请求节点只做校验落库与转发，worker 异步生成；支持集群部署与断线 / 刷新续传，助手占位主键 `msgId` 复用为结果流标识，一轮对话一条流，生成结果不丢失（详见 5.5）。
- 4、【亮点】**多模型供应商统一接入**：兼容 OpenAI 协议（Deepseek / 智谱GLM / Ollama / OpenCode 等），供应商 + 模型两级管理，支持连通测试与远程模型自动导入，区分对话模型 / 嵌入模型。
- 5、【新增】**工程化底座开箱即用**：XXL-SSO 登录、RBAC 菜单 / 按钮权限（动态菜单、零路由改动）、多业务空间隔离、Monorepo 前后端分离。
- 6、【对话】**Chat SSE 无状态化**：Redis Stream 任务队列解耦生成与下发，支持集群部署、断线 / 刷新续传；
- 7、【开发】**AI驱动开发**：内置开发 SKILL `.agents/skills/xxl-ai`，AI 编程助手一键加载、按平台规范直生业务代码并落位；
- 8、【开发】**前后端分离**：前后端分离模式，并采用流行技术栈；前端 Vue3 + Element Plus + TypeScript，后端 SpringBoot + Spring-AI + XXL-SSO；
- 9、【部署】**Docker Compose 一键部署**：支持 Docker Compose 一键部署应用（mysql + redis + milvus + api/ui）。

Docker Compose部署脚本：

```
# 第一步：代码clone本部 + 前往仓库目录
git clone https://github.com/xuxueli/xxl-ai.git
cd ./xxl-ai

# 第二步：构建前端并同步产物（npm run build 构建 dist，npm run sync:dist 复制到 xxl-ai-api 静态资源目录）
cd xxl-ai-ui && npm install && npm run build && npm run sync:dist && cd ..

# 第三步：构建后端
mvn clean package

# 第四步：进入 docker 目录（支持自定义 .env 配置，如修改 MYSQL_PATH 配置设置 Mysql 数据持久化目录）
cd ./docker/
cat .env

# 第五步：启动/停止项目
docker compose up -d
docker compose down
```

### v1.1.0 Release Notes[2026-10-08]

**【云版（Web 服务端）】**

- 1、【升级】项目依赖升级最新版本；
- 2、【优化】模型API请求通参调整，设置 User-Agent: XXL-AI 便于供应商识别；
- 3、【优化】供应商模型请求参数属性优化，支持格式检测与合法性检测；
- 4、【新增】I18N 模块重构：前后端国际化逻辑优化，统一后端控制；标准化国际化资源文件结构，支持多语言配置，并优化前端国际化加载逻辑；
- 5、【优化】项目部署优化：研发环节前后端分离，部署期前端产物内嵌进后端 Jar，单进程单端口对外；

**【本地版（Desk 桌面端）】**

> 本地版（Desk 桌面端）以·与云版零依赖、独立构建发布；详细安装与操作见《XXL-AI-DESK 官方文档》。

- 1、【重点】Desk/客户端上线：基于 Electron + Vue3 + TypeScript 构建跨平台桌面客户端，支持 mac / win / linux 一键安装及应用，本地优先、开箱即用；
- 2、【新增】多供应商模型接入：兼容 OpenAI 协议，供应商 + 模型本地配置；首次启动自动预置 OpenCodeGo / Ollama / Deepseek / 智谱GLM；
- 3、【新增】流式对话与执行过程可视化：流式输出思考过程 / 工具调用 / 回复内容；助手消息按片段发生顺序渲染时间线（思考 → 工具 → 正文交错），思考折叠、工具调用状态与耗时展示、Markdown 实时渲染与代码高亮；
- 4、【新增】项目与会话管理：项目绑定本地磁盘目录，会话归属项目，支持搜索 / 重命名 / 删除 / 排序；会话与消息落本地 SQLite 单文件，运行时数据目录可自定义；
- 5、【新增】Plan / Build 模式：Plan 只读（仅只读文件与检索工具）、Build 全量读写，随会话持久化，默认 Build；文件操作限定当前项目目录，越界弹原生对话框「允许本次 / 本会话允许 / 拒绝」；
- 6、【新增】本地系统能力：终端命令行（node-pty + xterm）；侧边任务面板含文件（目录树 + 编辑保存 + 预览 + 跟随本地变更）与浏览器（多标签 `webview`）；
- 7、【新增】个性化设置：应用名称 / Slogan / 自定义指令，浅色（默认）/ 深色主题，中 / 英双语；
- 8、【架构】Agent 运行时进程隔离：Pi（`pi-ai` + `pi-agent-core`）运行于独立 `utilityProcess`，主进程仅做 IPC 网关与越界审批，长会话与工具重活不阻塞 UI；
- 9、【优化】性能与体验：会话落库增量 + 单事务、运行时增量合批、渲染端按需滚动；生成中切换会话不丢内容；退出时回收运行时与终端进程。

### v1.1.1 Release Notes[2026-10-08]
- 1、【新增】Desk版本：命令运行环境（Node/Python）支持自定义PATH设置；Node默认使用内置版本，降低本地环境依赖；Python默认使用系统环境，支持自定义PATH；
- 2、【强化】Desk版本：终端locale显示设置UTF-8，解决中文乱码问题；


### v1.2.0 Release Notes[ING]
- 1、【TODO】云版：OpenAPI：针对搭建的Agent提供OpenAPI接口能力，通过agentId + accessToken访问，便于集成到第三方系统应用（提供内置Agent对话能力，可用于功能调试或快速集成应用）。
- 2、【TODO】云版：Memory：支持跨会话记忆、记忆内容主动沉淀更新、上下文检索及注入等。
- 3、【TODO】云版：可观测：支持Agent可观测，包括Session对话、工具/知识/记忆等Trace明细可观测等。
- 4、【TODO】Desk版本：支持SKILL/MCP工具；
- 5、【TODO】Desk版本：支持浏览器工具操作；
- 6、【TODO】Desk版本：自动更新；


### TODO LIST
- 1、AI 能力增强：
  - WorkFlow 定义：工作流及 Agent/模型编排定义、执行与日志、分布式执行；
  - 知识库：多类型文档（Word / PDF / 图片）解析与向量化；
  - Agent 生图：文生图 / 图生图，支持集成多模型供应商；
  - Agent 生视频：文生视频 / 图生视频，支持集成多模型供应商；
  - Chat 对话增强：对话记忆控制、多模态输入；
- 2、其他

## 七、其他

### 7.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-ai/issues/) 讨论新特性或者变更。

### 7.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-ai/issues/1 ) 登记，登记仅仅为了产品推广。

### 7.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )_
