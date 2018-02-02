### 环境搭建：WebStorm8 + Cocos2d-html5-v2.2.3 + Chrome（JetBrains-IDE-support ）

- 可参考：http://cn.cocos2d-x.org/tutorial/show?id=1105  
- 或: http://blog.csdn.net/song_hui_xiang/article/details/37763891


### 一、软件安装

```
a、WebStorm8官网（+破解软件）：http://www.jetbrains.com/webstorm/download/index.html
    修改字体：Editor->Colors&fonts -> Font点Sava as保存自定义字体MyFont，然后自己改；
b、Cocos2d-html5-v2.2.3：http://www.cocos2d-x.org/download/version#Cocos2d-JS   
   或 Cocos2d-JS 3.0Final官网：http://cn.cocos2d-x.org/download/     
c、Chrome安装，谷歌浏览器是基于Html5 实现的，是对Html5支持最好的浏览器。
d、JetBrains-IDE-support 安装：https://chrome.google.com/webstore/detail/jetbrains-ide-support/hmhgeddbohgjknpmjagkdomcpobmllji  
    在线安装：打开谷歌浏览器，并输入此地址，JetBrains IDE Support 插件起到为谷歌浏览器与WebStorm进行联合的桥梁作用。 
    到这步耽误了很多时间，是因为上面的地址始终打不开，这应该懂得，政策被和谐了。反复尝试了N多遍，最终成功打开并安装。浏览器地址栏右侧有 JB 俩字表示已安装。
    本地安装：http://download.csdn.net/download/ggjiji/7767227  下载后，直接拖至谷歌浏览器安装。
```

### 二、运行DEMO
```
a、打开WebStorm,选择Create New Project from Existing Code，意思是从现有的代码创建一个工程。
b、在新的弹出框中选择Source files are in a local directory, no Web server is yet configured，然后点击Next。
c、选择搭建开发环境第一步中我们的Cocos2d-html5目录。并点击左上角的Project Root按钮,将这个文件夹作为项目根目录，将这个目录作为项目的根目录，并点击Finish，这样我们就把引擎导入到了WebStorm。
d、左侧就是项目文件目录。其中有个HelloHTML5World目录，这个就是引擎给提供的HelloWorld程序。
e、双击打开其中的index.html文件，在代码中稍作停顿，能在右上角看到印有浏览器图标的弹出框。点击Google Chrome,就可以运行HelloWorld了。
f、看到如下画面“Hello World动画”，说明环境搭建成功。
```

### 三、发布

最简单方式，放到静态文件服务器上就行了 

### 网上教程

- 用Cocos2d-JS 来写一个跑酷游戏：http://cn.cocos2d-x.org/tutorial/lists?id=60
- 我的几个开源的Cocos2d-js实战项目：http://www.cocoachina.com/bbs/read.php?tid=207980
- 一步一步学Cocos2d-html5做游戏教程(入门篇)：http://bbs.html5china.com/thread-4509-1-1.html
- 使用Cocos2d-html5游戏引擎编写一个简单的游戏：http://www.cocoachina.com/bbs/read.php?tid=187939
- cocos2dx-html5 实现网页版flappy bird游戏：http://codingnow.cn/cocos2d-x/1472.html
- 用Cocos2d-JS制作类神经猫的游戏《你是我的小羊驼》：http://mobile.51cto.com/aengine-446762.htm
- cocos2d-html5 - Allenice1的专栏：http://blog.csdn.net/allenice1/article/category/1176530
- Cocos2d-JS 快速入门二 HelloWord：http://blog.csdn.net/dj0379/article/details/40914917