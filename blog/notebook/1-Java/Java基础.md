<h2 style="color:#4db6ac !important" >Java基础</h2>
> 本文内容来源于书籍和网络。
 
[TOCM]
 
[TOC]

# 一、数据类型

## 基本类型

- byte/8
- char/16
- short/16
- int/32
- float/32
- long/64
- double/64
- boolean/\~

boolean 只有两个值：true、false，可以使用 1 bit 来存储，但是具体大小没有明确规定。JVM 会在编译时期将 boolean 类型的数据转换为 int，使用 1 来表示 true，0 表示 false。JVM 支持 boolean 数组，但是是通过读写 byte 数组来实现的。

- [Primitive Data Types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html)
- [The Java® Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se8/jvms8.pdf)

## 包装类型

基本类型都有对应的包装类型，基本类型与其对应的包装类型之间的赋值使用自动装箱与拆箱完成。

```java
Integer x = 2;     // 装箱 调用了 Integer.valueOf(2)
int y = x;         // 拆箱 调用了 X.intValue()
```

- [Autoboxing and Unboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html)

## 缓存池

new Integer(123) 与 Integer.valueOf(123) 的区别在于：

- new Integer(123) 每次都会新建一个对象；
- Integer.valueOf(123) 会使用缓存池中的对象，多次调用会取得同一个对象的引用。

```java
Integer x = new Integer(123);
Integer y = new Integer(123);
System.out.println(x == y);    // false
Integer z = Integer.valueOf(123);
Integer k = Integer.valueOf(123);
System.out.println(z == k);   // true
```

valueOf() 方法的实现比较简单，就是先判断值是否在缓存池中，如果在的话就直接返回缓存池的内容。

```java
public static Integer valueOf(int i) {
   if (i >= IntegerCache.low && i <= IntegerCache.high)
       return IntegerCache.cache[i + (-IntegerCache.low)];
   return new Integer(i);
}
```

在 Java 8 中，Integer 缓存池的大小默认为 -128\~127。

```java
static final int low = -128;
static final int high;
static final Integer cache[];

static {
   // high value may be configured by property
   int h = 127;
   String integerCacheHighPropValue =
       sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
   if (integerCacheHighPropValue != null) {
       try {
           int i = parseInt(integerCacheHighPropValue);
           i = Math.max(i, 127);
           // Maximum array size is Integer.MAX_VALUE
           h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
       } catch( NumberFormatException nfe) {
           // If the property cannot be parsed into an int, ignore it.
       }
   }
   high = h;

   cache = new Integer[(high - low) + 1];
   int j = low;
   for(int k = 0; k < cache.length; k++)
       cache[k] = new Integer(j++);

   // range [-128, 127] must be interned (JLS7 5.1.7)
   assert IntegerCache.high >= 127;
}
```

编译器会在自动装箱过程调用 valueOf() 方法，因此多个值相同且值在缓存池范围内的 Integer 实例使用自动装箱来创建，那么就会引用相同的对象。

```java
Integer m = 123;
Integer n = 123;
System.out.println(m == n); // true
```

基本类型对应的缓冲池如下：

- boolean values true and false
- all byte values
- short values between -128 and 127
- int values between -128 and 127
- char in the range \u0000 to \u007F

在使用这些基本类型对应的包装类型时，如果该数值范围在缓冲池范围内，就可以直接使用缓冲池中的对象。

在 jdk 1.8 所有的数值类缓冲池中，Integer 的缓冲池 IntegerCache 很特殊，这个缓冲池的下界是 - 128，上界默认是 127，但是这个上界是可调的，在启动 jvm 的时候，通过 -XX:AutoBoxCacheMax=&lt;size&gt; 来指定这个缓冲池的大小，该选项在 JVM 初始化的时候会设定一个名为 java.lang.IntegerCache.high 系统属性，然后 IntegerCache 初始化的时候就会读取该系统属性来决定上界。

[StackOverflow : Differences between new Integer(123), Integer.valueOf(123) and just 123
](https://stackoverflow.com/questions/9030817/differences-between-new-integer123-integer-valueof123-and-just-123)

# 二、String

## 概览

String 被声明为 final，因此它不可被继承。(Integer 等包装类也不能被继承）

在 Java 8 中，String 内部使用 char 数组存储数据。

```java
public final class String
   implements java.io.Serializable, Comparable<String>, CharSequence {
   /** The value is used for character storage. */
   private final char value[];
}
```

在 Java 9 之后，String 类的实现改用 byte 数组存储字符串，同时使用 `coder` 来标识使用了哪种编码。

```java
public final class String
   implements java.io.Serializable, Comparable<String>, CharSequence {
   /** The value is used for character storage. */
   private final byte[] value;

   /** The identifier of the encoding used to encode the bytes in {@code value}. */
   private final byte coder;
}
```

value 数组被声明为 final，这意味着 value 数组初始化之后就不能再引用其它数组。并且 String 内部没有改变 value 数组的方法，因此可以保证 String 不可变。

## 不可变的好处

**1. 可以缓存 hash 值**  

因为 String 的 hash 值经常被使用，例如 String 用做 HashMap 的 key。不可变的特性可以使得 hash 值也不可变，因此只需要进行一次计算。

**2. String Pool 的需要**  

如果一个 String 对象已经被创建过了，那么就会从 String Pool 中取得引用。只有 String 是不可变的，才可能使用 String Pool。

<div align="center"> <img src="https://www.xuxueli.com/blog/static/images/img_119.png"/> </div>

**3. 安全性**  

String 经常作为参数，String 不可变性可以保证参数不可变。例如在作为网络连接参数的情况下如果 String 是可变的，那么在网络连接过程中，String 被改变，改变 String 的那一方以为现在连接的是其它主机，而实际情况却不一定是。

**4. 线程安全**  

String 不可变性天生具备线程安全，可以在多个线程中安全地使用。

[Program Creek : Why String is immutable in Java?](https://www.programcreek.com/2013/04/why-string-is-immutable-in-java/)

## String, StringBuffer and StringBuilder

**1. 可变性**  

- String 不可变
- StringBuffer 和 StringBuilder 可变

**2. 线程安全**  

- String 不可变，因此是线程安全的
- StringBuilder 不是线程安全的
- StringBuffer 是线程安全的，内部使用 synchronized 进行同步

[StackOverflow : String, StringBuffer, and StringBuilder](https://stackoverflow.com/questions/2971315/string-stringbuffer-and-stringbuilder)

## String Pool

字符串常量池（String Pool）保存着所有字符串字面量（literal strings），这些字面量在编译时期就确定。不仅如此，还可以使用 String 的 intern() 方法在运行过程将字符串添加到 String Pool 中。

当一个字符串调用 intern() 方法时，如果 String Pool 中已经存在一个字符串和该字符串值相等（使用 equals() 方法进行确定），那么就会返回 String Pool 中字符串的引用；否则，就会在 String Pool 中添加一个新的字符串，并返回这个新字符串的引用。

下面示例中，s1 和 s2 采用 new String() 的方式新建了两个不同字符串，而 s3 和 s4 是通过 s1.intern() 方法取得同一个字符串引用。intern() 首先把 s1 引用的字符串放到 String Pool 中，然后返回这个字符串引用。因此 s3 和 s4 引用的是同一个字符串。

```java
String s1 = new String("aaa");
String s2 = new String("aaa");
System.out.println(s1 == s2);           // false
String s3 = s1.intern();
String s4 = s1.intern();
System.out.println(s3 == s4);           // true
```

如果是采用 "bbb" 这种字面量的形式创建字符串，会自动地将字符串放入 String Pool 中。

```java
String s5 = "bbb";
String s6 = "bbb";
System.out.println(s5 == s6);  // true
```

在 Java 7 之前，String Pool 被放在运行时常量池中，它属于永久代。而在 Java 7，String Pool 被移到堆中。这是因为永久代的空间有限，在大量使用字符串的场景下会导致 OutOfMemoryError 错误。

- [StackOverflow : What is String interning?](https://stackoverflow.com/questions/10578984/what-is-string-interning)
- [深入解析 String#intern](https://tech.meituan.com/in_depth_understanding_string_intern.html)

## new String("abc")

使用这种方式一共会创建两个字符串对象（前提是 String Pool 中还没有 "abc" 字符串对象）。

- "abc" 属于字符串字面量，因此编译时期会在 String Pool 中创建一个字符串对象，指向这个 "abc" 字符串字面量；
- 而使用 new 的方式会在堆中创建一个字符串对象。

创建一个测试类，其 main 方法中使用这种方式来创建字符串对象。

```java
public class NewStringTest {
   public static void main(String[] args) {
       String s = new String("abc");
   }
}
```

使用 javap -verbose 进行反编译，得到以下内容：

```java
// ...
Constant pool:
// ...
  #2 = Class              #18            // java/lang/String
  #3 = String             #19            // abc
// ...
 #18 = Utf8               java/lang/String
 #19 = Utf8               abc
// ...

 public static void main(java.lang.String[]);
   descriptor: ([Ljava/lang/String;)V
   flags: ACC_PUBLIC, ACC_STATIC
   Code:
     stack=3, locals=2, args_size=1
        0: new           #2                  // class java/lang/String
        3: dup
        4: ldc           #3                  // String abc
        6: invokespecial #4                  // Method java/lang/String."<init>":(Ljava/lang/String;)V
        9: astore_1
// ...
```

在 Constant Pool 中，#19 存储这字符串字面量 "abc"，#3 是 String Pool 的字符串对象，它指向 #19 这个字符串字面量。在 main 方法中，0: 行使用 new #2 在堆中创建一个字符串对象，并且使用 ldc #3 将 String Pool 中的字符串对象作为 String 构造函数的参数。

以下是 String 构造函数的源码，可以看到，在将一个字符串对象作为另一个字符串对象的构造函数参数时，并不会完全复制 value 数组内容，而是都会指向同一个 value 数组。

```java
public String(String original) {
   this.value = original.value;
   this.hash = original.hash;
}
```

# 三、运算

## 参数传递

Java 的参数是以值传递的形式传入方法中，而不是引用传递。

以下代码中 Dog dog 的 dog 是一个指针，存储的是对象的地址。在将一个参数传入一个方法时，本质上是将对象的地址以值的方式传递到形参中。

```java
public class Dog {

   String name;

   Dog(String name) {
       this.name = name;
   }

   String getName() {
       return this.name;
   }

   void setName(String name) {
       this.name = name;
   }

   String getObjectAddress() {
       return super.toString();
   }
}
```

在方法中改变对象的字段值会改变原对象该字段值，因为引用的是同一个对象。

```java
class PassByValueExample {
   public static void main(String[] args) {
       Dog dog = new Dog("A");
       func(dog);
       System.out.println(dog.getName());          // B
   }

   private static void func(Dog dog) {
       dog.setName("B");
   }
}
```

但是在方法中将指针引用了其它对象，那么此时方法里和方法外的两个指针指向了不同的对象，在一个指针改变其所指向对象的内容对另一个指针所指向的对象没有影响。

```java
public class PassByValueExample {
   public static void main(String[] args) {
       Dog dog = new Dog("A");
       System.out.println(dog.getObjectAddress()); // Dog@4554617c
       func(dog);
       System.out.println(dog.getObjectAddress()); // Dog@4554617c
       System.out.println(dog.getName());          // A
   }

   private static void func(Dog dog) {
       System.out.println(dog.getObjectAddress()); // Dog@4554617c
       dog = new Dog("B");
       System.out.println(dog.getObjectAddress()); // Dog@74a14482
       System.out.println(dog.getName());          // B
   }
}
```

[StackOverflow: Is Java “pass-by-reference” or “pass-by-value”?](https://stackoverflow.com/questions/40480/is-java-pass-by-reference-or-pass-by-value)

## float 与 double

Java 不能隐式执行向下转型，因为这会使得精度降低。

1.1 字面量属于 double 类型，不能直接将 1.1 直接赋值给 float 变量，因为这是向下转型。

```java
// float f = 1.1;
```

1.1f 字面量才是 float 类型。

```java
float f = 1.1f;
```

## 隐式类型转换

因为字面量 1 是 int 类型，它比 short 类型精度要高，因此不能隐式地将 int 类型向下转型为 short 类型。

```java
short s1 = 1;
// s1 = s1 + 1;
```

但是使用 += 或者 ++ 运算符会执行隐式类型转换。

```java
s1 += 1;
s1++;
```

上面的语句相当于将 s1 + 1 的计算结果进行了向下转型：

```java
s1 = (short) (s1 + 1);
```

[StackOverflow : Why don't Java's +=, -=, *=, /= compound assignment operators require casting?](https://stackoverflow.com/questions/8710619/why-dont-javas-compound-assignment-operators-require-casting)

## switch

从 Java 7 开始，可以在 switch 条件判断语句中使用 String 对象。

```java
String s = "a";
switch (s) {
   case "a":
       System.out.println("aaa");
       break;
   case "b":
       System.out.println("bbb");
       break;
}
```

switch 不支持 long，是因为 switch 的设计初衷是对那些只有少数几个值的类型进行等值判断，如果值过于复杂，那么还是用 if 比较合适。

```java
// long x = 111;
// switch (x) { // Incompatible types. Found: 'long', required: 'char, byte, short, int, Character, Byte, Short, Integer, String, or an enum'
//     case 111:
//         System.out.println(111);
//         break;
//     case 222:
//         System.out.println(222);
//         break;
// }
```

[StackOverflow : Why can't your switch statement data type be long, Java?](https://stackoverflow.com/questions/2676210/why-cant-your-switch-statement-data-type-be-long-java)


# 四、关键字

## final

**1. 数据**  

声明数据为常量，可以是编译时常量，也可以是在运行时被初始化后不能被改变的常量。

- 对于基本类型，final 使数值不变；
- 对于引用类型，final 使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的。

```java
final int x = 1;
// x = 2;  // cannot assign value to final variable 'x'
final A y = new A();
y.a = 1;
```

**2. 方法**  

声明方法不能被子类重写。

private 方法隐式地被指定为 final，如果在子类中定义的方法和基类中的一个 private 方法签名相同，此时子类的方法不是重写基类方法，而是在子类中定义了一个新的方法。

**3. 类**  

声明类不允许被继承。

## static

**1. 静态变量**  

- 静态变量：又称为类变量，也就是说这个变量属于类的，类所有的实例都共享静态变量，可以直接通过类名来访问它。静态变量在内存中只存在一份。
- 实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。

```java
public class A {

   private int x;         // 实例变量
   private static int y;  // 静态变量

   public static void main(String[] args) {
       // int x = A.x;  // Non-static field 'x' cannot be referenced from a static context
       A a = new A();
       int x = a.x;
       int y = A.y;
   }
}
```

**2. 静态方法**  

静态方法在类加载的时候就存在了，它不依赖于任何实例。所以静态方法必须有实现，也就是说它不能是抽象方法。

```java
public abstract class A {
   public static void func1(){
   }
   // public abstract static void func2();  // Illegal combination of modifiers: 'abstract' and 'static'
}
```

只能访问所属类的静态字段和静态方法，方法中不能有 this 和 super 关键字，因此这两个关键字与具体对象关联。

```java
public class A {

   private static int x;
   private int y;

   public static void func1(){
       int a = x;
       // int b = y;  // Non-static field 'y' cannot be referenced from a static context
       // int b = this.y;     // 'A.this' cannot be referenced from a static context
   }
}
```

**3. 静态语句块**  

静态语句块在类初始化时运行一次。

```java
public class A {
   static {
       System.out.println("123");
   }

   public static void main(String[] args) {
       A a1 = new A();
       A a2 = new A();
   }
}
```

```html
123
```

**4. 静态内部类**  

非静态内部类依赖于外部类的实例，也就是说需要先创建外部类实例，才能用这个实例去创建非静态内部类。而静态内部类不需要。

```java
public class OuterClass {

   class InnerClass {
   }

   static class StaticInnerClass {
   }

   public static void main(String[] args) {
       // InnerClass innerClass = new InnerClass(); // 'OuterClass.this' cannot be referenced from a static context
       OuterClass outerClass = new OuterClass();
       InnerClass innerClass = outerClass.new InnerClass();
       StaticInnerClass staticInnerClass = new StaticInnerClass();
   }
}
```

静态内部类不能访问外部类的非静态的变量和方法。

**5. 静态导包**  

在使用静态变量和方法时不用再指明 ClassName，从而简化代码，但可读性大大降低。

```java
import static com.xxx.ClassName.*
```

**6. 初始化顺序**  

静态变量和静态语句块优先于实例变量和普通语句块，静态变量和静态语句块的初始化顺序取决于它们在代码中的顺序。

```java
public static String staticField = "静态变量";
```

```java
static {
   System.out.println("静态语句块");
}
```

```java
public String field = "实例变量";
```

```java
{
   System.out.println("普通语句块");
}
```

最后才是构造函数的初始化。

```java
public InitialOrderTest() {
   System.out.println("构造函数");
}
```

存在继承的情况下，初始化顺序为：

- 父类（静态变量、静态语句块）
- 子类（静态变量、静态语句块）
- 父类（实例变量、普通语句块）
- 父类（构造函数）
- 子类（实例变量、普通语句块）
- 子类（构造函数）

# 五、Object 通用方法

## 概览

```java

public native int hashCode()

public boolean equals(Object obj)

protected native Object clone() throws CloneNotSupportedException

public String toString()

public final native Class<?> getClass()

protected void finalize() throws Throwable {}

public final native void notify()

public final native void notifyAll()

public final native void wait(long timeout) throws InterruptedException

public final void wait(long timeout, int nanos) throws InterruptedException

public final void wait() throws InterruptedException
```

## equals()

**1. 等价关系**  

两个对象具有等价关系，需要满足以下五个条件：

Ⅰ 自反性

```java
x.equals(x); // true
```

Ⅱ 对称性

```java
x.equals(y) == y.equals(x); // true
```

Ⅲ 传递性

```java
if (x.equals(y) && y.equals(z))
   x.equals(z); // true;
```

Ⅳ 一致性

多次调用 equals() 方法结果不变

```java
x.equals(y) == x.equals(y); // true
```

Ⅴ 与 null 的比较

对任何不是 null 的对象 x 调用 x.equals(null) 结果都为 false

```java
x.equals(null); // false;
```

**2. 等价与相等**  

- 对于基本类型，== 判断两个值是否相等，基本类型没有 equals() 方法。
- 对于引用类型，== 判断两个变量是否引用同一个对象，而 equals() 判断引用的对象是否等价。

```java
Integer x = new Integer(1);
Integer y = new Integer(1);
System.out.println(x.equals(y)); // true
System.out.println(x == y);      // false
```

**3. 实现**  

- 检查是否为同一个对象的引用，如果是直接返回 true；
- 检查是否是同一个类型，如果不是，直接返回 false；
- 将 Object 对象进行转型；
- 判断每个关键域是否相等。

```java
public class EqualExample {

   private int x;
   private int y;
   private int z;

   public EqualExample(int x, int y, int z) {
       this.x = x;
       this.y = y;
       this.z = z;
   }

   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;

       EqualExample that = (EqualExample) o;

       if (x != that.x) return false;
       if (y != that.y) return false;
       return z == that.z;
   }
}
```

## hashCode()

hashCode() 返回哈希值，而 equals() 是用来判断两个对象是否等价。等价的两个对象散列值一定相同，但是散列值相同的两个对象不一定等价，这是因为计算哈希值具有随机性，两个值不同的对象可能计算出相同的哈希值。

在覆盖 equals() 方法时应当总是覆盖 hashCode() 方法，保证等价的两个对象哈希值也相等。

HashSet  和 HashMap 等集合类使用了 hashCode()  方法来计算对象应该存储的位置，因此要将对象添加到这些集合类中，需要让对应的类实现 hashCode()  方法。

下面的代码中，新建了两个等价的对象，并将它们添加到 HashSet 中。我们希望将这两个对象当成一样的，只在集合中添加一个对象。但是 EqualExample 没有实现 hashCode() 方法，因此这两个对象的哈希值是不同的，最终导致集合添加了两个等价的对象。

```java
EqualExample e1 = new EqualExample(1, 1, 1);
EqualExample e2 = new EqualExample(1, 1, 1);
System.out.println(e1.equals(e2)); // true
HashSet<EqualExample> set = new HashSet<>();
set.add(e1);
set.add(e2);
System.out.println(set.size());   // 2
```

理想的哈希函数应当具有均匀性，即不相等的对象应当均匀分布到所有可能的哈希值上。这就要求了哈希函数要把所有域的值都考虑进来。可以将每个域都当成 R 进制的某一位，然后组成一个 R 进制的整数。

R 一般取 31，因为它是一个奇素数，如果是偶数的话，当出现乘法溢出，信息就会丢失，因为与 2 相乘相当于向左移一位，最左边的位丢失。并且一个数与 31 相乘可以转换成移位和减法：`31*x == (x<<5)-x`，编译器会自动进行这个优化。

```java
@Override
public int hashCode() {
   int result = 17;
   result = 31 * result + x;
   result = 31 * result + y;
   result = 31 * result + z;
   return result;
}
```

## toString()

默认返回 ToStringExample@4554617c 这种形式，其中 @ 后面的数值为散列码的无符号十六进制表示。

```java
public class ToStringExample {

   private int number;

   public ToStringExample(int number) {
       this.number = number;
   }
}
```

```java
ToStringExample example = new ToStringExample(123);
System.out.println(example.toString());
```

```html
ToStringExample@4554617c
```

## clone()

**1. cloneable**  

clone() 是 Object 的 protected 方法，它不是 public，一个类不显式去重写 clone()，其它类就不能直接去调用该类实例的 clone() 方法。

```java
public class CloneExample {
   private int a;
   private int b;
}
```

```java
CloneExample e1 = new CloneExample();
// CloneExample e2 = e1.clone(); // 'clone()' has protected access in 'java.lang.Object'
```

重写 clone() 得到以下实现：

```java
public class CloneExample {
   private int a;
   private int b;

   @Override
   public CloneExample clone() throws CloneNotSupportedException {
       return (CloneExample)super.clone();
   }
}
```

```java
CloneExample e1 = new CloneExample();
try {
   CloneExample e2 = e1.clone();
} catch (CloneNotSupportedException e) {
   e.printStackTrace();
}
```

```html
java.lang.CloneNotSupportedException: CloneExample
```

以上抛出了 CloneNotSupportedException，这是因为 CloneExample 没有实现 Cloneable 接口。

应该注意的是，clone() 方法并不是 Cloneable 接口的方法，而是 Object 的一个 protected 方法。Cloneable 接口只是规定，如果一个类没有实现 Cloneable 接口又调用了 clone() 方法，就会抛出 CloneNotSupportedException。

```java
public class CloneExample implements Cloneable {
   private int a;
   private int b;

   @Override
   public Object clone() throws CloneNotSupportedException {
       return super.clone();
   }
}
```

**2. 浅拷贝**  

拷贝对象和原始对象的引用类型引用同一个对象。

```java
public class ShallowCloneExample implements Cloneable {

   private int[] arr;

   public ShallowCloneExample() {
       arr = new int[10];
       for (int i = 0; i < arr.length; i++) {
           arr[i] = i;
       }
   }

   public void set(int index, int value) {
       arr[index] = value;
   }

   public int get(int index) {
       return arr[index];
   }

   @Override
   protected ShallowCloneExample clone() throws CloneNotSupportedException {
       return (ShallowCloneExample) super.clone();
   }
}
```

```java
ShallowCloneExample e1 = new ShallowCloneExample();
ShallowCloneExample e2 = null;
try {
   e2 = e1.clone();
} catch (CloneNotSupportedException e) {
   e.printStackTrace();
}
e1.set(2, 222);
System.out.println(e2.get(2)); // 222
```

**3. 深拷贝**  

拷贝对象和原始对象的引用类型引用不同对象。

```java
public class DeepCloneExample implements Cloneable {

   private int[] arr;

   public DeepCloneExample() {
       arr = new int[10];
       for (int i = 0; i < arr.length; i++) {
           arr[i] = i;
       }
   }

   public void set(int index, int value) {
       arr[index] = value;
   }

   public int get(int index) {
       return arr[index];
   }

   @Override
   protected DeepCloneExample clone() throws CloneNotSupportedException {
       DeepCloneExample result = (DeepCloneExample) super.clone();
       result.arr = new int[arr.length];
       for (int i = 0; i < arr.length; i++) {
           result.arr[i] = arr[i];
       }
       return result;
   }
}
```

```java
DeepCloneExample e1 = new DeepCloneExample();
DeepCloneExample e2 = null;
try {
   e2 = e1.clone();
} catch (CloneNotSupportedException e) {
   e.printStackTrace();
}
e1.set(2, 222);
System.out.println(e2.get(2)); // 2
```

**4. clone() 的替代方案**  

使用 clone() 方法来拷贝一个对象即复杂又有风险，它会抛出异常，并且还需要类型转换。Effective Java 书上讲到，最好不要去使用 clone()，可以使用拷贝构造函数或者拷贝工厂来拷贝一个对象。

```java
public class CloneConstructorExample {

   private int[] arr;

   public CloneConstructorExample() {
       arr = new int[10];
       for (int i = 0; i < arr.length; i++) {
           arr[i] = i;
       }
   }

   public CloneConstructorExample(CloneConstructorExample original) {
       arr = new int[original.arr.length];
       for (int i = 0; i < original.arr.length; i++) {
           arr[i] = original.arr[i];
       }
   }

   public void set(int index, int value) {
       arr[index] = value;
   }

   public int get(int index) {
       return arr[index];
   }
}
```

```java
CloneConstructorExample e1 = new CloneConstructorExample();
CloneConstructorExample e2 = new CloneConstructorExample(e1);
e1.set(2, 222);
System.out.println(e2.get(2)); // 2
```

# 六、继承

## 访问权限

Java 中有三个访问权限修饰符：private、protected 以及 public，如果不加访问修饰符，表示包级可见。

可以对类或类中的成员（字段和方法）加上访问修饰符。

- 类可见表示其它类可以用这个类创建实例对象。
- 成员可见表示其它类可以用这个类的实例对象访问到该成员；

protected 用于修饰成员，表示在继承体系中成员对于子类可见，但是这个访问修饰符对于类没有意义。

设计良好的模块会隐藏所有的实现细节，把它的 API 与它的实现清晰地隔离开来。模块之间只通过它们的 API 进行通信，一个模块不需要知道其他模块的内部工作情况，这个概念被称为信息隐藏或封装。因此访问权限应当尽可能地使每个类或者成员不被外界访问。

如果子类的方法重写了父类的方法，那么子类中该方法的访问级别不允许低于父类的访问级别。这是为了确保可以使用父类实例的地方都可以使用子类实例去代替，也就是确保满足里氏替换原则。

字段决不能是公有的，因为这么做的话就失去了对这个字段修改行为的控制，客户端可以对其随意修改。例如下面的例子中，AccessExample 拥有 id 公有字段，如果在某个时刻，我们想要使用 int 存储 id 字段，那么就需要修改所有的客户端代码。

```java
public class AccessExample {
   public String id;
}
```

可以使用公有的 getter 和 setter 方法来替换公有字段，这样的话就可以控制对字段的修改行为。

```java
public class AccessExample {

   private int id;

   public String getId() {
       return id + "";
   }

   public void setId(String id) {
       this.id = Integer.valueOf(id);
   }
}
```

但是也有例外，如果是包级私有的类或者私有的嵌套类，那么直接暴露成员不会有特别大的影响。

```java
public class AccessWithInnerClassExample {

   private class InnerClass {
       int x;
   }

   private InnerClass innerClass;

   public AccessWithInnerClassExample() {
       innerClass = new InnerClass();
   }

   public int getValue() {
       return innerClass.x;  // 直接访问
   }
}
```

## 抽象类与接口

**1. 抽象类**  

抽象类和抽象方法都使用 abstract 关键字进行声明。如果一个类中包含抽象方法，那么这个类必须声明为抽象类。

抽象类和普通类最大的区别是，抽象类不能被实例化，只能被继承。

```java
public abstract class AbstractClassExample {

   protected int x;
   private int y;

   public abstract void func1();

   public void func2() {
       System.out.println("func2");
   }
}
```

```java
public class AbstractExtendClassExample extends AbstractClassExample {
   @Override
   public void func1() {
       System.out.println("func1");
   }
}
```

```java
// AbstractClassExample ac1 = new AbstractClassExample(); // 'AbstractClassExample' is abstract; cannot be instantiated
AbstractClassExample ac2 = new AbstractExtendClassExample();
ac2.func1();
```

**2. 接口**  

接口是抽象类的延伸，在 Java 8 之前，它可以看成是一个完全抽象的类，也就是说它不能有任何的方法实现。

从 Java 8 开始，接口也可以拥有默认的方法实现，这是因为不支持默认方法的接口的维护成本太高了。在 Java 8 之前，如果一个接口想要添加新的方法，那么要修改所有实现了该接口的类，让它们都实现新增的方法。

接口的成员（字段 + 方法）默认都是 public 的，并且不允许定义为 private 或者 protected。

接口的字段默认都是 static 和 final 的。

```java
public interface InterfaceExample {

   void func1();

   default void func2(){
       System.out.println("func2");
   }

   int x = 123;
   // int y;               // Variable 'y' might not have been initialized
   public int z = 0;       // Modifier 'public' is redundant for interface fields
   // private int k = 0;   // Modifier 'private' not allowed here
   // protected int l = 0; // Modifier 'protected' not allowed here
   // private void fun3(); // Modifier 'private' not allowed here
}
```

```java
public class InterfaceImplementExample implements InterfaceExample {
   @Override
   public void func1() {
       System.out.println("func1");
   }
}
```

```java
// InterfaceExample ie1 = new InterfaceExample(); // 'InterfaceExample' is abstract; cannot be instantiated
InterfaceExample ie2 = new InterfaceImplementExample();
ie2.func1();
System.out.println(InterfaceExample.x);
```

**3. 比较**  

- 从设计层面上看，抽象类提供了一种 IS-A 关系，需要满足里式替换原则，即子类对象必须能够替换掉所有父类对象。而接口更像是一种 LIKE-A 关系，它只是提供一种方法实现契约，并不要求接口和实现接口的类具有 IS-A 关系。
- 从使用上来看，一个类可以实现多个接口，但是不能继承多个抽象类。
- 接口的字段只能是 static 和 final 类型的，而抽象类的字段没有这种限制。
- 接口的成员只能是 public 的，而抽象类的成员可以有多种访问权限。

**4. 使用选择**  

使用接口：

- 需要让不相关的类都实现一个方法，例如不相关的类都可以实现 Compareable 接口中的 compareTo() 方法；
- 需要使用多重继承。

使用抽象类：

- 需要在几个相关的类中共享代码。
- 需要能控制继承来的成员的访问权限，而不是都为 public。
- 需要继承非静态和非常量字段。

在很多情况下，接口优先于抽象类。因为接口没有抽象类严格的类层次结构要求，可以灵活地为一个类添加行为。并且从 Java 8 开始，接口也可以有默认的方法实现，使得修改接口的成本也变的很低。

- [Abstract Methods and Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html)
- [深入理解 abstract class 和 interface](https://www.ibm.com/developerworks/cn/java/l-javainterface-abstract/)
- [When to Use Abstract Class and Interface](https://dzone.com/articles/when-to-use-abstract-class-and-intreface)


## super

- 访问父类的构造函数：可以使用 super() 函数访问父类的构造函数，从而委托父类完成一些初始化的工作。应该注意到，子类一定会调用父类的构造函数来完成初始化工作，一般是调用父类的默认构造函数，如果子类需要调用父类其它构造函数，那么就可以使用 super() 函数。
- 访问父类的成员：如果子类重写了父类的某个方法，可以通过使用 super 关键字来引用父类的方法实现。

```java
public class SuperExample {

   protected int x;
   protected int y;

   public SuperExample(int x, int y) {
       this.x = x;
       this.y = y;
   }

   public void func() {
       System.out.println("SuperExample.func()");
   }
}
```

```java
public class SuperExtendExample extends SuperExample {

   private int z;

   public SuperExtendExample(int x, int y, int z) {
       super(x, y);
       this.z = z;
   }

   @Override
   public void func() {
       super.func();
       System.out.println("SuperExtendExample.func()");
   }
}
```

```java
SuperExample e = new SuperExtendExample(1, 2, 3);
e.func();
```

```html
SuperExample.func()
SuperExtendExample.func()
```

[Using the Keyword super](https://docs.oracle.com/javase/tutorial/java/IandI/super.html)

## 重写与重载

**1. 重写（Override）**  

存在于继承体系中，指子类实现了一个与父类在方法声明上完全相同的一个方法。

为了满足里式替换原则，重写有以下三个限制：

- 子类方法的访问权限必须大于等于父类方法；
- 子类方法的返回类型必须是父类方法返回类型或为其子类型。
- 子类方法抛出的异常类型必须是父类抛出异常类型或为其子类型。

使用 @Override 注解，可以让编译器帮忙检查是否满足上面的三个限制条件。

下面的示例中，SubClass 为 SuperClass 的子类，SubClass 重写了 SuperClass 的 func() 方法。其中：

- 子类方法访问权限为 public，大于父类的 protected。
- 子类的返回类型为 ArrayList<Integer>，是父类返回类型 List<Integer> 的子类。
- 子类抛出的异常类型为 Exception，是父类抛出异常 Throwable 的子类。
- 子类重写方法使用 @Override 注解，从而让编译器自动检查是否满足限制条件。

```java
class SuperClass {
   protected List<Integer> func() throws Throwable {
       return new ArrayList<>();
   }
}

class SubClass extends SuperClass {
   @Override
   public ArrayList<Integer> func() throws Exception {
       return new ArrayList<>();
   }
}
```

在调用一个方法时，先从本类中查找看是否有对应的方法，如果没有再到父类中查看，看是否从父类继承来。否则就要对参数进行转型，转成父类之后看是否有对应的方法。总的来说，方法调用的优先级为：

- this.func(this)
- super.func(this)
- this.func(super)
- super.func(super)


```java
/*
   A
   |
   B
   |
   C
   |
   D
*/


class A {

   public void show(A obj) {
       System.out.println("A.show(A)");
   }

   public void show(C obj) {
       System.out.println("A.show(C)");
   }
}

class B extends A {

   @Override
   public void show(A obj) {
       System.out.println("B.show(A)");
   }
}

class C extends B {
}

class D extends C {
}
```

```java
public static void main(String[] args) {

   A a = new A();
   B b = new B();
   C c = new C();
   D d = new D();

   // 在 A 中存在 show(A obj)，直接调用
   a.show(a); // A.show(A)
   // 在 A 中不存在 show(B obj)，将 B 转型成其父类 A
   a.show(b); // A.show(A)
   // 在 B 中存在从 A 继承来的 show(C obj)，直接调用
   b.show(c); // A.show(C)
   // 在 B 中不存在 show(D obj)，但是存在从 A 继承来的 show(C obj)，将 D 转型成其父类 C
   b.show(d); // A.show(C)

   // 引用的还是 B 对象，所以 ba 和 b 的调用结果一样
   A ba = new B();
   ba.show(c); // A.show(C)
   ba.show(d); // A.show(C)
}
```

**2. 重载（Overload）**  

存在于同一个类中，指一个方法与已经存在的方法名称上相同，但是参数类型、个数、顺序至少有一个不同。

应该注意的是，返回值不同，其它都相同不算是重载。


# 七、反射

Java反射是Java被视为动态（或准动态）语言的一个关键性质。这个机制允许程序在运行时透过Reflection APIs取得任何一个已知名称的class的内部信息，包括其modifiers（诸如public, static 等）、superclass（例如Object）、实现之interfaces（例如Cloneable），也包括fields和methods的所有信息，并可于运行时改变fields内容或唤起methods。
Java反射机制容许程序在运行时加载、探知、使用编译期间完全未知的classes。换言之，Java可以加载一个运行时才得知名称的class，获得其完整结构。

## 反射的优点  

*     **可扩展性**   ：应用程序可以利用全限定名创建可扩展对象的实例，来使用来自外部的用户自定义类。
*     **类浏览器和可视化开发环境**   ：一个类浏览器需要可以枚举类的成员。可视化开发环境（如 IDE）可以从利用反射中可用的类型信息中受益，以帮助程序员编写正确的代码。
*     **调试器和测试工具**   ： 调试器需要能够检查一个类里的私有成员。测试工具可以利用反射来自动地调用类里定义的可被发现的 API 定义，以确保一组测试中有较高的代码覆盖率。

## 反射的缺点  

尽管反射非常强大，但也不能滥用。如果一个功能可以不用反射完成，那么最好就不用。在我们使用反射技术时，下面几条内容应该牢记于心。

*     **性能开销**   ：反射涉及了动态类型的解析，所以 JVM 无法对这些代码进行优化。因此，反射操作的效率要比那些非反射操作低得多。我们应该避免在经常被执行的代码或对性能要求很高的程序中使用反射。
*     **安全限制**   ：使用反射技术要求程序必须在一个没有安全限制的环境中运行。如果一个程序必须在有安全限制的环境中运行，如 Applet，那么这就是个问题了。
*     **内部暴露**   ：由于反射允许代码执行一些在正常情况下不被允许的操作（比如访问私有的属性和方法），所以使用反射可能会导致意料之外的副作用，这可能导致代码功能失调并破坏可移植性。反射代码破坏了抽象性，因此当平台发生改变的时候，代码的行为就有可能也随着变化。


## Java反射相关的API在包java.lang.reflect中，如下：

接口 | 功能
---|---
Member	| 该接口可以获取有关类成员（域或者方法）后者构造函数的信息。
AccessibleObject | 	该类是域(field)对象、方法(method)对象、构造函数(constructor)对象的基础类。它提供了将反射的对象标记为在使用时取消默认 Java 语言访问控制检查的能力。
Array	| 该类提供动态地生成和访问JAVA数组的方法。
Constructor | 提供一个类的构造函数的信息以及访问类的构造函数的接口。
Field	| 提供一个类的 Field 的信息以及访问类的域的接口。
Method | 提供一个类的 Method 的信息以及访问类的方法的接口。
Modifier | 提供了 static 方法和常量，对类和成员访问修饰符进行解码。
Proxy | 	提供动态地生成代理类和类实例的静态方法。


## 反射常用功能示例
### 1、获取类的Class对象
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

### 2、获取类的Fields
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

### 3、获取类的Method，调用指定方法
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

### 4、获取类的Constructor
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

### 5、新建类的实例
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

### 6、调用类的函数
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

### 7、设置/获取类的属性值
通过反射获取类的Field对象，调用Field方法设置或获取值

```
Class<?> classType = ExtendType.class;
Object inst = classType.newInstance();
Field intField = classType.getField("pubIntExtendField");
intField.setAccessible(true);
intField.setInt(inst, 100);
int value = intField.getInt(inst);
```

- [Trail: The Reflection API](https://docs.oracle.com/javase/tutorial/reflect/index.html)
- [深入解析 Java 反射（1）- 基础](http://www.sczyh30.com/posts/Java/java-reflection-1/)

# 八、异常

Throwable 可以用来表示任何可以作为异常抛出的类，分为两种：  **Error**   和 **Exception**。其中 Error 用来表示 JVM 无法处理的错误，Exception 分为两种：

-   **受检异常**  ：需要用 try...catch... 语句捕获并进行处理，并且可以从异常中恢复；
-   **非受检异常**  ：是程序运行时错误，例如除 0 会引发 Arithmetic Exception，此时程序崩溃并且无法恢复。

<div align="center"> <img src="https://www.xuxueli.com/blog/static/images/img_118.png" width="600"/> </div>

- [Java 入门之异常处理](https://www.tianmaying.com/tutorial/Java-Exception)
- [Java 异常的面试问题及答案 -Part 1](http://www.importnew.com/7383.html)

# 九、泛型

```java
public class Box<T> {
   // T stands for "Type"
   private T t;
   public void set(T t) { this.t = t; }
   public T get() { return t; }
}
```

- [Java 泛型详解](http://www.importnew.com/24029.html)
- [10 道 Java 泛型面试题](https://cloud.tencent.com/developer/article/1033693)

# 十、注解

Java 注解是附加在代码中的一些元信息，用于一些工具在编译、运行时进行解析和使用，起到说明、配置的功能。注解不会也不能影响代码的实际逻辑，仅仅起到辅助性的作用。

JDK5.0及以后版本引入的一个特性，与类、接口、枚举是在同一个层次。它可以声明在包、类、字段、方法、局部变量、方法参数等的前面，用来对这些元素进行说明，注释。

作用分类：

- 1、 编写文档：通过代码里标识的元数据生成文档【生成文档doc文档】
- 2、 代码分析：通过代码里标识的元数据对代码进行分析【使用反射】
- 3、 编译检查：通过代码里标识的元数据让编译器能够实现基本的编译检查【Override】

## java中注解的使用与实例
注解目前非常的流行，很多主流框架都支持注解，而且自己编写代码的时候也会尽量的去用注解，一时方便，而是代码更加简洁。

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

##  定义一个注解的方式：

```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    
}
```

除了@符号，注解很像是一个接口。定义注解的时候需要用到元注解，上面用到了@Target和@Retention，它们的含义在上面的表格中已近给出。

在注解中一般会有一些元素以表示某些值。注解的元素看起来就像接口的方法，唯一的区别在于可以为其制定默认值。没有元素的注解称为标记注解，上面的@Test就是一个标记注解。 

## 注解须知：
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

## 注解处理器：
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

[注解 Annotation 实现原理与自定义注解例子](https://www.cnblogs.com/acm-bingzi/p/javaAnnotation.html)


# 十一、Swing

Swing是一个用于开发Java应用程序用户界面的开发工具包。
以抽象窗口工具包（AWT）为基础使跨平台应用程序可以使用任何可插拔的外观风格。Swing开发人员只用很少的代码就可以利用Swing丰富、灵活的功能和模块化组件来创建优雅的用户界面。 工具包中所有的包都是以swing作为名称，例如javax.swing,javax.swing.event。

## 布局方式
- java.awt FlowLayout ：将组件按从左到右而后从上到下的顺序依次排列，一行不能放完则折到下一行继续放置
- java.awt GridLayout ：形似一个无框线的表格，每个单元格中放一个组件
- java.awt BorderLayout： 将组件按东、南、西、北、中五个区域放置，每个方向最多只能放置一个组件
- java.awt GridBagLayout ：非常灵活，可指定组件放置的具体位置及占用单元格数目

## 提示框
- JOptionPane.showMessageDialog(null, "这是一个简单的消息框");
- JOptionPane.showMessageDialog(null, "提示正文", "标题", - JOptionPane.ERROR_MESSAGE);
- JOptionPane.showConfirmDialog(null,"这是一个有三个按钮的确认框，\n按任意按钮返回");
- JOptionPane.showInputDialog(null,"这是一个可供用户输入信息的对话框");

## 实例：Server启动器
```
package com.xxl.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

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

## 实例：人脸识别，UI模块
```
package com.xxl.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

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
	 */
	private Component getTabbedpaneFaceLogin() {
		Panel p = new Panel();
		p.setBackground(Color.GRAY);
		return p;
	}

	/**
	 * 标签窗口2:视屏监控
	 */
	private Component getTabbedpaneVideoFind() {
		Panel p = new Panel();
		p.setBackground(Color.WHITE);
		return p;
	}
	
	/*** main测试 ***/
	public static void main(String[] args) {
		indexui = new IndexUI();
	}
}
```

[Swing文档](https://www.w3cschool.cn/swing/)

# 十二、环境配置

## JDK安装
[JDK下载地址](http://www.oracle.com/index.html )

** Mac下安装 **

```
# 查看jdk安装目录 
# /usr/libexec/java_home 
Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home

# 用户目录下，新建.bash_profile文件
cd ~
touch .bash_profile     (如果该文件不存在，将创建一个空文件)
open .bash_profile      (调用记事本编辑该文件)

# 配置jdk环境变量
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

# 配置maven环境变量
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置path
PATH=$PATH:$JAVA_HOME/bin
PATH=$PATH:$MAVEN_HOME/bin
export PATH

# 配置生效
source .bash_profile
```

** Windows下安装 **

- 安装JDK：自定义安装目录，注意jdk和jre需要放在不同的文件夹中。
- 环境变量：（位置在 我的电脑->属性->高级->环境变量）环境变量一般是指在操作系统中用来指定操作系统运行环境的一些参数，比如临时文件夹位置和系统文件夹位置等。这点有点类似于DOS时期的默认路径，当你运行某些程序时除了在当前文件夹中寻找外，还会到设置的默认路径中去查找。简单地说这里的“Path”就是一个变量，里面存储了一些常用命令所存放的目录路径。 
    - JAVA_HOME：Eclipse/Tomcat等JAVA开发的软件就是通过搜索JAVA_HOME变量来找到并使用安装好的JDK，如果你没有配置JAVA_HOME变量，你会发现Tomcat无法正常启动。 
    - PATH：PATH指向搜索命令路径，如果没有配置这个PATH变量指向JDK的命令路径，会发现在命令行下。无法运行javac、java等命令。
    - CLASSPATH：CLASSPAH指向类搜索路径，.;表示在当前目录搜索，由于java程序经常要用到lib目录下的dt.jar和tools.jar下类，所以这两项也要加进来，如果在命令行编译和运行的程序还需要用到第三方的jar文件，则也需要把第三方JAR文件加入进来。

```
JAVA_HOME：d:/jdk    
PATH：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin    
CLASSPATH：.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar    
``` 

** CentOS下安装 **

```
### 卸载旧版本jdk
java -version
rpm -qa | grep java
rpm -e --nodeps java-1.6.0-openjdk-1.6.0.0-1.45.1.11.1.el6.x86_64

### 下载JDK
wget http://download.oracle.com/otn/java/jdk/6u45-b06/jdk-6u45-linux-i586.bin?AuthParam=1432052036_e95492a0a833fa9ce7be7b3ce1e9427e
mkdir -p /usr/local/java
cp jdk-6u45-linux-x64.bin /usr/local/java/    
cd /usr/local/java/

### 安装JDK (默认安装位置：/usr/local/java/)
chmod u+x jdk-6u45-linux-x64.bin    （或：chmod 777 jdk-6u45-linux-x64.bin）
./jdk-6u45-linux-x64.bin            （rpm文件安装方式：rpm -ivh jdk-6u45-linux-x64.bin.rpm）

### 配置JDK环境变量
vi /etc/profile         （向文件里面追加以下内容：）

export JAVA_HOME=/usr/local/java/jdk1.6.0_45
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

### 使修改立即生效
#source /etc/profile 

### 验证安装
java
javac
java -version
echo $PATH 
```

## Maven安装
- [官网](http://maven.apache.org/download.cgi)
- [官方教程](http://maven.apache.org/guides/getting-started/index.html)
- [官方五分钟入门](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

** 前提 **
正确安装JDK，并配置环境变量

** Mac下安装 **

```
# 用户目录下，新建.bash_profile文件
cd ~
touch .bash_profile     (如果该文件不存在，将创建一个空文件)
open .bash_profile      (调用记事本编辑该文件)

# 配置maven环境变量（maven解压目录）
export MAVEN_HOME=/Users/xuxueli/programfils/apache-maven-3.3.9

# 配置path
PATH=$PATH:$MAVEN_HOME/bin
export PATH

# 配置生效
source .bash_profile
```

验证：在terminal下 "mvn -version" 确认
配置本地仓库：在Maven解压安装目录下 “\conf\settings.xml” 文件中配置
    ```
    <localRepository>/Users/xuxueli/workspaces/maven-libs</localRepository>
    ```
    

** Windows下安装 **

解压安装，并配置Windows环境变量：
```
MAVEN_HOME=D://maven
PATH=%MAVEN_HOME%\bin;  + 其他
```

验证：在dos下 "mvn -version" 确认
配置本地仓库：在Maven解压安装目录下 “\conf\settings.xml” 文件中配置
    ```
    <localRepository>e:/work/maven_lib</localRepository>
    ```

** 常用命令 **
```
mvn validate //验证工程是否正确，所有需要的资源是否可用
mvn compile//编译项目的源代码
mvn test-compile  //编译项目测试代码
mvn test  //使用已编译的测试代码，测试已编译的源代码
mvn package    //已发布的格式，如jar，将已编译的源代码打包
mvn integration-test //在集成测试可以运行的环境中处理和发布包
mvn verify //运行任何检查，验证包是否有效且达到质量标准
mvn install //把包安装在本地的repository中，可以被其他工程作为依赖来使用
mvn deploy //在整合或者发布环境下执行，将最终版本的包拷贝到远程的repository，使得其他的开发者或者工程可以共享
mvn generate-sources //产生应用需要的任何额外的源代码，如xdoclet
mvn archetype:generate //创建 Maven 项目
mvn compile //编译源代码
mvn test-compile//编译测试代码
mvn test //运行应用程序中的单元测试
mvn site //生成项目相关信息的网站
mvn clean //清除目标目录中的生成结果
mvn package //依据项目生成 jar 文件
mvn install //在本地 Repository 中安装 jar
mvn eclipse:eclipse //生成 Eclipse 项目文件

// etc
mvn -Dhttps.protocols=TLSv1.2   // 启用 TLSv1.2 协议，maven仓库要求
```

** Maven 参数 ** 

    -D 传入属性参数 
    -P 使用pom中指定的配置 
    -e 显示maven运行出错的信息 
    -o 离线执行命令,即不去远程仓库更新包 
    -X 显示maven允许的debug信息 
    -U 强制去远程参考更新snapshot包 
    例如 mvn install -Dmaven.test.skip=true -Poracle 
    其他参数可以通过mvn help 获取

** maven scope（依赖范围控制）说明 **
在POM 4中，<dependency>中还引入了<scope>，它主要管理依赖的部署。目前<scope>可以使用5个值：

- ** compile （编译范围） **：compile是默认的范围；如果没有提供一个范围，那该依赖的范围就是编译范围。编译范围依赖在所有的classpath 中可用，同时它们也会被打包。 
- ** provided （已提供范围） **：provided 依赖只有在当JDK 或者一个容器已提供该依赖之后才使用。例如， 如果你开发了一个web 应用，你可能在编译 classpath 中需要可用的Servlet API 来编译一个servlet，但是你不会想要在打包好的WAR 中包含这个Servlet API；这个Servlet API JAR 由你的应用服务器或者servlet 容器提供。已提供范围的依赖在编译classpath （不是运行时）可用。它们不是传递性的，也不会被打包。 
- ** runtime （运行时范围） **：runtime 依赖在运行和测试系统的时候需要，但在编译的时候不需要。比如，你可能在编译的时候只需要JDBC API JAR，而只有在运行的时候才需要JDBC 驱动实现。
- ** test （测试范围） **：test范围依赖 在一般的编译和运行时都不需要，它们只有在测试编译和测试运行阶段可用。 
- ** system （系统范围） ** ：system范围依赖与provided 类似，但是你必须显式的提供一个对于本地系统中JAR 文件的路径。这么做是为了允许基于本地对象编译，而这些对象是系统类库的一部分。这样的构件应该是一直可用的，Maven 也不会在仓库中去寻找它。如果你将一个依赖范围设置成系统范围，你必须同时提供一个 systemPath 元素。注意该范围是不推荐使用的（你应该一直尽量去从公共或定制的 Maven 仓库中引用依赖）。

## GIT安装
- [windows下Git安装与配置](http://blog.csdn.net/renfufei/article/details/41647875)
- [Mac-OSX下安装Git](http://blog.csdn.net/zhangkongzhongyun/article/details/7903148)
- [Git 常用命令速查表(图文+表格)](http://www.jb51.net/article/55442.htm)
- [Git 教程](http://www.runoob.com/git/git-tutorial.html)

** GIT常用命令汇总 **

```
// 初始化
cd git-workspace
git init
git clone http://xxxxx.git
cd xxxxx

// 查看
git status
git branch -a
git branch -r

// 拉取master分支到本地
git fetch origin master:master
git checkout master

// 获取远程分支master并merge到当前分支 
git fetch origin master
git pull origin master 

// 获取所有远程分支，并merge到本地分支
git fetch
git pull

// 在master基础上，新建分支，推送分支
git checkout master
git fetch origin master
git checkout -b XXX
git push origin XXX

// 加入缓存，提交代码，并push分支
git add xxx.java
git commit -m "init project"
git push orgin XXX

// 在 branch_a 分支上 merge 分支 master
git checkout branch_a
git merge master
git push orgin branch_a

// 在 branch_a 分支上 rebase 分支 master （不推荐）
git checkout branch_a
git rebase master
git push orgin branch_a
// (merge操作会生成一个新的节点，之前的提交分开显示。而rebase操作不会生成新的节点，是将两个分支融合成一个线性的提交，之前分支就没有了。)

// 删除分支，大写D强制删除，push远程删除
git branch -d XXX
git branch -D XXX
git push origin  :XXXX

// 文件加入/移除stage（加入stage才可commit和push）
git add xxx.imi
git reset HEAD xxl.imi

// .gitignore文件
加入.gitingore文件中的文件，不会被 “git status(检测未被git管理、git管理下被修改但未被commit和push的文件)”检测到；
git rm --cached file/path/to/be/ignored

// 冲突解决
add 
commit

// 撤消上一个commit，但保留add的文件
git reset --soft HEAD~1

// 生成公钥，默认位置：~/.ssh
$ ssh-keygen -t rsa -C "xxx@gmail.com"
cat .\.ssh\id_rsa.pub
》》New SSH key
ssh -T git@github.com

// 更新仓库地址
git remote set-url origin remote_git_address

// 更新config
git config --list
git config user.name
git config user.email   // query
git config user.email "email info"  // update each
git config --global user.email "email info"  // update global

// 回滚commit
git log
git reset --hard <commit_id>
git push origin HEAD --force

// 放弃本地的修改，用远程的库覆盖本地
git fetch --all
git reset --hard origin/master

// 强制覆盖推送
git push -f origin/bbbbbb
```

** Git常用命令 **

```
// 常用命令汇总
git clone <url>	clone远程版本库
git status	查看状态
git diff	查看变更内容
git add .	跟踪所有改动过的文件
git add <file>	跟踪指定的文件
git mv <old> <new>	文件改名
git rm <file>	删除文件
git rm --cached <file>	停止跟踪文件但不删除
git commit -m "commit message"	提交所有更新过的文件
git log	查看提交历史
git reset --hard HEAD	撤销工作目录中所有未提交文件的修改内容
git checkout HEAD <file>	撤销指定的未提交文件的修改内容
git revert <commit>	撤销指定的提交
git branch	显示所有本地分支
git checkout <branch/tag>	切换到指定分支或标签
git branch <new-branch>	创建新分支
git branch -d <branch>	删除本地分支
git merge <branch>	合并指定分支到当前分支
git rebase <branch>	Rebase指定分支到当前分支
git remote -v	查看远程版本库信息
git remote show <remote>	查看指定远程版本库信息
git remote add <remote> <url>	添加远程版本库
git fetch <remote>	从远程库获取代码
git pull <remote> <branch>	下载代码合并到当前分支
git push <remote> <branch>	上传代码到远程
git push <remote> :<branch/tag>	删除远程分支或标签

```

## Intellij安装

** 安装注册 **
- [下载地址](http://www.jetbrains.com/idea/) 

> Community Edition社区免费版功能有精简，推荐使用Ultimate Edition商业版本，可免费用30天，需要注册激活。

> 已经集成了：JDK、Tomcat、Maven、Git、Freemarker、explorer等功能，比较完善。（插件库比较智能完善，如markdown插件在打开MD文件时会主动询问是否安装）

** 设置 **
> Project等同于eclipse的workspace，Module等同于eclipse的Project。失去焦点自动保存；

* 1、JDK：

        开发版本/JDK：
        File》Other Setting》Default Project Structure》Platform Settings》SDKs》+JDK》选择全局jdk目录;
        File》Project Structure》Platform Settings》SDKs》+JDK》选择项目jdk目录
        编译版本/Compiler：
        Preferences》Default Settings》Build、Execution、Deployment》Java Compiler》Project bytecode version》置空，编译版本自动和开发版本一致；
        File》Other Settint》Default Settings》Build、Execution、Deployment》Java Compiler》Project bytecode version》置空，编译版本自动和开发版本一致；
        运行版本/JRE：
        Tomcat》Edit...》JRE》选择对应的JDK版本即可；（低版本spring2.x不兼容1.8）

* 2、maven设置：
    >File》Other Setting》Default Setting》Build、Execution、Deployment》Build Tools》Maven》配置maven安装目录和settin文件

    >File》Setting》Build、Execution、Deployment》Build Tools》Maven》配置maven安装目录和settin文件
    
    ```
    Maven home directory：maven安装目录 (Override)
    User settings file：setting文件目录 (Override)
    Local repository： 默认即可
    ```
    
    Tips：推荐依赖树查看插件 Maven Helper

* 3、Git配置：
    >配置GIT：安装Git，配置Git：File》Setting》Version Control》Git》Path to Git executable选择git.exe地址；
    
    >下载Git仓库：VCS》Checkout from Version Control》Git》...
        
    >导入Git项目：File》New》Module from exists Sources》Git仓库中项目；
        
    >非Git仓库顶级目录下项目，关联Git：VCS》Version Control Operation
        
    >多git仓库同时使用：File》Setting》Version Control》点击+新增git本地仓库地址；
        
    >取消git自动tracked文件，导致gitignore失效：File》Setting》Version Control》When fils are created》勾选Do not add;
    
* 4、Tomcat：

    >配置Server环境：File》Setting》Build、Execution、Deployment》Application Servers》点击+选择Tomcat类型，选择目录路径
    
    >配置Server实例：右上角》Edit Configurations》点击+号》Tomcat Server》Local》配置：环境、名称、默认浏览器、默认端口路径、端口，VM设置等；
    
    >部署项目：右上角》Edit Configurations》Deployment》点+号》Artifact》选择Web项目，配置根路径Application context；
    
    >动态Debug：右上角》Edit Configurations》Server》On update action改动/ On frame deactivation失活都选择update classes and resources(exploded格式才有)；

    >一个Tomcat实例只能启动一个项目，如需启动多台，需要配置多个Tomcat并修改端口号；
    
* 5、修改IDE字体，黑色风格：
    >File》Setting》Appearance & Behavior》Appearance》

    ```
    Theme：Darcula；Override default fonts by(not recommeded) 打钩选中；
    Name：dialogInput；Size：12；
    ```
        
    >修改编辑器/代码字体：File》Setting》Font》保存自定义Scheme》primary font：Consolas；Size：14；
    
    >控制台字体：
    File》Setting》Editor》Color&Fonts》Console Font》primary font：Consolas；Size：14；


* 6、Keymap（快捷键）设置：
    >Keymap》选中即可（因为熟悉Eclipse，可改为此；但为了以后适应，推荐使用默认）
* 7、显示行号：
    >File》Setting》Editor》General》Appearance》Show line numbers（勾选）
        
* 8、不自动打开上次项目：
    >File》Setting》Apprearance & Behavior》System Settings》Reopen last project on startup（去掉勾选）；

* 9、设置文件头注释：
    >File》Setting》Editor》File and Code Templates》Includes》File Header》输入：Created by ${USER} on ${DATE} ${TIME}.
* 10、文件自动保存：
    >挺好的，不用修改；
* 11、用*标识编辑过的文件：
    >Editor》Editor Tabs》Mark modifyied tabs with asterisk（勾选）
* 12、快捷键冲突：
    >Ctrl + Space：和Win7输入法冲突；语言栏》高级键设置》编辑“在输入语言之间”不修改点击确认》编辑“中文-输入法/非输入法切换”Ctrl+Space修改为如Ctrl+Home等即可》保存，重启即可；
* idea热部署
    >1.Build,Excution,Deployment>>Make project automatically（勾选）
    >2.CTRL + SHIFT + A --> 查找Registry;  勾选  compiler.automake.allow.when.app.running;
    springboot自动部署参考文档：https://blog.csdn.net/a1273022039/article/details/79590681
    >3. Edit Configurations --> 'On Update Action' 勾 'update classes and resources'


** 快捷键 **
官网文档很完善：Help》KeyMap Preference》双击即可查看快捷键表；


# 十三、特性

## Java 各版本的新特性

**New highlights in Java SE 8**  

1. Lambda Expressions
2. Pipelines and Streams
3. Date and Time API
4. Default Methods
5. Type Annotations
6. Nashhorn JavaScript Engine
7. Concurrent Accumulators
8. Parallel operations
9. PermGen Error Removed

**New highlights in Java SE 7**  

1. Strings in Switch Statement
2. Type Inference for Generic Instance Creation
3. Multiple Exception Handling
4. Support for Dynamic Languages
5. Try with Resources
6. Java nio Package
7. Binary Literals, Underscore in literals
8. Diamond Syntax

- [Difference between Java 1.8 and Java 1.7?](http://www.selfgrowth.com/articles/difference-between-java-18-and-java-17)
- [Java 8 特性](http://www.importnew.com/19345.html)

## Java 与 C++ 的区别

- Java 是纯粹的面向对象语言，所有的对象都继承自 java.lang.Object，C++ 为了兼容 C 即支持面向对象也支持面向过程。
- Java 通过虚拟机从而实现跨平台特性，但是 C++ 依赖于特定的平台。
- Java 没有指针，它的引用可以理解为安全指针，而 C++ 具有和 C 一样的指针。
- Java 支持自动垃圾回收，而 C++ 需要手动回收。
- Java 不支持多重继承，只能通过实现多个接口来达到相同目的，而 C++ 支持多重继承。
- Java 不支持操作符重载，虽然可以对两个 String 对象执行加法运算，但是这是语言内置支持的操作，不属于操作符重载，而 C++ 可以。
- Java 的 goto 是保留字，但是不可用，C++ 可以使用 goto。

[What are the main differences between Java and C++?](http://cs-fundamentals.com/tech-interview/java/differences-between-java-and-cpp.php)

## JRE or JDK

- JRE is the JVM program, Java application need to run on JRE.
- JDK is a superset of JRE, JRE + tools for developing java programs. e.g, it provides the compiler "javac"

## J2EE的核心API与组件

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


# 参考资料

- Eckel B. Java 编程思想[M]. 机械工业出版社, 2002.
- Bloch J. Effective java[M]. Addison-Wesley Professional, 2017.
   
