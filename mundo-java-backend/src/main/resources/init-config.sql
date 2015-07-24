--RESOURCE
insert into resources (name,description,url) values ('wfs','wfs','/gui/user/wfs');
insert into resources (name,description,url) values ('wms','wms','/gui/user/wms');
insert into resources (name,description,url) values ('queue','queue','/gui/user/queue');
insert into resources (name,description,url) values ('db','db','/gui/user/db');
insert into resources (name,description,url) values ('api19115','api 19115 resource','/gui/user/api19115');

--CONFIG
insert into config (key,value,description) values ('wms.params.center','52.240616,20.998012','');
insert into config (key,value,description) values ('wms.params.zoom','10','');
insert into config (key,value,description) values ('wms.params.size','800x500','');
insert into config (key,value,description) values ('wms.params.format','png','');
insert into config (key,value,description) values ('wfs.params.maxFeatures','1000','');
insert into config (key,value,description) values ('db.params.pageSize','100','');
insert into config (key,value,description) values ('http.default.connectTimeout','30000','Default timeout used for HTTP connections in milliseconds');
insert into config (key,value,description) values ('http.default.readTimeout','60000','Default timeout for reading HTTP response in milliseconds');
insert into config (key,value,description) values ('account.psw.limit','999','Limit of psw logins. Used for new account and psw reset.');

--ACCOUNT
INSERT INTO account (email,firstname,lastname,password,passwordlimit,type,username) VALUES ('admin','admin','admin','123456',999,'ROLE_SUPERADMIN','admin');

