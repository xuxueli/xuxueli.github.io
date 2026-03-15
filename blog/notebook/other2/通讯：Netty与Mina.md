##### Netty，Mina和Grizzly区别
> 首先，从设计的理念上来看，Mina的设计理念是最为优雅的。当然，由于Netty的主导作者与Mina的主导作者是同一人，出自同一人之手的Netty在设计理念上与Mina基本上是一致的。而Grizzly在设计理念上就较差了点，几乎是Java NIO的简单封装。

> 其次，从项目的出身来看，Mina出身于开源界的大牛Apache组织，Netty出身于商业开源大亨Jboss， 而Grizzly则出身于土鳖Sun公司。从其出身可以看到其应用的广泛程序，到目前为止，我见到业界还是使用Mina多一些，而Netty也在慢慢的应 用起来，而Grizzly则似乎只有Sun自已的项目使用了，如果还有其他的公司或开源项目在使用，那就算我孤陋寡闻。

> 最后，从入门的文档来说，由于Mina见世时间相对较长，官方以及民间的文档与入门示例都相当的多。Netty的官方文档也做得很好，而民间文档就要相对于Mina少一些了。至于Grizzly，不管是官方还是民间，都很少见到其文档。

> netty更新周期更短，新版本的发布比较快； 


##### Netty，Mina历史
- 2004年6月，Trustin Lee发布Netty2。在当时的Java社区，Netty2是第一款基于事件驱动架构的网络编程框架。
- 2004年9月，Trustin Lee正式加入了Apache Directory 项目组，之后主导发布MINA。
- 之后作者去了Jboss，又不管Mina了，又开始搞Netty3，包名也换成了org.jboss.netty，并且内部架构变动也很大。
- 之后，作者离开JBoss，大概大概从3.3.0开始，包名从org.jboss.netty改成了io.netty，托管到Github；（从4开始，Netty团队做了模块依赖的优化）


[Netty系列之Netty高性能之道](http://www.infoq.com/cn/articles/netty-high-performance)
