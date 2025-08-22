<h2 style="color:#4db6ac !important" >使用N8N搭建自动化工作流平台</h2>
> 【原创】2025/08/23

[TOCM]

[TOC]

### 1、n8n 简介
n8n 是一个基于节点的可视化工作流自动化工具，它允许你通过拖拽方式连接不同的服务和应用程序。n8n 支持 200+ 种集成，包括各种 AI 服务、数据库、API 等。

n8n 主要特点：
- 开源免费：完全开源，可以免费使用
- 可视化设计：通过拖拽方式创建复杂工作流
- 丰富的集成：支持 200+ 种服务和应用程序
- 本地部署：支持本地部署，保护数据隐私
- 强大的自定义能力：支持自定义节点和函数

### 2、n8n 使用场景

| 场景 | 	传统做法	              | n8n解决                     
|--------------------|---------------------|---------------------------|
| 收到表单提交，发通知并存储      | 	人工操作	              | n8n自动串联                   
| 每天定时抓取接口数据	        | 写脚本                 | 	n8n定时器+HTTP请求            
| 项目有新Issue自动推送到群	   | 手动搬运                | 	n8n监听Webhook             
| 多平台数据同步（如CRM、ERP）	 | 人工或昂贵定制开发           | 	n8n可视化整合                 
| 结合AI接口实现智能工作流      | 	高门槛开发	             | n8n集成OpenAI等

### 3、n8n 安装和部署 

拉取镜像：
```
docker pull n8nio/n8n:1.108.1
```

运行容器：
```

// 创建目录
cd /Users/admin/program/docker/instance/n8n 
mkdir -p ./n8n 

// 运行
docker run -it --rm \
  --name n8n \
  -p 5678:5678 \
  -v $PWD/n8n:/home/node/.n8n \
  n8nio/n8n:1.108.1
```

### 4、访问 n8n 界面
安装完成后，打开浏览器访问 http://localhost:5678，你将看到 n8n 的登录界面。

首次访问时，你需要： 创建管理员账户、 设置密码 以及 选择工作区。

## 5、创建第一个 AI 工作流       
让我们创建一个简单的 AI 工作流来体验 n8n 的基本功能。

**步骤 1：创建工作流**      
登录 n8n 后，点击"New Workflow"创建新工作流。给工作流起一个名字，比如"AI工作流"。

**步骤 2：工作流编排**      
在画布上点击"+"号，选择“On chat message”，然后点击“Back to canvas”。您现在应该在画布中间看到一个新节点：

点击节点右侧的加号，选择“Ollama Chat Model”。点击“Select Credential”，Ollama Base URL 设置：http://localhost:11434 。
（docker部署使用，改为配置： http://host.docker.internal:11434 ）

接下来，点击“Save”。您应该会看到一条绿色消息，显示“Connection tested successfully”。

![img](https://www.xuxueli.com/blog/static/images/img_285.png)

**步骤 3：工作流运行**  
点击底部的“Chat”，并输入“Hello”，此时工作量将会自动运行，输出响应结果。

至此，n8n 搭建以及第一个AI工作流创建完成，可以继续探索更多复杂应用场景。


