##### parameterType 和 resultMap
parameterType：单个参数时有效，Mapper方式@Param多参数时，可不设置；
resultMap：字段和Model映射；

##### mapper 传递多个参数：
    @Param 的作用是设置参数别名。设置后的参数只能通过`#{param[1..n]`或者`#{注解别名}`来访问
    多个参数情况下，均可使用  `#{参数位置[0..n-1]}` |   `#{param[1..n]}`来访问参数
    
    1、一个参数：
        1.1、无注解方式：
            1.1.1、参数为基本类型或为基本包装类型(int,Integer,String...)
                参数注释为: #{任意字符}
                例如：User getUserById(int id);  
                ... where id = #{id}（或 #{任意字符} 不可为空） ...
            1.1.2、参数为自定义对象
                参数注释为: #{对象属性}
                例如：User getUser(User user);  
                ... where name = #{name} and age = #{age} ...
                
        1.2、"@Param" 注解方式
            1.2.1、参数为基本类型或为基本包装类型(int,Integer,String...)
                参数注释为: #{注解名称} | #{param1}
                例如：User getUserById(@Param(value="keyId") int id); 
                ... where id = #{keyId} 或 #{param1} ...
            1.2.2、参数为自定义对象
                参数注释为: #{注解名称.对象属性} | #{param1.对象属性}
                例如：User getUser(@Param(value="usr") User user); 
                ... where name = #{user.name} 或者 #{param1.name} ...
        
    2、多个参数：
        2.1、无注解方式
            2.1.1、参数为基本类型或为基本包装类型(int,Integer,String...)
                参数注释为: #{参数位置[0..n-1]} | #{param[1..n]}
                例如：User getUser(String name, int age); 
                ... where name = #{0} and age = #{1} ...
                ... where name = #{param1} and age = #{param2} ...
            2.1.2、参数为自定义对象
                参数注释为: #{参数位置[0..n-1].对象属性} | #{param[1..n].对象属性}
                例如：User getUser(User usr, int flag);  
                ... where name = #{0.name} and age = {0.age} and flag = #{1} ...
                ... where name = #{param1.name} and age = {param1.age} and flag = #{param2} ...
                
        2.2、使用`@Param`注解
            2.2.1、参数为基本类型或为基本包装类型(int,Integer,String...)
                参数注释为: #{注解名称} | #{param[1..n]}
                例如：User getUser(@Param(value="xm") String name, @Param(value="nl") int age); 
                ... where name = #{xm} and age = #{nl}  ... 
                ... where name = #{param1} and age = #{param2}  ...
                ... where name = #{xm} and age = #{param2}  ...
            2.2.2、参数为自定义对象
                参数注释为: #{注解名称.对象属性} | #{param[1..n].对象属性}
                例如：User getUser(@Param(value="usr") User user, @Param(value="tag") int flag);
                ... where name = #{usr.name} and age = #{usr.age} and flag = #{tag} ...
                ... where name = #{param1.name} and age = #{param1.age} and flag = #{param2} ...
                ... where name = #{usr.name} and age = #{param1.age} and flag = #{param2} ...
            2.2.3、部分参数使用`@Param`注解
                当采用部分参数使用`@Param`注解时，参数注释为将以上两种情况结合起来即可
                例如：User getUser(String name, @Param(value="nl") age, int gendar);
                ... where name = #{0} and age = #{nl} and gendar = #{param3} ...

```
多个参数，三种典型传参方式：

// 方式一：#{0}、#{1}...
DAO层的函数方法 : Public User selectUser(String name,String area);
SQL : ... user_name = #{0} and user_area=#{1} ...
说明：#{0}代表接收的是dao层中的第一个参数，#{1}代表dao层中第二参数。不够直观；

// 方式二：
DAO层的函数方法 : Public User selectUser(Map paramMap);
SQL : ... user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea,jdbcType=VARCHAR} ...
说明：采用Map传多参数, Map 中存放参数名 "userName" 等。不够直观；

// 方式三：
DAO层的函数方法 : Public User selectUser(@param(“userName”)Stringname,@param(“userArea”)String area);
SQL : ... user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea,jdbcType=VARCHAR} ...
说明：采用注解参数方式，比较直观，推荐。

```

##### Mybatis动态SQL
[地址](http://blog.csdn.net/flanet/article/details/7759761)
```
// MyBatis中用于实现动态SQL的元素主要有：
if  
choose（when，otherwise）  
where  
trim  
set  
foreach

# 1、if
select * from t_blog where 1 = 1  
<if test="content != null">  
    and content = #{content}  
</if>

# 2、choose（when，otherwise）  
// choose元素的作用就相当于JAVA中的switch语句
select * from t_blog where 1 = 1  
<choose>  
    <when test="title != null">  
        and title = #{title}  
    </when>  
    <when test="content != null">  
        and content = #{content}  
    </when>  
    <otherwise>  
        and owner = "owner1"  
    </otherwise>  
</choose>  

# 3、where
// 简化SQL语句中where中的条件判断的
select * from t_blog  
<where>  
    <if test="title != null">  
        title = #{title}  
    </if>  
    <if test="content != null">  
        and content = #{content}  
    </if>  
    <if test="owner != null">  
        and owner = #{owner}  
    </if>  
</where>  

# 4、trim 
// 支持前后缀，prefix和suffix；覆盖首部，prefixOverrides和suffixOverrides；可以替代where
select * from t_blog  
<trim prefix="where" prefixOverrides="and |or"> 
    <if test="title != null">  
        title = #{title}  
    </if>  
    <if test="content != null">  
        and content = #{content}  
    </if>  
    <if test="owner != null">  
        or owner = #{owner}  
    </if>  
</trim>  

# 5、set
// 简化SQL中update的SET区域的数据，都为空会报错的
update t_blog  
<set>  
    <if test="title != null">  
        title = #{title},  
    </if>  
    <if test="content != null">  
        content = #{content},  
    </if>  
    <if test="owner != null">  
        owner = #{owner}  
    </if>  
</set>  
where id = #{id}  

# 6、foreach
// foreach的主要用在构建in条件中，它可以在SQL语句中进行迭代一个集合
select * from t_blog where id in  
<foreach collection="list" index="index" item="item" open="(" separator="," close=")">  
    #{item}  
</foreach>  
```

##### 返回值

    insert，返回值是：新插入行的主键（primary key）；需要包含<selectKey>语句，才会返回主键，否则返回值为null。
    update/delete，返回值是：更新或删除的行数；无需指明resultClass；但如果有约束异常而删除失败，只能去捕捉异常。
    queryForObject，返回的是：一个实例对象或null；需要包含<select>语句，并且指明resultMap；
    queryForList，返回的是：实例对象的列表；需要包含<select>语句，并且指明resultMap；

##### mybatis+mysql返回主键：
解决：在mapper中指定keyProperty属性
```
<insert id="insertAndGetId" useGeneratedKeys="true" keyProperty="userId" parameterType="com.chenzhou.mybatis.User">
    insert into user(userName,password,comment)
    values(#{userName},#{password},#{comment})
</insert>
```

##### mybatis+oracle返回主键：
```
<insert id="insertSelective" parameterType="com.jxxx.p2pp.model.UUserInfo">
    <selectKey resultType="java.math.BigDecimal" order="BEFORE" keyProperty="id">
        SELECT U_USER_INFO_SEQ.Nextval as ID from DUAL
    </selectKey>
    insert into user(id, userName, password, comment)
    values(#{id}, #{userName}, #{password}, #{comment})
</insert>
```

##### mybatis ${}与#{}的区别：
- "#{} 》${}"，前者可以防止SQL注入，解析出的参数带单引号包裹，推荐；后者简单拼接字符串；
- 能用#不适用$, $方式一般用于传入数据库对象，如表名；

##### 表字段和系统关键字冲突，如使用order、desc等
- 用撇号（`）包裹该字段

##### jdbcType：

    jdbcType=DATE 只传入了 年月日 
    jdbcType=TIMESTAMP 年月日+ 时分秒
    jdbcType=VARCHAR 字符串
    jdbcType=DECIMAL 数字
    jdbcType=INTEGER 整数
    jdbcType 是否必须：当传入字段值为null,时,需要加入. 否则报错


##### 转义字符的处理方法
- 方式1：<![CDATA[  脚本  ]]>
    - 将需要转移的脚本内容用该标签包裹；
- 方式2：使用转义字符
    转义 | 原符号 | 名称
    --|--|--
    &lt; | < | 小于号
    &gt; | > | 大于号
    &amp; | & | 和
    &apos; | ’ | 单引号
    &quot; | " | 双引号

##### Mybatis配置DAO的两种方式（多数据源）：Template方式、Mapper映射器
```
方式一：Template方式
applicationcontext-database.xml
----配置01----
<bean id="mainSqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="mainDataSource" />
        <!--    <property name="configLocation" value="classpath:sqlmap-config.xml" />    -->
        <property name="mapperLocations">
                <value>classpath*:com/xxl/core/model/main/mapper/*.xml</value>
        </property>
</bean>
<bean id="mainSqlSessionTemplate"  class="org.mybatis.spring.SqlSessionTemplate">  
        <constructor-arg index="0" ref="mainSqlSessionFactory" />  
</bean>
----配置02----
<bean id="main2SqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="main2DataSource" />
        <!--    <property name="configLocation" value="classpath:sqlmap-config.xml" />    -->
        <property name="mapperLocations">
                <value>classpath*:com/xxl/core/model/main2/mapper/*.xml</value>
        </property>
</bean>
<bean id="main2SqlSessionTemplate"  class="org.mybatis.spring.SqlSessionTemplate">  
        <constructor-arg index="0" ref="main2SqlSessionFactory" />  
</bean>

------------------------------------------
ITestDao.java
package com.xxl.dao;
public interface ITestDao {
        public int test();
}
-------------------------------------------
TestDaoImpl.java
package com.xxl.dao.impl;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.xxl.dao.ITestDao;

@Repository
public class TestDaoImpl implements ITestDao {

        @Autowired
        private SqlSessionTemplate sqlSessionTemplate;

        @Override 
        public int test() {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("aaa", 5);
                return sqlSessionTemplate.selectOne("UserMapper.countLine", params);
        }
}
--------------------------------------------
User.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="UserMapper">
        <select id="countLine" parameterType="java.lang.Integer" resultType="java.lang.Integer">
                select count(1) + #{aaa} from user
        </select>
</mapper>


方式二：Mapper映射器
aplicationcontent-datasource.xml
----配置01----
<!-- 为mybatis注入session工厂 -->
<bean id="mainSqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="mainDataSource"/>
    <!--    <property name="configLocation" value="classpath:mybatis/config.xml"/>    -->
    <property name="mapperLocations">
        <value>classpath*:com/xxl/core/model/main/mapper/*.xml</value>
    </property>
</bean>
<!-- 扫描 basePackage下所有的接口，根据对应的mapper.xml为其生成代理类-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="sqlSessionFactoryBeanName" value="mainSqlSessionFactory"/>
    <property name="basePackage" value="com.xxl.common.main.mapper" />
</bean>

----配置02----
<!-- 为mybatis注入session工厂 -->
<bean id="main2SqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="main2DataSource"/>
    <!--    <property name="configLocation" value="classpath:mybatis/config.xml"/>    -->
    <property name="mapperLocations">
       <value>classpath*:com/xxl/core/model/main2/mapper/*.xml</value>
    </property>
</bean>
<!-- 扫描 basePackage下所有的接口，根据对应的mapper.xml为其生成代理类-->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="sqlSessionFactoryBeanName" value="main2SqlSessionFactory"/>
    <property name="basePackage" value="com.xxl.common.main2.mapper" />
</bean>

----------------------------------------------
ArticleMapper.java
package com.xxl.common.mapper;
public interface ArticleMapper {
        int updateClikcount(int id);
}
---------------------------------------------
Article.xml（namespace指向Mapper接口）
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xxl.common.mapper.ArticleMapper">
        <update id="updateClikcount" parameterType="int">
                UPDATE t_article SET clikcount=clikcount+1 WHERE id=#{id}
        </update>
</mapper>
```


##### Mybatis和Hibernate区别
- 两者相同点
    - Hibernate与MyBatis都可以是通过SessionFactoryBuider由XML配置文件生成SessionFactory，然后由SessionFactory 生成Session，最后由Session来开启执行事务和SQL语句。其中SessionFactoryBuider，SessionFactory，Session的生命周期都是差不多的。
    - Hibernate和MyBatis都支持JDBC和JTA事务处理。
- Mybatis优势
    - MyBatis可以进行更为细致的SQL优化，可以减少查询字段。
    - MyBatis容易掌握，而Hibernate门槛较高。
- Hibernate优势
    - Hibernate的DAO层开发比MyBatis简单，Mybatis需要维护SQL和结果映射。
    - Hibernate对对象的维护和缓存要比MyBatis好，对增删改查的对象的维护要方便。
    - Hibernate数据库移植性很好，MyBatis的数据库移植性不好，不同的数据库需要写不同SQL。
    - Hibernate有更好的二级缓存机制，可以使用第三方缓存。MyBatis本身提供的缓存机制不佳。
- 他人总结
    - Hibernate功能强大，数据库无关性好，O/R映射能力强，如果你对Hibernate相当精通，而且对Hibernate进行了适当的封装，那么你的项目整个持久层代码会相当简单，需要写的代码很少，开发速度很快，非常爽。 
    - Hibernate的缺点就是学习门槛不低，要精通门槛更高，而且怎么设计O/R映射，在性能和对象模型之间如何权衡取得平衡，以及怎样用好Hibernate方面需要你的经验和能力都很强才行。 
    - iBATIS入门简单，即学即用，提供了数据库查询的自动对象绑定功能，而且延续了很好的SQL使用经验，对于没有那么高的对象模型要求的项目来说，相当完美。 
    - iBATIS的缺点就是框架还是比较简陋，功能尚有缺失，虽然简化了数据绑定代码，但是整个底层数据库查询实际还是要自己写的，工作量也比较大，而且不太容易适应快速数据库修改。