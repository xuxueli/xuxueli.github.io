<h2 style="color:#4db6ac !important" >Java基础</h2>

[TOCM]

[TOC]

---

### 【Annotation】

> 定义：注解（Annotation），也叫元数据。一种代码级别的说明。它是JDK5.0及以后版本引入的一个特性，与类、接口、枚举是在同一个层次。

> 它可以声明在包、类、字段、方法、局部变量、方法参数等的前面，用来对这些元素进行说明，注释。

作用分类：

- 1、 编写文档：通过代码里标识的元数据生成文档【生成文档doc文档】
- 2、 代码分析：通过代码里标识的元数据对代码进行分析【使用反射】
- 3、 编译检查：通过代码里标识的元数据让编译器能够实现基本的编译检查【Override】

#### java中注解的使用与实例

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

####  定义一个注解的方式：

```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    
}
```

除了@符号，注解很像是一个接口。定义注解的时候需要用到元注解，上面用到了@Target和@Retention，它们的含义在上面的表格中已近给出。

在注解中一般会有一些元素以表示某些值。注解的元素看起来就像接口的方法，唯一的区别在于可以为其制定默认值。没有元素的注解称为标记注解，上面的@Test就是一个标记注解。 

#### 注解须知：
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

#### 注解处理器：
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


#### 使用 “注解” + “反射” 的方式，汇总指定路径，执行注解的类信息（如XX游戏服务器，系统中的Logic类注册）

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


---
### 【IO】

#### Java流操作有关的类或接口：
![image](http://pic002.cnblogs.com/images/2012/384764/2012031413371190.png)

#### Java流类图结构：
![image](http://pic002.cnblogs.com/images/2012/384764/2012031413373126.jpg)

#### 流的概念和作用

流是一组有顺序的，有起点和终点的字节集合，是对数据传输的总称或抽象。即数据在两设备间的传输称为流，**流的本质是数据传输，根据数据传输特性将流抽象为各种类，方便更直观的进行数据操作**。 

#### IO流的分类
- 根据处理数据类型的不同分为：字符流和字节流
- 根据数据流向不同分为：输入流和输出流
 
#### 字符流和字节流

字符流的由来： 因为数据编码的不同，而有了对字符进行高效操作的流对象。本质其实就是基于字节流读取时，去查了指定的码表。 字节流和字符流的区别：

- 读写单位不同：字节流以字节（8bit）为单位，字符流以字符为单位，根据码表映射字符，一次可能读多个字节。
- 处理对象不同：字节流能处理所有类型的数据（如图片、avi等），而字符流只能处理字符类型的数据。
- 
结论：只要是处理纯文本数据，就优先考虑使用字符流。 除此之外都使用字节流。


#### 输入流和输出流
对输入流只能进行读操作，对输出流只能进行写操作，程序中需要根据待传输数据的不同特性而使用不同的流。 

#### Java IO流对象
##### 1.输入字节流InputStream
IO 中输入字节流的继承图可见上图，可以看出：

- InputStream 是所有的输入字节流的父类，它是一个抽象类。
- ByteArrayInputStream、StringBufferInputStream、FileInputStream 是三种基本的介质流，它们分别从Byte 数组、StringBuffer、和本地文件中读取数据。PipedInputStream 是从与其它线程共用的管道中读取数据，与Piped 相关的知识后续单独介绍。
- ObjectInputStream 和所有FilterInputStream 的子类都是装饰流（装饰器模式的主角）。
 
##### 2.输出字节流OutputStream
IO 中输出字节流的继承图可见上图，可以看出：

- OutputStream 是所有的输出字节流的父类，它是一个抽象类。
- ByteArrayOutputStream、FileOutputStream 是两种基本的介质流，它们分别向Byte 数组、和本地文件中写入数据。PipedOutputStream 是向与其它线程共用的管道中写入数据，
- ObjectOutputStream 和所有FilterOutputStream 的子类都是装饰流。
 
##### 3.字节流的输入与输出的对应
![image](http://pic002.cnblogs.com/images/2012/384764/2012031413383430.png)

图中蓝色的为主要的对应部分，红色的部分就是不对应部分。*紫色的虚线部分代表这些流一般要搭配使用*。

从上面的图中可以看出Java IO 中的字节流是极其对称的。“存在及合理”我们看看这些字节流中不太对称的几个类吧！

1. LineNumberInputStream 主要完成从流中读取数据时，会得到相应的行号，至于什么时候分行、在哪里分行是由改类主动确定的，并不是在原始中有这样一个行号。在输出部分没有对应的部分，我们完全可以自己建立一个LineNumberOutputStream，在最初写入时会有一个基准的行号，以后每次遇到换行时会在下一行添加一个行号，看起来也是可以的。好像更不入流了。
2. PushbackInputStream 的功能是查看最后一个字节，不满意就放入缓冲区。主要用在编译器的语法、词法分析部分。输出部分的BufferedOutputStream 几乎实现相近的功能。
3. StringBufferInputStream 已经被Deprecated，本身就不应该出现在InputStream 部分，主要因为String 应该属于字符流的范围。已经被废弃了，当然输出部分也没有必要需要它了！还允许它存在只是为了保持版本的向下兼容而已。
4. SequenceInputStream 可以认为是一个工具类，将两个或者多个输入流当成一个输入流依次读取。完全可以从IO 包中去除，还完全不影响IO 包的结构，却让其更“纯洁”――纯洁的Decorator 模式。
5. PrintStream 也可以认为是一个辅助工具。主要可以向其他输出流，或者FileInputStream 写入数据，本身内部实现还是带缓冲的。本质上是对其它流的综合运用的一个工具而已。一样可以踢出IO 包！System.out 和System.out 就是PrintStream 的实例！

##### 4.字符输入流Reader
在上面的继承关系图中可以看出：
1. Reader 是所有的输入字符流的父类，它是一个抽象类。
2. CharReader、StringReader 是两种基本的介质流，它们分别将Char 数组、String中读取数据。PipedReader是从与其它线程共用的管道中读取数据。
3. BufferedReader很明显就是一个装饰器，它和其子类负责装饰其它Reader 对象。
4. FilterReader 是所有自定义具体装饰流的父类，其子类PushbackReader 对Reader 对象进行装饰，会增加一个行号。
5. InputStreamReader 是一个连接字节流和字符流的桥梁，它将字节流转变为字符流。FileReader 可以说是一个达到此功能、常用的工具类，在其源代码中明显使用了将FileInputStream 转变为Reader 的方法。我们可以从这个类中得到一定的技巧。Reader 中各个类的用途和使用方法基本和InputStream 中的类使用一致。后面会有Reader 与InputStream 的对应关系。

##### 5.字符输出流Writer
在上面的关系图中可以看出：
1. Writer 是所有的输出字符流的父类，它是一个抽象类。
2. CharArrayWriter、StringWriter 是两种基本的介质流，它们分别向Char 数组、String 中写入数据。PipedWriter 是向与其它线程共用的管道中写入数据，
3. BufferedWriter 是一个装饰器为Writer 提供缓冲功能。
4. PrintWriter 和PrintStream 极其类似，功能和使用也非常相似。
5. OutputStreamWriter 是OutputStream 到Writer 转换的桥梁，它的子类FileWriter 其实就是一个实现此功能的具体类（具体可以研究一SourceCode）。功能和使用和OutputStream 极其类似，后面会有它们的对应图。

##### 6.字符流的输入与输出的对应
![image](http://pic002.cnblogs.com/images/2012/384764/2012031413390861.png)

##### 7.字符流与字节流转换
转换流的特点：
1. 其是字符流和字节流之间的桥梁
2. 可对读取到的字节数据经过指定编码转换成字符
3. 可对读取到的字符数据经过指定编码转换成字节

何时使用转换流？
1. 当字节和字符之间有转换动作时；
2. 流操作的数据需要编码或解码时。

具体的对象体现：
1. InputStreamReader:*字节到字符的桥梁*
2. OutputStreamWriter:*字符到字节的桥梁*

这两个流对象是字符体系中的成员，它们有转换作用，本身又是字符流，所以在构造的时候需要传入字节流对象进来。

##### 8.File类
File类是对文件系统中文件以及文件夹进行封装的对象，可以通过对象的思想来操作文件和文件夹。 File类保存文件或目录的各种元数据信息，包括文件名、文件长度、最后修改时间、是否可读、获取当前文件的路径名，判断指定文件是否存在、获得当前目录中的文件列表，创建、删除文件和目录等方法。  

##### 9.RandomAccessFile类
该对象并不是流体系中的一员，其封装了字节流，同时还封装了一个缓冲区（字符数组），通过内部的指针来操作字符数组中的数据。

该对象特点：
1. 该对象只能操作文件，所以构造函数接收两种类型的参数：a.字符串文件路径；b.File对象。
2. 该对象既可以对文件进行读操作，也能进行写操作，在进行对象实例化时可指定操作模式(r,rw)

***注意：该对象在实例化时，如果要操作的文件不存在，会自动创建；如果文件存在，写数据未指定位置，会从头开始写，即覆盖原有的内容。***

可以用于多线程下载或多个线程同时写数据到文件。

[原文地址](http://www.cnblogs.com/oubo/archive/2012/01/06/2394638.html)

[java中的IO整理(非常全面)](http://www.cnblogs.com/rollenholt/archive/2011/09/11/2173787.html)


---
### 【序列化】

- 序列化和反序列化的概念
- 把对象转换为字节序列的过程称为对象的序列化。
- 把字节序列恢复为对象的过程称为对象的反序列化。

对象的序列化主要有两种用途：
- 1） 把对象的字节序列永久地保存到硬盘上，通常存放在一个文件中；
- 2） 在网络上传送对象的字节序列。

特点：
- 如果某个类能够被序列化，其子类也可以被序列化。
- 声明为static和transient类型的成员数据不能被序列化。因为static代表类的状态， transient代表对象的临时数据。
- java 对象序列化不仅保留一个对象的数据，而且递归保存对象引用的每个对象的数据。可以将整个对象层次写入字节流中，可以保存在文件中或在网络连接上传递。利用 对象序列化可以进行对象的"深复制"，即复制对象本身及引用的对象本身。序列化一个对象可能得到整个对象序列。

- 序列化的实现
将需要被序列化的类实现Serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流(如：FileOutputStream)来构造一个 ObjectOutputStream(对象流)对象，接着，使用ObjectOutputStream对象的writeObject(Object obj)方法就可以将参数为obj的对象写出(即保存其状态)，要恢复的话则用输入流。

- 修改默认的序列化机制： 
在序列化的过程中，有些数据字段我们不想将其序列化，对于此类字段我们只需要在定义时给它加上transient关键字即可，对于transient字段 序列化机制会跳过不会将其写入文件，当然也不可被恢复。但有时我们想将某一字段序列化，但它在SDK中的定义却是不可序列化的类型，这样的话我们也必须把 他标注为transient。

- serialVersionUID
需要被序列化的类实现Serializable接口，implements Serializable只是为了标注该对象是可被序列化的。

serialVersionUID的默认取值是Java运行时环境根据类的内部细节自动生成的。如果对类的源代码作了修改，再重新编译，新生成的类文件的serialVersionUID的取值有可能也会发生变化。

类的serialVersionUID的默认值完全依赖于Java编译器的实现，对于同一个类，用不同的Java编译器编译，有可能会导致不同的 serialVersionUID，也有可能相同。

**为了提高serialVersionUID的独立性和确定性，强烈建议在一个可序列化类中显示的定义serialVersionUID，为它赋予明确的值。**

显式地定义serialVersionUID有两种用途：
- 1、 在某些场合，希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有相同的serialVersionUID；
- 2、 在某些场合，不希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有不同的serialVersionUID。


---
### 【反射机制】

#### 什么是JAVA的反射机制
> Java反射是Java被视为动态（或准动态）语言的一个关键性质。这个机制允许程序在运行时透过Reflection APIs取得任何一个已知名称的class的内部信息，包括其modifiers（诸如public, static 等）、superclass（例如Object）、实现之interfaces（例如Cloneable），也包括fields和methods的所有信息，并可于运行时改变fields内容或唤起methods。

> Java反射机制容许程序在运行时加载、探知、使用编译期间完全未知的classes。换言之，Java可以加载一个运行时才得知名称的class，获得其完整结构。

#### JDK中提供的Reflection API
Java反射相关的API在包java.lang.reflect中，如下：

接口 | 功能
---|---
Member接口	| 该接口可以获取有关类成员（域或者方法）后者构造函数的信息。
AccessibleObject类 | 	该类是域(field)对象、方法(method)对象、构造函数(constructor)对象的基础类。它提供了将反射的对象标记为在使用时取消默认 Java 语言访问控制检查的能力。
Array类	| 该类提供动态地生成和访问JAVA数组的方法。
Constructor类 | 提供一个类的构造函数的信息以及访问类的构造函数的接口。
Field类	| 提供一个类的域的信息以及访问类的域的接口。
Method类 | 提供一个类的方法的信息以及访问类的方法的接口。
Modifier类 | 提供了 static 方法和常量，对类和成员访问修饰符进行解码。
Proxy类 | 	提供动态地生成代理类和类实例的静态方法。

#### JAVA反射机制提供了什么功能
##### 1、获取类的Class对象
Class 类的实例表示正在运行的 Java 应用程序中的类和接口。获取类的Class对象有多种方式：

- 调用getClass
```
Boolean var1 = true;
Class<?> classType2 = var1.getClass();
System.out.println(classType2);
输出：class java.lang.Boolean
```
- 运用.class 语法
```
Class<?> classType4 = Boolean.class;
System.out.println(classType4);
输出：class java.lang.Boolean
```
- 运用static method Class.forName()
```
Class<?> classType5 = Class.forName("java.lang.Boolean");
System.out.println(classType5);
输出：class java.lang.Boolean
```
- 运用primitive wrapper classes的TYPE 语法(这里返回的是原生类型，和Boolean.class返回的不同)
```
Class<?> classType3 = Boolean.TYPE;
System.out.println(classType3);        
输出：boolean
```

##### 2、获取类的Fields
可以通过反射机制得到某个类的某个属性，然后改变对应于这个类的某个实例的该属性值。JAVA 的Class<T>类提供了几个方法获取类的属性。

- public Field getField(String name)
> 返回一个 Field 对象，它反映此 Class 对象所表示的类或接口的指定公共成员字段
- public Field[] getFields()
> 返回一个包含某些 Field 对象的数组，这些对象反映此 Class 对象所表示的类或接口的所有可访问公共字段
- public Field getDeclaredField(Stringname)
> 返回一个 Field 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明字段
- public Field[] getDeclaredFields()
> 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段

**getFields和getDeclaredFields区别：**
- getFields返回的是申明为public的属性，包括父类中定义，
- getDeclaredFields返回的是指定类定义的所有定义的属性，不包括父类的。

##### 3、获取类的Method，调用指定方法
- 1、通过反射机制得到某个类的某个方法；
- 2、然后调用对应于这个类的某个实例的该方法；

Class<T>类提供了几个方法获取类的方法。
- public Method getMethod(String name,Class<?>... parameterTypes)
    > 返回一个 Method 对象，它反映此 Class 对象所表示的类或接口的指定公共成员方法
- public Method[] getMethods()
    > 返回一个包含某些 Method 对象的数组，这些对象反映此 Class 对象所表示的类或接口（包括那些由该类或接口声明的以及从超类和超接口继承的那些的类或接口）的公共 member 方法
- public Method getDeclaredMethod(Stringname,Class<?>... parameterTypes)
    > 返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
- public Method[] getDeclaredMethods()
    > 返回 Method 对象的一个数组，这些对象反映此 Class 对象表示的类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法

##### 4、获取类的Constructor
通过反射机制得到某个类的构造器，然后调用该构造器创建该类的一个实例 

Class<T>类提供了几个方法获取类的构造器。
- public Constructor<T> getConstructor(Class<?>... parameterTypes)
 >返回一个 Constructor 对象，它反映此 Class 对象所表示的类的指定公共构造方法
- public Constructor<?>[] getConstructors()
 >返回一个包含某些 Constructor 对象的数组，这些对象反映此 Class 对象所表示的类的所有公共构造方法
- public Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)
 >返回一个 Constructor 对象，该对象反映此 Class 对象所表示的类或接口的指定构造方法
- public Constructor<?>[] getDeclaredConstructors()
 >返回 Constructor 对象的一个数组，这些对象反映此 Class 对象表示的类声明的所有构造方法。它们是公共、保护、默认（包）访问和私有构造方法

##### 5、新建类的实例
通过反射机制创建新类的实例，有几种方法可以创建

- 调用无自变量ctor

```
1、调用类的Class对象的newInstance方法，该方法会调用对象的默认构造器，如果没有默认构造器，会调用失败.
Class<?> classType = ExtendType.class;
Object inst = classType.newInstance();
System.out.println(inst);
输出：
Type:Default Constructor
ExtendType:Default Constructor
com.quincy.ExtendType@d80be3
 
2、调用默认Constructor对象的newInstance方法
Class<?> classType = ExtendType.class;
Constructor<?> constructor1 = classType.getConstructor();
Object inst = constructor1.newInstance();
System.out.println(inst);
输出：
Type:Default Constructor
ExtendType:Default Constructor
com.quincy.ExtendType@1006d75
```

- 调用带参数ctor

```
3、调用带参数Constructor对象的newInstance方法
Constructor<?> constructor2 =
classType.getDeclaredConstructor(int.class, String.class);
Object inst = constructor2.newInstance(1, "123");
System.out.println(inst);
输出：
Type:Default Constructor
ExtendType:Constructor with parameters
com.quincy.ExtendType@15e83f9
```

##### 6、调用类的函数
通过反射获取类Method对象，调用Field的Invoke方法调用函数。

```
Class<?> classType = ExtendType.class;
Object inst = classType.newInstance();
Method logMethod = classType.<strong>getDeclaredMethod</strong>("Log", String.class);
logMethod.invoke(inst, "test");
 
输出：
Type:Default Constructor
ExtendType:Default Constructor
Class com.quincy.ClassT can not access a member of class com.quincy.ExtendType with modifiers "private"
 
上面失败是由于没有权限调用private函数，这里需要设置Accessible为true;
Class<?> classType = ExtendType.class;
Object inst = classType.newInstance();
Method logMethod = classType.getDeclaredMethod("Log", String.class);
logMethod.setAccessible(true);
logMethod.invoke(inst, "test");
```

##### 7、设置/获取类的属性值
通过反射获取类的Field对象，调用Field方法设置或获取值

```
Class<?> classType = ExtendType.class;
Object inst = classType.newInstance();
Field intField = classType.getField("pubIntExtendField");
intField.setInt(inst, 100);
int value = intField.getInt(inst);
```

---
### 【集合】

java集合总体为：List，Set，Map；都是接口由其子类去实现具体的方法；
- 1、 Map（键值对，键唯一）

    **HashMap**：hash表数据接口
    - 1、非synchronized，可以通过Collections.synchronizedMap(hashMap)实现synchronized，返回一个封装了底层方法的因此同步的Map；
    - 2、key或valye都可接收null，因此无key或key为null，则get()可能返回null，必须使用containsKey判断是否存在key；
    - 3、只适用单线程，效率高；

    **TreeMap**：二叉树数据结构
    - 1、非synchronized；
    - 2、key不可接收null，value可接收null；
    - 3、可按照key进行排序；
    - 4、只使用单线程；

    **HashTable**：hash表数据接口
    - 1、synchronized，每次读写，都会锁住整个结构；
    - 2、key或valye都不可接收null；
    - 3、多线程，效率较低；

    **ConcurrentHashMap**：key-value
    - 1、synchronized，将hash表默认拆分16个桶，每次get、put、remove操作只锁当前桶。写线程锁粒度细，读线程几乎不用锁，读写分离，速度快，只有size才会锁住整张hash表；
    - 2、key或valye都不可接收null；
    - 3、多线程，效率相对高；

- 2、Set（value，无序，不可重复，非synchronized）

    - **HashSet**：非synchronized，哈希表数据结构，根据hashCode和equals方法来确定元素的唯一性；
    - **TreeSet**：非synchronized，二叉树数据结构，可按照元素排序，默认自然循序，也可使用Comparable自定义排序；

- 3、List（value，有顺序，可重复，因为每个元素有单独索引）
    
    - **ArrayList**：非synchronized，数组数据结构；查询很快，增/删稍微慢点；
    - **LinkedList**：synchronized，数组数据结构，FIFO；
    - **Vector**：非synchronized，链表数据结构；查询慢，增/删很快；


---
### 【内部类】

- 内部类分类
    
    - **普通内部类**：
    > 普通内部类依赖于外部类，普通内部类与外部类是共生共死的，创建普通内部类的对象之前，必须先创建外部类的对象。
    
    ```
    // 正确
    Outer o = new Outer();
    Outer.Inner inner = o.new Inner();      
    
    // 错误
    Outer.Inner inner = new o.Inner();
    Outer.Inner inner = new Outer.Inner();
    ```

    - **静态内部类**：
    > 静态内部类没有外部对象的引用，所以它无法获得外部对象的资源，当然好处是，静态内部类无需依赖于外部类，它可以独立于外部对象而存在。
    
    ```
    // 创建静态内部类的代码
    Outer.Inner inner = new Outer.Inner();
    ```

- 静态内部类的使用场景
    - 1.外部类需要使用内部类，而内部类无需使用外部类的资源
    - 2.内部类可以独立外部类创建对象

    使用静态内部类的好处是加强了代码的封装性以及提高了代码的可读性

- 静态内部类和普通内部类的区别
    - 1.普通内部类不能声明static的方法和变量
    > 注意这里说的是变量，常量（也就是final static修饰的属性）还是可以的，而静态内部类形似外部类，没有任何限制。
    - 2.使用静态内部类，多个外部类的对象可以共享同一个内部类的对象。
    > 而使用普通内部类，每个外部类的对象都有自己的内部类对象，外部对象之间不能共享内部类的对象


---
### 【J2EE的核心API与组件】

> J2EE平台由一整套服务（Services）、应用程序接口（APIs）和协议构成，它对开发基于Web的多层应用提供了功能支持，下面对J2EE中的13种技术规范进行简单的描述(限于篇幅，这里只能进行简单的描述):

- 1、 **JDBC(Java Database Connectivity)**: JDBC API为访问不同的数据库提供了一种统一的途径，象ODBC一样，JDBC对开发者屏蔽了一些细节问题，另外，JDCB对数据库的访问也具有平台无关性。 
- 2、 **JNDI(Java Name and Directory Interface)**: JNDI API被用于执行名字和目录服务。它提供了一致的模型来存取和操作企业级的资源如DNS和LDAP，本地文件系统，或应用服务器中的对象。 
- 3、 **EJB(Enterprise JavaBean)**: J2EE技术之所以赢得某体广泛重视的原因之一就是EJB。它们提供了一个框架来开发和实施分布式商务逻辑，由此很显著地简化了具有可伸缩性和高度复杂的企业级应用的开发。EJB规范定义了EJB组件在何时如何与它们的容器进行交互作用。容器负责提供公用的服务，例如目录服务、事务管理、安全性、资源缓冲池以及容错性。但这里值得注意的是，EJB并不是实现J2EE的唯一途径。正是由于J2EE的开放性，使得有的厂商能够以一种和EJB平行的方式来达到同样的目的。 
- 4、 **RMI(Remote Method Invoke)**: 正如其名字所表示的那样，RMI协议调用远程对象上方法。它使用了序列化方式在客户端和服务器端传递数据。RMI是一种被EJB使用的更底层的协议。 
- 5、 **Java IDL/CORBA**: 在Java IDL的支持下，开发人员可以将Java和CORBA集成在一起。他们可以创建Java对象并使之可在CORBA ORB中展开, 或者他们还可以创建Java类并作为和其它ORB一起展开的CORBA对象的客户。后一种方法提供了另外一种途径，通过它Java可以被用于将你的新的应用和旧的系统相集成。 
- 6、 **JSP(Java Server Pages)**: JSP页面由HTML代码和嵌入其中的Java代码所组成。服务器在页面被客户端所请求以后对这些Java代码进行处理，然后将生成的HTML页面返回给客户端的浏览器。 
- 7、 **Java Servlet**: Servlet是一种小型的Java程序，它扩展了Web服务器的功能。作为一种服务器端的应用，当被请求时开始执行，这和CGI Perl脚本很相似。Servlet提供的功能大多与JSP类似，不过实现的方式不同。JSP通常是大多数HTML代码中嵌入少量的Java代码，而servlets全部由Java写成并且生成HTML。 
- 8、 **XML(Extensible Markup Language)**: XML是一种可以用来定义其它标记语言的语言。它被用来在不同的商务过程中共享数据。XML的发展和Java是相互独立的，但是，它和Java具有的相同目标正是平台独立性。通过将Java和XML的组合，您可以得到一个完美的具有平台独立性的解决方案。 
- 9、 **JMS(Java Message Service)**: MS是用于和面向消息的中间件相互通信的应用程序接口(API)。它既支持点对点的域，有支持发布/订阅(publish/subscribe)类型的域，并且提供对下列类型的支持：经认可的消息传递,事务型消息的传递，一致性消息和具有持久性的订阅者支持。JMS还提供了另一种方式来对您的应用与旧的后台系统相集成。 
- 10、 **JTA(Java Transaction Architecture)**: JTA定义了一种标准的API，应用系统由此可以访问各种事务监控。 
- 11、 **JTS(Java Transaction Service)**: JTS是CORBA OTS事务监控的基本的实现。JTS规定了事务管理器的实现方式。该事务管理器是在高层支持Java Transaction API (JTA)规范，并且在较底层实现OMG OTS specification的Java映像。JTS事务管理器为应用服务器、资源管理器、独立的应用以及通信资源管理器提供了事务服务。 
- 12、 **JavaMail**: JavaMail是用于存取邮件服务器的API，它提供了一套邮件服务器的抽象类。不仅支持SMTP服务器，也支持IMAP服务器。 
- 13、 **JTA(JavaBeans Activation Framework)**: JavaMail利用JAF来处理MIME编码的邮件附件。MIME的字节流可以被转换成Java对象，或者转换自Java对象。大多数应用都可以不需要直接使用JAF。 


---
### 【内存泄露 vs 内存溢出】

- 内存泄露 memory leak

是指程序在申请内存后，无法释放已申请的内存空间，一次内存泄露危害可以忽略，但内存泄露堆积后果很严重，无论多少内存,迟早会被占光。

内存泄漏是指你向系统申请分配内存进行使用(new)，可是使用完了以后却不归还(delete)，结果你申请到的那块内存你自己也不能再访问（也许你把它的地址给弄丢了），而系统也不能再次将它分配给需要的程序。

- 内存溢出out of memory

是指程序在申请内存时，没有足够的内存空间供其使用，出现out of memory；比如申请了一个integer,但给它存了long才能存下的数，那就是内存溢出。

内存溢出就是你要求分配的内存超出了系统能给你的，系统不能满足需求，于是产生溢出。 

比方说栈，栈满时再做进栈必定产生空间溢出，叫上溢，栈空时再做退栈也产生空间溢出，称为下溢。就是分配的内存不足以放下数据项序列,称为内存溢出. 


**memory leak会最终会导致out of memory！**

- 以发生的方式来分类，内存泄漏可以分为4类： 

    - 1、常发性内存泄漏。发生内存泄漏的代码会被多次执行到，每次被执行的时候都会导致一块内存泄漏。 
    - 2、偶发性内存泄漏。发生内存泄漏的代码只有在某些特定环境或操作过程下才会发生。常发性和偶发性是相对的。对于特定的环境，偶发性的也许就变成了常发性的。所以测试环境和测试方法对检测内存泄漏至关重要。 
    - 3、一次性内存泄漏。发生内存泄漏的代码只会被执行一次，或者由于算法上的缺陷，导致总会有一块仅且一块内存发生泄漏。比如，在类的构造函数中分配内存，在析构函数中却没有释放该内存，所以内存泄漏只会发生一次。 
    - 4、隐式内存泄漏。程序在运行过程中不停的分配内存，但是直到结束的时候才释放内存。严格的说这里并没有发生内存泄漏，因为最终程序释放了所有申请的内存。但是对于一个服务器程序，需要运行几天，几周甚至几个月，不及时释放内存也可能导致最终耗尽系统的所有内存。所以，我们称这类内存泄漏为隐式内存泄漏。

从用户使用程序的角度来看，内存泄漏本身不会产生什么危害，作为一般的用户，根本感觉不到内存泄漏的存在。真正有危害的是内存泄漏的堆积，这会最终消耗尽系统所有的内存。从这个角度来说，一次性内存泄漏并没有什么危害，因为它不会堆积，而隐式内存泄漏危害性则非常大，因为较之于常发性和偶发性内存泄漏它更难被检测到 


---
### 【Swing简介】

#### Swing简介
> Swing是一个用于开发Java应用程序用户界面的开发工具包。
以抽象窗口工具包（AWT）为基础使跨平台应用程序可以使用任何可插拔的外观风格。Swing开发人员只用很少的代码就可以利用Swing丰富、灵活的功能和模块化组件来创建优雅的用户界面。 工具包中所有的包都是以swing作为名称，例如javax.swing,javax.swing.event。

#### swing小结
**Java布局方式**
- java.awt FlowLayout ：将组件按从左到右而后从上到下的顺序依次排列，一行不能放完则折到下一行继续放置
- java.awt GridLayout ：形似一个无框线的表格，每个单元格中放一个组件
- java.awt BorderLayout： 将组件按东、南、西、北、中五个区域放置，每个方向最多只能放置一个组件
- java.awt GridBagLayout ：非常灵活，可指定组件放置的具体位置及占用单元格数目

**刷新JPanel**：UiIndexMain.indexmain.repaint();

**提示框：**
- JOptionPane.showMessageDialog(null, "这是一个简单的消息框");
- JOptionPane.showMessageDialog(null, "提示正文", "标题", - JOptionPane.ERROR_MESSAGE);
- JOptionPane.showConfirmDialog(null,"这是一个有三个按钮的确认框，\n按任意按钮返回");
- JOptionPane.showInputDialog(null,"这是一个可供用户输入信息的对话框");

**销毁窗口：**
- mainfFrame.setVisible(true);    // 登陆成功，主窗口可见
- loginFrame.dispose();    // 销毁登陆窗口

**java调用webkit内核，做自己的浏览器：... **

#### 实例A：Server启动器
```
package com.xxl.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {
	
	public JButton startBtn;
	public JButton stopBtn;
	public JButton exitBtn;
	
	public Main() {
		
		// 界面元素
		startBtn = new JButton("启动");
		startBtn.addActionListener(this);
		stopBtn = new JButton("停止");
		stopBtn.addActionListener(this);
		exitBtn = new JButton("退出");
		exitBtn.addActionListener(this);
		
		this.setLayout(new FlowLayout());
		this.add(startBtn);
		this.add(stopBtn);
		this.add(exitBtn);
		
		// 主界面
		this.setTitle("服务器");
		this.setSize(250, 300);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startBtn) {
			//MsgServer.getInstance().start();
			this.getContentPane().setBackground(Color.GREEN);
		} else if (e.getSource() == stopBtn) {
			//MsgServer.getInstance().stop();
			this.getContentPane().setBackground(Color.GRAY);
		} else if (e.getSource() == exitBtn) {
			System.exit(0);
		}
		
	}

	public static void main(String[] args) {
		new Main();
	}

}

```

#### 实例B：人脸识别，UI模块

```
package com.xxl.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * 首窗口
 * @author xuxueli
 *
 */
@SuppressWarnings("serial")
public class IndexUI extends JFrame{
	/**首窗口：实例*/
	public static IndexUI indexui;
	/**首窗口：标签面板*/
	public static JTabbedPane tabbedpane;
	
	/**
	 * 构造
	 */
	public IndexUI() {
		tabbedpane=new JTabbedPane();
		tabbedpane.add("人脸识别",getTabbedpaneFaceLogin());
		tabbedpane.add("视屏监控",getTabbedpaneVideoFind());
		this.add(tabbedpane);
		
		this.setTitle("人脸识别登录系统");
		this.setSize(1000,618);
		this.setLocationRelativeTo(null);//居中
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭
		this.setResizable(false); //不可调整大小
		this.setVisible(true);
		//overwrite windowClosing
		/*this.addWindowListener(
			new java.awt.event.WindowAdapter(){
				public void windowClosing(java.awt.event.WindowEvent evt) {
					
				}
		});*/

	
	}
	
	/**
	 * 标签窗口1:人脸识别
	 * @return
	 */
	private Component getTabbedpaneFaceLogin() {
		Panel p = new Panel();
		p.setBackground(Color.GRAY);
		return p;
	}

	/**
	 * 标签窗口2:视屏监控
	 * @return
	 */
	private Component getTabbedpaneVideoFind() {
		Panel p = new Panel();
		p.setBackground(Color.WHITE);
		return p;
	}
	

	/***** main测试 ************************************************************/
	public static void main(String[] args) 
	{
		indexui = new IndexUI();
	}
}


```
