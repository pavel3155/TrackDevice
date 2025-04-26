DROP TABLE if exists  CASCADE;
create table act_types
(
	id serial primary key,
	type varchar(250)
);

insert into act_types (type) values
('---'),
('приема-передачи технического средства -Р(ТС)'),
('приема-передачи технического средства -П(ТС)'),
('приема-передачи ремкомплекта -Р(РК)');

DROP TABLE if exists act_dev CASCADE;
create table act_dev 
(
	id serial primary key,
	num varchar(20),
    date date,
    type_id integer default 1,
	csa_from_id integer default 1,
	csa_to_id integer default 1,
	dev_id integer default 92,
	order_id integer,
	note varchar(200),
	CONSTRAINT fk_type FOREIGN KEY(type_id) REFERENCES act_types(id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_csa_from FOREIGN KEY(csa_from_id) REFERENCES csa(id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_csa_to FOREIGN KEY(csa_to_id) REFERENCES csa(id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_dev FOREIGN KEY(dev_id) REFERENCES device(id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_order FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);


