CREATE DATABASE  IF NOT EXISTS `mylibrariesmanager` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `mylibrariesmanager`;
-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: localhost    Database: mylibrariesmanager
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) NOT NULL,
  `author` varchar(128) NOT NULL,
  `edition` varchar(128) NOT NULL,
  `id_genre` int(11) NOT NULL,
  `id_library` int(11) NOT NULL,
  `available` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_genre` (`id_genre`),
  KEY `id_library` (`id_library`),
  CONSTRAINT `book_ibfk_1` FOREIGN KEY (`id_genre`) REFERENCES `genre` (`id`),
  CONSTRAINT `book_ibfk_2` FOREIGN KEY (`id_library`) REFERENCES `library` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'The Philosopher\'s Stone','J.K.Rowling','Bloomsbury Publishing',1,1,1),(2,'The Chamber of Secrets','J.K.Rowling','Bloomsbury Publishing',1,1,1),(3,'The Prisoner of Azkaban','J.K.Rowling','Bloomsbury Publishing',1,2,1),(4,'The Goblet of Fire','J.K.Rowling','Bloomsbury Publishing',1,1,1),(5,'The Order of the Phoenix','J.K.Rowling','Bloomsbury Publishing',1,2,1),(6,'The Half-Blood Prince','J.K.Rowling','Bloomsbury Publishing',1,3,1),(7,'The Deathly Hallows','J.K.Rowling','Bloomsbury Publishing',1,1,1),(8,'1984','George Orwell','Secker & Warburg',7,1,1),(9,'Dune','Frank Herbert','Analog',7,3,1),(10,'Brave New World','Aldous Huxley','Chatto & Windus',7,1,1),(11,'Fahrenheit 451','Ray Bradbury','Ballentine Books',7,2,1),(12,'The Time Machine','H. G. Wells','Heinemann',2,1,1),(14,'Ender\'s Game','Orson Scott Card','Tor Books',2,1,1),(15,'The Time Machine','H. G. Wells','Heinemann',2,1,1),(16,'Do Androids Dream of Electric Sheep?','Philip K. Dick','Doubleday',2,3,1),(17,'Murder on the Orient Express','Agatha Christie','Collins Crime Club',6,2,1),(18,'Ten Little Niggers','Agatha Christie','Collins Crime Club',6,3,1),(19,'The Adventures of Sherlock Holmes','Arthur Conan Doyle','George Newnes',6,1,1),(20,'The Adventures of Sherlock Holmes','Arthur Conan Doyle','George Newnes',6,2,1),(21,'Pride and Prejudice','Jane Austen','T. Egerton',4,3,1),(22,'Romeo and Juliet','William Shakespeare','1597',4,2,1);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrowing`
--

DROP TABLE IF EXISTS `borrowing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrowing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_book` int(11) NOT NULL,
  `borrowing_date` date NOT NULL,
  `return_date` date NOT NULL,
  `expiration_date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_book` (`id_book`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `borrowing_ibfk_1` FOREIGN KEY (`id_book`) REFERENCES `book` (`id`),
  CONSTRAINT `borrowing_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrowing`
--

LOCK TABLES `borrowing` WRITE;
/*!40000 ALTER TABLE `borrowing` DISABLE KEYS */;
/*!40000 ALTER TABLE `borrowing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'fantasy'),(2,'science fiction'),(4,'romance'),(5,'thriller'),(6,'mystery'),(7,'dystopia');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `library` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library`
--

LOCK TABLES `library` WRITE;
/*!40000 ALTER TABLE `library` DISABLE KEYS */;
INSERT INTO `library` VALUES (1,'Library 1','Address 1 in Pisa'),(2,'Library 2','Address 2 in Pisa'),(3,'Library 3','Address 3 in Pisa');
/*!40000 ALTER TABLE `library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `surname` varchar(128) NOT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Giannoni','Marie','836 Avenue Yitzhak Rabin 83500 La Seyne sur Mer France','m.giannoni18@studenti.unipi.it','0033651180312');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-15 16:49:16
