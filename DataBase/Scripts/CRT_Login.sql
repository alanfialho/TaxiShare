CREATE TABLE `login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) DEFAULT NULL,
  `resposta` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  `idPessoa` bigint(20) DEFAULT NULL,
  `idPergunta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK462FF4936FFD13D` (`idPessoa`),
  KEY `FK462FF49719A838F` (`idPergunta`),
  CONSTRAINT `FK462FF4936FFD13D` FOREIGN KEY (`idPessoa`) REFERENCES `pessoa` (`id`),
  CONSTRAINT `FK462FF49719A838F` FOREIGN KEY (`idPergunta`) REFERENCES `pergunta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$





