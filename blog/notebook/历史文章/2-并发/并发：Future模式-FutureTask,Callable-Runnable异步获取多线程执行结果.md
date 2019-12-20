<h2 style="color:#4db6ac !important" >ConcurrentMap-线程安全Map</h2>

[TOCM]

[TOC]

---

##### Future模式，核心思想
> Future模式的核心在于：去除了主函数的等待时间，并使得原本需要等待的时间段可以用于处理其他业务逻辑（根据《Java程序性能优化》）。

##### Future模式，Java实现

- Client的实现
```
/**
    Client主要完成的功能包括：
        1. 返回一个FutureData；
        2.开启一个线程用于构造RealData。
*/
public class Client {
    public Data request(final String string) {
        final FutureData futureData = new FutureData();
         
        new Thread(new Runnable() {
            @Override
            public void run() {
                //RealData的构建很慢，所以放在单独的线程中运行
                RealData realData = new RealData(string);
                futureData.setRealData(realData);
            }
        }).start();
         
        return futureData; //先直接返回FutureData
    }
}
```

- Data的实现

```
/**
无论是FutureData还是RealData都实现该接口。
*/
public interface Data {
    String getResult() throws InterruptedException;
}
```

- FutureData的实现
```
/**
FutureData是Future模式的关键，它实际上是真实数据RealData的代理，封装了获取RealData的等待过程。
*/

//FutureData是Future模式的关键，它实际上是真实数据RealData的代理，封装了获取RealData的等待过程
public class FutureData implements Data {
    RealData realData = null; //FutureData是RealData的封装
    boolean isReady = false;  //是否已经准备好
     
    public synchronized void setRealData(RealData realData) {
        if(isReady)
            return;
        this.realData = realData;
        isReady = true;
        notifyAll(); //RealData已经被注入到FutureData中了，通知getResult()方法
    }
 
    @Override
    public synchronized String getResult() throws InterruptedException {
        if(!isReady) {
            wait(); //一直等到RealData注入到FutureData中
        }
        return realData.getResult(); 
    }
}
```

- RealData的实现
```
/**
RealData是最终需要使用的数据，它的构造函数很慢。
*/
public class RealData implements Data {
    protected String data;
 
    public RealData(String data) {
        //利用sleep方法来表示RealData构造过程是非常缓慢的
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.data = data;
    }
 
    @Override
    public String getResult() {
        return data;
    }
}
```

- 测试运行
```
/**
主函数主要负责调用Client发起请求，并使用返回的数据。
*/
public class Application {
    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        //这里会立即返回，因为获取的是FutureData，而非RealData
        Data data = client.request("name");
        //这里可以用一个sleep代替对其他业务逻辑的处理
        //在处理这些业务逻辑过程中，RealData也正在创建，从而充分了利用等待时间
        Thread.sleep(2000);
        //使用真实数据
        System.out.println("数据="+data.getResult());
    }
}
```

##### Future模式，JDK内置实现 (FutureTask)

> 由于Future是非常常用的多线程设计模式，因此在JDK中内置了Future模式的实现。这些类在java.util.concurrent包里面。

> 其中最为重要的是FutureTask类，它实现了Runnable接口，作为单独的线程运行。在其run()方法中，通过Sync内部类调用Callable接口，并维护Callable接口的返回对象。当使用FutureTask.get()方法时，将返回Callable接口的返回对象。

同样，针对上述的实例，如果使用JDK自带的实现，则需要作如下调整。

首先，Data接口和FutureData就不需要了，JDK帮我们实现了。

其次，RealData改为这样：
```
import java.util.concurrent.Callable;
 
public class RealData implements Callable<string> {
    protected String data;
 
    public RealData(String data) {
        this.data = data;
    }
 
    @Override
    public String call() throws Exception {
        //利用sleep方法来表示真是业务是非常缓慢的
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }
}
```

最后，在测试运行时，这样调用：
```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
 
public class Application {
    public static void main(String[] args) throws Exception {
        FutureTask<string> futureTask = new FutureTask<string>(new RealData("name"));
        ExecutorService executor = Executors.newFixedThreadPool(1); //使用线程池
        
        //执行FutureTask，相当于上例中的client.request("name")发送请求
        executor.submit(futureTask);
        
        //这里可以用一个sleep代替对其他业务逻辑的处理
        //在处理这些业务逻辑过程中，RealData也正在创建，从而充分了利用等待时间
        
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
        
        //使用真实数据
        //如果call()没有执行完成依然会等待
        System.out.println("数据=" + futureTask.get());
    }
}
```

### 实例A：FutureTask单线程方式使用
> FutureTask单线程方式使用：分支线程不会阻塞主线程；但是当需要获取FutureTask执行结果时，会阻塞主线程，直至获取执行结果；

```
package com.xxl.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask单线程方式使用：分支线程不会阻塞主线程；但是当需要获取FutureTask执行结果时，会阻塞主线程，直至获取执行结果；
 */
public class FutureTaskTest {

	public static void main(String[] args) {
		CallableObj co = new CallableObj(0);					// 1、新建callable
		FutureTask<Integer> ft = new FutureTask<Integer>(co);	// 2、新建FutureTask
		Thread td = new Thread(ft);								// 3、新建Thread
		
		System.out.println("futureTask开始执行计算:" + System.currentTimeMillis());
		td.start();												// 4、分支线程，start （注意区别run，run简单调用方法，阻塞的）
		
		System.out.println("main 主线程可以做些其他事情:" + System.currentTimeMillis());
		
		try {
			Integer result = ft.get();							// 5、futureTask的get方法会阻塞，直至取得结果为止
			System.out.println("计算的结果是:" + result);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("取得分支线程执行的结果后，主线程可以继续处理其他事项");
	}

}

class CallableObj implements Callable<Integer> {
	private Integer sum;
	public CallableObj(Integer sum) {
		this.sum = sum;
	}
	public Integer call() throws Exception {
		for (int i = 0; i < 100; i++) {
			sum = sum + i;
		}
		// 休眠5秒钟，观察主线程行为，预期的结果是主线程会继续执行，到要取得FutureTask的结果时等待直至完成。
		Thread.sleep(3000);
		System.out.println("futureTask 执行完成" + System.currentTimeMillis());
		return sum;
	}
}
```

### 实例B：FutureTask多线程方式使用
> FutureTask多线程方式使用：充分的利用CPU来运算数据，并且处理返回的结果

```
package com.xxl.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask多线程方式使用：充分的利用CPU来运算数据，并且处理返回的结果
 */
public class FutureTaskThreadPoolTest {

	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();					// 1、实例化，无边界线程池
		
		List<FutureTask<Integer>> list = new ArrayList<FutureTask<Integer>>();	// 2、实例化，FutureTask列表
		for (int i = 0; i < 3; i++) {
			CallableClass cc = new CallableClass(i);							// 3.1、新建callable
			FutureTask<Integer> ft = new FutureTask<Integer>(cc);				// 3.2、新建FutureTask
			// 添加到list,方便后面取得结果
			list.add(ft);			
			// 一个个提交给线程池，当然也可以一次性的提交给线程池，exec.invokeAll(list);
			exec.submit(ft);													// 3.3、线程池，提交FutureTask
		}

		// 开始统计结果
		Integer total = 0;
		for (FutureTask<Integer> tempFt : list) {
			try {
				total = total + tempFt.get();									// 4、futureTask的get方法会阻塞，直至取得结果为止
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		exec.shutdown();			// 处理完毕，一定要记住关闭线程池，这个不能在统计之前关闭，因为如果线程多的话,执行中的可能被打断
		System.out.println("多线程计算后的总结果是:" + total);
	}
}

/**
 * 这个类很简单，就是统计下简单的加法（从1 到total)
 */
class CallableClass implements Callable<Integer> {
	private Integer total;
	private Integer sum = 0;
	public CallableClass(Integer total) {
		this.total = total;
	}
	public Integer call() throws Exception {
		for (int i = 1; i < total + 1; i++) {
			sum = sum + i;
		}
		System.out.println(Thread.currentThread().getName() + " sum:" + sum);
		return sum;
	}
}
```

### 实例C：非FutureTask模式下，获取多线程执行结果
> 非FutureTask模式下，获取多线程执行结果：shutdown + awaitTermination，获取线程池执行结果

```
package com.xxl.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 非FutureTask模式下，获取多线程执行结果：shutdown + awaitTermination，获取线程池执行结果
 */
public class OldThreadPoolCallbackTest {
	
	public static void main(String[] args) throws InterruptedException {

		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i <= 5; i++) {
			list.add(i);
		}
		
		final Map<String, Integer> map = new HashMap<String, Integer>();	// 1、定义线程共享变量
		
		ExecutorService executorService = Executors.newCachedThreadPool();	// 2、实例化，无边界线程池
		for (final int item : list) {
			TestTheadRunable thread = new TestTheadRunable(map, item);
			if(!executorService.isShutdown()){
				executorService.execute(thread);							// 3.2、执行线程
			}
		}
		
		executorService.shutdown();	// 线程池就会不再接受任务
		executorService.awaitTermination(120 * 1000, TimeUnit.MINUTES);	// 等待所有任务执行完毕，执行完毕返回true或者超时返回false
		
		/*while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("线程池没有关闭");
        }*/
		
		System.out.println("******************************");
		for (Integer item : list) {
			String key = "item" + item;
			System.out.println(key + ":" + map.get(key).toString());
		}
	
	}
	
}

class TestTheadRunable implements Runnable{
	private Map<String, Integer> map;
	private int item;
	public TestTheadRunable(Map<String, Integer> map, int item){
		this.map = map;
		this.item = item;
	}
	public void run() {
		String key = "item" + item;
		int result = item;
		if (item == 3) {
			result = 0;
		}
		map.put(key, result);
		System.out.println(key + "=" + result);
	}
}

```

### 实例D：Callable + newFixedThreadPool 模式下，获取多线程执行结果

```
List<Callable<CalculateResponse>> callableList = new ArrayList<>();
for (int i=0; i< 3; i++) {

    Callable<CalculateResponse> callableItem = new Callable<CalculateResponse>() {
        @Override
        public CalculateResponse call() throws Exception {
            CalculateResponse response = new CalculateResponse();;
            // ---
            return response;
        }
    };

    callableList.add(callableItem);
}

ExecutorService executor = Executors.newFixedThreadPool(callableList.size());
try {
    List<Future<CalculateResponse>> calculateResponseList = executor.invokeAll(callableList, 2000L, TimeUnit.MILLISECONDS);
    for(Future<CalculateResponse> calculateResponseItem : calculateResponseList){
        calculateResponseItem.get(2000L, TimeUnit.MILLISECONDS);
    }
} catch (Exception e) {
    logger.error("并行计算异常：", e);
} finally {
    executor.shutdown();
}
```

[参考](http://www.2cto.com/kf/201411/351903.html)