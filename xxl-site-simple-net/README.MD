1、底层框架：spring3.2.9(mvc) + mybatis3.2.7 + mysql5.5
2、权限设计：RBAC：用户-角色-权限【权限模块-权限组-权限项】
3、登陆系统：cookie = sameKey:cookieIdentity; cache = sameKey + userName : loginIdentity
(用户登陆信息,缓存key (cookie=sameKey:cacheIdentity; cache:sameKey+userName:loginIdentity))
4、文章菜单：模块 -> 组 -> 文章
5、一面墙模块：爬虫+任务调度(暂时抓取糗百的数据)

-----------------------------------------------
线上线下,差异配置文件：
	1、jdbc.properties							：数据库配置
	2、memcached.properties						：缓存配置
	3、mail.properties							：公共邮件地址配置
	4、freemarker.variables.properties			：freemarker全局变量(跟地址配置,spring mvc 不需要，页面静态化时需要)
-----------------------------------------------
JS插件引用：
	001：001：bootstrap3.3.4:未做修改