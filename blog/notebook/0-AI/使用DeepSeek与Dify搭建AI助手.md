<h2 style="color:#4db6ac !important" >使用DeepSeek与Dify搭建AI助手</h2>
> 【原创】2025/03/08

[TOCM]

[TOC]

### 1、Dify 简介
Dify 是一款开源的大语言模型(LLM) 应用开发平台，提供完整的私有化部署方案。通过将本地部署的 DeepSeek 服务无缝集成到 Dify 平台，企业和个人可以在确保数据隐私的前提下，在本地服务器环境内构建功能强大的AI应用。

Dify私有化部署方案优势：
- 性能卓越：提供媲美商业模型的对话交互体验
- 环境隔离：完全离线运行，杜绝数据外泄风险
- 数据可控：完全掌控数据资产，符合合规要求

### 2、DeepSeek 简介
DeepSeek 是一款开创性的开源大语言模型，凭借其先进的算法架构和反思链能力，为AI对话交互带来了革新性的体验。 

关于DeepSeek此处不赘述，参考历史文章 《使用Ollama本地化部署DeepSeek》。

### 4、Docker Compose 方式部署 Dify
依次运行以下命令，将拉取Dify源代码至本地并完成环境配置，随后启动Dify。详细说明参考 [Docker Compose 部署](https://docs.dify.ai/zh-hans/getting-started/install-self-hosted/docker-compose)。

```bash
git clone https://github.com/langgenius/dify.git
cd dify/docker
cp .env.example .env
docker compose up -d    # 如果版本是 Docker Compose V1，使用以下命令：docker-compose up -d； 
```

注意：
- a、如果需更新 Dify，可以重新进入dify源代码的docker目录，按顺序执行以下命令：
- b、如果 .env.example 文件有更新，请务必同步修改本地的 .env 文件；检查 .env 文件中的所有配置项，确保它们与你的实际运行环境相匹配。
```bash
cd dify/docker
docker compose down
git pull origin main
docker compose pull
docker compose up -d
```

Docker Compose 启动成功后可看到如下信息：
![img_267.png](https://www.xuxueli.com/blog/static/images/img_267.png)

Dify 社区版默认使用 80 端口，点击链接 http://localhost 即可访问你的私有化 Dify 平台。

首次进入默认重定向至管理员账号设置界面，进行管理员账号和密码设立。
![img_268.png](https://www.xuxueli.com/blog/static/images/img_268.png)

登录后进入 Dify 首页。
![img_269.png](https://www.xuxueli.com/blog/static/images/img_269.png)

### 5、将 DeepSeek 接入至 Dify

配置模型供应商：点击Dify平台右上角头像 → 设置 → 模型供应商，选择 Ollama，轻点“添加模型”。

![img_270.png](https://www.xuxueli.com/blog/static/images/img_270.png)

添加本地 DeepSeek 模型：选中 Ollama 供应商 → 点击“添加模型”，参考如下配置模型信息

![img_271.png](https://www.xuxueli.com/blog/static/images/img_271.png)


特殊问题说明：如果Ollama作为macOS应用运行，上述配置模型URL可能无法连接，可更换成如下URL配置。请参考[Dify常见问题](https://docs.dify.ai/zh-hans/learn-more/use-cases/private-ai-ollama-deepseek-dify#id-1.-docker-bu-shu-shi-de-lian-jie-cuo-wu)。

```
http://host.docker.internal:11434
```

至此配置完成。

### 6、入门AI应用：AI Chatbot

聊天机器人（AI Chatbot）类AI应用能力相对单一，整体搭建流程复杂度一般，可抽象为如下几步：
- a、新建AI应用：轻点 Dify 平台首页左侧的"创建空白应用"，选择"聊天助手"类型应用并命名。本文示例命名为“中英文翻译专家”，支持中英文双语翻译能力。
- b、选择模型：在右上角的应用类型选择具体的模型。本文选择 Ollama 框架内的 deepseek-r1:7b 模型。
- c、编排prompt：在左侧“编排prompt”区域输入prompt模板并保存。本文prompt参考下图。
- d、绑定知识库（可选）：进入AI应用管理页面，轻点“知识库”按钮，可以上传知识文件并绑定。

上述配置完成后，在预览对话框中输入内容，验证AI应用是否能够符合预期运行。生成预期答复意味着AI应用搭建已完成。

![img_272.png](https://www.xuxueli.com/blog/static/images/img_272.png)

然后，轻点应用右上方的发布按钮，可获取AI应用链接并分享给他人或嵌入至其它网站内。

![img_273.png](https://www.xuxueli.com/blog/static/images/img_273.png)


### 7、进阶AI应用：AI Workflow

AI Workflow类AI应用可以支持更复杂业务场景，具备联网、文件识别、图像识别以及语音识别等能力；整体搭建流程复杂度相对较高，可抽象为如下几步：
- a、新建AI应用：轻点 Dify 平台首页左侧的"创建空白应用"，选择"Workflow"类型应用并命名。本文示例命名为“文本摘要生成工作流”，支持结合外部知识库整体分析，最终针对输入内容输出摘要总结。
- b、Workflow编排：进入AI应用的编排页面，以流程图方式编排业务流程节点以流转逻辑，部分编排工作包括：
  - 意图分析编排：识别用户输入意图，结合业务逻辑流转至分支链路：
  - 知识库编排：检索外部知识库信息，输入给大模型整合分析；
  - LLM节点编排：关联具体的大模型，结合节点propmt、知识库以及上下文信息，进行模型推理分析；
  - 其他：Workflow定制灵活性强，结合具体场景具体分析，此处略。

上述配置完成后，在预览对话框中输入内容，验证AI应用是否能够符合预期运行。生成预期答复意味着AI应用搭建已完成。

![img_275.png](https://www.xuxueli.com/blog/static/images/img_275.png)

然后，轻点应用右上方的发布按钮，可获取AI应用链接并分享给他人或嵌入至其它网站内。

![img_276.png](https://www.xuxueli.com/blog/static/images/img_276.png)





资料：
- [Link](https://github.com/langgenius/dify-docs/blob/main/zh_CN/learn-more/use-cases/private-ai-ollama-deepseek-dify.md) 