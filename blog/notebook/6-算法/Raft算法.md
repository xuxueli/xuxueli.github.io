<h2 style="color:#4db6ac !important" >Raft算法.md</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]

## 分布式一致性

想象一下，我们有一个单节点系统，且作为数据库服务器，然后存储了一个值（假设为X）。然后，有一个客户端往服务器发送了一个值（假设为8）。只要服务器接受到这个值即可，这个值在单节点上的一致性非常容易保证：

![](https://www.xuxueli.com/blog/static/images/img_255.jpg)
<center>(单机环境)</center>

但是，如果数据库服务器有多个节点呢？比如，如下图所示，有三个节点：a，b，c。这时候客户端对这个由3个节点组成的数据库集群进行操作时的值一致性如何保证，这就是分布式一致性问题。而Raft就是一种实现了分布式一致性的协议（还有其他一些一致性算法，例如：ZAB、PAXOS等）：

![](https://www.xuxueli.com/blog/static/images/img_250.png)
<center>(分布式环境)</center>

## 一些概念

讲解Raft算法之前，先普及一些Raft协议涉及到的概念：

- ** 1、term ** ：任期，比如新的选举任期，即整个集群初始化时，或者新的Leader选举就会开始一个新的选举任期。
- ** 2、大多数 ** ：假设一个集群由N个节点组成，那么大多数就是至少N/2+1。例如：3个节点的集群，大多数就是至少2；5个节点的集群，大多数就是至少3。
- ** 3、状态 ** ：每个节点有三种状态，且某一时刻只能是三种状态中的一种：Follower（图左），Candidate（图中），Leader（图右）。假设三种状态不同图案如下所示：

![](https://www.xuxueli.com/blog/static/images/img_247.png)
<center>(节点状态图)</center>

初始化状态时，三个节点都是Follower状态，并且term为0，如下图所示：

![](https://www.xuxueli.com/blog/static/images/img_260.png)
<center>(初始化)</center>

## Leader选举

Leader选举需要某个节点发起投票，在确定哪个节点向其他节点发起投票之前，每个节点会分配一个随机的选举超时时间（election timeout）。在这个时间内，节点必须等待，不能成为Candidate状态。现在假设节点a等待168ms , 节点b等待210ms , 节点c等待200ms 。由于a的等待时间最短，所以它会最先成为Candidate，并向另外两个节点发起投票请求，希望它们能选举自己为Leader：

![](https://www.xuxueli.com/blog/static/images/img_257.png)
<center>(发起投票请求)</center>

另外两个节点收到请求后，假设将它们的投票返回给Candidate状态节点a，节点a由于得到了大多数节点的投票，就会从Candidate变为Leader，如下图所示，这个过程就叫做Leader选举（Leader Election）。接下来，这个分布式系统所有的改变都要先经过节点a，即Leader节点：

![](https://www.xuxueli.com/blog/static/images/img_258.png)
<center>(Leader节点)</center>

如果某个时刻，Follower不再收到Leader的消息，它就会变成Candidate。然后请求其他节点给他投票（类似拉票一样）。其他节点就会回复它投票结果，如果它能得到大多数节点的投票，它就能成为新的Leader。

## 日志复制

假设接下来客户端发起一个SET 5的请求，这个请求会首先由leader即节点a接收到，并且节点a写入一条日志。由于这条日志还没被其他任何节点接收，所以它的状态是 **uncommitted **。

![](https://www.xuxueli.com/blog/static/images/img_239.png)

为了提交这条日志，Leader会将这条日志通过心跳消息复制给其他的Follower节点：

![](https://www.xuxueli.com/blog/static/images/img_248.png)
<center>(日志复制)</center>

一旦有大多数节点成功写入这条日志，那么Leader节点的这条日志状态就会更新为committed状态，并且值更新为5：

![](https://www.xuxueli.com/blog/static/images/img_252.png)

Leader节点然后通知其他Follower节点，其他节点也会将值更新为5。如下图所示，这个时候集群的状态是完全一致的，这个过程就叫做 **日志复制（Log Replication）** ：

![](https://www.xuxueli.com/blog/static/images/img_259.png)

## 两个超时

接下来介绍Raft中两个很重要的超时设置：选举超时和心跳超时。

- ** 选举超时 **

为了防止3个节点（假设集群由3个节点组成）同时发起投票，会给每个节点分配一个随机的选举超时时间（Election Timeout），即从Follower状态成为Candidate状态需要等待的时间。在这个时间内，节点必须等待，不能成为Candidate状态。如下图所示，节点C优先成为Candidate，而节点A和B还在等待中：

![](https://www.xuxueli.com/blog/static/images/img_245.jpg)
<center>(选举超时)</center>

- ** 心跳超时 **

如下图所示，节点A和C投票给了B，所以节点B是leader节点。节点B会固定间隔时间向两个Follower节点A和C发送心跳消息，这个固定间隔时间被称为heartbeat timeout。Follower节点收到每一条日志信息都需要向Leader节点响应这条日志复制的结果：

![](https://www.xuxueli.com/blog/static/images/img_253.png)
<center>(心跳超时)</center>

## 重新选举

选举过程中，如果Leader节点出现故障，就会触发重新选举。如下图所示，Leader节点B故障（灰色），这时候节点A和C就会等待一个随机时间（选举超时），谁等待的时候更短，谁就先成为Candidate，然后向其他节点发送投票请求：

![](https://www.xuxueli.com/blog/static/images/img_241.png)
<center>(re-election)</center>

如果节点A能得得到节点C的投票，加上自己的投票，就有大多数选票。那么节点A将成为新的Leader节点，并且Term即任期的值加1更新到2：

![](https://www.xuxueli.com/blog/static/images/img_244.png)
<center>(新Leader节点)</center>

需要说明的是，每个选举期只会选出一个Leader。假设同一时间有两个节点成为Candidate（它们随机等待选举超时时间刚好一样），如下图所示，并且假设节点A收到了节点B的投票，而节点C收到了节点D的投票：

![](https://www.xuxueli.com/blog/static/images/img_256.png)
<center>(2个Candidate节点)</center>

这种情况下，就会触发一次新的选举，节点A和节点B又等待一个随机的选举超时时间，直到一方胜出：

![](https://www.xuxueli.com/blog/static/images/img_249.png)

我们假设节点A能得到大多数投票，那么接下来节点A就会成为新的Leader节点，并且任期term加1：

![](https://www.xuxueli.com/blog/static/images/img_246.png)

## 网络分区

在发生网络分区的时候，Raft一样能保持一致性。如下图所示，假设我们的集群由5个节点组成，且节点B是Leader节点：

![](https://www.xuxueli.com/blog/static/images/img_240.png)
<center>(5个节点的集群)</center>

我们假设发生了网络分区：节点A和B在一个网络分区，节点C、D和E在另一个网络分区，如下图所示，且节点B和节点C分别是两个网络分区中的Leader节点：

![](https://www.xuxueli.com/blog/static/images/img_242.png)
<center>(发生网络分区)</center>

我们假设还有一个客户端，并且往节点B上发送了一个SET 3，由于网络分区的原因，这个值不能被另一个网络分区中的Leader即节点C拿到，它最多只能被两个节点（节点B和C）感知到，所以它的状态是uncomitted（红色）：

![](https://www.xuxueli.com/blog/static/images/img_243.png)
<center>(操作1)</center>

另一个客户端准备执行SET 8的操作，由于可以被同一个分区下总计三个节点（节点C、D和E）感知到，3个节点已经符合大多数节点的条件。所以，这个值的状态就是committed：

![](https://www.xuxueli.com/blog/static/images/img_251.png)
<center>(操作2)</center>

接下来，我们假设网络恢复正常，如下图所示。节点B能感知到C节点这个Leader的存在，它就会从Leader状态退回到Follower状态，并且节点A和B会回滚之前没有提交的日志（SET 3产生的uncommitted日志）。同时，节点A和B会从新的Leader节点即C节点获取最新的日志（SET 8产生的日志），从而将它们的值更新为8。如此以来，整个集群的5个节点数据完全一致了：

![](https://www.xuxueli.com/blog/static/images/img_254.png)
<center>(分区网络恢复)</center>

## 参考
[分布式系统理论进阶 - Paxos](https://www.cnblogs.com/bangerlee/p/5655754.html)
[分布式系统理论进阶 - Raft、Zab](https://www.cnblogs.com/bangerlee/p/5991417.html)
[图解Raft](https://www.jianshu.com/p/5b25b019eebb)
[Raft动画](http://thesecretlivesofdata.com/raft/)