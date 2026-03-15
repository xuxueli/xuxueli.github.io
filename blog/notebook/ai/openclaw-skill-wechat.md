<h2 style="color:#4db6ac !important" >使用OpenClaw+Skill自动发布微信公众号文章</h2>
> 【原创】2026/03/07

[TOCM]

[TOC]

## 一、OpenClaw 介绍

**OpenClaw** 是一个强大的开源**个人 AI 助手框架**，它可以运行在你自己的电脑上，通过各种聊天工具（微信、Telegram、Discord、Slack 等）与你对话，帮你完成各种任务。

### 什么是 OpenClaw？

你可以把它理解为：一个**本地运行的 AI 助手**，它能够：
- 帮你回复消息
- 搜索信息
- 写文章、发公众号
- 操作浏览器
- 管理文件
- 定时执行任务

### 核心特点

| 特点 | 说明 |
|------|------|
| 🏠 本地运行 | 数据存在自己电脑，安全可控 |
| 📱 多渠道 | 支持微信、Telegram、Discord 等 20+ 平台 |
| 🤖 AI 驱动 | 调用大模型帮你思考和执行 |
| 🛠️ 可扩展 | 通过 Skill 无限扩展能力 |
| 🔔 主动提醒 | 支持定时任务、心跳检查 |

### 谁在用 OpenClaw？

- 个人用户：作为私人 AI 助手
- 开发者：作为编程辅助工具
- 企业：作为客服、自动化流程

> 官网：https://openclaw.ai  
> GitHub：https://github.com/openclaw/openclaw  (300K+ ⭐)

---

## 二、Skill 介绍

### 什么是 Skill？

**Skill（技能）**是 OpenClaw 的扩展模块，就像手机上的 App。每个 Skill 让 AI 完成特定任务，比如：
- 发送邮件
- 搜索网页
- 发布公众号
- 控制浏览器

### Skill 标准格式

一个完整的 Skill 通常包含以下文件：

```
my-skill/
├── SKILL.md          # 技能说明文档（必需）
├── scripts/          # 执行脚本目录
│   └── publish.sh   # 发布脚本
├── assets/           # 静态资源
├── package.json      # 依赖配置
└── README.md        # 使用说明
```

### SKILL.md 格式示例

```markdown
# 技能名称

## 功能描述
这个技能可以做什么

## 前置要求
- 要求1
- 要求2

## 使用方法
1. 第一步
2. 第二步

## 示例
输入: "帮我 xxx"
输出: 结果xxx
```

### 如何使用 Skill

当你想让 AI 完成某个任务时，只需要告诉它：
- "帮我发布公众号文章"
- "帮我搜索 xxx 信息"
- "帮我控制浏览器 xxx"

AI 会自动调用对应的 Skill 来完成。

---

## 三、ClawHub 技能市场

**ClawHub** (https://clawhub.ai) 是 OpenClaw 官方的技能市场，汇聚了社区开发的数千个 Skills！

### 如何下载 Skill

使用 `npx skills` 命令：

```bash
# 搜索技能
npx skills find <关键词>

# 安装技能
npx skills add <owner/repo@skill> -g -y

# 查看已安装
npx skills list
```

### 如何新建 Skill

```bash
# 初始化一个新 Skill
npx skills init my-awesome-skill

# 查看目录结构
ls my-awesome-skill/
```

### Top 5 热门 Skills

以下是安装量较高的部分 Skills：

| 排名 | Skill 名称 | 功能 | 安装量 |
|------|-----------|------|--------|
| 1 | baoyu-post-to-wechat | 发布公众号文章 | 11.3K |
| 2 | wechat-article-writer | 微信文章写作 | 875 |
| 3 | wechat-article-publisher | 公众号文章发布 | 676 |
| 4 | agent-browser | 浏览器自动化控制 | 135 |
| 5 | unit-test-boundary-conditions | 单元测试边界条件 | 373 |

---

## 四、OpenClaw 发布公众号配置步骤

### 4.1 wechat-publisher 技能介绍

**wechat-publisher** 是一个可以将 Markdown 文章一键发布到微信公众号草稿箱的 Skill。

**核心功能：**
- 一键发布 Markdown 到公众号草稿箱
- 多主题支持（lapis、phycat、default 等）
- 代码高亮显示
- 图片自动上传到微信图床

**来源**：https://github.com/0731coderlee-sudo/wechat-publisher

### 4.2 安装 wechat-publisher

**方法一：通过 npx skills 安装**

```bash
npx skills add 0731coderlee-sudo/wechat-publisher -g -y
```

**方法二：手动克隆**

```bash
# 克隆项目
git clone https://github.com/0731coderlee-sudo/wechat-publisher.git
cd wechat-publisher

# 安装依赖
npm install @wenyan-md/cli
```

### 4.3 配置 AppID 和 AppSecret

**第一步：获取凭证**

1. 登录 https://mp.weixin.qq.com/
2. 进入「设置与开发」→「基本配置」
3. 获取「开发者ID(AppID)」和「开发者密码(AppSecret)」

**第二步：配置到 OpenClaw**

在 `~/.openclaw/workspace/TOOLS.md` 中添加：

```bash
## 微信公众号凭证
export WECHAT_APP_ID=你的AppID
export WECHAT_APP_SECRET=你的AppSecret
```

### 4.4 配置 IP 白名单

⚠️ **这是关键步骤！**

**第一步：查询服务器公网 IP**

```bash
curl ifconfig.me
```

**第二步：添加到公众号后台**

1. 登录 https://mp.weixin.qq.com/
2. 进入「设置与开发」→「基本配置」
3. 找到「IP白名单」
4. 添加刚才查询到的 IP 地址

---

## 五、实战：发布 XXL-JOB 最新版本文章

下面演示完整的操作流程。

### 步骤 1：让 AI 帮你写文章

告诉 OpenClaw 你的需求：

```
"帮我查询 XXL-JOB 最新发布的版本，生成一篇公众号文章"
```

AI 会自动：
- 查询 GitHub 最新 Release 信息
- 获取项目介绍和特性
- 生成 Markdown 格式文章

### 步骤 2：文章格式要求

文章顶部必须包含 frontmatter：

```yaml
---
title: 文章标题
cover: 封面图URL
---

# 正文内容
...
```

**注意：**
- title：文章标题（必填）
- cover：封面图地址（必填，建议 1080×864）

### 步骤 3：一键发布

```bash
cd ~/.openclaw/workspace/wechat-publisher

# 设置环境变量（如果还没配置）
export WECHAT_APP_ID=你的AppID
export WECHAT_APP_SECRET=你的AppSecret

# 发布文章
./scripts/publish.sh 你的文章.md
```

### 步骤 4：成功输出

```
🚀 开始发布文章...
✅ 封面图上传成功
✅ 文章内容转换成功
✅ 发布成功，Media ID: UqLqFEOAfH9W00FdAVE-xxx
```

### 步骤 5：手动发布

文章已推送到**草稿箱**，你需要：
1. 登录公众号后台 https://mp.weixin.qq.com/
2. 进入「新的创作」→「图文消息」
3. 点击「从草稿箱选取」
4. 预览并发布

---

## 六、完整操作流程总结

```
┌────────────────────────────────────────────────────────┐
│           OpenClaw 发布公众号完整流程                  │
└────────────────────────────────────────────────────────┘

第1步：安装 OpenClaw
        └─ npm install -g openclaw@latest

第2步：安装 wechat-publisher 技能
        └─ npx skills add 0731coderlee-sudo/wechat-publisher

第3步：配置公众号凭证
        └─ 在 TOOLS.md 添加 WECHAT_APP_ID 和 WECHAT_APP_SECRET

第4步：配置 IP 白名单
        └─ 公众号后台添加服务器 IP

第5步：让 AI 写文章
        └─ 告诉 AI 你的需求，如"帮我写一篇关于 xxx 的文章"

第6步：发布到公众号
        └─ ./scripts/publish.sh article.md

第7步：手动发布
        └─ 登录公众号后台 → 草稿箱 → 发布
```

---

## 七、常见问题

**Q1: 发布失败显示 IP 不在白名单？**
> 确保将服务器公网 IP 添加到公众号后台白名单

**Q2: 微信公众号是什么类型账号？**
> 目前支持订阅号和服务号

**Q3: 图片显示不出来？**
> 确保使用公网可访问的图片 URL

**Q4: 如何查看已安装的 Skills？**
```bash
npx skills list
```

---

## 八、总结

通过 OpenClaw + wechat-publisher 技能，你可以：

- ✅ 让 AI 自动写文章
- ✅ 一键发布到公众号草稿箱
- ✅ 支持 Markdown 格式
- ✅ 代码高亮显示
- ✅ 大幅节省排版时间

这就是 AI 时代的效率提升之道！

---

**参考链接：**
- OpenClaw 官网：https://openclaw.ai
- ClawHub 技能市场：https://clawhub.ai
- wechat-publisher：https://github.com/0731coderlee-sudo/wechat-publisher
- 微信公众号后台：https://mp.weixin.qq.com/

---

*本文由 AI 自动生成*