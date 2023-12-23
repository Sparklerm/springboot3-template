-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: boot3
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permission`
(
    `id`            bigint NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `resource_type` int    NOT NULL                                               DEFAULT '0' COMMENT '资源类型：0：API接口，1：前端菜单。默认0',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资源名',
    `path`          varchar(200) COLLATE utf8mb4_unicode_ci                       DEFAULT NULL COMMENT '资源路径',
    `create_time`   datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                                      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission`
    DISABLE KEYS */;
INSERT INTO `permission`
VALUES (1, 0, '管理员API', '/admin/**', '2023-12-24 02:30:15', '2023-12-24 02:30:17'),
       (2, 0, '普通用户API', '/user/**', '2023-12-24 02:30:51', '2023-12-24 02:30:53');
/*!40000 ALTER TABLE `permission`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role`
(
    `id`          bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `name`        varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名',
    `is_default`  tinyint(1)                                                    DEFAULT '0' COMMENT '默认角色：0：非默认，1：默认',
    `remark`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
    `create_time` datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                                                      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
INSERT INTO `role`
VALUES (1, 'Administrator', 0, '系统管理员', '2023-12-24 02:27:54', '2023-12-24 02:27:56'),
       (2, 'User', 1, '普通用户', '2023-12-24 02:28:29', '2023-12-24 02:28:32');
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission_relationship`
--

DROP TABLE IF EXISTS `role_permission_relationship`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_permission_relationship`
(
    `id`            bigint NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `role_id`       bigint NOT NULL COMMENT '角色Id',
    `permission_id` bigint NOT NULL COMMENT '权限Id',
    `create_time`   datetime DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission_relationship`
--

LOCK TABLES `role_permission_relationship` WRITE;
/*!40000 ALTER TABLE `role_permission_relationship`
    DISABLE KEYS */;
INSERT INTO `role_permission_relationship`
VALUES (1, 1, 1, '2023-12-24 02:33:07', '2023-12-24 02:33:09'),
       (2, 1, 2, '2023-12-24 02:33:44', '2023-12-24 02:33:46'),
       (3, 2, 2, '2023-12-24 02:33:44', '2023-12-24 02:33:46');
/*!40000 ALTER TABLE `role_permission_relationship`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user`
(
    `id`          bigint                                  NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `username`    varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
    `nick_name`   varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
    `password`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密码',
    `status`      tinyint(1)                              DEFAULT '1' COMMENT '状态\r\n1：启用\r\n0：禁用',
    `create_time` datetime                                DEFAULT NULL COMMENT '创建时间',
    `update_time` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='管理员用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user`
    DISABLE KEYS */;
INSERT INTO `user`
VALUES (1, 'admin', 'Administrator', 'G7EeTPnuvSU41T68qsuc/g==', 1, '2023-12-24 00:24:13', NULL),
       (2, 'user', 'User', 'G7EeTPnuvSU41T68qsuc/g==', 1, '2023-12-24 00:39:40', NULL);
/*!40000 ALTER TABLE `user`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role_relationship`
--

DROP TABLE IF EXISTS `user_role_relationship`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role_relationship`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT '主键Id',
    `user_id`     bigint NOT NULL COMMENT '用户Id',
    `role_id`     bigint NOT NULL COMMENT '角色Id',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role_relationship`
--

LOCK TABLES `user_role_relationship` WRITE;
/*!40000 ALTER TABLE `user_role_relationship`
    DISABLE KEYS */;
INSERT INTO `user_role_relationship`
VALUES (1, 1, 1, '2023-12-24 02:32:23', '2023-12-24 02:32:25'),
       (2, 2, 2, '2023-12-24 02:32:27', '2023-12-24 02:32:29');
/*!40000 ALTER TABLE `user_role_relationship`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'boot3'
--
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2023-12-24  4:06:31
