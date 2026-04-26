<h2 style="color:#4db6ac !important" >OpenClaw连接微信 | 你的AI助手现已支持微信通讯</h2>
> 【原创】2026/03/15

[TOCM]

[TOC]

## 1、OpenClaw连接微信

微信作为国内最主流的通讯工具，几乎承载了我们工作和生活的大部分沟通需求。    
而现在，OpenClaw 已经可以完美连接微信，用户可以通过微信聊天的方式快速与“龙虾”高效互动，让你的AI助手能够收发消息、发送文件，真正成为你的随身助理。

## 2、OpenClaw安装准备

要实现微信连接OpenClaw，需要前置完成OpenClaw安装，可以参考前序文章：  
[OpenClaw部署并集成QQ搭建自动化AI助理](https://www.xuxueli.com/blog/?blog=ai/openclaw-skill-wechat)

## 3、微信ClawBot配置

**第一步：微信升级准备**

打开微信，前往 “我的 -> 设置 -> 插件”，选中打开“微信ClawBot”插件。
当前插件仅新版可见，如果看不到需要升级微信版本、并重新进入上述插件。

![img](https://www.xuxueli.com/blog/static/images/default/2026/img_20260322_01.jpg)

**第二步：ClawBot插件安装** 

在运行OpenClaw的设备安装 openclaw-weixin-cli 插件，这个命令会自动下载并安装微信连接所需的依赖项。安装过程中可能会提示你确认一些信息，按照提示操作即可。

```
npx -y @tencent-weixin/openclaw-weixin-cli@latest install
```

上述命令执行完成后，将会显示「连接二维码」，可以通过微信扫码连接：
![img](https://www.xuxueli.com/blog/static/images/default/2026/img_20260322_02.jpg)

**第三步：微信扫码连接ClawBot**

使用微信扫描上一步中二维码，可以启用ClawBot插件并绑定OpenClaw。
然后，支持微信聊天的方式快速与“龙虾”高效互动。更多内容可自行探索～

![img](https://www.xuxueli.com/blog/static/images/default/2026/img_20260322_03.jpg)