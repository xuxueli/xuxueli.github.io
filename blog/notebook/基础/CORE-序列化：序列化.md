##### 序列化和反序列化的概念
- 把对象转换为字节序列的过程称为对象的序列化。
- 把字节序列恢复为对象的过程称为对象的反序列化。

对象的序列化主要有两种用途：
- 1） 把对象的字节序列永久地保存到硬盘上，通常存放在一个文件中；
- 2） 在网络上传送对象的字节序列。

特点：
- 如果某个类能够被序列化，其子类也可以被序列化。
- 声明为static和transient类型的成员数据不能被序列化。因为static代表类的状态， transient代表对象的临时数据。
- java 对象序列化不仅保留一个对象的数据，而且递归保存对象引用的每个对象的数据。可以将整个对象层次写入字节流中，可以保存在文件中或在网络连接上传递。利用 对象序列化可以进行对象的"深复制"，即复制对象本身及引用的对象本身。序列化一个对象可能得到整个对象序列。

##### 序列化的实现
将需要被序列化的类实现Serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流(如：FileOutputStream)来构造一个 ObjectOutputStream(对象流)对象，接着，使用ObjectOutputStream对象的writeObject(Object obj)方法就可以将参数为obj的对象写出(即保存其状态)，要恢复的话则用输入流。

##### 修改默认的序列化机制： 
在序列化的过程中，有些数据字段我们不想将其序列化，对于此类字段我们只需要在定义时给它加上transient关键字即可，对于transient字段 序列化机制会跳过不会将其写入文件，当然也不可被恢复。但有时我们想将某一字段序列化，但它在SDK中的定义却是不可序列化的类型，这样的话我们也必须把 他标注为transient。

##### serialVersionUID
需要被序列化的类实现Serializable接口，implements Serializable只是为了标注该对象是可被序列化的。

serialVersionUID的默认取值是Java运行时环境根据类的内部细节自动生成的。如果对类的源代码作了修改，再重新编译，新生成的类文件的serialVersionUID的取值有可能也会发生变化。

类的serialVersionUID的默认值完全依赖于Java编译器的实现，对于同一个类，用不同的Java编译器编译，有可能会导致不同的 serialVersionUID，也有可能相同。

**为了提高serialVersionUID的独立性和确定性，强烈建议在一个可序列化类中显示的定义serialVersionUID，为它赋予明确的值。**

显式地定义serialVersionUID有两种用途：
- 1、 在某些场合，希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有相同的serialVersionUID；
- 2、 在某些场合，不希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有不同的serialVersionUID。