DROP TABLE if exists Сompl CASCADE;
create table csa 
(
	id serial primary key,
	num varchar(10),
	address varchar(200)
);

create table orders 
(
	id serial primary key,
	date varchar(20),
	num varchar(20),
	compl_id integer default 1,
	dev_id integer default 1,
	description varchar(200),
	CONSTRAINT fk_compl FOREIGN KEY(compl_id) REFERENCES compl(id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_dev FOREIGN KEY(dev_id) REFERENCES Device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);

alter table orders add constraint fk_dev FOREIGN KEY(dev_id) REFERENCES device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE;

