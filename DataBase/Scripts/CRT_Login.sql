
CREATE TABLE `login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) DEFAULT NULL,
  `resposta` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  `idPessoa` bigint(20) DEFAULT NULL,
  `idPergunta` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK462FF497AA5F289` (`idPessoa`),
  KEY `FK462FF49719A838F` (`idPergunta`),
  CONSTRAINT `FK462FF49719A838F` FOREIGN KEY (`idPergunta`) REFERENCES `pergunta` (`id`),
  CONSTRAINT `FK462FF497AA5F289` FOREIGN KEY (`idPessoa`) REFERENCES `pessoa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1$$

