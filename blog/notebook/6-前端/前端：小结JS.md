### H5复制到剪切板
```
function copyTextToClipboard(text) {
    var textArea = document.createElement("textarea")

    textArea.style.position = 'fixed'
    textArea.style.top = 0
    textArea.style.left = 0
    textArea.style.width = '2em'
    textArea.style.height = '2em'
    textArea.style.padding = 0
    textArea.style.border = 'none'
    textArea.style.outline = 'none'
    textArea.style.boxShadow = 'none'
    textArea.style.background = 'transparent'
    textArea.value = text

    document.body.appendChild(textArea)

    textArea.select()

    try {
        var msg = document.execCommand('copy') ? '成功' : '失败'
        console.log('复制内容 ' + msg)
    } catch (err) {
        console.log('不能使用这种方法复制内容')
    }

    document.body.removeChild(textArea)
}
```



### 当前页面post方式下载文件
```
// HTML标准规定如果form表单没有被添加到document里，那么form表单提交将会被终止。Chrome56之后执行了此标准；
var $form = $("<form>");
        $form.attr({
                target: '_self',
                method: 'post',
                action: '/user/download' })
            .append( $('<input>').attr({ name: "userName", value: $('#userName').val() }) )
            .append( $('<input>').attr({ name: "userPhone", value: $('#userPhone').val() }) )
            .append( $('<input>').attr({ name: "page", value: 1 }) )
            .append( $('<input>').attr({ name: "pagesize", value: 1000 }) );

        $('#downloadFrom').html($form);

        $form.submit();
```

### selected选中

```
$('#addShopWin select[name=cooperateType]').val();
$('#addShopWin select[name=cooperateType] option[value=1]').prop('selected', true);


$("#mySelect option:first").attr("selected", 'selected');
$("#mySelect option:first").prop("selected", 'selected');
```


### javascript Date format(js日期格式化)
```
// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

调用：
var time1 = new Date().Format("yyyy-MM-dd");var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");
```


### jquery改变img的src在ie6下不刷新图片
    解决方法：直接更改<img ...>这段html

### jQuery JSONP 跨域实践
```
// 前端逻辑：
$(document).ready(function(){
    $.ajax({
        url:'http://192.168.9.5/jsonp_test1.jsp',
        dataType:"jsonp",
        jsonp:"jsonpcallback",
        success:function(data){
            var $ul = $("<ul></ul>");
            $.each(data,function(i,v){
            $("<li/>").text(v["id"] + " " + v["name"]).appendTo($ul) });
            $("#res").append($ul);
        }
    });
});

比如正常输出json应该是：[{"id":"1","name":"测试1"},{"id":"2","name":"测试2"}]

jsonp 则输出: jsonpcallback([{"id":"1","name":"测试1"},{"id":"2","name":"测试2"}])
```


### 动态添加select的option 
```
$("#parent_id2").empty();
$("#parent_id2").append("<option value='0'>请选择</option>");
var jsonDatas = eval("(" + data + ")");//转换为json数据
$.each(jsonDatas, function(i, item) {
    //输出每个root子对象的名称和值
    //alert("name:"+item.id+",value:"+item.name);
    $("#parent_id2").append("<option value="+item.id+">"+item.name+"</option>");
});
```

### 判断元素是否显示/隐藏
    if ($("#id").is(":visible"))...
    if ($("#id").is(":hidden"))...

### 判断元素是否存在
    if ($("#id").length > 0)...
    if ($("#id"))...

### JS监听整个页面的回车事件
```
<script type="text/javascript">
    document.onkeydown=keyDownSearch;
    function keyDownSearch(e) {
        // 兼容FF和IE和Opera 
        var theEvent = e || window.event; 
        var code = theEvent.keyCode || theEvent.which || theEvent.charCode; 
        if (code == 13) { 
            alert('回车');//具体处理函数 
            return false;
        }
        return true; 
    }
</script> 

如果只是针对某个DIV层应用回车查询的话，可以将：
    document.onkeydown=keyDownSearch;
改成：
    document.getElementById('层ID').onkeydown=keyDownSearch; 
```


### html取消右键
```
<body scroll="no" leftmargin="0" topmargin="0" style="width: 100%; border-width=0; border-style:none" oncontextmenu="return false" onselectstart="return false">
```

### js四舍五入
一、小数转为整数    
- floor：下退 Math.floor(12.9999) = 12 
- ceil：上进 Math.ceil(12.1) = 13; 
- round: 四舍五入 ，Math.round(12.5) = 13 ；Math.round(12.4) = 12；

二、小数位数控制
- 保留到整数：exam = Math.round(exam);
- 保留一位小数：exam = Math.round(exam * 10) / 10;
- 保留二位小数：exam = Math.round(exam * 100) / 100;
- 保留三位小数：exam = Math.round(exam * 1000) /1000;
- 其它依次类推..........

### js实现sleep10秒
```
function start(){
    function sleep(milliSeconds){
        var startTime = new Date().getTime();
        while(new Date().getTime() < startTime + milliSeconds){
        // 
        }
    }
    sleep(10000);
    return "Hello Start";
}
```

### js容错代码、屏蔽js错误，例如IEjs错误会出现黄色感叹号，甚至弹出js错误弹框；
```
// 第一种：把以上代码加到你出错网页的head区域就可以了。
<SCRIPT language=javascript>
    window.onerror=function(){return true;}
</SCRIPT>

// 第二种：try...catch 可以测试代码中的错误。
// try 部分包含需要运行的代码，而 catch 部分包含错误发生时运行的代码。注意：try...catch 使用小写字母。大写字母会出错。 
// 语法：
try {
    //在此运行代码
} catch(err) {
    //在此处理错误 ...
}
```

### 随机字符串
var $chars = 'abcdefhhijklmnoprrstuvwxyz0123456789';
var maxPos = $chars.length;
function randomString(len) {
    var pwd = '';
    for (i = 0; i < len; i++) {
        if( i>=3 && i<=5 ){
            pwd += '*';
        }else{
            pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
        }
    }
    return pwd;
}

### 处理流量拦截
document.write = function(){}; // 拦截外链js通过document.write写入；

### 点击超链接，出现：res://ieframe.dll/dnserrordiagoff_webOC.html
 原因：target="_blank" 和 window.open(url ,'_blank') 冲突导致；

### 打开新窗口的几种方式及target=_blank指定窗口名称
打开新窗口的几种方式：
- 1.form提交到新窗口：

<form action="action.jsp" target="_blank"> ......</form>

- 2.链接中打开新窗口：

<a href="action.jsp" target="_blank">打开新窗口<a>

- 3.按钮打开新窗口：

<input type="button" value="打开新窗口" onclick="window.open('action.jsp')"/>

使用 target="_blank"将每次都打开新窗口，若希望同一功能的页面在同一窗口中打开，可通过指定窗口名的方式，方法：只需将target="_blank"改成target="windName" 如：

<form action="action.jsp" target="windName">......</form>
或：
<a href="action.jsp" target="windName">打开新窗口<a>
或：
<input type="button" value="打开新窗口" onclick="window.open('action.jsp','windName')"/>

### 解决jquery append 动态添加的元素，事件on 不起作用：
    解决方法：将on时间绑定在其父元素上，通过子元素查找的方式定位绑定具体位置；
    - 1、原时间绑定方法：$(".delete").on("click", function(){ ...});
    - 2、新的事件绑定方法：$(".info").on("click", ".delete", function(){ ... });

### Boolean 表达式——if(arg)
    不要用java的思维来思考javascript，if后面跟不跟boolean表达式，是由语言规范定的，java说，if后面必须是boolean表达式，那你就必须要用一个boolean表达式，javascript说，if后面只要是null,undefined,0,"",false，就是false，那就是这样的，语言的specification而已

Boolean 表达式：一个值为true或者false的表达式。如果需要，非Boolean表达式也可以被转换为Boolean值，但是要遵循下列规则：
    - 1、所有的对象都被当作 true。
    - 2、当且仅当字符串为空时，该字符串被当作 false。
    - 3、null 和 undefined 被当作 false。
    - 4、当且仅当数字为零时，该数字被当作 false。
    
### js 数据类型
- parseInt()
- parseFloat()
    
    在判断字符串是事是数字值前，parseInt()和parseFloat()都会仔细分析该字符串。parseInt()方法首先查看位置0处的字符，判断它是否是个有效数字；如果不是，该方法返回NaN，不再继续执行其他操作。如果该字符是有效数字，该方法将查看位置1处的字符，进行同样的测试。这一过程将持续到发现非有效数字的字符为止，此时parseInt()将把该字符之前的字符串转换成数字。

- Boolean(value)--把给定的值转换成Boolean型，如果转换的值是至少有一个字符的字符串、非0数字或对象时，返回true；如果该值是空字符串、数字0、undefined或null，将返回false。
- Number(value)--把给定的值转换成数字（整数或浮点数），如果转换的值不是数值，则会返回NaN

### 闭包函数

**闭包**    
官方解释：一个拥有许多变量和绑定了这些变量的环境的表达式（通常是一个函数），因而这些变量也是该表达式的一部分。闭包的特点：
- 1、 作为一个函数变量的一个引用，当函数返回时，其处于激活状态。
- 2、 一个闭包就是当一个函数返回时，一个没有释放资源的栈区。

简单的说，Javascript允许使用内部函数---即函数定义和函数表达式位于另一个函数的函数体内。而且，这些内部函数可以访问它们所在的外部函数中声明的所有局部变量、参数和声明的其他内部函数。当其中一个这样的内部函数在包含它们的外部函数之外被调用时，就会形成闭包。

**变量的作用域**
- 全局变量：函数内部声明变量的时候，不用var的话则声明了一个全局变量；函数内部可以直接读取全局变量；
- 局部变量：函数内部声明变量的时候，使用var命令则为局部变量；


**闭包的用途**
- 可以读取函数内部的变量。
- 让这些变量的值始终保持在内存中。

**使用闭包的注意点**
- 由于闭包会使得函数中的变量都被保存在内存中，内存消耗很大，所以不能滥用闭包，否则会造成网页的性能问题，在IE中可能导致内存泄露。解决方法是，在退出函数之前，将不使用的局部变量全部删除。
- 闭包会在父函数外部，改变父函数内部变量的值。所以，如果你把父函数当作对象（object）使用，把闭包当作它的公用方法（Public Method），把内部变量当作它的私有属性（private value），这时一定要小心，不要随便
改变父函数内部变量的值。

```
// 方式1：这种方法使用较多，也最为方便。var obj = {}就是声明一个空的对象。
var Circle={  
   "PI":3.14159,  
    "area":function(r){  
        return this.PI * r * r;  
    }  
};  
alert( Circle.area(1.0) );  

// 方式2：这种写法是声明一个变量，将一个函数当作值赋给变量。
var Circle = function() {  
    var obj = new Object();  
    obj.PI = 3.14159;  
     
    obj.area = function( r ) {  
        return this.PI * r * r;  
    }  
    return obj;  
}  
var c = new Circle();  
alert( c.area( 1.0 ) );  

// 方式3：全局变量
PookIndex = function(me) {
	return me = {
		init : function() {
		    alert('init');
		},
		changeAdImg : function(index) {
			// ...
		}
	};
}();
PookIndex.init();

```

### 类方法，对象方法，原型的理解
```
function People(name) {
    this.name=name;
    //对象方法
    this.Introduce=function(){
        alert("My name is"+this.name);
    }
}
//类方法
People.Run=function(){
    alert("I can run");
}
//原型方法
People.prototype.IntroduceChinese=function(){
    alert("我的名字是"+this.name);
}

//测试
var p1=new People("Windking");
p1.Introduce();

People.Run();

p1.IntroduceChinese();
```
- 1、对象方法，理解很简单，主要是如果类生成一个实例，那么该实例就能使用该方法。实例的对象方法；
- 2、类方法，不需要通过生成实例就可以使用的方法。类似静态方法；
- 3、原型方法，主要是用来对JS已有的系统对象进行扩展而生的，例如Array数组没有什么方法，你可以为其增加原型方法，那么创建的数组就拥有了该方法。


### 40种网站设计常用技巧
[地址](http://www.cnblogs.com/cynthiadpeng/archive/2009/03/17/1414154.html)

### Jquery选择器
[文档](http://www.w3school.com.cn/jquery/jquery_ref_selectors.asp)

### Window对象：open函数
[文档](http://www.w3school.com.cn/jsref/met_win_open.asp)

### 随机数组
```
// 随机数组
var rewardList = [];
rewardList.sort(function(){
    return Math.random()>0.5?-1:1;
});
```

### 跑马灯，透明阴影
```
<div id="rewardListMarquee" class="rolling" style="z-index:105;height:80px;width:250px;margin:0 auto;display:none;filter:alpha(opacity=50); -moz-opacity:0.5; -khtml-opacity: 0.5; opacity: 0.5;">
<MARQUEE scrollAmount="3" scrollDelay="0" direction="up" height="100%" width="100%" bgcolor="black">
 <ui id="marqueeUi" style="list-style-type:none;text-align:center;font-size:14px;color:white;">
    <li>pkc159***001 领取10元话费</li></li>
    <li>pkc159***001 领取10元话费</li></li>
  </ui>
</MARQUEE>
</div>
```

- direction表示滚动的方向，值可以是left，right，up，down，默认为left
- behavior表示滚动的方式，值可以是scroll（连续滚动）slide（滑动一次）alternate（往返滚动）
- loop表示循环的次数，值是正整数，默认为无限循环
- scrollamount表示运动速度，值是正整数，默认为6
- scrolldelay表示停顿时间，值是正整数，默认为0，单位似乎是毫秒
- align表示元素的垂直对齐方式，值可以是top，middle，bottom，默认为middle
- bgcolor表示运动区域的背景色，值是16进制的RGB颜色，默认为白色
- height、width表示运动区域的高度和宽度，值是正整数（单位是像素）或百分数，默认width=100% height为标签内元素的高度
- hspace、vspace表示元素到区域边界的水平距离和垂直距离，值是正整数，单位是像素。
- onmouseover=this.stop() onmouseout=this.start()表示当鼠标以上区域的时候滚动停止，当鼠标移开的时候又继续滚动。

### js中undefined,null,NaN的区别
```
1.类型分析：
js中的数据类型有undefined,boolean,number,string,object等5种，前4种为原始类型，第5种为引用类型。
var a1;
var a2 = true;
var a3 = 1;
var a4 = "Hello";
var a5 = new Object();
var a6 = null;
var a7 = NaN;
var a8 = undefined;
alert(typeof a); //显示"undefined"
alert(typeof a1); //显示"undefined"
alert(typeof a2); //显示"boolean"
alert(typeof a3); //显示"number"
alert(typeof a4); //显示"string"
alert(typeof a5); //显示"object"
alert(typeof a6); //显示"object"
alert(typeof a7); //显示"number"
alert(typeof a8); //显示"undefined"
从上面的代码中可以看出未定义的值和定义未赋值的为undefined，null是一种特殊的object,NaN是一种特殊的number。
2.比较运算
var a1; //a1的值为undefined
var a2 = null;
var a3 = NaN;
alert(a1 == a2); //显示"true"
alert(a1 == a3); //显示"false"
alert(a2 == a3); //显示"false"
alert(a3 == a3); //显示"false"
从上面的代码可以得出结论：（1）undefined与null是相等；（2）NaN与任何值都不相等，与自己也不相等。

```


### js中匿名函数
匿名函数类似java中有private，没有实际名字，也没有指针
```
// 常规写法
(function() {
    alert('water');
})();

// 带参数
(function(o) {
    alert(o);
})('water');

// 链式调用
(function(o) {
alert(o);
    return arguments.callee;
})('water')('down');
```

### window对象函数：定时函数setTimeout()、周期函数setInterval()

[文档](http://www.w3school.com.cn/jsref/dom_obj_window.asp)

### 平台判断：Android、IOS、微信

```
//http://jstest.gc73.com.cn/mobile/webkit.js
var browser = {
	versions : function() {
		var u = navigator.userAgent, app = navigator.appVersion;
		return { 
			ie : u.indexOf('Trident') > -1, // IE内核
			opera : u.indexOf('Presto') > -1, // opera内核
			webKit : u.indexOf('AppleWebKit') > -1, // 
			gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, 
			mobile : !!u.match(/AppleWebKit.*Mobile.*/), // 是否是移动
			ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios内核
			android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android内核
			iPhone : u.indexOf('iPhone') > -1, // iPhone内核
			iPad : u.indexOf('iPad') > -1, // Ipad内核
			webApp : u.indexOf('浏览器') == -1,
            weixin : u.toLowerCase().match(/MicroMessenger/i) == "micromessenger"    // 微信
		};
	}(),
	language : (navigator.browserLanguage || navigator.language).toLowerCase()
};


var Webkit = {
		isMobile : function() {
			return browser.versions.mobile;
		},
		isAndroid : function() {
			return browser.versions.android;
		},
		isIos : function() {
			return browser.versions.ios || browser.versions.iPhone;
		},
		isPad : function() {
			return browser.versions.iPad;
		},
		isWeixin : function(){
		    return browser.versions.weixin;
		}
}
```

### jquery.ajax

[文档](http://www.w3school.com.cn/jquery/ajax_ajax.asp)
