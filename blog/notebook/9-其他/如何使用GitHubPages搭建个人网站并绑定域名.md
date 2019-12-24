<h2 style="color:#4db6ac !important" >如何使用GitHubPages搭建个人网站并绑定域名</h2>
> 创建时间：2015/04/29

[TOCM]

[TOC]

### 一、搭建 GitHubPages 个人主页

#### 1.1、新建GitHubPages仓库

仓库名格式要求为：“{用户名}.github.io”

#### 1.2、新建GitHubPages的网页

仓库中新建首页 **index.html** 文件，内容如下
```
<html>
<body>
    <h1>Hello World</h1>
    <p>This is xuxueli's github.</p>
</body>
</html>
```

至此，GitHubPages已经搭建完成，可通过以下连接访问：
- https://{用户名}.github.io/
- https://{用户名}.github.com

### 二、GitHubPages 绑定域名

#### 2.1、购买域名
自行购买即可，如 "www.{域名}.com"。

#### 2.2、GitHubPages仓库，设置域名
GitHubPages仓库的 "Settings》Custom domain" 位置，设置为新购买的域名，如 "www.{域名}.com"。

#### 2.3、GitHubPages仓库，新增CNAME文件
GitHubPages仓库新增文件 **CNAME** ，内容为新购买的域名，如 "www.{域名}.com"，如下。
```
www.xuxueli.com
```

#### 2.4、域名解析到GitHubPages
为域名添加两条CNAME类型解析配置，如下：
```
主机记录    记录类型      解析线路 	记录值	            TTL
www        CNAME        默认	    {用户名}.github.io	10min
@          CNAME	    默认	    {用户名}.github.io	10min
```

至此，域名已经成功绑定GitHubPages，可通过以下以下链接访问：

- www.{域名}.com
- https://{用户名}.github.io
- https://{用户名}.github.com

GitHub会将CNAME中的域名绑定为你的项目域名，同时将原有二级域名重定向到新域名下。而且，一旦项目文件变动，会触发“Page build” 实时生效，如果失败github可能会发送邮件。


### 参考
[GitHubPages官网](https://pages.github.com/)
[GitHubPages设置自定义域名](https://help.github.com/en/github/working-with-github-pages/managing-a-custom-domain-for-your-github-pages-site) 