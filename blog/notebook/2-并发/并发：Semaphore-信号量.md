<h2 style="color:#4db6ac !important" >ConcurrentMap-线程安全Map</h2>

[TOCM]

[TOC]

---

#### Semaphore概要
Semaphore也是一个线程同步的辅助类，可以维护当前访问自身的线程个数，并提供了同步机制。使用Semaphore可以控制同时访问资源的线程个数，例如，实现一个文件允许的并发访问数。

#### 主要方法摘要：

    void acquire():从此信号量获取一个许可，在提供一个许可前一直将线程阻塞，否则线程被中断。
    void release():释放一个许可，将其返回给信号量。
    int availablePermits():返回此信号量中当前可用的许可数。
    boolean hasQueuedThreads():查询是否有线程正在等待获取。