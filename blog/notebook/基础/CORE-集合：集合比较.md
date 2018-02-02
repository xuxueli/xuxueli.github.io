### java的集合就那么几种 总体为：List，Set，Map (都是接口由其子类去实现具体的方法)

* 1、 Map（键值对，键唯一）

    **HashMap**：hash表数据接口
    * 1、非synchronized，可以通过Collections.synchronizedMap(hashMap)实现synchronized，返回一个封装了底层方法的因此同步的Map；
    * 2、key或valye都可接收null，因此无key或key为null，则get()可能返回null，必须使用containsKey判断是否存在key；
    * 3、只适用单线程，效率高；

    **TreeMap**：二叉树数据结构
    * 1、非synchronized；
    * 2、key不可接收null，value可接收null；
    * 3、可按照key进行排序；
    * 4、只使用单线程；

    **HashTable**：hash表数据接口
    * 1、synchronized，每次读写，都会锁住整个结构；
    * 2、key或valye都不可接收null；
    * 3、多线程，效率较低；

    **ConcurrentHashMap**：key-value
    * 1、synchronized，将hash表默认拆分16个桶，每次get、put、remove操作只锁当前桶。写线程锁粒度细，读线程几乎不用锁，读写分离，速度快，只有size才会锁住整张hash表；
    * * 2、key或valye都不可接收null；
    * 3、多线程，效率相对高；

* 2、Set（value，无序，不可重复，非synchronized）

    * **HashSet**：非synchronized，哈希表数据结构，根据hashCode和equals方法来确定元素的唯一性；
    * **TreeSet**：非synchronized，二叉树数据结构，可按照元素排序，默认自然循序，也可使用Comparable自定义排序；

* 3、List（value，有顺序，可重复，因为每个元素有单独索引）
    
    * **ArrayList**：非synchronized，数组数据结构；查询很快，增/删稍微慢点；
    * **LinkedList**：synchronized，数组数据结构，FIFO；
    * **Vector**：非synchronized，链表数据结构；查询慢，增/删很快；