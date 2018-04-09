# 1、Windows下安装oracle XE 安装
[在 Windows 下安装 Oracle 11g XE (Express Edition)](http://www.oschina.net/question/12_27650)

[Oracle 11g安装图文攻略](http://www.2cto.com/database/201208/150620.html)

# 2、Windows安装使用PL/SQL
[教程](http://blog.sina.com.cn/s/blog_642fec670101f182.html)
### 免安装Oracle客户端，使用PL/SQL
- 1、Oracle官网下载Oracle Instant Client
    
    [下载地址](http://www.oracle.com/technetwork/topics/winsoft-085727.html)

- 2、解压到目标机器上，即可

    如地址：C:\instantclient_11_2_x86\

- 3、配置tnsnames.ora（配置连接地址，可配置多个）
    
    用记事本新建tnsnames.ora文件保存在该路径下：C:\instantclient_11_2_x86\NETWORK\ADMIN\tnsnames.ora
    内容参考下面的进行配置：

    ```
    MY_XXXServer =
    (DESCRIPTION =
    (ADDRESS = (PROTOCOL = TCP)(HOST = 10.199.200.102)(PORT = 1521))
    (CONNECT_DATA =
    (SID = oravm)
    (SERVER = DEDICATED)
    )
    )
    ```

- 4、下载PL/SQL，解压PL/SQL到目标机器上

    - [官网](https://www.allroundautomations.com/)下载，注册码；
    - 百度搜索 “plsql 11 汉化破解版”，例如[CSDN下载](http://download.csdn.net/detail/mysky2008/8852749)；

- 5、配置instantclient

    打开PL/SQL登录的时候，“取消”即可。
    
    进入“首选项”（工具-〉首选项），左侧选择Oracle，连接，右侧填写“Oracle主目录名（上述instantclient解压目录）”以及OCI库地址即可：

- 6、完成以上步骤，重新打开PLSQL，输入用户名密码，选择正确的数据库即可。
- 7、中文乱码问题

    设置环境变量（全局，可能对其它Oracle客户端产生影响）
    ```
    NLS_LANG=SIMPLIFIED CHINESE_CHINA.ZHS16GBK
    ```

### 配置登录账号，自动登录
ools>Logon History>Fixed Users>粘贴登录用户信息。如下：
```
>16
lvmama_ver/xxxxxx@db_16
>128
lvmama_ver/xxxxxx@db_128
lvmama_pet/xxxxxx@db_128
lvmama_super/xxxxxx@db_128
>138
--lvmama_test
LVMAMA_TEST/xxxxxx@db_138
--lvmama_super
lvmama_super/xxxxxx@db_138
--lvmama_pet
lvmama_pet/xxxxxx@db_138
--lvmama_ver
lvmama_ver/xxxxxx@db_138
```

### plsql工具如何自动生成字段
打开一个SQLWindow窗口，选中要查询的table，拖到SQLWindow窗口，在弹出的列表中选中Select，然后就自动生成了select所有字段的查询语句

###常用语句
```
---------建表
create table t_user(
       t_id number(11) not null primary key,
       username varchar(20) not null,
       password varchar(20) not null,
       name varchar(20)
)
---------修改字段
alter table T_USER rename column t_username to USERNAME;
alter table T_USER rename column t_password to PASSWORD;
alter table T_USER rename column t_name to NAME;
---------添加字段
alter table T_USER add log_time date;
---------建索引
create sequence t_user_seq
       minvalue 1 maxvalue 999999999
       increment by 1
       start with 1; /*步长为1*/
---------查询索引
select t_user_seq.nextval from dual;
select t_user_seq.currval from dual;
---------插入
insert into t_user( t_id, username, password, name, log_time )
       values( T_USER_SEQ.nextval, 'xuxueli', '123456', '许雪里', sysdate)
---------查询
select t.*, t.rowid from t_user t
```



