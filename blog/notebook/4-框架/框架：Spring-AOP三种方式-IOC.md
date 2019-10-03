### IOC

---

    控制反转（依赖注入）
    组件之间依赖关系由容器在运行期决定

```
// 接口
package com.xxl.spring.ioc.service;
public interface Animal {
	public void say();
}
// 猫
package com.xxl.spring.ioc.service.impl;
import com.xxl.spring.ioc.service.Animal;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class Cat implements Animal {
	@Resource
	private Animal fish;
	
	@Override
    public void say() {
		fish.say();
        System.out.println("I am Cat!");
    }  
}
// 鱼
package com.xxl.spring.ioc.service.impl;
import com.xxl.spring.ioc.service.Animal;
import org.springframework.stereotype.Service;

@Service("fish")
public class Fish implements Animal {

    @Override
    public void say() {
        System.out.println("I am Fish!");
    }

}

// 将Bean纳入Spring扫描范围
<context:component-scan base-package="com.xxl.spring.ioc.service"/>

// 测试（服务，依赖注入测试）
package com.xxl.spring.ioc;

import com.xxl.spring.ioc.service.Animal;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;

public class SpringIocTest {
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		
		@SuppressWarnings("resource")
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationcontext-ioc.xml");
		
		Animal cat = (Animal) context.getBean("cat");
		cat.say();
		
	}
}
```

### AOP
---
    优点：解耦、事务控制；
    原理：AOP 主要是利用代理模式的技术来实现的。

**AOP概念术语：**

    切面(aspect)：用来切插业务方法的类。：用来切插业务方法的类。
    切入点(pointcut)：业务类中指定的方法，作为切面切入的点。其实就是指定某个方法作为切面切的地方。
    连接点(joinpoint)：是切面类和业务类的连接点，其实就是封装了业务方法的一些基本属性，作为通知的参数来解析
    通知(advice)：在切面类中，声明对业务方法做额外处理的方法。、
    目标对象(target object)：被代理对象。
    AOP代理(aop proxy)：代理对象。
    
    通知：
        前置通知(before advice)：在切入点之前执行。
        后置通知(after returning advice)：在切入点执行完成后，执行通知。
        环绕通知(around advice)：包围切入点，调用方法前后完成自定义行为。
        异常通知(after throwing advice)：在切入点抛出异常后，执行通知。


Spring实现AOP的三种方式：
##### 方式一：Spring AOP接口

利用Spring AOP接口实现AOP，主要是为了指定自定义通知来供spring AOP机制识别。主要接口：

    前置通知 MethodBeforeAdvice ，
    后置通知：AfterReturningAdvice，
    环绕通知：MethodInterceptor，
    异常通知：ThrowsAdvice 。
    
（被切入的业务类如果是接口方式实现，spring aop 将默认通过jdk 动态代理来实现代理类；否则spring aop 将通过cglib 来实现代理类；）

**原理**：通过org.springframework.aop.framework.ProxyFactoryBean代理，proxyInterfaces指定接口，target指定bean实例，interceptorNames指定通知，实现；

**缺点**：代码还是非常的厚重的，定义一个切面就要定义一个切面类，然而切面类中，就一个通知方法；所以Spring提供了，依赖aspectj的schema配置和基于aspectj 注解方式。这两种方式非常简介方便使用，也是项目中普遍的使用方式。

源码如下：
- 1、Pointcut类
```
package com.xxl.spring.aop.iface.advice.impl;

import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

/**
 * 切点: 匹配方法 (继承"NameMatchMethodPointcut"，来用方法名匹配)
 */
public class Pointcut extends NameMatchMethodPointcut {

    private static final long serialVersionUID = 3990456017285944475L;

    @SuppressWarnings("rawtypes")
    @Override
    public boolean matches(Method method, Class targetClass) {


        // 设置单个方法匹配
        this.setMappedName("delete");
        //也可以用“ * ” 来做匹配符号
        this.setMappedName("get*");

        // 设置多个方法匹配
        String[] methods = { "delete", "modify" };
        this.setMappedNames(methods);

        return super.matches(method, targetClass);
    }

}
```
- 2、Advice：四种通知的实现类   
前置通知
```
package com.xxl.spring.aop.iface.advice.impl;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * 前置通知
 */
public class BaseBeforeAdvice implements MethodBeforeAdvice {

    /**
     * method   : 切入的方法
     * args     ：切入方法的参数
     * target   ：目标对象
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("before start");

        System.out.println("before 对象: " + target);
        System.out.println("before 方法: " + method);
        System.out.println("before 参数: " + args);

        System.out.println("before end");
    }

}
```

后置通知
```
package com.xxl.spring.aop.iface.advice.impl;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * 后置通知
 */
public class BaseAfterReturnAdvice implements AfterReturningAdvice {

    /**
     * returnValue  ：切入点执行完方法的返回值，但不能修改
     * method       ：切入点方法
     * args         ：切入点方法的参数数组
     * target       ：目标对象
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("afterReturning start");

        System.out.println("afterReturning 对象: " + target);
        System.out.println("afterReturning 方法: " + method);
        System.out.println("afterReturning 参数: " + args);

        System.out.println("afterReturning 返回值: " + returnValue);

        System.out.println("afterReturning end");
    }

}
```

环绕通知
```
package com.xxl.spring.aop.iface.advice.impl;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 环绕通知
 */
public class BaseAroundAdvice implements MethodInterceptor {

    /**
     * invocation ：连接点
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Around start");

        System.out.println("Around 对象: " + invocation.getThis());
        System.out.println("Around 方法: " + invocation.getMethod());
        System.out.println("Around 参数: " + invocation.getArguments());

        Object returnValue = invocation.proceed();

        System.out.println("Around 结果：" + returnValue);

        System.out.println("Around end");
        return returnValue;
    }

}
```

异常通知
```
package com.xxl.spring.aop.iface.advice.impl;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * 异常通知，接口没有包含任何方法。通知方法自定义
 */
public class BaseAfterThrowsAdvice implements ThrowsAdvice {

    /**
     * 通知方法，需要按照这种格式书写
     * 
     * @param method    可选：切入的方法
     * @param args      可选：切入的方法的参数
     * @param target    可选：目标对象
     * @param throwable 必填 : 异常子类，出现这个异常类的子类，则会进入这个通知。
     */
    public void afterThrowing(Method method, Object[] args, Object target, Throwable throwable) {
        System.out.println("删除出错");
    }

}
```

- 3、业务Service
接口
```
package com.xxl.spring.aop.iface.service;
/**
 * 代理类接口，也是业务类接口
 * 
 * 利用接口的方式，spring aop 将默认通过jdk 动态代理来实现代理类<br>
 * 不利用接口，则spring aop 将通过cglib 来实现代理类
 */
public interface IDemoService {

    /**
     * 用作代理的切入点方法
     * 
     * @param obj
     * @return
     */
    public String say(String obj);

}
```
接口实现
```
package com.xxl.spring.aop.iface.service.impl;

import com.xxl.spring.aop.iface.service.IDemoService;

/**
 * 业务Service
 */
public class DemoServiceImpl implements IDemoService {

    /**
     * 切入点
     */
    public String say(String param) {
        String result = "i am Demo Service, " + param;
        System.out.println(result);
        return result;
    }

}
```

- 4、Spring配置（applicationcontext-aop-iface.xml）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

    <!-- 切入点：匹配方法 -->
    <bean id="pointcut" class="com.xxl.spring.aop.iface.advice.impl.Pointcut" />

    <!-- 通知：四种简单通知 -->
    <bean id="baseBefore" class="com.xxl.spring.aop.iface.advice.impl.BaseBeforeAdvice" />
    <bean id="baseAfterReturn" class="com.xxl.spring.aop.iface.advice.impl.BaseAfterReturnAdvice" />
    <bean id="baseAfterThrows" class="com.xxl.spring.aop.iface.advice.impl.BaseAfterThrowsAdvice" />
    <bean id="baseAround" class="com.xxl.spring.aop.iface.advice.impl.BaseAroundAdvice" />

    <!-- "切点+通知"包装 -->
    <bean id="matchBeforeAdvisor" class="org.springframework.aop.support.DefaultPointcutAdvisor">
        <property name="pointcut" ref="pointcut"/>
        <property name="advice" ref="baseBefore"/>
    </bean>

	<!-- 业务Service -->
    <bean id="baseBusiness" class="com.xxl.spring.aop.iface.service.impl.DemoServiceImpl" />
    
    <!-- "ProxyFactoryBean"代理对象,实现AOP -->
    <bean id="businessProxy" class="org.springframework.aop.framework.ProxyFactoryBean">
        <!-- 代理对象接口 (利用接口的方式，spring aop 将默认通过jdk 动态代理来实现代理类; 不利用接口，则spring aop 将通过cglib 来实现代理类) -->
        <property name="proxyInterfaces" value="com.xxl.spring.aop.iface.service.IDemoService" />
        <!-- 目标对象 -->
        <property name="target" ref="baseBusiness" />
        <!-- 代理拦截器 -->
        <property name="interceptorNames">
            <list>
                <value>matchBeforeAdvisor</value>
                <value>baseAfterReturn</value>
                <value>baseAround</value>
            </list>
        </property>
    </bean>

</beans>
```

- 5、测试
```
package com.xxl.spring.aop.iface;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xxl.spring.aop.iface.service.IDemoService;

public class SpringAOPIFaceTest {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationcontext-aop-iface.xml");

		IDemoService business = (IDemoService) context.getBean("businessProxy");
        business.say("猫");
	}
	
}
```

##### 方式二：aspectj（XML Schema）+ Spring
- 1、定义AOP切面"通知"类
```
package com.xxl.spring.aop.aspectj.schema.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 定义AOP切面"通知"类
 */
public class AspectAdvice {

    /**
     * 前置通知
     *
     * @param jp    连接点
     */
    public void doBefore(JoinPoint jp) {
        System.out.println("doBefore start");

        System.out.println("doBefore 类: " + jp.getTarget().getClass());
        System.out.println("doBefore 方法: " + jp.getSignature().getName());
        System.out.println("doBefore 参数: " + jp.getArgs());

        System.out.println("doBefore end");
    }

    /**
     * 后置通知
     * 
     * @param jp        连接点
     * @param result    返回值
     */
    public void doAfter(JoinPoint jp, String result) {
        System.out.println("doAfter start");

        System.out.println("doAfter 类: " + jp.getTarget().getClass());
        System.out.println("doAfter 方法: " + jp.getSignature().getName());
        System.out.println("doAfter 参数: " + jp.getArgs());

        System.out.println("doAfter 结果：" + result);

        System.out.println("doAfter end");
    }

    /**
     * 环绕通知
     * 
     * @param pjp
     *            连接点
     */
    public void doAround(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("doAround start");


        System.out.println("doAround 类: " + pjp.getTarget().getClass());
        System.out.println("doAround 方法: " + pjp.getSignature().getName());
        System.out.println("doAround 参数: " + pjp.getArgs());

        Object result = pjp.proceed();

        System.out.println("doAround 结果：" + result);

        System.out.println("doAround end");
    }

    /**
     * 异常通知
     * 
     * @param jp
     * @param e
     */
    public void doThrow(JoinPoint jp, Throwable e) {
        System.out.println("doThrow,");
    }

}
```
- 2、开发业务Service
```
package com.xxl.spring.aop.aspectj.schema.service.impl;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String say(String param) {
        String result = "i am Demo Service, " + param;
        System.out.println(result);
        return result;
    }

}
```
- 3、Spring配置（applicationcontext-aop-aspectj-schema.xml）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-3.0.xsd
		   http://www.springframework.org/schema/aop 
		   http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">
	
    <!-- 业务Service, 扫描 -->
    <context:component-scan base-package="com.xxl.spring.aop.aspectj.schema.service.impl" />

    <!-- "通知" -->
    <bean id="aspectAdvice" class="com.xxl.spring.aop.aspectj.schema.advice.AspectAdvice" />

    <!-- AOP配置 -->
    <aop:config>
        <!-- "切面" -->
        <aop:aspect id="businessAspect" ref="aspectAdvice" >
            <!-- "切入点" -->
            <aop:pointcut id="point_cut" expression="execution(* com.xxl.spring.aop.aspectj.schema.service.impl.*.*(..))" />
            <!-- 只匹配add方法作为切入点
            <aop:pointcut id="except_add" expression="execution(* aop.schema.*.add(..))" />
             -->

            <!-- 前置通知 -->
            <aop:before pointcut-ref="point_cut" method="doBefore"  />
            <!-- 后置通知, returning指定返回参数 -->
            <aop:after-returning pointcut-ref="point_cut" method="doAfter" returning="result" />
            <!-- 环绕通知 -->
            <aop:around pointcut-ref="point_cut" method="doAround" />
            <!-- 异常通知 -->
            <aop:after-throwing pointcut-ref="point_cut" method="doThrow" throwing="e"/>
        </aop:aspect>
    </aop:config>

</beans>
```
- 3、测试
```
package com.xxl.spring.aop.aspectj.schema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xxl.spring.aop.aspectj.schema.service.impl.DemoService;

public class SpringAOPSchemaTest {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationcontext-aop-aspectj-schema.xml");

		DemoService business = (DemoService) context.getBean(DemoService.class);
        business.say("你好");
	}
	
}
```

##### 方式三：aspectj（Annotation）+ Spring
注解开发便利，可读性好；

- 1、定义AOP切面"通知"类（注解方式）

```
package com.xxl.spring.aop.aspectj.annotation.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 定义切面
 * 
 * @Aspect      : 标记为切面类
 * @Pointcut    : 指定匹配切点
 * @Before      : 指定前置通知，value中指定切入点匹配
 * @AfterReturning ：后置通知，具有可以指定返回值
 * @AfterThrowing ：异常通知
 */
@Component
@Aspect
public class AspectAdvice {

    /**
     * 指定切入点匹配表达式，注意它是以方法的形式进行声明的。
     */
    @Pointcut("execution(* com.xxl.spring.aop.aspectj.annotation.service.impl.*.*(..))")
    public void anyMethod() {
    }

    /**
     * 前置通知
     * 
     * @param jp
     */
    @Before(value = "execution(* com.xxl.spring.aop.aspectj.annotation.service.impl.*.*(..))")
    public void doBefore(JoinPoint jp) {
        System.out.println("doBefore start");

        System.out.println("doBefore 类: " + jp.getTarget().getClass());
        System.out.println("doBefore 方法: " + jp.getSignature().getName());
        System.out.println("doBefore 参数: " + jp.getArgs());

        System.out.println("doBefore end");
    }

    /**
     * 后置通知
     * 
     * @param jp        连接点
     * @param result    返回值
     */
    @AfterReturning(value = "anyMethod()", returning = "result")
    public void doAfter(JoinPoint jp, String result) {
        System.out.println("doAfter start");

        System.out.println("doAfter 类: " + jp.getTarget().getClass());
        System.out.println("doAfter 方法: " + jp.getSignature().getName());
        System.out.println("doAfter 参数: " + jp.getArgs());

        System.out.println("doAfter 结果：" + result);

        System.out.println("doAfter end");
    }

    /**
     * 环绕通知
     * 
     * @param pjp   连接点
     */
    @Around(value = "anyMethod()")
    public void doAround(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("doAround start");


        System.out.println("doAround 类: " + pjp.getTarget().getClass());
        System.out.println("doAround 方法: " + pjp.getSignature().getName());
        System.out.println("doAround 参数: " + pjp.getArgs());

        Object result = pjp.proceed();

        System.out.println("doAround 结果：" + result);

        System.out.println("doAround end");
    }

    /**
     * 异常通知
     * 
     * @param jp
     * @param e
     */
    @AfterThrowing(value = "anyMethod()", throwing = "e")
    public void doThrow(JoinPoint jp, Throwable e) {
        System.out.println("doThrow,");
    }

}
```
- 2、开发业务Service
```
package com.xxl.spring.aop.aspectj.annotation.service.impl;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String say(String param) {
        String result = "i am Demo Service, " + param;
        System.out.println(result);
        return result;
    }

}
```
- 3、Spring配置（applicationcontext-aop-aspectj-annotation.xml）
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:context="http://www.springframework.org/schema/context"
	   xmlns:aop="http://www.springframework.org/schema/aop"
	   xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-3.0.xsd
		   http://www.springframework.org/schema/aop 
		   http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">
           
	<!-- 业务Service,扫描 -->
	<context:component-scan base-package="com.xxl.spring.aop.aspectj.annotation.service.impl" />

	<!-- 切面类(AOP注解的"切面通知类"),扫描 -->
	<context:component-scan base-package="com.xxl.spring.aop.aspectj.annotation.advice" />

    <!-- AOP注解,启用 -->
    <aop:aspectj-autoproxy />

</beans>
```
- 4、测试
```
package com.xxl.spring.aop.aspectj.annotation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.xxl.spring.aop.aspectj.annotation.service.impl.DemoService;

public class SpringAOPAnnotationTest {
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationcontext-aop-aspectj-annotation.xml");

		DemoService business = (DemoService) context.getBean(DemoService.class);
		business.say("猫");
		
	}
	
}
```


