DROP TABLE if exists Users CASCADE;
create table Roles 
(
	id serial primary key,
	role  varchar(20)
);

create table Users 
(
	id serial primary key,
	name varchar(20),
	surname varchar(20),
	email varchar(20),
	password varchar(20),
	role_id integer default 1,
	CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES Roles (id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);

