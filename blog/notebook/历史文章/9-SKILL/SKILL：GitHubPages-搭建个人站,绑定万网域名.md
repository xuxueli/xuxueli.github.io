### 第一步，搭建 GitHubPages 个人主页
[GitHubPages官网](https://pages.github.com/)

- 新建一个名称为“用户名.github.io”的仓库,例如我的GitHubPages地址：[https://github.com/xuxueli/xuxueli.github.io](https://github.com/xuxueli/xuxueli.github.io)

- 仓库中新建首页**index.html**文件，内容如下
```
<html>
<body>
    <h1>Hello World</h1>
    <p>This is xuxueli's github.</p>
</body>
</html>
```
至此，GitHubPages已经搭建完成，可通过以下连接访问：

https://xuxueli.github.io/

https://xuxueli.github.com

### 第二部，GitHubPages 绑定万网域名

- 1、购买万网域名，并绑定 GitHubPages 的地址

[GitHubPages地址文档地址](https://help.github.com/articles/setting-up-an-apex-domain/) ，地址如下：


万网域名绑定参数为：
```
主机记录    记录类型      解析线路 	记录值	            TTL
www        CNAME        默认	    xuxueli.github.io	10min
@          CNAME	    默认	    xuxueli.github.io	10min
```

- 2、GitHubPages 仓库新增文件**CNAME**，内容为购买的万网域名，如下：
```
www.xuxueli.com
```

至此，域名已经成功绑定GitHubPages，可通过以下以下链接访问：

www.xuxueli.com

https://xuxueli.github.io/

https://xuxueli.github.com

GitHub会将CNAME中的域名绑定为你的项目域名，同时将原有二级域名重定向到新域名下。

一旦项目文件变动，会触发“Page build”，如果失败github可能会发送邮件；