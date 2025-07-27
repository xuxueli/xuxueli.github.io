<h2 style="color:#4db6ac !important" >MacOS上Colima与Docker安装配置</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]


## Colima
Colima是一个免费的开源容器运行时，它使用QEMU在虚拟机中运行Docker容器。它是由Lima Project创建的，Lima项目是一群致力于创建工具以方便在 macOS上运行容器化应用程序的开发人员。

Lima (Linux virtual machines (on macOS, in most cases)) 项目由一群MacBook开发人员用户于2019年启动，出于对macOS缺乏良好的容器运行时和工具的不满而DIY。当时，在 macOS 上运行Docker容器的唯一选择是适用于macOS的Docker Desktop，它需要大中型公司的许可证。Lima项目着手为macOS创建 Docker Desktop的免费开源替代方案，以提供更好的性能和更多功能。

Colima的主要特点包括：
- 支持多种芯片架构 - 完美支持搭载Intel和Apple Silicon芯片的Mac设备，以及Linux系统
- 简单的命令行界面 - 提供直观的CLI操作方式，并采用合理的默认配置
- 自动端口转发 - 智能处理容器端口映射，简化网络配置
- 卷挂载支持 - 支持在容器和主机之间共享文件系统
- 多实例管理 - 可以同时运行多个独立的Colima实例
- 灵活的运行时选择
- 支持多种容器运行时：
    - Docker（可选择性集成Kubernetes）
    - Containerd（可选择性集成Kubernetes）
    - Incus（支持容器和虚拟机）
    - 在底层实现上，Colima 通过 Lima 启动一个专用的Linux虚拟机来运行容器。



## 二、安装步骤

### 1. 安装 Colima 和相关工具

```Shell
# 安装 colima
brew install colima docker docker-compose
```

### 2. 验证安装

```Shell
# 检查版本
colima version
docker --version
docker-compose --version
```

## 三、基础配置

### 1. 首次启动

```Shell
# 启动 colima（使用默认配置，2G 2Core 60GB）
colima start

# 如果想调整参数
colima start -h
# e.g. 4G 2Core 30GB
colima start --memory 4 --disk 30
colima start --mount-type 9p
colima start --mount-type sshfs

# 停止 colima
colima stop
# 删除 colima
colima delete
```

### 2. 验证运行状态

```Shell
# 查看状态
colima status

# 测试 docker 连接
docker info
```

## 四、配置镜像源（换源）

### 1. 编辑配置文件

```Shell
# 方式1、编辑 colima 配置文件
vi ~/.colima/default/colima.yaml

# 方式2、配置 colima
colima template
```

### 2. 添加镜像源配置

在配置文件中找到配置关键字，进行修改：

```YAML
## 配置磁盘容量，单位G
disk: 20

## 配置镜像源
docker:
  registry-mirrors:
    - https://docker.1ms.run
    - https://docker.1panel.live
    - https://docker.mirrors.ustc.edu.cn
    - https://hub-mirror.c.163.com
```

### 3. 重启生效

```Shell
# 重启 colima
colima restart
```

### 4. 验证镜像源

```Shell
# 查看镜像源配置
docker info | grep -A 10 "Registry Mirrors"

# 测试拉取镜像
docker pull hello-world
```

## 五、配置代理

### 1. 环境变量方式配置代理

```Shell
# 设置代理环境变量
export HTTP_PROXY=http://127.0.0.1:7890
export HTTPS_PROXY=http://127.0.0.1:7890
```

### 2. 重启生效

```Shell
# 重启 colima
colima restart
```

## 其他

### 资料
https://www.zhangbj.com/p/1470.html
https://blog.kelu.org/tech/2025/05/01/macos-colima.html
https://www.cnblogs.com/heei/p/18574414

