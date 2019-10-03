---
### 反射机制

#### 一、什么是JAVA的反射机制
> Java反射是Java被视为动态（或准动态）语言的一个关键性质。这个机制允许程序在运行时透过Reflection APIs取得任何一个已知名称的class的内部信息，包括其modifiers（诸如public, static 等）、superclass（例如Object）、实现之interfaces（例如Cloneable），也包括fields和methods的所有信息，并可于运行时改变fields内容或唤起methods。

> Java反射机制容许程序在运行时加载、探知、使用编译期间完全未知的classes。换言之，Java可以加载一个运行时才得知名称的class，获得其完整结构。

#### 二、JDK中提供的Reflection API
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

#### 三、JAVA反射机制提供了什么功能
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
### 集合

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
### 内部类

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
### J2EE的核心API与组件

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
### 内存泄露 vs 内存溢出

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