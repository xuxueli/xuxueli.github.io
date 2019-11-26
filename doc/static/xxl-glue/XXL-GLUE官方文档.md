## 《分布式逻辑管理平台XXL-GLUE》

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-glue/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-glue/)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-glue.svg)](https://github.com/xuxueli/xxl-glue/releases)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-GLUE 是一个分布式环境下的 "可执行逻辑单元" 管理平台, 学习简单，扩展JVM的动态语言支持。现已开放源代码并接入多家公司线上产品线，开箱即用。

>GLUE：即"可执行逻辑"，本质上是一段可执行的代码。GLUE可以方便的嵌入业务代码中, GLUE中逻辑代码支持在线开发、动态推送更新、实时编译生效。
可以节省部分因为项目编译、打包、部署和重启线上机器所带来的时间和人工消耗, 提高开发效率。

>可以参考 “配置管理系统，如disconf xxl-conf等” 概念来帮助我们来理解XXL-GLUE。
前者维护 "配置信息"，而且支持数据类型有限。XXL-GLUE功能更强大, 支持维护"可执行逻辑代码"。
XXL-GLUE在功能上完全可以替代前者，只需要在可执行代码块中返回配置即可，支持返回任意类型配置数据。XXL-GLUE主要作用是托管"可执行逻辑代码"，将会为开发者代码不一样的开发体验。

### 1.2 特性
- 1、动态："可执行逻辑单元（GLUE）"支持在线开发、动态推送更新、实时编译生效, 扩展JVM的动态语言支持;
- 2、Wed IDE：提供WedIDE，支持在线开发GLUE代码；
- 3、推送更新：GLUE代码修改后，开发人员可手动触发GLUE更新广播，广播组件将会实时推送GLUE到接入方项目，从而实时更新GLUE；
- 4、兼容Spring：GLUE代码中支持@Resource和@Autowired两种方式注入Spring容器中服务;
- 5、版本：支持30个历史版本的版本回溯；
- 6、调试: 在开发阶段可开启本地模式, 该模式下将会加载本地GlueHandler文件, 支持Debug, 可以方便的进行本地调试;
- 7、项目分组：支持设置项目分组，以项目为维度进行GLUE分组管理；

### 1.3 下载

#### 文档地址
- [中文文档](https://www.xuxueli.com/xxl-glue/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-glue](https://github.com/xuxueli/xxl-glue) | [Download](https://github.com/xuxueli/xxl-glue/releases)
[https://gitee.com/xuxueli0323/xxl-glue](https://gitee.com/xuxueli0323/xxl-glue) | [Download](https://gitee.com/xuxueli0323/xxl-glue/releases)  


##### 技术交流群 (仅作技术交流)

- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.4 环境
- JDK：1.7+
- Servlet/JSP Spec：3.1/2.3
- Tomcat：8.5.x
- Mysql：5.6+
- Maven：3+


## 二、快速入门
### 源码目录介绍

    /db                : 数据库交表脚本位置
    /xxl-glue-admin    : GLUE管理中心
    /xxl-glue-core     : 公共依赖
    /xxl-glue-core-example : GLUE接入Example项目, 可以参考它来学习如何在项目中接入并使用GLUE

### 1、初始化数据库

执行数据库建表脚本: /xxl-glue/doc/db/mysql_xxl_glue.sql

### 2、部署部署"GLUE管理中心"(xxl-glue-admin)
配置文件位置：xxl-glue-admin/resources/xxl-glue-admin.properties

```
### JDBC 配置
xxl.glue.db.driverClass=com.mysql.jdbc.Driver
xxl.glue.db.url=jdbc:mysql://localhost:3306/xxl-glue?useUnicode=true&characterEncoding=UTF-8
xxl.glue.db.user=root
xxl.glue.db.password=root_pwd

### zookeeper 地址配置：例如 "127.0.0.1:2181" 或 "127.0.0.1:2181,127.0.0.1:2182"
xxl.glue.zkserver=127.0.0.1:2181

### 登录账号密码
xxl.glue.login.username=admin
xxl.glue.login.password=123456
```

编译War包部署即可。

### 3、部署部署 "GLUE接入Example项目"(xxl-glue-core-example)

#### 客户端maven依赖
```
<!-- https://mvnrepository.com/artifact/com.xuxueli/xxl-glue-core -->
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-glue-core</artifactId>
    <version>1.3.1</version>
</dependency>
```

#### 客户端配置文件
配置文件位置：xxl-glue-core-example/resources/xxl-glue.properties

```
### JDBC 配置
xxl.glue.db.driverClass=com.mysql.jdbc.Driver
xxl.glue.db.url=jdbc:mysql://localhost:3306/xxl-glue?useUnicode=true&characterEncoding=UTF-8
xxl.glue.db.user=root
xxl.glue.db.password=root_pwd

### zookeeper 地址配置：例如 "127.0.0.1:2181" 或 "127.0.0.1:2181,127.0.0.1:2182"
xxl.glue.zkserver=127.0.0.1:2181
```

编译War包部署即可。

### 4、开发第一个Glue (Hello World) 
登陆 "GLUE管理中心" 并点击右上角 "新增GLUE" 按钮，填写 “GLUE名称”（该名称是该GLUE项的唯一标示）和简介，确定后即新增一条GLUE。

点击GLUE右侧 “Web IDE”按钮，即可进入GLUE的代码开发界面，可在该界面开发GLUE代码，也可以在IDE中开发完成后粘贴进来，默认已经初始化Demo代码。
（每个Glue必须是实现统一父接口GlueHandler的子类；详情可参考章节 "5.1" ）

初始化数据局之后，系统默认生成了三个典型场景的GLUE示例，可以参考GLUE示例开发第一个GLUE（GLUE的三种经典使用场景，可参考 "章节四"）。
	
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_bzf0.png "在这里输入图片标题")
    
    
### 5、调用Glue
业务中调用Glue只需要执行以下一行代码即可：    
```
	Object result = GlueFactory.glue("glue名称", "glue入参，Map类型");
```


"GLUE接入Example项目(xxl-glue-core-example)" 中，针对 "初始化数据局之后，系统默认生成了三个典型场景的GLUE示例" 提供了相应的示例调用代码，代码位置是：

    xxl-glue-core-example/com.xxl.glue.example.controller.IndexController.index

部署启动 "GLUE接入Example项目(xxl-glue-core-example)"，假设项目部署在 "/xxl-glue-core-example" 路径，访问以下链接可执行测试逻辑。

    http://localhost:8080/xxl-glue-core-example/

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_YHDB.png "在这里输入图片标题")


## 三、操作详解

### 1、登陆GLUE
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_IXh1.png "在这里输入图片标题")


### 2、项目列表
系统以项目为维度进行GLUE分组管理；可以在 "项目管理" 模块查看系统中的项目列表，默认已经提供了一个 "示例项目"；

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_CSqk.png "在这里输入图片标题")

### 3、新增项目
在 "项目管理" 界面，点击右上角 "新增项目" 可以新增项目，项目属性说明如下：

    项目AppName：项目AppName为项目分组标识，在广播刷新GLUE时可指定AppName实现灰度刷新指定AppNamd项目中的GLUE示例。正确格式为：长度4-20位的小写字母、数字和下划线
    项目名称：项目中文名称

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_pcdP.png "在这里输入图片标题")

### 4、：GLUE列表
![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_bzf0.png "在这里输入图片标题")

### 5、新建GLUE
点击右上角 "新建GLUE" 按钮，弹框填写GLUE信息即可新建GLUE，属性介绍如下：

    项目：GLUE所属的项目，
    GLUE：Glue名称，每个GLUE的唯一标示，新建GLUE时将会自动将所项目的AppName作为名称前缀。正确格式为：长度4-20位的大小写字母、数字和下划线
    描述：GLUE的描述介绍信息

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_Vz22.png "在这里输入图片标题")


### 6、开发GLUE代码
找到新建的GLUE，点击右侧的 “Web IDE” 按钮进入GLUE开发的Wed IDE界面。默认已经初始化示例代码，如需开发业务代码，只需要在handle方法中开发即可。

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_i2HN.png "在这里输入图片标题")

### 7、一句话执行GlueHandler
首先确定项目中已经接入GLUE（参考上文 “GLUE接入Example项目(xxl-glue-core-example)”，接入非常方便）；

业务中调用Glue只需要执行以下一行代码即可：    
```
	Object result = GlueFactory.glue("glue名称", "glue入参，Map类型");
```

### 8、推送更新
Glue在第一次加载之后将会缓存在内存中，点击右侧 “清除缓存” 按钮可以推送刷新GLUE缓存。
清除缓存弹框中有一个输入框 "Witch APP", 输入接入方项目的AppName, 即可精确的灰度刷新该项目中的相应Glue。如果不输入, 则广播刷新所有项目中响应的Glue。


![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_4t4f.png "在这里输入图片标题")

### 9、如何Debug测试GlueHandler

首先，需要了解源码加载器配置,见项目"xxl-glue-core-example"的"applicationcontext-glue.xml"配置中"GlueFactory"实例的"glueLoader"属性;

- dbGlueLoader(数据库加载器,不支持Debug,源码线上维护): 默认XXL-GLUE在接入方通过加载数据库中GLUE源码进而实例化并执行, 需要配置一个"dbGlueLoader"。
- FileGlueLoader(本地加载器,支持Debug): XXL-GLUE支持配置本地加载器, 只是掉上述配置中的"glueLoader"属性即可,则使用默认的文件加载器;拥有以下特点:
    - 默认加载项目"resources/config/glue"目录下的groovy文件,文件名称必须和配置的GlueHandler名称一致;
    - 会循环遍历该目录下子目录文件进行匹配,因此文件可以在该目录下自由存放;
    - FileGlueLoader方式使用XXL-GLUE,支持GlueHandler的debug断点;
    
当源码加载器选择 "FileGlueLoader" 时，将会加载本地GLUE脚本文件，此时可以进行代码Debug操作。

## 四、GlueHandler的三种经典使用场景, 
### 示例场景01：托管 “配置信息”
尤其适用于数据结构比较复杂的配置项

```
package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.HashSet;
import java.util.Map;

/**
 * 示例场景01：托管 “配置信息”
 *
 * 优点：
 * 		1、在线编辑；推送更新；
 * 		2、该场景下，相较于同类型配置管理系统，支持数据类型更加丰富，不仅支持基础类型，甚至支持复杂对象；
 * 		3、该场景下，配置信息的操作和展示，更加直观；
 *
 * @author xuxueli 2016-4-14 15:36:37
 */
public class DemoGlueHandler01 implements GlueHandler {

	@Override
	public Object handle(Map<String, Object> params) {
		
		/*
		// 【基础类型配置】，例如：活动开关、短信发送次数阀值、redis地址等；
		boolean activitySwitch = true;													// 活动开关：true=开、false=关
		int smsLimitCount = 3;															// 短信发送次数阀值
		String brokerURL = "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.2:61616)";	// redis地址等

		// 【对象类型配置……】
		*/

		// 【列表配置】
		HashSet<String> blackTelephones = new HashSet<String>();						// 手机号码黑名单列表
		blackTelephones.add("15000000000");
		blackTelephones.add("15000000001");
		blackTelephones.add("15000000002");

		return blackTelephones;
	}
	
}

```

### 场景02：托管 “静态方法”
可以将配置解析逻辑一并托管，只关注返回结果即可
```
package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.HashSet;
import java.util.Map;

/**
 * 示例场景02：托管 “静态方法”
 *
 * 优点：
 * 		1、在线编辑；推送更新；
 * 		2、该场景下，托管公共组件，方便组件统一维护和升级；
 *
 * @author xuxueli 2016-4-14 16:07:03
 */
public class DemoGlueHandler02 implements GlueHandler {

	// 手机号码黑名单列表
	private static HashSet<String> blackTelephones = new HashSet<String>();
	static {
		blackTelephones.add("15000000000");
		blackTelephones.add("15000000001");
		blackTelephones.add("15000000002");
	}

	/**
	 * 手机号码黑名单校验Util
	 *
	 * @param telephone
	 * @return
	 */
	private boolean isBlackTelephone(String telephone) {
		if (telephone!=null && blackTelephones.contains(telephone)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Object handle(Map<String, Object> params) {
		String telephone = (params!=null)? (String) params.get("telephone") :null;
		return isBlackTelephone(telephone);
	}
	
}

```

### 场景C：示例场景03：托管 “动态服务”
可以灵活组装接口和服务,扩展服务的动态特性，作为公共服务。
```
package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.Map;

/**
 * 示例场景03：托管 “动态服务”
 *
 * 优点：
 * 		1、在线编辑；推送更新；
 * 		2、该场景下，服务内部可以灵活组装和调用其他Service服务, 扩展服务的动态特性，作为公共服务。
 *
 * @author xuxueli 2016-4-14 16:07:03
 */
public class DemoGlueHandler03 implements GlueHandler {
	private static final String SHOPID = "shopid";
	
	/*
	@Resource
	private UserPhoneService userPhoneService;	// 手机号码黑名单Service，此处仅作为示例
	*/
	
	/**
	 * 商户黑名单判断
	 */
	@Override
	public Object handle(Map<String, Object> params) {
		
		/*
		String telephone = (params!=null)? (String) params.get("telephone") :null;

		boolean isBlackTelephone = userPhoneService.isBlackTelephone(telephone);
		return isBlackTelephone;
		*/
		
		return true;
	}
	
}

```


## 五、系统设计

### 架构图

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_AEeQ.png "在这里输入图片标题")

##### 角色组成
- GlueHandler: "可执行逻辑"GLUE的代码实现, 本质上是实现统一父接口的子类, 约定了公共方法以及公共的输入输出以便于与业务代码交互。
- Broadcase: 广播组件, 当GlueHandler更新时, 将会触发广播消息;
- GLUE管理中心: 管理中心, 提供对GlueHandler的管理功能, 同时提供 WebIDE 支持在线开发GlueHandler, 并借助 "Broadcase" 组件提供逻辑单元"GlueHandler"实时推送功能;
- GLUE服务: Glue源码加载的RPC服务, 提供GlueHandler对应源码的加载服务;
- Client: GLUE的接入方, 可以动态的使用托管的GlueHandler, 并享受实时推送功能。避免了逻辑变更带来的一系列编译、打包、部署和重启线上机器等流程;

### 5.1、GlueHandler 剖析

GlueHandler是 "可执行逻辑"GLUE 的代码实现，本质上是实现统一父接口的子类, 约定了公共方法以及公共的输入输出以便于与业务代码交互。

其源码维护在数据库表中, 接入方通过GroovyClassLoader加载相应源码并实例化为 "GlueHandler对象", 调用时将会执行父类公共方法。

统一父接口:
```
package com.xxl.glue.core.handler;

import java.util.Map;

/**
 * default glue iface, it could be use in your biz service
 * @author xuxueli 2016-1-2 21:31:56
 */
public interface GlueHandler {
	
	/**
	 * defaule method
	 * @param params
	 * @return
	 */
	public Object handle(Map<String, Object> params);
	
}

```
 
Groovy简介 : 用于 Java 虚拟机的一种敏捷的动态语言;
- 1、以强大的Java为基础；
- 2、包含Python、Ruby、Smalltalk等语言强带附加功能，例如动态类型转换、闭包和元编程支持；
- 3、一种成熟的面向对象语言，同时可用作纯粹的校验语言；
- 4、适合与Spring动态语言支持一起使用，因为它专门为JVM设计，充分考虑Java继承；
- 5、与Java代码互操作很容易；

### 5.2、GlueHandler 执行步骤

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_xiE7.png "在这里输入图片标题")

接入方,执行托管在GLUE平台上的一个GlueHandler中的代码逻辑时, 执行步骤如下:

- 1、缓存命中: 首先, 在本地缓存中匹配 "GlueHandler实例" , 匹配成功则调用统一公共方法即可; 否则执行步骤2;
- 2、实例化: RPC方式加载相应GlueHandler对应源码, 并通过 "GroovyClassLoader" 最终实例化为 "GlueHandler实例" ;
- 3、依赖注入: 执行 "injectService", 反射方式为 "GlueHandler实例" 注入依赖的Spring服务;
- 4、加入缓存: 将 "GlueHandler实例" 加入本地缓存;
- 5、注册监听: 注册对该 "GlueHandler" 的广播消息监听;
- 6、invoke: 执行 "GlueHandler实例" 的公共方法, 返回执行结果;
- 7、Finish;

### 5.3、Spring服务注入
支持 “Resource.class” 和 “Autowired.class” 两种方式为GlueHandler输入Spring服务，实现逻辑如下：
- 1、 反射获取GlueHandler的Field数组；
- 2、 遍历Field数组，根据其注解 “@Resource” 和 “@Autowired” 在Spring容器匹配服务（注入规则同Spring默认规则：@Autowired按类型注入；@Resource按照首先名称注入，失败则按照类型注入；）；
- 3、将匹配到的Spring服务注入到该Field中。

### 5.4、广播组件

Glue中通过ZK实现了一套广播机制, 采用广播的方式进行触发主动更新。
系统在ZK中持久化一个node节点, 当GLUE需要广播更新时将会将广播消息(包含：GLUE名称、灰度项目、版本号等)序列化后赋值给该节点。该GLUE的接入方项目将会监听到事件通知并及时刷新缓存;


### 5.5、缓存更新策略（异步 + 覆盖）

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_vF6z.png "在这里输入图片标题")

##### 缓存对象
Glue中缓存的对象是“groovyClassLoader”解析生成的GlueHandler实例。

##### Timeout
GlueHandler缓存支持设置Timeout时间，单位毫秒，缓存失效时将会实例化加载新的GLueHander实例，Timeout设置为-1时将永不失效。

##### 避免缓存雪崩
常规缓存更新，通常是通过remove(key)的方式进行缓存清理，然后在下次请求时将会以懒加载的方式进行缓存初始化，但是这样在并发环境中有雪崩的隐患。
为避免缓存雪崩情况，GlueHandler采用 “异步（queue + thread）”+“覆盖”的方式进行GlueHandler更新，步骤如下：

    1、在接收到缓存更新的广播消息时，首先会将待更新的GlueHandler的名称push到待更新队列中；
    2、异步线程监控待更新队列，获取待更新GlueHandler名称，加载并实例化新GlueHandler实例；
    3、将新的GlueHandler实例，覆盖缓存中旧的GlueHandler实例，后续调用将会执行新的业务逻辑。

##### 避免冗余的缓存刷新
常规缓存刷新，通常流程是：每点击一次刷新，生成一个新value值，覆盖旧的value值。但是，当新value值和旧value值相等时，这种逻辑是冗余甚至会降低性能的，特别是生成新value比较复杂时。
为避免冗余缓存刷新情况，底层对每个GLUE记录一个version，当监听到GLUE广播刷新消息时会对比version是否一致，相同版本的GLUE不会触发GlueLoader的刷新流程。

### 5.6、灰度更新
GlueHandler通过广播的方式进行推送更新，在推送广播消息时支持输入待刷新该GlueHandler的项目AppName列表，只有匹配到的项目才会对本项目中GlueHandler进行覆盖更新，否则忽视该条广播消息。为空则全站广播。
因此，可通过上述机制只刷新集群中一台机器上的某个GlueHandler，从而实现灰度功能。

### 5.7、版本回溯
GlueHandler的每次更新都会进行历史版本源码备份，默认支持记录最近的30个版本。
同时，在Web IDE界面上，可以查看到所有的备份记录，并且可以方便的进行版本回退。

### 5.8、预热
GlueHandler创建时和项目关联起来，这样在项目启动时会主动加载关联到的GlueHandler，避免懒加载引起的并发问题。

### 5.9、避免因Permanet Generation空间被占满引起的Full GC
PermanetGeneration中存放的为一些class的信息等，当系统中要加载的类、反射的类和调用的方法较多时，Permanet Generation可能会被占满。

GLUE底层基于Groovy实现，Groovy之前使用时曾经出现过频繁Full GC的问题，原因如下：

系统在执行 “groovy.lang.GroovyClassLoader.parseClass(groovyScript)” 进行groovy代码解析时，Groovy为了保证解析后执行的都是最新的脚本内容，每进行一次解析都会生成一次新命名的Class文件，如下图：

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-glue/images/img_QHdj.jpg "在这里输入图片标题")

因此，如果Groovy类加载器设置为静态，当对同一段脚本均多次执行该方法时，会导致 “GroovyClassLoader” 装载的Class越来越多，从而导致PermGen被用满。

为了避免出现类似问题，GLUE做了以下几点优化。
- 1、针对每个GlueHandler解析后生成的Java对象实例做缓存，而不是代码本身做缓存；
- 2、仅仅在接收到清除缓存的广播时解析生成新的GlueHandler实例对象，避免groovy的频繁解析，减少Class装载频率；
- 3、周期性的异步刷新类加载器，避免因全局类加载器装载Class过多且不及时卸载导致的PermGen被用满。

### 5.10、调试      
由于GlueHandler源码存储在云端, 调试不便。因此系统提供了 "com.xxl.glue.core.loader.impl.FileGlueLoader" ,用于开启 "本地模式", 该模式系统将会从项目资源目录的子目录 "config/glue" 中加载源码, 同时支持debug, 可以方便的进行业务代码调试。


## 六、历史版本
### 版本1.0.0
- 1、动态(groovy)：托管在平台中的GlueHandler以 "groovy" 的方式进行加载实例化, 扩展JVM的动态语言支持;
- 2、在线(Wed IDE)：提供WedIDE，支持在线管理和开发GlueHandler；
- 3、推送更新：当GlueHandler变动时, 将会通过广播组件, 实时推送接入方对应的GlueHandler进行reload更新, 保证GlueHandler中业务逻辑的实时性;
- 4、兼容Spring：无缝兼容Spring, 支持@Resource和@Autowired两种方式注入Spring容器中服务;
- 5、版本：支持50个历史版本的版本回溯；

### 版本1.1.0
- 1、系统重构;
- 2、调试: 在开发阶段可开启本地模式, 该模式下将会加载本地GlueHandler文件, 支持Debug, 可以方便的进行本地调试;

### 版本1.2.0
- 1、广播组件由Activemq改为自主实现的基于ZK的广播组件, 减少系统第三方依赖;
- 2、新增Local模式，提供GLUE本地加载器, 支持加载本地GlueHandler, 方便进行Debug调试;
- 3、异步刷新缓存逻辑更新,新实例正常则覆盖,否则remove掉旧实例;
- 4、修复一处因ReentrantLock导致可能死锁的问题;
- 5、导航菜单更新;
- 6、底层代码重构, 结构优化;

### 版本1.3.0
- 1、项目分组：支持设置项目分组，以项目为维度进行GLUE分组管理；
- 2、UI和交互升级；
- 3、GLUE依赖注入逻辑优化，支持别名注入；
- 4、广播机制优化：接收到GLUE刷新广播时校验GLUE源码version，避免冗余的刷新逻辑； 

### 版本1.3.1
- 1、项目推送maven中央仓库，groupId迁移至 "com.xuxueli"；
- 2、规范maven依赖版本；

### 版本 v1.3.2 Release Notes[迭代中]
- [迭代中]项目迁移至SpringBoot格式；
- [迭代中]取消spring强依赖；


### TODO LIST
- 4、用户管理；
- 5、权限管理；


## 七、其他

### 7.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-glue/issues/) 讨论新特性或者变更。

### 7.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-glue/issues/1 ) 登记，登记仅仅为了产品推广。

### 7.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )
