官网：http://www.quartz-scheduler.org/downloads 
github：https://github.com/quartz-scheduler/quartz
cron表达式工具：http://cron.qqe2.com/


Quartz 是一个开源的作业调度框架，它完全由 Java 写成，并设计用于 J2SE 和 J2EE 应用中。它提供了巨大的灵活性而不牺牲简单性。你能够用它来为执行一个作业而创建简单的或复杂的调度。本系统结合通过 Spring 来集成 Quartz 。

### 单机部署
第一步：添加maven依赖
```
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.2.1</version>
</dependency>
```

第二步：配置Quartz对应XML文件（整合Spring）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:util="http://www.springframework.org/schema/util"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-3.0.xsd
           http://www.springframework.org/schema/util 
           http://www.springframework.org/schema/util/spring-util.xsd">

	<!-- 全站静态化:Job Detail -->
	<bean id="generateNetHtmlJobDetail" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
		<property name="targetObject" ref="triggerService" />
		<property name="targetMethod" value="generateNetHtml" />
		<property name="concurrent" value="false" />
	</bean>
	
	<!-- 全站静态化:Cron Trigger (quartz-2.x的配置) -->
	<bean id="generateNetHtmlTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
		<property name="jobDetail" ref="generateNetHtmlJobDetail" />
		<property name="cronExpression" value="0 0/5 * * * ? *" />
	</bean>
	
	<!-- 启动触发器的配置开始 -->
	<bean name="startQuertz" lazy-init="false" autowire="no" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
		<property name="triggers">
			<list>
				<ref bean="generateNetHtmlTrigger" />
			</list>
		</property>
	</bean>

</beans>
```

注意，上文中JobDetail中需要指定spring中的某一个Bean的名称和方法，该Bean代码如下：
```
// Service接口
package com.xxl.service;

/**
 * Trigger Service
 * @author xuxueli
 */
public interface ITriggerService {
	
	/**
	 * 全站静态化
	 */
	public void generateNetHtml();
}

// Service实现
package com.xxl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.service.IHtmlGenerateService;
import com.xxl.service.ITriggerService;

/**
 * Trigger Service
 * @author xuxueli
 */
@Service("triggerService")
public class TriggerServiceImpl implements ITriggerService {
	private static transient Logger logger = LoggerFactory.getLogger(TriggerServiceImpl.class);
	
	@Autowired
    private IHtmlGenerateService htmlGenerateService;
	
	/**
	 * 全站静态化
	 */
	public void generateNetHtml() {
		long start = System.currentTimeMillis();
		logger.info("全站静态化... start:{}", start);
		
		htmlGenerateService.generateNetHtml();
		
		long end = System.currentTimeMillis();
		logger.info("全站静态化... end:{}, cost:{}", end, end - start);
	}
	
}
```

### 集群部署（可参考XXL-JOB）
- 第一步：初始化Quartz底层库表

官网下载发布版本，建表脚本见发布源码“docs\dbTables”目录下。

- 第二步：配置本地quartz.properties

从Jar中copy出该配置，新增如下配置参数，并注释掉重复配置项目：
```
// 注释掉默认参数，RAMJobStore单机时使用，存储在内存
#org.quartz.jobStore.class: org.quartz.simpl.RAMJobStore

# for cluster
org.quartz.scheduler.instanceId: AUTO
org.quartz.jobStore.class: org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.isClustered: true
org.quartz.jobStore.clusterCheckinInterval: 1000
```

- 第三步：实例化Quartz调度器

```
<bean id="quartzScheduler" lazy-init="false" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <property name="dataSource" ref="dataSource" />
    <property name="autoStartup" value="true" />	<!--自动启动 -->
    <property name="startupDelay" value="20" />		<!--延时启动 -->
	<property name="applicationContextSchedulerContextKey"  value="applicationContextKey" /> 
    <property name="configLocation" value="classpath:quartz.properties"/>
</bean>
```

- 第四步：实例化Quartz操作的工具类（集群推荐使用API方式操作）

```
<!-- 协同-调度器 -->
<bean id="dynamicSchedulerUtil" class="com.xxl.job.admin.core.util.DynamicSchedulerUtil" init-method="init" destroy-method="destroy" >
	<!-- (轻易不要变更“调度器名称”, 任务创建时会绑定该“调度器名称”) -->
    <property name="scheduler" ref="quartzScheduler"/>
    <property name="callBackPort" value="8888"/>
</bean>

// 启动DynamicSchedulerUtil中封装了对Quartz的API操作，具体可查看XXL-JOB
```






