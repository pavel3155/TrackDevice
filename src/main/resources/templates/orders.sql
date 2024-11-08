DROP TABLE if exists Users CASCADE;
create table Сomplex 
(
	id serial primary key,
	num varchar(10),
	address varchar(20)
);
create table Device 
(
	id serial primary key,
	type varchar(50),
	model varchar(200),
	inv_num varchar(50),
	ser_num varchar(50)
);
create table Orders 
(
	id serial primary key,
	date varchar(20),
	num varchar(20),
	compl_id integer default 1,
	dev_id integer default 1,
	description varchar(200),
	CONSTRAINT fk_compl FOREIGN KEY(compl_id) REFERENCES Complex(id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_dev FOREIGN KEY(dev_id) REFERENCES Device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);

