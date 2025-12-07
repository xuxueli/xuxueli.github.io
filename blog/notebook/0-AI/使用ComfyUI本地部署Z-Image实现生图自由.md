<h2 style="color:#4db6ac !important" >使用ComfyUI本地部署Z-Image实现生图自由</h2>
> 【原创】2025/12/07

[TOCM]

[TOC]

### 1、Z-Image 简介

Z-Image（中文名“造相”）是阿里巴巴通义实验室研发并开源的高效图像生成基础模型，定位为“轻量且高性能”的AI图像解决方案，核心参数规模仅6B，却能对标参数量20B以上的闭源旗舰模型。
Z-Image发布之后快速成为开源图像生成领域的焦点，核心源于其“效率、质量、易用性”三者的平衡，具体功能特色可分为五大维度：

- 极致高效：仅需 8 步推理，H800 上亚秒级延迟；
- 低显存需求：16G 显存消费级显卡即可运行；
- 照片级质量：出色的写实图像生成能力；
- 双语文字渲染：准确渲染中英文复杂文字；
- 强指令遵循：精准理解和执行用户的文字描述；
- 开源可用：模型权重在 Hugging Face 和 ModelScope 开放下载；


### 2、ComfyUI 简介

ComfyUI是一款基于节点流程的AI图像/视频生成设计工具，通过将深度学习模型的工作流程简化为图形化节点，使用户能够通过拖放操作构建和调整生成流程。
ComfyUI不仅支持静态图像生成，更具备视频生成与序列化处理能力，是当前支持SD、SDXL、 Flux、LCM、AnimateDiff、Z-Image等主流AI模型较全面的开源解决方案。

核心特性：
- 全模态生成支持：支持图像/视频/3D多模态生成、帧序列动画与视频插值、实时预览生成效果；
- 模型全兼容体系：自动识别safetensors/ckpt格式，预集成SD、 Flux等数十个热门模型；
- 可视化节点编程：300+专业生成节点自由组合，支持Python自定义节点开发，节点版本自动管理；

### 3、使用ComfyUI本地部署Z-Image

##### 3.1、安装 ComfyUI

ComfyUI 提供多种安装方式，非研发人员推荐安装 ComfyUI Desktop；
>ComfyUI 安装非本文重心，参考文末ComfyUI资料安装即可。

##### 3.2、下载 Z-Image 模型

前往 huggingface 下载 diffusion_model/text_encoder/vae 三个模型文件，把下载下来的文件放到 ComfyUI 如下目录位置：

```
ComfyUI/
├── models
    └── diffusion_models
        └── z_image_turbo_bf16.safetensors
    └── text_encoders
        └── qwen_3_4b.safetensors
    └── vae
        └── ae.safetensors
```

至此，ComfyUI与Z-Image已安装配置完成。启动 ComfyUI 可看到如下界面：

![img](https://www.xuxueli.com/blog/static/images/img_286.jpg)


### 4、生成第一张图片

让我们运行一个简单的 “问生图” 工作流来体验 Z-Image 的基本功能。

**步骤 1：创建工作流**

进入 ComfyUI 后点击"创建空白工作流"，可参考下图进行工作流配置。这里我们采用提示词：
> 极具氛围感的暗调人像，一位优雅的中国美女在黑暗的房间里。一束强光通过遮光板，在她的脸上投射出一个清晰的闪电形状的光影，正好照亮一只眼睛。高对比度，明暗交界清晰，神秘感，莱卡相机色调。

![img](https://www.xuxueli.com/blog/static/images/img_287.jpg)

**步骤 2：运行工作流**

生图工作流配置完成后，点击"运行"按钮后等待一段时间，即可看到生成的图片。针对步骤1中提示词，所生成图片如下：
> 本文案例使用 macmini m4 运行，生成图片速度略慢（如有提速建议可反馈联系），可借助本案例体验生图效果。

![img](https://www.xuxueli.com/blog/static/images/img_288.png)


### 参考资料

- Z-Image仓库：https://github.com/Tongyi-MAI/Z-Image
- Z-Image模型下载：https://huggingface.co/Comfy-Org/z_image_turbo/tree/main/split_files
- ComfyUI官方文档：https://docs.comfy.org/zh-CN
- Z-Image工作流：https://github.com/Comfy-Org/workflow_templates/blob/main/templates/image_z_image_turbo.json


