## 《分布式单点登录框架XXL-SSO》

[![Actions Status](https://github.com/xuxueli/xxl-sso/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-sso/actions)
[![Maven Central](https://img.shields.io/maven-central/v/com.xuxueli/xxl-sso-core)](https://central.sonatype.com/artifact/com.xuxueli/xxl-sso-core/)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-sso.svg)](https://github.com/xuxueli/xxl-sso/releases)
[![GitHub stars](https://img.shields.io/github/stars/xuxueli/xxl-sso)](https://github.com/xuxueli/xxl-sso/)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![donate](https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square)](https://www.xuxueli.com/page/donate.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-SSO 是 单点登录框架，只需要登录一次就可以访问所有相互信任的应用系统。具备 “轻量级、高扩展、渐进式” 的等特性，支持 “登录认证、权限(角色)认证、分布式会话认证、单点登录、Web登录、Native(前后端分离)登录” 等多登录及认证类型，现已开放源代码，开箱即用。

### 1.2 特性

- 1、易用性：支持注解/API多方式接入，一行注解/代码即可实现 登录认证、权限认证、角色认证 等，接入灵活方便；
- 2、轻量级：针对第三方组件、部署环境零依赖，部署及接入低成本、轻量级；
- 3、高扩展：得益于模块化抽象设计，各框架组件可灵活扩展；可选用官方提供组件实现或自定义扩展。
  - 登录态持久化组价（LoginStore）：提供登录态/会话数据持久化能力；官方提供Cache、Redis等组件实现，可选用接入或自定义扩展；
  - 登录认证组件（Auth）：提供系统登录/认证集成能力；官方提供 Filter（Servlet）和Interceptor（Spring）等实现，可选用接入或自定义扩展；
  - 登录用户模型（LoginInfo）：提供统一登录用户模型，且模型支持扩展存储自定义扩展属性；
- 4、渐进式：支持渐进式集成接入使用，从简单到复杂场景，包括：单体Web系统、复杂企业内多系统、互联网多端高并发系统 等，均可接入使用；
- 5、多登录类型：
  - Web登录：适用于常规Web系统，不限制接入系统数量；但是限制相关Web系统部署在相同域名下，登录凭证存储在公共域名下；
  - Native登录：适用于 移动端、小程序、前后端分离系统 等系统，不限制接入系统数量，且无域名限制，支持多端登录；但是登录凭证需要客户端管理维护；
  - CAS单点登录：适用于多Web系统部署域名不一致场景，解决了系统 跨域登录认证 问题；但是需要单独部署CAS认证中心，CAS认证中心提供单点登录基础能力；
- 6、多认证方式：
  - 登录认证：本质为验证用户身份的过程，目的是确认“你是谁”，确保访问者合法可信；
  - 权限认证：在用户身份认证通过后，校验用户是否具备访问特定资源的权限，决定“你能做什么”；认证维度是“权限”；
  - 角色认证：在用户身份认证通过后，校验用户是否具备访问特定资源的权限，决定“你能做什么”；认证维度是“角色”；
- 7、安全性：针对系统框架多个模块落地安全性设计，包括：登录Token安全设计、客户端登录凭证Cookie安全设计、CAS跳转Ticket安全设计 等；
- 8、分布式会话/认证：支持分布式登录以及会话认证，集成分布式系统可共享的 登录态持久化组价（LoginStore），可选用或参考官方RedisLoginStore；
- 9、单点登录/注销：针对CAS单点登录场景，提供单点登录及注销能力；
- 10、高可用/HA：针对CAS单点登录场景，CAS认证中心支持集群部署，并可借助LoginStore实现登录态共享，从而实现系统水平扩展以及高可用；
- 11、跨域登录认证：针对CAS单点登录场景，支持跨域Web应用接入，解决了系统 跨域登录认证 问题；
- 12、多端登录认证：针对多端登录场景，如 Web、移动端、小程序 等多端，提供多端登录及认证能力；
- 13、前后端分离认证：针对前后端分离系统，提供 Native登录 方案，支持前后端分离场景登录认证能力；
- 14、记住密码：支持记住密码功能；记住密码时，支持登录态自动延期；未记住密码时，关闭浏览器则登录态失效；
- 15、登录态自动延期：支持自定义登录态有效期窗口，当登录态有效期窗口过半时自动顺延一个周期；

### 1.3 发展
于2018年初，我在github上创建XXL-SSO项目仓库并提交第一个commit，随之进行系统结构设计，UI选型，交互设计……

于2018-12-05，XXL-SSO参与"[2018年度最受欢迎中国开源软件](https://www.oschina.net/project/top_cn_2018?sort=1)"评比，在当时已录入的一万多个国产开源项目中角逐，最终排名第55名。

于2019-01-23，XXL-SSO被评选上榜"[2018年度新增开源软件排行榜之国产 TOP 50](https://www.oschina.net/news/103857/2018-osc-new-opensource-software-cn-top50)"评比，排名第8名。

至今，XXL-SSO已接入多家公司的线上产品线，接入场景如电商业务，O2O业务和核心中间件配置动态化等，截止2018-03-15为止，XXL-SSO已接入的公司包括不限于：

    1. 湖南创发科技
    2. 深圳龙华科技有限公司
    3. 摩根国际
    4. 印记云
    5、小太阳CRM
    6、盛歌行科技（深圳）有限公司
    7、苏州安软

> 更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-sso/issues/1 ) 登记，登记仅仅为了产品推广。

欢迎大家的关注和使用，XXL-SSO也将拥抱变化，持续发展。

### 1.4 下载

#### 文档地址
- [中文文档](https://www.xuxueli.com/xxl-sso/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-sso](https://github.com/xuxueli/xxl-sso) | [Download](https://github.com/xuxueli/xxl-sso/releases)
[https://gitee.com/xuxueli0323/xxl-sso](https://gitee.com/xuxueli0323/xxl-sso) | [Download](https://gitee.com/xuxueli0323/xxl-sso/releases)  
[https://gitcode.com/xuxueli/xxl-sso](https://gitcode.com/xuxueli/xxl-sso) | [Download](https://gitcode.com/xuxueli/xxl-sso/releases)

#### 中央仓库地址

```
<!-- https://mvnrepository.com/artifact/com.xuxueli/xxl-sso-core -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-sso-core</artifactId>
    <version>{最新Release版本}</version>
</dependency>
```

#### 技术交流
- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.5 环境

- Maven：3.9+
- Jdk：8+
- Redis：7.4+


## 二、快速入门

### 2.1、项目编辑
XXL-SSO 作为单点登录框架，支持业务渐进式集成接入使用。结合系统及业务特征差异，仓库代码提供三种业务中接入示例：

- 1、**Web登录**：适用于常规Web系统，不限制接入系统数量；但是限制相关Web系统部署在相同域名下，登录凭证存储在公共域名下；
- 2、**Native登录**：适用于 移动端、小程序、前后端分离系统 等系统，不限制接入系统数量，且无域名限制，支持多端登录；但是登录凭证需要客户端管理维护；
- 3、**CAS单点登录**：适用于多Web系统部署域名不一致场景，解决了系统 跨域登录认证 问题；但是需要单独部署CAS认证中心，CAS认证中心提供单点登录基础能力；

```
- xxl-sso-core: 客户端 核心依赖, 提供登录态持久化、登录认证及权限认证等能力；
- xxl-sso-server: CAS认证中心，仅 “CAS单点登录” 场景下才会使用；
- xxl-sso-samples：接入示例项目
    - xxl-sso-sample-web: Web登录方式，Interceptor（Spring）接入示例；
    - xxl-sso-sample-native: Native登录，Interceptor（Spring）接入示例；
    - xxl-sso-sample-cas: CAS单点登录，Interceptor（Spring）接入示例；
    - filter：
        - xxl-sso-sample-filter-web: Web登录方式，Filter（Servlet）接入示例；
        - xxl-sso-sample-filter-native: Native登录，Filter（Servlet）接入示例；
        - xxl-sso-sample-filter-cas: CAS单点登录，Filter（Servlet）接入示例；
```

### 2.2、环境配置
为模拟并体验生产环境多种登录认证方式，需要修改Host文件，以域名方式访问示例项目：

```
### 编辑Host 文件，在文件中添加以下内容：
127.0.0.1 xxlssoserver.com
127.0.0.1 xxlssoclient1.com
127.0.0.1 xxlssoclient2.com
```

### 2.3、接入项目示例（Web登录）

- Web登录：适用于常规Web系统，不限制接入系统数量；但是限制相关Web系统部署在相同域名下，登录凭证存储在公共域名下；
- 接入示例项目：xxl-sso-sample-web

#### 第一步、引入Maven依赖
参考章节 “1.4 下载”，pom引入 “xxl-sso-core” 相关maven依赖；

#### 第二步、添加 XXL-SSO 配置

配置文件位置：xxl-sso-sample-web/src/main/resources/application.properties

```
### xxl-sso 登录凭证/token传输key, 用于cookie、header登录凭证传输；
xxl-sso.token.key=xxl_sso_token
### xxl-sso 登录凭证/token超时时间，单位毫秒；
xxl-sso.token.timeout=604800000
### xxl-sso 登录态持久化配置，如下为Redis组件相关配置；
xxl-sso.store.redis.nodes=127.0.0.1:6379
xxl-sso.store.redis.user=
xxl-sso.store.redis.password=
### xxl-sso 登录态存储，Redis key前缀
xxl-sso.store.redis.keyprefix=xxl_sso_user:
### xxl-sso 客户端过滤排除路径，如 "/excluded/xpath"?"/excluded/xpath,/excluded/*"
xxl-sso.client.excluded.paths=/weblogin/*,/static/**
### xxl-sso 客户端登录页路径
xxl.sso.client.login.path=/weblogin/login
```

#### 第三步、添加 XXL-SSO 组件

配置组件位置：xxl-sso-sample-web/src/main/java/com/xxl/sso/sample/config/XxlSsoConfig

```
/**
 * 1、配置 XxlSsoBootstrap
 */
@Bean(initMethod = "start", destroyMethod = "stop")
public XxlSsoBootstrap xxlSsoBootstrap() {

    XxlSsoBootstrap bootstrap = new XxlSsoBootstrap();
    bootstrap.setLoginStore(new RedisLoginStore(
            redisNodes,
            redisUser,
            redisPassword,
            redisKeyprefix));
    bootstrap.setTokenKey(tokenKey);
    bootstrap.setTokenTimeout(tokenTimeout);
    return bootstrap;
}

/**
 * 2、配置 XxlSso 拦截器
 *
 * @param registry
 */
@Override
public void addInterceptors(InterceptorRegistry registry) {

    // 2.1、build xxl-sso interceptor 
    XxlSsoWebInterceptor webInterceptor = new XxlSsoWebInterceptor(excludedPaths, loginPath);   // 【专门用于支持 “Web登录” 的 XXL-SSO 拦截器】

    // 2.2、add interceptor
    registry.addInterceptor(webInterceptor).addPathPatterns("/**");
}
```


#### 第四步、代码中进行 XXL-SSO 登录验证、权限验证

接入 XXL-SSO 之后，业务可通过 注解 or API 进行 登录验证、权限验证。
示例代码位置：com.xxl.sso.sample.controller.IndexController

```
/**
 * 示例：不添加注解，限制登录
 *
 * @param request
 * @return
 */
@RequestMapping("/test11")
@ResponseBody
public Response<String> test11(HttpServletRequest request) {
    Response<LoginInfo> loginCheckResult = XxlSsoHelper.loginCheckWithAttr(request);
    return Response.ofSuccess("login success, LoginInfo:" + loginCheckResult.getData().getUserName());
}

/**
 * 示例：默认注解（@XxlSso），限制登录
 *
 * @return
 */
@RequestMapping("/test12")
@ResponseBody
@XxlSso
public Response<String> test12(HttpServletRequest request) {
    Response<LoginInfo>  loginCheckResult = XxlSsoHelper.loginCheckWithAttr(request);
    return Response.ofSuccess("login success, userName = " + loginCheckResult.getData().getUserName());
}

/**
 * 示例：注解login属性定制“@XxlSso(login = false)”，不限制登录
 *
 * @return
 */
@RequestMapping("/test13")
@ResponseBody
@XxlSso(login = false)
public Response<String> test13() {
    return Response.ofSuccess("not check login.");
}

/**
 * 示例：注解permission属性定制“@XxlSso(permission = "user:query")”，限制拥有指定权限
 *
 * @return
 */
@RequestMapping("/test21")
@ResponseBody
@XxlSso(permission = "user:query")
public Response<String> test21() {
    return Response.ofSuccess("has permission[user:query]");
}

/**
 * 示例：注解permission属性定制“@XxlSso(permission = "user:delete")”，限制拥有指定权限
 *
 * @return
 */
@RequestMapping("/test22")
@ResponseBody
@XxlSso(permission = "user:delete")
public Response<String> test22() {
    return Response.ofSuccess("has permission[user:delete]");
}

/**
 * 示例：注解role属性定制“@XxlSso(role = "role01")”，限制拥有指定角色
 *
 * @return
 */
@RequestMapping("/test31")
@ResponseBody
@XxlSso(role = "role01")
public Response<String> test31() {
    return Response.ofSuccess("has role[role01]");
}

/**
 * 示例：注解role属性定制“@XxlSso(role = "role02")”，限制拥有指定角色
 *
 * @return
 */
@RequestMapping("/test32")
@ResponseBody
@XxlSso(role = "role02")
public Response<String> test32() {
    return Response.ofSuccess("has role[role02]");
}

/**
 * 示例：API方式获取登录用户信息（LoginInfo）；API方式校验角色、权限；
 *
 * @return
 */
@RequestMapping("/test41")
@ResponseBody
@XxlSso
public Response<String> test41(HttpServletRequest request) {

    Response<LoginInfo> loginCheckResult = XxlSsoHelper.loginCheckWithAttr( request);
    Response<String> hasRole01 = XxlSsoHelper.hasRole(loginCheckResult.getData(), "role01");
    Response<String> hasRole02 = XxlSsoHelper.hasRole(loginCheckResult.getData(), "role02");
    Response<String> hasPermission01 = XxlSsoHelper.hasPermission(loginCheckResult.getData(), "user:query");
    Response<String> hasPermission02 = XxlSsoHelper.hasPermission(loginCheckResult.getData(), "user:delete");

    String data = "LoginInfo:" + loginCheckResult.getData() +
            ", hasRole01:" + hasRole01.isSuccess() +
            ", hasRole02:" + hasRole02.isSuccess() +
            ", hasPermission01:" + hasPermission01.isSuccess() +
            ", hasPermission02:" + hasPermission02.isSuccess();

    return Response.ofSuccess(data);
}

```

#### 第五步、验证

经过上述配置之后，启动示例项目，并访问如下域名地址访问系统：
- **启动项目**：xxl-sso-sample-web
- **访问地址**：http://xxlssoclient1.com:8083/xxl-sso-sample-web/

由于未进行登录，XXL-SSO 将会自动跳转到配置的登录页面：
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-sso/images/img_02.png "在这里输入图片标题")

页面登录之后，将会跳转到系统主页：
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-sso/images/img_03.png "在这里输入图片标题")


访问如下接口地址，验证 登录验证、权限验证 逻辑：（参考 “第四步、代码中进行 XXL-SSO 登录验证、权限验证” 中代码）

- 访问示例接口（需要登录，有权限）：http://xxlssoclient1.com:8083/xxl-sso-sample-web/test21
- 访问示例接口（需要登录，无权限）：http://xxlssoclient1.com:8083/xxl-sso-sample-web/test22

```
// 未登录，接口响应数据：
{"code":203,"msg":"com.xxl.sso.core.exception.XxlSsoException: not login for path:/test21"}

// 已登录：“/test21” 接口有权限 
{"code":200,"msg":"成功","data":"has permission[user:query]","success":true}
// 已登录：“/test21” 接口无权限  
{"code":203,"msg":"com.xxl.sso.core.exception.XxlSsoException: permission limit, current login-user does not have permission:user:delete"}
```

### 2.4、接入项目示例（Native登录）

- Native登录：适用于 移动端、小程序、前后端分离系统 等系统，不限制接入系统数量，且无域名限制，支持多端登录；但是登录凭证需要客户端管理维护；
- 接入示例项目：xxl-sso-sample-native

#### 第一步、引入Maven依赖
参考章节 “1.4 下载”，pom引入 “xxl-sso-core” 相关maven依赖；

#### 第二步、添加 XXL-SSO 配置

配置文件位置：xxl-sso-sample-native/src/main/resources/application.properties

```
### xxl-sso 登录凭证/token传输key, 用于cookie、header登录凭证传输；
xxl-sso.token.key=xxl_sso_token
### xxl-sso 登录凭证/token超时时间，单位毫秒；
xxl-sso.token.timeout=604800000
### xxl-sso 登录态持久化配置，如下为Redis组件相关配置；
xxl-sso.store.redis.nodes=127.0.0.1:6379
xxl-sso.store.redis.user=
xxl-sso.store.redis.password=
### xxl-sso 登录态存储，Redis key前缀
xxl-sso.store.redis.keyprefix=xxl_sso_user:
### xxl-sso 客户端过滤排除路径，如 "/excluded/xpath"?"/excluded/xpath,/excluded/*"
xxl-sso.client.excluded.paths=/native/openapi/*
```

#### 第三步、添加 XXL-SSO 组件

配置组件位置：xxl-sso-sample-native/src/main/java/com/xxl/sso/sample/config/XxlSsoConfig

```
/**
 * 1、配置 XxlSsoBootstrap
 */
@Bean(initMethod = "start", destroyMethod = "stop")
public XxlSsoBootstrap xxlSsoBootstrap() {

    XxlSsoBootstrap bootstrap = new XxlSsoBootstrap();
    bootstrap.setLoginStore(new RedisLoginStore(
            redisNodes,
            redisUser,
            redisPassword,
            redisKeyprefix));
    bootstrap.setTokenKey(tokenKey);
    bootstrap.setTokenTimeout(tokenTimeout);
    return bootstrap;
}


/**
 * 2、配置 XxlSso 拦截器
 *
 * @param registry
 */
@Override
public void addInterceptors(InterceptorRegistry registry) {

    // 2.1、build xxl-sso interceptor
    XxlSsoNativeInterceptor webInterceptor = new XxlSsoNativeInterceptor(excludedPaths);    // 【专门用于支持 “Native登录” 的 XXL-SSO 拦截器】

    // 2.2、add interceptor
    registry.addInterceptor(webInterceptor).addPathPatterns("/**");
}
```

#### 第四步、代码中进行 XXL-SSO 登录验证、权限验证
忽略。
该示例场景与章节 “2.3、接入项目示例（Web登录） 》 第四步、代码中进行 XXL-SSO 登录验证、权限验证” 使用方式一致，如有诉求可参考上文章节，此处不赘述。

#### 第五步、Native登录认证 OpenAPI
Native登录认证 OpenAPI 代码位置：xxl-sso-sample-native/src/main/java/com/xxl/sso/sample/openapi/controller/NativeOpenAPIController

Native登录认证 OpenAPI 接口列表：

##### a、登录 API

```
地址格式：{Native认证服务地址}/native/openapi/login

请求数据格式如下，放置在 RequestBody 中，JSON格式：
    {
        "username" : "xxxxxx",
        "password" : "xxxxxx"
    }
    
响应数据格式：
    {
        "code": 200,                // 200 表示成功
        "msg": "",                  // 错误提示消息
        "data": "xxxx"              // 登录凭证/Token信息；登录成功后才返回；
    }
```

##### b、注销 API

```
地址格式：{Native认证服务地址}/native/openapi/logout

请求数据格式如下，放置在 RequestBody 中，JSON格式：
    {
        "username" : "xxxxxx",
        "password" : "xxxxxx"
    }
    
响应数据格式：
    {
        "code": 200,                // 200 表示成功
        "msg": ""                   // 错误提示消息
    }
```

##### c、登录信息查询 API

```
地址格式：{Native认证服务地址}/native/openapi/logincheck

请求数据格式如下，放置在 RequestBody 中，JSON格式：
    {
        "token" : "xxxxxx"
    }
    
响应数据格式：
    {
        "code": 200,                    // 200 表示成功
        "msg": "",                      // 错误提示消息
        "data": {                       // 登录用户信息
            "userId": "111",
            "userName": "xxx",
            "realName": "xxx",
            "roleList": ["role1", "role2"],
            "permissionList": ["xxx", "xxx"]
        }                  
    }
```

#### 第六步、验证

经过上述配置之后，启动示例项目；然后，可通过 测试用例/验证代码 模拟 “Native登录” 相关操作：
- **启动项目**：xxl-sso-sample-native
- **Native登录认证，测试用例/验证代码位置**：xxl-sso-sample-native/src/test/java/com/xxl/app/sample/test/NativeClientTest

测试用例/验证代码，具体逻辑为：
- **登录流程**：
  - 1、获取用户输入的账号密码后，请求 Native OpenAPI 登录接口，获取用户 登录凭证/token ；（参考代码：NativeClientTest.login）
  - 2、登陆成功后，获取到 登录凭证/token，需要主动存储，后续请求时需要设置在 Header参数 中
  - 3、此时，使用 登录凭证/token 访问受保护的 "Client01应用" 和 "Client02应用" 提供的接口，接口均正常返回（参考代码：NativeClientTest.clientApiRequestTest）
- **注销流程**：
  - 1、请求 Native OpenAPI 注销接口，注销登陆凭证 登录凭证/token；（参考代码：NativeClientTest.logout）
  - 2、注销成功后，登录凭证/token 将会全局失效
  - 3、此时，使用 登录凭证/token 访问受保护的 "Client01应用" 和 "Client02应用" 提供的接口，接口请求将会被拦截，提示未登录并返回状态码 501（参考代码：NativeClientTest.clientApiRequestTest）

测试用例运行结果：

```
15:39:42.831 logback [main] INFO  NativeClientTest - 登录成功：token = eyJ1c2VySWQiOiIxMDAwIiwiZXhwaXJlVGltZSI6MCwidmVyc2lvbiI6IjA2ZmRhOGFhZmU2MzRhMzBhYzYzZWQ4ZjM1YjBjNTExIiwiYXV0b1JlbmV3IjpmYWxzZX0
15:39:42.838 logback [main] INFO  NativeClientTest - 当前为登录状态：登陆用户 = LoginInfo{userId='1000', userName='user', realName='null', extraInfo=null, roleList=[role01], permissionList=[user:query, user:add], expireTime='1753601982825', version=06fda8aafe634a30ac63ed8f35b0c511, autoRenew=false}
15:39:42.841 logback [main] INFO  NativeClientTest - 模拟请求APP应用接口：请求成功，登陆用户 = LoginInfo{userId='1000', userName='user', realName='null', extraInfo=null, roleList=[role01], permissionList=[user:query, user:add], expireTime='1753601982825', version=06fda8aafe634a30ac63ed8f35b0c511, autoRenew=false}
15:39:43.174 logback [main] INFO  NativeClientTest - 模拟请求APP应用接口：请求成功，登陆用户 = LoginInfo{userId='1000', userName='user', realName='null', extraInfo=null, roleList=[role01], permissionList=[user:query, user:add], expireTime='1753601982825', version=06fda8aafe634a30ac63ed8f35b0c511, autoRenew=false}
15:39:43.177 logback [main] INFO  NativeClientTest - 注销成功
15:39:43.180 logback [main] INFO  NativeClientTest - 当前为注销状态
15:39:43.181 logback [main] INFO  NativeClientTest - 模拟请求APP应用接口：请求失败：not login for path:/
15:39:43.183 logback [main] INFO  NativeClientTest - 模拟请求APP应用接口：请求失败：not login for path:/
```

### 2.5、接入项目示例（CAS单点登录）

- CAS单点登录：适用于多Web系统部署域名不一致场景，解决了系统 跨域登录认证 问题；但是需要单独部署CAS认证中心，CAS认证中心提供单点登录基础能力；
- 项目说明：
  - CAS认证中心: xxl-sso-server
  - 接入示例项目: xxl-sso-sample-cas

#### 第一步、部署 CAS认证中心

CAS单点登录 依赖 CAS认证中心，CAS认证中心提供单点登录基础能力；因此需要先行启动 CAS认证中心。
- CAS认证中心，启动项目: xxl-sso-server

#### 第一步、示例项目引入Maven依赖
参考章节 “1.4 下载”，pom引入 “xxl-sso-core” 相关maven依赖；

#### 第二步、添加 XXL-SSO 配置

配置文件位置：xxl-sso-sample-cas/src/main/resources/application.properties

```
### xxl-sso 登录凭证/token传输key, 用于cookie、header登录凭证传输；
xxl-sso.token.key=xxl_sso_token
### xxl-sso 登录凭证/token超时时间，单位毫秒；
xxl-sso.token.timeout=604800000
### xxl-sso 登录态持久化配置，如下为Redis组件相关配置；
xxl-sso.store.redis.nodes=127.0.0.1:6379
xxl-sso.store.redis.user=
xxl-sso.store.redis.password=
### xxl-sso 登录态存储，Redis key前缀
xxl-sso.store.redis.keyprefix=xxl_sso_user:
### xxl-sso CAS认证中心 地址；
xxl.sso.server.address=http://xxlssoserver.com:8080/xxl-sso-server
### xxl-sso CAS认证中心 登录跳转路径
xxl.sso.server.login.path=/login
### xxl-sso CAS认证中心 注销登录跳转路径
xxl.sso.server.logout.path=/logout
### xxl-sso 客户端过滤排除路径，如 "/excluded/xpath"?"/excluded/xpath,/excluded/*"
xxl-sso.client.excluded.paths=
```

#### 第三步、添加 XXL-SSO 组件

配置组件位置：xxl-sso-sample-cas/src/main/java/com/xxl/sso/sample/config/XxlSsoConfig

```
/**
 * 1、配置 XxlSsoBootstrap
 */
@Bean(initMethod = "start", destroyMethod = "stop")
public XxlSsoBootstrap xxlSsoBootstrap() {
    XxlSsoBootstrap bootstrap = new XxlSsoBootstrap();
    bootstrap.setLoginStore(new RedisLoginStore(
            redisNodes,
            redisUser,
            redisPassword,
            redisKeyprefix));
    bootstrap.setTokenKey(tokenKey);
    bootstrap.setTokenTimeout(tokenTimeout);

    return bootstrap;
}


/**
 * 2、配置 XxlSso 拦截器
 *
 * @param registry
 */
@Override
public void addInterceptors(InterceptorRegistry registry) {

    // 2.1、build xxl-sso interceptor
    XxlSsoCasInterceptor casInterceptor = new XxlSsoCasInterceptor(serverAddress, loginPath, excludedPaths);        // 【专门用于支持 “CAS单点登录” 的 XXL-SSO 拦截器】

    // 2.2、add interceptor
    registry.addInterceptor(casInterceptor).addPathPatterns("/**");
}
```

#### 第四步、代码中进行 XXL-SSO 登录验证、权限验证
忽略。
该示例场景与章节 “2.3、接入项目示例（Web登录） 》 第四步、代码中进行 XXL-SSO 登录验证、权限验证” 使用方式一致，如有诉求可参考上文章节，此处不赘述。

#### 第五步、验证

经过上述配置之后，启动示例项目。整体项目模块，以及可访问域名地址如下。
- **启动项目**：
  - CAS认证中心: xxl-sso-server
  - 接入方应用：xxl-sso-sample-cas
- **访问地址**：
  - CAS认证中心，域名地址：http://xxlssoserver.com:8080/xxl-sso-server/
  - Client应用01，域名地址：http://xxlssoclient1.com:8081/xxl-sso-sample-cas/
  - Client应用02，域名地址：http://xxlssoclient2.com:8081/xxl-sso-sample-cas/


CAS登录/注销流程验证：
- **登录流程**：
  - 1、访问 "Client应用01" 域名地址 ，将会自动 redirect 到 "CAS认证中心" 登录界面；
    ![输入图片说明](https://www.xuxueli.com/doc/static/xxl-sso/images/img_04.png "在这里输入图片标题")
  - 2、成功登录后，将会自动 redirect 返回到 "Client应用01"，并切换为已登录状态；
    ![输入图片说明](https://www.xuxueli.com/doc/static/xxl-sso/images/img_06.png "在这里输入图片标题")
  - 3、此时，访问 "Client应用02" 域名地址，不需登陆将会自动切换为已登录状态；
  - 4、另外，登录后直接访问 "CAS认证中心" 可进入CAS首页；可通过 “打开“Client应用01” 快速打开Client接入方应用。
    ![输入图片说明](https://www.xuxueli.com/doc/static/xxl-sso/images/img_05.png "在这里输入图片标题")

- **注销流程**：
  - 1、访问 "Client应用01" 配置的 "注销登陆path"，将会自动 redirect 到 "CAS认证中心" 并自动注销登陆状态；
  - 2、此时，访问 "Client应用02" 域名地址，也将会自动注销登陆状态；



## 四、总体设计

### 4.1、设计思想

XXL-SSO 整体基于 模块化领域抽象设计，各领域模块可灵活扩展：
- **登录态持久化模块**：LoginStore，提供登录态/会话数据持久化能力；官方提供Cache、Redis等组件实现，可选用接入或自定义扩展；
- **登录认证模块**：Auth，提供系统登录/认证集成能力；官方提供 Filter（Servlet）和Interceptor（Spring）等实现，可选用接入或自定义扩展；
- **登录用户模型**：LoginInfo，提供统一登录用户模型，且模型支持扩展存储自定义扩展属性；

### 4.2、架构图

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-sso/images/img_01.png "在这里输入图片标题")

### 4.3、核心概念

| 概念                          | 说明
|-----------------------------| ---
| Cas(SSO) Server             | Cas认证中心，提供单点登录基础能力；仅 “CAS单点登录” 登录类型依赖该服务；
| Cas Client                  | 接入CAS单点登录的Client应用，如 “xxl-sso-sample-cas”；
| Ticket                      | Cas Client 与 Cas Server 信息交换的临时票据信息；
| Token                       | 用户登录凭证信息；
| LoginInfo                   | 登录用户结构化信息，与 登录凭证/Token 相对应；
| LoginStore                  | 登录态信息持久化组件，官方提供Cache、Redis等组件实现，可选用接入或自定义扩展；
| Auth（Filter/Interceptor）    | 登录认证/鉴权组件，官方提供 Filter（Servlet）和Interceptor（Spring）等实现，可选用接入或自定义扩展；如 “XxlSsoCasInterceptor”、“XxlSsoNativeInterceptor”。
| XxlSsoHelper                | 单点登录框架核心辅助工具，提供登录、注销、登录用户获取、登录态验证、权限验证……等丰富能力；

### 4.4、多登录类型

XXL-SSO 支持多种登录类型，支持渐进式集成接入使用，从简单到复杂场景，包括：单体Web系统、复杂企业内多系统、互联网多端高并发系统 等，均可接入使用；
- **Web登录**：适用于常规Web系统，不限制接入系统数量；但是限制相关Web系统部署在相同域名下，登录凭证存储在公共域名下；
- **Native登录**：适用于 移动端、小程序、前后端分离系统 等系统，不限制接入系统数量，且无域名限制，支持多端登录；但是登录凭证需要客户端管理维护；
- **CAS单点登录**：适用于多Web系统部署域名不一致场景，解决了系统 跨域登录认证 问题；但是需要单独部署CAS认证中心，CAS认证中心提供单点登录基础能力；

### 4.5、核心流程剖析
#### 4.5.1、核心流程剖析：“Web登录”

#### 4.5.2、核心流程剖析：“Native登录”

#### 4.5.3、核心流程剖析：“CAS单点登录”

### 4.6、登录认证
XXL-SSO 支持灵活的登录认证能力；系统实现上，当前提供 注解、API 多种方式支持。

### 4.7、权限认证
XXL-SSO 支持 权限、角色 多个维度进行权限认证：
- 权限认证：在用户身份认证通过后，校验用户是否具备访问特定资源的权限，决定“你能做什么”；认证维度是“权限”；
- 角色认证：在用户身份认证通过后，校验用户是否具备访问特定资源的权限，决定“你能做什么”；认证维度是“角色”；

系统实现上，当前提供 注解、API 多种方式支持。

### 4.8、记住密码
支持记住密码功能；记住密码时，支持登录态自动延期；未记住密码时，关闭浏览器则登录态失效；

### 4.9、登录态自动延期
支持自定义登录态有效期窗口，当登录态有效期窗口过半时自动顺延一个周期；


## 五、版本更新日志

### 5.1 版本 v0.1.0，新特性[2018-04-04]
- 1、简洁：API直观简洁，可快速上手
- 2、轻量级：环境依赖小，部署与接入成本较低
- 3、单点登录：只需要登录一次就可以访问所有相互信任的应用系统
- 4、分布式：接入SSO认证中心的应用，支持分布式部署
- 5、HA：Server端与Client端，均支持集群部署，提高系统可用性
- 6、实时性：系统登陆、注销状态，全部Server与Client端实时共享
- 7、CS结构：基于CS结构，包括Server"认证中心"与Client"受保护应用"
- 8、跨域：支持跨域应用接入SSO认证中心

### 5.2 版本 v1.1.0 Release Notes [2018-11-06]
- 1、 Redis配置方式增强，支持自定义DB、密码、IP、PORT等等
- 2、 Token接入方式；除了常规Cookie方式外，新增Token接入方式，并提供Sample项目
- 3、 登录态自动延期：支持自定义登录态有效期窗口，默认24H，当登录态有效期窗口过半时，自动顺延一个周期
- 4、 "记住密码" 功能优化：未记住密码时，关闭浏览器则登录态失效；记住密码时，登录态自动延期，在自定义延期时间的基础上，原则上可以无限延期
- 5、 sessionId数据结构优化，进一步提升暴露破解难度
- 6、 认证数据存储结构调整，避免登陆信息存储冗余
- 7、 认证中心用户登录校验改为Mock数据方式，取消对DB强依赖，降低部署难度
- 8、 Client端依赖Core包，slf4j依赖优化，移除log4j强依赖
- 9、 Ajax请求未登录处理逻辑优化，返回JSON格式提示数据
- 10、项目结构梳理，清理冗余依赖，升级多项依赖版本至较近版本
- 11、路径排除：新增自定义属性 "excludedPaths"，允许设置多个，且支持Ant表达式。用于排除SSO客户端不需要过滤的路径


### 5.3 版本 v1.2.0 Release Notes [迭代中]
- 1、升级jedis、springboot等版本依赖版本；
- 2、Client跳转新增一次性Token验证；
- 3、拼写问题修复；



### TODO LIST
- 1、增强用户增强安全性：登陆用户数据中，新增客户端信息如ip、ua等，防止session被窃取；
- 2、认证中心与接入端交互数据加密，临时AccessToken阅后即焚，增强安全性；
- 3、CAS认证中心，支持维护客户端应用；防止跳转非法第三方导致登陆信息泄露；
- 4、集成网关支持；集成WebFlux, Spring-Cloud-Gateway等；
- 5、支持认证分组，分组内共享登陆状态，分组之间登录态隔离，


## 六、其他

### 6.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-sso/issues/) 讨论新特性或者变更。

### 6.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-sso/issues/1 ) 登记，登记仅仅为了产品推广。

### 6.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )
