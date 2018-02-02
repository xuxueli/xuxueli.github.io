
## 一、问题还原：
2017-02-09日接到QA反馈的一个线上Bug，mq测商户预约成功页500，该预约成功页复用的dp测原有预约成功页，dp测正常；

## 二、问题原因：
一次预约成功页请求流程如下：
- 1、进入路由Action；
- 2、判断请求来源是否mt测，如果来源mt则将mt商户ID（shopId）转换成dp商户ID；（不知何时，该页面开始兼容支持mt测预约成功页）
- 3、使用dp商户ID（shopId），调用dp测服务处理业务逻辑…… 。
- 4、判断预约看店来源，进行chain 方式跳转至第二个Action；
（通过 chain 方式进行 action 跳转）
- 5、进入第二个Action；
- 6、获取shopId ，调用dp测服务处理业务逻辑…… 。

（问题出现了，此时shopId依然为mt商户ID；上述“ 第二步 ”转换后的新shopId并没有传递到跳转后的Action中；后者使用mt商户ID调用dp服务，导致逻辑异常）

## 三、涉及的技术点：struts2的几种常见result type

    dispatcher
        1、页面；
        2、同一个线程；
        3、action中的数据一直保存；
    redirect、redirect-action
        1、页面、action或者一个网址连接；
        2、一次redirect会产生一个新线程；
        3、redirect跳转前的action执行线程中的数据（ActionContext）会丢失；
    chain
        1、action；
        2、同一个线程；
        3、chain 跳转前后的action共享ActionContext；
        
几种常见 action 跳转如何传递参数
##### 1、result type  使用type="redirect"跳转方式：

```
<action name="demoAction" class="..." >
    <result name="newaction" type="redirect">/***/newaction?param1=${param1}</result>
</action>
```

如上，使用  redirect方式可以通过URL方式拼接参数，比较简单，但是参数类型受限制；

##### 2、result type  使用type="redirect-action"  跳转方式：

```
<action name="demoAction" class="..." >
    <result name="topic" type="redirect-action"> 
        <param name="actionName">newaction</param> 
        <param name="topicId">${topicId}</param> 
    </result>
</action>
```
如上，使用redirect-action" 方式可以通过param 方式拼接参数，可参数类型比较丰富，支持对象参数；

##### 3、result type  使用 type="chain"  跳转方式01：

```
<action name="demoAction" class="..." >
    <param name="param1">${param1}</param>
    <result name="success" type="chain">
        <param name="actionName">${actionName}</param>
        <param name="namespace">${namespace}</param>
    </result>
</action>
```
##### 4、result type  使用 type="chain"  跳转方式02：

```
/**
 * 线程变量通用Util
 *
 * Created by xuxueli on 17/2/9.
 */
public class ThreadLocalParamMapUtil {

    private static ThreadLocal<Map<String, Object>> chainThreadParamMap = new ThreadLocal<Map<String, Object>>();

    public static Object getChainThreadParamValue(String key) {
        Map<String, Object> paramMap = chainThreadParamMap.get();
        return paramMap.get(key);
    }

    public static Object putChainThreadParam(String key, Object value) {
        Map<String, Object> paramMap = chainThreadParamMap.get();
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }
        return paramMap.put(key, value);
    }

}
```
上文可知，chain 跳转在同一个请求线程中进行，因此可以考虑使用 ThreadLocal 实现 chain 跳转前后的数据共享，核心逻辑如上；

## 四、问题解决：

清楚问题原因后，参考 “章节三”，本文问题很快解决了；

针对本文问题，有三种解决方案：

    1、result type改为type="redirect" ，参数以URL参数形式传递；
    2、保持 result type 为 type="chain"，使用 param 传参方式；
    3、保持 result type 为 type="chain"，使用 ThreadLocal 共享数据方式；




