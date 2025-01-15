DROP TABLE if exists restore CASCADE;
create table restore
(
	id serial primary key,
	method varchar(255)
);
insert into restore (method) values
('---'),
('замена ТС из ОФ'),
('ремонт ТС на КСА'),
('консультация');
