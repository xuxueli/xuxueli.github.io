<h2 style="color:#4db6ac !important" >使用Ollama本地化部署DeepSeek</h2>
> 【原创】2025/02/01

[TOCM]

[TOC]

### 1、Ollama 简介
Ollama 是一个开源的本地化大模型部署工具，旨在简化大型语言模型（LLM）的安装、运行和管理。它支持多种模型架构，并提供与 OpenAI 兼容的 API 接口，适合开发者和企业快速搭建私有化 AI 服务。

Ollama 的主要特点包括：
- 轻量化部署：支持在本地设备上运行模型，无需依赖云端服务。
- 多模型支持：兼容多种开源模型，如 LLaMA、DeepSeek 等。
- 高效管理：提供命令行工具，方便用户下载、加载和切换模型。
- 跨平台支持：支持 Windows、macOS 和 Linux 系统。

### 2、DeepSeek-R1 简介

DeepSeek-R1 是由深度求索（DeepSeek）公司开发的高性能 AI 推理模型，专注于数学、代码和自然语言推理任务。其核心优势包括：

- 强化学习驱动：通过强化学习技术显著提升推理能力，仅需少量标注数据即可高效训练。
- 长链推理（CoT）：支持多步骤逻辑推理，能够逐步分解复杂问题并解决。
- 模型蒸馏：支持将推理能力迁移到更小型的模型中，适合资源有限的场景。
- 开源生态：遵循 MIT 开源协议，允许用户自由使用、修改和商用。

DeepSeek-R1 在多个基准测试中表现优异，性能对标 OpenAI 的 o1 正式版，同时具有更高的性价比。

### 3、使用 Ollama 部署 DeepSeek-R1

##### 3.1、安装 Ollama

**下载 Ollama**： 访问 Ollama 官网，根据操作系统（Windows、macOS 或 Linux）下载安装包，并按照说明进行安装。
- 官网：https://ollama.com/
- Github：https://github.com/ollama/ollama

**验证安装**：在终端中运行以下命令验证安装：
```bash
ollama --version
```
如果安装成功，命令行会显示 Ollama 的版本信息。

##### 3.2、下载 DeepSeek-R1 模型

Ollama已支持DeepSeek-R1, 模型地址：[deepseek-r1](https://ollama.com/library/deepseek-r1) 。
根据自己的显存选择对应的模型，macmini m4 16g 可流畅支持 7b。

**下载模型**：

使用以下命令下载 DeepSeek-R1 模型：
```bash
// 下载 1.5B 模型，可结合机器资源选择对应的模型。
ollama pull deepseek-r1:1.5b
```
该命令会自动下载并加载模型，下载时间取决于网络速度和模型大小。

**查看模型信息**：

下载完成后，可以使用以下命令查看模型信息：
```bash
ollama list
```
该命令会显示已下载的模型列表，包括名称、大小和路径等。

**运行 DeepSeek-R1**:

使用以下命令启动 DeepSeek-R1 模型：
```bash
ollama run deepseek-r1:1.5b
```
该命令会启动 DeepSeek-R1 模型，并启动一个 REPL（交互式终端），你可以在这里输入问题，模型会根据问题生成回答。

**测试功能**:

在交互模式下，可以测试 DeepSeek-R1 的多种功能，例如：
- 智能客服：输入客户常见问题，如“如何安装Ollama？”。
- 内容创作：输入“为DeepSeek写一篇入门指南”。
- 编程辅助：输入“用 Java 实现快速排序”。
- 教育辅助：输入“解释牛顿第二定律”。

### 4、部署 Open-WebUI 增强交互体验

Ollama与Open WebUI结合，可以提供更丰富的交互体验。 可选择任意支持Ollama的webUI，如 AnythingLLM、Dify、Open-WebUI 等。
- AnythingLLM：更专注于文档知识库与问答场景，自带向量检索管理，可“多文档整合”，接入 Ollama 后实现本地化问答。
- Dify：功能多元，适合对话流管理、插件化扩展、团队协同等复杂需求。只要能在其后台正确配置 Ollama 地址，即可灵活调用。
- Open-WebUI：定位纯聊天界面，支持多模型集成，你可以把它当做一个能“轻松切换模型、马上对话”的 Web 面板，如果只是想单纯体验 Ollama 的生成效果，Open-WebUI 是最方便的。

本文场景比较简单，选择与Ollama结合比较紧密的open-webui。
- Open-WebUI：https://github.com/open-webui/open-webui
- 官方文档：https://docs.openwebui.com/getting-started/quick-start/

**下载 Open-WebUI**：

本地使用 docker 部署Open-WebUI，使用以下命令下载 Open-WebUI：
```bash
docker pull ghcr.io/open-webui/open-webui:main
```

**启动 Open-WebUI**：
```bash
// 创建本地目录，避免重启后数据丢失
mkdir /Users/admin/program/docker/instance/open-webui/data
cd /Users/admin/program/docker/instance/open-webui

// 启动容器
docker run -d -p 3000:3000 -v $PWD/data:/app/backend/data --name open-webui ghcr.io/open-webui/open-webui:main 
```

启动成功后，可在终端中查看容器状态，通过浏览器访问Open-WebUI：http://localhost:3000 

**配置 Ollama 地址**：

浏览器进入 Open-WebUI 后，点击右上角的设置图标 进入设置页面。在“模型”选项卡中，点击“添加模型”，选择“Ollama”，并输入 Ollama 的地址（默认为 http://localhost:11434）。

**测试功能**:

在 Open-WebUI 中，你可以选择使用 Ollama 的不同模型，新建对话并体验不同的功能。例如：
- 智能客服：输入客户常见问题，如“如何安装Ollama？”。
- 内容创作：输入“为DeepSeek写一篇入门指南”。
- 编程辅助：输入“用 Java 实现快速排序”。
- 教育辅助：输入“解释牛顿第二定律”。


