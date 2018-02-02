### Annotation

> 定义：注解（Annotation），也叫元数据。一种代码级别的说明。它是JDK5.0及以后版本引入的一个特性，与类、接口、枚举是在同一个层次。

> 它可以声明在包、类、字段、方法、局部变量、方法参数等的前面，用来对这些元素进行说明，注释。

作用分类：

- 1、 编写文档：通过代码里标识的元数据生成文档【生成文档doc文档】
- 2、 代码分析：通过代码里标识的元数据对代码进行分析【使用反射】
- 3、 编译检查：通过代码里标识的元数据让编译器能够实现基本的编译检查【Override】

### java中注解的使用与实例

> 注解目前非常的流行，很多主流框架都支持注解，而且自己编写代码的时候也会尽量的去用注解，一时方便，而是代码更加简洁。

注解的语法比较简单，除了@符号的使用之外，它基本与Java固有语法一致。Java SE5内置了三种**标准注解**：

- **@Override**，表示当前的方法定义将覆盖超类中的方法。
- **@Deprecated**，使用了注解为它的元素编译器将发出警告，因为注解@Deprecated是不赞成使用的代码，被弃用的代码。
- **@SuppressWarnings**，关闭不当编译器警告信息。

上面这三个注解多少我们都会在写代码的时候遇到。Java还提供了4中注解，专门负责新注解的创建（**元注解**）。

- **@Target** ：
    
    表示该注解可以用于什么地方，可能的ElementType参数有：
    - CONSTRUCTOR：构造器的声明
    - FIELD：域声明（包括enum实例）
    - LOCAL_VARIABLE：局部变量声明
    - METHOD：方法声明
    - PACKAGE：包声明
    - PARAMETER：参数声明
    - TYPE：类、接口（包括注解类型）或enum声明

- **@Retention** ：
    
    表示需要在什么级别保存该注解信息。可选的RetentionPolicy参数    包括：
    - SOURCE：注解将被编译器丢弃
    - CLASS：注解在class文件中可用，但会被VM丢弃
    - RUNTIME：VM将在运行期间保留注解，因此可以通过反射机制读取注解的信息。
    
- **@Document** ：
    将注解包含在Javadoc中
- **@Inherited** ：
    允许子类继承父类中的注解

###  定义一个注解的方式：

```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    
}
```

除了@符号，注解很像是一个接口。定义注解的时候需要用到元注解，上面用到了@Target和@Retention，它们的含义在上面的表格中已近给出。

在注解中一般会有一些元素以表示某些值。注解的元素看起来就像接口的方法，唯一的区别在于可以为其制定默认值。没有元素的注解称为标记注解，上面的@Test就是一个标记注解。 

### 注解须知：
- 注解的可用的类型包括以下几种：所有基本类型、String、Class、enum、Annotation、以上类型的数组形式。
- 元素不能有不确定的值，即要么有默认值，要么在使用注解的时候提供元素的值。而且元素不能使用null作为默认值。
- 注解在只有一个元素且该元素的名称是value的情况下，在使用注解的时候可以省略“value=”，直接写需要的值即可。


下面看一个定义了元素的注解。
```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    public String id();
    public String description() default "no description";
}
```
定义了注解，必然要去使用注解。 

```
public class PasswordUtils {
    @UseCase(id = 47, description = "Passwords must contain at least one numeric")
    public boolean validatePassword(String password) {
        return (password.matches("\\w*\\d\\w*"));
    }
    @UseCase(id = 48)
    public String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }
}
```

使用注解最主要的部分在于对注解的处理，那么就会涉及到注解处理器。

### 注解处理器：
从原理上讲，注解处理器就是通过反射机制获取被检查方法上的注解信息，然后根据注解元素的值进行特定的处理。


```
public static void main(String[] args) {
     List<Integer> useCases = new ArrayList<Integer>();
     Collections.addAll(useCases, 47, 48, 49, 50);
     trackUseCases(useCases, PasswordUtils.class);
 }
 
 public static void trackUseCases(List<Integer> useCases, Class<?> cl) {
     for (Method m : cl.getDeclaredMethods()) {
         UseCase uc = m.getAnnotation(UseCase.class);
         if (uc != null) {
             System.out.println("Found Use Case:" + uc.id() + " "
                         + uc.description());
             useCases.remove(new Integer(uc.id()));
         }
     }
     for (int i : useCases) {
         System.out.println("Warning: Missing use case-" + i);
     }
}
 
----------
Found Use Case:47 Passwords must contain at least one numeric
Found Use Case:48 no description
Warning: Missing use case-49
Warning: Missing use case-50
```

上面的三段代码结合起来是一个跟踪项目中用例的简单例子。

到这里，可以发现，可以根据 “注解，反射，拦截器，枚举” 等实现一套用户权限验证系统，将用户权限用枚举方式给出，或者处处在DB中，注解元素表明那个方法拥有哪些权限可以调用，拦截器拦截请求进行校验，根据用户权限进行处理。直接可以加载Action/Controller，Service上和一些特殊的util上等等。(可以查看我的几个开源项目，权限基本都是据此实现的)


### 使用 “注解” + “反射” 的方式，汇总指定路径，执行注解的类信息（如波克游戏服务器，系统中的Logic类注册）

##### 第一步：开发注解
```
package com.xxl.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation1 {
	
	/**
	 * 消息类型
	 * 
	 * @return
	 */
	int type1();
}

```

##### 第二步：使用注解
类1
```
package com.xxl.test.temp;

import com.xxl.test.Annotation1;

@Annotation1(type1 = 1)
public class Clazz1 {
	
}

```
类2
```
package com.xxl.test.temp;

import com.xxl.test.Annotation1;

@Annotation1(type1 = 2)
public class Clazz2 {
	
}

```

##### 第三步：根据注解+反射，加载指定的Class信息
```
package com.xxl.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Test {
	public static void main(String[] args) {
		init();
		
		for (Entry<Integer, Class<?>> item : logicInfos.entrySet()) {
			System.out.println(item.getValue().getName());
		}
		
	}
	
	public static Map<Integer, Class<?>> logicInfos = new HashMap<Integer, Class<?>>();
	public static void init() {
		// 当前目录的相对目录 “temp” 下
		String path = Test.class.getResource("").getPath();
		path = path + "temp/";
		// 加载目录下文件class文件
		File requestMsgFiles = new File(path);
		String[] allMsgInfos = requestMsgFiles.list();
		for (String msgInfo : allMsgInfos) {
			try {
				// 完整路径：/bin/com/xxl/test/temp/Clazz1.class
				String msgPathInfo = path + msgInfo;
				// 计算包名
				msgPathInfo = msgPathInfo.substring(msgPathInfo.indexOf("bin") + 4, msgPathInfo.lastIndexOf("."));
				msgPathInfo = msgPathInfo.replace("/", ".");
				// 根据报名反射获得Class，和注解 @Annotation1的type1值
				Class<?> msgClazz = Class.forName(msgPathInfo);
				Annotation1 requestMsgType = msgClazz.getAnnotation(Annotation1.class);
				// 用注解 @Annotation1的type1值作为key，Class作为value，注册到Map中
				logicInfos.put(requestMsgType.type1(), msgClazz);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		}
	}
}

```

这段业务逻辑，在之前我做游戏开发的时候，使用过，用来加载服务Logic类资源。

