### dbcp配置

dbcp所需jar：commons-dbcp.jar、级联commons-pool.jar
```
<!-- dbcp (1.x适用jdk6 + ) -->
<dependency>
    <groupId>commons-dbcp</groupId>
    <artifactId>commons-dbcp</artifactId>
    <version>1.4</version>
</dependency>
```
```
<!-- dbcp：Main数据源 -->
<bean id="dataSource0" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName"><value>${jdbc.driverClassName}</value></property>
    <property name="url"><value>${jdbc.url}</value></property>
    <property name="username"><value>${jdbc.username}</value></property>
    <property name="password"><value>${jdbc.password}</value></property>
    <property name="maxActive"><value>10</value></property>
    <property name="maxWait"><value>10000</value></property>
    <property name="maxIdle"><value>1</value></property>
    <property name="initialSize"><value>1</value></property>
    <property name="removeAbandoned"><value>true</value></property>
    <property name="testWhileIdle"><value>true</value></property>
    <property name="testOnBorrow"><value>false</value></property>
    <property name="validationQuery"><value>SELECT 1</value></property>
</bean>


<!-- 配置dbcp数据源 -->
      <bean id="dataSource2" destroy-method="close" class="org.apache.commons.dbcp.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
        <!-- 池启动时创建的连接数量 -->
        <property name="initialSize" value="5"/>
        <!-- 同一时间可以从池分配的最多连接数量。设置为0时表示无限制。 -->
        <property name="maxActive" value="30"/>
        <!-- 池里不会被释放的最多空闲连接数量。设置为0时表示无限制。 -->
        <property name="maxIdle" value="20"/>
        <!-- 在不新建连接的条件下，池中保持空闲的最少连接数。 -->
        <property name="minIdle" value="3"/>
        <!-- 设置自动回收超时连接 -->  
        <property name="removeAbandoned" value="true" />
        <!-- 自动回收超时时间(以秒数为单位) -->  
        <property name="removeAbandonedTimeout" value="200"/>
        <!-- 设置在自动回收超时连接的时候打印连接的超时错误  --> 
        <property name="logAbandoned" value="true"/>
        <!-- 等待超时以毫秒为单位，在抛出异常之前，池等待连接被回收的最长时间（当没有可用连接时）。设置为-1表示无限等待。  -->  
        <property name="maxWait" value="100"/>  
      </bean>

```

DBCP和JDK版本对应关系 ：http://commons.apache.org/proper/commons-dbcp/download_dbcp.cgi

DBCP 2 compiles and runs under Java 7 only (JDBC 4.1)
DBCP 1.4 compiles and runs under Java 6 only (JDBC 4)
Apache Commons Pool 2.2 (Java 6.0+)
Apache Commons Pool 1.6 (Java 5.0+)

### c3p0配置

c3p0所需jar：c3p0.jar，级联mchange-commons-java.jar
```
<!-- c3p0 -->
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.1</version>
</dependency>
```
```
<!-- c3p0：Main数据源 -->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close"> 
    <property name="driverClass" value="${c3p0.driverClass}" /> 
    <property name="jdbcUrl" value="${c3p0.url}" /> 
    <property name="user" value="${c3p0.user}" /> 
    <property name="password" value="${c3p0.password}" /> 
    <property name="maxIdleTime" value="60" />
    <property name="initialPoolSize" value="3" /> 
    <property name="minPoolSize" value="2" /> 
    <property name="maxPoolSize" value="10" /> 
    <property name="acquireRetryDelay" value="1000" /> 
    <property name="acquireRetryAttempts" value="60" /> 
    <property name="preferredTestQuery" value="SELECT 1" />
</bean>

<!-- 配置c3p0数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
        <property name="jdbcUrl" value="${jdbc.url}" />
        <property name="driverClass" value="${jdbc.driverClassName}" />
        <property name="user" value="${jdbc.username}" />
        <property name="password" value="${jdbc.password}" />
        <!--连接池中保留的最大连接数。Default: 15 -->
        <property name="maxPoolSize" value="100" />
        <!--连接池中保留的最小连接数。-->
        <property name="minPoolSize" value="1" />
        <!--初始化时获取的连接数，取值应在minPoolSize与maxPoolSize之间。Default: 3 -->
        <property name="initialPoolSize" value="10" />
        <!--最大空闲时间,60秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0 -->
        <property name="maxIdleTime" value="30" />
        <!--当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3 -->
        <property name="acquireIncrement" value="5" />
        <!--JDBC的标准参数，用以控制数据源内加载的PreparedStatements数量。但由于预缓存的statements
          属于单个connection而不是整个连接池。所以设置这个参数需要考虑到多方面的因素。
          如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0-->
        <property name="maxStatements" value="0" />
         
        <!--每60秒检查所有连接池中的空闲连接。Default: 0 -->
        <property name="idleConnectionTestPeriod" value="60" />
         
        <!--定义在从数据库获取新连接失败后重复尝试的次数。Default: 30 -->
        <property name="acquireRetryAttempts" value="30" />
        
        <!--获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
          保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
          获取连接失败后该数据源将申明已断开并永久关闭。Default: false-->
        <property name="breakAfterAcquireFailure" value="true" />
         
        <!--因性能消耗大请只在需要的时候使用它。如果设为true那么在每个connection提交的
          时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
          等方法来提升连接测试的性能。Default: false -->
        <property name="testConnectionOnCheckout"  value="false" />
        <!--两次连接中间隔时间，单位毫秒。Default:1000-->
        <property name="acquireRetryDelay" value="1000" />
        <!--定义所有连接测试都执行的测试语句。在使用连接测试的情况下这个一显著提高测试速度。注意：
        测试的表必须在初始数据源的时候就存在。Default: null-->
        <property name="preferredTestQuery" value="SELECT 1" />
    </bean>
```

### 对比：（推荐c3p0）
现在常用的开源数据连接池主要有c3p0、dbcp和proxool三种，其中：
hibernate开发组推荐使用c3p0;
spring开发组推荐使用dbcp(dbcp连接池有weblogic连接池同样的问题，就是强行关闭连接或数据库重启后，无法reconnect，告诉连接被重置，这个设置可以解决);
hibernate in action推荐使用c3p0和proxool;
    
      
    



