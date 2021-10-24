CREATE TABLE comerciante (
	id bigint PRIMARY KEY auto_increment,
	title varchar(200),
	description TEXT,
	price varchar(5),
	user_id int
);

CREATE TABLE user (
	id bigint PRIMARY KEY auto_increment,
	name varchar(200),
	email varchar(200),
	password varchar(200),
	githubuser varchar(200)
);

CREATE TABLE role (
	id int primary key auto_increment,
	name varchar(200)
);

CREATE TABLE user_roles(
	user_id int,
	roles_id int
);

INSERT INTO role (name) VALUES 
('ROLE_ADMIN'),
('ROLE_USER');

INSERT INTO user_roles VALUES(1, 1), (2, 2), (3,2);

INSERT INTO user (name, email, password, githubuser) VALUES
('Vinicius Rodrigues', 'vinnirx@gmail.com', '$2a$12$GCncvnswTrQXv94uYJw8Dem0RZQGR7iwyWIDPB3Is3TURYzGZiCHu', 'vinnirx'),
('Ronie Menezes', 'ronie@gmail.com', '$2a$12$GCncvnswTrQXv94uYJw8Dem0RZQGR7iwyWIDPB3Is3TURYzGZiCHu', 'menezesronie'),
('Gabriel Montovanni', 'gabriel@fiap.com.br', '$2a$12$GCncvnswTrQXv94uYJw8Dem0RZQGR7iwyWIDPB3Is3TURYzGZiCHu', 'GabrielMontovani');


INSERT INTO comerciante (title, description, price, user_id) VALUES 
	('Mercado Do Vinicius',
	'Batata, Tomate,CEBOLA',
	'10.0',
	1);
	
	
INSERT INTO comerciante (title, description, price, user_id) VALUES 
	('Mercado Do Ronie',
	'Arroz, Feijão,CEBOLA',  
	'10.0', 
	2);
	
INSERT INTO comerciante (title, description, price) VALUES 
	('Mercado Do Montovanni',
	'Arroz, Feijão,CEBOLA',  
	'10.0');