### JVM内存模型
- 1、Heap(堆)：运行时数据区域，所有类实例和数组的内存均从此处分配
    - Young Generation
        - Eden Space：用来存放新生的对象，内存最初从这个线程池分配给大部分对象。
        - Survivor Space：分为两个，为From和To，它们的大小总是一样，用于保存在eden space内存池中经过垃圾回收后没有被回收的对象。
    - Old Generation：用于保持已经在survivor space内存池中存在了一段时间的对象。
- 2、Non-Heap(非堆)
    - Code Cache：代码缓存区，一个用于编译和保存本地代码（native code）的内存。
    - Perm Gen：永久代，保存虚拟机自己的静态(reflective)数据，例如类（class）和方法（method）对象。Java虚拟机共享这些类数据。这个区域被分割为只读的和只写的。
    - （Jvm Stack）：java虚拟机栈，存放方法参数、局域变量等的引用
    - （Local Method Statck）：本地方法栈


> 堆就是Java代码可及的内存，是留给开发人员使用的；非堆就是JVM留给 自己用的，所以方法区、JVM内部处理或优化所需的内存(如JIT编译后的代码缓存)、每个类结构(如运行时常数池、字段和方法数据)以及方法和构造方法 的代码都在非堆内存中。 

### 常用的几个JVM参数
    -Xms
    JVM初始分配的堆内存大小，默认是物理内存的1/64。
    -Xmx
    JVM最大分配的堆内存大小，默认是物理内存的1/4。
    -XX:PermSize
    JVM初始分配的非堆内存大小，默认是物理内存的1/64。
    -XX:MaxPermSize
    JVM最大分配的非堆内存大小，默认是物理内存的1/4。
    -XX:NewSize
    JVM初始分配的新生代堆区域内存大小。
    -XX:MaxNewSize
    JVM最大分配的新生代堆区域内存大小。
    -XX:ReservedCodeCacheSize
    编译代码时的缓存空间大小。
    
完整的JVM参数，可以参考官方文档： 
- [地址1](http://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html )
- [地址2](http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html) 

```
// 日常开发中，Tomcat内存溢出解决 
-Xms512m 
-Xmx1024m 
-XX:PermSize=512m 
-XX:MaxPermSize=1024m
```

### JVM内存分配过程
- 1、JVM 会试图为相关Java对象在Eden中初始化一块内存区域。
- 2、当Eden空间足够时，内存申请结束；否则到下一步。
- 3、JVM 试图释放在Eden中所有不活跃的对象（这属于1或更高级的垃圾回收）。释放后若Eden空间仍然不足以放入新对象，则试图将部分Eden中活跃对象放入Survivor区。
- 4、Survivor区被用来作为Eden及Old的中间交换区域，当Old区空间足够时，Survivor区的对象会被移到Old区，否则会被保留在Survivor区。
- 5、当Old区空间不够时，JVM 会在Old区进行完全的垃圾收集（0级）。
- 6、完全垃圾收集后，若Survivor及Old区仍然无法存放从Eden复制过来的部分对象，导致JVM无法在Eden区为新对象创建内存区域，则出现”out of memory”错误。

![image](http://image72.360doc.com/DownloadImg/2014/05/0818/41431554_2.png)

### jvm的内存回收过程，简单过程
对象在Eden Space创建，当Eden Space满了的时候，**GC**就把所有在Eden Space中的对象扫描一次，把所有有效的对象复制到第一个Survivor Space，同时把无效的对象所占用的空间释放。

当Eden Space再次变满了的时候，就启动移动程序把Eden Space中有效的对象复制到第二个Survivor Space，同时，也将第一个Survivor Space中的有效对象复制到第二个Survivor Space。

如果填充到第二个Survivor Space中的有效对象被第一个Survivor Space或Eden Space中的对象引用，那么这些对象就是长期存在的，此时这些对象将被复制到Permanent Generation。

若垃圾收集器依据这种小幅度的调整收集不能腾出足够的空间，就会运行**Full GC**，此时jvm gc停止所有在堆中运行的线程并执行清除动作。

### 分区的目的
Young Generation由于对象产生的比较多并且大都是朝生夕灭的，所以直接**采用标记-清理算法**。而Old Generation生命力很强，则采用**复制算法**，针对不同情况使用不同算法。

### 垃圾回收算法

- 复制算法(copying)
> 将内存分成两块，每次只使用其中一块，垃圾回收时，将标记的对象拷贝到另外一块中，然后完全清除原来使用的那块内存。复制后的空间是连续的。**复制算法适用于新生代**，因为垃圾对象多于存活对象，复制算法更高效。在新生代串行垃圾回收算法中，将eden中标记存活的对象拷贝未使用的s1中，s0中的年轻对象也进入s1，如果s1空间已满，则进入老年代；这样交替使用s0和s1。这种改进的复制算法，既保证了空间的连续性，有避免了大量的内存空间浪费。 

![image](http://img.blog.csdn.net/20140101113433906?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQva2luZ29md29ybGQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

- 标记-清除算法(Mark-Sweep)
> 适合用于**老年代Full GC**的算法, 从根节点开始标记所有可达对象，其余没标记的即为垃圾对象，执行清除。但回收后的空间是不连续的。

- 标记-压缩算法(Mark-compact)
> 适合用于**老年代**的算法（存活对象多于垃圾对象）。
标记后不复制，而是将存活对象压缩到内存的一端，然后清理边界外的所有对象。

![image](http://img.blog.csdn.net/20140101114509375?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQva2luZ29md29ybGQ=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

