- [马士兵 spring 视频笔记](http://www.cnblogs.com/baolibin528/p/3979975.html)

IOC：
- [Spring IOC基础](http://www.cnblogs.com/linjiqin/p/3407047.html)
- [Spring IOC容器基本原理](http://www.cnblogs.com/linjiqin/p/3407126.html)
- [Spring IOC的配置使用](http://www.cnblogs.com/linjiqin/p/3408306.html)

其他：
- [Spring事务的传播行为](http://www.cnblogs.com/yangy608/archive/2010/12/15/1907065.html)
- [Spring Bean 初始化过程](http://m635674608.iteye.com/blog/2149665)
- [Spring事务配置的五种方式](http://www.blogjava.net/robbie/archive/2009/04/05/264003.html)
---

##### spring常用类
- ApplicationContextAware：实现接口，获取 "ApplicationContext"；
- ApplicationListener：实现接口，监听Sping事件，比如销毁事件 "ContextClosedEvent"；

- Spring 允许 Bean 在初始化完成后以及销毁前执行特定的操作。下面是常用的三种指定特定操作的方法
    - 实现 "InitializingBean.afterPropertiesSet/DisposableBean.destroy" 接口
    - <bean> 元素的 init-method/destroy-method属性指定方法；
    - 指定方法上加上@PostConstruct或@PreDestroy注解；
    - BeanPostProcessor实现 postProcessBeforeInitialization/postProcessAfterInitialization 方法；
    - SmartInitializingSingleton 实现 afterSingletonsInstantiated


        Bean在实例化的过程中，执行顺序：
            Constructor 
            @PostConstruct
            BeanPostProcessor.postProcessBeforeInitialization 
            ApplicationContextAware.setApplicationContext
            InitializingBean 
            init-method
            BeanPostProcessor.postProcessAfterInitialization
            SmartInitializingSingleton
            
        Bean在销毁的过程中，执行顺序：
            @PreDestroy
            DisposableBean
            destroy-method

        所有单例 Bean 实例化完成后：
            SmartInitializingSingleton.afterSingletonsInstantiated 
            
- Bean中初始化方法顺序：
    - 当@Scope为singleton时,bean会在ioc初始化时就被实例化,默认为singleton,可以配合@Lazy实现延时加载
    - 当@Scope为prototype时,bean在ioc初始化时不会被实例化,只有在通过context.getBean()时才会被实例化
    - 执行顺序 Constructor > @PostConstruct > InitializingBean > init-method > SmartInitializingSingleton
    - 实现SmartInitializingSingleton接口的类被Spring实例化为一个单例bean,在所有的Bean加载完成后,才会被调用。如果该类被设置为懒加载,那么SmartInitializingSingleton接口方法永远不会被触发,即使使用时bean被实例化了也不会触发。
    - 其他的初始化方式不管是否懒加载,在对象被创建后都会被调用
    - 如果一个类被设置为懒加载,但是其他类注入该懒加载类,也会立刻实例化为Spring Bean。解决办法:可以在注入的地方也设置成懒加载。

##### @RequestBody 请求体中传递JSON数据
```
// 客户端：发送请求
httpPost.setConfig(requestConfig);

// data
if (requestObj != null) {
    String json = JacksonUtil.writeValueAsString(requestObj);

    StringEntity entity = new StringEntity(json, "utf-8");
    entity.setContentEncoding("UTF-8");
    entity.setContentType("application/json");

    httpPost.setEntity(entity);
}

// 服务端：接收请求
public ReturnT<String> registry(@RequestBody RegistryParam registryParam){ ... }



```

##### Spring事务两种方式
```
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource" />
</bean>

// 方式一：注解方式，在Service上加注解 "@Transactional" 即可；
<tx:annotation-driven transaction-manager="transactionManager" proxy-target-class="true"/>

// 方式二：AOP方式
<tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="detail*" propagation="SUPPORTS" />
        <tx:method name="visit*" propagation="SUPPORTS" />
        <tx:method name="get*" propagation="SUPPORTS" />
        <tx:method name="find*" propagation="SUPPORTS" />
        <tx:method name="check*" propagation="SUPPORTS" />
        <tx:method name="list*" propagation="SUPPORTS" />
        <tx:method name="*" propagation="REQUIRED" rollback-for="exception" />
    </tx:attributes>
</tx:advice>

<aop:config>
    <aop:pointcut id="txoperation" expression="execution(* com.xxl.job.admin.service.impl.*.*(..))" />
    <aop:advisor pointcut-ref="txoperation" advice-ref="txAdvice" />
</aop:config>

```

##### Spring加载Properties方式
- 1、PropertyPlaceholderConfigurer：Spring属性占位符方式
应用场景：Properties配置文件不止一个，需要在系统启动时同时加载多个Properties文件
将多个配置文件读取到容器中，交给Spring管理
    - 获取值方式01（xml中）：${mail.host}
    - 获取值方式02（Bean中）：
        ```
        @Value("${mail.sendFrom}")
        private String sendFrom;
        ```
- 2、PropertiesFactoryBean：声明Properties类型的FactoryBean方式【整个配置当做bean的一个property】
只能当做Properties属性注入，而不能获其中具体的值
```
<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="order" value="1"/>
    <property name="fileEncoding" value="utf-8" />
    <property name="ignoreResourceNotFound" value="true" />	
        <!-- 忽略不存在配置文件 -->
        <property name="ignoreUnresolvablePlaceholders" value="true" />	
        <!-- 是否忽略不可解析的Placeholder，当存在多个config时 -->
        <property name="locations">
            <list>
            <value>classpath*:zkprops.properties</value>
            </list>
    </property>
</bean>
```

##### Spring + junit
```
// maven依赖 ：
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${spring.version}</version>
</dependency>

// <!-- junit -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
</dependency>

// 使用demo
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.xxl.core.model.main.AdminMenu;
import com.xxl.dao.IAdminMenuDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:applicationcontext-*.xml"})
public class AdminMenuDaoTest {
@Autowired
private IAdminMenuDao adminMenuDao;

    @Test
    public void test() {
        List<AdminMenu> list = adminMenuDao.getMyMenus(0);
        System.out.println("-------------------------");
        System.out.println(list!=null?list.size():-1);
    }
}
```

##### Spring常用注解，自动扫描装配Bean
```
// 1 引入context命名空间(在Spring的配置文件中)，配置文件如下：
xmlns:context="http://www.springframework.org/schema/context"
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context-2.5.xsd

// spring 会自动扫描cn.pic包下面有注解的类，完成Bean的装配。
<context:component-scan base-package="包名(扫描本包及子包)"/>


// --定义Bean的注解
@Controller
@Controller("Bean的名称")
定义控制层Bean,如Action

@Service
@Service("Bean的名称")
定义业务层Bean

@Repository
@Repository("Bean的名称")
定义DAO层Bean

@Component
定义Bean, 不好归类时使用.

// --自动装配Bean（选用一种注解就可以）
// default-autowire：默认值为“no”，表示默认不会对Bean属性进行注入；必须通过ref元素指定依赖；
@Autowired (Srping提供的)
默认按类型匹配,自动装配(Srping提供的)，可以写在成员属性上,或写在setter方法上

@Autowired(required=true) 
一定要找到匹配的Bean，否则抛异常。 默认值就是true

@Autowired
@Qualifier("bean的名字")
按名称装配Bean,与@Autowired组合使用，解决按类型匹配找到多个Bean问题。

@Resource  JSR-250提供的
默认按名称装配,当找不到名称匹配的bean再按类型装配.
可以写在成员属性上,或写在setter方法上
可以通过@Resource(name="beanName") 指定被注入的bean的名称, 要是未指定name属性, 默认使用成员属性的变量名,一般不用写name属性.
@Resource(name="beanName")指定了name属性,按名称注入但没找到bean, 就不会再按类型装配了.

@Inject  是JSR-330提供的
按类型装配，功能比@Autowired少，没有使用的必要。

--定义Bean的作用域和生命过程
@Scope("prototype")
值有:singleton,prototype,session,request,session,globalSession

@PostConstruct
相当于init-method,使用在方法上，当Bean初始化时执行。

@PreDestroy
相当于destory-method，使用在方法上，当Bean销毁时执行。

// --声明式事务
@Transactional 
```

### 修改对象Field属性值
```
... java reflect
field.setAccessible(true);
field.set(object, value);

... spring invoke set method
final MutablePropertyValues mpvs = beanFactoryToProcess.getBeanDefinition(beanName).getPropertyValues();
field.setAccessible(true);
MutablePropertyValues.addPropertyValue(field.getName(), value);
```
