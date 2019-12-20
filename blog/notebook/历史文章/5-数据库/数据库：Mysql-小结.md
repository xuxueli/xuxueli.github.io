### distance 慢查询
explain 语句，看extra属性：
Using where; Using temporary; Using filesort
1、Using temporary：临时表，避免；
2、Using filesort：文件排序，避免；

### Mybatis报错：SQLErrorCodes loaded: [DB2, Derby, H2, HSQL, Informix, MS-SQL, MySQL, Oracle, PostgreSQL, Sybase]
原因：Mybatis的XML文件中，SQL语句加了个分号“;”，去掉即可；

### MYSQL中的普通索引,主健,唯一,全文索引区别

MYSQL索引用来快速地寻找那些具有特定值的记录，所有MySQL索引都以B-树的形式保存。如果没有索引，执行查询时MySQL必须从第一个记录开始扫描整个表的所有记录，直至找到符合要求的记录。表里面的记录数量越多，这个操作的代价就越高。如果作为搜索条件的列上已经创建了索引，MySQL无需扫描任何记录即可迅速得到目标记录所在的位置。如果表有1000个记录，通过索引查找记录至少要比顺序扫描记录快100倍。


- MYSQL主键 （PRIMARY 主键。 就是 唯一 且 不能为空）：

MYSQL主键是一种唯一性索引，但它必须指定为“PRIMARY KEY”。
例如“CREATE TABLE tablename ( [...], PRIMARY KEY (列的列表) ); ”。但是，我们也可以通过修改表的方式加入主键，例如“ALTER TABLE tablename ADD PRIMARY KEY (列的列表); ”。每个表只能有一个主键。

- 普通索引（INDEX 索引，普通的）

这是最基本的索引类型，而且它没有唯一性之类的限制。
创建索引，例如CREATE INDEX <索引的名字> ON tablename (列的列表);
修改表，例如ALTER TABLE tablename ADD INDEX [索引的名字] (列的列表);
创建表的时候指定索引，例如CREATE TABLE tablename ( [...], INDEX [索引的名字] (列的列表) );

- 唯一性索引（UNIQUE 唯一索引。 不允许有重复。）

这种索引和前面的“普通索引”基本相同，但有一个区别：索引列的所有值都只能出现一次，即必须唯一。

- 全文索引 （FULLTEXT 是全文索引，用于在一篇文章中，检索文本信息的。）

MySQL从3.23.23版开始支持全文索引和全文检索。在MySQL中，全文索引的索引类型为FULLTEXT。全文索引可以在VARCHAR或者TEXT类型的列上创建。它可以通过CREATE TABLE命令创建，也可以通过ALTER TABLE或CREATE INDEX命令创建。对于大规模的数据集，通过ALTER TABLE（或者CREATE INDEX）命令创建全文索引要比把记录插入带有全文索引的空表更快。

### BDB中索引算法的选择：Hash vs BTree

小数据量，btree是很优秀的；在更大的时候，由于元数据占用太多cache的原因，导致性能下降，落后与hash了，而不是说hash能超过它；所以能在元数据占用cache不是太多以前，也就是你的cache足够大，使用btree只最好的选择。

当然，如果每次访问的数据都是随机的没有什么次序，也不是near的，那用btree也没什么优势了。


### in和exists
in是把外表和内表作hash连接，而exists是对外表作loop循环，每次loop循环再对内表进行查询，一直以来认为exists比in效率高的说法是不准确的。

如果查询的两个表大小相当，那么用in和exists差别不大；

如果两个表中一个较小一个较大，则子查询表大的用exists，子查询表小的用in；

### timestamp的两个属性
- CURRENT_TIMESTAMP：当要向数据库执行insert操作时，如果有个timestamp字段属性设为 CURRENT_TIMESTAMP；
- ON UPDATE CURRENT_TIMESTAMP：当执行update操作是，并且字段有ON UPDATE CURRENT_TIMESTAMP属性。

### MySQL复制表数据到新表
MySQL复制表结构及数据到新表： 
```
CREATE TABLE 新表 SELECT * FROM 旧表
```

只复制表结构到新表：
```
CREATE TABLE 新表 SELECT * FROM 旧表 WHERE 1=2
```

复制旧表的数据到新表(假设两个表结构一样)：
```
INSERT INTO 新表 SELECT * FROM 旧表
```

复制旧表的数据到新表(假设两个表结构不一样)
```
INSERT INTO 新表(字段1,字段2,…….) SELECT 字段1,字段2,…… FROM 旧表
```

### This version of MySQL doesn't yet support 'LIMIT & IN/ALL/ANY/SOME subquery

- 错误：select * from table where id in (select id from table limit 5, 10)
- 正确：select * from table where id in (select t.id from (select * from table limit 5, 10)as t) 


### DATE_ADD()

    函数向日期添加指定的时间间隔
    语法：DATE_ADD(date,INTERVAL expr type)
    date 参数是合法的日期表达式。expr 参数是您希望添加的时间间隔
    type：SECOND/MINUTE/HOUR/DAY/MONTH/YEAR/
    http://www.w3school.com.cn/sql/func_date_add.asp
    例如：DATE_ADD(NOW(),INTERVAL -1 HOUR)

### sysdate()和now()
- sysdate() 返回的是sysdate()函数被调用时的时间
- now()返回的是整条sql语句开始执行时的时间

### mysql 语句case when
```
select case when 1>0 then 'yes' else 'no' end
```

### Mysql coalesce() 函数
    
    coalesce()解释：返回参数中的第一个非空表达式（从左向右）：
    鉴于在mysql中没有nvl()函数, 我们用coalesce()来代替。coalesce相比nvl优点是，coalesce中参数可以有多个，而nvl()中参数就只有两个。当然，在oracle中也可以使用 case when....then....else......end（比较强悍，case when 后可以跟表达式）。
    select coalesce(a,b,c); 
    如果a==null,则选择b；如果b==null,则选择c；如果a!=null,则选择a；如果a b c 都为null ，则返回为null（没意义）。

### Mysql JDBC驱动版本与Mysql版本的对应问题
    有问题，到官网上找答案，其次才是百度：
    Connector/J 5.1 支持Mysql 4.1、Mysql 5.0、Mysql 5.1、Mysql 6.0 alpha这些版本。
    Connector/J 5.0 支持MySQL 4.1、MySQL 5.0 servers、distributed transaction (XA)。
    Connector/J 3.1 支持MySQL 4.1、MySQL 5.0 servers、MySQL 5.0 except distributed transaction (XA) support。
    Connector/J 3.0 支持MySQL 3.x or MySQL 4.1。

### maven依赖：

    <!-- mysql-connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.29</version>
    </dependency>


### 会员嵌套查询
```
// 会员嵌套查询
表：user(id,username,password,upmark)

a、至少有10个儿子；	11 = 1+10
-------------------------
---
select upmark 
from user 
group by upmark 
having count(username)>=10

---
select * 
from user 
where username in (a:username )

b、至少有10个儿子满足a；	111 = 1+10+100
----------------------------
---
select upmark 
from user 
where username in (
    select upmark 
    from user 
    group by upmark 
    having count(username)>=10
) 
group by upmark 
having count(username)>=10
----
select * 
from user 
where username in (
    select upmark 
    from user 
    where username in (
 	    select upmark 
 	    from user 
 	    group by upmark 
 	    having count(username)>=10
	) group by upmark 
	having count(username)>=10
)

c:username
------------------------------------
---
select upmark 
from user 
where username in (
    select upmark from 
    user where 
        username in (
      	    select upmark 
      	    from user 
      	    group by upmark 
      	    having count(username)>=10
        ) 
        group by upmark 
        having count(username)>=10
) 
group by upmark 
having count(username)>=10

---
select * 
from user 
where username in (c:username )

c:*
------------------------------------
select * from user where username in(
    select upmark 
    from user 
    where username in (
  		select upmark 
  		from user 
  		where username in (
  			select upmark 
  			from user 
  			group by upmark 
  			having count(username)>=10
  		) 
  		group by upmark 
  		having count(username)>=10
	) 
	group by upmark 
	having count(username)>=10
)
```
