delimiter $$

CREATE TABLE `rota` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_end_origem` bigint(20) DEFAULT NULL,
  `id_end_destino` bigint(20) DEFAULT NULL,
  `longitude` varchar(255) DEFAULT NULL,
  `latitute` varchar(255) DEFAULT NULL,
  `pass_existentes` tinyint(1) DEFAULT NULL,
  `pass_max` tinyint(1) DEFAULT NULL,
  `flag_aberta` bit(1) DEFAULT NULL,
  `flag_cancelada` bit(1) DEFAULT NULL,
  `data_rota` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_end_origem` (`id_end_origem`),
  KEY `id_end_destino` (`id_end_destino`),
  KEY `FK35816AAE0EB80E` (`id_end_origem`),
  KEY `FK35816ABC5FD55D` (`id_end_destino`),
  CONSTRAINT `FK35816ABC5FD55D` FOREIGN KEY (`id_end_destino`) REFERENCES `endereco` (`id`),
  CONSTRAINT `FK35816AAE0EB80E` FOREIGN KEY (`id_end_origem`) REFERENCES `endereco` (`id`),
  CONSTRAINT `rota_ibfk_1` FOREIGN KEY (`id_end_origem`) REFERENCES `endereco` (`id`),
  CONSTRAINT `rota_ibfk_2` FOREIGN KEY (`id_end_destino`) REFERENCES `endereco` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1$$

