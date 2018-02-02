### 缓存比较：
- ehcache 进程缓存：虽然目前ehcache提供集群方案，但是分布式缓存还是使用memcached/redis比较好;
- memcached 分布式缓存,：分布是通常通过分片方式实现, 多核, 数据结构单一, key250k和value1M容量首限;
- redis 分布式缓存：单核, 支持数据结构丰富, key和value512M容量巨大;
- mongodb Nosql：非关系型数据库(平级于mysql)，存储海量数据;

### 页面静态化，ehcache对比html静态化：
- html静态化：针对整页或整站，适用于内容变动频率较低，但是存在动态更新情况。官网等展示类子网站；
- ehcache：针对URL，适用于动态站中静态的特定URL缓存。

【个人感觉，站点缓存，要么整站html静态化，要么使用分布式缓存存储数据，这两种情况已经可以解决全部问题，不太懂ehcache存在的道理，而且ehcache缓存的URL列表更新需要重启，是在不如缓存来的方便，在此仅作为借鉴】

### maven依赖，配置文件
- 1、maven依赖
```
<!-- ehcache -->
<dependency>
	<groupId>net.sf.ehcache</groupId>
	<artifactId>ehcache</artifactId>
	<version>2.9.1</version>
</dependency>
<dependency>
	<groupId>net.sf.ehcache</groupId>
	<artifactId>ehcache-web</artifactId>
	<version>2.0.4</version>
</dependency>
```

- 2、配置文件：ehcache.xml
```
<?xml version="1.0" encoding="utf-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd"
         updateCheck="false" monitoring="autodetect"
         dynamicConfig="true">
    <diskStore path="java.io.tmpdir"/>
    
    <!-- 
        配置自定义缓存
        maxElementsInMemory：缓存中允许创建的最大对象数
        eternal：缓存中对象是否为永久的，如果是，超时设置将被忽略，对象从不过期。
        timeToIdleSeconds：缓存数据的钝化时间，也就是在一个元素消亡之前，
                    两次访问时间的最大时间间隔值，这只能在元素不是永久驻留时有效，
                    如果该值是 0 就意味着元素可以停顿无穷长的时间。
        timeToLiveSeconds：缓存数据的生存时间，也就是一个元素从构建到消亡的最大时间间隔值，
                    这只能在元素不是永久驻留时有效，如果该值是0就意味着元素可以停顿无穷长的时间。
        overflowToDisk：内存不足时，是否启用磁盘缓存。
        memoryStoreEvictionPolicy：缓存满了之后的淘汰算法。
        LRU是最近最少使用页面置换算法(Least Recently Used),也就是首先淘汰最长时间未被使用的页面!
		LFU是最近最不常用页面置换算法(Least Frequently Used),也就是淘汰一定时期内被访问次数最少的页!
    -->
    
    <defaultCache
		maxElementsInMemory="10000"
		eternal="true"
		timeToIdleSeconds="120"
		timeToLiveSeconds="120"
		overflowToDisk="false"
		diskPersistent="false"
		diskExpiryThreadIntervalSeconds="120"
		memoryStoreEvictionPolicy="LRU"
		/>
            
	<cache name="ehcache-obj" 
		maxElementsInMemory="100000" 
		eternal="true" 
		overflowToDisk="false" 
		memoryStoreEvictionPolicy="LRU">
	</cache>

	<cache name="SimplePageCachingFilter" 
		maxElementsInMemory="50000" 
		eternal="false" 
		timeToIdleSeconds="300"
		timeToLiveSeconds="300"
		overflowToDisk="false" 
		memoryStoreEvictionPolicy="LRU"
	/>

</ehcache>
```

##### 配置的cache说明：
- 1、ehcache-obj：ehcache本地对象缓存
- 2、SimplePageCachingFilter：ehcache页面缓存

### 用法1：encache，本地缓存
-1、encache本地缓存工具类：
```
package com.xxl.core.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * ehcache本地对象缓存
 * @author xuxueli 2015-12-5 21:50:28
 */
public class EhcacheUtil {

	// public static CacheManager manager = CacheManager.create("ehcache.xml");	// 指定文件，暂时未搞定
	public static CacheManager manager = CacheManager.create();	// 默认找src目录下的ehcche.xml文件
	
	public static void set(String cacheName, Object key, Object value) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			cache.put(new Element(key, value));
		}
	}
	
	public static Object get(String cacheName, Object key) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			Element element = cache.get(key);
			if (element != null) {
				return element.getObjectValue();
			}
		}
		return null;
	}

	public static boolean remove(String cacheName, Object key) {
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			return cache.remove(key);
		}
		return false;
	}

	public static void main(String[] args) {
		String key = "key";
		String value = "hello";
		EhcacheUtil.set("demo-ehcache", key, value);
		System.out.println(EhcacheUtil.get("demo-ehcache", key));
	}

}
```

- 2、使用Demo
```
@RequestMapping("/ehcacheObj")
@ResponseBody
private String ehcacheObj() {
	Long now = (Long) EhcacheUtil.get("ehcache-obj", "now");
	if (now == null) {
		now = System.currentTimeMillis();
		EhcacheUtil.set("ehcache-obj", "now", now);
	}
	return now.toString();
}
```

### 用法2：encache，页面缓存
- 1、encache页面缓存，过滤器配置（web.xml）
```
<!-- ehcache：缓存、gzip压缩核心过滤器 -->
<filter>  
    <filter-name>PageCacheFilter</filter-name>  
    <filter-class>net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter</filter-class>  
  </filter>  
  <filter-mapping>  
	<filter-name>PageCacheFilter</filter-name>  
	<url-pattern>/ehcacheUrl</url-pattern>  
  </filter-mapping>  
```

- 2、使用Demo
```
@RequestMapping("/ehcacheUrl")
@ResponseBody
private long ehcacheUrl() {
	return System.currentTimeMillis();
}
```


