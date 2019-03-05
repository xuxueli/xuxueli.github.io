### 目前流行的J2EE开发IDE
* Eclipse：[下载](http://www.eclipse.org/downloads/)
* Myeclipse：[下载](http://www.myeclipsecn.com/)    
* Intellij：[下载](http://www.jetbrains.com/idea/)         

### 一、Eclipse经验汇总 
***
##### 1.1 Eclipse插件汇总
**必备插件：（已集成：jdk，tomcat，maven，egit）freemarker、open-explorer...**

> 001：集成JDK + Tomcat
* 1：JDK配置：PreFerences 》Java 》Installed JREs 》Add 》Standard VM 配置 jdk路径。
* 2：Tomcat配置：PreFerences 》Server》Runtime Environments》Add 》选中Apache Tomcat版本、路径、JDK版本；
 
> 002：freemarker-ide：是一个FreeMarker(模板引擎)感知编辑插件
* 1：在线更新（推荐）：(jboss：http://download.jboss.org/jbosstools/updates/development/)
列出的工作选 JBoss Application Development -> FreeMarker IDE即可!
* 2：离线下载：[下载地址](http://sourceforge.net/projects/freemarker-ide/files/)

> 003：Open In Explorer：直接进入Windows资源管理器中打开选中文件所在的目录
* 离线安装：[下载网址](https://github.com/samsonw/OpenExplorer/downloads)

> 004：Subclipse：SVN插件（推荐使用Git）
* 1：MarketPlace安装（推荐）：Eclipse MarketPlace，搜索"subclipse"，Install；
* 2：在线更新安装：在官网页面（  [地址](http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA)  ）中找到最新稳定版在线更新安装URL（Eclipse update site URL），在线更新安装即可；
* 3：离线安装：在官网页面（  [地址](http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA)  ）中找到最新稳定版在线更新安装URL（Zipped downloads），下载ZIP压缩包后离线安装即可；

> 005：groovy-eclipse：Groovy插件
* 在线更新安装：在官方GIthub上找匹配的在线更新安装URL（ [地址](https://github.com/groovy/groovy-eclipse/wiki) ）；

> 006：activiti插件：
* 在线更新安装：Activiti BPMN 2.0 designer：http://activiti.org/designer/update/


> 007：Tao-ReviewBoard：Tao-ReviewBoard是在eclipse上开发的一款ReviewBoard代码评审插件
* 在线更新安装：见 [文档](http://code.taobao.org/p/tao-reviewboard/wiki/index/) 
* 离线安装：见上文文档

> 008：Python开发ide插件，结合Python使用：
* 在线更新安装：[官网](http://www.pydev.org/download.html) 的“URLs for PyDev as Eclipse plugin”位置找到更新安装URL；
* 离线安装：在官网的“Get zip releases”位置下载；

> 009：lombok：（提供set/get支持，得不偿失）
* 离线安装：[下载地址](https://projectlombok.org/download.html)
```
第一步：将下载的“lombok.jar”放置在eclipse.ini同目录下；
第二部：在eclipse.ini中添加一下内容：

// windows下
-javaagent:lombok.jar
-Xbootclasspath/a:lombok.jar
注意：lombok和eclipse版本存在对应关系：luna使用lombok版本1.16.5以上的，mars可使用1.16.6；

// Mac下：
-Xbootclasspath/a:lombok.jar
-javaagent:/Users/xuxueli/programfils/Eclipse.app/Contents/Eclipse/lombok.jar
```

> 010：Eclipse-Markdown-Editor-Plugin：markdown插件
* MarketPlace安装：搜索 “Eclipse-Markdown-Editor-Plugin ” ；
* 在线安装：在 [MarketPlace项目主页](http://marketplace.eclipse.org/content/markdown-text-editor) 中找到更新地址；[Github地址](https://github.com/winterstein/Eclipse-Markdown-Editor-Plugin)

> 011：配置maven（已经集成，但是需要配置）
在preference中配置（windows在右上角，mac在左上角）：
- 配置installations,即maven安装地址（找到本地安装maven地址）；
- 配置settings和本地仓库地址（reindex和maven一致）；
    
    地址：File->settings->Build,Execution,Deployment->Build Tools->Maven
    
    Maven项目操作：   
    - 一般工程转为maven工程（GIT导出项目时常用）： 工程右键--->Configure--->Convert to Maven Project。
    - maven转为工程一般工程： 工程右键--->Maven--->Disable Maven Nature转为一般工程。
    - 更新Maven项目POM：项目右键》Maven》Update Project；
    - 修改web Module：修改WEB_INF/web.xml文件;

> 012：配置git（已经集成，但是需要配置）
- eclipse插件egit已经集成git，如仅通过插件操作git，可不安装配置；如需命令行则自行安装git；

##### 1.2 Eclipse总结：                                            
* 1、配置eclipse.ini：
```
-vmargs
-Dosgi.requiredJavaVersion=1.7
-Xms256m
-Xmx1024m
-vm
E:\programfiles\jdk1.7\jdk\bin\
```
* 2、取消插件自启动（使用时启动）：
Windows》Preferences》General》Startup and Shutdown》自动清Plugin，全部取消勾选；

* 3、Validation关闭Build验证，只保留Manual ：
Windows》Preferences》Validation，只保留Manual一列，Build一列取消；

* 4、关闭自动构建（否则没保存一下，会构建整个项目；eclipse会在运行前自动构建；也可手工构建；我还喜欢自动部署，方便；）：Project》Build Automatically 取消勾选

* 5、Project》clean：把编译好的class等文件删除，激活eclipse的自动编译（一些莫名其妙的错误时，用一下这个功能，效果不错）

* 6、Debug + Didplay(allow execute code when debug);
* 7、TCP/IP monitor(client with new port<-->monitor port<-->real port-->server);
* 8、设置代码自动提示：Window》Preferences》JAVA》Editor》Content Assist》在“Auto activation triggers for JAVA”输入框中输入：“abcdefghijklmnopqrstuvwxyz.” 即可；

##### 1.3 快捷键
> 常用快捷键

* 1、 【Ctrl+K】、【Ctrl++Shift+K】快速向下和向上查找选定的内容，从此不再需要用鼠标单击查找对话框了。
* 2、 【Ctrl+Shift+T】查找工作空间（Workspace）构建路径中的可找到Java类文件，不要为找不到类而痛苦，而且可以使用“*”、“？”等通配符。
* 3、 【Ctrl+Shift+R】和【Ctrl+Shift+T】对应，查找工作空间（Workspace）中的所有文件（包括Java文件），也可以使用通配符。
* 4、 【Ctrl+Shift+G】查找类、方法和属性的引用。这是一个非常实用的快捷键，例如要修改引用某个方法的代码，可以通过【Ctrl+Shift+G】快捷键迅速定位所有引用此方法的位置。
* 5、 【Ctrl+Shift+O】快速生成import，当从网上拷贝一段程序后，不知道如何import进所调用的类，试试【Ctrl+Shift+O】快捷键，一定会有惊喜。
* 6、 【Ctrl+Shift+F】格式化代码，书写格式规范的代码是每一个程序员的必修之课，当看见某段代码极不顺眼时，选定后按【Ctrl+Shift+F】快捷键可以格式化这段代码，如果不选定代码则默认格式化当前文件（Java文件）。
* 7、 【ALT+Shift+W】查找当前文件所在项目中的路径，可以快速定位浏览器视图的位置，如果想查找某个文件所在的包时，此快捷键非常有用（特别在比较大的项目中）。
* 8、 【Ctrl+L】定位到当前编辑器的某一行，对非Java文件也有效。
* 9、 【Alt+←】、【Alt+→】后退历史记录和前进历史记录，在跟踪代码时非常有用，用户可能查找了几个有关联的地方，但可能记不清楚了，可以通过这两个快捷键定位查找的顺序。
* 10、 【F3】快速定位光标位置的某个类、方法和属性。
* 11、 【F4】显示类的继承关系，并打开类继承视图。
 
> 运行调试快捷键
* 1、 【Ctrl+Shift+B】：在当前行设置断点或取消设置的断点。
* 2、 【F11】：调试最后一次执行的程序。
* 3、 【Ctrl+F11】：运行最后一次执行的程序。
* 4、 【F5】：跟踪到方法中，当程序执行到某方法时，可以按【F5】键跟踪到方法中。
* 5、 【F6】：单步执行程序。
* 6、 【F7】：执行完方法，返回到调用此方法的后一条语句。
* 7、 【F8】：继续执行，到下一个断点或程序结束。
 
> 常用编辑器快捷键
* 1、 【Ctrl+C】：复制。
* 2、 【Ctrl+X】：剪切。
* 3、 【Ctrl+V】：粘贴。
* 4、 【Ctrl+S】：保存文件。
* 5、 【Ctrl+Z】：撤销。
* 6、 【Ctrl+Y】：重复。
* 7、 【Ctrl+F】：查找。
 
> 其他快捷键
* 1、 【Ctrl+F6】：切换到下一个编辑器。
* 2、 【Ctrl+Shift+F6】：切换到上一个编辑器。
* 3、 【Ctrl+F7】：切换到下一个视图。
* 4、 【Ctrl+Shift+F7】：切换到上一个视图。
* 5、 【Ctrl+F8】：切换到下一个透视图。
* 6、 【Ctrl+Shift+F8】：切换到上一个透视图。
 

> 有用的快捷键
* Alt+/ 代码助手完成一些代码的插入(但一般和输入法有冲突,可以修改输入法的热键,也可以暂用Alt+/来代替)
* Ctrl+1:光标停在某变量上，按Ctrl+1键，可以提供快速重构方案。选中若干行，按Ctrl+1键可将此段代码放入for、while、if、do或try等代码块中。
* 双击左括号（小括号、中括号、大括号），将选择括号内的所有内容。
* Alt+Enter 显示当前选择资源(工程,or 文件 or文件)的属性
 
> Ctrl系列
* Ctrl+K:将光标停留在变量上，按Ctrl+K键可以查找到下一个同样的变量
* Ctrl+Shift+K:和Ctrl+K查找的方向相反
* Ctrl+E 快速显示当前Editer的下拉列表(如果当前页面没有显示的用黑体表示)
* Ctrl+Shift+E 显示管理当前打开的所有的View的管理器(可以选择关闭,激活等操作)
* Ctrl+Q 定位到最后编辑的地方
* Ctrl+L 定位在某行 (对于程序超过100的人就有福音了)
* Ctrl+M 最大化当前的Edit或View (再按则反之)
* Ctrl+/ 注释当前行,再按则取消注释
* Ctrl+T 快速显示当前类的继承结构
* Ctrl+Shift-T: 打开类型（Open type）。如果你不是有意磨洋工，还是忘记通过源码树（source * tree）打开的方式吧。
* Ctrl+O:在代码中打开类似大纲视图的小窗口
* Ctrl+鼠标停留:可以显示类和方法的源码
* Ctrl+H:打开搜索窗口
* Ctrl+/(小键盘) 折叠当前类中的所有代码
* Ctrl+×(小键盘) 展开当前类中的所有代码
 
> Ctrl+Shift 系列
* Ctrl+Shift+F 格式化当前代码
* Ctrl+Shift+X 把当前选中的文本全部变味小写
* Ctrl+Shift+Y 把当前选中的文本全部变为小写
* Ctrl+Shift+O:快速地导入import
* Ctrl+Shift+R:打开资源 open Resource
 
> 行编辑用
* Ctrl+D: 删除当前行
* Ctrl+Alt+↓ 复制当前行到下一行(复制增加)
* Ctrl+Alt+↑ 复制当前行到上一行(复制增加)
* Alt+↓ 当前行和下面一行交互位置(特别实用,可以省去先剪切,再粘贴了)
* Alt+↑ 当前行和上面一行交互位置(同上)
* Shift+Enter 在当前行的下一行插入空行(这时鼠标可以在当前行的任一位置,不一定是最后)
* Ctrl+Shift+Enter 在当前行插入空行(原理同上条)
 
> 不常用的
* Alt+← 前一个编辑的页面
* Alt+→ 下一个编辑的页面(当然是针对上面那条来说了)
* Ctrl+Shift+S:保存全部
* Ctrl+W 关闭当前Editer
* Ctrl+Shift+F4 关闭所有打开的Editer
 
* Ctrl+Shift+G: 在workspace中搜索引用
* Ctrl+Shift+P 定位到对于的匹配符(譬如{}) (从前面定位后面时,光标要在匹配符里面,后面到前面,则反之)


* Ctrl+J 正向增量查找(按下Ctrl+J后,你所输入的每个字母编辑器都提供快速匹配定位到某个单词,如果没有,则在stutes line中显示没有找到了,查一个单词时,特别实用,这个功能Idea两年前就有了)
* Ctrl+Shift+J 反向增量查找(和上条相同,只不过是从后往前查) 


### 二、Intellij经验汇总 
***
##### 2.1 安装注册
- [下载地址](http://www.jetbrains.com/idea/) 

> Community Edition社区免费版功能有精简，推荐使用Ultimate Edition商业版本，可免费用30天，需要注册激活。

> 已经集成了：JDK、Tomcat、Maven、Git、Freemarker、explorer等功能，比较完善。（插件库比较智能完善，如markdown插件在打开MD文件时会主动询问是否安装）

##### 2.2 设置
> Project等同于eclipse的workspace，Module等同于eclipse的Project。失去焦点自动保存；

* 1、JDK：

        开发版本/JDK：
        File》Other Setting》Default Project Structure》Platform Settings》SDKs》+JDK》选择全局jdk目录;
        File》Project Structure》Platform Settings》SDKs》+JDK》选择项目jdk目录
        编译版本/Compiler：
        Preferences》Default Settings》Build、Execution、Deployment》Java Compiler》Project bytecode version》置空，编译版本自动和开发版本一致；
        File》Other Settint》Default Settings》Build、Execution、Deployment》Java Compiler》Project bytecode version》置空，编译版本自动和开发版本一致；
        运行版本/JRE：
        Tomcat》Edit...》JRE》选择对应的JDK版本即可；（低版本spring2.x不兼容1.8）

* 2、maven设置：
    >File》Other Setting》Default Setting》Build、Execution、Deployment》Build Tools》Maven》配置maven安装目录和settin文件

    >File》Setting》Build、Execution、Deployment》Build Tools》Maven》配置maven安装目录和settin文件
    
    ```
    Maven home directory：maven安装目录 (Override)
    User settings file：setting文件目录 (Override)
    Local repository： 默认即可
    ```
    
    Tips：推荐依赖树查看插件 Maven Helper

* 3、Git配置：
    >配置GIT：安装Git，配置Git：File》Setting》Version Control》Git》Path to Git executable选择git.exe地址；
    
    >下载Git仓库：VCS》Checkout from Version Control》Git》...
        
    >导入Git项目：File》New》Module from exists Sources》Git仓库中项目；
        
    >非Git仓库顶级目录下项目，关联Git：VCS》Version Control Operation
        
    >多git仓库同时使用：File》Setting》Version Control》点击+新增git本地仓库地址；
        
    >取消git自动tracked文件，导致gitignore失效：File》Setting》Version Control》When fils are created》勾选Do not add;
    
* 4、Tomcat：

    >配置Server环境：File》Setting》Build、Execution、Deployment》Application Servers》点击+选择Tomcat类型，选择目录路径
    
    >配置Server实例：右上角》Edit Configurations》点击+号》Tomcat Server》Local》配置：环境、名称、默认浏览器、默认端口路径、端口，VM设置等；
    
    >部署项目：右上角》Edit Configurations》Deployment》点+号》Artifact》选择Web项目，配置根路径Application context；
    
    >动态Debug：右上角》Edit Configurations》Server》On update action改动/ On frame deactivation失活都选择update classes and resources(exploded格式才有)；

    >一个Tomcat实例只能启动一个项目，如需启动多台，需要配置多个Tomcat并修改端口号；
    
* 5、修改IDE字体，黑色风格：
    >File》Setting》Appearance & Behavior》Appearance》

    ```
    Theme：Darcula；Override default fonts by(not recommeded) 打钩选中；
    Name：dialogInput；Size：12；
    ```
        
    >修改编辑器/代码字体：File》Setting》Font》保存自定义Scheme》primary font：Consolas；Size：14；
    
    >控制台字体：
    File》Setting》Editor》Color&Fonts》Console Font》primary font：Consolas；Size：14；


* 6、Keymap（快捷键）设置：
    >Keymap》选中即可（因为熟悉Eclipse，可改为此；但为了以后适应，推荐使用默认）
* 7、显示行号：
    >File》Setting》Editor》General》Appearance》Show line numbers（勾选）
        
* 8、不自动打开上次项目：
    >File》Setting》Apprearance & Behavior》System Settings》Reopen last project on startup（去掉勾选）；

* 9、设置文件头注释：
    >File》Setting》Editor》File and Code Templates》Includes》File Header》输入：Created by ${USER} on ${DATE} ${TIME}.
* 10、文件自动保存：
    >挺好的，不用修改；
* 11、用*标识编辑过的文件：
    >Editor》Editor Tabs》Mark modifyied tabs with asterisk（勾选）
* 12、快捷键冲突：
    >Ctrl + Space：和Win7输入法冲突；语言栏》高级键设置》编辑“在输入语言之间”不修改点击确认》编辑“中文-输入法/非输入法切换”Ctrl+Space修改为如Ctrl+Home等即可》保存，重启即可；
- idea热部署
    >1.Build,Excution,Deployment>>Make project automatically（勾选）
    >2.CTRL + SHIFT + A --> 查找Registry;  勾选  compiler.automake.allow.when.app.running;
    springboot自动部署参考文档：https://blog.csdn.net/a1273022039/article/details/79590681
    >3. Edit Configurations --> 'On Update Action' 勾 'update classes and resources'

##### 2.3 Compile、Make和Build的区别
    >针对Java的开发工具，一般都有Compile、Make和Build三个菜单项，完成的功能的都差不多，但是又有区别。
    
    >编译，是将源代码转换为可执行代码的过程。编译需要指定源文件和编译输出的文件路径（输出目录）。Java的编译会将java编译为class 文件，将非java的文件（一般成为资源文件、比如图片、xml、txt、poperties等文件）原封不动的复制到编译输出目录，并保持源文件夹的目 录层次关系。
    >在Java的集成开发环境中，比如Eclipse、IDEA中，有常常有三种与编译相关的选项Compile、Make、Build三个选项。这三个选项最基本的功能都是完成编译过程。但又有很大的区别，区别如下：
    
    * 1、Compile：只编译选定的目标，不管之前是否已经编译过。
    * 2、Make：编译选定的目标，但是Make只编译上次编译变化过的文件，减少重复劳动，节省时间。（具体怎么检查未变化，这个就不用考虑了，IDE自己内部会搞定这些的）
    * 3、Build：是对整个工程进行彻底的重新编译，而不管是否已经编译过。Build过程往往会生成发布包，这个具体要看对IDE的配置 了，Build在实际中应用很少，因为开发时候基本上不用，发布生产时候一般都用ANT等工具来发布。Build因为要全部编译，还要执行打包等额外工 作，因此时间较长。

##### 2.4 快捷键
官网文档很完善：Help》KeyMap Preference》双击即可查看快捷键表；


