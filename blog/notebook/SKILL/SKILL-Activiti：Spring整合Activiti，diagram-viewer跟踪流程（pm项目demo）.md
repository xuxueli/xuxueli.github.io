### Activiti
Activiti is a light-weight workflow and Business Process Management (BPM) Platform. Its core is a super-fast and rock-solid BPMN 2 process engine for Java.

- [官网](http://www.activiti.org/)
- [github](https://github.com/Activiti/Activiti)

- [中文用户手册5.16](http://www.mossle.com/docs/activiti/index.html)
- [教程-初识Activiti](http://wenku.baidu.com/view/bb7364ad4693daef5ff73d32.html)
- [教程-BPMN2新规范与Activiti5](http://www.infoq.com/cn/articles/bpmn2-activiti5)

源码解压目录

    /database   （建表SQL）
    /docs   （文档）
    /libs   （依赖Jar包）
    /wars   （Demo项目）
    
### PM项目
见 “xxl-incubator” 项目子项目 “xxl-pm” ；

功能定位：项目管理 [项目任务管理，工作流]

框架选型：spring mvc + mybatis + mysql（bootstrap + activiti）

dev任务功能
-  1、任务列表；
-  2、创建任务；
-  3、删除任务（同时删除流程实例）；
-  4、启动流程；
-  5、查看流程图；

工作流UserTask功能：开发提测--测试--审核
- 1、开发提测/提交审核/审核完成（更改任务状态）：流转到下一个节点“测试”+ 指派测试负责人（不指定，在组中找）；

- 首页
- 任务：
    - 模块
        - 项目
            - 任务【未开始、进行中、开发完成、提交测试、测试完成、上线申请、允许上线、历史】
                - 子任务【未完成、已完成】
                    - 评论
                - 子bug【未完成、已完成】
                - 上传附件
- 管理
    - 模块
        - 项目
    - 用户

### Spring整合Activiti：
- 第一步：maven依赖：
```
<!-- uuid: for activiti jiqun -->
<dependency>
   <groupId>com.fasterxml.uuid</groupId>
   <artifactId>java-uuid-generator</artifactId>
   <version>3.1.3</version>
</dependency>
<!-- activiti -->
<dependency>
   <groupId>org.activiti</groupId>
   <artifactId>activiti-engine</artifactId>
   <version>${activiti.version}</version>
</dependency>
<dependency>
   <groupId>org.activiti</groupId>
   <artifactId>activiti-spring</artifactId>
   <version>${activiti.version}</version>
</dependency>
```

- 第二步：新建spring配置文件：applicationcontext-activiti.xml
```
<!-- uuid:支持activiti集群 -->
<bean id="uuidGenerator" class="org.activiti.engine.impl.persistence.StrongUuidGenerator" />
 
<!-- Job执行器 -->
<!-- <bean id="processEngineJobExecutor" class="org.activiti.engine.impl.jobexecutor.DefaultJobExecutor">
    <property name="maxJobsPerAcquisition" value="10" />
    <property name="waitTimeInMillis" value="60000" />
</bean> -->

<!-- activiti引擎配置 -->
<bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
    <property name="idGenerator" ref="uuidGenerator" />
    <property name="dataSource" ref="dataSource" />
    <property name="transactionManager" ref="transactionManager" />
    <property name="databaseSchemaUpdate" value="true" />	<!-- 缺表时自动创建表 -->
    <property name="jobExecutorActivate" value="true" />	<!-- jobExecutor -->
    <!-- <property name="jobExecutor" ref="processEngineJobExecutor" /> -->
    <property name="history" value="activity" />
    <property name="deploymentResources" value="classpath:/activiti/*.bpmn" />
 </bean>

<!-- activiti引擎 -->
<bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean" destroy-method="destroy">
    <property name="processEngineConfiguration" ref="processEngineConfiguration" />
</bean>

<!-- activiti服务 -->
<bean id="repositoryService" factory-bean="processEngine" factory-method="getRepositoryService" />	<!-- 管理和控制发布包和流程定义(负责静态信息) -->
<bean id="runtimeService" factory-bean="processEngine" factory-method="getRuntimeService" />	<!-- 负责启动一个流程定义的新实例(获取和保存流程变量) -->
<bean id="taskService" factory-bean="processEngine" factory-method="getTaskService" />	<!-- 与任务有关的功能(由系统中真实人员执行) -->
<bean id="historyService" factory-bean="processEngine" factory-method="getHistoryService" />	!-- Activiti引擎的所有历史数据 -->
<bean id="managementService" factory-bean="processEngine" factory-method="getManagementService" />
```

- 第三步：画流程图，service注入activiti服务写逻辑

### activiti整合diagram-viewer（js + css 方式，比较灵活），跟踪流程图
- 第一步：maven依赖
```
<!-- activiti for diagram-viewerstart -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>2.3.3</version>
</dependency>
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-rest</artifactId>
    <version>${activiti.version}</version>
</dependency>
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-diagram-rest</artifactId>
    <version>${activiti.version}</version>
</dependency>
<dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-explorer</artifactId>
    <version>${activiti.version}</version>
    <exclusions>
        <exclusion>
            <artifactId>vaadin</artifactId>
            <groupId>com.vaadin</groupId>
        </exclusion>
        <exclusion>
            <artifactId>dcharts-widget</artifactId>
            <groupId>org.vaadin.addons</groupId>
        </exclusion>
        <exclusion>
            <artifactId>activiti-simple-workflow</artifactId>
            <groupId>org.activiti</groupId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- activiti for diagram-viewer end -->
```

第二部：配置项目web.xml，为diagram-viewer提供服务
```
<!-- servlet for diagram-viewer -->
<servlet>
    <servlet-name>ExplorerRestletServlet</servlet-name>
    <servlet-class>org.restlet.ext.servlet.ServerServlet</servlet-class>
    <init-param>
        <!-- Application class name -->
        <param-name>org.restlet.application</param-name>
        <param-value>org.activiti.rest.diagram.application.DiagramRestApplication</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>ExplorerRestletServlet</servlet-name>
    <url-pattern>/service/*</url-pattern>
</servlet-mapping>
```

- 第三部：配置diagram-viewer样式

    - 1、解压发布包找到activiti-16-exploer.war，copy其中的文件夹diagram-viewer至项目中；
    - 2、文件夹中index.html文件copy一份作为模板，替换文件中两个变量processDefinitionId/processInstanceId；
    - 3、文件夹中css文件中图片路径不对的，修复一下，全部OK；

### activiti5.10解决分布式集群部署的主键问题
- 1、引入java-uuid-generator的maven依赖；
- 2、修改activiti主键生成配置：
```
<property name="idGenerator" ref="uuidGenerator" />
...
<!-- uuid:支持activiti集群 -->
<bean id="uuidGenerator" class="org.activiti.engine.impl.persistence.StrongUuidGenerator" />
```
- 3、重启
    
    可参考博客：http://blog.csdn.net/kongqz/article/details/8027295


