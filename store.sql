-- MySQL dump 10.13  Distrib 9.3.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: sjpdemo
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_dictionary`
--

DROP TABLE IF EXISTS `t_dictionary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_dictionary` (
  `ID` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `DIC_NAME` varchar(40) NOT NULL COMMENT '字典名称',
  `DIC_VALUE` varchar(50) NOT NULL COMMENT '字典值',
  `DIC_TYPE` smallint NOT NULL COMMENT '字典中的类型区分',
  `DIC_ORDER` smallint NOT NULL COMMENT '同一类型下的顺序',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_dictionary`
--

LOCK TABLES `t_dictionary` WRITE;
/*!40000 ALTER TABLE `t_dictionary` DISABLE KEYS */;
INSERT INTO `t_dictionary` VALUES (7,'store_name','武汉耐克专卖店',1,0),(8,'img_path','img/index.jpg',1,0),(9,'kpi_base','2000',1,0);
/*!40000 ALTER TABLE `t_dictionary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_goods`
--

DROP TABLE IF EXISTS `t_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_goods` (
  `GoodsID` int NOT NULL AUTO_INCREMENT COMMENT '商品ID，主键，自动增长',
  `BarCode` char(15) NOT NULL COMMENT '货号/条形码，由6位字符组成，唯一索引',
  `TypeID` int NOT NULL,
  `GoodsName` varchar(50) NOT NULL COMMENT '商品名称',
  `StorePrice` decimal(8,2) NOT NULL COMMENT '进货价格',
  `SalePrice` decimal(8,2) NOT NULL COMMENT '零售价格',
  `Discount` decimal(4,2) NOT NULL COMMENT '折扣（0~1之间，保留两位小数，有效位4位）',
  `StockNum` int NOT NULL COMMENT '库存数量',
  `StockDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间，默认值：now()，获取系统当前时间',
  PRIMARY KEY (`GoodsID`),
  UNIQUE KEY `t_goods_ibuk_tID` (`BarCode`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_goods`
--

LOCK TABLES `t_goods` WRITE;
/*!40000 ALTER TABLE `t_goods` DISABLE KEYS */;
INSERT INTO `t_goods` VALUES (1,'4321',5,'羽毛球拍',34.50,34.50,0.80,49,'2020-08-21 09:29:22'),(2,'1234',8,'运动裤',56.00,89.00,0.90,84,'2025-06-10 15:57:44'),(3,'2345',5,'乒乓球拍',13.00,32.00,0.95,157,'2020-08-28 10:14:18');
/*!40000 ALTER TABLE `t_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_goodstype`
--

DROP TABLE IF EXISTS `t_goodstype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_goodstype` (
  `TypeID` int NOT NULL AUTO_INCREMENT COMMENT '类型ID，主键，自动增长',
  `TypeName` varchar(50) NOT NULL COMMENT '类型名称',
  `ParentID` int DEFAULT '0' COMMENT '父级分类ID，用于实现多级分类',
  PRIMARY KEY (`TypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_goodstype`
--

LOCK TABLES `t_goodstype` WRITE;
/*!40000 ALTER TABLE `t_goodstype` DISABLE KEYS */;
INSERT INTO `t_goodstype` VALUES (1,'运动装备',0),(2,'衣服',1),(3,'裤子',0),(4,'重型装备',1),(5,'轻型设备',1),(6,'男装',2),(7,'女装',2),(8,'裤裙',3),(9,'鞋子',0);
/*!40000 ALTER TABLE `t_goodstype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_sales`
--

DROP TABLE IF EXISTS `t_sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sales` (
  `SalesID` int NOT NULL AUTO_INCREMENT COMMENT '销售记录ID，主键，自动增长',
  `ReceiptsCode` char(14) NOT NULL COMMENT '小票流水号，固定14位，唯一索引',
  `SalesDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '销售日期，默认值：now()',
  `Amount` decimal(8,2) NOT NULL COMMENT '销售金额',
  `SalesmanID` int NOT NULL COMMENT '经手的导购员ID，外键',
  `CashierID` int NOT NULL COMMENT '经手的收银员ID，外键',
  PRIMARY KEY (`SalesID`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_sales`
--

LOCK TABLES `t_sales` WRITE;
/*!40000 ALTER TABLE `t_sales` DISABLE KEYS */;
INSERT INTO `t_sales` VALUES (34,'20200826105549','2020-08-25 10:56:06',944.38,25,24),(35,'20200826135240','2020-08-14 13:52:58',403.19,25,24),(36,'20200826140810','2020-08-26 14:08:24',375.59,28,24),(37,'20200827145732','2020-08-27 14:57:58',160.19,25,24),(38,'20200827150343','2020-08-27 15:04:19',82.80,25,24),(39,'20200827150444','2020-08-27 15:05:07',483.29,25,24),(40,'20200827150545','2020-08-27 15:06:09',240.28,25,24),(41,'20200828090102','2020-08-28 09:01:41',400.50,28,29),(42,'20200828100028','2020-08-28 10:00:59',430.79,28,30),(43,'20200828100148','2020-08-28 10:02:31',240.28,28,24),(44,'20200831134604','2020-08-31 13:46:30',485.99,28,24),(45,'20250609083002','2025-06-09 08:30:17',80.08,25,24),(46,'20250609085933','2025-06-09 08:59:48',80.08,25,24),(47,'20250609090933','2025-06-09 09:09:44',110.40,25,24),(48,'20250610102401','2025-06-10 10:24:19',80.08,25,24);
/*!40000 ALTER TABLE `t_sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_salesdetail`
--

DROP TABLE IF EXISTS `t_salesdetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_salesdetail` (
  `SDID` int NOT NULL AUTO_INCREMENT COMMENT '销售明细ID，主键，自动增长',
  `SalesID` int NOT NULL COMMENT '对应的销售记录ID，外键',
  `GoodsID` int NOT NULL COMMENT '对应的商品ID，外键',
  `Quantity` int NOT NULL COMMENT '销售数量',
  `AloneAmount` decimal(8,2) NOT NULL COMMENT '成交价格',
  PRIMARY KEY (`SDID`),
  KEY `SalesID` (`SalesID`),
  KEY `GoodsID` (`GoodsID`),
  CONSTRAINT `t_salesdetail_ibfk_1` FOREIGN KEY (`SalesID`) REFERENCES `t_sales` (`SalesID`),
  CONSTRAINT `t_salesdetail_ibfk_2` FOREIGN KEY (`GoodsID`) REFERENCES `t_goods` (`GoodsID`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_salesdetail`
--

LOCK TABLES `t_salesdetail` WRITE;
/*!40000 ALTER TABLE `t_salesdetail` DISABLE KEYS */;
INSERT INTO `t_salesdetail` VALUES (28,34,2,8,640.79),(32,36,2,4,320.39),(33,36,1,2,55.20),(34,37,2,2,160.19),(35,38,1,3,82.80),(36,39,2,5,400.50),(37,39,1,3,82.80),(38,40,2,3,240.29),(39,41,2,5,400.50),(40,42,1,4,110.40),(41,42,2,4,320.39),(42,43,2,3,240.29),(43,44,2,4,320.39),(45,45,2,1,80.09),(46,46,2,1,80.09),(47,47,1,4,110.40),(48,48,2,1,80.09);
/*!40000 ALTER TABLE `t_salesdetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_storage_record`
--

DROP TABLE IF EXISTS `t_storage_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_storage_record` (
  `RecordID` int NOT NULL AUTO_INCREMENT,
  `RecordCode` varchar(20) NOT NULL COMMENT '入库单号',
  `GoodsID` int NOT NULL COMMENT '商品ID',
  `GoodsName` varchar(50) NOT NULL COMMENT '商品名称',
  `Quantity` int NOT NULL COMMENT '入库数量',
  `StorePrice` decimal(10,2) NOT NULL COMMENT '进货价格',
  `TotalAmount` decimal(10,2) NOT NULL COMMENT '总金额',
  `StorageDate` datetime NOT NULL COMMENT '入库时间',
  `OperatorID` int NOT NULL COMMENT '操作员ID',
  `OperatorName` varchar(20) NOT NULL COMMENT '操作员姓名',
  PRIMARY KEY (`RecordID`),
  KEY `GoodsID` (`GoodsID`),
  KEY `OperatorID` (`OperatorID`),
  CONSTRAINT `t_storage_record_ibfk_1` FOREIGN KEY (`GoodsID`) REFERENCES `t_goods` (`GoodsID`),
  CONSTRAINT `t_storage_record_ibfk_2` FOREIGN KEY (`OperatorID`) REFERENCES `t_userinfo` (`SalesmanID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入库单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_storage_record`
--

LOCK TABLES `t_storage_record` WRITE;
/*!40000 ALTER TABLE `t_storage_record` DISABLE KEYS */;
INSERT INTO `t_storage_record` VALUES (8,'RK20250610851237',2,'运动裤',10,56.00,560.00,'2025-06-10 14:59:56',24,'沃寅博'),(11,'RK20250611081607',2,'运动裤',10,56.00,560.00,'2025-06-11 08:16:12',24,'沃寅博');
/*!40000 ALTER TABLE `t_storage_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_userinfo`
--

DROP TABLE IF EXISTS `t_userinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_userinfo` (
  `SalesmanID` int NOT NULL AUTO_INCREMENT COMMENT '员工编号，主键，自动增长',
  `SalesmanName` varchar(50) NOT NULL COMMENT '姓名',
  `Mobile` varchar(50) NOT NULL COMMENT '手机号码（登录号）',
  `Pwd` varchar(20) NOT NULL DEFAULT '123456' COMMENT '密码',
  `Gender` char(2) NOT NULL COMMENT '性别',
  `BaseSalary` float NOT NULL COMMENT '基本工资',
  `CommissionRate` decimal(6,2) NOT NULL COMMENT '提成比率（0~1之间，保留两位小数，有效位6位）',
  `Role` varchar(20) NOT NULL COMMENT '员工角色（店长、导购员、收银员）',
  PRIMARY KEY (`SalesmanID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_userinfo`
--

LOCK TABLES `t_userinfo` WRITE;
/*!40000 ALTER TABLE `t_userinfo` DISABLE KEYS */;
INSERT INTO `t_userinfo` VALUES (24,'沃寅博','17851828328','123654','女',3456,0.40,'店长'),(25,'李四中','13200006543','123456','女',2256,0.30,'导购员'),(28,'王二小','13289076543','123456','女',3422,0.40,'导购员'),(30,'张一','15925101900','123456','男',3000,0.10,'收银员'),(31,'里四','15925101800','123456','男',2300,0.20,'收银员');
/*!40000 ALTER TABLE `t_userinfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-11  8:47:46
