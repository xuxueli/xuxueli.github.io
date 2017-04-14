
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `article_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `desc` varchar(300) DEFAULT NULL,
  `click_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `article_menu` (
	`user_id` int(11) NOT NULL AUTO_INCREMENT,
	`email` varchar(100) NOT NULL,
	`password` varchar(50) NOT NULL,
	`user_token` varchar(50) DEFAULT NULL,
	`real_name` varchar(50) DEFAULT NULL,
	`state` int(11) NOT NULL,
	`reg_time` datetime NOT NULL,
	PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;