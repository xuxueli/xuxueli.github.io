<h2 style="color:#4db6ac !important" >ConcurrentMap-线程安全Map</h2>

[TOCM]

[TOC]

---

### LinkedBlockingQueue
**由于LinkedBlockingQueue实现是线程安全的，实现了先进先出等特性，是作为生产者消费者的首选**

LinkedBlockingQueue ，其中主要用到put和take方法，put方法在队列满的时候会阻塞直到有队列成员被消费，take方法在队列空的时候会阻塞，直到有队列成员被放进来。

BlockingQueue 不接受null 元素。null 被用作指示poll 操作失败的警戒值。

BlockingQueue位于juc包，常用语并发的生产者、消费者场景，与普通queue相比，增加两个put，take操作：获取元素时等待队列变为非空，以及存储元素时等待空间变得可用。


##### 1.BlockingQueue定义的常用方法如下

--          | 抛出异常  |   boolean/null  | 阻塞      | 超时
---|---|---|---|---
插入        | add(e)    |   offer(e)    | put(e)    | offer(e,time,unit)
移除并返回  | remove()  |   poll()      | take()    | poll(time,unit)
返回不移除  | element() |   peek()      | --        | --

##### BlockingQueue的几个注意点
- 1、BlockingQueue 可以是限定容量的：
    >可以指定容量，也可以不指定，不指定的话，默认最大是Integer.MAX_VALUE
- 2、BlockingQueue 实现主要用于生产者-使用者队列
- 3、BlockingQueue 实现是线程安全的

##### 简要概述BlockingQueue常用的四个实现类

- ArrayBlockingQueue：规定大小的BlockingQueue,其构造函数必须带一个int参数来指明其大小.其所含的对象是以FIFO(先入先出)顺序排序的.
- LinkedBlockingQueue : 大小不定的BlockingQueue,若其构造函数带一个规定大小的参数,生成的BlockingQueue有大小限制,若不带大小参数,所生成的BlockingQueue的大小由Integer.MAX_VALUE来决定.其所含的对象是以FIFO(先入先出)顺序排序的
- PriorityBlockingQueue : 类似于LinkedBlockQueue,但其所含对象的排序不是FIFO,而是依据对象的自然排序顺序或者是构造函数的Comparator决定的顺序.
- SynchronousQueue : 特殊的BlockingQueue,对其的操作必须是放和取交替完成的.

其中LinkedBlockingQueue和ArrayBlockingQueue比较起来,它们背后所用的数据结构不一样（分别通过数组、链表方式实现）,导致LinkedBlockingQueue的数据吞吐量要大于ArrayBlockingQueue,但在线程数量很大时其性能的可预见性低于ArrayBlockingQueue.  


##### LinkedBlockQueue源码分析

- 队列Node节点，内部类：
    >Node是Queue的一个内部类，代表队列中一个节点，此节点补单包含存储的对象值，还包含一个指向下一个节点的引用，这样第一个节点，指向第二个，一次类推，形成一个似链条一样的链。第一个元素称作：头，最后一个称作：尾，如果只有一个元素即是头又是尾。

- 构造函数
    >默认创建Integer.MAX_VALUE容量的队列，头元素和尾元素指向同一个没有值的Node。

- Put方法
 
```
public void put(E e) throws InterruptedException {
    // 不接受null值
    if (e == null) throw new NullPointerException();    
    // Note: convention in all put/take/etc is to preset local var
    // holding count negative to indicate failure unless set.
    int c = -1;
    Node<E> node = new Node(e);
    // 写入锁 (因为读写操作队头队尾，需要两把锁)
    final ReentrantLock putLock = this.putLock; 
    // 容量计步器
    final AtomicInteger count = this.count; 
    putLock.lockInterruptibly();
    try {
        /*
         * Note that count is used in wait guard even though it is
         * not protected by lock. This works because count can
         * only decrease at this point (all other puts are shut
         * out by lock), and we (or some other waiting put) are
         * signalled if it ever changes from capacity. Similarly
         * for all other uses of count in other wait guards.
         */
        // 队列慢，阻塞
        while (count.get() == capacity) {
            notFull.await();
        }
        // 新增元素 （ 队尾的last指针指向新添加的元素 ）
        enqueue(node);
        // 容量计步器+1
        c = count.getAndIncrement();
        // 容量未满，通知notFull
        if (c + 1 < capacity)
            notFull.signal();
    } finally {
        putLock.unlock();
    }
    if (c == 0)
        signalNotEmpty();
}

```

- take方法
```
public E take() throws InterruptedException {
    E x;
    int c = -1;
    // 容量计步器
    final AtomicInteger count = this.count;
    // 取数据锁
    final ReentrantLock takeLock = this.takeLock;
    takeLock.lockInterruptibly();
    try {
        // 队列为空，阻塞并等待
        while (count.get() == 0) {
            notEmpty.await();
        }
        // 队列不为空，返回对头元素，队头指针置空
        x = dequeue();
        // 容量计步器-1
        c = count.getAndDecrement();
        // 容量为空，通知notEmpty
        if (c > 1)
            notEmpty.signal();
    } finally {
        takeLock.unlock();
    }
    if (c == capacity)
        signalNotFull();
    return x;
}

// 移除队头元素并返回
private E dequeue() {
    // assert takeLock.isHeldByCurrentThread();
    // assert head.item == null;
    Node<E> h = head;
    Node<E> first = h.next;
    h.next = h; // help GC
    head = first;
    E x = first.item;
    first.item = null;
    return x;
}

```

##### ArrayBlocklingQueue 源码分析
与普通队列相比，其put、take方法法支持阻塞，下面看下这两个方法源码如下：

- put
```
public void put(E e) throws InterruptedException {
    // 不接受null值
    checkNotNull(e);
    // 获取lock
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        // 容量满，阻塞，通知notFull
        while (count == items.length)
            notFull.await();
        // 添加元素
        insert(e);
    } finally {
        lock.unlock();
    }
}

// 新增一个元素
private void insert(E x) {
    items[putIndex] = x;
    putIndex = inc(putIndex);
    ++count;
    notEmpty.signal();
}

```

- take
```
public E take() throws InterruptedException {
    // 获取lock
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        // 容量未空，阻塞
        while (count == 0)
            notEmpty.await();
        // 移除并返回元素
        return extract();
    } finally {
        // 释放锁
        lock.unlock();
    }
}
```

从上面的简单源码分析看，链表、数组实现程序的空值流程基本一致，获取锁，添加，移除元素，释放锁，唯一不同点在于链表内部维护指针，数组内部维护索引，具体采用哪一个较好，还是根据业务场景，看二者的put、take方法的时间复杂度。
