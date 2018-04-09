

NodeJS：NodeJS是一个使用了Google高性能V8 引擎 的服务器端JavaScript实现。它提供了一个（几乎）完全非阻塞I/O栈，与JavaScript提供的闭包和匿名函数相结合，使之成为编写高吞吐 量网络服务程序的优秀平台。

NPM：全称是Node Package Manager，是一个NodeJS定制的包管理和分发工具，如node-mysql，已经成为了非官方的发布Node模块（包）的标准。
（npm类似maven，package.json类似pom.xml文件）

mongodb：非关系数据库（nosql）,缺点就是不适合数据一致性要求高的比如金融方面的开发。但是优点就快。


    nodejs 本身不支持热更新，因为所有的js代码都被编译和缓存进内存
    node和mongodb组合起来特别适合一个应用场景——速度快，处理量大的情况。
    
- nodejs官网：https://nodejs.org/en/download/
- nodejs社区：http://www.cnodejs.org
- nodejs菜鸟教程：http://www.w3cschool.cc/nodejs/nodejs-tutorial.html
- hexo博客：https://hexo.io/
- ghost博客：https://ghost.org/ 

### mac + node, 环境安装
```
brew install node
```

### windows + node，环境搭建

```
1、nodejs安装
》下载node-v4.3.1-x64.msi，开始安装nodejs，选择安装路径；安装包含：
    安装nodejs至指定目录：
    配置node环境变量：将node安装目录添加进PATH环境变量【环境变量作用：系统命令的搜索目录】
    集成安装NPM：将npm安装目录添加进PATH环境变量（全局安装的模块将安装在其中，公用环境变量，但公用时新增模块命令需要重启机器）
》校验：node -v和npm -v

2、npm安装
新版的nodejs已经集成了npm
NPM用法：
# npm install jade -g        // Jade 是一个高性能的模板引擎，它深受 Haml 影响，它是用 JavaScript 实现的，并且可以供 Node 使用。
# npm install mysql -g      // 用于链接mysql
# npm install express -g   // Express 是一个基于 Node.js 平台的极简、灵活的 web 应用开发框架，它提供一系列强大的特性，帮助你创建各种 Web 和移动设备应用。

3、安装express
# npm install express -gd
# npm install express-generator  -gd
参数说明如下：
# npm uninstall express -g                     // 卸载
# npm install express -gd                      // 选项-g表示全局安装，目标模块将会被安装到NODE_PATH的lib里面（C:\Users\Administrator\AppData\Roaming\npm）。-d选项表示一并安装依赖模块。没有-g选项的话会在当前目录（通常是项目目录）建立一个node_modules目录。
# npm install -g express@3.5.0              // 安装指定版本
# npm install express-generator  -gd     // express 4.0之后将命令工具分出来了 故还需安装express-generator
重启机器
# express -V                                        // 安装成功后，命令行会提示 npm info ok，查看版本，注意express -V中的V要大写,不然很多版本中会不识别

4、使用express 创建HelloWorld
使用express创建一个工程，新版本中命令发生了一些改变, 创建好project之后还需要用npm进行添加依赖和启动：
# express helloworld
# cd helloworld
# npm install        // 如若已有项目，可以从这一步开始
# npm start
然后新创建的helloworld就已经运行在3000端口上
访问 http://localhost:3000/ 就看到熟悉的页面了

express项目标准目录结构：
bin/
node_modules/        # 保存node.js的module扩展文件，如模板、数据库接口，可以根据package.json生成
public/                    # 静态资源
routes/                    # 存放了MVC概念中controller的处理部分，路由信息在app.js中予以定义，类似springmvc的controller
views/                     # 存放了MVC概念中view的部分，类似j2ee中的freemarker
app.js                     # Express应用程序的入口文件，存放的Express项目中最基本的配置信息，类似netty项目中main启动方法
package.json           # 描述npm包的信息，用于构建npm程序，和npm平台关联，类似maven的pom.xml文件
Tips：由于Express只是一个轻量级的Web框架，多数功能只是围绕HTTP协议中常用部分进行了封装，其中没有内置ORM，所以没有MVC概念中Model的部分，在实际项目中必须通过module来进行扩展。
```

### centos + nodejs，源码方式安装 
```
1. 安装依赖包
# yum -y install gcc gcc-c++ openssl-devel
2. 下载源码包
# wget https://nodejs.org/dist/v4.3.1/node-v4.3.1.tar.gz 
# tar -zvxf node-v4.3.1.tar.gz 
# cd node-v4.3.1
3. 配置、编译、安装
# ./configure --prefix=/usr/local/node                 （centos6.5的gcc版本低于node4要求的4.8，升级centos7或者换二进制版本安装）
# make && make install
4. 配置nodejs环境
# vim /etc/profile
------
#set nodejs env
export NODE_HOME=/usr/local/node
export PATH=$NODE_HOME/bin:$PATH
export NODE_PATH=$NODE_HOME/lib/node_modules:$PATH
------
# source /etc/profile       #重启生效
5. 测试是否安装成功
# node -v
# npm -v
```

### centos + nodejs，二进制文件方式安装（更绿色，推荐）
```
1. 二进制文件安装
# yum -y install xz 
# wget https://nodejs.org/dist/v4.3.1/node-v4.3.1-linux-x64.tar.xz 
# tar -xvf node-v4.3.1-linux-x64.tar.xz 
# mv node-v4.3.1-linux-x64 /usr/local/node
# cd /usr/local/node
# cd bin
# ./node -v
2. 配置nodejs环境
# vim /etc/profile 
------
#set nodejs env
export NODE_HOME=/usr/local/node
export PATH=$NODE_HOME/bin:$PATH
export NODE_PATH=$NODE_HOME/lib/node_modules:$PATH
------
# source /etc/profile       #重启生效
3. 测试是否安装成功
# node -v
# npm -v

2、npm安装
同windows
3、安装express
同windows
4、使用express 创建HelloWorld
同windows
5、node后台运行（利用nohup）（node的forever命令启动也很方便）
# nohup node index.js > myLog.log 2>&1 &
# nohup npm start > myLog.log 2>&1 &
# ps -ef|grep node
# kill -9 2179
// halt -- reboot
```

### npm国内镜像站，以及使用方法：
```
淘宝NPM镜像站：http://npm.taobao.org/

永久使用镜像命令： # npm config set registry https://registry.npm.taobao.org 
临时使用镜像命令： # npm --registry "https://registry.npm.taobao.org" install 某一个包
```

### 源码实例
```
示例helloworld：

var http = require("http"); 
http.createServer(function(request, response) { 
response.writeHead(200, {"Content-Type": "text/html"}); 
response.write("Hello World!"); 
response.end(); 
}).listen(8080); 
console.log("Server running at http://localhost:8080/");

打开命令行，转到当前文件所存放的路径下，运行 node helloworld.js命令即可；
如果一切正常，可以看到命令行输出：Server running at http://localhost:8080/；
同时，在浏览器输入http://localhost:8080/，可以看到一个写着helloworld的网页。
```

### hexo博客搭建：适合小型个人博客
```
原理》博客数据存储在md文件中；使用hexo命令新建md文件，编辑，发布，程序自动加载md文件生成博客站点；
hexo博客：https://hexo.io/   
github：https://github.com/hexojs/hexo/ 
hexo主题列表：https://github.com/hexojs/hexo/wiki/Themes  
hexo博客主题01：https://github.com/iissnan/hexo-theme-next    （官网给出一些优秀开源主题，github上页也有许多开源主题，自己有兴趣也可以掉API制作）
hexo博客主题02：https://github.com/litten/hexo-theme-yilia 

hexo安装》详细教程查看官方文档，简单安装步骤如下
# npm install hexo-cli -g     （已经通过npm集成为通用包）
# hexo init blog
# cd blog
# npm install
# hexo generate        （静态化整站）
# hexo server            （启动server）
# hexo deploy        （配置git后，可同步到github）

主题安装》例如next主题
1.下载主题包解压： 前往github下载release主题包hexo-theme-next-0.4.5.2，解压在themes目录下
2. 修改项目配置：修改根目录下配置文件 _config.yml：找到默认主题【theme: landscape】修改为【theme: hexo-theme-next-0.4.5.2】即可；
3. 修改主题配置：按照next在github给定的主题文档（http://theme-next.iissnan.com/five-minutes-setup.html）自行定制；
```

### ghost博客搭建：适合小型个人博客（依赖太多，ghost-0.7.8版本安装后体积达到200M+，且需要翻墙或者切换国内镜像站才可以安装）
```
原理》博客数据存储在sqlite3数据库文件中（可修改为mysql）；图片存储在本地文件夹中；编辑器采用markdown；
官网：https://ghost.org/
github：https://github.com/TryGhost/Ghost
主题库：http://marketplace.ghost.org/
中文网：http://docs.ghostchina.com/zh/

安装》
    1. 官网下载源码包
    2. 安装：# npm install --production
    3. 启动：（详细安装查看官网文档 http://support.ghost.org/installation/）
        本地启动：# npm start
        服务器启动# npm start --production
    提示：Error: Cannot find module 'mime-db'；解决：临时使用镜像命令，按照提示一个一个的安装墙内无妨访问的npm包

Ghost管理使用》
    1. 首次进入，注册管理员：访问 “http://localhost:2368/ghost”
    2. 博客Settings一般设置中，可修改博客标题，描述，Logo，每页pagesize，主题等；
    3. 在User设置可以对作者进行相应修改；
    4. 发布文章使用Markdown语法，基本语法比较简单，特别是当输入" ![]() "时，编辑器会出现图片上传框，可以拖动图片上传；
    5. 给Ghost博客设置SMTP只需要编辑：vim config.js，在production下的Mail中加入SMTP信息即可。
    6. Ghost博客后台去掉Google Fonts需要进入到：core/server/views/default.hbs和core/server/views/user-error.hbs，把里面的fonts.googleapis.com链接删除了。
    7. 默认的主题去掉Google Fonts需要进入到：content/themes/casper/default.hbs，把里面的fonts.googleapis.com链接删除了。
    8. Ghost博客备份与恢复。Ghost 博客的所有文章内容都是存储在 sqlite3 数据库中的，其位置是 /content/data/ghost.db。另外，所有上传的图片都放在了 /content/images/ 目录下。
    9. Ghost博客自带了一个备份与恢复的页面，地址是：域名/ghost/debug/。 点击 Export 按钮就可以将博客内容导出为 .json 文件，还有一个导入工具 Import ，可以将 .json 格式的备份内容导入Ghost 系统。 最后一个红色按钮 Delete all content 是用来删除所有内容（即清空数据库）。
```
