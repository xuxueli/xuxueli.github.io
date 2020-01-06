<h2 style="color:#4db6ac !important" >Docker</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]

## 相关地址
- 官网：http://www.docker.com/    
- 中文文档：http://www.widuu.com/docker/  
- 教程：http://www.runoob.com/docker/docker-tutorial.html

## Docker是什么？

简单得来说，Docker是一个由GO语言写的程序运行的“容器”（Linux containers， LXCs）。
目前云服务的基石是操作系统级别的隔离，在同一台物理服务器上虚拟出多个主机。
Docker则实现了一种应用程序级别的隔离； 它改变我们基本的开发、操作单元，由直接操作虚拟主机（VM）,转换到操作程序运行的“容器”上来。

Docker是为开发者和系统管理员设计的，用来发布和运行分布式应用程序的一个开放性平台。
由两部分组成：

    Docker Engine: 一个便携式、轻量级的运行环境和包管理器。（注* 单OS vs 单线程，是不是跟NodeJS特别像？）
    Docker Hub: 为创建自动化工作流和分享应用创建的云服务组成。（注* 云端镜像/包管理 vs npm包管理，是不是跟npm特别像？）

从2013年3月20日，第一个版本的Docker正式发布到 2014年6月Docker 1.0 正式发布，经历了15个月。 虽然发展历程很短，但Docker正在有越来越流行的趋势。

其实Container技术并非Docker的创新，HeroKu, NodeJitsu 等云服务商都采用了类似这种轻量级的虚拟化技术，但Docker是第一个将这这种Container技术大规模开源并被社区广泛接受的。  

好的部分：Docker相对于VM虚拟机的优势十分明显，那就是轻量和高性能和便捷性， 以下部分摘自：KVM and Docker LXC Benchmarking with OpenStack

    快：运行时的性能可以获取极大提升（经典的案例是提升97%），管理操作（启动，停止，开始，重启等等） 都是以秒或毫秒为单位的。
    敏捷：像虚拟机一样敏捷，而且会更便宜，在bare metal（裸机）上布署像点个按钮一样简单。
    灵活：将应用和系统“容器化”，不添加额外的操作系统，
    轻量：你会拥有足够的“操作系统”，仅需添加或减小镜像即可。在一台服务器上可以布署100~1000个Containers容器。
    便宜：开源的，免费的，低成本的。由现代Linux内核支持并驱动。注* 轻量的Container必定可以在一个物理机上开启更多“容器”，注定比VMs要便宜。
    生态系统 ：正在越来越受欢迎，只需要看一看Google的趋势就知道了，docker or LXC. 还有不计其数的社区和第三方应用。
    云支持 ：不计其数的云服务提供创建和管理Linux容器框架。
    有关Docker性能方面的优势，还可参考此IBM工程师对性能提升的评测，从各个方面比VMs（OS系统级别虚拟化）都有非常大的提升。


有争论的部分：任何项目都会有争论，就像Go，像NodeJS, 同样Docker也有一些。

    能否彻底隔离：在超复杂的业务系统中，单OS到底能不能实现彻底隔离，一个程序的崩溃/内存溢出/高CPU占用到底会不会影响到其他容器或者整个系统？很多人对Docker能否在实际的多主机的生产环境中支持关键任务系统还有所怀疑。 注* 就像有人质疑Node.JS单线程快而不稳，无法在复杂场景中应用一样。
    不过可喜的是，目前Linux内核已经针对Container做了很多改进，以支持更好的隔离。
    GO语言还没有完全成熟：Docker由Go语言开发，但GO语言对大多数开发者来说比较陌生，而且还在不断改进，距离成熟还有一段时间。此半git、半包管理的方式让一些人产生不适。
    被私有公司控制：Docker是一家叫Dotcloud的私有公司设计的，公司都是以营利为目的，比如你没有办法使用源代码编绎Docker项目，只能使用黑匣子编出的Docker二进制发行包，未来可能不是完全免费的。目前Docker已经推出面向公司的企业级服务。


## 开发者可以使用Docker做什么？  
英文原文：[Docker Use Cases](http://rominirani.com/2015/04/09/docker-use-cases/)

Docker 如今赢得了许多关注，很多人觉得盛名之下其实难副，因为他们仍然搞不清 Docker 和普通开发者到底有什么关系。许多开发者觉得 Docker 离自己很远，Docker 是生产环境中的工具，和自己无关。我也是花了很长时间才想清楚作为普通开发人员如何在自己的开发中使用 Docker。坦率地说，我仍处在学习的过程中。

这篇文章提供了一个 Docker 用例列表，我希望它能更好地帮助你理解 Docker 并引发你的思考。本文只是描述 Docker 在普通开发者日常的应用，并不提供完整的解决方案。

在介绍用例之前，我希望你能先记住这句话：“Docker 是一个便携的应用容器”。你可以不知道 Docker 所说的的“便携式容器”到底是什么意思，但是你必须清楚 Docker 在日常中能带来非常大的效率提升。

当你需要在容器内运行自己的应用（当然可以是任何应用），Docker 都提供了一个基础系统镜像作为运行应用时的基础系统。也就是说，只要是 Linux 系统上的应用都可以运行在 Docker 中。

    可以在 Docker 里面运行数据库吗？当然可以。
    可以在 Docker 里面运行 Node.js 网站服务器吗？当然可以。
    可以在 Docker 里面运行 API 服务器吗？当然可以。
    
Docker 并不在乎你的应用程序是什么、做什么，Docker 提供了一组应用打包、传输和部署的方法，以便你能更好地在容器内运行任何应用。
下面的例子我自己经常使用，当然你有更好的案例也可以分享给我。

### 尝试新软件
对开发者而言，每天会催生出的各式各样的新技术都需要尝试，然而开发者却不太可能为他们一一搭建好环境并进行测试。时间非常宝贵，正是得益于 Docker，让我们有可能在一条或者几条命令内就搭建完环境。Docker 有一个傻瓜化的获取软件的方法，Docker 后台会自动获得环境镜像并且运行环境。
并不仅仅是新技术环境搭建用得到 Docker。如果你想快速在你的笔记本上运行一个 MySQL 数据库，或者一个 Redis 消息队列，那么使用 Docker 便可以非常容易地做到。例如 Docker 只需要一条命令便可以运行 MySQL 数据库：docker run -d -p 3306:3306 tutum/mysql。
译者注：虽然使用命令也能非常快地安装 MySQL 数据库，但是当用到最新的技术或者非常复杂的技术时，使用 Docker 便会是个非常好的选择，例如 Gitlab，普通用户大概需要一天的时间去搭建 Gitlab 平台，而 Docker 则只需要一条命令。

### 进行演示
现在我经常需要在周末用自己开发的成果对客户活着别人做一两个演示。搭建演示环境的过程非常麻烦。现在我发现 Docker 已经成为我演示这些工具的最合理的方式。同时，对于客户来说，我可以直接将 Docker 镜像提供给他们，而不必去做任何环境配置的工作，工作的效果也会和在他们演示中所看到的一模一样，同时不必担心他们的环境配置会导致我们的产品无法运行。
避免“我机器上可以运行”
无论是上一篇介绍的企业部署 Docker 还是本文的个人 Docker 用例，都提到了这个情况。因为环境配置不同，很多人在开发中也会遇到这个情况，甚至开发的软件到了测试人员的机器上便不能运行。但这都不是重点。重点是， 如果我们有一个可靠的、可分发的标准开发环境，那么我们的开发将不会像现在这么痛苦。Docker 便可以解决这个问题。Docker 镜像并不会因为环境的变化而不能运行，也不会在不同的电脑上有不同的运行结果。可以给测试人员提交含有应用的 Docker 镜像，这样便不再会发生“在我机器上是可以运行的”这种事情，很大程度上减轻了开发人员测试人员互相检查机器环境设置带来的时间成本。
另一个 Docker 可以发挥用处的地方是培训班。除了 Docker 容器的隔离性之外，更能体会到 Docker 优势的地方在于环境搭建。培训班的新手每个人都要在环境搭建上花费很多时间，但是如果在这里应用到 Docker 的话，那么我们只需要把标准的运行环境镜像分发下去，然后就可以开始上课了。使用 Docker 和使用虚拟机一样简单，但是 Docker 要更方便、更轻量级。同时，我们也可以告诉学员：“在培训的同时，我们还将学到当下最流行的技术——Docker”，这种双赢的结局，何乐而不为呢。

### 学习 Linux 脚本
当然这个原因看起来可能很奇怪，但是对不不熟悉 Linux 操作系统和 Shell 脚本的人来说，确实是一个好机会。即便本文并不是在讲 Linux，Linux 的重要度仍然不言而喻。如果你用的是 Windows，那么我给你一个建议：从云主机提供商那儿租用一台云主机：我推荐使用 CoreOS 系统的云主机。虽然这样并不会让你成为专业的 Linux 运维，但是可以让你快速地学到 Linux 基础知识，爱上命令行操作，并且慢慢开始熟悉和欣赏 Linux。

### 更好地利用资源
虚拟机的粒度是“虚拟出的机器”，而 Docker 的粒度则是“被限制的应用”，相比较而言 Docker 的内存占用更少，更加轻量级。
对我来说这是 Docker 的一个优势：因为我经常在自己电脑中运行多个 Docker 应用，使用 Docker 比使用虚拟机更加简单，方便，粒度更细，也能持续地跟踪容器状态。

### 为微服务定制
如果你一直在关注科技新闻的话，那么你应该听说过“微服务（Microservices）”的概念。Docker 可以很好地和微服务结合起来。从概念上来说，一个微服务便是一个提供一整套应用程序的部分功能，Docker 便可以在开发、测试和部署过程中一直充当微服务的容器。甚至生产环境也可以在 Docker 中部署微服务。
在云服务提供商之间移植
大多数的云主机提供商已经全面支持 Docker。对于开发人员来说，这表示你可以很方便地切换云服务提供商，当然也可以很方便地将你本地的开发环境移动到云主机上，不需要本地上配置一次运 行环境、在云主机上还配置一次运行环境。全面部署 Docker (Docker here and Docker there) 作为标准运行环境可以极大地减轻应用上线时的工作量和产生 BUG。

### API 端
API 是应用之间的粘合剂，一个合格开发者肯定使用过别人提供的 REST API，或者自己开发过 REST API。需要指出的是，无论是客户端还是 API 提供端，在开发之前都需要先定义一组公共的 API 接口，写成文档，然后才能进行编码。如果服务端和客户端是共同开发的话，那么服务端通常会先实现能返回固定字符串的 API 接口，在以后的开发中再慢慢去实现 API 的功能。
虽然有人会认为在这里 Docker 被滥用了，完全可以用 sample.json 这种文件去实现虚拟 API，但是下面有个实例可以更好地解决前后端分离开发时的 API 问题。
为了更好地解释我的意思，给大家提供一个实例：JSON Server，一个用于提供 JSON 数据的 REST API。使用过这个容器的人就会知道，既然有这么好用的 Docker JSON Server，我们没有理由不用 Docker。

### 技术的创新
这点应该算不上是用例，但是我还是来写一下。Docker 正在快速发展，工具也在不断更新，没有人能预见到未来 Docker 会是什么样子的。你在复杂的系统中 Docker 使用的越多，越是可能会发现技术上的空白和未来技术发展的方向。现在还处在 Docker 的发展期，任何你使用 Docker 创建的工具都有可能成为社区关注的热点。这是 Docker 的机会，也是成就你自己的机会。

### 你的用例
最后一条便不再是我的用例了，而是 Docker 在你手中能发挥多大的作用。我也很希望看到你能提供更多使用 Docker 的方式，欢迎留言。

### 其他
还有两个技巧可以分享给你们。在学习 Docker 的过程中因为有了这两个的帮助，我才得意不断地提升自己。

- 一：Docker Hub Registry。这是 Docker 的官方镜像仓库，除了托管着 Docker 官方的镜像外，和 Github 一样，你可以在上面上传自己的镜像，也可以在上面搜寻其他有用的镜像，极大地节省自己的时间。例如 Oracle-XE-11g 镜像，所有的一切都是现成的，完全不需要自己去下载 Oracle XE 11g 安装。这样为你和团队节约了大量的时间成本。如果你不太确定的话，可以去 Docker Hub 上搜有一下有没有自己用得到的镜像。大部分情况下你所需要的镜像在 Docker Hub 上都已经有人构建了。

- 二：多参考 IaaS 供应商的新闻，虽然我们不能像在他们会议室里那样完全了解他们的公司动态，但是仍然可以从新闻中可以了解到 Docker 最新的发展方向和技术趋势。可以肯定的是，容器化技术是未来的热点，我们不仅可以在本机运行 Docker，不仅仅在一家云服务提供商的主机上运行 Docker，未来所有的云服务提供商都会支持 Docker。Docker 前景很明确，采用 Docker 只会让开发变得更方便。果你不太确定的话，可以去 Docker Hub 上搜有一下有没有自己用得到的镜像。大部分情况下你所需要的镜像在 Docker Hub 上都已经有人构建了。

## 安装Docker
参考：https://www.runoob.com/docker/macos-docker-install.html

- win 安装 docker

    - 方式1：Win10 Pro（Hyper-V） + Docker-for-Windows-Installer.exe
    - 方式2：Win（CPU 虚拟） + DockerToolbox.exe
        - DockerToolbox 更换官方的中国区加速器
        ```
        docker-machine ssh default
        // DOCKER_OPTS新增一行参数
        --registry-mirror https://registry.docker-cn.com
        docker-machine restart default
        ```
        
- mac安装docker
    - 环境要求；https://docs.docker.com/docker-for-mac/install/#what-to-know-before-you-install
    - brew 安装
        ```
        brew install docker
        或者：
        brew update
        brew upgrade docker
        ```
    - 手动安装：下载DMG镜像安装

## 常用命令
参考：https://www.runoob.com/docker/docker-command-manual.html

```
docker --version
docker images
docker rmi images_id

docker ps
docker ps -a
docker rm second-mysql

docker logs -f zookeeper

docker exec -it 47fec42abbb7 /bin/sh
docker exec -it 47fec42abbb7 /bin/bash

docker inspect --format='{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)


// redis

docker start redis
docker stop redis
docker run -p 6379:6379 --name redis -v $PWD/data:/data  -d redis:4.0 redis-server --appendonly yes
/*
-p 6379:6379 : 将容器的6379端口映射到主机的6379端口
-v $PWD/data:/data : 将主机中当前目录下的data挂载到容器的/data
redis-server --appendonly yes : 在容器执行redis-server启动命令，并打开redis持久化配置
*/



// mysql

cd /Users/xxx/programfils/plugin/docker/mysql

mkdir -p ./data ./logs ./conf
/*
data目录将映射为mysql容器配置的数据文件存放路径
logs目录将映射为mysql容器的日志目录
conf目录里的配置文件将映射为mysql容器的配置文件
*/

// 参考文档：https://www.cnblogs.com/zqifa/p/mysql-6.html
docker run -p 3306:3306 --name mysql -v $PWD/conf:/etc/mysql/conf.d -v $PWD/logs:/var/log/mysql -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root_pwd -d mysql:5.7


cd /Users/xxx/programfils/plugin/docker/mysql
mkdir -p ./data
docker run -p 3306:3306 --name mysql -v $PWD/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root_pwd -d mysql:5.7  --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci 

/*
-p 3306:3306：将容器的3306端口映射到主机的3306端口
-v $PWD/conf:/etc/mysql/conf.d：将主机当前目录下的conf/my.cnf挂载到容器的/etc/mysql/my.cnf
-v $PWD/logs:/var/log/mysql：将主机当前目录下的logs目录挂载到容器的/logs
-v $PWD/data:/var/lib/mysql：将主机当前目录下的data目录挂载到容器的/mysql_data
-e MYSQL_ROOT_PASSWORD=123456：初始化root用户的密码
*/

// 同步时区
docker cp /etc/localtime mysql:/etc/localtime
// 同步时区2，重启后确认，参考：https://blog.csdn.net/zhangchao19890805/article/details/52690473
docker exec -it <image-id> /bin/bash  
cp /usr/share/zoneinfo/PRC /etc/localtime
date -R

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

// springboot
// maven配置 + DockerFile 
mvn clean package docker:build

// docker run
docker run -p 8080:8080 -v /tmp:/data/applogs --name xxl-code-generator-admin-0.0.2-SNAPSHOT  -d xxl-code-generator-admin:0.0.2-SNAPSHOT
docker run -e PARAMS="--mysqladdress=172.17.0.2:3306 --zkaddress=172.17.0.3:2181" -p 8080:8080 -v /tmp:/data/applogs --name xxl-conf-admin-1.5.0-SNAPSHOT  -d xxl-conf-admin:1.5.0-SNAPSHOT


// 推送镜像至docker-hub仓库
1、锁定镜像：docker images
2、登陆Hub：docker login (或 docker login docker.io )
3、tag镜像：docker tag <imageID> <namespace>/<image name>:<version tag eg latest>
4、push镜像：docker push <namespace>/<image name>:<version tag eg latest>

```
    