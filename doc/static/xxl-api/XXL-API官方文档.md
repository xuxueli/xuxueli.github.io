## 《API管理平台XXL-API》

[![Actions Status](https://github.com/xuxueli/xxl-api/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-api/actions)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-api.svg)](https://github.com/xuxueli/xxl-api/releases)
[![GitHub stars](https://img.shields.io/github/stars/xuxueli/xxl-api)](https://github.com/xuxueli/xxl-api/)
[![Docker Status](https://img.shields.io/docker/pulls/xuxueli/xxl-api-admin)](https://hub.docker.com/r/xuxueli/xxl-api-admin/)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![donate](https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat)](https://www.xuxueli.com/page/donate.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-API 是一个API管理平台，易学易用、简单高效且功能强大，服务于产品、前后端研发及测试等多角色人员，提供API管理、Mock、测试等一站式能力。

### 1.2 社区交流
- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.3 特性
- 1、易学易用：功能实用、交互简洁，三分钟上手；
- 2、容器化：提供官方docker镜像，并实时更新推送dockerhub，进一步实现产品开箱即用；
- 3、项目隔离：支持以项目维度进行API隔离管控；
- 4、分组管理：针对单个项目中API，支持自定义分组进行管理；
- 5、权限控制：支持以业务线为维度进行用户权限控制，分配权限才允许操作业务线下项目接口和数据类型，保障API数据安全；
- 6、API Mock：内置MockServer，支持为API灵活定义Mock数据并定制数据响应格式，从而快速提供Mock接口、加快开发进度；
- 7、API 测试：支持针对API进行在线测试，且支持定义管理测试数据，提升接口测试效率；
- 8、API 管理：支持创建、更新和删除API；
- 9、属性丰富：支持丰富的API属性设置，包括API状态、请求方法、请求URL、请求头部、请求参数、响应结果、响应结果格式、响应结果参数、API备注等；
- 10、星级标记：支持API星级标注，标记后优先提权展示；
- 11、Markdown：支持为API添加Markdown格式的备注信息；

### 1.4 下载

#### 文档地址

- [中文文档](https://www.xuxueli.com/xxl-api/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-api](https://github.com/xuxueli/xxl-api) | [Download](https://github.com/xuxueli/xxl-api/releases)  
[http://gitee.com/xuxueli0323/xxl-api](http://gitee.com/xuxueli0323/xxl-api) | [Download](http://gitee.com/xuxueli0323/xxl-api/releases)


### 1.5 环境
- Maven：3.9+
- Jdk：17+
- Mysql：8.0+


## 二、快速部署

### 2.1 初始化“API数据库”
请下载项目源码并解压，获取 "初始化SQL脚本"，脚本位置：
```
/xxl-api/doc/db/tables_xxl_api.sql
```

### 2.2 编译源码
解压源码,按照maven格式将源码导入IDE, 使用maven进行编译即可，源码结构如下：

```
- xxl-api
    - xxl-api-admin ：API管理中心项目；
```

### 2.3 配置JDBC连接
在以下项目文件中设置应用的JDBC连接；
```
/xxl-api/xxl-api-admin/src/main/resources/application.properties
```

### 2.4 部署
部署运行 "xxl-api-admin" 后，即可访问。

默认登录账号 "admin/123456"。

### 2.5 集群(可选)
API管理中心支持集群部署，提升系统可用性。

集群部署时，保持各项目JDBC配置一致即可；

### 2.6 Docker镜像构建
除通过原始方式部署外，可以通过以下命令快速构建项目，并启动运行；

- 下载镜像

```
// Docker地址：https://hub.docker.com/r/xuxueli/xxl-api-admin/     (建议指定版本号)
docker pull xuxueli/xxl-api-admin
```

- 创建容器并运行

```
/**
* 如需自定义 mysql 等配置，可通过 "-e PARAMS" 指定，参数格式 PARAMS="--key=value  --key2=value2" ；
* 配置项参考文件：/xxl-api/xxl-api-admin/src/main/resources/application.properties
* 如需自定义 JVM内存参数 等配置，可通过 "-e JAVA_OPTS" 指定，参数格式 JAVA_OPTS="-Xmx512m" ；
*/
docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_api?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai" -p 8080:8080 -v /tmp:/data/applogs --name xxl-api-admin  -d xuxueli/xxl-api-admin:{指定版本}
```


## 三、项目管理

系统中API以项目为单位进行管理，因此首先需要管理项目；项目管理界面如下图所示；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img01.png "在这里输入图片标题")


### 3.1 新建项目
进入项目管理界面，点击右侧"+新增项目"按钮可新建项目，如下图所示：

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img02.png "在这里输入图片标题")

项目属性说明：

    业务线：所属业务线
    项目名称：项目的名称；
    项目描述：项目的描述信息；
    根地址(线上)：项目线上环境根地址，项目中的API共用该根地址；
    根地址(预发布)：项目预发布环境根地址；
    根地址(测试)：项目测试环境根地址；

### 3.2 更新项目
进入项目管理界面，点击项目右侧的"编辑"按钮可更新项目信息；

### 3.3 删除项目

进入项目管理界面，点击项目右侧的"删除"按钮可删除项目信息；注意，项目中存在API时不允许删除；


## 四、API管理
在项目管理界面，点击项目右侧的"进入项目"按钮，可进入接口管理界面：

### 4.1 API分组管理

- 新增API分组

如下图，点击"左侧接口分组区域"右上角的"+"按钮，可新增AIP接口分组；（点击"全部"将会展示项目中所有分组下的接口；"默认分组"为系统分组，不允许删除；）

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img03.png "在这里输入图片标题")

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img04.png "在这里输入图片标题")

接口分组属性说明：

    分组名称：分组的名称
    分组排序：分组的排序顺序，数字类型，值越小越靠前；

- 更新API分组

在"左侧接口分组区域"，点击对应的API分组，右侧将会展示该分组下API接口列表；点击接口列表顶部的"编辑分组"按钮（新增的API分组才会有该功能），可修改API分组信息；

- 删除API分组

在"左侧接口分组区域"，点击对应的API分组，右侧将会展示该分组下API接口列表；点击接口列表顶部的"删除分组"按钮（新增的API分组才会有该功能），可修改API分组信息；

### 4.2 API管理

- 新增API

如下图，在API接口管理界面，点击接口列表顶部的"新增接口"按钮，可进入新增接口界面；
在新增接口界面，如下图所示，可以设置接口的API状态、请求方法、请求URL、请求头部、请求参数、响应结果、响应结果格式、响应结果参数、API备注等等信息；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img05.png "在这里输入图片标题")


![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img06.png "在这里输入图片标题")

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img07.png "在这里输入图片标题")

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img08.png "在这里输入图片标题")

API属性说明：

    基础信息：
        URL：接口请求的URL地址，注意此处为相对地址，根地址从所属项目的根地址属性上获取；
        分组：接口所属的分组；
        Method：请求方法，如POST、GET等；
        接口状态：接口的状态，在接口列表中，启用状态接口用绿色圆圈标识，维护状态接口用黄色圆圈标识，废弃状态接口用灰色圆圈标识；
        接口名称：接口的名称；
    请求头部：同一接口支持设置多个请求头部；
        头部标签：请求头部的类型，如Accept-Encoding；
        头部内容：请求头部的值，如Accept-Encoding头部标签对应的值UTF-8；
    请求参数：同一接口支持设置多个请求参数；
        参数：参数的名称；
        说明：参数的说明；
        类型：该参数的数据类型，如String；
        是否必填：该参数是否必填；
    响应结果：分别支持设置 "成功响应结果" 和 "失败响应结果"，作为接口响应数据的参考；
        响应数据类型(MIME)：响应结果类型，如JSON、XML等；
        响应结果数据：响应结果的数据，如响应结果类型为JSON时可设置响应结果数据为一段JSON数据；
    响应数据类型：可绑定数据结构，直观查看接口的响应数据的格式化信息。
    接口备注：markdown方式的接口备注；

- 更新API

在API接口管理界面，点击接口右侧的"更新接口图标"按钮，可进入更新接口界面；

- 删除API

在API接口管理界面，点击接口右侧的"删除接口图标"按钮，可删除接口数据；

### 4.3 API-Mock

- 新增Mock数据
  在API接口管理界面，点击接口名称，进入"接口详情页"，在接口详情页的"Mock数据"模块右上角点击"+Mock数据"按钮，可新增Mock数据；

Mock数据属性说明：

    数据类型(MIME)：响应结果类型，如JSON、XML等；
    结果数据：响应结果的数据，如响应结果类型为JSON时可设置响应结果数据为一段JSON数据；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img09.png "在这里输入图片标题")

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img10.png "在这里输入图片标题")

- 更新Mock数据
  在"接口详情页"的"Mock数据"模块，点击Mock数据列表右侧的"修改"按钮，可修改Mock数据；

- 删除Mock数据

在"接口详情页"的"Mock数据"模块，点击Mock数据列表右侧的"删除"按钮，可删除Mock数据；

- 运行Mock数据

在"接口详情页"的"Mock数据"模块，点击Mock数据列表右侧的"运行"按钮，可运行Mock数据；
系统将会为每一条Mock数据生成一个唯一的Mock连接，访问该连接将会按照设置的数据类型如JSON返回对应格式的Mock数据，如下图所示；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img11.png "在这里输入图片标题")


### 4.4 API-测试

- API-测试
  进入"接口详情页"，点击"Test历史"模块右上角的"+接口测试"按钮，可进入"接口测试界面"，
  该界面将会自动初始化接口URL（测试界面支持选择运行环境，将会自动生成不同环境的完整URL连接）和参数等信息。
  只需要填写测试的参数值，点击下方"运行"按钮，即可发起一次接口请求，请求结果将会在下方显示出来：

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img12.png "在这里输入图片标题")

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img13.png "在这里输入图片标题")

- 保存Test历史

在"接口测试界面"，在进行接口测试后, 点击下方"保存"按钮将会把本次测试数据（接口URL，测试参数等信息）保存下来。
在"接口详情页"的"Test历史"模块可查看所有的接口测试历史记录。点击一次测试记录右侧的"运行"按钮，将会进入到本次测试记录对应的接口测试界面，还原当时测试时使用的测试数据；

- 删除Test历史

在"接口详情页"的"Test历史"模块，点击测试历史记录右侧的"删除"按钮可删除本条记录；


## 五、数据结构管理
支持维护格式化的数据结构，更加直观方便的查看数据信息。

接口响应数据支持绑定数据结构，从而更加直观查看接口的响应数据的格式化信息。

数据结构属性支持嵌套引用，也支持被复用。

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img14.png "在这里输入图片标题")

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img15.png "在这里输入图片标题")


## 六、用户管理

系统提供两种用户角色：
- 管理员：拥有系统所有权限；
- 普通用户：默认只拥有项目接口和数据类型的只读权限，被分配业务线权限后，才允许操作业务线下项目和接口等；

管理员角色登陆之后，可管理系统用户信息，如添加用户、设置用户角色，分配权限等。

不同角色用户均可以通过右上角 "修改密码" 功能修改个人登陆密码；

系统初始化时，默认提供了两个示例用户：admin（管理员）、user（普通用户），密码默认为 123456；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img16.png "在这里输入图片标题")


## 七、业务线管理
业务线提供两种功能：
- 分类管理：项目接口归属于业务线下，可通过业务线隔离不同的项目接口，方便管理；
- 权限控制：系统以业务线为维度进行权限控制，用户只有拥有业务线权限，才允许操作业务线下项目接口，否则只允许查看；

系统初始化时，默认一个业务线 "默认业务线"，可以根据情况调整；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-api/images/doc-img17.png "在这里输入图片标题")


## 八、版本更新日志
### v1.0.0 Release Notes[2017-04-05]
- 1、极致简单：交互简洁，一分钟上手；
- 2、项目隔离：API以项目为维度进行拆分隔离；
- 3、分组管理：单个项目内的API支持自定义分组进行管理；
- 4、标记星级：支持标注API星级，标记后优先展示；
- 5、API管理：创建、更新和删除API；
- 6、API属性完善：支持设置丰富的API属性如：API状态、请求方法、请求URL、请求头部、请求参数、响应结果、响应结果格式、响应结果参数、API备注等等；
- 7、markdown：支持为API添加markdown格式的备注信息；
- 8、Mock：支持为API定义Mock数据并制定数据响应格式，从而快速提供Mock接口，加快开发进度；
- 9、在线测试：支持在线对API进行测试并保存测试数据，提供接口测试效率；

### v1.1.0 Release Notes[2018-03-20]
- 1、新增 "业务线" 模块，针对项目以业务线为粒度进行分类管理；
- 2、新增 "数据类型" 模块：系统支持录入数据类型，数据类型支持嵌套，每个API只需要绑定一个数据类型，不需要单独执行响应数据参数；
- 3、权限控制：支持以业务线为维度进行用户权限控制，分配权限才允许操作业务线下项目接口和数据类型，否则仅允许查看；
- 4、项目新增属性"业务线"；项目列表支持通过"业务线"条件查询；
- 5、接口新增属性 "响应数据类型" 属性，通过绑定 "数据类型" 格式化描述接口响应数据结构；
- 6、项目内API搜索关键字改为URL，更加贴合用户需求；
- 7、新增在线修改密码功能；
- 8、登陆Token与用户密码均进行md5加密，提升系统安全性；
- 9、项目maven依赖升级；
- 10、UI交互优化，列表自适应性优化；
- 11、底层代码重构；
- 12、登陆Cookie启用HttpOnly；
- 13、弹框插件改为使用Layui；
- 14、AdminLTE版本升级；
- 15、接口测试用例优化，支持存储接口Method与URL数据；
- 16、接口在线测试时，Get请求URL转换问题修复；

### v1.1.1 Release Notes[2018-10-26]
- 1、接口"成功/失败响应结果"支持JSON格式化校验与展示，方便数据查看；
- 2、项目迁移至 springboot 版本；
- 3、docker支持：提供 Dockerfile 方便快速构建docker镜像；
- 4、项目下存在接口时拒绝删除，防止数据误删除；
- 5、接口分组删除问题修复；
- 6、接口在线测试功能对于响应状态码为302的请求未能正确展示“Location”问题修复；

### v1.2.0 Release Notes[2024-11-16]
- 1、【新增】容器化：提供官方docker镜像，并实时更新推送dockerhub，进一步实现产品开箱即用；
- 2、【优化】Docker基础镜像切换，精简镜像；降低资源消耗、提升部署效率；
- 3、【优化】精简项目，移除依赖如commons-collections4、commons-lang3；
- 4、【优化】登录页默认填充密码移除，提升安全性；
- 5、【修复】数据类型循环处理逻辑优化，修复超5层循环处理空值问题；
- 6、【修复】序列化组件兼容性问题处理；
- 7、【修复】freemarker对数字默认加千分位问题修复，解决日志ID被分隔导致查看日志失败问题；
- 8、【升级】升级依赖版本，如springboot、mybatis、httpclient等；

### v1.3.0 Release Notes[2025-08-16]
- 1、【升级】项目升级 SpringBoot3 + JDK17；
- 2、【升级】升级多项依赖至较新版本，如xxl-sso、jakarta、spring等，适配JDK17；


### v1.4.0 Release Notes[2025-09-20]
- 1、【安全】登录安全升级，密码加密处理算法从Md5改为Sha256；(用户表password字段需要调整长度，执行如下命令)
```
// 1、用户表password字段需要调整长度，执行如下命令
ALTER TABLE xxl_api_user
    MODIFY COLUMN `password` varchar(100) NOT NULL COMMENT '密码加密信息';
    
// 2、存量用户密码需要修改，可执行如下命令将密码初始化 “123456”；也可以自行通过 “SHA256Tool.sha256” 工具生成其他初始化密码；
UPDATE xxl_api_user t SET t.password = '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92' WHERE t.username = {用户名};
```
- 2、【优化】登录态持久化逻辑调整，简化代码逻辑；
- 3、【优化】异常页面处理逻辑优化，新增兜底落地页配置；
- 4、【优化】登录信息页面空值处理优化，避免空值影响ftl渲染；
- 5、【优化】系统日志调整，支持启动时指定 -DLOG_HOME 参数自定义日志位置；同时优化日志格式提升易读性；

### v1.5.0 Release Notes[2025-09-21]
- 1、【重构】UI框架重构升级，提升交互体验；
- 2、【新增】新增国际化组件，多语言支持；
- 3、【优化】调整资源加载逻辑，移除不必要的拦截器逻辑，提升页面加载效率；
- 4、【升级】升级依赖版本，如 springboot、spring 等；

### v1.6.0 Release Notes[ING]
- 1、【ING】支持设置RequestBody类型，raw类型参数；
- 2、【ING】支持swagger、postman等格式接口数据导入；
- 3、【ING】支持API分享；


### TODO LIST
- 1、请求参数，除常规form之外，支持选择RequestBody方式，传递Json、XML和文本等数据；
- 2、API历史版本：支持对API修改历史版本进行对比，版本回溯等操作；
- 3、弹框插件优化：考虑第三方插件；
- 4、支持接口签名，sign逻辑；
- 5、API响应结果对象管理；
- 6、支持设置RequestBody类型，raw类型参数；
- 7、拥有人、管理员才允许删除；记录修改历史；
- 8、项目权限：支持对项目设置权限，拥有权限才允许操作项目中API；
- 9、根据URL全站匹配API；
- 10、数据类型，代码生成
- 11、配合xxl-web，实现线上Mocker，支持根据mock-uuid，对调用进行自动mock；
- 12、配合xxl-web，实现根据代码代码自动生成api文档并导入xxl-api管理平台；
- 13、支持Restful类型接口，如 GET /job/{id}/detail ，其中{id}在接口测试时支持动态变化；
- 14、接口历史版本功能，记录接口操作变更历史；
- 15、支持 https 类型接口在线测试，处理接口证书问题；
- 16、请求头部，除了支持下拉框外，支持手动填写；
- 17、简化测试页，支持Select方式选择接口，自动填充接口数据并测试；
- 18、接口绑定数据类型时，支持指定标准格式（code、msg、data）；支持自动生成完整数据接口；
- 19、接口数据类型，自动生成Mock数据，提供Runpath接口根据接口地址自动匹配接口生成Mock数据；
- 20、可配置参数支持拖动改变顺序；
- 21、接口快速搜索功能；
- 22、Mock添加说明字段；
- 23、权限进一步整理优化；项目支持设置公开、私有；私有必须授权。公开所有人只读；
- 24、协议转换：http>rpc;
- 25、API导出为PDF；


## 九、其他

### 9.1 项目贡献
欢迎参与项目贡献！比如提交PR修一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-api/issues/) 讨论新特性或者变更。

### 9.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-api/issues/1 ) 登记，登记仅仅为了产品推广。

### 9.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )
