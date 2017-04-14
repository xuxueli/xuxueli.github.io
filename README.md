# Personal Blog

--- 

- 静态化，分页

- 文章设计：分组 + 文章

```
CREATE TABLE `article_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `desc` varchar(300) DEFAULT NULL,
  `click_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

CREATE TABLE `article_info` (
  `article_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(50) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `create_time` datetime NOT NULL,
  `click_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
```

- 邮箱注册，激活
    - 生成 "激活码" ，生成邮件，"发送状态" 为 UN_SEND
    - 邮件异步发送，发送状态改为 "SEND"
    - 邮件链接进入激活页面，页面从URL获取账号和激活码，确认激活
    - 校验用户，是否为 "已激活状态"
    - 校验邮件，是否超时






