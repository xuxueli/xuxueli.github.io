[官网](http://projects.spring.io/spring-boot/)

[github地址](https://github.com/spring-projects/spring-boot)
（有很多demo，推荐参考下）


[深入学习微框架：Spring Boot](http://www.infoq.com/cn/articles/microframeworks1-spring-boot/)

### 使用步骤：
- 1、maven配置：
```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.xxl</groupId>
	<artifactId>spring-boot</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<!-- stater POMs，提供dependency management依赖管理：继承一些默认的依赖，工程需要依赖的jar包的管理，申明其他dependency的时候就不需要version -->
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.1.0.RELEASE</version>
	</parent>
	
	<properties>
		<java.version>1.6</java.version>
		<!-- <tomcat.version>7.0.55</tomcat.version> -->
	</properties>

	<dependencies>
		<!-- 提供了对web的支持，包含了spring webmvc和tomcat等web开发的特性 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<!-- 提供了直接运行项目的插件：如果是通过parent方式继承spring-boot-starter-parent则不用此插件 -->
			<!-- <plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin> -->
		</plugins>
	</build>

</project>
```

- 2、开发启动类
```
package com.xxl;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.xxl.controller.DemoRestController;

/**
 * @RestController				相当于	@Controller + @ResponseBody
 * @EnableAutoConfiguration		开启自动配置
 * 
 * @Configuration				注解bean
 * @ComponentScan("com.xxl")	扫描注册相应的bean
 *	
 */

@Configuration
@ComponentScan("com.xxl")
public class Application {

	public static void main(String[] args) {

		//《第一种方式》 ：@RestController + @EnableAutoConfiguration注解Controller;
		//然后，SpringApplication.run(DemoRestController.class);运行单个Controller;这种方式只运行一个控制器比较方便;
		SpringApplication.run(DemoRestController.class, args);

		//《第二种方式》：@RestController + @EnableAutoConfiguration注解Controller;
		//然后，@Configuration + @ComponentScan;开启注解扫描, 自动注册相应的注解Bean;
		SpringApplication.run(Application.class, args);
	}
	
}
```

- 3、开发Controller
```
package com.xxl.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class DemoRestController {

	@RequestMapping("/rest/{name}")
	public String hello(@PathVariable("name") String name) {
		return "Hi:" + name;
	}
}
```


##### 配置方式和优先级

- 1.配置方式和优先级

        a. 命令行参数
        b. 来自java:comp/env的JNDI属性
        b. Java系统属性（System.getProperties()）
        d. 操作系统环境变量
        e. RandomValuePropertySource配置的random.*属性值
        f. jar外部的application-{profile}.properties或application.yml(带spring.profile)配置文件
        g. jar内部的application-{profile}.properties或application.yml(带spring.profile)配置文件
        h. jar外部的application.properties或application.yml(不带spring.profile)配置文件
        i. jar内部的application.properties或application.yml(不带spring.profile)配置文件
        j. @Configuration注解类上的@PropertySource
        k. 通过SpringApplication.setDefaultProperties指定的默认属性

- 2.命令行参数

```
java -jar ***.jar --key="value"

// 可通过以下代码禁用
SpringApplication.setAddCommandLineProperties(false);
```
可通过 application.properties 中占位符，@Value 等方式获取，覆盖现有配置；


- 3. application-{profile}.properties参数加载

```
java -jar  ***.jar  --spring.profiles.active=dev
```
application.properties 会首先被加载；然后从 application-{profile}.properties 中获取替换；   
所以，一些基本配置可以写在 前者 中，个性化配置写在 后者 中。

- 4.application.properties 参数加载

默认从 application.properties 文件加载参数，application.properties只会被加载一次。

    a、加载的优先级：

        1、file:./config/ 当前jar目录的config
        2、file:./ 当前jar目录
        3、classpath:/config/ jar包中classpath的 config目录下
        4、classpath:/ jar包中classpath 路径下
        
    b、使用自定义文件
    
        // 方式1：这时就会加载myproject.propertie并且 application.properties不会被加载的.
        java -jar  ***.jar --spring.config.name=myproject
        
        // 方式2：这种情况下 application.properties也是会被加载，使用方式application-{profile}.properties相同.
        java -jar  ***.jar  spring.config.location=classpath:/myproject.properties

