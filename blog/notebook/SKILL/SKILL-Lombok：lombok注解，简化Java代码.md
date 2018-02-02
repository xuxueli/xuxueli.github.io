### lombok 注解
lombok 提供了简单的注解的形式来帮助我们简化消除一些必须有但显得很臃肿的 java 代码。特别是对于 POJO类来说，

Lombok 注解在线帮助文档：http://projectlombok.org/features/index.

下面介绍几个我常用的 lombok 注解：

- @Data   ：注解在类上；提供类所有属性的 getting 和 setting 方法，此外还提供了equals、canEqual、hashCode、toString 方法
- @Setter：注解在属性上；为属性提供 setting 方法
- @Getter：注解在属性上；为属性提供 getting 方法
- @Log4j ：注解在类上；为类提供一个 属性名为log 的 log4j 日志对象
- @NoArgsConstructor：注解在类上；为类提供一个无参的构造方法
- @AllArgsConstructor：注解在类上；为类提供一个全参的构造方法

### 不使用 lombok 的方案
```
public class Person {
    private String id;
    private String name;
    private String identity;
    private Logger log = Logger.getLogger(Person.class);
    public Person() {
    }
    public Person(String id, String name, String identity) {
        this.id              = id;
        this.name       = name;
        this.identity  = identity;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getIdentity() {
        return identity;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
```

### 使用 lombok 的方案
```
@Data
@Log4j
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String id;
    private String name;
    private String identity;
}
```

### 
使用lombok项目的方法很简单，分为四个步骤：
- 1)在需要自动生成getter和setter方法的类上，加上@Data注解
- 2)在编译类路径中加入lombok.jar包
- 3)使用支持lombok的编译工具编译源代码（关于支持lombok的编译工具，见“四、支持lombok的编译工具”）
- 4)编译得到的字节码文件中自动生成了getter和setter方法

### lombok的罪恶
使用lombok虽然能够省去手动创建setter和getter方法的麻烦，但是却大大降低了源代码文件的可读性和完整性，降低了阅读源代码的舒适度。

### 在eclipse中使用Lombok：
- 1、下载Lombok.jar http://projectlombok.googlecode.com/files/lombok.jar
- 2、运行Lombok.jar: java -jar  D:\001_software\work\Java\libs\lombok.jar
        数秒后将弹出一框，以确认eclipse的安装路径
- 3、确认完eclipse的安装路径后，点击install/update按钮，即可安装完成
- 4、安装完成之后，请确认eclipse安装路径下是否多了一个lombok.jar包，并且其
     配置文件eclipse.ini中是否 添加了如下内容:
           -javaagent:lombok.jar
           -Xbootclasspath/a:lombok.jar
     如果上面的答案均为true，那么恭喜你已经安装成功，否则将缺少的部分添加到相应的位置即可
5、重启eclipse或myeclipse 


### 在Intellij中使用Lombak；
搜索lombak关键字，安装对应插件即可；