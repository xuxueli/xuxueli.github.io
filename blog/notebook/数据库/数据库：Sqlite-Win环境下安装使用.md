>  SQLite，是一款轻型的数据库，是遵守ACID的关系型数据库管理系统，它包含在一个相对小的C库中。它是D.RichardHipp建立的公有领域项目。它的设计目标是嵌入式的，而且目前已经在很多嵌入式产品中使用了它，它占用资源非常的低，在嵌入式设备中，可能只需要几百K的内存就够了。它能够支持Windows/Linux/Unix等等主流的操作系统，同时能够跟很多程序语言相结合，比如 Tcl、C#、PHP、Java等，还有ODBC接口，同样比起Mysql、PostgreSQL这两款开源的世界著名数据库管理系统来讲，它的处理速度比他们都快。SQLite第一个Alpha版本诞生于2000年5月。 至2015年已经有15个年头，SQLite也迎来了一个版本 SQLite 3已经发布。

# 1. Windows

### sqlite3下载：
[官网](http://www.sqlite.org/download.html)

- 1、下载windows版本；
- 2、解压出sqlite3.exe文件；
- 3、新建库：
    cd 解压目录
    sqlite3.exe test.db
    quit;
- 4、在解压目录下及发现一个test.db文件，copy至项目目录，即可开始开发；（可使用navicat导入该文件操作）

### 可视化工具：
[navicat-for-sqlite](http://www.navicat.com.cn/download/navicat-for-sqlite)

功能强大，同mysql版本navicat注册方法；

### maven依赖，驱动：sqlite-jdbc
```
<!-- sqlite-jdbc -->
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.8.11</version>
</dependency>
```

### jdbc配置地址：
```
jdbc.driverClassName=org.sqlite.JDBC
jdbc.username=root
jdbc.password=root_pwd
// 磁盘根路径下
jdbc.url=jdbc:sqlite:/data/appcfg/cfg.db
// classpath目录下
#jdbc.url=jdbc:sqlite::resource:cfg.db
```

### 原生jdbc方式操作sqlite
```
package com.xxl.test;
import java.sql.*;

public class mysqlitejdbc {

/**
 * @param args
 */
public static void main(String[] args) {

String driver="org.sqlite.JDBC";
//红色部分路径要求全小写，大写会报错
String url="jdbc:sqlite://e:/workingfile/javaworkspace/TestSqlliteJDBC/db/sqlite.db";
//String user="root";
//String password="123456";
Connection conn=null;
PreparedStatement psmt=null;
ResultSet rs=null;

try{
Class.forName(driver); //加载驱动
conn=DriverManager.getConnection(url);//创建连接
String sql="select name from user where username=? and password=?";
psmt=conn.prepareStatement(sql);//创建Statement用来发送语句
psmt.setString(1, "user");
psmt.setString(2, "123456");
rs=psmt.executeQuery();//返回结果集
if(rs.next()){
String name=rs.getString("name");
System.out.println(name);
}
}catch(Exception e){
}
try{
rs.close();
psmt.close();
conn.close();
}catch(Exception e){
}
}
}
```

[sqlite基本sql语句使用](http://www.2cto.com/database/201304/205359.html) 

[Sqlite的基本日常SQL操作语句汇总](http://blog.itpub.net/16900201/viewspace-1291550/)


