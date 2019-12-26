<h2 style="color:#4db6ac !important" >一致性Hash算法</h2>
> 本文内容来源于书籍和网络。

[TOCM]

[TOC]


## 一致性hash算法是什么？
一致性hash算法，是麻省理工学院1997年提出的一种算法，目前主要应用于分布式缓存当中。
一致性hash算法可以有效地解决分布式存储结构下动态增加和删除节点所带来的问题。

在Memcached、Key-Value Store、Bittorrent DHT、LVS中都采用了一致性hash算法，可以说一致性hash算法是分布式系统负载均衡的首选算法。

## 传统hash算法的弊端
常用的算法是对hash结果取余数 (hash() mod N)：对机器编号从0到N-1，按照自定义的hash算法，对每个请求的hash值按N取模，得到余数i，然后将请求分发到编号为i的机器。
但这样的算法方法存在致命问题，如果某一台机器宕机，那么应该落在该机器的请求就无法得到正确的处理，这时需要将宕掉的服务器使用算法去除，此时候会有(N-1)/N的服务器的缓存数据需要重新进行计算；如果新增一台机器，会有N /(N+1)的服务器的缓存数据需要进行重新计算。对于系统而言，这通常是不可接受的颠簸（因为这意味着大量缓存的失效或者数据需要转移）。

>传统求余做负载均衡算法，缓存节点数由3个变成4个，缓存不命中率为75%。计算方法：穷举hash值为1-12的12个数字分别对3和4取模，然后比较发现只有前3个缓存节点对应结果和之前相同，所以有75%的节点缓存会失效，可能会引起缓存雪崩。

## 一致性hash算法

- 1、首先，我们将hash算法的值域映射成一个具有2的32次方个桶的空间中，即0~（2的32次方）-1的数字空间。现在我们可以将这些数字头尾相连，组合成一个闭合的环形。
- 2、每一个缓存key都可以通过Hash算法转化为一个32位的二进制数，也就对应着环形空间的某一个缓存区。我们把所有的缓存key映射到环形空间的不同位置。
- 3、我们的每一个缓存节点也遵循同样的Hash算法，比如利用IP或者主机名做Hash，映射到环形空间当中，如下图

![](https://www.xuxueli.com/blog/static/images/img_231.png)

** 如何让key和缓存节点对应起来呢？ **
很简单，每一个key的顺时针方向最近节点，就是key所归属的缓存节点。所以图中key1存储于node1，key2，key3存储于node2，key4存储于node3。

![](https://www.xuxueli.com/blog/static/images/img_232.png)

当缓存的节点有增加或删除的时候，一致性哈希的优势就显现出来了。让我们来看看实现的细节：

** 增加节点 **
当缓存集群的节点有所增加的时候，整个环形空间的映射仍然会保持一致性哈希的顺时针规则，所以有一小部分key的归属会受到影响。

![](https://www.xuxueli.com/blog/static/images/img_233.png)

** 有哪些key会受到影响呢？ **
图中加入了新节点node4，处于node1和node2之间，按照顺时针规则，从node1到node4之间的缓存不再归属于node2，而是归属于新节点node4。因此受影响的key只有key2。

![](https://www.xuxueli.com/blog/static/images/img_234.png)

最终把key2的缓存数据从node2迁移到node4，就形成了新的符合一致性哈希规则的缓存结构。

** 删除节点 **
当缓存集群的节点需要删除的时候（比如节点挂掉），整个环形空间的映射同样会保持一致性哈希的顺时针规则，同样有一小部分key的归属会受到影响。
    
![](https://www.xuxueli.com/blog/static/images/img_235.png)

** 有哪些key会受到影响呢？ **
图中删除了原节点node3，按照顺时针规则，原本node3所拥有的缓存数据就需要“托付”给node3的顺时针后继节点node1。因此受影响的key只有key4。

![](https://www.xuxueli.com/blog/static/images/img_236.png)

最终把key4的缓存数据从node3迁移到node1，就形成了新的符合一致性哈希规则的缓存结构。

>说明：这里所说的迁移并不是直接的数据迁移，而是在查找时去找顺时针的后继节点，因缓存未命中而刷新缓存。

>计算方法：假设节点hash散列均匀（由于hash是散列表，所以并不是很理想），采用一致性hash算法，缓存节点从3个增加到4个时，会有0-33%的缓存失效，此外新增节点不会环节所有原有节点的压力。

** 如果出现分布不均匀的情况怎么办？ **
一致性hash算法的结果相比传统hash求余算法已经进步很多，但是出现分布不均匀的情况会有问题。比如下图这样，按顺时针规则，所有的key都归属于统一个节点。

![](https://www.xuxueli.com/blog/static/images/img_237.png)


## 一致性hash算法+虚拟节点

为了优化这种节点太少而产生的不均衡情况。一致性哈希算法引入了 **虚拟节点** 的概念。
所谓虚拟节点，就是基于原来的物理节点映射出N个子节点，最后把所有的子节点映射到环形空间上。

![](https://www.xuxueli.com/blog/static/images/img_238.png)

> 虚拟节点越多，分布越均匀。使用一致性hash算法+虚拟节点这种情况下，缓存节点从3个变成4个，缓存失效率为25%，而且每个节点都平均的承担了压力。

## 一致性hash算法+虚拟节点的实现

原理理解了，实现并不难，主要是一些细节：

- 1、hash算法的选择。
    - Java代码不要使用hashcode函数，这个函数结果不够散列，而且会有负值需要处理。
    - 这种计算Hash值的算法有很多，比如CRC32HASH、FNV132HASH、KETAMAHASH等，其中KETAMAHASH是默认的MemCache推荐的一致性Hash算法，用别的Hash算法也可以，比如FNV132_HASH算法的计算效率就会高一些。
- 2、数据结构的选择。根据算法原理，我们的算法有几个要求：
    - 要能根据hash值排序存储
    - 排序存储要被快速查找 （List不行）
    - 排序查找还要能方便变更 （Array不行）
    
另外，由于二叉树可能极度不平衡。所以采用红黑树是最稳妥的实现方法。Java中直接使用TreeMap即可。

  
## Java实现示例

```
package com.xxl.util.core.skill.consistencyhash;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash
 * @author xuxueli 2015-10-26 22:50:29
 * 
 * 《普通hash方法》
 * NODE节点 : 有序数组或list列表, 如缓存redis服务器ip列表;
 * 数据节点 : 支持获取hashCode, 如redis key;
 * 映射方法 : key.hashCode % NODE.size
 * 优点 : 简单,高效;
 * 缺点 : NODE增减, 所有映射都会失效, 如redis服务某一个节点宕机, 所有持久化文件要做数据迁移, 其他缓存全部失效;
 * 
 * 《一致性hash算法》
 * hash环 : 抽象概念, 一张巨大的hash环装链表; 
 * NODE节点 : 对NODE进行hash计算, 散列对应到hash环上某一个位置;
 * NODE节点-虚拟节点 : 对每个NODE节点生成一定量虚拟节点, 每个NODE虚拟节点都对关联到一个真实NODE节点, 对虚拟节点进行hash计算, 散列对应到hash换上某一个位置;
 * 数据节点 : 对数据进行hash计算, 对应到hash环上某一个位置, 然后顺时针找到最近的NODE节点, 命中然后存储;
 * 
 * 优点 : NODE增减, 改节点原NODE节点的命中会漂移到相邻的后一个NODE节点, 不会造成整体失效, 只会影响其中一个节点;
 * 缺点 : 理解和维护起来, 需要一定学习成本;
 * 缺点(已解决) : NODE其中一个节点失效, 该节点数据瞬间映射到下一个节点, 会造成例如 “缓存雪崩”现象, 在此引入NODE虚拟节点, 可以将该节点数据, 平衡的散列到其他存活的NODE节点中;
 * 
 * --- 0 --------- node1_1 ----------- node2_2 --------- node1_2 ------ node2_1 ------ 2^64|0 ---
 * -------- key1 --------- key02 ---------
 */
public class ConsistencyHashUtil {

	private List<String> shardNodes;
	private final int NODE_NUM = 1000;
	private TreeMap<Long, String> virtualHash2RealNode = new TreeMap<Long, String>();

	/**
	 * init consistency hash ring, put virtual node on the 2^64 ring
	 */
	public void initVirtual2RealRing(List<String> shards) {
		this.shardNodes = shards;
		for (String node : shardNodes) {
			for (int i = 0; i < NODE_NUM; i++){
				long hashCode = hash("SHARD-" + node + "-NODE-" + i);
				virtualHash2RealNode.put(hashCode, node);
			}
		}
	}

	/**
	 * get real node by key's hash on the 2^64
	 */
	public String getShardInfo(String key) {
		long hashCode = hash(key);
		SortedMap<Long, String> tailMap = virtualHash2RealNode.tailMap(hashCode);
		if (tailMap.isEmpty()) {
			return virtualHash2RealNode.get(virtualHash2RealNode.firstKey());
		}
		return virtualHash2RealNode.get(tailMap.firstKey());
	}
	
	/**
     * prinf ring virtual node info
     */
     public void printMap() {
         System.out.println(virtualHash2RealNode);
     }

	/**
	 *  MurMurHash算法，是非加密HASH算法，性能很高，
	 *  比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
	 *  等HASH算法要快很多，而且据说这个算法的碰撞率很低.
	 *  http://murmurhash.googlepages.com/
	 */
	public static Long hash(String key) {
		
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;
		
		ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
	}
	
	/**
     * get hash code on 2^32 ring (md5散列的方式计算hash值)
     * @param digest
     * @param nTime
     * @return
     */
	public static long hash2(String key) {

		// md5 byte
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}
		md5.reset();
		byte[] keyBytes = null;
		try {
			keyBytes = key.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unknown string :" + key, e);
		}

		md5.update(keyBytes);
		byte[] digest = md5.digest();

		// hash code, Truncate to 32-bits
		long hashCode = ((long) (digest[3] & 0xFF) << 24)
				| ((long) (digest[2] & 0xFF) << 16)
				| ((long) (digest[1] & 0xFF) << 8) 
				| (digest[0] & 0xFF);

		long truncateHashCode = hashCode & 0xffffffffL;
		return truncateHashCode;
	}
	 
	public static void main(String[] args) {
		List<String> shards = new ArrayList<String>();
   	 	shards.add("consumer-uuid-2");
   	 	shards.add("consumer-uuid-1");
   	 
		ConsistencyHashUtil sh = new ConsistencyHashUtil();
		sh.initVirtual2RealRing(shards);
		sh.printMap();
		
		int consumer1 = 0;
		int consumer2 = 0;
		for (int i = 0; i < 10000; i++) {
			String key = "consumer" + i;
			System.out.println(hash(key) + ":" + sh.getShardInfo(key));
			if ("consumer-uuid-1".equals(sh.getShardInfo(key))) {
				consumer1++;
			}
			if ("consumer-uuid-2".equals(sh.getShardInfo(key))) {
				consumer2++;
			}
		}
		System.out.println("consumer1:" + consumer1);
		System.out.println("consumer2:" + consumer2);
		
		/*long start = System.currentTimeMillis();
		for (int i = 0; i < 1000 * 1000 * 1000; i++) {
			if (i % (100 * 1000 * 1000) == 0) {
				System.out.println(i + ":" + hash("key1" + i));
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);*/
	}

}

```
