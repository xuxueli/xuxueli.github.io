<h2 style="color:#4db6ac !important" >ConcurrentMap-线程安全Map</h2>

[TOCM]

[TOC]

---

### ThreadPoolExecutor
ThreadPoolExecutor作为java.util.concurrent包对外提供基础实现，以内部线程池的形式对外提供管理任务执行，线程调度，线程池管理等等服务； 

##### 构造方法参数讲解
参数名 | 作用
--- | ---
corePoolSize    |   核心线程池大小
maximumPoolSize |	最大线程池大小
keepAliveTime	|   线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
TimeUnit        |	keepAliveTime时间单位
workQueue	    |   阻塞任务队列
threadFactory	|   新建线程工厂
RejectedExecutionHandler    |   	当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理

##### 重点讲解： 
其中比较容易让人误解的是：corePoolSize，maximumPoolSize，workQueue之间关系。

- 1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。 
- 2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行 
- 3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务 
- 4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理 
- 5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程 
- 6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭 

##### 线程管理机制图示：
![image](http://dl2.iteye.com/upload/attachment/0105/9641/92ad4409-2ab4-388b-9fb1-9fc4e0d832cd.jpg)


### Executors提供的线程池配置方案
Executors方法提供的线程服务，都是通过参数设置来实现不同的线程池机制。 

- 1、构造一个固定线程数目的线程池，配置的corePoolSize与maximumPoolSize大小相同，同时使用了一个无界LinkedBlockingQueue存放阻塞任务，因此多余的任务将存在再阻塞队列，不会由RejectedExecutionHandler处理 

```
public static ExecutorService newFixedThreadPool(int nThreads) {  
    return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()); }  
```

- 2、构造一个缓冲功能的线程池，配置corePoolSize=0，maximumPoolSize=Integer.MAX_VALUE，keepAliveTime=60s,以及一个无容量的阻塞队列 SynchronousQueue，因此任务提交之后，将会创建新的线程执行；线程空闲超过60s将会销毁 

```
public static ExecutorService newCachedThreadPool() {  
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>()); }  
```

- 3、构造一个只支持一个线程的线程池，配置corePoolSize=maximumPoolSize=1，无界阻塞队列LinkedBlockingQueue；保证任务由一个线程串行执行 

```
public static ExecutorService newSingleThreadExecutor() {  
    return new FinalizableDelegatedExecutorService (new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));  
}  
```

- 4、构造有定时功能的线程池，配置corePoolSize，无界延迟阻塞队列DelayedWorkQueue；有意思的是：maximumPoolSize=Integer.MAX_VALUE，由于DelayedWorkQueue是无界队列，所以这个值是没有意义的

```
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {  
    return new ScheduledThreadPoolExecutor(corePoolSize);  
}  
  
public static ScheduledExecutorService newScheduledThreadPool(  
            int corePoolSize, ThreadFactory threadFactory) {  
    return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);  
}  

// detail
public ScheduledThreadPoolExecutor(int corePoolSize,  
                             ThreadFactory threadFactory) {  
    super(corePoolSize, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), threadFactory);  
}  
```

##### 总结： 
- 1、用ThreadPoolExecutor自定义线程池，看线程是的用途，如果任务量不大，可以用无界队列，如果任务量非常大，要用有界队列，防止OOM 
- 2、如果任务量很大，还要求每个任务都处理成功，要对提交的任务进行阻塞提交，重写拒绝机制，改为阻塞提交。保证不抛弃一个任务

```
// 重写RejectedExecutionHandler，阻塞提交任务，不抛弃任务
private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {    
    @Override    
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {    
        try {  
                            // 核心改造点，由blockingqueue的offer改成put阻塞方法  
            executor.getQueue().put(r);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }    
}   
```
- 3、最大线程数一般设为2N+1最好，N是CPU核数 
- 4、核心线程数，看应用，如果是任务，一天跑一次，设置为0，合适，因为跑完就停掉了，如果是常用线程池，看任务量，是保留一个核心还是几个核心线程数 
- 5、如果要获取任务执行结果，用CompletionService，但是注意，获取任务的结果的要重新开一个线程获取，如果在主线程获取，就要等任务都提交后才获取，就会阻塞大量任务结果，队列过大OOM，所以最好异步开个线程获取结果


### 生产消费者模型A：“LinkedBlockingQueue + Executors.newCachedThreadPool()”方式
```
package com.xxl.util.core.skill.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产消费者模型，“FIFO队列，线程池，异步”，“Executors.newCachedThreadPool() + LinkedBlockingQueue”方式实现
 * 特点：启动时初始化指定数量Producer、Consumer线程；Producer负责push数据到queue中，Consumer负责从queue中pull数据并处理；
 * @author xuxueli 2015-9-1 18:05:56
 */
public class ThreadPoolQueueHelper {
	private static Logger logger = LoggerFactory.getLogger(ThreadPoolLinkedHelper.class);
	
	private ExecutorService executor = Executors.newCachedThreadPool();
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(0xfff8);
	
	public ThreadPoolQueueHelper(){
		// producer thread, can be replaced by method "pushData()"
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					pushData("data-" + System.currentTimeMillis());
					try {
						TimeUnit.SECONDS.sleep(3L);
					} catch (InterruptedException e) {
						logger.info("ThreadPoolQueueHelper producer error：", e);
					}
				}
			}
		});
		// consumer thread
		for (int i = 0; i < 2; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					while (true) {
						String originData;
						try {
							originData = queue.poll(5L, TimeUnit.SECONDS);
							logger.info("poll:{}", originData);
							if (originData == null) {
								TimeUnit.SECONDS.sleep(5L);
							}
						} catch (InterruptedException e) {
							logger.info("ThreadPoolQueueHelper consumer error：", e);
						}
					}
				}
			});
		}
	}
	
	private static ThreadPoolQueueHelper helper = new ThreadPoolQueueHelper();
	public static ThreadPoolQueueHelper getInstance(){
		return helper;
	}
	
	public static boolean pushData(String originData){
		boolean status = false;
		if (originData != null && originData.trim().length()>0) {
			status = getInstance().queue.offer(originData);
		}
		logger.info("offer data:{}, status:{}", originData, status);
		return status;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			pushData("data" + i);
		}
	}
	
}

```
原理：该模型中每个消息对应阻塞队列中一条数据；生产者线程负责向阻塞队列Push消息，消费者线程将会监听阻塞队列并消费队列中消息；

分析：
- 1、Executors.newCachedThreadPool()：构造一个缓冲功能的线程池，配置corePoolSize=0，maximumPoolSize=Integer.MAX_VALUE，keepAliveTime=60s,以及一个无容量的阻塞队列 SynchronousQueue，因此任务提交之后，将会创建新的线程执行；线程空闲超过60s将会销毁 ；
- 2、LinkedBlockingQueue：构造了一个容量为0xfff8的线程安全的阻塞队列。
- 3、pushData()：提供给生产者线程使用的方法，Push数据到共享阻塞队列；


### 生产消费者模型B：“ThreadPoolExecutor”方式
```
package com.xxl.util.core.skill.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生产消费者模型，“FIFO队列，线程池，异步”，“ThreadPoolExecutor”方式实现
 * 特点：启动时初始化ThreadPool(内部线程使用LinkedBlockingQueue维护)，以及Producer线程；Producer负责为每条数据在ThreadPool中创建新的线程，每个线程run方法即消费者逻辑方法；
 * @author xuxueli 2015-9-1 16:57:16
 */
public class ThreadPoolLinkedHelper {
	private static Logger logger = LoggerFactory.getLogger(ThreadPoolLinkedHelper.class);

	private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, Integer.MAX_VALUE, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE),
                new ThreadPoolExecutor.CallerRunsPolicy());
	
	public ThreadPoolLinkedHelper(){
		// producer thread, can be replaced by method "pushData()"
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					pushData("data-" + System.currentTimeMillis());
					try {
						TimeUnit.SECONDS.sleep(3L);
					} catch (InterruptedException e) {
						logger.info("ThreadPoolQueueHelper producer error：", e);
					}
				}
			}
		});
		// consumer thread, is replaced by each thread's run method
	}
	
	private static ThreadPoolLinkedHelper helper = new ThreadPoolLinkedHelper();
	public static ThreadPoolLinkedHelper getInstance(){
		return helper;
	}

	public static void pushData(final String originData) {
		logger.info("producer data:" + originData);
		getInstance().executor.execute(new Runnable() {
			@Override
			public void run() {
				logger.info("consumer data:" + originData);
			}
		});
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			pushData("data" + i);
		}
	}
	
}

```

原理：该模型中每个消息将会生成一个线程并托管给线程池维护，线程池将会异步执行内部托管的线程，从而实现消费功能；

分析：
- 1、ThreadPoolExecutor：构造一个缓冲功能的线程池，配置corePoolSize=0，maximumPoolSize=Integer.MAX_VALUE，keepAliveTime=60s,以及一个容量Integer.MAX_VALUE的阻塞队列，因此任务提交之后，将会创建新的线程执行；线程空闲超过60s将会销毁；ThreadPoolExecutor.CallerRunsPolicy，当线程池满新线程不在入队列，将会直接执行run方法；
- 2、pushData(): 提供给生产者线程使用的方法, 每个消息生成一个生产线程并交给线程池维护；