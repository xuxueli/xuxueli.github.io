
- [struts2官网地址](http://struts.apache.org/)
- [struts2马士兵笔记](http://www.cnblogs.com/baolibin528/p/3975618.html)
- [struts2马士兵笔记](http://blog.csdn.net/chenggil10/article/details/5965806)
- [Struts2平凡之路（三）Struts2架构和运行流程](http://blog.csdn.net/pengpeng83/article/details/46443679)
- [struts2之单个文件上传](http://www.cnblogs.com/linjiqin/archive/2011/03/21/1990674.html)
- [struts2.xml 中result type属性说明](http://blog.csdn.net/voyage_mh1987/article/details/5829163)

##### 

##### Action中method或者属性，推荐不要以set或get开头
struts2 Action中方法不要以get和set开头,否则会被struts调用


##### Struts2接收批量提交数据解决方案，数组[]参数
问题说明：解决页面大批量提交数据到后台，封装对象，持久化到数据库，

- 第一种方式：对象集合接收

```
// jsp页面属性定义：
<td><input name="userList[0].userName"/></td>
<td><input name="userList[1].userName"/></td>
<td><input name="userList[2].userName"/></td>

// action中对象集合定义：
private List<User> userList;构造get和set方法
```

**优点：**
后台获取数据直接是封装好的User对象，各属性值已经自动set进去了，直接循环入库即可如：
```
for (User user : userList) {
    userManager.save(user);
}
```
**缺点：**
如果页面有js插入行，或者新增操作，或者删除其中某行时，userList索引就会重复或者缺失，
解决思路可以每新增一行通过js去修改各行中文本框name索引值使其不重复和
1千条测试用时30s才进入到action并且输出信息(未测试入库)

- 第二种方式：属性数组接收
```
// jsp页面属性定义：
<td><input name="userNames" /></td>
<td><input name="userNames"/></td>
<td><input name="userNames"/></td>

// action中属性数组定义：
private String[] userNames;构造get和set方法
```
**优点：**
页面不需要考虑通过js新增/删除操作对其userName数组大小索引的影响，后台得到数组即可

**缺点：**
后台获取数组后需要对各个属性进行封装User对象操作，并且action需要对每一个属性定义的数组
```
for (int i = 0; i < userNames.length; i++) {
    User user=new User();
    user.setUserName(userNames[i]);
    userManager.save(user);
}
```
1千条测试用时30s才进入到action并且输出信息(未测试入库)
从效率考虑，经过测试1千条数据批量提交数据（对象的属性为1个）来看，前端js遍历修改索引速度慢于后台批量创建对象的速度；


个人建议：
- 1.如果数目小于1千，考虑到后台action定义数组的繁琐，在对象属性较多情况下使用第一种方式比较好。
- 2.如果数目大于1千，考虑到前端每次js操作很有可能导致浏览器卡死，在对象属性不多情况下使用第二种方式比较好。

