[设计模式-Runoob教程](http://www.runoob.com/design-pattern/proxy-pattern.html) 

### 代理模式
> 在代理模式（Proxy Pattern）中，一个类代表另一个类的功能。这种类型的设计模式属于结构型模式。
在代理模式中，我们创建具有现有对象的对象，以便向外界提供功能接口。

- **意图**：为其他对象提供一种代理以控制对这个对象的访问。
- **主要解决**：在直接访问对象时带来的问题，比如说：要访问的对象在远程的机器上。在面向对象系统中，有些对象由于某些原因（比如对象创建开销很大，或者某些操作需要安全控制，或者需要进程外的访问），直接访问会给使用者或者系统结构带来很多麻烦，我们可以在访问此对象时加上一个对此对象的访问层。
- **何时使用**：想在访问一个类时做一些控制。
- **如何解决**：增加中间层。
- **关键代码**：实现与被代理类组合。
- **应用实例**： 1、Windows 里面的快捷方式。 2、买火车票不一定在火车站买，也可以去代售点。3、spring aop。
- **优点**： 1、职责清晰。 2、高扩展性。 3、智能化。
- **缺点**： 1、由于在客户端和真实主题之间增加了代理对象，因此有些类型的代理模式可能会造成请求的处理速度变慢。 2、实现代理模式需要额外的工作，有些代理模式的实现非常复杂。
- **使用场景**：按职责来划分，通常有以下使用场景： 1、远程代理。 2、虚拟代理。 3、Copy-on-Write 代理。 4、保护（Protect or Access）代理。 5、Cache代理。 6、防火墙（Firewall）代理。 7、同步化（Synchronization）代理。 8、智能引用（Smart Reference）代理。
- **注意事项**： 1、和适配器模式的区别：适配器模式主要改变所考虑对象的接口，而代理模式不能改变所代理类的接口。 2、和装饰器模式的区别：装饰器模式为了增强功能，而代理模式是为了加以控制。


### 静态代理、动态代理
> **代理模式**：代理模式是常用的java设计模式，他的特征是代理类与委托类有同样的接口，代理类主要负责为委托类预处理消息、过滤消息、把消息转发给委托类，以及事后处理消息等。代理类与委托类之间通常会存在关联关系，一个代理类的对象与一个委托类的对象关联，代理类的对象本身并不真正实现服务，而是通过调用委托类的对象的相关方法，来提供特定的服务。

按照代理的创建时期，代理类可以分为两种。 
- **静态代理**：由程序员创建或特定工具自动生成源代码，再对其编译。在程序运行前，代理类的.class文件就已经存在了。 
- **动态代理**：在程序运行时，运用反射机制动态创建而成。 
    - JDK方式，动态代理：创建速度比cglib的动态代理创建速度要快8倍左右，prototype bean 用jdk动态代理会较好；
    - cglib方式，动态代理：动态代理性能要比jdk动态代理快10倍以上，sington bean 用cglib动态代理比较好；
    （spring动态代理基于上述两种方式实现：若实现接口，默认JDK方式，也可手动制定cglib；若未实现接口，必须使用cglib方式）

##### 实例1：jdk方式，实现静态代理

```
package com.xxl.proxy.jdk.service;

/**
 * 定义一个账户接口
 */
public interface ICount {
	// 查看账户方法
	public void queryCount();

	// 修改账户方法
	public void updateCount();
}
```

```
package com.xxl.proxy.jdk.service.impl;

import com.xxl.proxy.jdk.service.ICount;

/**
 * 委托类(包含业务逻辑)
 */
public class CountImpl implements ICount {

	@Override
	public void queryCount() {
		System.out.println("查看账户方法...");

	}

	@Override
	public void updateCount() {
		System.out.println("修改账户方法...");

	}

}

```

```
package com.xxl.proxy.jdk.service.proxy;

import com.xxl.proxy.jdk.service.ICount;
import com.xxl.proxy.jdk.service.impl.CountImpl;

/**
 * 这是一个代理类（增强CountImpl实现类）
 */
public class CountProxy implements ICount {
	private CountImpl countImpl;

	/**
	 * 覆盖默认构造器
	 * @param countImpl
	 */
	public CountProxy(CountImpl countImpl) {
		this.countImpl = countImpl;
	}

	@Override
	public void queryCount() {
		System.out.println("事务处理之前");
		// 调用委托类的方法;
		countImpl.queryCount();
		System.out.println("事务处理之后");
	}

	@Override
	public void updateCount() {
		System.out.println("事务处理之前");
		// 调用委托类的方法;
		countImpl.updateCount();
		System.out.println("事务处理之后");
	}

}
```

```
package com.xxl.proxy.jdk;

import com.xxl.proxy.jdk.service.impl.CountImpl;
import com.xxl.proxy.jdk.service.proxy.CountProxy;

/**
 * 静态代理：由程序员创建或特定工具自动生成源代码，再对其编译。在程序运行前，代理类的.class文件就已经存在了。
 */
public class TestCount {
	
	public static void main(String[] args) {
		CountImpl countImpl = new CountImpl();
		CountProxy countProxy = new CountProxy(countImpl);
		countProxy.updateCount();
		countProxy.queryCount();

	}
}
```

##### 实例2：jdk方式，实现动态代理（代理类需要绑定接口）

```
package com.xxl.proxy.jdk2.service;

public interface IBookFacade {
	public void addBook();
}
```

```
package com.xxl.proxy.jdk2.service.impl;

import com.xxl.proxy.jdk2.service.IBookFacade;

public class BookFacadeImpl implements IBookFacade {

	@Override
	public void addBook() {
		System.out.println("增加图书方法。。。");
	}

}
```

```
package com.xxl.proxy.jdk2.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理代理类
 */
public class BookFacadeProxy implements InvocationHandler {
	private Object target;

	/**
	 * 绑定委托对象并返回一个代理类
	 * @param target
	 * @return
	 */
	public Object bind(Object target) {
		this.target = target;
		// 取得代理对象
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), this); // 要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)
	}

	@Override
	/** 
	 * 调用方法 
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		System.out.println("事物开始");
		// 执行方法
		result = method.invoke(target, args);
		System.out.println("事物结束");
		return result;
	}

}
```

```
package com.xxl.proxy.jdk2;

import com.xxl.proxy.jdk2.proxy.BookFacadeProxy;
import com.xxl.proxy.jdk2.service.IBookFacade;
import com.xxl.proxy.jdk2.service.impl.BookFacadeImpl;

/**
 * 与静态代理类对照的是动态代理类，动态代理类的字节码在程序运行时由Java反射机制动态生成，无需程序员手工编写它的源代码。动态代理类不仅简化了编程工作，
 * 而且提高了软件系统的可扩展性，因为Java 反射机制可以生成任意类型的动态代理类。java.lang.reflect
 * 包中的Proxy类和InvocationHandler 接口提供了生成动态代理类的能力。
 */
public class TestProxy {

	public static void main(String[] args) {
		BookFacadeProxy proxy = new BookFacadeProxy();
		IBookFacade bookProxy = (IBookFacade) proxy.bind(new BookFacadeImpl());
		bookProxy.addBook();
	}

}
```

##### 示例3：cglib方式，实现动态代理

```
package com.xxl.proxy.cglib.service.impl;

/**
 * 这个是没有实现接口的实现类
 * 
 * Cglib动态代理 JDK的动态代理机制只能代理实现了接口的类，而不能实现接口的类就不能实现JDK的动态代理，cglib是针对类来实现代理的，
 * 他的原理是对指定的目标类生成一个子类，并覆盖其中方法实现增强，但因为采用的是继承，所以不能对final修饰的类进行代理。
 */
public class BookFacadeImpl {
	public void addBook() {
		System.out.println("增加图书的普通方法...");
	}
}
```

```
package com.xxl.proxy.cglib.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 使用cglib动态代理
 */
public class BookFacadeCglib implements MethodInterceptor {
	private Object target;

	/**
	 * 创建代理对象
	 * 
	 * @param target
	 * @return
	 */
	public Object getInstance(Object target) {
		this.target = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(this.target.getClass());
		// 回调方法
		enhancer.setCallback(this);
		// 创建代理对象
		return enhancer.create();
	}

	@Override
	// 回调方法
	public Object intercept(Object obj, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		System.out.println("事物开始");
		proxy.invokeSuper(obj, args);
		System.out.println("事物结束");
		return null;

	}

}
```

```
package com.xxl.proxy.cglib;

import com.xxl.proxy.cglib.proxy.BookFacadeCglib;
import com.xxl.proxy.cglib.service.impl.BookFacadeImpl;

public class TestCglib {

	public static void main(String[] args) {
		BookFacadeCglib cglib = new BookFacadeCglib();
		BookFacadeImpl bookCglib = (BookFacadeImpl) cglib.getInstance(new BookFacadeImpl());
		bookCglib.addBook();
	}
}
```




