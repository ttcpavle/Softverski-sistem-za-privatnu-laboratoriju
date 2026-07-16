/*
SQLyog Community v13.3.1 (64 bit)
MySQL - 10.4.28-MariaDB : Database - laboratorija
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`laboratorija` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `laboratorija`;

/*Table structure for table `kupac` */

DROP TABLE IF EXISTS `kupac`;

CREATE TABLE `kupac` (
  `idKupac` int(11) NOT NULL AUTO_INCREMENT,
  `ime` varchar(255) NOT NULL,
  `prezime` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `telefon` varchar(15) NOT NULL,
  `datumRodjenja` date NOT NULL,
  `idMesto` int(11) NOT NULL,
  PRIMARY KEY (`idKupac`),
  KEY `idMesto` (`idMesto`),
  CONSTRAINT `kupac_ibfk_1` FOREIGN KEY (`idMesto`) REFERENCES `mesto` (`idMesto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `mesto` */

DROP TABLE IF EXISTS `mesto`;

CREATE TABLE `mesto` (
  `idMesto` int(11) NOT NULL,
  `zipKod` int(11) NOT NULL,
  `naziv` varchar(255) NOT NULL,
  PRIMARY KEY (`idMesto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `ovlascenje` */

DROP TABLE IF EXISTS `ovlascenje`;

CREATE TABLE `ovlascenje` (
  `idRadnik` int(11) NOT NULL,
  `idTipUsluge` int(11) NOT NULL,
  `datumOd` date NOT NULL,
  `datumDo` date NOT NULL CHECK (`datumDo` > `datumOd`),
  PRIMARY KEY (`idRadnik`,`idTipUsluge`),
  KEY `idTipUsluge` (`idTipUsluge`),
  CONSTRAINT `ovlascenje_ibfk_3` FOREIGN KEY (`idRadnik`) REFERENCES `radnik` (`idRadnik`),
  CONSTRAINT `ovlascenje_ibfk_4` FOREIGN KEY (`idTipUsluge`) REFERENCES `tipusluge` (`idTipUsluge`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `proizvod` */

DROP TABLE IF EXISTS `proizvod`;

CREATE TABLE `proizvod` (
  `idProizvod` int(11) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(255) NOT NULL,
  `vremeCekanjaSati` int(11) NOT NULL,
  `cena` double NOT NULL CHECK (`cena` > 0),
  `opis` text DEFAULT NULL,
  PRIMARY KEY (`idProizvod`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `radnik` */

DROP TABLE IF EXISTS `radnik`;

CREATE TABLE `radnik` (
  `idRadnik` int(11) NOT NULL AUTO_INCREMENT,
  `JMBG` varchar(13) NOT NULL,
  `ime` varchar(255) NOT NULL,
  `prezime` varchar(255) NOT NULL,
  `korisnickoIme` varchar(255) NOT NULL,
  `lozinka` varchar(255) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  PRIMARY KEY (`idRadnik`),
  UNIQUE KEY `korisnickoIme` (`korisnickoIme`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `stavkazahteva` */

DROP TABLE IF EXISTS `stavkazahteva`;

CREATE TABLE `stavkazahteva` (
  `idZahtev` int(11) NOT NULL AUTO_INCREMENT,
  `rbStavka` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL CHECK (`kolicina` >= 1),
  `jedinicnaCena` double NOT NULL CHECK (`jedinicnaCena` >= 0),
  `ukupnaCena` double NOT NULL CHECK (`ukupnaCena` >= 0),
  `idProizvod` int(11) NOT NULL,
  PRIMARY KEY (`idZahtev`,`rbStavka`),
  KEY `idProizvod` (`idProizvod`),
  CONSTRAINT `stavkazahteva_ibfk_3` FOREIGN KEY (`idProizvod`) REFERENCES `proizvod` (`idProizvod`),
  CONSTRAINT `stavkazahteva_ibfk_4` FOREIGN KEY (`idZahtev`) REFERENCES `zahtevzaanalizu` (`idZahtev`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `tipusluge` */

DROP TABLE IF EXISTS `tipusluge`;

CREATE TABLE `tipusluge` (
  `idTipUsluge` int(11) NOT NULL AUTO_INCREMENT,
  `nazivUsluge` varchar(255) NOT NULL,
  `opisUsluge` varchar(600) NOT NULL,
  PRIMARY KEY (`idTipUsluge`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Table structure for table `zahtevzaanalizu` */

DROP TABLE IF EXISTS `zahtevzaanalizu`;

CREATE TABLE `zahtevzaanalizu` (
  `idZahtev` int(11) NOT NULL AUTO_INCREMENT,
  `datum` date NOT NULL,
  `status` varchar(255) NOT NULL,
  `prioritet` tinyint(1) NOT NULL DEFAULT 0,
  `ukupnaCenaZahteva` double NOT NULL,
  `idRadnik` int(11) NOT NULL,
  `idKupac` int(11) NOT NULL,
  PRIMARY KEY (`idZahtev`),
  KEY `idRadnik` (`idRadnik`),
  KEY `idKupac` (`idKupac`),
  CONSTRAINT `zahtevzaanalizu_ibfk_3` FOREIGN KEY (`idRadnik`) REFERENCES `radnik` (`idRadnik`),
  CONSTRAINT `zahtevzaanalizu_ibfk_4` FOREIGN KEY (`idKupac`) REFERENCES `kupac` (`idKupac`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
