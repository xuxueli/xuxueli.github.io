/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50542
Source Host           : localhost:3306
Source Database       : db_xxl_main

Target Server Type    : MYSQL
Target Server Version : 50542
File Encoding         : 65001

Date: 2015-04-28 21:00:07
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin_menu
-- ----------------------------
INSERT INTO `admin_menu` VALUES ('1', '0', '1', '核心模块', '', '0');
INSERT INTO `admin_menu` VALUES ('2', '0', '2', '测试菜单模块', '', '0');
INSERT INTO `admin_menu` VALUES ('3', '1', '2', '权限管理', '', '0');
INSERT INTO `admin_menu` VALUES ('4', '1', '1', '文章管理', '', '0');
INSERT INTO `admin_menu` VALUES ('5', '3', '1', '后台用户管理', 'userPermission/userMain.do', '1000100');
INSERT INTO `admin_menu` VALUES ('6', '3', '2', '后台角色管理', 'userPermission/roleMain.do', '1000200');
INSERT INTO `admin_menu` VALUES ('7', '3', '3', '后台菜单管理', 'userPermission/menuMain.do', '1000300');
INSERT INTO `admin_menu` VALUES ('16', '4', '2', '文章管理', 'article/articleMain.do', '1000500');
INSERT INTO `admin_menu` VALUES ('19', '4', '1', '文章菜单管理', 'article/articleMenuMain.do', '1000400');

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article_info
-- ----------------------------
INSERT INTO `article_info` VALUES ('1', '5000', '11', '1', '阿斯蒂芬', '阿斯顿发斯蒂芬', '2015-04-22 15:13:00', '11');
INSERT INTO `article_info` VALUES ('4', '0', '16', '0', '阿斯顿发送到22', '<p>bbbbbbbb</p>', '2015-04-23 20:44:27', '0');
INSERT INTO `article_info` VALUES ('5', '0', '17', '0', '003表空间块级框', '<p><img src=\"http://img.baidu.com/hi/jx2/j_0015.gif\"/></p>', '2015-04-25 22:19:55', '0');
INSERT INTO `article_info` VALUES ('6', '0', '17', '0', '004归结于规划局', '<p><img src=\"http://img.baidu.com/hi/jx2/j_0038.gif\"/><img src=\"http://localhost:8080/5i-admin/uploadfiles/image/20150427/1430103099145012548.jpg\" title=\"1430103099145012548.jpg\" alt=\"asd.jpg\"/></p>', '2015-04-25 22:40:21', '0');

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
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article_menu
-- ----------------------------
INSERT INTO `article_menu` VALUES ('11', '0', '1', '模块一', '模块位顶级菜单', '0');
INSERT INTO `article_menu` VALUES ('16', '11', '1', '组一', '', '0');
INSERT INTO `article_menu` VALUES ('17', '11', '1', '组二', '', '0');

-- ----------------------------
-- Table structure for user_main
-- ----------------------------
DROP TABLE IF EXISTS `user_main`;
CREATE TABLE `user_main` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_token` varchar(50) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_main
-- ----------------------------
