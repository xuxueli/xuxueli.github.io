##### 
- 1.表空间:是一个或多个数据文件的集合,主要存放的是表,所有的数据对象都存放在指定的表空间中;一个数据文件只能属于一个表空间,一个数据库空间由若干个表空间组成；
- 2.分区表:当表中的数据量不断增大，查询数据的速度会变慢,应用程序的性能就会下降,这时就应该考虑对表分区;表进行分区后逻辑上还是一张完整的表，只是把表中的数据存放到不同的表空间(物理文件上),这样查询就不用扫描整张表。

优点:
    
     (1)改善查询性能:对分区对象的查询可以仅搜索自己关心的分区,提高查询效率。
     (2)增强可用性:如果表的某个分区出现故障，表在其他分区的数据仍然可用。
     (3)维护方便:如果表的某个分区出现故障，需要修复数据，只修复该分区即可。
     (4)均衡I/O:可以把不同的分区映射到磁盘以平衡I/O，改善整个系统性能。
     
缺点:已经存在的表没有方法可以直接转化为分区表。

[表空间和表分区](http://blog.csdn.net/pzasdq/article/details/49618165)

##### Rank()函数使用说明：
返回结果集分区内指定字段的值的排名，指定字段的值的排名是相关行之前的排名加一。
```
RANK() OVER([<partiton_by_clause>]<order by clause>)

partition_by_clause：将from子句生成的结果集划分为应用到RANK函数的分区。
Order_by_clause：确定将RANK值应用到分区中的行时所使用的顺序。

例如：按照课程对学生的成绩进行排序
select name,
    course,
    rank() over(partition by course order by score desc) as rank
    from student;
```

[OVER(PARTITION BY)函数用法](http://www.cnblogs.com/lanzi/archive/2010/10/26/1861338.html)

[rank() over(partition)的使用](http://www.cnblogs.com/wingsless/archive/2012/02/04/2338292.html)

[Oracle中rank() over, dense_rank(), row_number() 的区别](http://www.linuxidc.com/Linux/2015-04/116349.htm)


##### bitand() 函数
bitand() 函数：指定按位进行 AND 运算的两个数值。如果nExpression1和 nExpression2为非整数型，那么它们在按位进行 AND 运算之前转换为整数。 
```
bitand ，按位与操作。
select bitand(0,0) from dual --0
select bitand(1,0) from dual --0
select bitand(0,1) from dual --0
select bitand(1,1) from dual --1
select bitand(5,6) from dual --4（0101和0110位于的结果）
```

##### 时间函数
```
加法
select sysdate,add_months(sysdate,12) from dual; --加1年
select sysdate,add_months(sysdate,1) from dual; --加1月
select sysdate,to_char(sysdate+7,'yyyy-mm-dd HH24:MI:SS') from dual; --加1星期
select sysdate,to_char(sysdate+1,'yyyy-mm-dd HH24:MI:SS') from dual; --加1天
select sysdate,to_char(sysdate+1/24,'yyyy-mm-dd HH24:MI:SS') from dual; --加1小时
select sysdate,to_char(sysdate+1/24/60,'yyyy-mm-dd HH24:MI:SS') from dual; --加1分钟
select sysdate,to_char(sysdate+1/24/60/60,'yyyy-mm-dd HH24:MI:SS') from dual; --加1秒

减法
select sysdate,add_months(sysdate,-12) from dual; --减1年
select sysdate,add_months(sysdate,-1) from dual; --减1月
select sysdate,to_char(sysdate-7,'yyyy-mm-dd HH24:MI:SS') from dual; --减1星期
select sysdate,to_char(sysdate-1,'yyyy-mm-dd HH24:MI:SS') from dual; --减1天
select sysdate,to_char(sysdate-1/24,'yyyy-mm-dd HH24:MI:SS') from dual; --减1小时
select sysdate,to_char(sysdate-1/24/60,'yyyy-mm-dd HH24:MI:SS') from dual; --减1分钟
select sysdate,to_char(sysdate-1/24/60/60,'yyyy-mm-dd HH24:MI:SS') from dual; --减1秒

ORACLE时间函数(SYSDATE)简析
1:取得当前日期是本月的第几周
SQL> select to_char(sysdate,'YYYYMMDD W HH24:MI:SS') from dual;
TO_CHAR(SYSDATE,'YY
-------------------
20030327 4 18:16:09
SQL> select to_char(sysdate,'W') from dual;
T
-
4
2:取得当前日期是一个星期中的第几天,注意星期日是第一天
SQL> select sysdate,to_char(sysdate,'D') from dual;
SYSDATE T
--------- -
27-MAR-03 5
　　类似:
select to_char(sysdate,'yyyy') from dual; --年
select to_char(sysdate,'Q' from dual; --季
select to_char(sysdate,'mm') from dual; --月
select to_char(sysdate,'dd') from dual; --日
ddd 年中的第几天
WW 年中的第几个星期
W 该月中第几个星期
D 周中的星期几
hh 小时(12)
hh24 小时(24)
Mi 分
ss 秒
3:取当前日期是星期几中文显示:
SQL> select to_char(sysdate,'day') from dual;
TO_CHAR(SYSDATE,'DAY')
----------------------

星期四
4:如果一个表在一个date类型的字段上面建立了索引，如何使用
alter session set NLS_DATE_FORMAT='YYYY-MM-DD HH24:MI:SS'

5: 得到当前的日期
select sysdate from dual;
6: 得到当天凌晨0点0分0秒的日期
select trunc(sysdate) from dual;
-- 得到这天的最后一秒
select trunc(sysdate) + 0.99999 from dual;
-- 得到小时的具体数值
select trunc(sysdate) + 1/24 from dual;
select trunc(sysdate) + 7/24 from dual;
7.得到明天凌晨0点0分0秒的日期
select trunc(sysdate+1) from dual;
select trunc(sysdate)+1 from dual;
8: 本月一日的日期
select trunc(sysdate,'mm') from dual;
9:得到下月一日的日期
select trunc(add_months(sysdate,1),'mm') from dual;

10:返回当前月的最后一天?
select last_day(sysdate) from dual;
select last_day(trunc(sysdate)) from dual;
select trunc(last_day(sysdate)) from dual;
select trunc(add_months(sysdate,1),'mm') - 1 from dual;
11: 得到一年的每一天
select trunc(sysdate,'yyyy')+ rn -1 date0
from
(select rownum rn from all_objects
where rownum<366);
12:今天是今年的第N天
SELECT TO_CHAR(SYSDATE,'DDD') FROM DUAL;
13:如何在给现有的日期加上2年
select add_months(sysdate,24) from dual;
14:判断某一日子所在年分是否为润年
select decode(to_char(last_day(trunc(sysdate,'y')+31),'dd'),'29','闰年','平年') from dual;
15:判断两年后是否为润年
select decode(to_char(last_day(trunc(add_months(sysdate,24),'y')+31),'dd'),'29','闰年','平年') from dual;
16:得到日期的季度
select ceil(to_number(to_char(sysdate,'mm'))/3) from dual;
select to_char(sysdate, 'Q') from dual; 
```


##### 星期 算法
- next_day(date,'day')：指定时间的下一个星期几（由day指定）（周日 x=1 周一 x=2 周二 x=3 ...周六=7；周日为一周开始）所在的日期；
- trunc：按照指定的精度进行舍入，注意这个函数是直接截断，和round函数（四舍五入）有区别


计算某周周x的起始日期：

    周一：trunc(next_day(sysdate - 8, 1) + 1)
    周二：trunc(next_day(sysdate - 8, 1) + 2)
    周三：trunc(next_day(sysdate - 8, 1) + 3)
    周四：trunc(next_day(sysdate - 8, 1) + 4)
    周五：trunc(next_day(sysdate - 8, 1) + 5)
    周六：trunc(next_day(sysdate - 8, 1) + 6)
    周日：trunc(next_day(sysdate - 8, 1) + 7)

    之前求周一是trunc(next_day(sysdate - 7, 1) + 1) 这个方法无法避免周日这一天计算错误(周日临界问题)。因此修改为trunc(next_day(sysdate - 8, 1) + 1)，这样就可以按照中国人的习惯 周一~周日 都算本周。

统计上周成功订单总数：
```
select count(*) from orders o 
where o.gmt_create>=trunc(next_day(sysdate -8, 1)-6)
    and o.gmt_create<trunc(next_day(sysdate - 8, 1)+1)
    and o.status=5
```

##### 随机记录查询
```
// 1. Oracle,随机查询10条
select * from( select * from 表名 order by dbms_random.value)
where rownum <= 10;

// 2.SQL Server，随机查询10条
select top 10 * from 表名order by newid()

// 3.My SQL:，随机查询10条
select * from 表名 order by rand() limit 10

```


##### 格式化日期
```
格式化日期插入oracle数据库（保存为date）：
insert into t_student(stu_birthday, stu_cls_id, stu_id, stu_name, stu_sal, stu_sex)
  values(to_date('2007-06-12 14:14:14', 'yyyy-mm-dd hh24:mi:ss') ,0310401,031040103,'李立2',5000,'男')

格式化日期输出oracle数据库（输出为char）：
select to_char(stu_birthday,'yyyy-mm-dd hh24:mi:ss'), stu_cls_id, stu_id, stu_name, stu_sal, stu_sex from t_student
 where stu_name like '李立%'


格式化日期输出oracle数据库（输出为date）：
select stu_birthday, stu_cls_id, stu_id, stu_name, stu_sal, stu_sex from t_student where stu_name like '李立%'
```

##### oracleXE

    登陆：conn system/123456
    查询实例：select instance_name from v$instance;  (不要忘记末尾分号)   xe
    
    操作数据库之后，需要commit，才能查询到！
    退出自动commit

##### 分页：
```
select * from 
(
    select a1.*,rownum rn 
    from  (
        select * from emp
    ) a1
    where rownum<=10
) where rn >=6 ;


rowid是不变的 一行记录拥有一个固定的rowid  和数据库底层的存储和读取是相关的
rownum是本次查询结果的次序
```

##### 数据类型
CLOB: 字符大对象Clob 用来存储单字节的字符数据
BLOB: 用于存储二进制数据


##### 性能

1. 绑定变量 == 动态sql：
    - 1、SQL语句硬分析(Hard Parse)太多，严重消耗CPU资源，延长了SQL语句总的执行时间。
    - 2、共享池中SQL语句数量太多，重用性极低，加速了SQL语句的老化，导致共享池碎片过多。
2. WHERE条件中使用TRUNC(时间字段)非常影响效率：
    **不要对时间字段进行函数处理，非常慢** 
```
// 低能--每提取一条记录都要对时间字段进行函数处理才能确定是否合适  ,一般耗时4-5秒。 
SELECT COUNT(1) FROM A_BASIC_CS_RADIO_H  WHERE TRUNC(COLLECTTIME) = TRUNC(SYSDATE) - 1

// 高效--直接可以判断是否合适,一般耗时0.05秒。差别太大了。
SELECT COUNT(1) FROM A_BASIC_CS_RADIO_H WHERE COLLECTTIME BETWEEN TRUNC(SYSDATE) - 1 AND TRUNC(SYSDATE) - 1 + 23 / 24

```
3. where exists更高效：

    低效：where aa = bb             
    更高效：where exists ( select 1 from ******)

##### maven依赖：
```
<dependency>
    <groupid>com.oracle</groupid>
    <artifactid>ojdbc6</artifactid>
    <version>11.2.0.1.0</version>
</dependency>

ojdbc14.jar      jdk1.4
ojdbc5.jar        jdk1.5
ojdbc6.jar        jdk1.6（jdk1.5及以下，就不可用）

```

##### 处理 Oracle SQL in 超过1000 的解决方案
处理oracle sql 语句in子句中**（where id in (1, 2, ..., 1000, 1001)）**，如果子句中超过1000项就会报错。
这主要是oracle考虑性能问题做的限制。如果要解决次问题，可以用 where id (1, 2, ..., 1000) or id (1001, ...)


##### 表名小写引号
表名小写，并且用双引号括起来"user_id"，查询时也要用小写，不用括号，则默认大写；

##### 序列，插入数据：
```
insert into MAC_RETENTION VALUES 
    (mac_retention_seq.nextval, null,100, 3, 5);
```

##### to_date
```
to_date('2013-11-12 15:01:53', 'yyyy-MM-dd HH24:mi:ss') 

update exchange_money set log_time = to_date('2013-11-12 15:01:53', 'yyyy-MM-dd HH24:mi:ss') where user_name = 'xuxueli'
```

##### NVL( string1, replace_with)
功能：如果string1为NULL，则NVL函数返回replace_with的值，否则返回string1的值，如果两个参数都为NULL ，则返回NULL。

注意事项：string1和replace_with必须为同一数据类型，除非显式的使用TO_CHAR函数进行类型转换。

##### CASE WHEN
```
select pic_id,type,
    (case    when type=4 then '四'
                 when type=20 then '二十'
                 else '其他'
　end) "类型"
from pictures
-----
select * from ord_invoice
       where 1=(case when 'null' is not null and 'null' = deliver_status then 1
                                when 'null' is null then 1
                       end) 
```

##### 错误：order字段        
系统字段，最好不冲突

##### Oracle链接数据库：
```
package com.xxl.test;
import java.sql.Connection;    
import java.sql.DriverManager;    
import java.sql.ResultSet;    
import java.sql.Statement;    
   
//演示  如何使用 jdbc_odbc 桥连接方式    
public class OracleJdbc2 {    
   
    public static void main(String[] args) {    
        try {    
   
            // 1.加载驱动    
Class.forName("oracle.jdbc.driver.OracleDriver");
// new oracle.jdbc.driver.OracleDriver();
            // 2.得到连接      
            Connection ct = DriverManager.getConnection(
            "jdbc:oracle:thin:@127.0.0.1:1521:myoracle", "scott",
            "123456");
   
            // 从下面开始，和 SQL Server 一模一样    
            Statement sm = ct.createStatement();    
            ResultSet rs = sm.executeQuery("select * from emp");
  
            while (rs.next()) {    
                //用户名    
                System.out.println("用户"+rs.getString(2));    
                //默认是从 1 开始编号的    
            }    
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
}   
```

##### 存储过程并调用   
```
create or replace procedure p_user_insert is    
begin--执行部分    
  insert into t_user(username, password, name) values('user03','123456','李丽丽3');
end;
--
set serveroutput on
--
declare
  v_name char(20);
begin
  select name into v_name from t_user where username='&aaa';
  dbms_output.put_line('姓名：'||v_name);
exception    
  when no_data_found then   
    dbms_output.put_line('朋友，你的账号输入有误！');
end;
--
--
execute scott.p_user_update('1234567','user');

create or replace procedure p_user_update(newpassword char,hisusername char) is
begin
  update t_user set password=newpassword where username=hisusername;
end;
```

```
package com.xxl.test;

import java.sql.CallableStatement;
import java.sql.Connection;    
import java.sql.DriverManager;    
import java.sql.ResultSet;    
import java.sql.Statement;    
import java.util.Date;

import oracle.sql.TIMESTAMP;
   
//演示  如何使用 jdbc_odbc 桥连接方式    
public class OracleJdbc3 {    
   
    public static void main(String[] args) {    
        try {    
   
            // 1.加载驱动    
Class.forName("oracle.jdbc.driver.OracleDriver");
// new oracle.jdbc.driver.OracleDriver();
            // 2.得到连接      
            Connection conn = DriverManager.getConnection(
            "jdbc:oracle:thin:@127.0.0.1:1521:myoracle", "scott","123456");
            // 创建CallableStatement
            String sql = "{call  scott.p_user_update(?,?)}";
            //String sql = "{call  scott.p_user_insert}";
            CallableStatement cstmt = conn.prepareCall(sql);
            cstmt.setString(1, "123456java");
            cstmt.setString(2, "user");
            //执行
System.out.println(333);
            int num = cstmt.executeUpdate();
System.out.println(444);
            System.out.println(num);
            //关闭
            cstmt.close();
            conn.close();
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }    
}   
```
