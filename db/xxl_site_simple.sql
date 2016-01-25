/*
Navicat MySQL Data Transfer

Source Server         : meme-127.0.0.1
Source Server Version : 50544
Source Host           : 127.0.0.1:3306
Source Database       : xxl_site_simple

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2016-01-06 22:22:36
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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

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
INSERT INTO `admin_menu` VALUES ('20', '2', '1', '文章管理', '', '0');
INSERT INTO `admin_menu` VALUES ('21', '2', '2', '一面墙内容管理', '', '0');
INSERT INTO `admin_menu` VALUES ('22', '21', '1', '一面墙内容管理', 'wall/wallMain.do', '1000600');

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
INSERT INTO `article_info` VALUES ('6', '0', '16', '0', '这是一篇文章', '<p>内容6<img src=\"http://img.baidu.com/hi/jx2/j_0025.gif\"/></p>', '2015-04-25 22:40:21', '0');
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
INSERT INTO `article_menu` VALUES ('11', '0', '1', '文章模块', '模块位顶级菜单', '0');
INSERT INTO `article_menu` VALUES ('16', '11', '1', '第一模块', '', '0');
INSERT INTO `article_menu` VALUES ('17', '11', '3', '第三模块', '', '0');
INSERT INTO `article_menu` VALUES ('18', '11', '2', '第二模块', '', '0');
INSERT INTO `article_menu` VALUES ('19', '11', '4', '第四模块', '', '0');
INSERT INTO `article_menu` VALUES ('20', '11', '5', '第五模块', '', '0');
INSERT INTO `article_menu` VALUES ('21', '0', '1', '分享模块', '', '0');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of email_send_info
-- ----------------------------
INSERT INTO `email_send_info` VALUES ('3', '9', '100', '1', '2015-05-09 14:33:11', 'tnfomc', null);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_main
-- ----------------------------
INSERT INTO `user_main` VALUES ('9', '931591021@qq.com', 'e10adc3949ba59abbe56e057f20f883e', null, null, '100', '2015-05-09 14:33:11');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wall_info
-- ----------------------------
