<h2 style="color:#4db6ac !important" >Docker</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]

## Docker资料
- 官网：http://www.docker.com/
- 中文文档：http://www.widuu.com/docker/
- 教程：http://www.runoob.com/docker/docker-tutorial.html

## Docker是什么

Docker 是一个开源的应用容器引擎，基于 Go 语言 并遵从 Apache2.0 协议开源。
Docker 可以让开发者打包他们的应用以及依赖包到一个轻量级、可移植的容器中，然后发布到任何流行的 Linux 机器上，也可以实现虚拟化。
容器是完全使用沙箱机制，相互之间不会有任何接口（类似 iPhone 的 app）,更重要的是容器性能开销极低。

## Docker 的应用场景

- **微服务架构**：每个服务独立容器化，便于管理和扩展。
- **CI/CD流水线**：与 Jenkins/GitLab CI 集成，实现自动化构建和测试。
- **开发环境标准化**：新成员一键启动全套依赖服务（如数据库、消息队列）。
- **云原生基础**：Kubernetes 等编排工具基于 Docker 管理容器集群。

## 核心优势
- **跨平台一致性**：解决"在我机器上能跑"的问题，确保开发、测试、生产环境一致。
- **资源高效**：容器直接共享主机内核，无需虚拟化整个操作系统，节省内存和 CPU。
- **快速部署**：秒级启动容器，支持自动化扩缩容。
- **隔离性**：每个容器拥有独立的文件系统、网络和进程空间。

# 核心概念
- **容器**（Container）：轻量化的运行实例，包含应用代码、运行时环境和依赖库。基于镜像创建，与其他容器隔离，共享主机操作系统内核（比虚拟机更高效）。
- **镜像**（Image）：只读模板，定义了容器的运行环境（如操作系统、软件配置等）。通过分层存储（Layer）优化空间和构建速度。
- **Dockerfile**：文本文件，描述如何自动构建镜像（例如指定基础镜像、安装软件、复制文件等）。
- **仓库**（Registry）：存储和分发镜像的平台，如 Docker Hub（官方公共仓库）或私有仓库（如 Harbor）。


## 安装Docker
参考：https://www.runoob.com/docker/macos-docker-install.html

### 方式1、Docker Desktop
参考：https://docs.docker.com/desktop/setup/install/mac-install/#what-to-know-before-you-install 

### 方式2、HomeBrew
参考：https://www.runoob.com/docker/macos-docker-install.html

## Docker命令
参考：https://www.runoob.com/docker/docker-command-manual.html

### 常用命令
```
docker --version
docker images
docker rmi images_id

docker ps
docker ps -a
docker rm second-mysql
```

容器内部查看：
```
docker logs -f zookeeper

docker exec -it 47fec42abbb7 /bin/sh
docker exec -it 47fec42abbb7 /bin/bash

// 查看节点IP端口
docker inspect --format='{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)
```


### 部署常用软件

常用场景，如 mysql、redis、elasticsearch 等，见相关文档资料。

zookeeper：  
```
// zookeeper
docker pull zookeeper:3.4.12

cd /Users/xxx/programfils/plugin/docker/zookeeper 
mkdir -p ./conf ./data

// touch conf/zoo.cfg
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/opt/zookeeper/data
clientPort=2181

docker run -p 2181:2181 --name zookeeper -v $PWD/conf/zoo.cfg:/opt/zookeeper/conf/zoo.cfg  -v $PWD/data:/opt/zookeeper/data  -d zookeeper:3.4.12
// --restart=always ：开机启动
```

## Docker打包镜像

### 常规服务镜像打包：
```
// springboot
// 方式1：分步操作
mvn clean package
docker build -t xuxueli/xxl-xxx:{version} ./bbb

// docker run
docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai" -p 8080:8080 -v /tmp:/data/applogs --name xxl-job-admin  -d xuxueli/xxl-job-admin:{指定版本}

// 推送镜像至docker-hub仓库
1、锁定镜像：docker images
2、登陆Hub：docker login (或 docker login docker.io )
3、tag镜像：docker tag <imageID> <namespace>/<image name>:<version tag eg latest>
4、push镜像：docker push <namespace>/<image name>:<version tag eg latest>
```

### 多架构打包：
```
// 查看架构
docker inspect --format='{{.Architecture}}' <image_name_or_id>

// AMD架构 ：pull 方式1
docker pull openjdk:8-jre-slim --platform linux/amd64
docker pull openjdk:17-jdk-slim --platform linux/amd64
docker pull openjdk:21-jdk-slim --platform linux/amd64

// AMD架构 ：pull 方式2
export DOCKER_DEFAULT_PLATFORM=linux/amd64
docker pull openjdk:8-jre-slim

// AMD：build 
docker build -t xuxueli/xxl-job-admin:{指定版本} ./xxl-job-admin --platform linux/amd64  

// AMD：run
docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai" -p 8080:8080 -v /tmp:/data/applogs --name xxl-job-admin  -d xuxueli/xxl-job-admin:{指定版本} --platform linux/amd64

// AMD：run2
docker run -d \
 -e PARAMS="--spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai" \
 -p 8080:8080 \
 -v /tmp:/data/applogs \
 --name xxl-job-admin \
 --platform linux/amd64 \
 xuxueli/xxl-job-admin:{指定版本}

// 问题：DeadlineExceeded: failed to fetch oauth token 
// 配置镜像，ipv6 proxy不生效导致；参考：https://zhuanlan.zhihu.com/p/635984165 
```

## Docker Compose

Docker Compose 是一个用于定义和运行多容器 Docker 应用程序的工具。你可以使用 YAML 文件来配置应用程序的服务，然后通过单个命令启动或停止所有服务。

基本命令：   
```
// 创建并启动容器
docker-compose up
// 创建并启动容器 （后台方式） 
docker compose up -d 
// 停止并移除容器、网络等
docker-compose down
// 启动已存在的容器
docker-compose start
// 停止运行中的容器
docker-compose stop
// 重启容器
docker-compose restart
// 列出当前项目中的所有容器
docker-compose ps
// 查看容器的日志输出
docker-compose logs
```

构建与拉取镜像：        
```
// 构建或重新构建服务
docker-compose build
// 拉取服务镜像 
docker-compose pull
// 推送服务镜像
docker-compose push
```

管理服务：       
```
// 创建服务但不启动它们
docker-compose create
// 强制停止服务容器
docker-compose kill
// 移除已停止的服务容器
docker-compose rm
// 设置服务运行的容器数量
docker-compose scale 
```

执行命令：       
```
// 在运行的容器中执行命令
docker-compose exec
// 在服务上运行一次性命令
docker-compose run
```

常用命令：   
```
// maven项目编译打包，然后启动容器
mvn clean package
docker-compose up -d                    // 启动容器，并按需构建不存在镜像
docker-compose up --build               // 启动容器，并强制重新构建镜像
A=1 B=2 docker-compose up               // 启动容器，并传递参数；脚本中通过 ${A:-default} 承接参数； 
```

## Docker Hub镜像超时解决
参考：
[link1](https://gitee.com/wanfeng789/docker-hub)
[link2](https://www.runoob.com/docker/docker-mirror-acceleration.html)

### centos 

对于使用 systemd 的系统，请在 /etc/docker/daemon.json 中写入如下内容（如果文件不存在请新建该文件）：
```
{
    "registry-mirrors": [
        "https://docker.1ms.run",
        "https://hub.rat.dev",
        "https://docker.1panel.live"
    ]
}
```

之后重新启动服务：
```
$ sudo systemctl daemon-reload
$ sudo systemctl restart docker
```

### Mac OS 
对于 Mac 和 Windows 用户，直接在 Docker Desktop 系统设置中，配置 registry-mirrors 即可。

