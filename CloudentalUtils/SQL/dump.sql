select * from address
select * from patient
select * from dentist
select * from contactinfo
select * from tooth
select * from postit
select * from patienttooth
select * from patienthistory
select * from pricable
select * from activity
delete from dentist where id = 18
 drop table activity
select * from patienthistory h join activity a 
on a.patienthistid = h.patientid where patientid = 2

select count(co) from Dentist co 

select * from medicalhistory
select * from medicalhistoryentry


insert into dentist values ('Arilou', 'admin', 'Dimitris', 'Azarias');
insert into dentist values ('Patco', 'nimda', 'Patakas', 'Kostas');
insert into patient values (1, 'Arilou', 'gerry', 'mitropoulos' , 'i ll fix him right up!');
insert into patient values (2, 'Patco', 'spyros', 'malakasis' , 'getting new teeth!!');

insert into address values (1, 'SD st', 26, '123', 'Athens', 'Grece', 1)
insert into contactinfo values (1, '210-111222', 1)
insert into contactinfo values (1, '11111', 3)
insert into contactinfo values (1, 'a@b.c', 2)
insert into medicalhistory values (1, 'ge med history')
insert into medicalhistoryentry (id, alert, comments) values (1, 2,'entry1')
insert into medicalhistoryentry (id, alert, comments) values (1, 1,'entry2')


insert into address values (2, 'Pine st', 116, '123', 'Athens', 'Grece', 2)
insert into contactinfo values (2, '210-223333', 1)
insert into medicalhistory values (2, 'geClone med history')
insert into medicalhistoryentry (id, alert, comments) values (2, 2,'entry1')


insert into dentist values (2, 'patco');

insert into patient values (3, 2, 'sp', 'ma' , 'also a nice lad!');
insert into patient values (4, 2, 'spClone', 'ma', 'also a nice lad!');

delete from medicalhistory
insert into medicalhistory values (3, 'sp med history')
insert into medicalhistory values (4, 'spClone med history')

--address types 1 home adr, 2 billing afr, 3 secondary adrs
insert into address values (3, 'Sisini st', 6, '123', 'Athens', 'Grece', 2)
insert into address values (4, 'Agrbelis st', 116, '123', 'Athens', 'Grece', 1)

--contact types 1 fax, 2 email , 3-5 cnums1-3  

insert into contactinfo values (3, 'q@w.e', 2)
insert into contactinfo values (4, '213443', 1)
insert into contactinfo values (3, '22222', 4)
insert into contactinfo values (4, '33333', 5)
--medtype alert 1 : note 2 : warn
insert into medicalhistoryentry (id, alert, comments) values (3, 1,'entry1')
insert into medicalhistoryentry (id, alert, comments) values (4, 1,'entry1')
insert into medicalhistoryentry (id, alert, comments) values (4, 1,'entry2')
insert into medicalhistoryentry (id, alert, comments) values (4, 2,'entry3')

insert into tooth values (1, 'frontL')
insert into tooth values (2, 'frontR')
insert into tooth values (3, 'koptisL')
insert into tooth values (4, 'koptisL')
insert into tooth values (5, 'wisdomLupper')
insert into tooth values (6, 'wisdomRupper')
insert into tooth values (7, 'wisdomLlower')
insert into tooth values (8, 'wisdomRlower')

insert into patienttooth values (1, 1, 'rotten!!!', null)
insert into patienttooth values (1, 2, 'hmmm....', null)
insert into patienttooth values (1, 3, 'good one!!!', null)
insert into patienttooth values (1, 4, 'good one!!!', null)
insert into patienttooth values (1, 5, 'rotten!!!', null)

insert into patienttooth values (2, 4, 'rotten!!!', null)
insert into patienttooth values (2, 2, 'hmmm....', null)
insert into patienttooth values (2, 1, 'good one!!!', null)
insert into patienttooth values (2, 3, 'good one!!!', null)

insert into postit (id, post, alert) values (1, 'asdadada asdaa a asefes sfse sefsefesfsefs4t egsdrg ', 2)
insert into postit (id, post, alert) values (1, 'df4t egr reyh45y yh h56uy65 u gjh g', 1)
insert into postit (id, post, alert) values (2, '324rtrewg45y34 yu56 u563u 45 iu6764i7 7i4i4 67i647 ', 2)

insert into patienthistory ( patientid, enddate, comments ) values (1, null, 'welcome ge'); 
insert into patienthistory (patientid, enddate, comments ) values (2, null, 'welcome geClone');
insert into patienthistory (patientid, enddate, comments ) values (3, null, 'welcome sp'); 
insert into patienthistory (patientid, enddate, comments ) values (4, null, 'welcome spClone');

insert into pricable values (1, 1, 'pricable1', 'asdada adasdasd', 10.99)
insert into pricable values (2, 1, 'pricable2', 'asdada adasdasd', 20.99)
insert into pricable values (3, 1, 'pricable3', 'asdada adasdasd', 30.99)
insert into pricable values (4, 1, 'pricable4', 'asdada adasdasd', 40.99)

insert into pricable values (5, 2, 'pricable1', 'asdada adasdasd', 110.00)
insert into pricable values (6, 2, 'pricable2', 'asdada adasdasd', 120.20)
insert into pricable values (7, 2, 'pricable3', 'asdada adasdasd', 130.90)
insert into pricable values (-1, null, 'noprice', 'used internally to represent a non-priced item', 0.0)

select * from activity
insert into activity (patienthistid, pricable, enddate, description, price) values (1, -1, null, '', 11.99)
insert into activity (patienthistid, pricable, enddate, description, price) values (1, 1, null, '', -1)
insert into activity (patienthistid, pricable, enddate, description, price) values (2, 2, null, '', -1)
insert into activity (patienthistid, pricable, enddate, description, price) values (2, 4, null, 'activity 2', -1)
insert into activity (patienthistid, pricable, enddate, description, price) values (3, 5, null, '', -1)
insert into activity (patienthistid, pricable, enddate, description, price) values (4, -1, null, '', 50.0)

select * from visit
insert into visit (activityid, visitdate, duration, comments) values (6, '2012-02-26 17:10:21.392', 60, 'took a whole damn hour!')
insert into visit (activityid, visitdate, duration, comments) values (6, '2012-02-27 17:10:21.392', 59, 'took another whole damn hour!')
insert into visit (activityid, visitdate, duration, comments) values (6, '2012-03-01 17:10:21.392', 30, 'and a final half hour!')

insert into visit (activityid, visitdate, duration, comments) values (2, '2012-04-26 17:10:21.392', 60, 'took a whole damn hour!')
insert into visit (activityid, visitdate, duration, comments) values (2, '2012-04-27 17:10:21.392', 59, 'took another whole damn hour!')
insert into visit (activityid, visitdate, duration, comments) values (3, '2012-03-01 17:10:21.392', 30, 'a half hour!')
select * from toothhistory

insert into toothhistory (visitid, toothid, comments) values (1, 1, 'did stuff!')
insert into toothhistory (visitid, toothid, comments) values (1, 3, 'did stuff!')
insert into toothhistory (visitid, toothid, comments) values (1, 4, 'did stuff!')

