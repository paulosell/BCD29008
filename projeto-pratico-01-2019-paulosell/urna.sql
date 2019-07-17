-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: 172.18.0.3    Database: Urna
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Apuracao`
--

DROP TABLE IF EXISTS `Apuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Apuracao` (
  `idApuracao` int(11) NOT NULL AUTO_INCREMENT,
  `idResposta` int(11) NOT NULL,
  `idQuestao` int(11) NOT NULL,
  `idEleicao` int(11) NOT NULL,
  `qtdVotos` int(11) NOT NULL,
  PRIMARY KEY (`idApuracao`,`idResposta`,`idQuestao`,`idEleicao`),
  KEY `fk_Resultado_Resposta1_idx` (`idResposta`,`idQuestao`,`idEleicao`),
  CONSTRAINT `fk_Resultado_Resposta1` FOREIGN KEY (`idResposta`, `idQuestao`, `idEleicao`) REFERENCES `Resposta` (`idResposta`, `idQuestao`, `idEleicao`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Apuracao`
--

LOCK TABLES `Apuracao` WRITE;
/*!40000 ALTER TABLE `Apuracao` DISABLE KEYS */;
/*!40000 ALTER TABLE `Apuracao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Eleicao`
--

DROP TABLE IF EXISTS `Eleicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Eleicao` (
  `idEleicao` int(11) NOT NULL AUTO_INCREMENT,
  `TituloEleicao` varchar(45) NOT NULL,
  `InicioEleicao` date NOT NULL,
  `FimEleicao` date DEFAULT NULL,
  `EstadoEleicao` tinyint(1) NOT NULL,
  `ApuradaEleicao` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idEleicao`,`TituloEleicao`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Eleicao`
--

LOCK TABLES `Eleicao` WRITE;
/*!40000 ALTER TABLE `Eleicao` DISABLE KEYS */;
INSERT INTO `Eleicao` VALUES (35,'Eleição para reitor','2019-06-08',NULL,1,NULL);
/*!40000 ALTER TABLE `Eleicao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Eleitores`
--

DROP TABLE IF EXISTS `Eleitores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Eleitores` (
  `loginPessoa` varchar(45) NOT NULL,
  `idEleicao` int(11) NOT NULL,
  `Votou` tinyint(1) NOT NULL,
  PRIMARY KEY (`loginPessoa`,`idEleicao`),
  KEY `fk_Pessoa_has_Eleição_Eleição1_idx` (`idEleicao`),
  KEY `fk_Pessoa_has_Eleição_Pessoa1_idx` (`loginPessoa`),
  CONSTRAINT `fk_Pessoa_has_Eleição_Eleição1` FOREIGN KEY (`idEleicao`) REFERENCES `Eleicao` (`idEleicao`),
  CONSTRAINT `fk_Pessoa_has_Eleição_Pessoa1` FOREIGN KEY (`loginPessoa`) REFERENCES `Pessoa` (`loginPessoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Eleitores`
--

LOCK TABLES `Eleitores` WRITE;
/*!40000 ALTER TABLE `Eleitores` DISABLE KEYS */;
INSERT INTO `Eleitores` VALUES ('ana',35,0),('henrique',35,0),('joao',35,0),('lucas',35,0),('maria',35,0),('paulo',35,0),('pedro',35,0);
/*!40000 ALTER TABLE `Eleitores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pessoa`
--

DROP TABLE IF EXISTS `Pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Pessoa` (
  `NomePessoa` varchar(45) NOT NULL,
  `loginPessoa` varchar(45) NOT NULL,
  `senhaPessoa` varchar(45) NOT NULL,
  PRIMARY KEY (`loginPessoa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pessoa`
--

LOCK TABLES `Pessoa` WRITE;
/*!40000 ALTER TABLE `Pessoa` DISABLE KEYS */;
INSERT INTO `Pessoa` VALUES ('Aline Silva','aline','senhaaline'),('Ana Silva','ana','senhaana'),('Carlos Silva','carlos','senhacarlos'),('Daniel Silva','daniel','senhadaniel'),('Felipe Silva','felipe','senhafelipe'),('Guilherme Silva','guilherme','senhaguilherme'),('Henrique Silva','henrique','senhahenrique'),('Joao Silva','joao','senhajoao'),('Lucas Silva','lucas','senhalucas'),('Marcos Silva','marcos','senhamarcos'),('Maria Silva','maria','senhamaria'),('Marina Silva','marina','senhamarina'),('Paulo Silva','paulo','senhapaulo'),('Pedro Silva','pedro','senhapedro'),('Roberto Silva','roberto','senharoberto');
/*!40000 ALTER TABLE `Pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Questao`
--

DROP TABLE IF EXISTS `Questao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Questao` (
  `idQuestao` int(11) NOT NULL AUTO_INCREMENT,
  `idEleicao` int(11) NOT NULL,
  `respostasMinimasQuestao` int(11) NOT NULL,
  `respostasMaximasQuestao` int(11) NOT NULL,
  `TituloQuestao` varchar(90) NOT NULL,
  PRIMARY KEY (`idQuestao`,`idEleicao`),
  KEY `fk_Questão_Eleição1_idx` (`idEleicao`),
  CONSTRAINT `fk_Questão_Eleição1` FOREIGN KEY (`idEleicao`) REFERENCES `Eleicao` (`idEleicao`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Questao`
--

LOCK TABLES `Questao` WRITE;
/*!40000 ALTER TABLE `Questao` DISABLE KEYS */;
INSERT INTO `Questao` VALUES (31,35,1,1,'Quem deve ser o próximo reitor?');
/*!40000 ALTER TABLE `Questao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resposta`
--

DROP TABLE IF EXISTS `Resposta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Resposta` (
  `idResposta` int(11) NOT NULL AUTO_INCREMENT,
  `idQuestao` int(11) NOT NULL,
  `idEleicao` int(11) NOT NULL,
  `TItuloResposta` varchar(90) NOT NULL,
  PRIMARY KEY (`idResposta`,`idQuestao`,`idEleicao`),
  KEY `fk_Resposta_Questão1_idx` (`idQuestao`,`idEleicao`),
  CONSTRAINT `fk_Resposta_Questão1` FOREIGN KEY (`idQuestao`, `idEleicao`) REFERENCES `Questao` (`idQuestao`, `idEleicao`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resposta`
--

LOCK TABLES `Resposta` WRITE;
/*!40000 ALTER TABLE `Resposta` DISABLE KEYS */;
INSERT INTO `Resposta` VALUES (126,31,35,'Nulo'),(127,31,35,'Branco'),(128,31,35,'Reitor A'),(129,31,35,'Reitor B'),(130,31,35,'Reitor C');
/*!40000 ALTER TABLE `Resposta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Votos`
--

DROP TABLE IF EXISTS `Votos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Votos` (
  `idVotos` int(11) NOT NULL AUTO_INCREMENT,
  `idResposta` int(11) NOT NULL,
  `idQuestao` int(11) NOT NULL,
  `idEleicao` int(11) NOT NULL,
  PRIMARY KEY (`idVotos`,`idResposta`,`idQuestao`,`idEleicao`),
  KEY `fk_Votos_Resposta1_idx` (`idResposta`,`idQuestao`,`idEleicao`),
  CONSTRAINT `fk_Votos_Resposta1` FOREIGN KEY (`idResposta`, `idQuestao`, `idEleicao`) REFERENCES `Resposta` (`idResposta`, `idQuestao`, `idEleicao`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Votos`
--

LOCK TABLES `Votos` WRITE;
/*!40000 ALTER TABLE `Votos` DISABLE KEYS */;
/*!40000 ALTER TABLE `Votos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-08 20:05:14
