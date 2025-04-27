DROP TABLE if exists users CASCADE;
create table Roles 
(
	id serial primary key,
	role  varchar(20)
);

create table users 
(
	id serial primary key,
	name varchar(20),
	surname varchar(20),
	email varchar(20) UNIQUE,
	password varchar(20),
	role_id integer default 5,
	csa_id integer default 1,
	CONSTRAINT fk_role FOREIGN KEY(role_id) REFERENCES roles (id) ON DELETE SET DEFAULT ON UPDATE CASCADE,
	CONSTRAINT fk_csa FOREIGN KEY(csa_id) REFERENCES csa(id) ON DELETE SET DEFAULT ON UPDATE CASCADE
);

insert into users (name, surname, email, password, role_id, csa_id) values 
('Ivan','Ivanov','IvanovI@list.ru','$2a$10$JQr6wl/KI7cMkC.HJt7e5O7jiPrjZWGNVccD9A9pwuoZP.OVdNZom',1,9);