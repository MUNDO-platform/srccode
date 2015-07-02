--RESOURCE
insert into resources (name,description,url) values ('wfs','wfs','/gui/user/wfs');
insert into resources (name,description,url) values ('wms','wms','/gui/user/wms');
insert into resources (name,description,url) values ('queue','queue','/gui/user/queue');
insert into resources (name,description,url) values ('db','db','/gui/user/db');
insert into resources (name,description,url) values ('api19115','api 19115 resource','/gui/user/api19115');

--WFS
insert into wfs (name,description,capabilitiesUrl,featuresUrl) values ('Warszawa','WFS Warszawa','http://wfs.um.warszawa.pl/serwis?service=WFS&request=GetCapabilities','http://wfs.um.warszawa.pl/serwis?version=1.0.0&service=WFS&request=GetFeature');
--WMS
insert into wms (name,description,url,srs,latitude,longitude) values ('warszawa','Warszawa','http://wms.um.warszawa.pl/serwis?service=WMS&request=GetCapabilities','EPSG:2178','52.240616','20.998012');
insert into wms (name,description,url,srs,latitude,longitude) values ('bazowa','Gdańsk - Bazowa','http://mapa.gdansk.gda.pl/arcgis/services/WMS_Mapa_bazowa/MapServer/WMSServer?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1','EPSG:102175','54.26','18.66');
insert into wms (name,description,url,srs,latitude,longitude) values ('orto','Gdańsk - Orto','http://mapa.gdansk.gda.pl/arcgis/services/WMS_Ortofotomapa2011/MapServer/WMSServer?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1','EPSG:102175','54.26','18.66');
insert into wms (name,description,url,srs,latitude,longitude) values ('poi','Gdańsk - POI','http://mapa.gdansk.gda.pl/arcgis/services/WMS_POI/MapServer/WMSServer?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1','EPSG:102175','54.26','18.66');
insert into wms (name,description,url,srs,latitude,longitude) values ('model','Gdańsk - Model','http://mapa.gdansk.gda.pl/arcgis/services/WMS_Model_wysokosciowy/MapServer/WMSServer?REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.1.1','EPSG:102175','54.26','18.66');
--QUEUE
insert into queue (name,description,url) values ('um_starynkiewicza','um_starynkiewicza','http://kolejki.um.warszawa.pl/um_starynkiewicza/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('usc_andersa','usc_andersa','http://kolejki.um.warszawa.pl/usc_andersa/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('usc_klopotowskiego','usc_klopotowskiego','http://kolejki.um.warszawa.pl/usc_klopotowskiego/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('usc_falecka','usc_falecka','http://kolejki.um.warszawa.pl/usc_falecka/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('ud_bialoleka','ud_bialoleka','http://kolejki.um.warszawa.pl/ud_bialoleka/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('ud_bielany','ud_bielany','http://kolejki.um.warszawa.pl/ud_bielany/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('ud_ochota','ud_ochota','http://kolejki.um.warszawa.pl/ud_ochota/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('ud_wilanow','ud_wilanow','http://kolejki.um.warszawa.pl/ud_wilanow/QWintouch/export2xml.qsp');
insert into queue (name,description,url) values ('ud_wola','ud_wola','http://kolejki.um.warszawa.pl/ud_wola/webakis_muw/status.xml');
insert into queue (name,description,url) values ('ud_zoliborz','ud_zoliborz','http://kolejki.um.warszawa.pl/ud_zoliborz/QWintouch/export2xml.qsp');

--API 19115
INSERT INTO api19115 VALUES (1, 'mock service - simulation of API', 'mock_19115', 'http://10.255.240.66:8080/mock/19115/');


--CONFIG
INSERT INTO config VALUES (5, '', 'wms.params.center', NULL, '52.240616,20.998012');
INSERT INTO config VALUES (6, '', 'wms.params.zoom', NULL, '10');
INSERT INTO config VALUES (7, '', 'wms.params.size', NULL, '800x500');
INSERT INTO config VALUES (8, '', 'wms.params.format', NULL, 'png');
INSERT INTO config VALUES (9, '', 'wfs.params.maxFeatures', NULL, '1000');
INSERT INTO config VALUES (10, '', 'db.params.pageSize', NULL, '100');
INSERT INTO config VALUES (11, 'Default timeout used for HTTP connections in milliseconds', 'http.default.connectTimeout', NULL, '30000');
INSERT INTO config VALUES (12, 'Default timeout for reading HTTP response in milliseconds', 'http.default.readTimeout', NULL, '60000');


--***************************************
--performance test configuration
--MOCK resources for tests
--***************************************

--QUEUE
insert into queue (name,description,url) values ('mock1','mock1','http://10.255.240.66:8080/mock/queue/export2xml.qsp');

--WMS
insert into wms (name,description,url,srs,latitude,longitude) values ('mock1','WMS mock1','http://10.255.240.66:8080/mock/wms/serwis?service=WMS&request=GetCapabilities','EPSG:2178','52.240616','20.998012');

--WFS 
--ns198735381:Punkty_adresowe (big)
insert into wfs (name,description,capabilitiesUrl,featuresUrl) values ('mock1','WFS mock1','http://10.255.240.66:8080/mock/wfs/serwis?service=WFS&request=GetCapabilities','http://10.255.240.66:8080/mock/wfs/serwisFeat?version=1.0.0&service=WFS&request=GetFeature');
--ns249829301:TEATRY (small)
insert into wfs (name,description,capabilitiesUrl,featuresUrl) values ('mock2','WFS mock1','http://10.255.240.66:8080/mock/wfs/serwis?service=WFS&request=GetCapabilities','http://10.255.240.66:8080/mock/wfs/serwisFeat1?version=1.0.0&service=WFS&request=GetFeature');


--DB
insert into db (name,description,jndiname) values ('mock1','tetst','java:jboss/datasources/mockds');
insert into db (name,description,jndiname) values ('mock2','','java:jboss/datasources/mockds');


