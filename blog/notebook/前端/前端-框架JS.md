- jquery
    - 【javascript库】一个优秀的Javascript库。它是轻量级的js库 ，它兼容CSS3，还兼容各种浏览器（IE 6.0+, FF 1.5+, Safari 2.0+, Opera 9.0+），jQuery2.0及后续版本将不再支持IE6/7/8浏览器。
    - 官网：http://jquery.com/
    - w3c教程：http://www.w3school.com.cn/jquery/

- jquery-datatables（jquery插件）
    - 【表格】 Datatables
    - 官网：http://dt.thxopen.com/index.html

- jquery-cookie（jquery插件）
    - 【操作cookie】
    - 官网：http://plugins.jquery.com/cookie/ 
    - 教程：http://www.cnblogs.com/qiantuwuliang/archive/2009/07/19/1526663.html

- jquery-validation（jquery插件）
    - 【表单验证】
    - 官网：http://jqueryvalidation.org/
    - 教程文档：http://www.w3cschool.cc/jquery/jquery-plugin-validate.html

- pace
    - 进度条：多种形式 + 页面加载
    - https://github.com/HubSpot/pace
- nprogress
    - 进度条：顶部线条 + 页面加载
    - https://github.com/rstacruz/nprogress
    
- progress.js
    - 进度条，线形 + 页面/表单等
    - http://usablica.github.io/progress.js/

- ScrollUp（jquery插件）
    - 【滚动到顶部】ScrollUp是一个轻量级的Jquery插件，它创建一个可自定义的“滚动到顶部”的按钮，在任意的网站中进行简单的调用就能达到效果。
    - https://markgoodyear.com/labs/scrollup/
    - https://github.com/markgoodyear/scrollup/
    - 使用
    ```
    <!-- scrollup （修改image.css里面图片的地址）-->
    <link rel="stylesheet" href="${base_url}plugin/jquery/scrollup/image.css" >
    <script type="text/javascript" src="${base_url}plugin/jquery/scrollup/jquery.scrollUp.min.js"></script>
    // scrollup
    $.scrollUp({
    animation: 'fade',	// fade/slide/none
    scrollImg: true
    });
    ```

- thickbox
    - 【弹出层】ThickBox是一个基于JQuery类库的扩展，它能在浏览器界面上显示非常棒的UI框， 它可以显示单图片，多图片，ajax请求内容或链接内容.ThickBox 是用超轻量级的 jQuery 库 编写的. 压缩过 jQuery 库只15k, 未压缩过的有39k.
    - http://www.blueidea.com/articleimg/2007/12/5182/tickbox_demo.html

- fancybox（jquery插件）
    - 【弹出层】一款优秀的jquery插件，它能够展示丰富的弹出层效果。
    - 官网：http://fancybox.net/
    - 中文简介：http://www.helloweba.com/view-blog-65.html


- jqueryrotate（jquery插件）
    - 【旋转插件】CSS3 提供了多种变形效果，比如矩阵变形、位移、缩放、旋转和倾斜等等，让页面更加生动活泼有趣，不再一动不动。然后 IE10 以下版本的浏览器不支持 CSS3 变形，虽然 IE 有私有属性滤镜（filter），但不全面，而且效果和性能都不好。
今天介绍一款 jQuery 插件——jqueryrotate，它可以实现旋转效果。jqueryrotate 支持所有主流浏览器，包括 IE6。如果你想在低版本的 IE 中实现旋转效果，那么 jqueryrotate 是一个很好的选择。
    - 官网：http://plugins.jquery.com/rotate/
    - 示例
    ```
    示例：鼠标移动效果
    $('#img2').rotate({
        bind : {
            mouseover : function(){
                $(this).rotate({animateTo: 180});
            }, mouseout : function(){
                $(this).rotate({animateTo: 0});
            }
        }
    });
    ```

- Nivo Slider（jquery插件-可选版本）
    - 【图片轮播】这款插件号称世界上最棒的图片轮播插件，有独立的 jQuery 插件和 WordPress 插件两个版本。目前下载量已经突破 1,800,000 次！jQuery 独立版本的插件主要有如下特色：
        - ✓ 16个独特的过渡效果
        - ✓ 简洁和有效的标记
        - ✓ 加载参数设置
        - ✓ 内置方向和导航控制
        - ✓ 压缩版本大小只有12KB
        - ✓ 支持链接图像
        - ✓ 支持 HTML 标题
        - ✓ 3套精美光滑的主题
        - ✓ 在MIT许可下免费使用
        - ✓ 支持响应式设计
    - 官网：http://dev7studios.com/
    - demo：http://www.cnblogs.com/lhb25/p/jquery-nivo-slider-image-plugin.html
    - 示例
    ```
    $.fn.nivoSlider.defaults = {
        effect: 'random', // 过渡效果
        slices: 15, //effect为切片效果时的数量
        boxCols: 8, //effect为格子效果时的列
        boxRows: 4, //effect为格子效果时的行
        animSpeed: 500, //动画速度
        pauseTime: 3000, //图片切换速度
        startSlide: 0, //从第几张开始
        directionNav: true, //是否显示图片切换按钮(上/下页)
        directionNavHide: true, //是否鼠标经过才显示
        controlNav: true, // 显示序列导航
        controlNavThumbs: false, // 显示图片导航
        controlNavThumbsFromRel: false, // 使用img的rel属性作为缩略图地址
        controlNavThumbsSearch: '.jpg', // 查找特定字符串(controlNavThumbs必须为true)
        controlNavThumbsReplace: '_thumb.jpg', // 替换成这个字符(controlNavThumbs必须为true)
        keyboardNav: true, // 键盘控制（左右箭头）PS:建议把代码中的keypress换成keydown,因为在Chrome下有兼容问题.
        pauseOnHover: true, // 鼠标经过时暂停播放
        manualAdvance: false, // 是否手动播放(false为自动播放幻灯片)
        captionOpacity: 0.1, // 字幕透明度
        prevText: 'Prev',
        nextText: 'Next',
        randomStart: false, //是否随机图片开始
        beforeChange: function(){}, //动画开始前触发
        afterChange: function(){}, //动画结束后触发
        slideshowEnd: function(){}, // 本轮循环结束触发
        lastSlide: function(){}, // 最后一张图片播放结束触发
        afterLoad: function(){} // 加载完毕时触发
        };$.fn.nivoSlider.defaults = {
        effect: 'random', // 过渡效果
        slices: 15, //effect为切片效果时的数量
        boxCols: 8, //effect为格子效果时的列
        boxRows: 4, //effect为格子效果时的行
        animSpeed: 500, //动画速度
        pauseTime: 3000, //图片切换速度
        startSlide: 0, //从第几张开始
        directionNav: true, //是否显示图片切换按钮(上/下页)
        directionNavHide: true, //是否鼠标经过才显示
        controlNav: true, // 显示序列导航
        controlNavThumbs: false, // 显示图片导航
        controlNavThumbsFromRel: false, // 使用img的rel属性作为缩略图地址
        controlNavThumbsSearch: '.jpg', // 查找特定字符串(controlNavThumbs必须为true)
        controlNavThumbsReplace: '_thumb.jpg', // 替换成这个字符(controlNavThumbs必须为true)
        keyboardNav: true, // 键盘控制（左右箭头）PS:建议把代码中的keypress换成keydown,因为在Chrome下有兼容问题.
        pauseOnHover: true, // 鼠标经过时暂停播放
        manualAdvance: false, // 是否手动播放(false为自动播放幻灯片)
        captionOpacity: 0.1, // 字幕透明度
        prevText: 'Prev',
        nextText: 'Next',
        randomStart: false, //是否随机图片开始
        beforeChange: function(){}, //动画开始前触发
        afterChange: function(){}, //动画结束后触发
        slideshowEnd: function(){}, // 本轮循环结束触发
        lastSlide: function(){}, // 最后一张图片播放结束触发
        afterLoad: function(){} // 加载完毕时触发
    };
    ```

- jquery-easing（jquery插件）
    - 【缓动动画】jQuery实现动画，easing就是一款帮助jQuery实现缓动动画的插件。
    - 官网：http://plugins.jquery.com/jquery.easing/
    - 教程：http://www.helloweba.com/view-blog-212.html

- highcharts （jquery插件-可选版本）
    - 【报表】Highcharts是一款纯javascript编写的图表库。Highcharts 的运行需要两个 JS 文件， highcharts.js 及 jQuery 、 MooTools 、Prototype 、Highcharts Standalone Framework 常用 JS 框架中的一个。
    - 官网：http://www.highcharts.com/products/highcharts/
    - 教程：http://www.hcharts.cn/demo/
    - 中文api：http://www.hcharts.cn/api/index.php
    - 示例：在我开发Tiger监控的过程中，使用过，可参考；

- echarts
    - 【报表】ECharts，缩写来自Enterprise Charts，商业级数据图表，一个纯Javascript的图表库。
    - 官网：http://echarts.baidu.com/

- My97DatePicker
    - 【日期控件】My97日期控件
    - 官网：http://www.my97.net/

- Swipe JS
    - 【内容滑块】Swipe JS 是一个轻量级(仅3.7 kb)的移动Web浏览内容滑块类库，它可用来展示任意的HTML内容，支持精确的触摸移动，并能提供类似原生程序的操作体验，如智能缩放幻灯片大小等。它也不依赖于任何 JavaScript 框架，定制性也比较高，提供了4个可选的配置参数。
    - 官网：http://www.idangero.us/swiper/
    - 中文网：http://www.swiper.com.cn/

- toastr
    - 一个实现了类似 Android 的 Toast 提示的 jQuery 插件。
    - 官网：https://github.com/CodeSeven/toastr

- sweetalert
    - 一款使用纯js制作的消息警告框插件。这款消息警告框插件能够很容易的在警告框中插入图片、动画等元素，是替代原生消息警告框的最佳选择。
    - 官网：https://github.com/t4t5/sweetalert

- yu 易U
    - 【mobile提示等等】12KB让你：验证表单不需要写一个js； 美化浏览器的各种提示；ajax、移动|创建|删除元素、js读写cookies、无刷新上传图片……
    - 官网：http://www.yxsss.com/ui/

- layer（jquery插件）
    - 【弹出层】layer是一款近年来口碑极佳的web弹层组件。
    - http://layer.layui.com/

- jiathis
    - 【分享按钮】提供分享到微博、QQ、人人等代码...通过访客不断的分享行为，提升网站的优质外链、增加社会化流量、带来更多的用户！
    - 官网：http://www.jiathis.com/

- Ace Editor
    - 【代码编辑器】一个集成的Demo，通过RequireJS引入一条JS即可。
    - 地址：https://github.com/ajaxorg/ace

- CodeMirror
    - 【代码编辑器】Demo丰富，需要引入大量CSS、Js文件。自动提示。
    - 地址：https://github.com/codemirror/CodeMirror

- moment
    - 日期操作
    - 地址：https://github.com/moment/moment

- RequireJS
    - 【模块载入框架,】RequireJS是一个非常小巧的JavaScript模块载入框架,是AMD规范最好的实现者之一。
    - 官网：http://requirejs.org/
    - 地址：https://github.com/requirejs/requirejs

- Uploadify
    - 【模块载入框架,】RequireJS是一个非常小巧的JavaScript模块载入框架,是AMD规范最好的实现者之一。
    - 官网：http://requirejs.org/
    - 地址：https://github.com/requirejs/requirejs

- Kalendae
    - 一个JS实现的日期选择工具，支持单选，多选；
    - 地址：https://github.com/ChiperSoft/Kalendae

