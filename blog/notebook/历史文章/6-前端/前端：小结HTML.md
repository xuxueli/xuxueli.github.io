

### 常用网站
测试html5兼容性：http://html5test.com/

### html 锚点的使用
一篇很长的文章，你想按分段精确来看，那就可以用到锚点
代码：

```
<a href="#001">跳到001</a>
...文字省略
<a name="001" id="001" ></a>
...文字省略
```

其实锚点只需name就可以可，加id是为了让它兼容性更好. href的值要跟name \ i d 一致，前面必须加"#"，以上代码在ie6/7,ff中都可以兼容，但在ie8中就不行。 因为我们锚点的<a></a>值为空，为不影响美观我们加个空格就行了,


### html页面不让浏览器缓存 
```
<!-- 设置html页面不让浏览器缓存-->
<meta http-equiv="pragma" content="no-cache"><!-- 设置html页面不让浏览器缓存-->
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="Wed, 26 Feb 1997 08:21:57 GMT">
<!-- 设置html页面不让浏览器缓存-->
```

### 给文字加提示标签
在文字两边加<span title="11111">aaa</span> 鼠标放到aaa上显示提示出111。注意img的alt是为了图片加载失败是页面显示用的。


### html5，<input type="类型" >

    - email：email 类型，iOS和Android浏览器都显示了轻度定制过的键盘。注意缩短的空格键的存在和iOS键盘的最底一行加入了@ 和句号（.）键。 而在Android上，标准逗号键将出现在空格键的左边，已经被一个@键替换。

    - url：url input 类型可以用来帮助用户输入网址。在iOS上，所有的空格键已被替换成句号（.）键和正斜杠（/）键，以及一个特殊的.com键。我的测试显示，Android键盘没有变化。

    - tel：使用 tel input 类型时，iOS和Android都是提示显示拨号键盘，而不是标准键盘。

    - number：number input 类型促使iOS显示数字和标点符号的键盘。Android浏览器将启动一个类似显示电话输入的键盘。

    - date：对于日期和时间，也有许多input类型可用。由于他们保证了你的数据是以一个标准的格式提供，所以这些可以是非常有用的。在iOS上的 date input类型会提示显示一个日期选择器。不幸的是，Android浏览器还未支持任何datetime 的input类型。

    - time：使用time类型时会提示iOS显示一个简单的拾取器来选择小时和分钟。

    - datetime：使用datetime类型时将显示一个用于同时选择日期和时间的拾取器。虽然没有显式的选择年的选项，但是拾取器会自动根据您选择的日期和月份将年添加到你的input。

    - month：month类型时将会显示日期选择器的简化版本。HTML规范还定义了一个week的input类型，然而，在我测试过的浏览器上，这好像并没有实现。


### html5，webapp的meta标签

- <meta name="apple-mobile-web-app-capable" content="yes">

设置Web应用是否以全屏模式运行：如果content设置为yes，Web应用会以全屏模式运行，反之，则不会。content的默认值是no，表示正常显示。你可以通过只读属性window.navigator.standalone来确定网页是否以全屏模式显示。

- <meta name="apple-mobile-web-app-status-bar-style" content="blank">

设置Web App的状态栏（屏幕顶部栏）的样式：

除非你先使用apple-mobile-web-app-capable指定全屏模式，否则这个meta标签不会起任何作用。

如果content设置为default，则状态栏正常显示。如果设置为blank，则状态栏会有一个黑色的背景。如果设置为blank-translucent，则状态栏显示为黑色半透明。如果设置为default或blank，则页面显示在状态栏的下方，即状态栏占据上方部分，页面占据下方部分，二者没有遮挡对方或被遮挡。如果设置为blank-translucent，则页面会充满屏幕，其中页面顶部会被状态栏遮盖住（会覆盖页面20px高度，而iphone4和itouch4的Retina屏幕为40px）。默认值是default。

- <meta name="format-detection" content="telephone=no">

启动或禁用自动识别页面中的电话号码：默认情况下，设备会自动识别任何可能是电话号码的字符串。设置telephone=no可以禁用这项功能。

- <meta name="viewport" content="width=230,initial-scale=2.3,user-scalable=no">

使用viewport meta标签可以提升页面在设备上的表现效果，典型地，你可以设置视口（viewport）的宽度和初始缩放比例。多个属性时，应该使用逗号分隔赋值表达式。 空格也可以作为分隔符，但最好使用逗号。对于属性值是数字的属性，如果属性值包含了非数字字符但是以数字开头，那么只有数字的部分被当做属性值。例如，1.0x等价于1.0，123x456等价于123。如果参数不以数字开头，则属性值为0。不需要设置每一个属性，未设置的属性会自动采用默认值。

Viewport 属性

    - width ：视口的宽度。默认值是980，取值范围是200至10000，也可以取值为常量device-width。
    - height ：视口的高度。默认值是根据width的值和设备的宽高比来计算，取值范围是223至10000，也可以取值为常量device-height。
    - initial-scale：视口的初始缩放比例。默认值取决于页面充满屏幕的缩放比例，取值范围取决于minimum-scale和maximum-scale。如1.0。
    - minimum-scale：视口的最小缩放比例。默认值是0.25，取值范围是>0至10.0。
    - maximum-scale：视口的最大缩放比例。默认值是5.0，取值范围是>0至10.0。
    - user-scable：设置用户是否可以缩放视口。yes表示可以，no表示不能，默认值是yes。设置user-scable为no可以防止当用户在input标签的文本域中输入文本时页面发生滚动。

特殊的viewport属性值

    - device-width：设备的宽度（像素）。
    - device-height：设备的高度（像素）。