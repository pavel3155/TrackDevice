DROP TABLE if exists Сompl CASCADE;
create table type_device
(
	id serial primary key,
	type varchar(20),
);
create table model_device 
(
	id serial primary key,
	type_id integer default 1,
	name varchar(200),
	CONSTRAINT fk_type_model FOREIGN KEY(type_id) REFERENCES type_device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);
create table dev 
(
	id serial primary key,
	model_id integer default 1,
	serNum varchar(50) UNIQUE,
	invNum varchar(50) UNIQUE,
	status varchar(50),
	CONSTRAINT fk_dev_model FOREIGN KEY(model_id) REFERENCES model_device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);
DROP TABLE if exists dev CASCADE;
DROP TABLE if exists dev1 CASCADE;

insert into dev (model_id,serNum,invNum,status) values 
(2,'CZC112014501','1002011201001','исправный'),
(2,'CZC112014502','1002011201002','исправный'),
(2,'CZC112014503','1002011201003','исправный'),
(2,'CZC112014504','1002011201004','исправный'),
(3,'AAQ790222001','1003079001001','исправный'),
(3,'AAQ790222002','1003079001002','исправный'),
(3,'AAQ790222003','1003079001003','исправный'),
(4,'AAQ762222001','1004076202001','исправный'),
(4,'AAQ762222002','1004076202002','исправный'),
(4,'AAQ762222002','1004076202002','исправный'),
(5,'DL150T989001','1005015002001','исправный'),
(5,'DL150T989002','1005015002002','исправный'),
(6,'DL760R989001','1006076002001','исправный'),
(6,'DL760R989002','1006076002002','исправный'),
(7,'HP380DL45001','1007038007001','исправный'),
(7,'HP380DL45002','1007038007002','исправный'),
(8,'AC222QH23101','1008022201301','исправный'),
(8,'AC222QH23102','1008022201302','исправный'),
(10,'MPAD214R141021','1010021401301','исправный'),
(10,'MPAD214R141022','1010021401302','исправный'),
(10,'MPAD214R141023','1010021401303','исправный'),
(13,'CZC113025501','1013011301001','исправный'),
(13,'CZC113025502','1013011301002','исправный'),
(13,'CZC113025503','1013011301003','исправный'),
(13,'CZC113025504','1013011301004','исправный');