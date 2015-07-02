drop table account if exists;
drop table wfs if exists;
drop table wms if exists;
drop table queue if exists;
drop table resources if exists;
drop table db if exists;
drop table dbTable if exists;
drop table dbColumn if exists;
drop table config if exists;
drop table statistics if exists;

create table account (
    id integer identity primary key, 
    type varchar(255) not null, 
    username  varchar(255) not null, 
    password varchar(255) not null,
    firstName varchar(255),
    lastName varchar(255),
    email varchar(255),
    active boolean,
    passwordLimit integer
);

create table wfs (
    id integer identity primary key, 
    name varchar(50) not null, 
    description  varchar(255), 
    capabilitiesUrl varchar(255) not null,
    featuresUrl varchar(255) not null
);

create table wms (
    id integer identity primary key, 
    name varchar(255) not null, 
    description  varchar(255), 
    url varchar(255) not null,
    srs varchar(255) not null,
    latitude varchar(64),
    longitude varchar(64)
);

create table queue (
    id integer identity primary key, 
    name varchar(255) not null, 
    description  varchar(255), 
    url varchar(255)
);

create table resources (
    id integer identity primary key, 
    name varchar(255) not null, 
    title  varchar(255), 
    description  varchar(255), 
    url varchar(255),
    last_modified timestamp,
    url_json varchar(255),
    url_xml varchar(255)
);

create table db (
    id integer identity primary key, 
    name varchar(50) not null, 
    description  varchar(50), 
    jndiname varchar(255) not null
);

create table dbTable (
    id integer identity primary key, 
    db_id integer not null,
    cacheVariant int,
    name varchar(255) not null, 
    type  varchar(255) not null, 
    params varchar(255)
);

create table dbColumn (
    id integer identity primary key, 
    tab_id integer not null,
    name varchar(255) not null
);

create table config (
    id integer identity primary key, 
    key varchar(50) not null, 
    value  varchar(255) not null, 
    description varchar(255),
    last_modified timestamp
);

create table statistics (
    id integer identity primary key, 
    path varchar(255) not null, 
    counter integer not null
);



--CONFIG
insert into config (key,value,description) values ('wms.params.center','52.240616,20.998012','');
insert into config (key,value,description) values ('wms.params.zoom','10','');
insert into config (key,value,description) values ('wms.params.size','800x500','');
insert into config (key,value,description) values ('wms.params.format','png','');
insert into config (key,value,description) values ('wfs.params.maxFeatures','1000','');
insert into config (key,value,description) values ('db.params.pageSize','100','');
insert into config (key,value,description) values ('http.default.connectTimeout','30000','');
insert into config (key,value,description) values ('http.default.readTimeout','60000','');
