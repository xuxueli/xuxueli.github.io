/*
Navicat MySQL Data Transfer

Source Server         : meme-127.0.0.1
Source Server Version : 50544
Source Host           : 127.0.0.1:3306
Source Database       : xxl_site_simple

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2016-03-26 23:23:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_menu`;
CREATE TABLE `admin_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `url` varchar(50) DEFAULT NULL,
  `permession_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_menu
-- ----------------------------
INSERT INTO `admin_menu` VALUES ('1', '0', '2', '系统', '', '0');
INSERT INTO `admin_menu` VALUES ('2', '0', '1', '官网', '', '0');
INSERT INTO `admin_menu` VALUES ('4', '1', '1', '后台权限管理', '', '0');
INSERT INTO `admin_menu` VALUES ('5', '4', '1', '后台用户管理', 'userPermission/userMain.do', '1000100');
INSERT INTO `admin_menu` VALUES ('6', '4', '2', '后台角色管理', 'userPermission/roleMain.do', '1000200');
INSERT INTO `admin_menu` VALUES ('7', '4', '3', '后台菜单管理', 'userPermission/menuMain.do', '1000300');
INSERT INTO `admin_menu` VALUES ('16', '20', '1', '文章内容管理', 'article/articleMain.do', '1000500');
INSERT INTO `admin_menu` VALUES ('19', '20', '2', '文章菜单管理', 'article/articleMenuMain.do', '1000400');
INSERT INTO `admin_menu` VALUES ('20', '2', '2', '文章管理', '', '0');
INSERT INTO `admin_menu` VALUES ('21', '2', '3', '一面墙内容管理', '', '0');
INSERT INTO `admin_menu` VALUES ('22', '21', '1', '一面墙内容管理', 'wall/wallMain.do', '1000600');
INSERT INTO `admin_menu` VALUES ('23', '2', '1', '官网管理', '', '0');
INSERT INTO `admin_menu` VALUES ('24', '23', '1', '官网控制面板', 'net/netMain', '1000700');

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `order` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', '1', '超级管理员');
INSERT INTO `admin_role` VALUES ('2', '2', '一级管理员');

-- ----------------------------
-- Table structure for admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_menu`;
CREATE TABLE `admin_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_role_menu
-- ----------------------------
INSERT INTO `admin_role_menu` VALUES ('14', '2', '1');
INSERT INTO `admin_role_menu` VALUES ('16', '2', '4');
INSERT INTO `admin_role_menu` VALUES ('17', '2', '19');
INSERT INTO `admin_role_menu` VALUES ('19', '2', '16');

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_token` varchar(50) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name_unique` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5014 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('5000', 'xuxueli', '123456', null, '小刀');
INSERT INTO `admin_user` VALUES ('5001', 'xuxueli5001', '123456', '', '小刀1');
INSERT INTO `admin_user` VALUES ('5002', 'xuxueli5002', '123456', null, '小刀2');
INSERT INTO `admin_user` VALUES ('5003', 'xuxueli5003', '123456', null, '小刀3');
INSERT INTO `admin_user` VALUES ('5005', 'xuxueli5005', '123456', null, '小刀5');
INSERT INTO `admin_user` VALUES ('5006', 'xuxueli5006', '123456', null, '小刀6');
INSERT INTO `admin_user` VALUES ('5007', 'xuxueli5007', '123456', null, null);
INSERT INTO `admin_user` VALUES ('5008', 'xuxueli5008', '123456', null, null);
INSERT INTO `admin_user` VALUES ('5010', 'xuxueli5010', '123456', null, null);
INSERT INTO `admin_user` VALUES ('5011', 'xuxueli5011', '123456', null, null);
INSERT INTO `admin_user` VALUES ('5012', 'xuxueli5012', '123456', null, null);
INSERT INTO `admin_user` VALUES ('5013', 'xuxueli5013', '123456', null, '小刀');

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------
INSERT INTO `admin_user_role` VALUES ('1', '5000', '1');
INSERT INTO `admin_user_role` VALUES ('10', '5001', '2');

-- ----------------------------
-- Table structure for article_info
-- ----------------------------
DROP TABLE IF EXISTS `article_info`;
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

-- ----------------------------
-- Records of article_info
-- ----------------------------
INSERT INTO `article_info` VALUES ('6', '0', '16', '0', '这是一篇文章3', '<p>内容666<img src=\"http://img.baidu.com/hi/jx2/j_0025.gif\"/></p>', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('7', '0', '16', '0', '标题7', '内容7', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('8', '0', '16', '0', '标题8', '内容8', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('9', '0', '16', '0', '标题9', '内容9', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('11', '0', '16', '0', '标题11', '内容11', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('12', '0', '16', '0', '标题12', '内容12', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('13', '0', '16', '0', '标题13', '内容13', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('14', '0', '16', '0', '标题14', '内容14', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('18', '0', '16', '0', '标题18', '内容18', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('19', '0', '16', '0', '标题19', '内容19', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('20', '0', '16', '0', '标题20', '内容20', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('21', '0', '16', '0', '标题21', '内容21', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('22', '0', '16', '0', '标题22', '内容22', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('23', '0', '16', '0', '标题23', '内容23', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('24', '0', '16', '0', '标题24', '内容24', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('25', '0', '16', '0', '标题25', '内容25', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('33', '0', '16', '0', '标题33', '内容33', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('34', '0', '16', '0', '标题34', '内容34', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('35', '0', '16', '0', '标题35', '内容35', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('36', '0', '16', '0', '标题36', '内容36', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('37', '0', '16', '0', '标题37', '内容37', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('38', '0', '16', '0', '标题38', '内容38', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('39', '0', '16', '0', '标题39', '内容39', '2015-04-25 22:40:21', '0');
INSERT INTO `article_info` VALUES ('40', '0', '16', '0', '标题40', '内容40', '2015-04-25 22:40:21', '0');

-- ----------------------------
-- Table structure for article_menu
-- ----------------------------
DROP TABLE IF EXISTS `article_menu`;
CREATE TABLE `article_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `desc` varchar(300) DEFAULT NULL,
  `click_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article_menu
-- ----------------------------
INSERT INTO `article_menu` VALUES ('11', '0', '1', '灌水', '模块位顶级菜单', '0');
INSERT INTO `article_menu` VALUES ('16', '11', '1', '精华', '', '0');
INSERT INTO `article_menu` VALUES ('17', '11', '3', '问答', '', '0');
INSERT INTO `article_menu` VALUES ('18', '11', '2', '分享', '', '0');
INSERT INTO `article_menu` VALUES ('19', '11', '4', '二手', '', '0');
INSERT INTO `article_menu` VALUES ('20', '11', '5', '无节操', '', '0');
INSERT INTO `article_menu` VALUES ('21', '0', '1', '咨询', '', '0');
INSERT INTO `article_menu` VALUES ('22', '21', '1', '第一模块', '', '0');
INSERT INTO `article_menu` VALUES ('23', '21', '2', '第二模块', '', '0');
INSERT INTO `article_menu` VALUES ('24', '21', '3', '第三模块', '', '0');
INSERT INTO `article_menu` VALUES ('25', '21', '4', '第四模块', '', '0');
INSERT INTO `article_menu` VALUES ('26', '21', '5', '第五模块', '', '0');

-- ----------------------------
-- Table structure for common_param
-- ----------------------------
DROP TABLE IF EXISTS `common_param`;
CREATE TABLE `common_param` (
  `key` varchar(50) NOT NULL,
  `value` varchar(50) NOT NULL,
  `desc` varchar(100) DEFAULT NULL,
  UNIQUE KEY `key_unique` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of common_param
-- ----------------------------

-- ----------------------------
-- Table structure for email_send_info
-- ----------------------------
DROP TABLE IF EXISTS `email_send_info`;
CREATE TABLE `email_send_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `email_type` int(11) NOT NULL,
  `email_status` int(11) NOT NULL,
  `send_time` datetime DEFAULT NULL,
  `send_code` varchar(50) DEFAULT NULL,
  `send_content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of email_send_info
-- ----------------------------
INSERT INTO `email_send_info` VALUES ('4', '10', '100', '1', '2016-02-02 15:55:21', 'ytqdzp', null);
INSERT INTO `email_send_info` VALUES ('5', '11', '100', '1', '2016-02-02 15:58:09', 'khlvnj', null);

-- ----------------------------
-- Table structure for system_param
-- ----------------------------
DROP TABLE IF EXISTS `system_param`;
CREATE TABLE `system_param` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_param
-- ----------------------------
INSERT INTO `system_param` VALUES ('1', 'NET_ADDRESS', 'http://127.0.0.1:8080/xxl-site-simple-net/manage/generateHtml, http://127.0.0.1:8080/xxl-site-simple-net/manage/generateHtml2', '官网地址');

-- ----------------------------
-- Table structure for user_main
-- ----------------------------
DROP TABLE IF EXISTS `user_main`;
CREATE TABLE `user_main` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_token` varchar(50) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `state` int(11) NOT NULL,
  `reg_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_main
-- ----------------------------
INSERT INTO `user_main` VALUES ('11', '931591021@qq.com', 'e10adc3949ba59abbe56e057f20f883e', null, null, '100', '2016-02-02 15:58:09');

-- ----------------------------
-- Table structure for wall_info
-- ----------------------------
DROP TABLE IF EXISTS `wall_info`;
CREATE TABLE `wall_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL,
  `source` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `content` text NOT NULL,
  `image` varchar(100) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wall_info
-- ----------------------------
INSERT INTO `wall_info` VALUES ('1', '0', '0', '0', '今天在火车上，饿了拿巧克力蛋糕出来吃，旁边一小孩盯着我看(⊙o⊙)眼馋，这时他妈妈跟他说，别看，他吃的是屎！大姐，我…… \n<!--1459010871-->', null, '2016-03-26 21:22:05');
INSERT INTO `wall_info` VALUES ('2', '0', '0', '0', '近视400。等公交时把眼镜摘下来擦，一姑娘过来跟我说你不带眼镜的样子很帅，如果你可以不带眼镜我就追你，我默默的带上眼镜看了一眼还没来及开口，姑娘说好啦我懂了转身走了。我只是想看清楚你一点啊 \n<!--1459002092-->', null, '2016-03-26 21:22:05');
INSERT INTO `wall_info` VALUES ('3', '0', '0', '0', '“妈妈！为什么我长得这么丑？”\n<br />“你去问我妈！”\n<br />\n<br />“外婆！为什么我长得这么丑？”\n<br />“你去问我妈！”\n<br />\n<br />对着祖宗的灵位牌，她没敢再问下去…… \n<!--1459005765-->', null, '2016-03-26 21:22:05');
INSERT INTO `wall_info` VALUES ('4', '0', '0', '0', '做完事找老板结账，侄子在家无聊，陪着一起去了。结账时，态度卑谦，对老板递烟奉承，溜须拍马。回家途中，侄子一脸鄙夷：叔叔，我以后绝不会想你一样。心里暗暗感叹：是堕落了，可是又能怎么办，像你这么大的时候，我也是这么想的，可是进入社会才发现：太美的承诺是因为太年轻。 \n<!--1459010256-->', null, '2016-03-26 21:22:05');
