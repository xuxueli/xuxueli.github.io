热门博客： http://www.cnblogs.com/lhb25/


### 浮动层次：
```
z-index:100;
```

### 不透明级别：
```
css opacity
```

### 固定顶部：
```
html,body { z-index:1;}
<div style="position:fixed; top:0; left: 0;width:100%;height:20px;text-align:center;background: #f30;z-index:2;overflow:hidden;">浮动顶部</div>
<div style="position:absolute;bottom:0px;right:16px;width:100%;height:20px;text-align:center;background:#ccc;z-index:2;overflow:hidden;">固定底部</div>
```

### CSS控制文字只显示一行，超出部分显示省略号：
```
style="width:250px;white-space:nowrap;text-overflow:ellipsis;overflow: hidden"
```

### css3动画
```
动画：http://lobbynewtest.pook.com.cn/activity/iosBindPage.do?userId=590004858&userToken=4b272b227352be32

@-webkit-keyframes title{ 0%{ -webkit-transform:scale(0.1); opacity:0;} 100%{ -webkit-transform:scale(1); opacity:1;}}
@-moz-keyframes title{0%{ -moz-transform:scale(0.1); opacity:0;}100%{ -moz-transform:scale(1); opacity:1;}}
@-o-keyframes title{0%{ -o-transform:rotate(0.1); opacity:0;}100%{ -o-transform:scale(1); opacity:1;}}
@keyframes title{0%{ transform:scale(0.1); opacity:0;} 100%{ transform:scale(1); opacity:1;}}
.title{ margin:0 auto; background:url(http://pngtest.pook.com.cn/new_lobby/activity/iosbind_141030/title.png) no-repeat; -webkit-animation:title 1s 1; animation:title 2s cubic-bezier(.48,.46,.53,1.02);}
```


