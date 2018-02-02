##### SOA、分布式
- **SOA**：面向服务的架构，将业务逻辑提炼成共用服务，对外提供服务。
- **分布式**：从部署的角度将应用按照访问压力进行归类，主要目标是充分利用服务器的资源，避免资源分配不均。

##### RPC、REST：
- **RPC**：RPC是系统间内部调用；
- **REST**：REST应该是外部使用；

##### WebService、RESTful API(JSON)、RPC
- WebService：
    
    WebService是个很重型的规范，它的应用协议是SOAP（简单对象访问协议），它所依赖的下层通信方式不单单是HTTP，也有SOAP over SMTP, SOAP over TCP，由于HTTP协议群众基础广，开发调试方便，所以，成了WebService中最为流行的方式。(如CXF等)

    SOAP协议对于消息体和消息头都有定义，同时消息头的可扩展性为各种互联网的标准提供了扩展的基础，WS-*系列就是较为成功的规范。但是也由于SOAP由于各种需求不断扩充其本身协议的内容，导致在SOAP处理方面的性能有所下降。同时在易用性方面以及学习成本上也有所增加。

    WSDL写的让人崩溃；
    
- RESTful API(JSON) over HTTP：
    
    REST被人们的重视，其实很大一方面也是因为其高效以及简洁易用的特性。这种高效一方面源于其面向资源接口设计以及操作抽象简化了开发者的不良设计，同时也最大限度的利用了Http最初的应用协议设计理念。

    连RESTful都被嫌弃了，大伙儿干脆连PUT、DELETE都懒得用，直接用GET和POST。

- RPC

    但是：HTTP也是TCP上性能比较差的协议，因为HTTP是基于TCP的，有3次握手，再加上HTTP是个文本传输协议（虽然也可以传二进制的附件，但业务逻辑还是文本用的多），又有很多复杂的HEADER。所以人们发明了一些更高效的通信协议来做远程调用，比如ACE、ICE、Corba、淘宝的HSF。
    
##### JSON、XML
- JSON的可读性比XML强
- XML解析的时候规则太多
- JSON的缺点是数据类型支持较少
- JSON的解析性能高于XML；（XML性能糟糕到什么地步呢，有一种专门的CPU叫做XML Accelerator，专门为XML解析提供硬件加速）
- 基于XML和HTTP的WebService, 基于JSON的RESTful API，并没有性能差异。
- javascript原生支持json解析，而为http接口大部分是给页面的Javascript解析使用；

##### stub和skeleton：
- stub
    
    每个远程对象都包含一个代理对象stub，当运行在本地Java虚拟机上的程序调用运行在远程Java虚拟机上的对象方法时，它首先在本地创建该对象的代理对象stub, 然后调用代理对象上匹配的方法，代理对象会作如下工作：
    - 1.与远程对象所在的虚拟机建立连接。
    - 2.打包(marshal)参数并发送到远程虚拟机。
    - 3.等待执行结果。
    - 4.解包(unmarshal)返回值或返回的错误。
    - 5.返回调用结果给调用程序。
    
    stub 对象负责调用参数和返回值的流化(serialization)、打包解包，以及网络层的通讯过程。

- skeleton
    
    每一个远程对象同时也包含一个skeleton对象，skeleton运行在远程对象所在的虚拟机上，接受来自stub对象的调用。当skeleton接收到来自stub对象的调用请求后，skeleton会作如下工作：
    - 1.解包stub传来的参数
    - 2.调用远程对象匹配的方法
    - 3.打包返回值或错误发送给stub对象 ；

##### 常见通讯方案：RMI > Httpinvoker >= Hession >> Burlap >> webService
- 1、RPC
    
    RPC本身没有规范,但基本的工作机制是一样的，即：serialization/deserialization+stub+skeleton
    
    宽泛的讲，只要能实现远程调用，都是RPC，如:rmi .net-remoting ws/soap/rest hessian xmlrpc thrift potocolbuffer
    
- 2、RMI

    Java语言本身提供的远程通讯协议，它的优点是稳定、高效、以EJB为基础，缺点：只是用于Java程序之间通讯；服务端不能开防火墙
    
    RIM是最稳定的，特别是在大数据量的情况下，跟其它的通讯协议比，具有非常大的优势

- 3、Hessioin

    它是caucho公司提供的开源协议，是基于HTTP传输，服务端不用开防火墙，协议的规范公开化，可以用于任何语言
    
    Hession在传输少量对象时，效率上比RMI要高效，但在传输数据结构复杂的对象或大量数据对象时，比RMI要慢20%左右
    
    Hessian 序列化以后的数据放在 HTTPBody 里
    
- 4、Burlap

    类似Hession类似，XML协议传输。Burlap在传输1条时数据时，速度没问题，通常情况下，它的耗时的RMI的3倍
    
- 5、Httpinvoker（Spring）

    它是基于SpringFramework的远程通讯协议，它只能用于Java程序之间通讯，且服务器和客户端必须使用SpringFramework
    
    Httpinvoker使用Java序列化技术传输对象，从本质上与RIM一致，但从效率上看，两者也相差无几，基本持平
    
- 6、JMS

    是java平台上的消息规范。一般jms消息不是一个xml，而是一个java对象，很明显，jms没考虑异构系统，说白 了，JMS就没考虑非java的东西。但是好在现在大多数的jms provider（就是JMS的各种实现产品）都解决了异构问题。

- 7、WebService

    1. soap专注于远程服务调用，jms专注于信息交换。
    2. 大多数情况下soap是两系统间的直接交互（Consumer <--> Producer），而大多数情况下jms是三方系统交互（Consumer <- Broker -> Producer）。
    3. 当然，JMS也可以实现request-response模式的通信，只要Consumer或Producer其中一方兼任 broker即可。
    4. 多数情况下，ws是同步的，jms是异步。虽然，ws也可以是异步的，而jms也可以是同步的。
    5. webservice的效率低下是众所周知的，平均看来webservice的通讯耗时是RMI的10倍
    ：webService soap请求是HTTP POST的一个专用版本，遵循一种特殊的xml消息格式Content-type设置为: text/xml。任何数据都可以xml化。

##### 性能测试结果 
RMI > Httpinvoker >= Hession >> Burlap >> webService

- 直接调用：

    直接调用的所有耗时都接近零，这说明，程序在处理时，几乎没花费时间（只限于正常业务调用），记录的全部时间都在远程调用耗时上。
    
- RMI调用：

    在复杂数据结构、大数据量的情况下，与其它协议有的差距尤为明显。
    
    当使用RIM形式（继承UnicastRemoteObject对象）提供远程服务并调用，与Spring对POJO包装成RMI进行 效率比较。结果：两者持平，且Spring提供的服务还要稍快些。所以初步认为，Spring的代理和缓存机制比较强大，节省了对象重新获取的时间
    
- Hession调用

    caucho公司的resin服务器号称是最快的服务器，在java领域有一定的知名度。所以Hession做为resin的组成部分，其设计也非常精简高效，实际运行情况也证明了这一点，平均看来，Hession较RMI要慢20%左右，但这只是数据量过千万级以后，数据结构很复杂地情况下才能够体现出来，中等或百万级的数据量时，Hession并不比RMI慢。Hession的好处是精简且高效，可以跨语言，而且协议规范公开，我们可以针对任意语言开发对其协议的实现。目前可以支持的语言有，Java、.net、C++、phthon、ruby，目前还没有实现delphi。Hession与WEB服务器结合非常好，借助WEB服务器的成熟功能，在处理大量用户并发访问时会有很大的优势，在资源分配、线程排队、异常处理等方面都可以由成熟的WEB服务器保证。而RMI本身并不提供多线程服务器，而且RMI需要开防火墙端口，Hession不用
    
- Burlap调用

    Burlap与Hession都是caucho公司的开源产品，只不过Hession采用二进制的方式，而Burlap采用的XML格式。测试结果显示，Burlap在数据结构不复杂、数据量中等的情况下，效率还是可以接受的，但如果数据结构复杂、数据量较大时，效率会急剧下降。平均计算，Burlap的调用耗时是RMI的3倍。我认为效率低只要有两个方面：[1]、XML数据描述内容太多，同样的数据结构，其传输量要大得多；[2]、众所周知，对XML的解析是非常耗资源的，特别对于大数据量情况下更是如此。

- Httpinvoker调用

    Httpinvoker是SpringFramework提供的JAVA远程调用方式，使用JAVA的序列化机制处理对象的传输。从测试结果来看，其效率还是不错的，与RMI基本持平。不过，它只能用于JAVA语言之间的通讯，而且，客户端和服务端都是必须使用Spring框架。另外，Httpinvoker并没有经过实践检验，目前还没有找到相对应的项目
    
- webService调用

    本次我们测试也是使用网上抄得最火的，apache的AXIS组件作为webservice的实现，AXIS在webservice领域相对来说，也是老资格的。为了仅测试数据传输和编码、解码的时间，客户端和服务器都使用缓存，对象只缓存一次。但是，测试结果显示webservice的效率还是比其他通讯协议慢10倍左右。如果考虑到多个引用指向同一个对象的传输情况，webservice要落后得更多。因为RMI、Hession等协议都可以传递引用，而webservice有多少引用，就要复制多少份对象实例。webservice传输的冗余信息过多也是其影响速度的原因之一，监控发现，同样的访问请求，描述相同的数据，webservice返回的数据量是Hession协议的6.5倍。另外，webservice处理也很耗时，目前的XML解析器效率普遍不高，（就算使用stax，也一样），处理XML <->bean很耗资源。从测试结果看，异地调用比本地调用要快得多，这也从侧面来说明了其耗时主要在编码和解码XML文件上，这比冗余信息更为严重，冗余信息占用的只是网络带宽，而每次调用资源耗费直接影响到服务器的负载能力。网上也传得比较多的一句话是（MS工工程师曾经说过，用webservice不能负载100个以上的并发用户）。测试过程中，还发现webservice编码非常不方便，对非基本的数据类型需要逐个注册序列化和反序列化类，生成sub更加麻烦，不如Spring+RMI/Hession处理得那么顺心流畅。而且webservice不支持集合类型，只能用数组，所以在解析方面会带来不便
    

结论：
- 内部SOA服务RPC通讯：推荐使用Hessian（Netty + Hessian，如果dubbo）；
- 外部Web接口，第三方服务接口：推荐使用JSON + POST/Restful