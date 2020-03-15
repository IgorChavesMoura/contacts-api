SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

INSERT INTO `contact` (`id`, `email`, `name`, `phone`, `whatsapp`, `person_id`) VALUES
(5,	'zefulano@email.com',	'Ze Fulano',	'+5511912345678',	'+5511912345678',	1),
(6,	'claudiobeltrano@email.com',	'Claudio Beltrano',	'+5511987654321',	'+5511987654321',	1),
(8,	'igorchaves@email.com',	'Igor Chaves',	'+5511996455615',	'+5511996455615',	2),
(9,	'claudiobeltrano@email.com',	'Claudio Beltrano',	'+5511987654321',	'+5511987654321',	2),
(10,	'igorchaves@email.com',	'Igor Chaves',	'+5511996455615',	'+5511996455615',	3);

INSERT INTO `person` (`id`, `name`) VALUES
(1,	'Igor Chaves'),
(2,	'Ze Fulano'),
(3,	'Claudio Beltrano');
