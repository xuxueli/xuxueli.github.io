## 《Java工具类库XXL-TOOL》

[![Actions Status](https://github.com/xuxueli/xxl-tool/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-tool/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-tool/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-tool/)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-tool.svg)](https://github.com/xuxueli/xxl-tool/releases)
[![GitHub stars](https://img.shields.io/github/stars/xuxueli/xxl-tool)](https://github.com/xuxueli/xxl-tool/)
![License](https://img.shields.io/github/license/xuxueli/xxl-tool.svg)
[![donate](https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square)](https://www.xuxueli.com/page/donate.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-TOOL 是一个Java工具类库，致力于让Java开发更高效。包含 “集合、字符串、缓存、并发、Excel、Emoji、Response、Pipeline……” 等数十个模块。

### 1.2 组件列表
模块 | 说明
--- | ---
Core模块 | 包含集合、缓存、日期……等基础组件工具。
Gson模块 | json序列化、反序列化工具封装，基于Gson。
Json模块 | json序列化、反序列化自研工具 。
Response模块 | 统一响应数据结构体，标准化数据结构、状态码等，降低协作成本。
Pipeline模块 | 高扩展性流程编排引擎。
Excel模块 | 一个灵活的Java对象和Excel文档相互转换的工具。一行代码完成Java对象和Excel之间的转换。
Emoji模块 | 一个灵活可扩展的Emoji表情编解码库，可快速实现Emoji表情的编解码。
Fiber模块 | Java协程库，基于quasar封装实现。
Freemarker模块 | 模板引擎工具，支持根据模板文件生成文本、生成文件…等。
IO模块 | 一系列处理IO（输入/输出）操作的工具。
Encrypt模块 | 一系列处理编解码、加解密的工具。
Http模块 | 一系列处理Http通讯、IP、Cookie等相关工具。
JsonRpc模块 | 一个轻量级、跨语言远程过程调用实现，基于json、http实现（对比传统RPC框架：[XXL-RPC](https://github.com/xuxueli/xxl-rpc)）。
Concurrent模块 | 一系列并发编程工具，具备良好的线程安全、高并发及高性能优势，包括循环线程、高性能队列等。
... | ...

### 1.4 下载

#### 文档地址

- [中文文档](https://www.xuxueli.com/xxl-tool/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-tool](https://github.com/xuxueli/xxl-tool) | [Download](https://github.com/xuxueli/xxl-tool/releases)
[https://gitee.com/xuxueli0323/xxl-tool](https://gitee.com/xuxueli0323/xxl-tool) | [Download](https://gitee.com/xuxueli0323/xxl-tool/releases)  


#### 技术交流
- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.5 环境
- JDK：1.8+

### 1.6 Maven依赖
```
<!-- http://repo1.maven.org/maven2/com/xuxueli/xxl-tool/ -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-tool</artifactId>
    <version>${最新稳定版}</version>
</dependency>
```

### 1.7 发展历程
XXL-TOOL 前身为  XXL-EXCEL、XXL-EMOJI 两个独立项目，以及 XXL-JOB 内部经过验证的成熟工具类等，经过整合演进最终诞生。
- 1、XXL-EXCEL：首版发布于2017年9月，一个灵活的Java对象和Excel文档相互转换的工具。一行代码完成Java对象和Excel文档之间的转换。同时保证性能和稳定。（已废弃，整合至 XXL-TOOL）
- 2、XXL-EMOJI：首版发布于2018年7月，一个灵活可扩展的Emoji表情编解码库，可快速实现Emoji表情的编解码。（已废弃，整合至 XXL-TOOL）


## 二、接入指南

### 2.1、Core模块

参考单元测试，见目录：com.xxl.tool.test.core
```
// DateTool
String dateTimeStr = DateTool.formatDateTime(new Date());
Date date = DateTool.parseDateTime(dateTimeStr);

// CollectionTool
CollectionTool.isEmpty(list);
CollectionTool.union(a,b);
CollectionTool.intersection(a,b);
CollectionTool.disjunction(a,b);
CollectionTool.subtract(a,b);
CollectionTool.subtract(b,a);
CollectionTool.newArrayList();
CollectionTool.newArrayList(1,2,3);

// StringTool
StringTool.isEmpty("  ");
StringTool.isBlank("  ");

// MapTool
MapTool.isNotEmpty(map);
MapTool.getInteger(map, "k1");    // 根据类型（Integer），获取数据
MapTool.getLong(map, "k1");       // 根据类型（Long），获取数据 
MapTool.newHashMap(               // 快速创建map，支持 key-value 键值对初始化
        "k1", 1,
        "k2", 2
))

// …… 更多请查阅API
```

### 2.2、Json模块

参考单元测试：com.xxl.tool.test.response.GsonToolTest
```
// Object 转成 json
String json = GsonTool.toJson(new Demo());

// json 转成 特定的cls的Object
Demo demo = GsonTool.fromJson(json, Demo.class);
    
// json 转成 特定的 rawClass<classOfT> 的Object
Response<Demo> response = GsonTool.fromJson(json, Response.class, Demo.class);

// json 转成 特定的cls的 ArrayList
List<Demo> demoList = GsonTool.fromJsonList(json, Demo.class);

// json 转成 特定的cls的 HashMap
HashMap<String, Demo> map = GsonTool.fromJsonMap(json, String.class, Demo.class);

// …… 更多请查阅API
```

### 2.3、Response模块

参考单元测试：com.xxl.tool.test.response.ResponseBuilderTest
```
// 快速构建
Response<String> response1 = new ResponseBuilder<String>().success().build();
Response<Object> response2 = new ResponseBuilder<Object>().success().data("响应正文数据").build();
Response<String> response3 = new ResponseBuilder<String>().fail().build();
Response<String> response4 = new ResponseBuilder<String>().fail("错误提示消息").build();

// 完整构建
Response<String> response = new ResponseBuilder<String>()
                .code(ResponseCode.CODE_200.getCode())    // 状态码
                .msg("Sucess")                            // 提示消息
                .data("Hello World")                      // 响应正文数据
                .build();
```

### 2.4、Pipeline模块

**案例1：执行单个pipeline**        
说明：开发业务逻辑节点handler，定义编排单个pipeline；模拟执行参数，运行pipeline，获取响应结果。

参考单元测试：com.xxl.tool.test.pipeline.PipelineTest
```
// 开发业务逻辑节点handler
PipelineHandler handler1 = new Handler1();
PipelineHandler handler2 = new Handler2();
PipelineHandler handler3 = new Handler3();

// 定义编排单个pipeline
Pipeline p1 = new Pipeline()
        .name("p1")
        .status(PipelineStatus.RUNTIME.getStatus())
        .addLasts(handler1, handler2, handler3);

// 模拟执行参数
DemoRequest requet = new DemoRequest("abc", 100);

// 执行 pipeline
Response<Object>  response = p1.process(requet);
```

**案例2：执行单个pipeline**        
说明：开发业务逻辑节点handler，定义编排多个pipeline；定义pipeline执行器，并注册多个pipeline； 模拟执行参数，通过 pipeline 执行器路由 并 执行 pipeline，获取响应结果。

参考单元测试：com.xxl.tool.test.pipeline.PipelineExecutorTest
```
// 开发业务逻辑节点handler
PipelineHandler handler1 = new Handler1();
PipelineHandler handler2 = new Handler2();
PipelineHandler handler3 = new Handler3();

// 定义编排多个pipeline
Pipeline p1 = new Pipeline()
        .name("p1")
        .status(PipelineStatus.RUNTIME.getStatus())
        .addLasts(handler1, handler2, handler3);

Pipeline p2 = new Pipeline()
        .name("p2")
        .status(PipelineStatus.RUNTIME.getStatus())
        .addLasts(handler2, handler1, handler3);

// 定义pipeline执行器，并注册多个pipeline
PipelineExecutor executor = new PipelineExecutor();
executor.registry(p1);
executor.registry(p2);

// 模拟执行参数
PipelineTest.DemoRequest requet1 = new PipelineTest.DemoRequest("aaa", 100);
PipelineTest.DemoRequest requet2 = new PipelineTest.DemoRequest("bbb", 100);

// 通过 pipeline 执行器路由 并 执行 pipeline
Response<Object> response1 = p1.process(requet1);
logger.info("response1: {}", response1);
Assertions.assertEquals(response1.getCode(), ResponseCode.CODE_200.getCode());

Response<Object>  response2 = p2.process(requet2);
logger.info("response2: {}", response2);
Assertions.assertEquals(response2.getCode(), ResponseCode.CODE_200.getCode());
```


### 2.5、Excel模块

**功能定位**

一个灵活的Java对象和Excel文档相互转换的工具。一行代码完成Java对象和Excel文档之间的转换。同时保证性能和稳定。
（原名 XXL-EXCEL，整合至该项目）

**特性**
- 1、Excel导出：支持Java对象装换为Excel，并且支持File、字节数组、Workbook等多种导出方式；
- 2、Excel导入：支持Excel转换为Java对象，并且支持File、InputStream、文件路径、Workbook等多种导入方式；
- 3、全基础数据类型支持：Excel的映射Java对象支持设置任意基础数据类型，将会自动完整值注入；
- 4、Field宽度自适应；
- 5、多Sheet导出：导出Excel时支持设置多张sheet；
- 6、多Sheet导入：导入Excel时支持设置多张sheet，通过 "@ExcelSheet.name" 注解匹配Sheet;

**Java 对象 和 Excel映射关系**

-- | Excel | Java 对象
--- | --- | ---
表 | Sheet | Java对象列表
表头 | Sheet首行 | Java对象Field
数据 | Sheet一行记录 | Java对象列表中一个元素

**核心注解：ExcelSheet**

功能：描述Sheet信息，注解添加在待转换为Excel的Java对象类上，可选属性如下。

ExcelSheet | 说明
--- | ---
name | 表/Sheet名称
headColor | 表头/Sheet首行的颜色

**核心注解：ExcelField**

功能：描述Sheet的列信息，注解添加在待转换为Excel的Java对象类的字段上，可选属性如下。

ExcelField | 说明
--- | ---
name | 属性/列名称


**使用指南**
- a、引入依赖

该模块需要主动引入如下关联依赖（默认provided模式，精简不必须依赖），可参考仓库pom获取依赖及版本：https://github.com/xuxueli/xxl-tool/blob/master/pom.xml

```
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>${poi.version}</version>
</dependency>
```

- b、定义Java对象

```java
@ExcelSheet(name = "商户列表", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
public class ShopDTO {

    @ExcelField(name = "商户ID")
    private int shopId;

    @ExcelField(name = "商户名称")
    private String shopName;

    public ShopDTO() {
    }
    public ShopDTO(int shopId, String shopName) {
        this.shopId = shopId;
        this.shopName = shopName;
    }

    // set、get
}
```

- c、Excel导入、导出代码

```java
// 参考测试代码：com.xxl.tool.test.excel.ExcelToolTest

/**
 * Excel导出：Object 转换为 Excel
 */
public static void exportToFile(boolean xlsx, List<List<?>> sheetDataListArr, String filePath) {…}



/**
 * Excel导入：Excel 转换为 Object
 */
public static List<Object> importExcel(String filePath, Class<?> sheetClass) {…}
```

### 2.6、Emoji模块

**功能定位**
一个灵活可扩展的Emoji表情编解码库，可快速实现Emoji表情的编解码.
（原名 XXL-EMOJI）

**特性**
- 1、简洁：API直观简洁，一分钟上手；
- 2、易扩展：模块化的结构，可轻松扩展；
- 3、别名自定义：支持为Emoji自定义别名；
- 4、实时性：实时收录最新发布的Emoji；

**Emoji编码类型**

概念 | 说明
--- | ---
EmojiEncode.ALIASES | 将Emoji表情转换为别名，格式为 ": alias :"；
EmojiEncode.HTML_DECIMAL | 将Emoji表情Unicode数据转换为十进制数据；
EmojiEncode.HTML_HEX_DECIMAL | 将Emoji表情Unicode数据转换为十六进制数据；

**Emoji编解码API**

API | 说明
--- | ---
public static String encodeUnicode(String input, EmojiTransformer transformer, FitzpatrickAction fitzpatrickAction) | Emoji表情编码方法，支持自定义编码逻辑；
public static String encodeUnicode(String input, EmojiEncode emojiEncode, FitzpatrickAction fitzpatrickAction) | Emoji表情编码方法，支持自定义编码类型；
public static String encodeUnicode(String input, EmojiEncode emojiEncode) | Emoji表情编码方法，支持自定义编码类型；
public static String encodeUnicode(String input) | Emoji表情编码方法，编码类型默认为 "ALIASES" ；
public static String decodeToUnicode(String input) | Emoji表情解码方法，支持针对 "ALIASES、HTML_DECIMAL、HTML_HEX_DECIMAL" 等编码方式解码；
public static String removeEmojis(String input, final Collection<Emoji> emojisToRemove, final Collection<Emoji> emojisToKeep) | 清除输入字符串中的Emoji数据；
public static List<String> findEmojis(String input) | 查找输入字符转中的全部Emoji数据列表；

**自定义Emoji别名**

略

**使用指南**

- a、使用示例

```java
// 参考测试代码：com.xxl.tool.test.emoji.EmojiToolTest

String input = "一朵美丽的茉莉🌹";
System.out.println("unicode：" + input);

// 1、alias
String aliases = EmojiTool.encodeUnicode(input, EmojiEncode.ALIASES);
System.out.println("\naliases encode: " + aliases);
System.out.println("aliases decode: " + EmojiTool.decodeToUnicode(aliases, EmojiEncode.ALIASES));

// 2、html decimal
String decimal = EmojiTool.encodeUnicode(input, EmojiEncode.HTML_DECIMAL);
System.out.println("\ndecimal encode: " + decimal);
System.out.println("decimal decode: " + EmojiTool.decodeToUnicode(decimal, EmojiEncode.HTML_DECIMAL));

// 3、html hex decimal
String hexdecimal = EmojiTool.encodeUnicode(input, EmojiEncode.HTML_HEX_DECIMAL);
System.out.println("\nhexdecimal encode: " + hexdecimal);
System.out.println("hexdecimal decode: " + EmojiTool.decodeToUnicode(hexdecimal, EmojiEncode.HTML_HEX_DECIMAL));
        
```

- b、运行日志输出

```text
aliases encode: 一朵美丽的茉莉:rose:
aliases decode: 一朵美丽的茉莉🌹

decimal encode: 一朵美丽的茉莉&#127801;
decimal decode: 一朵美丽的茉莉🌹

hexdecimal encode: 一朵美丽的茉莉&#x1f339;
hexdecimal decode: 一朵美丽的茉莉🌹
```

### 2.7、Freemarker 模块

参考单元测试，见目录：com.xxl.tool.test.freemarker.FtlTool

```
// 初始化设置 模板文件目录地址
FtlTool.init("/Users/admin/Downloads/");

// 根据模板文件，生成文本；支持传入变量
String text = FtlTool.processString("test.ftl", new HashMap<>());
logger.info(text);
```

### 2.8、Http 模块

参考单元测试，见目录：com.xxl.tool.test.http.HttpToolTest
```
// Http Post 请求
String resp = HttpTool.postBody("http://www.baidu.com/", "hello world");
String resp = HttpTool.postBody("http://www.baidu.com/", "hello world", 3000);
String resp = HttpTool.postBody("http://www.baidu.com/", "hello world", 3000, headers);
        
// Http Get 请求
String resp = HttpTool.get("http://www.baidu.com/");
String resp = HttpTool.get("http://www.baidu.com/", 3000);
String resp = HttpTool.get("http://www.baidu.com/", 3000, null);
```

### 2.9、IP 模块

参考单元测试，见目录：com.xxl.tool.test.http.IPToolTest
```
// Port相关
IPTool.isPortInUsed(port);    
IPTool.isValidPort(port);
IPTool.getRandomPort();
IPTool.getAvailablePort();

// Host相关
IPTool.isLocalHost(host));
IPTool.isAnyHost(host));
IPTool.isValidLocalHost(host));
IPTool.getIp();   // 兼容多网卡

// Address相关
IPTool.isValidV4Address(address)
IPTool.toAddressString(new InetSocketAddress(host, port)));
IPTool.toAddress(address));
```

### 2.10、JsonRpc

**功能定位**
一个轻量级、跨语言远程过程调用实现，基于json、http实现（传统RPC框架对比：[XXL-RPC](https://github.com/xuxueli/xxl-rpc)）。

参考单元测试，见目录：
- com.xxl.tool.test.jsonrpc.service.UserService：RPC业务代码
- com.xxl.tool.test.jsonrpc.TestServer：服务端代码
- com.xxl.tool.test.jsonrpc.TestClient：客户端代码

RPC业务代码：
```
public interface UserService {
    public ResultDTO createUser(UserDTO userDTO);
    public UserDTO loadUser(String name);
    ... ...
}
```

服务端代码：
```
// a、JsonRpcServer 初始化
JsonRpcServer jsonRpcServer = new JsonRpcServer();

// b、业务服务注册（支持多服务注册）
jsonRpcServer.register("userService", new UserServiceImpl());

// c、Web框架集成（支持与任意web框架集成，如下以最简单原生 HttpServer 为例讲解；可参考集成springmvc等；）
HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
httpServer.createContext("/jsonrpc", new HttpHandler() {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ... ...
        // 核心代码：Http请求的 RequestBody 作为入参；业务响应作为输出（服务路由匹配、）；
        String jsonRpcResponse = jsonRpcServer.invoke(requestBody);
        ... ...        
    }
});
```

客户端代码：
```
// 方式1：代理方式使用 （针对接口构建代理，通过代理对象实现远程调用；）
UserService userService = new JsonRpcClient("http://localhost:8080/jsonrpc", 3000)
                                    .proxy("userService", UserService.class);   // 根据接口创建代理对象
UserDTO result = userService.loadUser("zhangsan");


// 方式2：常规客户端方式 （针对目标地址构建Client，手动设置请求细节参数；）
JsonRpcClient jsonRpcClient = new JsonRpcClient("http://localhost:8080/jsonrpc", 3000);
UserDTO result2 = jsonRpcClient.invoke(
        "userService",                    // 服务名称
        "loadUser",                       // 方法名称
        new Object[]{ "zhangsan" },       // 参数列表
        UserDTO.class);                   // 返回类型
```

### 2.11、Concurrent模块

一系列并发编程工具，具备良好的线程安全、高并发及高性能优势，包括循环线程、高性能队列等。

**CyclicThread （循环/后台线程）**    
说明：专注于周期性执行/后台服务场景，具备良好的线程安全和异常处理机制。

参考单元测试，见目录：com.xxl.tool.test.concurrent.CyclicThread
```
// 定义循环线程
CyclicThread threadHelper = new CyclicThread(
      "demoCyclicThread",     // 线程名称
      true,                   // 是否后台执行
      200,                    // 循环执行时间间隔（单位：毫秒）
      new Runnable() {        // 线程执行逻辑
          @Override
          public void run() {
              System.out.println("thread running ... ");
          }
      });
                
// 启动
threadHelper.start();

// 停止
threadHelper.stop();
```

**ProducerConsumerQueue （高性能/生产消费队列）**            
说明：高性能内存队列，具备良好的性能及高并发优势，支持生产消费模型。

```
// 定义队列
ProducerConsumerQueue<Long> queue = new ProducerConsumerQueue<>(
        "demoQueue",                // 队列名称
        10000,                      // 队列容量
        3,                          // 队列消费线程数
        new Consumer<Long>() {      // 消费者消费逻辑
            @Override
            public void accept(Long data) {
                totalCount.addAndGet(data * -1);
                System.out.println("消费: -" + data + ", Current count : " + totalCount.get());
            }
        }
);

// 生产消息 
queue.produce(addData);

// 停止队列 （需主动停止）
queue.stop();
```


### 2.12、更多
略


## 三、版本更新日志
### 3.1 v1.0.0 Release Notes[2017-09-13]
- 1、Excel导出：支持Java对象装换为Excel，并且支持File、字节数组、Workbook等多种导出方式；
- 2、Excel导入：支持Excel转换为Java对象，并且支持File、InputStream、文件路径、Workbook等多种导入方式；

### 3.2 v1.1.0 Release Notes[2017-12-14]
- 1、字段支持Date类型。至此，已经支持全部基础数据类型。
- 2、Java转换Excel时，字段类型改为从Field上读取，避免Value为空时空指针问题。
- 3、升级POI至3.17版本；
- 4、支持设置Field宽度；如果不指定列宽，将会自适应调整宽度；
- 5、多Sheet导出：导出Excel时支持设置多张sheet；
- 6、多Sheet导入：导入Excel时支持设置多张sheet，通过 "@ExcelSheet.name" 注解匹配Sheet;

### 3.3 v1.1.1 Release Notes[2018-10-24]
- 1、支持设置Field水平位置，如居中、居左；
- 2、底层API优化，预约多Sheet操作支持；
- 3、空Cell导入抛错问题修复；
- 4、Cell数据类型识别优化，全类型支持；
- 5、导入时支持空Excel；导出时限制非空，否则无法进行类型推导。

### 3.4 v1.2.0 Release Notes[2020-04-16]
- 将 XXL-EXCEL 和 XXL-Emoji 两个单独项目，统一合并至 XXL-TOOL，方便统一迭代维护；
- excel模块：
  - 1、Excel 多版本导入导出兼容支持，包括：HSSFWorkbook=2003/xls、XSSFWorkbook=2007/xlsx ；
  - 2、升级POI至4.1.2版本；
- emoji模块：
  - 1、json组件调整为调整为gson；

### 3.5 v1.3.0 Release Notes[2024-06-09]
- 1、开源协议变更，由 GPLv3 调整为 Apache2.0 开源协议；
- 2、新增Response模块，统一响应数据结构体，标准化数据结构、状态码等，降低协作成本；
- 3、新增Pipeline模块，高扩展性流程编排引擎；
- 4、新增Freemarker模块，模板引擎工具，支持根据模板文件生成文本、生成文件…等。

### 3.6 v1.3.1 Release Notes[2024-11-09]
- 1、【强化】已有工具能力完善，包括：StringTool、GsonTool 等；
- 2、【新增】新增多个工具类模块，包括：FtlTool、CookieTool、PageModel、CacheTool、StreamTool 等；
- 3、【完善】工具类单测完善；
- 4、【升级】升级依赖版本，如slf4j、poi、spring、gson…等。

### 3.7 v1.3.2 Release Notes[2024-12-29]
- 1、【新增】新增多个工具类模块，包括：Md5Tool、HexTool、HttpTool 等；
- 2、【完善】工具类单测完善；
- 3、【升级】升级依赖版本，如freemarker、junit…等。

### 3.8 v1.4.0 Release Notes[迭代中]
- 1、【新增】JsonRpc模块：一个轻量级、跨语言远程过程调用实现，基于json、http实现（传统RPC框架对比：[XXL-RPC](https://github.com/xuxueli/xxl-rpc)）。
- 2、【新增】Concurrent模块：一系列并发编程工具，具备良好的线程安全、高并发及高性能优势，包括循环线程、高性能队列等。
- 3、【强化】已有工具能力完善，包括：CollectionTool、MapTool、HttpTool 等；
- 4、【升级】升级依赖版本，如slf4j、poi、spring、gson…等。


### TODO LIST
- excel模块：大数据导出，流式导入导出；
- excel模块
  - 1、单个Excel多Sheet导出导出；
  - 2、列合并导入导出；
  - 3、行合并导入导出；
  - 4、同一个单元格，横向、竖向拆分多个单元格；List属性；
  - 5、流式导入：多批次导入数据；
  - 7、流式导出：分页方式导出数据；
  - 6、单表行数限制：2003/xls=65536，2007/xlsx=1048576；行数限制内进行性能测试和优化；
  - 8、排序的字段，对时间等其他类型的处理。
  - 9、Java已经支持全基础数据类型导入导出，但是Excel仅支持STRING类型CELL，需要字段属性支持定义CELL类型；
  - 10、Excel导入多Sheet支持，API 格式 "Map<String, List<Object>> importExcel(String filePath, Class<?> sheetClass ...)" ；
  - 11、Excel导入、导出时，CellType 全类型支持，如string、number、date等；
- emoji模块
  - 1、Emoji远程编解码服务；
  - 2、升级Emoji版本至最新Release版本：Unicode Emoji 11.0；


## 四、其他

### 4.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-tool/issues/) 讨论新特性或者变更。

### 4.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-tool/issues/1 ) 登记，登记仅仅为了产品推广。

### 4.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the Apache License, Version 2.0.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
XXL-TOOL 是一个开源且免费项目，其正在进行的开发完全得益于支持者的支持。开源不易，[前往赞助项目开发](https://www.xuxueli.com/page/donate.html )
