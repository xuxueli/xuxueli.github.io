##### 一、什么是JAVA的反射机制
> Java反射是Java被视为动态（或准动态）语言的一个关键性质。这个机制允许程序在运行时透过Reflection APIs取得任何一个已知名称的class的内部信息，包括其modifiers（诸如public, static 等）、superclass（例如Object）、实现之interfaces（例如Cloneable），也包括fields和methods的所有信息，并可于运行时改变fields内容或唤起methods。

> Java反射机制容许程序在运行时加载、探知、使用编译期间完全未知的classes。换言之，Java可以加载一个运行时才得知名称的class，获得其完整结构。

##### 二、JDK中提供的Reflection API
Java反射相关的API在包java.lang.reflect中，JDK 1.6.0的reflect包如下图：

![image](http://images.cnblogs.com/cnblogs_com/Quincy/201106/201106191021251045.png)


接口 | 功能
--|--
Member接口	| 该接口可以获取有关类成员（域或者方法）后者构造函数的信息。
AccessibleObject类 | 	该类是域(field)对象、方法(method)对象、构造函数(constructor)对象的基础类。它提供了将反射的对象标记为在使用时取消默认 Java 语言访问控制检查的能力。
Array类	| 该类提供动态地生成和访问JAVA数组的方法。
Constructor类 | 提供一个类的构造函数的信息以及访问类的构造函数的接口。
Field类	| 提供一个类的域的信息以及访问类的域的接口。
Method类 | 提供一个类的方法的信息以及访问类的方法的接口。
Modifier类 | 提供了 static 方法和常量，对类和成员访问修饰符进行解码。
Proxy类 | 	提供动态地生成代理类和类实例的静态方法。

##### 三、JAVA反射机制提供了什么功能
###### 1、获取类的Class对象
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
