/*
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Schema         : boot3
 Date: 31/12/2023 03:28:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE database if NOT EXISTS `boot3` default character set utf8mb4 collate utf8mb4_0900_ai_ci;
use `boot3`;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`
(
    `id`            bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `resource_type` int                                                           NOT NULL DEFAULT 0 COMMENT '资源类型：0：API接口，1：前端菜单。默认0',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '资源名',
    `path`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL     DEFAULT NULL COMMENT '资源路径',
    `create_time`   datetime                                                      NULL     DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                                      NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission`
VALUES (1, 0, '用户API', '/user/**', '2023-12-24 02:30:15', '2023-12-24 02:30:17');
INSERT INTO `permission`
VALUES (2, 0, '权限API', '/permission/**', '2023-12-30 13:25:36', NULL);
INSERT INTO `permission`
VALUES (3, 0, '角色API', '/role/**', '2023-12-30 13:25:46', NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名',
    `is_default`  tinyint(1)                                                    NULL DEFAULT 0 COMMENT '默认角色：0：非默认，1：默认',
    `remark`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role`
VALUES (1, 'Administrator', 0, '系统管理员', '2023-12-24 02:27:54', '2023-12-24 02:27:56');
INSERT INTO `role`
VALUES (2, 'User', 1, '普通用户', '2023-12-24 02:28:29', '2023-12-24 02:28:32');

-- ----------------------------
-- Table structure for role_permission_relationship
-- ----------------------------
DROP TABLE IF EXISTS `role_permission_relationship`;
CREATE TABLE `role_permission_relationship`
(
    `id`            bigint   NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `role_id`       bigint   NOT NULL COMMENT '角色Id',
    `permission_id` bigint   NOT NULL COMMENT '权限Id',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission_relationship
-- ----------------------------
INSERT INTO `role_permission_relationship`
VALUES (1, 1, 1, '2023-12-24 02:33:07', '2023-12-24 02:33:09');
INSERT INTO `role_permission_relationship`
VALUES (2, 1, 2, '2023-12-30 13:33:01', '2023-12-24 02:33:09');
INSERT INTO `role_permission_relationship`
VALUES (3, 1, 3, '2023-12-30 13:33:01', '2023-12-24 02:33:09');
INSERT INTO `role_permission_relationship`
VALUES (4, 2, 1, '2023-12-30 13:33:01', '2023-12-24 02:33:09');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `username`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
    `nick_name`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密码',
    `status`      tinyint(1)                                                    NULL DEFAULT 1 COMMENT '状态\r\n1：启用\r\n0：禁用',
    `create_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '管理员用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, 'admin', 'Administrator', 'G7EeTPnuvSU41T68qsuc/g==', 1, '2023-12-24 00:24:13', NULL);
INSERT INTO `user`
VALUES (2, 'user', 'User', 'G7EeTPnuvSU41T68qsuc/g==', 1, '2023-12-24 00:39:40', NULL);

-- ----------------------------
-- Table structure for user_role_relationship
-- ----------------------------
DROP TABLE IF EXISTS `user_role_relationship`;
CREATE TABLE `user_role_relationship`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `user_id`     bigint   NOT NULL COMMENT '用户Id',
    `role_id`     bigint   NOT NULL COMMENT '角色Id',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_relationship
-- ----------------------------
INSERT INTO `user_role_relationship`
VALUES (1, 1, 1, '2023-12-24 02:32:23', '2023-12-24 02:32:25');
INSERT INTO `user_role_relationship`
VALUES (2, 2, 2, '2023-12-24 02:32:27', '2023-12-24 02:32:29');

SET FOREIGN_KEY_CHECKS = 1;
