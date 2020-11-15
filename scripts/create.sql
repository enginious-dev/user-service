-- generated at 24-10-2020 09:32:13.943
drop database if exists user_service;
create database if not exists user_service;
create table application (id bigint not null auto_increment, addedAt TIMESTAMP not null, name varchar(255) not null, role varchar(255) not null, secret varchar(255) not null, uuid varchar(255) not null, organization bigint not null, primary key (id)) engine=InnoDB;
create table organization (id bigint not null auto_increment, addedAt TIMESTAMP not null, name varchar(255) not null, primary key (id)) engine=InnoDB;
create table role (id bigint not null auto_increment, addedAt TIMESTAMP not null, name varchar(255) not null, application bigint not null, primary key (id)) engine=InnoDB;
create table user (id bigint not null auto_increment, addedAt TIMESTAMP not null, enabled bit not null, password varchar(255) not null, username varchar(255) not null, application bigint not null, primary key (id)) engine=InnoDB;
create table user_role (user bigint not null, role bigint not null, primary key (user, role)) engine=InnoDB;
alter table application add constraint unq_application_organizzation_name unique (organization, name);
alter table application add constraint unq_application_uuid unique (uuid);
alter table organization add constraint unq_organizzation_name unique (name);
alter table role add constraint unq_role_application_name unique (application, name);
alter table user add constraint unq_user_application_username unique (application, username);
alter table application add constraint fk_application_organizzation foreign key (organization) references organization (id);
alter table role add constraint fk_role_application foreign key (application) references application (id);
alter table user add constraint fk_role_application foreign key (application) references application (id);
alter table user_role add constraint fk_user_role_role foreign key (role) references role (id);
alter table user_role add constraint fk_user_role_user foreign key (user) references user (id);
