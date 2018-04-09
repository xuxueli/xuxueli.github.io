> 有三种运行方式，jar，eclipse插件，maven，我比较喜欢第三种，方便，侵入性少；

- 1、配置maven依赖
```
<build>
	<plugins>
		<!-- mybatis-generator -->
		<plugin>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.2</version>
			<configuration>
				<!-- 项目的/src/main/resources(默认目录)的文件目录下加入generateConfig.xml -->
				<configurationFile>src/main/resources/mybatis-generator/generatorConfig.xml</configurationFile>  
				<!--允许移动生成的文件-->
				<verbose>true</verbose>
				<!--允许覆盖生成的文件-->
				<overwrite>true</overwrite>
			</configuration>
		</plugin>
	</plugins>
</build>
```

- 2、配置generatorConfig.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
	<!-- 驱动包地址 -->
	<classPathEntry	location="F:/WorkspaceFiles/mavenWorkspace/maven_lib/mysql/mysql-connector-java/5.1.34/mysql-connector-java-5.1.34.jar" />
	<context id="my" targetRuntime="MyBatis3">
		<commentGenerator>
			<property name="suppressDate" value="false" />
			<property name="suppressAllComments" value="true" />
		</commentGenerator>

		<!-- DB链接配置 -->
		<jdbcConnection driverClass="com.mysql.jdbc.Driver"
			connectionURL="jdbc:mysql://localhost:3306/db_xxl_5i" userId="root"
			password="root_pwd" />

		<!-- 实体定义地址 -->
		<javaModelGenerator targetPackage="com.xxl.core.model"
			targetProject="D:/temp">
			<property name="enableSubPackages" value="true" />
			<property name="trimStrings" value="true" />
		</javaModelGenerator>

		<!-- sql的配置文件定义 -->
		<sqlMapGenerator targetPackage="com.xxl.core.model.mapper"
			targetProject="D:/temp">
			<property name="enableSubPackages" value="true" />
		</sqlMapGenerator>

		<!-- DAO定义 XMLMAPPER是xml的配置方式，还有ANNOTabelMapper的是注解方式 -->
		<javaClientGenerator targetPackage="com.xxl.service.mapper"
			targetProject="D:/temp" type="XMLMAPPER">
			<property name="enableSubPackages" value="true" />
		</javaClientGenerator>

		<!-- 需要处理的表 -->
		<table tableName="wall_info" domainObjectName="WallInfo"
			enableCountByExample="false" enableUpdateByExample="false"
			enableDeleteByExample="false" enableSelectByExample="false"
			selectByExampleQueryId="false">
			<!--<columnRenamingRule searchString="^D_" replaceString=""/> -->
		</table>

	</context>
</generatorConfiguration>
```

- 3、运行：项目 右键--》run as --》 maven bulid --》弹出对话框 --》在goals中输入：mybatis-generator:generate
