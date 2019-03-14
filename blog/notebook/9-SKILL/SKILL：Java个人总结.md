### AQS 为什么采用双向队列
next：FIFO方式入队；
pre：AQS线程三种状态 "获取锁、自旋、park"，队头的获取锁的线程，需要通过next寻找首个park状态线程唤醒，自旋状态线程需要通过pre判断是否获取锁；

### 小目标
- java基础；
- 开源：迭代维护XXL系列；为底层依赖项目贡献输出。
- reactor，reactor-netty，了解rscoket；
- spring、netty、jetty、openjdk、dubbo 等(争取贡献pr)
- sofastack、fescar；
- k8s、istio以及sofamosn，实践servicemesh架构（备选）
- 架构深入：
    - 高可用性泳道隔离：比如百度红包，泳道+redis，缓存与DB异步同步。可水平部署多套，多泳道高可用；
    - 异地多中心：就近路由。


### hashmap并发问题
多线程时，扩容会导致闭环，cpu飙升；并发时推荐currenthashmap;

### SQL性能优化
状况：大表、尤其只关注大表中少量数据；或者多表关联SQL复杂；导致的性能较低；
方案：搜索引擎、新建宽表；

### freemarker.properties : 推荐配置
```
template_update_delay=0
default_encoding=UTF-8
output_encoding=UTF-8
locale=zh_CN
number_format=0.##########
date_format=yyyy-MM-dd
time_format=HH:mm:ss
datetime_format=yyyy-MM-dd HH:mm:s
classic_compatible=true
template_exception_handler=ignore
```

### HTML5添加 "data-*"，元素携带数据；
data-*可以解决自定义属性混乱无管理;
```
<div data-bigimg="big.jpg" ></div>
// 存取
$("div").data("bigimg");
$("div").data("bigimg","newBig.jpg");
```


### Emoji 表情处理，编解码
https://github.com/vdurmont/emoji-java

```
String str = "An 😀awesome 😃string with a few 😉emojis!";
String result = EmojiParser.parseToAliases(str);
System.out.println(result);


String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
String result = EmojiParser.parseToUnicode(str);
System.out.println(result);
```

### 封箱、拆箱的陷阱
```
Integer tmp_id = null;
……
public void test(int id){
}
……

// 这样会发生NPE，拆箱时无法完整类型转换，需要判空
test(tmp_id)
```


### 编程建议：越抽象、越基础，兼容性越好
源码如下：
```
ConcurrentMap<String, LocalCacheData> cacheRepository = new ConcurrentHashMap<String, LocalCacheData>();
……
for (String key: cacheRepository.keySet()) {
……
}
```
操作：JDK7编译，运行在JDK9报错；
报错如下：
```
java.lang.NoSuchMethodError: java.util.concurrent.ConcurrentHashMap.keySet() 
Ljava/util/concurrent/ConcurrentHashMap$KeySetView;
```
原因：不同版本，对 "ConcurrentHashMap.keySet" 实现不一致。
修改方案：代码左侧类型从实现类 "ConcurrentHashMap" 改为抽象父类 "ConcurrentMap"；
结论：越抽象、越基础，兼容性越好；反之，越具体、越封装，兼容性越差；


### 流close
- 在代码块finally里主动close；
- jdk之后，支持在 "try (OutputStream out = new FileOutputStream("")) { ..." 里创建流，将会自动close；

### Arrays.asList 返回的 Arrays.ArrayList 固定长度；
Arrays.asList 返回 Arrays.ArrayList 并不是常规 new ArrayList()；并没有新增和删除方法；

### 解决跨域问题, JSONP与CROS
```
// 方式1：jsonp
<bean id="jsonpAdvice" class="com.xxx.mvc.advice.JsonpAdvice" />

@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice() {
        super("jsonp");
    }

}

// 方式2：CROS
String origin = request.getHeader("Origin");
response.addHeader("Access-Control-Allow-Origin", origin);
/*response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
response.addHeader("Access-Control-Allow-Headers", "content-type");
response.addHeader("Access-Control-Max-Age", "1800");
response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");*/
response.addHeader("Access-Control-Allow-Credentials", "true");
return super.preHandle(request, response, handler);
```

### freemarker截取字符串subString
```
${content?substring(0,100)}.
${root.keyWord[0..100]} // 结果一样,但却简单
```

### select2 设置选中
```
$('#soff_gar').val(<?php echo $garante; ?>).trigger("change");

```
### freemarker 首字母大写，实现代码生成
```
// 首字母大写
?cap_first

// 代码生成
/**
 *  ${document.name}
 *
 *  Created by ApiHome on '${.now?string('yyyy/MM/dd')}'.
 */
public class ${preficClassName}Request extends ApiRequest {

    <#if queryParamList?exists && queryParamList?size gt 0>
    <#list queryParamList as queryParam>
   /**
    * ${queryParam.desc}
    */
    private ${queryParam.type?lower_case} ${queryParam.name};

    </#list>
    </#if>

    <#if queryParamList?exists && queryParamList?size gt 0>
    <#list queryParamList as queryParam>
    public ${queryParam.type?lower_case} get${queryParam.name?cap_first}() {
        return ${queryParam.name};
    }

    public void set${queryParam.name?cap_first}(${queryParam.type?lower_case} a) {
        this.${queryParam.name} = ${queryParam.name};
    }

    </#list>
    </#if>
}
```

### freemarker, 
```
// 方式一：货币 + 千分位 + 小数两位
freemarkerConfig.setLocale(Locale.CHINA);
?string.currency

// 千分位 + 小数两位
?string("###,###.##")
```


### 极端高并发情况下，缓存优化
- 1、异步刷新缓存，过期缓存仍然视为有效，等待异步刷新，异步刷新时做去重避免冗余刷新；
- 2、缓存命中率，数据变更做双写，不做失效，确保缓存命中率99.99%以上； 
- 3、db层面做优化，分库分表。


### jackson序列化对象时，必须set/get

### hashmap，并发时存在问题
jdk1.8之前，hashmap实现原理：数据 + 链表，链表为解决hash碰撞；
在并发情况下，碰撞可能会导致多个赋值操作因碰撞只有其中一次成功，其余赋值操作丢失。

### return + finally
- try块前return：finally不会被执行；
- try块中有 “System.exit(0);”：finally不会被执行；
- try中return执行顺序："return语句" 》"finally语句块" 》“return返回”
- try和finally都有return执行顺序："try-return语句" 》"finally语句块"》“finally-return返回”

### 防止DDOS
- 1、通过IP；
- 2、通过写Cookie：方式代理换IP；

### 邮箱注册，激活
- 生成 "激活码" ，生成邮件，"发送状态" 为 UN_SEND
- 邮件异步发送，发送状态改为 "SEND"
- 邮件链接进入激活页面，页面从URL获取账号和激活码，确认激活
- 校验用户，是否为 "已激活状态"
- 校验邮件，是否超时

### 读写锁，锁降级
- 1.当读写锁是写加锁时，在这个锁被解索之前所有企图对它加锁的线程都将要阻塞。
- 2.当读写锁是读加锁时，在这个锁被解索之前所有企图以读模式对它加锁的线程都可以获得访问权；以写模式
加锁的线程将堵塞，并且堵塞随后的读模式加锁。这样可以避免读模式锁长期占用，导致等待的写模式锁请求
一直得不到满足。


### Freemarker-Function
<#function matchYzsShop shopId >
    <#return null >
</#function>    
<#assign secondShopItem = matchYzsShop(secondShopId) />

### Freemarkar-Map
- Map的Key默认只接受String，否则需要定制；
- null对象用“?exists”判断无效总是true，需要用"!=null"


### 索引
UpdateTime 处理增量索引；
搜索结构，fiter 逻辑过滤

### 秒杀
1、人校验，是否重复秒杀；
2、商品，减库存， 乐观锁校验；

### 提高性能
1、cache
2、批量接口
3、批量逻辑 + fulture

### 协议无关
http://*** 改为 https:// 或者 //***

### 单机，并发限制（分布式Redis或Memcached）
```
private final AtomicInteger accessCount = new AtomicInteger(0);

if (accessCount.get() > 5) {
}
try{
    accessCount.incrementAndGet();
} finally {
	accessCount.decrementAndGet();
}
```


### 正则转义问题
“/d” 复制到剪切板，被转移到“d”，正则出错；

### ArrayList之set、add

```
set(int index, E element)   // 指定位置覆盖
add(int index, E element)   // 指定位置插入
```



### Apache给出的Tomcat对应的Servlet/JSP规范和JDK版本：
http://tomcat.apache.org/whichversion.html

Servlet| JSP Spec | Supported Java Versions | Tomcat | Jetty
---|---|---|---|---
4.0 | TBD (2.4?) | 8 and later | 9.0.x | 9.3.x
3.1 | 2.3 | 7 and later | 8.5.x | 9.2.x
3.0 | 2.2 | 6 and later(7 and later for WebSocket) | 7.0.x | 8.x
2.5 | 2.1 | 5 and later | 6.0.x | 7.x



### API升级，导致Dto字段更新为null
比如版本1.1在Dto中新家字段abc；Dto作为Mybatis参数；此时1.0版本调用该接口，旧属性Dto并无该参数，反序列化导致该参数为null，并不会采用表默认值，值永远为空；
- 解决：1、升级API版本；2、biz做判断；3、api和mybatis不采用对象做入参，基础参数做入参；4、写新方法单独更新字段；

### filter改变request参数

```
页面
 <html:form action="user.do">
   <input type="text"  name="uname" >-----------------输入zhangsan
   <html:submit value="submit"></html:submit>
   </html:form>

过滤器
request.setAttribute("uname", "lisi");
filterChain.doFilter(request, response);

action

System.out.println(request.getAttribute("uname"));----------------输出 lisi
```


#### LinkedBlockingQueue的poll的CPU被占满的问题
循环线程while(true)，内部使用poll会导致CPU占满， 改用take即可，或手动sleep

#### Freemarker之，Map里面嵌套Map，子Map获取不到的问题
```
// 后端代码
Map<String, Map<String, String>> test = new HashMap<>();

Map<String, String> map21 = new HashMap<>();
map21.put("aaa", "111");
Map<String, String> map22 = new HashMap<>();
map22.put("aaa", "222");

test.put("map1", map21);
test.put("map2", map22);

model.addAttribute("test", test);

// 模板代码
<#list test?keys as key>
    <#assign map2 = test[key?string] />

    <#list map2?keys as key2 >
        ${key2}=${map2[key2?string]}        <#-- ${map2["aaa"]} -->
    </#list>    <br>

</#list>
```


#### MessageFormat.format占位符，数字千分位分隔符的问题
```
// 错误，结果为：num=15,992,879
MessageFormat.format("num={0}", 15992879);
// 正确，结果为：num=15992879
MessageFormat.format("num={0}", String.valueOf(15992879));
```

#### Mybatis中Mysql，表字段和系统关键字冲突
解决方法：把字段用 “ `”（引用号）包围起来；

#### 后端分页，分组
```
// 分页分组
int pagesize = 6;
int pageNum = (finalOfficialAlbumList.size() % pagesize == 0) ? (finalOfficialAlbumList.size()/pagesize) : (finalOfficialAlbumList.size()/pagesize +1) ;

// 分页,分组
List<OfficialAlbumDTO>[] pageRecordArr = new List[pageNum];
for(int i=0; i<pageNum; ++i) {
	List<OfficialAlbumDTO> pageRecord = new ArrayList<OfficialAlbumDTO>();
	for(int j=0; j<6; ++j){
		int index = i*6+j;
		if(index >= finalOfficialAlbumList.size()) {
			break;
		}
		pageRecord.add(finalOfficialAlbumList.get(index))
	}
	pageRecordArr[i] = pageRecord;
}
```


#### LOGGER.error(e.getCause().getMessage(), e);报错
因为e.getCause()为空，导致空指针异常；

##### static final和final static区别
编译上无区别，static final更推荐，sonar推荐阅读性更好

##### freemarker配置文件之number_format
freemarker配置文件中number_format属性，仅在渲染数据时格式化数字时生效，并不会更改数字的实际数值。
例如：temp1=1.1和temp2=1.2，在number_format设置为“#”时，显示为同样的值为1，但是实质上值未变，${temp1 == temp2} 时不一致。
如分页逻辑中，<#assign page=total/pagesize />，获取的页数实际上为小数，如需取整需要做以下处理“?floor”或“?int”

##### TimeUnit.SECONDS.sleep优于Thread.sleep
提供更好的 可读性；（TimeUnit是枚举实现一个很好的实例）

##### Readme.MD文件：markdown 语法和工具
![image](http://images.cnitblog.com/i/46653/201406/211438200988939.png)


##### 开源许可证比较BSD、Apache、GPL、MIT 
![image](http://image.beekka.com/blog/201105/free_software_licenses.png)

##### properties文件加载问题
```
String prop = "year.config.properties";
Properties properties = new Properties();

// 加载路径无区别
//ClassLoader loder = Configuration.class.getClassLoader();
ClassLoader loder = Thread.currentThread().getContextClassLoader();

---每次执行，都会加载最新文件
URL url = loder.getResource(prop);
InputStream ins = new FileInputStream(url.getPath());
//InputStream ins = new BufferedInputStream(new FileInputStream(url.getPath());

---第一次加载之后，会将文件缓存在内存中；JVM重启，才会刷新
InputStream ins2 = loder.getResourceAsStream("/" + prop);

properties.load(ins);
```


##### @Value注解（Spring提供）方式加载配置
利用Spring的@Value注解，可以获取Spring容器中以PropertyPlaceholderConfigurer方式加载<value>classpath*:jdbc.properties</value>的配置信息。
```
@Value("${mail.sendFrom}")
 private String sendFrom;
```

##### Apache Common包
* 日期、字符转，转换
```
FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date())
DateUtils.parseDate("2015-10-09 16:51:07", new String[]{"yyyy-MM-dd HH:mm:ss"})
DateUtils.isSameDay(addTime, DateUtils.addDays(new Date(), -1))
if(addCal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {}
```

* 对象clone
```
SerializationUtils.clone
```

* 集合操作：
```
// 仅仅针对，非空字段才有效（切记，蛋疼）
StringUtils.isNumeric("")     = true

/**
    集合判断： 
    例1: 判断集合是否为空:
*/
CollectionUtils.isEmpty(null): true
CollectionUtils.isEmpty(new ArrayList()): true
CollectionUtils.isEmpty({a,b}): false

// 例2: 判断集合是否不为空:
CollectionUtils.isNotEmpty(null): false
CollectionUtils.isNotEmpty(new ArrayList()): false
CollectionUtils.isNotEmpty({a,b}): true

/**
    2个集合间的操作： 
    集合a: {1,2,3,3,4,5}
    集合b: {3,4,4,5,6,7}
*/
CollectionUtils.union(a, b)(并集):  // {1,2,3,3,4,4,5,6,7}
CollectionUtils.intersection(a, b)(交集):   // {3,4,5}
CollectionUtils.disjunction(a, b)(交集的补集):  // {1,2,3,4,6,7}
CollectionUtils.disjunction(b, a)(交集的补集):  // {1,2,3,4,6,7}
CollectionUtils.subtract(a, b)(A与B的差):   // {1,2,3}
CollectionUtils.subtract(b, a)(B与A的差):   // {4,6,7}

// 抽取列表对象属性
List<Integer> fieldList = (List<Integer>) CollectionUtils.collect(voList, new Transformer() {
    @Override
    public Object transform(Object input) {
        Vo vo = (Vo) input;
        return vo.getId();
    }
});
```

* 推荐参考一下common-lang的api
```
StringUtils.isBlank(temp);
Assert.notEmpty
...
Arrays.asList    :    []  》 List
```

##### Tomcat进程退出
```
System.exit(0);    // 项目中出现这行，tomcat直接shutdown
```

##### List和Set的contains区别    
> ArrayList的contains速度比较慢，遍历调用equals，标准为：equals返回true；
HashSet比较块，利用hashCode定位哈希表，然后equals比较，标准为：hashcode定位成功，且equals校验返回true

##### 通用toString
```
@Override
 public String toString() {
  return ToStringBuilder.reflectionToString(this,
    ToStringStyle.SHORT_PREFIX_STYLE);
 }
```

##### JAVA中创建对象的四种方式
* CreateObj s1 = new CreateObj();     // 1.第一种常用方式 
* CreateObj s2 = (CreateObj) Class.forName( "com.newland.commons.collectionutil.CreateObj").newInstance();    // 2.第二种方式 静态方式 
* ObjectOutputStream objectOutputStream = new ObjectOutputStream( new FileOutputStream(filename));     //第三种方式 用对象流来实现 前提是对象必须实现 Serializable 
* CreateObj s4= (CreateObj) obj.clone();    //第四种 clone 必须 实现Cloneable接口 否则抛出CloneNotSupportedException
 
##### hash code
hash code是一种编码方式，在Java中，每个对象都会有一个hashcode，Java可以通过这个hashcode来识别一个对象。

至于hashcode的具体编码方式，比较复杂（事实上这个编码是可以由程序员通过继承和接口的实现重写的），可以参考数据结构书籍。

而hashtable等结构，就是通过这个哈希实现快速查找键对象。

这是他们的内部联系，但一般编程时无需了解这些，只要知道hashtable实现了一种无顺序的元素排列就可以了。

两个对象值相同(x.equals(y) == true)，则一定有相同的hash code。

因为：Hash，一般翻译做“散列”，也有直接音译为"哈希"的，就是把任意长度的输入（又叫做预映射， pre-image），通过散列算法，变换成固定长度的输出，该输出就是散列值。这种转换是一种压缩映射，也就是，散列值的空间通常远小于输入的空间，不同的输入可能会散列成相同的输出，而不可能从散列值来唯一的确定输入值。

以下是java语言的定义：
* 1) 对象相等则hashCode一定相等；
* 2) hashCode相等对象未必相等。

这也涉及到如何写自定义的hashCode方法的问题：必须符合以上条件。注意条件2中的未必。
具体可参见java doc; Effective Java中有更详细论述。

补充一点个人简介 hash 就是 类似于数学集合，
每一个键，k可以对应一个或多个值，对象就类似于值，所以“相同的对象”具有相同的键值，也就是hashCode;


##### String +“”、StringBuffer、StringBuilder 区别
* String +“”：操作多个String对象，消耗较大； 
* StringBuffer：线程安全，只操作一个变量；
* StringBuilder：非线程安全，只操作一个变量
 
##### break,continue,return的区别
* break ：直接跳出当前的循环，从当前循环外面开始执行,忽略循环体中任何其他语句和循环条件测试。他只能跳出一层循环，如果你的循环是嵌套循环，那么你需要按照你嵌套的层次，逐步使用break来跳出.
* continue：也是终止当前的循环过程，但他并不跳出循环,而是继续往下判断循环条件执行语句.他只能结束循环中的一次过程,但不能终止循环继续进行.      
* return：语句可被用来使 正在执行分支程序返回到调用它方法。(费解)
 
##### Regex.Split额外处理：
* Regex.Split 在分割 "|" 符号时出现问题,应该使用 "[|]" 才可以分组
* Regex.Split 在分割“ . ” 符号时出现问题,应该使用"/." 才可以分组
 
##### 用Java求两时间点之间日期差的简洁方法
```
return date2.getTime() / 86400000 - date1.getTime() / 86400000;  // 用立即数，减少乘法计算的开销
```

##### 时间格式化，12小制和24小时制度，区别
```
12小时制：hh:mm:ss；24小时制：HH:mm:ss
```

##### 简单数据类型


数据类型 | 数据类型名称 | 大小（bits） | 默认值
---|---|---|---
boolean |	布尔类型	    |   1	|   false
char    |   字符型	        |   16	|   0
byte    |   字节型	        |   8	|   0
short   |   短整型	        |   16	|   0
int     |   整型	        |   32	|   0
long	|   长整型	        |   64	|   0
float	|   单精度浮点型	|   32	|   0.0
double	|   双精度浮点型	|   64	|   0.0

##### Spring Assert(方法入参检测工具类-断言)

```
public InputStream getData(String file) { 
    if (file == null || file.length() == 0|| file.replaceAll("\\s", "").length() == 0) { 
        throw new IllegalArgumentException("file入参不是有效的文件地址"); 
    } 
    … 
} 
使用 Assert 断言类可以简化方法入参检测的代码，如 InputStream getData(String file) 在应用 Assert 断言类后，其代码可以简化为以下的形式： 
public InputStream getData(String file){ 
    Assert.hasText(file,"file入参不是有效的文件地址"); 
    ① 使用 Spring 断言类进行方法入参检测 
    … 
}

```

##### 参数--传递引用

```
public class HbaseTest {
    private static String const_1 = "1111";
    private static String const_2 = "2222";
    public static void main(String[] args){
        // 创建两个东西：引用ref1	--------->  常量1：const_1
        String ref1 = const_1;
        myTest(ref1);
        // 打印，实际上答应的是ref1
        System.out.println(ref1);
    }
    static void myTest(String ref2) {
        // 创建一个东西：引用ref2 -------------> 常量1：const_1
        // 修改ref2的引用：ref2 --------------> 常量2：const_2
        ref2 = const_2;
    }
}
执行结果：11111
```

##### 包装类型，Long比较相等
```
new Long(32).equals(new Long(32));   // true  
new Long(32) == (new Long(32));      // false
```

##### classpath 和 classpath* 区别
* classpath：只会到你的class路径中查找找文件;
* classpath*：不仅包含class路径，还包括jar文件中(class路径)进行查找.

##### final类型的Map 能在回调函数里put数据  为啥 final类型的String 不能修改 
final申明的变量只是保证不能被重新赋值，至于变量所代表的对象的内容是可以改变的！map的put是改对象的内容，String修改是查询赋值了

##### Java中List的排序

```
第一种方法，就是list中对象实现Comparable接口：
public class Person implements Comparable<Person> { 
    private String name;
    private Integer order;
    // ... set get
    @Override 
    public int compareTo(Person arg0) {
        return this.getOrder().compareTo(arg0.getOrder());
    }
}
使用：
List<Person> listA = new ArrayList<Person>();
// ... add1 ... add2 ... add3
Collections.sort(listA); 
第二种方法，就是在重载Collections.sort方法，代码如下： 
public class Person { 
    private String name;
    private Integer order;
    // ... set get
}
使用：
List<Person> listA = new ArrayList<Person>();
// ... add1 ... add2 ... add3
Collections.sort(listA, new Comparator<Person>() {
    public int compare(Person arg0, Person arg1) {
        return arg0.getOrder().compareTo(arg1.getOrder());
    }
});
```

##### ArrayList集合clone
```
ArrayList a = new ArrayList();
ArrayList b = new ArrayList();
b=a; // 不行，这样只是复制一个pointer。
b=a是将b的地址值指向a,而b原先的对象会被垃圾回收。

但是在这里我想告诉你的是集合之间的复制方法： 
方法一：ArrayList<Integer>  b= new ArrayList<Integer>(a);//利用集合自带的构造方法

方法二：ArrayList<Integer> b =(ArrayList<Integer>) a.clone();//利用克隆的方法进行赋值
```

##### 字符串模板替换：

```
System.out.println(String.format("lexical error at position %s, encountered %s, expected %s ", 123, 100, 456));  
System.out.println(MessageFormat.format("lexical error at position {0}, encountered {1}, expected {2}", new Date(), 100, 456));  

tips：String.format转换符：字符串%s、字符%c、布尔%b、整数类型（十进制）%d、浮点%f、换行符%n、等等。

tips：MessageFormat的api占位符参数功能更加强大点，支持type，style等限定。如果需要使用高级功能建议是使用MessageFormat。
```

##### 位与运算（两个数组，求所有组合）


```
package com.xxl.util.core.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、位与运算（两个数组，求所有组合）
 * 2、用与计算方法，给定数字数组，求相加得到指定结果的一种组合方案
 * at POOK
 * @author xuxueli 2016-6-30 19:37:55
 */
public class AndCalculate {
	public static void main(String[] args) {
		int[] array = { 3, 3, 3, 1, 7 };
		List<Integer> indexs = strategy(array, 9);
		if (indexs == null || indexs.size() == 0) {
			System.out.println("not suitable found...");
		} else {
			for (Integer index : indexs) {
				System.out.print(array[index] + " ");
			}
			System.out.println("");
		}
	}

	/**
	 * @param array		组合元素
	 * @param expected	期望相加后等于的值
	 * @return
	 */
	public static List<Integer> strategy(int[] array, int expected) {
		List<Integer> indexs = new ArrayList<Integer>();

		// 从1循环到2^N
		for (int i = 1; i < 1 << array.length; i++) {
			int sum = 0;
			indexs = new ArrayList<Integer>();

			for (int j = 0; j < array.length; j++) {

				// 用i与2^j进行位与运算，若结果不为0,则表示第j位不为0,从数组中取出第j个数
				if ((i & 1 << j) != 0) {
					sum += array[j];
					indexs.add(j);
				}
			}
			if (sum == expected) {
				break;
			} else {
				indexs = null;
			}
		}

		return indexs;
	}

}
```
