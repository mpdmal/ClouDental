drop table if exists dentist CASCADE ;
CREATE TABLE dentist
(
	username text not null,
	password text not null,
	name text not null,
	surname text not null,
	constraint dentist_pk primary key (username)
);

drop table if exists patient CASCADE;
CREATE TABLE patient
(
	id serial,
	dentistID text references dentist not null,
	name text not null,
	surname text not null,
	comments text,
	created timestamp with time zone default now() not null,
	constraint patient_pk primary key (id)
);

drop table if exists pricelist CASCADE;
CREATE TABLE pricelist
(
	id serial,
	dentistid text references dentist not null,
	title text not null,
	description text,
	price decimal not null,
	constraint pricelist_pk primary key (id)
);

drop table if exists discount CASCADE;
CREATE TABLE discount
(
	id serial,
	dentistid text references dentist not null,
	title text not null,
	description text,
	discount decimal not null,
	constraint discount_pk primary key (id)
);

drop table if exists tooth CASCADE;
CREATE TABLE tooth
(
	position integer,
	name text,
	constraint tooth_pk primary key (position)
);

drop table if exists address CASCADE;
CREATE TABLE address
(
	id integer references patient,
	street text,
	number integer,
	postalcode text,
	city text,
	country text,
	adrstype int,
	constraint address_pk primary key (id, adrstype)
);

drop table if exists contactinfo CASCADE;
CREATE TABLE contactinfo
(
	id integer references patient,
	info text,
	infotype int,
	constraint contactinfo_pk primary key (id, infotype)
);
drop table if exists medicalhistory CASCADE;
create table medicalhistory
(
	id integer references patient,
	comments text,
	constraint medicalHistory_pk primary key (id)
);

drop table if exists medicalhistoryentry CASCADE;
create table medicalhistoryentry
(
	id integer references medicalhistory,
	added timestamp with time zone default now(),
	alert integer,
	comments text,
	constraint medicalHistory_entry_pk primary key (id, added)
);

drop table if exists postit CASCADE;
create table postit
(
	id text references dentist,
	postdate timestamp with time zone default now(),
	post text,
	alert integer,
	constraint postit_pk primary key (id, postdate)
);

drop table if exists patienttooth  CASCADE;
create table patienttooth
(
	patientID integer references patient,
	toothID integer references tooth,
	comments text,
	image oid,
	constraint patienttooth_pk primary key (toothID, patientID)
);

drop table if exists patienthistory CASCADE;
create table patienthistory
(
	patientid integer references patient,
	startdate timestamp with time zone default now(),
	enddate timestamp with time zone default now(),
	comments text,
	constraint patienthistory_pk primary key (patientID)
);


drop table if exists activity CASCADE;
create table activity
(
	id serial, 
	patienthistid integer references patienthistory not null,
	priceable integer references pricelist not null,
	discount integer references discount not null,
	startdate timestamp with time zone default now() not null,
	enddate timestamp with time zone default now(),
	isopen boolean,
	description text,
	price decimal,
	constraint activity_pk primary key (id)
);

drop table if exists visit CASCADE;
create table visit
(
	id serial,
	activityID integer references activity,
	visitdate timestamp with time zone default now(),
	enddate timestamp with time zone default now(),
	comments text,
	deposit decimal,
	constraint visit_pk primary key (id)
);


drop table if exists toothhistory CASCADE;
create table toothhistory
(
	id serial,
	visitid integer references visit,
	toothid integer references tooth,
	comments text,
	constraint toothhistory_pk primary key (id)
);


--DEFAULTS
insert into pricelist values (-1, null, 'noprice', 'used internally to represent a non-priced item', 0.0);
insert into dentist (username, password, name, surname) values ('demo', 'demo', 'demo', 'demo');
