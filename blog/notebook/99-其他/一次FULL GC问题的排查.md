<h2 style="color:#4db6ac !important" >一次FULL GC问题的排查</h2>
> 发表于：2016/06/15

[TOCM]

[TOC]

---

#### 一、背景
线上一个项目，每次机器重启时项目都会报出大量的Timeout，同时每个集群节点都被监控到较为频繁的Full GC。之后同事虽然尝试过JVM调优并适当调大了老年代空间，但依然不能根本上解决问题。当时该问题被初步归咎于系统中整合的Groovy，但并未证实。问题汇总如下：

- 问题一：项目启动时报出大量Timeout；
- 问题二：项目运行时，频繁Full GC；

随后，我着手做另外一个项目GLUE，该项目同样需要整合Groovy，在做并发测试时，我发现了同样的问题。

经过排查并做出优化，新项目GLUE在并发测试下基本不存在Full GC的问题，在此将问题处理过程记录如下，希望可以给大家一点参考。

#### 二、分析
新系统GLUE底层基于Groovy实现，系统通过执行 “groovy.lang.GroovyClassLoader.parseClass(groovyScript)” 进行Groovy代码解析，Groovy为了保证解析后执行的都是最新的脚本内容，每进行一次解析都会生成一次新命名的Class文件，底层代码如下图：

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_c8Hk.jpg "在这里输入图片标题")

因此，如果Groovy类加载器设置为单例，当对脚本（即使同一段脚本）多次执行该方法时，会导致 “GroovyClassLoader” 装载的Class越来越多。如果此处临时加载的类不能够被及时释放，最终将会导致PermGen OutOfMemoryError。即使情况没有那么糟糕，也会引起频繁的full GC，从而影响稳定运行时的性能。

然后，我翻阅了线上启动时大量Timeout以及Full GC的项目代码。发现该项目同样适用“GroovyClassLoader”进行groovy脚本解析，断点接入如下：

![输入图片说明](https://www.xuxueli.com/blog/static/images/img_Yk4G.png "在这里输入图片标题")

首先，我发现该项目中的Groovy类加载器是单例；
其次，该项目中的加载一次页面，将会调用多达31次“groovy.lang.GroovyClassLoader.parseClass(groovyScript)”方法进行groovy脚本解析。这很震惊，但是庆幸的是，该系统对解析后的Class做了缓存。

#### 三、分析结果
经过分析，该项目启动是被报大量Timeout和运行Full GC的问题基本锁定，原因如下：

 - 启动时Timeout原因：项目启动完成后，该节点经过健康检查无误被切到线上集群环境，接收线上流量。但是，由于该项目上单个页面模块太多，上文中一张页面加载需要执行解析函数多达31次，而且该项目还托管这许多其他的页面，这导致这些页面的预热时间比较久。但是不幸的是，项目已经通过了健康检查，大量流量涌入阻塞等待页面加载完成，因此导致项目启动时被报大量Timeout。

- 频繁Full GC原因：该项目中Groovy类加载使用单例，当对脚本（即使同一段脚本）多次执行该方法时，会导致 “GroovyClassLoader” 装载的Class越来越多。如果此处临时加载的类不能够被及时释放，最终将会导致PermGen OutOfMemoryError。即使情况没有那么糟糕，也会引起频繁的full GC，从而影响稳定运行时的性能。

#### 三、验证
为了对上述猜想进行验证，设计了一下三段代码进行简单测试。代码逻辑分别为：
- Test1.java：并行启动100个线程，并行解析groovy脚本，使用单例类加载器；
- Test2.java：并行启动100个线程，并行解析groovy脚本，使用非单例类加载器；
- Test3.java：并行启动100个线程，并行打印log。

本文中测试方法为，启动下面三段测试代码中的Main方法，通过查看各自JVM的GC情况从而验证GroovyClassLoader对JVM的影响。

##### 代码A：Test1.java
```
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import groovy.lang.GroovyClassLoader;

public class Test {

	public static void main(String[] args) throws InterruptedException, IOException {
		final String code = readAll("DemoHandlerAImpl.groovy");
		final AtomicInteger count = new AtomicInteger(0);

		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Object object = parseClass(code);
						System.out.println("COUNT1=" + count.incrementAndGet() + ", " + object.hashCode());
					}
				}
			});
		}

	}

	static GroovyClassLoader classLoader = new GroovyClassLoader();
	public static Object parseClass(String code){
		return classLoader.parseClass(code);
	}

	public static String readAll(String logFile){
		try {
			InputStream ins = null;
			BufferedReader reader = null;
			try {
				ins = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource(logFile).getPath());
				reader = new BufferedReader(new InputStreamReader(ins, "utf-8"));
				if (reader != null) {
					String content = null;
					StringBuilder sb = new StringBuilder();
					while ((content = reader.readLine()) != null) {
						sb.append(content).append("\n");
					}
					return sb.toString();
				}
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
```

##### 代码2：Test2.java
```
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import groovy.lang.GroovyClassLoader;

public class Test2 {

	public static void main(String[] args) throws InterruptedException, IOException {
		final String code = readAll("DemoHandlerAImpl.groovy");
		final AtomicInteger count = new AtomicInteger(0);

		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Object object = parseClass(code);
						System.out.println("COUNT2=" + count.incrementAndGet() + ", " + object.hashCode());
					}
				}
			});
		}

	}

	static GroovyClassLoader classLoader = new GroovyClassLoader();
	public static Object parseClass(String code){
		classLoader = new GroovyClassLoader();
		return classLoader.parseClass(code);
	}

	public static String readAll(String logFile){
		try {
			InputStream ins = null;
			BufferedReader reader = null;
			try {
				ins = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource(logFile).getPath());
				reader = new BufferedReader(new InputStreamReader(ins, "utf-8"));
				if (reader != null) {
					String content = null;
					StringBuilder sb = new StringBuilder();
					while ((content = reader.readLine()) != null) {
						sb.append(content).append("\n");
					}
					return sb.toString();
				}
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
```

##### 代码3：Test3.java
```
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import groovy.lang.GroovyClassLoader;

public class Test3 {

	public static void main(String[] args) throws InterruptedException, IOException {
		final String code = readAll("DemoHandlerAImpl.groovy");
		final Object object = parseClass(code);

		final AtomicInteger count = new AtomicInteger(0);

		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						System.out.println("COUNT3=" + count.incrementAndGet() + ", " + object.hashCode());
					}
				}
			});
		}

	}

	static GroovyClassLoader classLoader = new GroovyClassLoader();
	public static Object parseClass(String code){
		classLoader = new GroovyClassLoader();
		return classLoader.parseClass(code);
	}

	public static String readAll(String logFile){
		try {
			InputStream ins = null;
			BufferedReader reader = null;
			try {
				ins = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource(logFile).getPath());
				reader = new BufferedReader(new InputStreamReader(ins, "utf-8"));
				if (reader != null) {
					String content = null;
					StringBuilder sb = new StringBuilder();
					while ((content = reader.readLine()) != null) {
						sb.append(content).append("\n");
					}
					return sb.toString();
				}
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
```

##### 测试Groovy脚本：DemoHandlerAImpl.groovy
```
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 场景A：托管 “配置信息” ，尤其适用于数据结构比较复杂的配置项
 * 优点：在线编辑；推送更新；+ 直观；
 * @author xuxueli 2016-4-14 15:36:37
 */
public class DemoHandlerAImpl  {

	public Object handle(Map<String, Object> params) {

		// 【基础类型配置】
		boolean ifOpen = true;															// 开关
		int smsLimitCount = 3;															// 短信发送次数阀值
		String brokerURL = "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.2:61616)";	// 套接字配置

		// 【列表配置】
		Set<Integer> blackShops = new HashSet<Integer>();								// 黑名单列表
		blackShops.add(15826714);
		blackShops.add(15826715);
		blackShops.add(15826716);
		blackShops.add(15826717);
		blackShops.add(15826718);
		blackShops.add(15826719);

		// 【KV配置】
		Map<Integer, String> emailDispatch = new HashMap<Integer, String>();			// 不同BU标题文案配置
		emailDispatch.put(555, "淘宝");
		emailDispatch.put(666, "天猫");
		emailDispatch.put(777, "聚划算");

		// 【复杂集合配置】
		Map<Integer, List<Integer>> openCitys = new HashMap<Integer, List<Integer>>();	// 不同城市推荐商户配置
		openCitys.put(11, Arrays.asList(15826714, 15826715));
		openCitys.put(22, Arrays.asList(15826714, 15651231, 86451231));
		openCitys.put(33, Arrays.asList(48612323, 15826715));

		return smsLimitCount;
	}

}
```

##### 在系统运行四分钟后，Test1.java对应JVM的GC如图：
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_PSq7.png "在这里输入图片标题")

从日志可以发现，共解析groovy达38694次。
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_0hka.png "在这里输入图片标题")

##### 在系统运行四分钟后，Test2.java对应JVM的GC如图：
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_bxSA.png "在这里输入图片标题")

从日志可以发现，共解析groovy达39100次。
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_R8QK.png "在这里输入图片标题")

##### 在系统运行四分钟后，Test3.java对应JVM的GC如图：
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_B5I2.png "在这里输入图片标题")

从日志可以发现，共解析groovy达40000次。
![输入图片说明](https://www.xuxueli.com/blog/static/images/img_LgWJ.png "在这里输入图片标题")

#### 三、测试结果分析
通过观察内存曲线图，可以获取测试结果：
- Test1.java：Test1.java：PS MarkSweep有5次，PS Scavenge高达1210次，分散均匀；
- Test2.java：Test2.java：PS MarkSweep有5次，PS Scavenge达到485次，分散均匀；
- Test3.java：Test3.java：PS MarkSweep有0次，PS Scavenge仅5次，且仅在线程启动时触发PS Scavenge。

从上述测试结果可以得到结论：

- 1、Groovy类加载器，频繁解析Groovy代码将会导致PS MarkSweep；
- 2、单例Groovy类加载器，比非单例更容易导致PS Scavenge；
- 3、单例和多实例Groovy类加载器方式，PS MarkSweep基本一致，因为两种方式parseClass生成的Class数量基本一致，即占用的PermGen空间基本一致，所以两种方式在Full GC上的表现基本一致，如果要减少Full GC，减少parseClass才是根本解决方法；但是二者PS Scavenge却有数倍的差别，是因为单例方式parseClass过程中冗余大量的中间对象，这些中间对象会被PS Scavenge掉，不会引起大的问题。因此，减少parseClass次数才是解决的正途。


#### 四、总结优化
- 1、为避免启动时Timeout，应该在项目完全预热完成后再切入线上环境；
- 2、避免在在单次调用时触发多次groovy脚本解析，解析过程本身比较耗时，可并行处理，或者将多个脚本合并为单个脚本；
- 3、针对每个groovy脚本解析后生成的Java对象实例做缓存，而不是代码本身做缓存；
- 4、仅仅在接收到清除缓存的广播时解析生成新的Java实例对象，避免groovy的频繁解析，减少Class装载频率；
- 5、周期性的异步刷新类加载器，避免因全局类加载器频繁parseClass导致的PS Scavenge。

#### PermGen回收
PermGen中对象回收规则：ClassLoader可以被回收，其下的所有加载过的没有对应实例的类信息（保存在PermGen）可被回收。因此，JVM回收之后，可以将GroovyClassLoader加载的冗余新信息回收掉。

但是。GC在JVM中通常是由一个或一组进程来实现的，它本身也和用户程序一样占用heap空间，运行时也占用CPU。因此，当GC运行时间较长时，用户能够感到 Java程序的停顿。因此，尽量避免GC。