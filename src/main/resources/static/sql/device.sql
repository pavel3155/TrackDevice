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
create table device 
(
	id serial primary key,
	model_id integer default 1,
	ser_num varchar(50),
	inv_num varchar(50),
	status varchar(50),
	CONSTRAINT fk_dev_model FOREIGN KEY(model_id) REFERENCES model_device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);

